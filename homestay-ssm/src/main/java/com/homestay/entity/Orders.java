package com.homestay.entity;

public class Orders {
    private Long id;
    private String orderId;
    private Long roomId;
    private String roomName;
    private String roomImg;
    private String startTime;
    private String endTime;
    private Integer roomCount;
    private Integer peopleCount;
    private String contactPhone;
    private Integer totalPrice;
    private String payMethod;
    private Integer payStatus;
    private Integer orderStatus;
    private String createTime;
    private String userId;

    // ================== 【我加的：预订人姓名】 ==================
    private String userName;

    // 无参构造
    public Orders() {
    }

    // ================== 【我加的：userName 的 get/set】 ==================
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // GETTER / SETTER 必须写（老师要求）
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomImg() {
        return roomImg;
    }

    public void setRoomImg(String roomImg) {
        this.roomImg = roomImg;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", roomImg='" + roomImg + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", roomCount=" + roomCount +
                ", peopleCount=" + peopleCount +
                ", contactPhone='" + contactPhone + '\'' +
                ", totalPrice=" + totalPrice +
                ", payMethod='" + payMethod + '\'' +
                ", payStatus=" + payStatus +
                ", orderStatus=" + orderStatus +
                ", createTime='" + createTime + '\'' +
                ", userName=" + userName + '\'' +
                '}';
    }
}