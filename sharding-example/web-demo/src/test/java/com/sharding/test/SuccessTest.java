package com.sharding.test;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sharding.service.JdbcTemplateService;
import com.sharding.service.ShardingService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-mvc.xml", "classpath:spring/databasesource-configeration-mult.xml"})
public class SuccessTest {

	@Autowired
	private ShardingService dataAnalysisService;

	@Test
	public void insertByMybatis(){
		String id = UUID.randomUUID().toString();
		dataAnalysisService.insert(id, new Random().nextInt(1000));
		System.out.println("id: "+id);
	}

	@Test
	public void queryByMybatis(){
		String id = "3b3e5801-92d7-4bd7-9ba4-36ea91171d46";
		String result = dataAnalysisService.query(id);
		System.out.println("id: "+result);
	}

	//批量
	@Test
	public void batchTestByMybatis(){
		int round = 1000;
		AtomicInteger faild = new AtomicInteger(0);
		AtomicInteger success = new AtomicInteger(0);
		for(int index = 0; index<round; index++){
			String id = UUID.randomUUID().toString();
			dataAnalysisService.insert(id, new Random().nextInt(1000));
			String result = dataAnalysisService.query(id);
			if(result==null || result.equals("")){
				faild.incrementAndGet();
			}else{
				success.incrementAndGet();
			}
		}
		System.out.println("执行总次数为："+round);
		System.out.println("错误数为："+faild.get()+"， 错误率为："+faild.get()/round);
		System.out.println("成功数为："+success.get()+"， 成功率为："+(double)success.get()/round);
	}


	//并发
	@Test
	public void concurrentTestByMybatis(){
		final int round = 1000;
		final AtomicInteger faild = new AtomicInteger(0);
		final AtomicInteger success = new AtomicInteger(0);

		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(round);

		for(int index = 0; index<round; index++){
			fixedThreadPool.execute(new Runnable(){
				@Override
				public void run() {
					String id = UUID.randomUUID().toString();
					dataAnalysisService.insert(id, new Random().nextInt(round));
					String result = dataAnalysisService.query(id);
					System.out.println(result+" == " + id);
					if(result==null || result.equals("")){
						System.out.println("失败次数："+faild.incrementAndGet());
					}else{
						System.out.println("成功次数："+success.incrementAndGet());
					}
				}

			});
		}

		
		try {
			System.out.println("按任意键返回");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("执行总次数为："+round);
		System.out.println("错误数为："+faild.get()+"， 错误率为："+faild.get()/round);
		System.out.println("成功数为："+success.get()+"， 成功率为："+(double)success.get()/round);
	}
	
	
	
	
	
	
	
	
	
	@Autowired
	private JdbcTemplateService jdbcTemplateService;

	@Test
	public void insertByJDBC(){
		String id = UUID.randomUUID().toString();
		jdbcTemplateService.insert(id, new Random().nextInt(1000));
		System.out.println("id: "+id);
	}

	@Test
	public void queryByJDBC(){
		String id = "5d713520-1c0d-45ac-b421-b574cba9c3e2";
		String result = jdbcTemplateService.query(id);
		System.out.println("id: "+result);
	}
	//并发
	@Test
	public void concurrentTestByJDBC(){
		final int round = 1000;
		final AtomicInteger faild = new AtomicInteger(0);
		final AtomicInteger success = new AtomicInteger(0);
		
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(round);
		
		for(int index = 0; index<round; index++){
			fixedThreadPool.execute(new Runnable(){
				@Override
				public void run() {
					String id = UUID.randomUUID().toString();
					jdbcTemplateService.insert(id, new Random().nextInt(1000));
					String result = jdbcTemplateService.query(id);
					if(result==null || result.equals("")){
						faild.incrementAndGet();
					}else{
						success.incrementAndGet();
					}
				}
				
			});
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//等待执行完毕
		fixedThreadPool.shutdown();
		
		System.out.println("执行总次数为："+round);
		System.out.println("错误数为："+faild.get()+"， 错误率为："+faild.get()/round);
		System.out.println("成功数为："+success.get()+"， 成功率为："+(double)success.get()/round);
	}

}
