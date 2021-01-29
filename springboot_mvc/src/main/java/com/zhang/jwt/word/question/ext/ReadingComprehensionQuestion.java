package com.zhang.jwt.word.question.ext;

import com.zhang.jwt.word.exception.ImportQuestionException;
import com.zhang.jwt.word.handler.QuestionHandler;
import com.zhang.jwt.word.handler.WordHandler;
import com.zhang.jwt.word.question.*;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 阅读理解
 * @author tiancaixi
 */
public class ReadingComprehensionQuestion extends AbstractQuestion {

	public List<Option> splitQuestionOption(List<Element> optionsElements) {
		List<Option> list = new ArrayList<Option>();
		return list;
	}

	public Answer splitQuestionAnswer(List<Element> answerElements){
		return null;
	}

	/*public List<SubQuestion> splitSubQuestion(QuestionElementContainer elementContainer) {
		List<Element> optionsElements = elementContainer.getOptionsElements();
		List<Element> answerElements = elementContainer.getAnswerElements();
		List<Element> analysisElements = elementContainer.getAnalysisElements();

		StringBuffer optionSb = new StringBuffer();
		for(Element element:optionsElements) {
			String text = WordHandler.getText(element).trim();
			optionSb.append(text);
		}

		StringBuffer answerSb = new StringBuffer();
		for(Element element:answerElements) {
			String text = WordHandler.getText(element).trim();
			answerSb.append(text);
		}

		StringBuffer analysisSb = new StringBuffer();
		for(Element element:analysisElements) {
			String text = WordHandler.getText(element).trim();
			analysisSb.append(text);
		}

		List<String> optionStrs = QuestionHandler.extractOptionStr(optionSb.toString());
		List<String> answerStrs = QuestionHandler.extractAnswerStr(answerSb.toString(),true);
		List<String> analysisStrs = QuestionHandler.extractAnalysisStrs(analysisSb.toString());
		List<SubQuestion> subQuestions = new ArrayList<SubQuestion>();
		for(int i=0;i<optionStrs.size();i++) {
			String optionStr = optionStrs.get(i);
			String answerStr = answerStrs.get(i);
			String analysisStr = analysisStrs.get(i);
			String topic = extractTopicStr(optionStr);
			SubQuestion question = new SubQuestion();

			question.setTopic(topic);
			question.setOptions(QuestionHandler.createOption(optionStr));
			question.setAnswer(QuestionHandler.createAnswer(answerStr));
			question.setAnalysis(analysisStr);
			subQuestions.add(question);
		}
		return subQuestions;
	}*/

	public List<SubQuestion> splitSubQuestion(QuestionElementContainer elementContainer) throws ImportQuestionException {
		List<Element> optionsElements = elementContainer.getOptionsElements();
		List<Element> answerElements = elementContainer.getAnswerElements();
		List<Element> analysisElements = elementContainer.getAnalysisElements();
		List<Element> starElements = elementContainer.getStarElements();
		List<Element> examPointElements = elementContainer.getExaminationElements();
		List<Element> recommendElements = elementContainer.getRecommendElements();

		//分割选项
		StringBuffer optionSb = new StringBuffer();
		for(Element element:optionsElements) {
			String text = WordHandler.getText(element).trim();
			optionSb.append(text);
		}

		StringBuffer answerSb = new StringBuffer();
		for(Element element:answerElements) {
			String text = WordHandler.getText(element).trim();
			answerSb.append(text);
		}

		StringBuffer analysisSb = new StringBuffer();
		for(Element element:analysisElements) {
			String text = WordHandler.getText(element).trim();
			analysisSb.append(text);
		}



		List<String> optionStrs = QuestionHandler.extractOptionStr(optionSb.toString());
		List<String> answerStrs = QuestionHandler.extractAnswerStr(answerSb.toString(),true);
		List<String> analysisStrs = QuestionHandler.extractAnalysisStrs(analysisSb.toString());

		List<Integer> stars = splitQuestionStar(starElements);
		List<String> examinations = splitQuestionExamination(examPointElements);
		List<String> recommends = splitQuestionRecommend(recommendElements);

		List<SubQuestion> subQuestions = new ArrayList<SubQuestion>();
		if(optionStrs.size() == 0){
			throw new ImportQuestionException("第"+getSeq()+"题选项为空");
		}
		if(optionStrs.size() != answerStrs.size()){
			throw new ImportQuestionException("第"+getSeq()+"题答案和选项个数不对应");
		}
		if(optionStrs.size() != analysisStrs.size()){
			throw new ImportQuestionException("第"+getSeq()+"题解析和选项个数不对应");
		}
		if(optionStrs.size() != stars.size()){
			throw new ImportQuestionException("第"+getSeq()+"题星级和选项个数不对应");
		}
		if(optionStrs.size() != examinations.size()){
			throw new ImportQuestionException("第"+getSeq()+"题考点和选项个数不对应");
		}
		if(optionStrs.size() != recommends.size()){
			throw new ImportQuestionException("第"+getSeq()+"题课程推荐和选项个数不对应");
		}

		for(int i=0;i<optionStrs.size();i++) {
			String optionStr = optionStrs.get(i);
			String answerStr = answerStrs.get(i);
			if(answerStr.length()>1){
				int subSeq = i+1;
				throw new ImportQuestionException("第"+getSeq()+"题第"+subSeq+"小题答案格式错误");
			}

			String analysisStr = analysisStrs.get(i);
			Integer star = stars.get(i);
			String examinationPoint = examinations.get(i);
			String recommend = recommends.get(i);
			String topic = extractTopicStr(optionStr);

			SubQuestion question = new SubQuestion();
			question.setTopic(topic);
			question.setOptions(QuestionHandler.createOption(optionStr));
			question.setAnswer(QuestionHandler.createAnswer(answerStr));
			question.setAnalysis(analysisStr);
			question.setDifficulty(star);
			question.setExaminationPoint(examinationPoint);
			question.setRecommend(recommend);
			subQuestions.add(question);
		}
		//类型转换
		return subQuestions;
	}

	private String extractTopicStr(String text) {
		//从"A."开始之前的部分都是题文
		String topic = text.substring(0,text.indexOf(Option.keys[0]));
		return topic;
	}

}
