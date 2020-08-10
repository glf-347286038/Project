var currentPage = 1;  //当前页码  初始值为1
var pageSize = 10;         //表的行数
var rowTotal;       //数据库有多少条数据
var $html = "";
window.onload = function () {    //页面在加载时就默认调用此函数该函数只会执行一次 之后的点击事件不会再执行 刷新可执行
    $(".newsTable").remove();       //刷新时删除
    createTable(currentPage);  //调用追加表格函数
};

function createTable(currentPage) {     //创造table的函数  传入值为当前页码数
    $.ajax({
        type: "post",
        url: "news",
        data: {
            "action": "findAll",
            "currentPage": currentPage,
            "pageSize": pageSize,
        },
        dataTye: "json",   //预计接受服务器返回数据的类型
        success: function (result) {
            var Data = JSON.parse(result);    //特别注意(我被这畜生弄哭了)
            var jsonNews;
            for (var k in Data) {     //k是key  Data是value
                rowTotal = parseInt(k);        //得到一共多少条数据  注意这是String类型
                jsonNews = Data[k];
            }
            var strHtml = "";
            strHtml += "<table class=newsTable>";
            strHtml += "<thead>";
            strHtml += "</tr>";
            strHtml += "<td>" + "编 号" + "</td>";
            strHtml += "<td>" + "标 题" + "</td>";
            strHtml += "<td>" + "来 源" + "</td>";
            strHtml += "<td>" + "日 期" + "</td>";
            strHtml += "<td>" + "操 作" + "</td>";
            strHtml += "</tr>";
            strHtml += "</thead>";
            strHtml += "<tbody>";
            $.each(jsonNews, function (i, value) {
                var trId = value.id;
                strHtml += "<tr>";
                strHtml += "<td>" + value.id + "</td>";
                strHtml += "<td>" + value.title + "</td>";
                strHtml += "<td>" + value.source + "</td>";
                strHtml += "<td>" + value.date + "</td>";
                strHtml += "<td>" + "<a href='news?action=lookById&newsId=" + value.id + "'>" + "<img src='/image/look.png' alt='查看' class='lookButton'>" + "</a>" + "&nbsp;&nbsp;&nbsp;";
                //strHtml += "<td>" + "<img src='/image/look.png' alt='查看' class='lookButton' '>"onclick='look("+value.id+")+"&nbsp;&nbsp;&nbsp;";   //这样就是变量了
                strHtml += "<img src='/image/delete.png' alt='删除'  class='deleteButton' onclick='deleteById(" + value.id + ",this)'>" + "</td>";
                strHtml += "</tr>";
            });
            strHtml += "</tbody>";
            strHtml += "</table>";
            $html = $(strHtml);
            //$html = $(""+strHtml+"");  (不知道为什么别人要这么干？)
            $(".contentBody").append($html);
            $("#currentPageButton").val(currentPage);
            $("#rowTotal").val(Math.ceil(rowTotal / pageSize) + "页 " + " 共" + rowTotal + "条数据");    //将多少页显示出来
        },
        error: function () {
            alert("调用ajax发生错误！1");
        }
    });
}

function previousPage() {   /*点击上一页*/
    //1.先将追加的表格删除 (如果currentPage-1=1的话 不删除)
    // 2.将currentPage-1
    // 3.(将下一页按钮背景色改变)
    //currentPage = parseInt($("#currentPageButton").val()) - 1;
    if (currentPage == 1) {    //如果上一页是第0页  还是显示第一页 并要弹出提出消息
        alert("当前页为首页!");
    } else {   //如果当前页不是第一页的话，1.删除追加的表格 2.再追加新的表格 3.将当前页按钮的值减一 4.变换按钮背景颜色   1、2两步自动执行的函数执行了
        //createTable(currentPage);
        currentPage -= 1;            //将全局变量当前页减一
        $(".newsTable").remove();
        createTable(currentPage);
        $("#currentPageButton").val(currentPage);    //将显示当前页按钮的值改变
    }
}

function nextPage() {       /*点击下一页*/
    if (currentPage == Math.ceil(rowTotal / pageSize)) {     //如果当前页为最后一页   向上取整
        alert("这是最后一页了!")
        //  $(".newsTable").after("<img src='/image/end.jpg' alt='人家也是有底线的~ ~ ~'>");
    } else {
        currentPage += 1;
        $(".newsTable").remove();
        createTable(currentPage);
        $("#currentPageButton").val(currentPage);
    }
}

function jump() {   /*确定跳到某一页*/
    if ($("#inputJumpPage").val().valueOf()) {     //1.先判断输入的内容是否是数字类型
        var input = $("#inputJumpPage").val().valueOf();
        if (input % 1 === 0) {      //2.再判断是否是整数
            //alert(typeof input);  结果是String
            var input01 = parseInt(input);
            //alert(typeof input01);  number
            if (input01 > (Math.ceil(rowTotal / pageSize))) {
                alert("一共只有" + Math.ceil(rowTotal / pageSize) + "页!");
            } else {
                currentPage = input01;
                $("#currentPageButton").val(currentPage);
                $("#inputJumpPage").val(currentPage);
                $(".newsTable").remove();
                createTable(currentPage);
            }
        } else {
            alert("请输入一个整数" + "比如:2");
        }
    } else {
        alert(typeof jumpPage);
        alert("请输入一个合理的整数!");
    }
}

/**
 点击查找事件
 */
