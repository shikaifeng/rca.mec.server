package tv.zhiping.mec.api.program.ctrl;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.acr.api.AcrApi;
import tv.zhiping.acr.api.AcrApi.AcrChooseEpisode;
import tv.zhiping.acr.api.AcrApi.AcrChooseRes;
import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mec.api.common.ApiCons;
import tv.zhiping.mec.api.jfinal.ApiRes;
import tv.zhiping.mec.api.jfinal.ApiValidator;
import tv.zhiping.mec.api.jfinal.CommIdApiValidator;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Element.ElementType;
import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.mec.user.model.UserEpisode;
import tv.zhiping.mec.user.model.UserEpisode.UserEpisodeType;
import tv.zhiping.redis.Redis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * 剧集相关接口
 * @author 张有良
 * 2014-5-17
 */
public class EpisodeApiCtrl extends BaseElementApi {
	
	private Redis redis = new Redis();
	
	/**
	 * 根据acr识别返回剧情信息
	 * 测试：http://localhost:8080/api/episode/recognition?acrCallBack=?
	 */
	@Before(EpisodeRecognitionApiValidator.class)
	public void recognition(){
		Long ucid = getParaToLong(Cons._UCID);
		String acrCallBack = getPara("acrCallBack");
		ApiRes res = new ApiRes();
		res.setStatus(ApiCons.STATUS_SUC);
		
		//解析acr 协议
		AcrApi acrApi = new AcrApi();
		AcrChooseRes acrRes = acrApi.parset2MdmResult(acrCallBack);
		AcrChooseEpisode acrEpisode = acrRes.getPossible_acr_chooose_episode();
		Integer acrResEpisodeCount = acrRes.getPossible_acr_chooose_episode_count();
		Integer offsetCount = acrRes.getPossible_acr_chooose_offset_count();
		
		log.debug("识别结果 节目数量>"+acrRes.getProgrma_count()+" 剧集数量="+acrRes.getEpisode_count());
		Long episodeId = null;
		Long programId = null;
		Long offset = 0L;
		
		if(acrEpisode!=null){//不太可能的，acr的id是要mdm给的
			episodeId = acrEpisode.getEpisode_id();
			programId = acrEpisode.getProgram_id();
			offset = acrEpisode.getOffset();
		}else{
			log.warn("data errror acrCallBack:"+acrCallBack);
		}
		
		if(programId!=null && episodeId!=null && offset!=null){
			JSONObject data = new JSONObject();
			res.setData(data);
			
			log.debug("最可能结果 节目id="+programId+" 剧集id="+episodeId+"  第"+acrEpisode.getCurrent_episode()+"集  集数="+acrResEpisodeCount+" offsetCount="+offsetCount+" offset="+offset);
			Integer acr_running = Cons.YES;
			Integer live_flag = acrRes.getLive_flag();
			Integer display_object = DISPLAY_OBJ_PROGRAM;//默认显示节目信息
			Boolean show_scene = Cons.TRUE;
			
			if(Cons.ONE.equals(acrResEpisodeCount)){//acr结果中这个节目只有单个剧集
				if(Cons.ONE.equals(offsetCount)){//单个的offset时间
					acr_running = Cons.NO;
				}else{
					show_scene = false;
				}
				display_object = DISPLAY_OBJ_EPISODE;
			}
			
			getProgramInfo(programId, episodeId,data);
			setEpgInfo(offset, acr_running,live_flag,display_object,show_scene,data);
			
			if(Cons.FALSE.equals(show_scene)){//多个offsettime，不显示场景 设置scene=0
				JSONObject episode_json = data.getJSONObject("episode");
				if(episode_json!=null){
					episode_json.put("scene_count",0);//设置未0 ，不显示feed信息
				}
			}
			
			String user_episode_type = getUser_episode_type_by_display_object(data);
			//增加识别记录
			saveUserEpisode(ucid,programId,episodeId,offset,user_episode_type);
		}else{
			log.warn("data errror acrCallBack:"+acrCallBack);
		}
		super.renderJsonV1(res);		
	}
	
	public static final Integer DISPLAY_OBJ_EPISODE = 1;//1:剧集
	public static final Integer DISPLAY_OBJ_PROGRAM = 2;//2:节目
	
	//根据app端显示类型处理历史
	private String getUser_episode_type_by_display_object(JSONObject data){
		JSONObject epg = data.getJSONObject("epg");
		if(epg!=null){
			if(DISPLAY_OBJ_EPISODE.equals(epg.getInteger("display_object"))){//显示剧集
				return UserEpisodeType.episode.toString();
			}else{
				return UserEpisodeType.program.toString();
			}
		}
		return null;
	}

	public static void main(String[] args) {
		JSONObject data = new JSONObject();
		JSONObject obj = data.getJSONObject("epg");
		System.out.println(obj);
	}
	
