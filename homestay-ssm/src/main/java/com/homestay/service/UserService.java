package com.homestay.service;
import com.homestay.entity.User;
import java.util.List;

public interface UserService {
    // 原有
    User login(String username, String password);
    boolean register(User user);

    // 个人中心
    User getUserInfo(String userId);
    boolean updateUserInfo(User user);
    boolean updatePassword(String userId, String oldPwd, String newPwd);

    User findByUsername(String username);

    // 查询所有用户
    List<User> findAllUsers();

    String getMax2026UserId();

    // 【新增】删除用户
    void deleteUserById(String userId);
}