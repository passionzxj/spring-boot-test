package com.zhang.toword.word2xml;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Word2Xml {

    public static void main(String[] args) {
        String s1 = "F:/word/1213.docx";
        String s2 = "F:/xml/1213.xml";

        Word2Xml.wordToXml(s1,s2);
    }

    /**
     * @param filePath    word目录
     * @param xmlFilePath 生成xml存放路径
     * @Description:
     * @author Administrator
     */
    public static void wordToXml(String filePath, String xmlFilePath) {
        try {
            ActiveXComponent app = new ActiveXComponent("Word.Application"); //启动word
            app.setProperty("Visible", new Variant(false)); //为false时设置word不可见，为true时是可见要不然看不到Word打打开文件的过程
            Dispatch docs = app.getProperty("Documents").toDispatch();
            //打开编辑器
            Dispatch doc = Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[]{filePath, new Variant(false), new Variant(true)}, new int[1]).toDispatch(); //打开word文档
            Dispatch.call(doc, "SaveAs", xmlFilePath, 11);//xml文件格式宏11
            Dispatch.call(doc, "Close", false);
            app.invoke("Quit", 0);
            System.out.println("---------word转换完成--------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}