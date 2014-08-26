package tv.zhiping.mdm.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.jfinal.BasicModel;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;


/**
 * 媒资库中明星表
 * @author 樊亚容
 * @version 1.1
 * @since 2014-05-16
 */
@SuppressWarnings("serial")
public class Person extends BasicModel<Person> {

	public static final Person dao = new Person();
	

	/**
	 * 分页查询
	 */
	public Page<Person> paginate(Person obj,int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		if(obj!=null){
			if(StringUtils.isNoneBlank(obj.getName())){
				sql.append(" and (name like ? or name_en like ?)");
				params.add(LIKE+obj.getName()+LIKE);
				params.add(LIKE+obj.getName()+LIKE);
			}
			if(StringUtils.isNoneBlank(obj.getGender())){
				sql.append(" and gender=?");
				params.add(obj.getGender());
			}
			if(StringUtils.isNoneBlank(obj.getMtime_url())){
				sql.append(" and mtime_url like ?");
				params.add(LIKE+obj.getMtime_url()+LIKE);
			}
			if(StringUtils.isNoneBlank(obj.getImdb_url())){
				sql.append(" and imdb_url like ?");
				params.add(LIKE+obj.getImdb_url()+LIKE);
			}
		}
		
		sql.append("  order by name");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),params.toArray());
	}
	
	
	/**
	 * 根据节目id和明星名称获取明星信息
	 * @param name
	 * @return
	 */
	public List<Person> queryByProgramIdName(Long program_id,String name){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and name=?";
		
		sql = "select p.* from person_program pp inner join person p on pp.person_id=p.id where p.status=? and pp.status=? and pp.program_id=? and p.name=?";
				
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		
		params.add(program_id);
		params.add(name);
		
		return find(sql, params.toArray());
	}
	
	/**
	 * 根据明星名称获取明星信息
	 * @param name
	 * @return
	 */
	public List<Person> queryByName(String name){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and name=?";
		params.add(Cons.STATUS_VALID);	
		params.add(name);
		return find(sql, params.toArray());
	}
	
	/**
	 * 根据明星名称获取明星信息
	 * @param name
	 * @return
	 */
	public List<Person> queryByIdOrImdbUrlOrNameEn(Long id,String imdb_url,String name){
		if(id!=null && !Cons.DEF_NULL_NUMBER.equals(id)){//优先用id
			List<Person> result = new ArrayList<Person>();
			Person person = findById(id);
			if(person!=null){
				result.add(person);
				return result;
			}
		}
		
		if(StringUtils.isNotBlank(imdb_url)){//再使用imdb_url
			List<Person> result = queryByImdbUrl(imdb_url);
			if(result !=null && !result.isEmpty()){
				return result;
			}
		}

		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);	
		sql.append(" and (name=? or lower(name_en)=?)");
		params.add(name.toLowerCase());
		params.add(name.toLowerCase());
		sql.append(" and (imdb_url is null or imdb_url!='')");	//代表没有被匹配过的
		
		return find(sql.toString(), params.toArray());
	}
	
	public List<Person> queryByImdbUrl(String imdb_url){
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);	
		sql.append(" and imdb_url=?");
		params.add(imdb_url);
		
		return find(sql.toString(), params.toArray());
	}
	
	
	/**
	 * 根据节目id和明星的英文名称获取明星信息
	 * @param name
	 * @return
	 */
	public List<Person> queryByProgramIdNameEn(Long program_id,String name_en){
		List<Object> params = new ArrayList<Object>();
		String sql = "select distinct p.* from person_program pp inner join person p on pp.person_id=p.id where p.status=? and pp.status=? and pp.program_id=? and (lower(p.name_en)=? or name=?)";

		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		
		params.add(program_id);
		params.add(name_en.toLowerCase());//名称统一小写去查
		params.add(name_en);//名称统一小写去查
		
		return find(sql, params.toArray());
	}
	
//	//根据名称,生日的年查询用户信息
//	public List<Person> matchPersonByNmaeYear(String name,String year) {
//		List<Object> params = new ArrayList<Object>();
//		String sql = "select * from "+tableName+" where status=? and (lower(name_en)=? or name=?) and substr(birthday,1,4)=?";
//
//		params.add(Cons.STATUS_VALID);
//		
//		params.add(name.toLowerCase());//名称统一小写去查
//		params.add(name);
//		
//		params.add(year);
//		return find(sql, params.toArray());
//	}
	
	
	//根据节目id，名称查询用户信息
	public List<Person> queryProgramIdName(Long program_id, String name) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select distinct b.id as id from person_program a,"+tableName+" b where a.person_id=b.id and a.status=? and b.status=? and a.program_id=? and (lower(b.name_en)=? or b.name=?)";

		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		
		params.add(program_id);
		params.add(name.toLowerCase());//名称统一小写去查
		params.add(name);
		
		return find(sql, params.toArray());
	}
	
