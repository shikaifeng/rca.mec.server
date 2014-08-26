package tv.zhiping.mec.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import tv.zhiping.mec.service.CacheService;
import tv.zhiping.mec.sys.model.EnumValue;
import tv.zhiping.mec.sys.model.SysConfig;

import com.jfinal.kit.PathKit;

/**
 * 缓存的同步
 * @author liang
 *
 */
public class CacheJob implements Job {
	private Logger log = Logger.getLogger(this.getClass());
	
	
    @Override
    public void execute(JobExecutionContext arg0) {
		try {
			CacheService.service.initSysConfigDb();
		}catch (Exception e) {
			log.error(e.getMessage(),e);
		}
    }
}
