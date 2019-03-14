package com.Hsia.sharding.parser;

import com.Hsia.sharding.utils.ShardingUtil;

import junit.framework.TestCase;

/*
 * 判断是否有where条件查询
 */
public class SQLHasWhereColumnTest extends TestCase{

	public void testIHasColumn() {
		String sql = "";

		sql = "SELECT c2,c3 FROM tab WHERE c1=?";
		System.out.println(String.valueOf(ShardingUtil.containWhere(sql)));

		sql = "UPDATE tab SET c2=?,c3=? WHERE c1=?";
		System.out.println(String.valueOf(ShardingUtil.containWhere(sql)));

		sql = "DELETE FROM tab";
		System.out.println(String.valueOf(ShardingUtil.containWhere(sql)));
		sql = "DELETE FROM tab WHERE c1=?";
		System.out.println(String.valueOf(ShardingUtil.containWhere(sql)));

		sql = "INSERT INTO TEST (id) VALUES (1)";
		System.out.println(String.valueOf(ShardingUtil.containWhere(sql)));
	}
}