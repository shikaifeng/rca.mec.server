package tv.zhiping.mec.feed.ctrl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.PersonProgram;
import tv.zhiping.mec.feed.model.BannerImg;
import tv.zhiping.mec.feed.model.BannerTitle;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;

public class BannerCtrl extends SysBaseCtrl {
	
	public void img_list(){
		int pageNumber = getParaToInt("pageNumber",1);
		int pageSize = getParaToInt("pageSize",10);
		BannerImg obj = getModel(BannerImg.class,"obj");
		Page<BannerImg> page = BannerImg.dao.paginate(pageNumber, pageSize);
		List list = page.getList();
		if(list!=null && !list.isEmpty()){
			List resList = new ArrayList(); 
			for(int i=0;i<list.size();i++){
				obj = (BannerImg) list.get(i);
				JSONObject json = obj.getJSONObj();				
				json.put("path",ComUtil.getStHttpPath(json.getString("path")));				
				resList.add(json);
			}
			list.clear();
			list.addAll(resList);
		}
		setAttr("page", page);
		renderJson();
	}
	
	public void img_index(){
		render("/page/feed/banner/img_index.jsp");
	}
	
	public void save(){
		BannerTitle obj = getModel(BannerTitle.class, "obj");
		if (obj != null) {			
			if (obj.getId() != null) {
				obj.setUpdDef();
				obj.update();
			} else {
				obj.setAddDef();
				obj.save();
			}
		}		
		renderJson(Cons.JSON_SUC);		
	}
	
	public void input(){
		Long banner_id = getParaToLong("banner_id");		
		if(banner_id!=null){
			BannerTitle obj = BannerTitle.dao.findById(banner_id);
			setAttr("obj", obj);
		}
		render("/page/feed/banner/input.jsp");
	}
	
	public void index() {
		render("/page/feed/banner/index.jsp");
	}
	public void list(){
		int pageNumber = getParaToInt("pageNumber",1);
		int pageSize = getParaToInt("pageSize",10);
		Page<BannerTitle> page = BannerTitle.dao.paginate(pageNumber, pageSize);
		setAttr("page", page);
		renderJson();
	}
	
	public void del(){
		BannerTitle obj = new BannerTitle();
		obj.setId(getParaToLong(0));
		obj.setDelDef();
		obj.update();		
		renderJson(Cons.JSON_SUC);
	}
}
