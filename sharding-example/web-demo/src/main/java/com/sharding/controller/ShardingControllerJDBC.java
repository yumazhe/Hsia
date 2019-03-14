package com.sharding.controller;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Hsia.sharding.utils.ShardingUtil;
import com.sharding.common.ResultObj;
import com.sharding.service.JdbcTemplateService;

@Controller
@RequestMapping(value = "/sharding/jdbc")
public class ShardingControllerJDBC {

	@Autowired
	private JdbcTemplateService jdbcTemplateService;
	private Logger logger = Logger.getLogger(ShardingControllerJDBC.class);

	ResultObj resultObj = null;

	@RequestMapping(value = "/query/{id}")
	@ResponseBody
	private ResultObj query(@PathVariable String id) {
		resultObj = new ResultObj();
		try {
			if(null == id){
				resultObj.setErrcode("1");
				resultObj.setMessage("传入参数为空");
				return resultObj;
			}
			String throwable = jdbcTemplateService.query(id);
			if(null == throwable){
				resultObj.setMessage("没有发现异常信息,请确认输入参数：[id="+id+"]");
				return resultObj;
			}else{
				resultObj.setData(throwable);
				return resultObj;
			}
		} catch (Exception e) {
			logger.error("getDubboIpList failed", e);
			resultObj.setErrcode("1");
			resultObj.setMessage("faild to query list");
		}
		return resultObj;
	}
	@RequestMapping(value = "/insert")
	@ResponseBody
	private ResultObj insert() {
		resultObj = new ResultObj();
		try {
			String id = UUID.randomUUID().toString();
			logger.debug("uuid:-->"+id);
			jdbcTemplateService.insert(id, new Random().nextInt(1000));
			resultObj.setData(id);
			return resultObj;
		} catch (Exception e) {
			e.printStackTrace();
			resultObj.setErrcode("1");
			resultObj.setMessage("faild to query list");
		}
		return resultObj;
	}
	@RequestMapping(value = "/insert_mult")
	@ResponseBody
	private ResultObj insert_mult() {
		resultObj = new ResultObj();
		try {
			Thread thread1 = new Thread(){
				@Override
				public void run() {
					String id = UUID.randomUUID().toString();
					logger.info("uuid:-->"+id);
					jdbcTemplateService.insert(id, new Random().nextInt(1000));
				}
			};
			Thread thread2 = new Thread(){
				@Override
				public void run() {
					String id = UUID.randomUUID().toString();
					logger.info("uuid:-->"+id);
					jdbcTemplateService.insert(id, new Random().nextInt(1000));
				}
			};
			Thread thread3 = new Thread(){
				@Override
				public void run() {
					String id = UUID.randomUUID().toString();
					logger.info("uuid:-->"+id);
					jdbcTemplateService.insert(id, new Random().nextInt(1000));
				}
			};
			
			thread1.start();
			thread2.start();
			thread3.start();
			
			return resultObj;
		} catch (Exception e) {
			logger.error("getDubboIpList failed", e);
			resultObj.setErrcode("1");
			resultObj.setMessage("faild to query list");
		}
		return resultObj;
	}

	AtomicInteger count_0_0 = new AtomicInteger();
	AtomicInteger count_0_1 = new AtomicInteger();
	AtomicInteger count_1_0 = new AtomicInteger();
	AtomicInteger count_1_1 = new AtomicInteger();
	AtomicInteger other = new AtomicInteger();

	@RequestMapping(value = "/balance/{count}")
	@ResponseBody
	private ResultObj balance(@PathVariable Integer count){
		resultObj = new ResultObj();
		try {
			//判断是否均匀分片
			int n = count;
			int dbQuantity = 2;
			int tbQuantity = 4;
			long start = System.currentTimeMillis();
			for(int i=0; i<n; i++){
				String shardKey = i+"";
//				String shardKey = UUID.randomUUID().toString();
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
			
			Map<String, Integer> result = new HashMap<String, Integer>();
			result.put("数据库0 表0 数量", count_0_0.get());
			result.put("数据库0 表1 数量", count_0_1.get());
			result.put("数据库1 表0 数量", count_1_0.get());
			result.put("数据库1 表1 数量", count_1_1.get());
			result.put("other 数量", other.get());

			resultObj.setData(result);
			return resultObj;
		} catch (Exception e) {
			logger.error("getDubboIpList failed", e);
			resultObj.setErrcode("1");
			resultObj.setMessage("faild to query balance");
		}
		return resultObj;
	}
}
