package tv.zhiping.media.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-07-02
 */
@SuppressWarnings("serial")
public class ImdbMergeSound extends BasicModel<ImdbMergeSound> {

	public static final ImdbMergeSound dao = new ImdbMergeSound();

	/**
	 * 匹配失败的重新匹配
	 */
	public void backFail2Wait() {
		StringBuilder sql = new StringBuilder("update "+tableName+" set match_state=? where status=? and match_state=?");
		List<Object> params = new ArrayList<Object>();
		params.add(Cons.THREAD_STATE_WAIT);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_FAIL);
		
		Db.use(Cons.DB_NAME_MEDIA).update(sql.toString(),params.toArray());
	}
	
	/**
	 * 歌曲匹配失败的
	 * @return
	 */
	public List<ImdbMergeSound> queryMatchErrorAll() {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" ");
		sql.append("where status=? and match_state=?");
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_FAIL);
		
		sql.append("  order by id asc");
		return find(sql.toString(),params.toArray());
	}
	
	/**
	 * 根据名称查询
	 * @param product_key
	 * @param name
	 * @return
	 */
	public ImdbMergeSound queryByProductKeyName(String product_key, String name) {
		ImdbMergeSound sound = null;
		if(StringUtils.isNotBlank(product_key)){
			sound = queryByProductKey(product_key);
		}else if(StringUtils.isNotBlank(name)){
			sound = queryByName(name);
		}
		return sound;
	}
	
	/**
	 * 根据名称查询
	 * @param product_key
	 * @param name
	 * @return
	 */
	public ImdbMergeSound queryByName(String name) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and name=?";
		params.add(Cons.STATUS_VALID);
		params.add(name);
		return findFirst(sql, params.toArray());
	}
	
	/**
	 * 根据产品key
	 * @param product_key
	 * @return
	 */
	public ImdbMergeSound queryByProductKey(String product_key) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and product_key=? ";
		params.add(Cons.STATUS_VALID);
		params.add(product_key);
		
		return findFirst(sql, params.toArray());
	}
	
	/**
	 * 分页查询
	 */
	public Page<ImdbMergeSound> paginateByWiatMatch(int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("from "+tableName);
		sql.append(" where status=? and match_state=?");
		params.add(Cons.STATUS_VALID);
		
		params.add(Cons.THREAD_STATE_WAIT);
		return paginate(pageNumber, pageSize, "select *", sql.toString(),params.toArray());
	}
	
	
	public void parseSoundItem(ImdbSoundItem imdb){
		this.setName(imdb.getName());
		this.setSummary(imdb.getSummary());
		this.setArtist(imdb.getArtist());
		this.setImage(imdb.getImage());
		this.setR_image(imdb.getR_image());
		this.setProduct_key(imdb.getProduct_key());
	}
	
	
	
	public java.lang.String getName(){
		return this.getStr("name");
	}
	
	public ImdbMergeSound setName(java.lang.String name){
		if(StringUtils.isNotBlank(name)){
			super.set("name",name);
		}
		return this;
	}
	public java.lang.String getSummary(){
		return this.getStr("summary");
	}
	
	public ImdbMergeSound setSummary(java.lang.String summary){
		if(StringUtils.isNotBlank(summary)){
			super.set("summary",summary);
		}
		return this;
	}
	public java.lang.String getArtist(){
		return this.getStr("artist");
	}
	
	public ImdbMergeSound setArtist(java.lang.String artist){
		if(StringUtils.isNotBlank(artist)){
			super.set("artist",artist);
		}
		return this;
	}
	public java.lang.String getImage(){
		return this.getStr("image");
	}
	
	public ImdbMergeSound setImage(java.lang.String image){
		if(StringUtils.isNotBlank(image)){
			super.set("image",image);
		}
		return this;
	}
	public java.lang.String getR_image(){
		return this.getStr("r_image");
	}
	
	public ImdbMergeSound setR_image(java.lang.String r_image){
		if(StringUtils.isNotBlank(r_image)){
			super.set("r_image",r_image);
		}
		return this;
	}
	public java.lang.String getProduct_key(){
		return this.getStr("product_key");
	}
	
	public ImdbMergeSound setProduct_key(java.lang.String product_key){
		if(StringUtils.isNotBlank(product_key)){
			super.set("product_key",product_key);
		}
		return this;
	}
	public java.lang.Integer getMatch_state(){
		return this.getInt("match_state");
	}
	
	public ImdbMergeSound setMatch_state(java.lang.Integer match_state){
		if(match_state!=null){
			super.set("match_state",match_state);
		}
		return this;
	}
	public java.lang.Long getMatch_id(){
		return this.getLong("match_id");
	}
	
	public ImdbMergeSound setMatch_id(java.lang.Long match_id){
		if(match_id!=null){
			super.set("match_id",match_id);
		}
		return this;
	}
	public java.lang.String getMsg(){
		return this.getStr("msg");
	}
	
	public ImdbMergeSound setMsg(java.lang.String msg){
		if(StringUtils.isNotBlank(msg)){
			if(msg.length()>200){
				msg = msg.substring(0,200);
			}
			super.set("msg",msg);
		}
		return this;
	}
}