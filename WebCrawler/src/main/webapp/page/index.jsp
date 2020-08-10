<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%--
  Created by IntelliJ IDEA.
  User: 高凌峰
  Date: 2020/1/9
  Time: 11:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<c:set var="app" value="<%=basePath%>"></c:set>
<html>
<head>
    <title>首页</title>
    <link rel="stylesheet" type="text/css" href="/css/index.css">
    <script type="text/javascript" src="${app}/js/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src="${app}/js/index.js"></script>
</head>
<body class="BodyClass">
<div class="heading">
    <span class="titie">基于新闻大数据的文本语义信息提取与可视化</span>
</div>
<div class="nav">
    <ul class="clearfix">
        <li>
            查找:
        </li>
        <li class="search">
            <input type="text" class="inputName" placeholder="支持模糊查找" style="float: left;">
            <img src="${app}/image/search.png" onclick="findByTitle()" alt="查找新闻" width="100px" height="100px">
        </li>

        <li class="add">
            添加新闻:
            <%--<a href="news?action=add&"><img src="${app}/image/add.png" alt="添加新闻"></a>--%>
            <img src="${app}/image/add.png" alt="添加新闻" onclick="add()">
        </li>
    </ul>
</div>
<div class="contentBody">

</div>

<div id="buttons">
    <img src="${app}/image/arrow_left.png" alt="上一页" id="previousButton" onclick="previousPage()">
    <input type="button" id="currentPageButton">
    <img src="${app}/image/arrow_right.png" alt="下一页" id="nextButton" onclick="nextPage()">
    <span class="text01">到 第 </span><input type="text" name="jumpPage" id="inputJumpPage" value=""><span class="text02">页</span>
    <input type="button" id="jumpButton" value="确定" onclick="jump()">
    <input type="button" id="rowTotal">
    <input type="hidden" name="currentPage" value="${currentPage}" id="hiddenCurrentPage">
</div>
<div class="footing">
    @2020 安徽工程大学毕业设计-基于新闻大数据的文本语义信息提取与可视化(开发者:软件164高凌峰)
</div>

</body>
</html>
