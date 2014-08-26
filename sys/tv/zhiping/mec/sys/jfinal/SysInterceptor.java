package tv.zhiping.mec.sys.jfinal;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.common.util.HttpUtil;
import tv.zhiping.mec.api.common.ApiCons;
import tv.zhiping.mec.sys.power.PowerHelper;
import tv.zhiping.sys.bean.MsgException;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

/**
 * 说明: Global 级
 * @author 张有良
 */
public class SysInterceptor  implements Interceptor{
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public void intercept(ActionInvocation ai) {
		HttpServletRequest request = ai.getController().getRequest();
		
		String path = ai.getViewPath();
		
		boolean isInvoke = true;
		
		if(path.indexOf("/sys/")>-1 && !PowerHelper.isLogin(request) && path.indexOf("login")<0){
			isInvoke = false;
			if (HttpUtil.isAjaxReq(request)) {//如果是异步请求
				ai.getController().renderJson("{\"status\":"+ApiCons.SESSION_TIME_OUT+",\"msg\":\"must login\"}");				
			}else{
				ai.getController().redirect(CacheFun.getConVal(ConfigKeys.WEB_HTTP_PATH)+"/page/login.jsp");
			}
		}

		
		if(isInvoke){
			try {
				ai.invoke();
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				if (HttpUtil.isAjaxReq(request)) {//如果是异步请求
					ai.getController().renderJson("{\"status\":"+ApiCons.STATUS_ERROR+"}");
				}else{
					request.setAttribute("status",false);
					request.setAttribute("message",e.toString());
					request.setAttribute("exception",e);
					log.error(e.getMessage(),e);
					if(e instanceof MsgException){
						ai.getController().redirect(CacheFun.getConVal(ConfigKeys.WEB_HTTP_PATH)+"/page/main/msgShow.jsp");
						request.setAttribute("msgBean", ((MsgException)e).getMsgBean());
					}else{
						ai.getController().redirect(CacheFun.getConVal(ConfigKeys.WEB_HTTP_PATH)+"/page/main/exception.jsp");
					}
				}
			}
		}
	}
}
