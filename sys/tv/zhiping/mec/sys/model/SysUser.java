package tv.zhiping.mec.sys.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import tv.zhiping.jfinal.BasicModel;
import tv.zhiping.utils.DigestUtil;

/**
 * System user model.
 * @author robin
 *
 */

public class SysUser extends BasicModel<SysUser>
{
	public static final SysUser dao = new SysUser();
	
	public SysUser getUser(String username)
	{
		StringBuilder sql = new StringBuilder("select * from sys_user where status=1 and username=?");
		return findFirst(sql.toString(), new Object[]{username});
	}
	
	public SysUser getUserById(String user_id)
	{
		String sql = "select * from sys_user where status=1 and id=?";
		return findFirst(sql, new Object[]{user_id});
	}
	

	public boolean checkPassword(String password)
	{
		if (DigestUtil.MD5(password).equals(getStr("password")))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String queryRealNameByUid(Long id) {
		if(id!=null){
			SysUser obj = findById(id);//可以使用缓存
			if(obj!=null){
				return obj.getRealname();
			}			
		}
		return null;
	}	
	
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
	
	public String getUsername()
	{
		return getStr("username");
	}
	
	public String getPassword()
	{
		return getStr("password");
	}
	
	public void setPassword(String password)
	{
		set("password", password);
	}
	
	
	public void setStatus(String status)
	{
		set("status", status);
	}
	
	
	public String getRealname()
	{
		return getStr("realname");
	}
	
	public void setRealname(String realname)
	{
		set("realname", realname);
	}

}