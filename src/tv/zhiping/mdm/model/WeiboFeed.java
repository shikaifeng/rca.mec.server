package tv.zhiping.mdm.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.jfinal.BasicModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-20
 */
@SuppressWarnings("serial")
public class WeiboFeed extends BasicModel<WeiboFeed> {
	public static final WeiboFeed dao = new WeiboFeed();
	
	/**
	 * 当前剧集，最后的修改时间
	 */
	public Timestamp queryMaxUpdatedAtByEpisodeId(Long episode_id,Timestamp prev_updated_at,String start_time_str) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select max(updated_at) as updated_at from "+tableName+" where episode_id=? and start_time_str<=?");
		params.add(episode_id);
		params.add(start_time_str);
		if(prev_updated_at!=null){
			sql.append(" and updated_at>?");
			params.add(prev_updated_at);
		}
		WeiboFeed obj = findFirst(sql.toString(),params.toArray());
		if(obj!=null){
			return obj.getTimestamp("updated_at");
		}
		return null;
	}

	
	/**
	 * 当前剧集，时间的修改条数
	 */
	public Long queryCountByEpisodeId(Long episode_id,String start_time_str,Date date) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select count(1) as count from "+tableName+" where episode_id=? and start_time_str<=? and updated_at=?");
		params.add(Cons.STATUS_VALID);
		params.add(episode_id);
		params.add(start_time_str);
		params.add(date);
		WeiboFeed obj = findFirst(sql.toString(),params.toArray());
		if(obj!=null){
			return obj.get("count");
		}
		return null;
	}

	/**
	 * 获取最大最小的时间
	 */
	public WeiboFeed queryMinMaxTimeByEpisode(Long episode_id) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select min(start_time) as min_time,max(start_time) as max_time from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and episode_id=?");
		params.add(episode_id);
			
		return findFirst(sql.toString(),params.toArray());
	}
	
	public List<WeiboFeed> queryAllByEpisode(Long episode_id) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where 1=1");
		
		sql.append(" and episode_id=?");
		params.add(episode_id);
			
		sql.append(" order by start_time");//program,topic
		return find(sql.toString(),params.toArray());
	}
	
	
	/**
	 * 分页查询
	 */
	public List<WeiboFeed> queryByEpisode(Long episode_id) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and episode_id=?");
		params.add(episode_id);
			
		sql.append(" order by start_time");//program,topic
		return find(sql.toString(),params.toArray());
	}
	
	/**
	 * 分页查询
	 */
	public List<WeiboFeed> queryByEpisodeTime(Long episode_id,Long start_time,Long end_time) {
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
	
	/**
	 * 这个剧集下的数量
	 */
	public Long queryCountByEpisode(Long episode_id) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select count(1) as count from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and episode_id=?");
		params.add(episode_id);
		
		
		WeiboFeed obj = findFirst(sql.toString(),params.toArray());
		if(obj!=null){
			return obj.getLong("count");			
		}
		return null;
	}
	
	public static enum WeiboFeedType {
		program,person,topic
	}
		
	public java.lang.Long getProgram_id(){
		return this.getLong("program_id");
	}
	
	public WeiboFeed setProgram_id(java.lang.Long program_id){
		super.set("program_id",program_id);
		return this;
	}
	public java.lang.Long getEpisode_id(){
		return this.getLong("episode_id");
	}
	
	public WeiboFeed setEpisode_id(java.lang.Long episode_id){
		super.set("episode_id",episode_id);
		return this;
	}
	public java.lang.String getSeries(){
		return this.getStr("series");
	}
	
	public WeiboFeed setSeries(java.lang.String series){
		super.set("series",series);
		return this;
	}
	public java.lang.Long getStart_time(){
		return this.getLong("start_time");
	}
	
	public WeiboFeed setStart_time(java.lang.Long start_time){
		super.set("start_time",start_time);
		return this;
	}
	public java.lang.String getStart_time_str(){
		return this.getStr("start_time_str");
	}
	
	public WeiboFeed setStart_time_str(java.lang.String start_time_str){
		super.set("start_time_str",start_time_str);
		return this;
	}
	public java.lang.String getContent(){
		return this.getStr("content");
	}
	
	public WeiboFeed setContent(java.lang.String content){
		super.set("content",content);
		return this;
	}
	public java.lang.String getImages(){
		return this.getStr("images");
	}
	
	public WeiboFeed setImages(java.lang.String images){
		super.set("images",images);
		return this;
	}
	public java.lang.String getVideos(){
		return this.getStr("videos");
	}
	
	public WeiboFeed setVideos(java.lang.String videos){
		super.set("videos",videos);
		return this;
	}
	
	public java.lang.String getSender_name(){
		return this.getStr("sender_name");
	}
	
	public WeiboFeed setSender_name(java.lang.String sender_name){
		if(StringUtils.isNotBlank(sender_name)){
			super.set("sender_name",sender_name);
		}
		return this;
	}
	public java.lang.String getSender_avatar(){
		return this.getStr("sender_avatar");
	}
	
	public WeiboFeed setSender_avatar(java.lang.String sender_avatar){
		if(StringUtils.isNotBlank(sender_avatar)){
			super.set("sender_avatar",sender_avatar);
		}
		return this;
	}
	public java.lang.String getSender_url(){
		return this.getStr("sender_url");
	}
	
	public WeiboFeed setSender_url(java.lang.String sender_url){
		if(StringUtils.isNotBlank(sender_url)){
			super.set("sender_url",sender_url);
		}
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public WeiboFeed setType(java.lang.String type){
		if(StringUtils.isNotBlank(type)){
			super.set("type",type);
		}
		return this;
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
	 * 转换给app端显示json
	 * @param json
	 */
	public void parseWeiboFeedJson(JSONObject json,int index,int per_num_second) {
		Long start_time =  this.getStart_time();
		if(start_time!=null){
			start_time = ((start_time/60)*60)+(index*per_num_second);
		}
		
		parseWeiboFeedJson(json,start_time);
	}
	
	/**
	 * 转换给app端显示json
	 * @param json
	 */
	public void parseWeiboFeedJson(JSONObject json) {
		parseWeiboFeedJson(json,this.getStart_time());
	}

//	/**
//	 * 统计这个节目下各个剧集的各种类型的数量
//	 * @param program_id
//	 */
//	public Map<Long,Long> groupMapEpisodeByProgram(Long program_id) {
//		List<WeiboFeed> list = groupEpisodeByProgram(program_id);
//		Map<Long,Long> m = new HashMap<Long,Long>();
//		if(list!=null && !list.isEmpty()){
//			WeiboFeed obj = null;
//			for(int i=0;i<list.size();i++){
//				obj = list.get(i);
//				m.put(obj.getEpisode_id(),obj.getLong("count"));
//			}
//		}
//		return m;
//	}
	
	
	
//	/**
//	 * 统计这个节目下各个剧集的各种类型的数量
//	 * @param program_id
//	 */
//	public List<WeiboFeed> groupEpisodeByProgram(Long program_id) {
//		List<Object> params = new ArrayList<Object>();
//		StringBuilder sql = new StringBuilder("select episode_id,count(1) as count from "+tableName+" where status=?");
//		params.add(Cons.STATUS_VALID);
//		
//		if(program_id!=null){
//			sql.append(" and program_id=? ");
//			params.add(program_id);
//		}
//		
//		sql.append(" group by episode_id");
//		return find(sql.toString(),params.toArray());	
//	}


	/**
	 * 查询大于开始时间，小于截止时间：的时间最大哪条
	 * @param episode_id
	 * @param type
	 * @param start_time
	 * @return
	 */
	public WeiboFeed queryByLtEndTimeMaxTime(Long episode_id,Long time) {
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
	 * 查询大于id的数据进行匹配
	 * @param episode_id
	 * @param type
	 * @param start_time
	 * @return
	 */
	public List<WeiboFeed> queryByGtId(Long episode_id,Long gt_id,String start_time_str) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episode_id!=null){
			sql.append(" and episode_id=?");
			params.add(episode_id);
		}
		
		if(gt_id!=null){
			sql.append(" and id>?");
			params.add(gt_id);
		}
		
		if(StringUtils.isNotBlank(start_time_str)){
			sql.append(" and start_time_str<=?");
			params.add(start_time_str);
		}
		
		
		sql.append(" order by start_time");
		return find(sql.toString(),params.toArray());
	}

//	/**
//	 * 统计之前处理的数据是否有变化了
//	 * @param updated_at
//	 */
//	public List<WeiboFeed> queryStaticStateByGtTime(Long episode_id,Date prev_max_updated_at,Long prev_max_id) {
//		List<Object> params = new ArrayList<Object>();
//		StringBuilder sql = new StringBuilder("select status,count(1) as count from "+tableName+" where episode_id=?");
//		params.add(episode_id);
//		
//		if(prev_max_updated_at != null){
//			params.add(prev_max_updated_at);
//			sql.append(" and updated_at>?");
//		}
//		
//		if(max_updated_at != null){
//			params.add(max_updated_at);
//			sql.append(" and updated_at<=?");
//		}
//		sql.append(" group by status");
//		
//		return find(sql.toString(),params.toArray());
//	}
}