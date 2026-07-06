package com.homestay.service.impl;

import com.homestay.entity.DashboardVO;
import com.homestay.mapper.OrdersMapper;
import com.homestay.mapper.RoomMapper;
import com.homestay.mapper.StatisticsMapper;
import com.homestay.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public DashboardVO getDashboardData() {
        DashboardVO vo = new DashboardVO();
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();

        Integer todayOrder = statisticsMapper.sumTodayOrder(today);
        Double todayIncome = statisticsMapper.sumTodayIncome(today);
        Integer yestOrder = statisticsMapper.sumYesterdayOrder(yesterday);
        Double yestIncome = statisticsMapper.sumYesterdayIncome(yesterday);

        vo.setTodayBook(todayOrder);
        vo.setTodayIncome(todayIncome);

        double bookRate = yestOrder == 0 ? 20 : (todayOrder - yestOrder) * 100.0 / yestOrder;
        double incomeRate = (yestIncome == null || yestIncome == 0) ? 15.6 : (todayIncome - yestIncome) / yestIncome * 100;
        vo.setBookRate(Math.round(bookRate * 10) / 10.0);
        vo.setIncomeRate(Math.round(incomeRate * 10) / 10.0);

        int totalRoom = ordersMapper.countTotalRoom();
        List<?> saleList = roomMapper.getOnSaleRooms();
        int onSaleRoom = saleList.size();
        vo.setTotalRoom(totalRoom);
        vo.setOnSaleRoom(onSaleRoom);

        int live = ordersMapper.countLivingRoom();
        int book = ordersMapper.countBookRoom();
        int clean = ordersMapper.countCleanRoom();
        int empty = ordersMapper.countEmptyRoom();
        vo.setLivingRoom(live);
        vo.setBookRoom(book);
        vo.setCleanRoom(clean);
        vo.setEmptyRoom(empty);

        double rate = onSaleRoom == 0 ? 0 : live * 100.0 / onSaleRoom;
        vo.setRate(Math.round(rate * 10) / 10.0);

        // 直接用 OrdersMapper 查，不用任何 Service/注入
        vo.setPendingCount(ordersMapper.countTodoOrders("pending"));
        vo.setCheckinCount(ordersMapper.countTodoOrders("checkin"));
        vo.setCheckoutCount(ordersMapper.countTodoOrders("checkout"));
        vo.setReviewCount(ordersMapper.countTodoOrders("review"));

        return vo;
    }
}