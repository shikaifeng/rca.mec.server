package tv.zhiping.mec.feed.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.SysBasicModel;
import tv.zhiping.mec.user.model.UserQuestion;

import com.jfinal.plugin.activerecord.Db;

/**
 * @author 作者
 * @version 1.0
 * @since 2014-07-16
 */
@SuppressWarnings("serial")
public class Question extends SysBasicModel<Question> {

	public static final Question dao = new Question();
	
	/**
	 * 获取小于这个时间，最接近的一条互动
	 * @param episode_id
	 * @param lt_start_time
	 * @return
	 */
	public Question queryByLtStartTimeMaxTime(Long episode_id, Long lt_start_time) {
		List<Object> params = new ArrayList<Object>();
	
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episode_id!=null){
			sql.append(" and episode_id=?");
			params.add(episode_id);
		}
		
		sql.append(" and start_time<?");
		params.add(lt_start_time);
		
		sql.append(" order by start_time desc");
		return findFirst(sql.toString(),params.toArray());
	}
	

	public void deleteByEpisodeId(Long episode_id) {
		StringBuilder sql = new StringBuilder("update " + tableName
				+ " set status=?,updated_at=? where episode_id=?");
		List<Object> params = new ArrayList<Object>();

		params.add(Cons.STATUS_INVALID);
		params.add(new Timestamp(System.currentTimeMillis()));
		params.add(episode_id);

		Db.update(sql.toString(), params.toArray());
	}

	public List<Question> queryByEpisodeId(Long episode_id) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from " + tableName
				+ " where status=?");
		params.add(Cons.STATUS_VALID);
		if (episode_id != null) {
			sql.append(" and episode_id=?");
			params.add(episode_id);
		}
		sql.append(" order by if(start_time is not null,start_time,2147483647)");
		return find(sql.toString(), params.toArray());
	}

	/**
	 * 查询大于开始时间，小于截止时间：的时间最大哪条
	 * 
	 * @param episode_id
	 * @param type
	 * @param start_time
	 * @return
	 */
	public Question queryByLtEndTimeMaxTime(Long episode_id, Long time) {
		List<Object> params = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder("select * from " + tableName
				+ " where status=?");
		params.add(Cons.STATUS_VALID);

		if (episode_id != null) {
			sql.append(" and episode_id=?");
			params.add(episode_id);
		}

		sql.append(" and ((start_time<=? and ?<end_time) or (start_time<=? and end_time is null))");
		params.add(time);
		params.add(time);
		params.add(time);

		sql.append(" order by start_time desc");
		return findFirst(sql.toString(), params.toArray());
	}
	
	
	/**
	 * 判断是否有小于这个时间的
	 * @param episode_id
	 * @param gt_start_time
	 * @return
	 */
	public boolean queryFrontFirstQuestion(Long episode_id, Long gt_start_time) {
		Long count = queryCountByParams(episode_id,gt_start_time);
		if(count == null || Cons.DEF_NULL_NUMBER.equals(count)){
			count = queryCountByParams(episode_id,null);
			if(count!=null && count>0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 返回条数
	 * @param episode_id
	 * @param gt_start_time
	 * @return
	 */
	public Long queryCountByParams(Long episode_id, Long gt_start_time) {
		List<Object> params = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder("select count(1) as count from " + tableName + " where status=?");
		params.add(Cons.STATUS_VALID);

		if (episode_id != null) {
			sql.append(" and episode_id=?");
			params.add(episode_id);
		}
		
		if (gt_start_time != null) {
			sql.append(" and start_time<?");
			params.add(gt_start_time);
		}
		
		Question obj  = findFirst(sql.toString(), params.toArray());
		if(obj!=null){
			return obj.get("count");
		}
		return 0L;
	}
	
	
	
	public void saveQueAndOption(Question obj,List<QuestionOption> opts,Integer answer_index) {
		Long que_id = obj.getId();
		if(answer_index == null){
			obj.setAnswer_id(null);
		}
		
		Long old_answer_id = null;
		Long new_answer_id = null;
		
		if(que_id==null){
			obj.save();
			que_id = obj.getId();
		}else{
			Question old_que = findById(que_id);
			if(old_que!=null){
				old_answer_id = old_que.getAnswer_id();
			}
			
			obj.update();
		}
		
		
		if(opts!=null && !opts.isEmpty()){
			int size = opts.size();
			QuestionOption opt = null;
			for(int i=0;i<size;i++){
				opt = (QuestionOption)opts.get(i);
				opt.setQuestion_id(que_id);
				if(opt.getId()==null){
					opt.setAddDef();
					opt.save();
				}else{
					opt.setUpdDef();
					opt.update();
				}
			}
			
			if(answer_index != null){
				opt = opts.get(answer_index);
				new_answer_id = opt.getId();
			}
		}
		
		if((new_answer_id==null && new_answer_id!=old_answer_id) || (new_answer_id!=null && !new_answer_id.equals(old_answer_id))){//和之前数据不相同
			obj.setAnswer_id(new_answer_id);
			obj.setUpdDef();
			obj.update();
			
			if(new_answer_id != null){
				UserQuestion.dao.updateSetRightOrWrongAnswer(que_id,new_answer_id);				
			}else{
				UserQuestion.dao.updateAllSetWrongAnswer(que_id);
			}
		}
	}
	

	public java.lang.String getTitle() {
		return this.getStr("title");
	}

	public Question setTitle(java.lang.String title) {
		super.set("title", title);
		return this;
	}

	public java.lang.Long getProgram_id() {
		return this.getLong("program_id");
	}

	public Question setProgram_id(java.lang.Long program_id) {
		super.set("program_id", program_id);
		return this;
	}

	public java.lang.Long getEpisode_id() {
		return this.getLong("episode_id");
	}

	public Question setEpisode_id(java.lang.Long episode_id) {
		super.set("episode_id", episode_id);
		return this;
	}

	public java.lang.Long getAnswer_id() {
		return this.getLong("answer_id");
	}

	public Question setAnswer_id(java.lang.Long answer_id) {
		super.set("answer_id", answer_id);
		return this;
	}

	public java.lang.Long getStart_time() {
		return this.getLong("start_time");
	}

	public Question setStart_time(java.lang.Long start_time) {
		super.set("start_time", start_time);
		return this;
	}

	public java.lang.Long getEnd_time() {
		return this.getLong("end_time");
	}

	public Question setEnd_time(java.lang.Long end_time) {
		super.set("end_time", end_time);
		return this;
	}

	public java.lang.Long getDeadline() {
		return this.getLong("deadline");
	}

	public Question setDeadline(java.lang.Long deadline) {
		super.set("deadline", deadline);
		return this;
	}

	public java.lang.Long getPublic_time() {
		return this.getLong("public_time");
	}

	public Question setPublic_time(java.lang.Long public_time) {
		super.set("public_time", public_time);
		return this;
	}

	public java.lang.String getSummary() {
		return this.getStr("summary");
	}

	public Question setSummary(java.lang.String summary) {
		super.set("summary", summary);
		return this;
	}
}