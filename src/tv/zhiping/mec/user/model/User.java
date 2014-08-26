package tv.zhiping.mec.user.model;

import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

/**
 * Device 设备(手机信息)
 * @author 樊亚容
 * 2014-5-14
 * */
@SuppressWarnings("serial")
public class User extends BasicModel<User>{
	
	public static final User dao = new User();
	/**
	 * 手机用户phone2_udid注册
	 */
	public void doPhone2Register(User obj){
		User oldDevice = queryByUdid(obj.getUdid());
		if(oldDevice!=null){/*该device已经存在，更新操作*/
			obj.setId(oldDevice.getId());			
			obj.setUpdDef();//设置更新时间
			obj.update();//更新到数据库			
		}else{/*该device不存在，保存*/
			obj.setAddDef();			
			obj.save();			
		}
	}	
	/**
	 * 根据手机udid得到设备信息
	 */
	public User queryByUdid(String udid) {
		List<Object> params = new ArrayList<Object>();
		params.add(Cons.STATUS_VALID);
		params.add(udid);
		String sql = "select * from "+tableName+" where status= ? and udid =?";
		return findFirst(sql, params.toArray());
	}
	
	
	
	/*UDID*/
	public java.lang.String getUdid(){
		return this.getStr("udid");
	}
	public User setUdid(java.lang.String udid){
		super.set("udid",udid);
		return this;
	}
	/*OS*/
	public java.lang.String getOS(){
		return this.getStr("os");
	}	
	public User setOS(java.lang.String os){ 
		super.set("os",os);
		return this;
	}
	/*Version*/
	public java.lang.String getVersion(){
		return this.getStr("version");
	}	
	public User setVersion(java.lang.String version){
		super.set("version",version);
		return this;
	}
	/*Brand*/
	public java.lang.String getBrand(){
		return this.getStr("brand");
	}
	public User setBrand(java.lang.String brand){
		super.set("brand",brand);
		return this;
	}
}
