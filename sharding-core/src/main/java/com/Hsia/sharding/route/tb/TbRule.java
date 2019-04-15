package com.Hsia.sharding.route.tb;

import com.Hsia.sharding.route.Rule;
import com.Hsia.sharding.utils.ShardingUtil;

/**
 * 
 * @ClassName: TbRule
 * @Description: 解析分表规则后计算分表索引
 * @author qsl. email：Hsia_Sharding@163.com
 * @date 2016年3月7日 上午10:20:36
 *
 */
public class TbRule implements Rule {

	@Override
	public int getRouteIndex(Object routeValue, int dbQuantity, int tbQuantity) {
		return ShardingUtil.getTableIndex(routeValue, dbQuantity, tbQuantity);
	}
}