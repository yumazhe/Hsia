package com.Hsia.sharding.aop;

import com.Hsia.sharding.route.Route;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;

import com.Hsia.sharding.dataSource.DataSourceContextHolder;
import com.Hsia.sharding.dataSource.DatasourceGroup;
import com.Hsia.sharding.route.JDBCShardingRouteFactory;
import com.Hsia.sharding.route.ShardingRule;
import com.Hsia.sharding.utils.ShardingUtil;

/**
 * 
 * @ClassName: SQLExecuterInterceptor
 * @Description: AOP 基于spring的jdbc 设置 切片 代理，数据路由入口
 * @author qsl. email：Hsia@163.com
 * @date 2016年3月6日 下午4:59:19
 *
 */
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class JDBCExecuterInterceptor {

	private Logger logger = Logger.getLogger(JDBCExecuterInterceptor.class);

//	@Autowired
	private ShardingRule shardingRule;

	@Autowired
	private DataSourceContextHolder dataSourceHolder;

	@Autowired
	private JDBCShardingRouteFactory shardingRouteFactory;

	/**
	 * @throws Throwable 
	 * 
	 * @Title: interceptUpdateSQL 
	 * @Description: 基于Spring Aop的方式对org.springframework.jdbc.core.JdbcTemplate类下所有的update()方法进行拦截 
	 * @param @param proceedingJoinPoint 委托对象的上下文信息
	 * @param @return    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@Around("execution(* org.springframework.jdbc.core.JdbcTemplate.update*(..))")
	public Object interceptUpdateSQL(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = null;
		/* 执行路由检测 */
		if (this.isDataSource(proceedingJoinPoint)) {
			//获取切片中的相关信息
			Object[] params = proceedingJoinPoint.getArgs();

			if (0 >= params.length)
				return result;

			// 通过参数判断分库分表规则
			Route shardingRoute = shardingRouteFactory.getRoute();
			if (shardingRule.getDbQuantity() > 1){// 多库 分表

				params = shardingRoute.route(params, true, shardingRule);

			} else {// 单库 分表
				final int index = ShardingUtil.getBeginIndex(shardingRule, true);
				dataSourceHolder.setDataSourceIndex(index);
			}
			try {
				//继续执行
				result = proceedingJoinPoint.proceed(params);
			} catch (Throwable e) {
				logger.error(e.getMessage());
				throw e;
			}
		} else {
			try {
				logger.info("sharding mysql source: no need to routing");
				result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @throws Throwable 
	 * 
	 * @Title: interceptQuerySQL 
	 * @Description: 基于Spring Aop的方式对org.springframework.jdbc.core.JdbcTemplate类下所有的query()方法进行拦截 
	 * @param @param proceedingJoinPoint
	 * @param @return    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@Around("execution(* org.springframework.jdbc.core.JdbcTemplate.query*(..))")
	public Object interceptQuerySQL(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = null;
		/* 执行路由检测 */
		if (this.isDataSource(proceedingJoinPoint)) {
			//获取切片中的相关信息
			Object[] params = proceedingJoinPoint.getArgs();

			if (0 >= params.length)
				return result;

			// 通过参数判断分库分表规则
			Route shardingRoute = shardingRouteFactory.getRoute();
			if (shardingRule.getDbQuantity() > 1){// 多库 分表

				params = shardingRoute.route(params, false, shardingRule);

			} else {// 单库 分表
				final int index = ShardingUtil.getBeginIndex(shardingRule, false);
				dataSourceHolder.setDataSourceIndex(index);
			}
			try {
				//继续执行
				result = proceedingJoinPoint.proceed(params);
			} catch (Throwable e) {
				logger.error(e.getMessage());
				throw e;
			}
		} else {
			try {
				logger.info("sharding mysql source: no need to routing");
				result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 
	 * @Title: isDataSource 
	 * @Description: 路由检测 
	 * @param @param proceedingJoinPoint
	 * @param @return    设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	public synchronized boolean isDataSource(ProceedingJoinPoint proceedingJoinPoint) {
		//	如果JdbcTemplate持有的不是 com.qishiliang.sharding.route.impl.DatasourceGroup 动态数据源,则不进行数据路由操作
		boolean flag = ((JdbcTemplate) proceedingJoinPoint.getTarget()).getDataSource() instanceof DatasourceGroup;
		logger.info(flag ? "the datasource IS sharding datasource." : "the datasource IS NOT sharding datasource.");
		return flag;
	}
}