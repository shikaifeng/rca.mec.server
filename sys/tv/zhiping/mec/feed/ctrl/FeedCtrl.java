package tv.zhiping.mec.feed.ctrl;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Person;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mdm.model.WeiboFeed;
import tv.zhiping.mec.api.common.ApiCons;
import tv.zhiping.mec.api.program.ctrl.BaseElementApi;
import tv.zhiping.mec.epg.model.MecChannel;
import tv.zhiping.mec.epg.model.MecSchedule;
import tv.zhiping.mec.feed.model.Baike;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Element.ElementType;
import tv.zhiping.mec.feed.model.ImdbFact;
import tv.zhiping.mec.feed.model.MecPerson;
import tv.zhiping.mec.feed.model.MecWeiboFeed;
import tv.zhiping.mec.feed.model.Music;
import tv.zhiping.mec.feed.model.Question;
import tv.zhiping.mec.feed.model.QuestionOption;
import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.mec.luck.model.LuckyDrawEpisode;
import tv.zhiping.mec.luck.model.LuckyDrawEvent;
import tv.zhiping.mec.sys.model.SysConfig;
import tv.zhiping.mec.sys.model.SysUser;
import tv.zhiping.utils.DateUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import freemarker.core._RegexBuiltins.matchesBI;

/**
 * feed的预先
 * 
 * @author 张有良
 */
public class FeedCtrl extends BaseElementApi {
	/**
	 * 删除
	 */
	public void del_element(){
		Element obj = new Element();
		obj.setId(getParaToLong(0));
		obj.setDelDef();
		obj.update();
		renderJson(Cons.JSON_SUC);
	}
	
	public void feed_manage() throws ParseException {
		Long program_id = getParaToLong("program_id");
		Long episode_id = getParaToLong("episode_id");
		
		Program program = Program.dao.findById(program_id);
		List<Episode> episode_list = Episode.dao.queryByPid(program_id);
		if(episode_id==null && episode_list!=null && !episode_list.isEmpty()){
			episode_id = episode_list.get(0).getId();
		}
		
		loadEpg(episode_id);
//		if(episode_id!=null){
//			JSONObject element_json = loadAllElement(episode_id);
//			setAttr("element", element_json);
//		}
		
		setAttr("program",program);
		setAttr("episode_list", episode_list);
		setAttr("episode_id", episode_id);
		render("/page/feed/feed/feed_manage.jsp");
	}
	
	/**
	 * 获取feed信息
	 * @throws ParseException
	 */
	public void get_feeds(){
		Long episode_id = getParaToLong("episode_id");
		String modules = getPara("modules");
		
		JSONObject element_json = null;
		if(StringUtils.isBlank(modules)){
			element_json = loadAllElement(episode_id);
		}else{
			element_json = new JSONObject();
			if(modules.indexOf("mec_person")>-1){
				element_json.put("mec_person_list",getMecPersonList(episode_id));
			}
			
			if(modules.indexOf("baike")>-1){
				element_json.put("baike_list",getBaikeList(episode_id));
			}
			
			if(modules.indexOf("music")>-1){
				element_json.put("music_list",getMusicList(episode_id));
			}
			
			if(modules.indexOf("question")>-1){
				element_json.put("question_list",getQuestionList(episode_id));
			}
			
			if(modules.indexOf("weibo_feed")>-1){
				element_json.put("weibo_feed_list",get_weibo_feed_list(episode_id));
			}
		}
		
		renderJson(element_json.toJSONString());
	}
	

	//加载所有的element
	private JSONObject loadAllElement(Long episode_id) {
		JSONObject element_json = new JSONObject();
		
		// 人物Elemen
		element_json.put("mec_person_list",getMecPersonList(episode_id));
		
		// 百科
		element_json.put("baike_list", getBaikeList(episode_id));
		
		// 音乐信息music
		element_json.put("music_list",getMusicList(episode_id));
		
		//互动问答question
		element_json.put("question_list",getQuestionList(episode_id));
		
		element_json.put("weibo_feed_list",get_weibo_feed_list(episode_id));
		return element_json;
	}
	
