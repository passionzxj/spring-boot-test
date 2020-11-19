package com.zhang.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jodconverter.DocumentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author : lichenfei
 * @date : 2019年5月23日
 * @time : 下午6:10:58
 */
@Controller
public class OnLinePreviewController {

    // 第一步：转换器直接注入
    @Autowired
    private DocumentConverter converter;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("toPdfFile")
    public String toPdfFile() {
        File file = new File("E:\\workspace\\springboot_parent\\htmltopdf\\src\\main\\resources\\template\\file\\班级管理.docx");//需要转换的文件
        try {
            File newFile = new File("E:\\workspace\\springboot_parent\\htmltopdf\\src\\main\\resources\\template\\pdf");//转换之后文件生成的地址
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            //文件转化
            File pdf = new File("E:\\workspace\\springboot_parent\\htmltopdf\\src\\main\\resources\\template\\pdf\\hello.pdf");
            converter.convert(file).to(pdf).execute();
            //使用response,将pdf文件以流的方式发送的前段
            OutputStream outputStream = response.getOutputStream();
            InputStream in = new FileInputStream(pdf);// 读取文件
            // copy文件
            int i = IOUtils.copy(in, outputStream);
            System.out.println(i);
            in.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "This is to pdf";
    }

}