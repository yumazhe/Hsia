package com.Hsia.sharding.route;

import com.Hsia.sharding.dataSource.DataSourceContextHolder;
import com.Hsia.sharding.route.tb.SetTableName;
import com.Hsia.sharding.utils.ShardingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Hsia.sharding.parser.ResolveTableName;

/**
 * 
 * @ClassName: ShardingRoute
 * @Description: 数据库分库分表 实现类
 * @author qsl. email：components_yumazhe@163.com
 * @date 2016年3月6日 下午4:42:37
 *
 */
@Component
public class MybatisShardingRoute extends RouteImpl {

	@Autowired
	protected ShardingRule shardingRule;

	@Autowired
	private DataSourceContextHolder dataSourceHolder;

	@Override
	public Object[] route(Object[] params, boolean sqlType) {

		String srcSql = (String) params[0];//源sql

		Object routeValue = params[1];

		/* 解析数据库表名 */
		final String tbName = ResolveTableName.getTableName(srcSql);

		String targetSql = null;

		//数据库数量
		final int dbQuantity = shardingRule.getDbQuantity();
		//表数量
		final int tbQuantity = shardingRule.getTbQuantity();

		Rule dbRule = super.getDbRule();

		int dbIndex = dbRule.getRouteIndex(routeValue, dbQuantity, tbQuantity);

		Rule tbRule = super.getTbRule();
		int tbIndex = tbRule.getRouteIndex(routeValue, dbQuantity, tbQuantity);

		/* 单库多表模式下设定真正的数据库表名 */
		targetSql = SetTableName.setRouteTableName(shardingRule, dbIndex, tbIndex, dbQuantity, tbQuantity, tbName, srcSql);

		final int beginIndex = ShardingUtil.getBeginIndex(shardingRule, sqlType);

		/* 切换数据源索引 */
		dataSourceHolder.setDataSourceIndex((dbIndex + beginIndex));

		return new Object[]{targetSql};
	}
}