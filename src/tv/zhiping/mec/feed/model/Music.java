package tv.zhiping.mec.feed.model;

import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.jfinal.SysBasicModel;
import tv.zhiping.mdm.model.Album;
import tv.zhiping.mdm.model.Song;

import com.alibaba.fastjson.JSONObject;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-05-20
 */
@SuppressWarnings("serial")
public class Music extends SysBasicModel<Music> {

	public static final Music dao = new Music();
	
	/**
	 * 解析json数据
	 * @param json
	 */
	public void parseMusicFeedJson(JSONObject json) {
		json.put("singer",this.getSinger());
		json.put("url",this.getSource_url());
		json.put("cover",ComUtil.getStHttpPath(this.getCover()));
		json.put("title",this.getTitle());
	}
	/**
	 * 根据节目id查询某节目中的所有音乐
	 */
	public List<Music> queryByProgramId(Long program_id){
		List<Object> params = new ArrayList<Object>();
		String sql = "select m.* from "+tableName+" m inner join element e on m.id=e.fid where e.type=? and e.program_id=? and m.status=? and e.status=?";
		params.add(Element.ElementType.music.toString());
		params.add(program_id);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		
		List<Music> result = find(sql, params.toArray());	
		return result;
	}
	
	/**
	 * 根据名称查询歌曲信息
	 * @param name
	 * @return
	 */
	public Music queryByTitle(String title){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and title=?";
		params.add(Cons.STATUS_VALID);
		params.add(title);
		
		return findFirst(sql, params.toArray());
	}
	
	/**
	 * 根据来源查询歌曲信息
	 * @param name
	 * @return
	 */
	public Music queryBySourceUrl(String source_url){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and source_url=?";
		params.add(Cons.STATUS_VALID);
		params.add(source_url);
		
		return findFirst(sql, params.toArray());
	}
	
	/**
	 * 根据标题，歌唱者查询歌曲信息
	 * @param name
	 * @return
	 */
	public Music queryByNameSinger(String title,String singer){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and title=? and singer=?";
		params.add(Cons.STATUS_VALID);
		params.add(title);
		params.add(singer);
		
		return findFirst(sql, params.toArray());
	}
	
	
//	/**
//	 * 根据歌曲id，播放歌曲地址 查询歌曲信息
//	 * @param name
//	 * @return
//	 */
//	public Music queryBySongIdOrSourceUrl(Long songId,String play_url){
//		List<Object> params = new ArrayList<Object>();
//		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
//		params.add(Cons.STATUS_VALID);
//		
//		if(StringUtils.isNoneBlank(play_url)){
//			sql.append(" and source_url=?");
//			params.add(play_url);
//		}else if(songId!=null){
//			sql.append(" and song_id=?");
//			params.add(songId);
//		}
//	
//		return findFirst(sql.toString(), params.toArray());
//	}

	
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public Music setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getOrg_title(){
		return this.getStr("org_title");
	}
	
	public Music setOrg_title(java.lang.String org_title){
		super.set("org_title",org_title);
		return this;
	}
	public java.lang.String getCover(){
		return this.getStr("cover");
	}
	
	public Music setCover(java.lang.String cover){
		super.set("cover",cover);
		return this;
	}
	public java.lang.String getLyricist(){
		return this.getStr("lyricist");
	}
	
	public Music setLyricist(java.lang.String lyricist){
		super.set("lyricist",lyricist);
		return this;
	}
	public java.lang.String getComposer(){
		return this.getStr("composer");
	}
	
	public Music setComposer(java.lang.String composer){
		super.set("composer",composer);
		return this;
	}
	public java.lang.String getSinger(){
		return this.getStr("singer");
	}
	
	public Music setSinger(java.lang.String singer){
		super.set("singer",singer);
		return this;
	}
	public java.lang.String getLyric(){
		return this.getStr("lyric");
	}
	
	public Music setLyric(java.lang.String lyric){
		super.set("lyric",lyric);
		return this;
	}
	public java.lang.String getSummary(){
		return this.getStr("summary");
	}
	
	public Music setSummary(java.lang.String summary){
		super.set("summary",summary);
		return this;
	}
	public java.lang.String getFile_type(){
		return this.getStr("file_type");
	}
	
	public Music setFile_type(java.lang.String file_type){
		super.set("file_type",file_type);
		return this;
	}
	public java.lang.String getSource_url(){
		return this.getStr("source_url");
	}
	
	public Music setSource_url(java.lang.String source_url){
		super.set("source_url",source_url);
		return this;
	}
	public java.lang.String getPath(){
		return this.getStr("path");
	}
	
	public Music setPath(java.lang.String path){
		super.set("path",path);
		return this;
	}
	
	public java.lang.String getImages(){
		return this.getStr("images");
	}
	
	public Music setImages(java.lang.String images){
		super.set("images",images);
		return this;
	}
	
	public java.lang.String getTag(){
		return this.getStr("tag");
	}
	
	public Music setTag(java.lang.String tag){
		super.set("tag",tag);
		return this;
	}
	
	
	public Long getSong_id(){
		return super.getLong("song_id");
	}

	public Music setSong_id(Long song_id){
		super.set("song_id",song_id);
		return (Music) this;
	}
	
	/**
	 * 歌曲转成json
	 * @param m
	 * @param json
	 */
	public void parseMusic2Json(JSONObject json){
		String cover = null;
		String title = null;
		String singer = null;
		String url = null;
		Long id = null;
		
		Song song = null;
		
		id = this.getId();
		cover = this.getCover();
		title = this.getTitle();
		singer = this.getSinger();
		url = this.getSource_url();
		song = Song.dao.queryByIdOrSourceUrl(this.getSong_id(),this.getSource_url());
		
		
		if(song!=null){
			Album album = Album.dao.findById(song.getPid());
			if(album!=null){
				cover = album.getView_cover();
			}
			title = song.getTitle();
			singer = song.getSinger();
			url = song.getPlay_url();
			if(this.getSong_id() == null){//自动补充
				this.setSong_id(song.getId());
				this.setUpdDef();
				this.update();
			}
		}
		
		json.put("id", id);
		json.put("cover",ComUtil.getStHttpPath(cover));
		json.put("title",title);
		json.put("url",url);
		json.put("singer",singer);
	}
}