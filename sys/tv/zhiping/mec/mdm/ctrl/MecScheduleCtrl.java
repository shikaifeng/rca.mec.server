package tv.zhiping.mec.mdm.ctrl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mec.epg.model.MecChannel;
import tv.zhiping.mec.epg.model.MecSchedule;
import tv.zhiping.mec.feed.model.SynWeiboTask;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.utils.DateUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MecScheduleCtrl extends SysBaseCtrl {
	public void index() {
		render("/page/mdm/mec_schedule/index.jsp");
	}

	public static Long zs_time = (8 * 3600 + 59 * 60 + 59) * 1000L; // 0:00 到 8:59:59
	public static Long sw_time = (11 * 3600 + 59 * 60 + 59) * 1000L; // 9:00 到 11:59:59
	public static Long xw_time = (17 * 3600 + 59 * 60 + 59) * 1000L; // 12:00 到  17:59:59
	public static Long wj_time = (23 * 3600 + 59 * 60 + 59) * 1000L; // 18:00 到 23:59:59
	public static Long day_time = 24 * 60 * 60 * 1000L; // 18:00 到 23:59:59

	/**
	 * epg页面展现
	 * @throws ParseException
	 */
	public void epg_index() throws ParseException {
		List<MecChannel> channel_list = MecChannel.dao.queryChannelList();
		setAttr("channel_list", channel_list);

		Long mec_channel_id = getParaToLong("mec_channel_id");
		if (mec_channel_id == null && channel_list != null
				&& channel_list.size() > 0) {
			mec_channel_id = channel_list.get(0).getId();
		}

		Integer week = getParaToInt("week");
		Date minDate = null;
		Date maxDate = null;

		Date date = new Date();
		String str = DateUtil.getDateSampleString(date);
		Integer year = Integer.parseInt(str.substring(0, 4));
		if (week == null) {
			week = DateUtil.getWeekOfYear(date);
		}

		JSONObject repObj = new JSONObject();
		repObj.put("mec_channel_id", mec_channel_id);
		repObj.put("mec_channel_name", MecChannel.dao.findById(mec_channel_id)
				.getName());
		repObj.put("week", week);

		repObj.put("next_week", (week + 1));
		repObj.put("prev_week", (week - 1));

		minDate = DateUtil.getFirstDayOfWeek(year, week);
		minDate = DateUtil.clearTiemConvert(minDate).getTime();

		maxDate = DateUtil.addDay(DateUtil.getLastDayOfWeek(year, week), 1);
		maxDate = DateUtil.clearTiemConvert(maxDate).getTime();

		List<MecSchedule> list = MecSchedule.dao.queryAllByChannelTime(
				mec_channel_id, minDate.getTime(), maxDate.getTime());

		List<JSONObject> res_list = new ArrayList<JSONObject>();
		if (list != null && !list.isEmpty()) {
			String prev_ymd = null;
			JSONObject prev_json = null;

			JSONArray prev_zs = null;// 早上
			JSONArray prev_sw = null;// 上午
			JSONArray prev_xw = null;// 下午
			JSONArray prev_wj = null;// 晚间

			Long prev_date_time = null;

			int day = 1;
			for (int i = 0; i < list.size(); i++) {
				MecSchedule obj = list.get(i);
				Date db_date = new Date(obj.getMec_start_at());
				String ymd = DateUtil.getDateSampleString(db_date);
				// 如果不是同一天的，创建新的list
				if (prev_ymd == null || !prev_ymd.equals(ymd)) {
					prev_json = new JSONObject();
					prev_ymd = ymd;

					prev_zs = new JSONArray();
					prev_sw = new JSONArray();
					prev_xw = new JSONArray();
					prev_wj = new JSONArray();

					prev_date_time = DateUtil.getDate(prev_ymd).getTime();

					prev_json.put("week_day", day++);// 周几
					prev_json.put("ymd", prev_ymd);// 年月日

					prev_json.put("zs", prev_zs);// 早上
					prev_json.put("sw", prev_sw);// 上午
					prev_json.put("xw", prev_xw);// 下午
					prev_json.put("wj", prev_wj);// 晚间
					res_list.add(prev_json);
				}
				Long hms_time = obj.getMec_start_at() - prev_date_time;
				if (hms_time < zs_time) {
					loadSchedule(obj, prev_zs);
				} else if (hms_time < sw_time) {
					loadSchedule(obj, prev_sw);
				} else if (hms_time < xw_time) {
					loadSchedule(obj, prev_xw);
				} else if (hms_time < wj_time) {
					loadSchedule(obj, prev_wj);
				}
			}
		}

		repObj.put("min_date", minDate);		
		repObj.put("max_date", new Date(maxDate.getTime() - 10000));

		setAttr("obj", repObj);

		setAttr("schedule_list", res_list);
		render("/page/mdm/mec_schedule/epg_index.jsp");
	}

	/**
	 * 为页面整理数据，并添入
	 * */
	private void loadSchedule(MecSchedule obj, JSONArray array) {
		JSONObject json = obj.getJSONObj();
		Long program_id = json.getLong("mdm_program_id");
		Long episode_id = json.getLong("mdm_episode_id");
		if (program_id != null) {
			Program p = Program.dao.findById(program_id);
			if (p != null) {
				Episode e = Episode.dao.findById(episode_id);
				String title = ComUtil.getEpisodeAppTitle(e, p);
				json.put("title", title);
			}
		}
		Long start_at = json.getLong("mec_start_at");
		if (start_at != null) {
			json.put("mec_start_at",DateUtil.getTimeSampleString(new Date(start_at)).split(" ")[1]);
		}
		Long end_at = json.getLong("mec_end_at");
		if (end_at != null) {
			json.put("mec_end_at",DateUtil.getTimeSampleString(new Date(end_at)).split(" ")[1]);
		}
		array.add(json);
	}

	// public void epg_index() {
	// //List<MecChannel> channels = MecChannel.dao.queryChannelList();
	// //setAttr("channels",channels);
	// //render("/page/mdm/mec_schedule/epg_index.jsp");
	// }
	public void epg_input() {
		Long channel_id = getParaToLong("mec_channel_id");
		Long mec_schedule_id = getParaToLong("mec_schedule_id");
		if (channel_id != null) {
			JSONObject json = new JSONObject();
			json.put("mec_channel_id", channel_id);
			setAttr("obj", json);
		}
		if (mec_schedule_id != null) {
			MecSchedule ms = MecSchedule.dao.findById(mec_schedule_id);
			JSONObject json = ms.getJSONObj();
			Long start_at = json.getLong("mec_start_at");
			if (start_at != null) {
				json.put("mec_start_at",
						DateUtil.getTimeSampleString(new Date(start_at)));
			}
			Long end_at = json.getLong("mec_end_at");
			if (end_at != null) {
				json.put("mec_end_at",
						DateUtil.getTimeSampleString(new Date(end_at)));
			}
			setAttr("obj", json);
		}
		List<MecChannel> channels = MecChannel.dao.queryChannelList();
		setAttr("channels", channels);
		render("/page/mdm/mec_schedule/epg_input.jsp");
	}

	public void epg_save() throws ParseException {
		// 运营 schedule保存
		MecSchedule obj = getModel(MecSchedule.class, "obj");
		String start_at = getPara("start_at");
		String end_at = getPara("end_at");
		if(StringUtils.isNotBlank(start_at)){
			obj.setMec_start_at(DateUtil.getGenTime(start_at).getTime());
		}else{
			obj.setMec_start_at(null);
		}
		if(StringUtils.isNotBlank(end_at)){
			obj.setMec_end_at(DateUtil.getGenTime(end_at).getTime());
		}else{
			obj.setMec_end_at(null);
		}
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

	/**
	 * feed 管理 页面的单个频道,剧集的 快速设置
	 * @throws
	 */
	public void sign_set() throws ParseException {
		Long id = getParaToLong("id");
		Long mec_channel_id = getParaToLong("channel_id");
		Long episode_id = getParaToLong("episode_id");
		String type = getPara("type");
		
		MecSchedule obj = null;
		if(id!=null){
			obj = MecSchedule.dao.findById(id);
		}
		if(obj == null){
			obj = MecSchedule.dao.queryByChannelAndEpisodeId(mec_channel_id,episode_id);
		}
		if(obj == null){
			obj = new MecSchedule();
		}
		
		obj.setMec_channel_id(mec_channel_id);
		
		Episode episode = Episode.dao.findById(episode_id);
		if(episode!=null){
			obj.setMdm_program_id(episode.getPid());
			Program program = Program.dao.findById(episode.getPid());
			if(program!=null){
				obj.setEpg_name(ComUtil.getEpisodeAppTitle(episode, program));
			}
		}
		obj.setMdm_episode_id(episode_id);
		
		if("start_at".equals(type)){//设置开始时间
			obj.setMec_start_at(System.currentTimeMillis());
		}else{//设置截止时间
			obj.setMec_end_at(System.currentTimeMillis());
		}
		
		if(obj.getId() != null){
			obj.setUpdDef();
			obj.update();
		}else{
			obj.setAddDef();
			obj.save();
		}
		
		//增加一个同步消息
		SynWeiboTask.dao.resetWeiboTask(episode_id);
		renderJson(Cons.JSON_SUC);
	}
	
//	public void epg_list() throws Exception {
//		MecSchedule obj = new MecSchedule();
//		String start_at = getPara("obj.mec_start_at");
//		String end_at = getPara("obj.mec_end_at");
//		Long channel_id = getParaToLong("obj.mec_channel_id");
//		if (start_at != null && start_at != "") {
//			obj.setMec_start_at(DateUtil.getGenTime(start_at).getTime());
//		}
//		if (end_at != null && end_at != "") {
//			obj.setMec_end_at(DateUtil.getGenTime(end_at).getTime());
//		}
//		if (channel_id != null) {
//			obj.setMec_channel_id(channel_id);
//		}
//		int pageNumber = getParaToInt("pageNumber", 1);
//		int pageSize = getParaToInt("pageSize", 1);
//		Page<?> page = MecSchedule.dao.paginate(obj, pageNumber, pageSize);
//		List<MecSchedule> list = (List<MecSchedule>) page.getList();
//		parseScheduleList(list);
//		setAttr("page", page);
//		renderJson();
//	}
//
//	private void parseScheduleList(List<MecSchedule> list)
//			throws ParseException {
//		if (list != null && !list.isEmpty()) {
//			List resList = new LinkedList();
//			for (int i = 0; i < list.size(); i++) {
//				MecSchedule mecSchedule = list.get(i);
//				JSONObject json = mecSchedule.getJSONObj();
//				Long program_id = json.getLong("mdm_program_id");
//				Long episode_id = json.getLong("mdm_episode_id");
//				if (program_id != null) {
//					Program p = Program.dao.findById(program_id);
//					if (p != null) {
//						Episode e = Episode.dao.findById(episode_id);
//						String title = ComUtil.getEpisodeAppTitle(e, p);
//						json.put("title", title);
//					}
//				}
//				Long start_at = json.getLong("mec_start_at");
//				if (start_at != null) {
//					json.put("mec_start_at",
//							DateUtil.getTimeSampleString(new Date(start_at)));
//				}
//				Long end_at = json.getLong("mec_end_at");
//				if (end_at != null) {
//					json.put("mec_end_at",
//							DateUtil.getTimeSampleString(new Date(end_at)));
//				}
//				resList.add(json);
//			}
//			list.clear();
//			list.addAll(resList);
//		}
//	}

}
