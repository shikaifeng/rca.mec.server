package tv.zhiping.mec.api.tmallbox.ctrl;

import tv.zhiping.jfinal.BaseCtrl;

public class TmallBoxCtrl extends BaseCtrl {
	/**
	 * 方法：index 
	 * 地址：/m/tmallbox
	 * 功能：天猫魔盒主页面
	 */
	public void index()
	{
		int random = (int)(Math.random()*1000);
		setAttr("random", random);
		render("/page/m/tmallbox/index.jsp");
	}
}
