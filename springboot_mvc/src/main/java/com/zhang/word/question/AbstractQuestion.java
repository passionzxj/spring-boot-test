package com.zhang.word.question;

import com.zhang.word.enumeration.QuestionComponent;
import com.zhang.word.enumeration.QuestionStar;
import com.zhang.word.exception.ImportQuestionException;
import com.zhang.word.handler.QuestionHandler;
import com.zhang.word.handler.WordHandler;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class AbstractQuestion implements QuestionResolver{
	
	int seq = 0;
	int type;
	String topic;		//题文
	String analysis;	//解析

	String subject;		//科目
	String grade;		//年级
	String knowledgePoint;	//知识点

	String term;
	String scope;

	List<SubQuestion> subQuestions;
	
	public void replace(StringSubstitutor stringSubstitutor) {
		if(topic != null) {
			topic = stringSubstitutor.replace(topic.toString());
		}
		if(analysis != null) {
			analysis = stringSubstitutor.replace(analysis.toString());
		}
		if(subQuestions != null) {
			for(SubQuestion optionAndAnswer: subQuestions) {
				optionAndAnswer.replace(stringSubstitutor);
			}
		}
	}

	@Override
	public List<Integer> splitQuestionStar(List<Element> starElements) throws ImportQuestionException {
		StringBuffer sb = new StringBuffer();
		for(Element element:starElements) {
			sb.append(WordHandler.getText(element));
		}
		String text = sb.toString();
		//将中文空格替换为英文空格
		text = text.replace(" "," ").replace(QuestionComponent.STAR.getKey(),"");
		text = text.trim();
		if(StringUtils.isBlank(text)){
			throw new ImportQuestionException("第"+getSeq()+"题星级为空");
		}

		List<Integer> list = new ArrayList<>();
		if(text.startsWith("1、")){
			//包含多个小题
			List<String> starStrs = QuestionHandler.splitBySequence(text);
			for(String starStr:starStrs){
				QuestionStar questionStar = QuestionStar.getQuestionStarByKey(starStr);
				if(questionStar == null){
					throw new ImportQuestionException("第"+getSeq()+"题星级填写错误");
				}
				list.add(questionStar.getCode());
			}
		}else{
			//不包含小题
			QuestionStar questionStar = QuestionStar.getQuestionStarByKey(text);
			if(questionStar == null){
				throw new ImportQuestionException("第"+getSeq()+"题星级填写错误");
			}
			list.add(questionStar.getCode());
		}

		return list;
	}

	@Override
	public List<String> splitQuestionExamination(List<Element> examElements) throws ImportQuestionException {
		String text = WordHandler.getText(examElements);
		//将中文空格替换为英文空格
		text = text.replace(" "," ").replace(QuestionComponent.EXAMINATION_POINT.getKey(),"");
		text = text.trim();
		if(StringUtils.isBlank(text)){
			throw new ImportQuestionException("第"+getSeq()+"题考点为空");
		}
		List<String> list = new ArrayList<>();
		if(text.startsWith("1、")){
			//包含多个小题
			list = QuestionHandler.splitBySequence(text);
		}else{
			//不包含小题
			list.add(text);
		}
		return list;
	}

	@Override
	public List<String> splitQuestionRecommend(List<Element> recommendElements) throws ImportQuestionException {
		String text = WordHandler.getText(recommendElements);
		//将中文空格替换为英文空格
		text = text.replace(" "," ").replace(QuestionComponent.RECOMMEND.getKey(),"");
		text = text.trim();
		if(StringUtils.isBlank(text)){
			throw new ImportQuestionException("第"+getSeq()+"题课程推荐为空");
		}
		List<String> list = new ArrayList<>();
		if(text.startsWith("1、")){
			//包含多个小题
			list = QuestionHandler.splitBySequence(text);
		}else{
			//不包含小题
			list.add(text);
		}

		return list;
	}

	public short calculateAverageDifficulty(){
		double sum = 0;
		for(SubQuestion subQuestion:subQuestions){
			sum += subQuestion.getDifficulty();
		}
		if(subQuestions.size()>0){
			double average = sum/subQuestions.size();
			Long result = Math.round(average);
			return result.shortValue();
		}
		return 1;
	}
}
