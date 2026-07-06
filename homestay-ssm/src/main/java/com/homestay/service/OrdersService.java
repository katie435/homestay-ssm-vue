package com.homestay.service;

import com.homestay.entity.Orders;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OrdersService {
    List<Orders> findAll();
    List<Orders> getByUserId(String userId);
    int addOrder(Orders orders);
    void cancelOrder(String orderId);
    int countTodayBook();
    int countTodayCheckIn();

    void checkIn(Long id);
    void checkOut(Long id);
    void cleanFinish(Long id);
    void cancelOrder(Long id);

    // ================== 纯追加：通知相关方法 ==================
    List<Orders> getNewOrderNotices();
    List<Orders> getCheckInNotices();
    List<Orders> getCheckOutNotices();

    // ================== 纯追加：待处理事项 ==================
    List<Orders> getTodoOrders(String type);

    // ================== 【只追加这4个，不动其他】 ==================
    int countPendingOrders();   // 待处理
    int countCheckinOrders();   // 待入住
    int countCheckoutOrders();  // 待退房
    int countReviewOrders();    // 待评价
}