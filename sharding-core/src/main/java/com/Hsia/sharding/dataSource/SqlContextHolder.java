package com.Hsia.sharding.dataSource;

import org.apache.log4j.Logger;

/**
 * 
 * @ClassName: sqlContextHolder
 * @Description: 数据源上下文持有
 * @author qsl. email：components_yumazhe@163.com
 * @date 2016年3月8日 上午9:53:19
 *
 */

public class SqlContextHolder {

	private static Logger logger = Logger.getLogger(SqlContextHolder.class);

	static SqlContextHolder SqlContextHolder = null;

	private static final Object locker = new Object();

	//single instance
	public static SqlContextHolder getInstance(){
		if(SqlContextHolder == null){
			synchronized (locker) {
				if(SqlContextHolder == null){
					SqlContextHolder = new SqlContextHolder();
				}
			}
		}
		logger.info("the SqlContextHolder is: " + SqlContextHolder);
		return SqlContextHolder;
	}

	//通过ThreadLocal 每个线程维护一个sql语句
	private final static ThreadLocal<String> sqlContextHolder = new ThreadLocal<String>();

	//设置数据源索引
	public void setExecuteSql(String sql) {
		sqlContextHolder.set(sql);
	}

	//获取数据源索引
	public String getExecuteSql() {
		return sqlContextHolder.get();
	}

	//删除索引
	public void clearExecuteSql(){
		sqlContextHolder.remove();
	}
}