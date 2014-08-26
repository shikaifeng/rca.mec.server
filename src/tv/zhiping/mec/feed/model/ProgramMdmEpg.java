package tv.zhiping.mec.feed.model;

import java.util.ArrayList;
import java.util.List;

import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-05-21
 */
@SuppressWarnings("serial")
public class ProgramMdmEpg extends BasicModel<ProgramMdmEpg> {

	public static final ProgramMdmEpg dao = new ProgramMdmEpg();

	/**
	 * 更新 或者 添加 : 根据媒资库 Id 添加
	 * @author 樊亚容
	 * 2014-5-26
	 */
	public void doUpdate(ProgramMdmEpg newPme) {
		ProgramMdmEpg olderObj 	= queryByEpgId(newPme.getEpg_id());
		if(olderObj != null){
			newPme.setId(olderObj.getId());
			newPme.setUpdDef();
			newPme.update();
		}else{
			newPme.setAddDef();
			newPme.save();
		}
		
	}
	
	
	public ProgramMdmEpg queryByEpgId(Long epg_id) {
		List<Object> params = new ArrayList<Object>();	

		params.add(epg_id);	
		String sql = "select * from "+tableName+" where epg_id = ?";			
		return findFirst(sql, params.toArray());
	}

 
	public java.lang.Long getMdm_id(){
		return this.getLong("mdm_id");
	}
	
	public ProgramMdmEpg setMdm_id(java.lang.Long mdm_id){
		super.set("mdm_id",mdm_id);
		return this;
	}
	public java.lang.Long getEpg_id(){
		return this.get("epg_id");
	}
	
	public ProgramMdmEpg setEpg_id(java.lang.Long epg_id){
		super.set("epg_id",epg_id);
		return this;
	}


}