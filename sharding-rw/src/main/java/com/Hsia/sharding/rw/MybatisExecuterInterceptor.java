package com.Hsia.sharding.rw;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 注意：使用本拦截器进行读写分离
 *
 * @author qsl
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MybatisExecuterInterceptor implements Interceptor {

    private Logger logger = Logger.getLogger(MybatisExecuterInterceptor.class);

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
                Object parameter = params[1];
                BoundSql boundSql = mappedStatement.getBoundSql(parameter);
                String srcSql = boundSql.getSql();

                if (null == shardingRule) throw new RuntimeException("the shardingRule must not be null");

                final int index = ShardingUtil.getBeginIndex(shardingRule, SqlResolve.sqlIsUpdate(srcSql));
                dataSourceHolder.setDataSourceIndex(index);
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
    public synchronized boolean isDataSource(MappedStatement mappedStatement) {
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