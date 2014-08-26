package tv.zhiping.media.imdb.match.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import tv.zhiping.common.Cons;
import tv.zhiping.mdm.model.Album;
import tv.zhiping.mdm.model.Song;
import tv.zhiping.media.model.ImdbMergeSound;
import tv.zhiping.media.model.ImdbSoundAlbum;
import tv.zhiping.media.model.ImdbSoundItem;

import com.jfinal.plugin.activerecord.Page;

public class MatchImdbSoundService {
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 合并歌曲
	 */
	public void mergeSound(){
		while(true){
			Page<ImdbSoundItem> page = ImdbSoundItem.dao.paginateByWiatMatch(1,Cons.MAX_PAGE_SIZE);
			List<ImdbSoundItem> list = page.getList();
			if(list==null || list.isEmpty()){
				break;
			}else{
				for(int i=0;i<list.size();i++){
					ImdbSoundItem imdb = list.get(i);
					imdb.setMatch_state(Cons.THREAD_STATE_WAIT);
					imdb.setUpdDef();
					imdb.update();
					
					ImdbMergeSound sound = ImdbMergeSound.dao.queryByProductKeyName(imdb.getProduct_key(),imdb.getName());
					if(sound == null){
						sound = new ImdbMergeSound();
					}
					sound.parseSoundItem(imdb);
					
					if(sound.getId() != null){
						sound.setUpdDef();
						sound.update();
					}else{
						sound.setMatch_state(Cons.THREAD_STATE_WAIT);
						sound.setAddDef();
						sound.save();
					}
					imdb.setMatch_id(sound.getId());
					imdb.setMatch_state(Cons.THREAD_STATE_SUC);
					
					imdb.setUpdDef();
					imdb.update();
				}
			}
		}
	}
	
	/**
	 * 匹配歌曲
	 */
	public void matchSound(){
		while(true){
			Page<ImdbMergeSound> page = ImdbMergeSound.dao.paginateByWiatMatch(1,Cons.MAX_PAGE_SIZE);
			List<ImdbMergeSound> list = page.getList();
			if(list==null || list.isEmpty()){
				break;
			}else{
				for(int i=0;i<list.size();i++){
					ImdbMergeSound imdb = list.get(i);
					
					List<Song> mdms = Song.dao.queryByTitle(imdb.getName());
					imdb.setMatch_state(Cons.THREAD_STATE_FAIL);
					
					if(mdms!=null && !mdms.isEmpty()){
						Song song = null;
						if(mdms.size()>1){
							imdb.setMsg("歌曲名称关联 有多个"+logSong(mdms));
							song = queryMpnSong(mdms,imdb);
						}else{
							song = mdms.get(0);
						}
						if(song!=null){
							imdb.setMatch_id(song.getId());
							imdb.setMatch_state(Cons.THREAD_STATE_SUC);							
						}
					}
					imdb.setUpdDef();
					imdb.update();
				}
			}
		}
	}
	
	/**
	 * 返回最可能的数据
	 * @return
	 */
	private Song queryMpnSong(List<Song> mdms,ImdbMergeSound imdb){
		Song song = null;
		Song max_match_song = null;
		String artist = imdb.getArtist();
		int match_num = 0;
		if(StringUtils.isNotBlank(artist)){
			for(int i=0;i<mdms.size();i++){
				song = mdms.get(i);
				if(artist.equalsIgnoreCase(song.getSinger())){//现在只是去找寻作者相同的
					match_num++;
					max_match_song = song;
				}else{
					String singer = song.getSinger();
					if(singer!=null){
						singer = singer.replace("&","and");
						if(artist.equalsIgnoreCase(singer)){
							match_num++;
							max_match_song = song;
						}
					}
				}
				
			}
		}
		if(match_num == 1){//如果只匹配了一个
			return max_match_song;
		}
		return null;
	}
	
	/**
	 * 匹配专辑
	 */
	public void matchSoundAlbum(){
		while(true){
			Page<ImdbSoundAlbum> page = ImdbSoundAlbum.dao.paginateByWiatMatch(1,Cons.MAX_PAGE_SIZE);
			List<ImdbSoundAlbum> list = page.getList();
			if(list==null || list.isEmpty()){
				break;
			}else{
				for(int i=0;i<list.size();i++){
					ImdbSoundAlbum imdb = list.get(i);
					
					List<Album> mdms = Album.dao.queryByTitle(imdb.getTitle());
					imdb.setMatch_state(Cons.THREAD_STATE_FAIL);
					
					if(mdms!=null && !mdms.isEmpty()){
						if(mdms.size()>1){
							imdb.setMsg("歌曲名称关联 有多个"+logAlbum(mdms));
						}else{
							Album album = mdms.get(0);
							imdb.setMatch_id(album.getId());
							imdb.setMatch_state(Cons.THREAD_STATE_SUC);
						}
					}
					ImdbSoundAlbum db = ImdbSoundAlbum.dao.findById(imdb.getId());
					if(!Cons.THREAD_STATE_SUC.equals(db.getMatch_state())){
						imdb.setUpdDef();
						imdb.update();
					}
					
					imdb.setUpdDef();
					imdb.update();
				}
			}
		}
	}
	
	/**
	 * 歌曲日志
	 * @param list
	 */
	private String logSong(List<Song> list){
		StringBuilder sbuf = new StringBuilder();
		
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Song obj = list.get(i);
				
				sbuf.append("   id="+obj.getId()+" name="+obj.getTitle());
				if(StringUtils.isNotBlank(obj.getOrig_title())){
					sbuf.append(" 英文名="+obj.getOrig_title());
				}
				if(StringUtils.isNotBlank(obj.getQq_url())){
					sbuf.append(" qqurl="+obj.getQq_url());
				}
				sbuf.append("\r\n");
			}
		}
		return sbuf.toString();
	}
	
	
	/**
	 * 专辑日志
	 * @param list
	 */
	private String logAlbum(List<Album> list){
		StringBuilder sbuf = new StringBuilder();
		
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Album obj = list.get(i);
				
				sbuf.append("   id="+obj.getId()+" name="+obj.getTitle());
				if(StringUtils.isNotBlank(obj.getOrig_title())){
					sbuf.append(" 英文名="+obj.getOrig_title());
				}
				if(StringUtils.isNotBlank(obj.getQq_url())){
					sbuf.append(" qqurl="+obj.getQq_url());
				}
				sbuf.append("\r\n");
			}
		}
		return sbuf.toString();
	}
}
