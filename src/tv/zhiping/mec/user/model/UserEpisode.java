package tv.zhiping.mec.user.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

/**
 * 设备_剧集识别记录表
 * @author 樊亚容 2014-5-17
 * */
@SuppressWarnings("serial")
public class UserEpisode extends BasicModel<UserEpisode> {

	public static final UserEpisode dao = new UserEpisode();

	/**
	 * 分页查询 设备剧集识别记录表
	 * 
	 * @return
	 */
	public Page<UserEpisode> paginate(Long ucid, int pageNumber, int pageSize) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("from "+tableName+" where status=? ");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and ucid = ? ");
		params.add(ucid);
		
		sql.append(" order by last_at desc ");
		return paginate(pageNumber, pageSize,"select * ", sql.toString(),
				 params.toArray());
	}

	/**
	 * 根据用用户id和剧集id获取观看记录
	 * @param ucid
	 * @param episodeId
	 * @return
	 */
	public UserEpisode queryByUcidAndEpisode(Long ucid,Long programId,Long episodeId,String type) {
		List<Object> paras = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=? ");
		paras.add(Cons.STATUS_VALID);
		
		sql.append(" and ucid = ? ");
		paras.add(ucid);
		
		if(UserEpisodeType.episode.toString().equals(type) && episodeId!=null){
			sql.append(" and episode_id = ? ");
			paras.add(episodeId);
		}else if(UserEpisodeType.program.toString().equals(type) &&  programId!=null){
			sql.append(" and program_id = ? ");
			paras.add(programId);
		}
		
		sql.append(" and type = ?");
		paras.add(type);
		
		return findFirst(sql.toString(), paras.toArray());
	}
	
	//类型字段
	public static enum UserEpisodeType {
		program, episode
	}
	

	public java.lang.String getType() {
		return this.getStr("type");
	}

	public UserEpisode setType(java.lang.String type) {
		super.set("type", type);
		return this;
	}
	
	/* UCID */
	public java.lang.Long getUcid() {
		return this.getLong("ucid");
	}

	public UserEpisode setUcid(java.lang.Long ucid) {
		super.set("ucid", ucid);
		return this;
	}

	/* program_id */
	public java.lang.Long getProgramId() {
		return this.getLong("program_id");
	}

	public UserEpisode setProgramId(java.lang.Long program_id) {
		super.set("program_id", program_id);
		return this;
	}

	/* episode_id */
	public java.lang.Long getEpisodeId() {
		return this.getLong("episode_id");
	}

	public UserEpisode setEpisodeId(java.lang.Long episode_id) {
		super.set("episode_id", episode_id);
		return this;
	}

	/* start_time */
	public java.lang.Long getStartTime() {
		return this.getLong("start_time");
	}

	public UserEpisode setStartTime(java.lang.Long start_time) {
		super.set("start_time", start_time);
		return this;
	}

	/* end_time */
	public java.lang.Long getEndTime() {
		return this.getLong("end_time");
	}

	public UserEpisode setEndTime(java.lang.Long end_time) {
		super.set("end_time", end_time);
		return this;
	}

	/* last_at 最后一次识别时间 */
	public Timestamp getLastAt() {
		return this.getTimestamp("last_at");
	}

	public UserEpisode setLastAt(java.sql.Timestamp last_at) {
		super.set("last_at", last_at);
		return this;
	}
}
