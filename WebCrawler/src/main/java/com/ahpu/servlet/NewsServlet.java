package com.ahpu.servlet;

import com.ahpu.method.Hanlp;
import com.ahpu.method.JsonGetConfig;
import com.ahpu.method.NewsCrawling;
import com.ahpu.model.KeyWords;
import com.ahpu.model.News;
import com.ahpu.model.Phrase;
import com.ahpu.model.Summary;
import com.ahpu.service.HanLpService;
import com.ahpu.service.NewsService;
import com.ahpu.service.impl.HanLpServiceImpl;
import com.ahpu.service.impl.NewsServiceImpl;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@WebServlet(name = "NewsServlet", urlPatterns = "/news")
public class NewsServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.设置编码格式
        resp.setContentType("text/html;charset=utf-8");
        req.setCharacterEncoding("utf-8");

        News news = null;
        Integer currentPage = 1;     //设置成对象类型 防止空指针异常（当前页 初始为1）
        NewsService newsService = null;
        HanLpService hanLpService = null;
        JsonConfig config = null;
        PrintWriter out = resp.getWriter();

        String action = req.getParameter("action");
        System.out.println("action为：" + action);
        //处理请求
        if (action == null || ("init").equals(action)) {
            currentPage = (Integer) req.getAttribute("currentPage");
            if (currentPage == null) {
                currentPage = 1;
            }
            req.setAttribute("currentPage", currentPage);
            req.getRequestDispatcher("/page/index.jsp").forward(req, resp);
            return;
        }

        switch (action) {
            case "findAll":    //首页  分页开始
                //显示新闻开始  开始分页动作
                //自定义转换器 否则java.sql.date类型无法转换成json数据
                config = new JsonConfig();
                config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    @Override
                    public Object processArrayValue(Object o, JsonConfig jsonConfig) {
                        return simpleDateFormat.format(o);
                    }

                    @Override
                    public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
                        if (o != null) {
                            return simpleDateFormat.format(o);
                        } else {
                            return null;
                        }
                    }
                });
                currentPage = Integer.valueOf(req.getParameter("currentPage"));   //第一步，获取当前页码 初始为1(在前台设置默认为1)
                int limit = 10;     //每页查询十条数据
                int startRow = (currentPage * limit) - limit;    //页开始行数
                System.out.println("当前页数为：" + currentPage + "  页开始行数为：" + startRow);

                newsService = new NewsServiceImpl();
                Map<String, List<News>> map = newsService.findAllNewsService(startRow, limit);  //需要将limit转为String格式（在dao层已经转过了）
                //net.sf.json.JSONException: java.lang.ClassCastException: JSON keys must be strings.
                // JSONArray newsJsonObjct = JSONArray.fromObject(map,config);
                JSONObject newsJsonObject = JSONObject.fromObject(map, config);
                out.print(newsJsonObject);
                out.flush();
                out.close();
                break;

            case "add":   //从主页转发到添加新闻页面
                currentPage = Integer.valueOf(req.getParameter("currentPage"));
                System.out.println(":" + currentPage);
                req.setAttribute("currentPage", currentPage);      //将当前页传递到下个页面  之后返回当前页再传递回来
                req.getRequestDispatcher("page/SearchNew.jsp").forward(req, resp);
                break;

            case "save":        //ajax保存新闻进数据库开始
                String url = req.getParameter("url");
                List<String> listKeyWord = null;
                List<String> listSummary = null;
                List<String> listPhrase = null;

                System.out.println("url为" + url);
                //调用两个方法 获得 爬取文章   并用hanlp分词
                NewsCrawling newsCrawling = new NewsCrawling();
                news = newsCrawling.getNews(url);      //将获得数据存入News对象中 需要将News对象中的所有数据全部显示到前台
                //将news中的content参数传入Hanlp类中的几个方法中
                Hanlp hanlp = new Hanlp();
                listKeyWord = hanlp.getKeyWords(news.getContent());              //关键词10个  需要显示到前台

                listSummary = hanlp.getSummary(news.getContent());  //3句摘要 需要保存进数据库

                listPhrase = hanlp.getPhrase(news.getContent());    //5句短语 需要保存到数据库

                //往数据库中存储 先将爬虫获取的新闻对象存入数据库 存储成功了再存储Hanlp的数据
                newsService = new NewsServiceImpl();
                if (newsService.addNewsService(news)) {    //如果存储新闻的返回结果是true(存储成功) 则存储hanlp的3个list
                    //System.out.println("查id所需要的title:"+news.getTitle());
                    Integer news_id = newsService.findNewsIdByTitleService(news.getTitle()); //新闻在数据库中的id
                    hanLpService = new HanLpServiceImpl();
                    boolean addKeyWord = hanLpService.addKeyWordService(listKeyWord, news_id);   //存关键词
                    boolean addSummary = hanLpService.addSummaryService(listSummary, news_id);   //摘要
                    boolean addKPhrase = hanLpService.addPhraseService(listPhrase, news_id);    //短语
                    if (addKeyWord && addSummary && addKPhrase) {   //三个结果都为ture  通过输出流将结果返回给前台ajax
                        out.print("已成功将本页面数据添加进数据库!");
                        out.flush();
                        out.close();
                    } else { //四个对象只要有一个没有存进数据库，则删掉news那个表
                        newsService.deleteNewsService(news_id);
                    }
                } else {  //如果添加新闻进数据就失败了 那么将结果返回给前台
                    out.print("添加进数据库失败!");
                    out.close();
                }   // 添加新闻进数据库结束
                break;

            case "delete":
                newsService = new NewsServiceImpl();
                int newsDeleteId = Integer.parseInt(req.getParameter("newsId"));
                if (newsService.deleteNewsService(newsDeleteId)) {  //删除执行成功
                    out.print("删除新闻成功");
                } else {
                    out.print("删除失败");
                }
                break;

            case "editNews":
                newsService = new NewsServiceImpl();
                news = new News();
                try {
                    news.setId(Integer.valueOf(req.getParameter("newsId")));
                    news.setTitle(req.getParameter("newsTitle"));
                    news.setSource(req.getParameter("newsSource"));
                    news.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("newsDate")));
                    news.setContent(req.getParameter("newsContent"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (newsService.upDateNewsService(news)) {
                    out.print("修改成功");
                } else {
                    out.print("修改失败");
                }
                break;

            case "findByTitle":
                config = new JsonGetConfig().getConfig();
                String title = req.getParameter("newsTitle");
                currentPage = Integer.valueOf(req.getParameter("currentPage"));   //第一步，获取当前页码 初始为1(在前台设置默认为1)
                int limit02 = 10;     //每页查询十条数据
                int startRow02 = (currentPage * limit02) - limit02;    //页开始行数
                newsService = new NewsServiceImpl();
                Map<String, List<News>> map02 = newsService.findByTitleNewsService(title, startRow02, limit02);  //需要将limit转为String格式（在dao层已经转过了）
                JSONObject newsJsonObject02 = JSONObject.fromObject(map02, config);
                out.print(newsJsonObject02);
                out.flush();
                out.close();
                System.out.println(title + currentPage + startRow02);
                System.out.println(newsJsonObject02);
                break;

            case "lookById":
                int newsId = Integer.parseInt(req.getParameter("newsId"));     //将要查询的新闻id获取
                newsService = new NewsServiceImpl();
                hanLpService = new HanLpServiceImpl();
                req.setAttribute("news", newsService.findByIdNewsService(newsId));
                Map mapHanLp = hanLpService.findHanLpService(newsId);   //要在前端展示，前端遍历Map中的list麻烦 所以在后端将Map中的值取出存入三个list中
                List<KeyWords> keyWordsList = (List<KeyWords>) mapHanLp.get("keyWords");
                List<Summary> summaryList = (List<Summary>) mapHanLp.get("summary");
                List<Phrase> phraseList = (List<Phrase>) mapHanLp.get("phrase");
                System.out.println(keyWordsList);
                System.out.println(summaryList);
                System.out.println(phraseList);
                req.setAttribute("keyWords", keyWordsList);
                req.setAttribute("summary", summaryList);
                req.setAttribute("phrase", phraseList);
                req.getRequestDispatcher("page/NewsDetail.jsp").forward(req, resp);
                break;
            // resp.sendRedirect("page/NewsDetail.jsp");
        }


    }
}
