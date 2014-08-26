package tv.zhiping.mdm.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;
import tv.zhiping.mec.feed.model.Music;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-19
 */
@SuppressWarnings("serial")
public class Song extends BasicModel<Song> {

	public static final Song dao = new Song();

	/**
	 * 根据来源id,播放的url 查询歌曲信息
	 * @param name
	 * @return
	 */
	public Song queryByIdOrSourceUrl(Long song_id,String play_url){
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		 if(song_id!=null){
				sql.append(" and id=?");
				params.add(song_id);
		}else if(StringUtils.isNoneBlank(play_url)){
			sql.append(" and play_url=?");
			params.add(play_url);
		}
		 
		return findFirst(sql.toString(), params.toArray());
	}
	
	/**
	 * 根据名称查询
	 */
	public List<Song> queryByTitle(String title_en) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and lower(title)=?");
		params.add(title_en.toLowerCase());
		return find(sql.toString(), params.toArray());
	}
	
	public java.lang.Long getPid(){
		return this.getLong("pid");
	}
	
	public Song setPid(java.lang.Long pid){
		if(pid!=null){
			super.set("pid",pid);
		}
		return this;
	}
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public Song setTitle(java.lang.String title){
		if(StringUtils.isNoneBlank(title)){
			super.set("title",title);
		}
		return this;
	}
	public java.lang.String getOrig_title(){
		return this.getStr("orig_title");
	}
	
	public Song setOrig_title(java.lang.String orig_title){
		if(StringUtils.isNoneBlank(orig_title)){
			super.set("orig_title",orig_title);
		}
		return this;
	}
	public java.lang.String getLanguage(){
		return this.getStr("language");
	}
	
	public Song setLanguage(java.lang.String language){
		if(StringUtils.isNoneBlank(language)){
			super.set("language",language);
		}
		return this;
	}
	public java.sql.Timestamp getRelease_date(){
		return this.getTimestamp("release_date");
	}
	
	public Song setRelease_date(java.sql.Timestamp release_date){
		if(release_date!=null){
			super.set("release_date",release_date);
		}
		return this;
	}
	public java.lang.String getSinger(){
		return this.getStr("singer");
	}
	
	public Song setSinger(java.lang.String singer){
		if(StringUtils.isNoneBlank(singer)){
			super.set("singer",singer);
		}
		return this;
	}
	public java.lang.String getSinger_url(){
		return this.getStr("singer_url");
	}
	
	public Song setSinger_url(java.lang.String singer_url){
		if(StringUtils.isNoneBlank(singer_url)){
			super.set("singer_url",singer_url);
		}
		return this;
	}
	public java.lang.String getLrc(){
		return this.getStr("lrc");
	}
	
	public Song setLrc(java.lang.String lrc){
		if(StringUtils.isNoneBlank(lrc)){
			super.set("lrc",lrc);
		}
		return this;
	}
	public java.lang.String getQq_url(){
		return this.getStr("qq_url");
	}
	
	public Song setQq_url(java.lang.String qq_url){
		if(StringUtils.isNoneBlank(qq_url)){
			super.set("qq_url",qq_url);
		}
		return this;
	}
	public java.lang.String getPlay_url(){
		return this.getStr("play_url");
	}
	
	public Song setPlay_url(java.lang.String play_url){
		if(StringUtils.isNoneBlank(play_url)){
			super.set("play_url",play_url);
		}
		return this;
	}
	public java.lang.String getQq_mid(){
		return this.getStr("qq_mid");
	}
	
	public Song setQq_mid(java.lang.String qq_mid){
		if(StringUtils.isNoneBlank(qq_mid)){
			super.set("qq_mid",qq_mid);
		}
		return this;
	}
}