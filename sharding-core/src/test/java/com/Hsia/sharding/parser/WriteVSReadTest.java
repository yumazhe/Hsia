package com.Hsia.sharding.parser;

import com.Hsia.sharding.route.ShardingRule;
import com.Hsia.sharding.utils.ShardingUtil;
import org.junit.Test;

public class WriteVSReadTest {
    ShardingRule shardingRule = new ShardingRule();

    @Test
    public void parseWR() {
        System.out.println(String.valueOf(ShardingUtil.getBeginIndex(shardingRule, true)));
        System.out.println(String.valueOf(ShardingUtil.getBeginIndex(shardingRule, false)));
    }
}
