package com.Hsia.sharding.parser;

import org.junit.Test;

public class ResolveTableNameTest {

	@Test 
	public void getTabNamebySelect() {
		String sql = "SELECT * FROM userinfo_test WHERE uid = 10000 AND name = xiaolang";
		System.out.println(ResolveTableName.getTableName(sql));
		sql = "SELECT * FROM userinfo_test u WHERE u.uid = 10000 AND u.name = xiaolang";
		System.out.println(ResolveTableName.getTableName(sql));
	}

	@Test 
	public void getTabNamebyInsert() {
		String sql = "INSERT INTO userinfo_test(uid,name) VALUES(10000,xiaolang)";
		System.out.println(ResolveTableName.getTableName(sql));
	}

	public @Test void getTabNamebyUpdate() {
		String sql = "UPDATE userinfo_test SET sex = ? WHERE uid=10000 AND name=xiaolang";
		System.out.println(ResolveTableName.getTableName(sql));
		sql = "UPDATE userinfo_test u SET u.sex = ? WHERE u.uid=10000 AND u.name=xiaolang";
		System.out.println(ResolveTableName.getTableName(sql));
	}

	@Test 
	public void getTabNamebyDelete() {
		String sql = "DELETE FROM userinfo_test WHERE uid=10000 AND name=xiaolang";
		System.out.println(ResolveTableName.getTableName(sql));
		sql = "DELETE FROM userinfo_test u WHERE u.uid=10000 AND u.name=xiaolang";
		System.out.println(ResolveTableName.getTableName(sql));
	}
}