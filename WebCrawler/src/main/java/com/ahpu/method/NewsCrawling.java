package com.ahpu.method;

import com.ahpu.model.News;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//获取新闻内容的方法，要传入一个字符串类型的url地址    返回的是一个News对象
public class NewsCrawling {
    String title = null;   //文章标题
    String content = null; //文章内容
    String source = null;   //文章来源
    Date date = null;
    //将爬取的新闻存入news实体中
    News news = null;
    Connection connection = null;
    Document document = null;

    public News getNews(String url) {
        connection = Jsoup.connect(url);
        try {
            document = connection.get();
            //获得文章的标题
            title = document.title();
            System.out.println("文章标题是" + title);
            //获得发表时间 将时间格式统一为yyyy-MM-dd
            String datestr = document.select("[class=time]").text();
            date = dateFormat(datestr);
            System.out.println("发表时间是：" + date);
            //获取文章来源
//            Elements elementSource = document.getElementsByClass("source");
//            for (Element element2 : elementSource) {
//                source = element2.getElementsByTag("span").text();
//                source = source.substring(3); //将span标签改为a标签 不用再截取了
//                System.out.println("文章来源是：" + source);
//            }
            source = document.getElementsByClass("source").text();
            source=source.substring(3);
            //获取文章内容
            Elements element = document.getElementsByClass("b-container");
            for (Element elementContent : element) {
                content = elementContent.getElementsByTag("article").text();
                System.out.println("文章内容是:" + content);
            }
            news = new News();
            news.setTitle(title);
            news.setContent(content);
            news.setSource(source);
            news.setDate(new java.sql.Date(date.getTime()));    //以1997-05-13的样式存入   后面转换为json格式需要注意
            System.out.println("新闻对象" + news);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return news;
    }


    public Date dateFormat(String dateStr) {  //将字符串类型改为date类型
        String inDateStr = "";
        Date date = null;
        if (dateStr.length() > 10) {       //如果字符串长度大于10,即有精确到小数的数据，则去掉
            inDateStr = dateStr.substring(0, 10);
        } else {
            inDateStr = dateStr;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = new Date(sf.parse(inDateStr).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
