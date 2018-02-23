package com.zhengrenjie.wenda.controller;

import com.zhengrenjie.wenda.model.*;
import com.zhengrenjie.wenda.service.CommentService;
import com.zhengrenjie.wenda.service.QuestionService;
import com.zhengrenjie.wenda.service.UserService;
import com.zhengrenjie.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = "/question/add",method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content){
        try {
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser() == null){
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
            }else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question) > 0){
                return WendaUtil.getJSONString(0);
            }

        }catch (Exception e){
            logger.error("题目增加失败"+e.getMessage());
        }
        return WendaUtil.getJSONString(1,"添加失败");
    }

    @RequestMapping(path = "/question/{qid}",method = {RequestMethod.GET})
    public String questionDetail(Model model,@PathVariable("qid") int qid){
        Question question = questionService.selectById(qid);
        List<ViewObject> comments = getComment(qid,EntityType.ENTITY_QUESTION);
        model.addAttribute("comments",comments);
        model.addAttribute("question",question);
        model.addAttribute("user",userService.getUser(question.getUserId()));
        return "detail";
    }

    private List<ViewObject> getComment(int entityId,int entityType){
        List<ViewObject> vos = new ArrayList<ViewObject>();
        List<Comment> comments = commentService.getCommentByEntity(entityId, entityType);

        for(Comment comment:comments){
            ViewObject vo = new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
