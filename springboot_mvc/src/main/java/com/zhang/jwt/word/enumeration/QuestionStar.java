package com.zhang.jwt.word.enumeration;

public enum QuestionStar {

		THREE("三星",3),
		FOUR("四星",4),
		FIVE("五星",5);

		private String key;
		private int code;
		private QuestionStar(String key, int code) {
			this.key = key;
			this.code = code;
		}
		
		public String getKey() {
			return key;
		}
		public int getCode() {
			return code;
		}
		
		public static QuestionStar getQuestionStarByKey(String key) {
			QuestionStar[] questionTypes = QuestionStar.values();
			for(QuestionStar questionType:questionTypes) {
				if(questionType.key.equals(key)) {
					return questionType;
				}
			}
			return null;
		}

		public static String getKeyByCode(int code){
			QuestionStar[] questionTypes = QuestionStar.values();
			for(QuestionStar questionType:questionTypes) {
				if(questionType.code == code) {
					return questionType.getKey();
				}
			}
			return null;
		}
		
	}