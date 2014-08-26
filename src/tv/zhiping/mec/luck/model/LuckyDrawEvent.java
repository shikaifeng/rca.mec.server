package tv.zhiping.mec.luck.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-08-04
 */
@SuppressWarnings("serial")
public class LuckyDrawEvent extends BasicModel<LuckyDrawEvent> {
	public static final LuckyDrawEvent dao = new LuckyDrawEvent();

	/**
	 * 设置中奖的序列
	 * @param id
	 */
	public void addWin_seq() {
		Long seq = getSeq();
		if(seq == null){
			seq = 0L;
		}
		seq++;
		setSeq(seq);
		
		seq = getWin_seq();
		if(seq == null){
			seq = 0L;
		}
		seq++;
		setWin_seq(seq);
		
		update();
	}
	

	/**
	 * 设置序列
	 * @param id
	 */
	public void addSeq() {
		Long seq = getSeq();
		if(seq == null){
			seq = 0L;
		}
		seq++;
		setSeq(seq);
		
		update();
	}
	
	/**
	 * 获取有效时间内的活动
	 * @param episode_id
	 * @param time
	 * @return
	 */
	public LuckyDrawEvent queryByLastClose(Long episode_id, Long time) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select a.* from "+tableName+" a,lucky_draw_episode b ");
		sql.append(" where a.id=b.lucky_id");
		
		sql.append(" and a.status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and b.status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and a.start_time<=?");
		sql.append(" and ?<=a.end_time");
		
		Date now = new Date();
		params.add(now);
		params.add(now);

		sql.append(" and b.episode_id=?");
		params.add(episode_id);
		
		if(time!=null){
			sql.append(" and ((b.start_time<? and ?<b.end_time) or (b.start_time<? and b.end_time is null))");
			params.add(time);
			params.add(time);
			params.add(time);			
		}
		
		return findFirst(sql.toString(),params.toArray());
	}
	
	public List<LuckyDrawEvent> queryAllEvent() {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? order by start_time ";
		params.add(Cons.STATUS_VALID);
		return find(sql,params.toArray());
	}
	
	public java.lang.String getTitle(){
		return this.get("title");
	}
	
	public LuckyDrawEvent setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getSummary(){
		return this.get("summary");
	}
	
	public java.lang.Long getSeq(){
		return this.get("seq");
	}
	
	public LuckyDrawEvent setWin_seq(java.lang.Long win_seq){
		super.set("win_seq",win_seq);
		return this;
	}
	
	public java.lang.Long getWin_seq(){
		return this.get("win_seq");
	}
	
	public LuckyDrawEvent setSeq(java.lang.Long seq){
		super.set("seq",seq);
		return this;
	}
	
	public LuckyDrawEvent setSummary(java.lang.String summary){
		super.set("summary",summary);
		return this;
	}
	
	public java.lang.String getUrl(){
		return this.get("url");
	}
	
	public LuckyDrawEvent setUrl(java.lang.String url){
		super.set("url",url);
		return this;
	}
	
	public java.sql.Timestamp getStart_time(){
		return this.get("start_time");
	}
	
	public LuckyDrawEvent setStart_time(java.sql.Timestamp start_time){
		super.set("start_time",start_time);
		return this;
	}
	public java.sql.Timestamp getEnd_time(){
		return this.get("end_time");
	}
	
	public LuckyDrawEvent setEnd_time(java.sql.Timestamp end_time){
		super.set("end_time",end_time);
		return this;
	}
	public java.lang.Long getEstimate_count(){
		return this.get("estimate_count");
	}
	
	public LuckyDrawEvent setEstimate_count(java.lang.Long estimate_count){
		super.set("estimate_count",estimate_count);
		return this;
	}
	
	public java.lang.Integer getWin_day(){
		return this.get("win_day");
	}
	
	public LuckyDrawEvent setWin_day(java.lang.Integer win_day){
		super.set("win_day",win_day);
		return this;
	}
	public java.lang.Integer getWin_count(){
		return this.get("win_count");
	}
	
	public LuckyDrawEvent setWin_count(java.lang.Integer win_count){
		super.set("win_count",win_count);
		return this;
	}
}