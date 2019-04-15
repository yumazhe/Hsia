package com.Hsia.sharding.route.db;

import com.Hsia.sharding.route.Rule;
import com.Hsia.sharding.route.RuleFactory;

/**
 * 
 * @ClassName: DbRuleFactory
 * @Description: 分库路由规则工厂类
 * @author qsl. email：Hsia_Sharding@163.com
 * @date 2016年3月5日 下午9:15:28
 *
 */
public class DbRuleFactory implements RuleFactory{
	@Override
	public Rule getRule() {
		return new DbRule();
	}
}