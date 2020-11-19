package com.zhang.toword.xmltoword;

import lombok.extern.slf4j.Slf4j;
import org.docx4j.Docx4J;
import org.docx4j.fonts.fop.util.LogUtil;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.*;

@Slf4j
public class XmlDocToDocxUtil {
    private XmlDocToDocxUtil() {
    }

    ;

    /**
     * 转换执行方法,转换后和原始路径
     *
     * @param xmlPath 原始路径
     */
    public static String invoke(String xmlPath) {
//        if(xmlPath.endsWith(".doc")){
        //DOC文档才转换
        String docxPath = xmlPath.replaceAll(Constant.CONVERT_SUF, Constant.DOCX_SUF);
        try (FileInputStream inputStream = new FileInputStream(xmlPath)) {
            WordprocessingMLPackage wmlPackage = Docx4J.load(inputStream);
            //转换为DOCX
            try (FileOutputStream docx = new FileOutputStream(docxPath);) {
                Docx4J.save(wmlPackage, docx, Docx4J.FLAG_SAVE_ZIP_FILE);
                xmlPath = docxPath;
            }
        } catch (Exception e) {
            log.error(xmlPath + ":不需要转换:" + e.getLocalizedMessage());
        }
//        }
        log.info("WORD 路径：" + xmlPath);
        return xmlPath;
    }


    public static void main(String[] args) {
        String s = "F:\\只有单选多选.xml";

        String invoke = XmlDocToDocxUtil.invoke(s);

        System.out.println(invoke);
    }
}