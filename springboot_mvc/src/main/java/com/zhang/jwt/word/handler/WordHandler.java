package com.zhang.jwt.word.handler;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.*;


public class WordHandler {

    public static void main(String[] args) {
        //String result = "<p><span style=\"font-family:宋体;font-size:21;color:#FF0000;\"></span><span style=\"font-family:宋体;color:#000000;\"></span><span style=\"font-family:宋体;color:#000000;\">&nbsp;</span></p><p><span style=\"font-family:宋体;font-size:21;\">（1）故木受绳则直，&nbsp;</span><span style=\"font-family:宋体;font-size:21;text-decoration: underline\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span style=\"font-family:宋体;font-size:21;\">，&nbsp;</span><span style=\"font-family:宋体;font-size:21;text-decoration: underline\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span style=\"font-family:宋体;font-size:21;\">&nbsp;，则知明而行无过矣。</span></p>";
        String result = "<span style=\"color:#000000;\">___________English&nbsp;&nbsp;book.&nbsp;___________&nbsp;English&nbsp;book&nbsp;is&nbsp;blue.</span>";
        result = replaceMultSpace(result);
        System.out.println(result);
    }

    private static Map<String,Integer> countPerpetualBlankSpace(String str, int offset){
        //int index = str.indexOf("&nbsp;",offset);
        Map<String,Integer> map = new HashMap<>();
        String blankSpace = "&nbsp;";
        int count = 0;
        map.put("offset",offset);
        map.put("count",count);
        if(StringUtils.isBlank(str)){
            return map;
        }
        StringBuffer sb = new StringBuffer("");
        int firstIndex = str.indexOf(blankSpace,offset);
        while(true){
            sb.append(blankSpace);
            int index = str.indexOf(sb.toString(),offset);

            if(index == -1 || firstIndex != index){
                break;
            }
            count++;
        }
        offset = firstIndex+count*6;
        map.put("offset",offset);
        map.put("count",count);
        return map;
    }

    public static String replaceMultSpace(String str){
        if(StringUtils.isBlank(str)){
            return str;
        }
        String replaceStr = "&nnnn;";
        int offset2 = 0;
        while(true){
            Map<String,Integer> map2 = countPerpetualBlankSpace(str,offset2);
            int count2 = map2.get("count");
            offset2 = map2.get("offset");
            if(count2 == 0){
                break;
            }

            int startIndex = offset2 - count2*6;

            if(count2 == 1){

                StringBuffer sub = new StringBuffer();
                for(int i=0;i<count2;i++){
                    sub.append(replaceStr);
                }
                str = str.substring(0,startIndex)+sub.toString()+str.substring(offset2);
            }

        }
        str = str.replace(replaceStr," ");

        return str;
    }

    /**
     * 将问题的word段落转成html格式
     * @param pList
     * @param startStr
     * @param splitStrs
     * @return
     */
    public static StringBuffer extractQuestionComponent(List<Element> pList, String startStr, List<String> splitStrs) {
        StringBuffer sb = new StringBuffer();
        List<Element> analysisElements = splitQuestionContent(pList, startStr, splitStrs,false);
        for (int i = 0; i < analysisElements.size(); i++) {
            Element elenent = analysisElements.get(i);
            if (i > 0) {
                sb.append("\n");
            }
            if("p".equals(elenent.getName())){
                sb.append(getHtml(elenent,startStr));
            }else if("tbl".equals(elenent.getName())){
                String tableHtml = getTableHtml(elenent);
                sb.append(tableHtml);
            }
        }
        sb = new StringBuffer(replaceMultSpace(sb.toString()));
        return sb;
    }

