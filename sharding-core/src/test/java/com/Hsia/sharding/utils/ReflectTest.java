package com.Hsia.sharding.utils;

import java.lang.reflect.Field;

public class ReflectTest {  
	@SuppressWarnings("unused")
	private final String readOnly = "before";  

	public static void main(String[] args) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {  
		ReflectTest test = new ReflectTest();  
		Field f = test.getClass().getDeclaredField("readOnly");  
		f.setAccessible(true);  
		f.set(test, "after");  
		System.out.println(f.get(test));  
	}  
} 