package com.sharding.jdbc.service.impl;

import com.sharding.jdbc.service.IJdbcTemplateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class JdbcTemplateServiceImpl implements IJdbcTemplateService {
	
	private static Logger logger = Logger.getLogger(JdbcTemplateServiceImpl.class);
  
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public String query(String id) {
		String sql = "select money from t_sharding where id='"+id+"'";
		return jdbcTemplate.queryForObject(sql, String.class);
	}

	@Override
	public void insert(String id, int money) {
		String sql = "insert into t_sharding(id, money, create_time) values ('"+id+"', "+money+", '"+sf.format(new Date())+"')";
		int result = jdbcTemplate.update(sql);
		logger.debug("结果为："+result);
	}

	@Override
	public Map<String, Object> balance() {
		
		return null;
	}  
}  