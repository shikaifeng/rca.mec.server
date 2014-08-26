package tv.zhiping.mec.api.program.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.cache.CacheKey;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Person;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mec.api.jfinal.ApiBaseCtrl;
import tv.zhiping.mec.feed.model.Baike;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Element.ElementType;
import tv.zhiping.mec.feed.model.ImdbFact;
import tv.zhiping.mec.feed.model.MecPerson;
import tv.zhiping.mec.feed.model.MecWeiboFeed;
import tv.zhiping.mec.feed.model.Music;
import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.mec.sys.model.SysConfig;
import tv.zhiping.redis.Redis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BaseElementApi extends ApiBaseCtrl {
	/**
	 * 补充节目，剧集信息  和最终的元素信息
	 * @parm  programId 节目id
	 * @param episodeId 剧集id
	 * @param data
	 */
	protected void getProgramAndEpisodeInfo(Program program,Episode episode,JSONObject data) {
		JSONObject episodeJson = null;
		
		if(episode != null){
			String episode_title = episode.getTitle();
			
			episodeJson = new JSONObject();
			episodeJson.put("id",episode.getId());
		
			episodeJson.put("current_episode",episode.getCurrent_episode());
			episodeJson.put("scene_count",Scene.dao.queryCountByEpisodeIdUseApp(episode.getId()));
			
			String episodeCover = episode.getCover();
			String episodeSummary =episode.getSummary();
			
			
			if(program!=null){
				episode_title = ComUtil.getEpisodeAppTitle(episode, program);
				episodeCover = StringUtils.isNotBlank(episodeCover) ? episodeCover : program.getView_cover();
				episodeSummary = StringUtils.isNotBlank(episodeSummary) ? episodeSummary : program.getSummary();
			}
			
			episodeJson.put("title",episode_title);
			episodeJson.put("cover",ComUtil.getStHttpPath(episodeCover));//如果没有剧集，使用节目的
			episodeJson.put("summary",episodeSummary);//如果没有剧集，使用节目的
		}
		
		
		if(program!=null){
			JSONObject programJson = new JSONObject();
			
			programJson.put("id",program.getId());
			programJson.put("title",program.getTitle());
			programJson.put("cover",ComUtil.getStHttpPath(program.getView_cover()));
			programJson.put("year",program.getYear());
			programJson.put("summary",program.getSummary());
			
			data.put("program",programJson);
		}
		
		data.put("episode",episodeJson);;
	}
	
	/**
	 *  节目维度的明星，音乐信息:根据节目ID返回明星、音乐信息
	 * @author 樊亚容
	 * 2015-5-28 
	 */
	protected void parsPersonMusicElement2JSONByProgramId(Long program_id,JSONObject data){//存储返回的明星信息
		//节目维度的明星信息
		String cache_key = CacheKey.PROGRAM_PERSON_JSON+program_id;
		JSONArray person_list = Redis.get(cache_key);
		if(person_list==null){
			person_list = new JSONArray();
			List<Person> pps = Person.dao.queryPersonByPeogramIdActorHost(program_id);
			for(Person p : pps){
				JSONObject json = new JSONObject();
				p.parse2Json(json);
				person_list.add(json);
			}
			Redis.setex(cache_key,person_list,CacheKey.PROGRAM_PERSON_EXPIRY);
		}
		
		//节目维度的音乐信息
		cache_key = CacheKey.PROGRAM_PERSON_JSON+program_id;
		JSONArray music_list = Redis.get(cache_key);
		if(music_list == null){
			music_list = new JSONArray();
			JSONArray start_music_list = new JSONArray();//片头音乐信息
			JSONArray end_music_list = new JSONArray();//片尾音乐信息
			
			List<Music> ms = Music.dao.queryByProgramId(program_id);
			Map<Long,String> distinctMap = new HashMap<Long,String>();//因为要用到：type_title，导致关联查询返回所有
			for(Music m : ms){
				Long key = m.getId();
				if(distinctMap.containsKey(key)){
					continue;
				}
				distinctMap.put(key, "");
				
				JSONObject json = new JSONObject();
				
				m.parseMusic2Json(json);
				
				Element e = Element.dao.queryByMusicId(m.getId());
				String type_title = null;
				if(e != null){
					type_title = e.getTag();
				}
				json.put("start_time", null);
				json.put("end_time", null);
				json.put("type_title",type_title);
				
				orderByTitle(music_list, start_music_list, end_music_list, json,type_title);
			}
			
			start_music_list.addAll(end_music_list);
			music_list.addAll(0,start_music_list);
			
			Redis.setex(cache_key,music_list,CacheKey.PROGRAM_MUSIC_EXPIRY);
		}
		
		
		if(!person_list.isEmpty()){
			data.put("person_list", person_list);
		}else{
			data.put("person_list",null);
		}
		if(!music_list.isEmpty()){
			data.put("music_list", music_list);
		}else{
			data.put("music_list",null);
		}
	}

	
	/**
	 * 对歌曲排序
	 * @param music_list
	 * @param start_music_list
	 * @param end_music_list
	 * @param json
	 * @param type_title
	 */
	private void orderByTitle(JSONArray music_list, JSONArray start_music_list,
			JSONArray end_music_list, JSONObject json, String type_title) {
		if("主题曲".equals(type_title) || "片头曲".equals(type_title)){
			start_music_list.add(json);
		}else if("片尾曲".equals(type_title)){
			end_music_list.add(json);
		}else{
			music_list.add(json);						
		}
	}
	
	
	/**
	 * 剧集维度的明星，音乐信息
	 * @param episode_id  剧集id
	 * @param data      
	 */
	protected void parsPersonMusicElement2JSONByEpisodeId(Long program_id,Long episode_id,JSONObject data){
//		List<String> types = new ArrayList<String>();
//		types.add(ElementType.person.toString());
//		types.add(ElementType.music.toString());
//		List<Element> list = Element.dao.queryAllByEpisode(episode_id,types);
		
		data.put("music_list",null);
		List<Element> list = Element.dao.queryByProgramIdEpisodeIdType(program_id,episode_id,ElementType.music.toString());
		if(list!=null && !list.isEmpty()){
			JSONArray music_list = getEpisodeMusic(list);//剧集维度的音乐信息
			
			if(!music_list.isEmpty()){
				data.put("music_list",music_list);
			}
		}
		
		
		list = Element.dao.queryByProgramIdEpisodeIdType(program_id,episode_id,ElementType.person.toString());
		data.put("person_list",null);
		if(list!=null && !list.isEmpty()){
			List<Long> person_ids = new ArrayList<Long>();
			for(int i=0;i<list.size();i++){
				Long fid = list.get(i).getFid();
				if(!list.contains(fid)){
					person_ids.add(fid);
				}
			}
			
			JSONArray person_list = getEpisodePerson(program_id,person_ids);
			if(!person_list.isEmpty()){
				data.put("person_list",person_list);
			}		
		}
	}
	
	/**
	 * 剧集维度的音乐信息
	 * @param data
	 * @param person_ids
	 * @param list
	 */
	private JSONArray getEpisodeMusic(List<Element> list) {
		JSONArray music_list = new JSONArray();//音乐信息
		JSONArray start_music_list = new JSONArray();//片头音乐信息
		JSONArray end_music_list = new JSONArray();//片尾音乐信息
		
		Map<String,String> distinctMap = new HashMap<String,String>();
		for(int i=0;i<list.size();i++){
			Element element = list.get(i);
			Long fid = element.getFid();
			String type = element.getType();
			
			String key = type+"#"+fid;
			if(distinctMap.containsKey(key)){
				continue;
			}
			distinctMap.put(key, "");
			
			//person, baike,lines, music,video
			
			JSONObject json = new JSONObject();
			json.put("start_time",null);
			json.put("end_time",null);
			
			Music music = Music.dao.findById(fid);
			if(music!=null){
				music.parseMusic2Json(json);
			}
			
			String type_title = element.getTag();
			json.put("type_title",type_title);
			orderByTitle(music_list, start_music_list, end_music_list,json, type_title);
		}
		start_music_list.addAll(end_music_list);
		music_list.addAll(0,start_music_list);
		
		return music_list;
	}

	/**
	 * 获取剧集维度的明星信息
	 * @param program_id
	 * @param program_type
	 * @param program_persons
	 * @param person_ids
	 * @param data
	 */
	private JSONArray getEpisodePerson(Long program_id,List<Long> person_ids) {
		List<Person> persons = Person.dao.queryPersonByIds(program_id,person_ids);
		
		//明星信息
		JSONArray person_list = new JSONArray();
		if(persons!=null && !persons.isEmpty()){
			for(int i=0;i<persons.size();i++){
				Person person = persons.get(i);
				JSONObject json = new JSONObject();
				person.parse2Json(json);
				person_list.add(json);
			}				
		}
	
		return person_list;
	}
	
	/**
	 * 根据剧集获取所有的微直播的
	 * @param scene_id  场景id
	 * @param data      
	 */
	protected void parsWeiboFeedElement2JSONBySceneId(Long episode_id,JSONObject data){
		List<MecWeiboFeed> weibos = MecWeiboFeed.dao.queryByEpisode(episode_id);
		JSONArray weibo_list = parseWeiboFeeda(weibos);
//		weibo_list = addParseWeiboFeed(weibos);
		data.put("weibo_list",weibo_list);
	}
	
	/**
	 * 根据剧集，场景获取所有的 明星，音乐，百科，台词，小片段
	 * @param scene_id  场景id
	 * @param data      
	 */
	protected void parsAllElement2JSONBySceneId(Long episode_id,Long scene_id,JSONObject data){
		List<Element> list = Element.dao.queryAllByEpisodeOrSceneId(episode_id,scene_id);
		
		if(list!=null && !list.isEmpty()){
			//明星信息
			JSONArray person_list = new JSONArray();
			//音乐信息
			JSONArray music_list = new JSONArray();
			//百科信息
			JSONArray baike_list = new JSONArray();
//			台词信息
//			JSONArray lines_list = new JSONArray();
			//小片段视频信息
			JSONArray video_list = new JSONArray();
			
			//imdb 的百科信息
			JSONArray fact_list = new JSONArray();
			
			//视频延迟时间
			int video_dealy_time = SysConfig.dao.getIntByKey(ConfigKeys.VIDEO_DEALY_TIME);
			
			for(int i=0;i<list.size();i++){
				Element element = list.get(i);
				
				String element_type = element.getType();
				JSONObject json = new JSONObject();
				
				Long fid = element.getFid();
				
				json.put("id",element.getId());
				json.put("start_time",element.getApp_start_time(video_dealy_time));
				json.put("end_time",element.getEnd_time());
				
				//person, baike,lines, music,video
				if(ElementType.person.toString().equals(element_type)){//明星
					json.put("cover", ComUtil.getStHttpPath(element.getCover()));
					String avatar = null;
					String name = null;
					String huUrl = null;
					Person person = Person.dao.findById(element.getFid());
					if(person!=null){
						avatar = person.getView_avatar();
						name = person.getName();
						huUrl = person.getHuUrl();
					}
					json.put("name",name);
					json.put("avatar",ComUtil.getStHttpPath(avatar));
					json.put("url",ComUtil.getHuHttpPath(huUrl));
					
					//c1:字段表示出演角色    c2:剧中角色描述
					json.put("character",element.getTag());					
					person_list.add(json);
				}else{
					json.put("cover",ComUtil.getStHttpPath(element.getCover()));
					json.put("title",element.getTitle());
					
					if(ElementType.baike.toString().equals(element_type)){//baike
						Baike baike = Baike.dao.findById(fid);
						if(baike != null){
							if(StringUtils.isNotBlank(baike.getCover())){//存在图片
								baike.parseBaikeFeedJson(json);
								baike_list.add(json);
							}else{//不存在图片
								baike.parseFactBaikeFeedJson(json);
								fact_list.add(json);
							}
						}
					}else if(ElementType.music.toString().equals(element_type)){//歌曲，音乐
						Music music = Music.dao.findById(fid);
						if(music!=null){
							music.parseMusicFeedJson(json);
						}else{
							log.warn("data errror music:"+fid);
						}
						//c1: 文件类型：mp3,3gp c2: 本地保存路径 c3: 歌曲类型
						json.put("type_title",element.getTag());
						music_list.add(json);
					}else if(ElementType.video.toString().equals(element_type)){//视频 片段
						//c1: 文件类型：mp3,3gp c2: 本地保存路径  t1:
						json.put("content", element.getContent());
						json.put("url",ComUtil.getStHttpPath(element.getUrl()));
						
						video_list.add(json);
					}else if(ElementType.fact.toString().equals(element_type)){//imdb百科
						ImdbFact fact = ImdbFact.dao.findById(fid);
						if(fact!=null){
							fact.parseFactJson(json);
							fact_list.add(json);
						}else{
							log.warn("data errror imdb_fact:"+fid);
						}
					}else if(ElementType.mec_person.toString().equals(element_type)){//学院
						MecPerson person = MecPerson.dao.findById(fid);
						if(person!=null){
							person.parseMecPerson2BaikeJson(json);
							baike_list.add(json);
						}else{
							log.warn("data errror mec_person:"+fid);
						}
					}

				}
			}
			
			Scene scene = Scene.dao.findById(scene_id);
			JSONArray weibo_list = null;
			if(scene!=null){
				List<MecWeiboFeed> weibos = MecWeiboFeed.dao.queryByEpisodeTime(scene.getPid(),scene.getStart_time(),scene.getEnd_time());
				weibo_list = parseWeiboFeeda(weibos);
			}
			
			
			//微直播信息
			data.put("weibo_list",weibo_list);
			
			if(!person_list.isEmpty()){
				data.put("person_list",person_list);				
			}else{
				data.put("person_list",null);
			}
			data.put("music_list",music_list);
			if(!person_list.isEmpty()){
				data.put("person_list",person_list);				
			}else{
				data.put("person_list",null);
			}
			
		 	if(!baike_list.isEmpty()){
		 		data.put("baike_list",baike_list);
			}else{
				data.put("baike_list",null);
			}
		 	
//			if(!lines_list.isEmpty()){
//				data.put("lines_list",lines_list);				
//			}else{
//				data.put("lines_list",null);
//			}
			
			if(!video_list.isEmpty()){
				data.put("video_list",video_list);				
			}else{
				data.put("video_list",null);
			}
			
			if(!fact_list.isEmpty()){
				data.put("fact_list",fact_list);				
			}else{
				data.put("fact_list",null);
			}
		}
	}
	
	/**
	 * 不过滤任何
	 * @param weibos
	 * @return
	 */
	private JSONArray parseWeiboFeeda(List<MecWeiboFeed> weibos) {
		JSONArray weibo_list = null;
		if(weibos!=null &&  !weibos.isEmpty()){
			weibo_list = new JSONArray();
			
			for(int i=0;i<weibos.size();i++){
				MecWeiboFeed obj = weibos.get(i);
				JSONObject json = new JSONObject();
				obj.parseWeiboFeedJson(json);
				weibo_list.add(json);
			}
		}
		return weibo_list;
	}
}