//	/**
//	 * 查询某节目中的所有明星,含明星信息
//	 * @return 
//	 */
//	public List<Person> queryPersonByPeogramIdHost(Long program_id) {
//		List<Object> params = new ArrayList<Object>();
//		
//		String sql = "select p.*,pp.character_name from "+tableName+" p inner join person_program pp on p.id=pp.person_id where pp.program_id=? and p.status=? and pp.status=? and pp.profession=? order by is_primary desc,pp.id";
//
//		params.add(program_id);
//		params.add(Cons.STATUS_VALID);
//		params.add(Cons.STATUS_VALID);
//		params.add(PersonType.host.toString());
//		
//		return find(sql, params.toArray());			
//	}
	
	/**
	 * 查询某节目中的所有明星,含明星信息
	 * @return 
	 */
	public List<Person> queryPersonByPeogramId(Long program_id) {
		List<Object> params = new ArrayList<Object>();
		
		String sql = "select p.*,pp.character_name,pp.profession from "+tableName+" p inner join person_program pp on p.id=pp.person_id where pp.program_id=? and p.status=? and pp.status=? order by profession desc,is_primary desc,pp.id";

		params.add(program_id);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		
		return find(sql, params.toArray());			
	}
	
	
	/**
	 * 查询某节目中的所有明星,含明星信息
	 * @return 
	 */
	public List<Person> queryPersonByPeogramIdActorHost(Long program_id) {
		List<Object> params = new ArrayList<Object>();
		
		String sql = "select p.*,pp.character_name from "+tableName+" p inner join person_program pp on p.id=pp.person_id where pp.program_id=? and p.status=? and pp.status=? and (pp.profession=? or pp.profession=?) order by profession desc,is_primary desc,pp.id";

		params.add(program_id);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		params.add(PersonType.actor.toString());
		params.add(PersonType.host.toString());
		
		return find(sql, params.toArray());			
	}
	
	/**
	 * 查询某节目中的所有明星,含明星信息
	 * @return 
	 */
	public List<Person> queryPersonByIds(Long program_id,List ids) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select pp.id as person_program_id,pp.profession,pp.character_name,pp.is_primary,p.* from ");
		sql.append(tableName);
		sql.append(" p inner join person_program pp on p.id=pp.person_id where pp.program_id=? and p.status=? and pp.status=? and p.id in #iterate# order by pp.is_primary desc,pp.id");
		params.add(program_id);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
