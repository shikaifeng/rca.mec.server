package tv.zhiping.mec.luck.model;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-08-05
 */
@SuppressWarnings("serial")
public class LuckyDrawOption extends BasicModel<LuckyDrawOption> {

	public static final LuckyDrawOption dao = new LuckyDrawOption();
	/**
	 * 将某活动的所有奖项失效
	 * */
	public void setStatusInvalidByLuckyId(Long lucky_id) {
		StringBuilder sql = new StringBuilder("update "+tableName+" set status=?,updated_at=? where lucky_id=? and status=?");
		List<Object> params = new ArrayList<Object>();		
		params.add(Cons.STATUS_INVALID);
		params.add(new Timestamp(System.currentTimeMillis()));
		params.add(lucky_id);
		params.add(Cons.STATUS_VALID);		
		Db.update(sql.toString(),params.toArray());		
	}
	/*
	 * 获取这个活动下的所有奖项
	 * @param episode_id
	 * @param time
	 * @return
	 */
	public List<LuckyDrawOption> queryByLuckyId(Long lucky_id,Integer isluck) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName);
		sql.append(" where lucky_id=?");
		params.add(lucky_id);
		
		if(isluck!=null){
			sql.append(" and isluck=?");
			params.add(isluck);
		}
		
		sql.append(" and status=?");
		params.add(Cons.STATUS_VALID);
		
		return find(sql.toString(),params.toArray());
	}
	
	public java.lang.Long getLucky_id(){
		return this.get("lucky_id");
	}
	
	public LuckyDrawOption setLucky_id(java.lang.Long lucky_id){
		super.set("lucky_id",lucky_id);
		return this;
	}
	
	public java.lang.String getUrl(){
		return this.get("url");
	}
	
	public LuckyDrawOption setUrl(java.lang.String url){
		super.set("url",url);
		return this;
	}
	

	public java.lang.Integer getPosition(){
		return this.get("position");
	}

	public LuckyDrawOption setPosition(java.lang.Integer position){
		this.set("position",position);
		return this;
	}
	
	public java.lang.Integer getIsluck(){
		return this.get("isluck");
	}

	public LuckyDrawOption setIsluck(java.lang.Integer isluck){
		this.set("isluck",isluck);
		return this;
	}
	
	public java.lang.String getTitle(){
		return this.get("title");
	}
	
	public LuckyDrawOption setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getSummary(){
		return this.get("summary");
	}
	
	public LuckyDrawOption setSummary(java.lang.String summary){
		super.set("summary",summary);
		return this;
	}
	public java.lang.Integer getPrize_count(){
		return this.get("prize_count");
	}
	
	public LuckyDrawOption setPrize_count(java.lang.Integer prize_count){
		super.set("prize_count",prize_count);
		return this;
	}
	public java.lang.Integer getSurplus_prize_count(){
		return this.get("surplus_prize_count");
	}
	
	public LuckyDrawOption setSurplus_prize_count(java.lang.Integer surplus_prize_count){
		super.set("surplus_prize_count",surplus_prize_count);
		return this;
	}
	public java.lang.String getWin_rate(){
		return this.get("win_rate");
	}
	
	public LuckyDrawOption setWin_rate(java.lang.String win_rate){
		super.set("win_rate",win_rate);
		return this;
	}
	public java.lang.Long getEstimate_count(){
		return this.get("estimate_count");
	}
	
	public LuckyDrawOption setEstimate_count(java.lang.Long estimate_count){
		super.set("estimate_count",estimate_count);
		return this;
	}

}
