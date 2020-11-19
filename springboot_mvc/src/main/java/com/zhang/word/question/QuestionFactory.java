package com.zhang.word.question;


import com.zhang.word.enumeration.QuestionType;
import com.zhang.word.question.ext.*;

/**
 * 创建Question的简单工厂类
 * @author tiancaixi
 *
 */
public class QuestionFactory{

	/**
	 * 根据type创建对应类型的question
	 * @param type
	 * @return
	 */
	public static AbstractQuestion createQuestion(int type){

		AbstractQuestion question = null;
		if(type == QuestionType.SINGLECHOICE.getCode()){
			question = new SingleChoiceQuestion();
		}if(type == QuestionType.MULTIPLECHOICE.getCode()){
			question = new MultipleChoiceQuestion();
		}/*else if(type == QuestionType.COMPLETION.getCode()){
			question = new CompletionQuestion();
		}*/else if(type == QuestionType.CLOZETEST.getCode() || type == QuestionType.CHOICE_COMPLETION.getCode()) {
			question = new ClozetestQuestion();
		} if(type == QuestionType.READINGCOMPREHENSION.getCode()) {
			question = new ReadingComprehensionQuestion();
		} else if(type == QuestionType.CHOICE_COMPLETION.getCode()){
			question = new ChoiceCompletionQuestion();
		}
		if(question != null) {
			question.setType(type);
		}
		return question;
	}
}
