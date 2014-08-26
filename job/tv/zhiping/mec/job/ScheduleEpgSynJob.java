package tv.zhiping.mec.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import tv.zhiping.common.Cons;
import tv.zhiping.mec.epg.EpgApi;
import tv.zhiping.mec.epg.model.MecSchedule;
import tv.zhiping.media.model.EpgProgram;

import com.zhiping.epg.thrift.InvalidRequestException;
import com.zhiping.epg.thrift.Program;
import com.zhiping.epg.thrift.TimedOutException;
import com.zhiping.epg.thrift.UnavailableException;

/**
 * 节目单的匹配
 * @author liang
 *
 */
public class ScheduleEpgSynJob implements Job {
	private Logger log = Logger.getLogger(this.getClass());
	private EpgApi api = new EpgApi();
	
    @Override
    public void execute(JobExecutionContext arg0) {
		try {

			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
    }

	private void processSchedule(){
		List<MecSchedule> list = MecSchedule.dao.queryByWaitMath();
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				MecSchedule obj = list.get(i); 
				obj.setThread_state(Cons.THREAD_STATE_DEALING);
				obj.update();
				
				match(obj);
				
				
				obj.setThread_state(Cons.THREAD_STATE_SUC);
				obj.update();
			}
		}
		
		
	}

	//匹配
	private void match(MecSchedule obj) {
		if(Cons.DEF_NULL_NUMBER.equals(obj.getEpg_program_id())){
			obj.setThread_msg("节目id为空");
		}else if(Cons.DEF_NULL_NUMBER.equals(obj.getEpg_episode_id())){
			saveOrUpdProgram(obj.getEpg_program_id());
		}else{
			
			
		}
	}

	/**
	 * 补充剧集信息
	 * @param epg_program_id
	 * @throws TException 
	 * @throws TimedOutException 
	 * @throws UnavailableException 
	 * @throws InvalidRequestException 
	 */
	private void saveOrUpdProgram(String epg_program_id){
		EpgProgram obj = EpgProgram.dao.findById(epg_program_id);
		boolean save = false;
		if(obj == null){
			obj = new EpgProgram();
			
//			Program epgObj = api.get_program_detail(epg_program_id);
			
//			obj.setId(epgObj.getId());
//			obj.setTitle(epgObj.getName());
//			obj.setType(epgObj.getProgram_type_name());
			
			//presenter
			
//			obj.setEpisodes_count(epgObj.getEpisodes_count());
			
			
//			obj.setSummary(epgObj.getDescription());
			

			
			save = true;
		}
		
		if(save){
			obj.setAddDef();
			obj.save();
		}else{
			obj.setUpdDef();
			obj.save();
		}
		
	}
}
