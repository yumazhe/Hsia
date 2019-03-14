package com.Hsia.sharding.parser;

import org.junit.Test;

/**
 * @ClassName: StringTest
 * @Description:
 * @author qsl. email：components_yumazhe@163.com
 * @date 2016年3月5日 下午9:20:54
 */
public class StringTest {

    @Test
    public void sub(){
        String shardingKey = "'bae9d207-aadf-4790-818f-28c91db16503'";
        System.out.println(shardingKey);
        if ((shardingKey instanceof String)
                && (shardingKey.toString().length() > 0)
                && (shardingKey.toString().startsWith("'"))
                && (shardingKey.toString().endsWith("'"))) {
            String key = (String) shardingKey;
            System.out.println(key.length());
            key = key.substring(1, key.length() - 1);
            shardingKey = key;
        }
        System.out.println(shardingKey);
    }
}
