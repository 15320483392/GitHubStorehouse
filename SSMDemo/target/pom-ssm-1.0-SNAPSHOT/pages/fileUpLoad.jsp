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

    </style>
</head>
<body>

<div class="upliaddiv" id="uploadimg">
    <div id="uploadBtn" class="btn btn-upload">上传文件</div>
    <button id="multiUpload" class="btn btn-default">开始上传</button>
    <div id="list" class="upload-box clearfix"></div>
</div>
</body>

<script type="text/javascript">
    //验证数组方法
    Array.prototype.contains = function (obj) {
        var i = this.length;
        while (i--) {
            if (this[i] === obj) {
                return true;
            }
        }
        return false;
    }

    var arr_md5 = [];//加载页面时，将已上传成功的分片数组化
    var chunks = 0;//分片总数量，用来上传成功以后校验分片文件总数
    var repart_chunks = 0;
    var upload_success = false;
    var times = 0;
    var interval = 0;

    //注册组件
    WebUploader.Uploader.register({
        'before-send': 'preupload'
    }, {
        preupload: function( block ) {
            var me = this,
                    owner = this.owner,
                    deferred = WebUploader.Deferred();
            chunks = block.chunks;
            owner.md5File(block.blob)
            //及时显示进度
                    .progress(function(percentage) {
                        console.log('Percentage:', percentage);
                    })
                    //如果读取出错了，则通过reject告诉webuploader文件上传出错。
                    .fail(function() {
                        deferred.reject();
                        console.log("==1==");
                    })
                    //md5值计算完成
                    .then(function( md5 ) {
                        if(arr_md5.contains(md5)){//数组中包含+(block.chunk+1)
                            deferred.reject();
                            console.log("分片已上传"+block.file.id);
                            $("#speed_"+block.file.id).text("正在断点续传中...");
                            if(block.end == block.total){
                                $("#speed_"+block.file.id).text("上传完毕");
                                $("#pro_"+block.file.id).css( 'width', '100%' );
                            }else{
                                $("#pro_"+block.file.id).css( 'width',(block.end/block.total) * 100 + '%' );
                            }
                        }else{
                            deferred.resolve();
                            console.log("开始上传分片："+md5);
                        }
                    });
            return deferred.promise();
        }
    });

    //初始化WebUploader
    var uploader = WebUploader.create({
        //swf文件路径
        //swf: 'http://www.ssss.com.cn/js/webuploader/Uploader.swf',
        //文件接收服务端。
        server: '<%=request.getContextPath()%>/fileUpLoadController/fileUpLoad.do?methodName=fileupload&tokenid='+$("#tokenid").val(),
        //选择文件的按钮，可选。内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#uploadBtn',
        //不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        resize: false,
        // 选完文件后，是否自动上传。
        auto:false,
        //是否分片
        chunked :true,
        //分片大小
        chunkSize :1024*1024*2,
        chunkRetry :3,
        threads :3,//最大并发
        fileNumLimit :1,
        fileSizeLimit :1024*1024*1024*1024,
        fileSingleSizeLimit: 1024*1024*1024,
        duplicate :true
        /*accept: {
            title: 'file',
            extensions: 'jpg,png,ai,zip,rar,psd,pdf,cdr,psd,tif',
            mimeTypes: '*!/!*'
        }*/
    });

    //当文件被加入队列之前触发，此事件的handler返回值为false，则此文件不会被添加进入队列。
    uploader.on('beforeFileQueued',function(file){
        /*if(",jpg,png,ai,zip,rar,psd,pdf,cdr,psd,tif,docx,xlsx,".indexOf(","+file.ext+",")<0){
            alert("不支持的文件格式");
        }*/
    });

    //当有文件添加进来的时候
    uploader.on( 'fileQueued', function( file ) {
        times = 1;
        var src = "<%=request.getContextPath()%>/images/fileType/file.png";
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

        upload_success = false;
        $("#list").html(
                '<div class="clearfix"><img src='+src+' width="50px" class="icon-file" />'+
                '<div class="fl" style="margin-left: 70px;">'+
                '<p class="speed font-12" id="speed_'+file.id+'">校验文件...</p>'+
                '<div class="progress">'+
                '<span id="pro_'+file.id+'" class="progressing"></span>'+
                '</div>'+
                '<span class="file-size">'+(file.size/1024/1024).toFixed(2)+'MB</span>'+
                '<a href="javascript:void(0);" id="stopOretry_'+file.id+'" onclick="stop(\''+file.id+'\');" class="a-pause">暂停</a>'+
                '</div></div><span class="file-name">'+file.name+'</span><br/>' );
        //文件开始上传后开始计时，计算实时上传速度
        interval = setInterval(function(){times++;},1000);

    });

    //上传过程中触发，携带上传进度。文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var status_pre = file.size*percentage-arr_md5.length*2*1024*1024;
         if(status_pre<=0){
         return;
         }
         $("#pro_"+file.id).css( 'width', percentage * 100 + '%' );
         var speed = ((status_pre/1024)/times).toFixed(0);
         $("#speed_"+file.id).text(speed +"KB/S");
         if(percentage == 1){
         $("#speed_"+file.id).text("上传完毕");
         }

    });

    //文件上传成功时触发
    uploader.on( 'uploadSuccess', function( file,response) {
        console.log("成功上传"+file.name+"  res="+response);
        $.ajax({
            type:'post',
            url:'<%=request.getContextPath()%>/fileUpLoadController/fileUpLoad.do',
            dataType: 'json',
            data: {
                methodName:'composeFile',
                name:file.name,
                chunks:chunks,
                tokenid:$("#tokenid").val()
            },
            async:false,
            success: function(data) {
                console.log("==compose=="+data.status);
                if(data.status == "success"){
                    upload_success = true;
                    $("#url").val(data.url);
                    console.log(data.url);
                }else{
                    upload_success = false;
                    alert(data.errstr);
                }
            }
        });
    });

    //文件上传异常失败触发
    uploader.on( 'uploadError', function( file,reason ) {
        console.log("失败"+reason );
    });
    $("#multiUpload").on("click",function(){
        uploader.upload();
    });
    //某个文件开始上传前触发，一个文件只会触发一次。
    uploader.on( 'uploadStart', function(file) {
        console.info("file="+file.name);
        //分片文件上传之前
        $.ajax({
            type:'post',
            url:'<%=request.getContextPath()%>/fileUpLoadController/fileUpLoad.do',
            dataType: 'json',
            data: {
                methodName:'md5Validation',
                name:file.name,
                tokenid:$("#tokenid").val()
            },
            async:false,
            success: function(data) {
                if(data.md5_arr != ""){
                    arr_md5 = data.md5_arr.split(",")
                }else{
                    arr_md5 = new Array();
                }
            }
        });
    });

    //当所有文件上传结束时触发。
    uploader.on( 'uploadFinished', function() {

    });

    function stop(id){
        uploader.stop(true);
        $("#stopOretry_"+id).attr("onclick","retry('"+id+"')");
        $("#stopOretry_"+id).text("恢复");
        clearInterval(interval);
    }
    function retry(id){
        uploader.retry();
        $("#stopOretry_"+id).attr("onclick","stop('"+id+"')");
        $("#stopOretry_"+id).text("暂停");
        interval = setInterval(function(){times++;},1000);
    }
</script>
</html>
