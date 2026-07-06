package com.homestay.service.impl;

import com.homestay.entity.Admin;
import com.homestay.mapper.AdminMapper;
import com.homestay.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String username, String password, String phone) {
        return adminMapper.login(username, password, phone);
    }

    @Override
    public List<Admin> listAdmins() {
        return adminMapper.listAdmins();
    }

    @Override
    public boolean addAdmin(Admin admin) {
        return adminMapper.insertAdmin(admin) > 0;
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        return adminMapper.updateAdmin(admin) > 0;
    }

    @Override
    public boolean deleteAdmin(Integer adminId) {
        return adminMapper.deleteAdmin(adminId) > 0;
    }

    @Override
    public boolean updateStatus(Integer adminId, Integer status) {
        return adminMapper.updateStatus(adminId, status) > 0;
    }
}