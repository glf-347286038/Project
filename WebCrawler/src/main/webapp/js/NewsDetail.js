var currentPage = 1;
var newsId, newsTitle, newsSource, newsDate, newsContent;
var keyWords;
var summart;
var phrase;

function edit() {
    window.open("/page/KeyWords.jsp", "height=300,width=300");
}

function closeSide() {
    // $(".left-side01").animate({width:$(this).width()+10,},200); 滑动失败
    $(".left-side").fadeOut(700);
}

//展开右侧导航栏就将新闻对象的属性传递进来
function openSide() {
    $(".left-side").fadeIn(500);
    $(".left-side").css("display", "block");
}

function editNews(id, title, source, date, content) {  //点击修改新闻一级菜单 就将新闻的所有信息传递进js中
    $("#secendNav").slideToggle("2000");  //二级菜单显示
    newsId = id;
    newsTitle = title;
    newsSource = source;
    newsDate = date;
    newsContent = content;
}

/*
我是为了实验下二级菜单 所以将修改新闻强行拆成四个
其实还是每次将新闻的所有内容都传给dao层
 */

function editTitle() {
    $(".newTitle").css("display", "none");   //将原先的html隐藏
    $(".editNewsTitle").remove();             //防止重复点击修改
    var strHtml = "";
    strHtml += "<div class='editNewsTitle'>";
    strHtml += "<textarea class='editAfterTitle'>" + newsTitle + "</textarea>" + "</br>";
    strHtml += "<input type='button' onclick='editTitleConfirm()' value='修改' class='editTitleConfirm'>";
    strHtml += "<input type='button' onclick='editTitleCancle()' value='取消' class='editTitleCancle'>";
    strHtml += "</div>";
    $(".newsSource").before(strHtml);
}

function editTitleConfirm() {
    newsTitle = $(".editAfterTitle").val();
    $.ajax({
        type: "post",
        url: "news",
        data: {
            "action": "editNews",
            "newsId": newsId,
            "newsTitle": newsTitle,
            "newsSource": newsSource,
            "newsDate": newsDate,
            "newsContent": newsContent,
        },
        success: function (result) {
            $(".newTitle").remove();  //删除之前的标题
            $(".editNewsTitle").remove();  //删除追加的修改样式
            var strHtml = "";
            strHtml += "<p class='newTitle'>" + newsTitle + "</p>";
            $(".newsSource").before(strHtml);       //追加新的 样式
        },
        error: function (result) {
            $(".newTitle").css("display", "block");
            $(".editNewsTitle").remove();
            alert(result);
        }
    });
}

function editTitleCancle() {  //取消修改标题就将隐藏的内容显示出来，删除追加的html
    $(".newTitle").css("display", "block");
    $(".editNewsTitle").remove();
}

function editSource() {   //修改新闻来源
    $(".newsSource").css("display", "none");   //将原先的html隐藏
    $(".editNewsSource").remove();              //防止重复点击
    var strHtml = "";
    strHtml += "<div class='editNewsSource'>";
    strHtml += "<input type='text' class='editAfterSource'>" + "</br>";
    strHtml += "<input type='button' onclick='editSourceConfirm()' value='修改' class='editSourceConfirm'>";
    strHtml += "<input type='button' onclick='editSourceCancle()' value='取消' class='editSourceCancle'>";
    strHtml += "</div>";
    $(".newsDate").before(strHtml);
    $(".editAfterSource").val(newsSource);
}

function editSourceConfirm() {
    newsSource = $(".editAfterSource").val();
    $.ajax({
        type: "post",
        url: "news",
        data: {
            "action": "editNews",
            "newsId": newsId,
            "newsTitle": newsTitle,
            "newsSource": newsSource,
            "newsDate": newsDate,
            "newsContent": newsContent,
        },
        success: function (result) {
            $(".newSource").remove();  //删除之前的标题
            $(".editNewsSource").remove();  //删除追加的修改样式
            var strHtml = "";
            strHtml += "<p class='newsSource'>" + newsSource + "</p>";
            $(".newsDate").before(strHtml);       //追加新的 样式
            alert(result);
        },
        error: function (result) {
            $(".newsSource").css("display", "block");
            $(".editNewsSource").remove();
            alert(result);
        }
    });
}

