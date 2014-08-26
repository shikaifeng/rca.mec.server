package tv.zhiping.mec.api.jfinal;

import org.apache.log4j.Logger;

import tv.zhiping.jfinal.BaseCtrl;
import tv.zhiping.mec.api.common.ApiCons;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class ApiBaseCtrl extends BaseCtrl{
	protected Logger log = Logger.getLogger(this.getClass());
	
	public void renderJsonV1(ApiRes res){
		if(ApiCons.STATUS_SUC.equals(res.getStatus())){
			res.setMsg("success");			
		}
		renderJson(JSON.toJSONString(res,SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect));  // Null 直接显示 null，ref进行深度拷贝		
	}
	
	public void renderJsonV1(String jsonStr){
		renderJson(jsonStr);
	}
	
	public String getJsonString(ApiRes res) {
		if(ApiCons.STATUS_SUC.equals(res.getStatus())){
			res.setMsg("success");			
		}
		String result = JSON.toJSONString(res,SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect);
		return result;
	}
}
