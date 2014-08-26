
package tv.zhiping.common.cache;

import java.util.Map;

import tv.zhiping.utils.ValidateUtil;

/**
 * 缓存的工具类
 * @author 张有良
 * @version 1.0
 * @since 2011-10-28
 */
public class CacheFun {
	
	/**
	 * 得到系统配置文件的值
	 * @param field
	 * @return
	 */
	public static String getConVal(String field) {
		return CachePool.sysConfigCache.get(field);
	}
	
	public static long getConLongVal(String field) {
		String str = CachePool.sysConfigCache.get(field);
		if(ValidateUtil.isNum(str)){
			return Long.parseLong(str);
		}
		return 0;
	}
	
	public static int getConIntVal(String field) {
		String str = CachePool.sysConfigCache.get(field);
		if(ValidateUtil.isNum(str)){
			return Integer.parseInt(str);
		}
		return 0;
	}
	
	//boolean
	public static boolean getConBooleanVal(String field) {
		return "true".equalsIgnoreCase(CachePool.sysConfigCache.get(field));
	}
	
	public static String getConValToUpper(String field) {
		return CachePool.sysConfigCache.get(field);
	}
	
	
//	得到省一级
//	public static Map<String,String> getProvince(){
//		return CachePool.areaProvicnce;
//	}
//	
//	/**
//	 * 根据区域code得到AreaCode Map
//	 * @param key
//	 * @return
//	 */
//	public static Map<String,String> getSelAreaMap(String superior){
//		if(StringUtils.isNotBlank(superior)){
//			return CachePool.areaSelect.get(superior);
//		}
//		return null;
//	}
	
	/**
	 * 枚举信息表
	 * @param key
	 * @return
	 */
	public static Map<String,String> getEnumMap(String type){
		return CachePool.enumSelect.get(type);
	}
	
	/**
	 * 枚举信息 根据类型得到值
	 * @param key
	 * @return
	 */
	public static String getEnumValue(String type,String code){
		Map m = getEnumMap(type);
		if(m != null && code!=null){
			return (String)m.get(code);
		}
		return null;
	}
	
	/**
	 * 枚举信息 根据类型得到值
	 * @param key
	 * @return
	 */
	public static String getEnumValue(String type,Boolean code){
		Map m = getEnumMap(type);
		if(m != null && code!=null){
			return (String)m.get(String.valueOf(code));
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println();
	}
	
	/**
	 * 枚举信息 根据类型得到值
	 * @param key
	 * @return
	 */
	public static String getEnumValue(String type,Integer code){
		Map m = getEnumMap(type);
		if(m != null && code!=null){
			return (String)m.get(String.valueOf(code));
		}
		return null;
	}
	
	/**
	 * 枚举信息 根据类型得到值
	 * @param key
	 * @return
	 */
	public static String getEnumKeyValueSwitchValue(String type,String value){
		Map m = CachePool.enumKeyValueSwitchSelect.get(type);
		if(m != null && value!=null){
			return (String)m.get(value);
		}
		return null;
	}
}
