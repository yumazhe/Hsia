
package com.Hsia.sharding.utils;

public class CommonUtils {

	public static void main(String[] args) {
		splitTest();
	}

	public static void splitTest(){
		String src = "t_sharding.insert";
		String[] params = src.split("\\.");
		System.out.println(params[0]);
	}
}
