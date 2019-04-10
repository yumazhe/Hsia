package com.Hsia.sharding.dataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * @ClassName: DatasourceGroup
 * @Description: 动态数据源实现
 * 该数据源继承自Spring提供的AbstractRoutingDataSource,
 * 可以根据配置文件中的数据源索引对多数据源进行动态切换,
 * 能够非常方便的实现数据源路由工作
 * @author qsl. email：components_yumazhe@163.com
 * @date 2016年3月6日 上午11:09:12
 *
 */
public class DatasourceGroup extends AbstractRoutingDataSource {

	private static Logger logger = Logger.getLogger(DatasourceGroup.class);

	@Autowired
	private DataSourceContextHolder dataSourceHolder;

	@Override
	protected Object determineCurrentLookupKey() {
		int index = -1;
		/* 获取存放在ThreadLocal中的数据源索引 */
		index = dataSourceHolder.getDataSourceIndex();
		return index;
	}
}
