package tv.zhiping.mec.app.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Page;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-05-14
 */
@SuppressWarnings("serial")
public class AppConfig extends BasicModel<AppConfig> {

	public static final AppConfig dao = new AppConfig();
	/**
	 * 遍历数据库，返回数据库中所有系统参数配置有效数据
	 */
	public HashMap<String, String> iteratorDatabase(){	
		
		HashMap<String, String> result = new HashMap<String, String>();
		List<Object> params = new ArrayList<Object>();
		params.add(Cons.STATUS_VALID);
		String sql = "select * from app_config where status= ?";
		List<AppConfig> result1 = find(sql, params.toArray());
		for(AppConfig a : result1){
			result.put(a.getTitle(), a.getValue());		
		}	
		return result;	
	}
	
	/**
	 * 分页查询
	 */
	public Page<AppConfig> paginate(AppConfig obj,int pageNumber,int pageSize) {
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
	
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public AppConfig setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
	public java.lang.String getValue(){
		return this.getStr("value");
	}
	
	public AppConfig setValue(java.lang.String value){
		super.set("value",value);
		return this;
	}
	public java.lang.String getDescription(){
		return this.getStr("description");
	}
	
	public AppConfig setDescription(java.lang.String description){
		super.set("description",description);
		return this;
	}
}