package tv.zhiping.mdm.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.common.cache.CacheKey;
import tv.zhiping.jfinal.BasicModel;
import tv.zhiping.redis.Redis;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-05-17
 */
@SuppressWarnings("serial")
public class Episode extends BasicModel<Episode> {
	public static final Episode dao = new Episode();
	
	
	public Episode findById(Object id) {
		String key = CacheKey.EPISODE_ID+id;
		Episode obj = Redis.get(key);
		if(obj == null){
			obj = super.findById(id);
			Redis.setex(key,obj,CacheKey.EPISODE_ID_EXPIRY);
		}
		return obj;
	}
	
	public boolean save() {
		boolean isSave = super.save();
        
		String key = CacheKey.EPISODE_ID+getId();
		Redis.del(key);
		
        return isSave;
    }
 
	
	public boolean update() {
		boolean isUpdate = super.update();
        
		String key = CacheKey.EPISODE_ID+getId();
		Redis.del(key);
		
        return isUpdate;
    }

	/**
	 * 根据节目id,剧集数查询
	 */
	public Episode queryByPidcurrentEpisodea(Long pid,Long current_episode) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=? and pid=? and current_episode=?");
		params.add(Cons.STATUS_VALID);
		params.add(pid);
		params.add(current_episode);
		
		return findFirst(sql.toString(), params.toArray());
	}
	
//	/**
//	 * 根据imdb 节目id,剧集数查询
//	 */
//	public Episode queryByImdbUrlPidcurrentEpisode(String imdb_url,Long pid,Long current_episode) {
//		List<Object> params = new ArrayList<Object>();
//		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=? and imdb_url=?");
//		params.add(Cons.STATUS_VALID);
//		params.add(imdb_url);
//		
//		Episode obj = findFirst(sql.toString(), params.toArray());
//		if(obj == null){
//			obj = queryByPidcurrentEpisode(pid, current_episode);
//		}
//		return obj;
//	}
	
	/**
	 * 根据节目id,剧集数查询
	 */
	public Long queryCountByPid(Long pid) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select count(1) as count from "+tableName+" where status=? and pid=? ");
		params.add(Cons.STATUS_VALID);
		params.add(pid);
		Episode obj = findFirst(sql.toString(), params.toArray());
		if(obj!=null){
			return obj.getLong("count");
		}
		return null;
	}
	
	/**
	 * 根据节目id,剧集数查询
	 */
	public List<Episode> queryByPid(Long pid) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=? and pid=? ");
		params.add(Cons.STATUS_VALID);
		params.add(pid);
		sql.append(" order by current_episode desc,series desc");
		
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * 根据节目id,剧集数查询
	 */
	public Episode queryMovieByPid(Long pid) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=? and pid=? ");
		params.add(Cons.STATUS_VALID);
		params.add(pid);
		
		return findFirst(sql.toString(), params.toArray());
	}
	
	/**
	 * 根据英文名称获取节目信息
	 * @param srr
	 * @return
	 */
	public List<Episode> queryByNameEns(String[] srr) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select b.* from "+tableName+" b,program a where a.id=b.pid and a.status=? and b.status=?");
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		sql.append(" and a.orig_title in ");
		getSqlIdsParam(sql,srr,params);
		sql.append(" order by a.title,a.current_season,b.current_episode");
		return find(sql.toString(), params.toArray());
	}
	
	
//	/**
//	 * 根据节目id查询条数
//	 */
//	public Episode queryCountByPid(Long pid) {
//		List<Object> params = new ArrayList<Object>();
//		StringBuilder sql = new StringBuilder("select count(1) as count from "+tableName+" where status=? and pid=?");
//		params.add(Cons.STATUS_VALID);
//		params.add(pid);
//		
//		return findFirst(sql.toString(), params.toArray());
//	}
	
	
	public java.lang.Long getPid(){
		return this.getLong("pid");
	}
	
	public Episode setPid(java.lang.Long pid){
		super.set("pid",pid);
		return this;
	}
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public Episode setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getOrig_title(){
		return this.getStr("orig_title");
	}
	
	public Episode setOrig_title(java.lang.String orig_title){
		super.set("orig_title",orig_title);
		return this;
	}
	public java.lang.String getAka(){
		return this.getStr("aka");
	}
	
	public Episode setAka(java.lang.String aka){
		super.set("aka",aka);
		return this;
	}
	public java.lang.String getCover(){
		return this.getStr("cover");
	}
	
	public Episode setCover(java.lang.String cover){
		super.set("cover",cover);
		return this;
	}
	public java.lang.String getSubtype(){
		return this.getStr("subtype");
	}
	
	public Episode setSubtype(java.lang.String subtype){
		super.set("subtype",subtype);
		return this;
	}
	public java.lang.String getWebsite(){
		return this.getStr("website");
	}
	
	public Episode setWebsite(java.lang.String website){
		super.set("website",website);
		return this;
	}
	public java.lang.String getYear(){
		return this.getStr("year");
	}
	
	public Episode setYear(java.lang.String year){
		super.set("year",year);
		return this;
	}
	public java.lang.String getLanguages(){
		return this.getStr("languages");
	}
	
	public Episode setLanguages(java.lang.String languages){
		super.set("languages",languages);
		return this;
	}
	public java.lang.String getCountry(){
		return this.getStr("country");
	}
	
	public Episode setCountry(java.lang.String country){
		super.set("country",country);
		return this;
	}
	public java.lang.Long getEpisodes_count(){
		return this.getLong("episodes_count");
	}
	
	public Episode setEpisodes_count(java.lang.Long episodes_count){
		super.set("episodes_count",episodes_count);
		return this;
	}
	public java.lang.Long getCurrent_episode(){
		return this.getLong("current_episode");
	}
	
	public Episode setCurrent_episode(java.lang.Long current_episode){
		super.set("current_episode",current_episode);
		return this;
	}
	public java.lang.Long getDuration(){
		return this.getLong("duration");
	}
	
	public Episode setDuration(java.lang.Long duration){
		super.set("duration",duration);
		return this;
	}
	public java.lang.String getTags(){
		return this.getStr("tags");
	}
	
	public Episode setTags(java.lang.String tags){
		super.set("tags",tags);
		return this;
	}
	public java.lang.String getImages(){
		return this.getStr("images");
	}
	
	public Episode setImages(java.lang.String images){
		super.set("images",images);
		return this;
	}
	public java.lang.String getSummary(){
		return this.getStr("summary");
	}
	
	public Episode setSummary(java.lang.String summary){
		super.set("summary",summary);
		return this;
	}
	public java.lang.String getPhotos(){
		return this.getStr("photos");
	}
	
	public Episode setPhotos(java.lang.String photos){
		super.set("photos",photos);
		return this;
	}
	
	public java.lang.String getSeries(){
		return this.getStr("series");
	}
	
	public Episode setSeries(java.lang.String series){
		super.set("series",series);
		return this;
	}
	
	public java.lang.String getImdb_url(){
		return this.getStr("imdb_url");
	}
	
	public Episode setImdb_url(java.lang.String imdb_url){
		if(StringUtils.isNoneBlank(imdb_url)){
			super.set("imdb_url",imdb_url);
		}
		return this;
	}
	
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public Episode setType(java.lang.String type){
		if(StringUtils.isNoneBlank(type)){
			super.set("type",type);
		}
		return this;
	}
	
	public Episode setFile_path(java.lang.String file_path){
		if(StringUtils.isNotBlank(file_path)){
			super.set("file_path",file_path);
		}
		return this;
	}

	public java.lang.String getFile_path(){
		return this.getStr("file_path");
	}
}