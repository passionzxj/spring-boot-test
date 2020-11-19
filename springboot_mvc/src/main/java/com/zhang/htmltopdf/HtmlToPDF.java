package com.zhang.htmltopdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.font.FontProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class HtmlToPDF {
//    private static final String ORIG = "E:/pdf/15950828549231.html";
//    private static final String OUTPUT_FOLDER = "E:/wkpdf/";

    public static void main(String[] args) throws IOException {
//        File htmlSource = new File(ORIG);
//        File pdfDest = new File(OUTPUT_FOLDER + "output.pdf");
//        ConverterProperties props = new ConverterProperties();
//        FontProvider fp = new FontProvider(); // 提供解析用的字体
//        fp.addStandardPdfFonts(); // 添加标准字体库、无中文
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        URL resource = classLoader.getResource("simsun.ttf");
//        String path = resource.getPath();
//        int i = fp.addDirectory(classLoader.getResource("simsun.ttf").getPath());// 自定义字体路径、解决中文,可先用绝对路径测试。
//        props.setFontProvider(fp);
//        // props.setBaseUri(baseResource); // 设置html资源的相对路径


        //导入字体
        FontProvider font = new FontProvider();
        boolean b = font.addFont("template/font/simsun.ttf");
        ConverterProperties c = new ConverterProperties();
        c.setFontProvider(font);
        c.setCharset("utf-8");



//        PdfFont sysFont = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
//        Paragraph paragraph = new Paragraph();
//        Document document = new Document();
//        document.add(paragraph.setFont(sysFont));

//        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest),c);



    }



}