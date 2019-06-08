package com.Hsia.sharding.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.Hsia.sharding.exceptions.SqlParserException;
import com.Hsia.sharding.route.ShardingRule;
import com.alibaba.druid.util.JdbcConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qsl. email：Hsia_Sharding@163.com
 * @ClassName: ShardingUtil
 * @Description: 路由规则工具类
 * @date 2016年3月5日 下午9:20:54
 */
public class ShardingUtil {

    private static Logger logger = LoggerFactory.getLogger(ShardingUtil.class);

    public static String getDBTBIndex(String key, int dbSize, int tbSize, String dbName, String tbName) {
        int dbIndex = ShardingUtil.getDataBaseIndex(key, dbSize, tbSize);
        int tbIndex = ShardingUtil.getTableIndex(key, dbSize, tbSize);

        String result = dbName + "_" + CommonUtil.completionNumberFormat(dbIndex) + "." + tbName + "_" + CommonUtil.completionNumberFormat(tbIndex);

        return "the route rule is：--> " + result;
    }

    /**
     * @param @param  index
     * @param @param  srcName
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getRouteName
     * @Description: 根据源名称 设置符合路由规则的名称
     */
    public static String getRouteName(int index, String srcName) {
        String targetName = srcName + "_" + CommonUtil.completionNumberFormat(index);
        return targetName;
    }

    /**
     * @param @param  shardKey
     * @param @param  dbQuantity
     * @param @param  tbQuantity
     * @param @return 设定文件
     * @return int    返回类型
     * @throws
     * @Title: getDataBaseIndex
     * @Description: 根据分库规则计算出数据源索引  TODO 可根据自己的规则进行定制
     * 每个数据库的索引都是从0000开始
     * shardKey % tbSize 得到 0~(tbSize-1)
     * 0~(tbSize-1) % dbSize 得到 0~(dbSize - 1)
     * 正好是库的索引
     */
    public static int getDataBaseIndex(Object shardKey, int dbQuantity, int tbQuantity) {
        Long routeKey = CommonUtil.crc32(shardKey);
        // shardKey % tbSize % dbSize
        int index = (int) (routeKey % tbQuantity % dbQuantity);
        return index;
    }

    /**
     * @param @param  shardKey 路由键值
     * @param @param  dbQuantity 数据库数量
     * @param @param  tbQuantity 数据表数量
     * @param @param  shardMode 是否分片
     * @param @return 设定文件
     * @return int    返回类型
     * @throws
     * @Title: getTbIndex
     * @Description: 根据分片规则计算出数据库表索引  TODO 可根据自己的规则进行定制
     * 每个库中表的索引都是从0000开始
     * shardKey % tbSize 得到 0~(tbSize-1)
     * 0~(tbSize-1) / dbSize 得到 0~(每个库中表的数量 - 1)
     * 正好是单库中表的索引
     */
    public static int getTableIndex(Object shardKey, int dbQuantity, int tbQuantity) {
        Long routeKey = CommonUtil.crc32(shardKey);
        // shardKey % tbSize / dbSize
        int index = (int) (routeKey % tbQuantity / dbQuantity);
        return index;

    }

    /**
     * @param @param  where
     * @param @return 设定文件
     * @return long    返回类型
     * @throws
     * @Title: getRouteValue
     * @Description: 根据where条件后的sql语句，解析 第一个参数的值 作为路由key的值
     */
    public static Object getRouteValue(SQLExpr where, int type) {

        List<String> columns = getConditionList(where);

        if (columns.size() < 2) {
            throw new SqlParserException("no codition");
        }
        if (type == 1) {//数据库引擎为springjdbc
            return columns.get(2);
        } else if (type == 2) {// 数据库引擎为mybatis
            return columns.get(0);
        } else {
            throw new RuntimeException("no type");
        }
    }

    public static Object getRouteValue(String sql, String key) {
        sql = escapeSql(sql);
        int insertIndex = sql.indexOf("insert");
        if (insertIndex == -1) {
            int whereIndex = sql.indexOf("where");
            if (whereIndex == -1) {
                throw new SqlParserException("the sql[" + sql + "] no route key[" + key + "].");
            } else {
                sql = sql.substring(whereIndex);
            }
        }

        logger.debug("source sql => escape sql [{}]", sql);
        String[] blocks = sql.split(" ");
        logger.debug("arrays: {}",Arrays.asList(blocks).toString());
        if (insertIndex == -1) {
            for (int i = 0; i < blocks.length; i++) {
                if (blocks[i].trim().equals(key)) {
                    return blocks[i + 2];
                }
            }
        } else {
            int first = sql.indexOf("(");
            int last = sql.indexOf(")");
            String keySql = sql.substring(first + 1, last);
            logger.debug("key:[{}]", keySql);
            sql = sql.substring(last + 1);
            first = sql.indexOf("(");
            last = sql.indexOf(")");
            String valueSql = sql.substring(first + 1, last);
            logger.debug("value:[{}]", valueSql);

            String[] keys = keySql.split(",");
            String[] values = valueSql.split(",");
            for (int i = 0; i < keys.length; i++) {
                if (keys[i].trim().equals(key)) {
                    return values[i];
                }
            }

        }
        return null;
    }

    public static List<String> getConditionList(SQLExpr where) {
        List<String> columns = null;
        if (null == where) {
            throw new SqlParserException("no condition");
        } else {
            columns = Arrays.asList(where.toString().split("\\s"));
        }

        return columns;
    }

