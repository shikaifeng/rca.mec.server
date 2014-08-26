package tv.zhiping.mec.mdm.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Element.ElementType;
import tv.zhiping.mec.feed.model.MecWeiboFeed;
import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;

/**
 * 
 * @author 张有良
 */
public class EpisodeCtrl extends SysBaseCtrl {
	
	public void index() {
		Long program_id = getParaToLong("program_id");
		setAttr("program_id",program_id);
		
		if(program_id!=null){
			Program program = Program.dao.findById(program_id);
			setAttr("program",program);
		}
		render("/page/mdm/episode/index.jsp");
	}
	
	public void list() {
		Long program_id = getParaToLong("program_id");
		
		List<Episode> list = Episode.dao.queryByPid(program_id);
		
		Map<String,Long> elment_map = Element.dao.groupMapEpisodeByProgram(program_id);
		Map<Long,Long> weibo_map = MecWeiboFeed.dao.groupMapEpisodeByProgram(program_id);
		
		if(list!=null && !list.isEmpty()){
			List resList = new ArrayList();
			Long count = null;
			for(int i=0;i<list.size();i++){
				Episode obj = list.get(i);
				JSONObject json = obj.getJSONObj();
				json.put("cover",ComUtil.getStHttpPath(json.getString("cover")));
				
				count = elment_map.get(obj.getId()+"#"+ElementType.person.toString());
				count = count==null ? 0 : count;
				json.put("person_count",count);//明星数
				
				count = elment_map.get(obj.getId()+"#"+ElementType.music.toString());
				count = count==null ? 0 : count;
				json.put("music_count",count);//歌曲数
				
				count = Scene.dao.queryCountByEpisodeId(obj.getId());
				count = count==null ? 0 : count;
				json.put("scene_count",count);
				
				count = weibo_map.get(obj.getId());
				count = count==null ? 0 : count;
				json.put("weibo_count",count);
				
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
			setAttr("obj", Episode.dao.findById(id));
		}else{
			Episode obj = new Episode();
			Long program_id = getParaToLong("program_id");
			if(program_id!=null){
				Program program = Program.dao.findById(program_id);
				if(program!=null){
					obj.setPid(program.getId());
					obj.setTitle(program.getTitle());
					obj.setType(program.getType());
					obj.setYear(program.getYear());
				}
			}
			setAttr("obj", obj);
		}
		render("/page/mdm/episode/input.jsp");
	}
	
	public void save() {
		Episode obj = getModel(Episode.class,"obj");
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


