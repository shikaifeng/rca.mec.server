package tv.zhiping.mec.sys.ctrl;

import tv.zhiping.common.Cons;
import tv.zhiping.common.cache.CacheKey;
import tv.zhiping.common.util.HttpUtil;
import tv.zhiping.jfinal.BaseValidator;
import tv.zhiping.mec.app.model.AppConfig;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.redis.Redis;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.plugin.redis.JedisKit;

/**
 * App系统配置相关
 * @author 樊亚容
 */

public class AppConfigCtrl extends SysBaseCtrl
{	
	
	/**
	 * App系统配置 index
	 * */
	public void index()
	{
		renderJsp("/page/sys/appconfig/index.jsp");
	}	
	
	
	/**
	 * 查询
	 * */
	public void list() {
		AppConfig obj = getModel(AppConfig.class,"obj");
		int pageNumber = getParaToInt("pageNumber",1);
		int pageSize = getParaToInt("pageSize",Cons.DEF_PAGE_SIZE);	
		setAttr("page", AppConfig.dao.paginate(obj,pageNumber,pageSize));
		renderJson();
	}
	
	/**
	 * 网页新增数据 跳转
	 * */
	public void input() {
		Long id = getParaToLong(0);
		if(id!=null){
			setAttr("obj",AppConfig.dao.findById(id));			
		}
		render("/page/sys/appconfig/input.jsp");
	}
	
	/**
	 * 网页删除数据
	 * */
	public void del() {
		Long id = getParaToLong(0);
		if(id!=null){
			AppConfig model = new AppConfig();
			model.setId(id);
			model.setDelDef();
			model.update();
			
			Redis.del(CacheKey.APP_SYSTEM_CONFIG);
		}
		renderJson(Cons.JSON_SUC);
	}
	/**
	 * 网页新增数据保存
	 * */
	@Before(AppConfigSaveValidator.class)
	public void save() {
		AppConfig obj = getModel(AppConfig.class,"obj");
		if(obj!=null){
			if(obj.getId()!=null){
				obj.setUpdDef();
				obj.update();
			}else{
				obj.setAddDef();
				obj.save();				
			}
			Redis.del(CacheKey.APP_SYSTEM_CONFIG);
		}
		renderJson(Cons.JSON_SUC);
	}
	
	public static class AppConfigSaveValidator extends BaseValidator {
		
		protected void validate(Controller controller) {
			validateRequiredString("obj.title", _MSG_START+"title", "请输入参数名!");
			validateRequiredString("obj.value", _MSG_START+"value", "请输入参数值");
			validateRequiredString("obj.description", _MSG_START+"description", "请输入参数描述");
		}
		
		protected void handleError(Controller ctrl) {
			ctrl.keepModel(AppConfig.class,"obj");
			
			if(HttpUtil.isAjaxReq(ctrl.getRequest())){
				super.handleAjaxError(ctrl);
			}else{
				ctrl.render("/page/appconfig/input.jsp");
			}
			
		}
	}
}
