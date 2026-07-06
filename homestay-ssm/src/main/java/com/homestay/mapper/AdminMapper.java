package com.homestay.mapper;

import com.homestay.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {
    // 管理员登录
    Admin login(@Param("username") String username,
                @Param("password") String password,
                @Param("phone") String phone);

    // 查询所有管理员（用于AdminManage.vue列表）
    List<Admin> listAdmins();

    // 新增管理员
    int insertAdmin(Admin admin);

    // 修改管理员
    int updateAdmin(Admin admin);

    // 删除管理员
    int deleteAdmin(Integer adminId);

    // 修改状态（启用/禁用）
    int updateStatus(@Param("adminId") Integer adminId,
                     @Param("status") Integer status);
}