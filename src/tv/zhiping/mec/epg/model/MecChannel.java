package tv.zhiping.mec.epg.model;

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
public class MecChannel extends BasicModel<MecChannel> {

	public static final MecChannel dao = new MecChannel();
	
	/**
	 * 查询所有channel id list
	 * @return 
	 */
	public List<MecChannel> queryChannelList(){	
		String sql = "select * from "+tableName +" where status=?";
		List<Object> params = new ArrayList<Object>();		
		params.add(Cons.STATUS_VALID);		
		return find(sql,params.toArray());
		
	}

	/**
	 * 根据rid得到对象
	 */
	public MecChannel queryByRid(String rid){
		List<Object> params = new ArrayList<Object>();		
		String sql = "select * from "+tableName+" where rid=?";
		params.add(rid);		
		return findFirst(sql, params.toArray());
	}
	
	/**
	 * 根据拼音得到对象
	 * @param py
	 * @return
	 */
	public MecChannel queryByPy(String py){
		List<Object> params = new ArrayList<Object>();
		
		String sql = "select * from "+tableName+" where pinyin=?";
		params.add(py);
		
		return findFirst(sql, params.toArray());
	}
	
	public java.lang.String getRid(){
		return this.getStr("rid");
	}
	
	public MecChannel setRid(java.lang.String rid){
		super.set("rid",rid);
		return this;
	}
	public java.lang.String getName(){
		return this.getStr("name");
	}
	
	public MecChannel setName(java.lang.String name){
		super.set("name",name);
		return this;
	}
	public java.lang.String getPinyin(){
		return this.getStr("pinyin");
	}
	
	public MecChannel setPinyin(java.lang.String pinyin){
		super.set("pinyin",pinyin);
		return this;
	}
	public java.lang.String getLogo(){
		return this.getStr("logo");
	}
	
	public MecChannel setLogo(java.lang.String logo){
		super.set("logo",logo);
		return this;
	}
	public java.lang.String getDescription(){
		return this.getStr("description");
	}
	
	public MecChannel setDescription(java.lang.String description){
		super.set("description",description);
		return this;
	}
	public java.lang.String getStream(){
		return this.getStr("stream");
	}
	
	public MecChannel setStream(java.lang.String stream){
		super.set("stream",stream);
		return this;
	}


}