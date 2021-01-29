package com.zhang.jwt.word.question.ext;

import com.zhang.jwt.word.enumeration.QuestionComponent;
import com.zhang.jwt.word.exception.ImportQuestionException;
import com.zhang.jwt.word.handler.HTMLHandler;
import com.zhang.jwt.word.handler.WordHandler;
import com.zhang.jwt.word.question.*;
import com.zhang.jwt.word.question.helper.ChoiceQuestionHelper;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 多选题
 * @author tiancaixi
 */
public class MultipleChoiceQuestion extends AbstractQuestion {


	public List<Option> splitQuestionOption(List<Element> optionsElements) {
		StringBuffer sb1 = new StringBuffer();
		for(Element element:optionsElements) {
			sb1.append(WordHandler.getText(element));
		}

		List<List<Element>> result = new ArrayList<List<Element>>();
		String[] optionKeys = Option.keys;
		List<String> keyList = new ArrayList<String>(optionKeys.length);
		Collections.addAll(keyList,optionKeys);
		//分割选项
		List<Element> optionElements = null;
		for (Element element : optionsElements) {
			String text = WordHandler.getText(element).trim();
			for(int i=0;i<keyList.size();i++) {
				String key = keyList.get(i);
				if(text.startsWith(key)) {
					optionElements = new ArrayList<Element>();
					result.add(optionElements);
					break;
				}
			}
			if(optionElements!=null) {
				optionElements.add(element);
			}
		}
		//类型转换
		List<Option> list = new ArrayList<Option>();
		for(int i=0;i<result.size();i++) {
			List<Element> elements = result.get(i);
			StringBuffer sb = new StringBuffer();
			String key = Option.keys[i];
			for(int j=0;j<elements.size();j++) {
				Element element  = elements.get(j);
				String excludeStr ="";
				if(j==0) {
					excludeStr = key;
				}
				String html = WordHandler.getHtml(element,excludeStr);
				sb.append(html);
			}
			Option option = new Option();
			option.setKey(key.substring(0,1));
			option.setValue(sb.toString());
			list.add(option);
		}

		return list;
	}

	public Answer splitQuestionAnswer(List<Element> answerElements) throws ImportQuestionException {

		StringBuffer answerHtml = WordHandler.extractQuestionComponent(answerElements, QuestionComponent.ANSWER.getKey(), QuestionComponent.getKeys());
		String text = HTMLHandler.getTextFromHtml(answerHtml.toString());
		text = text.replace("  ", " ").replace(" ", " ").trim();//处理特殊的空格符
		if(StringUtils.isBlank(text.trim())){
			throw new ImportQuestionException("第"+getSeq()+"题答案为空");
		}
		//将中文空格替换为英文空格
		text = text.replace(" "," ");
		String[] answerStrs = text.split(" ");
		Answer answer = new Answer();
		List<String> values = new ArrayList<String>();
		answer.setValues(values);
		for(String answerStr:answerStrs) {
			if(answerStr.length()>1){
				throw new ImportQuestionException("第"+getSeq()+"题答案格式错误");
			}
			values.add(answerStr.toUpperCase());
		}

		return answer;
	}

	public List<SubQuestion> splitSubQuestion(QuestionElementContainer elementContainer) throws ImportQuestionException {
		List<SubQuestion> list = ChoiceQuestionHelper.splitSubQuestion(elementContainer,this);
		return list;
	}

}
