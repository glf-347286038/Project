package com.ahpu.dao;

import com.ahpu.model.News;

import java.util.List;
import java.util.Map;

public interface NewsDao {
    boolean addNewsDao(News news);          //通过传入对象添加新闻

    boolean deleteNewsDao(int id);      //通过id删除文章

    boolean updateNewsDao(News news);       //通过传过来一个对象修改新闻

    News findByIdNewsDao(Integer id);       //通过id查看新闻

    Map<String, List<News>> findByTitleNewsDao(String name, int startRow, int limit);      //通过新闻标题,返回list是因为可能不止查询到一个

    Map<String, List<News>> findAllNewsDao(Integer startRow, Integer limit);            //查看每一页的新闻 传来一个起始查询的数字

    Integer findNewsIdByTitle(String title);   //通过新闻标题查出新闻id
    //删除多篇文章
}
