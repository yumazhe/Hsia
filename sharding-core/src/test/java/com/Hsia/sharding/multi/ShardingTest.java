package com.Hsia.sharding.multi;

import com.Hsia.sharding.dataSource.DataSourceContextHolder;
import com.Hsia.sharding.multi.model.User;
import com.Hsia.sharding.multi.service.IUserService;
import com.Hsia.sharding.route.ShardingRule;
import com.Hsia.sharding.utils.SnowflakeIdWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

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
        int i = 0;
        int n = 10;
        while (i < n) {

            long id = new SnowflakeIdWorker(0, 0).nextId();
            int money = 200;
            User user = new User(id, money);

            userService.save(user, 1);
            User u2 = userService.query(id, 1);
            System.out.println("分库分表：" + id + "--" + u2);

            long uid = new SnowflakeIdWorker(0, 0).nextId();
            user = new User(uid, money);
            userService.save(user, 0);
            User u1 = userService.query(uid, 0);
            System.out.println("读写分离：" + uid + "--" + u1);

            Thread.sleep(1000);
            i++;
        }


    }

    @Test
    public void update() {
        long id = 568436094710841344l;
        int money = 10110;
        userService.update(id, money);

        User u1 = userService.query(id, 1);
        System.out.println("读写分离：" + id + "--" + u1);
    }

    @Test
    public void update_multi() {
        long id1 = 568436094710841341l;
        long id2 = 568436094710841342l;
        long id3 = 568436094710841343l;
        long id4 = 568436094710841344l;
        long id5 = 568436094710841345l;
        int money = 10110;
        Long[] ids = new Long[]{id1, id2, id3, id4, id5};
        userService.updateMulti(ids, money, 1);

        User u1 = userService.query(id1, 1);
        User u2 = userService.query(id2, 1);
        System.out.println("分库分表：" + id1 + "--" + u1);
        System.out.println("分库分表：" + id2 + "--" + u2);
    }

    @Test
    public void update_signle() {
        long id3 = 568436094710841343l;
        long id4 = 568436094710841344l;
        long id5 = 568436094710841345l;
        int money = 10110;
        Long[] ids = new Long[]{id3, id4, id5};
        userService.updateMulti(ids, money, 0);
    }


    @Autowired
    private DataSourceContextHolder dataSourceHolder;
    @Resource(name = "shardingRule_multi")
    private ShardingRule shardingRule;

    @Test
    public void update_total() {
        int dbSize = shardingRule.getDbQuantity();
        int tbSize = shardingRule.getTbQuantity();
        int count = tbSize / dbSize;

        System.out.println(dbSize + " : " + tbSize + " : " + count);

        int money = 123;
        for (int i = 0; i < dbSize; i++) {
            for (int j = 0; j < count; j++) {
                dataSourceHolder.setDataSourceIndex(i);
                userService.updateMultiFullTableScan(money, j);
            }
        }


    }
}
