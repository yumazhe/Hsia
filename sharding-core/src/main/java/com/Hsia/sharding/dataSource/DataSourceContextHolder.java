package com.Hsia.sharding.dataSource;

import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: DataSourceContextHolder
 * @Description: 数据源上下文持有
 * @author qsl. email：components_yumazhe@163.com
 * @date 2016年3月8日 上午9:53:19
 *
 */

@Component
public class DataSourceContextHolder {

	//通过ThreadLocal 每个线程维护一个数据源
	private final static ThreadLocal<Integer> dataSourceContextHolder = new ThreadLocal<Integer>();
	
	//设置数据源索引
	public void setDataSourceIndex(int index) {
 		dataSourceContextHolder.set(index);
	}

	//获取数据源索引
	public int getDataSourceIndex() {
		return dataSourceContextHolder.get();
	}
	
	//删除索引
	public void clearDataSourceIndex(){
		dataSourceContextHolder.remove();
	}
}