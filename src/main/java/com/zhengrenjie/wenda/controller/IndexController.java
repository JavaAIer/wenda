package com.zhengrenjie.wenda.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Controller
public class IndexController {

    @RequestMapping(path = {"/" , "/index"},method = {RequestMethod.GET})
    @ResponseBody
    public String index(){
        return "Hello Nowcoder";
    }

    @RequestMapping(path = {"/fm"},method = {RequestMethod.GET})
    //@ResponseBody
    public String template(Model model){
        model.addAttribute("value1","vvvvvvv1");
        List<String> colors = Arrays.asList(new String[]{"red","green","blue"});
        model.addAttribute("colors",colors);
        return "home";
    }
}
