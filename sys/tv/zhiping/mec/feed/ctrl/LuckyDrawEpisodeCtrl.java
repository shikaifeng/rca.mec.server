package tv.zhiping.mec.feed.ctrl;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import tv.zhiping.common.Cons;
import tv.zhiping.mec.api.common.ApiCons;
import tv.zhiping.mec.epg.model.MecSchedule;
import tv.zhiping.mec.luck.model.LuckyDrawEpisode;
import tv.zhiping.mec.luck.model.LuckyDrawEvent;
import tv.zhiping.mec.service.ArithLuckDrawService;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;

public class LuckyDrawEpisodeCtrl extends SysBaseCtrl {
	
	public void set_sign_time(){
		String res = Cons.JSON_SUC;
		
		Long id = getParaToLong("id");
		String type = getPara("type");
		Long schedule_id = getParaToLong("schedule_id");
		
		String msg = null;
		try {
			LuckyDrawEpisode obj = LuckyDrawEpisode.dao.findById(id);
			MecSchedule schedule = MecSchedule.dao.findById(schedule_id);
			if(obj!=null && schedule!=null){
				Long time = System.currentTimeMillis() - schedule.getMec_start_at();
				time = time / 1000;
				if(time>-1){
					if("start_time".equals(type)){//设置开始时间
						obj.setStart_time(time);
						
						LuckyDrawEvent event = LuckyDrawEvent.dao.findById(obj.getLucky_id());
						if(event!=null){
							ArithLuckDrawService.service.processLuckyDraw(event);
						}
					}else{//设置截止时间
						obj.setEnd_time(time);
					}
					obj.setUpdDef();
					obj.update();					
				}else{
					msg = "time 小于0";
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);			
			msg = e.getMessage();
		}
		if(StringUtils.isNotBlank(msg)){
			JSONObject json = new JSONObject();
			json.put("status",ApiCons.STATUS_ERROR);
			json.put("msg",msg);
			res = json.toJSONString();
		}
		renderJson(res);
	}
	
	
	
	
	public  void save(){
		LuckyDrawEpisode obj = getModel(LuckyDrawEpisode.class, "obj");
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

	public void input(){
		Long id = getParaToLong("id");
		if(id!=null){
			LuckyDrawEpisode obj = LuckyDrawEpisode.dao.findById(id);
			setAttr("obj", obj);
		}
		render("/page/feed/luck/episode/input.jsp");
	}
}
