package tv.zhiping.jfinal;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public abstract class BaseValidator extends Validator{

	protected String _MSG_START = "_msgFor_";

	protected void handleAjaxError(Controller ctrl) {
		HttpServletRequest reuqest = ctrl.getRequest();
		Enumeration<String> attrs = reuqest.getAttributeNames();
		
		List msgList = new ArrayList();
		List msgFields = new ArrayList();
		String key = null;
		while(attrs.hasMoreElements()){
			key = attrs.nextElement();
			if(key.startsWith(_MSG_START)){
				msgList.add(reuqest.getAttribute(key));				
				msgFields.add(key.replace(_MSG_START,""));
			}
		}
		
		if(msgList!=null && !msgList.isEmpty()){
			Map res = new HashMap();
			res.put("status",-1);
			res.put("msg",msgList);
			res.put("msgFields",msgFields);
			
			ctrl.renderJson(JSON.toJSONString(res));
		}else{
			ctrl.renderJson();
		}
		
	}

}
