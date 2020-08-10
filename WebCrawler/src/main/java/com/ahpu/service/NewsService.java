package com.ahpu.service;

import com.ahpu.model.News;

import java.util.List;
import java.util.Map;

public interface NewsService {
    boolean addNewsService(News news);      //通过传入一个对象将新闻添加进数据库

    boolean deleteNewsService(Integer id);  //通过id删除文章

    boolean upDateNewsService(News news);   // 修改文章

    News findByIdNewsService(Integer id);   //通过id查看文章  返回News对象

    Map<String, List<News>> findByTitleNewsService(String title, int startRow, int limit);

    Map<String, List<News>> findAllNewsService(Integer startRow, Integer limit);        //通过传入要查多少数据返回一个News集合

    Integer findNewsIdByTitleService(String title);     //通过文章标题查询文章ID
}
