package com.homestay.service;

import com.homestay.entity.Admin;
import java.util.List;

public interface AdminService {
    Admin login(String username, String password, String phone);

    List<Admin> listAdmins();

    boolean addAdmin(Admin admin);

    boolean updateAdmin(Admin admin);

    boolean deleteAdmin(Integer adminId);

    boolean updateStatus(Integer adminId, Integer status);
}