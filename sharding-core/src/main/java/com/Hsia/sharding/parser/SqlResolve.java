package com.Hsia.sharding.parser;

import java.util.List;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.Hsia.sharding.exceptions.SqlParserException;

public class SqlResolve {

    /**
     * 判断sql是否为更新语句
     *
     * @param sql
     * @return
     */
    public static boolean sqlIsUpdate(String sql) throws SqlParserException {

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statements = parser.parseStatementList();

        if (!statements.isEmpty()) {
            SQLStatement statement = statements.get(0);

            if (statement instanceof SQLSelectStatement) {//选择语句
                return false;

            } else if (statement instanceof SQLInsertStatement) {//插入语句
                return true;

            } else if (statement instanceof SQLDeleteStatement) {//删除语句
                return true;

            } else if (statement instanceof SQLUpdateStatement) {//更新语句
                return true;

            } else {
                throw new SqlParserException("the statements is empty.");
            }
        } else {
            throw new SqlParserException("something is wrong to resolve sql : " + sql);
        }
    }
}
