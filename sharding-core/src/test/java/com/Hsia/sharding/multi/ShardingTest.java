package com.Hsia.sharding.multi;

import com.Hsia.sharding.multi.model.User;
import com.Hsia.sharding.multi.service.IUserService;
import com.Hsia.sharding.utils.SnowflakeIdWorker;
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
@ContextConfiguration(locations = {"classpath:spring.xml", "classpath:dataSource-rw.xml", "classpath:dataSource-multi.xml"})
public class ShardingTest {

    @Autowired
    private IUserService userService;

    @Test
    public void test() throws InterruptedException {
        while (true) {

            long id = new SnowflakeIdWorker(0, 0).nextId();
            int money = 200;
            User user = new User(id, money);

            userService.save(user, 1);
            User u2 = userService.query(id, 1);
            System.out.println("分库分表："+id+"--" + u2);

            long uid = new SnowflakeIdWorker(0, 0).nextId();
            user = new User(uid, money);
            userService.save(user, 0);
            User u1 = userService.query(uid, 0);
            System.out.println("读写分离："+uid+"--" + u1);

//            Thread.sleep(1000);
        }


    }
}
