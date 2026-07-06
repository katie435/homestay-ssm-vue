package com.homestay.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Statistics {
    private Integer statId;
    private Integer roomId;
    private Integer orderCount;
    private BigDecimal income;
    private Date statDate;

    public Statistics() {}

    public Statistics(Integer statId, Integer roomId, Integer orderCount, BigDecimal income, Date statDate) {
        this.statId = statId;
        this.roomId = roomId;
        this.orderCount = orderCount;
        this.income = income;
        this.statDate = statDate;
    }

    public Integer getStatId() { return statId; }
    public void setStatId(Integer statId) { this.statId = statId; }
    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }
    public Integer getOrderCount() { return orderCount; }
    public void setOrderCount(Integer orderCount) { this.orderCount = orderCount; }
    public BigDecimal getIncome() { return income; }
    public void setIncome(BigDecimal income) { this.income = income; }
    public Date getStatDate() { return statDate; }
    public void setStatDate(Date statDate) { this.statDate = statDate; }

    @Override
    public String toString() {
        return "Statistics{" +
                "statId=" + statId +
                ", roomId=" + roomId +
                ", orderCount=" + orderCount +
                ", income=" + income +
                ", statDate=" + statDate +
                '}';
    }
}