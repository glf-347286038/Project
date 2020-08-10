package com.ahpu.service.impl;

import com.ahpu.dao.NewsDao;
import com.ahpu.dao.impl.NewsDaoImpl;
import com.ahpu.model.News;
import com.ahpu.service.NewsService;

import java.util.List;
import java.util.Map;

public class NewsServiceImpl implements NewsService {

    News news = null;
    //创建dao层过度对象
    NewsDao newsDao = new NewsDaoImpl();

    @Override
    //添加新闻
    public boolean addNewsService(News news) {
        // String sql = "insert into news(title,source,date,content) value('"+news.getTitle()+"','"+news.getSource()+"','"+(java.sql.Date)news.getDate()+"','"+news.getContent()+"')";
        return newsDao.addNewsDao(news);
    }

    @Override
    //删除新闻
    public boolean deleteNewsService(Integer id) {
        return newsDao.deleteNewsDao(id);
    }

    @Override
    //修改新闻
    public boolean upDateNewsService(News news) {
        return newsDao.updateNewsDao(news);
    }

    @Override
    //通过id查看新闻
    public News findByIdNewsService(Integer id) {
        return newsDao.findByIdNewsDao(id);
    }

    @Override
    public Map<String, List<News>> findByTitleNewsService(String title, int startRow, int limit) {
        return newsDao.findByTitleNewsDao(title, startRow, limit);
    }

    @Override
    //查看一页的新闻
    public Map<String, List<News>> findAllNewsService(Integer startRow, Integer limit) {
        //因为之后要将map转为json格式  map的key类型必须是String格式，所以要转
        return newsDao.findAllNewsDao(startRow, limit);
    }

    /*
        通过新闻标题查询新闻id
     */
    @Override
    public Integer findNewsIdByTitleService(String title) {
        return newsDao.findNewsIdByTitle(title);
    }
}
