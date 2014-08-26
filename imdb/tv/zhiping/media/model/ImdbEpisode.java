package tv.zhiping.media.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.NoIdModel;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mdm.model.Program.ProgramType;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-12
 */
@SuppressWarnings("serial")
public class ImdbEpisode extends NoIdModel<ImdbEpisode> {

	public static final ImdbEpisode dao = new ImdbEpisode();

	
//	public static final Integer MATCH_FEED_STATE_WAIT = 10; //待处理
//	public static final Integer MATCH_FEED_STATE_DEALING = 11; //处理ing
//	public static final Integer MATCH_FEED_STATE_SUC = 12; //处理成功
//	public static final Integer MATCH_FEED_STATE_FAIL = 13; //处理失败
	
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
	

	public ImdbEpisode queryBySelfOrPidSeasonEpisode(String id,String pid, Long season,Long current_episode) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(StringUtils.isNotBlank(id)){
			sql.append(" and id=?");
			params.add(id);	
		}else{
			if(StringUtils.isNotBlank(pid)){
				sql.append(" and pid=?");
				params.add(pid);
			}
			
			if(season!=null){
				sql.append(" and season=?");
				params.add(season);
			}
			
			if(current_episode!=null){
				sql.append(" and current_episode=?");
				params.add(current_episode);
			}
		}
		return findFirst(sql.toString(),params.toArray());
	}
	
	/**
	 * 分页查询待匹配feed的节目信息
	 * 
	 */
	public List<ImdbEpisode> getMatchFeedList(String id,String created_at) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and mdm_program_id!=''");
		sql.append(" and mdm_episode_id!=''");
		
		if(StringUtils.isNotBlank(id)){
			sql.append(" and id=?");
			params.add(id);
		}else if(StringUtils.isNotBlank(created_at)){
			sql.append(" and created_at>?");
			params.add(created_at);
		}
		
		sql.append(" and match_state=?");
		params.add(Cons.THREAD_STATE_SUC);
		return find(sql.toString(),params.toArray());
	}
	
	/**
	 * 分页查询
	 */
	public Page<ImdbEpisode> paginateWaitMatchTv(int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
//		sql.append(" and pid='/title/tt0285331/'");//测试使用
		sql.append(" and current_episode>0");
		sql.append(" and match_state=?");
		params.add(Cons.THREAD_STATE_WAIT);
			
		sql.append(" and type = ?");
		params.add(ImdbEpisodeType.tvEpisode.toString());
		
		//百科关系暂时不匹配
		sql.append(" and (source = ? or source=?)");
		params.add(1);//节目的
		params.add(2);//剧集
				
//		params.add(ImdbEpisodeType.tvSeries.toString());
		
		sql.append(" order by pid,season,current_episode");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),params.toArray());
	}
	
	public String queryMinYearByPidSeason(String pid, String season) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select min(year) as year from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and current_episode>0");
		
		sql.append(" and pid = ?");
		params.add(pid);
		
		//百科关系暂时不匹配
		sql.append(" and season=?");
		params.add(season);
		
		sql.append(" and year!=''");
		
		ImdbEpisode obj = findFirst(sql.toString(),params.toArray());
		if(obj!=null){
			return obj.getYear();
		}
		return null;
	}
	
	/**
	 * 根据用户编号得到所有的电视剧信息
	 * @param person_id
	 * @return
	 */
	public List<ImdbEpisode> queryByPersonId(String person_id) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select a.* from "+tableName+" a,imdb_person_video b where  b.video_id=a.id  and a.status=? and b.status=? and b.person_id=?");
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		
		params.add(person_id);
	
		return find(sql.toString(),params.toArray());
	}
	
	
	/**
	 * 返回mdm的节目信息
	 * @param imdb_episode
	 * @param program
	 */
	public void parseMdmProgram(ImdbEpisode imdb_episode,Program program,String title) {
		program.setTitle(title);
		program.setOrig_title(this.getOrig_title());
		program.setAka(this.getAka());
		program.setAka_en(this.getAka());
		
		program.setType(ProgramType.TV.toString());
		program.setWebsite(this.getWebsite());
		program.setYear(imdb_episode.getYear());
		program.setLanguages(this.getLanguages());
		program.setCountry(this.getCountry());
		if(StringUtils.isNotBlank(imdb_episode.getSeason())){
			program.setCurrent_season(new Long(imdb_episode.getSeason()));							
		}
		program.setEpisodes_count(this.getEpisodes_count());
		program.setImdb_url(this.getUrl());
		program.setTags(this.getTags());
		program.setImages(this.getImages());
		program.setSummary(this.getSummary());
		program.setPhotos(this.getPhotos());
		program.setPhotos(this.getPhotos());
	}
	
	//类型字段
	public static enum ImdbEpisodeType {
		movie,tv,tvSeries,tvEpisode
	}
	
	/**
	 * 分页查询
	 */
	public Page<ImdbEpisode> paginateByMovie(ImdbEpisode obj,int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(obj!=null){
			if(obj.getMatch_state()!=null){
				sql.append(" and match_state=?");
				params.add(obj.getMatch_state());
			}
			
			if(StringUtils.isNotBlank(obj.getType())){
				sql.append(" and type = ?");
				params.add(obj.getType());
			}
		}

		//百科关系暂时不匹配
		sql.append(" and (source = ? or source=?)");
		params.add(1);//节目的
		params.add(2);//剧集
		
		sql.append("  order by pid,year,season");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),params.toArray());
	}
	
	
	public void parseMdmEpisode(Program program,Episode episode,String title) {
		episode.setPid(program.getId());
		episode.setTitle(title);
		episode.setOrig_title(this.getOrig_title());
		episode.setAka(this.getAka());
		episode.setType(ProgramType.TV.toString());
		episode.setYear(this.getYear());
		episode.setLanguages(this.getLanguages());
		episode.setCountry(this.getCountry());
		episode.setEpisodes_count(this.getEpisodes_count());
		episode.setCurrent_episode(this.getCurrent_episode());
		episode.setDuration(this.getDuration());
		episode.setTags(this.getTags());
		episode.setImages(this.getImages());
		episode.setSummary(this.getSummary());
		episode.setPhotos(this.getPhotos());
		episode.setSeries(this.getSeries());
		episode.setImdb_url(this.getUrl());
	}

	public String getId(){
		return super.getStr("id");
	}

	public ImdbEpisode setId(String id){
		super.set("id",id);
		return this;
	}
	
	public java.lang.String getPrevious_episode(){
		return this.getStr("previous_episode");
	}
	
	public ImdbEpisode setPrevious_episode(java.lang.String previous_episode){
		if(StringUtils.isNotBlank(previous_episode)){
			super.set("previous_episode",previous_episode);
		}
		return this;
	}
	public java.lang.String getNext_episode(){
		return this.getStr("next_episode");
	}
	
	public ImdbEpisode setNext_episode(java.lang.String next_episode){
		if(StringUtils.isNotBlank(next_episode)){
			super.set("next_episode",next_episode);
		}
		return this;
	}
	public java.lang.String getPid(){
		return this.getStr("pid");
	}
	
	public ImdbEpisode setPid(java.lang.String pid){
		if(StringUtils.isNotBlank(pid)){
			super.set("pid",pid);
		}
		return this;
	}
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public ImdbEpisode setTitle(java.lang.String title){
		if(StringUtils.isNotBlank(title)){
			super.set("title",title);
		}
		return this;
	}
	public java.lang.String getOrig_title(){
		return this.getStr("orig_title");
	}
	
	public ImdbEpisode setOrig_title(java.lang.String orig_title){
		if(StringUtils.isNotBlank(orig_title)){
			super.set("orig_title",orig_title);
		}
		return this;
	}
	public java.lang.String getAka(){
		return this.getStr("aka");
	}
	
	public ImdbEpisode setAka(java.lang.String aka){
		if(StringUtils.isNotBlank(aka)){
			super.set("aka",aka);
		}
		return this;
	}
	public java.lang.String getCover(){
		return this.getStr("cover");
	}
	
	public ImdbEpisode setCover(java.lang.String cover){
		if(StringUtils.isNotBlank(cover)){
			super.set("cover",cover);
		}
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public ImdbEpisode setType(java.lang.String type){
		if(StringUtils.isNotBlank(type)){
			super.set("type",type);
		}
		return this;
	}
	public java.lang.String getWebsite(){
		return this.getStr("website");
	}
	
	public ImdbEpisode setWebsite(java.lang.String website){
		if(StringUtils.isNotBlank(website)){
			super.set("website",website);
		}
		return this;
	}
	public java.lang.String getYear(){
		return this.getStr("year");
	}
	
	public ImdbEpisode setYear(java.lang.String year){
		if(StringUtils.isNotBlank(year)){
			super.set("year",year);
		}
		return this;
	}
	public java.lang.String getSeason(){
		return this.getStr("season");
	}
	
	public ImdbEpisode setSeason(java.lang.String season){
		if(StringUtils.isNotBlank(season)){
			super.set("season",season);
		}
		return this;
	}
	public java.lang.String getStart_year(){
		return this.getStr("start_year");
	}
	
	public ImdbEpisode setStart_year(java.lang.String start_year){
		if(StringUtils.isNotBlank(start_year)){
			super.set("start_year",start_year);
		}
		return this;
	}
	public java.lang.String getEnd_year(){
		return this.getStr("end_year");
	}
	
	public ImdbEpisode setEnd_year(java.lang.String end_year){
		if(StringUtils.isNotBlank(end_year)){
			super.set("end_year",end_year);
		}
		return this;
	}
	public java.lang.String getLanguages(){
		return this.getStr("languages");
	}
	
	public ImdbEpisode setLanguages(java.lang.String languages){
		if(StringUtils.isNotBlank(languages)){
			super.set("languages",languages);
		}
		return this;
	}
	public java.lang.String getCountry(){
		return this.getStr("country");
	}
	
	public ImdbEpisode setCountry(java.lang.String country){
		if(StringUtils.isNotBlank(country)){
			super.set("country",country);
		}
		return this;
	}
	public java.lang.Long getEpisodes_count(){
		return this.getLong("episodes_count");
	}
	
	public ImdbEpisode setEpisodes_count(java.lang.Long episodes_count){
		if(episodes_count!=null){
			super.set("episodes_count",episodes_count);
		}
		return this;
	}
	public java.lang.Long getCurrent_episode(){
		return this.getLong("current_episode");
	}
	
	public ImdbEpisode setCurrent_episode(java.lang.Long current_episode){
		if(current_episode!=null){
			super.set("current_episode",current_episode);
		}
		return this;
	}
	public java.lang.Long getDuration(){
		return this.getLong("duration");
	}
	
	public ImdbEpisode setDuration(java.lang.Long duration){
		if(duration!=null){
			super.set("duration",duration);
		}
		return this;
	}
	public java.lang.String getTags(){
		return this.getStr("tags");
	}
	
	public ImdbEpisode setTags(java.lang.String tags){
		if(StringUtils.isNotBlank(tags)){
			super.set("tags",tags);
		}
		return this;
	}
	public java.lang.String getImages(){
		return this.getStr("images");
	}
	
	public ImdbEpisode setImages(java.lang.String images){
		if(StringUtils.isNotBlank(images)){
			super.set("images",images);
		}
		return this;
	}
	public java.lang.String getSummary(){
		return this.getStr("summary");
	}
	
	public ImdbEpisode setSummary(java.lang.String summary){
		if(StringUtils.isNotBlank(summary)){
			super.set("summary",summary);
		}
		return this;
	}
	public java.lang.String getPhotos(){
		return this.getStr("photos");
	}
	
	public ImdbEpisode setPhotos(java.lang.String photos){
		if(StringUtils.isNotBlank(photos)){
			super.set("photos",photos);
		}
		return this;
	}
	public java.lang.String getJson_txt(){
		return this.getStr("json_txt");
	}
	
	public ImdbEpisode setJson_txt(java.lang.String json_txt){
		if(StringUtils.isNotBlank(json_txt)){
			super.set("json_txt",json_txt);
		}
		return this;
	}
	public java.lang.String getSeries(){
		return this.getStr("series");
	}
	
	public ImdbEpisode setSeries(java.lang.String series){
		if(StringUtils.isNotBlank(series)){
			super.set("series",series);
		}
		return this;
	}
	public java.lang.String getUrl(){
		return this.getStr("url");
	}
	
	public ImdbEpisode setUrl(java.lang.String url){
		if(StringUtils.isNotBlank(url)){
			super.set("url",url);
		}
		return this;
	}
	public java.lang.String getDownload_url(){
		return this.getStr("download_url");
	}
	
	public ImdbEpisode setDownload_url(java.lang.String download_url){
		if(StringUtils.isNotBlank(download_url)){
			super.set("download_url",download_url);
		}
		return this;
	}
	public java.lang.Integer getMatch_state(){
		return this.getInt("match_state");
	}
	
	public ImdbEpisode setMatch_state(java.lang.Integer match_state){
		if(match_state!=null){
			super.set("match_state",match_state);
		}
		return this;
	}
	
	public java.lang.Long getMdm_program_id(){
		return this.getLong("mdm_program_id");
	}
	
	public ImdbEpisode setMdm_program_id(java.lang.Long mdm_program_id){
		if(mdm_program_id!=null){
			super.set("mdm_program_id",mdm_program_id);
		}
		return this;
	}
	
	public java.lang.Long getMdm_episode_id(){
		return this.getLong("mdm_episode_id");
	}
	
	public ImdbEpisode setMdm_episode_id(java.lang.Long mdm_episode_id){
		if(mdm_episode_id!=null){
			super.set("mdm_episode_id",mdm_episode_id);
		}
		return this;
	}

	public java.lang.String getMsg(){
		return this.getStr("msg");
	}
	
	public ImdbEpisode setMatch_type(java.lang.String match_type){
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
	public ImdbEpisode setSource(java.lang.Integer source){
		if(source!=null){
			super.set("source",source);
		}
		return this;
	}

	public java.lang.Integer getSource(){
		return this.getInt("source");
	}

	public ImdbEpisode setMsg(java.lang.String msg){
		if(StringUtils.isNotBlank(msg)){
			if(msg.length()>200){
				msg = msg.substring(0,200);
			}
			super.set("msg",msg);
		}
		return this;
	}
}