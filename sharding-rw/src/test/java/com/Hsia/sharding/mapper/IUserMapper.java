package com.Hsia.sharding.mapper;

import com.Hsia.sharding.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auther: yumazhe
 * @Date: 2019/4/2 12:07
 * @Description:
 */
@Mapper
public interface IUserMapper {
    void save(User user);
    User query(int id);
}
