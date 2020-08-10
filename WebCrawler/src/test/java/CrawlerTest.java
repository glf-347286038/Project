import com.ahpu.model.News;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrawlerTest {
    String title = null;   //文章标题
    String content = null; //文章内容
    String source = null;   //文章来源
    Date date = null;
    //将爬取的新闻存入news实体中
    News news = null;
    Connection connection = null;
    Document document = null;

    @Test
    public void getNews() {
        String url = "https://mil.huanqiu.com/article/3wlwGrsdfbn";
        connection = Jsoup.connect(url);
        try {
            document = connection.get();
            //获得文章的标题
            title = document.title();
            System.out.println("文章标题是" + title);
            //获得发表时间 将时间格式统一为yyyy-MM-dd
            String datestr = document.select("[class=time]").text();
            //date = dateFormat(datestr);
            System.out.println("发表时间是：" + datestr);
            //获取文章来源
            Elements elementSource = document.getElementsByClass("source");
            for (Element element2 : elementSource) {
                String sourceBefor = element2.getElementsByTag("span").text();
                source = sourceBefor.substring(7, sourceBefor.length());      //对数据进行处理   因为获取到的数据是 来源：环球网 环球网
                System.out.println("文章来源是：" + source + source.length());
            }

            //获取文章内容
            Elements element = document.getElementsByClass("b-container");
            for (Element elementContent : element) {
                content = elementContent.getElementsByTag("article").text();
                System.out.println("文章内容是:" + content);
            }
            System.out.println(title + datestr + source + content);
            // news.setTitle(title);
            //  news.setContent(content);
            // news.setSource(source);
            //  news.setDate(new java.sql.Date(date.getTime()));    //以1997-05-13的样式存入   后面转换为json格式需要注意
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    @Test
    public void getGood() {
        String url = "https://item.jd.com/100011513372.html";
        connection = Jsoup.connect(url);
        try {
            document = connection.get();

//            System.out.println("文章标题是" + title);
//            获得发表时间 将时间格式统一为yyyy-MM-dd
//            String price = document.select("[class=p-price]").text();
//            System.out.println("价格：" + price);
            //获取文章来源
//            Elements elementSource = document.getElementsByClass("source");
//            for (Element element2 : elementSource) {
//                source = element2.getElementsByTag("a").text();
////                source = sourceBefor.substring(3); 将span标签改为a标签 不用再截取了
//                System.out.println("文章来源是：" + source);
//            }
//            //获取文章内容
//            Elements element = document.getElementsByClass("dd");
//            for (Element elementContent : element) {
//                content = elementContent.getElementsByTag("a").text();
//                System.out.println(content);
//            }
//            Elements elemen02 = document.getElementsByClass("li p-choose");
//            for(Element element1:elemen02){
//                String colorType = element1.getElementsByTag("a").text();
//                System.out.println(colorType);
//            }
            Elements elemen02 = document.select("#choose-attr-1");
            for(Element element1:elemen02){
                String colorType = element1.getElementsByTag("a").text();
                System.out.println(colorType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
