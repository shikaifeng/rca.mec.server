package tv.zhiping.mec.sys.demo.blog;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import tv.zhiping.common.util.HttpUtil;
import tv.zhiping.jfinal.BaseValidator;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.Controller;

/**
 * BlogValidator.
 */
public class BlogSaveValidator extends BaseValidator {
	
	protected void validate(Controller controller) {
		validateRequiredString("obj.title", _MSG_START+"title", "请输入Blog标题!");
		validateRequiredString("obj.content", _MSG_START+"content", "请输入Blog简介!");
	}
	
	protected void handleError(Controller ctrl) {
		ctrl.keepModel(Blog.class,"obj");
		
		if(HttpUtil.isAjaxReq(ctrl.getRequest())){
			super.handleAjaxError(ctrl);
		}else{
			ctrl.render("/page/blog/input.jsp");
		}
		
//		HttpServletRequest request = ctrl.getRequest();
		
//		if (!(request.getHeader("accept").indexOf("application/json") > -1)) {	//如果是同步请求
//			request.setAttribute("status",false);
//		}else{	//如果是异步请求
//			ai.getctrl().renderJson("{\"status\":0}");
//		}
		
		
//		String actionKey = getActionKey();
//		if (actionKey.equals("/blog/save"))
//			
//		else if (actionKey.equals("/blog/update"))
//			ctrl.render("edit.html");
	}
}
