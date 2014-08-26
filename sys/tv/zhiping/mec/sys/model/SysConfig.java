package tv.zhiping.mec.sys.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.Cons;
import tv.zhiping.common.cache.CachePool;
import tv.zhiping.jfinal.BasicModel;
import tv.zhiping.utils.ValidateUtil;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * Config model.
 * @author robin
 *
 */
@SuppressWarnings("serial")
public class SysConfig extends BasicModel<SysConfig> {
	public static final SysConfig dao = new SysConfig();
	
	public SysConfig getConfigByKey(String key)
	{
		String sql = "select * from sys_config where status=1 and title=?";
		return findFirst(sql, new Object[]{key});
	}
	
	public SysConfig getConfigByValue(String value)
	{
		List<Object> params = new ArrayList<Object>();
		params.add(value);
		String sql = "select * from sys_config where value=?";
		return findFirst(sql, params.toArray());
	}
	
	
	/**
	 * 通过key 得到数字
	 * @param key
	 * @return
	 */
	public int getIntByKey(String key){
		SysConfig obj = getConfigByKey(key);
		String str = null;
		if(obj!=null){
			str = obj.getValue();
		}
		if(StringUtils.isNoneBlank(str) && ValidateUtil.isNum(str)){
			return Integer.parseInt(str);
		}
		return 0;
	}
	/**
	 * 分页查询
	 */
	public Page<SysConfig> paginate(SysConfig obj,int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();		
		StringBuilder sql = new StringBuilder("from "+tableName+" where status=1");
		if(obj!=null){
			if(StringUtils.isNoneBlank(obj.getTitle())){
				sql.append(" and title like ?");
				params.add(LIKE+obj.getTitle()+LIKE);
			}
		}		
		sql.append(" order by id asc");
		return paginate(pageNumber, pageSize, "select * ", sql.toString(),params.toArray());
	}
	
	
	public List<SysConfig> queryAll(){
		String sql = "select * from "+tableName+" where status=?";
		return find(sql, new Object[]{Cons.STATUS_VALID});
	}
	
	//初始化系统的配置文件
	public void initSysConfigDb(){
		Map<String,String> tproperties = CachePool.sysConfigCache;
	    
	    List<SysConfig> list = queryAll();
	    SysConfig obj = null;
	    if(list!=null && !list.isEmpty()){
		    for(int i = 0; i<list.size(); i++){
		    	obj = list.get(i);
		    	tproperties.put(obj.getTitle(),obj.getValue());
		    }   
	    }
	    
//	    if(StringUtils.isNotBlank(webAbsPath)){
//	    	webAbsPath = webAbsPath.replace("\\","/");
//		    tproperties.put(ConfigKeys.WEB_ABS_PATH,webAbsPath);
//	    }
	}
	
	
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public SysConfig setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getValue(){
		return this.getStr("value");
	}
	
	public SysConfig setValue(java.lang.String value){
		super.set("value",value);
		return this;
	}
	public java.lang.String getDescription(){
		return this.getStr("description");
	}
	
	public SysConfig setDescription(java.lang.String description){
		super.set("description",description);
		return this;
	}
	
}