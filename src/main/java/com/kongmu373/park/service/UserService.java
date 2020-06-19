package com.kongmu373.park.service;

import com.kongmu373.park.entity.User;
import com.kongmu373.park.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class UserService {

    private UserMapper userMapper;

    @Inject
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }
}
