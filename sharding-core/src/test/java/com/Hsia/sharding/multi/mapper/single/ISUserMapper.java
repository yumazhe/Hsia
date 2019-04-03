package com.Hsia.sharding.multi.mapper.single;

import com.Hsia.sharding.multi.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: yumazhe
 * @Date: 2019/4/2 12:07
 * @Description:
 */
@Mapper
public interface ISUserMapper {
    void save(User user);
    User query(@Param("id") long id, @Param("status") int status);
}