	private List<JSONObject> get_weibo_feed_list(Long episode_id) {
		List<WeiboFeed> weibo = WeiboFeed.dao.queryAllByEpisode(episode_id);
		List<JSONObject> weibo_list = new LinkedList<JSONObject>();
		for(WeiboFeed obj:weibo){
			JSONObject json = obj.getJSONObj();
			json.put("start_time", ComUtil.secondFormate(obj.getStart_time()));
			weibo_list.add(json);
		}
		return weibo_list;
	}
	
	
	private List<JSONObject> getQuestionList(Long episode_id) {
		List<Question> question = Question.dao.queryByEpisodeId(episode_id);
		List<JSONObject> question_list = new LinkedList<JSONObject>();
		Long mark_time = 0L;
		for(Question quest:question){
			JSONObject json = quest.getJSONObj();
			
			Long start_time = quest.getStart_time();
			Long end_time = quest.getEnd_time();
			
			mark_time = set_time_warn(mark_time, json, start_time, end_time);
			
			json.put("start_time", ComUtil.secondFormate(quest.getStart_time()));
			json.put("deadline", ComUtil.secondFormate(quest.getDeadline()));
			json.put("public_time", ComUtil.secondFormate(quest.getPublic_time()));
			json.put("end_time", ComUtil.secondFormate(quest.getEnd_time()));
			json.put("updated_at",quest.getUpdated_at_str());
			json.put("updated_user",SysUser.dao.queryRealNameByUid(quest.getUpdated_user()));
			json.put("long_start_time", quest.getStart_time());
			json.put("long_end_time", quest.getEnd_time());
			json.put("long_deadline", quest.getDeadline());
			json.put("long_public_time", quest.getPublic_time());
			
			List<QuestionOption> options = QuestionOption.dao.queryByQuestionId(quest.getId());
			
			if(options!=null && !options.isEmpty()){
				Long answer_id = quest.getAnswer_id();
				for(int i=0;i<options.size();i++){
					QuestionOption opt = options.get(i);
					json.put("option_"+i,opt.getTitle());
					if(opt.getId().equals(answer_id)){
						json.put("answer", (char)('A'+i));
					}
				}				
			}
			question_list.add(json);
		}
		return question_list;
	}

