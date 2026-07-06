package com.homestay.mapper;

import com.homestay.entity.Staff;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface StaffMapper {

    @Select("SELECT * FROM staff WHERE staff_name=#{staffName} AND password=#{password} AND phone=#{phone}")
    Staff login(@Param("staffName") String staffName,
                @Param("password") String password,
                @Param("phone") String phone);

    // 👇 绝对正确
    @Insert("INSERT INTO staff(staff_name, phone, password, role, position) VALUES(#{staffName}, #{phone}, #{password}, #{role}, #{position})")
    int register(Staff staff);

    @Select("SELECT * FROM staff WHERE phone=#{phone}")
    Staff findByPhone(String phone);

    @Select("SELECT * FROM staff")
    List<Staff> findAll();

    @Delete("DELETE FROM staff WHERE staff_id=#{staffId}")
    int deleteById(Integer staffId);

    // 👇 绝对正确
    @Update("UPDATE staff SET staff_name=#{staffName}, phone=#{phone}, password=#{password}, role=#{role}, position=#{position} WHERE staff_id=#{staffId}")
    int updateStaff(Staff staff);
}