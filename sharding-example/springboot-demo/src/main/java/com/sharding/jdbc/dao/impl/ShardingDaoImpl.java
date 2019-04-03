package com.sharding.jdbc.dao.impl;

import com.sharding.jdbc.dao.ShardingDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ShardingDaoImpl implements ShardingDao {
	@Autowired	
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public String query(String id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		return sqlSessionTemplate.selectOne("t_sharding.query", param);
	}

	@Override
	public void insert(String id, int money) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("money", money);
		param.put("status", 1);
		sqlSessionTemplate.insert("t_sharding.insert", param);
		
	}

}
