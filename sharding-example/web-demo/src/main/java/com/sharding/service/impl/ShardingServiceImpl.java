package com.sharding.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharding.dao.ShardingDao;
import com.sharding.service.ShardingService;

@Service
public class ShardingServiceImpl implements ShardingService {

	@Autowired
	private ShardingDao dataAnalysisDao;


	@Override
	public String query(String id) {
		String content = dataAnalysisDao.query(id);
		return content;
	}


	@Override
	public void insert(String id, int money) {
		dataAnalysisDao.insert(id, money);
	}

	

}
