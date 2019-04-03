package com.Hsia.sharding.route;

/**
 * 
 * @ClassName: Route
 * @Description: 分库分表路由模式接口
 * @author qsl. email：components_yumazhe@163.com
 * @date 2016年3月6日 上午10:09:15
 *
 */
public interface Route {
	
	/**
	 * 
	 * @Title: route 
	 * @Description: 数据路由 
	 * @param @param sql
	 * @param @param params
	 * @param @param indexType
	 * @param @return    设定文件 
	 * @return Object[]    返回类型 
	 * @throws
	 */
	public Object[] route(Object[] params, boolean sqlType, ShardingRule shardingRule);
}