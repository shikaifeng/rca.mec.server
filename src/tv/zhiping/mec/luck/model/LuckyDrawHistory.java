package tv.zhiping.mec.luck.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import com.jfinal.plugin.activerecord.Page;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

/**
 * @author 作者
 * @version 1.0
 * @since 2014-08-05
 */
@SuppressWarnings("serial")
public class LuckyDrawHistory extends BasicModel<LuckyDrawHistory> {

	public static final LuckyDrawHistory dao = new LuckyDrawHistory();
	
	/**
	 * 根据活动id 分页查询中奖人物
	 * */
	public Page<LuckyDrawHistory> paginate(Long lucky_id, int pageNumber, int pageSize) {
		List<Object> params = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder("from " + tableName
				+ " where status>=?");
		params.add(LuckyDrawBefore.WIND);
		if(lucky_id!=null){
			sql.append(" and lucky_id=?");
			params.add(lucky_id);
		}
		sql.append("  order by created_at desc");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),params.toArray());
	}

	/**
	 * 根据活动id查询中奖人物
	 * */
	public LuckyDrawHistory queryByLuckyIdStatus(Long lucky_id,Integer status) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from " + tableName);
		sql.append(" where lucky_id=?");
		params.add(lucky_id);
		
		if(status!=null){
			sql.append(" and status=?");
			params.add(status);
		}
		return findFirst(sql.toString(), params.toArray());
	}
	
	/**
	 * 根据活动id查询中奖人物
	 * */
	public List<LuckyDrawHistory> queryByLuckyId(Long lucky_id) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from " + tableName);
		sql.append(" where status>="+LuckyDrawBefore.WIND);
		sql.append(" and lucky_id=?");
		params.add(lucky_id);
		sql.append(" order by created_at desc");
		return find(sql.toString(), params.toArray());
	}

	/**
	 * 查看一个人，在这个活动中，中奖次数
	 * 
	 * @param lucky_id
	 * @param udid
	 * @return
	 */
	public List<LuckyDrawHistory> queryCountByLuckyId(Long lucky_id,
			String udid, Integer status, Timestamp start_time,
			Timestamp end_time) {
		List<Object> params = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder("select * from " + tableName);
		sql.append(" where udid=? and lucky_id=?");

		params.add(udid);
		params.add(lucky_id);

		if (status != null) {
			sql.append(" and a.status=?");
			params.add(status);
		}

		if (start_time != null) {
			sql.append(" and a.created_at>=?");
			params.add(start_time);
		}

		if (end_time != null) {
			sql.append(" and a.created_at<=?");
			params.add(end_time);
		}
		return find(sql.toString(), params.toArray());
	}

	public java.lang.String getUdid() {
		return this.get("udid");
	}

	public LuckyDrawHistory setUdid(java.lang.String udid) {
		super.set("udid", udid);
		return this;
	}

	public java.lang.Long getSeq() {
		return this.get("seq");
	}

	public LuckyDrawHistory setSeq(java.lang.Long seq) {
		super.set("seq", seq);
		return this;
	}

	public java.lang.Long getLucky_id() {
		return this.get("lucky_id");
	}

	public LuckyDrawHistory setLucky_id(java.lang.Long lucky_id) {
		super.set("lucky_id", lucky_id);
		return this;
	}

	public java.lang.Long getOption_id() {
		return this.get("option_id");
	}

	public LuckyDrawHistory setOption_id(java.lang.Long option_id) {
		super.set("option_id", option_id);
		return this;
	}
}