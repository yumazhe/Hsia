package com.Hsia.sharding.route.db;

import com.Hsia.sharding.route.Rule;
import com.Hsia.sharding.utils.ShardingUtil;

/**
 * 
 * @ClassName: DbRule
 * @Description: 解析分库规则后计算数据源索引
 * @author qsl. email：components_yumazhe@163.com
 * @date 2016年3月5日 下午4:01:02
 *
 */
public class DbRule implements Rule{

	@Override
	public int getRouteIndex(Object routeValue, int dbQuantity, int tbQuantity) {
		int dbIndex = ShardingUtil.getDataBaseIndex(routeValue, dbQuantity, tbQuantity);
		return dbIndex;
	}
}