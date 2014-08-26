package tv.zhiping.media.imdb.ctrl;

import java.io.IOException;

import tv.zhiping.mec.feed.ctrl.FeedBaseCtrl;
import tv.zhiping.media.imdb.match.service.MatchImdbPersonService;
import tv.zhiping.media.imdb.match.service.MatchImdbProgramService;
import tv.zhiping.media.imdb.match.service.MatchImdbSceneService;
import tv.zhiping.media.imdb.match.service.MatchImdbSoundService;
import tv.zhiping.media.model.ImdbEpisode;
import tv.zhiping.media.model.ImdbEvent;
import tv.zhiping.media.model.ImdbMergeSound;
import tv.zhiping.media.model.ImdbPerson;
import tv.zhiping.media.model.ImdbPersonPrincipalt;
import tv.zhiping.media.model.ImdbPersonVideo;
import tv.zhiping.media.model.ImdbScene;

/**
 * imdb的匹配
 * @author 张有良
 */
public class ImdbMatchCtrl extends FeedBaseCtrl{	
	/**
	 * App系统配置 index
	 * */
	public void index(){
		renderJsp("/page/imdb/imdbMatch/input.jsp");
	}
	
	//歌曲匹配
	private MatchImdbSoundService matchImdbSoundService = new MatchImdbSoundService();
	
	//节目匹配
	private MatchImdbProgramService matchImdbProgramService = new MatchImdbProgramService();
	
	//人物匹配
	private MatchImdbPersonService matchImdbPersonService = new MatchImdbPersonService();
	
	//场景匹配
	private MatchImdbSceneService matchImdbSceneService = new MatchImdbSceneService();
	
	public void save() throws IOException{
		StringBuilder msg = new StringBuilder();
		
		backFail2Wait();//先恢复匹配失败的，重新去匹配
		
		matchImdbProgramService.matchMovie();
		matchImdbProgramService.matchTvSeries();
		
		matchImdbPersonService.matchPerson();//人物匹配 人物修复了：需要重新去匹配相关场景信息，节目和明星关系数据，主要演员信息,然后重新去匹配
		matchImdbPersonService.matchPersonVideoRelation();//人物剧集匹配video_id=0 person_id=0匹配不上
		matchImdbPersonService.matchPersonPrincipalt();//主角匹配,重新执行
		
		matchImdbSoundService.mergeSound();
		matchImdbSoundService.matchSound();//歌曲修复，他相关的歌曲都重新匹配下
		matchImdbSoundService.matchSoundAlbum();
		
		matchImdbSceneService.matchScene();
		
		if(msg.length() == 0){
			msg.append("搞定");
		}
		renderText(msg.toString());
	}
	
	
	/**
	 * 恢复待处理状态
	 *
		update media.imdb_episode set match_state=-1 where match_state=2;
		update media.imdb_person set match_state=-1 where match_state=2;
		update media.imdb_person_video set match_state=-1 where match_state=2;
	
		update media.imdb_merge_sound set match_state=-1 where match_state=2;
		update media.imdb_sound_album set match_state=-1 where match_state=2;
	
		update media.imdb_scene a,media.imdb_event b set a.match_state=-1 where a.id=b.scene_id and b.match_state=2
		update media.imdb_event set match_state=-1 where match_state=2;
	 * @param episode_id
	 */
	public void backFail2Wait(){
		ImdbEpisode.dao.backFail2Wait();
		ImdbPerson.dao.backFail2Wait();
		ImdbPersonVideo.dao.backFail2Wait();
		ImdbPersonPrincipalt.dao.backFail2Wait();
		ImdbMergeSound.dao.backFail2Wait();
		ImdbScene.dao.backFail2Wait();
		ImdbEvent.dao.backFail2Wait();
	}
	

//	/**
//	 * 处理图片的下载
//	 * @param episode
//	 * @param abs_root_folder
//	 */
//	private void processMovieCover(Episode episode,String abs_root_folder) {
//		String absSrcAvatar = abs_root_folder+"/"+episode.getCover();
//		//images/program/2008/132735/cover/1402024162.5.jpg
//		String tarAvatar = "images/program/"+episode.getYear()+"/"+episode.getId()+"/cover/";
//		
//		File file = new File(absSrcAvatar);
//		tarAvatar = tarAvatar+file.getName();
//		
//		String absTargetAvatar = abs_root_folder+"/"+tarAvatar;
//		
//		FileUtil.createParentFilePath(new File(absTargetAvatar));
//		FileUtil.copy(absSrcAvatar, absTargetAvatar);
//		
//		episode.setCover(tarAvatar);
//		episode.update();
//		
//		System.out.println(episode.getId()+" "+episode.getCover()+"  "+absTargetAvatar);
//		System.out.println(episode.getCover());
//	}
}