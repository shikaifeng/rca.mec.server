package tv.zhiping.mec.sys.demo.blog;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.jfinal.BasicModel;

import com.jfinal.plugin.activerecord.Page;

/**
 * Blog model.

将表结构放在此，消除记忆负担
mysql> desc blog;
+---------+--------------+------+-----+---------+----------------+
| Field   | Type         | Null | Key | Default | Extra          |
+---------+--------------+------+-----+---------+----------------+
| id      | int(11)      | NO   | PRI | NULL    | auto_increment |
| title   | varchar(200) | NO   |     | NULL    |                |
| content | mediumtext   | NO   |     | NULL    |                |
+---------+--------------+------+-----+---------+----------------+

数据库字段名建议使用驼峰命名规则，便于与 java 代码保持一致，如字段名： userId
 */
@SuppressWarnings("serial")
public class Blog extends BasicModel<Blog> {
	public static final Blog dao = new Blog();
	
	/**
	 * 分页查询
	 */
	public Page<Blog> paginate(Blog obj,int pageNumber,int pageSize) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("from blog where status=1");
		if(obj!=null){
			if(StringUtils.isNoneBlank(obj.getTitle())){
				sql.append(" and title like ?");
				params.add(LIKE+obj.getTitle()+LIKE);
			}
		}
		
		sql.append("  order by id asc");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),params.toArray());
	}
	
	
	public String getTitle(){
		return this.getStr("title");
	}
}
