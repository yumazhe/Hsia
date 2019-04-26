package com.Hsia.sharding.multi.dao;

import com.Hsia.sharding.multi.mapper.multi.IMUserMapper;
import com.Hsia.sharding.multi.mapper.single.ISUserMapper;
import com.Hsia.sharding.multi.model.User;
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
    private ISUserMapper suserMapper;
    @Autowired
    private IMUserMapper muserMapper;

    @Override
    public void save(User user, int num) {
        if (num == 0)
            suserMapper.save(user);
        else
            muserMapper.save(user);
    }

    @Override
    public User query(long id, int num) {
        if (num == 0)
            return suserMapper.query(id, 0);
        else {
            User user = muserMapper.query(id, 0);
            return user;

        }
    }

    @Override
    public void update(long id, int money) {
        muserMapper.update(id, money);
    }

    @Override
    public void updateMulti(Long[] ids, int money) {
        muserMapper.updateMulti(ids, money);
    }
}
