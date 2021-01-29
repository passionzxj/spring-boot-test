package com.zhang.jwt.word.handler;

import com.zhang.jwt.word.ImgConverter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.function.BiConsumer;

public class HTMLHandler {
	public static void main(String[] args) {
		
		String str = "<p>1<span>sdf【题文</span><span>】sdf</span><span>【题文】</span></p>";
		String exclude = "【题文】";
		/*String result = extractTextFromHtml(str,exclude);
		System.out.println(result);
		
		System.out.println(getTextFromHtml(str));
		System.out.println(indexOf(str,"【题文")[1]);*/
		
		String html = "<p>123<span1>123<span2>abc</span2>123</span1><span3>快点快点看</span3><input />123<label></label></p>";
		String html2 = "<p></p>";
		String html3 = "asdfasf";
		//getFirstChildLabel(html);
		
		String beforhtml = "<p>123<span1>123<span2>abc</span2>123";
		String afterhtml = "</span1><span3>快点快点看</span3><input />123<label></label></p>";
		//System.out.println(completionHtml(beforhtml));
		String empty = "<p><span style=\"font-family:宋体;font-size:21;\">A.</span><span style=\"font-family:宋体;color:#000000;\"></span><span style=\"font-family:宋体;color:#000000;\"></span><span >${img:1}</span><span style=\"font-family:宋体;color:#000000;\"></span><span style=\"font-family:宋体;color:#000000;\"></span><span ></span><span style=\"font-family:宋体;color:#000000;\"></span></p>";
		//removeEmptyLabel(empty);
		String html4 = "<p><span style=\"font-family:宋体;font-size:21;color:#FF0000;\">【答案】</span></p><p><span style=\"font-family:宋体;font-size:21;\">1</span><span style=\"font-family:宋体;font-size:21;\">、</span><span style=\"font-family:宋体;font-size:21;\">金就砺则利</span><span style=\"font-family:宋体;font-size:21;\">&nbsp;&nbsp;&nbsp;</span><span style=\"font-family:宋体;font-size:21;\">君子博学而日参省乎己</span><span style=\"font-family:宋体;font-size:21;\">&nbsp;&nbsp;</span><span style=\"font-family:宋体;font-size:21;\">嘻嘻嘻2</span><span style=\"font-family:宋体;font-size:21;\">、行行行</span></p>";
		String html5 = "<p><span style=\"font-family:宋体;font-size:21;color:#FF0000;\">【题文】</span><span style=\"font-family:宋体;font-size:21;color:#FF0000;\">[单选题</span><span style=\"font-family:宋体;font-size:21;color:#FF0000;\">]</span><span >&nbsp;1.</span><span >${img:0}</span><span >&nbsp;&nbsp;__at</span></p>";
		//extractTextFromHtml(html5,"【题文】");
		//System.out.println(extractTextFromHtml(html5,"【题文】"));
		//splitTextAndLabel(empty);
		String html6 = "<p><span style=\"font-family:宋体;font-size:21;color:#FF0000;\"></span><span style=\"font-family:宋体;font-size:21;color:#FF0000;\"></span><span style=\"font-family:宋体;font-size:21;color:#FF0000;\"></span><span >如图所示，真空中有一个半径为</span><span >R</span><span >、质量分布均匀的玻璃球，一细激光束在真空中沿直线</span><span >BC</span><span >传播，并与玻璃球表面的</span><span >C</span><span >点经折射进入玻璃球，在玻璃球表面的</span><span >D</span><span >点又经折射进入真空中，已知</span><span >∠</span><span >COD</span><span >=120°</span><span >，激光束的入射角为</span><span >α=60°</span><span >，则下列说法中正确的是（　　）</span></p>\n" +
				"<p><span ><img class=\"img\" style=\"width:109px;height:50px;\" src=\"data:image/jpeg|png|gif;base64,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\" ></span></p>";
		List<String> reuslt = extractImgDataFromHtml(html6);
		int i = 0;
		for(String imgData : reuslt){
			String fileName = "E:/imges/"+i+".jpg";
			String base64Str = imgData.substring(imgData.indexOf(",")+1);
			ImgConverter.base64StrToImage(base64Str,fileName);
		}
		System.out.println();
	}
	
