package tv.zhiping.mec.epg.model;

import java.util.ArrayList;
import java.util.List;

import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-11
 */
@SuppressWarnings("serial")
public class MecEpgProgramType extends BasicModel<MecEpgProgramType> {

	public static final MecEpgProgramType dao = new MecEpgProgramType();

	
	public java.lang.String getRid(){
		return this.getStr("rid");
	}
	
	/**
	 * 根据rid得到对象
	 */
	public MecEpgProgramType queryByRid(String rid){
		List<Object> params = new ArrayList<Object>();		
		String sql = "select * from "+tableName+" where rid=?";
		params.add(rid);		
		return findFirst(sql, params.toArray());
	}
	
	
	public MecEpgProgramType setRid(java.lang.String rid){
		super.set("rid",rid);
		return this;
	}
	public java.lang.String getName(){
		return this.getStr("name");
	}
	
	public MecEpgProgramType setName(java.lang.String name){
		super.set("name",name);
		return this;
	}
}