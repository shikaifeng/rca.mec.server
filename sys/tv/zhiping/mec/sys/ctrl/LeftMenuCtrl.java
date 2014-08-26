package tv.zhiping.mec.sys.ctrl;

import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;

/**
 * 说明: 左边菜单
 * @author 张有良
 * @version 1.0
 * @since 2013-1-14
 */
public class LeftMenuCtrl extends SysBaseCtrl{
	public void index(){
		renderJsp("/page/base/leftMenu/index.jsp");
	}
	
}
