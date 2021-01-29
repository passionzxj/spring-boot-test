package com.zhang.jwt.word.question;

import com.zhang.jwt.word.exception.ImportQuestionException;
import org.dom4j.Element;

import java.util.List;

public interface QuestionResolver {

	/**
	 * 分割选项
	 * @param optionsElements
	 * @return
	 */
	public abstract List<Option> splitQuestionOption(List<Element> optionsElements);
	/**
	 * 分割答案
	 * @param answerElements
	 * @return
	 */
	public abstract Answer splitQuestionAnswer(List<Element> answerElements) throws ImportQuestionException;

	/**
	 * 分割子题目
	 * @param elementContainer
	 * @return
	 * @throws ImportQuestionException
	 */
	public abstract List<SubQuestion> splitSubQuestion(QuestionElementContainer elementContainer) throws ImportQuestionException;

	public List<Integer> splitQuestionStar(List<Element> starElements) throws ImportQuestionException;

	public List<String> splitQuestionExamination(List<Element> examElements) throws ImportQuestionException;

	public List<String> splitQuestionRecommend(List<Element> recommendElements) throws ImportQuestionException;
}
