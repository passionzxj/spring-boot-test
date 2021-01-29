package com.zhang.jwt.word.question;

import lombok.Data;
import org.dom4j.Element;

import java.util.List;

@Data
public class QuestionElementContainer {
    private List<Element> optionsElements;
    private List<Element> answerElements;
    private List<Element> analysisElements;

    private List<Element> starElements;
    private List<Element> examinationElements;
    private List<Element> recommendElements;
}
