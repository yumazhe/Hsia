package com.Hsia.sharding.route;

import java.util.Arrays;
import java.util.List;

import com.Hsia.sharding.route.tb.TbRuleFactory;
import com.Hsia.sharding.route.db.DbRuleFactory;

/**
 * 
 * @ClassName: RouteImpl
 * @Description: 分库分表模式的外观类
 * @author qsl. email：components_yumazhe@163.com
 * @date 2016年3月6日 上午10:10:02
 *
 */
public abstract class RouteImpl implements Route {

	/**
	 * 
	 * @Title: updateParam 
	 * @Description: 重写委托对象的上下文信息，替换原先入参
	 * @param @param newSQL 持有真正片名的sql
	 * @param @param params 委托对象的方法入参
	 * @param @return    设定文件 
	 * @return Object[]    返回类型 
	 * 				替换后的方法入参
	 * @throws
	 */
	protected Object[] updateParam(String newSQL, Object[] params) {
		List<Object> newParams = Arrays.asList(params);
		newParams.set(0, newSQL);
		return newParams.toArray();
	}

	/**
	 * 
	 * @Title: getDbRule 
	 * @Description: 获取数据库路由规则 
	 * @param @return    设定文件 
	 * @return Rule    返回类型 
	 * @throws
	 */
	protected Rule getDbRule() {
		RuleFactory dbRuleFactory = new DbRuleFactory();
		return dbRuleFactory.getRule();
	}

	/**
	 * 
	 * @Title: getTbRule 
	 * @Description: 获取表的路由规则 
	 * @param @return    设定文件 
	 * @return Rule    返回类型 
	 * @throws
	 */
	protected Rule getTbRule() {
		RuleFactory tbRuleFactory = new TbRuleFactory();
		return tbRuleFactory.getRule();
	}
}