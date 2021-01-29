package com.zhang.jwt.html;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.font.FontProvider;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PDFUtil {


    public static void toPDF(HttpServletRequest request)throws IOException {
        Map<String, File> map = PDFUtil.creatHtmlFile(request);
        File htmlFilePath = map.get("htmlFile");
        File pdfFilePath = map.get("pdfFile");
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

        HtmlConverter.convertToPdf(new FileInputStream(htmlFilePath), new FileOutputStream(pdfFilePath),c);
//        boolean del = FileUtil.del(htmlFilePath);
//        boolean del1 = FileUtil.del(pdfFilePath);
    }

    public static Map<String,File> creatHtmlFile(HttpServletRequest request) throws IOException {
        HashMap<String,File> map = new HashMap<>();
        String name = System.currentTimeMillis() + "";
//        String root = request.getSession().getServletContext().getRealPath("");
//        String htmlPath = root+"WEB-INF\\classes\\template\\html\\";
//        String pdfPath = root+"WEB-INF\\classes\\template\\pdf\\";
//        String path = "E:\\pdf\\";
//        String htmlFilePath = htmlPath + name + ".html";
//        String pdfFilePath = pdfPath + name + ".pdf";
        String userDir = System.getProperties().getProperty("user.dir");
//        String htmlFilePath = "E:\\workspace\\springboot_parent\\springboot_mvc\\src\\main\\resources\\template\\html\\1595082854923.html";
//        String pdfFilePath = "E:\\workspace\\springboot_parent\\springboot_mvc\\src\\main\\resources\\template\\pdf\\1595082854923.pdf";
        String htmlFilePath = userDir+"\\htmltopdf\\src\\main\\resources\\template\\html\\"+name+".html";
        String pdfFilePath = userDir+"\\htmltopdf\\src\\main\\resources\\template\\pdf\\"+name+".pdf";
        File htmlFile = new File(htmlFilePath);
        File pdfFile = new File(pdfFilePath);
        if (!htmlFile.exists()) {
            htmlFile.createNewFile();
        } else {
//倘若文件存在则将原有文件移除并创建新的文件
            htmlFile.delete();
            htmlFile.createNewFile();
        }
        map.put("htmlFile",htmlFile);
        if (!pdfFile.exists()) {
            pdfFile.createNewFile();
        } else {
//倘若文件存在则将原有文件移除并创建新的文件
            pdfFile.delete();
            pdfFile.createNewFile();
        }
        map.put("pdfFile",pdfFile);
        return map;
    }

    public static void writeHtmlFile(String htmlStr,File htmlFile) throws IOException {
// 先读取原有文件内容，然后进行写入操作 
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
//// 文件路径 
//            File file = new File(htmlFilePath);

// 将文件读入输入流 
            fis = new FileInputStream(htmlFile);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            fos = new FileOutputStream(htmlFile);
            pw = new PrintWriter(fos);
            pw.write(htmlStr.toCharArray());
            pw.flush();
        } catch (IOException e1) {
            throw e1;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }
}
