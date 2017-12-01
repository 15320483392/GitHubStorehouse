<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>主页</title>
</head>
<style type="text/css">
    ul {
        list-style-type: none;
        margin: 0;
        padding: 0;
        margin-bottom: 10px;
        background-color: #d1d1d1;
    }
    li {
        margin: 2% 4% 2% 3%;
        padding: 5px;
        width: 90%;
        background-color: #7fca83;
    }
    .droptrue{
        float: left;
    }
    .dropfalse{
        width: 20%;
    }
    .searchBar{
        width: 100%;height: 50px;border: 1px solid grey;
        border-radius: 10px;
        outline:none;
    }
    .liwidth{
        width: 200px;
    }
</style>
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
<body>
<div class="searchBar">

</div>
<ul id="sortable" class="droptrue" style="width: 20%;">
    <li class="ui-state-default">Item 1</li>
    <li class="ui-state-default">Item 2</li>
    <li class="ui-state-default">Item 3</li>
    <li class="ui-state-default">Item 4</li>
    <li class="ui-state-default">Item 5</li>
</ul>
<ul id="sortable2" class="droptrue" style="width: 80%;height: 600px; background-color: #b4ecf3;">
</ul>
</body>
<script type="text/javascript">
    var count = 0;
    $(function() {
       /* $( "#sortable" ).sortable({
            revert: true,
            connectWith: "ul"
        });*/
        $( "#sortable, #sortable2" ).sortable({
            connectWith: ".droptrue",
            revert: true
        }).disableSelection();
    });
</script>
</html>
