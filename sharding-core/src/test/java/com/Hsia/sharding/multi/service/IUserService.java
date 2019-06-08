package com.Hsia.sharding.multi.service;


import com.Hsia.sharding.multi.model.User;

/**
 * @Auther: yumazhe
 * @Date: 2019/4/2 12:05
 * @Description:
 */
public interface IUserService {

    Integer query4batch(long id);

    void save(User user, int num);

    User query(long id, int num);

    void update(long id, int money);

    void updateMulti(Long[] ids, int money, int type);

    void updateMulti(int money, int type);

    void updateMultiFullTableScan(int money, int tbindex);
}
