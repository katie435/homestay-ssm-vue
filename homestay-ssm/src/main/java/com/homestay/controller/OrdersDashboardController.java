package com.homestay.controller;

import com.homestay.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrdersDashboardController {

    @Autowired
    private OrdersMapper ordersMapper;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        Map<String, Object> map = new HashMap<>();

        int todayBook = ordersMapper.countTodayBook();
        Double todayIncome = ordersMapper.sumTodayIncome();

        int totalRoom = ordersMapper.countTotalRoom();
        int emptyRoom = ordersMapper.countEmptyRoom();
        int livingRoom = ordersMapper.countLivingRoom();
        int bookRoom = ordersMapper.countBookRoom();
        int cleanRoom = ordersMapper.countCleanRoom();

        double rate = 0.0;
        if (totalRoom > 0) {
            rate = (double) livingRoom / totalRoom * 100;
        }

        map.put("todayBook", todayBook);
        map.put("todayIncome", todayIncome);
        map.put("livingRoom", livingRoom);
        map.put("totalRoom", totalRoom);
        map.put("emptyRoom", emptyRoom);
        map.put("bookRoom", bookRoom);
        map.put("cleanRoom", cleanRoom);
        map.put("onSaleRoom", totalRoom);
        map.put("bookRate", 20.0);
        map.put("incomeRate", 15.6);
        map.put("rate", Math.round(rate * 10) / 10.0);

        // ===================== 以下纯追加，不动原有代码 =====================
        map.put("pendingCount", ordersMapper.countTodoOrders("pending"));
        map.put("checkinCount", ordersMapper.countTodoOrders("checkin"));
        map.put("checkoutCount", ordersMapper.countTodoOrders("checkout"));
        map.put("reviewCount", ordersMapper.countTodoOrders("review"));

        return map;
    }

    @GetMapping("/sevenData")
    public Map<String,Object> getSevenData(){
        Map<String,Object> map = new HashMap<>();
        map.put("days",ordersMapper.getSevenDay());
        map.put("order",ordersMapper.getSevenOrder());
        map.put("income",ordersMapper.getSevenIncome());
        map.put("rate",ordersMapper.getSevenRate());
        return map;
    }
}