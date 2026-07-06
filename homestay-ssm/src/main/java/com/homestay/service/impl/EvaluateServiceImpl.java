package com.homestay.service.impl;

import com.homestay.entity.Evaluate;
import com.homestay.mapper.EvaluateMapper;
import com.homestay.service.EvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EvaluateServiceImpl implements EvaluateService {
    @Autowired
    private EvaluateMapper evaluateMapper;

    @Override
    public boolean addEvaluate(Evaluate evaluate) {
        evaluate.setEvaId(UUID.randomUUID().toString().replace("-",""));
        evaluate.setEvaTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return evaluateMapper.add(evaluate) > 0;
    }

    @Override
    public List<Evaluate> getList(String userId) {
        return evaluateMapper.getList(userId);
    }

    @Override
    public List<Evaluate> getAllList() {
        return evaluateMapper.getAllList();
    }

    @Override
    public boolean updateReply(String evaId, String reply) {
        return evaluateMapper.updateReply(evaId, reply) > 0;
    }
}