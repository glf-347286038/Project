package com.ahpu.service.impl;

import com.ahpu.dao.HanLpDao;
import com.ahpu.dao.impl.HanLpDaoImpl;
import com.ahpu.model.KeyWords;
import com.ahpu.model.Phrase;
import com.ahpu.model.Summary;
import com.ahpu.service.HanLpService;

import java.util.List;
import java.util.Map;

public class HanLpServiceImpl implements HanLpService {
    List<String> listKeyWord = null;
    List<String> listSummary = null;
    List<String> listPhrase = null;
    HanLpDao hanLpDao = new HanLpDaoImpl();

    @Override
    public boolean addKeyWordService(List<String> listKeyWords, Integer news_id) {
//        listKeyWord = (List<String>) HanLpMap.get("listKeyWord");
        return hanLpDao.addKeyWordDao(listKeyWords, news_id);
    }

    @Override
    public boolean addSummaryService(List<String> listSummary, Integer news_id) {
        return hanLpDao.addSummaryDao(listSummary, news_id);
    }

    @Override
    public boolean addPhraseService(List<String> listPhrase, Integer news_id) {
        return hanLpDao.addKPhraseDao(listPhrase, news_id);
    }

    /*
    删除关键词、摘要、短语
     */
    @Override
    public boolean deleteKeyWordsService(int keyWordId) {
        return hanLpDao.deleteKeyWords(keyWordId);
    }

    @Override
    public boolean deleteSummaryService(int summaryId) {
        return hanLpDao.deleteSummary(summaryId);
    }

    @Override
    public boolean deletePhraseService(int phraseId) {
        return hanLpDao.deletePhrase(phraseId);
    }

    /*
    修改关键词、摘要、短语
     */
    @Override
    public boolean updateKeyWordService(String keyWord, int keyWordId) {
        return hanLpDao.updateKeyWordsDao(keyWord, keyWordId);
    }

    @Override
    public boolean updateSummaryService(String summary, int summaryId) {
        return hanLpDao.updateSummaryDao(summary, summaryId);
    }

    @Override
    public boolean updatePhraseService(String phrase, int phraseId) {
        return hanLpDao.updatePhraseDao(phrase, phraseId);
    }

    /*
    查找关键词、摘要、短语
     */
    @Override
    public Map findHanLpService(Integer id) {   //一次性全部查询出来
        return hanLpDao.findHanLpDao(id);
    }

    @Override
    public List<KeyWords> findKeyWordsByNewsId(int news_id) {
        List<KeyWords> list = (List<KeyWords>) hanLpDao.findHanLpDao(news_id).get("keyWords");
        return list;
    }

    @Override
    public List<Summary> findSummaryByNewsId(int news_id) {
        List<Summary> list = (List<Summary>) hanLpDao.findHanLpDao(news_id).get("summary");
        return list;
    }

    @Override
    public List<Phrase> findPhraseByNewsId(int news_id) {
        List<Phrase> list = (List<Phrase>) hanLpDao.findHanLpDao(news_id).get("phrase");
        return list;
    }

}
