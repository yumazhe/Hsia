package com.Hsia.sharding.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: HorizontalFacadeFactory
 * @Description: 水平分库,水平分片模式的工厂类
 * @author qsl. email：Hsia_Sharding@163.com
 * @date 2016年3月6日 下午4:34:29
 *
 */
@Component
public class MybatisShardingRouteFactory implements RouteFactory {
	@Autowired
	private MybatisShardingRoute mybatisShardingRoute;
	@Override
	public Route getRoute() {
		return mybatisShardingRoute;
	}
}