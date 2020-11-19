package com.zhang.word.enumeration;

import java.util.ArrayList;
import java.util.List;

public enum QuestionComponent{
		TOPIC("【题文】"),
		OPTIONS("【选项】"),
		ANSWER("【答案】"),
		SUBJECT("【科目】"),
		GRADE("【年级】"),
		TERM("【学期】"),
		SCOPE("【类型】"),
		ANALYSIS("【解析】"),
		KNOWLEDGE_POINTS("【知识点】"),
		STAR ("【星级】"),
		EXAMINATION_POINT ("【考点】"),
		RECOMMEND ("【课程推荐】"),
		END("【结束】");
		
		private String key;
		private QuestionComponent(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
		
		public static List<String> getKeys(){
			QuestionComponent[] components = values();
			List<String> keys = new ArrayList<String>();
			for(QuestionComponent component:components) {
				keys.add(component.key);
			}
			return keys;
		}
	}