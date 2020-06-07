package com.Hsia.sharding.oracle.datasources;

import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: DataSourceContextHolder
 * @Description: 数据源上下文持有
 * @author qsl. email：Hsia_Sharding@163.com
 * @date 2016年3月8日 上午9:53:19
 *
 */

public class DataSourceContextHolder {

	//通过ThreadLocal 每个线程维护一个数据源
	private final static ThreadLocal<Integer> dataSourceContextHolder = new ThreadLocal<Integer>();
	
	//设置数据源索引
	public static void setDataSourceIndex(int index) {
 		dataSourceContextHolder.set(index);
	}

	//获取数据源索引
	public static int getDataSourceIndex() {
		return dataSourceContextHolder.get();
	}
	
	//删除索引
	public void clearDataSourceIndex(){
		dataSourceContextHolder.remove();
	}
}