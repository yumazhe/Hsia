package com.Hsia.sharding.service;

import com.Hsia.sharding.dao.IUserDao;
import com.Hsia.sharding.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: yumazhe
 * @Date: 2019/4/2 12:05
 * @Description:
 */
@Service
public class UserService implements  IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public User query(int id) {
        return userDao.query(id);
    }
}
