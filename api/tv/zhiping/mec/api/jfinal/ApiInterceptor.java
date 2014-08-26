package tv.zhiping.mec.api.jfinal;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import tv.zhiping.common.util.HttpUtil;
import tv.zhiping.mec.api.common.ApiCons;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * 说明: Global 级
 * @author 张有良
 */
public class ApiInterceptor  implements Interceptor{
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public void intercept(ActionInvocation ai) {
		HttpServletRequest request = ai.getController().getRequest();
		
		String path = ai.getViewPath();
		
		if(path.indexOf("/api/")>-1){
			logAppReq(request);//打印日志便于调试
		}
		
		try {
			ai.invoke();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			
			String res = "{\"status\":"+ApiCons.STATUS_ERROR+"}";
			
			JSONObject json = new JSONObject();
			json.put("status", ApiCons.STATUS_ERROR);
			json.put("msg",e.getMessage());
			
			res = json.toJSONString();
			
			Controller ct =  ai.getController();
			if(ct instanceof ApiBaseCtrl){
				((ApiBaseCtrl)ct).renderJsonV1(res);
			}else{
				ai.getController().renderJson(res);
			}
		}
	}
	
	
	/**
	 * logapp端的请求日志，利于调试
	 * @param request
	 */
	private void logAppReq(HttpServletRequest request){
		 if(!log.isInfoEnabled()){
			 return;
		 }
		 
		String url = request.getRequestURI();
		Map paramMap = request.getParameterMap();
		
		StringBuffer buf = new StringBuffer();
        if (paramMap!=null && !paramMap.isEmpty()) {
        	Set paramSet = new TreeMap(paramMap).entrySet();

            boolean first = true;

            for (Iterator it = paramSet.iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                String[] values = (String[]) entry.getValue();

                for (int i = 0; i < values.length; i++) {
                    String key = (String) entry.getKey();
                  
                    if (first) {
                        first = false;
                    } else {
                        buf.append('&');
                    }

                    buf.append(key).append('=').append(values[i]);
                    
                }
            }
        }

    	String str = "url:"+url+" method:"+request.getMethod()+" ip:"+HttpUtil.getClientIp(request)+" reqParam:"+buf;
    	log.info(str);        	
	}
}