    private static String getTableHtml(Element tableElenent) {
        //提取表格的边框样式
        Element borderElement = tableElenent.element("tblPr").element("tblBorders");
        String tableStyle = "border: 1px solid #000000;";
        String tdLeftBorder = "border-left: 1px solid #000000;";
        String tdTopBorder = "border-top: 1px solid #000000;";
        if(borderElement != null){
            Element topBorder = borderElement.element("top");
            Element leftBordere = borderElement.element("left");
            Element bottomBorder = borderElement.element("bottom");
            Element rightBorder = borderElement.element("right");
            tableStyle = /*getBorderStyle(topBorder)
					+getBorderStyle(leftBordere)*/
                    getBorderStyle(bottomBorder)
                            +getBorderStyle(rightBorder);
            Element insideVBorder = borderElement.element("insideV");
            Element insideHBorder = borderElement.element("insideH");
            tdLeftBorder= getBorderStyle(insideVBorder).replace("insideV","left");
            tdTopBorder= getBorderStyle(insideHBorder).replace("insideH","top");
        }


        //提取表格的列宽度
        List<Element> gridCols = tableElenent.element("tblGrid").elements("gridCol");
        //获取表格的每一行
        List<Element> trs = tableElenent.elements("tr");

        StringBuffer table = new StringBuffer("<table style=\""+tableStyle+" cellspacing:0; border-collapse: collapse;\">");
        for(Element trElement:trs){
            StringBuffer tr = new StringBuffer("<tr>");
            List<Element> tcs = trElement.elements("tc");
            for(Element tcElement:tcs){
                StringBuffer td = new StringBuffer("<td style=\""+tdLeftBorder+tdTopBorder+" \" "+getColumNums(tcElement)+">");

                tcElement.element("tcPr").element("gridSpan");
                List<Element> ps = tcElement.elements("p");
                for(Element p:ps){
                    td.append(getHtml(p,""));
                }
                td.append("</td>");
                tr.append(td);
            }
            tr.append("</tr>");
            table.append(tr);
        }

        table.append("</table>");
        return table.toString();
    }

    private static String getColumNums(Element tc){
        Element tcPr = tc.element("tcPr");
        if(tcPr != null){
            Element gridSpan = tcPr.element("gridSpan");
            if(gridSpan != null){
                String val = gridSpan.attributeValue("val");
                if(StringUtils.isNotBlank(val)){
                    return "colspan=\""+val+"\"";
                }
            }
        }
        return "";
    }

    private static String getBorderStyle(Element element){
        String name = element.getName();
        String val = element.attributeValue("val");
        String sz = element.attributeValue("sz");
        double size= Integer.valueOf(sz)/4;
        String color = element.attributeValue("color");
        color = "auto".equals(color) ? "000000": color;
        String style = "border-"+name+": "+size+"pt solid #"+color+";";
        return style;
    }

    /**
     * 将element 根据question的指定部分进行分割
     * @param questionList
     * @param startStr
     * @param splitStrs
     * @param containEndStr	提取内容是否包含"结尾标记"的字符串
     * @return
     */
    public static List<Element> splitQuestionContent(List<Element> questionList, String startStr, List<String> splitStrs,
                                                     boolean containEndStr) {
        List<Element> contents = new ArrayList<Element>();

        boolean isContent = false;
        for (Element element : questionList) {
            String text = getText(element).trim();

            if (text.startsWith(startStr)) {
                isContent = true;
            }
            // 包含结束标记的段落
            if (isContent && containEndStr) {
                contents.add(element);
            }

            for (String splitStr : splitStrs) {
                if (!startStr.equals(splitStr) && text.startsWith(splitStr)) {
                    isContent = false;
                    break;
                }
            }
            // 不包含结束标记的段落
            if (isContent && !containEndStr) {
                contents.add(element);
            }
        }
        return contents;
    }