	/**
	 * 设置时间不正确的信息
	 * @param mark_time
	 * @param json
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	private Long set_time_warn(Long mark_time, JSONObject json,
			Long start_time, Long end_time) {
		if(start_time!=null){
			if(start_time<mark_time){
				json.put("start_time_warn", '1');
			}else{
				mark_time = start_time;
			}
		}
		if(end_time!=null){
			if(end_time<mark_time){
				json.put("end_time_warn", '1');
			}else{
				mark_time = end_time;
			}	
		}
		return mark_time;
	}
	
	
	private List<JSONObject> getMusicList(Long episode_id) {
		List<Element> music = Element.dao.queryFidAllByEpisode2(episode_id,ElementType.music.toString());
		List<JSONObject> music_list = new LinkedList<JSONObject>();
		for (Element mu : music) {
			JSONObject json = mu.getJSONObj();
			
			Music cell = Music.dao.findById(mu.getFid());
			if (cell != null) {
				json.put("singer", cell.getSinger());
				json.put("source_url", cell.getSource_url());
				json.put("cover", ComUtil.getStHttpPath(cell.getCover()));
				json.put("music_tag", mu.getTag());
				json.put("music_title", cell.getTitle());
				
				json.put("updated_at",cell.getUpdated_at_str());
				json.put("updated_user",SysUser.dao.queryRealNameByUid(cell.getUpdated_user()));
			}				
			json.put("start_time", ComUtil.secondFormate(mu.getStart_time()));
			json.put("end_time", ComUtil.secondFormate(mu.getEnd_time()));
			json.put("long_start_time", mu.getStart_time());
			json.put("long_end_time", mu.getEnd_time());
			music_list.add(json);
		}
		return music_list;
	}

	private List<JSONObject> getBaikeList(Long episode_id) {
		List<Element> baike = Element.dao.queryFidAllByEpisode2(episode_id,ElementType.baike.toString());
		List<JSONObject> baike_list = new ArrayList<JSONObject>();
		for (Element bai : baike) {
			JSONObject json = bai.getJSONObj();
			
			Baike cell = Baike.dao.findById(bai.getFid());
			if (cell != null) {
				json.put("desc", cell.getSummary());
				json.put("baike_url",cell.getSource_url());
				json.put("baike_title",cell.getTitle());
				json.put("cover",ComUtil.getStHttpPath(cell.getCover()));
				
				json.put("updated_at",cell.getUpdated_at_str());
				json.put("updated_user",SysUser.dao.queryRealNameByUid(cell.getUpdated_user()));
			}
			json.put("start_time", ComUtil.secondFormate(bai.getStart_time()));
			json.put("end_time", ComUtil.secondFormate(bai.getEnd_time()));
			json.put("long_start_time", bai.getStart_time());
			json.put("long_end_time", bai.getEnd_time());
			baike_list.add(json);
		}
		return baike_list;
	}

	private List<JSONObject> getMecPersonList(Long episode_id) {
		List<Element> mec_person = Element.dao.queryFidAllByEpisode2(episode_id, ElementType.mec_person.toString());
		List<JSONObject> person_list = new LinkedList<JSONObject>();
		for (Element person : mec_person) {
			JSONObject json = person.getJSONObj();
			
			MecPerson cell = MecPerson.dao.findById(person.getFid());
			if (cell != null) {
				json.put("person_avatar", ComUtil.getStHttpPath(cell.getAvatar()));
				json.put("person_name", cell.getName());
				json.put("person_desc", cell.getDescription());
				json.put("baike_url", cell.getBaike_url());
				
				json.put("updated_at",cell.getUpdated_at_str());
				json.put("updated_user",SysUser.dao.queryRealNameByUid(cell.getUpdated_user()));
			}
			json.put("start_time", ComUtil.secondFormate(person.getStart_time()));
			json.put("end_time", ComUtil.secondFormate(person.getEnd_time()));
			json.put("long_start_time", person.getStart_time());
			json.put("long_end_time", person.getEnd_time());
			person_list.add(json);
		}
		return person_list;
	}
	
	/**
	 * 加载epg信息
	 */
	private void loadEpg(Long episode_id) {
		Long channel_id = getParaToLong("channel_id");
		List<MecChannel> channel_list = MecChannel.dao.queryChannelList();
		setAttr("channel_list", channel_list);
		setAttr("channel_id",channel_id);
		
		if(episode_id!=null){
			MecSchedule schedule = MecSchedule.dao.queryByChannelAndEpisodeId(channel_id,episode_id);
			JSONObject json = null;
			if(schedule!=null){
				json = schedule.getJSONObj();
				Long mec_start_at = json.getLong("mec_start_at");
				if(mec_start_at!=null){
					json.put("mec_start_at",DateUtil.getTimeSampleString(new Date(mec_start_at)));
				}
				Long mec_end_at = json.getLong("mec_end_at");
				if(mec_end_at!=null){
					json.put("mec_end_at",DateUtil.getTimeSampleString(new Date(mec_end_at)));
				}
				setAttr("channel_id",schedule.getMec_channel_id());
			}
			setAttr("schedule",json);
		}
	}
	
