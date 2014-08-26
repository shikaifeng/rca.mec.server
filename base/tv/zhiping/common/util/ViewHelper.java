package tv.zhiping.common.util;

public class ViewHelper {
	
	/**
	 * 文本域内容用HTML方式展示
	 * @param textareaText
	 * @return
	 */
	public static String textarea2Html(String textareaText){
		if(textareaText == null){
			return null;
		}
		return textareaText.replaceAll("[\\t\\n\\r]", "</br>");
	}
}
