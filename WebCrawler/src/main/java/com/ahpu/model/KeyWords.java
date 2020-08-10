package com.ahpu.model;

public class KeyWords {
    private Integer id;         //id编号 自增
    private String keyword;     //文章关键词
    private Integer news_id;    //关联外键

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getNews_id() {
        return news_id;
    }

    public void setNews_id(Integer news_id) {
        this.news_id = news_id;
    }
}
