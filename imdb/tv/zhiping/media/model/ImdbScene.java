package tv.zhiping.media.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

import com.jfinal.plugin.activerecord.Db;

/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-16
 */
@SuppressWarnings("serial")
public class ImdbScene extends BasicModel<ImdbScene> {

	public static final ImdbScene dao = new ImdbScene();
	
	/**
	 * 匹配失败的重新匹配
	 */
	public void backFail2Wait() {
		StringBuilder sql = new StringBuilder("update "+tableName+" a,imdb_event b set a.match_state=? where a.id=b.scene_id and a.status=? and b.status=? and b.match_state=?");
		List<Object> params = new ArrayList<Object>();
		params.add(Cons.THREAD_STATE_WAIT);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_FAIL);
		
		Db.use(Cons.DB_NAME_MEDIA).update(sql.toString(),params.toArray());
	}
	
	

	public ImdbScene queryByObj(String pid,Long start_time,Long end_time){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and pid=? and start_time=? and end_time=?";
		params.add(Cons.STATUS_VALID);
		params.add(pid);
		params.add(start_time);
		params.add(end_time);
		
		return findFirst(sql, params.toArray());
	} 
	
	public List<ImdbScene> queryByAll(String pid) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and pid=? order by start_time";
		params.add(Cons.STATUS_VALID);
		params.add(pid);
		
		return find(sql, params.toArray());
	}
	
	public List<ImdbScene> queryByWait() {
		List<Object> params = new ArrayList<Object>();
		String sql = "select a.*,b.mdm_program_id,b.mdm_episode_id from "+tableName+" a,imdb_episode b where a.pid=b.id and a.status=? and b.status=? and a.match_state=? order by b.mdm_program_id,b.mdm_episode_id,a.start_time limit 0,1000";
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_WAIT);
		
		return find(sql, params.toArray());
	}
	
	public void updMatchState(Integer match_state){
		setMatch_state(match_state);
		setUpdDef();
		update();
	}
	
	public java.lang.String getPid(){
		return this.getStr("pid");
	}
	
	public ImdbScene setPid(java.lang.String pid){
		super.set("pid",pid);
		return this;
	}
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public ImdbScene setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.Long getStart_time(){
		return this.getLong("start_time");
	}
	
	public ImdbScene setStart_time(java.lang.Long start_time){
		super.set("start_time",start_time);
		return this;
	}
	public java.lang.Long getEnd_time(){
		return this.getLong("end_time");
	}
	
	public ImdbScene setEnd_time(java.lang.Long end_time){
		super.set("end_time",end_time);
		return this;
	}
	public java.lang.Long getDuration(){
		return this.getLong("duration");
	}
	
	public ImdbScene setDuration(java.lang.Long duration){
		super.set("duration",duration);
		return this;
	}
	
	public java.lang.String getSummary(){
		return this.getStr("summary");
	}
	
	public ImdbScene setSummary(java.lang.String summary){
		super.set("summary",summary);
		return this;
	}
	
	public java.lang.Integer getMatch_state(){
		return this.getInt("match_state");
	}
	
	public ImdbScene setMatch_state(java.lang.Integer match_state){
		if(match_state!=null){
			super.set("match_state",match_state);
		}
		return this;
	}
	public java.lang.Long getRca_id(){
		return this.getLong("rca_id");
	}
	
	public ImdbScene setRca_id(java.lang.Long rca_id){
		if(rca_id!=null){
			super.set("rca_id",rca_id);
		}
		return this;
	}
	public java.lang.String getMsg(){
		return this.getStr("msg");
	}
	
	public ImdbScene setMsg(java.lang.String msg){
		if(StringUtils.isNotBlank(msg)){
			super.set("msg",msg);
		}
		return this;
	}
	
	public java.lang.String getJson_txt(){
		return this.getStr("json_txt");
	}
	
	public ImdbScene setJson_txt(java.lang.String json_txt){
		super.set("json_txt",json_txt);
		return this;
	}
}