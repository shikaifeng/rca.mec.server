package tv.zhiping.mec.api.person.ctrl;


import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.Person;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mec.api.common.ApiCons;
import tv.zhiping.mec.api.jfinal.ApiBaseCtrl;
import tv.zhiping.mec.api.jfinal.ApiRes;
import tv.zhiping.mec.api.jfinal.CommIdApiValidator;
import tv.zhiping.utils.DateUtil;
import tv.zhiping.redis.Redis;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;

/**
 * 个人用户相关接口
 * */
public class PersonApiCtrl extends ApiBaseCtrl {
//	/**
//	 * 明星详情接口:根据明星id返回明星详情
//	 * 测试：http://localhost:8080/api/person/detail?id=1
//	 * @author 樊亚容
//	 * 2014-5-8
//	 */
//	@Before(CommIdApiValidator.class)
//	public void detail() {	
//		Long id = getParaToLong("id");
//		Person obj = Person.dao.findById(id);
//		ApiRes res = new ApiRes();
//		if(obj != null){
//			res.setStatus(ApiCons.STATUS_SUC);
//			JSONObject data = new JSONObject();	
//			data.put("id", id);
//			data.put("name", obj.getName());
//			data.put("avatar", ComUtil.getStHttpPath(obj.getAvatar()));
//			data.put("description", obj.getDescription());			
//			res.setData(data);		
//		}
//		super.renderJsonV1(res);		
//	}
	
	private Redis redis = new Redis();
	
	/**
	 * 明星详情接口:根据明星id返回明星详情
	 * 测试：http://localhost:8080/api/person/show?_ucid=1&id=1
	 * @author 樊亚容
	 * 2014-5-16
	 */
	@Before(CommIdApiValidator.class)
	public void show(){
		Long id = getParaToLong("id");
		String key = "person_id_" + id.toString();
		String value = redis.get(key);
		if (value != null) {
			super.renderJsonV1(value);
		}
		else {
			Person obj = Person.dao.findById(id);
			ApiRes res = new ApiRes();
			res.setStatus(ApiCons.STATUS_SUC);
			JSONObject data = null;
			if(obj != null){//如果为null，没有返回值
				data = new JSONObject();
				data.put("id", id);
				data.put("name", obj.getName());
				data.put("nickname", obj.getNickname());
				data.put("avatar", ComUtil.getStHttpPath(obj.getView_avatar()));
				data.put("birthday",DateUtil.getDateSampleString(obj.getBirthday()));
				data.put("born_place", obj.getBorn_place());
				data.put("constellation", obj.getConstellation());
				data.put("description", obj.getDescription());	
				List<Program> list = Program.dao.querryByPersonId(id);
				List<JSONObject> program_list = null;
				if(list!=null && !list.isEmpty()){
					program_list = new ArrayList<JSONObject>();
					for(Program program:list){
						JSONObject programVo = new JSONObject();
						programVo.put("id", program.getId());
						programVo.put("title", program.getTitle());
						programVo.put("cover", ComUtil.getStHttpPath(program.getView_cover()));
						programVo.put("summary", program.getSummary());
						program_list.add(programVo);
					}
				}
				data.put("program_list", program_list);				
			}else{
				log.warn("data errror personId:"+id);
			}
			res.setData(data);
			value = super.getJsonString(res);
			redis.setnx(key, value);
			super.renderJsonV1(value);	
		}
	}

}
