package tv.zhiping.mec.feed.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

import com.jfinal.plugin.activerecord.Db;


/**
 * 元素
 * @author 张有良
 * @version 1.0
 * @since 2014-05-20
 */
@SuppressWarnings("serial")
public class Element extends BasicModel<Element> {

	public static final Element dao = new Element();
	
	/**
	 * 查询小于开始时间：的时间最大哪条
	 * @param episode_id
	 * @param type
	 * @param lt_start_time
	 * @return
	 */
	public Element queryByLtStartTimeMaxTime(Long episode_id,String type,Long lt_start_time) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episode_id!=null){
			sql.append(" and episode_id=?");
			params.add(episode_id);
		}
		
		if(StringUtils.isNoneBlank(type)){
			sql.append(" and type=?");
			params.add(type);
		}
		
		sql.append(" and start_time<?");
		params.add(lt_start_time);
		
		sql.append(" order by start_time desc");
		return findFirst(sql.toString(),params.toArray());
	}
	
	
	/**
	 * 查询大于开始时间，小于截止时间：的时间最大哪条
	 * @param episode_id
	 * @param type
	 * @param start_time
	 * @return
	 */
	public Element queryByLtEndTimeMaxTime(Long episode_id,String type,Long time) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episode_id!=null){
			sql.append(" and episode_id=?");
			params.add(episode_id);
		}
		
		if(StringUtils.isNoneBlank(type)){
			sql.append(" and type=?");
			params.add(type);
		}
		
		sql.append(" and ((start_time<=? and ?<end_time) or (start_time<=? and end_time is null))");
		params.add(time);
		params.add(time);
		params.add(time);
		
		sql.append(" order by start_time desc");
		return findFirst(sql.toString(),params.toArray());
	}
	
	
	public Element queryByObj(Element obj) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
	
		if(obj.getScene_id()!=null){
			sql.append(" and scene_id=?");
			params.add(obj.getScene_id());
		}
		
		if(StringUtils.isNotBlank(obj.getType())){
			sql.append(" and type=?");
			params.add(obj.getType());
		}
		
		if(obj.getFid()!=null){
			sql.append(" and fid=?");
			params.add(obj.getFid());
		}
		
		if(obj.getStart_time()!=null){
			sql.append(" and start_time=?");
			params.add(obj.getStart_time());
		}
		if(obj.getEnd_time()!=null){
			sql.append(" and end_time=?");
			params.add(obj.getEnd_time());
		}
		return findFirst(sql.toString(),params.toArray());
	}

	
	public Element queryByEpisodeIdType(Long episode_id,String type) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episode_id!=null){
			sql.append(" and episode_id=?");
			params.add(episode_id);
		}
		
		if(StringUtils.isNoneBlank(type)){
			sql.append(" and type=?");
			params.add(type);
		}
		
		sql.append(" order by start_time");
		return findFirst(sql.toString(),params.toArray());
	}
	
	public List<Element> queryByProgramIdEpisodeIdType(Long program_id,Long episode_id,String type) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
//		sql.append(" and type=?");
//		params.add(type);
		
		if(episode_id!=null){
			sql.append(" and episode_id=?");
			params.add(episode_id);
		}else if(program_id!=null){
			sql.append(" and program_id=?");
			params.add(program_id);
		}
		
		if(StringUtils.isNotBlank(type)){
			sql.append(" and type=?");
			params.add(type);
		}
		
		sql.append(" order by start_time");
		return find(sql.toString(),params.toArray());
	}
	
//	public Element queryByObj(Element obj) {
//		List<Object> params = new ArrayList<Object>();
//		
//		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
//		params.add(Cons.STATUS_VALID);
//		
//		if(obj!=null){
//			if(obj.getFid()!=null){
//				sql.append(" and episode_id=?");
//				params.add(obj.getFid());
//			}
//			if(StringUtils.isNoneBlank(obj.getType())){
//				sql.append(" and type=?");
//				params.add(obj.getType());
//			}
//			if(obj.getStart_time()!=null){
//				sql.append(" and start_time=?");
//				params.add(obj.getStart_time());
//			}
//			if(obj.getEnd_time()!=null){
//				sql.append(" and end_time=?");
//				params.add(obj.getEnd_time());
//			}
//		}
//		return null;
//	}
	
	
	public Element queryCountByProgramIdType(Long program_id, String type) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select count(distinct fid) as count from "+tableName+" where program_id=? and type=? and status=?";
		params.add(program_id);
		params.add(type);
		params.add(Cons.STATUS_VALID);
		return findFirst(sql,params.toArray());
	}
	
	
	/**
	 * 根据音乐id 查询 出音乐元素
	 */
	public Element queryByMusicId(Long fid){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where fid=? and type = ? and status=?";
		params.add(fid);	
		params.add(ElementType.music.toString());
		params.add(Cons.STATUS_VALID);
		return findFirst(sql,params.toArray());
	}
	/**
	 * 根据episode_id/type/fid 查重
	 */
	public Element queryByEpisodeFid(Long episode_id,Long fid,String type){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where fid=? and type = ?and episode_id = ? and status=?";
		params.add(fid);	
		params.add(type);
		params.add(episode_id);
		params.add(Cons.STATUS_VALID);
		return findFirst(sql,params.toArray());
	}
	
	
