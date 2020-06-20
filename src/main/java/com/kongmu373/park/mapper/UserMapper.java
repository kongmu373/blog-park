package com.kongmu373.park.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kongmu373.park.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    default User selectByUsername(@Param("username") String username) {
        return selectOne(new QueryWrapper<User>().eq("username", username));
    }
}
