package com.homestay.mapper;

import com.homestay.entity.Evaluate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EvaluateMapper {
    int add(Evaluate evaluate);
    List<Evaluate> getList(String userId);
    List<Evaluate> getAllList();
    int updateReply(@Param("evaId") String evaId, @Param("reply") String reply);
}