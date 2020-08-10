package com.ahpu.model;

import java.util.Date;

public class News {
    private Integer id;    //id编号自增    设置对象类型 而不是int类型 int类型会初始化 对象类型是null
    private String title;  //新闻标题
    private String content;  //新闻内容
    private Date date;       //新闻发布时间
    private String source;   //文章来源

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