//		params.add(PersonType.actor.toString());
//		params.add(PersonType.host.toString());

		return getIteratorSqlIdsParam(sql,ids,params);
	}
	
	/**
	 * 转换成json
	 * @param json
	 */
	public void parse2Json(JSONObject json) {
		json.put("id",this.getId());
		json.put("cover",null);
		json.put("avatar",ComUtil.getStHttpPath(this.getView_avatar()));
		json.put("name",this.getName());
		json.put("character",this.get("character_name"));					
		json.put("url",ComUtil.getHuHttpPath(this.getHuUrl()));
		json.put("start_time",null);
		json.put("end_time",null);
	}
	
	/**
	 * 判断是否有详情
	 * @return
	 */
	public boolean isNoHaveDetail(){
		boolean flag = false;
		
		if(StringUtils.isNotBlank(this.getNickname())){
			flag = true;
		}
		if(this.getBirthday()!=null){
			flag = true;
		}
		if(StringUtils.isNotBlank(this.getBorn_place())){
			flag = true;
		}
		if(StringUtils.isNotBlank(this.getConstellation())){
			flag = true;
		}
		if(StringUtils.isNotBlank(this.getTags())){
			flag = true;
		}
		if(StringUtils.isNotBlank(this.getDescription())){
			flag = true;
		}
		
		if(StringUtils.isNotBlank(this.getPhotos())){
			flag = true;
		}
		if(StringUtils.isNotBlank(this.getEducation())){
			flag = true;
		}
		
		if(StringUtils.isNotBlank(this.getFamily())){
			flag = true;
		}
		if(StringUtils.isNotBlank(this.getBlood_type())){
			flag = true;
		}
		if(StringUtils.isNotBlank(this.getHeight())){
			flag = true;
		}
		if(StringUtils.isNotBlank(this.getWeight())){
			flag = true;
		}
		if(StringUtils.isNotBlank(this.getCountry())){
			flag = true;
		}
		return flag;
	}
	
	
	//类型字段 host:主持人 actor:演员
	public static enum PersonType {
		actor,director,writer,host
	}
	
	public java.lang.String getName(){
		return this.getStr("name");
	}
	
	public Person setName(java.lang.String name){
		super.set("name",name);
		return this;
	}
	public java.lang.String getName_en(){
		return this.getStr("name_en");
	}
	
	public Person setName_en(java.lang.String name_en){
		super.set("name_en",name_en);
		return this;
	}
	public java.lang.String getAka(){
		return this.getStr("aka");
	}
	
	public Person setAka(java.lang.String aka){
		super.set("aka",aka);
		return this;
	}
	public java.lang.String getAka_en(){
		return this.getStr("aka_en");
	}
	
	public Person setAka_en(java.lang.String aka_en){
		super.set("aka_en",aka_en);
		return this;
	}
	public java.lang.String getGender(){
		return this.getStr("gender");
	}
	
	public Person setGender(java.lang.String gender){
		super.set("gender",gender);
		return this;
	}
	public java.lang.String getAvatar(){
		return this.getStr("avatar");
	}
	
	public Person setAvatar(java.lang.String avatar){
		super.set("avatar",avatar);
		return this;
	}
	public java.lang.String getNickname(){
		return this.getStr("nickname");
	}
	
	public Person setNickname(java.lang.String nickname){
		super.set("nickname",nickname);
		return this;
	}
	public java.util.Date getBirthday(){
		return this.getDate("birthday");
	}
	
	public Person setBirthday(java.util.Date birthday){
		super.set("birthday",birthday);
		return this;
	}
	public java.lang.String getBorn_place(){
		return this.getStr("born_place");
	}
	
	public Person setBorn_place(java.lang.String born_place){
		super.set("born_place",born_place);
		return this;
	}
	public java.lang.String getConstellation(){
		return this.getStr("constellation");
	}
	
	public Person setConstellation(java.lang.String constellation){
		super.set("constellation",constellation);
		return this;
	}
	public java.lang.String getTags(){
		return this.getStr("tags");
	}
	
	public Person setTags(java.lang.String tags){
		super.set("tags",tags);
		return this;
	}
	public java.lang.String getDescription(){
		return this.getStr("description");
	}
	
	public Person setDescription(java.lang.String description){
		super.set("description",description);
		return this;
	}
	public java.lang.String getPhotos(){
		return this.getStr("photos");
	}
	
	public Person setPhotos(java.lang.String photos){
		super.set("photos",photos);
		return this;
	}
	public java.lang.String getMtime_url(){
		return this.getStr("mtime_url");
	}
	
	public Person setMtime_url(java.lang.String mtime_url){
		super.set("mtime_url",mtime_url);
		return this;
	}
	public java.lang.String getDouban_url(){
		return this.getStr("douban_url");
	}
	
	public Person setDouban_url(java.lang.String douban_url){
		super.set("douban_url",douban_url);
		return this;
	}
	public java.lang.String getImdb_url(){
		return this.getStr("imdb_url");
	}
	
	public Person setImdb_url(java.lang.String imdb_url){
		super.set("imdb_url",imdb_url);
		return this;
	}
	public java.lang.String getAvatar_mtime(){
		return this.getStr("avatar_mtime");
	}
	
	public Person setAvatar_mtime(java.lang.String avatar_mtime){
		super.set("avatar_mtime",avatar_mtime);
		return this;
	}
	public java.lang.String getEducation(){
		return this.getStr("education");
	}
	
	public Person setEducation(java.lang.String education){
		super.set("education",education);
		return this;
	}
	public java.lang.String getFamily(){
		return this.getStr("family");
	}
	
	public Person setFamily(java.lang.String family){
		super.set("family",family);
		return this;
	}
	public java.lang.String getBlood_type(){
		return this.getStr("blood_type");
	}
	
	public Person setBlood_type(java.lang.String blood_type){
		super.set("blood_type",blood_type);
		return this;
	}
	public java.lang.String getHeight(){
		return this.getStr("height");
	}
	
	public Person setHeight(java.lang.String height){
		super.set("height",height);
		return this;
	}
	public java.lang.String getWeight(){
		return this.getStr("weight");
	}
	
	public Person setWeight(java.lang.String weight){
		super.set("weight",weight);
		return this;
	}
	
	public java.lang.String getCountry(){
		return this.getStr("country");
	}
	
	public Person setCountry(java.lang.String country){
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
	
	
	
	public java.lang.String getHuUrl(){
		return "m/person/"+getId();
	}
}