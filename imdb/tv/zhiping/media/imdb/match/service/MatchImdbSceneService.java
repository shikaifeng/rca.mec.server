package tv.zhiping.media.imdb.match.service;

import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mdm.model.Song;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Element.ElementType;
import tv.zhiping.mec.feed.model.ImdbFact;
import tv.zhiping.mec.feed.model.Music;
import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.mec.util.model.FfmpegHistory;
import tv.zhiping.media.model.ImdbEvent;
import tv.zhiping.media.model.ImdbMergeSound;
import tv.zhiping.media.model.ImdbPerson;
import tv.zhiping.media.model.ImdbScene;
import tv.zhiping.media.model.ImdbSoundItem;

import common.Logger;

public class MatchImdbSceneService {
	private Logger log = Logger.getLogger(this.getClass());
	
	
	/**
	 * 匹配场景
	 */
	public void matchScene(){
		while(true){
			List<ImdbScene> list = ImdbScene.dao.queryByWait();
			if(list==null || list.isEmpty()){
				break;
			}else{
				Program program = null;
				for(int i=0;i<list.size();i++){
					ImdbScene imdbScene = list.get(i);
					imdbScene.setMatch_state(Cons.THREAD_STATE_DEALING);
					
					Long mdm_program_id =imdbScene.getLong("mdm_program_id");
					Long mdm_episode_id = imdbScene.getLong("mdm_episode_id");
					
					if(mdm_program_id == null || Cons.DEF_NULL_NUMBER.equals(mdm_program_id) ||
							mdm_episode_id == null || Cons.DEF_NULL_NUMBER.equals(mdm_episode_id)){
						imdbScene.setMsg("节目数据未匹配 mdm_program_id="+mdm_program_id+" mdm_episode_id="+mdm_episode_id);
						
						imdbScene.setMatch_state(Cons.THREAD_STATE_FAIL);
					}else{
						if(program==null || !program.getId().equals(mdm_program_id)){
							program = Program.dao.findById(mdm_program_id);
						}
						
						if(program!=null){
							log.info("开始匹配场景 节目id="+program.getId()+" 节目名称="+program.getTitle()+" 剧集id="+mdm_episode_id);
							Scene rcaScene = saveOrUpdRcaScene(mdm_episode_id, imdbScene);
							imdbScene.setRca_id(rcaScene.getId());
							
							matchSignScene(program, imdbScene, mdm_program_id, mdm_episode_id,rcaScene);
							
							imdbScene.setMatch_state(Cons.THREAD_STATE_SUC);
						}else{
							imdbScene.setMsg("节目数据未匹配 mdm_program_id="+mdm_program_id+" mdm_episode_id="+mdm_episode_id);
							imdbScene.setMatch_state(Cons.THREAD_STATE_FAIL);
						}				
					}
					
					imdbScene.setUpdDef();
					imdbScene.update();
				}
			}
		}
	}

	/**
	 * 单个场景的匹配，对应多个元素
	 * @param program
	 * @param imdbScene
	 * @param mdm_program_id
	 * @param mdm_episode_id
	 * @param rcaScene
	 */
	private void matchSignScene(Program program, ImdbScene imdbScene,Long mdm_program_id, Long mdm_episode_id, Scene rcaScene) {
		List<ImdbEvent> imdb_events = ImdbEvent.dao.queryBySceneId(imdbScene.getId(),Cons.THREAD_STATE_WAIT);
		for(int j=0;j<imdb_events.size();j++){
			ImdbEvent imdbEvent = imdb_events.get(j);
			
			matchSignEvent(program, mdm_program_id, mdm_episode_id, rcaScene,imdbEvent);
			
			imdbEvent.update();
		}
	}
	
