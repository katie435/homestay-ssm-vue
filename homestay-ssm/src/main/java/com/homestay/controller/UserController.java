package com.homestay.controller;

import com.homestay.entity.Evaluate;
import com.homestay.entity.User;
import com.homestay.service.EvaluateService;
import com.homestay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EvaluateService evaluateService;

    // 测试接口
    @GetMapping(value = "/test", produces = "text/plain;charset=UTF-8")
    public String test() {
        return "接口彻底通了！🎉";
    }

    // 登录
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        int status = ((com.homestay.service.impl.UserServiceImpl) userService).getLoginStatus();
        if (status == com.homestay.service.impl.UserServiceImpl.NO_USER) {
            return Result.error("还没有账户要先注册哟~");
        }
        if (status == com.homestay.service.impl.UserServiceImpl.PWD_ERR) {
            return Result.error("密码错误");
        }
        return Result.success("登录成功", loginUser);
    }

    // 注册
    @PostMapping("/add")
    public Result register(@RequestBody User user) {
        boolean flag = userService.register(user);
        if (flag) {
            return Result.success("注册成功！", user);
        } else {
            return Result.error("用户名已存在");
        }
    }

    // 获取用户信息
    @GetMapping("/info")
    public Result getUserInfo(@RequestParam("userId") String userId) {
        User user = userService.getUserInfo(userId);
        if (user != null) {
            return Result.success("获取成功", user);
        } else {
            return Result.error("用户不存在");
        }
    }

    // 修改用户信息
    @PostMapping("/update")
    public Result updateUser(@RequestBody User user) {
        boolean success = userService.updateUserInfo(user);
        if (success) {
            return Result.success("修改成功", null);
        } else {
            return Result.error("修改失败");
        }
    }

    // 修改密码
    @PostMapping("/updatePwd")
    public Result updatePassword(@RequestBody java.util.Map<String, String> map) {
        String userId = map.get("userId");
        String oldPwd = map.get("oldPwd");
        String newPwd = map.get("newPwd");
        boolean success = userService.updatePassword(userId, oldPwd, newPwd);
        if (success) {
            return Result.success("密码修改成功", null);
        } else {
            return Result.error("旧密码错误");
        }
    }

    @GetMapping("/infoByUsername")
    public Result infoByUsername(String username) {
        User user = userService.findByUsername(username);
        return Result.success("ok", user);
    }

    // ==================== 评价功能 ====================
    @PostMapping("/addEvaluate")
    public Result addEvaluate(
            String userId,
            Integer score,
            String content,
            @RequestParam(required = false) MultipartFile evaImg
    ) throws Exception {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error("用户未登录，请先登录");
        }
        if (score == null || score < 1 || score > 5) {
            return Result.error("请选择1-5星评分");
        }
        if (content == null || content.trim().isEmpty()) {
            return Result.error("评价内容不能为空");
        }

        Evaluate eva = new Evaluate();
        eva.setUserId(userId);
        eva.setScore(score);
        eva.setContent(content);

        if (evaImg != null && !evaImg.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + evaImg.getOriginalFilename();
            String path = "D:/upload/eva/";
            File filePath = new File(path);
            if (!filePath.exists()) filePath.mkdirs();
            evaImg.transferTo(new File(path + fileName));
            eva.setEvaImg("/upload/eva/" + fileName);
        }

        boolean ok = evaluateService.addEvaluate(eva);
        return ok ? Result.success("提交成功") : Result.error("提交失败");
    }

    @GetMapping("/getEvaluate")
    public Result getEvaluate(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error("用户未登录，请先登录");
        }
        return Result.success("查询成功", evaluateService.getList(userId));
    }

    // ==================== 统一返回结果 ====================
    public static class Result {
        private Integer code;
        private String msg;
        private Object data;

        public static Result success(String msg, Object data) {
            Result r = new Result();
            r.code = 200;
            r.msg = msg;
            r.data = data;
            return r;
        }
        public static Result success(String msg) {
            Result r = new Result();
            r.code = 200;
            r.msg = msg;
            r.data = null;
            return r;
        }
        public static Result error(String msg) {
            Result r = new Result();
            r.code = 500;
            r.msg = msg;
            r.data = null;
            return r;
        }

        public Integer getCode() { return code; }
        public void setCode(Integer code) { this.code = code; }
        public String getMsg() { return msg; }
        public void setMsg(String msg) { this.msg = msg; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }

    // ==================== 查询全部用户（客户列表） ====================
    @GetMapping("/list")
    public Result getUserList(){
        return Result.success("查询成功", userService.findAllUsers());
    }

    // ==================== 【新增】删除用户 ====================
    @GetMapping("/delete")
    public Result deleteUser(@RequestParam("userId") String userId) {
        try {
            userService.deleteUserById(userId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error("删除失败");
        }
    }
}