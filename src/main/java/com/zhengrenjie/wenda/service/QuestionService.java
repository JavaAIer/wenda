package com.zhengrenjie.wenda.service;

import com.zhengrenjie.wenda.dao.QuestionDAO;
import com.zhengrenjie.wenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {


    @Autowired
    QuestionDAO questionDAO;

    public int addQuestion(Question question){
        //敏感词过滤
        //TODO：
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public void deleteQuestion(int id){
        questionDAO.deleteQuestion(id);
    }

    public List<Question> selectQuestion(int userId,int offset,int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public int updateCommentCount(int id, int count) {
        return questionDAO.updateCommentCount(id, count);
    }
}
