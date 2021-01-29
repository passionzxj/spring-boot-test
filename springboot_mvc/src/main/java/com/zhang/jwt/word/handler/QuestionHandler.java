package com.zhang.jwt.word.handler;

import com.zhang.jwt.word.enumeration.QuestionComponent;
import com.zhang.jwt.word.question.Answer;
import com.zhang.jwt.word.question.Option;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author tiancaixi
 *
 */
public class QuestionHandler {

	public static List<String> extractKnowledgeStr(String text){
		text = text.replace("  ", " ").replace(" ", " ").trim();
		List<String> list = new ArrayList<String>();
		String[] strs = text.split("#");
		for(String str:strs){
			if(StringUtils.isNotBlank(str)){
				list.add(str);
			}
		}
		return list;
	}

	public static List<String> splitBySequence(String text){
		List<String> list = new ArrayList<String>();
		int currentAnswerIndex = 1;
		while(true) {
			String sequenceStr = currentAnswerIndex+"、";
			String nextSequenceStr = (currentAnswerIndex+1)+"、";
			int startIndex = text.indexOf(sequenceStr);
			int endIndex = text.indexOf(nextSequenceStr);

			if(startIndex == -1) {
				break;
			}
			String optionStr = null;
			if(startIndex>-1 && endIndex>-1) {
				optionStr = text.substring(startIndex,endIndex);
				currentAnswerIndex++;
			}
			if(startIndex>-1 && endIndex == -1) {
				optionStr = text.substring(startIndex);
				currentAnswerIndex++;
			}

			if(optionStr!=null) {
				optionStr = optionStr.replace(sequenceStr, "").replace("  ", " ").replace(" ", " ").trim();//处理特殊的空格符
				list.add(optionStr);
			}
		}
		return list;
	}

	public static List<String> extractOptionStr(String text){
		List<String> list = new ArrayList<String>();
		text = text.replace(QuestionComponent.OPTIONS.getKey(), "");

		list = splitBySequence(text);
		return list;
	}

	/**
	 * 将答案的内容截取成和题号对应的list
	 * @param text
	 * @param toUpper	是否将提取的答案转为大写（比如选择题的答案为a时转为A），<br>
	 *                     但对于填空题这类题型，答案为一个字符串时就不需要转大写
	 * @return
	 */
	public static List<String> extractAnswerStr(String text, boolean toUpper){
		List<String> list = new ArrayList<String>();
		text = text.replace(QuestionComponent.ANSWER.getKey(), "");

		list = splitBySequence(text);
		for(int i=0;i<list.size();i++){
			String answerStr = list.get(i);
			answerStr = toUpper? answerStr.toUpperCase():answerStr;
			list.set(i,answerStr);
		}
		return list;
	}

	public static List<String> extractAnalysisStrs(String extractAnalysis){
		List<String> list = new ArrayList<String>();
		extractAnalysis = extractAnalysis.replace(QuestionComponent.ANSWER.getKey(), "");

		list = splitBySequence(extractAnalysis);
		return list;

	}

	public static List<String> extractStarStr(String text){
		List<String> list = new ArrayList<String>();
		text = text.replace(QuestionComponent.STAR.getKey(), "");

		list = splitBySequence(text);
		return list;
	}
	
	public static List<Option> createOption(String optionsStr){
		List<Option> options = new ArrayList<Option>();
		
		int index = 0;
		while(true) {
			String optionKey = Option.keys[index];
			String nextOptionKey = Option.keys[index+1];
			int startIndex = optionsStr.indexOf(optionKey);
			int endIndex = optionsStr.indexOf(nextOptionKey);
			
			if(startIndex == -1) {
				break;
			}
			String optionStr = null;
			if(startIndex>-1 && endIndex>-1) {
				optionStr = optionsStr.substring(startIndex,endIndex);
				index++;
			}
			if(startIndex>-1 && endIndex == -1) {
				optionStr = optionsStr.substring(startIndex);
				index++;
			}
			
			if(optionStr != null) {
				Option option = new Option();
				option.setKey(optionKey.replace(".",""));
				option.setValue(optionStr.replace(optionKey, "").trim());
				options.add(option);
			}
			
		}
		
		return options;
	}

	public static Answer createAnswer(String answerStr){
		if(StringUtils.isBlank(answerStr.trim())) {
			return null;
		}
		//将中文空格替换为英文空格
		answerStr = answerStr.replace(" "," ");
		String[] strs = answerStr.trim().split(" ");
		Answer answer = new Answer();
		List<String> values = new ArrayList<String>();
		Collections.addAll(values, strs);
		answer.setValues(values);
		return answer;
	}
	
	public static void main(String[] args) {
		String optionsStr = "A. hate        B. Permitted    C. escaped      D. regretted";
		createOption(optionsStr);

		String str = "1、Abc你好 2、哈哈哈";
		List<String> list = extractAnswerStr(str,false);
		System.out.println(list);

		String str2 = "1、课程推荐12、课程推荐23、课程推荐34、课程推荐45、课程推荐56、课程推荐67、课程推荐78、课程推荐89、课程推荐910、课程推荐1011、课程推荐112、课程推荐213、课程推荐314、课程推荐415、课程推荐516、课程推荐617、课程推荐718、课程推荐819、课程推荐920、课程推荐10";
		List<String> list2 = splitBySequence(str2);
		System.out.println(list2);
	}
}
