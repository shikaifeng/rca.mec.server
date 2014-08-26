package tv.zhiping.mec.feed.model;

import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-05-20
 */
@SuppressWarnings("serial")
public class BaikeProperty extends BasicModel<BaikeProperty> {

	public static final BaikeProperty dao = new BaikeProperty();

	
	public List<BaikeProperty> qurreyByPid(java.lang.Long pid){
		List<Object> params = new ArrayList<Object>();
		
		String sql = "select * from "+tableName+" where pid=? and status=?";
		params.add(pid);
		params.add(Cons.STATUS_VALID);
		
		return find(sql, params.toArray());
	}
	
	public java.lang.Long getPid(){
		return this.getLong("pid");
	}
	
	public BaikeProperty setPid(java.lang.Long pid){
		super.set("pid",pid);
		return this;
	}
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public BaikeProperty setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getValue(){
		return this.getStr("value");
	}
	
	public BaikeProperty setValue(java.lang.String value){
		super.set("value",value);
		return this;
	}
}