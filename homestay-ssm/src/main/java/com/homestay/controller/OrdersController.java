package com.homestay.controller;

import com.homestay.entity.Orders;
import com.homestay.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
@RestController
@RequestMapping("/api/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    // 统一返回结果
    private Map<String, Object> result(String msg, Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", msg);
        res.put("data", data);
        return res;
    }

    @GetMapping("/list")
    public Map<String, Object> list() {
        try {
            List<Orders> list = ordersService.findAll();
            return result("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return result("查询失败", null);
        }
    }

    @GetMapping("/getMyOrder")
    public Map<String, Object> getMyOrder(@RequestParam String userId) {
        List<Orders> myList = ordersService.getByUserId(userId);
        return result("查询成功", myList);
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Map<String, Object> params) {
        try {
            Orders orders = new Orders();
            orders.setOrderId((String) params.get("orderId"));
            orders.setRoomId(Long.valueOf(params.get("roomId").toString()));
            orders.setRoomName((String) params.get("roomName"));
            orders.setRoomImg((String) params.get("roomImg"));
            orders.setStartTime((String) params.get("startTime"));
            orders.setEndTime((String) params.get("endTime"));
            orders.setRoomCount(Integer.valueOf(params.get("roomCount").toString()));
            orders.setPeopleCount(Integer.valueOf(params.get("peopleCount").toString()));
            orders.setContactPhone((String) params.get("contactPhone"));
            orders.setTotalPrice(Integer.valueOf(params.get("totalPrice").toString()));
            orders.setPayMethod((String) params.get("payMethod"));
            orders.setPayStatus(Integer.valueOf(params.get("payStatus").toString()));
            orders.setOrderStatus(Integer.valueOf(params.get("orderStatus").toString()));
            orders.setCreateTime((String) params.get("createTime"));

            orders.setUserName((String) params.get("customerName"));

            String userId = params.get("userId").toString();
            orders.setUserId(userId);

            if (orders.getOrderId() == null || orders.getOrderId().isEmpty()) {
                String orderId = "ORDER" + System.currentTimeMillis();
                orders.setOrderId(orderId);
            }

            if (orders.getCreateTime() == null) {
                orders.setCreateTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
            }

            System.out.println("即将插入数据库的订单：" + orders);
            ordersService.addOrder(orders);
            System.out.println("订单插入方法执行成功！");

            return result("下单成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return result("下单失败", null);
        }
    }

    @PostMapping("/cancel")
    public Map<String, Object> cancel(@RequestBody Map<String,Long> body){
        try{
            Long id = body.get("id");
            ordersService.cancelOrder(id);
            return result("取消成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return result("取消失败", null);
        }
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        Map<String, Object> map = new HashMap<>();
        int todayBook = ordersService.countTodayBook();
        int todayCheckIn = ordersService.countTodayCheckIn();
        map.put("code", 200);
        map.put("todayBook", todayBook);
        map.put("todayCheckIn", todayCheckIn);
        return map;
    }

    @PostMapping("/checkIn")
    public Map<String, Object> checkIn(@RequestBody Map<String, Long> body){
        try{
            Long id = body.get("id");
            ordersService.checkIn(id);
            return result("办理成功",null);
        }catch (Exception e){
            e.printStackTrace();
            return result("办理失败",null);
        }
    }

    @PostMapping("/checkOut")
    public Map<String, Object> checkOut(@RequestBody Map<String, Long> body){
        try{
            Long id = body.get("id");
            ordersService.checkOut(id);
            return result("办理成功",null);
        }catch (Exception e){
            e.printStackTrace();
            return result("办理失败",null);
        }
    }

    @PostMapping("/cleanFinish")
    public Map<String, Object> cleanFinish(@RequestBody Map<String, Long> body){
        try{
            Long id = body.get("id");
            ordersService.cleanFinish(id);
            return result("打扫完成",null);
        }catch (Exception e){
            e.printStackTrace();
            return result("操作失败",null);
        }
    }

    // ================== 纯追加：系统通知接口 ==================
    @GetMapping("/notices")
    public Map<String, Object> getNotices() {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, String>> list = new ArrayList<>();

        try {
            // 1. 新订单
            List<Orders> newOrders = ordersService.getNewOrderNotices();
            for (Orders o : newOrders) {
                Map<String, String> item = new HashMap<>();
                item.put("title", "新订单#" + o.getOrderId());

                // 强制截取 HH:mm 格式，杜绝多余字符
                String time = "未知";
                if (o.getCreateTime() != null) {
                    String ct = o.getCreateTime().trim();
                    if (ct.length() >= 16) {
                        // 标准格式，直接取 11-16 位（HH:mm）
                        time = ct.substring(11, 16);
                    } else if (ct.contains(":")) {
                        // 非标准格式，取前 5 位（HH:mm）
                        time = ct.split(" ")[0].substring(0, 5);
                    }
                }
                item.put("time", time);

                String userName = (o.getUserName() == null || o.getUserName().isEmpty()) ? "未知用户" : o.getUserName();
                item.put("content", userName + " 预订了 " + o.getRoomName());
                list.add(item);
            }

            // 2. 已入住
            List<Orders> checkInOrders = ordersService.getCheckInNotices();
            for (Orders o : checkInOrders) {
                Map<String, String> item = new HashMap<>();
                item.put("title", "入住通知");

                String time = "未知";
                if (o.getCreateTime() != null) {
                    String ct = o.getCreateTime().trim();
                    if (ct.length() >= 16) {
                        time = ct.substring(11, 16);
                    } else if (ct.contains(":")) {
                        time = ct.split(" ")[0].substring(0, 5);
                    }
                }
                item.put("time", time);

                String userName = (o.getUserName() == null || o.getUserName().isEmpty()) ? "未知用户" : o.getUserName();
                item.put("content", userName + " 已入住 " + o.getRoomName());
                list.add(item);
            }

            // 3. 待打扫
            List<Orders> checkOutOrders = ordersService.getCheckOutNotices();
            for (Orders o : checkOutOrders) {
                Map<String, String> item = new HashMap<>();
                item.put("title", "待打扫");

                String time = "未知";
                if (o.getCreateTime() != null) {
                    String ct = o.getCreateTime().trim();
                    if (ct.length() >= 16) {
                        time = ct.substring(11, 16);
                    } else if (ct.contains(":")) {
                        time = ct.split(" ")[0].substring(0, 5);
                    }
                }
                item.put("time", time);

                item.put("content", o.getRoomName() + " 已退房，请打扫");
                list.add(item);
            }

            map.put("code", 200);
            map.put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 200);
            map.put("data", list);
        }
        return map;
    }

    // ================== 纯追加：待处理事项订单接口 ==================
    @GetMapping("/todo/{type}")
    public Map<String, Object> getTodoOrders(@PathVariable String type) {
        try {
            List<Orders> list = ordersService.getTodoOrders(type);
            return result("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return result("查询失败", null);
        }
    }
}