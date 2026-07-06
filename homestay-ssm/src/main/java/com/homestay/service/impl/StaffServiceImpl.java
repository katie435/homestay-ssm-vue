package com.homestay.service.impl;

import com.homestay.entity.Staff;
import com.homestay.mapper.StaffMapper;
import com.homestay.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;

    @Override
    public Staff login(String staffName, String password, String phone) {
        return staffMapper.login(staffName, password, phone);
    }

    @Override
    public int register(Staff staff) {
        return staffMapper.register(staff);
    }

    @Override
    public Staff findByPhone(String phone) {
        return staffMapper.findByPhone(phone);
    }

    // ===================== 修复 =====================
    @Override
    public List<Staff> getAllStaff() {
        return staffMapper.findAll();
    }

    @Override
    public int deleteStaff(Integer staffId) {
        return staffMapper.deleteById(staffId);
    }

    @Override
    public int updateStaff(Staff staff) {
        return staffMapper.updateStaff(staff);
    }
}