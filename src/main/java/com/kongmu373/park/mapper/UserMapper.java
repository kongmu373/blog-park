package com.kongmu373.park.mapper;

import com.kongmu373.park.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User selectById(@Param("id") Integer id);
}
