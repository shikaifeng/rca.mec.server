package tv.zhiping.mec.sys.demo.blog;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.Cons;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.mec.sys.jfinal.SysInterceptor;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * 
 * @author 张有良
 */
public class BlogCtrl extends SysBaseCtrl {
	
	public void index() {
		render("/page/blog/index.jsp");
	}
	
	public void list() {
		Blog obj = getModel(Blog.class,"obj");
		
		int pageNumber = getParaToInt("pageNumber",1);
		int pageSize = getParaToInt("pageSize",1);
		
		setAttr("page", Blog.dao.paginate(obj,pageNumber,pageSize));
		renderJson();
	}
	
	public void input() {
		Long id = getParaToLong(0);
		if(id!=null){
//			Blog obj = Blog.dao.findById(id);
			setAttr("obj", Blog.dao.findById(id));
		}
		
		render("/page/blog/input.jsp");
	}
	
	@Before(BlogSaveValidator.class)
	public void save() {
		Blog obj = getModel(Blog.class,"obj");
		if(obj!=null){
			if(obj.getId()!=null){
				obj.setUpdDef();
				obj.update();
			}else{
				obj.setAddDef();
				obj.save();				
			}
		}
		renderJson(Cons.JSON_SUC);
	}
	
	@Before(BlogSaveValidator.class)
	public void update() {
		getModel(Blog.class).update();
		redirect(CacheFun.getConVal(ConfigKeys.WEB_HTTP_PATH)+"/blog");
	}
	
	public void del() {
		Blog obj = new Blog();
		obj.setId(getParaToLong(0));
		obj.setDelDef();
		obj.update();
		
		renderJson(Cons.JSON_SUC);
	}
}


