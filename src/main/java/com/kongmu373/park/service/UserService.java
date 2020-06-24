package com.kongmu373.park.service;

import com.kongmu373.park.entity.User;
import com.kongmu373.park.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserMapper userMapper;

    private PasswordEncoder passwordEncoder;

    @Inject
    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public User selectByUserName(String username) {
        return userMapper.selectByUsername(username);
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(selectByUserName(authentication == null ? null : authentication.getName()));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = selectByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " 不存在");
        }
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), Collections.emptyList());
    }

    public User insert(String username, String password) {
        User user = new User()
                .setUsername(username)
                .setPassword(passwordEncoder.encode(password))
                .setCreatedAt(Instant.now())
                .setUpdatedAt(Instant.now())
                .setAvatar("");
        userMapper.insert(user);
        return user;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123456"));
    }


}
