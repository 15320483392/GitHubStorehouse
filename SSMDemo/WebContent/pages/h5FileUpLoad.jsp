<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>H5文件断点上传</title>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <style type="text/css">
        .maindiv{
            width:360px;
            min-height: 40px;
            position: absolute;
            left:50%;
            top:20%;
            margin-left: -180px;
            background-color: #d6edd8;
            padding: 10px 10px 5px 10px;
        }
        #fileToUpload{
            width: 340px;
            font-size: 12pt;
            padding: 5px;
            border: 1px solid black;
            border-radius: 10px;
            outline:none;
            margin: 0px 0px 10px 0px;
        }
        .filetext{
            font-size: 11pt;
            margin: 0px 0px 5px 0px;
        }
        input:hover{
            background-color:#0d9461;
        }
        body{
            font-family: "微软雅黑", YaHei, 黑体, Hei, Tahoma, Helvetica, arial, sans-serif;
        }
        .scrollbardiv{
            width: 100%;
            height:40px;
        }
        .scrollbar{
            width: 0px;
            height:20px;
            background-color: #0d9461;
            border-radius: 5px;
            outline:none;
        }
        .progressNumber{
            float: left;width: 37px;
            height:20px;
            line-height: 20px;
            margin:0px 5px 0px 0px;
        }
        .bgcdiv{
            height:20px; width: 80%; float: left;
            border-radius: 5px;
            outline:none;
            background-color: white;
        }
        #uploadTextDiv{
            min-height: 115px;
            /*max-height:115px;
            overflow-y: auto;*/
            background-color: #A6B7D0;
            margin: 0px 0px 5px 0px;
            display: none;
        }
        /* #fileUploadDiv{
             width:60px;height:20px;cursor:hand;background-color: #00acc1;
         }*/
    </style>
</head>

<body>
<div class="maindiv">
    <%-- <label for="fileToUpload" style="font-size: 13pt;">请选择需要上传的文件(支持多文件):</label>--%>
    <div id="fileUploadDiv" title="请选择需要上传的文件(支持多文件)"></div>
    <input type="file" name="fileToUpload" id="fileToUpload" onchange="fileSelected();" multiple="multiple"/>
    <div id="uploadTextDiv" >
        <%--<div class="filetext">
            名称:&nbsp;+file.name+file.name+fie.name+</br>
            大小:&nbsp;+fileSize+</br>
            类型:&nbsp;"+file.type
        </div>
        <div class="scrollbardiv">
            <label id="progressNumber" class="progressNumber">100%</label>
            <div class="bgcdiv">
                <div id="scrollBar" class="scrollbar" style="width: 30%;"></div>
            </div>
            <div style="height:20px; float: left;margin-left: 40px;">
                <label id="siz1" style="margin:0px;">30MB</label>
                <label id="siz2" style="margin:0px 0px 0px 10px;">|</label>
                <label id="siz3" style="margin:0px 0px 0px 10px;">120MB</label>
            </div>
        </div>--%>
    </div>
    <div id="btndiv" style="width: 100%;display: none;">
        <button class="btn btn-primary btn-paddin" onclick="uploadFiles()">上传</button>
        <button class="btn btn-primary btn-paddin" onclick="pauseUpload()">暂停</button>
        <button class="btn btn-primary btn-paddin" onclick="cancleView();">关闭</button>
    </div>
