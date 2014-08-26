package tv.zhiping.mec.sys.ctrl;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.HttpUtil;
import tv.zhiping.jfinal.BaseValidator;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.mec.sys.model.EnumValue;
import tv.zhiping.mec.sys.model.SysConfig;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;

/**
 * enum配置相关
 * @author 张有良
 */

public class EnumValueCtrl extends SysBaseCtrl
{	
	/**
	 * App系统配置 index
	 * */
	public void index(){
		renderJsp("/page/base/enumValue/index.jsp");
	}
	
	/**
	 * 查询
	 * */
	public void list() {
		EnumValue obj = getModel(EnumValue.class,"obj");
		int pageNumber = getParaToInt("pageNumber",1);
		int pageSize = getParaToInt("pageSize",Cons.DEF_PAGE_SIZE);	
		setAttr("page", EnumValue.dao.paginate(obj,pageNumber,pageSize));
		renderJson();
	}
	
	/**
	 * 网页新增数据 跳转
	 * */
	public void input() {
		Long id = getParaToLong(0);
		if(id!=null){
			setAttr("obj",SysConfig.dao.findById(id));			
		}
		render("/page/base/enumValue/input.jsp");
	}
	
	/**
	 * 网页删除数据
	 * */
	public void del() {
		Long id = getParaToLong(0);
		if(id!=null){
			SysConfig model = new SysConfig();
			model.setId(id);
			model.setDelDef();
			model.update();
		}
		EnumValue.dao.initCache();
		renderJson(Cons.JSON_SUC);
	}
	/**
	 * 网页新增数据保存
	 * */
	public void save() {
		EnumValue obj = getModel(EnumValue.class,"obj");
		if(obj!=null){
			if(obj.getId()!=null){
				obj.setUpdDef();
				obj.update();
			}else{
				obj.setAddDef();
				obj.save();				
			}
			EnumValue.dao.initCache();
		}
		renderJson(Cons.JSON_SUC);
	}
	
//	public static class AppConfigSaveValidator extends BaseValidator {
//		
//		protected void validate(Controller controller) {
//			validateRequiredString("obj.title", _MSG_START+"title", "请输入参数名!");
//			validateRequiredString("obj.value", _MSG_START+"value", "请输入参数值");
//			validateRequiredString("obj.description", _MSG_START+"description", "请输入参数描述");
//		}
//		
//		protected void handleError(Controller ctrl) {
//			ctrl.keepModel(SysConfig.class,"obj");
//			
//			if(HttpUtil.isAjaxReq(ctrl.getRequest())){
//				super.handleAjaxError(ctrl);
//			}else{
//				ctrl.render("/page/appconfig/input.jsp");
//			}
//			
//		}
//	}
}
