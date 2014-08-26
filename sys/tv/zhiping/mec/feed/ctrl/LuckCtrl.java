package tv.zhiping.mec.feed.ctrl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.Cons;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mec.luck.model.LuckyDrawEpisode;
import tv.zhiping.mec.luck.model.LuckyDrawEvent;
import tv.zhiping.mec.luck.model.LuckyDrawHistory;
import tv.zhiping.mec.luck.model.LuckyDrawOption;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.utils.DateUtil;
import tv.zhiping.utils.FileUtil;
import tv.zhiping.utils.XlsUtil;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;

public class LuckCtrl extends SysBaseCtrl {
	
	public void luck_manage(){
		Long lucky_id = getParaToLong("event_id");
		LuckyDrawEvent lde = LuckyDrawEvent.dao.findById(lucky_id);
		if(lde!=null){
			JSONObject obj = lde.getJSONObj();
			setAttr("obj", obj);
			//
			List<LuckyDrawOption> list = LuckyDrawOption.dao.queryByLuckyId(lucky_id,null);
			if(list !=null && !list.isEmpty()){
				List<LuckyDrawOption> opt_list = new ArrayList<LuckyDrawOption>();
				List<LuckyDrawOption> no_opt_list = new ArrayList<LuckyDrawOption>();				
				for (LuckyDrawOption ldo : list) {
					if(Cons.YES.equals(ldo.getIsluck())){
						opt_list.add(ldo);
					}else{
						no_opt_list.add(ldo);
					}
				}				
				setAttr("no_opt_list", no_opt_list);
				setAttr("opt_list", opt_list);
			}
			//已关联的节目
			List<LuckyDrawEpisode> episodes = LuckyDrawEpisode.dao.queryByLuckId(lucky_id);
			List<JSONObject> episode_list = new LinkedList<JSONObject>();
			if(episodes!=null){
				for(LuckyDrawEpisode cell:episodes){
					Program program = Program.dao.findById(cell.getProgram_id());
					Episode episode = Episode.dao.findById(cell.getEpisode_id());
					JSONObject json = cell.getJSONObj();
					json.put("title", ComUtil.getEpisodeAppTitle(episode, program));
					episode_list.add(json);
				}
				setAttr("episode_list", episode_list);
			}			
//			List json_list = new LinkedList();
//			List<Episode> list = Episode.dao.queryByPid(program_id);
//			if(list!=null && !list.isEmpty()){
//				
//				Map<Long,Long> check_map = getCheckMap(id);
//				Program program = Program.dao.findById(program_id);
//				for(int i=0;i<list.size();i++){
//					Episode episode = list.get(i);
//					
//					JSONObject json = episode.getJSONObj();
//					json.put("title",ComUtil.getEpisodeAppTitle(episode, program));
//					if(check_map.containsKey(episode.getId())){
//						json.put("checked",Cons.YES);
//					}
//					json_list.add(json);			
//				}
//				Page<JSONObject> page = new Page<JSONObject>(json_list, 0, json_list.size(), 0, json_list.size());
//				setAttr("page", page);
			
		}				
		render("/page/feed/luck/luck_manage.jsp");	
	}
	
	
	/**
	 * 获奖人员导出
	 * @throws IOException
	 */
	public void export_awards() throws IOException{
		Long lucky_id = getParaToLong("lucky_id");
		List<Object[]> datas = new ArrayList<Object[]>();
		if(lucky_id!=null){
			List<LuckyDrawHistory> obj_list = LuckyDrawHistory.dao.queryByLuckyId(lucky_id);
			for(LuckyDrawHistory cell:obj_list){
				String[] srr = new String[4];
				srr[0] = cell.getId().toString();
				srr[1] = DateUtil.getTimeSampleString(cell.getCreated_at());
				srr[2] = cell.getUdid();
				if(cell.getOption_id()!=null){
					LuckyDrawOption option = LuckyDrawOption.dao.findById(cell.getOption_id());
					srr[3] = option.getTitle();
				}
				datas.add(srr);
			}
		}
		
		OutputStream os = null;
		String path = CacheFun.getConVal(ConfigKeys.UPLOAD_TEMP_FOLDER)+"/"+"lucky_information.xls";
		try{
			FileUtil.createParentFilePath(new File(path));
			os = new FileOutputStream(path);
			
			String[] heads = {"id","中奖时间","用户udid","奖品名称"};
			XlsUtil.downXlsData(heads,datas,os);
		}finally{
			if(null != os){
				os.flush();
				os.close();
				os = null;
			}
		}		
		renderFile(new File(path));
	}
	
	public void check_awards(){
		Long lucky_id = getParaToLong("event_id");
		setAttr("lucky_id", lucky_id);
		render("/page/feed/luck/check_awards.jsp");
	}
	
