package com.zhang.jwt.word.question;

import lombok.Data;
import org.apache.commons.text.StringSubstitutor;

import java.util.List;

/**
 * 	子题目，包含选项和答案
 * @author tiancaixi
 *
 */
@Data
public class SubQuestion {
	
	String topic;			//题文

	String analysis;			//解析
	int difficulty;				//难度
	int knowledgePoints;		//知识点
	String examinationPoint;	//考点
	String recommend;			//课程推荐

	Answer answer;				//答案
	List<Option> options;		//一组选项
	
	public void replace(StringSubstitutor stringSubstitutor) {
		if(answer != null) {
			List<String> values = answer.getValues();
			for(int i=0;i<values.size();i++) {
				values.set(i, stringSubstitutor.replace(values.get(i)));
			}
		}
		
		if(options != null) {
			for(Option option:options) {
				String value = option.getValue();
				option.setValue(stringSubstitutor.replace(value));
			}
		}
		
		if(topic != null) {
			topic = stringSubstitutor.replace(topic);
		}
		
		if(analysis != null) {
			analysis = stringSubstitutor.replace(analysis);
		}
	}
}
