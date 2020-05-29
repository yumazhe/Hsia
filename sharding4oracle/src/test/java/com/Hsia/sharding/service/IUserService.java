package com.Hsia.sharding.service;

import com.Hsia.sharding.model.User;

/**
 * @Auther: yumazhe
 * @Date: 2019/4/2 12:05
 * @Description:
 */
public interface IUserService {

    void save(User user);

    User query(int id);
}
