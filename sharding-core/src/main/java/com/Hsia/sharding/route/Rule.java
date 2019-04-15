package com.Hsia.sharding.route;

/**
 * 
 * @ClassName: Rule
 * @Description: 路由规则接口
 * @author qsl. email：Hsia_Sharding@163.com
 * @date 2016年3月6日 上午10:12:31
 *
 */
public interface Rule {
	/**
	 * 
	 * @Title: getRouteIndex 
	 * @Description: 获取数据源索引/表索引 
	 * @param @param routeValue
	 * @param @param ruleArray
	 * @param @return    设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	public int getRouteIndex(Object routeValue, int dbQuantity, int tbQuantity);
}