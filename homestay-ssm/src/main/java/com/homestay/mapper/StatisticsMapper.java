package com.homestay.mapper;

import com.homestay.entity.Statistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.Date;
import java.util.List;

@Mapper
public interface StatisticsMapper {
    List<Statistics> selectAll();
    int insert(Statistics statistics);

    @Select("select IFNULL(sum(order_count),0) from statistics where stat_date=#{today}")
    Integer sumTodayOrder(Date today);

    @Select("select IFNULL(sum(income),0) from statistics where stat_date=#{today}")
    Double sumTodayIncome(Date today);

    @Select("select IFNULL(sum(order_count),0) from statistics where stat_date=#{yesterday}")
    Integer sumYesterdayOrder(Date yesterday);

    @Select("select IFNULL(sum(income),0) from statistics where stat_date=#{yesterday}")
    Double sumYesterdayIncome(Date yesterday);
}