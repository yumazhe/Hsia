package com.sharding.test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.Hsia.sharding.utils.ShardingUtil;

public class ShardValueTest {


	public static void main(String[] args){

		testEvenlySharding();
	}

	public static void testDbandTable(){
		String key = "12325542111";
		int dbSize = 2;
		int tbSize = 4;
		String dbName = "sharding";
		String tbName = "t_sharding";

		System.out.println(ShardingUtil.getDBTBIndex(key, dbSize, tbSize, dbName, tbName));

	}

	static AtomicInteger count_0_0 = new AtomicInteger();
	static AtomicInteger count_0_1 = new AtomicInteger();
	static AtomicInteger count_1_0 = new AtomicInteger();
	static AtomicInteger count_1_1 = new AtomicInteger();
	static AtomicInteger other = new AtomicInteger();
	//判断是否均匀分片
	private static void testEvenlySharding(){
		int n = 10000000;
		int dbQuantity = 4;
		int tbQuantity = 1024;
		long start = System.currentTimeMillis();
		for(int i=0; i<n; i++){
			String shardKey = UUID.randomUUID().toString();
			int dbIndex = ShardingUtil.getDataBaseIndex(shardKey, dbQuantity, tbQuantity);
			int tbIndex = ShardingUtil.getTableIndex(shardKey, dbQuantity, tbQuantity);
			if(dbIndex == 0 && tbIndex == 0){
				count_0_0.incrementAndGet();
				continue;
			}
			if(dbIndex == 0 && tbIndex == 1) {
				count_0_1.incrementAndGet();
				continue;
			}
			if(dbIndex == 1 && tbIndex == 0) {
				count_1_0.incrementAndGet();
				continue;
			}
			if(dbIndex == 1 && tbIndex == 1) {
				count_1_1.incrementAndGet();
				continue;
			}

			other.incrementAndGet();
		}
		long end = System.currentTimeMillis();
		System.out.println("执行"+n+"次，总耗时为："+(end - start)+"毫秒,平均耗时为："+(double)(end-start)/n+"毫秒");
		System.out.println("数据库0 表0："+count_0_0.get());
		System.out.println("数据库0 表1："+count_0_1.get());
		System.out.println("数据库1 表0："+count_1_0.get());
		System.out.println("数据库1 表1："+count_1_1.get());
		System.out.println("其他数据："+other.get());
	}
}
