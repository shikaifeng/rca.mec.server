package tv.zhiping.media.model;

import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-16
 */
@SuppressWarnings("serial")
public class ImdbPlot extends BasicModel<ImdbPlot> {

	public static final ImdbPlot dao = new ImdbPlot();

	
	public ImdbPlot queryByObj(String pid,String type,String author){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and pid=? and type=? and author=?";
		params.add(Cons.STATUS_VALID);
		params.add(pid);
		params.add(type);
		params.add(author);
		
		return findFirst(sql, params.toArray());
	}
	
	public java.lang.String getPid(){
		return this.getStr("pid");
	}
	
	public ImdbPlot setPid(java.lang.String pid){
		super.set("pid",pid);
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public ImdbPlot setType(java.lang.String type){
		super.set("type",type);
		return this;
	}
	public java.lang.String getAuthor(){
		return this.getStr("author");
	}
	
	public ImdbPlot setAuthor(java.lang.String author){
		super.set("author",author);
		return this;
	}
	public java.lang.String getText(){
		return this.getStr("text");
	}
	
	public ImdbPlot setText(java.lang.String text){
		super.set("text",text);
		return this;
	}

	public java.lang.String getJson_txt(){
		return this.getStr("json_txt");
	}
	
	public ImdbPlot setJson_txt(java.lang.String json_txt){
		super.set("json_txt",json_txt);
		return this;
	}
}