package tv.zhiping.mdm.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-19
 */
@SuppressWarnings("serial")
public class Album extends BasicModel<Album> {

	public static final Album dao = new Album();

	/**
	 * 根据名称查询
	 */
	public List<Album> queryByTitle(String title_en) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and (lower(orig_title)=? or lower(title)=?)");
		params.add(title_en.toLowerCase());
		params.add(title_en.toLowerCase());
		return find(sql.toString(), params.toArray());
	}
	
	
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public Album setTitle(java.lang.String title){
		if(StringUtils.isNoneBlank(title)){
			super.set("title",title);
		}
		return this;
	}
	public java.lang.String getOrig_title(){
		return this.getStr("orig_title");
	}
	
	public Album setOrig_title(java.lang.String orig_title){
		if(StringUtils.isNoneBlank(orig_title)){
			super.set("orig_title",orig_title);
		}
		return this;
	}
	public java.lang.String getAka(){
		return this.getStr("aka");
	}
	
	public Album setAka(java.lang.String aka){
		if(StringUtils.isNoneBlank(aka)){
			super.set("aka",aka);
		}
		return this;
	}
	public java.lang.String getAka_en(){
		return this.getStr("aka_en");
	}
	
	public Album setAka_en(java.lang.String aka_en){
		if(StringUtils.isNoneBlank(aka_en)){
			super.set("aka_en",aka_en);
		}
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public Album setType(java.lang.String type){
		if(StringUtils.isNoneBlank(type)){
			super.set("type",type);
		}
		return this;
	}
	public java.lang.String getYear(){
		return this.getStr("year");
	}
	
	public Album setYear(java.lang.String year){
		if(StringUtils.isNoneBlank(year)){
			super.set("year",year);
		}
		return this;
	}
	public java.lang.String getLanguage(){
		return this.getStr("language");
	}
	
	public Album setLanguage(java.lang.String language){
		if(StringUtils.isNoneBlank(language)){
			super.set("language",language);
		}
		return this;
	}
	public java.sql.Timestamp getRelease_date(){
		return this.getTimestamp("release_date");
	}
	
	public Album setRelease_date(java.sql.Timestamp release_date){
		if(release_date!=null){
			super.set("release_date",release_date);
		}
		return this;
	}
	public java.lang.String getGenre(){
		return this.getStr("genre");
	}
	
	public Album setGenre(java.lang.String genre){
		if(StringUtils.isNoneBlank(genre)){
			super.set("genre",genre);
		}
		return this;
	}
	public java.lang.String getCover(){
		return this.getStr("cover");
	}
	
	public Album setCover(java.lang.String cover){
		if(StringUtils.isNoneBlank(cover)){
			super.set("cover",cover);
		}
		return this;
	}
	public java.lang.String getSummary(){
		return this.getStr("summary");
	}
	
	public Album setSummary(java.lang.String summary){
		if(StringUtils.isNoneBlank(summary)){
			super.set("summary",summary);
		}
		return this;
	}
	public java.lang.String getQq_url(){
		return this.getStr("qq_url");
	}
	
	public Album setQq_url(java.lang.String qq_url){
		if(StringUtils.isNoneBlank(qq_url)){
			super.set("qq_url",qq_url);
		}
		return this;
	}
	public java.lang.String getQq_mid(){
		return this.getStr("qq_mid");
	}
	
	public Album setQq_mid(java.lang.String qq_mid){
		if(StringUtils.isNoneBlank(qq_mid)){
			super.set("qq_mid",qq_mid);
		}
		return this;
	}
	public java.lang.Long getSong_num(){
		return this.getLong("song_num");
	}
	
	public Album setSong_num(java.lang.Long song_num){
		if(song_num!=null){
			super.set("song_num",song_num);
		}
		return this;
	}
	
	public java.lang.String getImage(){
		return this.getStr("image");
	}
	
	public Album setImage(java.lang.String image){
		super.set("image",image);
		return this;
	}
	
	//返回视图层的头像
	public java.lang.String getView_cover(){
		String str = getCover();
		if(StringUtils.isBlank(str)){
			str = getImage();			
		}
		return str;
	}
}