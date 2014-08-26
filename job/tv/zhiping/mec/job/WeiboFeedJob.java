package tv.zhiping.mec.job;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.Cons;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.mdm.model.WeiboFeed;
import tv.zhiping.mdm.model.WeiboFeed.WeiboFeedType;
import tv.zhiping.mec.epg.model.MecSchedule;
import tv.zhiping.mec.feed.model.MecWeiboFeed;
import tv.zhiping.mec.feed.model.SynWeiboTask;
import tv.zhiping.utils.DateUtil;

import com.jfinal.plugin.activerecord.Page;

/**
 * 微博的匹配
 * @author 张有良
 * 需求： 每分钟显示3条，
 * 		  显示时间不一样，按先后顺序排列如 0,20,40
 * 		  如果显示条数变化了，app端显示的条数变化
 *       天猫版：运营工具点击了立即显示，client 立即显示。记录显示状态 或 超过2分钟就显示
*/
public class WeiboFeedJob implements Job {
	private Logger log = Logger.getLogger(this.getClass());
	
	private static volatile boolean running = false;
	
    @Override
    public void execute(JobExecutionContext arg0) {
    	if(running){//如果正在运行
    		return;
    	}
		try {
			running = true;
			processWeiboFeedTask();
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}finally{
			running = false;
		}
    }
    
    private void processWeiboFeedTask(){
    	SynWeiboTask.dao.upd2Wait();
    	
    	while(true){
			Page<SynWeiboTask> page = SynWeiboTask.dao.paginateByWiat(1,Cons.MAX_PAGE_SIZE);
			List<SynWeiboTask> list = page.getList();
			if(list==null || list.isEmpty()){
				break;
			}else{
				for(int i=0;i<list.size();i++){
					SynWeiboTask task = list.get(i);
					try {
						task.setThread_state(Cons.THREAD_STATE_DEALING);
						task.setUpdDef();
						task.update();
						
						MecSchedule epg = MecSchedule.dao.queryByChannelAndEpisodeId(null,task.getEpisode_id());
						matchTask(task,epg);
						
						task.setThread_state(Cons.THREAD_STATE_SUC);
					} catch (Exception e) {
						log.error(e.getMessage(),e);
						task.setThread_state(Cons.THREAD_STATE_FAIL);
					}
//					log.info("weibo_feed_job episode_id="+task.getEpisode_id()+" max_id="+task.getMax_id()+" times="+task.getTimes());
					task.setUpdDef();
					task.update();
				}
			}
		}
	}
    
