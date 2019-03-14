package com.sharding.controller;


import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sharding.common.ResultObj;
import com.sharding.service.ShardingService;

@Controller
@RequestMapping(value = "/sharding/mybatis")
public class ShardingControllerMybatis {
	@Autowired
	private ShardingService dataAnalysisService;

	private Logger logger = Logger.getLogger(ShardingControllerMybatis.class);

	ResultObj resultObj = null;

	@RequestMapping(value = "/query/{id}")
	@ResponseBody
	private ResultObj query(@PathVariable String id) {
		resultObj = new ResultObj();
		try {
			if(null == id){
				resultObj.setErrcode("1");
				resultObj.setMessage("传入参数为空");
				return resultObj;
			}
			String throwable = dataAnalysisService.query(id);
			if(null == throwable){
				resultObj.setMessage("没有发现异常信息,请确认输入参数：[id="+id+"]");
				return resultObj;
			}else{
				resultObj.setData(throwable);
				return resultObj;
			}
		} catch (Exception e) {
			logger.error("query failed", e);
			resultObj.setErrcode("1");
			resultObj.setMessage("faild to query list");
		}
		return resultObj;
	}
	@RequestMapping(value = "/insert")
	@ResponseBody
	private ResultObj insert() {
		resultObj = new ResultObj();
		try {
			String id = UUID.randomUUID().toString();
			logger.debug("uuid:-->"+id);
			dataAnalysisService.insert(id, new Random().nextInt(1000));
			resultObj.setData(id);
			return resultObj;
		} catch (Exception e) {
			logger.error("insert failed", e);
			resultObj.setErrcode("1");
			resultObj.setMessage("faild to query list");
		}
		return resultObj;
	}
}
