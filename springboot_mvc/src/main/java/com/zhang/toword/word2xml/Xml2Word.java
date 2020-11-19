package com.zhang.toword.word2xml;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.zhang.toword.WordUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;


public class Xml2Word {
    public static void main(String[] args) throws IOException, TemplateException {
        // 模板文件路径：
        String templetFilePath = "F:/xml/只有单选多选(2).xml";
        // 目标文件存放路径
        String targetFilePath = "F:/word/1213.doc";
        // 将xml模板转换为后缀为doc文件，本质仍是属于xml
        xml2XmlDoc(null, templetFilePath, targetFilePath);
        System.out.println("----------1----------");
        docToDocx("F:/word/1213.doc", "F:/word/1213.docx");
        System.out.println("----------2----------");


        OutputStream process = WordUtil.process(null, "只有单选多选.xml");


    }


    /**
     * 将xml模板转换为后缀为doc文件，本质仍是属于xml
     * @param dataMap    需要填充到模板的数据
     * @param templetFilePath    模板文件路径
     * @param targetFilePath    目标文件保存路径
     * @throws IOException
     * @throws TemplateException
     */
    public static void xml2XmlDoc(Map<String, Object> dataMap, String templetFilePath, String targetFilePath) throws IOException, TemplateException {
        // 将模板文件路径拆分为文件夹路径和文件名称
        String tempLetDir = templetFilePath.substring(0, templetFilePath.lastIndexOf("/"));
        // 注意：templetFilePath.lastIndexOf("/")中，有的文件分隔符为：\ 要注意文件路径的分隔符
        String templetName = templetFilePath.substring(templetFilePath.lastIndexOf("/") + 1);
        // 将目标文件保存路径拆分为文件夹路径和文件名称
        String targetDir = targetFilePath.substring(0, targetFilePath.lastIndexOf("/"));
        String targetName = targetFilePath.substring(targetFilePath.lastIndexOf("/") + 1);

        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
        // 如果目标文件目录不存在，则需要创建
        File file = new File(targetDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 加载模板数据（从文件路径中获取文件，其他方式，可百度查找）
        configuration.setDirectoryForTemplateLoading(new File(tempLetDir));
        // 获取模板实例
        Template template = configuration.getTemplate(templetName);
        File outFile = new File(targetDir + File.separator + targetName);
        //将模板和数据模型合并生成文件
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
        //生成文件
        template.process(dataMap, out);
        out.flush();
        out.close();
    }

    /**
     * xml形式的doc文件转换为Docx格式
     *
     * @param sourcePath 被转换文件的路径
     * @param targetPath 目标文件路径
     * @return
     * @author lixs
     * @Date 2018年5月29日16:24:08
     */
    public static void docToDocx(String sourcePath, String targetPath) {
        //Word.Application代表COM OLE编程标识，可查询MSDN得到
        ActiveXComponent app = new ActiveXComponent("Word.Application");
        //设置Word不可见
        app.setProperty("Visible", false);
        //调用Application对象的Documents属性，获得Documents对象
        Dispatch docs = app.getProperty("Documents").toDispatch();
        //Dispatch doc = Dispatch.call(docs,"Open",sourcePath,new Variant(false),new Variant(true)).getDispatch();
        Dispatch doc = Dispatch.call(docs, "Open", sourcePath).getDispatch();
        Dispatch.call(doc, "SaveAS", targetPath, 12);
        //关闭打开的Word文件
        Dispatch.call(doc, "Close", false);
        //关闭Word应用程序
        app.invoke("Quit", 0);
    }


}