//	/**
//	 * 通过节目id、type获取某节目的某一类元素的所有的片段
//	 * @author 樊亚容
//	 * @param episode_id
//	 * @param types
//	 * @return
//	 */
//	public List<Element> queryAllByPrograma(Long program_id,List<String> types){
//		List<Object> params = new ArrayList<Object>();
//		StringBuilder sql = new StringBuilder("from "+tableName+" e left join element_addon addon on e.id=addon.id where e.status=?");
//		params.add(Cons.STATUS_VALID);
//		
//		if(program_id!=null){
//			sql.append(" and e.program_id=? ");
//			params.add(program_id);
//		}
//		
//		if(types!=null && !types.isEmpty()){
//			sql.append(" and type in ");
//			getSqlIdsParam(sql,types,params);
//		}
//		
//		sql.append(" order by e.start_time");
//		return paginate(1,Cons.MAX_PAGE_SIZE,"select e.*,addon.* ", sql.toString(),params.toArray()).getList();
//	}
	
	/**
	 * 通过剧集id和type list获取所有的片段
	 * @param episode_id
	 * @param types
	 * @return
	 */
	public List<Element> queryAllByEpisode(Long episode_id,List<String> types){//ElementType
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episode_id!=null){
			sql.append(" and episode_id=? ");
			params.add(episode_id);
		}
		
		if(types!=null && !types.isEmpty()){
			sql.append(" and type in ");
			getSqlIdsParam(sql,types,params);
		}
		
		sql.append(" order by start_time");
		return paginate(1,Cons.MAX_PAGE_SIZE,"select * ", sql.toString(),params.toArray()).getList();
	}
	
	/**
	 * 通过剧集id和type list获取fid片段
	 * @param episode_id
	 * @param types
	 * @return
	 */
	public List<Element> queryDistinctFidAllByEpisode(Long episode_id,String type){
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select distinct fid from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episode_id!=null){
			sql.append(" and episode_id=? ");
			params.add(episode_id);
		}
		
		if(StringUtils.isNotEmpty(type)){
			sql.append(" and type = ? ");
			params.add(type);
		}
		
		return find(sql.toString(),params.toArray());
	}
	
