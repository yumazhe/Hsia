package com.Hsia.sharding.route.tb;

import com.Hsia.sharding.route.Rule;
import com.Hsia.sharding.route.RuleFactory;

/**
 * 
 * @ClassName: TbRuleFactory
 * @Description: 分表路由规则工厂类
 * @author qsl. email：components_yumazhe@163.com
 * @date 2016年3月7日 上午10:23:17
 *
 */
public class TbRuleFactory implements RuleFactory{
	@Override
	public Rule getRule() {
		return new TbRule();
	}
}