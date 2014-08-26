package tv.zhiping.media.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Page;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.NoIdModel;
import tv.zhiping.mdm.model.Program.ProgramType;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-17
 */
@SuppressWarnings("serial")
public class ImdbSoundItem extends NoIdModel<ImdbSoundItem> {

	public static final ImdbSoundItem dao = new ImdbSoundItem();

	/**
	 * 分页查询
	 */
	public Page<ImdbSoundItem> paginateByWiatMatch(int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("from "+tableName);
		sql.append(" where status=? and match_state=?");
		params.add(Cons.STATUS_VALID);
		
		params.add(Cons.THREAD_STATE_WAIT);
		
		sql.append("  order by id asc");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),params.toArray());
	}
	
	public String getId(){
		return super.getStr("id");
	}

	public ImdbSoundItem setId(String id){
		super.set("id",id);
		return this;
	}
	
	public java.lang.String getName(){
		return this.getStr("name");
	}
	
	public ImdbSoundItem setName(java.lang.String name){
		if(StringUtils.isNoneBlank(name)){
			super.set("name",name);
		}
		return this;
	}
	public java.lang.String getSummary(){
		return this.getStr("summary");
	}
	
	public ImdbSoundItem setSummary(java.lang.String summary){
		if(StringUtils.isNoneBlank(summary)){
			super.set("summary",summary);
		}
		return this;
	}
	public java.lang.String getArtist(){
		return this.getStr("artist");
	}
	
	public ImdbSoundItem setArtist(java.lang.String artist){
		if(StringUtils.isNoneBlank(artist)){
			super.set("artist",artist);
		}
		return this;
	}
	public java.lang.String getImage(){
		return this.getStr("image");
	}
	
	public ImdbSoundItem setImage(java.lang.String image){
		if(StringUtils.isNoneBlank(image)){
			super.set("image",image);
		}
		return this;
	}
	public java.lang.String getR_image(){
		return this.getStr("r_image");
	}
	
	public ImdbSoundItem setR_image(java.lang.String r_image){
		if(StringUtils.isNoneBlank(r_image)){
			super.set("r_image",r_image);
		}
		return this;
	}
	public java.lang.Long getProducts_count(){
		return this.getLong("products_count");
	}
	
	public ImdbSoundItem setProducts_count(java.lang.Long products_count){
		if(products_count!=null){
			super.set("products_count",products_count);
		}
		return this;
	}
	public java.lang.Long getRelated_name_count(){
		return this.getLong("related_name_count");
	}
	
	public ImdbSoundItem setRelated_name_count(java.lang.Long related_name_count){
		if(related_name_count!=null){
			super.set("related_name_count",related_name_count);
		}
		return this;
	}
	
	public ImdbSoundItem setProduct_key(java.lang.String product_key){
		if(StringUtils.isNoneBlank(product_key)){
			super.set("product_key",product_key);
		}
		return this;
	}
	public java.lang.String getProduct_key(){
		return this.getStr("product_key");
	}
	
	public java.lang.String getJson_txt(){
		return this.getStr("json_txt");
	}
	
	public ImdbSoundItem setJson_txt(java.lang.String json_txt){
		if(StringUtils.isNoneBlank(json_txt)){
			super.set("json_txt",json_txt);
		}
		return this;
	}
	public java.lang.Integer getMatch_state(){
		return this.getInt("match_state");
	}
	
	public ImdbSoundItem setMatch_state(java.lang.Integer match_state){
		if(match_state!=null){
			super.set("match_state",match_state);
		}
		return this;
	}

	public java.lang.Long getMatch_id(){
		return this.getLong("match_id");
	}
	
	public ImdbSoundItem setMatch_id(java.lang.Long match_id){
		if(match_id!=null){
			super.set("match_id",match_id);
		}
		return this;
	}

	public java.lang.String getMsg(){
		return this.getStr("msg");
	}
	
	public ImdbSoundItem setMsg(java.lang.String msg){
		if(StringUtils.isNoneBlank(msg)){
			if(msg.length()>200){
				msg = msg.substring(0,200);
			}
			super.set("msg",msg);
		}
		return this;
	}
}