
package com.Hsia.sharding.utils;

import org.junit.Test;

public class CommonUtilsTest {

    @Test
    public void split() {
        String src = "t_sharding.insert";
        String[] params = src.split("\\.");
        System.out.println(params[0]);
    }

    @Test
    public void reflect() throws NoSuchFieldException {
        User user = new User();
        user.setId(1);
        user.setName("xiaolang");
        user.setAge(18);

        String key = "tt";
        Object value = CommonUtil.getObjectByReflect(user, key);
        System.out.println(key + " = " + value);
    }


}

class User extends Parent {
    private String name;
    private Integer age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

class Parent {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
