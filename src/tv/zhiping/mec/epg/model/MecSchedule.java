package tv.zhiping.mec.epg.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-05-21
 */
@SuppressWarnings("serial")
public class MecSchedule extends BasicModel<MecSchedule> {

	public static final MecSchedule dao = new MecSchedule();
	/**
	 * 根据频道，时间，分页查询
	 */
	public Page<?> paginate(MecSchedule obj, int pageNumber, int pageSize) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		if(obj!=null){
			if(obj.getMec_channel_id()!=null){
				sql.append(" and (mec_channel_id = ?)");
				params.add(obj.getMec_channel_id());
			}
			if(obj.getMec_start_at() != null){
				sql.append(" and mec_start_at>=?");
				params.add(obj.getMec_start_at());
			}
			if(obj.getMec_end_at()!= null){
				sql.append(" and mec_end_at<=?");
				params.add(obj.getMec_end_at());
			}
		}	
		sql.append(" order by mec_start_at asc");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),params.toArray());
	}
	
	/**
	 * 删除
	 * @param channel_id
	 * @param epg_start_at
	 */
	public void delByChannel(Long mec_channel_id,Long epg_start_at){
		StringBuilder sql = new StringBuilder("update "+tableName+" set status=?,updated_at=? where mec_channel_id=? and epg_start_at>=? and status=?");
		List<Object> params = new ArrayList<Object>();
		
		params.add(Cons.STATUS_INVALID);
		params.add(new Timestamp(System.currentTimeMillis()));
		
		params.add(mec_channel_id);
		params.add(epg_start_at);
		params.add(Cons.STATUS_VALID);
		
		Db.update(sql.toString(),params.toArray());
	}
	
	/**
	 * 根据频道，时间，获取这个时间内所有的节目信息
	 */
	public List<MecSchedule> queryAllByChannelTime(Long mec_channel_id,Long epg_start_at,Long epg_end_at){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and mec_channel_id=? and epg_start_at>=? and epg_start_at<=? order by mec_start_at";
		params.add(Cons.STATUS_VALID);
		params.add(mec_channel_id);
		params.add(epg_start_at);
		params.add(epg_end_at);
		
		return find(sql, params.toArray());
	}
	
	/**
	 * 根据rid得到对象
	 */
	public MecSchedule queryByRid(String rid){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where rid=? and status=?";
		params.add(rid);
		params.add(Cons.STATUS_VALID);
		return findFirst(sql, params.toArray());
	}


	/**
	 * 获取当前时间频道播放的节目信息
	 * @param epg_channel_id
	 * @param now
	 * @return
	 */
	public MecSchedule queryByChannelPyAndTime(String py, Long now) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select a.* from "+tableName+" a,mec_channel b where a.mec_channel_id=b.id and b.pinyin=? and a.status=? and b.status=? and a.mec_start_at<=? and ?<=a.mec_end_at";
		params.add(py);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		params.add(now);
		params.add(now);
		return findFirst(sql, params.toArray());
	}
	
	
	/**
	 * 获取当前时间频道播放的节目信息
	 * @param epg_channel_id
	 * @param now
	 * @return
	 */
	public MecSchedule queryByChannelAndTime(Long mec_channel_id, Long now) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where mec_channel_id=? and mec_start_at<=? and mec_end_at>=? and status=?";
		params.add(mec_channel_id);
		params.add(now);
		params.add(now);
		params.add(Cons.STATUS_VALID);
		return findFirst(sql, params.toArray());
	}
	
	/**
	 * 获取当前时间频道播放的剧集id的
	 * @param mec_channel_id
	 * @param mdm_episode_id
	 * @return
	 */
	public MecSchedule queryByChannelAndEpisodeId(Long mec_channel_id, Long mdm_episode_id) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		if(mec_channel_id!=null){
			sql.append(" and mec_channel_id=?");
			params.add(mec_channel_id);
		}
		if(mdm_episode_id!=null){
			sql.append(" and mdm_episode_id=?");
			params.add(mdm_episode_id);
		}
		sql.append(" order by mec_start_at");
		
		return findFirst(sql.toString(), params.toArray());
	}
	
	/**
	 * 待匹配的数据
	 * @return
	 */
	public List<MecSchedule> queryByWaitMath() {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and thread_state=? order by mec_start_at limit 0,100";
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_WAIT);
		return find(sql, params.toArray());
	}
	
	public java.lang.String getRid(){
		return this.getStr("rid");
	}
	
	public MecSchedule setRid(java.lang.String rid){
		super.set("rid",rid);
		return this;
	}
	public java.lang.String getEpg_channel_id(){
		return this.getStr("epg_channel_id");
	}
	
	public MecSchedule setEpg_channel_id(java.lang.String epg_channel_id){
		super.set("epg_channel_id",epg_channel_id);
		return this;
	}
	
	public java.lang.String getEpg_name(){
		return this.getStr("epg_name");
	}
	
	public MecSchedule setEpg_name(java.lang.String epg_name){
		super.set("epg_name",epg_name);
		return this;
	}
	
	public java.lang.String getEpg_program_id(){
		return this.getStr("epg_program_id");
	}
	
	public MecSchedule setEpg_program_id(java.lang.String epg_program_id){
		super.set("epg_program_id",epg_program_id);
		return this;
	}
	
	public java.lang.String getEpg_episode_id(){
		return this.getStr("epg_episode_id");
	}
	
	public MecSchedule setEpg_episode_id(java.lang.String epg_episode_id){
		super.set("epg_episode_id",epg_episode_id);
		return this;
	}
	public java.lang.Long getEpg_start_at(){
		return this.getLong("epg_start_at");
	}
	
	public MecSchedule setEpg_start_at(java.lang.Long epg_start_at){
		super.set("epg_start_at",epg_start_at);
		return this;
	}
	
	public java.lang.Long getMec_channel_id(){
		return this.getLong("mec_channel_id");
	}
	
	public MecSchedule setMec_channel_id(java.lang.Long mec_channel_id){
		super.set("mec_channel_id",mec_channel_id);
		return this;
	}
	
	public MecSchedule setMdm_program_id(java.lang.Long mdm_program_id){
		super.set("mdm_program_id",mdm_program_id);
		return this;
	}
	
	public java.lang.Long getMdm_program_id(){
		return this.getLong("mdm_program_id");
	}
	
	public MecSchedule setMdm_episode_id(java.lang.Long mdm_episode_id){
		super.set("mdm_episode_id",mdm_episode_id);
		return this;
	}
	
	public java.lang.Long getMdm_episode_id(){
		return this.getLong("mdm_episode_id");
	}
	
	public java.lang.Long getScene_id(){
		return this.getLong("scene_id");
	}
	
	public java.lang.Long getEpg_end_at(){
		return this.getLong("epg_end_at");
	}
	
	public MecSchedule setEpg_end_at(java.lang.Long epg_end_at){
		super.set("epg_end_at",epg_end_at);
		return this;
	}
	public java.lang.Long getMec_start_at(){
		return this.getLong("mec_start_at");
	}
	
	public MecSchedule setMec_start_at(java.lang.Long mec_start_at){
		super.set("mec_start_at",mec_start_at);
		return this;
	}
	public java.lang.Long getMec_end_at(){
		return this.getLong("mec_end_at");
	}
	
	public MecSchedule setMec_end_at(java.lang.Long mec_end_at){
		super.set("mec_end_at",mec_end_at);
		return this;
	}

	public java.lang.Integer getThread_state(){
		return this.getInt("thread_state");
	}
	
	public MecSchedule setThread_state(java.lang.Integer thread_state){
		super.set("thread_state",thread_state);
		return this;
	}
	
	public java.lang.String getThread_msg(){
		return this.getStr("thread_msg");
	}
	
	public MecSchedule setThread_msg(java.lang.String thread_msg){
		super.set("epg_episode_id",thread_msg);
		return this;
	}

}