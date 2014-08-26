package tv.zhiping.mec.sys.ctrl;

import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.mec.sys.power.PowerHelper;
import tv.zhiping.mec.sys.model.SysConfig;

/**
 * 说明:后台首页
 * @author 张有良
 * @version 1.0
 */
public class IndexCtrl extends SysBaseCtrl{
	
	public void index(){
		String username = PowerHelper.getValue(getRequest(), "username");
		SysConfig config = SysConfig.dao.getConfigByKey("sys_name");
		setAttr("username", username);
		setAttr("sys_name", config.getValue());
		renderJsp("/page/main/index.jsp");
	}
}
