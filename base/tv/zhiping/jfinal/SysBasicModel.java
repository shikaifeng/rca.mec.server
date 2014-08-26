package tv.zhiping.jfinal;



/**
 * 业务系统的基累数据库模型
 * @author 张有良
 */
public abstract class SysBasicModel<M extends SysBasicModel> extends BasicModel<M> {
	private static final long serialVersionUID = 1L;

	public Long getCreated_user(){
		return super.getLong("created_user");
	}
	
	public M setCreated_user(Long created_user){
		super.set("created_user",created_user);
		return (M) this;
	}
	
	public Long getUpdated_user(){
		return super.getLong("updated_user");
	}
	
	public M setUpdated_user(Long updated_user){
		super.set("updated_user",updated_user);
		return (M) this;
	}
}
