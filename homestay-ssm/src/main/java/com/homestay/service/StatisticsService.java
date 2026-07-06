package com.homestay.service;

import com.homestay.entity.Statistics;
import java.util.List;

public interface StatisticsService {
    List<Statistics> listAll();
    int add(Statistics statistics);
}