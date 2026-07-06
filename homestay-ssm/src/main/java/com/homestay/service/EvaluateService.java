package com.homestay.service;
import com.homestay.entity.Evaluate;
import java.util.List;

public interface EvaluateService {
    boolean addEvaluate(Evaluate evaluate);
    List<Evaluate> getList(String userId);
    List<Evaluate> getAllList();
    boolean updateReply(String evaId, String reply);
}