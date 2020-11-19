//package com.zhang.web.controller;
//
//import com.zhang.htmltopdf.PDFUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.File;
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/pdf")
//public class PDFController {
//
//
//    @RequestMapping("to")
//    public String pdf(HttpServletRequest request,@RequestParam("htmlStr") String htmlStr) {
//        try {
//            File htmlFile = PDFUtil.creatHtmlFile(request).get("htmlFile");
//            PDFUtil.writeHtmlFile(htmlStr,htmlFile);
//            PDFUtil.toPDF(request);
//            return "ok";
//        }catch (IOException e){
//            e.printStackTrace();
//            return "fail";
//        }
//
//    }
//
//}