    /**
     * @param @param  sql
     * @param @return 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: containWhere
     * @Description: 判断sql是否包含 where条件语句，true 为包含 ，否则抛出异常
     */
    public static boolean containWhere(String sql) {

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statements = parser.parseStatementList();

        if (!statements.isEmpty()) {
            SQLStatement statement = statements.get(0);
            if (statement instanceof SQLSelectStatement) {
                SQLSelectStatement selectStatement = (SQLSelectStatement) statement;
                SQLSelect sqlselect = selectStatement.getSelect();
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) sqlselect.getQuery();
                try {
                    queryBlock.getWhere();
                    return true;
                } catch (SqlParserException e) {
                    throw new SqlParserException("no condition");
                }
            } else if (statement instanceof SQLInsertStatement) {
                /* INSERT语句直接放行 */
                return true;

            } else if (statement instanceof SQLDeleteStatement) {
                SQLDeleteStatement deleteStatement = (SQLDeleteStatement) statement;
                try {
                    deleteStatement.getWhere();
                    return true;

                } catch (SqlParserException e) {
                    throw new SqlParserException("no condition");
                }
            } else if (statement instanceof SQLUpdateStatement) {
                SQLUpdateStatement updateStatement = (SQLUpdateStatement) statement;
                try {
                    updateStatement.getWhere();
                    return true;

                } catch (SqlParserException e) {
                    throw new SqlParserException("no condition");
                }
            }
        }
        return false;
    }


    /**
     * @param shardingRule 读写起始索引,如比r0w512
     * @param sqlType      true为master起始索引,false为slave起始索引
     * @return int    返回类型  起始索引
     * @throws
     * @Title: getBeginIndex
     * @Description: 获取master/slave的数据源起始索引
     */
    public static int getBeginIndex(ShardingRule shardingRule, boolean sqlType) {

        String write_index = shardingRule.getWrite_index();
        String read_index = shardingRule.getRead_index();

        Integer index;
        if (sqlType) {// 写库
            logger.debug("the database's type is WRITE");
            index = Integer.parseInt(write_index);
        } else {// 读库
            logger.debug("the database's type is READ");
            index = Integer.parseInt(read_index);
        }

        return index;
    }

    /**
     * @param shardingKey
     * @param dbsize
     * @param tbsize
     */
    public static void printDbTbIndex(Object shardingKey, int dbsize, int tbsize) {
        int dbIndex = ShardingUtil.getDataBaseIndex(shardingKey, dbsize, tbsize);
        int tbIndex = ShardingUtil.getTableIndex(shardingKey, dbsize, tbsize);
        logger.debug("路由键：" + shardingKey.toString() + "， 库：" + dbIndex + "， 表 ：" + tbIndex);
    }


    /**
     * 将路由键进行分类,用于对数据的批量更新
     *
     * @param values
     * @return
     */
    public static Map<String, List<Object>> parse(List<Object> values, int dbsize, int tbsize) {
        if (values == null) {
            return null;
        }
        ConcurrentHashMap<String, List<Object>> indexMap = new ConcurrentHashMap<>();

        for (Object value : values) {
            int tbIndex = getTableIndex(value, dbsize, tbsize);
            int dbIndex = getDataBaseIndex(value, dbsize, tbsize);
            String key = dbIndex + "_" + tbIndex;
            List indexs = indexMap.get(key);
            if (indexs == null) {
                indexs = new ArrayList<>();
            }
            indexs.add(value);
            indexMap.put(key, indexs);
        }
        return indexMap;
    }

    /**
     * 判断sql是否包含路由条件
     *
     * @param sql 原始sql
     * @param key 路由主键
     * @return
     */
    public static boolean haveRouteKey(String sql, String key) {
        if (sql == null || sql.trim().length() == 0) {
            throw new SqlParserException("sql must not be null");
        }

        // sql 转义
        sql = escapeSql(sql);

        //是否为插入语句
        int insertIndex = sql.indexOf("insert");
        if (insertIndex == -1) {
            // insertIndex == -1 表示 此sql为非插入sql 则需要判断是否包含where条件
            int whereIndex = sql.indexOf("where");
            if (whereIndex > -1) {
                // whereIndex > -1 ： 包含where 条件
                sql = sql.substring(whereIndex);
            } else {
                return false;
            }
        }

        String[] blocks = sql.split(" ");
        for (String block : blocks) {
            if (block.equals(key)) {
                return true;
            }
        }
        return false;

    }

    private static String escapeSql(String sql) {
        logger.debug("source sql:[{}]", sql);
        //屏蔽掉分号;
        if (sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        //转换为小写
        sql = sql.toLowerCase();

        sql = sql.replace("(", " ( ");
        sql = sql.replace(")", " ) ");
        sql = sql.replace("{", " { ");
        sql = sql.replace("}", " } ");
        sql = sql.replace(",", " , ");
        sql = sql.replace("=", " = ");
        sql = sql.replace("+", " + ");
        sql = sql.replace("-", " - ");
        sql = sql.replace("*", " * ");
        sql = sql.replace("/", " / ");

        // 多空格 转换 为 一个空格
        sql = sql.replaceAll(" +", " ");

        logger.debug("escape sql:[{}]", sql);
        return sql;
    }


    /**
     * 格式化索引
     *
     * @param index
     * @param format
     * @return
     */
    public static String formatIndex(long index, int format) {
        String end = String.format("%0" + format + "d", index);
        return end;
    }

    /**
     * 格式化索引
     *
     * @param index
     * @return
     */
    public static String formatIndex(long index) {
        return formatIndex(index, 4);
    }
}
