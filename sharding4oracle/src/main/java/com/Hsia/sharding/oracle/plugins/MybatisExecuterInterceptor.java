package com.Hsia.sharding.oracle.plugins;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.Hsia.sharding.oracle.exceptions.ShardingRuleException;
import com.Hsia.sharding.oracle.parser.ResolveTableName;
import com.Hsia.sharding.oracle.route.ShardingRule;
import com.Hsia.sharding.oracle.datasources.DataSourceContextHolder;
import com.Hsia.sharding.oracle.datasources.DatasourceGroup;
import com.Hsia.sharding.oracle.exceptions.SqlParserException;
import com.Hsia.sharding.oracle.utils.CommonUtil;
import com.Hsia.sharding.oracle.utils.ShardingUtil;
import com.alibaba.fastjson.JSON;
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

/**
 * 注意：使用本拦截器进行读写分离
 *
 * @author qsl
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}), @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}), @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class MybatisExecuterInterceptor implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(MybatisExecuterInterceptor.class);

    private ShardingRule shardingRule;

    @Autowired
    private DataSourceContextHolder dataSourceHolder;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = null;
        Object[] params = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) params[0];
        try {

            /* 执行路由检测 */
            if (this.isDataSource(mappedStatement)) {
                if (null == shardingRule) {
                    throw new ShardingRuleException("the shardingRule must not be null, please set it.");
                }

                Object parameter = params[1];
                BoundSql boundSql = mappedStatement.getBoundSql(parameter);
                String srcSql = boundSql.getSql();
                logger.debug("执行前sql为[{}]", srcSql);

                // 根据sql获取表名
                final String tbName = ResolveTableName.getTableName(srcSql);

                // TODO 根据表名获取路由主键
                String routeKey = shardingRule.getRouteKey();
                if (routeKey == null) {
                    throw new ShardingRuleException("table[" + routeKey + "]不支持分库分表策略或者忘记配置路由键.");
                }

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

                    if (parameterMap.containsKey(routeKey)) {
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

                    } else {
                        logger.warn("you have not set route key. the map is [{}]", JSON.toJSONString(parameterMap));
                    }

                } else if (parameter instanceof Integer || parameter instanceof Long || parameter instanceof Double || parameter instanceof Float || parameter instanceof Short || parameter instanceof String) {//解析 入参只有一个的情况 判断是否为基础类型

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

                final int index = ShardingUtil.getDbIndex(shardingRule, routeValue);
                logger.debug("table[{}]: routekey=[{}],value=[{}], db=[{}]", tbName, routeKey, routeValue, index);
                dataSourceHolder.setDataSourceIndex(index);

            } else {
                // TODO 不分库分表
//            final int index = ShardingUtil.getBeginIndex(shardingRule);
//            dataSourceHolder.setDataSourceIndex(index);
                logger.warn("不分库分表");
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
     * @Description: 路由检测
     */
    public boolean isDataSource(MappedStatement mappedStatement) {
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