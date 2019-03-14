package com.Hsia.sharding.route.tb;

import com.Hsia.sharding.utils.ShardingUtil;
import org.apache.log4j.Logger;

import com.Hsia.sharding.route.ShardingRule;

/**
 * 
 * @ClassName: SetTableName
 * @Description: 数据路由前重设数据库表名,比如通用的表名为tab,那么重设后为tab_0000
 * @author qsl. email：components_yumazhe@163.com
 * @date 2016年3月5日 下午9:50:18
 *
 */
public class SetTableName {

	private static Logger logger = Logger.getLogger(SetTableName.class);

	/**
	 * 
	 * @Title: setName 
	 * @Description: 多库多表模式下重设真正的数据库表名 
	 * @param @param shardingConfigInfo 分库分表配置信息
	 * @param @param dbIndex 数据源索引
	 * @param @param tbIndex 数据库表索引
	 * @param @param dbSize 配置文件中数据库的数量
	 * @param @param tbSize 配置文件中数据库表的数量
	 * @param @param tbName 数据库通用表名
	 * @param @param srcSql
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public static String setRouteTableName(ShardingRule shardingRule, int dbIndex, int tbIndex, int dbSize, int tbSize,	String tbName, String srcSql) {
		final String newTableName = ShardingUtil.getRouteName(tbIndex, tbName);
		String targetSql = srcSql.replaceFirst(tbName, newTableName);
		logger.debug("the target sql is : --> "+targetSql);
		return targetSql;
	}

	/**
	 * 
	 * @Title: setName 
	 * @Description: 单库多表模式下设定真正的数据库表名 
	 * @param @param shardingConfigInfo 分库分表配置信息
	 * @param @param tbIndex 数据库表索引
	 * @param @param tbName 数据库通用表名
	 * @param @param sql
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	protected static String setRouteTableName(int tbIndex, String tbName, String sql) {

		final String newTableName = ShardingUtil.getRouteName(tbIndex, tbName);

		String targetSql = sql.replaceFirst(tbName, newTableName);
		logger.debug("单库多表模式下，用于执行的正式sql为：--> "+targetSql);
		return targetSql;
	}
}