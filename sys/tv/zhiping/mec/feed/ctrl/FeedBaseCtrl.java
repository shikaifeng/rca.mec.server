package tv.zhiping.mec.feed.ctrl;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.Cons;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.common.util.HttpUtil;
import tv.zhiping.mdm.model.Song;
import tv.zhiping.mec.feed.model.Baike;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.MecPerson;
import tv.zhiping.mec.feed.model.Music;
import tv.zhiping.mec.feed.model.Question;
import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.mec.util.model.DownHistory;
import tv.zhiping.utils.FileUtil;

/**
 * excel导入
 * 
 * @author 张有良
 */

public class FeedBaseCtrl extends SysBaseCtrl {

	/**
	 * 获取答案的序列
	 * 
	 * @param str
	 * @return
	 */
	public int getOptionLen(String[] srr, int start) {
		int len = 0;
		for (int i = start; i < srr.length; i++) {
			if (StringUtils.isBlank(srr[i])) {
				break;
			} else {
				len++;
			}
		}
		return len;
	}


	/**
	 * 获取答案的序列
	 * 
	 * @param str
	 * @return
	 */
	public int getAwardIndex(String str) {
		str = str.toUpperCase();
		return str.charAt(0) - 'A';
	}

	// person/id/avatar
	public void downPersonAvatar(MecPerson person, String partnerPath,
			String upload_file_folder) throws Exception {
		person.setAvatar("images/mec_person/" + person.getId() + "/avatar/"
				+ ComUtil.getRandomFileName(person.getAvatar_mtime()));
		if (ComUtil.isRemotePath(person.getAvatar_mtime())) {// 是远程图片
			String downPath = down(person.getAvatar_mtime(), partnerPath,person.getAvatar(), "mec_person_avatar");
			person.setAvatar(downPath);
		} else {// 是本地图片
			String src = upload_file_folder + "/" + person.getAvatar_mtime();
			File src_file = new File(src);
			if (src_file.exists() && src_file.exists()) {
				FileUtil.copy(src, partnerPath, person.getAvatar());
				person.setAvatar_mtime(null);
			} else {
				throw new Exception("人物" + person.getAvatar_mtime() + "本地图片不存在");
			}
		}
	}

	public void downBaikeCover(Element element, String remote_cover,
			String partnerPath, String upload_file_folder) throws Exception {
		element.setCover("images/baike/"+ ComUtil.getDateIndexSavePath(remote_cover));
		if (ComUtil.isRemotePath(remote_cover)) {// 是远程图片
			String downPath = down(remote_cover, partnerPath,element.getCover(), "baike_cover");
			element.setCover(downPath);
		} else {// 本地图片
			String src = upload_file_folder + "/" + remote_cover;
			File src_file = new File(src);
			if (src_file.exists() && src_file.exists()) {
				FileUtil.copy(src, partnerPath, element.getCover());
			} else {
				throw new Exception("百科" + remote_cover + "本地图片不存在");
			}
		}
	}

	public void downMusic(String partnerPath, String remote_pic,
			Element element, String upload_file_folder) throws Exception {
		element.setCover("images/music/"+ ComUtil.getDateIndexSavePath(remote_pic));// /////////////////////////////////下载
		if (ComUtil.isRemotePath(remote_pic)) {
			String downPath = down(remote_pic, partnerPath, element.getCover(),
					"music_cover");
			element.setCover(downPath);
		} else {
			String src = upload_file_folder + "/" + remote_pic;
			File src_file = new File(src);
			if (src_file.exists() && src_file.exists()) {
				FileUtil.copy(src, partnerPath, element.getCover());
			} else {
				throw new Exception("音乐" + remote_pic + "本地图片不存在");
			}
		}
	}

	public void delHsyByEpisodeId(Long episode_id) {
		Question.dao.deleteByEpisodeId(episode_id);
		delByEpisodeId(episode_id);
	}

	public void delByEpisodeId(Long episode_id) {
		// ElementAddon.dao.deleteByEpisodeId(episode_id);
		Element.dao.deleteByEpisodeId(episode_id);
		Scene.dao.deleteByEpisodeId(episode_id);
	}

	// 设置公共参数
	public void setElementProgram(Long program_id, Long episode_id,
			Element element) {
		element.setProgram_id(program_id);
		element.setEpisode_id(episode_id);
	}