	/**
	 * 根据识别记录id返回节目，剧集信息
	 * 测试：http://localhost:8080/api/episode/user_timeline_show?_ucid=1&id=1
	 */
	@Before(CommIdApiValidator.class)
	public void user_timeline_show(){
		Long id = getParaToLong("id");
		String key = "user_episode_id_" + id.toString();
		String value = redis.get(key);
		if (value != null) {
			super.renderJsonV1(value);
		}
		else {
			ApiRes res = new ApiRes();
			res.setStatus(ApiCons.STATUS_SUC);
			
			UserEpisode ue = UserEpisode.dao.findById(id);
			
			if(ue!=null){
				JSONObject data = new JSONObject();
				
				Long programId = ue.getProgramId();
				Long episodeId = ue.getEpisodeId();
				
				Long offset = Scene.dao.queryMaxEndTimeByEpisodeId(episodeId);
				if(offset==null){
					offset = 0L;
				}
				offset = offset * 1000L;
				
				Integer acr_running = Cons.NO;
				Integer live_flag = Cons.NO;
				Integer display_object = 1;//默认显示剧集信息
				if(episodeId == null || Cons.DEF_NULL_NUMBER.equals(episodeId)){
					display_object = 2;//如果没有剧集，显示节目
				}
				Boolean show_scene = Cons.TRUE;//是否显示场景
				
				getProgramInfo(programId,episodeId,data);
				setEpgInfo(offset, acr_running,live_flag,display_object,show_scene,data);
				res.setData(data);
			}else{
				log.warn("data errror user_episode:"+id);
			}
			value = super.getJsonString(res);
			redis.setnx(key, value);
			super.renderJsonV1(value);
		}
	}
	
	private void setEpgInfo(Long offset,Integer acr_running, Integer live_flag,Integer display_object,Boolean show_scene,JSONObject data){
		JSONObject epgObj = new JSONObject();
		epgObj.put("live_flag",live_flag);
		epgObj.put("offset_time",offset);
		epgObj.put("acr_running",acr_running);
		
		JSONObject episode_json = data.getJSONObject("episode_json");
		if(episode_json!=null){
			if(DISPLAY_OBJ_EPISODE.equals(display_object) && episode_json.containsKey("scene_count") && episode_json.getLong("scene_count")>0){//系那是剧集，但是没有打点过
				display_object = DISPLAY_OBJ_EPISODE;
			}else{
				display_object = DISPLAY_OBJ_PROGRAM;
			}			
		}
		epgObj.put("display_object",display_object);
		
		data.put("epg",epgObj);
	}
	
