package com.Hsia.sharding.sharding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.Hsia.sharding.utils.ShardingUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShardValueTest {

    private static final Logger logger = LoggerFactory.getLogger(ShardValueTest.class);


    @Test
    public void testDbandTable() {
        int num = 100;
        for (int i = 0; i < num; i++) {
            String key = UUID.randomUUID().toString();
            int dbSize = 1;
            int tbSize = 4;
            String dbName = "sharding";
            String tbName = "t_sharding";

            logger.debug(ShardingUtil.getDBTBIndex(key, dbSize, tbSize, dbName, tbName));

        }

    }

    static AtomicInteger count_0_0 = new AtomicInteger();
    static AtomicInteger count_0_1 = new AtomicInteger();
    static AtomicInteger count_1_0 = new AtomicInteger();
    static AtomicInteger count_1_1 = new AtomicInteger();
    static AtomicInteger count_2_1 = new AtomicInteger();
    static AtomicInteger count_3_0 = new AtomicInteger();
    static AtomicInteger other = new AtomicInteger();
    //判断是否均匀分片

    @Test
    public void name() throws Exception {
    }

    @Test
    public void testEvenlySharding() {
        int n = 100000;
        int dbQuantity = 4;
        int tbQuantity = 64;
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            String shardKey = UUID.randomUUID().toString();
            int dbIndex = ShardingUtil.getDataBaseIndex(shardKey, dbQuantity, tbQuantity);
            int tbIndex = ShardingUtil.getTableIndex(shardKey, dbQuantity, tbQuantity);
            if (dbIndex == 0 && tbIndex == 0) {
                count_0_0.incrementAndGet();
                continue;
            }
            if (dbIndex == 0 && tbIndex == 1) {
                count_0_1.incrementAndGet();
                continue;
            }
            if (dbIndex == 1 && tbIndex == 0) {
                count_1_0.incrementAndGet();
                continue;
            }
            if (dbIndex == 1 && tbIndex == 1) {
                count_1_1.incrementAndGet();
                continue;
            }
            if (dbIndex == 2 && tbIndex == 0) {
                count_2_1.incrementAndGet();
                continue;
            }
            if (dbIndex == 2 && tbIndex == 1) {
                count_3_0.incrementAndGet();
                continue;
            }

            other.incrementAndGet();
        }
        long end = System.currentTimeMillis();
        logger.debug("执行" + n + "次，总耗时为：" + (end - start) + "毫秒,平均耗时为：" + (double) (end - start) / n + "毫秒");
        logger.debug("数据库0 表0：" + count_0_0.get());
        logger.debug("数据库0 表1：" + count_0_1.get());
        logger.debug("数据库1 表0：" + count_1_0.get());
        logger.debug("数据库1 表1：" + count_1_1.get());
        logger.debug("数据库2 表1：" + count_2_1.get());
        logger.debug("数据库3 表0：" + count_3_0.get());
        logger.debug("其他数据：" + other.get());
    }

    @Test
    public void multi() {
        List<Object> values = new ArrayList<>();
        values.add(123456);
        values.add(454512);
        values.add(84563361);
        values.add(74114647);
        values.add(74114647);
        values.add(741134647);
        values.add(7411344647l);
        values.add(7411435647l);
        values.add(7411344647l);
        int dbsize = 2;
        int tbsize = 4;
        Map<String, List<Object>> re
                = ShardingUtil.parse(values, dbsize, tbsize);
        for (Map.Entry<String, List<Object>> entry : re.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }
}
