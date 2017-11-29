<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>文件上传</title>
    <%--sogno·王--%>
    <link href="${pageContext.request.contextPath}/css/webuploader.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/webuploader.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/webuploader.nolog.js"></script>
    <style type="text/css">
        .upliaddiv{
            width: 40%;
            position: absolute;
            left: 50%;
            top: 20%;
            margin-left: -20%;
            background-color: #d1d1d1;
        }
        .fileshow{
            float: left;width: 80%;margin-left: 10px;
        }
        .fileshow label{
            margin: 0px;
        }
        .widths{
            width: 100%;
            float: left;
        }
        .bgcd{
            width: 200px;height:10px;float: left; margin-left: 10px;margin-top: 5px; background-color: white;
            border-radius: 5px;outline:none;
        }
        .bgc{
            width: 0%; height:10px; background-color: #7fca83;
            border-radius: 5px;outline:none;
        }
        .imgc{
            width: 60px;height:60px;
        }
        .cfileid{
            width: 100%;height:60px;margin-bottom: 10px;
        }
    </style>
</head>
<body>
<div id="uploader" class="upliaddiv">
    <div id="attach" class="btn btn-upload">选择文件</div>
    <button id="upload" class="btn btn-default">开始上传</button>
    <div id="thelist" class="upload-box clearfix">
    </div>
</div>

</body>
<script type="text/javascript">
    $(function () {
        var $list = $("#thelist");
        var $btn = $("#upload");
        //初始化插件
        var uploader = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: false,
            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick:{
                id:'#attach',
                //这个地方 name 没什么用，和fileVal 配合使用。
                name:"multiFile",
                //默认为true，true表示可以多选文件，HTML5的属性
                multiple:true
            },
            fileVal:'multiFile',
            //在这里必需要引入swf文件，
            swf: '<%=request.getContextPath()%>/webupload/Uploader.swf',
            // 文件接收服务端。
            server: "<%=request.getContextPath()%>/fileUpLoadController/webUploader.do",
            //分片处理
            chunked: true,
            //每片5M
            chunkSize:(5*1024*1024),
            //上传并发数
            threads:3,
            //如果失败，则不重试
            //chunkRetry:2,
            //上传的文件总数
            //fileNumLimit:1,
            method:'POST'
        });
        // 当有文件添加进来的时候
        uploader.on( 'fileQueued', function( file ) {  // webuploader事件.当选择文件后，文件被加载到文件队列中，触发该事件。等效于 uploader.onFileueued = function(file){...} ，类似js的事件定义。
            var allsize = "0MB";
            if (file.size > 1024 * 1024){
                allsize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
            }else{
                allsize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
            }

            var src = "<%=request.getContextPath()%>/images/fileType/file.png";0
            if(file.ext == 'jpg'){
                src = "<%=request.getContextPath()%>/images/fileType/JPG.jpg";
            }else if(file.ext == 'png'){
                src = "<%=request.getContextPath()%>/images/fileType/PNG.jpg";
            }else if(file.ext == 'ai'){
                src = "<%=request.getContextPath()%>/images/fileType/AI.jpg";
            }else if(file.ext == 'zip'){
                src = "<%=request.getContextPath()%>/images/fileType/ZIP.jpg";
            }else if(file.ext == 'rar'){
                src = "<%=request.getContextPath()%>/images/fileType/RAR.jpg";
            }else if(file.ext == 'psd'){
                src = "<%=request.getContextPath()%>/images/fileType/PSD.jpg";
            }else if(file.ext == 'pdf'){
                src = "<%=request.getContextPath()%>/images/fileType/PDF.jpg";
            }else if(file.ext == 'cdr'){
                src = "<%=request.getContextPath()%>/images/fileType/CDR.jpg";
            }else if(file.ext == 'tif'){
                src = "<%=request.getContextPath()%>/images/fileType/TIF.jpg";
            }else if(file.ext == 'docx'){
                src = "<%=request.getContextPath()%>/images/fileType/docx.png";
            }else if(file.ext == 'xlsx'){
                src = "<%=request.getContextPath()%>/images/fileType/xlsx.png";
            }

            var str = "<div id='"+  file.id + "' class=\"cfileid\"> " +
                    "<div style=\"float: left;\"> " +
                    "<img src='"+src+"' class=\"imgc\"/></div>"+
                    "<div class=\"fileshow\">"+
                    "<div class=\"widths\">"+
                    "<label>"+file.name+"</label>"+
                    "</div>"+
                    "<div class=\"widths\">"+
                    "<label id=\""+file.id+"b\" style=\"float: left;\">0%</label>"+
                    "<div class=\"bgcd\">"+
                    "<div id=\""+file.id+"g\" class=\"bgc\"></div>"+
                    "</div>"+
                    "</div>"+
                    "<div class=\"widths\">"+
                    "<label id=\""+file.id+"l\">0MB</label>"+
                    "<label>|</label>"+
                    "<label id=\""+file.id+"s\">"+allsize+"</label>"+
                    "<label id=\""+file.id+"sus\"></label>"+
                    "</div>"+
                    "</div>"+
                    "</div>";
            $list.append(str);
        });

//上传过程中触发，携带上传进度。文件上传过程中创建进度条实时显示。
        uploader.on( 'uploadProgress', function( file, percentage ) {
            var nowsize = file.size*percentage;
            var allsize = "0MB";
            if (file.size > 1024 * 1024){
                allsize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
            }else{
                allsize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
            }
            if(nowsize > 1024 * 1024){
                nowsize = (Math.round(nowsize * 100 / (1024 * 1024)) / 100).toString() + 'MB';
            }else{
                nowsize = (Math.round(nowsize * 100 / 1024) / 100).toString() + 'KB';
            }
            $("#"+file.id+"b").html(parseInt(percentage*100)+"%");
            $("#"+file.id+"g").css("width",parseInt(percentage*100)+"%");
            $("#"+file.id+"l").html(nowsize);
        });
        //当所有文件上传结束时触发
        uploader.on("uploadFinished",function(){
            console.log("uploadFinished:");
        });

//当文件上传成功时触发。
        uploader.on( "uploadSuccess", function( file ,response) {
            $("#"+file.id+"sus").html("上传完成!");
            $( "#"+file.id ).find("p.state").text("已上传");
        });

        uploader.on( "uploadError", function( file ) {
            $("#"+file.id+"sus").html("上传出错!");
            uploader.cancelFile(file);
            uploader.removeFile(file,true);
            uploader.reset();
        });

//如果是手动上传,用下面的事件,并启用jsp页面上的上传按钮
        $btn.on( 'click', function() {
            uploader.upload();
        });
    });
</script>
</html>