    /**
     * 匹配一个任务的
     * @param task
     */
	private void matchTask(SynWeiboTask task,MecSchedule epg) {
		try {
			Long max_id = task.getMax_id();
			Long episode_id = task.getEpisode_id();
			
			String start_time_str = null;//延迟时间
			Timestamp prev_max_updated_at = task.getLast_updated_at();
			Timestamp max_updated_at = null;
			
			boolean del_flag = false;
			while(true){
				start_time_str = DateUtil.getTimeSampleString(new Date(System.currentTimeMillis() - CacheFun.getConIntVal(ConfigKeys.WEIBO_FEED_DEALY_MILLISECOND)));//当前时间-延迟时间=正式获取时间
				
				max_updated_at = WeiboFeed.dao.queryMaxUpdatedAtByEpisodeId(episode_id,prev_max_updated_at,start_time_str);
				if(max_updated_at == null){//表示在上一次处理完后没数据变化
					return;
				}
				
				Long now_time = System.currentTimeMillis();//当前时间
				if((now_time - max_updated_at.getTime()) > 1000){//超过1秒了,表示没有在并发执行
					del_flag = true;//发现有变化了，就重新处理
					break;
				}
				
				//发现了时间碰撞，休眠1s
				log.warn("weibo job time crash time episode_id="+episode_id+" time="+now_time);
				Thread.sleep(1000L);
			}
			task.setLast_updated_at(max_updated_at);
			
//			if(max_id == null || Cons.DEF_NULL_NUMBER.equals(max_id) || prev_max_updated_at == null){//首次执行,或改变了数量
//				del_flag = true;
//			}
			
			if(del_flag){//重新处理
				max_id = null;
			}
			List<WeiboFeed> list = WeiboFeed.dao.queryByGtId(task.getEpisode_id(),max_id,start_time_str);
			if(list!=null && !list.isEmpty()){
				List<WeiboFeed> filterProgramList = new ArrayList<WeiboFeed>();
				List<WeiboFeed> filterPersonList = new ArrayList<WeiboFeed>();
				List<WeiboFeed> filterOtherList = new ArrayList<WeiboFeed>();
				
				String programWeiboFeedType = WeiboFeedType.program.toString();
				String personWeiboFeedType = WeiboFeedType.person.toString();
				
				Long prev_minute = null;
				
				List<MecWeiboFeed> res_list = new ArrayList<MecWeiboFeed>();
				
				for(int i=0;i<list.size();i++){
					WeiboFeed obj = list.get(i);
					if(max_id == null || max_id<obj.getId()){
						max_id = obj.getId();
					}
					
					if(epg!=null && StringUtils.isNotBlank(obj.getStart_time_str())){
						Long time = epg.getMec_start_at();
						try {
							if(time!=null){
								time =DateUtil.getGenTime(obj.getStart_time_str()).getTime() - time;
								time = time / 1000;
//								time = Math.abs(time);
								if(time>0){//超过时间太长的，就用原来的。
									obj.setStart_time(time);
								}
							}
						} catch (ParseException e) {
							log.error(e.getMessage(),e);
						}
					}
					
					
					Long minute = obj.getStart_time()/60;
					if(prev_minute == null){
						prev_minute = minute;
					}
					
					if(prev_minute != minute){
						res_list.addAll(filterWeiboFeed(filterProgramList,filterPersonList,filterOtherList,episode_id,prev_minute,del_flag));
						prev_minute = minute;
					}
					
					if(programWeiboFeedType.equalsIgnoreCase(obj.getType())){
						filterProgramList.add(obj);
					}else if(personWeiboFeedType.equalsIgnoreCase(obj.getType())){
						filterPersonList.add(obj);
					}else{
						filterOtherList.add(obj);
					}
				}
				
				if(!filterProgramList.isEmpty() || !filterPersonList.isEmpty() || !filterOtherList.isEmpty()){
					res_list.addAll(filterWeiboFeed(filterProgramList,filterPersonList,filterOtherList,episode_id,prev_minute,del_flag));
				}
				
				if(del_flag){
					MecWeiboFeed.dao.deleteByEpisodeId(task.getEpisode_id());
				}
				saveRes_list(res_list);
				
				task.setMax_id(max_id);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	
	/**
	 * 保存过滤后的数据
	 * @param res_list
	 */
	private void saveRes_list(List<MecWeiboFeed> res_list) {
		for (MecWeiboFeed obj : res_list) {
			obj.setAddDef();
			obj.save();
		}
	}


	/**
	 * 只返回3条记录
	 * @param filterProgramList
	 * @param filterOtherList
	 * @return
	 */
	private List<MecWeiboFeed> filterWeiboFeed(List<WeiboFeed> filterProgramList, List<WeiboFeed> filterPersonList,List<WeiboFeed> filterOtherList,Long episode_id,Long minute,boolean del_flag) {
		List<MecWeiboFeed> res = new ArrayList<MecWeiboFeed>();
		try {
			filterProgramList.addAll(filterPersonList);
			filterProgramList.addAll(filterOtherList);
			
			int size = filterProgramList.size();
			int per_num_second = 0;
			int max_weibo_feed_size = CacheFun.getConIntVal(ConfigKeys.WEIBO_FEED_MAX_SIZE);
			per_num_second = 60 / max_weibo_feed_size;
			if (size > max_weibo_feed_size){
				size = max_weibo_feed_size; 
			}
			
			int index = 0;
			if(!del_flag){
				MecWeiboFeed old_feed = MecWeiboFeed.dao.queryCountByEpisodeMinute(episode_id,minute);
				if(old_feed!=null){
					index = old_feed.getLong("count").intValue();
					int tmp_size = max_weibo_feed_size - index;//剩余能加入的条数
					size = size > tmp_size ? tmp_size : size; 
				}
			}
			
			for(int i=0;i<size;i++){
				WeiboFeed obj = filterProgramList.get(i);
				MecWeiboFeed mec = new MecWeiboFeed();
				parseWeiboFeedJson(obj,mec,index,per_num_second);
				mec.setMinute(minute.intValue());
				res.add(mec);
				index++;
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}finally{
			filterProgramList.clear();
			filterPersonList.clear();
			filterOtherList.clear();
		}
		
		return res;
	}
	
	/**
	 * 转换给app端显示json
	 * @param json
	 */
	public void parseWeiboFeedJson(WeiboFeed feed,MecWeiboFeed mec,int index,int per_num_second) {
		Long show_start_time =  feed.getStart_time();
		if(show_start_time!=null){
			show_start_time = ((show_start_time/60)*60)+(index*per_num_second);
		}
		
		mec.setMdm_id(feed.getId());
		mec.setProgram_id(feed.getProgram_id());
		mec.setEpisode_id(feed.getEpisode_id());
		mec.setSeries(feed.getSeries());
		mec.setStart_time(feed.getStart_time());
		mec.setStart_time_str(feed.getStart_time_str());
		mec.setContent(feed.getContent());
		mec.setImages(feed.getImages());
		mec.setVideos(feed.getVideos());
		
//		mec.setStatus(feed.getStatus());
//		mec.setCreated_at(feed.getCreated_at());
//		mec.setUpdated_at(feed.getUpdated_at());
		
		mec.setSender_name(feed.getSender_name());
		mec.setSender_avatar(feed.getSender_avatar());
		mec.setSender_url(feed.getSender_url());
		mec.setType(feed.getType());
		
		//计算后的显示开始时间
		mec.setShow_start_time(show_start_time);
	}
}
