package com.Hsia.sharding.plugins.mybatis;

import com.Hsia.sharding.dataSource.SqlContextHolder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.*;

/**
 * @Auther: yumazhe
 * @Date: 2019/4/2 17:18
 * @Description: 重载源码中的instantiateStatement方法
 */
public class HsiaPreparedStatementHandler extends PreparedStatementHandler {

    public HsiaPreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameter, rowBounds, resultHandler, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        //TODO 通过上下文获取sql
        String sql = null;
        try {
            sql = SqlContextHolder.getInstance().getExecuteSql();
            if (sql == null || "".equals(sql.trim())) {
                sql = boundSql.getSql();
            }
        } finally {
            SqlContextHolder.getInstance().clearExecuteSql();
        }

        if (sql == null || "".equals(sql.trim())) {
            throw new SQLException("the execute sql must not be null. ");
        }



        /*************************以下为原装代码**********************************/
        if (mappedStatement.getKeyGenerator() instanceof Jdbc3KeyGenerator) {
            String[] keyColumnNames = mappedStatement.getKeyColumns();
            if (keyColumnNames == null) {
                return connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            } else {
                return connection.prepareStatement(sql, keyColumnNames);
            }
        } else if (mappedStatement.getResultSetType() != null) {
            return connection.prepareStatement(sql, mappedStatement.getResultSetType().getValue(), ResultSet.CONCUR_READ_ONLY);
        } else {
            return connection.prepareStatement(sql);
        }
    }

}