	public static String getTextFromHtml(String html) {
		StringBuffer sb = new StringBuffer();
		
		while(true) {
			int firstEndTarg = html.indexOf(">");
			html = html.substring(firstEndTarg+1);
			int secondStartTarg = html.indexOf("<");
			if(secondStartTarg == -1) {
				break;
			}
			if(secondStartTarg == 0) {
				continue;
			}
			if(secondStartTarg > 0) {
				String text = html.substring(0,secondStartTarg);
				sb.append(text);
				html = html.substring(secondStartTarg);
			}
		}
		
		String result = sb.toString();
		result = StringEscapeUtils.unescapeHtml4(result);
		
		return result;
	}
	
	
	/**
	 * 将排除字符串excludeStr从html里移除，并保证html结构不变
	 * @param html
	 * @param excludeStr
	 * @return
	 */
	public static String extractTextFromHtml(String html,String excludeStr) {
		if(StringUtils.isBlank(excludeStr)) {
			return html;
		}
		
		List<String> list = splitTextAndLabel(html);
		StringBuffer sb = new StringBuffer();
		
		int startIndexOfList = -1;
		int endIndexOfList = -1;
		
		int startIndexOfText = -1;
		//找到截取字符串过后，末尾部分字符串
		String afterStr = "";
		for(int i=1;i<list.size();i += 2) {
			sb.append(list.get(i));
			startIndexOfText = sb.toString().lastIndexOf(excludeStr);
			if(startIndexOfText > -1) {
				afterStr = sb.toString().substring(startIndexOfText+excludeStr.length());
				endIndexOfList = i;
				break;
			}
		}
		if(startIndexOfText == -1) {
			return html;
		}
		sb = new StringBuffer();
		//找到截取字符串过后，开头部分字符串
		String beforeStr = "";
		for(int i=1;i<list.size();i += 2) {
			int previousLength = sb.length();
			int length = sb.append(list.get(i)).length();
			if(length > startIndexOfText) {
				startIndexOfList = i;
				beforeStr = sb.toString().substring(previousLength,startIndexOfText);
				break;
			}
		}
		//重新拼接html
		StringBuffer result = new StringBuffer();
		for(int i=0;i<list.size();i++) {
			if(startIndexOfList<i && endIndexOfList>i && i%2==1) {
				continue;
			}
			boolean hasAppend = false;
			if(startIndexOfList == i) {
				result.append(beforeStr);
				hasAppend = true;
			}
			if(endIndexOfList == i) {
				result.append(afterStr);
				hasAppend = true;
			}
			if(hasAppend) {
				continue;
			}
			result.append(list.get(i));
			
		}
		
		return result.toString();
	}
	
	
	public static int[] indexOf(String html,String str) {

		int[] result = {-1,-1};
		String temp = html;
		StringBuffer sb = new StringBuffer();
		int startIndex = -1;
		int endIndex = -1;
		int size = 0;
		while(true) {
			
			int firstEndTarg = temp.indexOf(">");
			size += firstEndTarg+1;
			
			temp = temp.substring(firstEndTarg+1);
			int secondStartTarg = temp.indexOf("<");
			if(secondStartTarg == -1) {
				//后面没有标签了跳出循环
				break;
			}
			if(secondStartTarg == 0) {
				continue;
			}
			if(secondStartTarg>0) {
				String text = temp.substring(0,secondStartTarg);
				sb.append(text);
				
				temp = temp.substring(secondStartTarg);
				size += text.length();
			}
			
			if(sb.indexOf(str) > -1) {
				//找到str首字符位子
				startIndex = getFirstCharIndex(str, html, size);
				//找到str尾字符位置
				endIndex = getEndCharIndex(str, html,startIndex);
				result[0] = startIndex;
				result[1] = endIndex;
				break;
			}
		}
		
		return result;
	}

