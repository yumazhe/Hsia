package com.Hsia.sharding.oracle.exceptions;


/**
 * 
 * @ClassName: SqlParserException
 * @Description: sharding异常
 * @author qsl. email：Hsia_Sharding@163.com
 * @date 2016年5月6日 上午10:27:17
 *
 */
public class ShardingRuleException extends RuntimeException {
	public ShardingRuleException(String msg) {
		super(msg);
	}
	public ShardingRuleException(String msg, Throwable e) {
		super(msg, e);
	}
}