	/**
	 * 加载抽奖活动信息
	 */
	public void loadLuckyDrawEvent() {
		Long episode_id = getParaToLong("episode_id");
		if(episode_id!=null){
			LuckyDrawEvent event = LuckyDrawEvent.dao.queryByLastClose(episode_id,null);
			if(event!=null){
				LuckyDrawEpisode lucky_draw_episode = LuckyDrawEpisode.dao.queryByLuckEpisode(event.getId(),episode_id);
				if(lucky_draw_episode!=null){
					JSONObject json = lucky_draw_episode.getJSONObj();
					json.put("start_time",ComUtil.secondFormate(lucky_draw_episode.getStart_time()));
					json.put("end_time",ComUtil.secondFormate(lucky_draw_episode.getEnd_time()));
					
					setAttr("lucky_draw_episode",json);
				}
				setAttr("lucky_draw_event",event);
			}
		}
		render("/page/feed/feed/time/lucky_draw_event.jsp");
	}

	
	
	public void time_input() {
		Long element_id = getParaToLong("element_id");
		if (element_id != null) {
			Element element = Element.dao.findById(element_id);
			setAttr("obj", element);
		}
		render("/page/feed/feed/time_input.jsp");
	}

	public void time_save() {
		Element obj = getModel(Element.class, "obj");
		if (obj != null) {
			if (obj.getId() != null) {
				obj.setUpdDef();
				obj.update();
			} else {
				obj.setAddDef();
				obj.save();
			}
		}
		renderJson(Cons.JSON_SUC);
	}

	public void index_episode() {
		Long episode_id = getParaToLong("episode_id");

		if (episode_id != null) {
			Episode episode = Episode.dao.findById(episode_id);
			setAttr("episode", episode);

			if (episode != null) {
				Program program = Program.dao.findById(episode.getPid());
				setAttr("program", program);
			}
		}

		List<Scene> queryResult = Scene.dao.queryByEpisode(episode_id);

		List<JSONObject> list = new ArrayList<JSONObject>();

		// 视频延迟时间
		int video_dealy_time = SysConfig.dao
				.getIntByKey(ConfigKeys.VIDEO_DEALY_TIME);

		if (queryResult != null && !queryResult.isEmpty()) {// 有场景的
			for (Scene scene : queryResult) {
				JSONObject sceneData = new JSONObject();
				sceneData.put("id", scene.getId());
				sceneData.put("cover", ComUtil.getStHttpPath(scene.getCover()));
				sceneData.put("summary", scene.getSummary());
				sceneData.put("start_time",
						ComUtil.secondFormate(scene.getStart_time()));
				sceneData.put("end_time",
						ComUtil.secondFormate(scene.getEnd_time()));

				List element_json = getElementJson(episode_id, scene,
						sceneData, video_dealy_time);

				sceneData.put("element_count", element_json.size());
				sceneData.put("element_list", element_json);

				list.add(sceneData);
			}
		} else {// 无场景，判断是否有微博的
			Long weiboFeedCount = MecWeiboFeed.dao
					.queryCountByEpisode(episode_id);
			if (weiboFeedCount != null && weiboFeedCount > 0) {
				Episode episode = Episode.dao.findById(episode_id);
				if (episode != null) {
					Program program = Program.dao.findById(episode.getPid());
					if (program != null) {
						MecWeiboFeed weiboFeed = MecWeiboFeed.dao
								.queryMinMaxTimeByEpisode(episode_id);
						if (weiboFeed != null) {
							String title = ComUtil.getEpisodeAppTitle(episode,
									program);
							JSONObject sceneData = new JSONObject();
							sceneData.put("id",
									Cons.VIRTUAL_SCENE_ID * episode.getId());
							sceneData.put("cover", null);
							sceneData.put("summary", title);
							sceneData.put("start_time", ComUtil
									.secondFormate(weiboFeed
											.getLong("min_time")));
							sceneData.put("end_time", ComUtil
									.secondFormate(weiboFeed
											.getLong("max_time")));

							parsWeiboFeedElement2JSONBySceneId(episode_id,
									sceneData);
							JSONArray element_list = sceneData
									.getJSONArray("weibo_list");

							for (int i = 0; i < element_list.size(); i++) {
								element_list.getJSONObject(i).put("type",
										"weibo");
							}

							sceneData.put("element_list", element_list);
							sceneData.put("element_count", element_list.size());

							list.add(sceneData);
						}
					}
				}
			}

		}
		setAttr("table_list", list);
		render("/page/feed/feed/index_episode.jsp");
	}