    public static String getText(Element root) {
        StringBuffer sb = new StringBuffer();
        // 遍历根结点的所有孩子节点
        for (Iterator iter = root.elementIterator(); iter.hasNext();) {
            Element element = (Element) iter.next();
            if (element == null) {
                continue;
            }
            String innerName = element.getName();
            if (innerName.equals("t")) {
                String innerValue = element.getText();
                sb.append(innerValue);
            } else {
                // 递归调用
                sb.append(getText(element));
            }
        }
        if(root.getName().equals("p")){
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String getText(List<Element> elements) {
        StringBuffer sb = new StringBuffer();
        for(Element root:elements){
            sb.append(getText(root));
        }
        return sb.toString();
    }



    public static String getHtml(Element root,String excludeStr) {
        StringBuffer sb = new StringBuffer();

        if(root!=null && root.getName().equals("p")) {
            //String pStyle = getTextIndexStyle(root); //读取缩进的样式
            String pStyle = "";
            sb.append("<p "+pStyle+">");
        }
        // 遍历根结点的所有孩子节点
        for (Iterator iter = root.elementIterator(); iter.hasNext();) {
            Element element = (Element) iter.next();
            if (element == null) {
                continue;
            }
            String innerName = element.getName();
            String vertAlignVal = null;
            if(innerName.equals("r")) {
                //处理上标和下标
                Element rPr = element.element("rPr");
                if(rPr != null){
                    Element vertAlign = element.element("rPr").element("vertAlign");
                    if(vertAlign != null){
                        vertAlignVal = vertAlign.attributeValue("val");
                    }
                }

                String span = "<span "+extractStyle(element)+">";
                if("superscript".equals(vertAlignVal)){
                    span = "<sup>";
                }else if("subscript".equals(vertAlignVal)){
                    span = "<sub>";
                }
                sb.append(span);
            }

            if (innerName.equals("t")) {
                String innerValue = element.getText();
                sb.append(innerValue.replace(" ", "&nbsp;").replace(" ", "&nbsp;"));
            } else {
                // 递归调用
                sb.append(getHtml(element,""));
            }
            if(innerName.equals("r")) {
                String spanEnd = "</span>";
                if("superscript".equals(vertAlignVal)){
                    spanEnd = "</sup>";
                }else if("subscript".equals(vertAlignVal)){
                    spanEnd = "</sub>";
                }
                sb.append(spanEnd);
            }
        }
        if(root!=null && root.getName().equals("p")) {
            String pEnd = "</p>";
            sb.append(pEnd);
        }
        String result = HTMLHandler.extractTextFromHtml(sb.toString(), excludeStr);
        return result;
    }

    private static String getTextIndexStyle(Element root) {
        String pStyle = "";
        Element firstPr = root.element("pPr");
        if(firstPr != null){
             Element ind = firstPr.element("ind");
             if(ind != null){
                 String firstLine = ind.attributeValue("firstLine");
                 if(StringUtils.isNotBlank(firstLine)){
                     Double textIndent = Double.valueOf(firstLine) / 20;
                     pStyle = "style=\"text-indent: "+textIndent+"pt\"";
                 }
             }
        }
        return pStyle;
    }

    public static String extractStyle(Element rElement) {
        StringBuffer style = new StringBuffer();
        Element element = rElement.element("rPr");
        if(element==null) {
            return "";
        }
        //获取样式元素
        Element rFontsElement = element.element("rFonts");
        Element szCsElement = element.element("szCs");
        Element colorElement = element.element("color");
        Element uElement = element.element("u");
        Element bElement = element.element("b");

        if(rFontsElement != null) {
            String fontFamily = rFontsElement.attributeValue("ascii");
            style.append("font-family:"+fontFamily+";");
        }
        if(szCsElement != null) {
            String fontSize = szCsElement.attributeValue("val");
            style.append("font-size:"+fontSize+";");
        }
        if(bElement!=null) {
            style.append("font-weight:bold;");
        }
        if(colorElement != null) {
            String color = colorElement.attributeValue("val");
            style.append("color:#"+color+";");
        }
        if(uElement != null) {
            String underlineVal = uElement.attributeValue("val");
            if(StringUtils.isNotBlank(underlineVal)){
                //目前只能添加 文本下的一条线
                style.append("text-decoration: underline");
            }
        }

        if(StringUtils.isBlank(style)) {
            return "";
        }

        return "style=\""+style.toString()+"\"";
    }
}
