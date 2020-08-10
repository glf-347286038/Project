var newsId, summaryId, summary, thisObject;   //定义要修改的新闻的id、关键词id、关键词
var onclickNum = 0;  // 鼠标点击修改按钮的次数，初始为0
var confirmCancel = 0;  //鼠标点击确认修改或者取消的次数，初始为1
function addSummary(news_id) {
    newsId = news_id;
    var strHtml = "";
    strHtml += "<div class='addSummaryWindows'>";
    strHtml += "<input type='text' class='inputAddSummary' placeholder='请输入想添加的摘要'>";
    strHtml += "</br>";
    strHtml += "<input type='button' onclick='addSummaryConfirm(" + newsId + ")' value='确认' class='addSummaryConfirm'>";
    strHtml += "<input type='button' onclick='addSummaryCancle()' value='取消' class='addSummaryCancle'>";
    strHtml += "</div>";
    $(".contentBody").append(strHtml);
}

/*
确认添加摘要
 */
function addSummaryConfirm(news_id) {
    summary = $(".inputAddSummary").val();
    newsId = news_id;
    $.ajax({
        type: "get",
        url: "hanLp",
        data: {
            "action": "addSummary",
            "summary": summary,
            "newsId": newsId,
        },
        success: function (result) {
            // var strHtml = "";
            // strHtml+="<tr>";
            // strHtml+="<td>"+"<p>"+"+keyWords+"+"</p>"+"</td>";
            // strHtml+="<td>"+121+"</td>";
            // strHtml+="</tr>";
            // $("table tbody").append();
            // $(".addKeyWordWindows").remove();
            // alert(result);
            window.location.reload();
        },
        error: function (result) {
            alert(result);
        }
    });
}

/*
取消添加
 */
function addSummaryCancle() {
    $(".addSummaryWindows").remove();
}

function updateSummary(id, string, t) {  //将要修改的关键词id和关键词传递进函数
    summaryId = id;
    summary = string;
    thisObject = t;
    var strHtml = "";
    onclickNum += 1;  //修改按钮点击次数+1
    if (onclickNum == 1) {  //首次点击修改按钮
        strHtml += "<div class='updateBefore'>";
        strHtml += "<input type='text' class='inputSummary'>";
        strHtml += "<input type='button' onclick='confirmUpdate(thisObject)' value='修改' class='confirmUpdate'>";
        strHtml += "<input type='button' onclick='cancleUpdate(thisObject)' value='取消' class='cancleUpdate'>";
        strHtml += "</div>";
        $(t).parents().prev("td").children("p").css("display", "none");   //将原先内容隐藏(不要删除)
        $(t).parents().prev("td").append(strHtml);                          //追加
        $(".inputSummary").val(summary);                                  //将修改前的内容赋值进input
    } else {         //多次点击修改按钮
        if (confirmCancel == 0) {  //如果没有点击确认修改或者取消
            alert("请完成上一步操作！");
        } else {  //多次点击修改按钮并且完成了之前的操作
            $(".updateBefore").remove();        //多次点击并完成操作，每次点击都将上次点击追加的内容删除
            strHtml += "<div class='updateBefore'>";
            strHtml += "<input type='text' class='inputSummary'>";
            strHtml += "<input type='button' onclick='confirmUpdate(thisObject)' value='修改' class='confirmUpdate'>";
            strHtml += "<input type='button' onclick='cancleUpdate(thisObject)' value='取消' class='cancleUpdate'>";
            strHtml += "</div>";
            $(t).parents().prev("td").children("p").css("display", "none");   //将原先内容隐藏(不要删除)
            $(t).parents().prev("td").append(strHtml);                          //追加
            $(".inputSummary").val(summary);                                  //将修改前的内容赋值进input
            confirmCancel = 0;   //切记将confrimCancle重置为0；
        }
    }
}

function confirmUpdate(t) {  //确认修改按钮
    confirmCancel = 1;  //点击flag=1
    summary = $(".inputSummary").val();  //1.先获得的输入的值
    $.ajax({
        type: "get",
        url: "hanLp",
        data: {
            "action": "updateSummary",
            "summaryId": summaryId,
            "summary": summary,
        },
        success: function (result) { //后台操作成功后将前台删除替换
            $(".updateBefore").remove();                        //删除追加的内容
            $(t).parents().prev("td").children("p").remove();  //删除之前的段落内容
            var strHtml = "<p>" + summary + "</p>";
            $(t).parents().prev("td").append(strHtml)   //追加新的内容+keyWordsId,keyWords,t+
        },
        error: function (result) {
            alert(result);
        }
    });
}

function cancleUpdate(t) {  //取消按钮 还原之前的样式
    confirmCancel = 1;
    $(t).parents().prev("td").children("p").css("display", "block");
    $(".updateBefore").remove();
}

function deleteSummary(summaryId, t) {
    $.ajax({
        type: "get",
        url: "hanLp",
        data: {
            "action": "deleteSummary",
            "summaryId": summaryId,
        },
        success: function (result) {//后端删除成功后进行前端删除
            $(t).parents().parents("tr").remove();
        },
        error: function (result) {
            alert(result);
        }
    });
}