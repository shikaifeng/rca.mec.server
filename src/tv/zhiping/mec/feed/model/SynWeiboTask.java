package tv.zhiping.mec.feed.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.utils.DateUtil;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author 作者
 * @version 1.0
 * @since 2014-07-22
 */
@SuppressWarnings("serial")
public class SynWeiboTask extends BasicModel<SynWeiboTask> {
	public static final SynWeiboTask dao = new SynWeiboTask();

	
	/**
	 * 重新启动匹配
	 */
	public void upd2Wait() {
		StringBuilder sql = new StringBuilder("update "+tableName+" set thread_state=? where status=? and ((min_time<=? and ?<=max_time) or (min_time is null and max_time is null))");
		List<Object> params = new ArrayList<Object>();
		params.add(Cons.THREAD_STATE_WAIT);
		params.add(Cons.STATUS_VALID);
		
		Date date = new Date();
		params.add(date);
		params.add(date);
		
		Db.update(sql.toString(),params.toArray());
	}
	
	/**
	 * 分页查询 待匹配的
	 */
	public Page<SynWeiboTask> paginateByWiat(int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("from "+tableName+" ");
		sql.append("where status=? and thread_state=?");
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_WAIT);
		
		sql.append("  order by id asc");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),params.toArray());
	}
	
	/**
	 * 根据episodeid获取task
	 */
	public SynWeiboTask saveSynWeiboTask(Long episode_id) {
		SynWeiboTask obj = queryByEpisodeId(episode_id);
		if(obj == null){
			obj = new SynWeiboTask();
			
			Episode episode = Episode.dao.findById(episode_id);
			if(episode!=null){
				obj.setProgram_id(episode.getPid());				
				obj.setEpisode_id(episode_id);
				obj.setAddDef();
				obj.save();
			}
		}
		return obj;
	}

	public SynWeiboTask queryByEpisodeId(Long episode_id) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" ");
		sql.append("where status=? and episode_id=?");
		params.add(Cons.STATUS_VALID);
		params.add(episode_id);
		
		return findFirst(sql.toString(),params.toArray());
	}
	
	/**
	 * 重新导入
	 * @param episode_id
	 */
	public void resetWeiboTask(Long episode_id) {
		SynWeiboTask obj = queryByEpisodeId(episode_id);
		if(obj != null){
			obj.setMax_id(Cons.DEF_NULL_NUMBER);
			obj.setUpdDef();
			obj.update();
		}else{
			saveSynWeiboTask(episode_id);
		}
	}
	
	public void resetSyn() {
		StringBuilder sql = new StringBuilder("update "+tableName+" set max_id=?,last_updated_at=?, updated_at=? where status=?");
		List<Object> params = new ArrayList<Object>();
		
		params.add(Cons.DEF_NULL_NUMBER);
		params.add(null);
		
		params.add(new Timestamp(System.currentTimeMillis()));
		params.add(Cons.STATUS_VALID);
		
		Db.update(sql.toString(),params.toArray());
	}
	
	public java.lang.Long getProgram_id(){
		return this.getLong("program_id");
	}
	
	public SynWeiboTask setProgram_id(java.lang.Long program_id){
		super.set("program_id",program_id);
		return this;
	}
	public java.lang.Long getEpisode_id(){
		return this.getLong("episode_id");
	}
	
	public SynWeiboTask setEpisode_id(java.lang.Long episode_id){
		super.set("episode_id",episode_id);
		return this;
	}
	public java.lang.Long getMax_id(){
		return this.getLong("max_id");
	}
	
	public SynWeiboTask setTimes(java.lang.Long times){
		super.set("times",times);
		return this;
	}
	public java.lang.Long getTimes(){
		return this.getLong("times");
	}
	
	public SynWeiboTask setMax_id(java.lang.Long max_id){
		super.set("max_id",max_id);
		return this;
	}
	public java.sql.Timestamp getLast_updated_at(){
		return this.getTimestamp("last_updated_at");
	}
	
	public SynWeiboTask setLast_updated_at(java.sql.Timestamp last_updated_at){
		super.set("last_updated_at",last_updated_at);
		return this;
	}
	
	public java.sql.Timestamp getMin_time(){
		return this.getTimestamp("min_time");
	}
	
	public SynWeiboTask setMin_time(java.sql.Timestamp min_time){
		super.set("min_time",min_time);
		return this;
	}
	
	public java.sql.Timestamp getMax_time(){
		return this.getTimestamp("max_time");
	}
	
	public SynWeiboTask setMax_time(java.sql.Timestamp max_time){
		super.set("max_time",max_time);
		return this;
	}
	
	public java.lang.Long getThread_state(){
		return this.getLong("thread_state");
	}
	
	public SynWeiboTask setThread_state(java.lang.Integer thread_state){
		super.set("thread_state",thread_state);
		return this;
	}
}
