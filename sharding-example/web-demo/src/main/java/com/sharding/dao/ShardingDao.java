package com.sharding.dao;

public interface ShardingDao {


	String query(String id);

	void insert(String id, int money);

}
