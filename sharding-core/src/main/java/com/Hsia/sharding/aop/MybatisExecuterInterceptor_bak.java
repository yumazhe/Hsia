package com.Hsia.sharding.aop;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.Hsia.sharding.dataSource.DataSourceContextHolder;
import com.Hsia.sharding.route.Route;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.Hsia.sharding.dataSource.DatasourceGroup;
import com.Hsia.sharding.parser.SqlResolve;
import com.Hsia.sharding.route.MybatisShardingRouteFactory;
import com.Hsia.sharding.route.ShardingRule;
import com.Hsia.sharding.utils.CommonUtil;
import com.Hsia.sharding.utils.ShardingUtil;

/**
 * 注意：本拦截器 由于加了锁 解决并发问题 导致 性能十分低下，无法用于高并发场景，仅作参考
 * @author qsl
 *
 */
@Intercepts({
	@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }),
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })
})
@Deprecated
public class MybatisExecuterInterceptor_bak implements Interceptor {

	private Logger logger = Logger.getLogger(MybatisExecuterInterceptor_bak.class);

//	@Autowired
	private ShardingRule shardingRule;

	@Autowired
	private DataSourceContextHolder dataSourceHolder;

	@Autowired
	private MybatisShardingRouteFactory shardingRouteFactory;

	private static final Object lock = new Object();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		synchronized (lock) {
			MappedStatement mappedStatement = null;
			SqlSource sqlSource = null;
			Object result = null;
			String srcSql = "";
			boolean flag = false;
			try {
				Object[] params = invocation.getArgs();
				mappedStatement = (MappedStatement) params[0];

				@SuppressWarnings("unchecked")
				Map<String, Object> parameterMap = (Map<String, Object>) params[1];
				BoundSql boundSql = mappedStatement.getBoundSql(parameterMap);
				srcSql = boundSql.getSql();

				/* 执行路由检测 */
				if (this.isDataSource(mappedStatement)) {

					if (shardingRule.getDbQuantity() > 1){// 多库 分表
						logger.debug("the project is sharding.");
						sqlSource = mappedStatement.getSqlSource();

						//创建一个数组
						String routeKey = shardingRule.getRouteKey();

						logger.debug("指定的路由主键为："+routeKey);
						Object routeValue = parameterMap.get(routeKey);
						logger.debug("the route key is : "+routeValue);

						Object[] p = new Object[]{srcSql, routeValue}; 

						Route shardingRoute = shardingRouteFactory.getRoute();
						Object[] sql = shardingRoute.route(p, SqlResolve.sqlIsUpdate(srcSql), shardingRule);
						String targetSql = (String)sql[0];// 获取目标sql

						//根据反射获取对象内容
						StaticSqlSource staticSqlSource = (StaticSqlSource) CommonUtil.getObjectByReflect(sqlSource, "sqlSource") ;
						//利用反射 修改相关属性
						CommonUtil.setValueByReflect(staticSqlSource, "sql", targetSql);

						flag = true;// 利用反射 对sql进行外科手术， 程序执行完需要释放

					} else {// 单库 分表
						final int index = ShardingUtil.getBeginIndex(shardingRule, SqlResolve.sqlIsUpdate(srcSql));
						dataSourceHolder.setDataSourceIndex(index);
					}
				}

				logger.debug("the program is starting executing...");
				result = invocation.proceed();

			} catch (Exception e) {
				logger.error("there is something wrong...", e);
				throw e;
			} finally {
				if(flag){
					//TODO 无法应对并发环境
					//Identifier name 't_sharding_0001_0001_0001_0000_0001_0000_0000_0000_0001_0001_0001_0000_0001_0001_0001_0001_0000_0000' is too long
					StaticSqlSource staticSqlSource = (StaticSqlSource) CommonUtil.getObjectByReflect(sqlSource, "sqlSource") ;
					CommonUtil.setValueByReflect(staticSqlSource, "sql", srcSql);
				}
			}
			return result;
		}

	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
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
	public synchronized boolean isDataSource(MappedStatement mappedStatement) {
		Configuration configuration = mappedStatement.getConfiguration();
		Environment environment = configuration.getEnvironment();
		DataSource dataSource = environment.getDataSource();
		//如果SqlSessionTemplate持有的不是 com.qishiliang.sharding.route.impl.DatasourceGroup 动态数据源,则不进行数据路由操作 
		boolean flag = dataSource instanceof DatasourceGroup;
		logger.debug(flag ? "the datasource IS sharding datasource." : "the datasource IS NOT sharding datasource.");
		return flag;
	}

}