	/**
	 * 奖品配置页面
	 */
	public void config_awards(){
		Long event_id = getParaToLong("event_id");
		setAttr("event_id", event_id);
		List<LuckyDrawOption> list = LuckyDrawOption.dao.queryByLuckyId(event_id,null);
		if(list !=null && !list.isEmpty()){
			List<LuckyDrawOption> opt_list = new ArrayList<LuckyDrawOption>();
			List<LuckyDrawOption> no_opt_list = new ArrayList<LuckyDrawOption>();
			
			for (LuckyDrawOption obj : list) {
				if(Cons.YES.equals(obj.getIsluck())){
					opt_list.add(obj);
				}else{
					no_opt_list.add(obj);
				}
			}
			
			setAttr("no_opt_list", no_opt_list);
			setAttr("opt_list", opt_list);
		}
		
		render("/page/feed/luck/config_awards.jsp");
	}
	
	/**
	 * 保存奖品
	 */
	public void awards_list(){
		Long lucky_id = getParaToLong("lucky_id");
		setAttr("lucky_id", lucky_id);
		int pageNumber = getParaToInt("pageNumber",1);
		int pageSize = getParaToInt("pageSize",10);		
		if(lucky_id!=null){	
			Page page = LuckyDrawHistory.dao.paginate(lucky_id, pageNumber, pageSize);
			List<LuckyDrawHistory> list = page.getList();			
			if(list!=null&&!list.isEmpty()){
				List reslist = new LinkedList();
				for(int i=0;i<list.size();i++){
					LuckyDrawHistory obj = list.get(i);
					JSONObject json = obj.getJSONObj();
					json.put("created_at", DateUtil.getTimeSampleString(obj.getCreated_at()));
					if(obj.getOption_id()!=null){
						LuckyDrawOption opt = LuckyDrawOption.dao.findById(obj.getOption_id());
						json.put("option_title", opt.getTitle());
					}
					reslist.add(json);
				}
				list.clear();
				list.addAll(reslist);
			}
			setAttr("page",page);
		}
		renderJson();
	}

	public void save_awards(){
		Long lucky_id = getParaToLong("lucky_id");
		if(lucky_id!=null){
			LuckyDrawOption.dao.setStatusInvalidByLuckyId(lucky_id);
			String[] titles  = getParaValues("optTitle");
			if(titles!=null){
				String[] ids  = getParaValues("optId");
				String[] positions = getParaValues("position");
				String[] urls = getParaValues("url");
				String[] win_rates  = getParaValues("winRate");
				String[] prize_counts  = getParaValues("prizeCount");
				String[] surplus_counts = getParaValues("surplusCount");
				int size = titles.length;
				for(int i=0;i<size;i++){
					LuckyDrawOption obj = new LuckyDrawOption();
					obj.setLucky_id(lucky_id);
					obj.setPosition(new Integer(positions[i]));
					obj.setUrl(urls[i]);
					obj.setTitle(titles[i]);
					obj.setWin_rate(win_rates[i]);
					if(StringUtils.isNotBlank(prize_counts[i])){
						obj.setPrize_count(Integer.parseInt(prize_counts[i]));
					}
					if(StringUtils.isNotBlank(surplus_counts[i])){
						obj.setSurplus_prize_count(Integer.parseInt(surplus_counts[i]));
					}
					obj.setStatus(Cons.STATUS_VALID);
					obj.setIsluck(Cons.YES);
					if(StringUtils.isNotBlank(ids[i])){
						obj.setId(Long.parseLong(ids[i]));
						obj.setUpdDef();
						obj.update();						
					}else{
						obj.setAddDef();
						obj.save();
					}
				}
			}
			
			titles  = getParaValues("no_opt_title");
			if(titles!=null){
				String[] ids  = getParaValues("no_opt_id");
				String[] positions = getParaValues("no_position");
				
				int size = titles.length;
				for(int i=0;i<size;i++){
					LuckyDrawOption obj = new LuckyDrawOption();
					obj.setLucky_id(lucky_id);
					obj.setPosition(new Integer(positions[i]));
					obj.setTitle(titles[i]);
					obj.setStatus(Cons.STATUS_VALID);
					obj.setIsluck(Cons.NO);
					
					if(StringUtils.isNotBlank(ids[i])){
						obj.setId(Long.parseLong(ids[i]));
						obj.setUpdDef();
						obj.update();						
					}else{
						obj.setAddDef();
						obj.save();
					}
				}
			}
			
			
		}
		renderJson(Cons.JSON_SUC);
	}
	
