package tv.zhiping.mec.feed.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.common.cache.CacheKey;
import tv.zhiping.jfinal.BasicModel;
import tv.zhiping.redis.Redis;

import com.jfinal.plugin.activerecord.Db;


/**
 * @author 樊亚容
 * @since 2014-05-19
 */
@SuppressWarnings("serial")
public class Scene extends BasicModel<Scene> {

	public static final Scene dao = new Scene();

	/**
	 * 根据剧集id 所有有效的场景 
	 */
	public List<Scene> queryAllByEpisodeIdValidate(Long episodeId) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select distinct b.* from element a,"+tableName+" b where a.scene_id=b.id");
		
		if(episodeId!=null){
			sql.append(" and b.pid=?");
			params.add(episodeId);			
		}
		
		sql.append(" and a.status=? and b.status=?");
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
				
		sql.append(" and a.start_time!='' and a.end_time!=''");
		
		sql.append(" order by b.start_time ");
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * 根据剧集id获取场最大的截止时间
	 * @param episodeId
	 */
	public Long queryMaxEndTimeByEpisodeId(Long episodeId) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select max(end_time) as end_time from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episodeId!=null){
			sql.append(" and pid=?");
			params.add(episodeId);			
		}
		
		Scene obj = findFirst(sql.toString(),params.toArray());
		if(obj!=null){
			return obj.getLong("end_time");			
		}
		return null;
	}
	
	/**
	 * 根据剧集id获取场景条数
	 * @param episodeId
	 */
	public Long queryCountByEpisodeId(Long episodeId) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select count(1) as count from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episodeId!=null){
			sql.append(" and pid=?");
			params.add(episodeId);			
		}
		
		Scene obj = findFirst(sql.toString(),params.toArray());
		if(obj!=null){
			return obj.getLong("count");			
		}
		return null;
	}
	
	
	/**
	 * 根据剧集id获取有效的场景条数
	 * @param episodeId
	 */
	public Long queryCountByEpisodeIdValidate(Long episodeId) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select count(distinct b.id) from element a,"+tableName+" b where a.scene_id=b.id");
	
		if(episodeId!=null){
			sql.append(" and b.pid=?");
			params.add(episodeId);
		}
		
		sql.append(" and a.status=? and b.status=?");
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and a.start_time!='' and a.end_time!=''");
		
		Scene obj = findFirst(sql.toString(),params.toArray());
		if(obj!=null){
			return obj.getLong("count");			
		}
		return null;
	}
	
	
	/**
	 * 根据剧集id获取场景条数
	 * @param episodeId
	 */
	public Long queryCountByEpisodeIdUseApp(Long episodeId) {
		Long count = Redis.get(CacheKey.SCENE_COUNT+episodeId);
		if(count == null){
			count = queryCountByEpisodeIdValidate(episodeId);
			if(count == null || Cons.DEF_NULL_NUMBER.equals(count)){//显示为0
				count = MecWeiboFeed.dao.queryCountByEpisode(episodeId);
				if(count!=null && count>1){
					count = 1L;
				}
			}
			Redis.setex(CacheKey.SCENE_COUNT,count,CacheKey.SCENE_COUNT_EXPIRY);
		}
		return count;
	}
	
	
	public void deleteByEpisodeId(Long episode_id) {
		StringBuilder sql = new StringBuilder("update scene set status=?,updated_at=? where pid=?");
		List<Object> params = new ArrayList<Object>();
		
		params.add(Cons.STATUS_INVALID);
		params.add(new Timestamp(System.currentTimeMillis()));
		params.add(episode_id);
		
		Db.update(sql.toString(),params.toArray());
	}
	
	
	public Scene queryByEpisodeTime(Long episode_id, Long start_time,Long end_time) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episode_id!=null){
			sql.append(" and pid=?");
			params.add(episode_id);			
		}
		if(start_time!=null){
			sql.append(" and start_time=?");
			params.add(start_time);			
		}
		if(end_time!=null){
			sql.append(" and end_time=?");
			params.add(end_time);			
		}
		return findFirst(sql.toString(),params.toArray());
	}
	
	public Scene queryByEpisodeGtStartTime(Long episode_id, Long start_time) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episode_id!=null){
			sql.append(" and pid=?");
			params.add(episode_id);			
		}
		if(start_time!=null){
			sql.append(" and start_time>?");
			params.add(start_time);			
		}
		sql.append(" order by start_time");
		return findFirst(sql.toString(),params.toArray());
	}
	
	
	public List<Scene> queryByEpisode(Long episode_id) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episode_id!=null){
			sql.append(" and pid=?");
			params.add(episode_id);			
		}
		
		sql.append(" order by start_time");
		return find(sql.toString(),params.toArray());
	}
	
	public java.lang.Long getPid(){
		return this.getLong("pid");
	}
	
	public Scene setPid(java.lang.Long pid){
		super.set("pid",pid);
		return this;
	}
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public Scene setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getCover(){
		return this.getStr("cover");
	}
	
	public Scene setCover(java.lang.String cover){
		super.set("cover",cover);
		return this;
	}
	public java.lang.String getImages(){
		return this.getStr("images");
	}
	
	public Scene setImages(java.lang.String images){
		super.set("images",images);
		return this;
	}
	public java.lang.Long getStart_time(){
		return this.getLong("start_time");
	}
	
	public Scene setStart_time(java.lang.Long start_time){
		super.set("start_time",start_time);
		return this;
	}
	public java.lang.Long getEnd_time(){
		return this.getLong("end_time");
	}
	
	public Scene setEnd_time(java.lang.Long end_time){
		super.set("end_time",end_time);
		return this;
	}
	public java.lang.Long getDuration(){
		return this.getLong("duration");
	}
	
	public Scene setDuration(java.lang.Long duration){
		super.set("duration",duration);
		return this;
	}
	public java.lang.String getSummary(){
		return this.getStr("summary");
	}
	
	public Scene setSummary(java.lang.String summary){
		super.set("summary",summary);
		return this;
	}
	public java.lang.String getTags(){
		return this.getStr("tags");
	}
	
	public Scene setFtype(java.lang.String ftype){
		super.set("ftype",ftype);
		return this;
	}
	
	public java.lang.String getFtype(){
		return this.getStr("ftype");
	}
	
	public Scene setTags(java.lang.String tags){
		super.set("tags",tags);
		return this;
	}
	
	public java.lang.String getFid(){
		return this.getStr("fid");
	}
	
	public Scene setFid(java.lang.String fid){
		super.set("fid",fid);
		return this;
	}
}