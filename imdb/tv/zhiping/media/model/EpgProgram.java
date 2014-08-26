package tv.zhiping.media.model;

import tv.zhiping.jfinal.NoIdModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-11
 */
@SuppressWarnings("serial")
public class EpgProgram extends NoIdModel<EpgProgram> {

	public static final EpgProgram dao = new EpgProgram();

	
	public String getId(){
		return super.getStr("id");
	}

	public EpgProgram setId(String id){
		super.set("id",id);
		return this;
	}
	
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public EpgProgram setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getOrig_title(){
		return this.getStr("orig_title");
	}
	
	public EpgProgram setOrig_title(java.lang.String orig_title){
		super.set("orig_title",orig_title);
		return this;
	}
	public java.lang.String getAka(){
		return this.getStr("aka");
	}
	
	public EpgProgram setAka(java.lang.String aka){
		super.set("aka",aka);
		return this;
	}
	public java.lang.String getAka_en(){
		return this.getStr("aka_en");
	}
	
	public EpgProgram setAka_en(java.lang.String aka_en){
		super.set("aka_en",aka_en);
		return this;
	}
	public java.lang.String getCover(){
		return this.getStr("cover");
	}
	
	public EpgProgram setCover(java.lang.String cover){
		super.set("cover",cover);
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public EpgProgram setType(java.lang.String type){
		super.set("type",type);
		return this;
	}
	public java.lang.String getWebsite(){
		return this.getStr("website");
	}
	
	public EpgProgram setWebsite(java.lang.String website){
		super.set("website",website);
		return this;
	}
	public java.lang.String getYear(){
		return this.getStr("year");
	}
	
	public EpgProgram setYear(java.lang.String year){
		super.set("year",year);
		return this;
	}
	public java.lang.String getLanguages(){
		return this.getStr("languages");
	}
	
	public EpgProgram setLanguages(java.lang.String languages){
		super.set("languages",languages);
		return this;
	}
	public java.lang.String getCountry(){
		return this.getStr("country");
	}
	
	public EpgProgram setCountry(java.lang.String country){
		super.set("country",country);
		return this;
	}
	public java.lang.Long getPer_duration(){
		return this.getLong("per_duration");
	}
	
	public EpgProgram setPer_duration(java.lang.Long per_duration){
		super.set("per_duration",per_duration);
		return this;
	}
	public java.lang.String getGenres(){
		return this.getStr("genres");
	}
	
	public EpgProgram setGenres(java.lang.String genres){
		super.set("genres",genres);
		return this;
	}
	public java.lang.Long getSeasons_count(){
		return this.getLong("seasons_count");
	}
	
	public EpgProgram setSeasons_count(java.lang.Long seasons_count){
		super.set("seasons_count",seasons_count);
		return this;
	}
	public java.lang.Long getCurrent_season(){
		return this.getLong("current_season");
	}
	
	public EpgProgram setCurrent_season(java.lang.Long current_season){
		super.set("current_season",current_season);
		return this;
	}
	public java.lang.Long getEpisodes_count(){
		return this.getLong("episodes_count");
	}
	
	public EpgProgram setEpisodes_count(java.lang.Long episodes_count){
		super.set("episodes_count",episodes_count);
		return this;
	}
	public java.lang.String getUrl(){
		return this.getStr("url");
	}
	
	public EpgProgram setUrl(java.lang.String url){
		super.set("url",url);
		return this;
	}
	public java.lang.String getTags(){
		return this.getStr("tags");
	}
	
	public EpgProgram setTags(java.lang.String tags){
		super.set("tags",tags);
		return this;
	}
	public java.lang.String getImages(){
		return this.getStr("images");
	}
	
	public EpgProgram setImages(java.lang.String images){
		super.set("images",images);
		return this;
	}
	public java.lang.String getSummary(){
		return this.getStr("summary");
	}
	
	public EpgProgram setSummary(java.lang.String summary){
		super.set("summary",summary);
		return this;
	}
	public java.lang.String getPhotos(){
		return this.getStr("photos");
	}
	
	public EpgProgram setPhotos(java.lang.String photos){
		super.set("photos",photos);
		return this;
	}
	public java.lang.Boolean getMatch(){
		return this.getBoolean("match");
	}
	
	public EpgProgram setMatch(java.lang.Boolean match){
		super.set("match",match);
		return this;
	}
}