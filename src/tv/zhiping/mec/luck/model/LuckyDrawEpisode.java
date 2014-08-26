package tv.zhiping.mec.luck.model;

import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

/**
 * @author 作者
 * @version 1.0
 * @since 2014-08-04
 */
@SuppressWarnings("serial")
public class LuckyDrawEpisode extends BasicModel<LuckyDrawEpisode> {

	public static final LuckyDrawEpisode dao = new LuckyDrawEpisode();
	
//	public void setStatusInvalidByLuckyIdProgramId(Long lucky_id,Long program_id) {
//		StringBuilder sql = new StringBuilder("update "+tableName+" set status=?,updated_at=? where lucky_id=? and program_id=? and status=?");
//		List<Object> params = new ArrayList<Object>();		
//		params.add(Cons.STATUS_INVALID);
//		params.add(new Timestamp(System.currentTimeMillis()));
//		params.add(lucky_id);
//		params.add(program_id);
//		params.add(Cons.STATUS_VALID);		
//		Db.update(sql.toString(),params.toArray());		
//	}
	
	
	public List<LuckyDrawEpisode> queryByLuckId(Long lucky_id) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=? and lucky_id=?");
		params.add(Cons.STATUS_VALID);
		params.add(lucky_id);
		return find(sql.toString(), params.toArray());
	}
	
//	public void saveOrUpdate(LuckyDrawEpisode obj) {
//		LuckyDrawEpisode lde = queryByLuckEpisode(obj.getLucky_id(),obj.getEpisode_id());
//		obj.setStatus(Cons.STATUS_VALID);
//		if(lde!=null){
//			obj.setId(lde.getId());
//			obj.setUpdDef();
//			obj.update();
//		}else{
//			obj.setAddDef();
//			obj.save();
//		}
//	}
	
	/**查询数据库中包含注销状态为0的所有数据*/
	public LuckyDrawEpisode queryByLuckEpisode(Long lucky_id, Long episode_id) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where lucky_id=? and episode_id=? and status=?");
		params.add(lucky_id);
		params.add(episode_id);
		params.add(Cons.STATUS_VALID);
		return findFirst(sql.toString(), params.toArray());
	}

	public java.lang.Long getLucky_id(){
		return this.get("lucky_id");
	}
	
	public LuckyDrawEpisode setLucky_id(java.lang.Long lucky_id){
		super.set("lucky_id",lucky_id);
		return this;
	}
	
	public java.lang.Long getProgram_id(){
		return this.get("program_id");
	}
	
	public LuckyDrawEpisode setProgram_id(java.lang.Long program_id){
		super.set("program_id",program_id);
		return this;
	}
	public java.lang.Long getEpisode_id(){
		return this.get("episode_id");
	}
	
	public LuckyDrawEpisode setEpisode_id(java.lang.Long episode_id){
		super.set("episode_id",episode_id);
		return this;
	}
	public java.lang.Long getStart_time(){
		return this.get("start_time");
	}
	
	public LuckyDrawEpisode setStart_time(java.lang.Long start_time){
		super.set("start_time",start_time);
		return this;
	}
	public java.lang.Long getEnd_time(){
		return this.get("end_time");
	}
	
	public LuckyDrawEpisode setEnd_time(java.lang.Long end_time){
		super.set("end_time",end_time);
		return this;
	}

}