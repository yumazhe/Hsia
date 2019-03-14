package com.Hsia.sharding.parser;

import java.util.List;

import com.Hsia.sharding.utils.ShardingUtil;
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

/**
 * 
 * @ClassName: ResolveRouteValue
 * @Description: 解析sql语句中的路由参数
 * @author qsl. email：components_yumazhe@163.com
 * @date 2016年3月5日 下午9:20:54
 *
 */
public class ResolveRouteValue {
	/**
	 * 
	 * @Title: getRoute 
	 * @Description: 解析路由参数 ，由sql条件的第一个参数决定
	 * @param @param sql
	 * @param @return    设定文件 
	 * @return long    返回类型  
	 * @throws
	 */
	public static Object getRoute(String sql, int type) {
		
		/* 生成AST抽象语法树 逻辑来自 durid */
		SQLStatementParser parser = new MySqlStatementParser(sql);
		
		List<SQLStatement> statements = parser.parseStatementList();
		
		if (!statements.isEmpty()) {
			SQLStatement statement = statements.get(0);
			
			if (statement instanceof SQLSelectStatement) {//选择语句
				SQLSelectStatement selectStatement = (SQLSelectStatement) statement;
				SQLSelect sqlselect = selectStatement.getSelect();
				SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) sqlselect.getQuery();
				SQLExpr where = queryBlock.getWhere();
				return ShardingUtil.getRouteValue(where, type);
				
			} else if (statement instanceof SQLInsertStatement) {//插入语句
				SQLInsertStatement insertStatement = (SQLInsertStatement) statement;
				//第一个参数为路由参数
				return insertStatement.getValues().getValues().get(0).toString();
				
			} else if (statement instanceof SQLDeleteStatement) {//删除语句
				SQLDeleteStatement deleteStatement = (SQLDeleteStatement) statement;
				SQLExpr where = deleteStatement.getWhere();
				return ShardingUtil.getRouteValue(where, type);
				
			} else if (statement instanceof SQLUpdateStatement) {//更新语句
				SQLUpdateStatement updateStatement = (SQLUpdateStatement) statement;
				SQLExpr where = updateStatement.getWhere();
				return ShardingUtil.getRouteValue(where, type);
			}else{
				throw new SqlParserException("其他类型的sql， 暂不支持解析");
			}
			
		}else{
			throw new SqlParserException("sql 解析出错");
		}
	}

}