package tv.zhiping.mec.sys.ctrl;

import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.mec.sys.model.SysUser;
import tv.zhiping.mec.sys.power.PowerHelper;
import tv.zhiping.sys.bean.MsgException;
import tv.zhiping.utils.DigestUtil;

/**
 * System user management 
 * @author robin
 * @version 1.0
 */

public class UserCtrl extends SysBaseCtrl
{
	public void index()
	{
		//
	}
	
	public void reset()  throws MsgException
	{
		String user_id = getPara("user_id");
		if (user_id != null)
		{
			String old_pwd = getPara("old_pwd");
			String new_pwd = getPara("new_pwd");
			String cfm_pwd = getPara("cfm_pwd");
			String code = "";
			SysUser user = SysUser.dao.getUserById(user_id);
			if (user!=null && old_pwd!=null && user.checkPassword(old_pwd))
			{
				if (new_pwd.equals(cfm_pwd))
				{
					user.setPassword(DigestUtil.MD5(new_pwd));
					user.update();
					code = "200";
				}
				else 
				{
					code = "201";
				}
			}
			else 
			{
				code = "202";
			}
			setAttr("code", code);
		}
		else 
		{
			user_id = PowerHelper.getValue(getRequest(), PowerHelper._SESSION);
		}
		String username = PowerHelper.getValue(getRequest(), "username");
		setAttr("username", username);
		setAttr("user_id", user_id);
		renderJsp("/page/sys/reset.jsp");
	}
}
