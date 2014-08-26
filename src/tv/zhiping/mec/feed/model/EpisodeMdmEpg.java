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
public class EpisodeMdmEpg extends BasicModel<EpisodeMdmEpg> {

	public static final EpisodeMdmEpg dao = new EpisodeMdmEpg();

	/**
	 * 根据媒资库id查询
	 */
	public EpisodeMdmEpg queryByMdaId(Long mdm_id){
		List<Object> params = new ArrayList<Object>();
		params.add(mdm_id);
		String sql = "select *  from "+tableName+" where mdm_id=?";
		return findFirst(sql, params.toArray());
	}
	
	/**
	 * 根据epgid获取媒资库的id
	 */
	public EpisodeMdmEpg queryByEgpId(Long epg_id){
		List<Object> params = new ArrayList<Object>();
		params.add(epg_id);
		String sql = "select *  from "+tableName+" where epg_id=?";
		return findFirst(sql, params.toArray());
	
	}
	
	public java.lang.Long getMdm_program_id(){
		return this.getLong("mdm_program_id");
	}
	
	public EpisodeMdmEpg setMdm_program_id(java.lang.Long mdm_program_id){
		super.set("mdm_program_id",mdm_program_id);
		return this;
	}
	public java.lang.Long getEpg_program_id(){
		return this.getLong("epg_program_id");
	}
	
	public EpisodeMdmEpg setEpg_program_id(java.lang.Long epg_program_id){
		super.set("epg_program_id",epg_program_id);
		return this;
	}
	public java.lang.Long getMdm_id(){
		return this.getLong("mdm_id");
	}
	
	public EpisodeMdmEpg setMdm_id(java.lang.Long mdm_id){
		super.set("mdm_id",mdm_id);
		return this;
	}
	public java.lang.Long getEpg_id(){
		return this.getLong("epg_id");
	}
	
	public EpisodeMdmEpg setEpg_id(java.lang.Long epg_id){
		super.set("epg_id",epg_id);
		return this;
	}
}