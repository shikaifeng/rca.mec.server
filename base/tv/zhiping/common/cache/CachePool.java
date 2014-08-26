package tv.zhiping.common.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 缓存池 到时候丢到redis
 * @author 张有良
 * @version 1.0
 * @since 2011-10-28
 */
public class CachePool {
//	public static ThreadLocal<HttpServletRequest> REQUEST_LOCAL = new ThreadLocal();
//	public static ThreadLocal<HttpServletResponse> RESPONSE_LOCAL = new ThreadLocal();
	
	public static volatile Map<String,String> sysConfigCache = new HashMap<String,String>();
	
//	//基于线程同步的 420000---湖北省的AreaCode 实体
//	public static volatile Map <Long,AreaCode> areaCode = new HashMap<Long,AreaCode>();
//	
//	//上级SUPERIOR 到 Map  就是根据市id得到区的下了列表，现在则是下拉列表是一个存了区的map
//	public static volatile Map<String,Map<String,String>> areaSelect = new HashMap<String,Map<String,String>>();
//	
//	//省
//	public static volatile Map<String,String> areaProvicnce = new LinkedHashMap<String,String>();
//	
	//字典信息
	public static volatile Map<String,Map<String,String>> enumSelect = new HashMap<String,Map<String,String>>();
	
	//字典表key，value 呼唤
	public static volatile Map<String,Map<String,String>> enumKeyValueSwitchSelect = new HashMap<String,Map<String,String>>();
	
}
