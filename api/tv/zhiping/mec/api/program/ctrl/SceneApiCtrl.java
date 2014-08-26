package tv.zhiping.mec.api.program.ctrl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mec.api.common.ApiCons;
import tv.zhiping.mec.api.jfinal.ApiBaseCtrl;
import tv.zhiping.mec.api.jfinal.ApiRes;
import tv.zhiping.mec.api.jfinal.CommIdApiValidator;
import tv.zhiping.mec.feed.model.MecWeiboFeed;
import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.redis.Redis;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;

/**
 * 场景相关接口
 * @author 樊亚容
 * 2014-5-19
 */
public class SceneApiCtrl extends ApiBaseCtrl {
	
	private Redis redis = new Redis();
	
	/**
	 * 根据剧集ID获取所有场景feed
	 * 介绍:
	 * 测试：http://localhost:8080/api/scene/episode_timeline?_ucid=1&id=1
	 * @author 樊亚容 2014-5-19
	 */
	@Before(CommIdApiValidator.class)
	public void episode_timeline(){
		Long episodeId = getParaToLong("id");
		String key = "get_feeds_by_episode_id_" + episodeId.toString();
		String value = redis.get(key);
		if (value != null) {
			super.renderJsonV1(value);
		}
		else {
			ApiRes res = new ApiRes();
			res.setStatus(ApiCons.STATUS_SUC);
			JSONObject data = new JSONObject();
			
//			int pageNumber = getParaToInt("page_number", 1);
//			int pageSize = getParaToInt("page_size", Cons.MAX_PAGE_SIZE);
			
			List<Scene> queryResult = Scene.dao.queryAllByEpisodeIdValidate(episodeId);
			
			System.out.println(queryResult.size());
			List<JSONObject> list = new ArrayList<JSONObject>();
			if(queryResult !=  null && !queryResult.isEmpty()){
				Episode episode = Episode.dao.findById(episodeId);
				if(episode!=null){
					Program program = Program.dao.findById(episode.getPid());
					if(program!=null){
						String episode_title = ComUtil.getEpisodeAppTitle(episode, program);
						for(Scene scene:queryResult){
							JSONObject sceneData = new JSONObject();
							sceneData.put("id", scene.getId());
							sceneData.put("cover", ComUtil.getStHttpPath(scene.getCover()));
							if(StringUtils.isNotBlank(scene.getSummary())){
								sceneData.put("summary",scene.getTitle());
							}else{
								sceneData.put("summary",episode_title);						
							}
							sceneData.put("start_time", scene.getStart_time());
							sceneData.put("end_time", scene.getEnd_time());
							list.add(sceneData);
						}
					}
				}
			}else{
				Long weiboFeedCount = MecWeiboFeed.dao.queryCountByEpisode(episodeId);
				if(weiboFeedCount!=null &&  weiboFeedCount>0){
					Episode episode = Episode.dao.findById(episodeId);
					if(episode!=null){
						Program program = Program.dao.findById(episode.getPid());
						if(program!=null){
							MecWeiboFeed weiboFeed = MecWeiboFeed.dao.queryMinMaxTimeByEpisode(episodeId);
							if(weiboFeed!=null){
								String title = ComUtil.getEpisodeAppTitle(episode, program);
								JSONObject sceneData = new JSONObject();
								sceneData.put("id", Cons.VIRTUAL_SCENE_ID * episode.getId());
								sceneData.put("cover",null);
								sceneData.put("summary",title);
								sceneData.put("start_time",weiboFeed.getLong("min_time"));
								sceneData.put("end_time",weiboFeed.getLong("max_time"));
								list.add(sceneData);
							}
						}
					}
				}			
			}
			data.put("list", list);
			res.setData(data);
			value = super.getJsonString(res);
			redis.setnx(key, value);
			super.renderJsonV1(value);
		}
	}	
}
