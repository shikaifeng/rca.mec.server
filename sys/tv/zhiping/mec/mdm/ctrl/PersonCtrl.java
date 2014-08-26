package tv.zhiping.mec.mdm.ctrl;

import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Person;
import tv.zhiping.mdm.model.PersonProgram;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Element.ElementType;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;

/**
 * 媒资库明星管理
 * @author 张有良
 */
public class PersonCtrl extends SysBaseCtrl {
	
	
	public void index() {
		Long program_id = getParaToLong("program_id");
		render("/page/mdm/person/index.jsp");
	}

	/**
	 * 根据节目id或剧集id获取
	 */
	public void list() {
		Person obj = getModel(Person.class,"obj");
		
		int pageNumber = getParaToInt("pageNumber",1);
		int pageSize = getParaToInt("pageSize",1);
		
		Page page = Person.dao.paginate(obj,pageNumber,pageSize);
		List<Person> list = page.getList();
		
		if(list!=null && !list.isEmpty()){
			List resList = new ArrayList(); 
			for(int i=0;i<list.size();i++){
				obj = list.get(i);
				JSONObject json = obj.getJSONObj();
				
				json.put("avatar",ComUtil.getStHttpPath(json.getString("avatar")));
				json.put("program_count",PersonProgram.dao.queryCountByPersonId(obj.getId()).getLong("count"));//参演节目数
				
				resList.add(json);
			}
			list.clear();
			list.addAll(resList);
		}
		setAttr("page",page);
		renderJson();
	}
	
	
	/**
	 * 根据节目或剧集的关系页面
	 */
	public void index_program_episode() {
		Long program_id = getParaToLong("program_id");
		if(program_id!=null){
			setAttr("program_id",program_id);
			
			Program program = Program.dao.findById(program_id);
			setAttr("program",program); 
		}
		
		Long episode_id = getParaToLong("episode_id");
		if(episode_id!=null){
			setAttr("episode_id",episode_id);
			
			Episode episode = Episode.dao.findById(episode_id);
			setAttr("episode",episode);
		}
		
		render("/page/mdm/person/index_program_episode.jsp");
	}
	
	/**
	 * 根据节目id或剧集id获取
	 */
	public void list_program_episode() {
		Long program_id = getParaToLong("program_id");
		Long episode_id = getParaToLong("episode_id");
		
		
		List<Person> list = null;
		if(episode_id!=null){
			List<Element> elements = Element.dao.queryDistinctFidAllByEpisode(episode_id,ElementType.person.toString());
			List<Long> fids = new ArrayList<Long>();
			for(int i=0;i<elements.size();i++){
				fids.add(elements.get(i).getFid());
			}
			list = Person.dao.queryPersonByIds(program_id,fids);
		}else if(program_id != null){
			list = Person.dao.queryPersonByPeogramId(program_id);
		}
		
		if(list!=null && !list.isEmpty()){
			List resList = new ArrayList();
			for(int i=0;i<list.size();i++){
				Person obj = list.get(i);
				JSONObject json = obj.getJSONObj();
				json.put("avatar",ComUtil.getStHttpPath(obj.getAvatar()));
				resList.add(json);				
			}
			
			list.clear();
			list.addAll(resList);
		}
		
		Page  page = new Page(list,1,1,1,1);
		setAttr("page",page);
		renderJson();
	}
	
	public void input() {
		Long id = getParaToLong(0);
		if(id!=null){
			Person obj = Person.dao.findById(id);
			if(obj!=null){
				obj.setAvatar(ComUtil.getStHttpPath(obj.getAvatar()));
				setAttr("obj",obj);
			}
		}
		render("/page/mdm/person/input.jsp");
	}
	
	public void save() {
		Person obj = getModel(Person.class,"obj");
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
	
	
	public void update() {
//		getModel(Blog.class).update();
//		redirect(CacheFun.getConVal(ConfigKeys.WEB_HTTP_PATH)+"/blog");
	}
	
	public void del() {
//		Blog obj = getAutoModel(Blog.class);
//		obj.setDelDef();
//		obj.update();
		
		renderJson(Cons.JSON_SUC);
	}
}