	private List<JSONObject> getElementJson(Long episode_id, Scene scene,
			JSONObject sceneData, int video_dealy_time) {
		List<JSONObject> element_json = new ArrayList<JSONObject>();
		List<Element> elements = Element.dao.queryAllByEpisodeOrSceneId(
				episode_id, scene.getId());
		if (elements != null && !elements.isEmpty()) {

			for (int i = 0; i < elements.size(); i++) {
				Element element = elements.get(i);

				String element_type = element.getType();
				JSONObject json = new JSONObject();

				Long fid = element.getFid();

				json.put("id", element.getId());
				json.put("start_time",
						element.getApp_start_time(video_dealy_time));
				json.put("end_time", element.getEnd_time());
				json.put("type", element_type);

				// person, baike,lines, music,video
				if (ElementType.person.toString().equals(element_type)) {// 明星
					json.put("cover", ComUtil.getStHttpPath(element.getCover()));
					String avatar = null;
					String name = null;
					String huUrl = null;
					Person person = Person.dao.findById(element.getFid());
					if (person != null) {
						avatar = person.getView_avatar();
						name = person.getName();
						huUrl = person.getHuUrl();
					}
					json.put("name", name);
					json.put("avatar", ComUtil.getStHttpPath(avatar));
					json.put("url", ComUtil.getHuHttpPath(huUrl));

					// c1:字段表示出演角色 c2:剧中角色描述
					json.put("character", element.getTag());
					element_json.add(json);
				}
				if (ElementType.mec_person.toString().equals(element_type)) {// 明星
					json.put("cover", ComUtil.getStHttpPath(element.getCover()));
					String avatar = null;
					String name = null;
					String huUrl = null;
					MecPerson person = MecPerson.dao.findById(element.getFid());
					if (person != null) {
						avatar = person.getView_avatar();
						name = person.getName();
						// huUrl = person.getHuUrl();
					}
					json.put("name", name);
					json.put("avatar", ComUtil.getStHttpPath(avatar));
					// json.put("url",ComUtil.getHuHttpPath(huUrl));

					// c1:字段表示出演角色 c2:剧中角色描述
					// json.put("character",element.get(ElementAddonFiled.c1.toString()));
					element_json.add(json);
				} else {
					json.put("cover", ComUtil.getStHttpPath(element.getCover()));
					json.put("title", element.getTitle());

					if (ElementType.baike.toString().equals(element_type)) {// baike
						Baike baike = Baike.dao.findById(fid);
						if (baike != null) {
							baike.parseBaikeFeedJson(json);
						}
						element_json.add(json);
					} else if (ElementType.music.toString()
							.equals(element_type)) {// 歌曲，音乐
						Music music = Music.dao.findById(fid);
						if (music != null) {
							music.parseMusicFeedJson(json);
						} else {
							log.warn("data errror music:" + fid);
						}
						// c1: 文件类型：mp3,3gp c2: 本地保存路径 c3: 歌曲类型
						json.put("type_title", element.getTag());
						element_json.add(json);
					} else if (ElementType.video.toString()
							.equals(element_type)) {// 视频 片段
						// c1: 文件类型：mp3,3gp c2: 本地保存路径 t1:
						json.put("content", element.getContent());
						json.put("url", ComUtil.getStHttpPath(element.getUrl()));

						element_json.add(json);
					} else if (ElementType.fact.toString().equals(element_type)) {// imdb百科
						ImdbFact fact = ImdbFact.dao.findById(fid);
						if (fact != null) {
							fact.parseFactJson(json);
							element_json.add(json);
						} else {
							log.warn("data errror imdb_fact:" + fid);
						}
					}
				}
			}
		}

		List<MecWeiboFeed> weibos = MecWeiboFeed.dao.queryByEpisodeTime(
				scene.getPid(), scene.getStart_time(), scene.getEnd_time());
		if (weibos != null && !weibos.isEmpty()) {
			for (int i = 0; i < weibos.size(); i++) {
				MecWeiboFeed obj = weibos.get(i);
				JSONObject weibo_json = new JSONObject();
				obj.parseWeiboFeedJson(weibo_json);
				weibo_json.put("type", "weibo");
				element_json.add(weibo_json);
			}
		}

		return element_json;
	}
	