	/**
	 * 剧集编辑提交
	 * @throws ParseException
	 */
	public void relate_episode_save(){
		Long lucky_id = getParaToLong("id");
		Long program_id = getParaToLong("program_id");
		String[] checks = getParaValues("checks");
		if(lucky_id!=null){
			Map<Long,Long> check_map = getCheckMap(lucky_id);
			
			LuckyDrawEpisode obj = new LuckyDrawEpisode();
			
			obj.setLucky_id(lucky_id);
			obj.setProgram_id(program_id);
			if(checks!=null){
				for(int i=0;i<checks.length;i++){
					Long episode_id = Long.parseLong(checks[i]);
					obj.setEpisode_id(episode_id);
					Long id = check_map.remove(episode_id);
					obj.setId(id);
					if(id==null){
						obj.setAddDef();
						obj.save();
					}else{
						obj.setUpdDef();
						obj.update();
					}
				}
			}				
			
			if(!check_map.isEmpty()){
				Iterator<Long> ite = check_map.values().iterator();
				while(ite.hasNext()){
					obj.setId(ite.next());
					obj.setDelDef();
					obj.update();
				}
			}
		}
		renderJson(Cons.JSON_SUC);
	}
	
	/**
	 * 关联剧集页面
	 */
	public void relate_episode_index(){
		Long id = getParaToLong("id");
		if(id!=null){
			LuckyDrawEvent obj = LuckyDrawEvent.dao.findById(id);
			setAttr("obj", obj);
		}
		Long program_id = getParaToLong("program_id");
		Program program = Program.dao.findById(program_id);
		setAttr("program", program);
		
		render("/page/feed/luck/relate_episode.jsp");
	}
	
	/**
	 * 关联剧集的列表
	 */
	public void relate_episode_list(){
		Long id = getParaToLong("id");
		Long program_id = getParaToLong("program_id");
		
		
		List json_list = new ArrayList();
		List<Episode> list = Episode.dao.queryByPid(program_id);
		if(list!=null && !list.isEmpty()){
			
			Map<Long,Long> check_map = getCheckMap(id);
			Program program = Program.dao.findById(program_id);
			for(int i=0;i<list.size();i++){
				Episode episode = list.get(i);
				
				JSONObject json = episode.getJSONObj();
				json.put("title",ComUtil.getEpisodeAppTitle(episode, program));
				if(check_map.containsKey(episode.getId())){
					json.put("checked",Cons.YES);
				}
				json_list.add(json);				
			}
			Page<JSONObject> page = new Page<JSONObject>(json_list, 0, json_list.size(), 0, json_list.size());
			setAttr("page", page);
		}
		
		
		renderJson();
	}

	/**
	 * 已经
	 * @param program_id
	 * @return
	 */
	public Map<Long,Long> getCheckMap(Long lucky_id){
		Map<Long,Long> check_map = new HashMap<Long,Long>();
		List<LuckyDrawEpisode> relate_episode_list = LuckyDrawEpisode.dao.queryByLuckId(lucky_id);
		if(relate_episode_list!=null && !relate_episode_list.isEmpty()){
			for(int i=0;i<relate_episode_list.size();i++){
				LuckyDrawEpisode obj = relate_episode_list.get(i);
				check_map.put(obj.getEpisode_id(),obj.getId());
			}
		}
		return check_map;
	}
	
	
	public  void save(){
		LuckyDrawEvent obj = getModel(LuckyDrawEvent.class, "obj");
		if (obj != null) {			
			if (obj.getId() != null) {
				obj.setUpdDef();
				obj.update();
			} else {
				obj.setSeq(0L);
				obj.setWin_seq(0L);
				obj.setAddDef();
				obj.save();
			}
		}		
		renderJson(Cons.JSON_SUC);
	}

	public void input(){
		Long event_id = getParaToLong("event_id");
		if(event_id!=null){
			LuckyDrawEvent obj = LuckyDrawEvent.dao.findById(event_id);
			setAttr("obj", obj);
		}
		render("/page/feed//luck/input.jsp");
	}
	
	public void index() {
		render("/page/feed/luck/index.jsp");
	}
	
	public void list() {
		List<LuckyDrawEvent> list = LuckyDrawEvent.dao.queryAllEvent();
		List<JSONObject> eventList = new LinkedList<JSONObject>();		
		for(LuckyDrawEvent luck:list){
			JSONObject json = luck.getJSONObj();
			if(luck.getStart_time()!=null){
				json.put("start_time", luck.getStart_time().toString());
			}
			if(luck.getEnd_time()!=null){
				json.put("end_time", luck.getEnd_time().toString());
			}
			eventList.add(json);		
		}		
		Page<JSONObject> page = new Page<JSONObject>(eventList, 0, list.size(), 0, list.size());
		setAttr("page", page);
		renderJson();
	}
	
	/**
	 * 删除活动
	 */
	public void del() {
		LuckyDrawEvent obj = new LuckyDrawEvent();
		obj.setId(getParaToLong(0));
		obj.setDelDef();
		obj.update();
		
		renderJson(Cons.JSON_SUC);
	}
}
