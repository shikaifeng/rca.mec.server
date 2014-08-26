package tv.zhiping.mec.util.model;

import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-16
 */
@SuppressWarnings("serial")
public class FfmpegHistory extends BasicModel<FfmpegHistory> {

	public static final FfmpegHistory dao = new FfmpegHistory();

	/**
	 * 判断是否已经截图
	 * @param episode_id 节目id
	 * @param start_time 开始时间
	 * @param end_time   结束时间
	 * @param type	 	  类型
	 * @return
	 */
	public FfmpegHistory queryByEpisodeTimeType(Long episode_id,Long start_time,Long end_time,String type) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=? and episode_id=?");
		params.add(Cons.STATUS_VALID);
		params.add(episode_id);
		
		if(start_time!=null){
			sql.append(" and start_time=?");
			params.add(start_time);
		}
		
		if(end_time!=null){
			sql.append(" and end_time=?");
			params.add(end_time);
		}
		
		if(type!=null){
			sql.append(" and type=?");
			params.add(type);
		}
		
		return findFirst(sql.toString(), params.toArray());
	}
	
	/**
	 * 查询待处理的
	 */
	public List<FfmpegHistory> queryByWait() {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=? and thread_state=?");
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_WAIT);

		sql.append(" order by episode_id,start_time");
		return find(sql.toString(), params.toArray());
	}
	
	public java.lang.Long getEpisode_id(){
		return this.getLong("episode_id");
	}
	
	public FfmpegHistory setEpisode_id(java.lang.Long episode_id){
		super.set("episode_id",episode_id);
		return this;
	}
	public java.lang.Long getStart_time(){
		return this.getLong("start_time");
	}
	
	public FfmpegHistory setStart_time(java.lang.Long start_time){
		super.set("start_time",start_time);
		return this;
	}
	public java.lang.Long getEnd_time(){
		return this.getLong("end_time");
	}
	
	public FfmpegHistory setEnd_time(java.lang.Long end_time){
		super.set("end_time",end_time);
		return this;
	}
	public java.lang.String getFilename(){
		return this.getStr("filename");
	}
	
	public FfmpegHistory setFilename(java.lang.String filename){
		super.set("filename",filename);
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public FfmpegHistory setType(java.lang.String type){
		super.set("type",type);
		return this;
	}
	
	public java.lang.String getCmd(){
		return this.getStr("cmd");
	}
	
	public FfmpegHistory setCmd(java.lang.String cmd){
		super.set("cmd",cmd);
		return this;
	}
	
	
	public java.lang.Integer getThread_state(){
		return this.getInt("thread_state");
	}
	
	public FfmpegHistory setThread_state(java.lang.Integer thread_state){
		super.set("thread_state",thread_state);
		return this;
	}
	
	public java.lang.Integer getTimes(){
		return this.getInt("times");
	}
	
	public FfmpegHistory setTimes(java.lang.Integer times){
		super.set("times",times);
		return this;
	}
}