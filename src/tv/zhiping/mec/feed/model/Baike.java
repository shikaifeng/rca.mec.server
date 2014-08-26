package tv.zhiping.mec.feed.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.jfinal.BasicModel;
import tv.zhiping.jfinal.SysBasicModel;
import tv.zhiping.mdm.model.Person;


/**
 * 百科
 * @author 作者
 * @version 1.0
 * @since 2014-05-20
 */
@SuppressWarnings("serial")
public class Baike extends SysBasicModel<Baike> {
	

	public static final Baike dao = new Baike();

	public Baike queryByProgramIdName(Long program_id, String title) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and program_id=? and title=?";
		params.add(Cons.STATUS_VALID);
		params.add(program_id);
		params.add(title);
		
		return findFirst(sql, params.toArray());
	}
	
	/**
	 * app端展示
	 * @param json
	 * @param baike
	 */
	public void parseBaikeFeedJson(JSONObject json) {
		json.put("cover",ComUtil.getStHttpPath(this.getCover()));
		json.put("title",this.getTitle());
		json.put("summary",this.getSummary());
		json.put("url",this.getSource_url());
		json.put("properties",null);//暂时没有该信息
	}

	/**
	 * app端展示 使用fact类型
	 * @param json
	 * @param baike
	 */
	public void parseFactBaikeFeedJson(JSONObject json) {
		json.put("title",this.getTitle());
		json.put("summary",this.getSummary());
	}

	
//	/**
//	 * 根据百科的url查询百科信息
//	 * @param name
//	 * @return
//	 */
//	public Baike queryBySourceUrl(String url){
//		List<Object> params = new ArrayList<Object>();
//		String sql = "select * from "+tableName+" where status=? and source_url=?";
//		params.add(Cons.STATUS_VALID);
//		params.add(url);
//		
//		return findFirst(sql, params.toArray());
//	}
	
	public java.lang.Long getProgram_id(){
		return this.getLong("program_id");
	}
	
	public Baike setProgram_id(java.lang.Long program_id){
		super.set("program_id",program_id);
		return this;
	}
	
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public Baike setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getCover(){
		return this.getStr("cover");
	}
	
	public Baike setCover(java.lang.String cover){
		super.set("cover",cover);
		return this;
	}
	public java.lang.String getSummary(){
		return this.getStr("summary");
	}
	
	public Baike setSummary(java.lang.String summary){
		super.set("summary",summary);
		return this;
	}
	public java.lang.String getSource_url(){
		return this.getStr("source_url");
	}
	
	public Baike setSource_url(java.lang.String source_url){
		super.set("source_url",source_url);
		return this;
	}
}
