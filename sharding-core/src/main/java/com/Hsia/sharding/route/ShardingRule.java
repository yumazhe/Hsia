package com.Hsia.sharding.route;

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

	//数据库写库 索引
	private String write_index;
	//数据库读库索引
	private String read_index;
	// 数据库数量
	private Integer dbQuantity;
	// 数据表数量
	private Integer tbQuantity;
	//路由主键
	private Map<String, String> routers;

	public String getWrite_index() {
		return write_index;
	}

	public void setWrite_index(String write_index) {
		this.write_index = write_index;
	}

	public String getRead_index() {
		return read_index;
	}

	public void setRead_index(String read_index) {
		this.read_index = read_index;
	}

	public Integer getDbQuantity() {
		return dbQuantity;
	}

	public void setDbQuantity(Integer dbQuantity) {
		this.dbQuantity = dbQuantity;
	}

	public Integer getTbQuantity() {
		return tbQuantity;
	}

	public void setTbQuantity(Integer tbQuantity) {
		this.tbQuantity = tbQuantity;
	}

	public Map<String, String> getRouters() {
		return routers;
	}

	public void setRouters(Map<String, String> routers) {
		this.routers = routers;
	}
}