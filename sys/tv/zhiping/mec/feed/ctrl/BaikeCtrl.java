package tv.zhiping.mec.feed.ctrl;

import java.io.File;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mec.feed.model.Baike;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.mec.feed.model.Element.ElementType;

/**
 * 百科feed
 * */
public class BaikeCtrl extends FeedBaseCtrl {
	/**运营后台百科feed保存*/
	public void save_element(){
		File file = getUploadFile();
		
		Element element = getModel(Element.class,"element");
		Baike obj = getModel(Baike.class,"obj");
		Long episode_id = element.getEpisode_id();
		if(element != null){
			
			if(obj!=null){
				obj.setProgram_id(element.getProgram_id());
				setLastEditSysUser(obj);
				
				Baike b = Baike.dao.queryByProgramIdName(obj.getProgram_id(), obj.getTitle());
				if(b!=null){
					obj.setId(b.getId());
				}
				if(obj.getId()!=null){
					obj.update();
				}else{
					obj.save();
				}
				copyBaikeCover(file,obj);
			}
			setElmentSceneId(element, episode_id);
			element.setFid(obj.getId());
			element.setCover(obj.getCover());
			element.setType(ElementType.baike.toString());
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
	/**运营后台百科feed添加*/
	public void input_element() throws Exception{
		Long element_id = getParaToLong("element_id");
		if(element_id==null){//添加百科
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
		}else{//编辑百科
			Element element = Element.dao.findById(element_id);
			if(element!=null){
				Baike obj = Baike.dao.findById(element.getFid());
				if(obj!=null){
					JSONObject json = obj.getJSONObj();
					json.put("show_cover",ComUtil.getStHttpPath(obj.getCover()));
					setAttr("obj",json);
				}
			}
			setAttr("element",element);
		}
		render("/page/feed/baike/input_element.jsp");
	}
}