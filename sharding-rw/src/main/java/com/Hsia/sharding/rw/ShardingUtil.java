package com.Hsia.sharding.rw;

import org.apache.log4j.Logger;

/**
 *
 * @ClassName: ShardingUtil
 * @Description: 路由规则工具类
 * @author qsl. email：components_yumazhe@163.com
 * @date 2016年3月5日 下午9:20:54
 *
 */
public class ShardingUtil {

	private static Logger logger = Logger.getLogger(ShardingUtil.class);

	/**
	 * 
	 * @Title: getBeginIndex 
	 * @Description: 获取master/slave的数据源起始索引
	 * @param  shardingRule 读写起始索引,如比r0w512
	 * @param  sqlType
	 * 					true为master起始索引,false为slave起始索引
	 * @return int    返回类型  起始索引
	 * @throws
	 */
	public static int getBeginIndex(ShardingRule shardingRule, boolean sqlType) {

		String write_index = shardingRule.getWrite_index();
		String read_index = shardingRule.getRead_index();

		Integer index ;
		if (sqlType){// 写库
			logger.debug("the database's type is WRITE");
			index = Integer.parseInt(write_index);
		} else {// 读库
			logger.debug("the database's type is READ");
			index = Integer.parseInt(read_index);
		}

		return index;
	}
	
}
