package com.Hsia.sharding.dao;

import com.Hsia.sharding.mapper.IUserMapper;
import com.Hsia.sharding.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Auther: yumazhe
 * @Date: 2019/4/2 12:06
 * @Description:
 */
@Repository
public class UserDao implements IUserDao {

    @Autowired
    private IUserMapper userMapper;

    @Override
    public void save(User user) {
        userMapper.save(user);
    }

    @Override
    public User query(int id) {
        return userMapper.query(id);
    }
}
