package tv.zhiping.jfinal;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class BasicModel<M extends BasicModel> extends NoIdModel<M> {
	public Long getId(){
		return super.getLong("id");
	}

	public M setId(Long id){
		super.set("id",id);
		return (M) this;
	}
}
