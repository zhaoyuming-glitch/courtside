package com.jr.service.impl;

import com.jr.entity.User;
import com.jr.mapper.UserMapper;
import com.jr.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public int register(String username, String account, String password) {
        User exist = userMapper.selectUserByAccount(account);
        if (exist != null) {
            return 1;
        }

        String encoded = passwordEncoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setAccount(account);
        user.setPassword(encoded);
        user.setEmail("");
        user.setAvatar("");
        userMapper.insertUser(user);

        return 0;
    }
}