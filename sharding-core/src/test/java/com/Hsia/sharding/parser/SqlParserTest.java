package com.Hsia.sharding.parser;

import java.util.List;

import org.junit.Test;

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
import com.Hsia.sharding.utils.ShardingUtil;

public class SqlParserTest {
	
	
	@Test
	public void parserbySelect() throws Exception {
		final String SQL = "SELECT * FROM userinfo_test";

		SQLStatementParser parser = new MySqlStatementParser(SQL);
		List<SQLStatement> statements = parser.parseStatementList();
		if (0 < statements.size()) {
			SQLStatement statement = statements.get(0);
			if (statement instanceof SQLSelectStatement) {
				SQLSelectStatement selectStatement = (SQLSelectStatement) statement;
				SQLSelect sqlselect = selectStatement.getSelect();
				SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) sqlselect.getQuery();
				SQLExpr where = queryBlock.getWhere();
				List<String> values = ShardingUtil.getConditionList(where);
				System.out.println("tabName-->" + queryBlock.getFrom());
				System.out.println("routeKey-->" + values.get(0));
				System.out.println("routeValue-->" + values.get(2));
				System.out.println(queryBlock.getWhere().toString());
			}
		}
	}

	@Test
	public void parserbyInsert() throws Exception {
		String sql = "INSERT INTO userinfo_test(uid,name) VALUES(10000,xiaolang)";
		/* 生成AST抽象语法树 */
		SQLStatementParser parser = new MySqlStatementParser(sql);
		List<SQLStatement> statements = parser.parseStatementList();
		if (0 < statements.size()) {
			SQLStatement statement = statements.get(0);
			if (statement instanceof SQLInsertStatement) {
				SQLInsertStatement insertStatement = (SQLInsertStatement) statement;
				System.out.println("tabName-->" + insertStatement.getTableName());
				System.out.println("routeKey-->" + insertStatement.getColumns().get(0));
				System.out.println("routeValue-->" + insertStatement.getValues().getValues().get(0));
			}
		}
	}
	
	@Test
	public void parserbyUpdate() throws Exception {
		String sql = "UPDATE userinfo_test SET sex = ? WHERE uid=10000 AND name=xiaolang";
		/* 生成AST抽象语法树 */
		SQLStatementParser parser = new MySqlStatementParser(sql);
		List<SQLStatement> statements = parser.parseStatementList();
		if (0 < statements.size()) {
			SQLStatement statement = statements.get(0);
			if (statement instanceof SQLUpdateStatement) {
				SQLUpdateStatement updateStatement = (SQLUpdateStatement) statement;
				SQLExpr where = updateStatement.getWhere();
				List<String> values = ShardingUtil.getConditionList(where);
				System.out.println("tabName-->" + updateStatement.getTableName());
				System.out.println("routeKey-->" + values.get(0));
				System.out.println("routeValue-->" + values.get(2));
				System.out.println(updateStatement.getWhere().toString());
			}
		}
	}

	@Test
	public void parserbyDelete() throws Exception {
		String sql = "DELETE FROM userinfo_test WHERE uid=10000 AND name=xiaolang";
		/* 生成AST抽象语法树 */
		SQLStatementParser parser = new MySqlStatementParser(sql);
		List<SQLStatement> statements = parser.parseStatementList();
		if (0 < statements.size()) {
			SQLStatement statement = statements.get(0);
			if (statement instanceof SQLDeleteStatement) {
				SQLDeleteStatement deleteStatement = (SQLDeleteStatement) statement;
				System.out.println("tabName-->" + deleteStatement.getTableName());
				SQLExpr where = deleteStatement.getWhere();
				List<String> values = ShardingUtil.getConditionList(where);
				System.out.println("routeKey-->" + values.get(0));
				System.out.println("routeValue-->" + values.get(2));
				System.out.println(deleteStatement.getWhere().toString());
			}
		}
	}
}