	/**
	 * 保存或修改歌曲
	 * 
	 * @param music_singer
	 * @param music_url
	 * @param element
	 * @param music
	 */
	public void saveOrUpdMusic(String music_singer, String music_url,
			Element element, Music music) {
		setMdmMusicRelation(music_url, music);

		music.setTitle(element.getTitle());
		music.setCover(element.getCover());
		music.setSinger(music_singer);
		music.setSource_url(music_url);

		setLastEditSysUser(music);
		if (music.getId() == null) {
//			music.setAddDef();
			music.save();
		} else {
//			music.setUpdDef();
			music.update();
		}
	}


	public void setMdmMusicRelation(String music_url, Music music) {
		if (StringUtils.isNotBlank(music_url)) {
			Song song = Song.dao.queryByIdOrSourceUrl(null, music_url);
			if (song != null) {
				music.setSong_id(song.getId());
			}
		}
	}

	//下载图片
	public String down(String urlPath ,String partnerPath,String filename,String type) throws Exception{
		String res = null;
		try {
			res = filename;
			DownHistory down = DownHistory.dao.queryByUrlAndType(urlPath,type);
			if(down!=null){//已经下载
				res  = down.getFilename();
			}else{
				HttpUtil.download(urlPath,partnerPath,filename);
				
				down = new DownHistory();
				down.setUrl(urlPath);
				down.setFilename(filename);
				down.setType(type);
				down.setAddDef();
				down.save();
			}
		} catch (Exception e) {
			log.info("下载出错="+urlPath);
			throw e;
		}
		return res;
	}

	// 下载图片
	public String down(String urlPath, String filename, String type)
			throws Exception {
		String partnerPath = "";
		String res = null;
		try {
			res = filename;
			DownHistory down = DownHistory.dao.queryByUrl(urlPath);
			if (down != null) {// 已经下载
				res = down.getFilename();
			} else {
				HttpUtil.download(urlPath, partnerPath, filename);

				down = new DownHistory();
				down.setUrl(urlPath);
				down.setFilename(filename);
				down.setType(type);
				down.setAddDef();
				down.save();
			}
		} catch (Exception e) {
			log.info("下载出错=" + urlPath);
			throw e;
		}
		return res;
	}

	// 添加默认的场景
	protected void saveDefScene(Element element, Long episode_id) {
		Scene scene = new Scene();
		scene.setPid(episode_id);
		scene.setStart_time(0L);
		scene.setEnd_time(new Long(Integer.MAX_VALUE));
		scene.setDuration(scene.getEnd_time() - scene.getStart_time());
		scene.setAddDef();
		scene.save();
		element.setScene_id(scene.getId());
	}

	//复制明星图片
	protected void copyMecPersonAvatar(File src,MecPerson obj) {
//		String avatar = obj.getAvatar();
		if(src != null){
			String path = "images/mec_person/"+obj.getId()+"/"+ComUtil.getRandomFileName(src.getName());
			String target = CacheFun.getConVal(ConfigKeys.ST_LOCAL_FOLDER)+path;
			
			FileUtil.copy(src, target);
			
			obj.setAvatar(path);
			obj.update();
			
			src.delete();//删除
		}
	}

	// 复制百科cover
	public void copyBaikeCover(File src ,Baike obj) {
//		String cover = obj.getCover();
		if (src != null) {
			String path = "images/baike/"+ ComUtil.getDateIndexSavePath(src.getName());
			String target = CacheFun.getConVal(ConfigKeys.ST_LOCAL_FOLDER) + path;
			FileUtil.copy(src, target);
			obj.setCover(path);
			obj.update();
			src.delete();// 删除
		}
	}

	// 复制音乐cover
	public void copyMusicCover(File src,Music obj) {
//		String cover = obj.getCover();
		if (src != null) {
			String path = "images/music/"+ ComUtil.getDateIndexSavePath(src.getName());
			String target = CacheFun.getConVal(ConfigKeys.ST_LOCAL_FOLDER) + path;
			FileUtil.copy(src, target);
			obj.setCover(path);
			obj.update();
			src.delete();// 删除
		}
	}

	/**
	 * 设置元素的场景id，如果没有场景，自动增加一个
	 * 
	 * @param element
	 * @param episode_id
	 */
	protected void setElmentSceneId(Element element, Long episode_id) {
		if (element.getScene_id() == null) {
			List<Scene> scene_list = Scene.dao.queryByEpisode(episode_id);
			if (scene_list != null && !scene_list.isEmpty()) {
				element.setScene_id(scene_list.get(0).getId());
			} else {
				saveDefScene(element, episode_id);
			}
		}
	}
}
