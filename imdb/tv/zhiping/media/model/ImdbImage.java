package tv.zhiping.media.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-17
 */
@SuppressWarnings("serial")
public class ImdbImage extends BasicModel<ImdbImage> {

	public static final ImdbImage dao = new ImdbImage();
	
	
	public ImdbImage queryByUrl(String fid,String type,String url) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		sql.append(" and fid=?");
		params.add(fid);
		
		sql.append(" and type=?");
		params.add(type);
		
		sql.append(" and url=?");
		params.add(url);
		
		return findFirst(sql.toString(), params.toArray());
	}

	public java.lang.String getRid(){
		return this.getStr("rid");
	}
	
	public ImdbImage setRid(java.lang.String rid){
		super.set("rid",rid);
		return this;
	}
	
	public java.lang.String getFid(){
		return this.getStr("fid");
	}
	
	public ImdbImage setFid(java.lang.String fid){
		if(StringUtils.isNoneBlank(fid)){
			super.set("fid",fid);
		}
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public ImdbImage setType(java.lang.String type){
		if(StringUtils.isNoneBlank(type)){
			super.set("type",type);
		}
		return this;
	}
	
	public java.lang.Long getHeight(){
		return this.getLong("height");
	}
	
	public ImdbImage setHeight(java.lang.Long height){
		super.set("height",height);
		return this;
	}
	public java.lang.Long getWidth(){
		return this.getLong("width");
	}
	
	public ImdbImage setWidth(java.lang.Long width){
		super.set("width",width);
		return this;
	}
	public java.lang.String getUrl(){
		return this.getStr("url");
	}
	
	public ImdbImage setUrl(java.lang.String url){
		super.set("url",url);
		return this;
	}
	public java.lang.String getFilename(){
		return this.getStr("filename");
	}
	
	public ImdbImage setFilename(java.lang.String filename){
		super.set("filename",filename);
		return this;
	}
}