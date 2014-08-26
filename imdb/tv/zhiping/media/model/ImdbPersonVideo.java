package tv.zhiping.media.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;
import tv.zhiping.media.model.ImdbEpisode.ImdbEpisodeType;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-17
 */
@SuppressWarnings("serial")
public class ImdbPersonVideo extends BasicModel<ImdbPersonVideo> {

	public static final ImdbPersonVideo dao = new ImdbPersonVideo();

	/**
	 * 匹配失败的重新匹配
	 */
	public void backFail2Wait() {
		StringBuilder sql = new StringBuilder("update "+tableName+" set match_state=? where status=? and match_state=?");
		List<Object> params = new ArrayList<Object>();
		params.add(Cons.THREAD_STATE_WAIT);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_FAIL);
		
		Db.use(Cons.DB_NAME_MEDIA).update(sql.toString(),params.toArray());
	}
	
	/**
	 * 分页查询
	 */
	public Page<ImdbPersonVideo> paginateWait(int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and match_state=?");
		params.add(Cons.THREAD_STATE_WAIT);
			
	
		sql.append("  order by video_id,person_id");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),params.toArray());
	}
	
	public ImdbPersonVideo queryByObj(String person_id,String video_id,String profession){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and person_id=? and video_id=? and profession=?";
		params.add(Cons.STATUS_VALID);
		params.add(person_id);
		params.add(video_id);
		params.add(profession);
		
		return findFirst(sql, params.toArray());
	}
	
	public java.lang.String getPerson_id(){
		return this.getStr("person_id");
	}
	
	public ImdbPersonVideo setPerson_id(java.lang.String person_id){
		super.set("person_id",person_id);
		return this;
	}
	public java.lang.String getVideo_id(){
		return this.getStr("video_id");
	}
	
	public ImdbPersonVideo setVideo_id(java.lang.String video_id){
		super.set("video_id",video_id);
		return this;
	}
	public java.lang.String getProfession(){
		return this.getStr("profession");
	}
	
	public ImdbPersonVideo setProfession(java.lang.String profession){
		super.set("profession",profession);
		return this;
	}
	public java.lang.String getCharacter_name(){
		return this.getStr("character_name");
	}
	
	public ImdbPersonVideo setCharacter_name(java.lang.String character_name){
		super.set("character_name",character_name);
		return this;
	}
	public java.lang.String getCharacter_name_en(){
		return this.getStr("character_name_en");
	}
	
	public ImdbPersonVideo setCharacter_name_en(java.lang.String character_name_en){
		super.set("character_name_en",character_name_en);
		return this;
	}
	public java.lang.String getCharacter_avatar(){
		return this.getStr("character_avatar");
	}
	
	public ImdbPersonVideo setCharacter_avatar(java.lang.String character_avatar){
		super.set("character_avatar",character_avatar);
		return this;
	}
	public java.lang.Boolean getIs_primary(){
		return this.getBoolean("is_primary");
	}
	
	public ImdbPersonVideo setIs_primary(java.lang.Boolean is_primary){
		super.set("is_primary",is_primary);
		return this;
	}
	
	public java.lang.String getCharacter_desc(){
		return this.getStr("character_desc");
	}
	
	public ImdbPersonVideo setCharacter_desc(java.lang.String character_desc){
		super.set("character_desc",character_desc);
		return this;
	}
	public java.lang.String getLev(){
		return this.getStr("lev");
	}
	
	public ImdbPersonVideo setLev(java.lang.String lev){
		super.set("lev",lev);
		return this;
	}
	public java.lang.String getJson_txt(){
		return this.getStr("json_txt");
	}
	
	public ImdbPersonVideo setJson_txt(java.lang.String json_txt){
		super.set("json_txt",json_txt);
		return this;
	}
	public java.lang.String getCharacter_avatar_mtime(){
		return this.getStr("character_avatar_mtime");
	}
	
	public ImdbPersonVideo setCharacter_avatar_mtime(java.lang.String character_avatar_mtime){
		super.set("character_avatar_mtime",character_avatar_mtime);
		return this;
	}
	public java.lang.Integer getMatch_state(){
		return this.getInt("match_state");
	}
	
	public ImdbPersonVideo setMatch_state(java.lang.Integer match_state){
		if(match_state!=null){
			super.set("match_state",match_state);
		}
		return this;
	}
	public java.lang.Long getMdm_id(){
		return this.getLong("mdm_id");
	}
	
	public ImdbPersonVideo setMdm_id(java.lang.Long mdm_id){
		if(mdm_id != null){
			super.set("mdm_id",mdm_id);
		}
		return this;
	}
	
	public java.lang.String getMsg(){
		return this.getStr("msg");
	}
	
	public ImdbPersonVideo setMsg(java.lang.String msg){
		if(StringUtils.isNoneBlank(msg)){
			if(msg.length()>200){
				msg = msg.substring(0,200);
			}
			super.set("msg",msg);
		}
		return this;
	}
}