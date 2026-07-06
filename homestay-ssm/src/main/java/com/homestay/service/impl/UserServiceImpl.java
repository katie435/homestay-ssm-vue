package com.homestay.service.impl;

import com.homestay.entity.User;
import com.homestay.mapper.UserMapper;
import com.homestay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final int NO_USER = 101;
    public static final int PWD_ERR = 102;
    public static final int SUCCESS = 200;

    private int loginStatus;

    @Autowired
    private UserMapper userMapper;

    // 登录
    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            loginStatus = NO_USER;
            return null;
        }
        if (!user.getPassword().equals(password)) {
            loginStatus = PWD_ERR;
            return null;
        }
        loginStatus = SUCCESS;
        return user;
    }

    // 注册（已添加 20260001 自增ID）
    @Override
    public boolean register(User user) {
        User exist = userMapper.findByUsername(user.getUsername());
        if (exist != null) return false;

        user.setRole("user");

        // ===================== 在这里添加 ID 生成逻辑 =====================
        String maxId = userMapper.getMax2026UserId();
        String newId;
        if(maxId==null){
            newId="20260001";
        }else{
            newId=String.valueOf(Long.parseLong(maxId)+1);
        }
        user.setUserId(newId);
        // ===============================================================

        return userMapper.addUser(user) > 0;
    }

    // =============== 个人中心 ===============
    @Override
    public User getUserInfo(String userId) {
        return userMapper.findByUserId(userId);
    }

    @Override
    public boolean updateUserInfo(User user) {
        return userMapper.updateUser(user) > 0;
    }

    @Override
    public boolean updatePassword(String userId, String oldPwd, String newPwd) {
        return userMapper.updatePassword(userId, oldPwd, newPwd) > 0;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    // 查询所有用户
    @Override
    public List<User> findAllUsers() {
        return userMapper.findAllUsers();
    }

    @Override
    public String getMax2026UserId() {
        return userMapper.getMax2026UserId();
    }

    // ================== 【修复：按 user_id 删除（匹配你的数据库）】 ==================
    @Override
    public void deleteUserById(String userId) {
        userMapper.deleteUserByUserId(userId);
    }
}