	/**
	 * 返回节目或剧集的信息
	 * @param programId 节目id
	 * @param episodeId 剧集id
	 */
	private void getProgramInfo(Long programId, Long episodeId,JSONObject data) {
		//节目，剧集信息
		Program program = Program.dao.findById(programId);
		Episode episode = Episode.dao.findById(episodeId);
		
		getProgramAndEpisodeInfo(program,episode, data);
		
		JSONObject programJson = data.getJSONObject("program");
		JSONArray program_persons_json = null;
		if(programJson != null){//节目维度的信息
			parsPersonMusicElement2JSONByProgramId(programId,programJson);
			program_persons_json = programJson.getJSONArray("person_list");
		}
		
		JSONObject episode_json = data.getJSONObject("episode");
		if(episode_json!=null){//剧集维度的信息
			parsPersonMusicElement2JSONByEpisodeId(programId,episodeId,episode_json);//剧集维度明星、音乐
			
			JSONArray episode_persons_json = episode_json.getJSONArray("person_list");
			if(episode_persons_json==null || episode_persons_json.isEmpty()){//如果为空，直接继承
				episode_json.put("person_list",program_persons_json);
			}
		}
	}

	
	/**
	 * 增加识别记录
	 * @param ucid 用户id
	 * @param program_id 节目id
	 * @param episodeId  剧集id
	 * @param offset     offset时间
	 * @param type       识别到剧集还是节目
	 */
	private void saveUserEpisode(Long ucid,Long program_id,Long episodeId, Long offset,String type) {
		try {
			if(program_id!=null && ucid!=null){
				UserEpisode ue = UserEpisode.dao.queryByUcidAndEpisode(ucid,program_id,episodeId,type);			
				if(ue==null){
					ue = new UserEpisode();
					ue.setUcid(ucid);
					
					ue.setProgramId(program_id);
					ue.setStartTime(offset);
					ue.setEndTime(offset);
				}
				
				ue.setType(type);
				
				if(UserEpisodeType.episode.toString().equals(type)){//剧集维度才有时间的,剧集id
					ue.setEpisodeId(episodeId);
					
					if(ue.getStartTime() == null || ue.getStartTime()>offset){
						ue.setStartTime(offset);
					}
					
					if(ue.getEndTime() == null || ue.getEndTime()<offset){
						ue.setEndTime(offset);
					}
				}else{
					ue.setEpisodeId(Cons.DEF_NULL_NUMBER);
				}
				
				ue.setLastAt(new Timestamp(System.currentTimeMillis()));
				
				
				if(ue.getId() == null){
					ue.setAddDef();
					ue.save();
				}else{
					ue.setUpdDef();
					ue.update();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	
	/**
	 * 获取节目详情接口
	 * 测试：http://localhost:8080/api/episode/show?_ucid=1&id=1
	 */
	@Before(CommIdApiValidator.class)
	public void show() {
//		Long episodeId = getParaToLong("id");
//		
//		ApiRes res = new ApiRes();
//		
//		JSONObject data = new JSONObject();
//		res.setData(data);
//		
//		if(episodeId!=null){
//			res.setStatus(ApiCons.STATUS_SUC);
//			getProgramAndEpisodeInfo(episodeId,data);
//			
//			JSONObject episode_json = data.getJSONObject("episode");
//			if(episode_json!=null){
//				parsAllElement2JSONBySceneId(episodeId,null,episode_json);
//			}
//		}else{
//			log.warn("data errror episodeId:"+episodeId);
//		}
//	
//		super.renderJsonV1(res);		
	}
	
	
	/**
	 * 获取用户的播放历史接口
	 * 介绍:传入分页页码和分页条数，返回时间倒序的播放历史
	 * 测试：http://localhost:8080/api/episode/user_timeline?_ucid=1&page_number=2&page_size=2
	 * @author 樊亚容 
	 * 2014-5-17
	 */
	@Before(EpisodeUserTimelineApiValidator.class)
	public void user_timeline(){
		Long ucid = getParaToLong(Cons._UCID);
		int pageNumber = getParaToInt("page_number", 1);
		int pageSize = getParaToInt("page_size", Cons.DEF_PAGE_SIZE);
		
		String key = "user_timeline_" + ucid.toString() + "_" + Integer.toString(pageNumber) + "_" + Integer.toString(pageSize);
		String value = redis.get(key);
		if (value != null) {
			super.renderJsonV1(value);
		}
		else {
			Page page = UserEpisode.dao.paginate(ucid, pageNumber, pageSize);
			List<UserEpisode> queeryResult = page.getList();
			ApiRes res = new ApiRes();
			res.setStatus(ApiCons.STATUS_SUC);
			res.setPage(pageNumber, pageSize, page.getTotalPage());
			
			List<JSONObject> data =  null;
			if(queeryResult!=null && !queeryResult.isEmpty()){
				data = new ArrayList<JSONObject>();
				for(UserEpisode deviceEpisode : queeryResult){
					JSONObject json = new JSONObject();
					
					json.put("id", deviceEpisode.getId());
					
					Long programId = deviceEpisode.getProgramId();
					Long episodeId = deviceEpisode.getEpisodeId();
					
					json.put("program_id", programId);
					json.put("episode_id", episodeId);
					
					Episode episode = null;
					if(episodeId!=null && !Cons.DEF_NULL_NUMBER.equals(episodeId)){
						episode = Episode.dao.findById(episodeId);
					}
					
					Program program = Program.dao.findById(programId);
					
					String title = null;
					String cover = null;
					if(program != null){
						title = ComUtil.getEpisodeAppTitle(episode, program);
						Element element = Element.dao.queryByEpisodeIdType(episodeId,ElementType.video.toString());;
						if(element!=null){
							cover = element.getCover();
						}
						if(StringUtils.isBlank(cover)){
							cover = ComUtil.getEpisodeCover(episode, program);
						}
					}else{
						log.warn("data errror programId:"+programId+"  episodeId:"+episodeId);
					}
					
					json.put("title", title);
					json.put("cover", ComUtil.getStHttpPath(cover));
					data.add(json);				
				}			
			}	
			res.setData(data);
			value = super.getJsonString(res);
			redis.setnx(key, value);
			super.renderJsonV1(value);
		}
	}

	
	
	
	/**
	 * 获取用户的播放历史接口校验
	 * @author 范亚容
	 *
	 */
	public static class EpisodeUserTimelineApiValidator extends ApiValidator {
		public void validate(Controller controller) {
			super.validate(controller);
			addPageNumberValidateUid(controller);
		}
	}
	
	

	/**
	 * acr识别校验
	 * @author 张有良
	 */
	public static class EpisodeRecognitionApiValidator extends ApiValidator {
		public void validate(Controller controller) {
			super.validate(controller);
			validateRequiredString("acrCallBack", _MSG_START+"acrCallBack", "acrCallBack不能为空");
		}
	}
}
