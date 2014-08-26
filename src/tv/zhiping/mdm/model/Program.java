package tv.zhiping.mdm.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

import com.jfinal.plugin.activerecord.Page;

/**
 * 节目类 model
 * */
@SuppressWarnings("serial")
public class Program extends BasicModel<Program>{
	
	public static final Program dao = new Program();
	
	/**
	 * 分页查询
	 */
	public Page<Program> paginate(Program obj,int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		if(obj!=null){
			if(StringUtils.isNoneBlank(obj.getTitle())){
				sql.append(" and (title like ? or orig_title like ?)");
				params.add(LIKE+obj.getTitle()+LIKE);
				params.add(LIKE+obj.getTitle()+LIKE);
			}
			if(StringUtils.isNoneBlank(obj.getType())){
				sql.append(" and type=?");
				params.add(obj.getType());
			}
			if(StringUtils.isNoneBlank(obj.getYear())){
				sql.append(" and year=?");
				params.add(obj.getYear());
			}
		}
		
		sql.append("  order by type,updated_at asc");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),params.toArray());
	}
	
	/**
	 * 根据明星id,查询出其相关的program
	 */
	public List<Program> querryByPersonId(Long personId){
		List<Object> params = new ArrayList<Object>();
		
		String sql = "select p.* from person_program pp inner join program p on pp.program_id=p.id  where pp.person_id=? and pp.status=? and p.status=?  order by p.year desc";
		
		params.add(personId);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		
		return find(sql, params.toArray());
	}
	/**
	 * 根据节目名称,查询program
	 */
	public Program queryByTitle(String name) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where title = ? and status=?";
		params.add(name);
		params.add(Cons.STATUS_VALID);
		return findFirst(sql, params.toArray());
	}
	
