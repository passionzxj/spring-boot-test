package com.zhang.jwt.toword;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/word")
public class WordController {
	
	@RequestMapping("/getWord")
	public void returnWord(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> map = new HashMap<>();
//		map.put("user","庞国庆");
//		map.put("title","进境动植物在线检验检疫平台");
//		List<Word> list = new ArrayList<>();
//		list.add(new Word("李四",20));
//		list.add(new Word("王五",21));
//		list.add(new Word("赵六",22));
//		map.put("list",list);
		String ftlName = "只有单选多选.xml";
		//将参数写入模板文件中
		ByteArrayOutputStream outputStream = WordUtil.process(map, ftlName);
		//返回doc文档，并下载。
		DownloadUtil.download(outputStream, response, "wqwq.doc");
	}

}