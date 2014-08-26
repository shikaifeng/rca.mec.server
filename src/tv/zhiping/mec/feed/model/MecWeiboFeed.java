package tv.zhiping.mec.feed.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.jfinal.BasicModel;
import tv.zhiping.mdm.model.WeiboFeedMediaType;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;

/**
 * @author 作者
 * @version 1.0
 * @since 2014-07-22
 */
@SuppressWarnings("serial")
public class MecWeiboFeed extends BasicModel<MecWeiboFeed> {
	public static final MecWeiboFeed dao = new MecWeiboFeed();

	/**
	 * 分页查询
	 */
	public List<MecWeiboFeed> queryByEpisode(Long episode_id) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and episode_id=?");
		params.add(episode_id);
			
		sql.append(" order by start_time");//program,topic
		return find(sql.toString(),params.toArray());
	}
	
	/**
	 * 统计这个节目下各个剧集的各种类型的数量
	 * @param program_id
	 */
	public Map<Long,Long> groupMapEpisodeByProgram(Long program_id) {
		List<MecWeiboFeed> list = groupEpisodeByProgram(program_id);
		Map<Long,Long> m = new HashMap<Long,Long>();
		if(list!=null && !list.isEmpty()){
			MecWeiboFeed obj = null;
			for(int i=0;i<list.size();i++){
				obj = list.get(i);
				m.put(obj.getEpisode_id(),obj.getLong("count"));
			}
		}
		return m;
	}
	
	/**
	 * 统计这个节目下各个剧集的各种类型的数量
	 * @param program_id
	 */
	public List<MecWeiboFeed> groupEpisodeByProgram(Long program_id) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select episode_id,count(1) as count from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(program_id!=null){
			sql.append(" and program_id=? ");
			params.add(program_id);
		}
		
		sql.append(" group by episode_id");
		return find(sql.toString(),params.toArray());	
	}
	
	/**
	 * 清除这个剧集下的所有微博
	 * @param episode_id
	 */
	public void deleteByEpisodeId(Long episode_id) {
		StringBuilder sql = new StringBuilder("delete from "+tableName+" where episode_id=?");
		List<Object> params = new ArrayList<Object>();
		params.add(episode_id);
		
		Db.update(sql.toString(),params.toArray());
	}
	
	/**
	 * 通过剧集id，当前的时间数，查询条数
	 * @param episode_id
	 * @param minute
	 * @return
	 */
	public MecWeiboFeed queryCountByEpisodeMinute(Long episode_id, Long minute) {
		StringBuilder sql = new StringBuilder("select count(1) as count from "+tableName+" where episode_id=? and minute=?");
		List<Object> params = new ArrayList<Object>();
		params.add(episode_id);
		params.add(minute);
		
		return findFirst(sql.toString(),params.toArray());
	}
	
	
	/**
	 * 查询大于开始时间，小于截止时间：的时间最大哪条
	 * @param episode_id
	 * @param type
	 * @param start_time
	 * @return
	 */
	public MecWeiboFeed queryByLtEndTimeMaxTime(Long episode_id,Long time) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episode_id!=null){
			sql.append(" and episode_id=?");
			params.add(episode_id);
		}
		
		sql.append(" and start_time<=?");
		params.add(time);
		
		sql.append(" order by start_time desc");
		return findFirst(sql.toString(),params.toArray());
	}
	
	/**
	 * 分页查询
	 */
	public List<MecWeiboFeed> queryByEpisodeTime(Long episode_id,Long start_time,Long end_time) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and episode_id=?");
		params.add(episode_id);
		
		if(start_time!=null){
			sql.append(" and ?<=start_time");
			params.add(start_time);
		}
		
		if(end_time!=null){
			sql.append(" and start_time<=?");
			params.add(end_time);
		}
			
		sql.append(" order by start_time");
		return find(sql.toString(),params.toArray());
	}
	
	
	public void parseImageJson(JSONObject json) {
		String images = this.getImages();
		String videos = this.getVideos();
		if(StringUtils.isNoneBlank(images)){
			WeiboFeedMediaType type = WeiboFeedMediaType.weburl;
			String[] videosArr = null;
			if(StringUtils.isBlank(videos)){
				type = WeiboFeedMediaType.image;
			}else{
				videosArr = videos.replace("，",",").split(",");
			}
			String[] imagesArr = images.replace("，",",").split(",");
			String[] urls = null;
			String[] thumbnail_pic = null;
			if(WeiboFeedMediaType.image == type){
				urls = imagesArr;
				thumbnail_pic = imagesArr;
			}else{
				urls = videosArr;
				thumbnail_pic = imagesArr;
			}
			
			if(urls!=null && thumbnail_pic!=null && urls.length == thumbnail_pic.length){
				JSONArray links = new JSONArray();
				for(int j=0;j<urls.length;j++){
					JSONObject link_json = new JSONObject();									
					link_json.put("type",type);
					link_json.put("thumbnail_pic",thumbnail_pic[j]);
					link_json.put("url",urls[j]);
					links.add(link_json);
				}
				json.put("links",links);
			}
		}
	}
	
	/**
	 * 这个剧集下的数量
	 */
	public Long queryCountByEpisode(Long episode_id) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select count(1) as count from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and episode_id=?");
		params.add(episode_id);
		
		
		MecWeiboFeed obj = findFirst(sql.toString(),params.toArray());
		if(obj!=null){
			return obj.getLong("count");			
		}
		return null;
	}
	
	

	/**
	 * 分页查询
	 */
	public MecWeiboFeed queryMinMaxTimeByEpisode(Long episode_id) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select min(start_time) as min_time,max(start_time) as max_time from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and episode_id=?");
		params.add(episode_id);
			
		return findFirst(sql.toString(),params.toArray());
	}
	
	public void parseWeiboFeedJson(JSONObject json,Long start_time) {
		json.put("id",this.getId());
		json.put("start_time",start_time);
		json.put("end_time",start_time);
		
		json.put("content", this.getContent());
		if(StringUtils.isNotBlank(this.getSender_name())){//测试使用
			json.put("content",this.getSender_name()+"："+this.getContent());	
		}else{
			json.put("content", this.getContent());
		}
		
		json.put("sender_name", this.getSender_name());
		json.put("sender_url", this.getSender_url());
		json.put("sender_avatar",ComUtil.getStHttpPath(this.getSender_avatar()));
		
		parseImageJson(json);
	}
	
	/**
	 * 转换给app端显示json
	 * @param json
	 */
	public void parseWeiboFeedJson(JSONObject json) {
		parseWeiboFeedJson(json,this.getStart_time());
	}
	
	public java.lang.Long getMdm_id(){
		return this.getLong("mdm_id");
	}
	
	public MecWeiboFeed setMdm_id(java.lang.Long mdm_id){
		super.set("mdm_id",mdm_id);
		return this;
	}
	
	public java.lang.Long getProgram_id(){
		return this.getLong("program_id");
	}
	
	public MecWeiboFeed setProgram_id(java.lang.Long program_id){
		super.set("program_id",program_id);
		return this;
	}
	public java.lang.Long getEpisode_id(){
		return this.getLong("episode_id");
	}
	
	public MecWeiboFeed setEpisode_id(java.lang.Long episode_id){
		super.set("episode_id",episode_id);
		return this;
	}
	public java.lang.String getSeries(){
		return this.getStr("series");
	}
	
	public MecWeiboFeed setSeries(java.lang.String series){
		if(StringUtils.isNotBlank(series)){
			super.set("series",series);
		}
		return this;
	}
	public java.lang.Integer getMinute(){
		return this.getInt("minute");
	}
	
	public MecWeiboFeed setMinute(java.lang.Integer minute){
		super.set("minute",minute);
		return this;
	}
	public java.lang.Long getShow_start_time(){
		return this.getLong("show_start_time");
	}
	
	public MecWeiboFeed setShow_start_time(java.lang.Long show_start_time){
		super.set("show_start_time",show_start_time);
		return this;
	}
	public java.lang.Long getStart_time(){
		return this.getLong("start_time");
	}
	
	public MecWeiboFeed setStart_time(java.lang.Long start_time){
		super.set("start_time",start_time);
		return this;
	}
	public java.lang.String getStart_time_str(){
		return this.getStr("start_time_str");
	}
	
	public MecWeiboFeed setStart_time_str(java.lang.String start_time_str){
		if(StringUtils.isNotBlank(start_time_str)){
			super.set("start_time_str",start_time_str);
		}
		return this;
	}
	public java.lang.String getContent(){
		return this.getStr("content");
	}
	
	public MecWeiboFeed setContent(java.lang.String content){
		if(StringUtils.isNotBlank(content)){
			super.set("content",content);
		}
		return this;
	}
	public java.lang.String getImages(){
		return this.getStr("images");
	}
	
	public MecWeiboFeed setImages(java.lang.String images){
		if(StringUtils.isNotBlank(images)){
			super.set("images",images);
		}
		return this;
	}
	public java.lang.String getVideos(){
		return this.getStr("videos");
	}
	
	public MecWeiboFeed setVideos(java.lang.String videos){
		if(StringUtils.isNotBlank(videos)){
			super.set("videos",videos);
		}
		return this;
	}
	public java.lang.String getSender_name(){
		return this.getStr("sender_name");
	}
	
	public MecWeiboFeed setSender_name(java.lang.String sender_name){
		if(StringUtils.isNotBlank(sender_name)){
			super.set("sender_name",sender_name);
		}
		return this;
	}
	public java.lang.String getSender_avatar(){
		return this.getStr("sender_avatar");
	}
	
	public MecWeiboFeed setSender_avatar(java.lang.String sender_avatar){
		if(StringUtils.isNotBlank(sender_avatar)){
			super.set("sender_avatar",sender_avatar);
		}
		return this;
	}
	public java.lang.String getSender_url(){
		return this.getStr("sender_url");
	}
	
	public MecWeiboFeed setSender_url(java.lang.String sender_url){
		if(StringUtils.isNotBlank(sender_url)){
			super.set("sender_url",sender_url);
		}
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public MecWeiboFeed setType(java.lang.String type){
		if(StringUtils.isNotBlank(type)){
			super.set("type",type);
		}
		return this;
	}

	
}