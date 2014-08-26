package tv.zhiping.mec.feed.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mec.feed.ctrl.FeedBaseCtrl;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.mec.feed.model.Element.ElementType;
import tv.zhiping.mec.feed.model.Music;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;

/**
 * 
 * @author 张有良
 */
public class MusicCtrl extends FeedBaseCtrl {
	
	public void save_element(){
		File file = getUploadFile();
		
		Element element = getModel(Element.class,"element");
		Music obj = getModel(Music.class,"obj");
		
		
		if(element != null){
			Long episode_id = element.getEpisode_id();
			if(obj!=null){
				obj.setTag(element.getTag());
				setLastEditSysUser(obj);
				
				String soure_url = obj.getSource_url();
				Music m = Music.dao.queryBySourceUrl(soure_url);
				if(m!=null){
					obj.setId(m.getId());
				}
				
				setMdmMusicRelation(obj.getSource_url(), obj);				
			
				if (obj.getId() == null) {
					obj.save();
				} else {
					obj.update();
				}
				copyMusicCover(file,obj);
			}
			setElmentSceneId(element, episode_id);
			element.setFid(obj.getId());
			element.setCover(obj.getCover());
			element.setType(ElementType.music.toString());
			if(element.getStart_time()!=null && element.getEnd_time()!=null){
				element.setDuration(element.getEnd_time() - element.getStart_time());
			}
			if(element.getId() == null){
				element.setAddDef();
				element.save();
			}else{
				element.setAddDef();
				element.update();
			}
		}
		renderJson(Cons.JSON_SUC);
	}
	
	public void input_element() throws Exception{
		Long element_id = getParaToLong("element_id");
		if(element_id==null){//添加音乐
			Element element = new Element();
			Long episode_id = getParaToLong("episode_id");
			element.setProgram_id(getParaToLong("program_id"));
			element.setEpisode_id(episode_id);
			List<Scene> scene_list = Scene.dao.queryByEpisode(episode_id);
			if(scene_list!=null && !scene_list.isEmpty()){
				if(scene_list.size() == 1){
					element.setScene_id(scene_list.get(0).getId());					
				}else{
					throw new Exception("场景有多个"+scene_list.size());
				}
			}
			setAttr("element",element);
		}else{//编辑音乐
			Element element = Element.dao.findById(element_id);
			if(element!=null){
				Music obj = Music.dao.findById(element.getFid());
				if(obj!=null){
					JSONObject json = obj.getJSONObj();
					json.put("show_cover",ComUtil.getStHttpPath(obj.getCover()));
					setAttr("obj",json);
				}
			}
			setAttr("element",element);
		}
		render("/page/feed/music/input_element.jsp");
	}
	
	public void index_program_episode() {
		Long program_id = getParaToLong("program_id");
		if(program_id!=null){
			setAttr("program_id",program_id);
		}
		
		Long episode_id = getParaToLong("episode_id");
		if(episode_id!=null){
			setAttr("episode_id",episode_id);
		}
		
		render("/page/mdm/music/index_program_episode.jsp");
	}
	
	/**
	 * 根据节目id或剧集id获取
	 */
	public void list_program_episode() {
		Long program_id = getParaToLong("program_id");
		Long episode_id = getParaToLong("episode_id");
		
		List list = new ArrayList();
		
		List<Element> elements = Element.dao.queryByProgramIdEpisodeIdType(program_id,episode_id,ElementType.music.toString());
		
		Map<Long,String> distinctMap = new HashMap();
		for(int i=0;i<elements.size();i++){
			Element obj = elements.get(i);
			JSONObject json = new JSONObject();
			
			if(!distinctMap.containsKey(obj.getFid())){
				distinctMap.put(obj.getFid(),"");
				Music music = Music.dao.findById(obj.getFid());
				if(music!=null){
					music.parseMusic2Json(json);
					json.put("type_title",obj.getTag());
					list.add(json);
				}				
			}
			
		}
		 
		Page  page = new Page(list,1,1,1,1);
		setAttr("page",page);
		renderJson();
	}
	
	public void input() {
		Long id = getParaToLong(0);
		if(id!=null){
			setAttr("obj", Episode.dao.findById(id));
		}
		render("/page/mdm/music/input.jsp");
	}
	
	public void save() {
		Music obj = getModel(Music.class,"obj");
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


