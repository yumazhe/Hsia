package com.Hsia.sharding.multi.mapper.multi;

import com.Hsia.sharding.multi.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: yumazhe
 * @Date: 2019/4/2 12:07
 * @Description:
 */
@Mapper
public interface IMUserMapper {
//    void save(@Param("user") User user);
    void save(User user);
    User query(@Param("id") long id, @Param("status") int status);

    void update(@Param("id") long id, @Param("money") int money);

    void updateMulti(@Param("id") Long[] ids, @Param("money") int money);

    void update_multi(int money);

    void updateMultiFullTableScan(@Param("money") int money, @Param("index") String tbindex);

    Integer query4batch();
}
