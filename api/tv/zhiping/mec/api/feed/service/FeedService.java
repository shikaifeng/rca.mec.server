package tv.zhiping.mec.api.feed.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mec.feed.model.Baike;
import tv.zhiping.mec.feed.model.BannerTitle;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Element.ElementType;
import tv.zhiping.mec.feed.model.BannerImg;
import tv.zhiping.mec.feed.model.MecPerson;
import tv.zhiping.mec.feed.model.MecWeiboFeed;
import tv.zhiping.mec.feed.model.Music;
import tv.zhiping.mec.feed.model.Question;
import tv.zhiping.mec.feed.model.QuestionOption;
import tv.zhiping.mec.luck.model.LuckyDrawBefore;
import tv.zhiping.mec.luck.model.LuckyDrawEvent;
import tv.zhiping.mec.luck.model.LuckyDrawHistory;
import tv.zhiping.mec.user.model.UserQuestion;
import tv.zhiping.utils.DateUtil;
import tv.zhiping.utils.RandomUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FeedService {
	
//	/**
//	 * 加载feed数据
//	 * @param data
//	 * @param episode_id
//	 * @param relative_time
//	 */
//	public void loadFeedData(JSONObject data, Long episode_id,Long relative_time,String udid) {
//		JSONObject feeds = new JSONObject();
//		boolean hava_element = false;
//		
//		JSONObject element_json = null;
//		
//		//人物明星
//		element_json = loadLastCloseMecPerson(episode_id,relative_time);
//		if(element_json!=null){
//			feeds.put("person",element_json);
//			hava_element = true;
//		}
//		
//		//百科
//		element_json = loadLastCloseBaike(episode_id,relative_time);
//		if(element_json!=null){
//			feeds.put("baike",element_json);
//			hava_element = true;
//		}
//		//微博
//		element_json = loadLastCloseWeibo(episode_id,relative_time);
//		if(element_json!=null){
//			feeds.put("weibo",element_json);
//			hava_element = true;
//		}
//		//音乐
//		element_json = loadLastCloseMusic(episode_id,relative_time);
//		if(element_json!=null){
//			data.put("music",element_json);
//			hava_element = true;
//		}
//		//互动问答
//		element_json = loadLastCloseQuestion(episode_id,relative_time,udid);
//		if(element_json!=null){
//			data.put("question",element_json);
//			hava_element = true;
//		}
//		//抽奖信息
//		element_json = loadLastCloseLucky(episode_id,relative_time,udid);
//		if(element_json!=null){
//			data.put("lucky",element_json);
//			hava_element = true;
//		}
//		
//		
//		data.put("feeds",feeds);
//		if(hava_element){
//			data.put("page",ComUtil.getHuHttpPath("m/tmallbox/index"));
//		}
//	}
	
	/**
	 * 加载feed数据
	 * @param data
	 * @param episode_id
	 * @param relative_time
	 */
	public void loadFeedData(JSONObject data, Long episode_id,Long relative_time,String udid) {
		JSONObject feeds = new JSONObject();
		boolean hava_element = false;
		
		JSONObject element_json = null;
		
		//人物明星
		element_json = loadLastCloseMecPerson(episode_id,relative_time);
		if(element_json!=null){
			feeds.put("person",element_json);
			hava_element = true;
		}
		
		//百科
		element_json = loadLastCloseBaike(episode_id,relative_time);
		if(element_json!=null){
			feeds.put("baike",element_json);
			hava_element = true;
		}
		
		//音乐
		element_json = loadLastCloseMusic(episode_id,relative_time);
		if(element_json!=null){
			feeds.put("music",element_json);
			hava_element = true;
		}
		
		//微博
		element_json = loadLastCloseWeibo(episode_id,relative_time);
		if(element_json!=null){
			data.put("weibo",element_json);
			hava_element = true;
		}
		
		//互动问答
		element_json = loadLastCloseQuestion(episode_id,relative_time,udid);
		if(element_json!=null){
			data.put("question",element_json);
			hava_element = true;
		}else{//如果没有，判断是否是第一个互动问答前
			element_json = loadFrontFirstQuestion(episode_id, relative_time);
			
			if(element_json!=null){
				data.put("question",element_json);
				hava_element = true;
			}
		}
		
		
		//抽奖信息
		element_json = loadLastCloseLucky(episode_id,relative_time,udid);
		if(element_json!=null){
			data.put("lucky",element_json);
			hava_element = true;
		}
		
		data.put("feeds",feeds);
		if(hava_element){
			data.put("page",ComUtil.getHuHttpPath("m/tmallbox/index"));
		}
	}

	/**
	 * 获取互动问答还没有出现的banner
	 * @param episode_id
	 * @param relative_time
	 * @return
	 */
	private JSONObject loadFrontFirstQuestion(Long episode_id, Long relative_time) {
		JSONObject element_json = null;
		boolean flag = Question.dao.queryFrontFirstQuestion(episode_id,relative_time);
		if(flag){//表示 节目刚开始，没出现互动问答。
			element_json = new JSONObject();
			
			String banner_title = getBanner_title(QUESTION_NO_SHOW);
			if(StringUtils.isNotBlank(banner_title)){
				BannerImg img = BannerImg.dao.querySwitchPriorityByObj(null, episode_id,null);
				if(img!=null){
					element_json = new JSONObject();
					element_json.put("banner_title",ComUtil.getHtmlBrStr(banner_title));
					element_json.put("banner_img_path",ComUtil.getStHttpPath(img.getPath()));
				}
			}
		}
		return element_json;
	}

	
	/**
	 * 获取最接近的抽奖活动
	 * @param episode_id
	 * @param time
	 */
	public JSONObject loadLastCloseLucky(Long episode_id,Long time,String udid) {
		JSONObject json = null;
		
		LuckyDrawEvent srcObj = LuckyDrawEvent.dao.queryByLastClose(episode_id,time);				
		if(srcObj!=null){
			Long lucky_id = srcObj.getId();
			Long sy_count = getLuckyDrawSurplusCount(udid,srcObj);
			if(sy_count!=null && sy_count>0){
				json = new JSONObject();
				json.put("id",lucky_id);
				json.put("url",ComUtil.getHuHttpPath(srcObj.getUrl())+"?id="+lucky_id+"&udid="+udid);
				
				json.put("lucky_surplus_count",sy_count);//抽奖剩余次数				
			}
		}
		
		return json;
	}

	/**
	 * 这个活动，用户的剩余 剩余抽奖次数
	 * @param udid
	 * @param lucky_id
	 * @param srcObj
	 * @return
	 */
	public Long getLuckyDrawSurplusCount(String udid,LuckyDrawEvent srcObj) {
		Long lucky_id = srcObj.getId();
		Long sy_count = 0L;
		Long all_rigth_count = UserQuestion.dao.queryCountByLucIdUdid(lucky_id,udid,srcObj.getStart_time(),srcObj.getEnd_time());//所有正确数
		if(all_rigth_count!=null && all_rigth_count > 0){
			sy_count = all_rigth_count;
			List<LuckyDrawHistory>  lucky_historys = LuckyDrawHistory.dao.queryCountByLuckyId(lucky_id,udid,null,null,null);//抽奖次数
			if(lucky_historys != null && !lucky_historys.isEmpty()){
				sy_count = all_rigth_count - lucky_historys.size();//已经抽奖次数
			}
		}
		return sy_count;
	}
	
	/**
	 * 这个活动，用户的剩余 剩余抽奖次数
	 * @param udid
	 * @param lucky_id
	 * @param srcObj
	 * @return
	 */
	public Long getWinLuckyDrawSurplusCount(String udid,LuckyDrawEvent srcObj) {
		Long lucky_id = srcObj.getId();
		Long sy_count = 0L;
		Long all_rigth_count = UserQuestion.dao.queryCountByLucIdUdid(lucky_id,udid,srcObj.getStart_time(),srcObj.getEnd_time());//所有正确数
		if(all_rigth_count!=null && all_rigth_count > 0){
			sy_count = all_rigth_count;
			List<LuckyDrawHistory>  lucky_historys = LuckyDrawHistory.dao.queryCountByLuckyId(lucky_id,udid,null,null,null);//抽奖次数
			if(lucky_historys != null && !lucky_historys.isEmpty()){
				sy_count = all_rigth_count - lucky_historys.size();//已经抽奖次数
				if(sy_count > 0){//剩余次数,判断下中奖信息
					int win_count = getWinLuckyCount(srcObj,lucky_historys); //中奖数
					if(win_count > 0){//已中奖次数
						Integer config_win_count = srcObj.getWin_count();
						if(config_win_count != null && config_win_count > 0){
							int tmp_sy_count = config_win_count - win_count;
							if(tmp_sy_count > 0){//还有剩余次数
								sy_count = sy_count < tmp_sy_count ? sy_count : tmp_sy_count;
							}else{
								sy_count = 0L;
							}
						}
					}
				}
			}
		}
		return sy_count;
	}
	
	/**
	 * 判断中奖数
	 * @param event
	 * @param list
	 * @return
	 */
	public int getWinLuckyCount(LuckyDrawEvent event,List<LuckyDrawHistory>  list){
		int count = 0 ;
		Integer day = event.getWin_day();
		
		Long start_time = null;
		if(day != null){
			start_time = DateUtil.addDay(DateUtil.clearTiemConvert(new Date()).getTime(),0-day).getTime();
		}
		
		for(int i=0;i<list.size();i++){
			LuckyDrawHistory obj = list.get(i);
			if(LuckyDrawBefore.WIND.equals(obj.getStatus()) && (start_time == null || start_time <= obj.getCreated_at().getTime())){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 获取最接近的互动问答
	 * @param episode_id
	 * @param time
	 * @param baike_array
	 */
	public JSONObject loadLastCloseQuestion(Long episode_id, Long time,String udid) {
		JSONObject json = null;
		Question question = Question.dao.queryByLtEndTimeMaxTime(episode_id,time);
		if(question!=null){
			json = new JSONObject();
			json.put("id",question.getId());
			json.put("title",question.getTitle());
			json.put("summary",question.getSummary());
//			json.put("answer_id",answer_id);//答案id
			json.put("deadline",question.getDeadline());//结束答题时间
			json.put("public_time",question.getPublic_time());//公布答题时间
			json.put("updated_at",question.getUpdated_at().getTime());
			
			Integer status = loadUserQuestion(question,udid,time,json);
			String banner_title = getBanner_title(status);
			if(StringUtils.isNotBlank(banner_title)){
				BannerImg img = BannerImg.dao.querySwitchPriorityByObj(null, episode_id, question.getId());
				if(img!=null){
					json.put("banner_title",ComUtil.getHtmlBrStr(banner_title));
					json.put("banner_img_path",ComUtil.getStHttpPath(img.getPath()));					
				}
			}
			
			List<QuestionOption> opts = QuestionOption.dao.queryByQuestionId(question.getId());
			
			JSONArray opt_array = new JSONArray();
			for(int i=0;i<opts.size();i++){
				QuestionOption opt = opts.get(i);
				JSONObject opt_json = new JSONObject();
				opt_json.put("id",opt.getId());
				opt_json.put("title",opt.getTitle());
				opt_array.add(opt_json);
			}
			json.put("options",opt_array);
		}
		return json;
	}
	
	/**
	 * 根据互动问答的状态获取随机的文案
	 * @param question_status
	 * @return
	 */
	private String getBanner_title(Integer question_status){
		if(ANSWER_NO_WRITE_NO.equals(question_status)){
			question_status = ANSWER_NO_PUB;
		}
		String banner_title = null;
		List<BannerTitle> list = BannerTitle.dao.queryByQuestion_status(question_status);
		if(list!=null && !list.isEmpty()){
			BannerTitle title = list.get(RandomUtil.getRandomInt(list.size()));
			banner_title = title.getTitle();
		}
		return banner_title;
	}
	
	
//	2：公布答案（答题正确）
//	3：公布答案（答题错误）
//	4：公布答案（未答题
//	5：未答题（可以答题状态）
//	6：未答题（不可以答题）
//	7：已答题（等待答案中）
	public static final Integer QUESTION_NO_SHOW = 0;//0 互动问答还没有出现
	public static final Integer ANSWER_RIGHT = UserQuestion.RIGHT;//2：公布答案（答题正确）
	public static final Integer ANSWER_WRONG = UserQuestion.WRONG;//3：公布答案（答题错误）
	public static final Integer ANSWER_NO_PUB = 4;//4：公布答案（未答题)
	public static final Integer ANSWER_NO_WRITE_YES = 5;//5：未答题（可以答题状态）
	public static final Integer ANSWER_NO_WRITE_NO = 6;//6：未答题（不可以答题）
	public static final Integer ANSWER_ED_WAIT_RESULT = 7;//7：已答题（等待答案中）
	
	/**
	 * 获取用户参与这个互动问答
	 * @param episode_id
	 * @param time
	 * @param baike_array
	 */
	public Integer loadUserQuestion(Question question,String udid,Long time,JSONObject json) {
		Long deadline = question.getDeadline();//关闭答题时间
		Long public_time = question.getPublic_time();//答案公布时间
		Long user_option_id = null;
		
		if(StringUtils.isNotBlank(udid)){
			UserQuestion uq = UserQuestion.dao.queryByQuestionIdUdid(question.getId(),udid);
			if(uq!=null){
				user_option_id = uq.getOption_id();
				json.put("user_option_id",user_option_id);//用户选择的选项id
//				user_option_status = uq.getStatus();
//				json.put("user_option_status",); 
			}
		}
		Integer status = null;
		if(user_option_id == null){//未答题
			if(deadline!=null){
				if(time < deadline){
					status = ANSWER_NO_WRITE_YES;//未答题，可以答题
				}else{
					if(public_time != null && time >= public_time){
						Long answer_id = question.getAnswer_id(); 
						json.put("answer_id",answer_id);//用户选择的选项id
						status = ANSWER_NO_PUB;
					}else{
						status = ANSWER_NO_WRITE_NO;
					}
				}
			}else{
				status = ANSWER_NO_WRITE_YES;//未答题，可以答题
			}
		}else{//已答题
			if(public_time!=null && time >= public_time){
				Long answer_id = question.getAnswer_id(); 
				json.put("answer_id",answer_id);//用户选择的选项id
				if(user_option_id.equals(answer_id)){
					status = ANSWER_RIGHT;//未答题，可以答题					
				}else{
					status = ANSWER_WRONG;//未答题，可以答题
				}
			}else{
				status = ANSWER_ED_WAIT_RESULT;//已答题，等待答案中
			}
		}
		
		if(ANSWER_RIGHT.equals(status)){//答对，返回答对题数量
			UserQuestion uq = UserQuestion.dao.queryCountByEpisodeIdUdid(question.getEpisode_id(), udid);
			if(uq != null){
				json.put("user_right_count",uq.getLong("user_right_count"));
			}
		}
		
		json.put("status",status);//用户选择的选项id
		
		return status;
	}

	/**
	 * 获取最接近的歌曲信息
	 * @param episode_id
	 * @param time
	 * @param baike_array
	 */
	public JSONObject loadLastCloseMusic(Long episode_id, Long time) {
		JSONObject json = null;
		Element obj = Element.dao.queryByLtEndTimeMaxTime(episode_id,ElementType.music.toString(),time);
		if(obj!=null){
			Music srcObj = Music.dao.findById(obj.getFid());
			if(srcObj!=null){
				json = new JSONObject();
				
				json.put("id",srcObj.getId());
				json.put("cover",ComUtil.getStHttpPath(srcObj.getCover()));
				json.put("title",srcObj.getTitle());
				json.put("tag",obj.getTag());
				json.put("singer",srcObj.getSinger());
				json.put("summary",srcObj.getSummary());
				json.put("updated_at",srcObj.getUpdated_at().getTime());
			}
		}
		return json;
	}

	/**
	 * 获取最接近的微直播信息
	 * @param episode_id
	 * @param time
	 * @param baike_array
	 */
	public JSONObject loadLastCloseWeibo(Long episode_id, Long time) {
		JSONObject json = null;
		
//		WeiboFeed weibo = WeiboFeed.dao.queryByLtEndTimeMaxTime(episode_id,time);
//		if(weibo!=null){
//			json = new JSONObject();
//			
//			json.put("id",weibo.getId());
//			json.put("sender_name",weibo.getSender_name());
//			json.put("sender_avatar",ComUtil.getStHttpPath(weibo.getSender_avatar()));
//			json.put("content",weibo.getContent());
//			
//			weibo.parseImageJson(json);;
//		}
		
		MecWeiboFeed weibo = MecWeiboFeed.dao.queryByLtEndTimeMaxTime(episode_id,time);
		if(weibo!=null){
			json = new JSONObject();
			
			json.put("id",weibo.getId());
			json.put("sender_name",weibo.getSender_name());
			json.put("sender_avatar",ComUtil.getStHttpPath(weibo.getSender_avatar()));
			json.put("content",weibo.getContent());
			json.put("updated_at",weibo.getUpdated_at().getTime());
			weibo.parseImageJson(json);;
		}
		
		return json;
	}
	
	/**
	 * 获取最接近的百科信息
	 * @param episode_id
	 * @param time
	 */
	public JSONObject loadLastCloseBaike(Long episode_id, Long time) {
		JSONObject json = null;
		Element obj = Element.dao.queryByLtEndTimeMaxTime(episode_id,ElementType.baike.toString(),time);
		if(obj!=null){
			Baike srcObj = Baike.dao.findById(obj.getFid());
			if(srcObj!=null){
				json = new JSONObject();
				
				json.put("id",srcObj.getId());
				json.put("title",srcObj.getTitle());
				json.put("cover",ComUtil.getStHttpPath(srcObj.getCover()));
				json.put("summary",ComUtil.getHtmlBrStr(srcObj.getSummary()));
				json.put("updated_at",srcObj.getUpdated_at().getTime());
			}
		}
		return json;
	}

	/**
	 * 获取最接近的人物信息
	 * @param episode_id
	 * @param time
	 */
	public JSONObject loadLastCloseMecPerson(Long episode_id, Long time) {
		JSONObject json = null;
		Element obj = Element.dao.queryByLtEndTimeMaxTime(episode_id,ElementType.mec_person.toString(),time);
		if(obj!=null){
			MecPerson srcObj = MecPerson.dao.findById(obj.getFid());
			if(srcObj!=null){
				json = new JSONObject();
				json.put("id",srcObj.getId());
//				json.put("type_title","人物介绍");
				json.put("name",srcObj.getName());
				json.put("avatar",ComUtil.getStHttpPath(srcObj.getView_avatar()));
				json.put("description",ComUtil.getHtmlBrStr(srcObj.getDescription()));
				
				json.put("updated_at",srcObj.getUpdated_at().getTime());
			}
		}
		return json;
	}
}
