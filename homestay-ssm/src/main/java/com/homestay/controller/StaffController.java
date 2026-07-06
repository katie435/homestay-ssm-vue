package com.homestay.controller;

import com.homestay.entity.Staff;
import com.homestay.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @PostMapping("/staff/login")
    public Map<String, Object> login(@RequestBody Staff staff) {
        Map<String, Object> map = new HashMap<>();

        String username = staff.getUsername();
        String password = staff.getPassword();
        String phone = staff.getPhone();

        Staff dbStaff = staffService.login(username, password, phone);

        if (dbStaff != null) {
            map.put("code", 200);
            map.put("data", dbStaff);
        } else {
            map.put("code", 500);
            map.put("msg", "账号密码手机号错误");
        }
        return map;
    }

    // ===================== 修复版接口 =====================
    @GetMapping("/employee/list")
    public Map<String, Object> list() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("data", staffService.getAllStaff());
        return map;
    }

    @PostMapping("/employee/add")
    public Map<String, Object> add(@RequestBody Staff staff) {
        Map<String, Object> map = new HashMap<>();
        staff.setRole("staff");
        staffService.register(staff);
        map.put("code", 200);
        map.put("msg", "添加成功");
        return map;
    }

    @PostMapping("/employee/update")
    public Map<String, Object> update(@RequestBody Staff staff) {
        Map<String, Object> map = new HashMap<>();
        staffService.updateStaff(staff);
        map.put("code", 200);
        map.put("msg", "修改成功");
        return map;
    }

    // 这里修复！参数名从 id → staffId
    @GetMapping("/employee/delete")
    public Map<String, Object> delete(@RequestParam("staffId") Integer staffId) {
        Map<String, Object> map = new HashMap<>();
        staffService.deleteStaff(staffId);
        map.put("code", 200);
        map.put("msg", "删除成功");
        return map;
    }
}