package tv.zhiping.common;


/**
 * 说明:
 * @author 张有良
 * @version 1.0
 * @since 2011-10-27
 */
public class Cons {
	//外部媒资库的标识
	public static final Boolean MEDIA_DEV = false;
	
	//是否开发标识
	public static final Boolean DEVELOP = false;
	
	//正在开发测试,每次要清除掉
	public static final Boolean DEVELOPING = false;
		
	//配置的标识
	public static final String CONFIG_SUFFIX = "";//_acr  _product_test _product
	
	/** 验证码 **/
	public static final String RTX ="rtx";
	
	public static final Integer MAX_RESULT_APP_PER_PAGE = 20;
	
	public static final Integer JSON_SUC_INT = 0;
	public static final String JSON_SUC = "{\"status\":"+JSON_SUC_INT+"}";
	
	public static final Integer ONE = 1;
	
	public static final Integer YES = 1;
	public static final Integer NO = 0;
	
	public static final Boolean TRUE = true;
	public static final Boolean FALSE = false;
	
	//状态数据:1有效 -1效果
//	public static final Boolean STATUS_VALID = TRUE;
//	public static final Boolean STATUS_INVALID = FALSE;
	
	public static final Integer STATUS_VALID = 1;
	public static final Integer STATUS_INVALID = 0;
	
	
	//机器id
	public static final String _UCID ="_ucid";
	//用户id
	public static final String _UID ="_uid";
	
	public static final String  TV_TAOBAO_MARK= "RcaMec";

	//分页的默认条数
	public static final Integer DEF_PAGE_SIZE = 20;
	//最大条数，防止溢出
	public static final Integer MAX_PAGE_SIZE = 2000;
	
	//epg接口调用的分页
	public static final Integer DEF_EPG_PAGE_SIZE = 200;
	
	public static final Long DEF_NULL_NUMBER = 0L;
	
	public static final Integer DEF_NULL_INT = 0;
	
	public static final String DEF_NULL_BIRTHDAY = "0000-00-00";
	
	public static final Integer THREAD_STATE_WAIT = -1; //待处理
	public static final Integer THREAD_STATE_DEALING = 0; //处理ing
	public static final Integer THREAD_STATE_SUC = 1; //处理成功
	public static final Integer THREAD_STATE_FAIL = 2; //处理失败
	
	public static final Long VIRTUAL_SCENE_ID = 100000000L; //虚拟场景id

	//文件上传临时目录
	public static final String UPLOAD_TEMP_FLAG = "temp/";
//	public static final String UPLOAD_NAME = "upload";
	
	public static final String DB_NAME_MEDIA = "media";
	public static final String DB_NAME_MDM = "mdm";
	public static final String DB_NAME_RCA_MEC_SERVER = "rca_mec_server";

	public static final String CHARACTER = "UTF-8";
	
	public final static Integer SYS_LEVEL = 1;
	public final static Integer PROGRAM_LEVEL = 2;
	public final static Integer EPISODE_LEVEL = 3;
	public final static Integer FEED_LEVEL = 4;
}