	/**
	 * 设置元素的开始时间
	 */
	public void set_element_start_time() {
		String res = Cons.JSON_SUC;
		Long element_id = getParaToLong("element_id");
		Long schedule_id = getParaToLong("schedule_id");
		
		MecSchedule schedule = MecSchedule.dao.findById(schedule_id);
		Element element = Element.dao.findById(element_id);
		if(schedule!=null && element != null){
			Long now = System.currentTimeMillis();
			Long start_time = now - schedule.getMec_start_at();
			start_time = start_time/1000;
			if(start_time > -1){
				element.setStart_time(start_time);
				
				element.setUpdDef();
				element.update();
				
				Element prev_element = Element.dao.queryByLtStartTimeMaxTime(element.getEpisode_id(),element.getType(),start_time);
				if(prev_element!=null && prev_element.getEnd_time() == null){
					prev_element.setEnd_time(start_time);
					prev_element.setDuration(prev_element.getEnd_time() - prev_element.getStart_time());
					prev_element.setUpdDef();
					prev_element.update();
				}
			}else{
				JSONObject json = new JSONObject();
				json.put("status",ApiCons.STATUS_ERROR);
				json.put("msg","EPG时间设置过前，设置的时间为负数");
				res = json.toJSONString();
			}
		}
		renderJson(res);
	}

	/**
	 * 设置元素的截止时间
	 */
	public void set_element_end_time() {
		String res = Cons.JSON_SUC;
		Long element_id = getParaToLong("element_id");
		Long schedule_id = getParaToLong("schedule_id");
		
		MecSchedule schedule = MecSchedule.dao.findById(schedule_id);
		Element element = Element.dao.findById(element_id);
		if(schedule!=null && element != null){
			Long now = System.currentTimeMillis();
			Long time = now - schedule.getMec_start_at();
			time = time/1000;
			if(time > -1){
				element.setEnd_time(time);
				
				element.setUpdDef();
				element.update();
			}else{
				JSONObject json = new JSONObject();
				json.put("status",ApiCons.STATUS_ERROR);
				json.put("msg","EPG时间设置过前，设置的时间为负数");
				res = json.toJSONString();
			}
		}
		renderJson(res);
	}
	
	// public void input() {
	// Long id = getParaToLong(0);
	// if(id!=null){
	// setAttr("obj", Episode.dao.findById(id));
	// }
	// render("/page/feed/music/input.jsp");
	// }

	// public void save() {
	// Music obj = getModel(Music.class,"obj");
	// if(obj!=null){
	// if(obj.getId()!=null){
	// obj.setUpdDef();
	// obj.update();
	// }else{
	// obj.setAddDef();
	// obj.save();
	// }
	// }
	// renderJson(Cons.JSON_SUC);
	// }

	public void update() {
		// getModel(Blog.class).update();
		// redirect(CacheFun.getConVal(ConfigKeys.WEB_HTTP_PATH)+"/blog");
	}

	public void del() {
		// Blog obj = getAutoModel(Blog.class);
		// obj.setDelDef();
		// obj.update();

		renderJson(Cons.JSON_SUC);
	}
}
