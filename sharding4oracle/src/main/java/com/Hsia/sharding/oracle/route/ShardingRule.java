package com.Hsia.sharding.oracle.route;

import java.util.Map;

/**
 * 
 * @ClassName: ShardingRule
 * @Description: 分库分表规则
 * @author qsl. email：Hsia_Sharding@163.com
 * @date 2016年3月5日 下午9:25:57
 *
 */
public class ShardingRule {

	// 库数量
	private int dbSize;
	// 表数量
	private int tbSize;
	//表的路由键
	private Map<String, String> routes;

	public int getDbSize() {
		return dbSize;
	}

	public void setDbSize(int dbSize) {
		this.dbSize = dbSize;
	}

	public int getTbSize() {
		return tbSize;
	}

	public void setTbSize(int tbSize) {
		this.tbSize = tbSize;
	}

	public Map<String, String> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<String, String> routes) {
		this.routes = routes;
	}
}