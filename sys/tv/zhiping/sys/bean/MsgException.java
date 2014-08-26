package tv.zhiping.sys.bean;

/*******************************************************************************************************************************************
 * struts2对这个异常进行拦截 他是错误信息提示
 * 
 * @author Administrator
 ******************************************************************************************************************************************/
public class MsgException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private MsgBean msgBean;// 提示信息，

	public MsgException() {

	}

	public MsgException(MsgBean msgBean) {
		this.msgBean = msgBean;
	}

	public MsgException(String errorMessage) {
		msgBean = new MsgBean(errorMessage);
	}

	/**
	 * @param errorMessage 错误提示信息
	 * @param url  返回的url地址
	 */
	public MsgException(String errorMessage,String url) {
		this(errorMessage);
		
		msgBean = new MsgBean(errorMessage);
		if(url!=null){
			msgBean.setOnclickTodo(url);
			msgBean.setUrlType(MsgBean.URL_TYPE_LOCATION);
		}
	}
	
	/**
	 * @param errorMessage 错误提示信息
	 * @param url  返回的url地址
	 * @param skipType url路径
	 * @param autoExcute 是否自动执行
	 */
	public MsgException(String errorMessage,String url,String urlType,String autoExcute) {
		this(errorMessage,url);

		if(urlType!=null){
			msgBean.setUrlType(urlType);
		}
		
		if(autoExcute!=null){
			msgBean.setAutoExcute(autoExcute);
		}
	}

	public MsgBean getMsgBean() {
		return msgBean;
	}

	public void setMsgBean(MsgBean msgBean) {
		this.msgBean = msgBean;
	}
}
