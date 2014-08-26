package tv.zhiping.mec.api.jfinal;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.jfinal.BaseValidator;
import tv.zhiping.mec.api.common.ApiCons;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.Controller;

import freemarker.core._RegexBuiltins.matchesBI;

/**
 * BlogValidator.
 */
public class ApiValidator extends BaseValidator {
	
	protected void validate(Controller controller) {
//		validateRequiredString("_uid", _MSG_START+"uid", "uid不能为空");
		validateLong("_ucid", _MSG_START+"ucid", "ucid不能为空,且为数字");
	}
	
//	//用户id校验
//	protected void addValidateUid(Controller controller) {
//		validateRequiredString("_uid", _MSG_START+"uid", "uid不能为空");
//	}
	
	
	/**
	 * 增加分页信息校验
	 * @param controller
	 */
	protected void addPageNumberValidateUid(Controller controller) {
		if(StringUtils.isNoneBlank(controller.getPara("page_number"))){
			validateInteger("page_number", 1, Integer.MAX_VALUE, _MSG_START+"page_number", "分页页码只能正整数，且大于等于1");
		}
		
		if(StringUtils.isNoneBlank(controller.getPara("page_number"))){
			validateInteger("page_number",1, Integer.MAX_VALUE, _MSG_START+"page_size", "分页条数只能正整数，且大于等于1");
		}
	}
	
	public static void main(String[] args) {
		String str = "  ";
		System.out.println(StringUtils.isNoneBlank(str));
		System.out.println(StringUtils.isNotBlank(str));
	}
		
	
	
	protected void handleError(Controller ctrl) {
		handleApidError(ctrl);
	}
	
	protected void handleApidError(Controller ctrl) {
		HttpServletRequest reuqest = ctrl.getRequest();
		Enumeration<String> attrs = reuqest.getAttributeNames();
		
		StringBuilder msg = new StringBuilder();
		String key = null;
		while(attrs.hasMoreElements()){
			key = attrs.nextElement();
			if(key.startsWith(_MSG_START)){
				msg.append(reuqest.getAttribute(key)).append(",");
			}
		}
		
		if(msg.length()>0){
			Map res = new HashMap();
			res.put("status",ApiCons.STATUS_ERROR);
			res.put("msg",msg);
			ctrl.renderJson(JSON.toJSONString(res));
		}else{
			ctrl.renderJson();
		}
		
	}
}
