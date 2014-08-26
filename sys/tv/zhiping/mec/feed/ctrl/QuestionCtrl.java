package tv.zhiping.mec.feed.ctrl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mec.api.common.ApiCons;
import tv.zhiping.mec.epg.model.MecSchedule;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Question;
import tv.zhiping.mec.feed.model.QuestionOption;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;

/**
 * 互动问答
 * @author 张有良
 */
public class QuestionCtrl extends SysBaseCtrl {
	
	public void index() {
		render("/page/feed/question/index.jsp");
	}

//	public void list() {
//		Program obj = getModel(Program.class,"obj");
//		
//		int pageNumber = getParaToInt("pageNumber",1);
//		int pageSize = getParaToInt("pageSize",1);
//		
//		Page page = Program.dao.paginate(obj,pageNumber,pageSize);
//		List<Program> list = page.getList();
//		
//		parseProgramList(list);
//		setAttr("page",page);
//		renderJson();
//	}
	
	public void input() {
		Long id = getParaToLong(0);
		Question obj = null;
		if(id!=null){
			obj = Question.dao.findById(id);
			
			List<QuestionOption> opt_list = QuestionOption.dao.queryByQuestionId(id);
			setAttr("opt_list",opt_list);
			if(obj!=null && obj.getAnswer_id()!=null && opt_list != null && !opt_list.isEmpty()){
				int index = 0;
				for (QuestionOption opt : opt_list) {
					if(opt.getId().equals(obj.getAnswer_id())){
						break;
					}
					index ++;
				}
				setAttr("answer_index",index);
			}
		}else{
			Long program_id = getParaToLong("program_id");
			Long episode_id = getParaToLong("episode_id");
			
			obj = new Question();
			obj.setProgram_id(program_id);
			obj.setEpisode_id(episode_id);
		}
		
		JSONObject json = obj.getJSONObj();
		
//		json.put("start_time",ComUtil.secondFormate(obj.getStart_time()));
//		json.put("end_time",ComUtil.secondFormate(obj.getEnd_time()));
//		json.put("public_time",ComUtil.secondFormate(obj.getPublic_time()));
//		json.put("deadline",ComUtil.secondFormate(obj.getDeadline()));
		setAttr("obj",json);
		render("/page/feed/question/input.jsp");
	}
	
	public void save() {
		Question question = getModel(Question.class,"obj");
		Integer answer_index = getParaToInt("answer_index");
		if(question!=null){
			String[] ids  = getParaValues("optId");
			String[] titles  = getParaValues("optTitle");
			
			List<QuestionOption> opts = new ArrayList<QuestionOption>();
			if(titles!=null){
				int size = titles.length;
				QuestionOption obj = null;
				for(int i=0;i<size;i++){
					if(StringUtils.isNotBlank(titles[i])){//标题不能为空
						obj = new QuestionOption();
						if(StringUtils.isNotBlank(ids[i])){
							obj.setId(new Long(ids[i]));
						}
						obj.setTitle(titles[i]);
						
						opts.add(obj);
					}
				}
			}
			setLastEditSysUser(question);
			Question.dao.saveQueAndOption(question,opts,answer_index);
		}
		renderJson(Cons.JSON_SUC);
	}
	
	/**
	 * 设置元素的开始时间
	 */
	public void set_start_time() {
		String res = Cons.JSON_SUC;
		
		Long id = getParaToLong("id");
		Long schedule_id = getParaToLong("schedule_id");
		
		MecSchedule schedule = MecSchedule.dao.findById(schedule_id);
		Question question = Question.dao.findById(id);
		if(schedule!=null && question != null){
			Long now = System.currentTimeMillis();
			Long start_time = now - schedule.getMec_start_at();
			start_time = start_time/1000;
			
			if(start_time > -1){
				question.setStart_time(start_time);
				
				question.setUpdDef();
				question.update();
				
				Question prev_question = Question.dao.queryByLtStartTimeMaxTime(question.getEpisode_id(),start_time);
				if(prev_question!=null && prev_question.getEnd_time() == null){
					prev_question.setEnd_time(start_time);
					prev_question.setUpdDef();
					prev_question.update();
				}
			}else{
				JSONObject json = new JSONObject();
				json.put("status",ApiCons.STATUS_ERROR);
				json.put("msg","EPG时间设置过前，设置的时间为负数");
				res = json.toJSONString();
			}
		}
		renderJson(res);
	}
	
	/**
	 * 设置元素的开始时间
	 */
	public void set_time() {
		String res = Cons.JSON_SUC;
		Long id = getParaToLong("id");
		Long schedule_id = getParaToLong("schedule_id");
		String field = getPara("field");
		
		MecSchedule schedule = MecSchedule.dao.findById(schedule_id);
		Question question = Question.dao.findById(id);
		if(schedule!=null && question != null){
			Long now = System.currentTimeMillis();
			Long start_time = now - schedule.getMec_start_at();
			start_time = start_time/1000;
			if(start_time > -1){
				question.set(field,start_time);
				
				question.setUpdDef();
				question.update();
			}else{
				JSONObject json = new JSONObject();
				json.put("status",ApiCons.STATUS_ERROR);
				json.put("msg","EPG时间设置过前，设置的时间为负数");
				res = json.toJSONString();
			}
		}
		renderJson(res);
	}
	
	public void update() {
//		getModel(Blog.class).update();
//		redirect(CacheFun.getConVal(ConfigKeys.WEB_HTTP_PATH)+"/blog");
	}
	
	public void del() {
		Question obj = new Question();
		obj.setId(getParaToLong(0));
		obj.setDelDef();
		obj.update();
		
		renderJson(Cons.JSON_SUC);
	}
}


