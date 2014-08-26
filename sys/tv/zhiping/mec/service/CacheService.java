package tv.zhiping.mec.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.Cons;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.common.cache.CachePool;
import tv.zhiping.mec.sys.model.EnumValue;
import tv.zhiping.mec.sys.model.SysConfig;

/**
 * 缓存
 * @author 张有良
 * @version 1.0
 * @since 2011-10-27
 */
public class CacheService{
	public static final CacheService service = new CacheService();
	
	public void init() throws Exception{
		initSysConfigDb();//初始化系统的配置文件
		initEnumInfo();//初始化字典表
	}

	public void destroy(){
		
	}
	
	public void initEnumInfo() {
		EnumValue.dao.initCache();
	}
	
	//初始化系统的配置文件
	public void initSysConfigDb(){
		SysConfig.dao.initSysConfigDb();
		setUploadProperties();
	}
	
	/**
	 * Load property file
	 * Example: loadPropertyFile("db_username_pass.txt");
	 * @param file the file in WEB-INF directory
	 */
	public void loadPropertyFile(String file) {
		Properties properties = null;;
		if (StrKit.isBlank(file))
			throw new IllegalArgumentException("Parameter of file can not be blank");
		if (file.contains(".."))
			throw new IllegalArgumentException("Parameter of file can not contains \"..\"");
		
		InputStream inputStream = null;
		String fullFile;	// String fullFile = PathUtil.getWebRootPath() + file;
		if (file.startsWith(File.separator))
			fullFile = PathKit.getWebRootPath() + File.separator + "WEB-INF" + file;
		else
			fullFile = PathKit.getWebRootPath() + File.separator + "WEB-INF" + File.separator + file;
		
		try {
			inputStream = new FileInputStream(new File(fullFile));
			Properties p = new Properties();
			p.load(inputStream);
			properties = p;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Properties file not found: " + fullFile);
		} catch (IOException e) {
			throw new IllegalArgumentException("Properties file can not be loading: " + fullFile);
		}
		finally {
			try {if (inputStream != null) inputStream.close();} catch (IOException e) {e.printStackTrace();}
		}
		if (properties == null)
			throw new RuntimeException("Properties file loading failed: " + fullFile);
		
		initSysConfigFile(properties);
	}

	
	/**
	 * 加载配置文件中的数据
	 * @param properties
	 */
	public void initSysConfigFile(Properties properties){
		Map<String,String> tproperties = CachePool.sysConfigCache;
	    
		if(properties!=null && !properties.isEmpty()){
			Set<Object> keys = properties.keySet();
			
			Iterator ite = keys.iterator();
			while(ite.hasNext()){
				String key = (String)ite.next();
				tproperties.put(key, properties.getProperty(key));
			}
		}
		setUploadProperties();
	}
	
	/**
	 * 设置文件上传的参数
	 */
	private void setUploadProperties() {
		Map<String,String> tproperties = CachePool.sysConfigCache;
		if(Cons.DEVELOP){//如果开发就存到本地
			tproperties.put(ConfigKeys.ST_LOCAL_FOLDER,PathKit.getWebRootPath()+"/upload/");
			tproperties.put(ConfigKeys.ST_WEB_HTTP_URL,"http://localhost:"+tproperties.get(ConfigKeys.PORT)+"/upload/");
		}
		String str = tproperties.get(ConfigKeys.ST_LOCAL_FOLDER);
		if(StringUtils.isNotBlank(str)){
			tproperties.put(ConfigKeys.ST_LOCAL_FOLDER,str.replace("\\","/"));
		}
		
		tproperties.put(ConfigKeys.UPLOAD_TEMP_FOLDER,tproperties.get(ConfigKeys.ST_LOCAL_FOLDER)+Cons.UPLOAD_TEMP_FLAG);
	}
}