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
	// 分区数量
	private int areaSize;
	//路由键
	private String routeKey;
	//表的路由键
	private Map<Integer, Integer> routes;

	public int getDbSize() {
		return dbSize;
	}

	public void setDbSize(int dbSize) {
		this.dbSize = dbSize;
	}

	public String getRouteKey() {
		return routeKey;
	}

	public void setRouteKey(String routeKey) {
		this.routeKey = routeKey;
	}

	public Map<Integer, Integer> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<Integer, Integer> routes) {
		this.routes = routes;
	}

	public int getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(int areaSize) {
		this.areaSize = areaSize;
	}
}