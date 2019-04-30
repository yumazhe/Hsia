package com.Hsia.sharding.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.zip.CRC32;

public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    // 默认格式化类型
    private static final int format_number = 4;

    /**
     * 数字格式化 31 -> 0031
     *
     * @param number
     * @return
     */
    public static String completionNumberFormat(int number) {
        String formatNumber = String.format("%0" + format_number + "d", number);
        return formatNumber;
    }

    /**------------------------------------route key 的分片算法  start--------------------------------------------------------------*/

    /**
     * @param obj
     * @return
     */
    public static final long crc32(Object obj) {
        byte[] bytes = toByteArray(obj);
        return crc32(bytes);
    }

    /**
     * @param @param  obj
     * @param @return 设定文件
     * @return byte[]    返回类型
     * @throws
     * @Title: toByteArray
     * @Description: java 对象转换为字节数组
     */
    private static byte[] toByteArray(Object obj) {
        if (null == obj) throw new RuntimeException("null对象不能进行字节序列化...");
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            throw new RuntimeException("对象序列化出错...");
        }
        return bytes;
    }

    /**
     * @param @param  array
     * @param @return 设定文件
     * @return long    返回类型
     * @throws
     * @Title: crc32
     * @Description: 采用crc32加密，分片均衡
     */
    private static final long crc32(byte[] array) {
        if (array != null) {
            return crc32(array, 0, array.length);
        }
        return 0;
    }

    private static final long crc32(byte[] array, int offset, int length) {
        CRC32 crc32 = new CRC32();
        crc32.update(array, offset, length);
        return (long) (crc32.getValue());
    }
    /**------------------------------------route key 的分片算法  end--------------------------------------------------------------*/


    /**------------------------------- 利用反射原理操作对象相关属性 start -------------------------------**/
    /**
     * 通过反射获取对象
     *
     * @param target
     * @param key
     * @return
     */
    public static Field getFieldByReflect(Object target, String key) throws NoSuchFieldException {

        Class<?> clazz = target.getClass();
        do {
            try {
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                //TODO 必须捕获异常，不能抛出 否则 代码： clazz = clazz.getSuperclass(); 将不被执行
                logger.warn("there is no such Filed[{}] in {}", key, clazz.getName());
            }
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);

        throw new NoSuchFieldException("there is no such Filed[" + key + "] in " + target.getClass().getName());

    }

    /**
     * 通过反射获取属性数据
     *
     * @param target
     * @param key
     * @return
     */
    public static Object getObjectByReflect(Object target, String key) throws NoSuchFieldException {
        Field field = getFieldByReflect(target, key);
        Object object = null;
        try {
            object = field.get(target);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 通过反射设置属性
     *
     * @param target
     * @param key
     * @param value
     */
    public static void setValueByReflect(Object target, String key, Object value) {
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

    /**
     * ------------------------------- 利用反射原理操作对象相关属性 end -------------------------------
     **/


}

