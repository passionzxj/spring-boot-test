package com.zhang.jwt.word;

import org.dom4j.Element;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XmlConverter {

	public static String xslConvert(String s, String xslpath, URIResolver uriResolver){
	    TransformerFactory tFac = TransformerFactory.newInstance();
	    if(uriResolver != null)  tFac.setURIResolver(uriResolver);
	    
	    StreamSource xslSource = new StreamSource(xslpath);
	    StringWriter writer = new StringWriter();   
	    try {
	        Transformer t = tFac.newTransformer(xslSource);
	        Source source = new StreamSource(new StringReader(s));
	        Result result = new StreamResult(writer);   
	        t.transform(source, result);
	    } catch (TransformerException e) {
	        e.printStackTrace();
	    }
	    return writer.getBuffer().toString();
	}
	
	/**
	 * <p>Description: 将mathml转为latx </p>
	 * @param mml
	 * @return
	 */
	public static String convertMML2Latex(String mml,String xslRootPath){
		final String path = xslRootPath+"/xsltml/";
	    mml = mml.substring(mml.indexOf("?>")+2, mml.length()); //去掉xml的头节点
	    URIResolver r = new URIResolver(){  //设置xls依赖文件的路径
	        public Source resolve(String href, String base) throws TransformerException {
	        	File file = new File(path + href);
	            InputStream inputStream = null;
				try {
					inputStream = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
	            return new StreamSource(inputStream);
	        }
	    };
	    
	    String latex = xslConvert(mml, path+"mmltex.xsl", r);
	    if(latex != null && latex.length() > 1){
	        latex = latex.substring(1, latex.length() - 1);
	    }
	    return latex;
	}
	
	
	
	/**
	 * <p>Description: office mathml转为mml </p>
	 * @param xml
	 * @return
	 */
	public static String convertOMML2MML(String xml,String xslRootPath){
		String xslpath = xslRootPath+"/OMML2MML.XSL";
	    String result = xslConvert(xml, xslpath, null);
	    return result;
	}
	
	public static void main(String[] args) {
		String xml = "<m:oMath xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"><m:r><w:rPr xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"><w:rFonts w:ascii=\"Cambria Math\" w:hAnsi=\"Cambria Math\"/></w:rPr><m:t>x=</m:t></m:r><m:sSub><m:sSubPr><m:ctrlPr><w:rPr xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"><w:rFonts w:ascii=\"Cambria Math\" w:hAnsi=\"Cambria Math\"/></w:rPr></m:ctrlPr></m:sSubPr><m:e><m:r><w:rPr xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"><w:rFonts w:ascii=\"Cambria Math\" w:hAnsi=\"Cambria Math\"/></w:rPr><m:t>x</m:t></m:r></m:e><m:sub><m:r><w:rPr xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"><w:rFonts w:ascii=\"Cambria Math\" w:hAnsi=\"Cambria Math\"/></w:rPr><m:t>0</m:t></m:r></m:sub></m:sSub></m:oMath>";
		String omml2 = "<xml-fragment xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\" xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\" xmlns:w15=\"http://schemas.microsoft.com/office/word/2012/wordml\" xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\" xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\"><m:r><w:rPr><w:rFonts w:ascii=\"Cambria Math\" w:hAnsi=\"Cambria Math\"/></w:rPr><m:t>x=</m:t></m:r><m:sSub><m:sSubPr><m:ctrlPr><w:rPr><w:rFonts w:ascii=\"Cambria Math\" w:hAnsi=\"Cambria Math\"/></w:rPr></m:ctrlPr></m:sSubPr><m:e><m:r><w:rPr><w:rFonts w:ascii=\"Cambria Math\" w:hAnsi=\"Cambria Math\"/></w:rPr><m:t>x</m:t></m:r></m:e><m:sub><m:r><w:rPr><w:rFonts w:ascii=\"Cambria Math\" w:hAnsi=\"Cambria Math\"/></w:rPr><m:t>0</m:t></m:r></m:sub></m:sSub></xml-fragment>";
		String xslRootPath = System.getProperty("user.dir")+"\\ice-web\\src\\main\\resources\\xsl";
		String mml = convertOMML2MML(xml,xslRootPath);
		System.out.println(mml);
		String latex = convertMML2Latex(mml,xslRootPath);
		System.out.println("-->"+latex);

	}

	public static List getNodes(Element root,String name){
		List<Element> result = new ArrayList<>();

		//当前节点的名称、文本内容和属性
		if(root.getName().equals(name)){
			result.add(root);
		}

		//递归遍历当前节点所有的子节点
		List<Element> listElement=root.elements();//所有一级子节点的list
		for(Element e:listElement){//遍历所有一级子节点
			result.addAll(getNodes(e,name));//递归
		}
		return result;
	}
}

