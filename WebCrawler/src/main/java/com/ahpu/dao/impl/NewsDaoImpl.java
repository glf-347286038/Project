package com.ahpu.dao.impl;

import com.ahpu.dao.NewsDao;
import com.ahpu.method.DatabaseConnect;
import com.ahpu.model.News;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsDaoImpl implements NewsDao {

    //    News news = null;
//    Connection conn = null;
//    PreparedStatement ps = null;
//    Statement st = null;
//    ResultSet rs = null;
    Savepoint sp = null;    //初始化事务保存点
    boolean success = false;

    public void close(Connection conn, ResultSet rs, PreparedStatement ps) {   //将接口关闭
        try {
            if (conn != null) {
                conn.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(Connection conn, PreparedStatement ps) {
        try {
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

    /*
         添加新闻
    * */
    @Override
    public boolean addNewsDao(News news) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "insert into news(title,content,source,date)values(?,?,?,?)";
        try {
            conn = new DatabaseConnect().getconn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, news.getTitle());
            ps.setString(2, news.getContent());
            ps.setString(3, news.getSource());
            ps.setDate(4, (java.sql.Date) news.getDate());  //java.util.Data->sql
            conn.setAutoCommit(false);  //取消自动提交
            sp = conn.setSavepoint();   //设置事务保存点，发生错误返回此处
            success = ps.executeUpdate() == 1;  //执行
        } catch (SQLException e) {
            try {
                conn.rollback(sp);  //发生错就回滚
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.commit();//添加文章成功
                System.out.println("添加新闻进数据库成功");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.close(conn, ps);  //关闭连接
        }
        return success;
    }

    /*
        删除新闻
    * */
    @Override
    public boolean deleteNewsDao(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DatabaseConnect().getconn();
            sp = conn.setSavepoint();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("DELETE FROM news WHERE id=? LIMIT 1");
            ps.setInt(1, id);
            success = ps.executeUpdate() == 1;
        } catch (SQLException e) {
            try {
                conn.rollback(sp);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.commit();
                this.close(conn, ps);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    /*
        修改新闻内容
  * */
    @Override
    public boolean updateNewsDao(News news) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DatabaseConnect().getconn();
            ps = conn.prepareStatement("update news set title=?,source=?,date=?,content=? WHERE id=? limit 1");
            ps.setString(1, news.getTitle());
            ps.setString(2, news.getSource());
            java.sql.Date sqlDate = new java.sql.Date(news.getDate().getTime());
            ps.setDate(3, sqlDate);
            ps.setString(4, news.getContent());
            ps.setInt(5, news.getId());
            conn.setAutoCommit(false);
            sp = conn.setSavepoint();
            success = ps.executeUpdate() == 1;
        } catch (SQLException e) {
            try {
                conn.rollback(sp);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.commit();
                this.close(conn, ps);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }


    /*
    通过id查找新闻
    * */
    @Override
    public News findByIdNewsDao(Integer id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        News news = null;
        try {
            conn = new DatabaseConnect().getconn();
            ps = conn.prepareStatement("SELECT * FROM news WHERE id=? LIMIT 1");
            ps.setInt(1, id);
            conn.setAutoCommit(false);
            sp = conn.setSavepoint();
            rs = ps.executeQuery();
            while (rs.next()) {
                news = new News();
                news.setId(id);
                news.setTitle(rs.getString("title"));
                news.setSource(rs.getString("source"));
                news.setDate(rs.getDate("date"));
                news.setContent(rs.getString("content"));
                break;  //保证执行一次即跳出
            }
        } catch (SQLException e) {
            try {
                conn.rollback(sp);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.commit();
                this.close(conn, rs, ps);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return news;
    }

    @Override
    public Map<String, List<News>> findByTitleNewsDao(String title, int startRow, int limit) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        News news = null;
        List<News> listNews = new ArrayList<>();
        Map<String, List<News>> map = new HashMap<>();
        Savepoint sp = null;
        try {   //查询limit条数据
            conn = new DatabaseConnect().getconn();
            ps = conn.prepareStatement("SELECT * FROM news WHERE title LIKE ? LIMIT ?,?");
            ps.setString(1, "%" + title + "%");
            ps.setInt(2, startRow);
            ps.setInt(3, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                news = new News();
                news.setId(rs.getInt("id"));
                news.setTitle(rs.getString("title"));
                news.setSource(rs.getString("source"));
                news.setDate(rs.getDate("date"));
                news.setContent(rs.getString("content"));
                listNews.add(news);
            }
            //数据库共有多少条标题为title的数据
            ps = conn.prepareStatement("SELECT COUNT(*) FROM news WHERE title LIKE ?");
            ps.setString(1, "%" + title + "%");
            rs = ps.executeQuery();
            rs.next();
            Integer rowTotal = rs.getInt(1);
            System.out.println("数据库共有" + rowTotal + "条数据");
            map.put(String.valueOf(rowTotal), listNews);  //将map返回   注意一定要将int转为String
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, rs, ps);
        }
        return map;
    }

    /*
    由于做分页，从第startRow条数据开始查，每次查询出limit条新闻，
    加入listNews中  并将数据库中共有多少条数据一并返回
     */
    @Override
    public Map<String, List<News>> findAllNewsDao(Integer startRow, Integer limit) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        News news = null;
        List<News> listNews = new ArrayList<>();
        Map<String, List<News>> map = new HashMap<>();
        Savepoint sp = null;
        try {   //查询limit条数据
            conn = new DatabaseConnect().getconn();
            ps = conn.prepareStatement("SELECT * from news LIMIT ?,?");
            ps.setInt(1, startRow);
            ps.setInt(2, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                news = new News();
                news.setId(rs.getInt("id"));
                news.setTitle(rs.getString("title"));
                news.setSource(rs.getString("source"));
                news.setDate(rs.getDate("date"));
                news.setContent(rs.getString("content"));
                listNews.add(news);
            }
            //数据库共有多少条数据
            ps = conn.prepareStatement("SELECT COUNT(*) FROM news");
            rs = ps.executeQuery();
            rs.next();
            Integer rowTotal = rs.getInt(1);
            System.out.println("数据库共有" + rowTotal + "条数据");
            map.put(String.valueOf(rowTotal), listNews);  //将map返回   注意一定要将int转为String
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, rs, ps);
        }
        return map;
    }

    /*
        通过新闻标题查看新闻id
     */
    @Override
    public Integer findNewsIdByTitle(String title) {
        Integer news_id = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "select id from news where title=? limit 0,1";
        try {
            conn = new DatabaseConnect().getconn();
            sp = conn.setSavepoint();
            // System.out.println("传过来查询的新闻标题是："+title);
            ps = conn.prepareStatement(sql);
            ps.setString(1, title);
            conn.setAutoCommit(false);
            rs = ps.executeQuery();
            while (rs.next()) {
                news_id = rs.getInt("id");
                System.out.println("通过新闻标题查找id成功");
            }
        } catch (SQLException e) {
            try {
                conn.rollback(sp);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println("通过新闻标题查找id失败");
            e.printStackTrace();
        } finally {
            try {
                conn.commit();
                System.out.println("jdbc查询出来新闻的id是" + news_id);
                this.close(conn, rs, ps);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return news_id;
    }
}
