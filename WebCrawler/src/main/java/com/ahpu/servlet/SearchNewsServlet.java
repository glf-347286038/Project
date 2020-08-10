package com.ahpu.servlet;

import com.ahpu.method.Hanlp;
import com.ahpu.method.JsonGetConfig;
import com.ahpu.method.NewsCrawling;
import com.ahpu.model.News;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/SearchNewsServlet")
public class SearchNewsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //设置响应编码格式
        resp.setContentType("text/html;charset=utf-8");
        req.setCharacterEncoding("utf-8");

        News news = null;
        List<String> listKeyWord = null;
        List<String> listSummary = null;
        List<String> listPhrase = null;
        PrintWriter out = resp.getWriter();
        JsonConfig config = null;

        String url = req.getParameter("url");   //获得前端输入的url(后面保存到数据库要用，将其存入session中)
        if (url != null) {
            System.out.println("url为" + url);
            //调用两个方法 获得 爬取文章   并用hanlp分词
            NewsCrawling newsCrawling = new NewsCrawling();
            news = newsCrawling.getNews(url);      //将获得数据存入News对象中 需要将News对象中的所有数据全部显示到前台
            //将news中的content参数传入Hanlp类中的几个方法中
            Hanlp hanlp = new Hanlp();
            listKeyWord = hanlp.getKeyWords(news.getContent());              //关键词  需要显示到前台

            listSummary = hanlp.getSummary(news.getContent());  //3句摘要 需要显示到前台

            listPhrase = hanlp.getPhrase(news.getContent());    //5句短语 需要显示到前台

            config = new JsonGetConfig().getConfig();

            //将news对象引用转换成jsonObject数据格式  让前端ajax调用
            // JSONObject jsonNews = JSONObject.fromObject(news,config);

            //将News对象和3个list同时存入一个新建的list集合中 再将这个list集合转换为jsonArray格式
            List list = new ArrayList<>();
            list.add(news);  //对象
            list.add(listKeyWord);  //关键字  10
            list.add(listSummary);  //摘要    3
            list.add(listPhrase);   //短语    5
            JSONArray jsonArray = JSONArray.fromObject(list, config);  //转换为json格式 然后返回给ajax
            out.print(jsonArray);
            System.out.println("json对象:" + jsonArray);
            out.flush();   //清空当前缓冲区
            out.close();
            //创建session对象
            //如果要将搜索的文章保存到数据库的话 将本SearchNewsservlet的值传递给AddNewsServlet
            // HttpSession session = req.getSession();
            //session.setAttribute("url",url);    //将查询的url通过请求转发发送给NewsServlet
            //req.getRequestDispatcher("/news").forward(req,resp);   //rep已经向前台输出out所以不能转发了
            //  session.removeAttribute("url");   //转发完就删除session属性
        } else {

        }
    }
}