	/**
	 * 找到要排除字符串的尾字符位置
	 * @param str
	 * @param html
	 * @param startIndex
	 * @return
	 */
	private static int getEndCharIndex(String str, String html, int startIndex) {
		
		String endSub = html.substring(startIndex);
		int endIndex = -1;
		char endChar = str.charAt(str.length()-1);
		for(int i=0;i<str.length();i++) {
			char item = str.charAt(i);
			if(endChar == item) {
				endIndex= endSub.indexOf(endChar)+1;
				endSub = endSub.substring(endIndex);
			}
		}
		if(endIndex == -1) {
			return endIndex;
		}
		endIndex = startIndex+endIndex;
		return endIndex;
	}

	/**
	 * 找到要排除的字符串的首字母位置
	 * @param str
	 * @param html
	 * @param size
	 * @return
	 */
	private static int getFirstCharIndex(String str, String html,  int size) {
		String sub = html.substring(0,size);
		int startIndex = -1;
		char firstChar = str.charAt(0);
		for(int i=0;i<str.length();i++) {
			char item = str.charAt(i);
			if(firstChar == item) {
				startIndex= sub.lastIndexOf(firstChar);
				sub = sub.substring(0,startIndex);
			}
		}
		return startIndex;
	}
	
	/**
	 * html拆分成label和text<br>
	 * list的索引为偶数时存label，为奇数时存text<br>
	 * eg. &lt;span style="font-family:宋体;font-size:21;color:#FF0000;"&gt;【答案】&lt;/span&gt;<br>
	 * list = [&lt;span style="font-family:宋体;font-size:21;color:#FF0000;"&gt;, 【答案】, &lt;/span&gt]
	 * @param html
	 * @return
	 */
	public static List<String> splitTextAndLabel(String html) {
		List<String> list = new ArrayList<String>();
		StringBuffer labelSb = new StringBuffer();
		String temp = html;
		int offset = 0;
		while(true) {
			
			int endTargIndex = temp.indexOf(">",offset);
			int nextIndex = endTargIndex+1;
			labelSb.append(temp.substring(offset, nextIndex));
			if(nextIndex == temp.length()) {
				//已经是最后一个字符了
				list.add(labelSb.toString());
				labelSb = new StringBuffer();
				break;
			}
			offset = nextIndex;
			
			if(temp.charAt(nextIndex) == '<') {
				continue;
			}else {
				//提取标签
				list.add(labelSb.toString());
				labelSb = new StringBuffer();
				//提取文字
				int nextStartTargIndex = temp.indexOf("<",offset);
				String text = temp.substring(nextIndex,nextStartTargIndex);
				list.add(text);
				offset = nextStartTargIndex;
			}
		}
		
		return list;
	}
	
	/**
	 * 移除空的span、p标签
	 * @param html
	 * @return
	 */
	public static String removeEmptyLabel(String html) {
		String str = html;
		int offset = 0;
		while(true) {
			int index = str.indexOf("></",offset);
			if(index == -1) {
				break;
			}
			offset += index;
			int startIndex = str.lastIndexOf("<",index);
			int endIndex = str.indexOf(">",index+3);
			char secendChar = str.charAt(startIndex+1);
			if(secendChar == '/') {
				continue;
			}
			str = str.substring(0,startIndex)+str.substring(endIndex+1);
			offset = 0;
		}
		
		return str;
	}
	
