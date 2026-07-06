package com.homestay.controller;

import com.homestay.entity.Room;
import com.homestay.service.StaffService;
import com.homestay.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@ResponseBody
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private StaffService staffService;

    // ======================== 核心：员工房源列表（正确接口） ========================
    @GetMapping("/staff/list")
    public Map<String, Object> list() {
        Map<String, Object> map = new HashMap<>();
        List<Room> list = roomService.findAll();
        map.put("code", 0);
        map.put("data", list);
        return map;
    }

    // ======================== 详情 ========================
    @GetMapping("/detail")
    public Map<String, Object> detail(Integer id) {
        Map<String, Object> map = new HashMap<>();
        Room room = roomService.findById(id);
        map.put("code", 0);
        map.put("data", room);
        return map;
    }

    // ======================== 客户端列表 ========================
    @GetMapping("/client/list")
    public Map<String, Object> clientList() {
        Map<String, Object> map = new HashMap<>();
        List<Room> list = roomService.getOnSaleRooms();
        map.put("code", 0);
        map.put("data", list);
        return map;
    }

    // ======================== 删除 ========================
    @GetMapping("/staff/delete")
    public Map<String, Object> deleteStaffGet(Integer roomId) {
        Map<String, Object> map = new HashMap<>();
        boolean ok = roomService.deleteRoom(roomId);
        map.put("code", ok ? 0 : 1);
        map.put("msg", ok ? "删除成功" : "删除失败");
        return map;
    }

    // ======================== ✅ 添加（已修复图片上传） ========================
    @PostMapping("/staff/add")
    public Map<String, Object> add(
            Room room,
            @RequestParam(value = "imgFile", required = false) MultipartFile imgFile
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String uploadPath = "D:/upload/room/";
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            if (imgFile != null && !imgFile.isEmpty()) {
                String originalFilename = imgFile.getOriginalFilename();
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                String fileName = UUID.randomUUID() + suffix;
                imgFile.transferTo(new File(dir, fileName));
                room.setImg("/upload/room/" + fileName);
            } else {
                room.setImg("/upload/room/default.png");
            }

            room.setStatus(1);
            room.setRoomStatus("empty");

            boolean ok = roomService.addRoom(room);
            map.put("code", ok ? 0 : 1);
            map.put("msg", ok ? "添加成功" : "添加失败");

        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 500);
            map.put("msg", "服务器错误：" + e.getMessage());
        }
        return map;
    }

    // ======================== ✅ 修改（已修复图片上传） ========================
    @PostMapping("/staff/update")
    public Map<String, Object> update(
            Room room,
            @RequestParam(value = "imgFile", required = false) MultipartFile imgFile
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String uploadPath = "D:/upload/room/";
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            if (imgFile != null && !imgFile.isEmpty()) {
                String originalFilename = imgFile.getOriginalFilename();
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                String fileName = UUID.randomUUID() + suffix;
                imgFile.transferTo(new File(dir, fileName));
                room.setImg("/upload/room/" + fileName);
            }

            boolean ok = roomService.updateRoom(room);
            map.put("code", ok ? 0 : 1);
            map.put("msg", ok ? "修改成功" : "修改失败");

        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 500);
            map.put("msg", "服务器错误：" + e.getMessage());
        }
        return map;
    }

    // ======================== 上下架（前端用 GET） ========================
    @GetMapping("/staff/status")
    public Map<String, Object> statusGet(
            @RequestParam("roomId") Integer roomId,
            @RequestParam("status") Integer status
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            System.out.println("上下架请求：roomId=" + roomId + ", status=" + status);
            boolean ok = roomService.changeStatus(roomId, status);
            map.put("code", ok ? 0 : 1);
            map.put("msg", ok ? "状态更新成功" : "更新失败");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 500);
            map.put("msg", "服务器错误：" + e.getMessage());
        }
        return map;
    }

    // ======================== 修改房态（前端用 GET） ========================
    @GetMapping("/staff/roomStatus")
    public Map<String, Object> roomStatusGet(
            @RequestParam("roomId") Integer roomId,
            @RequestParam("roomStatus") String roomStatus
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            System.out.println("房态修改请求：roomId=" + roomId + ", roomStatus=" + roomStatus);
            boolean ok = roomService.changeRoomStatus(roomId, roomStatus);
            map.put("code", ok ? 0 : 1);
            map.put("msg", ok ? "房态更新成功" : "更新失败");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 500);
            map.put("msg", "服务器错误：" + e.getMessage());
        }
        return map;
    }

    // ======================== 管理员看板 ========================
    @GetMapping("/admin/dashboard")
    public Map<String, Object> dashboard() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> data = roomService.getDashboardData();
        map.put("code", 0);
        map.put("data", data);
        return map;
    }

    // ======================== ✅ 新增：房态详情（只加这里，不动其他） ========================
    @GetMapping("/empty")
    public Map<String, Object> getEmptyRooms() {
        Map<String, Object> map = new HashMap<>();
        List<Room> list = roomService.getEmptyRooms();
        map.put("code", 0);
        map.put("data", list);
        return map;
    }

    @GetMapping("/checkin")
    public Map<String, Object> getCheckInRooms() {
        Map<String, Object> map = new HashMap<>();
        List<Room> list = roomService.getCheckInRooms();
        map.put("code", 0);
        map.put("data", list);
        return map;
    }

    @GetMapping("/book")
    public Map<String, Object> getBookedRooms() {
        Map<String, Object> map = new HashMap<>();
        List<Room> list = roomService.getBookedRooms();
        map.put("code", 0);
        map.put("data", list);
        return map;
    }

    @GetMapping("/clean")
    public Map<String, Object> getCleanRooms() {
        Map<String, Object> map = new HashMap<>();
        List<Room> list = roomService.getCleanRooms();
        map.put("code", 0);
        map.put("data", list);
        return map;
    }

}