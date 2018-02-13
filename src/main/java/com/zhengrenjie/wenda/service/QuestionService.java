package com.zhengrenjie.wenda.service;

import com.zhengrenjie.wenda.dao.QuestionDAO;
import com.zhengrenjie.wenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public int addQuestion(Question question){
        return questionDAO.addQuestion(question);
    }

    public void deleteQuestion(int id){
        questionDAO.deleteQuestion(id);
    }

    public List<Question> selectQuestion(int userId,int offset,int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }
}