</div>
</body>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script>
    $(function () {
        /*var sbdw = $("#scrollBarDiv").width();
         $("#bgcdiv"). css("width",sbdw-40-10);*/

        $("#fileToUpload").hide();
        $("#fileUploadDiv").html("文件上传");
    });

    //给div添加file选择文件事件
    $("#fileUploadDiv").click(function () {
        $('input[id=fileToUpload]').click();
    });

    /*$("#fileUpload").click(function () {
     fileSelected();
     });*/
    var paragraph = 1024*1024*4;  //每次分片传输文件的大小 2M
    var blob = null;//  分片数据的载体Blob对象
    var fileList = null; //传输的文件
    var uploadState = 0;  // 0: 无上传/取消， 1： 上传中， 2： 暂停

    function uploadFiles(){
        //将上传状态设置成1
        uploadState = 1;
        if(fileList == null){
            alert("请选择上传文件！");
            return;
        }
        if(fileList.files.length>0){
            for(var i = 0; i< fileList.files.length; i++){
                var file = fileList.files[i];
                uploadFileInit(file,i);
            }
        }else{
            alert("请选择上传文件！");
        }
    }
    /**
     * 获取服务器文件大小，开始续传
     * @param file
     * @param i
     */
    function uploadFileInit(file,i){
        if(file){
            var startSize = 0;
            var endSize = 0;
            var date = file.lastModifiedDate;
            var lastModifyTime = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+"-"
                    +date.getHours()+"-"+date.getMinutes()+"-"+date.getSeconds();
            //获取当前文件已经上传大小
            jQuery.post("<%=request.getContextPath()%>/fileUpLoadController/getChunkedFileSize.do", {
                        "fileName":encodeURIComponent(file.name),
                        "fileSize":file.size,"lastModifyTime":lastModifyTime,
                        "chunkedFileSize":"chunkedFileSize"},
                    function(data){
                        if(data != -1){
                            endSize = Number(data);
                        }
                        uploadFile(file,startSize,endSize,i);

                    });
        }
    }
    /**
     * 分片上传文件
     */
    function uploadFile(file,startSize,endSize,i) {
        console.log("i:"+i);
        var date = file.lastModifiedDate;
        var lastModifyTime = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+"-"
                +date.getHours()+"-"+date.getMinutes()+"-"+date.getSeconds()
        var reader = new FileReader();
        reader.onload = function loaded(evt) {
            // 构造 xmlHttpRequest 对象，发送文件 Binary 数据
            var xhr = new XMLHttpRequest();
            xhr.sendAsBinary = function(text){
                var data = new ArrayBuffer(text.length);
                var ui8a = new Uint8Array(data, 0);
                for (var i = 0; i < text.length; i++) ui8a[i] = (text.charCodeAt(i) & 0xff);
                this.send(ui8a);
            }

            xhr.onreadystatechange = function(){
                if(xhr.readyState==4){
                    //表示服务器的相应代码是200；正确返回了数据
                    if(xhr.status==200){
                        //纯文本数据的接受方法
                        var message=xhr.responseText;
                        message = Number(message);
                        console.log("--i:"+i);
                        uploadProgress(file,startSize,message,i);
                    } else{
                        alert("上传出错，服务器相应错误");
                    }
                }
            };//创建回调方法
            xhr.open("POST",
                    "<%=request.getContextPath()%>/fileUpLoadController/appendUpload2Server.do?fileName=" + encodeURIComponent(file.name)+"&fileSize="+file.size+"&lastModifyTime="+lastModifyTime,
                    false);
            xhr.overrideMimeType("application/octet-stream;charset=utf-8");
            xhr.sendAsBinary(evt.target.result);
        };
        if(endSize < file.size){
            //处理文件发送（字节）
            if(file.size > 1024 * 1024 * 1024){
                paragraph = 1024 * 1024 * 20;
            }
            startSize = endSize;
            if(paragraph > (file.size - endSize)){
                endSize = file.size;
            }else{
                endSize += paragraph ;
            }
            if (file.webkitSlice) {
                //webkit浏览器
                blob = file.webkitSlice(startSize, endSize);
            }else
                blob = file.slice(startSize, endSize);
            reader.readAsBinaryString(blob);
        }else{
            $('#progressNumber').html("100%");
        }
    }

    //显示处理进程
    function uploadProgress(file,startSize,uploadLen,i) {
        //获取到百分比
        var percentComplete = Math.round(uploadLen * 100 / file.size);
        var sbdw = $("#scrollBarDiv"+i).width();
        $("#scrollBar"+i).css("width",percentComplete*((sbdw-40-10)/100));
        $('#progressNumber'+i).html(percentComplete.toString() + '%');

        var siz3 = "";
        var siz1 = "";
        if (file.size > 1024 * 1024){
            siz3 = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
        }else{
            siz3 = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
        }
        if(uploadLen > 1024 * 1024){
            siz1 = (Math.round(uploadLen * 100 / (1024 * 1024)) / 100).toString() + 'MB';
        }else{
            siz1 = (Math.round(uploadLen * 100 / 1024) / 100).toString() + 'KB';
        }
        $("#siz1"+i).html(siz1);
        $("#siz2"+i).html("|");
        $("#siz3"+i).html(siz3);
        //续传
        if(uploadState == 1){
            uploadFile(file,startSize,uploadLen,i);
        }
    }

    /*
     暂停上传
     */
    function pauseUpload(){
        uploadState = 2;
    }

    /**
     * 选择文件之后触发事件
     */
    function fileSelected() {
        fileList = document.getElementById('fileToUpload');
        var length = fileList.files.length;
        if(length != 0){
            $("#btndiv").show();
            $("#uploadTextDiv").show();
        }else{
            $("#btndiv").hide();
            $("#uploadTextDiv").hide();
        }

        $("#fileText").html("");
        var str = "";
        for(var i=0; i<length; i++){
            file = fileList.files[i];
            if(file){
                var fileSize = 0;
                if (file.size > 1024 * 1024 * 500){
                    $("#fileToUpload").val("");
                    alert("文件过大，不能超过500MB!");
                    return;
                }
                if (file.size > 1024 * 1024){
                    fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
                }else{
                    fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
                }
                str = str + "<div class=\"filetext\">名称:&nbsp;"+file.name+"</br>"+"大小:&nbsp;"+fileSize+"</br>"+"类型:&nbsp;"+file.type+"</br></div>";

                str = str + "<div id=\"scrollBarDiv"+i+"\" class=\"scrollbardiv\"><label id=\"progressNumber"+i+"\" class=\"progressNumber\">0%</label>";
                str = str + "<div class=\"bgcdiv\"><div id=\"scrollBar"+i+"\" class=\"scrollbar\"></div></div>";

                str = str + "<div style=\"height:20px; float: left;margin-left: 40px;\">" +
                        "<label id=\"siz1"+i+"\" style=\"margin:0px;\">0MB</label>" +
                        "<label id=\"siz2"+i+"\" style=\"margin:0px 0px 0px 10px;\">|</label>" +
                        "<label id=\"siz3"+i+"\" style=\"margin:0px 0px 0px 10px;\">"+fileSize+"</label></div>";
                str = str + "</div>";
            }
        }
        $("#uploadTextDiv").html(str);
    }
    function cancleView() {
        $("#btndiv").hide();
        $("#uploadTextDiv").hide();
    }
</script>
</html>
