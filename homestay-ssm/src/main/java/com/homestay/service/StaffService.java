package com.homestay.service;

import com.homestay.entity.Staff;
import java.util.List;

public interface StaffService {
    Staff login(String staffName, String password, String phone);
    int register(Staff staff);
    Staff findByPhone(String phone);

    List<Staff> getAllStaff();
    int deleteStaff(Integer staffId);
    int updateStaff(Staff staff);
}