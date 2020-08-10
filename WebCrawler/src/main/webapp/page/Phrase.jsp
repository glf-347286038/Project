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
    <title>新闻短语</title>
    <link rel="stylesheet" type="text/css" href="/css/Phrase.css">
    <script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src="/js/Phrase.js"></script>
</head>
<body>
<div class="heading">
    <ul>
        <li><a href="news?action=lookById&newsId=${newsId}">返回上一页</a></li>
        <li><input type="button" value="添加新闻摘要" onclick="addPhrase(${newsId})"></li>
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
        <c:forEach var="phrase" items="${phrase}">
            <tr>
                    <%--<td>${phrase.id}</td>--%>
                <td><p>${phrase.phrase}</p></td>
                <td><input type="button" value="修改" onclick="updatePhrase('${phrase.id}','${phrase.phrase}',this)"
                           class="updatePhrase">
                    <input type="button" value="删除" onclick="deletePhrase(${phrase.id},this)" class="deletePhrase"></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
