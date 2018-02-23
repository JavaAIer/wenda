package com.zhengrenjie.wenda.controller;

import com.zhengrenjie.wenda.model.Comment;
import com.zhengrenjie.wenda.model.EntityType;
import com.zhengrenjie.wenda.model.HostHolder;
import com.zhengrenjie.wenda.service.CommentService;
import com.zhengrenjie.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);


    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = "/addComment",method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){

        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if(hostHolder.getUser() != null){
                comment.setUserId(hostHolder.getUser().getId());
            }else{
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            commentService.addComment(comment);
        }catch (Exception e){
            logger.error("评论添加失败"+e.getMessage());
        }

        return "redirect:/question/" + String.valueOf(questionId);
    }
}
