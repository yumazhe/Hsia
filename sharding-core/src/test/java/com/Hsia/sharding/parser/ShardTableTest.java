package com.Hsia.sharding.parser;

import com.Hsia.sharding.utils.ShardingUtil;

public class ShardTableTest {

	
	
	public static void main(String[] args) {
		final int dbsize = 2;
		final int tbsize = 4;
		String orderId = "1202455874222";
		ShardingUtil.printDbTbIndex(orderId, dbsize, tbsize);
	}

}
