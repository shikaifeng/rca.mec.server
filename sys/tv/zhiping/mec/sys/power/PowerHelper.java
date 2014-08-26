package tv.zhiping.mec.sys.power;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * 登录权限
 * @author 张有良
 */
public class PowerHelper {
	private static Logger log = Logger.getLogger(PowerHelper.class);
	
	//获取用户id
	public static boolean isLogin(HttpServletRequest request){
		String str = getValue(request,_SESSION);
		if(StringUtils.isNotBlank(str)){
			return true;
		}
		return false;
	}
	
	/**
	 * getsession的方法
	 * @param key
	 * @return
	 */
	public static String setValue(HttpServletResponse response,String key,Object value){
		if(response!=null && value!=null){
			Cookie cookie = new Cookie(key,value.toString());
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		return null;
	}
	
	
	
	/**
	 * getsession的方法
	 * @param key
	 * @return
	 */
	public static String getValue(HttpServletRequest request,String key){
		if(request!=null){
			Cookie[] cookies=request.getCookies();
			if(cookies!=null){
				for(int i=0;i<cookies.length;i++){
					if(cookies[i].getName().equals(key)){
						return cookies[i].getValue();
					}
				}
			}
		}
		return null;
	}
	
	public final static String _SESSION  = "_zp_session";
	public final static String COOKIE_SEPARATOR  = ",";
	
	
	/**
	 * 删除
	 * @param key
	 * @param value
	 * @return
	 */
	public static void remove(HttpServletResponse response,String key){
		if(response!=null){
			Cookie cookie = new Cookie(key,null);
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);			
		}
	}
}
