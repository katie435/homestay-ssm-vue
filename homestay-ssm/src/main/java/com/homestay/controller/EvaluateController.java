package com.homestay.controller;

import com.homestay.entity.Evaluate;
import com.homestay.service.EvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/evaluate")
public class EvaluateController {
    @Autowired
    private EvaluateService evaluateService;

    @PostMapping("/add")
    public Map<String,Object> add(@RequestBody Map<String,Object> params){
        Map<String,Object> map = new HashMap<>();
        Evaluate eva = new Evaluate();
        //====新增日志打印====
        System.out.println("前端全部入参："+params);
        System.out.println("接收的orderId："+params.get("orderId"));

        eva.setUserId((String) params.get("userId"));
        String userName = (String) params.get("userName");
        eva.setUserName(userName);

        eva.setScore(Integer.parseInt(params.get("score").toString()));
        eva.setContent((String) params.get("content"));
        eva.setRoomName((String) params.get("roomName"));

        String orderId = (String) params.get("orderId");
        eva.setOrderId(orderId);

        Object imgArrObj = params.get("imgArr");
        String imgStr = "";
        if(imgArrObj != null){
            imgStr = imgArrObj.toString().replace("[","").replace("]","");
        }
        eva.setEvaImg(imgStr);

        boolean flag = evaluateService.addEvaluate(eva);
        if(flag){
            map.put("code",200);
            map.put("msg","成功");
        }else{
            map.put("code",500);
            map.put("msg","失败");
        }
        return map;
    }

    @PostMapping("/reply")
    public Map<String, Object> reply(@RequestBody Map<String, String> params) {
        Map<String, Object> map = new HashMap<>();
        String evaId = params.get("evaId");
        String reply = params.get("reply");

        boolean flag = evaluateService.updateReply(evaId, reply);
        if (flag) {
            map.put("code", 200);
            map.put("msg", "回复成功");
        } else {
            map.put("code", 500);
            map.put("msg", "回复失败");
        }
        return map;
    }

    @GetMapping("/myList")
    public Map<String,Object> myList(@RequestParam String userId){
        Map<String,Object> map = new HashMap<>();
        List<Evaluate> list = evaluateService.getList(userId);
        map.put("code",200);
        map.put("data",list);
        return map;
    }

    @GetMapping("/allList")
    public Map<String,Object> allList(){
        Map<String,Object> map = new HashMap<>();
        List<Evaluate> list = evaluateService.getAllList();
        map.put("code",200);
        map.put("data",list);
        return map;
    }
}