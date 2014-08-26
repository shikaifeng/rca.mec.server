package tv.zhiping.mec.feed.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import tv.zhiping.common.Cons;
import tv.zhiping.common.EnumValueKeys;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.jfinal.BasicModel;
import tv.zhiping.jfinal.SysBasicModel;


/**
 * 媒资库中明星表
 * @author 樊亚容
 * @version 1.1
 * @since 2014-05-16
 */
@SuppressWarnings("serial")
public class MecPerson extends SysBasicModel<MecPerson> {
	public static final MecPerson dao = new MecPerson();
	
	/**
	 * 根据明星名称获取明星信息
	 * @param name
	 * @return
	 */
	public MecPerson queryByProgramIdName(Long program_id,String name,Long nid){
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=? and program_id=? and name=?");
		params.add(Cons.STATUS_VALID);
		params.add(program_id);
		params.add(name);
		
		if(nid!=null){
			sql.append(" and id !=?");
			params.add(nid);	
		}
		
		return findFirst(sql.toString(), params.toArray());
	}
	
	public java.lang.Long getProgram_id(){
		return this.getLong("program_id");
	}
	
	public MecPerson setProgram_id(java.lang.Long program_id){
		super.set("program_id",program_id);
		return this;
	}
	
	public java.lang.String getName(){
		return this.getStr("name");
	}
	
	public MecPerson setName(java.lang.String name){
		super.set("name",name);
		return this;
	}
	public java.lang.String getName_en(){
		return this.getStr("name_en");
	}
	
	public MecPerson setName_en(java.lang.String name_en){
		super.set("name_en",name_en);
		return this;
	}
	public java.lang.String getAka(){
		return this.getStr("aka");
	}
	
	public MecPerson setAka(java.lang.String aka){
		super.set("aka",aka);
		return this;
	}
	public java.lang.String getAka_en(){
		return this.getStr("aka_en");
	}
	
	public MecPerson setAka_en(java.lang.String aka_en){
		super.set("aka_en",aka_en);
		return this;
	}
	public java.lang.String getGender(){
		return this.getStr("gender");
	}
	
	public MecPerson setGender(java.lang.String gender){
		super.set("gender",gender);
		return this;
	}
	public java.lang.String getAvatar(){
		return this.getStr("avatar");
	}
	
	public MecPerson setAvatar(java.lang.String avatar){
		super.set("avatar",avatar);
		return this;
	}
	public java.lang.String getNickname(){
		return this.getStr("nickname");
	}
	
	public MecPerson setNickname(java.lang.String nickname){
		super.set("nickname",nickname);
		return this;
	}
	public java.util.Date getBirthday(){
		return this.getDate("birthday");
	}
	
	public MecPerson setBirthday(java.util.Date birthday){
		super.set("birthday",birthday);
		return this;
	}
	public java.lang.String getBorn_place(){
		return this.getStr("born_place");
	}
	
	public MecPerson setBorn_place(java.lang.String born_place){
		super.set("born_place",born_place);
		return this;
	}
	public java.lang.String getConstellation(){
		return this.getStr("constellation");
	}
	
	public MecPerson setConstellation(java.lang.String constellation){
		super.set("constellation",constellation);
		return this;
	}
	public java.lang.String getTags(){
		return this.getStr("tags");
	}
	
	public MecPerson setTags(java.lang.String tags){
		super.set("tags",tags);
		return this;
	}
	public java.lang.String getDescription(){
		return this.getStr("description");
	}
	
	public MecPerson setDescription(java.lang.String description){
		super.set("description",description);
		return this;
	}
	public java.lang.String getPhotos(){
		return this.getStr("photos");
	}
	
	public MecPerson setPhotos(java.lang.String photos){
		super.set("photos",photos);
		return this;
	}
	
	public java.lang.String getBaike_url(){
		return this.getStr("baike_url");
	}
	
	public MecPerson setBaike_url(java.lang.String baike_url){
		super.set("baike_url",baike_url);
		return this;
	}
	
	public java.lang.String getMtime_url(){
		return this.getStr("mtime_url");
	}
	
	public MecPerson setMtime_url(java.lang.String mtime_url){
		super.set("mtime_url",mtime_url);
		return this;
	}
	public java.lang.String getDouban_url(){
		return this.getStr("douban_url");
	}
	
	public MecPerson setDouban_url(java.lang.String douban_url){
		super.set("douban_url",douban_url);
		return this;
	}
	public java.lang.String getImdb_url(){
		return this.getStr("imdb_url");
	}
	
	public MecPerson setImdb_url(java.lang.String imdb_url){
		super.set("imdb_url",imdb_url);
		return this;
	}
	public java.lang.String getAvatar_mtime(){
		return this.getStr("avatar_mtime");
	}
	
	public MecPerson setAvatar_mtime(java.lang.String avatar_mtime){
		super.set("avatar_mtime",avatar_mtime);
		return this;
	}
	public java.lang.String getEducation(){
		return this.getStr("education");
	}
	
	public MecPerson setEducation(java.lang.String education){
		super.set("education",education);
		return this;
	}
	public java.lang.String getFamily(){
		return this.getStr("family");
	}
	
	public MecPerson setFamily(java.lang.String family){
		super.set("family",family);
		return this;
	}
	public java.lang.String getBlood_type(){
		return this.getStr("blood_type");
	}
	
	public MecPerson setBlood_type(java.lang.String blood_type){
		super.set("blood_type",blood_type);
		return this;
	}
	public java.lang.String getHeight(){
		return this.getStr("height");
	}
	
	public MecPerson setHeight(java.lang.String height){
		super.set("height",height);
		return this;
	}
	public java.lang.String getWeight(){
		return this.getStr("weight");
	}
	
	public MecPerson setWeight(java.lang.String weight){
		super.set("weight",weight);
		return this;
	}
	
	public java.lang.String getCountry(){
		return this.getStr("country");
	}
	
	public MecPerson setCountry(java.lang.String country){
		super.set("country",country);
		return this;
	}
	
	//返回视图层的头像
	public java.lang.String getView_avatar(){
		String str = getAvatar();
		if(StringUtils.isBlank(str)){
			str = getAvatar_mtime();			
		}
		return str;
	}

	public void parseMecPerson2BaikeJson(JSONObject json) {
		json.put("cover",ComUtil.getStHttpPath(this.getView_avatar()));
		json.put("title",this.getName());
		json.put("summary",this.getDescription());
		json.put("url",null);
	}
	
//	public java.lang.String getHuUrl(){
//		return "m/person/"+getId();
//	}
}