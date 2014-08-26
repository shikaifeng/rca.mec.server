package tv.zhiping.media.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.NoIdModel;
import tv.zhiping.mdm.model.Person;
import tv.zhiping.mdm.model.Program.ProgramType;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;


/**
 * @author 张有良
 * @version 1.0
 * @since 2014-06-16
 */
@SuppressWarnings("serial")
public class ImdbPerson extends NoIdModel<ImdbPerson> {

	public static final Long ADD_MDM_ID_SIGN = -1L;
	
	public static final ImdbPerson dao = new ImdbPerson();
	
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
	
	public List<ImdbPerson> queryMatchErrorAll() {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" ");
		sql.append("where status=? and match_state=?");
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_FAIL);
		
		sql.append("  order by id asc");
		return find(sql.toString(),params.toArray());
	}
	
	/**
	 * 分页查询
	 * 已关联了电影的
	 */
	public Page<ImdbPerson> paginateByWiatMatch(int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("from "+tableName+" ");
		sql.append("where status=? and match_state=?");
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_WAIT);
		
		sql.append("  order by id asc");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),params.toArray());
	}
	
	
	/**
	 * 分页查询
	 * 已关联了电影的
	 */
	public Page<ImdbPerson> paginateByWiatMatchMovie(int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("from "+tableName+" a inner join imdb_person_video b inner join imdb_episode c");
		sql.append(" on a.id=b.person_id and b.video_id=c.id where a.status=? and b.status=? and c.status=? and c.type=? and a.match_state=? and  c.match_state=?");
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		params.add(ProgramType.Movie.toString());
		
		params.add(Cons.THREAD_STATE_WAIT);
		params.add(Cons.THREAD_STATE_SUC);
		
		sql.append("  order by id asc");
		return paginate(pageNumber, pageSize, "select a.*,c.mdm_program_id", sql.toString(),params.toArray());
	}
	
	public void parseImdb2Person(Person person) {
		person.setName(this.getName());
		person.setName_en(this.getName_en());
		
		person.setAka(getJsonAka());
		person.setAka_en(person.getAka());
		person.setGender(this.getGender());
		
		person.setAvatar_mtime(this.getR_avatar());
		person.setNickname(this.getNickname());
		if(this.getBirthday()!=null){
			person.setBirthday(this.getBirthday());
		}
		person.setBorn_place(this.getBorn_place());
		person.setConstellation(this.getConstellation());
		person.setTags(this.getTags());
		person.setDescription(this.getDescription());
		person.setPhotos(this.getPhotos());
		person.setEducation(this.getEducation());
		person.setFamily(this.getFamily());
		person.setBlood_type(this.getBlood_type());
		person.setHeight(this.getHeight());
		person.setWeight(this.getWeight());
		person.setCountry(this.getCountry());
		person.setImdb_url(this.getImdb_url());
	}
	
	public void parseUpdPerson2Imdb(Person person) {
		person.setImdb_url(this.getImdb_url());
		if(StringUtils.isBlank(person.getName_en()) && StringUtils.isNotBlank(this.getName_en())){
			person.setName_en(this.getName_en());
		}
		
		if(StringUtils.isBlank(person.getAka()) && StringUtils.isNotBlank(this.getAka())){
			String str = this.getJsonAka();
			person.setAka(str);
		}
		
		if(StringUtils.isBlank(person.getAka_en()) && StringUtils.isNotBlank(this.getAka_en())){
			String str = this.getJsonAka();
			person.setAka_en(str);
		}
		if(StringUtils.isNotBlank(person.getAka()) && person.getAka().startsWith("[")){//["Tom Hildreth"] json数组转换成字符串
			String str = this.getJsonAka();
			person.setAka(str);
		}
		if(StringUtils.isNotBlank(person.getAka_en()) && person.getAka_en().startsWith("[")){//["Tom Hildreth"] json数组转换成字符串
			String str = this.getJsonAka();
			person.setAka_en(str);
		}
		
		if(StringUtils.isBlank(person.getGender()) && StringUtils.isNotBlank(this.getGender())){
			person.setGender(this.getGender());
		}
		
		if(person.getBirthday()==null && this.getBirthday()!=null){
			person.setBirthday(this.getBirthday());
		}
		
		if(StringUtils.isBlank(person.getNickname()) && StringUtils.isNotBlank(this.getNickname())){
			String str = this.getJsonNickname();
			person.setNickname(str);
		}
		
		if(StringUtils.isNotBlank(person.getNickname()) && person.getNickname().startsWith("[")){//["Tom Hildreth"] json数组转换成字符串
			String str = this.getJsonNickname();
			person.setNickname(str);
		}
		
		if(StringUtils.isBlank(person.getBorn_place()) && StringUtils.isNotBlank(this.getBorn_place())){
			person.setBorn_place(this.getBorn_place());
		}
		
		if(StringUtils.isBlank(person.getConstellation()) && StringUtils.isNotBlank(this.getConstellation())){
			person.setConstellation(this.getConstellation());
		}
		
		if(StringUtils.isBlank(person.getTags()) && StringUtils.isNotBlank(this.getTags())){
			person.setTags(this.getTags());
		}
		
		if(StringUtils.isBlank(person.getDescription()) && StringUtils.isNotBlank(this.getDescription())){
			person.setDescription(this.getDescription());
		}
		
		if(StringUtils.isBlank(person.getPhotos()) && StringUtils.isNotBlank(this.getPhotos())){
			person.setPhotos(this.getPhotos());
		}
		
		if(StringUtils.isBlank(person.getAvatar()) && StringUtils.isBlank(person.getAvatar_mtime()) && StringUtils.isNoneBlank(this.getR_avatar())){
			person.setAvatar_mtime(this.getR_avatar());
		}
		
		if(StringUtils.isBlank(person.getEducation()) && StringUtils.isNotBlank(this.getEducation())){
			person.setEducation(this.getEducation());
		}
		
		if(StringUtils.isBlank(person.getFamily()) && StringUtils.isNotBlank(this.getFamily())){
			person.setFamily(this.getFamily());
		}
		
		if(StringUtils.isBlank(person.getBlood_type()) && StringUtils.isNotBlank(this.getBlood_type())){
			person.setBlood_type(this.getBlood_type());
		}
		
		if(StringUtils.isBlank(person.getHeight()) && StringUtils.isNotBlank(this.getHeight())){
			person.setHeight(this.getHeight());
		}
		
		if(StringUtils.isBlank(person.getWeight()) && StringUtils.isNotBlank(this.getWeight())){
			person.setWeight(this.getWeight());
		}
		
		if(StringUtils.isBlank(person.getCountry()) && StringUtils.isNotBlank(this.getCountry())){
			person.setCountry(this.getCountry());
		}
	}
	
	
	public String getJsonAka(){
		String str = getAka();
		StringBuilder res = new StringBuilder();
		if(StringUtils.isNotBlank(str)){
			JSONArray array = JSONArray.parseArray(str);
			int size = array.size();
			if(size>5){
				size = 5;
			}
			for(int i=0;i<size;i++){
				res.append(array.get(i));
				if((i+1)!=size){
					res.append("/");
				}
			}
		}
		
		return res.toString();
	}
	
	public String getJsonNickname(){
		String str = getNickname();
		StringBuilder res = new StringBuilder();
		if(StringUtils.isNotBlank(str)){
			JSONArray array = JSONArray.parseArray(str);
			int size = array.size();
			if(size>5){
				size = 5;
			}
			for(int i=0;i<size;i++){
				res.append(array.get(i));
				if((i+1)!=size){
					res.append("/");
				}
			}
		}
		
		return res.toString();
	}
	
	
	public java.lang.String getId(){
		return this.getStr("id");
	}
	
	public ImdbPerson setId(java.lang.String id){
		super.set("id",id);
		return this;
	}
	
	public java.lang.String getName(){
		return this.getStr("name");
	}
	
	public ImdbPerson setName(java.lang.String name){
		if(StringUtils.isNotBlank(name)){
			super.set("name",name);
		}
		return this;
	}
	public java.lang.String getName_en(){
		return this.getStr("name_en");
	}
	
	public ImdbPerson setName_en(java.lang.String name_en){
		if(StringUtils.isNotBlank(name_en)){
			super.set("name_en",name_en);
		}
		return this;
	}
	public java.lang.String getAka(){
		return this.getStr("aka");
	}
	
	public ImdbPerson setAka(java.lang.String aka){
		if(StringUtils.isNotBlank(aka)){
			super.set("aka",aka);
		}
		return this;
	}
	public java.lang.String getAka_en(){
		return this.getStr("aka_en");
	}
	
	public ImdbPerson setAka_en(java.lang.String aka_en){
		if(StringUtils.isNotBlank(aka_en)){
			super.set("aka_en",aka_en);
		}
		return this;
	}
	public java.lang.String getReal_name(){
		return this.getStr("real_name");
	}
	
	public ImdbPerson setReal_name(java.lang.String real_name){
		if(StringUtils.isNotBlank(real_name)){
			super.set("real_name",real_name);
		}
		return this;
	}
	public java.lang.String getGender(){
		return this.getStr("gender");
	}
	
	public ImdbPerson setGender(java.lang.String gender){
		if(StringUtils.isNotBlank(gender)){
			super.set("gender",gender);
		}
		return this;
	}
	public java.lang.String getAvatar(){
		return this.getStr("avatar");
	}
	
	public ImdbPerson setAvatar(java.lang.String avatar){
		if(StringUtils.isNotBlank(avatar)){
			super.set("avatar",avatar);
		}
		return this;
	}
	public java.lang.String getNickname(){
		return this.getStr("nickname");
	}
	
	public ImdbPerson setNickname(java.lang.String nickname){
		if(StringUtils.isNotBlank(nickname)){
			super.set("nickname",nickname);
		}
		return this;
	}
	public java.util.Date getBirthday(){
		return this.getDate("birthday");
	}
	
	public ImdbPerson setBirthday(java.sql.Timestamp birthday){
		if(birthday!=null){
			super.set("birthday",birthday);
		}
		return this;
	}
	public java.lang.String getBorn_place(){
		return this.getStr("born_place");
	}
	
	public ImdbPerson setBorn_place(java.lang.String born_place){
		if(StringUtils.isNotBlank(born_place)){
			super.set("born_place",born_place);
		}
		return this;
	}
	public java.lang.String getConstellation(){
		return this.getStr("constellation");
	}
	
	public ImdbPerson setConstellation(java.lang.String constellation){
		if(StringUtils.isNotBlank(constellation)){
			super.set("constellation",constellation);
		}
		return this;
	}
	public java.lang.String getTags(){
		return this.getStr("tags");
	}
	
	public ImdbPerson setTags(java.lang.String tags){
		if(StringUtils.isNotBlank(tags)){
			super.set("tags",tags);
		}
		return this;
	}
	public java.lang.String getDescription(){
		return this.getStr("description");
	}
	
	public ImdbPerson setDescription(java.lang.String description){
		if(StringUtils.isNotBlank(description)){
			super.set("description",description);
		}
		return this;
	}
	public java.lang.String getPhotos(){
		return this.getStr("photos");
	}
	
	public ImdbPerson setPhotos(java.lang.String photos){
		if(StringUtils.isNotBlank(photos)){
			super.set("photos",photos);
		}
		return this;
	}
	public java.lang.String getR_avatar(){
		return this.getStr("r_avatar");
	}
	
	public ImdbPerson setR_avatar(java.lang.String r_avatar){
		if(StringUtils.isNotBlank(r_avatar)){
			super.set("r_avatar",r_avatar);
		}
		return this;
	}
	public java.lang.String getEducation(){
		return this.getStr("education");
	}
	
	public ImdbPerson setEducation(java.lang.String education){
		if(StringUtils.isNotBlank(education)){
			super.set("education",education);
		}
		return this;
	}
	public java.lang.String getFamily(){
		return this.getStr("family");
	}
	
	public ImdbPerson setFamily(java.lang.String family){
		if(StringUtils.isNotBlank(family)){
			super.set("family",family);
		}
		return this;
	}
	public java.lang.String getBlood_type(){
		return this.getStr("blood_type");
	}
	
	public ImdbPerson setBlood_type(java.lang.String blood_type){
		if(StringUtils.isNotBlank(blood_type)){
			super.set("blood_type",blood_type);
		}
		return this;
	}
	public java.lang.String getHeight(){
		return this.getStr("height");
	}
	
	public ImdbPerson setHeight(java.lang.String height){
		if(StringUtils.isNotBlank(height)){
			super.set("height",height);
		}
		return this;
	}
	public java.lang.String getWeight(){
		return this.getStr("weight");
	}
	
	public ImdbPerson setWeight(java.lang.String weight){
		if(StringUtils.isNotBlank(weight)){
			super.set("weight",weight);
		}
		return this;
	}
	public java.lang.String getCountry(){
		return this.getStr("country");
	}
	
	public ImdbPerson setCountry(java.lang.String country){
		if(StringUtils.isNotBlank(country)){
			super.set("country",country);
		}
		return this;
	}
	public java.lang.String getImdb_url(){
		return this.getStr("imdb_url");
	}
	
	public ImdbPerson setImdb_url(java.lang.String imdb_url){
		if(StringUtils.isNotBlank(imdb_url)){
			super.set("imdb_url",imdb_url);
		}
		return this;
	}
	public java.lang.String getJson_txt(){
		return this.getStr("json_txt");
	}
	
	public ImdbPerson setJson_txt(java.lang.String json_txt){
		if(StringUtils.isNotBlank(json_txt)){
			super.set("json_txt",json_txt);
		}
		return this;
	}
	public java.lang.Integer getMatch_state(){
		return this.getInt("match_state");
	}
	
	public ImdbPerson setMatch_state(java.lang.Integer match_state){
		if(match_state!=null){
			super.set("match_state",match_state);
		}
		return this;
	}
	public java.lang.Long getMdm_id(){
		return this.getLong("mdm_id");
	}
	
	public ImdbPerson setMdm_id(java.lang.Long mdm_id){
		if(mdm_id != null){
			super.set("mdm_id",mdm_id);
		}
		return this;
	}
	
	public java.lang.String getMsg(){
		return this.getStr("msg");
	}
	
	public ImdbPerson setMatch_type(java.lang.String match_type){
		if(StringUtils.isNotBlank(match_type)){
			super.set("match_type",match_type);
		}
		return this;
	}

	public java.lang.String getMatch_type(){
		return this.getStr("match_type");
	}
	
	/**
	 * 1: program
	 * 2: episode 
	 * 3: event
	 * 4: factRelateName
	 * @param source
	 * @return
	 */
	public ImdbPerson setSource(java.lang.Integer source){
		if(source!=null){
			super.set("source",source);
		}
		return this;
	}

	public java.lang.Integer getSource(){
		return this.getInt("source");
	}
	
	public ImdbPerson setMsg(java.lang.String msg){
		if(StringUtils.isNotBlank(msg)){
			if(msg.length()>200){
				msg = msg.substring(0,200);
			}
			super.set("msg",msg);
		}
		return this;
	}
}