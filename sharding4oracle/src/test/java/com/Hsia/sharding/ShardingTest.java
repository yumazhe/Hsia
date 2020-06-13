package com.Hsia.sharding;

import com.Hsia.sharding.model.User;
import com.Hsia.sharding.oracle.datasources.DataSourceContextHolder;
import com.Hsia.sharding.oracle.route.ShardingRule;
import com.Hsia.sharding.oracle.utils.ShardingUtil;
import com.Hsia.sharding.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: yumazhe
 * @Date: 2019/4/2 11:59
 * @Description:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml", "classpath:dataSource.xml"})
public class ShardingTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private ShardingRule shardingRule;

    @Test
    public void test() {
        final AtomicInteger integer = new AtomicInteger(1);
        int count = 10;
        final CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 1; i <= count; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int id = integer.getAndIncrement();
                    System.out.println(id + "-!!!!!!!!!!!!!");
                    int money = new Random().nextInt();
                    User user = new User(id, money,id%4);
                    if(id < 5){
                        int index = ShardingUtil.getDbIndex(shardingRule, id%shardingRule.getAreaSize());
                        DataSourceContextHolder.setDataSourceIndex(1);
                    }
                    try{
                        userService.save(user);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        countDownLatch.countDown();
                    }
                }
            }).start();

//            User u = userService.query(id);
//            System.out.println(u);

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
