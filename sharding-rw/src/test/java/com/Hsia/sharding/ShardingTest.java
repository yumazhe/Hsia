package com.Hsia.sharding;

import com.Hsia.sharding.model.User;
import com.Hsia.sharding.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Auther: yumazhe
 * @Date: 2019/4/2 11:59
 * @Description:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml", "classpath:dataSource-rw.xml"})
public class ShardingTest {

    @Autowired
    private IUserService userService;

    @Test
    public void test(){
        int id = 1;
        int money = 200;
//        User user = new User(id, money);
//        userService.save(user);

        User u = userService.query(id);
        System.out.println(u);


    }
}
