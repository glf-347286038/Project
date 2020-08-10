<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%--
  Created by IntelliJ IDEA.
  User: 高凌峰
  Date: 2020/2/11
  Time: 23:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%--%>
<%--News news = (News)request.getAttribute("news"); 传统方式取值 ，不提倡--%>
<%--%>--%>
<%--${news}--%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<c:set var="app" value="<%=basePath%>"></c:set>
<html>
<head>
    <title>新闻id:${news.id}</title>
    <link rel="stylesheet" type="text/css" href="${app}/css/NewsDetail.css">
    <script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src="${app}/js/NewsDetail.js"></script>
    <script type="text/javascript" src="/js/echarts.min.js"></script>
</head>
<body>
<div class="heading">
    <ul>
        <li><a href="news?action=init">首页</a></li>
        <li><a href="news?action=add&currentPage=1">添加新闻</a></li>
        <li><img src="${app}/image/edit.png" onclick="edit()" alt 修改新闻 class="editNews"></li>
    </ul>
</div>
<%--右侧导航栏开始--%>
<div class="openSide">
    <img src="/image/open.png" onclick="openSide()"> <%-- 加引号不然报错少) --%>
</div>
<div class="left-side">
    <img src="/image/close.png" id="closeSide" onclick="closeSide()" alt="关闭">
    <ul id="navId">
        <li>
            <span onclick="editNews('${news.id}','${news.title}','${news.source}','${news.date}','${news.content}')">修改新闻内容</span>
            <ul id="secendNav">
                <li onclick="editTitle()">修改标题</li>
                <li onclick="editSource()">修改来源</li>
                <li onclick="editDate()">修改时间</li>
                <li onclick="editContent()">修改正文</li>
            </ul>
        </li>
        <li onclick="editKeyWord('${news.id}')">修改新闻关键词</li>
        <li onclick="editSummary('${news.id}')">修改新闻摘要</li>
        <li onclick="editPhrase('${news.id}')">修改新闻短语</li>
    </ul>
</div>

