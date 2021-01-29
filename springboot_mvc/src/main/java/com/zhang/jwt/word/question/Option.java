package com.zhang.jwt.word.question;

import lombok.Data;

@Data
public class Option {
	private String key;
	private String value;
	//ABCDEF GHIJK LMN OPQ RST UVW XYX
	public static final String[] keys = {
			"A.","B.","C.","D.","E.","F.","G.",
			"H.","I.","J.","K.","L.","M.","N.","O.","P.","Q.",
			"R.","S.","T.","U.","V.","W.","X.","Y.","Z."	
			};
	
	public String generateFullOption() {
		return "<span>"+key+"</span>"+value;
	}
}