package com.homestay.service.impl;

import com.homestay.entity.Room;
import com.homestay.mapper.RoomMapper;
import com.homestay.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public List<Room> findAll() {
        return roomMapper.findAll();
    }

    @Override
    public Room findById(Integer roomId) {
        return roomMapper.findById(roomId);
    }

    @Override
    public boolean addRoom(Room room) {
        // 前端页面手动输入roomId，直接入库，不再自动生成ID
        return roomMapper.addRoom(room) > 0;
    }

    @Override
    public boolean updateRoom(Room room) {
        return roomMapper.updateRoom(room) > 0;
    }

    @Override
    public boolean changeStatus(Integer roomId, Integer status) {
        return roomMapper.changeStatus(roomId, status) > 0;
    }

    @Override
    public boolean changeRoomStatus(Integer roomId, String roomStatus) {
        return roomMapper.changeRoomStatus(roomId, roomStatus) > 0;
    }

    @Override
    public List<Room> getOnSaleRooms() {
        return roomMapper.getOnSaleRooms();
    }

    @Override
    public Map<String, Object> getDashboardData() {
        List<Room> list = findAll();
        int total = list.size();
        long onSale = list.stream().filter(r -> r.getStatus() == 1).count();
        long empty = list.stream().filter(r -> "empty".equals(r.getRoomStatus())).count();

        Map<String, Object> map = new HashMap<>();
        map.put("totalRoom", total);
        map.put("onSale", onSale);
        map.put("empty", empty);
        map.put("occupancy", total == 0 ? 0 : ((total - empty) * 100 / total));
        return map;
    }

    @Override
    public boolean deleteRoom(Integer roomId) {
        return roomMapper.deleteRoom(roomId) > 0;
    }

    // ======================== 【新增：房态列表查询，只加了这里】 ========================
    @Override
    public List<Room> getEmptyRooms() {
        return roomMapper.getEmptyRooms();
    }

    @Override
    public List<Room> getCheckInRooms() {
        return roomMapper.getCheckInRooms();
    }

    @Override
    public List<Room> getBookedRooms() {
        return roomMapper.getBookedRooms();
    }

    @Override
    public List<Room> getCleanRooms() {
        return roomMapper.getCleanRooms();
    }
}