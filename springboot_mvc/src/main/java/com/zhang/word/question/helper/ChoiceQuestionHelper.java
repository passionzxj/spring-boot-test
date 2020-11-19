package com.zhang.word.question.helper;

import com.zhang.word.enumeration.QuestionComponent;
import com.zhang.word.exception.ImportQuestionException;
import com.zhang.word.handler.WordHandler;
import com.zhang.word.question.*;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class ChoiceQuestionHelper {

    public static List<SubQuestion> splitSubQuestion(QuestionElementContainer elementContainer, AbstractQuestion question) throws ImportQuestionException {
        List<Element> optionsElements = elementContainer.getOptionsElements();
        List<Element> answerElements = elementContainer.getAnswerElements();
        List<Element> analysisElements = elementContainer.getAnalysisElements();

        List<Option> options = question.splitQuestionOption(optionsElements);
        Answer answer = question.splitQuestionAnswer(answerElements);
        List<String> analysisList = splitAnalysis(analysisElements);

        if(options == null || options.size() == 0){
            throw new ImportQuestionException("第"+question.getSeq()+"题选项为空");
        }
        SubQuestion optionAndAnswer = new SubQuestion();
        optionAndAnswer.setOptions(options);
        optionAndAnswer.setAnswer(answer);
        if(analysisList != null){
            optionAndAnswer.setAnalysis(analysisList.get(0));
        }

        List<Element> starElements = elementContainer.getStarElements();
        List<Element> examPointElements = elementContainer.getExaminationElements();
        List<Element> recommendElements = elementContainer.getRecommendElements();
        List<Integer> stars =  question.splitQuestionStar(starElements);
        List<String> examPoints = question.splitQuestionExamination(examPointElements);
        List<String> recommends = question.splitQuestionRecommend(recommendElements);
        //TODO 内容没有是提示异常
        optionAndAnswer.setDifficulty(stars.get(0));
        optionAndAnswer.setExaminationPoint(examPoints.get(0));
        optionAndAnswer.setRecommend(recommends.get(0));

        List<SubQuestion> list = new ArrayList<SubQuestion>();
        list.add(optionAndAnswer);
        return list;
    }

    private static List<String> splitAnalysis(List<Element> analysisElements){
        List<String> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        for(Element element:analysisElements) {
            String html = WordHandler.getHtml(element, QuestionComponent.ANALYSIS.getKey());
            sb.append(html);
        }
        list.add(sb.toString());
        return list;
    }
}