var findCurrentPage = 1;    //新的分页，新的当前页初始为1
function findByTitle() {
    findByTitle02(findCurrentPage);
}

function findByTitle02(findCurrentPage) {
    var newsTitle = $(".inputName").val();   //获取输入的标题
    $.ajax({
        url: "news",
        type: "get",
        data: {
            "action": "findByTitle",
            "newsTitle": newsTitle,
            "currentPage": findCurrentPage,
            "pageSize": pageSize,
        },
        dataType: "json",
        success: function (result) {
            // var Data = JSON.parse(result);    //特别注意(我被这畜生弄哭了)
            var jsonNews;
            for (var k in result) {     //k是key  Data是value
                rowTotal = parseInt(k);        //得到一共多少条数据  注意这是String类型
                jsonNews = result[k];
            }
            if (rowTotal == 0) {
                alert("没有查询到相关内容!");
            } else {    //若查询结果不为0
                $(".newsTable").remove();       //将原来的表格删除
                var strHtml = "";
                strHtml += "<table class=newsTable>";
                strHtml += "<thead>";
                strHtml += "</tr>";
                strHtml += "<td>" + "编 号" + "</td>";
                strHtml += "<td>" + "标 题" + "</td>";
                strHtml += "<td>" + "来 源" + "</td>";
                strHtml += "<td>" + "日 期" + "</td>";
                strHtml += "<td>" + "操 作" + "</td>";
                strHtml += "</tr>";
                strHtml += "</thead>";
                strHtml += "<tbody>";
                $.each(jsonNews, function (i, value) {
                    var trId = value.id;
                    strHtml += "<tr>";
                    strHtml += "<td>" + value.id + "</td>";
                    strHtml += "<td>" + value.title + "</td>";
                    strHtml += "<td>" + value.source + "</td>";
                    strHtml += "<td>" + value.date + "</td>";
                    strHtml += "<td>" + "<a href='news?action=lookById&newsId=" + value.id + "'>" + "<img src='/image/look.png' alt='查看' class='lookButton'>" + "</a>" + "&nbsp;&nbsp;&nbsp;";
                    //strHtml += "<td>" + "<img src='/image/look.png' alt='查看' class='lookButton' '>"onclick='look("+value.id+")+"&nbsp;&nbsp;&nbsp;";   //这样就是变量了
                    strHtml += "<img src='/image/delete.png' alt='删除'  class='deleteButton' onclick='deleteById(" + value.id + ",this)'>" + "</td>";
                    strHtml += "</tr>";
                });
                strHtml += "</tbody>";
                strHtml += "</table>";
                $html = $(strHtml);
                $(".contentBody").append($html);
                $("#currentPageButton").val(findCurrentPage);
                $("#rowTotal").val(Math.ceil(rowTotal / pageSize) + "页 " + " 共" + rowTotal + "条数据");    //将多少页显示出来
                // var choseText = $(".newsTable").html();          //欲用正则表达式替换 将输入的搜索字css改变 不幸失败  确实难道我了
                // var reg = new RegExp(newsTitle,"g");   //g表示全部替换
                // choseText.replace(reg,"<span class='findNewsTitle'>"+newsTitle+"</span>");
                // var inHtmlColor = "<span style='color: red'>"+newsTitle+"</span>";
                $("#previousButton").removeAttr("onclick");     //删除点击函数重新添加点击函数
                $("#previousButton").attr("onclick", "findPrevious()")
                $("#nextButton").removeAttr("onclick")
                $("#nextButton").attr("onclick", "findNext()")
            }
        },
        error: function () {
            alert("出 现 错 误！");
        }
    });
}

function findPrevious() {   //模糊查找的下一页按钮
    if (findCurrentPage == 1) {    //如果上一页是第0页  还是显示第一页 并要弹出提出消息
        alert("当前页为首页!");
    } else {   //如果当前页不是第一页的话，1.删除追加的表格 2.再追加新的表格 3.将当前页按钮的值减一 4.变换按钮背景颜色   1、2两步自动执行的函数执行了
        //createTable(currentPage);
        findCurrentPage -= 1;            //将全局变量当前页减一
        $(".newsTable").remove();
        findByTitle02(findCurrentPage);
        $("#currentPageButton").val(findCurrentPage);    //将显示当前页按钮的值改变
    }
}

function findNext() {    //模糊查找的下一页按钮
    if (findCurrentPage == Math.ceil(rowTotal / pageSize)) {     //如果当前页为最后一页   向上取整
        alert("这是最后一页了!")
    } else {
        findCurrentPage += 1;
        $(".newsTable").remove();
        findByTitle02(findCurrentPage);
        $("#currentPageButton").val(findCurrentPage);
    }
}

/*
    删除点击事件(单删)
 */
function deleteById(newsId, t) {
    var deleteId = newsId;
    //alert(deleteId);
    // $(t).parents().parents("tr").css("background","red");//测试选中这一行成功
    $.ajax({
        url: "news",
        type: "get",
        data: {
            "action": "delete",
            "newsId": deleteId
        },
        // dataType:"json",    后端返回的是字符串 会发生错误进入error
        success: function (result) {   //还要进行前端删除
            $(t).parents().parents("tr").remove();
            // alert(result);
        },
        error: function () {
            alert("删除新闻失败");
        }
    });
}

/*
    添加点击事件
 */
function add() {
    location.href = "news?action=add&currentPage=" + currentPage;
}