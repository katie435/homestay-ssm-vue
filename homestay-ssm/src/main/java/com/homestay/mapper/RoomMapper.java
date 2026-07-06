package com.homestay.mapper;

import com.homestay.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RoomMapper {
    List<Room> findAll();
    Room findById(Integer roomId);
    List<Room> getOnSaleRooms();
    int addRoom(Room room);
    int updateRoom(Room room);
    int changeStatus(Integer roomId, Integer status);
    int changeRoomStatus(Integer roomId, String roomStatus);
    int deleteRoom(Integer roomId);
    Integer getMinFreeRoomId();

    // ======================== ✅ 新增 ========================
    List<Room> getEmptyRooms();
    List<Room> getCheckInRooms();
    List<Room> getBookedRooms();
    List<Room> getCleanRooms();
}