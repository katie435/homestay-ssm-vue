package com.homestay.common;

public class Result {
    private Integer code;
    private String msg;
    private Object data;

    // 成功（带数据）
    public static Result success(String msg, Object data) {
        Result r = new Result();
        r.code = 200;
        r.msg = msg;
        r.data = data;
        return r;
    }

    // 成功（不带数据）
    public static Result success(String msg) {
        Result r = new Result();
        r.code = 200;
        r.msg = msg;
        return r;
    }

    // 失败
    public static Result error(String msg) {
        Result r = new Result();
        r.code = 500;
        r.msg = msg;
        return r;
    }

    // Getter/Setter
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}