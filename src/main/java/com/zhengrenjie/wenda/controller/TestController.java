package com.zhengrenjie.wenda.controller;

import com.zhengrenjie.wenda.dao.QuestionDAO;
import com.zhengrenjie.wenda.model.Question;
import com.zhengrenjie.wenda.model.User;
import com.zhengrenjie.wenda.service.QuestionService;
import com.zhengrenjie.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Random;

@Controller
public class TestController {

    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/test/database/user/insert"},method = {RequestMethod.GET})
    @ResponseBody
    public String userInsert(){
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("aa");
            user.setSalt("aa");
            userService.addUser(user);

        }
        return "user insert success";
    }

    @RequestMapping(path = {"/test/database/user/update"},method = {RequestMethod.GET})
    @ResponseBody
    public String userUpdate(){
        for (int i = 0; i < 11; ++i) {
            User user = userService.getUser(i+1);
            user.setPassword("xx");
            userService.updateUser(user);
        }
        return "user update success";
    }

    @RequestMapping(path = {"/test/database/question/insert"},method = {RequestMethod.GET})
    @ResponseBody
    public String questionInsert(){
        for (int i = 0; i < 11; ++i) {
            Question question = new Question();
            question.setTitle(String.format("Title{%d}",i));
            question.setContent(String.format("lalala{%d}",i));
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*i);
            question.setCreatedDate(date);
            question.setCommentCount(i);
            question.setUserId(i+1);

            questionService.addQuestion(question);

        }
        return "question insert success";
    }

    @RequestMapping(path = {"/test/database/question/delete"},method = {RequestMethod.GET})
    @ResponseBody
    public String questionDelete(){
        for (int i = 0; i < 11; ++i) {

            questionService.deleteQuestion(i+12);

        }
        return "question delete success";
    }

    @RequestMapping(path = {"/test/database/question/select"},method = {RequestMethod.GET})
    @ResponseBody
    public String questionSelect(){
        System.out.println(questionService.selectQuestion(0,0,10));
        return "question select success";
    }

}
