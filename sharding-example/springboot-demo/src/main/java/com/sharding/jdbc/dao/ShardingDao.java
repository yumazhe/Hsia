package com.sharding.jdbc.dao;

public interface ShardingDao {


	String query(String id);

	void insert(String id, int money);

}