	/**
	 * 单个事件匹配
	 * @param program
	 * @param mdm_program_id
	 * @param mdm_episode_id
	 * @param rcaScene
	 * @param imdbEvent
	 */
	private void matchSignEvent(Program program, Long mdm_program_id,Long mdm_episode_id, Scene rcaScene, ImdbEvent imdbEvent) {
		imdbEvent.updMatchState(Cons.THREAD_STATE_DEALING);
		
		try {
			String type = imdbEvent.getType();
			Element element =  new Element();
			
			parseElement(mdm_program_id, mdm_episode_id, rcaScene, imdbEvent,type, element);
			
//			ElementAddon addon = null;
			boolean save = false;
			
			if(ElementType.person.toString().equals(type)){//人物
				ImdbPerson person = ImdbPerson.dao.findById(imdbEvent.getFid());
				if(person!=null && !Cons.DEF_NULL_NUMBER.equals(person.getMdm_id()) && person.getMdm_id()!=null){
					element.setFid(person.getMdm_id());
					
					//ffmpeg 截图
					element.setCover("images/element/"+program.getYear()+"/"+mdm_program_id+"/"+mdm_episode_id+"/"+(element.getStart_time())+".jpg");
					printTvImg(mdm_episode_id,element);
					save = true;
//					addon = new ElementAddon();
//					addon.setC1(imdbEvent.getC1());
					element.setTag(imdbEvent.getC1());
				}else{
					imdbEvent.setMsg("data errror personId no match:"+imdbEvent.getFid());
				}
			}else if(ElementType.fact.toString().equals(type)){//imdb 百科
				ImdbFact fact = ImdbFact.dao.queryByImdbId(imdbEvent.getFid());
				if(fact!=null){
					save = true;
					element.setFid(fact.getId());
					element.setTitle(fact.getType());
				}else{
					imdbEvent.setMsg("data errror factId:"+imdbEvent.getFid());
				}
			}else if(ElementType.music.toString().equals(type)){//歌曲信息
				Long mdmSoundId = getMdmSoundId(imdbEvent);
				if(mdmSoundId != null){
					Song song = Song.dao.findById(mdmSoundId);
					if(song!=null){
						save = true;
						parseElementMusic(element, song);
					}else{
						imdbEvent.setMsg("data errror sound Id:"+imdbEvent.getFid());
					}
				}else{
					imdbEvent.setMsg("data errror sound Id:"+imdbEvent.getFid());
				}
			}
			
			if(save){
				imdbEvent.setMatch_state(Cons.THREAD_STATE_SUC);
				Element dbElement = Element.dao.queryByObj(element);
				if(dbElement==null){
					element.setAddDef();
					element.save();
//					if(addon != null){
//						addon.setId(element.getId());
//						addon.setAddDef();
//						addon.save();
//					}
					imdbEvent.setRca_id(element.getId());
				}else{
					imdbEvent.setRca_id(dbElement.getId());					
				}
				imdbEvent.setMatch_state(Cons.THREAD_STATE_SUC);
			}else{
				imdbEvent.setMatch_state(Cons.THREAD_STATE_FAIL);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			imdbEvent.setMsg(e.getMessage());
		}
		imdbEvent.update();
	}
	
	/**
	 * 通过imdb的音乐id获取mdm的音乐id
	 * @param imdbEvent
	 * @return
	 */
	private Long getMdmSoundId(ImdbEvent imdbEvent) {
		Long mdmSoundId = null;
		
		ImdbSoundItem sound = ImdbSoundItem.dao.findById(imdbEvent.getFid());
		if(sound!=null && !Cons.DEF_NULL_NUMBER.equals(sound.getMatch_id()) && sound.getMatch_id()!=null){
			ImdbMergeSound mergeSound = ImdbMergeSound.dao.findById(sound.getMatch_id());
			if(mergeSound!=null && mergeSound.getMatch_id()!=null && Cons.DEF_NULL_NUMBER.equals(mergeSound.getMatch_id())){
				mdmSoundId = mergeSound.getMatch_id();
			}
		}
		return mdmSoundId;
	}

	private void parseElement(Long mdm_program_id, Long mdm_episode_id,
			Scene rcaScene, ImdbEvent imdbEvent, String type, Element element) {
		element.setProgram_id(mdm_program_id);
		element.setEpisode_id(mdm_episode_id);
		element.setScene_id(rcaScene.getId());
		element.setStart_time(imdbEvent.getStart_time());
		element.setEnd_time(imdbEvent.getEnd_time());
		element.setDuration(imdbEvent.getDuration());
		element.setType(type);
	}

	/**
	 * 匹配元素的音乐
	 * @param element
	 * @param song
	 * @return
	 */
	private void parseElementMusic(Element element, Song song) {
//		ElementAddon addon = null;
		Music music = Music.dao.queryBySourceUrl(song.getPlay_url());
		if(music == null){
			music = new Music();
//			music.setTitle(song.getTitle());
//			music.setCover();
			music.setSinger(song.getSinger());
			music.setSource_url(song.getPlay_url());
		}
		music.setSong_id(song.getId());
		
		if(music.getId() == null){
			music.setAddDef();
			music.save();					
		}else{
			music.setUpdDef();
			music.update();	
		}
		
		element.setFid(music.getId());
		element.setTitle(music.getTitle());
		element.setCover(music.getCover());
		
//		addon = new ElementAddon();
//		addon.setC3("");//没有片首片尾
//		return addon;
	}

	private Scene saveOrUpdRcaScene(Long mdm_episode_id, ImdbScene imdbScene) {
		Scene rcaScene = Scene.dao.queryByEpisodeTime(mdm_episode_id,imdbScene.getStart_time(),imdbScene.getEnd_time());
		if(rcaScene == null){
			rcaScene = new Scene();
		}
		rcaScene.setPid(mdm_episode_id);
		rcaScene.setTitle(imdbScene.getTitle());
		rcaScene.setStart_time(imdbScene.getStart_time());
		rcaScene.setEnd_time(imdbScene.getEnd_time());
		rcaScene.setDuration(imdbScene.getDuration());
		rcaScene.setSummary(imdbScene.getSummary());
		rcaScene.setFid(String.valueOf(imdbScene.getId()));
		rcaScene.setFtype("imdb");
		
		if(rcaScene.getId() == null){
			rcaScene.setAddDef();
			rcaScene.save();
		}else{
			rcaScene.setUpdDef();
			rcaScene.update();
		}
		return rcaScene;
	}
	
	/**
	 * 截取元素图
	 * @param episode_id
	 * @param cover_ffmpeg
	 * @param element
	 */
	private void printTvImg(Long episode_id,Element element) {
		String type="element_img";
		Long start_time = element.getStart_time();
		Long end_time = element.getEnd_time();
		String filepath = element.getCover();
		if(end_time == null){
			end_time = start_time;
		}
		judgeFfmpegImg(episode_id, type, start_time, end_time,filepath);
	}
	
	/**
	 * 判断是否截图
	 * @param episode_id
	 * @param cover_ffmpeg
	 * @param type
	 * @param start_time
	 * @param end_time
	 * @param filepath
	 */
	private void judgeFfmpegImg(Long episode_id, String type,Long start_time, Long end_time, String filepath) {
		FfmpegHistory hist = FfmpegHistory.dao.queryByEpisodeTimeType(episode_id, start_time, end_time, type);
		if(hist == null){
			String cmd = "ffmpeg -ss "+start_time+" -i "+episode_id+".flv -y -f image2 -t 0.001 "+filepath;
			
			hist = new FfmpegHistory();
			hist.setEpisode_id(episode_id);
			hist.setStart_time(start_time);
			hist.setEnd_time(end_time);
			hist.setFilename(filepath);
			hist.setType(type);
			hist.setCmd(cmd);
			hist.setThread_state(Cons.THREAD_STATE_WAIT);
			hist.setAddDef();
			hist.save();
		}
	}
}
