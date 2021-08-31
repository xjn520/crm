<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
    <script src="ECharts/echarts.min.js"></script><%--这个是引入图标外部样式文件--%>
    <script src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript">
        $(function (){
            //页面加载后，绘制统计图表
            getCharts();
        });
        function getCharts()
        {
        }
    </script>
</head>
<body>
        <%--为ECharts准备一个具备大小宽高的DOM容器--%>
        <div id="main" style="width: 600px;height: 400px;"></div>
</body>
</html>