///**
// * 过滤weibo feed，每分钟只显示3条
// * @param weibos
// * @return
// */
//private JSONArray addParseWeiboFeed(List<WeiboFeed> weibos) {
//	JSONArray weibo_list = null;
//	if(weibos!=null &&  !weibos.isEmpty()){
//		weibo_list = new JSONArray();
//		List<WeiboFeed> filterProgramList = new ArrayList<WeiboFeed>();
//		List<WeiboFeed> filterPersonList = new ArrayList<WeiboFeed>();
//		List<WeiboFeed> filterOtherList = new ArrayList<WeiboFeed>();
//		String programWeiboFeedType = WeiboFeedType.program.toString();
//		String personWeiboFeedType = WeiboFeedType.person.toString();
//		
//		Long prev_minute = null;
//		
//		for(int i=0;i<weibos.size();i++){
//			WeiboFeed obj = weibos.get(i);
//			Long minute = obj.getStart_time()/60;
//			if(prev_minute == null){
//				prev_minute = minute;
//			}
//			
//			if(prev_minute != minute){
//				weibo_list.addAll(filterWeiboFeed(filterProgramList,filterPersonList,filterOtherList));
//				prev_minute = minute;
//			}
//			
//			if(programWeiboFeedType.equalsIgnoreCase(obj.getType())){
//				filterProgramList.add(obj);
//			}else if(personWeiboFeedType.equalsIgnoreCase(obj.getType())){
//				filterPersonList.add(obj);
//			}else{
//				filterOtherList.add(obj);
//			}
//		}
//		
//		if(!filterProgramList.isEmpty() || !filterPersonList.isEmpty() || !filterOtherList.isEmpty()){
//			weibo_list.addAll(filterWeiboFeed(filterProgramList,filterPersonList,filterOtherList));
//		}
//	}
//	return weibo_list;
//}

