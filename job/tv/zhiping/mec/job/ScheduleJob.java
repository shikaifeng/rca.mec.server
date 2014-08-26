package tv.zhiping.mec.job;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import tv.zhiping.common.Cons;
import tv.zhiping.mec.epg.EpgApi;
import tv.zhiping.mec.epg.model.MecChannel;
import tv.zhiping.mec.epg.model.MecSchedule;
import tv.zhiping.utils.DateUtil;

import com.zhiping.epg.thrift.InvalidRequestException;
import com.zhiping.epg.thrift.Page;
import com.zhiping.epg.thrift.Schedule;
import com.zhiping.epg.thrift.Schedules;
import com.zhiping.epg.thrift.TimedOutException;
import com.zhiping.epg.thrift.UnavailableException;

public class ScheduleJob implements Job {
	private Logger log = Logger.getLogger(this.getClass());

	/**
	 * select (select b.name from mec_channel b where a.epg_channel_id=b.rid) as '频道名称', 
		(select b.name from mec_channel b where a.mec_channel_id=b.id) as '频道名称', 
		FROM_UNIXTIME(epg_start_at/1000,'%Y-%m-%d %h:%i:%s') as '开始时间', 
		FROM_UNIXTIME(epg_end_at/1000,'%Y-%m-%d %h:%i:%s')  as '截止时间',a.epg_program_Id,a.epg_episode_id,a.epg_name 
		from mec_schedule a order by mec_channel_id,epg_start_at
		
		epg调整策略：
		     如果时间不变，  id不变，program_id,episode_id变化
		     如果时间变化了，id删除
		
		测试用例:
		1：全部增加 ok
		2: 有新增   ok
		3: 中间节目变化了，后面都删除,并且重新增加 ok
	 */
    @Override
    public void execute(JobExecutionContext arg0) {
		try {
			EpgApi api = new EpgApi();
			
			Date day = DateUtil.getGenTime(DateUtil.getDateSampleString(new Date()));
			Long start_time = DateUtil.addDay(day,0).getTime();
			Long end_time = DateUtil.addDay(day,1).getTime();
			
			List<MecChannel> channelList = MecChannel.dao.queryChannelList();//找到所有的频道表
			for(MecChannel mc:channelList){
				excuteChannelSchedule(api, start_time, end_time, mc);
			}		
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
    }

	private void excuteChannelSchedule(EpgApi api, Long start_time,
			Long end_time, MecChannel mc) throws InvalidRequestException,
			UnavailableException, TimedOutException, TException {
		List<Schedule> resList = getChannelAllSchedule(api, start_time,end_time, mc.getRid());
		
		if(resList!=null && !resList.isEmpty()){
			List<MecSchedule> oldList = MecSchedule.dao.queryAllByChannelTime(mc.getId(),start_time,end_time);
			Schedule obj = null;
			int oldSize = 0;
			int newSize = resList.size();
			
			if(oldList!=null && !oldList.isEmpty()){
				oldSize = oldList.size();
			}
			MecSchedule oldObj = null;
			boolean is_comp = true;
			Long last_del_start_time = null;//最后一个的
			for(int i=0;i<newSize;i++){
				obj = resList.get(i);
				oldObj = null;
				if(oldSize > i){
					oldObj = oldList.get(i);
					last_del_start_time = oldObj.getEpg_start_at();
				}
				
				if(is_comp){
					if(schedule_del(oldObj, obj)){//删除
						is_comp = false;
						MecSchedule.dao.delByChannel(mc.getId(), last_del_start_time);		
					}
				}
				
				saveOrUpd(mc, obj);
			}
		}
	}

	private void saveOrUpd(MecChannel mc, Schedule obj) {
		MecSchedule newMs = new MecSchedule();
		newMs.setRid(obj.getId());
		newMs.setEpg_name(obj.getName());
		
		newMs.setEpg_channel_id(String.valueOf(obj.getChannel_id()));
		
		newMs.setEpg_program_id(String.valueOf(obj.getProgram_id()));
		newMs.setEpg_episode_id(String.valueOf(obj.getEpisode_id()));
		
		newMs.setEpg_start_at(obj.getStart_at());
		newMs.setEpg_end_at(obj.getEnd_at());
		newMs.setMec_channel_id(mc.getId());

		
		MecSchedule olderMs = MecSchedule.dao.queryByRid(newMs.getRid());
		if(olderMs!=null){
			if(!olderMs.getEpg_program_id().equals(newMs.getEpg_program_id()) 
					|| !olderMs.getEpg_episode_id().equals(newMs.getEpg_episode_id())){//如果节目id，或剧集id变化了，重新匹配
				newMs.setThread_state(Cons.THREAD_STATE_WAIT);
			}
			
			newMs.setId(olderMs.getId());
			newMs.setUpdDef();
			newMs.update();
		}else{
			newMs.setThread_state(Cons.THREAD_STATE_WAIT);
			newMs.setAddDef();
			newMs.save();
		}
	}
    
    /**
     * 判断是否一致
     * @param mecObj
     * @param epgObj
     * @return
     */
    public boolean schedule_del(MecSchedule mecObj,Schedule epgObj){
    	//id不一样代表出错了
    	if(mecObj != null && epgObj !=null && !mecObj.getRid().equals(epgObj.getId())){
    		return true;
    	}
    	
//    	long old_start_at = 0L;
//    	if(mecObj.getEpg_start_at()!=null){
//    		old_start_at = mecObj.getEpg_start_at();
//    	}
//    	long old_end_at = 0L;
//    	if(mecObj.getEpg_end_at()!=null){
//    		old_end_at = mecObj.getEpg_end_at();
//    	}
//    	
//    	long mec_start_at = epgObj.getStart_at();
//    	long mec_end_at = epgObj.getEnd_at();
//    	
//    	if(old_start_at!=mec_start_at || old_end_at!=mec_end_at){
//    		return true;
//    	}
    	return false;
    }

    /**
     * 获取一个频道内在这个时间段内所有的信息
     * @param api  
     * @param start_time 开始时间
     * @param end_time   截止时间
     * @param channel    频道
     * @return
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws TException
     */
	private List<Schedule> getChannelAllSchedule(EpgApi api, Long start_time,
			Long end_time, String channel)
			throws InvalidRequestException, UnavailableException,
			TimedOutException, TException {
		Integer offset = 0;
		Integer row = Cons.DEF_EPG_PAGE_SIZE;
		
		List<Schedule> resList = new ArrayList<Schedule>();
		while(true){
			Schedules schedules = api.get_schedules(channel,null,start_time,end_time,offset,row);		
			Page page = schedules.getPage();
			List<Schedule> list = schedules.getData();		
			if(list!=null &&!list.isEmpty()){
				resList.addAll(list);
			}
			if(page.getMore_index()<1){
				break;
			}
			offset = page.getMore_index();
		}
		return resList;
	}
    
    public static void main(String[] args) throws ParseException {
		System.out.println(new Date().getTime());
	}

}
