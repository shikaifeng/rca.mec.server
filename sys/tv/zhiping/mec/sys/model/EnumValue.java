package tv.zhiping.mec.sys.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Page;

import tv.zhiping.common.cache.CachePool;
import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-07-05
 */
@SuppressWarnings("serial")
public class EnumValue extends BasicModel<EnumValue> {
	public static final EnumValue dao = new EnumValue();

	/**
	 * 分页查询
	 */
	public Page<EnumValue> paginate(EnumValue obj,int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();		
		StringBuilder sql = new StringBuilder("from "+tableName+" where status=1");
		if(obj!=null){
			if(StringUtils.isNoneBlank(obj.getType())){
				sql.append(" and type=?");
				params.add(obj.getType());
			}
		}		
		sql.append(" order by code asc");
		return paginate(pageNumber, pageSize, "select * ", sql.toString(),params.toArray());
	}
	
	public List<EnumValue> queryAll(){
		String sql = "select * from "+tableName;
		return find(sql, new Object[]{});
	}
	
	public void initCache() {
		List<EnumValue> list = queryAll();
		CachePool.enumSelect.clear();
		CachePool.enumKeyValueSwitchSelect.clear();
		if(list!=null && !list.isEmpty()){
			int listSize = list.size();
			EnumValue bean = null;
			for(int i=0;i<listSize;i++){
				bean = (EnumValue)list.get(i);
				
				Map<String,String> selectMap = (Map)CachePool.enumSelect.get(bean.getType());
				Map<String,String> keyValueSwitchSelectMap = (Map)CachePool.enumKeyValueSwitchSelect.get(bean.getType());
				if(selectMap==null){
					selectMap = new LinkedHashMap<String,String>();
					keyValueSwitchSelectMap = new LinkedHashMap<String,String>();
					CachePool.enumSelect.put(bean.getType(),selectMap);
					CachePool.enumKeyValueSwitchSelect.put(bean.getType(),keyValueSwitchSelectMap);
				}
				selectMap.put(bean.getCode(),bean.getName());
				keyValueSwitchSelectMap.put(bean.getName(),bean.getCode());
			}
			list.clear();
		}
	}
	
	public java.lang.String getCode(){
		return this.getStr("code");
	}
	
	public EnumValue setCode(java.lang.String code){
		if(StringUtils.isNotBlank(code)){
			super.set("code",code);
		}
		return this;
	}
	public java.lang.String getName(){
		return this.getStr("name");
	}
	
	public EnumValue setName(java.lang.String name){
		if(StringUtils.isNotBlank(name)){
			super.set("name",name);
		}
		return this;
	}
	public java.lang.String getType(){
		return this.getStr("type");
	}
	
	public EnumValue setType(java.lang.String type){
		if(StringUtils.isNotBlank(type)){
			super.set("type",type);
		}
		return this;
	}
	public java.lang.String getNote(){
		return this.getStr("note");
	}
	
	public EnumValue setNote(java.lang.String note){
		if(StringUtils.isNotBlank(note)){
			super.set("note",note);
		}
		return this;
	}
}