package com.zhang.word.question;

import lombok.Data;

import java.util.List;

@Data
public class Answer {
	//int seq;
	List<String> values;	//一个填空题有多个空，多选题都会有多个value
	
	public String generateValueHtml() {
		StringBuffer reuslt = new StringBuffer();
		reuslt.append("<span>");
		for(String value:values) {
			reuslt.append(value+"&nbsp;");
		}
		reuslt.append("</span>");
		return reuslt.toString();
	}
}