package tv.zhiping.mec.util.model;

import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;
import tv.zhiping.mec.feed.model.Baike;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-04
 */
@SuppressWarnings("serial")
public class DownHistory extends BasicModel<DownHistory> {

	public static final DownHistory dao = new DownHistory();

	//判断是否已经下载
	public DownHistory queryByUrl(String url) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and url=?";
		params.add(Cons.STATUS_VALID);
		params.add(url);
		
		return findFirst(sql, params.toArray());
	}
	
	//判断是否已经下载
	public DownHistory queryByUrlAndType(String url,String type) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and url=? and type=?";
		params.add(Cons.STATUS_VALID);
		params.add(url);
		params.add(type);
		
		return findFirst(sql, params.toArray());
	}
	
	
	public java.lang.String getUrl(){
		return this.getStr("url");
	}
	
	public DownHistory setUrl(java.lang.String url){
		super.set("url",url);
		return this;
	}
	public java.lang.String getFilename(){
		return this.getStr("filename");
	}
	
	public DownHistory setFilename(java.lang.String filename){
		super.set("filename",filename);
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public DownHistory setType(java.lang.String type){
		super.set("type",type);
		return this;
	}
}