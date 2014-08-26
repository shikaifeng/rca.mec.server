package tv.zhiping.sys.bean;

public class MsgBean {
	public final static String URL_TYPE_JS = "1";// 直接是 history.go(-1); 返回
	public final static String URL_TYPE_LOCATION = "2";//按路径返回
	
	public final static String AUTO_EXCUTE_YES = "1";//等待一段时间自动执行js
	public final static String AUTO_EXCUTE_NO = "2";//等待一段时间不执行js
	
	private String autoExcute = AUTO_EXCUTE_YES;//是否执行自动跳转
	private String urlType = URL_TYPE_JS;//页面返回的类型
	private String msg; 
	private String onclickTodo="history.go(-1);";//一般提示页面，都是有个返回按钮
	
	public MsgBean(String msg){
		this.msg = msg;
	}

	public String getAutoExcute() {
		return autoExcute;
	}

	public void setAutoExcute(String autoExcute) {
		this.autoExcute = autoExcute;
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getOnclickTodo() {
		return onclickTodo;
	}
	public void setOnclickTodo(String onclickTodo) {
		this.onclickTodo = onclickTodo;
	}
	
}
