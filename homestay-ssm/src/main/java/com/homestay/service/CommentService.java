package com.homestay.service;

import com.homestay.entity.Comment;
import java.util.List;

public interface CommentService {
    List<Comment> listAll();
    int add(Comment comment);
}