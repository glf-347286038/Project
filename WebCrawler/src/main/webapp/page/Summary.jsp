<%--
  Created by IntelliJ IDEA.
  User: 高凌峰
  Date: 2020/2/14
  Time: 3:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>
<html>
<head>
    <title>修改新闻</title>
    <link rel="stylesheet" type="text/css" href="/css/Summary.css">
    <script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src="/js/Summary.js"></script>
</head>
<body>
<div class="heading">
    <ul>
        <li><a href="news?action=lookById&newsId=${newsId}">返回上一页</a></li>
        <li><input type="button" value="添加新闻摘要" onclick="addSummary(${newsId})"></li>
    </ul>
</div>
<div class="contentBody">
    <table>
        <thead>
        <tr>
            <%--<td width="20%">id</td>--%>
            <td width="70%">摘要</td>
            <td width="30%">操作</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="summary" items="${summary}">
            <tr>
                    <%--<td>${summary.id}</td>--%>
                <td><p>${summary.summary}</p></td>
                <td><input type="button" value="修改" onclick="updateSummary('${summary.id}','${summary.summary}',this)"
                           class="updateSummary">
                    <input type="button" value="删除" onclick="deleteSummary(${summary.id},this)" class="deleteSummary">
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
