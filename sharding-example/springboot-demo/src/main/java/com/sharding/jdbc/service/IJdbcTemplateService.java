package com.sharding.jdbc.service;

import java.util.Map;

public interface IJdbcTemplateService {

	
	public String query(String id);

	public void insert(String id, int money);

	public Map<String, Object> balance();
}
