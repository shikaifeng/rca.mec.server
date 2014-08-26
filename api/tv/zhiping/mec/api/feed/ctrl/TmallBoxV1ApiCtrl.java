package tv.zhiping.mec.api.feed.ctrl;

import java.util.HashMap;

import tv.zhiping.acr.api.AcrApi;
import tv.zhiping.acr.api.AcrApi.TmallBoxAcrRes;
import tv.zhiping.common.cache.CacheKey;
import tv.zhiping.mec.api.common.ApiCons;
import tv.zhiping.mec.api.feed.service.FeedService;
import tv.zhiping.mec.api.feed.service.LuckyService;
import tv.zhiping.mec.api.jfinal.ApiBaseCtrl;
import tv.zhiping.mec.api.jfinal.ApiRes;
import tv.zhiping.mec.api.jfinal.ApiValidator;
import tv.zhiping.mec.app.model.AppConfig;
import tv.zhiping.mec.feed.model.Question;
import tv.zhiping.mec.feed.model.QuestionOption;
import tv.zhiping.mec.luck.model.LuckyDrawBefore;
import tv.zhiping.mec.luck.model.LuckyDrawEvent;
import tv.zhiping.mec.luck.model.LuckyDrawHistory;
import tv.zhiping.mec.user.model.UserQuestion;
import tv.zhiping.redis.Redis;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * 天猫魔盒接口接口
 * @author 张有良
 */
public class TmallBoxV1ApiCtrl extends ApiBaseCtrl {
	
	private Redis redis = new Redis();
	
	/**
	 * 方法: get_system_config
	 * 地址: /api/v1/get_system_config
	 * 描述: 系统配置查询接口
	 */
	public void get_system_config(){
		String key =   CacheKey.APP_SYSTEM_CONFIG;
		
		String value = redis.get(key);
		if (value != null) {
			super.renderJsonV1(value);
		}
		else {
			HashMap<String, String> result = AppConfig.dao.iteratorDatabase();
			ApiRes res = new ApiRes();
			res.setStatus(ApiCons.STATUS_SUC);
			res.setData(result);
			value = super.getJsonString(res);
			redis.setnx(key, value);
			super.renderJsonV1(value);
		}
	}
	
	/**
	 * 方法: get_feeds
	 * 地址: /api/v1/get_feeds
	 * 描述: 实时Feed查询接口
	 * 点播：
	 * http://localhost/api/v1/get_feeds?type=nolive&reference_id=112804&start_time=480000&udid=3307
	 * 
	 * 直播：
	 * http://localhost/api/v1/get_feeds?type=live&reference_id=zhejiangweishi&start_time=1405669318577&udid=3307
	 */
	@Before(TmallBoxV1GetFeedsApiValidator.class)
	public void get_feeds(){
		String type = getPara("type");
		String reference_id = getPara("reference_id");
		Long start_time = getParaToLong("start_time");
		String udid = getPara("udid");
		
		AcrApi acrApi = new AcrApi();
		TmallBoxAcrRes acrRes = acrApi.tmallRecognitionRes(type, reference_id,start_time);
		
		ApiRes res = new ApiRes();
		JSONObject data = new JSONObject();
		res.setData(data);
		
		Long episode_id = acrRes.getEpisode_id();
		Long relative_time = acrRes.getOffsettime();
		if(episode_id!=null && relative_time!=null){
			relative_time = relative_time/1000;
			
			data.put("offset_time",relative_time);
			res.setStatus(ApiCons.STATUS_SUC);
			
			FeedService feedService = new FeedService();
			feedService.loadFeedData(data, episode_id, relative_time,udid);
		}else{
			res.setMsg("episode_id is null or relative_time is null");
			res.setStatus(ApiCons.STATUS_ERROR);
		}
		super.renderJsonV1(res);
	}
	
