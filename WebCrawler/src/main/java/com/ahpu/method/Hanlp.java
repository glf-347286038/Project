package com.ahpu.method;

import com.hankcs.hanlp.HanLP;

import java.util.ArrayList;
import java.util.List;

public class Hanlp {
    public List<String> getKeyWords(String content) {                         //获取新闻文章关键词以及关键词的词性,获得10个
        List<String> keywordlist = new ArrayList<>();
        List<String> keywordslist1 = HanLP.extractKeyword(content, 10);   //获取关键词10个
        for (String word : keywordslist1) {
            keywordlist.add(String.valueOf(HanLP.segment(word)));            //  将关键词及关键词词性一并存入keywordlist中
        }
        return keywordlist;
    }

    public List<String> getSummary(String content) {    //获取新闻文章3句摘要
        List<String> summarylist = HanLP.extractSummary(content, 3);
        return summarylist;
    }

    public List<String> getPhrase(String content) {      //获取新闻短语5句
        List<String> phraselist = HanLP.extractPhrase(content, 5);
        return phraselist;
    }

}
