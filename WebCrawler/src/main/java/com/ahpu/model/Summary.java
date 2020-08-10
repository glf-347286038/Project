package com.ahpu.model;

public class Summary {
    private Integer id;
    private String summary;
    private Integer news_id;   //关联外键

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getNews_id() {
        return news_id;
    }

    public void setNews_id(Integer new_id) {
        this.news_id = new_id;
    }

}