//	/**
//	 * 通过fid和type 获取关联的剧集id
//	 * @param fid
//	 * @param type
//	 * @return
//	 */
//	public List<Element> queryDistinctEpisodeIdByFidType(Long nprogram_id,Long fid,String type){
//		List<Object> params = new ArrayList<Object>();
//		StringBuilder sql = new StringBuilder("select distinct program_id from "+tableName+" where status=?");
//		params.add(Cons.STATUS_VALID);
//		
//		if(nepisode_id!=null){
//			sql.append(" and episode_id!=? ");
//			params.add(nepisode_id);
//		}
//		
//		if(fid!=null){
//			sql.append(" and fid=? ");
//			params.add(fid);
//		}
//		
//		if(StringUtils.isNotEmpty(type)){
//			sql.append(" and type = ? ");
//			params.add(type);
//		}
//		
//		return find(sql.toString(),params.toArray());
//	}
	
	/**
	 * 通过剧集id和type list获取所有的片段
	 * @param episode_id
	 * @param types
	 * @return
	 */
	public List<Element> queryFidAllByEpisode2(Long episode_id,String type){
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);		
		if(episode_id!=null){
			sql.append(" and episode_id=? ");
			params.add(episode_id);
		}		
		if(StringUtils.isNotEmpty(type)){
			sql.append(" and type = ? ");
			params.add(type);
		}
		sql.append(" order by  if(start_time is not null,start_time,2147483647)");
		return find(sql.toString(),params.toArray());
	}
	

	/**
	 * 统计这个节目下各个剧集的各种类型的数量
	 * @param program_id
	 */
	public Map<String,Long> groupMapEpisodeByProgram(Long program_id) {
		List<Element> list = groupEpisodeByProgram(program_id);
		Map<String,Long> m = new HashMap<String,Long>();
		if(list!=null && !list.isEmpty()){
			Element obj = null;
			for(int i=0;i<list.size();i++){
				obj = list.get(i);
				m.put(obj.getEpisode_id()+"#"+obj.getType(),obj.getLong("count"));
			}
		}
		return m;
	}
	
	
	
	/**
	 * 统计这个节目下各个剧集的各种类型的数量
	 * @param program_id
	 */
	public List<Element> groupEpisodeByProgram(Long program_id) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select episode_id,type,count(distinct fid) as count from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(program_id!=null){
			sql.append(" and program_id=? ");
			params.add(program_id);
		}
		
		sql.append(" group by episode_id,type ");
		return find(sql.toString(),params.toArray());	
	}
	
	
	/**
	 * 通过剧集id删除
	 * @param episode_id
	 */
	public void deleteByEpisodeId(Long episode_id) {
		StringBuilder sql = new StringBuilder("update element set status=?,updated_at=? where episode_id=?");
		List<Object> params = new ArrayList<Object>();
		
		params.add(Cons.STATUS_INVALID);
		params.add(new Timestamp(System.currentTimeMillis()));
		params.add(episode_id);
		
		Db.update(sql.toString(),params.toArray());
	}

	/**
	 * 通过剧集id或场景id获取所有的片段
	 * @param episode_id
	 * @param scene_id
	 * @return
	 */
	public List<Element> queryAllByEpisodeOrSceneId(Long episode_id,Long scene_id){
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(scene_id!=null){
			sql.append(" and scene_id=? ");
			params.add(scene_id);
		}else if(episode_id!=null){
			sql.append(" and episode_id=? ");
			params.add(episode_id);
		}
		sql.append(" and start_time!='' and end_time!=''");
		sql.append(" order by start_time ");
		return paginate(1,Cons.MAX_PAGE_SIZE,"select * ", sql.toString(),params.toArray()).getList();
	}
	//类型字段
	public static enum ElementType {
		person,mec_person,baike,lines, music,video,weibo,fact,question;
	}
	
	
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public Element setType(java.lang.String type){
		super.set("type",type);
		return this;
	}
	public java.lang.Long getFid(){
		return this.getLong("fid");
	}
	
	public Element setFid(java.lang.Long fid){
		super.set("fid",fid);
		return this;
	}
	
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public Element setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getCover(){
		return this.getStr("cover");
	}
	
	public Element setCover(java.lang.String cover){
		super.set("cover",cover);
		return this;
	}
	public java.lang.Long getProgram_id(){
		return this.getLong("program_id");
	}
	
	public Element setProgram_id(java.lang.Long program_id){
		super.set("program_id",program_id);
		return this;
	}
	public java.lang.Long getEpisode_id(){
		return this.getLong("episode_id");
	}
	
	public Element setEpisode_id(java.lang.Long episode_id){
		super.set("episode_id",episode_id);
		return this;
	}
	public java.lang.Long getScene_id(){
		return this.getLong("scene_id");
	}
	
	public Element setScene_id(java.lang.Long scene_id){
		super.set("scene_id",scene_id);
		return this;
	}
	public java.lang.Long getStart_time(){
		return this.getLong("start_time");
	}
	
	public Element setStart_time(java.lang.Long start_time){
		super.set("start_time",start_time);
		return this;
	}
	public java.lang.Long getEnd_time(){
		return this.getLong("end_time");
	}
	
	public Element setEnd_time(java.lang.Long end_time){
		super.set("end_time",end_time);
		return this;
	}
	public java.lang.Long getDuration(){
		return this.getLong("duration");
	}
	
	public Element setDuration(java.lang.Long duration){
		super.set("duration",duration);
		return this;
	}
	//原唱，翻唱，片尾曲，片首曲
	public java.lang.String getTag(){
		return this.getStr("tag");
	}
	
	public Element setTag(java.lang.String tag){
		super.set("tag",tag);
		return this;
	}
	
	
	public java.lang.String getContent(){
		return this.getStr("content");
	}
	
	public Element setContent(java.lang.String content){
		super.set("content",content);
		return this;
	}
	
	public java.lang.String getUrl(){
		return this.getStr("url");
	}
	
	public Element setUrl(java.lang.String url){
		super.set("url",url);
		return this;
	}
	
	/**
	 * app端显示的代码
	 * @param video_dealy_time
	 * @return
	 */
	public java.lang.Long getApp_start_time(int video_dealy_time){
		Long start_time = this.getLong("start_time");
		if(video_dealy_time!=0 && ElementType.video.toString().equals(getType())){
			if(start_time!=null){
				start_time = start_time + video_dealy_time;
			}
		}
		return start_time;
	}
}