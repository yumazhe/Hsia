package com.Hsia.sharding.dao;



import com.Hsia.sharding.model.User; /**
 * @Auther: yumazhe
 * @Date: 2019/4/2 12:06
 * @Description:
 */
public interface IUserDao {
    void save(User user);
    User query(int id);
}
