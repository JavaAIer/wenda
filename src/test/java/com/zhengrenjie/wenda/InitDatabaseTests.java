package com.zhengrenjie.wenda;

/*
*
* Failed
*
* */
import com.zhengrenjie.wenda.dao.UserDAO;
import com.zhengrenjie.wenda.model.User;
import com.zhengrenjie.wenda.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserService userService;

    @Test
    public void contextLoads() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("aa");
            user.setSalt("aa");
            userService.addUser(user);

            //user.setPassword("newpassword");
            //userDAO.updatePassword(user);
        }
    }
}
