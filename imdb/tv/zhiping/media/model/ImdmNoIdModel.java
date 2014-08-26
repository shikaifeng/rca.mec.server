package tv.zhiping.media.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import tv.zhiping.common.Cons;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

import freemarker.core._RegexBuiltins.matchesBI;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class ImdmNoIdModel<M extends ImdmNoIdModel> extends Model<M> {
//    private static final TableInfoMapping tableInfoMapping = TableInfoMapping.me();
    protected Logger log = Logger.getLogger(this.getClass());
    
    protected Table table = TableMapping.me().getTable(this.getClass());
    protected String tableName = table.getName();
    
    //分批查询
    protected static final Integer _SQL_ITERATOR_IN_COUNT = 500;
    
    protected final static String LIKE = "%";
    
    /**
	 * 分隔的sql
	 * @param sql
	 * @param ids  
	 * @param params
	 */
	protected List<M> getIteratorSqlIdsParam(StringBuilder src_sql,List src_ids, List<Object> src_params) {
		List res = new ArrayList();
		
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		
		int size = src_ids.size();
		
		int count = size / _SQL_ITERATOR_IN_COUNT;
		if(size % _SQL_ITERATOR_IN_COUNT>0){
			count++;
		}
		
		int start = 0;
		int end = 0;
		
		for(int i=0;i<count;i++){
			start = end;
			if((i+1) == count){
				end = size;
			}else{
				end = (i+1)*_SQL_ITERATOR_IN_COUNT;
			}
			
			params.clear();
			sql.setLength(0);
			params.addAll(src_params);
			sql.append(sql);
			sql.append(" (");
			boolean is_param = false;
			for(int j=start;j<end;j++){
				is_param = true;
				sql.append("?").append(" ,");
				params.add(src_ids.get(j));
			}
			if(is_param){
				sql.setLength(sql.length()-1);
			}
			sql.append(" )");
			res.addAll(find(src_sql.toString().replace("#iterate#",sql.toString()),params.toArray()));
		}
		return res;
	}
	
    /**
	 * 通过多个id组合 in 查询
	 * @param ids  
	 * @param params
	 */
	protected void getSqlIdsParam(StringBuilder sql,String[] ids, List<Object> params) {
		sql.append(" (");
		int size = ids.length;
		boolean is_param = false;
		for(int i=0;i<size;i++){
			if(StringUtils.isNoneBlank(ids[i])){
				is_param = true;
				sql.append("?").append(",");
				params.add(ids[i]);
			}
		}
		
		if(is_param){
			sql.setLength(sql.length()-1);
		}
		sql.append(" )");
	}
	
    
	/**
	 * 通过多个type组合 in 查询
	 * @param sql
	 * @param ids  
	 * @param params
	 */
	protected void getSqlIdsParam(StringBuilder sql,List<String> types, List<Object> params) {
		sql.append(" (");
		int size = types.size();
		boolean is_param = false;
		for(int i=0;i<size;i++){
			is_param = true;
			sql.append("?").append(" ,");
			params.add(types.get(i));
		}
		if(is_param){
			sql.setLength(sql.length()-1);
		}
		sql.append(" )");
	}
	
    public void setDelDef(){
    	setStatus(Cons.STATUS_INVALID);
    	setUpdDef();
    }

    public void setUpdDef(){
    	this.setUpdated_at(new Timestamp(System.currentTimeMillis()));
    }
    
    public void setAddDef(){
    	this.setCreated_at(new Timestamp(System.currentTimeMillis()));
    	this.setUpdated_at(this.getCreated_at());
    	this.setStatus(Cons.STATUS_VALID);
    }
    
	
	
	public Integer getStatus(){
		return super.getInt("status");
	}
	
	public M setStatus(Integer status){
		super.set("status",status);
		return (M) this;
	}
	
	public Timestamp getCreated_at(){
		return super.getTimestamp("created_at");
	}
	
	public M setCreated_at(Timestamp created_at){
		super.set("created_at",created_at);
		return (M) this;
	}
	
	public Timestamp getUpdated_at(){
		return super.getTimestamp("updated_at");
	}
	
	public M setUpdated_at(Timestamp updated_at){
		super.set("updated_at",updated_at);
		return (M) this;
	}
	
	
//	public boolean delSoft(Object id){
//		return set("id",id).set("status", -1).update();
//	}
	

//    public String getTableName() {
//        return tableInfoMapping.getTableInfo(getClass()).getTableName();
//    }
//
//    public String getCacheName() {
//        return getTableName() + "_Cache";
//    }
//
//    public String getPkName() {
//        return tableInfoMapping.getTableInfo(getClass()).getPrimaryKey();
//    }

//    public List<M> findAll() {
//        return find("SELECT * FROM " + getTableName());
//    }




    /**
     * 覆盖,一般用于更新.temp的值会覆盖原有的值
     * 如果是要删除某个值.merge后请重新赋值.
     *
     * @param temp
     * @return 更新日志.
     */
//    public List<String> cover(M temp) {
//        return cover(temp.getAttrs());
//    }

//    /**
//     * 覆盖,一般用于更新.temp的值会覆盖原有的值
//     * 如果是要删除某个值.merge后请重新赋值.
//     *
//     * @param temp
//     * @return 更新日志.
//     */
//    public List<String> cover(Map<String, Object> temp) {
//        List<String> changeLog = new ArrayList<String>();
//        for (String attrName : temp.keySet()) {
//            if (getPkName().equals(attrName)) {
//                continue;
//            }
//            Object value = temp.get(attrName);
//            if ( tableInfoMapping.getTableInfo(getClass()).hasColumnLabel(attrName) && !value.equals(get(attrName))) {
//                set(attrName, value);
//                changeLog.add(attrName + " : " + get(attrName) + " to " + value);
//            }
//        }
//        return changeLog;
//    }

    /**
     * 覆盖,一般用于更新.temp的值会覆盖原有的值
     * 如果是要删除某个值.merge后请重新赋值.
     * 一般处理某一状态值
     * @param temp
     * @return 更新日志.
     */
//    public List<String> coverNotNull(Map<String, Object> temp) {
//        List<String> changeLog = new ArrayList<String>();
//        for (String attrName : temp.keySet()) {
//            if (getPkName().equals(attrName)) {
//                continue;
//            }
//            Object value = temp.get(attrName);
//            if ( value!=null && StringUtils.isNotBlank(value.toString())&& tableInfoMapping.getTableInfo(getClass()).hasColumnLabel(attrName) && !value.equals(get(attrName))) {
//                set(attrName, value);
//                changeLog.add(attrName + " : " + get(attrName) + " to " + value);
//            }
//        }
//        return changeLog;
//
//    }

    /**
     * 将另外的Model的值PUT进去合并起来,
     * PUT和SET有区别.
     *
     * @param map
     */
    public void merge(Map<String,Object> map) {

        for (String s : map.keySet()) {
            Object value = map.get(s);
            if (value != null && !value.equals("")) {
                put(s, value);
            }
        }
    }


    protected Logger getLog() {
        long startTime = Long.parseLong((String) MDC.get("_startTime"));
        MDC.put("costTime", String.valueOf(System.currentTimeMillis() - startTime));
        return log;
    }

    /**
     * CUD 操作重写加上日志系统
     *
     * @return
     */

    @Override
    public boolean save() {
        long startTime = System.currentTimeMillis();
        boolean isSave = super.save();    //To change body of overridden methods use File | Settings | File Templates.
        long costTime = System.currentTimeMillis() - startTime;
        if (isSave) {
            MDC.put("costTime", String.valueOf(costTime));
//            getLog().trace("insert@[" + getTableName() + "]  values:" + toString());
        }
        return isSave;
    }

    @Override
    public boolean update() {
        long startTime = System.currentTimeMillis();
        boolean isUpdate = super.update();    //To change body of overridden methods use File | Settings | File Templates.
        long costTime = System.currentTimeMillis() - startTime;
        if (isUpdate) {
//            CacheKit.remove(getCacheName(), get(getPkName()));
            MDC.put("costTime", String.valueOf(costTime));
//            getLog().trace("update@[" + getTableName() + "]  values:" + toString());
        }
        return isUpdate;
    }

    @Override
    public boolean deleteById(Object id) {
        long startTime = System.currentTimeMillis();
        boolean isDelete = super.deleteById(id);    //To change body of overridden methods use File | Settings | File Templates.
        long costTime = System.currentTimeMillis() - startTime;
        if (isDelete) {
//            CacheKit.remove(getCacheName(), id);
            MDC.put("costTime", String.valueOf(costTime));
//            getLog().trace("delete@[" + getTableName() + "]  values:" + id);
        }
        return isDelete;
    }

    @Override
    public boolean delete() {
        long startTime = System.currentTimeMillis();
        boolean isDelete = super.delete();    //To change body of overridden methods use File | Settings | File Templates.
        long costTime = System.currentTimeMillis() - startTime;
        if (isDelete) {
//            CacheKit.remove(getCacheName(), get(getPkName()));
            MDC.put("costTime", String.valueOf(costTime));
//            getLog().trace("delete@[" + getTableName() + "]  values:" + getAttrs().get(getPkName()));
        }
        return isDelete;
    }

    @Override
    /**
     * xxs过滤
     */
    public M set(String attr, Object value) {
//        attr = Jsoup.clean(attr, Whitelist.simpleText());
        return super.set(attr, value);
    }

//    @Override
//    public Long getLong(String attr) {
//        if (null == get(attr)){
//            return 0L;
//        }
//        return Convert.toLong(get(attr));
//    }

    /**
     * 不过滤
     *
     * @param attr
     * @param value
     * @return
     */
    public M setUnSafe(String attr, Object value) {
        return super.set(attr, value);
    }

    /**
     * 初始化日志记录
     */
    public static void initLogger() {
//        logger = LogManager.getLogger(EventLogInterceptor.EventLogName);
    }
}
