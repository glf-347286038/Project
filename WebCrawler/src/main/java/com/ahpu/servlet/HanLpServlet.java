package com.ahpu.servlet;

import com.ahpu.model.KeyWords;
import com.ahpu.model.Phrase;
import com.ahpu.model.Summary;
import com.ahpu.service.HanLpService;
import com.ahpu.service.impl.HanLpServiceImpl;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HanLpServlet",urlPatterns = "/hanLp")
public class HanLpServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.设置编码格式
        resp.setContentType("text/html;charset=utf-8");
        req.setCharacterEncoding("utf-8");

        KeyWords keyWords = null;
        Summary summary = null;
        Phrase phrase = null;
        Integer newsId = null;
        HanLpService hanLpService = new HanLpServiceImpl();
        PrintWriter out = resp.getWriter();
        JSONArray jsonArray = null;

        String action = req.getParameter("action");
        System.out.println("action为：" + action);

        switch (action){
            case"editKeyWords":
                List<KeyWords> listKeyWords = hanLpService.findKeyWordsByNewsId(Integer.parseInt(req.getParameter("newsId")));
                req.setAttribute("newsId",Integer.parseInt(req.getParameter("newsId")));
                req.setAttribute("keyWords",listKeyWords);
                req.getRequestDispatcher("page/KeyWords.jsp").forward(req,resp);
                System.out.println("跳转到修改关键词界面"+req.getParameter("newsId"));
                break;
            case "addKeyWords":
                List<String> listKeyWords02 = new ArrayList<>();
                listKeyWords02.add(req.getParameter("keyWords"));
                newsId = Integer.parseInt(req.getParameter("newsId"));
                if(hanLpService.addKeyWordService(listKeyWords02,newsId)){
                    out.print("添加关键词成功");
                }else {
                    out.print("添加关键词失败");
                }
                break;
            case"deleteKeyWords":
                int deleteKeyWordId = Integer.parseInt(req.getParameter("keyWordsId"));
                if(hanLpService.deleteKeyWordsService(deleteKeyWordId)){   //删除成功
                    out.print("true");
                }else{
                    out.print("删除关键词失败");
                }
                break;
            case"updateKeyWords":
                String updateKeyWords= req.getParameter("keyWords");
                int updateKeyWordsId = Integer.parseInt(req.getParameter("keyWordsId"));
                if(hanLpService.updateKeyWordService(updateKeyWords,updateKeyWordsId)){
                    out.print("修改成功");
                }else{
                    out.print("修改失败");
                }
                break;

            case"editSummary":
                List<Summary> listSummary = hanLpService.findSummaryByNewsId(Integer.parseInt(req.getParameter("newsId")));
                req.setAttribute("newsId",Integer.parseInt(req.getParameter("newsId")));
                req.setAttribute("summary",listSummary);
                req.getRequestDispatcher("page/Summary.jsp").forward(req,resp);
                System.out.println("跳转到修改摘要界面"+req.getParameter("newsId"));
                break;
            case"addSummary":
                List<String> listSummary02 = new ArrayList<>();
                listSummary02.add(req.getParameter("summary"));
                newsId = Integer.parseInt(req.getParameter("newsId"));
                if(hanLpService.addSummaryService(listSummary02,newsId)){
                    out.print("添加摘要成功");
                }else {
                    out.print("添加摘要失败");
                }
                break;
            case"deleteSummary":
                int deleteSummaryId = Integer.parseInt(req.getParameter("summaryId"));
                if(hanLpService.deleteSummaryService(deleteSummaryId)){   //删除成功
                    out.print("true");
                }else{
                    out.print("删除摘要失败");
                }
                break;
            case"updateSummary":
                String updateSummary= req.getParameter("summary");
                int updateSummaryId = Integer.parseInt(req.getParameter("summaryId"));
                if(hanLpService.updateSummaryService(updateSummary,updateSummaryId)){
                    out.print("修改成功");
                }else{
                    out.print("修改失败");
                }
                break;

            case"editPhrase":
                List<Phrase> listPhrase= hanLpService.findPhraseByNewsId(Integer.parseInt(req.getParameter("newsId")));
                req.setAttribute("newsId",Integer.parseInt(req.getParameter("newsId")));
                req.setAttribute("phrase",listPhrase);
                req.getRequestDispatcher("page/Phrase.jsp").forward(req,resp);
                System.out.println("跳转到修改摘要界面"+req.getParameter("newsId"));
                break;
            case"addPhrase":
                List<String> listPhrase02 = new ArrayList<>();
                listPhrase02.add(req.getParameter("phrase"));
                newsId = Integer.parseInt(req.getParameter("newsId"));
                if(hanLpService.addPhraseService(listPhrase02,newsId)){
                    out.print("添加摘要成功");
                }else {
                    out.print("添加摘要失败");
                }
                break;
            case"deletePhrase":
                int deletePhraseId = Integer.parseInt(req.getParameter("phraseId"));
                if(hanLpService.deletePhraseService(deletePhraseId)){   //删除成功
                    out.print("true");
                }else{
                    out.print("删除短语失败");
                }
                break;
            case"updatePhrase":
                String updatePhrase= req.getParameter("phrase");
                int updatePhraseId = Integer.parseInt(req.getParameter("phraseId"));
                if(hanLpService.updatePhraseService(updatePhrase,updatePhraseId)){
                    out.print("修改成功");
                }else{
                    out.print("修改失败");
                }
                break;
        }
    }
}
