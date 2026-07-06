package com.homestay.entity;

import java.util.Date;

public class Comment {
    private Integer commentId;
    private String orderId;
    private Integer userId;
    private Integer roomId;
    private Integer score;
    private String content;
    private Date createTime;

    public Comment() {}

    public Comment(Integer commentId, String orderId, Integer userId, Integer roomId, Integer score, String content, Date createTime) {
        this.commentId = commentId;
        this.orderId = orderId;
        this.userId = userId;
        this.roomId = roomId;
        this.score = score;
        this.content = content;
        this.createTime = createTime;
    }

    public Integer getCommentId() { return commentId; }
    public void setCommentId(Integer commentId) { this.commentId = commentId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", orderId='" + orderId + '\'' +
                ", userId=" + userId +
                ", roomId=" + roomId +
                ", score=" + score +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}