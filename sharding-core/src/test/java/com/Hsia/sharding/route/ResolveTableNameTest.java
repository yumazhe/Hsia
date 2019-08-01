package com.Hsia.sharding.route;

import com.Hsia.sharding.parser.ResolveTableName;
import org.junit.Test;

public class ResolveTableNameTest {

	@Test 
	public void getTabNamebySelect() {
		String sql = "SELECT * FROM t_sharding where id = ? and name = ? and age = ?";
		System.out.println(ResolveTableName.getTableName(sql));
		sql = "SELECT * FROM t_sharding u WHERE u.uid = 10000 AND u.name = xiaolang";
		System.out.println(ResolveTableName.getTableName(sql));
	}

	@Test 
	public void getTabNamebyInsert() {
		String sql = "INSERT INTO t_sharding(uid,name) VALUES(10000,xiaolang)";
		System.out.println(ResolveTableName.getTableName(sql));
	}

	public @Test void getTabNamebyUpdate() {
		String sql = "UPDATE t_sharding SET sex = ? WHERE uid=10000 AND name=xiaolang";
		System.out.println(ResolveTableName.getTableName(sql));
		sql = "UPDATE t_sharding u SET u.sex = ? WHERE u.uid=10000 AND u.name=xiaolang";
		System.out.println(ResolveTableName.getTableName(sql));
	}

	@Test 
	public void getTabNamebyDelete() {
		String sql = "DELETE FROM t_sharding WHERE uid=10000 AND name=xiaolang";
		System.out.println(ResolveTableName.getTableName(sql));
		sql = "DELETE FROM t_sharding u WHERE u.uid=10000 AND u.name=xiaolang";
		System.out.println(ResolveTableName.getTableName(sql));
	}
}