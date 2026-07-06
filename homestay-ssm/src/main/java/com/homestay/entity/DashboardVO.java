package com.homestay.entity;

public class DashboardVO {
    private Integer todayBook;
    private Double todayIncome;
    private Integer livingRoom;
    private Integer totalRoom;
    private Integer emptyRoom;
    private Integer bookRoom;
    private Integer cleanRoom;
    private Integer onSaleRoom;
    private Double bookRate;
    private Double incomeRate;
    private Double rate;

    // ================== 【只追加这4个字段，不动其他】 ==================
    private Integer pendingCount;   // 待处理订单
    private Integer checkinCount;   // 待入住
    private Integer checkoutCount;  // 待退房
    private Integer reviewCount;    // 待评价

    //get set 完全保留你原来的
    public Integer getTodayBook() {return todayBook;}
    public void setTodayBook(Integer todayBook) {this.todayBook = todayBook;}
    public Double getTodayIncome() {return todayIncome;}
    public void setTodayIncome(Double todayIncome) {this.todayIncome = todayIncome;}
    public Integer getLivingRoom() {return livingRoom;}
    public void setLivingRoom(Integer livingRoom) {this.livingRoom = livingRoom;}
    public Integer getTotalRoom() {return totalRoom;}
    public void setTotalRoom(Integer totalRoom) {this.totalRoom = totalRoom;}
    public Integer getEmptyRoom() {return emptyRoom;}
    public void setEmptyRoom(Integer emptyRoom) {this.emptyRoom = emptyRoom;}
    public Integer getBookRoom() {return bookRoom;}
    public void setBookRoom(Integer bookRoom) {this.bookRoom = bookRoom;}
    public Integer getCleanRoom() {return cleanRoom;}
    public void setCleanRoom(Integer cleanRoom) {this.cleanRoom = cleanRoom;}
    public Integer getOnSaleRoom() {return onSaleRoom;}
    public void setOnSaleRoom(Integer onSaleRoom) {this.onSaleRoom = onSaleRoom;}
    public Double getBookRate() {return bookRate;}
    public void setBookRate(Double bookRate) {this.bookRate = bookRate;}
    public Double getIncomeRate() {return incomeRate;}
    public void setIncomeRate(Double incomeRate) {this.incomeRate = incomeRate;}
    public Double getRate() {return rate;}
    public void setRate(Double rate) {this.rate = rate;}

    // ================== 【只追加这4个 getter/setter】 ==================
    public Integer getPendingCount() {return pendingCount;}
    public void setPendingCount(Integer pendingCount) {this.pendingCount = pendingCount;}
    public Integer getCheckinCount() {return checkinCount;}
    public void setCheckinCount(Integer checkinCount) {this.checkinCount = checkinCount;}
    public Integer getCheckoutCount() {return checkoutCount;}
    public void setCheckoutCount(Integer checkoutCount) {this.checkoutCount = checkoutCount;}
    public Integer getReviewCount() {return reviewCount;}
    public void setReviewCount(Integer reviewCount) {this.reviewCount = reviewCount;}
}