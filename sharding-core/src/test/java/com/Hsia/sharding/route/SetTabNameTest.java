package com.Hsia.sharding.route;

import com.Hsia.sharding.route.tb.SetTableName;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author qsl. email：Hsia_Sharding@163.com
 * @ClassName: SetTabNameTest
 * @Description: 替换路由表
 * @date 2016年3月6日 下午4:15:59
 */
public class SetTabNameTest {
    @Test
    public void setName() {
        String tableName = "user";
        int index = 1;

        String sql = "select user_id, account, user_name, id_card, user_type, user_status, is_delete, create_time from user where user_id = ?";
        Assert.assertEquals("select user_id, account, user_name, id_card, user_type, user_status, is_delete, create_time from user_0001 where user_id = ? ",SetTableName.setRouteTableName(index, tableName, sql));
        sql = "select user_id, account, user_name, id_card, user_type, user_status, is_delete, create_time from user where user_id = ?;";
        Assert.assertEquals("select user_id, account, user_name, id_card, user_type, user_status, is_delete, create_time from user_0001 where user_id = ? ",SetTableName.setRouteTableName(index, tableName, sql));

        sql = "select user_id, account, user_name, id_card, user_type, user_status, is_delete, create_time from user where user_id >= ?";
        Assert.assertEquals("select user_id, account, user_name, id_card, user_type, user_status, is_delete, create_time from user_0001 where user_id >= ? ",SetTableName.setRouteTableName(index, tableName, sql));
        sql = "select user_id, account, user_name, id_card, user_type, user_status, is_delete, create_time from user where user_id >= ?;";
        Assert.assertEquals("select user_id, account, user_name, id_card, user_type, user_status, is_delete, create_time from user_0001 where user_id >= ? ",SetTableName.setRouteTableName(index, tableName, sql));
        sql = "select user_id, account, user_name, id_card, user_type, user_status, is_delete, create_time from user where user_id >= ?; ";
        Assert.assertEquals("select user_id, account, user_name, id_card, user_type, user_status, is_delete, create_time from user_0001 where user_id >= ? ",SetTableName.setRouteTableName(index, tableName, sql));

    }

    @Test
    public void setRouteTableName(){

        int index = 18;
        String name = "t_sharding";

        String sql = "select t_sharding, id, name from    t_sharding";
        System.out.println(SetTableName.setRouteTableName(index,  name,  sql));

        sql = "insert into  t_sharding(id, name) values (?,?)";
        System.out.println(SetTableName.setRouteTableName(index,  name,  sql));

    }
}