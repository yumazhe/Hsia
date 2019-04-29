package com.Hsia.sharding.multi.dao;


import com.Hsia.sharding.multi.model.User;

/**
 * @Auther: yumazhe
 * @Date: 2019/4/2 12:06
 * @Description:
 */
public interface IUserDao {
    void save(User user, int num);
    User query(long id, int num);

    void update(long id, int money);

    void updateMulti(Long[] ids, int money, int type);

    void updateMulti(int money, int type);

    void updateMultiFullTableScan(int money, String tbindex);
}
