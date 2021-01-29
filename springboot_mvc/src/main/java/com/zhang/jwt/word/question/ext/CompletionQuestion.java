package com.zhang.jwt.word.question.ext;

import com.zhang.jwt.word.question.*;
import com.zhang.jwt.word.handler.WordHandler;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 填空题
 * @author tiancaixi
 *
 */
public class CompletionQuestion extends AbstractQuestion {
	
	public static void main(String[] args) {
		String html = "<p><span>123&nbsp;&nbsp;</span></p>";
		int index = 24;
		String str = html.substring(0,index);
		System.out.println(str);
		String str1 = "   ";
		String str2 = "   ";
		System.out.println(str1.length());
		
	}


	public List<Option> splitQuestionOption(List<Element> optionsElements) {
		List<Option> list = new ArrayList<Option>();
		return list;
	}

	public Answer splitQuestionAnswer(List<Element> answerElements){
		return null;
	}


	public List<SubQuestion> splitSubQuestion(QuestionElementContainer elementContainer) {
		List<Element> optionsElements = elementContainer.getOptionsElements();
		List<Element> answerElements = elementContainer.getAnswerElements();
		List<Element> analysisElements = elementContainer.getAnalysisElements();
		List<Answer> answers = splitQuestionAnswers(answerElements);
		List<SubQuestion> list = new ArrayList<SubQuestion>();
		for(Answer answer :answers) {
			SubQuestion optionAndAnswer = new SubQuestion();
			List<Option> options = new ArrayList<Option>();
			optionAndAnswer.setOptions(options);
			optionAndAnswer.setAnswer(answer);
			list.add(optionAndAnswer);
		}
		return list;
	}

	private List<Answer> splitQuestionAnswers(List<Element> answerElements){

		List<Answer> answers = new ArrayList<Answer>();

		StringBuffer textSb = new StringBuffer();
		for(Element element: answerElements) {
			String str = WordHandler.getText(element);
			textSb.append(str);
		}

		//处理包含多个答案的情况
		int currentAnswerIndex = 1;
		String text = textSb.toString();
		while(true) {
			String sequenceStr = currentAnswerIndex+"、";
			String nextSequenceStr = (currentAnswerIndex+1)+"、";
			int startIndex = text.indexOf(sequenceStr);
			int endIndex = text.indexOf(nextSequenceStr);

			if(startIndex == -1) {
				break;
			}
			String answerStr = null;
			if(startIndex>-1 && endIndex>-1) {
				answerStr = text.substring(startIndex,endIndex);
				currentAnswerIndex++;
			}
			if(startIndex>-1 && endIndex == -1) {
				answerStr = text.substring(startIndex);
				currentAnswerIndex++;
			}

			if(answerStr!=null) {
				answerStr = answerStr.replace(sequenceStr, "").replace("  ", " ").replace(" ", " ").trim();//处理特殊的空格符
				String[] answerStrs = answerStr.split(" +");
				for(int i=0;i<answerStrs.length;i++) {
					answerStrs[i] = "<span>"+answerStrs[i]+"</span>";
				}
				Answer answer = new Answer();
				List<String> values = new ArrayList<String>();
				Collections.addAll(values, answerStrs);
				answer.setValues(values);
				answers.add(answer);
			}
		}
		//处理只有一个答案的情况
		String sequenceStr = "1、";
		if(text.indexOf(sequenceStr) == -1) {
			String[] answerStrs = text.split(" +");
			Answer answer = new Answer();
			List<String> values = new ArrayList<String>();
			Collections.addAll(values, answerStrs);
			answer.setValues(values);
			answers.add(answer);
		}

		return answers;
	}


}