	/**
	 * 方法: question_and_answer
	 * 地址: /api/v1/question_and_answer
	 * ?question_id=1&answer=1&time=11&udid=3307
	 * 
	 * http://localhost/api/v1/question_and_answer?question_id=157&answer=438&time=123&udid=3307
	 * 描述: 有奖竞答答题接口
	 */
	@Before(TmallBoxV1QuestionAndAnswerApiValidator.class)
	public void question_and_answer(){
		Long question_id = getParaToLong("question_id");
		Long answer = getParaToLong("answer");
		String time = getPara("time");
		String udid = getPara("udid");
		
		ApiRes res = new ApiRes();
		res.setStatus(ApiCons.STATUS_SUC);
		JSONObject data = new JSONObject();
		res.setData(data);
		
		QuestionOption option = QuestionOption.dao.findById(answer);
		if(option == null || !question_id.equals(option.getQuestion_id())){
			res.setStatus(ApiCons.STATUS_ERROR);
			res.setMsg("wrong option");
		}else{
			UserQuestion ud = UserQuestion.dao.queryByQuestionIdUdid(question_id, udid);
			if(ud == null){
				Question question = Question.dao.findById(question_id);
				if(question!=null){
					UserQuestion obj = new UserQuestion();
					obj.setQuestion_id(question_id);
					obj.setOption_id(answer);
					obj.setUdid(udid);
					obj.setClient_time(time);
					
					obj.setAddDef();
					if(answer.equals(question.getAnswer_id())){//表示答对了
						obj.setStatus(UserQuestion.RIGHT);
					}else{
						obj.setStatus(UserQuestion.WRONG);
					}
					obj.save();			
				}
			}else{
				res.setStatus(ApiCons.STATUS_ERROR);//错误
				res.setMsg("processed");
			}			
		}
		
		super.renderJsonV1(res);
	}
	
	
	/**
	 * 方法: draw_result_register
	 * 地址: /api/v1/draw_result_register
	 * 描述: 抽奖的接口
	 * http://localhost/api/v1/draw_result_register?id=1&udid=3307
	 */
	@Before(TmallBoxV1DrawResultRegisterApiValidator.class)
	public void draw_result_register(){
		String udid = getPara("udid");//用户id
		Long id = getParaToLong("id");//抽奖活动id
		
		ApiRes res = new ApiRes();
		
		LuckyDrawEvent event = LuckyDrawEvent.dao.findById(id);
		if(event!=null){
			FeedService feedService = new FeedService();
			
			Long sy_count = feedService.getLuckyDrawSurplusCount(udid,event);//还有抽奖次数
			if(sy_count>0){
				res.setStatus(ApiCons.STATUS_SUC);
				JSONObject data = new JSONObject();
				
				Long sy_win_count = feedService.getWinLuckyDrawSurplusCount(udid,event);//剩余抽奖次数
				
				LuckyService luckyService = new LuckyService();
				if(sy_win_count!=null && sy_win_count>0){//有中奖记录的
					luckyService.excuteLucky(udid,event,data);					
				}else{
					luckyService.excuteAddHistory(udid,event,data);
				}
				
				//剩余次数
//				sy_count = feedService.getLuckyDrawSurplusCount(udid,event);
				data.put("lucky_surplus_count",(--sy_count));//剩余次数
				
				res.setData(data);
			}else{
				res.setStatus(ApiCons.STATUS_ERROR);
				res.setMsg("no_lucky_count");
			}
		}else{
			res.setStatus(ApiCons.STATUS_ERROR);
			res.setMsg("no_lucky_event");
		}
//		System.out.println(super.getJsonString(res));
		super.renderJsonV1(res);
	}
	
	/**
	 * 方法: lucky_asy_notify
	 * 地址: /api/v1/lucky_asy_notify
	 * 描述: 中奖订单回调接口
	 */
	public void lucky_asy_notify(){
		Long id = getParaToLong(0);
		
		LuckyDrawHistory obj = LuckyDrawHistory.dao.findById(id);
		if(obj!=null){
			obj.setStatus(LuckyDrawBefore.TRADE_CREATED);
			obj.setUpdDef();
			obj.update();
		}
		
		renderText("success");
	}
	
	
	/**
	 * tmall 魔盒 抽奖登记 接口校验
	 * @author 张有良
	 */
	public static class TmallBoxV1DrawResultRegisterApiValidator extends ApiValidator {
		public void validate(Controller controller) {
			validateLong("id", _MSG_START+"id", "id 不能为空,且必须是数字");
			validateRequiredString("udid", _MSG_START+"udid", "udid不能为空");
		}
	}
	
	/**
	 * tmall 魔盒 答题 接口校验
	 * @author 张有良
	 */
	public static class TmallBoxV1QuestionAndAnswerApiValidator extends ApiValidator {
		public void validate(Controller controller) {
			validateLong("question_id", _MSG_START+"question_id", "question_id 不能为空,且必须是数字");
			validateLong("answer", _MSG_START+"answer ", "answer 不能为空,且必须是数字");
			validateRequiredString("udid", _MSG_START+"udid", "udid不能为空");
		}
	}
	
	/**
	 * tmall 魔盒 get_feeds 接口校验
	 * @author 张有良
	 */
	public static class TmallBoxV1GetFeedsApiValidator extends ApiValidator {
		public void validate(Controller controller) {
			validateRequiredString("type", _MSG_START+"type", "type不能为空");
			validateRequiredString("reference_id", _MSG_START+"reference_id", "reference_id不能为空");
			validateLong("start_time", _MSG_START+"start_time", "start_time不能为空,且必须是数字");
		}
	}
}