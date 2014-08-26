package tv.zhiping.mec.user.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Db;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

/**
 * @author 作者
 * @version 1.0
 * @since 2014-07-18
 */
@SuppressWarnings("serial")
public class UserQuestion extends BasicModel<UserQuestion> {

	public static final UserQuestion dao = new UserQuestion();

	public static final Integer SUC = 1;
	public static final Integer RIGHT = 2;
	public static final Integer WRONG = 3;
	
	/**
	 * 根据活动id，用户id查询用户答对数量
	 * @param question_id
	 * @param udid
	 * @return
	 */
	public Long queryCountByLucIdUdid(Long lucky_id,String udid,Timestamp start_time,Timestamp end_time){
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select count(distinct a.id) as count from "+tableName+" a inner join question b inner join lucky_draw_episode c ");
		sql.append(" on a.question_id=b.id and  b.episode_id=c.episode_id ");
		
		sql.append(" where c.lucky_id=?");
		params.add(lucky_id);

		if(udid!=null){
			sql.append(" and a.udid=?");
			params.add(udid);			
		}
		
		sql.append(" and a.status=?");
		params.add(RIGHT);
		
		if(start_time != null){
			sql.append(" and a.created_at>=?");
			params.add(start_time);			
		}
		
		if(end_time != null){
			sql.append(" and a.created_at<=?");
			params.add(end_time);
		}
		UserQuestion obj = findFirst(sql.toString(),params.toArray());
		if(obj!=null){
			return obj.getLong("count");
		}
		return null;
	}
	
	
	/**
	 * 根据剧集id，用户id查询用户的选项
	 * @param question_id
	 * @param udid
	 * @return
	 */
	public UserQuestion queryCountByEpisodeIdUdid(Long episode_id,String udid){
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select count(a.id) as user_right_count from "+tableName+" a,question b where a.question_id=b.id and b.status=1");
		
		sql.append(" and a.udid=?");
		params.add(udid);
		
		sql.append(" and a.status=?");
		params.add(RIGHT);
		
		sql.append(" and b.episode_id=?");
		params.add(episode_id);
		
		return findFirst(sql.toString(),params.toArray());
	}
	
	/**
	 * 根据问题id，用户id查询用户的选项
	 * @param question_id
	 * @param udid
	 * @return
	 */
	public UserQuestion queryByQuestionIdUdid(Long question_id,String udid){
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where ");
		
		sql.append(" question_id=?");
		params.add(question_id);
		
		if(StringUtils.isNoneBlank(udid)){
			sql.append(" and udid=?");
			params.add(udid);
		}
		
		sql.append(" order by id desc");
		return findFirst(sql.toString(),params.toArray());
	}
	
	/**
	 * 设置正确，错误的答案
	 * @param que_id
	 * @param option_id
	 */
	public void updateSetRightOrWrongAnswer(Long que_id, Long option_id) {
		StringBuilder sql = new StringBuilder("update " + tableName
				+ " set status=?,updated_at=? where question_id=? and option_id=?");
		List<Object> params = new ArrayList<Object>();

		params.add(RIGHT);
		params.add(new Timestamp(System.currentTimeMillis()));
		params.add(que_id);
		params.add(option_id);

		Db.update(sql.toString(), params.toArray());
		
		
		sql.setLength(0);
		params.clear();
		
		sql.append("update " + tableName
				+ " set status=?,updated_at=? where question_id=? and option_id != ?");
		
		params.add(WRONG);
		params.add(new Timestamp(System.currentTimeMillis()));
		params.add(que_id);
		params.add(option_id);

		Db.update(sql.toString(), params.toArray());
	}
	
	/**
	 * 设置待确定的答案
	 * @param que_id
	 * @param option_id
	 */
	public void updateAllSetWrongAnswer(Long que_id) {
		StringBuilder sql = new StringBuilder("update " + tableName
				+ " set status=?,updated_at=? where question_id=?");
		List<Object> params = new ArrayList<Object>();

		params.add(Cons.STATUS_VALID);
		params.add(new Timestamp(System.currentTimeMillis()));
		params.add(que_id);

		Db.update(sql.toString(), params.toArray());
	}
	
	public int countUserRightNum(String udid) {
		String sql = "select count(*) from user_question";
		return 1;
	}
	
	public java.lang.String getUdid(){
		return this.getStr("udid");
	}
	
	public UserQuestion setUdid(java.lang.String udid){
		super.set("udid",udid);
		return this;
	}
	
	public java.lang.String getClient_time(){
		return this.getStr("client_time");
	}
	
	public UserQuestion setClient_time(java.lang.String client_time){
		super.set("client_time",client_time);
		return this;
	}
	
	public java.lang.Long getQuestion_id(){
		return this.getLong("question_id");
	}
	
	public UserQuestion setQuestion_id(java.lang.Long question_id){
		super.set("question_id",question_id);
		return this;
	}
	
	public java.lang.Long getOption_id(){
		return this.getLong("option_id");
	}
	
	public UserQuestion setOption_id(java.lang.Long option_id){
		super.set("option_id",option_id);
		return this;
	}
}