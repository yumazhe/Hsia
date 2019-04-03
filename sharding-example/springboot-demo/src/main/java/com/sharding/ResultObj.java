package com.sharding;

public class ResultObj {

	private String errcode="0";
	private String message="操作成功";
	private Object data;
	
	public ResultObj(){
		
	}
	
	
	public ResultObj(String errcode, String message) {
		super();
		this.errcode = errcode;
		this.message = message;
	}


	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