///**
// * 只返回3条记录
// * @param filterProgramList
// * @param filterOtherList
// * @return
// */
//private List<JSONObject> filterWeiboFeed(List<WeiboFeed> filterProgramList, List<WeiboFeed> filterPersonList,List<WeiboFeed> filterOtherList) {
//	List<JSONObject> res = new ArrayList<JSONObject>();
//	filterProgramList.addAll(filterPersonList);
//	filterProgramList.addAll(filterOtherList);
//	int size = filterProgramList.size();
//	int per_num_second = 0;
//	int max_weibo_feed_size = CacheFun.getConIntVal(ConfigKeys.WEIBO_FEED_MAX_SIZE);
//	per_num_second = 60 / max_weibo_feed_size;
//	if (size > max_weibo_feed_size){
//		size = max_weibo_feed_size; 
//	}
//	
//	for(int i=0;i<size;i++){
//		WeiboFeed obj = filterProgramList.get(i);
//		JSONObject json = new JSONObject();
//		obj.parseWeiboFeedJson(json,i,per_num_second);
//		res.add(json);
//	}
//	
//	filterProgramList.clear();
//	filterPersonList.clear();
//	filterOtherList.clear();
//	return res;
//}

///**
// * 剧集维度的明星，音乐信息
// * @param episode_id  剧集id
// * @param data      
// */
//protected void parsPersonMusicElement2JSONByEpisodeId(Long episode_id,JSONObject data){
//	List<String> types = new ArrayList<String>();
//	types.add(ElementType.person.toString());
//	types.add(ElementType.music.toString());
//	
//	List<Element> list = Element.dao.queryAllByEpisode(episode_id,types);
//	
//	if(list!=null && !list.isEmpty()){
//		//明星信息
//		JSONArray person_list = new JSONArray();
//		//音乐信息
//		JSONArray music_list = new JSONArray();
//		
//		
//		Map<String,String> distinctMap = new HashMap<String,String>();
//		for(int i=0;i<list.size();i++){
//			Element element = list.get(i);
//			Long fid = element.getFid();
//			String type = element.getType();
//			String key = element.getType()+"#"+fid;
//			if(distinctMap.containsKey(key)){
//				continue;
//			}
//			distinctMap.put(key, "");
//			JSONObject json = new JSONObject();
//			
//			
//			json.put("id",element.getId());
//			json.put("start_time",element.getStart_time());
//			json.put("end_time",element.getEnd_time());
//			
//			//person, baike,lines, music,video
//			if(ElementType.person.toString().equals(type)){//明星					
//				json.put("cover",ComUtil.getStHttpPath(element.getCover()));
//				String avatar = null;
//				String name = null;
//				String huUrl = null;
//				Person person = Person.dao.findById(element.getFid());
//				if(person!=null){
//					avatar = person.getView_avatar();
//					name = person.getName();
//					huUrl = person.getHuUrl();
//				}else{
//					log.warn("data errror personId:"+element.getFid());
//				}
//				
//				json.put("url",ComUtil.getHuHttpPath(huUrl));
//				json.put("name",name);
//				json.put("avatar",ComUtil.getStHttpPath(avatar));
//				//c1:字段表示出演角色 c2:剧中角色描述
//				json.put("character",element.get(ElementAddonFiled.c1.toString()));
//				
//				person_list.add(json);
//			}else{
//				if(ElementType.music.toString().equals(type)){//歌曲，音乐
//					Music music = Music.dao.findById(fid);
//					String singer = null;
//					String cover = null;
//					String title = null;
//					String url = null;
//					if(music!=null){
//						singer = music.getSinger();
//						cover = music.getCover();
//						title = music.getTitle();
//						url = music.getSource_url();
//					}
//					json.put("singer",singer);
//					json.put("cover",ComUtil.getStHttpPath(cover));
//					json.put("title",title);
//					json.put("url",url);
//					
//					//c1: 文件类型：mp3,3gp c2: 本地保存路径 c3: 歌曲类型
//					json.put("type_title",element.get(ElementAddonFiled.c3.toString()));
//					music_list.add(json);
//				}
//			}
//		}
//		
//		if(!person_list.isEmpty()){
//			data.put("person_list",person_list);
//		}else{
//			data.put("person_list",null);
//		}
//		if(!music_list.isEmpty()){
//			data.put("music_list",music_list);
//		}else{
//			data.put("music_list",null);
//		}
//	}
//}

