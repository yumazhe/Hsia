package com.Hsia.sharding.route;

import com.Hsia.sharding.dataSource.DataSourceContextHolder;
import com.Hsia.sharding.parser.ResolveRouteValue;
import com.Hsia.sharding.parser.ResolveTableName;
import com.Hsia.sharding.route.tb.SetTableName;
import com.Hsia.sharding.utils.ShardingUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qsl. email：components_yumazhe@163.com
 * @ClassName: ShardingRoute
 * @Description: 数据库分库分表 实现类
 * @date 2016年3月6日 下午4:42:37
 */
@Component
public class JDBCShardingRoute extends RouteImpl {

    private static Logger logger = Logger.getLogger(JDBCShardingRoute.class);

//    @Autowired
//    protected ShardingRule shardingRule;

    @Autowired
    private DataSourceContextHolder dataSourceHolder;

    @Override
    public Object[] route(Object[] params, boolean sqlType, ShardingRule shardingRule) {

        Object param = params[0];

        String srcSql = param.toString();
        logger.debug("before sql-->" + srcSql);

        String targetSql = null;

        //数据库数量
        final int dbQuantity = shardingRule.getDbQuantity();
        //表数量
        final int tbQuantity = shardingRule.getTbQuantity();

		/* 解析sql语句中的路由条件 */
        Object shardingKey = ResolveRouteValue.getRoute(srcSql, 1);
        logger.debug("parse sharding key(before) is : "+shardingKey);
        if ((shardingKey instanceof String)
                && (shardingKey.toString().length() > 0)
                && (shardingKey.toString().startsWith("'"))
                && (shardingKey.toString().endsWith("'"))) {
            String key = (String) shardingKey;
            key = key.substring(1, key.length() - 1);
            shardingKey = key;
        }
        logger.debug("parse sharding key(after) is : "+shardingKey);

        logger.debug("src sql：" + srcSql + " and the sharding key is：" + shardingKey);

        Rule dbRule = super.getDbRule();

        int dbIndex = dbRule.getRouteIndex(shardingKey, dbQuantity, tbQuantity);

        Rule tbRule = super.getTbRule();
        int tbIndex = tbRule.getRouteIndex(shardingKey, dbQuantity, tbQuantity);

		/* 解析数据库表名 */
        final String tbName = ResolveTableName.getTableName(srcSql);
        /* 单库多表模式下设定真正的数据库表名 */
        targetSql = SetTableName.setRouteTableName(shardingRule, dbIndex, tbIndex, dbQuantity, tbQuantity, tbName, srcSql);

        final int beginIndex = ShardingUtil.getBeginIndex(shardingRule, sqlType);

		/* 切换数据源索引 */
        dataSourceHolder.setDataSourceIndex((dbIndex + beginIndex));

        return updateParam(targetSql, params);
    }
}