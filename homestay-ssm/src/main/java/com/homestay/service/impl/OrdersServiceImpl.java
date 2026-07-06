package com.homestay.service.impl;

import com.homestay.entity.Orders;
import com.homestay.mapper.OrdersMapper;
import com.homestay.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public List<Orders> findAll() {
        return ordersMapper.findAll();
    }

    @Override
    public int addOrder(Orders orders) {
        return ordersMapper.addOrder(orders);
    }

    @Override
    public void cancelOrder(String orderId) {
        ordersMapper.cancelOrder(orderId);
    }

    @Override
    public void cancelOrder(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        orders.setOrderStatus(3);
        ordersMapper.updateById(orders);
    }

    @Override
    public List<Orders> getByUserId(String userId) {
        return ordersMapper.getByUserId(userId);
    }

    @Override
    public int countTodayBook() {
        return ordersMapper.countTodayBook();
    }

    @Override
    public int countTodayCheckIn() {
        return ordersMapper.countTodayCheckIn();
    }

    @Override
    public void checkIn(Long id) {
        Orders o = new Orders();
        o.setId(id);
        o.setOrderStatus(1);
        ordersMapper.updateById(o);
    }

    @Override
    public void checkOut(Long id) {
        Orders o = new Orders();
        o.setId(id);
        o.setOrderStatus(2);
        ordersMapper.updateById(o);
    }

    @Override
    public void cleanFinish(Long id) {
        Orders o = new Orders();
        o.setId(id);
        o.setOrderStatus(4);
        ordersMapper.updateById(o);
    }

    @Override
    public List<Orders> getNewOrderNotices() {
        return ordersMapper.getNewOrderNotices();
    }

    @Override
    public List<Orders> getCheckInNotices() {
        return ordersMapper.getCheckInNotices();
    }

    @Override
    public List<Orders> getCheckOutNotices() {
        return ordersMapper.getCheckOutNotices();
    }

    @Override
    public List<Orders> getTodoOrders(String type) {
        return ordersMapper.getTodoOrders(type);
    }

    // ================== 【只追加这4个，不动其他】 ==================
    @Override
    public int countPendingOrders() {
        return ordersMapper.countTodoOrders("pending");
    }

    @Override
    public int countCheckinOrders() {
        return ordersMapper.countTodoOrders("checkin");
    }

    @Override
    public int countCheckoutOrders() {
        return ordersMapper.countTodoOrders("checkout");
    }

    @Override
    public int countReviewOrders() {
        return ordersMapper.countTodoOrders("review");
    }
}