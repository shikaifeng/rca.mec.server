package tv.zhiping.mdm.model;

import java.io.Serializable;


public class TProgram implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final TProgram dao = new TProgram();
	  protected String tableName = "tprogram";
	    
	    //分批查询
	    protected static final Integer _SQL_ITERATOR_IN_COUNT = 500;
	    
	    protected final static String LIKE = "%";
	    
	private Long id;
	private java.lang.String title; 
	private java.lang.String summary; 
	private java.sql.Timestamp startTime; 
	private java.sql.Timestamp endTime; 
	private java.lang.Long estimateCount; 
	private java.lang.String url; 
	private java.lang.Long winDay; 
	private java.lang.Long winCount; 
	private java.lang.Boolean status; 
	private java.sql.Timestamp createdAt; 
	private java.sql.Timestamp updatedAt; 

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public java.lang.String getTitle(){
		return title;
	}
	public void setTitle(java.lang.String title){
		this.title = title;
	}
	public java.lang.String getSummary(){
		return summary;
	}
	public void setSummary(java.lang.String summary){
		this.summary = summary;
	}
	public java.sql.Timestamp getStartTime(){
		return startTime;
	}
	public void setStartTime(java.sql.Timestamp startTime){
		this.startTime = startTime;
	}
	public java.sql.Timestamp getEndTime(){
		return endTime;
	}
	public void setEndTime(java.sql.Timestamp endTime){
		this.endTime = endTime;
	}
	public java.lang.Long getEstimateCount(){
		return estimateCount;
	}
	public void setEstimateCount(java.lang.Long estimateCount){
		this.estimateCount = estimateCount;
	}
	public java.lang.String getUrl(){
		return url;
	}
	public void setUrl(java.lang.String url){
		this.url = url;
	}
	public java.lang.Long getWinDay(){
		return winDay;
	}
	public void setWinDay(java.lang.Long winDay){
		this.winDay = winDay;
	}
	public java.lang.Long getWinCount(){
		return winCount;
	}
	public void setWinCount(java.lang.Long winCount){
		this.winCount = winCount;
	}
	public java.lang.Boolean getStatus(){
		return status;
	}
	public void setStatus(java.lang.Boolean status){
		this.status = status;
	}
	public java.sql.Timestamp getCreatedAt(){
		return createdAt;
	}
	public void setCreatedAt(java.sql.Timestamp createdAt){
		this.createdAt = createdAt;
	}
	public java.sql.Timestamp getUpdatedAt(){
		return updatedAt;
	}
	public void setUpdatedAt(java.sql.Timestamp updatedAt){
		this.updatedAt = updatedAt;
	}
}
