package com.zhang.html;

import cn.hutool.core.io.FileUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class IText7Util {

    public static void main(String[] args) {
        String url = "http://10.0.0.118:90/file-view/group1/M00/02/12/CgBuF18VW-uADfnAAACYqZ50lFU57.html";
        Map<String, Object> map = htmlToPdf(url);

        String s = upload((MultipartFile) map.get("multipartFile"), (File) map.get("file"));
    }

    /**
     * html的url转pdf文件
     *
     * @param url
     * @return
     */
    public static Map<String, Object> htmlToPdf(String url) {
        Document document;
        Map<String, Object> map = new HashMap<>();
        try {
            document = Jsoup.connect(url).get();
            String userDir = System.getProperties().getProperty("user.dir");
            String name = System.currentTimeMillis() + "";
            FontProvider font = new FontProvider();
            font.addFont("template/font/simsun.ttf");
            ConverterProperties c = new ConverterProperties();
            System.out.println("1111111" + c);
            c.setFontProvider(font);
            c.setCharset("utf-8");
            String pdfName = name + ".pdf";
            StringBuffer buffer = new StringBuffer();
            String pdfFilePath = buffer.append(userDir).append("\\htmltopdf\\src\\main\\resources\\template\\pdf\\").append(pdfName).toString();
            PdfWriter writer = new PdfWriter(pdfFilePath);
            HtmlConverter.convertToPdf(document.html(), writer, c);


            File file = new File(pdfFilePath);
            map.put("file", file);
            InputStream inputStream = new FileInputStream(file);
            String fileName = file.getName();
            MultipartFile multipartFile = new MockMultipartFile(fileName, fileName,
                    ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);

            map.put("multipartFile", multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;

    }

    /**
     * 上传到文件服务器
     *
     * @param multipartFile
     * @return
     */
    public static String upload(MultipartFile multipartFile, File file) {

        String fileName = multipartFile.getOriginalFilename();
        byte[] file_buff = null;
        InputStream inputStream = null;
        String path = null;
        try {
            inputStream = multipartFile.getInputStream();
            if (inputStream != null) {
                int len1 = inputStream.available();
                file_buff = new byte[len1];
                inputStream.read(file_buff);
            }
            assert fileName != null;
            path = FastDFSClient.upload(file_buff, fileName);
//            File pdfFile = multipartFileToFile(multipartFile);
            log.info("上传的pdf文件地址{}" + path);
            FileUtil.del(file);
        } catch (IOException e) {
            log.error("上传fail{}" + e.getMessage());
        } catch (Exception e1) {
            log.error("程序异常{}" + e1.getMessage());
        }
        return path;
    }

//    /**
//     * 转file文件
//     *
//     * @param file
//     * @return
//     * @throws Exception
//     */
//    public static File multipartFileToFile(MultipartFile file) throws Exception {
//
//        File toFile = null;
//        if ("".equals(file) || file.getSize() <= 0) {
//            file = null;
//        } else {
//            InputStream ins = null;
//            ins = file.getInputStream();
//            toFile = new File(file.getOriginalFilename());
//            inputStreamToFile(ins, toFile);
//            ins.close();
//        }
//        return toFile;
//    }
//
//    //获取流文件
//    private static void inputStreamToFile(InputStream ins, File file) {
//        try {
//            OutputStream os = new FileOutputStream(file);
//            int bytesRead = 0;
//            byte[] buffer = new byte[8192];
//            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//            os.close();
//            ins.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
