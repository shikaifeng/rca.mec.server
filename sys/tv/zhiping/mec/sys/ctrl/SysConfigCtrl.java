package tv.zhiping.mec.sys.ctrl;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.Cons;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.common.util.HttpUtil;
import tv.zhiping.jfinal.BaseValidator;
import tv.zhiping.mec.feed.model.SynWeiboTask;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.mec.sys.model.SysConfig;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * sys系统配置相关
 * @author 张有良
 */

public class SysConfigCtrl extends SysBaseCtrl
{	
	
	/**
	 * App系统配置 index
	 * */
	public void index()
	{
		renderJsp("/page/sys/sysconfig/index.jsp");
	}	
	
	
	/**
	 * 查询
	 * */
	public void list() {
		SysConfig obj = getModel(SysConfig.class,"obj");
		int pageNumber = getParaToInt("pageNumber",1);
		int pageSize = getParaToInt("pageSize",Cons.DEF_PAGE_SIZE);	
		setAttr("page", SysConfig.dao.paginate(obj,pageNumber,pageSize));
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
		render("/page/sys/sysconfig/input.jsp");
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
		SysConfig.dao.initSysConfigDb();
		renderJson(Cons.JSON_SUC);
	}
	/**
	 * 网页新增数据保存
	 * */
	@Before(AppConfigSaveValidator.class)
	public void save() {
		SysConfig obj = getModel(SysConfig.class,"obj");
		if(obj!=null){
			if(obj.getId()!=null){
				obj.setUpdDef();
				obj.update();
			}else{
				obj.setAddDef();
				obj.save();				
			}
			
			if(ConfigKeys.WEIBO_FEED_MAX_SIZE.equals(obj.getTitle())){
				if(!obj.getValue().equals(CacheFun.getConVal(ConfigKeys.WEIBO_FEED_MAX_SIZE))){
					SynWeiboTask.dao.resetSyn();
				}
			}
			SysConfig.dao.initSysConfigDb();
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
			ctrl.keepModel(SysConfig.class,"obj");
			
			if(HttpUtil.isAjaxReq(ctrl.getRequest())){
				super.handleAjaxError(ctrl);
			}else{
				ctrl.render("/page/appconfig/input.jsp");
			}
			
		}
	}
}
