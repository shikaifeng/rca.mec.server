package tv.zhiping.media.model;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.jfinal.NoIdModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-18
 */
@SuppressWarnings("serial")
public class ImdbProduct extends NoIdModel<ImdbProduct> {

	public static final ImdbProduct dao = new ImdbProduct();

	public String getId(){
		return super.getStr("id");
	}

	public ImdbProduct setId(String id){
		super.set("id",id);
		return this;
	}
	
	public java.lang.String getFid(){
		return this.getStr("fid");
	}
	
	public ImdbProduct setFid(java.lang.String fid){
		if(StringUtils.isNoneBlank(fid)){
			super.set("fid",fid);
		}
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public ImdbProduct setType(java.lang.String type){
		if(StringUtils.isNoneBlank(type)){
			super.set("type",type);
		}
		return this;
	}
	public java.lang.String getAmazon_marketplace_id(){
		return this.getStr("amazon_marketplace_id");
	}
	
	public ImdbProduct setAmazon_marketplace_id(java.lang.String amazon_marketplace_id){
		if(StringUtils.isNoneBlank(amazon_marketplace_id)){
			super.set("amazon_marketplace_id",amazon_marketplace_id);
		}
		return this;
	}
	public java.lang.String getKey(){
		return this.getStr("key");
	}
	
	public ImdbProduct setKey(java.lang.String key){
		if(StringUtils.isNoneBlank(key)){
			super.set("key",key);
		}
		return this;
	}
	public java.lang.String getKey_type(){
		return this.getStr("key_type");
	}
	
	public ImdbProduct setKey_type(java.lang.String key_type){
		if(StringUtils.isNoneBlank(key_type)){
			super.set("key_type",key_type);
		}
		return this;
	}
	public java.lang.String getRegion(){
		return this.getStr("region");
	}
	
	public ImdbProduct setRegion(java.lang.String region){
		if(StringUtils.isNoneBlank(region)){
			super.set("region",region);
		}
		return this;
	}
}