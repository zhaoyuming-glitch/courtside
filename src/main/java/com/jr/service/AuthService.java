package com.jr.service;



public interface AuthService {
    /**
     * 注册
     * @return 0=成功，1=账号已存在
     */
    int register(String username, String account, String password);
}
