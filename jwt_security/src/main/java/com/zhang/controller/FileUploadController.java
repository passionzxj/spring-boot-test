package com.zhang.controller;

import com.zhang.conf.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Controller
@RequestMapping("/label/file/upload/test")
public class FileUploadController {


    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    @ResponseBody
    public AjaxResult useAbility(@RequestParam MultipartFile file) {
        log.info("AbilityController getAbility, request={}", file);
        return AjaxResult.success("aa");
    }

    /*@PostMapping("upload")
    @ResponseBody
    public AjaxResult getPercent(@RequestParam MultipartFile file){
        log.info("AbilityController getAbility, request={}", file);

        Thread thread = new Thread(() -> {
            long pBytesRead = progressEntity.getPBytesRead();
            long pContentLength = progressEntity.getPContentLength();
            float tmp = (float) pBytesRead;
            float result = tmp / pContentLength * 100;
            String percent = result + "";
            System.out.println("percent========="+percent);
            webSocketServer.sendOneMessage("123",percent);
        });
        thread.start();

        return AjaxResult.success("ok");
    }*/
}