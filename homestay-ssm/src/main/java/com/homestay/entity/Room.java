package com.homestay.entity;

public class Room {
    private Integer roomId;
    private String roomName;
    private Integer price;
    private String img;
    private String desc;

    // 员工/管理新增字段
    private String categoryId;
    private String roomType;
    private Integer status;
    private String roomStatus;
    private String tags;
    private String code;
    private String createTime;

    // getter + setter
    public Integer getRoomId() {return roomId;}
    public void setRoomId(Integer roomId) {this.roomId = roomId;}
    public String getRoomName() {return roomName;}
    public void setRoomName(String roomName) {this.roomName = roomName;}
    public Integer getPrice() {return price;}
    public void setPrice(Integer price) {this.price = price;}
    public String getImg() {return img;}
    public void setImg(String img) {this.img = img;}
    public String getDesc() {return desc;}
    public void setDesc(String desc) {this.desc = desc;}
    public String getCategoryId() {return categoryId;}
    public void setCategoryId(String categoryId) {this.categoryId = categoryId;}
    public String getRoomType() {return roomType;}
    public void setRoomType(String roomType) {this.roomType = roomType;}
    public Integer getStatus() {return status;}
    public void setStatus(Integer status) {this.status = status;}
    public String getRoomStatus() {return roomStatus;}
    public void setRoomStatus(String roomStatus) {this.roomStatus = roomStatus;}
    public String getTags() {return tags;}
    public void setTags(String tags) {this.tags = tags;}
    public String getCode() {return code;}
    public void setCode(String code) {this.code = code;}
    public String getCreateTime() {return createTime;}
    public void setCreateTime(String createTime) {this.createTime = createTime;}
}