	public static final int LABELTYPE_START = 0;
	public static final int LABELTYPE_END = 1;
	public static final int LABELTYPE_COMPLETE = 2;
	
	
	public static String completionHtml(String html) {
		final StringBuffer sb = new StringBuffer(html);
		int level = 0;
		int offset = 0;
		int status = 0;
		LinkedHashMap<Integer,String> map = new LinkedHashMap<Integer, String>();
		while(true) {
			int htmlTartIndex = html.indexOf(">",offset);
			String label = html.substring(offset,htmlTartIndex+1);//<p>
			String name = getNameFromLabel(label);
			int type = label.contains("/>") ? LABELTYPE_COMPLETE: label.contains("</") ? LABELTYPE_END: LABELTYPE_START;
			if(type == LABELTYPE_START ) {
				if(status == 0) {
					status = 1;
				}
				if(status == 1) {
					level++;
				}
				if(level <0) {
					map.remove(level);
				}else if(level>0) {
					map.put(level,name);
				}
				if(status == -1) {
					level++;
				}
			}
			if(type == LABELTYPE_END) {
				if(status == 0) {
					status = -1;
				}
				
				if(status == -1) {
					level--;
				}
				
				if(level <0) {
					map.put(level,name);
				}else if(level>0) {
					map.remove(level);
				}
				if(status == 1) {
					level--;
				}
			}
			
			
			offset = htmlTartIndex+1;
			int nextTargIndex = html.indexOf("<",offset);
			if(nextTargIndex == -1) {
				break;
			}
			//去除文字
			offset = nextTargIndex;
			if(offset == html.length()) {
				break;
			}
		}
		
		if(status > 0) {
			ListIterator<Map.Entry<Integer,String>> i=new ArrayList<Map.Entry<Integer,String>>(map.entrySet()).listIterator(map.size());
			while(i.hasPrevious()) {  
				Map.Entry<Integer, String> entry=i.previous();  
				if(entry.getKey()>0) {
					sb.append("</"+entry.getValue()+">");
				}
				if(entry.getKey()<0) {
					sb.insert(0, "<"+entry.getValue()+">");
				}
			}
		}
		if(status < 0) {
			map.forEach(new BiConsumer<Integer,String>() {
				public void accept(Integer t, String u) {
					if(t>0) {
						sb.insert(0, "</"+u+">");
					}
					if(t<0) {
						sb.insert(0, "<"+u+">");
					}
				}
			});
		}

		
		return sb.toString();
	}
	
	
	/**
	 * 从html（必须有根节点）中提取第一个子节点的内容
	 * @param html
	 * @return
	 */
	private static String getFirstChildLabel(String html) {
		String templ = html;
		int level = 0;
		
		int startIndex = -1;
		int endIndex = -1;
		int size = 0;
		boolean hasSub = false;
		
		while(true) {
			
			int htmlTartIndex = templ.indexOf(">");
			String label = templ.substring(0,htmlTartIndex+1);//<p>
			
			int type = label.contains("/>") ? LABELTYPE_COMPLETE: label.contains("</") ? LABELTYPE_END: LABELTYPE_START;
			if(type == LABELTYPE_START || type == LABELTYPE_COMPLETE) {
				level++;
			}
			
			if(level == 2 && hasSub == false) {
				hasSub = true;
				startIndex = size;
			}
			
			if(type == LABELTYPE_END || type == LABELTYPE_COMPLETE) {
				level--;
			}
			//去除标签
			templ = templ.substring(htmlTartIndex+1);
			if(StringUtils.isBlank(templ)) {
				return "";
			}
			size += htmlTartIndex+1;
			int nextTargIndex = templ.indexOf("<");
			if(nextTargIndex == -1) {
				return "";
			}
			//去除文字
			templ = templ.substring(nextTargIndex);
			size += nextTargIndex;
			
			if(level == 1 && hasSub) {
				endIndex = size - nextTargIndex;
				break;
			}
		}
		
		String sub = html.substring(startIndex,endIndex);
		return sub;
	}
	
	private static String getNameFromLabel(String label) {
		String str = label.replace("</", "").replace("/>", "").replace("<", "").replace(">", "");
		String name = str.trim().split(" ")[0];
		return name;
	}

	public static List<String> extractImgDataFromHtml(String html){
		List<String> imgDatas = new ArrayList<>();
		Document document = Jsoup.parse(html);
		Elements elements = document.getElementsByTag("img");
		for(Element element :elements){
			String src = element.attr("src");
			imgDatas.add(src);
		}
		return imgDatas;
	}


}