//	用户排序
//	public static class PersonShowComparator implements Comparator<Person>{
//		public int compare(Person p0, Person p1) {
//			Boolean primary0 = p0.getBoolean("is_primary");
//			Boolean primary1 = p1.getBoolean("is_primary");
////			System.out.println();
////			System.out.print(p0.getName()+"  "+p1.getName()+" "+primary0+" "+primary1);
//			if(primary0!=null && primary0!=null){
//				int flag = primary0.compareTo(primary1);
////				System.out.print(" "+flag +" po="+p0.getLong("person_program_id")+" p1="+p1.getLong("person_program_id")+"  "+idCom(p0, p1));
//				if(primary0.equals(primary1)){
//					return idCom(p0, p1);
//				}else if(Cons.TRUE.equals(primary0) && Cons.FALSE.equals(primary0)){
//					return -1;
//				}else{
//					return 1;
//				}
//			}else{
//				return idCom(p0, p1);
//			}
//		}
//
//		private int idCom(Person p0, Person p1) {
//			Long id0 = p0.getLong("person_program_id");
//			Long id1 = p1.getLong("person_program_id");
//			if(id0<id1){
//				return 1;
//			}if(id0.equals(id1)){
//				return 0;
//			}else{
//				return -1;
//			}
//		}
//	}
/*List<BaikeProperty> baike_prop_list = BaikeProperty.dao.qurreyByPid(baike.getId());
if(baike_prop_list!=null && !baike_prop_list.isEmpty()){
	JSONArray properties = new JSONArray();
	for(BaikeProperty property : baike_prop_list){
		JSONObject propertiy_json = new JSONObject();									
		propertiy_json.put("id",property.getId());
		propertiy_json.put("key",property.getTitle());
		propertiy_json.put("value",property.getValue());
		
		properties.add(propertiy_json);
	}
	json.put("properties",properties);
}*/
//else if(ElementType.lines.toString().equals(element_type)){//台词   t1 为台词字段
//json.put("content",element.get(ElementAddonFiled.t1.toString()));
//lines_list.add(json);
//}
