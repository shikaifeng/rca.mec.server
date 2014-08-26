package tv.zhiping.redis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;


/**
 * @author robin
 * @detail Redis操作类
 * @date   2014-07-24
 */
public class RedisBak {
	
	private Properties prop = new Properties();
	private Jedis jedis;
	private JedisPool jedisPool;
	private ShardedJedis shardedJedis;
	private ShardedJedisPool shardedJedisPool;
	private String host;
	private int port;
	
	public RedisBak()
	{
		try {
			initialParams();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		initialPool();
		initialShardedPool();
		shardedJedis = shardedJedisPool.getResource();
		jedis = jedisPool.getResource();
	}
	
	/**
	 * 初始化配置参数
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void initialParams() throws FileNotFoundException, IOException
	{
		InputStream in = RedisBak.class.getClassLoader().getResourceAsStream("config.properties");
		prop.load(in);
		host = prop.getProperty("redisHost").trim();
		port = Integer.parseInt(prop.getProperty("redisPort").trim());
	}
	
	/**
	 * 初始化非切片池
	 */
	private void initialPool()
	{
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMinIdle(1);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(10001);
		config.setTestOnBorrow(false);
		jedisPool = new JedisPool(config, host, port);
	}
	
	/**
	 * 初始化切片池
	 */
	private void initialShardedPool()
	{
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMinIdle(1);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(10001);
		config.setTestOnBorrow(false);
		// Slave链接
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo(host, port, "master"));
		// 构造池
		shardedJedisPool = new ShardedJedisPool(config, shards);
	}
	
	/**
	 * @param key
	 * @param value
	 * @detail 键值不存在则新增
	 */
	public void setnx(String key, String value)
	{
		shardedJedis.setnx(key, value);
	}
	
	/**
	 * @param key
	 * @param value
	 * @detail 键值数组不存在则新增
	 */
	public void setnx(byte[] key, byte[] value)
	{
		shardedJedis.setnx(key, value);
	}
	
	/**
	 * @param key
	 * @param seconds
	 * @param value
	 * @detail 设置键值和有效期
	 */
	public void setex(String key, int seconds, String value)
	{
		shardedJedis.setex(key, seconds, value);
	}
	
	/**
	 * @param key
	 * @param seconds
	 * @param value
	 * @detail 设置键值数组和有效期
	 */
	public void setex(byte[] key, int seconds, byte[] value)
	{
		shardedJedis.setex(key, seconds, value);
	}
	
	/**
	 * @param key
	 * @return value
	 * @detail 获取Redis缓存数据
	 */
	public String get(String key)
	{
		return shardedJedis.get(key);
	}
	
	/**
	 * @param key
	 * @return value
	 * @detail 获取Redis缓存数据
	 */
	public byte[] get(byte[] key)
	{
		return shardedJedis.get(key);
	}
	
	/**
	 * 删除某个key
	 */
	public void delKey(String key)
	{
		jedis.del(key);
	}
	
	/**
	 * 清空缓存中的所有数据
	 */
	public void clear()
	{
		jedis.flushDB();
	}
	
	public void show()
	{
		KeyOperate();
		StringOperate();
		ListOperate();
		SetOperate();
		SortedSetOperate();
		HashOperate();
		jedisPool.returnResource(jedis);
		shardedJedisPool.returnResource(shardedJedis);
	}
	
	private void KeyOperate()
	{
		//
	}
	
	private void StringOperate()
	{
		//
	}
	
	private void ListOperate()
	{
		//
	}
	
	private void SetOperate()
	{
		//
	}
	
	private void SortedSetOperate()
	{
		//
	}
	
	private void HashOperate()
	{
		//
	}
	
}
