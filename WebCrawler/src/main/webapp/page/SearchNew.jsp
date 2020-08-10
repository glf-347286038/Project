<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>
<%--
  Created by IntelliJ IDEA.
  User: 高凌峰
  Date: 2020/1/8
  Time: 0:15
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page language="java" contentType="text/html ; charset=UTF-8" pageEncoding="UTF-8" %>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="app" value="<%=request.getContextPath()%>"/>
<html>
<head>
    <title>查找文章</title>
    <link rel="stylesheet" type="text/css" href="${app}/css/SearchNews.css">
    <script type="text/javascript" src="${app}/js/jquery-1.12.4.min.js"></script>
    <script type="text/javascript">
        var jsonNews, jsonKeyWord, jsonSummary, jsonPhrase; //定义新闻对象 关键字等
        var $html = "";   //全局变量，用于存储html文本对象
        function searchAjax() {   //当点击查找按钮 执行该函数
            //document.getElementById("url");  //通过javascript拿到url
            var $url = $("#url").val();         //通过jquery拿到输入的url
            $("#searchBody").remove();         //每次加载页面之前将上次追加的html删除
            $.ajax({
                url: "search",               //后端的请求地址 在xml里配置的servl-Mapping url
                type: "get",                    //向服务器发送数据的方式
                // data:"url="+$url,   //要向服务器发送的数据 将url传给servlet
                data: {
                    "action": "search",
                    "url": $url,
                },
                dataType: "json",       //后端返回的是json字符串
                success: function (result, testStatus) {   //result代表后端返回的值  status代表状态
                    var json = eval(result);   //js通过eval函数将返回值转换为一个js能够识别的json对象
                    //result中的顺序是News新闻对象、10个关键字、3句摘要、5句短语
                    // console.log(json); //正确 勿删 在浏览器控制台中显示
                    $(".add").css({     //将删除的css加回来
                        "background": "blue",
                    });
                    $(".add:hover").css("background", "crimson");
                    for (var i = 0; i < json.length; i++) {   //将后端的值赋值
                        jsonNews = json[0];
                        jsonKeyWord = json[1];
                        jsonSummary = json[2];
                        jsonPhrase = json[3];
                    }
                    //alert(jsonNews.date);   正确
                    //alert(jsonKeyWord);    [印度/nsf],[导弹/n],[K-/nx],[报道/v],[成功/a],[试射/v],[水下/s],[发射/v],[1月/t],[射程/n]
                    //window.location.reload();   //每次点击事件后重新刷新页面
                    addNewsToHtml(jsonNews, jsonKeyWord, jsonSummary, jsonPhrase); //将jsonNews等等全部添加到jsp页面
                },
                error: function () {
                    alert("爬虫失败，请保证您的网络信号正常");
                }
            });   //ajax结束
        }

        function addNewsToHtml(jsonNews, jsonKeyWord, jsonSummary, jsonPhrase) {

            var strHtml = "<div id=searchBody>";
            strHtml += "<div id=news>";
            strHtml += "<p id=newsTitle>" + jsonNews.title + "</p>";
            strHtml += "<p id=newsSource>" + "来源：" + jsonNews.source + "</p>";
            strHtml += "<p id=newsDate>" + jsonNews.date + "</p>";
            strHtml += "<p id=newsContent>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + jsonNews.content + "</p>";
            strHtml += "</div>";      //添加新闻进页面结束
            strHtml += "<div id=hanLp>";

            strHtml += "<p id=keyWord>" + "新闻关键词(10个)" + "</p>";
            strHtml += "<p class='keyWord'>";
            $.each(jsonKeyWord, function (i, value) {
                strHtml += value + "&nbsp;&nbsp";
            });
            strHtml += "</p>";   //关键词加入结束
            strHtml += "</br>";

            strHtml += "<p id=summary>" + "新闻摘要" + "</p>";
            strHtml += "<p class='summary'>";
            $.each(jsonSummary, function (i, value) {
                strHtml += i + 1 + "." + value + "。" + "</br>";
            });
            strHtml += "</p>";    //新闻摘要加入结束

            strHtml += "</br>";
            strHtml += "<p id=phrase>" + "新闻短语" + "</p>";
            strHtml += "<p class='phrase'>";
            $.each(jsonPhrase, function (i, value) {
                strHtml += i + 1 + "." + value + "</br>";
            });
            strHtml += "</p>";    //新闻短语加入结束
            strHtml += "</div>";     //添加关键词、摘要、短语进页面结束
            strHtml += "</div>";

            $html = $("" + strHtml + "");
            $(".SearchHeard").after($html);

            $("#news").css({  //给边框赋多个属性
                "border-right-style": "solid",  //添加右边边框
                "border-color": "blue",
            });
        }    //添加新闻到jsp页面中

        //保存到数据库的点击事件函数
        function savaToDataBase() {
            var url01 = $("#url").val();
            $.ajax({
                url: "news",
                type: "get",
                data: {
                    "url": url01,
                    "action": "save",
                },
                success: function (result) {
                    $("#url").val("");   //将url清空 二次点击就会获取不到url
                    $(".add").css("background", "#808080");   //改变背景颜色 之后点击搜索按钮会改回来
                    alert(result);
                },
                error: function () {
                    alert("保存失败！");
                }
            });
        }

        function backIndex(currentPage) {    //有空可以向后台传回一个current返回原来的地方
            location.href = "news?action=init&currentPage=" + currentPage;
        }
    </script>
</head>
<body>
<div class="SearchHeard">
    <ul>
        <li><input type="url" name="url" placeholder="请输入完整的URL" id="url"></li>
        <li><input type="button" value="查找" id="search" onclick="searchAjax()"></li>
        <li><input type="button" value="保存到数据库" class="add" onclick="savaToDataBase()"></li>
        <li><input type="button" value="返回第${currentPage}页" onclick="backIndex(${currentPage})" class="backButton"></li>
    </ul>
</div>
</div>
</body>
</html>
