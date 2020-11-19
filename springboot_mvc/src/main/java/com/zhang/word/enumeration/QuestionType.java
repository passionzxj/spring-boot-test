package com.zhang.word.enumeration;

public enum QuestionType{

		//题型 1:单选 2:多选 3:填空 4:问答 5:阅读理解 6:完型填空 7:短文填空 8:完成对话 9:判断题 10:选择填空
		SINGLECHOICE("[单选题]",1),
		MULTIPLECHOICE("[多选题]",2),
		COMPLETION("[填空题]",3),
		READINGCOMPREHENSION("[阅读理解]",5),
		CLOZETEST("[完形填空]",6),

		CHOICE_COMPLETION("[选择填空]",10);

		private String key;
		private int code;
		private QuestionType(String key,int code) {
			this.key = key;
			this.code = code;
		}
		
		public String getKey() {
			return key;
		}
		public int getCode() {
			return code;
		}
		
		public static QuestionType getQuestionTypeByKey(String key) {
			QuestionType[] questionTypes = QuestionType.values();
			for(QuestionType questionType:questionTypes) {
				if(questionType.key.equals(key)) {
					return questionType;
				}
			}
			return null;
		}

		public static String getKeyByCode(int code){
			QuestionType[] questionTypes = QuestionType.values();
			for(QuestionType questionType:questionTypes) {
				if(questionType.code == code) {
					return questionType.getKey();
				}
			}
			return null;
		}
		
	}