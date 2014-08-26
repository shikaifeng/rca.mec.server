package tv.zhiping.mec.api.jfinal;


import com.jfinal.core.Controller;



/**
 * 通过id获取对象的业务校验
 */
public class CommIdApiValidator extends ApiValidator {
	
	public void validate(Controller controller) {
		super.validate(controller);
		validateRequiredString("id", _MSG_START+"id", "id不能为空");
	}
}
