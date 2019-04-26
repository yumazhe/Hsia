package com.Hsia.sharding.multi.service;

import com.Hsia.sharding.multi.dao.IUserDao;
import com.Hsia.sharding.multi.model.User;
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
    public void save(User user, int num) {
        userDao.save(user, num);
    }

    @Override
    public User query(long id, int num) {
        return userDao.query(id, num);
    }

    @Override
    public void update(long id, int money) {
        userDao.update(id, money);
    }

    @Override
    public void updateMulti(Long[] ids, int money) {
        userDao.updateMulti(ids, money);
    }
}
