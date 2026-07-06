package com.homestay.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UploadController {

    // 图片保存到 D盘
    private final String localSavePath = "D:/upload/";
    // 前端访问地址
    private final String preUrl = "http://localhost:8080/ssm_springmvc/upload/";

    @PostMapping("/upload")
    public Map<String,Object> uploadFile(@RequestParam("file") MultipartFile file){
        Map<String,Object> result = new HashMap<>();
        try{
            // 文件夹不存在就创建
            File dir = new File(localSavePath);
            if(!dir.exists()) dir.mkdirs();

            // 文件名
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = System.currentTimeMillis() + suffix;

            // 保存图片
            file.transferTo(new File(localSavePath + fileName));

            // 返回给前端
            result.put("code", 200);
            result.put("data", preUrl + fileName);

        }catch(Exception e){
            result.put("code", 500);
            result.put("msg", "上传失败");
            e.printStackTrace();
        }
        return result;
    }
}