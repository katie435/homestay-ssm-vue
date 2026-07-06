package com.homestay.mapper;

import com.homestay.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


@Mapper
public interface CommentMapper {
    List<Comment> selectAll();
    int insert(Comment comment);
}