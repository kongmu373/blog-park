package com.kongmu373.park.service;

import com.kongmu373.park.entity.User;
import com.kongmu373.park.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testSelectById() {
        userService.selectById(10);
        verify(userMapper).selectById(10);
    }

    @Test
    void testSelectByUserName() {
        userService.selectByUserName("kongmu1236");
        verify(userMapper).selectByUsername("kongmu1236");
    }

    @Test
    void testThrowUsernameNotFoundExceptionLoadUserByUsername() {
        when(userMapper.selectByUsername("kongmu12")).thenReturn(null);
        Assertions.assertThrows(UsernameNotFoundException.class,()-> userService.loadUserByUsername("kongmu12"));
        verify(userMapper).selectByUsername("kongmu12");
    }

    @Test
    void testReturnUserLoadUserByUsername() {
        when(userMapper.selectByUsername("kongmu1236"))
                .thenReturn(new User()
                        .setUsername("kongmu1236")
                        .setPassword("encodedPassword"));
        UserDetails userDetails = userService.loadUserByUsername("kongmu1236");
        Assertions.assertEquals("kongmu1236", userDetails.getUsername());
        Assertions.assertEquals("encodedPassword", userDetails.getPassword());
        verify(userMapper).selectByUsername("kongmu1236");
    }

    @Test
    void testInsert() {
        when(passwordEncoder.encode("raw")).thenReturn("encoded");
        User insert = userService.insert("kongmu373", "raw");
        verify(passwordEncoder).encode("raw");
        verify(userMapper).insert(insert);
    }
}