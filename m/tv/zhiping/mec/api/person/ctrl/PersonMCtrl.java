package tv.zhiping.mec.api.person.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.zhiping.common.util.ComUtil;
import tv.zhiping.jfinal.BaseCtrl;
import tv.zhiping.mdm.model.Person;
import tv.zhiping.mdm.model.Program;

/**
 * 明星相关接口：手机端
 * */
public class PersonMCtrl extends BaseCtrl{
	
	/**
	 * 明星详情接口:根据明星id返回明星对象
	 * 测试：http://localhost:8080/m/person/71463
	 * @author 樊亚容
	 * 2014-6-5
	 */
	public void index() {
		Long id = getParaToLong(0);
		Person obj = Person.dao.findById(id);
	
		if(obj != null){//如果为null，没有返回值
			obj.setAvatar(ComUtil.getStHttpPath(obj.getAvatar()));
			setAttr("obj",obj);	
			StringBuilder title_str = new StringBuilder();
			List<Program> list = Program.dao.querryByPersonId(id);
			if(list!=null && !list.isEmpty()){
				Map<String,String> distinctMap = new HashMap<String,String>();
				String title = null;
				for(Program program:list){
					title = "《"+program.getTitle()+"》 ";
					if(!distinctMap.containsKey(title)){
						distinctMap.put(title,"");
						title_str.append(title);
					}
//					program.setCover(ComUtil.getStHttpPath(program.getView_cover()));
				}
//				setAttr("program_list",list);
				if(title_str.length()>0){
					setAttr("program_title",title_str);
				}
			}
		}else{
			log.warn("data errror personId:"+id);
		}		
		render("/page/m/person/index.jsp");
	}
}
