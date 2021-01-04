package com.leyou.controller;

import com.leyou.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 解决文件上传问题
 * @Author:ykym
 * @Date:2020/8/17 16:04
 */
@Controller
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private UploadService service;

    /**
    *图片上传
    *@Author：ykym
    * @param: []
    * @return: org.springframework.http.ResponseEntity<java.lang.String>
    * @Date:  22:23 2020/8/17
    */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file){
              String url= this.service.upload(file);

              if (StringUtils.isBlank(url)){
                  return ResponseEntity.badRequest().build();
              }
              return ResponseEntity.status(HttpStatus.CREATED).body(url);
    }
}
