package com.Hsia.sharding.aop;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.Hsia.sharding.dataSource.DataSourceContextHolder;
import com.Hsia.sharding.exceptions.ShardingRuleException;
import com.Hsia.sharding.route.Route;
import com.Hsia.sharding.utils.CommonUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.Hsia.sharding.dataSource.DatasourceGroup;
import com.Hsia.sharding.dataSource.SqlContextHolder;
import com.Hsia.sharding.exceptions.SqlParserException;
import com.Hsia.sharding.parser.SqlResolve;
import com.Hsia.sharding.route.MybatisShardingRouteFactory;
import com.Hsia.sharding.route.ShardingRule;
import com.Hsia.sharding.utils.ShardingUtil;

/**
 * 注意：使用本拦截器，请将系统中自依赖的mybatis-x.x.x.jar屏蔽掉，否则，拦截器使用的mybatis版本将会被覆盖，导致功能缺失，分库分表无法实现
 *
 * @author qsl. email：Hsia_Sharding@163.com
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MybatisExecuterInterceptor implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(MybatisExecuterInterceptor.class);

    /**
     * 显示声明注入 set方法
     * <bean id="executerInterceptor_multi" class="com.Hsia.sharding.aop.MybatisExecuterInterceptor">
     * <property name="shardingRule" ref="shardingRule_multi"/>
     * </bean>
     * <p>
     * <bean id="shardingRule_multi" class="com.Hsia.sharding.route.ShardingRule">
     * <property name="write_index" value="0"/><!-- 写索引 -->
     * <property name="read_index" value="0"/><!-- 读索引 -->
     * <property name="dbQuantity" value="4"/><!-- 数据库总数量 -->
     * <property name="tbQuantity" value="16"/><!-- 表的总数量 -->
     * <property name="routeKey" value="id"/><!-- 指定路由主键，如果没有指定则全局扫描 -->
     * </bean>
     */
    private ShardingRule shardingRule;

    @Autowired
    private DataSourceContextHolder dataSourceHolder;

    @Autowired
    private MybatisShardingRouteFactory shardingRouteFactory;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = null;
        Object[] params = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) params[0];

        try {

			/* 执行路由检测 */
            if (this.isDataSource(mappedStatement)) {
                Object parameter = params[1];
                BoundSql boundSql = mappedStatement.getBoundSql(parameter);
                String srcSql = boundSql.getSql();

                if (null == shardingRule) {
                    throw new ShardingRuleException("the shardingRule must not be null, please set it.");
                }

                // 多库 分表
                if (shardingRule.getDbQuantity() > 1) {
                    //创建一个数组
                    String routeKey = shardingRule.getRouteKey();
                    logger.debug("the route key is [" + routeKey + "]");

                    Object routeValue = null;
                    if (parameter == null) {
                        if (ShardingUtil.haveRouteKey(srcSql, routeKey)) {
                            // 获取路由key
                            routeValue = ShardingUtil.getRouteValue(srcSql, routeKey);
                        } else {
                            throw new SqlParserException("you must set database index by yourself.");
                        }
                    } else if (parameter instanceof Map) {
                        //多参数, 用于路由键非唯一问题
                        Map<String, Object> parameterMap = (Map<String, Object>) parameter;
                        try {
                            routeValue = parameterMap.get(routeKey);

                            if (routeValue == null) {
                                throw new SqlParserException("you have not set route key[" + routeKey + "]");

                            } else if (routeValue.getClass().isArray()) {
                                //TODO 判断是否为数组 针对批量操作，前提是 参数值需要提前进行归类
                                routeValue = Array.get(routeValue, 0);
                            }
                        } catch (Exception e) {
                            throw new SqlParserException("you have not set route key[" + routeKey + "]");
                        }

                    } else if (parameter instanceof Integer
                            || parameter instanceof Long
                            || parameter instanceof Double
                            || parameter instanceof Float
                            || parameter instanceof Short
                            || parameter instanceof String) {//解析 入参只有一个的情况 判断是否为基础类型

                        if (ShardingUtil.haveRouteKey(srcSql, routeKey)) {
                            routeValue = parameter;
                        } else {
                            throw new SqlParserException("you must set database index by yourself.");
                        }

                    } else if (parameter instanceof Object) {
                        // 解析对象
                        routeValue = CommonUtil.getObjectByReflect(parameter, routeKey);
                    } else {
                        throw new SqlParserException("what is this ghost!");
                    }

                    if (routeValue == null) {
                        //如果没有路由主键，数据库将会进行全部扫描，性能低下，建议通过第三方系统确认路由主键
                        //本插件将会报错 the route value must not be null
                        throw new SqlParserException("the route value is null, so excute directly.[DataSourceContextHolder.setDataSourceIndex(dbIndex)]");

                    } else {
                        Object[] p = new Object[]{srcSql, routeValue};

                        Route shardingRoute = shardingRouteFactory.getRoute();
                        Object[] sql = shardingRoute.route(p, SqlResolve.sqlIsUpdate(srcSql), shardingRule);
                        // 获取目标sql
                        String targetSql = (String) sql[0];
                        logger.debug("the route key is : [" + routeKey + "] and the route value is : [" + routeValue + "] and the target sql is [ " + targetSql + " ]");

                        //save sql into the ThreadLocal and to modify eazily the sql
                        SqlContextHolder.getInstance().setExecuteSql(targetSql);

                    }

                } else {// 单库 分表
                    final int index = ShardingUtil.getBeginIndex(shardingRule, SqlResolve.sqlIsUpdate(srcSql));
                    dataSourceHolder.setDataSourceIndex(index);
                }
            }

            result = invocation.proceed();

        } catch (Exception e) {
            logger.error("there is something wrong...", e);
            throw e;
        }
        return result;

    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * @param @param  proceedingJoinPoint
     * @param @return 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: isDataSource
     * @Description: 路由检测 [synchronized] 不要加synchronized 超级影响性能
     */
    private boolean isDataSource(MappedStatement mappedStatement) {
        boolean flag = false;
        try {
            Configuration configuration = mappedStatement.getConfiguration();
            Environment environment = configuration.getEnvironment();
            DataSource dataSource = environment.getDataSource();
            //如果SqlSessionTemplate持有的不是 com.qishiliang.sharding.route.impl.DatasourceGroup 动态数据源,则不进行数据路由操作
            flag = dataSource instanceof DatasourceGroup;
            logger.debug(flag ? "the datasource IS sharding datasource." : "the datasource IS NOT sharding datasource.");
        } catch (Exception e) {
            throw new SqlParserException("the datasource is wrong.", e);
        }
        return flag;
    }

    public ShardingRule getShardingRule() {
        return shardingRule;
    }

    public void setShardingRule(ShardingRule shardingRule) {
        this.shardingRule = shardingRule;
    }
}