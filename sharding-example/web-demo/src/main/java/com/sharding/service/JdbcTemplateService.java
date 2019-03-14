package com.sharding.service;

import java.util.Map;

public interface JdbcTemplateService {

	
	public String query(String id);

	public void insert(String id, int money);

	public Map<String, Object> balance();
}
