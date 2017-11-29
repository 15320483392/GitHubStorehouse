<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>主页</title>
    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
</head>
<input id="username" type="text" style="width:200px;height:30px;line-height: 30px;"/>
<input id="password" type="text" style="width: 200px;hiehgt:30px;line-height: 30px;"/>
<input type="button" onclick="submitbtn();" value="提交"/>
<br/>
<input type="button" value="获取数据" onclick="findUser();"/>
<a href="<%=request.getContextPath()%>/UserController/otherpag.do">跳转</a>
</body>
<script type="text/javascript">
    function submitbtn() {
        var username = $("#username").val();
        var password = $("#password").val();
        $.ajax({
            url:"<%=request.getContextPath()%>/UserController/longin.do",
            type:"post",
            data:{"username":username,"password":password},
            success:function (res) {
                console.log(res);
            }

        });
    }

    function findUser() {
        $.ajax({
            url:"<%=request.getContextPath()%>/UserController/getdata.do",
            dataType:"json",
            success:function(res){
                console.log(res);
            }
        });
    }
</script>
</html>
