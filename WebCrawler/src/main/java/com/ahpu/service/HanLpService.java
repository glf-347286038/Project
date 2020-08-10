package com.ahpu.service;

import com.ahpu.model.KeyWords;
import com.ahpu.model.Phrase;
import com.ahpu.model.Summary;

import java.util.List;
import java.util.Map;

public interface HanLpService {
    boolean addKeyWordService(List<String> listKeyWords, Integer news_id);

    boolean addSummaryService(List<String> listSummary, Integer news_id);

    boolean addPhraseService(List<String> listPhrase, Integer news_id);

    boolean deleteKeyWordsService(int keyWordId);

    boolean deleteSummaryService(int summaryId);

    boolean deletePhraseService(int phraseId);

    boolean updateKeyWordService(String keyWord, int keyWordId);

    boolean updateSummaryService(String summary, int summaryId);

    boolean updatePhraseService(String phrase, int phraseId);

    Map findHanLpService(Integer id);

    List<KeyWords> findKeyWordsByNewsId(int news_id);

    List<Summary> findSummaryByNewsId(int news_id);

    List<Phrase> findPhraseByNewsId(int news_id);


}
