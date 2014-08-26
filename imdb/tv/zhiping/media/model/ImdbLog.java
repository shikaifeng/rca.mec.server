package tv.zhiping.media.model;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-18
 */
@SuppressWarnings("serial")
public class ImdbLog extends BasicModel<ImdbLog> {

	public static final ImdbLog dao = new ImdbLog();

	
	public java.lang.String getLev(){
		return this.getStr("lev");
	}
	
	public ImdbLog setLev(java.lang.String lev){
		if(StringUtils.isNoneBlank(lev)){
			super.set("lev",lev);
		}
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public ImdbLog setType(java.lang.String type){
		if(StringUtils.isNoneBlank(type)){
			super.set("type",type);
		}
		return this;
	}
	public java.lang.String getTxt(){
		return this.getStr("txt");
	}
	
	public ImdbLog setTxt(java.lang.String txt){
		if(StringUtils.isNoneBlank(txt)){
			if(txt.length()>200){
				txt = txt.substring(0,200);
			}
			super.set("txt",txt);
		}
		return this;
	}
}