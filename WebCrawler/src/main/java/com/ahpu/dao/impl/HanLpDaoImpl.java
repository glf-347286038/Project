package com.ahpu.dao.impl;

import com.ahpu.dao.HanLpDao;
import com.ahpu.method.DatabaseConnect;
import com.ahpu.model.KeyWords;
import com.ahpu.model.Phrase;
import com.ahpu.model.Summary;

import java.sql.*;
import java.util.*;

public class HanLpDaoImpl implements HanLpDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<String> list = null;
    boolean success = false;
    Savepoint sp = null;

    public void close() {
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

    @Override
    public boolean addKeyWordDao(List<String> listKeyWord, Integer news_id) {
        String sql1 = "insert into keywords(keyword,news_id) VALUES(?,?)";
        try {
            conn = new DatabaseConnect().getconn();
            ps = conn.prepareStatement(sql1);
            for (int i = 0; i < listKeyWord.size(); i++) {  //对关键词集合遍历，给占位符赋值存入数据库
                ps.setString(1, listKeyWord.get(i));
                ps.setInt(2, news_id);
                conn.setAutoCommit(false);
                sp = conn.setSavepoint();
                success = ps.executeUpdate() == 1;
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
                this.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    @Override
    public boolean addSummaryDao(List<String> summary, Integer news_id) {
        try {
            conn = new DatabaseConnect().getconn();
            sp = conn.setSavepoint();
            ps = conn.prepareStatement("INSERT INTO summary(summary,news_id) VALUES(?,?)");
            for (int i = 0; i < summary.size(); i++) {
                ps.setString(1, summary.get(i));
                ps.setInt(2, news_id);
                conn.setAutoCommit(false);
                success = ps.executeUpdate() == 1;
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
                this.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    @Override
    public boolean addKPhraseDao(List<String> phrase, Integer news_id) {
        try {
            conn = new DatabaseConnect().getconn();
            sp = conn.setSavepoint();
            ps = conn.prepareStatement("INSERT INTO phrase(phrase,news_id) VALUES (?,?)");
            for (int i = 0; i < phrase.size(); i++) {
                ps.setString(1, phrase.get(i));
                ps.setInt(2, news_id);
                conn.setAutoCommit(false);
                success = ps.executeUpdate() == 1;
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
                this.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return success;
    }


    /*
        将新闻对象相关的三个hanlp一次全部从数据库中取出
     */
    @Override
    public Map findHanLpDao(Integer news_id) {
        Map hanLpMap = new HashMap();    //将三个list装入Map
        List<KeyWords> listKeyWords = new ArrayList<>();
        List<Summary> listSummary = new ArrayList<>();
        List<Phrase> listPhrase = new ArrayList<>();
        KeyWords keyWords = null;
        Summary summary = null;
        Phrase phrase = null;
        try {
            conn = new DatabaseConnect().getconn();
            ps = conn.prepareStatement("select * from keywords where news_id=?");
            ps.setInt(1, news_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                keyWords = new KeyWords();
                keyWords.setId(rs.getInt("id"));
                keyWords.setKeyword(rs.getString("keyword"));
                keyWords.setNews_id(news_id);
                listKeyWords.add(keyWords);
            }
            ps = conn.prepareStatement("select * from summary where news_id=?");
            ps.setInt(1, news_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                summary = new Summary();
                summary.setId(rs.getInt("id"));
                summary.setSummary(rs.getString("summary"));
                summary.setNews_id(news_id);
                listSummary.add(summary);
            }
            ps = conn.prepareStatement("select * from phrase where news_id=?");
            ps.setInt(1, news_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                phrase = new Phrase();
                phrase.setId(rs.getInt("id"));
                phrase.setPhrase(rs.getString("phrase"));
                phrase.setNews_id(news_id);
                listPhrase.add(phrase);
            }

            hanLpMap.put("keyWords", listKeyWords);
            hanLpMap.put("summary", listSummary);
            hanLpMap.put("phrase", listPhrase);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
        return hanLpMap;
    }

    /*
    通过id删除关键词     */
    @Override
    public boolean deleteKeyWords(int id) {
        conn = new DatabaseConnect().getconn();
        try {
            ps = conn.prepareStatement("DELETE FROM keywords WHERE id=?");
            sp = conn.setSavepoint();
            ps.setInt(1, id);
            conn.setAutoCommit(false);
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
                this.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    /*
    通过id删除摘要
     */
    @Override
    public boolean deleteSummary(int id) {
        conn = new DatabaseConnect().getconn();
        try {
            ps = conn.prepareStatement("DELETE FROM summary WHERE id=?");
            sp = conn.setSavepoint();
            ps.setInt(1, id);
            conn.setAutoCommit(false);
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
                this.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    /*
    通过id删除短语
     */
    @Override
    public boolean deletePhrase(int id) {
        conn = new DatabaseConnect().getconn();
        try {
            ps = conn.prepareStatement("DELETE FROM phrase WHERE id=?");
            sp = conn.setSavepoint();
            ps.setInt(1, id);
            conn.setAutoCommit(false);
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
                this.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    /*
    修改短语、摘要、关键词
     */
    @Override
    public boolean updateKeyWordsDao(String keyWord, int keyWords_id) {
        conn = new DatabaseConnect().getconn();
        try {
            ps = conn.prepareStatement("UPDATE keywords SET keyword=? WHERE id=?");
            sp = conn.setSavepoint();
            ps.setString(1, keyWord);
            ps.setInt(2, keyWords_id);
            conn.setAutoCommit(false);
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
                this.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    @Override
    public boolean updateSummaryDao(String summary, int id) {
        conn = new DatabaseConnect().getconn();
        try {
            ps = conn.prepareStatement("UPDATE summary SET summary=? WHERE id=?");
            sp = conn.setSavepoint();
            ps.setString(1, summary);
            ps.setInt(2, id);
            conn.setAutoCommit(false);
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
                this.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    @Override
    public boolean updatePhraseDao(String phrase, int id) {
        conn = new DatabaseConnect().getconn();
        try {
            ps = conn.prepareStatement("UPDATE phrase SET phrase=? WHERE id=?");
            sp = conn.setSavepoint();
            ps.setString(1, phrase);
            ps.setInt(2, id);
            conn.setAutoCommit(false);
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
                this.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }


}
