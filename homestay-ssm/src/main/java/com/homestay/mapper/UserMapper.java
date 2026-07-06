package com.homestay.mapper;

import com.homestay.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {

    User findByUsername(String username);
    List<User> findAll();
    int addUser(User user);
    int updateUser(User user);
    int deleteUser(Integer id);
    User findById(Integer id);
    List<User> findAllUsers();
    User findByUserId(String userId);
    int updatePassword(String userId, String oldPwd, String newPwd);
    String getMax2026UserId();

    // 新增：根据 userId 删除用户
    int deleteUserByUserId(String userId);
}