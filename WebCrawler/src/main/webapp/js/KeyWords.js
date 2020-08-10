var newsId, keyWordsId, keyWords, thisObject;   //定义要修改的新闻的id、关键词id、关键词
var onclickNum = 0;  // 鼠标点击修改按钮的次数，初始为0
var confirmCancel = 0;  //鼠标点击确认修改或者取消的次数，初始为1
function addKeyWords(news_id) {
    newsId = news_id;
    var strHtml = "";
    strHtml += "<div class='addKeyWordWindows'>";
    strHtml += "<input type='text' class='inputAddKeyWords' placeholder='请输入想添加的关键词'>";
    strHtml += "</br>";
    strHtml += "<input type='button' onclick='addKeyWordsConfirm(" + newsId + ")' value='确认' class='addKeyWordsConfirm'>";
    strHtml += "<input type='button' onclick='addKeyWordsCancle()' value='取消' class='addKeyWordsCancle'>";
    strHtml += "</div>";
    $(".contentBody").append(strHtml);
}

/*
确认添加关键词
 */
function addKeyWordsConfirm(news_id) {
    keyWords = $(".inputAddKeyWords").val();
    newsId = news_id;
    $.ajax({
        type: "get",
        url: "hanLp",
        data: {
            "action": "addKeyWords",
            "keyWords": keyWords,
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
function addKeyWordsCancle() {
    $(".addKeyWordWindows").remove();
}

function updateKeyWords(id, string, t) {  //将要修改的关键词id和关键词传递进函数
    keyWordsId = id;
    keyWords = string;
    thisObject = t;
    var strHtml = "";
    onclickNum += 1;  //修改按钮点击次数+1
    if (onclickNum == 1) {  //首次点击修改按钮
        strHtml += "<div class='updateBefore'>";
        strHtml += "<input type='text' class='inputKeyWords'>";
        strHtml += "<input type='button' onclick='confirmUpdate(thisObject)' value='修改' class='confirmUpdate'>";
        strHtml += "<input type='button' onclick='cancleUpdate(thisObject)' value='取消' class='cancleUpdate'>";
        strHtml += "</div>";
        $(t).parents().prev("td").children("p").css("display", "none");   //将原先内容隐藏(不要删除)
        $(t).parents().prev("td").append(strHtml);                          //追加
        $(".inputKeyWords").val(keyWords);                                  //将修改前的内容赋值进input
    } else {         //多次点击修改按钮
        if (confirmCancel == 0) {  //如果没有点击确认修改或者取消
            alert("请完成上一步操作！");
        } else {  //多次点击修改按钮并且完成了之前的操作
            $(".updateBefore").remove();        //多次点击并完成操作，每次点击都将上次点击追加的内容删除
            strHtml += "<div class='updateBefore'>";
            strHtml += "<input type='text' class='inputKeyWords'>";
            strHtml += "<input type='button' onclick='confirmUpdate(thisObject)' value='修改' class='confirmUpdate'>";
            strHtml += "<input type='button' onclick='cancleUpdate(thisObject)' value='取消' class='cancleUpdate'>";
            strHtml += "</div>";
            $(t).parents().prev("td").children("p").css("display", "none");   //将原先内容隐藏(不要删除)
            $(t).parents().prev("td").append(strHtml);                          //追加
            $(".inputKeyWords").val(keyWords);                                  //将修改前的内容赋值进input
            confirmCancel = 0;   //切记将confrimCancle重置为0；
        }
    }
}

function confirmUpdate(t) {  //确认修改按钮
    confirmCancel = 1;  //点击flag=1
    keyWords = $(".inputKeyWords").val();  //1.先获得的输入的值
    $.ajax({
        type: "get",
        url: "hanLp",
        data: {
            "action": "updateKeyWords",
            "keyWordsId": keyWordsId,
            "keyWords": keyWords,
        },
        success: function (result) { //后台操作成功后将前台删除替换
            $(".updateBefore").remove();                        //删除追加的内容
            $(t).parents().prev("td").children("p").remove();  //删除之前的段落内容
            var strHtml = "<p>" + keyWords + "</p>";
            $(t).parents().prev("td").append(strHtml)   //追加新的内容+keyWordsId,keyWords,t+
            // $(".updateKeyWords").attr("onclick", "updateKeyWords("+keyWordsId+","+keyWords+","+this+")");
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

function deleteKeyWords(keyWordsId, t) {
    $.ajax({
        type: "get",
        url: "hanLp",
        data: {
            "action": "deleteKeyWords",
            "keyWordsId": keyWordsId,
        },
        success: function (result) {//后端删除成功后进行前端删除
            $(t).parents().parents("tr").remove();
        },
        error: function (result) {
            alert(result);
        }
    });
}