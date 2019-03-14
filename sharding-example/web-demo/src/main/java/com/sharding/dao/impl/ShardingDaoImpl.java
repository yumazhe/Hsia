package com.sharding.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sharding.dao.ShardingDao;

@Component
public class ShardingDaoImpl implements ShardingDao {
	@Autowired	
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public String query(String id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		return sqlSessionTemplate.selectOne("mybatis.query", param);
	}

	@Override
	public void insert(String id, int money) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("money", money);
		param.put("status", 1);
		sqlSessionTemplate.insert("mybatis.insert", param);
		
	}

}
