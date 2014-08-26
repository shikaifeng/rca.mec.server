package tv.zhiping.mec.api.tmallbox.ctrl;

import java.util.List;

import tv.zhiping.jfinal.BaseCtrl;
import tv.zhiping.mec.api.feed.service.FeedService;
import tv.zhiping.mec.api.feed.service.LuckyService;
import tv.zhiping.mec.luck.model.LuckyDrawBefore;
import tv.zhiping.mec.luck.model.LuckyDrawEvent;
import tv.zhiping.mec.luck.model.LuckyDrawHistory;
import tv.zhiping.mec.luck.model.LuckyDrawOption;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;

public class LuckyDrawCtrl extends BaseCtrl {
	/**
	 * 方法：index 
	 * 地址：/m/lucky_draw/the_voice_of_china
	 * 功能：抽奖页面
	 */
	public void the_voice_of_china()
	{
		Long id = getParaToLong("id");
		String udid = getPara("udid");
		//剩余抽奖次数
		
		JSONObject obj = new JSONObject();
		obj.put("id",id);
		obj.put("udid",udid);
		
		LuckyDrawEvent event = LuckyDrawEvent.dao.findById(id);
		
		FeedService feedService = new FeedService();
		if(event!=null){
			long sy_count = feedService.getLuckyDrawSurplusCount(udid, event);
			obj.put("lucky_surplus_count",sy_count);
		}
		
		
		LuckyDrawHistory history = LuckyDrawHistory.dao.queryByLuckyIdStatus(id,LuckyDrawBefore.WIND);
		if(history!=null){
			LuckyDrawOption option = LuckyDrawOption.dao.findById(history.getOption_id());
			if(option!=null){
				LuckyService luckyService = new LuckyService();
				obj.put("url",luckyService.getLucky_asy_notify(option.getUrl(),history.getId()));	
			}
		}
		
		
		List<LuckyDrawOption> opts = LuckyDrawOption.dao.queryByLuckyId(id,null);
		if(opts!=null && !opts.isEmpty()){
			JSONArray array = new JSONArray();
			for(int i=0;i<opts.size();i++){
				LuckyDrawOption opt = opts.get(i);
				JSONObject json = new JSONObject();
				json.put("position",opt.getPosition());
				json.put("description",opt.getTitle());
				array.add(json);
			}
			setAttr("luckList",array.toJSONString());
		}
		
		
		Page<LuckyDrawHistory> page = LuckyDrawHistory.dao.paginate(id, 1, 3);
		List<LuckyDrawHistory> historys = page.getList();
		if(historys!=null){
			JSONArray hist_array = new JSONArray();
			for(int i=0;i<historys.size();i++){
				JSONObject json = new JSONObject();
				LuckyDrawHistory hist = historys.get(i);
				json.put("udid",hist.getUdid());
				LuckyDrawOption option = LuckyDrawOption.dao.findById(hist.getOption_id());
				if(option!=null){
					json.put("opt_title",option.getTitle());
				}
				hist_array.add(json);
			}
			
			setAttr("historys",hist_array);
		}
		setAttr("obj", obj);
		
		int random = (int)(Math.random()*1000);
		setAttr("random", random);
		render("/page/m/lucky_draw/the_voice_of_china/index.jsp");
	}
}
