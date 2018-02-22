package com.zhengrenjie.wenda.service;


import com.zhengrenjie.wenda.dao.LoginTicketDAO;
import com.zhengrenjie.wenda.dao.UserDAO;
import com.zhengrenjie.wenda.model.LoginTicket;
import com.zhengrenjie.wenda.model.User;
import com.zhengrenjie.wenda.util.WendaUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public int addUser(User user){
        return userDAO.addUser(user);
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public void updateUser(User user){
        userDAO.updatePassword(user);
    }

    public String addTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();

        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(3600*24*30 + date.getTime());
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));

        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public LoginTicket getTicket(String ticket){
        return loginTicketDAO.selectByTicket(ticket);
    }

    public void updateTicket(String ticket,int status){
        loginTicketDAO.updateStatus(ticket,status);
    }

    public Map<String,String> register(String username,String password){
        Map<String,String> map = new HashMap<String, String>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msg","用户名已被注册");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));

        userDAO.addUser(user);

        String ticket = addTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }


    public Map<String,String> login(String username,String password){
        Map<String,String> map = new HashMap<String, String>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msg","用户名不存在");
            return map;
        }

        if(!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
        }

        String ticket = addTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public void logout(String ticket){
        LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
        loginTicketDAO.updateStatus(ticket,1);
    }
}
