package com.Hsia.sharding.parser.mybatis;

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
 * @Description: 暂未想到优雅的解决方式，所有先改源码
 */
public class HsiaPreparedStatementHandler extends PreparedStatementHandler {

    public HsiaPreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameter, rowBounds, resultHandler, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        // String sql = boundSql.getSql();//原生逻辑

        //TODO 通过上下文获取sql
        String sql = SqlContextHolder.getInstance().getExecuteSql();
        if(sql == null || sql.trim().equals("")){
            sql = boundSql.getSql();
        }

        if(sql == null || sql.trim().equals("")){
            throw new SQLException("the execute sql must not be null. ");
        }
        System.out.println("target: "+sql);
        System.out.println("src: "+boundSql.getSql());
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
