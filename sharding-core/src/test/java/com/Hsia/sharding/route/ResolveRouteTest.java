package com.Hsia.sharding.route;

import com.Hsia.sharding.parser.ResolveRouteValue;
import com.Hsia.sharding.utils.ShardingUtil;
import org.junit.Test;

public class ResolveRouteTest {

	@Test
	public void getRoutebySelect() {
		final String SQL1 = "SELECT * FROM t_sharding WHERE uid = 10010 and name=?";
		System.out.println(ResolveRouteValue.getRoute(SQL1, 1).toString());
		final String SQL2 = "INSERT INTO t_sharding(uid,name) VALUES(10010,?)";
		System.out.println(ResolveRouteValue.getRoute(SQL2, 1).toString());
		final String SQL3 = "UPDATE t_sharding SET sex = ? WHERE uid=10010 AND email=?";
		System.out.println(ResolveRouteValue.getRoute(SQL3, 1).toString());
		final String SQL4 = "DELETE FROM t_sharding WHERE uid=10010 AND name=?";
		System.out.println(ResolveRouteValue.getRoute(SQL4, 1).toString());
	}

	@Test
	public void routeKey(){
//		String sql = "update t_sharding \r\n set money = ? where              id = ?";
		String sql = "insert into  t_sharding (id, money, name) values (?, ?, ?)";//无法判断
		String key = "id";
		System.out.println(ShardingUtil.haveRouteKey(sql, key));

	}
}