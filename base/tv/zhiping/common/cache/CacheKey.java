package tv.zhiping.common.cache;

/**
 * 缓存常量
 * @author liang
 */
public class CacheKey {
	//app的系统常量
	public static final String APP_SYSTEM_CONFIG = "app_system_config";
	
	
	
	//剧集的的场景scene数
	public static final String SCENE_COUNT = "scene_count_";
	//剧集的的场景scene数,失效时间
	public static final int SCENE_COUNT_EXPIRY  = 60;
	
	
	//节目的明星人员
	public static final String PROGRAM_PERSON_JSON = "program_person_json_";
	public static final int PROGRAM_PERSON_EXPIRY = 10;
	
	//节目的明星数据
	public static final String PROGRAM_MUSIC_JSON = "program_music_json_";
	public static final int PROGRAM_MUSIC_EXPIRY = 10;
	
	
	
	//剧集的数据
	public static final String EPISODE_ID = "episode_id_";
	public static final int EPISODE_ID_EXPIRY = 10;
	
	
}
