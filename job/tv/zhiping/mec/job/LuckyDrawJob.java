package tv.zhiping.mec.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import tv.zhiping.mec.luck.model.LuckyDrawEvent;
import tv.zhiping.mec.service.ArithLuckDrawService;

/**
 * 中奖顺序计算
 * @author liang
 *
 */
public class LuckyDrawJob implements Job {
	private Logger log = Logger.getLogger(this.getClass());
	private static volatile boolean running = false;
	
    @Override
    public void execute(JobExecutionContext arg0) {
    	if(running){//如果正在运行
    		return;
    	}

		try {
			running = true;
			//先计算是否要到抽奖时间了
			List<LuckyDrawEvent> lucky_list = ArithLuckDrawService.service.getLastStartLuckyDraw();
			
			if(lucky_list!=null && !lucky_list.isEmpty()){
				for(int i=0;i<lucky_list.size();i++){
					LuckyDrawEvent event = lucky_list.get(i);
					
					ArithLuckDrawService.service.processLuckyDraw(event);
				}
			}
		}catch (Exception e) {
			log.error(e.getMessage(),e);
		}
    }
    
}
