package com.Hsia.sharding.route.tb;

import com.Hsia.sharding.utils.ShardingUtil;

import com.Hsia.sharding.route.ShardingRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qsl. email：Hsia_Sharding@163.com
 * @ClassName: SetTableName
 * @Description: 数据路由前重设数据库表名, 比如通用的表名为tab, 那么重设后为tab_0000
 * @date 2016年3月5日 下午9:50:18
 */
public class SetTableName {

    private static Logger logger = LoggerFactory.getLogger(SetTableName.class);

    /**
     * @param @param  shardingConfigInfo 分库分表配置信息
     * @param @param  dbIndex 数据源索引
     * @param @param  tbIndex 数据库表索引
     * @param @param  dbSize 配置文件中数据库的数量
     * @param @param  tbSize 配置文件中数据库表的数量
     * @param @param  tbName 数据库通用表名
     * @param @param  srcSql
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: setName
     * @Description: 多库多表模式下重设真正的数据库表名
     */
    public static String setRouteTableName(int tbIndex, String tbName, String srcSql) {
        final String newTableName = ShardingUtil.getRouteName(tbIndex, tbName);
        srcSql = ShardingUtil.escapeSql(srcSql);
        String targetSql = null;
        if (srcSql.indexOf("from") > -1) {
            // 判断是否包含表名
            String[] arrys = srcSql.split("from");
            String first = arrys[0];
            String second = arrys[1];

            second = second.replaceFirst(tbName, newTableName);

            targetSql = first + " from " + second;

        } else {
            targetSql = srcSql.replaceFirst(tbName, newTableName);

        }
        logger.debug("the target sql is : --> " + targetSql);
        return targetSql;
    }

}