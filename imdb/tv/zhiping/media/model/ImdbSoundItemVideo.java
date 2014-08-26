package tv.zhiping.media.model;

import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-06-17
 */
@SuppressWarnings("serial")
public class ImdbSoundItemVideo extends BasicModel<ImdbSoundItemVideo> {

	public static final ImdbSoundItemVideo dao = new ImdbSoundItemVideo();

	public ImdbSoundItemVideo queryByObj(String sound_item_id,String video_id,String lev){
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+tableName+" where status=? and sound_item_id=? and video_id=? and lev=?";
		params.add(Cons.STATUS_VALID);
		params.add(sound_item_id);
		params.add(video_id);
		params.add(lev);
		
		return findFirst(sql, params.toArray());
	}
	
	
	public java.lang.String getSound_item_id(){
		return this.getStr("sound_item_id");
	}
	
	public ImdbSoundItemVideo setSound_item_id(java.lang.String sound_item_id){
		super.set("sound_item_id",sound_item_id);
		return this;
	}
	public java.lang.String getVideo_id(){
		return this.getStr("video_id");
	}
	
	public ImdbSoundItemVideo setVideo_id(java.lang.String video_id){
		super.set("video_id",video_id);
		return this;
	}
	public java.lang.String getLev(){
		return this.getStr("lev");
	}
	
	public ImdbSoundItemVideo setLev(java.lang.String lev){
		super.set("lev",lev);
		return this;
	}
	public java.lang.String getCharacter_avatar_mtime(){
		return this.getStr("character_avatar_mtime");
	}
	
	public ImdbSoundItemVideo setCharacter_avatar_mtime(java.lang.String character_avatar_mtime){
		super.set("character_avatar_mtime",character_avatar_mtime);
		return this;
	}
}