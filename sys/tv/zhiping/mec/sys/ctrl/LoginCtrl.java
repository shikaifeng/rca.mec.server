package tv.zhiping.mec.sys.ctrl;


import org.apache.commons.lang3.StringUtils;

import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.mec.sys.model.SysUser;
import tv.zhiping.mec.sys.power.PowerHelper;
import tv.zhiping.sys.bean.MsgException;
import tv.zhiping.utils.DigestUtil;

/**
 * 登录
 * @author 张有良
 */
public class LoginCtrl extends SysBaseCtrl{
	public static final String _name="hessian";
	
	public void index() {
		renderJsp("/page/login.jsp");
	}
	
	public void logIn() throws MsgException{
		String name = getPara("name");
		String pwd = getPara("pwd");
		String res = "0";
		if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(pwd)){
			SysUser user = SysUser.dao.getUser(name);
			if (user!=null && pwd!=null && user.checkPassword(pwd))
			{
				PowerHelper.setValue(getResponse(), PowerHelper._SESSION, user.getId());
				PowerHelper.setValue(getResponse(), "username", user.getUsername());
				res = "1";
			}
			else 
			{
				res = "0";
			}
		}
		renderText(res);
	}
	
	public void logOut() {
		PowerHelper.remove(getResponse(),PowerHelper._SESSION);
		
		renderJsp("/page/login.jsp");
	}
}
