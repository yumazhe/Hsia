package com.Hsia.sharding.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.zip.CRC32;

public class CommonUtil {

	/**
	 * 数字格式化 31 -> 0031
	 * @param number
	 * @return
	 */
	public static String completionNumberFormat(int number) {
		DecimalFormat df = new DecimalFormat("0000");
		String formatNumber = df.format(number);
		return formatNumber;
	}

	/**
	 * 
	 * @Title: toByteArray 
	 * @Description: java 对象转换为字节数组 
	 * @param @param obj
	 * @param @return    设定文件 
	 * @return byte[]    返回类型 
	 * @throws
	 */
	public static byte[] toByteArray (Object obj) {  
		if(null == obj) throw new RuntimeException("null对象不能进行字节序列化...");
		byte[] bytes = null;        
		ByteArrayOutputStream bos = new ByteArrayOutputStream();        
		try {          
			ObjectOutputStream oos = new ObjectOutputStream(bos);           
			oos.writeObject(obj);          
			oos.flush();           
			bytes = bos.toByteArray ();        
			oos.close();           
			bos.close();          
		} catch (IOException ex) {          
			throw new RuntimeException("对象序列化出错...");   
		}        
		return bytes;      
	}

	/**
	 * 
	 * @Title: crc32 
	 * @Description: 采用crc32加密，分片均衡 
	 * @param @param array
	 * @param @return    设定文件 
	 * @return long    返回类型 
	 * @throws
	 */
	public static final long crc32(byte[] array) {
		if (array != null) {
			return crc32(array, 0, array.length);
		}

		return 0;
	}


	public static final long crc32(byte[] array, int offset, int length) {
		CRC32 crc32 = new CRC32();
		crc32.update(array, offset, length);
		return (long) (crc32.getValue());
	}

	
	/**------------------------------- 利用反射原理操作对象相关属性 start -------------------------------**/
	/**
	 * 通过反射获取对象
	 * @param target
	 * @param key
	 * @return
	 */
	public static Field getFieldByReflect(Object target, String key){
		Field field = null;
		try {
			field = target.getClass().getDeclaredField(key);
			field.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return field;
	}

	public static Object getObjectByReflect(Object target, String key){
		Field field = getFieldByReflect(target, key);
		Object object = null;
		try {
			object =  field.get(target);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return object;
	}

	public static void setValueByReflect(Object target, String key, Object value){
		Field field;
		try {
			field = target.getClass().getDeclaredField(key);
			field.setAccessible(true);
			field.set(target, value);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	/**------------------------------- 利用反射原理操作对象相关属性 end -------------------------------**/



	public static void main(String[] args) {
		User user = new User(10, 20);
		Object value = getObjectByReflect(user, "id");
		System.out.println(value);
	}
}

class User{

	private int id;
	private int money;

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", money=" + money +
				'}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public User(int id, int money) {

		this.id = id;
		this.money = money;
	}
}