function editSourceCancle() {  //取消修改来源就将隐藏的内容显示出来，删除追加的html
    $(".newsSource").css("display", "block");
    $(".editNewsSource").remove();
}

/*
修改新闻日期
 */
function editDate() {
    $(".newsDate").css("display", "none");
    $(".editNewsDate").remove();              //防止重复点击
    var strHtml = "";
    strHtml += "<div class='editNewsDate'>";
    strHtml += "<input type='date' class='editAfterDate'>" + "</br>";
    strHtml += "<input type='button' onclick='editDateConfirm()' value='修改' class='editDateConfirm'>";
    strHtml += "<input type='button' onclick='editDateCancle()' value='取消' class='editDateCancle'>";
    strHtml += "</div>";
    $(".newsContent").before(strHtml);
    $(".editAfterDate").val(newsDate);
}

function editDateConfirm() {
    newsDate = $(".editAfterDate").val();
    $.ajax({
        type: "post",
        url: "news",
        data: {
            "action": "editNews",
            "newsId": newsId,
            "newsTitle": newsTitle,
            "newsSource": newsSource,
            "newsDate": newsDate,
            "newsContent": newsContent,
        },
        success: function (result) {
            $(".newsDate").remove();  //删除之前的日期
            $(".editNewsDate").remove();  //删除追加的修改样式
            var strHtml = "";
            strHtml += "<p class='newsDate'>" + newsDate + "</p>";
            $(".newsContent").before(strHtml);       //追加新的 样式
        },
        error: function (result) {
            $(".newsDate").css("display", "block");
            $(".editNewsDate").remove();
            alert(result);
        }
    });
}

function editDateCancle() {
    $(".newsDate").css("display", "block");
    $(".editNewsDate").remove();
}

/*
修改新闻正文
 */
function editContent() {
    $(".newsContent").css("display", "none");   //将原先的html隐藏
    $(".editNewsContent").remove();             //防止重复点击修改
    var strHtml = "";
    strHtml += "<div class='editNewsContent'>";
    strHtml += "<textarea rows='10' class='editAfterContent'>" + "  " + newsContent + "</textarea>" + "</br>";
    strHtml += "<input type='button' onclick='editContentConfirm()' value='修改' class='editContentConfirm'>";
    strHtml += "<input type='button' onclick='editContentCancle()' value='取消' class='editContentCancle'>";
    strHtml += "</div>";
    $(strHtml).appendTo($(".news"));   //将子元素追加到父级的最后
    //设置 textarea 的高度随着 内容 增加 自适应

}

function editContentConfirm() {
    newsContent = $(".editAfterContent").val();   //获得输入的值
    $.ajax({
        type: "post",
        url: "news",
        data: {
            "action": "editNews",
            "newsId": newsId,
            "newsTitle": newsTitle,
            "newsSource": newsSource,
            "newsDate": newsDate,
            "newsContent": newsContent,
        },
        success: function (result) {
            $(".newsContent").remove();  //删除之前的日期
            $(".editNewsContent").remove();  //删除追加的修改样式
            var strHtml = "";
            strHtml += "<p class='newsContent'>" + newsContent + "</p>";
            $(strHtml).appendTo($(".news"));
        },
        error: function (result) {
            $(".newsContent").css("display", "block");
            $(".editNewsContent").remove();
            alert(result);
        }
    });
}

function editContentCancle() {
    $(".newsContent").css("display", "block");
    $(".editNewsContent").remove();
}


//点击修改关键词
function editKeyWord(news_id) {
    location.href = "hanLp?action=editKeyWords&newsId=" + news_id;
}

function editSummary(news_id) {
    location.href = "hanLp?action=editSummary&newsId=" + news_id;
}

function editPhrase(news_id) {
    location.href = "hanLp?action=editPhrase&newsId=" + news_id;
}