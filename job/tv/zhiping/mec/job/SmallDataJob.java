package tv.zhiping.mec.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import tv.zhiping.mec.epg.EpgApi;
import tv.zhiping.mec.epg.model.MecChannel;
import tv.zhiping.mec.epg.model.MecEpgProgramType;

import com.zhiping.epg.thrift.Channel;
import com.zhiping.epg.thrift.Channels;
import com.zhiping.epg.thrift.Page;
import com.zhiping.epg.thrift.ProgramType;

/**
 * 频道和节目类型的同步
 * @author 张有良
 */
public class SmallDataJob implements Job {
	private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext arg0) {
		sysChannel();
		sysProgramType();
    }


    /**
     * 同步节目类型
     */
	private void sysProgramType() {
		try {
			EpgApi api = new EpgApi();
			List<ProgramType> list = api.get_program_types();
			if (list != null && !list.isEmpty()) { 
				for (int i = 0; i < list.size(); i++) {
					ProgramType obj = list.get(i);
					MecEpgProgramType mecObj = new MecEpgProgramType();		
					mecObj.setRid(obj.getId());
					mecObj.setName(obj.getName());
					
					MecEpgProgramType oldObj = MecEpgProgramType.dao.queryByRid(mecObj.getRid());
					if(oldObj!=null){
						mecObj.setId(oldObj.getId());
						mecObj.setUpdDef();
						mecObj.update();
					}else{
						mecObj.setAddDef();
						mecObj.save();
					}
				}
			}
		}catch (TException e) {
			log.error(e.getMessage(),e);
		}
	}
	
    /**
     * 同步频道
     */
	private void sysChannel() {
		try {
			EpgApi api = new EpgApi();
			int offset = 0;
			int row = 10;
			while (true) {
				Channels channels = api.get_channels(offset, row);
				Page page = channels.getPage();
				List<Channel> list = channels.getData();
				if (list != null && !list.isEmpty()) { 
					for (int i = 0; i < list.size(); i++) {
						Channel obj = list.get(i);
						MecChannel newMc = new MecChannel();		
						newMc.setRid(obj.getId());
						newMc.setName(obj.getName());
						if(obj.getP_name()!=null){
							newMc.setPinyin(obj.getP_name());
						}
						if(obj.getImage()!=null){
							if(obj.getImage().containsKey("small")){
								if(obj.getImage().get("small").get("url")!=null){
									newMc.setLogo(obj.getImage().get("small").get("url"));
								}
							}
						}
						
						MecChannel olderChannel = MecChannel.dao.queryByRid(newMc.getRid());
						if(olderChannel!=null){
							newMc.setId(olderChannel.getId());
							newMc.setUpdDef();
							newMc.update();
						}else{
							newMc.setAddDef();
							newMc.save();
						}
					}
				}
				
				if (page.getMore_index() < 1) {
					break;
				}
				offset = page.getMore_index();
			}
		}catch (TException e) {
			log.error(e.getMessage(),e);
		}
	}

}
