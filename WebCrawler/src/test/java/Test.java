import com.ahpu.model.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test {

    @org.junit.Test
    public void getconn() {   //测试是否成功连接数据库
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/ahpu";
        String user = "root";
        String password = "root";
        Connection conn = null;

        try {
            Class.forName(driver);    //加载jdbc驱动
            conn = DriverManager.getConnection(url, user, password); //获取数据库连接
            conn.setAutoCommit(false);
            System.out.println(conn);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    @org.junit.Test
    public void testSelect() {    //测试查询news 表的数据
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/ahpu";
        String user = "root";
        String password = "root";

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;

        String sql = "select id from news where title=? limit 0,1";
        String title = "世卫组织：不赞成采取旅行或贸易禁令，相信中国！";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            ps = conn.prepareStatement(sql);
            ps.setString(1, title);
            rs = ps.executeQuery();
            while (rs.next()) {
//                String title = rs.getString("title");
//
//                String content = rs.getString("content");
//                System.out.println(title + "  " +content);
                System.out.println(rs.getInt("id"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    @org.junit.Test
    public void testAddArticle() {

        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/ahpu";
        String user = "root";
        String password = "root";

        PreparedStatement ps = null;
        Connection conn = null;
        Savepoint sp = null;  //初始化事务保存点
        boolean success = false;  //初始化返回对象为false
        //添加文章开始
        String title = null;
        String content = null;
        String url02 = "https://new.qq.com/omn/20200103/20200103A0575600.html";
        org.jsoup.Connection connection = Jsoup.connect(url02);
        Document document = null;
        try {
            document = connection.get();
            title = document.getElementsByTag("h1").first().text();  //标题
            System.out.println("新闻标题是：" + title);
            //获取内容
            content = document.select("[class=content-article]").text();
            System.out.println("新闻内容是：" + content);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String sql = "insert into news(title,content)values(?,?)";

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            //创建sql命令对象
            ps = conn.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, content);
            conn.setAutoCommit(false);   //取消自动提交事务
            sp = conn.setSavepoint();    //设置事务保存点，发生错误返回此处
            success = ps.executeUpdate() == 1;
            System.out.println("插入数据是否成功" + success);
        } catch (ClassNotFoundException | SQLException e) {
            try {
                conn.rollback(sp);  //发生错误则返回事务保存点
                System.out.println("插入文章失败");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.commit();  //提交事务
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @org.junit.Test
    public void deleteById() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/ahpu";
        String user = "root";
        String password = "root";

        PreparedStatement ps = null;
        Connection conn = null;
        Savepoint sp = null;  //初始化事务保存点
        boolean success = false;  //初始化返回对象为false

        String sql = "delete from news where id=?";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            ps = conn.prepareStatement(sql);
            ps.setString(1, "3");
            conn.setAutoCommit(false);
            sp = conn.setSavepoint();
            success = ps.executeUpdate() == 1;
        } catch (ClassNotFoundException | SQLException e) {
            try {
                conn.rollback(sp);
                System.out.println("删除失败");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.commit();  //提交事务
                System.out.println("删除是否成功" + success);
                if (conn != null) {
                    conn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }


    @org.junit.Test
    public void testList() {
        News n = new News();
        n.setId(1);
        n.setTitle("高");
        n.setContent("是帅哥");
        List<News> list = new ArrayList<>();
        list.add(n);

        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list1.add(list.get(i).getTitle());
            System.out.println(list.get(i).getTitle());
            //list.add(list.get(i).getTitle());
        }
        for (int i = 0; i < list1.size(); i++) {
            System.out.println(list1.get(i));
        }


    }


    @org.junit.Test
    public void testlist() {
        List list = new ArrayList();
        Map map = new HashMap();
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();
        list1.add("我");
        list1.add("是");
        list1.add("高");
        list1.add("凌");
        list1.add("峰");
        list2.add("你");
        list2.add("是");
        list2.add("李");
        list2.add("惠");
        list2.add("敏");

        map.put("list1", list1);
        map.put("list2", list2);
        map.put("0", list3);

        list.add(list1);
        list.add(list2);
        System.out.println(list.get(1));

        List list4 = new ArrayList();
        // list3 = (List) map.get("list1");
        System.out.println(map.get("list1").toString());
//        if(((List)map.get("list3")).size()!=0){  //可以
        if (map.containsKey("0")) {
//            System.out.println(((List)map.get("list3")).size());
            System.out.println("map为空");
        } else {
            System.out.println("map不为空");
        }
        System.out.println("list3" + map.get("list3"));
    }

    @org.junit.Test
    public void te() {
        String s = "2011-01-02";
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        java.util.Date  udt = null;
//        try {
//            udt = df.parse(s);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        java.sql.Date sdt = new java.sql.Date(udt.getTime());
//        System.out.println("util:"+udt+"sdt:"+sdt);
        try {
            java.util.Date da = new SimpleDateFormat("yyyy-MM-dd").parse(s);
            System.out.println(da);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @org.junit.Test
    public void sub(){
        String url = "https://world.huanqiu.com/article/3xBawnqxqbg";
        org.jsoup.Connection connection = Jsoup.connect(url);
        Document document = null;
        try {
            document = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elementSource = document.getElementsByClass("source");

        for (Element element2 : elementSource) {
            String sourceBefor = element2.getElementsByTag("a").text();
            System.out.println(sourceBefor);
            String source = sourceBefor.substring(3);
            System.out.println("文章来源是：" + source);
        }
//        Element element =
//        String source = elementSource
    }

    @org.junit.Test
    public void huanhang(){
        String s  = "666" + "\n"+"555";
        System.out.println(s);
    }

}
