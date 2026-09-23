package com.jr.mapper;

import com.jr.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE account = #{account}")
    User selectUserByAccount(@Param("account") String account);

    @Select("SELECT role_code FROM user_role WHERE user_id = #{userId}")
    List<String> selectRoleCodeByUserId(@Param("userId") Integer userId);

    @Insert("INSERT INTO user(username, account, password, email, avatar, create_time) " +
            "VALUES(#{username}, #{account}, #{password}, #{email}, #{avatar}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "user_id")
    int insertUser(User user);
}