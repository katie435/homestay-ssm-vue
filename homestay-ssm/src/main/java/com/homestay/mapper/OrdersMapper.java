package com.homestay.mapper;

import com.homestay.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrdersMapper {

    // 原有功能 完全保留不动
    List<Orders> findAll();
    int addOrder(Orders orders);
    List<Orders> getByUserId(String userId);
    int cancelOrder(String orderId);

    // 工作台统计 只更换SQL语句，方法名不变
    int countTodayBook();

    int countTodayCheckIn();

    int countTodayCheckOut();

    Double sumTodayIncome();

    int countWaitConfirm();

    int countWaitIn();

    int countWaitOut();

    int countWaitEvaluate();

    int countTotalRoom();

    int countEmptyRoom();

    int countLivingRoom();

    int countBookRoom();

    int countCleanRoom();

    //新增更新状态方法，去掉@Param
    int updateById(Orders orders);

    List<String> getSevenDay();
    List<Integer> getSevenOrder();
    List<Double> getSevenIncome();
    List<Double> getSevenRate();

    // ================== 纯追加：系统通知接口（不动其他） ==================
    List<Orders> getNewOrderNotices();
    List<Orders> getCheckInNotices();
    List<Orders> getCheckOutNotices();

    List<Orders> getTodoOrders(String type);

    int countTodoOrders(String type);
}