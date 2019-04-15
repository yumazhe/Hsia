package com.Hsia.sharding.exceptions;


/**
 * 
 * @ClassName: SqlParserException
 * @Description: sql运行时异常
 * @author qsl. email：Hsia_Sharding@163.com
 * @date 2016年5月6日 上午10:27:17
 *
 */
public class SqlParserException extends RuntimeException {
	private static final long serialVersionUID = -3671607184598679934L;

	public SqlParserException(String msg) {
		super(msg);
	}
	public SqlParserException(String msg, Throwable e) {
		super(msg, e);
	}
}