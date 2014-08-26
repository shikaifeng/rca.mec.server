package tv.zhiping.jfinal;

import com.jfinal.core.Controller;

/**
 * CommonController
 */
public class CommonCtrl extends Controller {
	
	public void index() {
		render("/page/login.jsp");
	}
	
	
	public void down() {
		renderFile("/feed/tmallbox/tmallbox_feed.xls");
	}
	
}
