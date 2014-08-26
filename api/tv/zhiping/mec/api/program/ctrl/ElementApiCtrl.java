package tv.zhiping.mec.api.program.ctrl;


import tv.zhiping.common.Cons;
import tv.zhiping.mec.api.common.ApiCons;
import tv.zhiping.mec.api.jfinal.ApiRes;
import tv.zhiping.mec.api.jfinal.CommIdApiValidator;
import tv.zhiping.redis.Redis;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;

/**
 * 元素相关接口
 * @author 张有良
 * 2014-5-20
 */
public class ElementApiCtrl extends BaseElementApi {
	
	private Redis redis = new Redis();
	
	/**
	 * 根据场景ID获取所有元素 包含:明星，百科，音乐,视频,imdb百科,微直播
	 * 测试：http://localhost:8080/api/element/scene_timeline?_ucid=1&id=1
	 */
	@Before(CommIdApiValidator.class)
	public void scene_timeline () {
		Long scene_id = getParaToLong("id");
//		Long episode_id = getParaToLong("episode_id");
		String key = "scene_id_" + scene_id.toString();
		String value = redis.get(key);
		if (value != null) {
			super.renderJsonV1(value);
		}
		else {
		
			ApiRes res = new ApiRes();
			
			JSONObject data = new JSONObject();
			res.setData(data);
			
			if(scene_id!=null){
				res.setStatus(ApiCons.STATUS_SUC);
				if(scene_id>Cons.VIRTUAL_SCENE_ID){
					Long episode_id = scene_id / Cons.VIRTUAL_SCENE_ID;
					parsWeiboFeedElement2JSONBySceneId(episode_id,data);
				}else{
					parsAllElement2JSONBySceneId(null,scene_id,data);
				}
			}
			value = super.getJsonString(res);
			redis.setnx(key, value);
			super.renderJsonV1(value);
		}
	}
}
