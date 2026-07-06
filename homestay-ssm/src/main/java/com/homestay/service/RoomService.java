package com.homestay.service;

import com.homestay.entity.Room;
import java.util.List;
import java.util.Map;

public interface RoomService {
    List<Room> findAll();
    Room findById(Integer roomId);
    boolean addRoom(Room room);
    boolean updateRoom(Room room);
    boolean changeStatus(Integer roomId, Integer status);
    boolean changeRoomStatus(Integer roomId, String roomStatus);
    List<Room> getOnSaleRooms();
    Map<String,Object> getDashboardData();
    boolean deleteRoom(Integer roomId);

    //新增
    List<Room> getEmptyRooms();
    List<Room> getCheckInRooms();
    List<Room> getBookedRooms();
    List<Room> getCleanRooms();
}