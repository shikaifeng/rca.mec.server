package tv.zhiping.mec.feed.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.common.EnumValueKeys;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.jfinal.BasicModel;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-17
 */
@SuppressWarnings("serial")
public class ImdbFact extends BasicModel<ImdbFact> {
	public static final ImdbFact dao = new ImdbFact();
	
	public List<ImdbFact> queryAll(){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? order by type,text";
		params.add(Cons.STATUS_VALID);
		
		return find(sql, params.toArray());
	}
	
	public ImdbFact queryByImdbId(String imdb_id){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and imdb_id=?";
		params.add(Cons.STATUS_VALID);
		params.add(imdb_id);
		
		return findFirst(sql, params.toArray());
	}
	
	/**
	 * app端显示使用
	 * @param json
	 * @param fact
	 */
	public void parseFactJson(JSONObject json) {
		String type =  CacheFun.getEnumValue(EnumValueKeys.IMDB_FACT_TYPE, this.getType());
		if(StringUtils.isBlank(type)){
			type = this.getType();
		}
		json.put("title",type);
		json.put("summary",this.getText());
	}
	
	
	public java.lang.String getImdb_id(){
		return this.getStr("imdb_id");
	}
	
	public ImdbFact setImdb_id(java.lang.String imdb_id){
		super.set("imdb_id",imdb_id);
		return this;
	}
	
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public ImdbFact setType(java.lang.String type){
		super.set("type",type);
		return this;
	}
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public ImdbFact setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getText_zh(){
		return this.getStr("text_zh");
	}
	
	public ImdbFact setText_zh(java.lang.String text_zh){
		super.set("text_zh",text_zh);
		return this;
	}
	
	public java.lang.String getText(){
		return this.getStr("text");
	}
	
	public ImdbFact setText(java.lang.String text){
		super.set("text",text);
		return this;
	}
	public java.lang.String getJson_txt(){
		return this.getStr("json_txt");
	}
	
	public ImdbFact setJson_txt(java.lang.String json_txt){
		super.set("json_txt",json_txt);
		return this;
	}
	public java.lang.String getPid(){
		return this.getStr("pid");
	}
	
	public ImdbFact setPid(java.lang.String pid){
		super.set("pid",pid);
		return this;
	}
}