<div class="contentBody">
    <div class="news">
        <p class="newTitle">${news.title}</p>

        <p class="newsSource">来源：${news.source}</p>
        <p class="newsDate">${news.date}</p>
        <p class="newsContent">&nbsp;&nbsp;&nbsp;${news.content}</p>
        <%--<iframe src="/page/KeyWords.jsp" height="400" width="700" class="editFrame"></iframe>--%>
    </div>

    <div class="hanLp">
        <div class="keyWord">
            <div>关键词</div>
            <table>
                <tbody>
                <c:forEach var="keyWords" items="${keyWords}" varStatus="status">
                    <%-- 判断迭代器的单双数，如果是双数则把当前表格颜色变为灰色 --%>
                    <td>${keyWords.keyword}</td>
                    <%--<td><img src="/image/edit.png"alt="编辑"><img src="/image/delete.png"></td>--%>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="summary">
            <div>摘要</div>
            <table>
                <c:forEach var="summary" items="${summary}">
                    <tr>
                        <td>${summary.summary}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div class="phrase">
            <div>短语</div>
            <table>
                <c:forEach var="phrase" items="${phrase}">
                    <tr>
                        <td>${phrase.id}</td>
                        <td>${phrase.phrase}</td>
                        <td>${phrase.news_id}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>

    <%--知识图谱可视化--%>
    <%--为Echarts准备一个具备大小的DOM--%>
    <div id="main">
    </div>
    <script type="text/javascript">
        var News = new Array();
        var title = '${news.title}';
        var source = '${news.source}';
        var date = '${news.date}';
        var content = '${news.content}';
        var x = content.length;
        x=String(x);
        News.push(title);
        News.push(source);
        News.push(date);
        News.push(x);
        News.push('关键词');  //4
        News.push('摘要');    //5
        News.push('短语');    //6
        var KeyWordList = new Array();
        <c:forEach var="keyWords" items="${keyWords}">
            KeyWordList.push('${keyWords.keyword}')
        </c:forEach>
        var SummaryList = new Array();
        <c:forEach var="summary" items="${summary}">
            SummaryList.push(' ${summary.summary}')
        </c:forEach>
        var PhraseList = new Array();
        <c:forEach var="phrase" items="${phrase}">
            PhraseList.push('${phrase.phrase}')
        </c:forEach>

        <%--基于准备好的dom，初始化echarts实例--%>
        var myChart = echarts.init(document.getElementById('main'));
        // 指定图表的配置项和数据
        var option={
            title:{
              text:"新闻可视化分析",  //标题文本
              left:'3%',   //标题距离左边距
              top:'3%',     //标题顶部边距
                textStyle:{
                     color:'#506bff', //标题颜色
                     fontSize:'60', //标题字体
                }
            },
            tooltip:{    //提示框的配置
                formatter:function (param) {
                    if(param.dataType=='edge'){
                        return param.data.target;
                    }
                    return param.data.name;
                }
            },

            series:[{
                type:"graph",  //系列类型：关系图
                left:'200',
                top:'150',       //图表距离容器顶部的距离
                roam:true,      //受否开启鼠标缩放和平移漫游，默认不开启
                focusNodeAdjacency: true,   // 是否在鼠标移到节点上的时候突出显示节点以及节点的边和邻接节点。[ default: false ]
                force: {                // 力引导布局相关的配置项，力引导布局是模拟弹簧电荷模型在每两个节点之间添加一个斥力，每条边的两个节点之间添加一个引力，每次迭代节点会在各个斥力和引力的作用下移动位置，多次迭代后节点会静止在一个受力平衡的位置，达到整个模型的能量最小化。
                    // 力引导布局的结果有良好的对称性和局部聚合性，也比较美观。
                    repulsion: 7000,            // [ default: 50 ]节点之间的斥力因子(关系对象之间的距离)。支持设置成数组表达斥力的范围，此时不同大小的值会线性映射到不同的斥力。值越大则斥力越大
                    // edgeLength: [150, 100]      // [ default: 30 ]边的两个节点之间的距离(关系对象连接线两端对象的距离,会根据关系对象值得大小来判断距离的大小)，
                    edgeLength: [600, 600]                          // 这个距离也会受 repulsion。支持设置成数组表达边长的范围，此时不同大小的值会线性映射到不同的长度。值越小则长度越长。如下示例:
                                                // 值最大的边长度会趋向于 10，值最小的边长度会趋向于 50      edgeLength: [10, 50]
                },

                layout: "force",            // 图的布局。[ default: 'none' ]
                // 'none' 不采用任何布局，使用节点中提供的 x， y 作为节点的位置。
                // 'circular' 采用环形布局;'force' 采用力引导布局.
                // 标记的图形
                //symbol: "path://M19.300,3.300 L253.300,3.300 C262.136,3.300 269.300,10.463 269.300,19.300 L269.300,21.300 C269.300,30.137 262.136,37.300 253.300,37.300 L19.300,37.300 C10.463,37.300 3.300,30.137 3.300,21.300 L3.300,19.300 C3.300,10.463 10.463,3.300 19.300,3.300 Z",
                symbol: 'circle',
                lineStyle: {            // 关系边的公用线条样式。其中 lineStyle.color 支持设置为'source'或者'target'特殊值，此时边会自动取源节点或目标节点的颜色作为自己的颜色。
                    normal: {
                        color: '#000',          // 线的颜色[ default: '#aaa' ]
                        width: 5,               // 线宽[ default: 1 ]
                        type: 'solid',          // 线的类型[ default: solid ]   'dashed'    'dotted'
                        opacity: 1,           // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。[ default: 0.5 ]
                        curveness: 0            // 边的曲度，支持从 0 到 1 的值，值越大曲度越大。[ default: 0 ]
                    }
                },

                label: {                // 关系对象上的标签
                    normal: {
                        show: true,                 // 是否显示标签
                        position: "inside",         // 标签位置:'top''left''right''bottom''inside''insideLeft''insideRight''insideTop''insideBottom''insideTopLeft''insideBottomLeft''insideTopRight''insideBottomRight'
                        textStyle: {                // 文本样式
                            color: '#020102',
                            fontSize: 50   //节点上字体大小
                        }
                    }
                },

                edgeLabel: {                // 连接两个关系对象的线上的标签
                    normal: {
                        show: true,
                        textStyle: {
                            fontSize: 50
                        },
                        formatter: function(param) {        // 标签内容
                            return param.data.category;
                        }
                    }
                },

                data: [{
                    name: News[0], //标题
                    draggable: true,                // 节点是否可拖拽，只在使用力引导布局的时候有用。
                    symbolSize: [400, 400],         // 关系图节点标记的大小，可以设置成诸如 10 这样单一的数字，也可以用数组分开表示宽和高，例如 [20, 10] 表示标记宽为20，高为10。
                    itemStyle: {
                        color: '#38ffda'				// 关系图节点标记的颜色
                    },
                    textStyle: {
                        fontSize:'100px',
                        color: '#020102'          // 图例文字颜色
                    },
                    category: "标题"         // 数据项所在类目的 index。
                }, {
                    name: News[1],  //来源
                    draggable: true,
                    symbolSize: [200, 200],
                    itemStyle: {
                        color: '#0000ff'
                    },
                    category: "来源"
                }, {
                    name: News[2],  //日期
                    draggable: true,
                    symbolSize: [200, 200],
                    itemStyle: {
                        color: '#0000ff'
                    },
                    category: "日期"
                }, {
                    name: News[3],    // name: "生活开销\n1400",  换行   字数
                    draggable: true,
                    symbolSize: [200, 200],
                    itemStyle: {
                        color: '#0000ff'
                    },
                    category: "字数"
                },{
                    name: News[4],    //关键词
                    draggable: true,
                    symbolSize: [200, 200],
                    itemStyle: {
                        color: '#4dff2f'
                    },
                    category: "字数"
                },{
                    name: News[5],    // 摘要
                    draggable: true,
                    symbolSize: [200, 200],
                    itemStyle: {
                        color: '#ff0003'
                    },
                    category: "字数"
                },{
                    name: News[6],    // 短语
                    draggable: true,
                    symbolSize: [200, 200],
                    itemStyle: {
                        color: '#a2ffca'
                    },
                    category: "字数"
                },{
                    name: KeyWordList[0],    // 关键词数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#4dff2f'
                    },
                    category: "关键词"
                },{
                    name: KeyWordList[1],
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#4dff2f'
                    },
                    category: "关键词"
                },{
                    name: KeyWordList[2],    // 关键词数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#4dff2f'
                    },
                    category: "关键词"
                },{
                    name: KeyWordList[3],    // 关键词数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#4dff2f'
                    },
                    category: "关键词"
                },{
                    name: KeyWordList[4],    // 关键词数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#4dff2f'
                    },
                    category: "关键词"
                },{
                    name: KeyWordList[5],    // 关键词数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#4dff2f'
                    },
                    category: "关键词"
                },{
                    name: KeyWordList[6],    // 关键词数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#4dff2f'
                    },
                    category: "关键词"
                },{
                    name: KeyWordList[7],  //关键词数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#4dff2f'
                    },
                    category: "关键词"
                },{
                    name: KeyWordList[8],  //关键词数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#4dff2f'
                    },
                    category: "关键词"
                },{
                    name: KeyWordList[9],    //关键词数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#4dff2f'
                    },
                    category: "关键词"
                },{
                    name: SummaryList[0],   //摘要数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#ff0003'
                    },
                    category: "摘要"
                },{
                    name: SummaryList[1],    //摘要数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#ff0003'
                    },
                    category: "摘要"
                },{
                    name: SummaryList[2],    // 摘要数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#ff0003'
                    },
                    category: "摘要"
                },{
                    name: PhraseList[0],    // 短语数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#a2ffca'
                    },
                    category: "短语"
                },{
                    name: PhraseList[1],    // 短语数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#a2ffca'
                    },
                    category: "短语"
                },{
                    name: PhraseList[2],    // 短语数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#a2ffca'
                    },
                    category: "短语"
                },{
                    name: PhraseList[3],    // 短语数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#a2ffca'
                    },
                    category: "短语"
                },{
                    name: PhraseList[4],    // 短语数据
                    draggable: true,
                    symbolSize: [150, 150],
                    itemStyle: {
                        color: '#a2ffca'
                    },
                    category: "短语"
                }],

                categories: [{              // 节点分类的类目，可选。如果节点有分类的话可以通过 data[i].category 指定每个节点的类目，类目的样式会被应用到节点样式上。图例也可以基于categories名字展现和筛选。
                    name: "新闻标题"            // 类目名称，用于和 legend 对应以及格式化 tooltip 的内容。
                }, {
                    name: "关键词"
                }, {
                    name: "摘要"
                }, {
                    name: "短语"
                }, {
                    name: "字数"
                }],

                links: [{                   // 节点间的关系数据
                    target: News[1],  //来源
                    source: News[0],
                    category: "来源"              // 关系对象连接线上的标签内容
                }, {
                    target: News[2],  //日期
                    source: News[0],   //标题
                    category: "发布日期"
                }, {
                    target: News[3],   //字数
                    source: News[0],
                    category: "字数"
                }, {
                    target: News[4],   //关键词
                    source: News[0],
                    category: "关键词"
                }, {
                    target: News[5],   //摘要
                    source: News[0],
                    category: "摘要"
                }, {
                    target: News[6],   //短语
                    source: News[0],
                    category: "短语"
                },{
                    target: KeyWordList[0],   //关键词
                    source: News[4],
                    category: "关键词"
                },{
                    target: KeyWordList[1],   //关键词
                    source: News[4],
                    category: "关键词"
                },{
                    target: KeyWordList[2],   //关键词
                    source: News[4],
                    category: "关键词"
                },{
                    target: KeyWordList[3],   //关键词
                    source: News[4],
                    category: "关键词"
                },{
                    target: KeyWordList[4],   //关键词
                    source: News[4],
                    category: "关键词"
                },{
                    target: KeyWordList[5],   //关键词
                    source: News[4],
                    category: "关键词"
                },{
                    target: KeyWordList[6],   //关键词
                    source: News[4],
                    category: "关键词"
                },{
                    target: KeyWordList[7],   //关键词
                    source: News[4],
                    category: "关键词"
                },{
                    target: KeyWordList[8],   //关键词
                    source: News[4],
                    category: "关键词"
                },{
                    target: KeyWordList[9],   //关键词
                    source: News[4],
                    category: "关键词"
                },{
                    target: SummaryList[0],   //摘要
                    source: News[5],
                    category: "摘要"
                },{
                    target: SummaryList[1],   //摘要
                    source: News[5],
                    category: "摘要"
                },{
                    target: SummaryList[2],   //摘要
                    source: News[5],
                    category: "摘要"
                },{
                    target: PhraseList[0],   //短语
                    source: News[6],
                    category: "短语"
                },{
                    target: PhraseList[1],   //短语
                    source: News[6],
                    category: "短语"
                },{
                    target: PhraseList[2],   //短语
                    source: News[6],
                    category: "短语"
                },{
                    target: PhraseList[3],   //短语
                    source: News[6],
                    category: "短语"
                },{
                    target: PhraseList[4],   //短语
                    source: News[6],
                    category: "短语"
                }]
            }],

            animationEasingUpdate: "quinticInOut",          // 数据更新动画的缓动效果。[ default: cubicOut ]    "quinticInOut"
            animationDurationUpdate: 100                    // 数据更新动画的时长。[ default: 300 ]
        };
        // 使用刚指定的配置项和数据显示图表
        myChart.setOption(option);
    </script>

</div>
</body>
</html>
