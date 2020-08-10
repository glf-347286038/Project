package com.ahpu.dao;

import java.util.List;
import java.util.Map;

public interface HanLpDao {
    boolean addKeyWordDao(List<String> keyWord, Integer news_id);

    boolean addSummaryDao(List<String> summary, Integer news_id);

    boolean addKPhraseDao(List<String> phrase, Integer news_id);

    Map findHanLpDao(Integer news_id);

    boolean deleteKeyWords(int id);

    boolean deleteSummary(int id);

    boolean deletePhrase(int id);

    boolean updateKeyWordsDao(String keyWord, int news_id);

    boolean updateSummaryDao(String summary, int news_id);

    boolean updatePhraseDao(String phrase, int news_id);


}
