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
 * @since 2014-06-17
 */
@SuppressWarnings("serial")
public class ImdbEvent extends BasicModel<ImdbEvent> {

	public static final ImdbEvent dao = new ImdbEvent();

	/**
	 * 匹配失败的重新匹配
	 */
	public void backFail2Wait() {
		StringBuilder sql = new StringBuilder("update "+tableName+" set match_state=? where status=? and match_state=?");
		List<Object> params = new ArrayList<Object>();
		params.add(Cons.THREAD_STATE_WAIT);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_FAIL);
		
		Db.use(Cons.DB_NAME_MEDIA).update(sql.toString(),params.toArray());
	}
	
	
	public ImdbEvent queryByObj(String pid,String fid,String type, Long start, Long end) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and pid=? and type=? and fid=? and start_time=? and end_time=?";
		params.add(Cons.STATUS_VALID);
		params.add(pid);
		params.add(type);
		params.add(fid);
		params.add(start);
		params.add(end);
		
		return findFirst(sql, params.toArray());
	}
	
	
	public List<ImdbEvent> queryBySceneObj(String pid, Long start_time,Long end_time) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and pid=? and start_time>=? and end_time<=?";
		params.add(Cons.STATUS_VALID);
		params.add(pid);
		params.add(start_time);
		params.add(end_time);
		
		return find(sql, params.toArray());
	}
	
	/**
	 * 根据场景id获取所有元素
	 * @param scene_id
	 * @param match_state
	 * @return
	 */
	public List<ImdbEvent> queryBySceneId(Long scene_id,Integer match_state) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=? and scene_id=?");
		params.add(Cons.STATUS_VALID);
		params.add(scene_id);
		
		if(match_state!=null){
			sql.append(" and match_state=?");
			params.add(match_state);
		}
		
		return find(sql.toString(), params.toArray());
	}
	
	public void updMatchState(Integer match_state){
		setMatch_state(match_state);
		setUpdDef();
		update();
	}
	
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public ImdbEvent setType(java.lang.String type){
		super.set("type",type);
		return this;
	}
	public java.lang.String getFid(){
		return this.getStr("fid");
	}
	
	public ImdbEvent setFid(java.lang.String fid){
		super.set("fid",fid);
		return this;
	}
	public java.lang.String getC1(){
		return this.getStr("c1");
	}
	
	public ImdbEvent setC1(java.lang.String c1){
		super.set("c1",c1);
		return this;
	}
	public java.lang.String getProgram_id(){
		return this.getStr("program_id");
	}
	
	public ImdbEvent setProgram_id(java.lang.String program_id){
		super.set("program_id",program_id);
		return this;
	}
	public java.lang.String getEpisode_id(){
		return this.getStr("episode_id");
	}
	
	public ImdbEvent setEpisode_id(java.lang.String episode_id){
		super.set("episode_id",episode_id);
		return this;
	}
	public java.lang.String getPid(){
		return this.getStr("pid");
	}
	
	public ImdbEvent setPid(java.lang.String pid){
		super.set("pid",pid);
		return this;
	}
	public java.lang.Long getScene_id(){
		return this.getLong("scene_id");
	}
	
	public ImdbEvent setScene_id(java.lang.Long scene_id){
		super.set("scene_id",scene_id);
		return this;
	}
	public java.lang.Long getStart_time(){
		return this.getLong("start_time");
	}
	
	public ImdbEvent setStart_time(java.lang.Long start_time){
		super.set("start_time",start_time);
		return this;
	}
	public java.lang.Long getEnd_time(){
		return this.getLong("end_time");
	}
	
	public ImdbEvent setEnd_time(java.lang.Long end_time){
		super.set("end_time",end_time);
		return this;
	}
	public java.lang.Long getDuration(){
		return this.getLong("duration");
	}
	
	public ImdbEvent setDuration(java.lang.Long duration){
		super.set("duration",duration);
		return this;
	}
	
	public java.lang.Integer getMatch_state(){
		return this.getInt("match_state");
	}
	
	public ImdbEvent setMatch_state(java.lang.Integer match_state){
		if(match_state!=null){
			super.set("match_state",match_state);
		}
		return this;
	}
	public java.lang.Long getRca_id(){
		return this.getLong("rca_id");
	}
	
	public ImdbEvent setRca_id(java.lang.Long rca_id){
		if(rca_id!=null){
			super.set("rca_id",rca_id);
		}
		return this;
	}
	public java.lang.String getMsg(){
		return this.getStr("msg");
	}
	
	public ImdbEvent setMsg(java.lang.String msg){
		if(StringUtils.isNotBlank(msg)){
			super.set("msg",msg);
		}
		return this;
	}
	
	public java.lang.String getJson_txt(){
		return this.getStr("json_txt");
	}
	
	public ImdbEvent setJson_txt(java.lang.String json_txt){
		super.set("json_txt",json_txt);
		return this;
	}
}