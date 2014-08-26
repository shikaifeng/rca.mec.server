package tv.zhiping.mec.api.person.ctrl;

import tv.zhiping.jfinal.BaseCtrl;

public class AboutMCtrl extends BaseCtrl{
	/**
	 * 测试用例：http://localhost:8080/m/about
	 * */
	public void index(){
		render("/page/m/about/index.jsp");
	}
}
