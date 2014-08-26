package tv.zhiping.mec.luck.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;


/**
 * 预中奖信息
 * @author 作者
 * @version 1.0
 * @since 2014-08-06
 */
@SuppressWarnings("serial")
public class LuckyDrawBefore extends BasicModel<LuckyDrawBefore> {

	public static final LuckyDrawBefore dao = new LuckyDrawBefore();

	//中奖了
	public static final Integer WIND = 2;
	
	//领奖了
	public static final Integer TRADE_CREATED = 3;
	
	/**
	 * 获取有效时间内的活动
	 * @param episode_id
	 * @param time
	 * @return
	 */
	public LuckyDrawBefore queryByLuckyIdSeq(Long lucky_id,Long seq) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName);
		sql.append(" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and lucky_id=?");
		params.add(lucky_id);
		
		sql.append(" and seq=?");
		params.add(seq);
		
		return findFirst(sql.toString(),params.toArray());
	}
	
	/**
	 * 设置之前的数据失效
	 * @param id
	 */
	public void updateBeforeInvalid(Long lucky_id) {
		StringBuilder sql = new StringBuilder("update "+tableName+" set status=?,updated_at=? where lucky_id=? and status=?");
		List<Object> params = new ArrayList<Object>();
		
		params.add(Cons.STATUS_INVALID);
		params.add(new Timestamp(System.currentTimeMillis()));
		params.add(lucky_id);
		params.add(Cons.STATUS_VALID);
		
		Db.update(sql.toString(),params.toArray());
	}
	
	public java.lang.Long getLucky_id(){
		return this.get("lucky_id");
	}
	
	public LuckyDrawBefore setLucky_id(java.lang.Long lucky_id){
		super.set("lucky_id",lucky_id);
		return this;
	}
	public java.lang.Long getOption_id(){
		return this.get("option_id");
	}
	
	public LuckyDrawBefore setOption_id(java.lang.Long option_id){
		super.set("option_id",option_id);
		return this;
	}
	
	public java.lang.Long getSeq(){
		return this.get("seq");
	}
	
	public LuckyDrawBefore setSeq(java.lang.Long seq){
		super.set("seq",seq);
		return this;
	}
}