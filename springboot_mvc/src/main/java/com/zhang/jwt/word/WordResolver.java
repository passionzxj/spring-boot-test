package com.zhang.jwt.word;

import com.zhang.jwt.word.enumeration.QuestionComponent;
import com.zhang.jwt.word.enumeration.QuestionType;
import com.zhang.jwt.word.exception.ImportQuestionException;
import com.zhang.jwt.word.handler.HTMLHandler;
import com.zhang.jwt.word.question.*;
import com.zhang.jwt.word.handler.WordHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordResolver {

	public static void main(String[] args) {
		WordResolver resolver = new WordResolver();
		try {
			//InputStream is = new FileInputStream("E:\\文件\\测试题目.xml");
			//InputStream is = new FileInputStream("E:\\文件\\in - 带题型 - 副本.xml");
			//InputStream is = new FileInputStream("E:\\文件\\in - 带题型模板.xml");
			InputStream is = new FileInputStream("E:\\导入公式异常文件\\qqqqq.xml");
			String xslRootPath = System.getProperty("user.dir")+"\\ice-web\\src\\main\\resources\\xsl";
			List<AbstractQuestion> questions = resolver.readXml(is,xslRootPath);

			File file =new File("E:\\question.html");
			Writer out =new FileWriter(file);
			for(int i=0;i<questions.size();i++) {
				AbstractQuestion question = questions.get(i);
				out.write(i+">>>>>"+question.getTopic());
				StringBuffer sb = new StringBuffer();
				
				List<SubQuestion> subQuestions = question.getSubQuestions();
				if(subQuestions!=null) {
					sb.append("【选项】");
					int subAnswerindex = 1;
					for(SubQuestion sub : subQuestions) {
						String topic = sub.getTopic();
						sb.append(subAnswerindex+"、");
						if(StringUtils.isNotBlank(topic)) {
							sb.append(topic);
						}
						sb.append("<br/>");
						for(Option option :sub.getOptions()) {
							sb.append(option.generateFullOption());
						}
						sb.append("<br/>");
						subAnswerindex++;
					}
					sb.append("【答案】");
					subAnswerindex = 1;
					for(SubQuestion sub : subQuestions) {
						Answer answer = sub.getAnswer();
						if(answer != null) {
							sb.append(subAnswerindex+"、"+answer.generateValueHtml());
							subAnswerindex++;
							sb.append("<br/>");
						}
					}
				}
				
				
				sb.append(question.getAnalysis());
				out.write(sb.toString());
			}
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImportQuestionException e){
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param is			word的xml格式文件输入流
	 * @param xslRootPath	解析xml格式docx必须的xsl文件路径
	 * @return
	 * @throws ImportQuestionException
	 */
	public List<AbstractQuestion> readXml(InputStream is,String xslRootPath) throws ImportQuestionException {
		List<AbstractQuestion> questions = new ArrayList<AbstractQuestion>();
		SAXReader reader = new SAXReader();
		
		try {
			Document document = reader.read(is);
			this.close(is);
			Element root = document.getRootElement();
			//提取公式为latex格式
			List<Element> xmlDatas = root.selectNodes("//pkg:part[@pkg:name='/word/document.xml']/pkg:xmlData");
			Element wdocument = xmlDatas.get(0).element("document");
			List<Element> oMathElements = wdocument.selectNodes("//m:oMath");
			List<String> latexList = extractFormula(oMathElements,xslRootPath);

			//提取图片
			List<Element> rels = root.selectNodes("//pkg:part[@pkg:name='/word/_rels/document.xml.rels']/pkg:xmlData");
			Element relationships = rels.get(0).element("Relationships");
			List<WordImg> imgs = extractImg(root, wdocument, relationships);


			//获取word中的段落
			//List<Element> pElements = wdocument.element("body").elements("p");
			List<Element> pElements = wdocument.element("body").selectNodes("w:p|w:tbl");
			//将试题拆分成独立单元
			List<List<Element>> questionElements = splitQuestion(pElements);
			
			//StringSubstitutor：替换占位符的执行类
			StringSubstitutor stringSubstitutor = getStrSubstitutoer(imgs, latexList);
			
			//将element转换成question
			int seq = 1;
			for (List<Element> elementList : questionElements) {
				AbstractQuestion question = createQuestion(elementList,stringSubstitutor,seq++);
				questions.add(question);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return questions;
	}
	
	/*private StringSubstitutor getStrSubstitutoer(List<WordImg> imgs,List<String> latexList) {
		Map<String, String> replaceValue = new HashMap();
		//用<img>标签替换图片的占位符
		for(int i=0;i<imgs.size();i++) {
			WordImg wordImg = imgs.get(i);
			String imgData = wordImg.getData();
			String imgStyle = "width:"+wordImg.getWidth()+";height:"+wordImg.getHeight()+";";
			String imgHtml = "<img class=\"img\" style=\""+imgStyle+"vertical-align: middle;\" src=\""+imgData+"\" >";
			replaceValue.put("img:"+i, imgHtml);
		}
		//用<img>标签替换公式占位符
		for(int i=0;i<latexList.size();i++) {
			String latex = latexList.get(i);
			String latexImgStr = ImgConverter.latex2Png(latex);

			String imgHtml = "<img class=\"img\"  data-latex=\""+latex+"\" class=\"kfformula\" src=\"data:image/jpeg|png|gif;base64,"+latexImgStr+"\" >";
			replaceValue.put("id:"+i, imgHtml);
		}
		StringSubstitutor stringSubstitutor = new StringSubstitutor(replaceValue);
		return stringSubstitutor;
	}*/

	private StringSubstitutor getStrSubstitutoer(List<WordImg> imgs,List<String> latexList) throws ImportQuestionException {
		Map<String, String> replaceValue = new HashMap();
		//用<img>标签替换图片的占位符
		for(int i=0;i<imgs.size();i++) {
			WordImg wordImg = imgs.get(i);
			String imgData = wordImg.getData();
			String imgStyle = "width:"+wordImg.getWidth()+";height:"+wordImg.getHeight()+";";
			String imgHtml = "<img class=\"img\" style=\""+imgStyle+"vertical-align: middle;\" src=\""+imgData+"\" >";
			replaceValue.put("img:"+i, imgHtml);
		}
		//用<img>标签替换公式占位符
		for(int i=0;i<latexList.size();i++) {
			String latex = latexList.get(i);
			String latexImgStr = ImgConverter.latex2Png(latex);
			String url = uploadImg(latexImgStr, "latex"+i+".png");
			String imgHtml = "<img class=\"img kfformula\" style=\"vertical-align: middle;\"  data-latex=\""+latex+"\" src=\""+url+"\" >";
			replaceValue.put("id:"+i, imgHtml);
		}
		StringSubstitutor stringSubstitutor = new StringSubstitutor(replaceValue);
		return stringSubstitutor;
	}

	private List<WordImg> extractImg(Element root, Element wdocument, Element relationships) throws ImportQuestionException {
		List<String> ids = new ArrayList<String>();

		List<WordImg> imgs = new ArrayList<WordImg>();
		//TODO 可以优化重复图片的处理方式
		//处理w:object类型图片
		List<Element> objectElemnt = wdocument.selectNodes("//w:object");
		int imgIndex = 0;
		for (int i = 0; i < objectElemnt.size(); i++) {
			Element element = objectElemnt.get(i);
			Element shape = element.element("shape");
			String style = shape.attributeValue("style");
			Element imagedata = shape.element("imagedata");
			String rid = imagedata.attributeValue("id");
			if(StringUtils.isBlank(rid)){
				continue;
			}
			// 替换为占位符
			replaceTextElement(element, "${img:" + imgIndex + "}");
			/*if(ids.contains(rid)) {
				//replaceTextElement(element, "");
				continue;
			}*/
			ids.add(rid);
			String imgDataStr = uploadImgById(root, relationships, rid);
			WordImg img = new WordImg();
			img.setData(imgDataStr);
			parseStyle(style, img);
			imgs.add(img);



			imgIndex++;
		}
		//处理w:drawing类型的图片
		List<Element> drawingElemnts = wdocument.selectNodes("//w:drawing");
		for(int i = 0; i < drawingElemnts.size(); i++) {
			Element drawingElement = drawingElemnts.get(i);
			List<Element> extents =drawingElement.selectNodes("//wp:extent");
			Element extent = extents.get(i);
			String widthStr = extent.attributeValue("cx");
			String heightStr = extent.attributeValue("cy");

			List<Element> graphics = XmlConverter.getNodes(drawingElement,"graphic");
			Element graphic = graphics.get(0);

			List<Element> blips = graphic.selectNodes("//a:blip");
			Element blip = blips.get(i);
			String imgId = blip.attributeValue("embed");
			if(StringUtils.isBlank(imgId)){
				continue;
			}
			// 替换为占位符
			replaceTextElement(drawingElement, "${img:" + imgIndex + "}");
			/*if(ids.contains(imgId)) {
				//replaceTextElement(drawingElement, "");
				continue;
			}*/
			ids.add(imgId);

			String imgDataStr = uploadImgById(root, relationships, imgId);


			int width = Integer.valueOf(widthStr)/9525;
			int height = Integer.valueOf(heightStr)/9525;

			WordImg img = new WordImg();
			img.setWidth(width+"px");
			img.setHeight(height+"px");
			img.setData(imgDataStr);
			imgs.add(img);

			imgIndex++;
		}
		List<Element> pictElements = wdocument.selectNodes("//w:pict");
		for(int i = 0; i < pictElements.size(); i++) {
			Element pictElement = pictElements.get(i);
			Element shape = pictElement.element("shape");
			Element imagedata = shape.element("imagedata");
			String imgId = imagedata.attributeValue("id");
			if(StringUtils.isBlank(imgId)){
				continue;
			}
			// 替换为占位符
			replaceTextElement(pictElement, "${img:" + imgIndex + "}");
			/*if(ids.contains(imgId)) {
				//replaceTextElement(pictElement, "");
				continue;
			}*/
			ids.add(imgId);

			String style = shape.attributeValue("style");
			String[] strs = style.split(";");
			WordImg img = new WordImg();
			parseStyle(style, img);
			String imgDataStr = uploadImgById(root, relationships, imgId);
			img.setData(imgDataStr);
			imgs.add(img);

			imgIndex++;
		}

		return imgs;
	}
	
	private void replaceTextElement(Element element, String text) {
		element.setName("w:r");
		Element wrPr = element.addElement("w:rPr");
		wrPr.addElement("w:b");
		Element wt = element.addElement("w:t");
		wt.setText(text);
	}

	private String uploadImgById(Element root, Element relationships, String rid) throws ImportQuestionException {
		String target = "";
		List<Element> imgIds = relationships.elements("Relationship");
		for (Element imgId : imgIds) {
			if (rid.equals(imgId.attributeValue("Id"))) {
				target = imgId.attributeValue("Target");
			}
		}

		List<Element> imgData = root.selectNodes("//pkg:part[@pkg:name='/word/" + target + "']/pkg:binaryData");
		String imgDataStr = imgData.get(0).getText();
		String fileName = target.substring(target.lastIndexOf("/")+1);
		String[] nameStrs = fileName.split("\\.");
		String prefix = nameStrs.length > 0?nameStrs[0]:"";
		String suffix = nameStrs.length == 2? nameStrs[1]: "";


		if("wmf".equals(suffix)) {
			try {
				fileName = prefix+"."+"svg";
				imgDataStr = ImgConverter.wmfConversionSvg(imgDataStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String url = uploadImg(imgDataStr,fileName);
		return url;
	}

	private String uploadImg(String base64Str,String fileName) throws ImportQuestionException{
		String url = "";
		try {
			url = FastDFSClient.upload(base64Str,fileName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ImportQuestionException("网络不稳定，请稍后重试！");
		}
		return url;
	}

	private String getImgDataById(Element root, Element relationships, String rid) {
		String target = "";
		List<Element> imgIds = relationships.elements("Relationship");
		for (Element imgId : imgIds) {
			if (rid.equals(imgId.attributeValue("Id"))) {
				target = imgId.attributeValue("Target");
			}
		}
		
		List<Element> imgData = root.selectNodes("//pkg:part[@pkg:name='/word/" + target + "']/pkg:binaryData");
		String imgDataStr = imgData.get(0).getText();
		String[] nameStrs = target.split("\\.");
		String suffix = nameStrs.length == 2? nameStrs[1]: "";
		if("wmf".equals(suffix)) {
			try {
				// wmf转为svg的base64格式，"data:image/svg+xml;utf8,"直接跟svg的xml字符串遇到某些特殊字符串图片无法展示
				// 改为"data:image/svg+xml;base64,"跟svg的xml转为base64的格式就能正常显示
				imgDataStr = "data:image/svg+xml;base64,"+ImgConverter.wmfConversionSvg(imgDataStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			imgDataStr = "data:image/jpeg|png|gif;base64,"+imgDataStr;
		}
		return imgDataStr;
	}

	private List<String> extractFormula(List<Element> elements,String xslRootPath) {
		List<String> latexList = new ArrayList<String>();
		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);

			String latex = XmlConverter.convertMML2Latex(XmlConverter.convertOMML2MML(element.asXML(),xslRootPath),xslRootPath);
			latexList.add(latex);

			element.setName("w:r");
			element.clearContent();

			Element wrPr = element.addElement("w:rPr");
			wrPr.addElement("w:b");
			Element wt = element.addElement("w:t");
			wt.setText("${id:" + i + "}");

		}
		return latexList;
	}

	private void parseStyle(String style, WordImg img) {
		String[] strs = style.split(";");
		for(String str:strs) {
			String[] keyValue = str.split(":");
			if(str.startsWith("width")) {
				img.setWidth(keyValue[1]);
			}
			if(str.startsWith("height")) {
				img.setHeight(keyValue[1]);
			}
		}
	}

	private static AbstractQuestion createQuestion(List<Element> pList,StringSubstitutor stringSubstitutor,int seq) throws ImportQuestionException {
		if (pList == null || pList.size() == 0) {
			return null;
		}
		
		int type = getQuestionType(pList);
		AbstractQuestion question = QuestionFactory.createQuestion(type);
		if(question == null){
			throw new ImportQuestionException("第"+seq+"题为不能处理的题型");
		}
		question.setSeq(seq);
		
		List<String> splitStrs = QuestionComponent.getKeys();
		//提取题文为html
		StringBuffer topic = WordHandler.extractQuestionComponent(pList, QuestionComponent.TOPIC.getKey(), splitStrs);
		//将体型从题文中去除
		String topicStr = HTMLHandler.extractTextFromHtml(topic.toString(), QuestionType.getKeyByCode(type));
		//提取解析为html
		StringBuffer analysis = WordHandler.extractQuestionComponent(pList, QuestionComponent.ANALYSIS.getKey(), splitStrs);
		
		//提取选项
		List<Element> optionsElements = WordHandler.splitQuestionContent(pList, QuestionComponent.OPTIONS.getKey(), splitStrs,false);
		//提取答案
		List<Element> answerElements = WordHandler.splitQuestionContent(pList, QuestionComponent.ANSWER.getKey(), splitStrs,false);
		//提取解析
		List<Element> analysisElements = WordHandler.splitQuestionContent(pList, QuestionComponent.ANALYSIS.getKey(), splitStrs,false);
		//提取科目
		StringBuffer subjectHtml = WordHandler.extractQuestionComponent(pList, QuestionComponent.SUBJECT.getKey(), splitStrs);
		String subject = HTMLHandler.getTextFromHtml(subjectHtml.toString());
		//提取学期
		StringBuffer termHtml = WordHandler.extractQuestionComponent(pList, QuestionComponent.TERM.getKey(), splitStrs);
		String term = HTMLHandler.getTextFromHtml(termHtml.toString());
		//提取类型
		StringBuffer scopeHtml = WordHandler.extractQuestionComponent(pList, QuestionComponent.SCOPE.getKey(), splitStrs);
		String scope = HTMLHandler.getTextFromHtml(scopeHtml.toString());
		//提取年级
		StringBuffer gradeHtml = WordHandler.extractQuestionComponent(pList, QuestionComponent.GRADE.getKey(), splitStrs);
		String grade = HTMLHandler.getTextFromHtml(gradeHtml.toString());
		//提取知识点
		StringBuffer knowledge = WordHandler.extractQuestionComponent(pList, QuestionComponent.KNOWLEDGE_POINTS.getKey(), splitStrs);
		String knowledgeStr = HTMLHandler.getTextFromHtml(knowledge.toString());
		//提取星级
		List<Element> starElements = WordHandler.splitQuestionContent(pList, QuestionComponent.STAR.getKey(), splitStrs,false);
		//提取考点
		List<Element> examinationElements = WordHandler.splitQuestionContent(pList, QuestionComponent.EXAMINATION_POINT.getKey(), splitStrs,false);
		List<Element> recommendElements = WordHandler.splitQuestionContent(pList, QuestionComponent.RECOMMEND.getKey(), splitStrs,false);

		QuestionElementContainer elementContainer = new QuestionElementContainer();
		elementContainer.setOptionsElements(optionsElements);
		elementContainer.setAnswerElements(answerElements);
		elementContainer.setAnalysisElements(analysisElements);
		elementContainer.setStarElements(starElements);
		elementContainer.setExaminationElements(examinationElements);
		elementContainer.setRecommendElements(recommendElements);

		//获取选项和答案
		List<SubQuestion> subQuestions = question.splitSubQuestion(elementContainer);
		
		question.setTopic(topicStr);
		question.setAnalysis(analysis.toString());
		question.setSubQuestions(subQuestions);
		question.setGrade(grade);
		question.setSubject(subject);
		question.setKnowledgePoint(knowledgeStr);
		question.setTerm(term);
		question.setScope(scope);
		//替换占位符
		question.replace(stringSubstitutor);

		return question;
	}
	
	private static int getQuestionType(List<Element> questionList) {
		
		for (Element element : questionList) {
			String text = WordHandler.getText(element).trim();
			if(text.contains("[") && text.contains("]")) {
				String key = text.substring(text.indexOf("["),text.indexOf("]")+1);
				QuestionType questionType = QuestionType.getQuestionTypeByKey(key);
				if(questionType != null) {
					return questionType.getCode();
				}
			}
		}
		
		return 0;
	}

	private static List<List<Element>> splitQuestion(List<Element> pElements) {
		List<List<Element>> questionList = new ArrayList<List<Element>>();
		List<Element> questions = null;
		boolean isContent = false;
		for (int i = 0; i < pElements.size(); i++) {
			Element pElement = pElements.get(i);
			String text = WordHandler.getText(pElement).trim();
			if (text.startsWith(QuestionComponent.TOPIC.getKey())) {
				questions = new ArrayList<Element>();
				questionList.add(questions);
				isContent = true;
			}
			if (isContent) {
				questions.add(pElement);
			}
			if (text.startsWith(QuestionComponent.END.getKey())) {
				isContent = false;
			}
		}

		return questionList;
	}

	private void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
