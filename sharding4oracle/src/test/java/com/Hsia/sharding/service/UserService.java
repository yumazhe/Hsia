package com.Hsia.sharding.service;

import com.Hsia.sharding.dao.IUserDao;
import com.Hsia.sharding.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class, value = "trade_Transaction")
    public void save(User user) {
        userDao.save(user);
//        user.setId(2);
//        userDao.save(user);
//        user.setId(3);
//        user.setNodeId(3);
//        userDao.save(user);
        if(user.getId() == 4){
            throw new RuntimeException("xxxxxxx");
        }
    }

    @Override
    public User query(int id) {
        return userDao.query(id);
    }
}
