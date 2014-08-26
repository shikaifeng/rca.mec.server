package tv.zhiping.mec.feed.ctrl;

import java.io.File;
import java.util.List;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Element.ElementType;
import tv.zhiping.mec.feed.model.MecPerson;
import tv.zhiping.mec.feed.model.Scene;

import com.alibaba.fastjson.JSONObject;

/**
 * rca明星管理
 * @author 张有良
 */
public class MecPersonCtrl extends FeedBaseCtrl {
	/**
	 * 根据element 修改element和person
	 * @throws Exception 
	 */
	public void input_element() throws Exception {
		Long element_id = getParaToLong(0);
		if(element_id!=null){
			Element element = Element.dao.findById(element_id);
			if(element!=null){
				MecPerson obj = MecPerson.dao.findById(element.getFid());
				if(obj!=null){
					JSONObject json = obj.getJSONObj();
				
					json.put("show_avatar",ComUtil.getStHttpPath(json.getString("avatar")));
					setAttr("obj",json);
				}
			}
			setAttr("element",element);
		}else{
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
		}
		
		render("/page/feed/mec_person/input_element.jsp");
	}
	
	/**
	 * 修改element和person
	 */
	public void save_element() {
		File file = getUploadFile();
		
		Element element = getModel(Element.class,"element");
		MecPerson obj = getModel(MecPerson.class,"obj");
		
//		String msg = null;
//		if((msg = validate_input_element(element,obj))!=null){
//			JSONObject json = new JSONObject();
//			json.put("msg",msg);
//			json.put("status",ApiCons.STATUS_ERROR);
//			res = json.toJSONString();
//		}else{
		if(element != null){
			Long episode_id = element.getEpisode_id();
			if(obj!=null){
				MecPerson person = MecPerson.dao.queryByProgramIdName(element.getProgram_id(),obj.getName(),null);
				if(person!=null){
					obj.setId(person.getId());
				}
				
				obj.setProgram_id(element.getProgram_id());
				setLastEditSysUser(obj);
				if(obj.getId()!=null){
					obj.update();
				}else{
					obj.save();				
				}
				copyMecPersonAvatar(file,obj);
			}
			
			setElmentSceneId(element, episode_id);
			
			element.setTitle(obj.getName());
			element.setFid(obj.getId());
			element.setCover(obj.getAvatar());
			element.setType(ElementType.mec_person.toString());
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
//		}
		renderJson(Cons.JSON_SUC);
	}
	
//	/**
//	 * 输入校验
//	 * @param element
//	 * @param obj
//	 * @return
//	 */
//	public String validate_input_element(Element element,MecPerson obj){
//		StringBuilder str = new StringBuilder();
//		if(element!=null && obj!=null){
//			MecPerson person = MecPerson.dao.queryByProgramIdName(element.getProgram_id(),obj.getName(),obj.getId());
//			if(person!=null){
//				str.append("同一个节目下已存在同名人物");
//			}
//			
////			if(obj.getId() != null){//表示已经有的了,判断其他剧集是否存在
////				List<Element> list = Element.dao.queryDistinctEpisodeIdByFidType(element.getEpisode_id(),obj.getId(),ElementType.mec_person.toString());
////				if(list!=null && list.size()>0){
////					str.append("同一个节目下已存在同名明星");	
////				}
////			}
//		}
//		if(str.length()>0){
//			return str.toString();
//		}else{
//			return null;
//		}
//	}
}
