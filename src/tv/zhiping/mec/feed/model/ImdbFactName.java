package tv.zhiping.mec.feed.model;

import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-17
 */
@SuppressWarnings("serial")
public class ImdbFactName extends BasicModel<ImdbFactName> {

	public static final ImdbFactName dao = new ImdbFactName();
	
	public ImdbFactName queryByObj(String fact_id, String name_id) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and fact_id=?");
		params.add(fact_id);
		
		sql.append(" and name_id=?");
		params.add(name_id);
		
		return findFirst(sql.toString(), params.toArray());
	}
	
	public java.lang.String getName_id(){
		return this.getStr("name_id");
	}
	
	public ImdbFactName setName_id(java.lang.String name_id){
		super.set("name_id",name_id);
		return this;
	}
	public java.lang.String getFact_id(){
		return this.getStr("fact_id");
	}
	
	public ImdbFactName setFact_id(java.lang.String fact_id){
		super.set("fact_id",fact_id);
		return this;
	}
	public java.lang.String getJson_txt(){
		return this.getStr("json_txt");
	}
	
	public ImdbFactName setJson_txt(java.lang.String json_txt){
		super.set("json_txt",json_txt);
		return this;
	}
}