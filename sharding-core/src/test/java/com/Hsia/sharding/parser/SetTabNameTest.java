package com.Hsia.sharding.parser;

import org.junit.Test;

/**
 * 
 * @ClassName: SetTabNameTest
 * @Description: 替换路由表
 * @author qsl. email：Hsia_Sharding@163.com
 * @date 2016年3月6日 下午4:15:59
 *
 */
public class SetTabNameTest {
	@Test 
	public void setName() {
		final String TABLE_NAME = "t_sharding";
		final String SQL1 = "INSERT INTO " + TABLE_NAME + "(t_sharding_id,sex) VALUES(?,?)";
		System.out.println(SQL1.replaceFirst(TABLE_NAME, TABLE_NAME + "_0010"));
		final String SQL2 = "UPDATE " + TABLE_NAME + " SET sex=? WHERE t_sharding_id = ?";
		System.out.println(SQL2.replaceFirst(TABLE_NAME, TABLE_NAME + "_0010"));
		final String SQL3 = "DELETE FROM " + TABLE_NAME + " WHERE t_sharding_id = ?";
		System.out.println(SQL3.replaceFirst(TABLE_NAME, TABLE_NAME + "_0010"));
		final String SQL4 = "SELECT sex FROM " + TABLE_NAME + " where t_sharding_id = ?";
		System.out.println(SQL4.replaceFirst(TABLE_NAME, TABLE_NAME + "_0010"));
	}
}