//	/**
//	 * 先根据节目名称，季查询，
//	 * 如果不存在，再通过节目名称，年份查询
//	 */
//	public Program queryByTitleSeasonYear(String name_en,String season,String year) {
//		List<Object> params = new ArrayList<Object>();
//		String sql = "select * from "+tableName+" where (lower(orig_title) = ? or lower(title) = ?) and status=? and current_season=? order by year";
//		params.add(name_en.toLowerCase());
//		params.add(name_en.toLowerCase());
//		params.add(Cons.STATUS_VALID);
//		params.add(season);
//		Program obj =  findFirst(sql, params.toArray());
//		if(obj == null){
//			obj = queryByTitleYear(name_en,year);
//		}
//		return obj;
//	}
	
	
//	/**
//	 * 根据节目名称,查询program
//	 */
//	public Program queryByTitleYear(String name_en,String year) {
//		List<Object> params = new ArrayList<Object>();
//		String sql = "select * from "+tableName+" where (lower(orig_title) = ? or lower(title) = ?) and status=? and year=?";
//		params.add(name_en.toLowerCase());
//		params.add(name_en.toLowerCase());
//		params.add(Cons.STATUS_VALID);
//		params.add(year);
//		return findFirst(sql, params.toArray());
//	}
	
	/**
	 * 根据节目名称,查询program
	 */
	public Program queryByObj(Long program_id,String imdb_url,String title,String year,String type) {//String type
		Program res = null;
		if(program_id!=null && !Cons.DEF_NULL_NUMBER.equals(program_id)){
			res = findById(program_id);
			if(res!=null){
				return res;
			}
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(StringUtils.isNotBlank(imdb_url)){
			sql.append(" and imdb_url=?");
			params.add(imdb_url);
			
			res = findFirst(sql.toString(), params.toArray());
			if(res!=null){
				return res;
			}
		}
		
		
		params.clear();
		sql.setLength(0);
		
		sql.append("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and (lower(orig_title)=? or lower(title)=? or lower(aka)=? or lower(aka_en)=?) ");
		title = title.toLowerCase();
		params.add(title);
		params.add(title);
		params.add(title);
		params.add(title);
		
		sql.append(" and year=?");
		params.add(year);
		
		sql.append(" and type=?");
		params.add(type);
		
		return findFirst(sql.toString(), params.toArray());
	}
	
//	/**
//	 * 根据节目名称,查询program
//	 */
//	public Program queryByObj(String title,String year,String type) {//String type	
//		List<Object> params = new ArrayList<Object>();
//		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
//		params.add(Cons.STATUS_VALID);
//		
//		sql.append(" and (lower(orig_title)=? or lower(title)=? or lower(aka)=? or lower(aka_en)=?) ");
//		title = title.toLowerCase();
//		params.add(title);
//		params.add(title);
//		params.add(title);
//		params.add(title);
//		
//		sql.append(" and year=?");
//		params.add(year);
//		
//		sql.append(" and type=?");
//		params.add(type);
//		
//		return findFirst(sql.toString(), params.toArray());
//	}
	
	/**
	 * 获取中文标题
	 * @param title
	 * @param type
	 * @return
	 */
	public String queryByOrigTitleType(String title, String type) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and (lower(orig_title)=? or lower(title)=? or lower(aka)=? or lower(aka_en)=?) ");
		title = title.toLowerCase();
		params.add(title);
		params.add(title);
		params.add(title);
		params.add(title);
		
		
		sql.append(" and type=?");
		params.add(type);
		
		sql.append(" order by year limit 0,1");
		
		Program program = findFirst(sql.toString(), params.toArray());
		if(program!=null){
			return program.getTitle();
		}
		return null;
	}
	
	//类型字段
	public static enum ProgramType {
		Movie, TV,Variety
	}
		
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public Program setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getOrig_title(){
		return this.getStr("orig_title");
	}
	
	public Program setOrig_title(java.lang.String orig_title){
		super.set("orig_title",orig_title);
		return this;
	}
	public java.lang.String getAka(){
		return this.getStr("aka");
	}
	
	public Program setAka(java.lang.String aka){
		super.set("aka",aka);
		return this;
	}
	public java.lang.String getAka_en(){
		return this.getStr("aka_en");
	}
	
	public Program setAka_en(java.lang.String aka_en){
		super.set("aka_en",aka_en);
		return this;
	}
	public java.lang.String getCover(){
		return this.getStr("cover");
	}
	
	public Program setCover(java.lang.String cover){
		super.set("cover",cover);
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public Program setType(java.lang.String type){
		super.set("type",type);
		return this;
	}
	public java.lang.String getWebsite(){
		return this.getStr("website");
	}
	
	public Program setWebsite(java.lang.String website){
		super.set("website",website);
		return this;
	}
	public java.lang.String getYear(){
		return this.getStr("year");
	}
	
	public Program setYear(java.lang.String year){
		super.set("year",year);
		return this;
	}
	public java.lang.String getLanguages(){
		return this.getStr("languages");
	}
	
	public Program setLanguages(java.lang.String languages){
		super.set("languages",languages);
		return this;
	}
	public java.lang.String getCountry(){
		return this.getStr("country");
	}
	
	public Program setCountry(java.lang.String country){
		super.set("country",country);
		return this;
	}
	public java.lang.Long getPer_duration(){
		return this.getLong("per_duration");
	}
	
	public Program setPer_duration(java.lang.Long per_duration){
		super.set("per_duration",per_duration);
		return this;
	}
	public java.lang.String getGenres(){
		return this.getStr("genres");
	}
	
	public Program setGenres(java.lang.String genres){
		super.set("genres",genres);
		return this;
	}
	public java.lang.Long getSeasons_count(){
		return this.getLong("seasons_count");
	}
	
	public Program setSeasons_count(java.lang.Long seasons_count){
		super.set("seasons_count",seasons_count);
		return this;
	}
	public java.lang.Long getCurrent_season(){
		return this.getLong("current_season");
	}
	
	public Program setCurrent_season(java.lang.Long current_season){
		super.set("current_season",current_season);
		return this;
	}
	public java.lang.Long getEpisodes_count(){
		return this.getLong("episodes_count");
	}
	
	public Program setEpisodes_count(java.lang.Long episodes_count){
		super.set("episodes_count",episodes_count);
		return this;
	}
	public java.lang.String getMtime_url(){
		return this.getStr("mtime_url");
	}
	
	public Program setMtime_url(java.lang.String mtime_url){
		super.set("mtime_url",mtime_url);
		return this;
	}
	public java.lang.String getDouban_url(){
		return this.getStr("douban_url");
	}
	
	public Program setDouban_url(java.lang.String douban_url){
		super.set("douban_url",douban_url);
		return this;
	}
	public java.lang.String getImdb_url(){
		return this.getStr("imdb_url");
	}
	
	public Program setImdb_url(java.lang.String imdb_url){
		super.set("imdb_url",imdb_url);
		return this;
	}
	public java.lang.String getTags(){
		return this.getStr("tags");
	}
	
	public Program setTags(java.lang.String tags){
		super.set("tags",tags);
		return this;
	}
	public java.lang.String getImages(){
		return this.getStr("images");
	}
	
	public Program setImages(java.lang.String images){
		super.set("images",images);
		return this;
	}
	public java.lang.String getSummary(){
		return this.getStr("summary");
	}
	
	public Program setSummary(java.lang.String summary){
		super.set("summary",summary);
		return this;
	}
	public java.lang.String getPhotos(){
		return this.getStr("photos");
	}
	
	public Program setPhotos(java.lang.String photos){
		super.set("photos",photos);
		return this;
	}
	public java.lang.Float getMtime_score(){
		return this.getFloat("mtime_score");
	}
	
	public Program setMtime_score(java.lang.Float mtime_score){
		super.set("mtime_score",mtime_score);
		return this;
	}
	public java.lang.String getBaidu_url(){
		return this.getStr("baidu_url");
	}
	
	public Program setBaidu_url(java.lang.String baidu_url){
		super.set("baidu_url",baidu_url);
		return this;
	}
	public java.lang.String getWasu_url(){
		return this.getStr("wasu_url");
	}
	
	public Program setWasu_url(java.lang.String wasu_url){
		super.set("wasu_url",wasu_url);
		return this;
	}
	
	public String getView_cover(){
		String str = getCover();
		if(StringUtils.isBlank(str)){
			str = getImages();
		}
		return str;
	}
}
