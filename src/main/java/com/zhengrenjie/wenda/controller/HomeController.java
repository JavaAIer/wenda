package com.zhengrenjie.wenda.controller;

import com.zhengrenjie.wenda.model.Question;
import com.zhengrenjie.wenda.model.ViewObject;
import com.zhengrenjie.wenda.service.QuestionService;
import com.zhengrenjie.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/user/{userId}"},method = {RequestMethod.GET})
    public String userIndex(Model model, @PathVariable("userId") int userId){
        List<ViewObject> vos = getQuestion(userId,0,10);
        model.addAttribute("vos",vos);
        return "index";
    }

    @RequestMapping(path = {"/" ,"/index"},method = {RequestMethod.GET})
    public String index(Model model){
        List<ViewObject> vos = getQuestion(0,0,10);
        model.addAttribute("vos",vos);
        return "index";
    }

    private List<ViewObject> getQuestion(int userId,int offset,int limit){
        List<Question> questionList = questionService.selectQuestion(userId,offset,limit);
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for(Question question:questionList){
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
