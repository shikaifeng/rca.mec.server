package tv.zhiping.redis;

import java.io.Serializable;

import com.jfinal.ext.plugin.redis.JedisKit;



/**
 * @author robin
 * @detail Redis操作类
 * @date   2014-07-24
 * @date   2014-08-13 : 实际操作类是缓存框架提供
 */
public class Redis {
	//开发表示
	public static final boolean DEVELOP = true;
	
	private static final int CACHE_EXPIRY = 3600 * 1000;
	
	/**
	 * @param key
	 * @param value
	 * @detail 键值不存在则新增
	 */
	public static void setnx(String key, Serializable value)
	{
		if(!DEVELOP){
			JedisKit.set(key, value);
		}
	}
	
	public static void setex(String key,Serializable value,int seconds){
		if(!DEVELOP){
			JedisKit.set(key, value,seconds);
		}	
	}
	
	/**
	 * @param key
	 * @return value
	 * @detail 获取Redis缓存数据
	 */
	public static <T extends Serializable> T get(String key)
	{
		if(!DEVELOP){
			return JedisKit.get(key);
		}
		return null;
	}

	public static void del(String key) {
		if(!DEVELOP){
			JedisKit.del(key);
		}
	}

	
	
}
