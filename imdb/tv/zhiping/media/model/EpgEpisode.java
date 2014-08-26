package tv.zhiping.media.model;

import tv.zhiping.jfinal.NoIdModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-11
 */
@SuppressWarnings("serial")
public class EpgEpisode extends NoIdModel<EpgEpisode> {

	public static final EpgEpisode dao = new EpgEpisode();

	public String getId(){
		return super.getStr("id");
	}

	public EpgEpisode setId(String id){
		super.set("id",id);
		return this;
	}
	
	public java.lang.String getPid(){
		return this.getStr("pid");
	}
	
	public EpgEpisode setPid(java.lang.String pid){
		super.set("pid",pid);
		return this;
	}
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public EpgEpisode setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getOrig_title(){
		return this.getStr("orig_title");
	}
	
	public EpgEpisode setOrig_title(java.lang.String orig_title){
		super.set("orig_title",orig_title);
		return this;
	}
	public java.lang.String getAka(){
		return this.getStr("aka");
	}
	
	public EpgEpisode setAka(java.lang.String aka){
		super.set("aka",aka);
		return this;
	}
	public java.lang.String getCover(){
		return this.getStr("cover");
	}
	
	public EpgEpisode setCover(java.lang.String cover){
		super.set("cover",cover);
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public EpgEpisode setType(java.lang.String type){
		super.set("type",type);
		return this;
	}
	public java.lang.String getWebsite(){
		return this.getStr("website");
	}
	
	public EpgEpisode setWebsite(java.lang.String website){
		super.set("website",website);
		return this;
	}
	public java.lang.String getYear(){
		return this.getStr("year");
	}
	
	public EpgEpisode setYear(java.lang.String year){
		super.set("year",year);
		return this;
	}
	public java.lang.String getLanguages(){
		return this.getStr("languages");
	}
	
	public EpgEpisode setLanguages(java.lang.String languages){
		super.set("languages",languages);
		return this;
	}
	public java.lang.String getCountry(){
		return this.getStr("country");
	}
	
	public EpgEpisode setCountry(java.lang.String country){
		super.set("country",country);
		return this;
	}
	public java.lang.Long getEpisodes_count(){
		return this.getLong("episodes_count");
	}
	
	public EpgEpisode setEpisodes_count(java.lang.Long episodes_count){
		super.set("episodes_count",episodes_count);
		return this;
	}
	public java.lang.Long getCurrent_episode(){
		return this.getLong("current_episode");
	}
	
	public EpgEpisode setCurrent_episode(java.lang.Long current_episode){
		super.set("current_episode",current_episode);
		return this;
	}
	public java.lang.Long getDuration(){
		return this.getLong("duration");
	}
	
	public EpgEpisode setDuration(java.lang.Long duration){
		super.set("duration",duration);
		return this;
	}
	public java.lang.String getTags(){
		return this.getStr("tags");
	}
	
	public EpgEpisode setTags(java.lang.String tags){
		super.set("tags",tags);
		return this;
	}
	public java.lang.String getImages(){
		return this.getStr("images");
	}
	
	public EpgEpisode setImages(java.lang.String images){
		super.set("images",images);
		return this;
	}
	public java.lang.String getSummary(){
		return this.getStr("summary");
	}
	
	public EpgEpisode setSummary(java.lang.String summary){
		super.set("summary",summary);
		return this;
	}
	public java.lang.String getPhotos(){
		return this.getStr("photos");
	}
	
	public EpgEpisode setPhotos(java.lang.String photos){
		super.set("photos",photos);
		return this;
	}
	public java.lang.String getSeries(){
		return this.getStr("series");
	}
	
	public EpgEpisode setSeries(java.lang.String series){
		super.set("series",series);
		return this;
	}
	public java.lang.String getUrl(){
		return this.getStr("url");
	}
	
	public EpgEpisode setUrl(java.lang.String url){
		super.set("url",url);
		return this;
	}
	public java.lang.String getDownload_url(){
		return this.getStr("download_url");
	}
	
	public EpgEpisode setDownload_url(java.lang.String download_url){
		super.set("download_url",download_url);
		return this;
	}
}