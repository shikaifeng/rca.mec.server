package tv.zhiping.mec.api.user.ctrl;

import java.util.HashMap;

import tv.zhiping.common.cache.CacheKey;
import tv.zhiping.mec.api.common.ApiCons;
import tv.zhiping.mec.api.jfinal.ApiBaseCtrl;
import tv.zhiping.mec.api.jfinal.ApiRes;
import tv.zhiping.mec.api.jfinal.ApiValidator;
import tv.zhiping.mec.app.model.AppConfig;
import tv.zhiping.mec.user.model.User;
import tv.zhiping.redis.Redis;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;

/*
 * 个人用户相关接口
 */
public class UserApiCtrl extends ApiBaseCtrl{	
	private Redis redis = new Redis();
	/**
	 * 设备注册接口：登记手机udid等信息，返回ucid值(即系统生成的id)
	 * 测试：http://localhost:8080/api/user/register?udid=33047&os=ios&version=7.0&brand=apple
	 * @author 樊亚容
	 * 2014-5-14
	 */
	public void register() {
		User obj = getAutoModel(User.class);	
		obj.doPhone2Register(obj);//注册	
		Long id = obj.queryByUdid(obj.getUdid()).getId();
		String ucid = id.toString();
		
		JSONObject data = new JSONObject();		
		data.put("ucid", ucid);
		
		ApiRes res = new ApiRes();
		res.setStatus(ApiCons.STATUS_SUC);
		res.setData(data);
		super.renderJsonV1(res);	
	}	
	/**
	 * 获取系统参数配置接口：返回数据库中app_config所有有效数据
	 * 测试：http://localhost:8080/api/user/config?_ucid=1
	 * @author 樊亚容
	 * 2014-5-14
	 */
	@Before(ApiValidator.class)
	public void config(){
		String key = CacheKey.APP_SYSTEM_CONFIG;
		String value = redis.get(key);
		if (value != null) {
			super.renderJsonV1(value);
		}
		else {
			HashMap<String, String> result = AppConfig.dao.iteratorDatabase();
			ApiRes res = new ApiRes();
			res.setStatus(ApiCons.STATUS_SUC);
			res.setData(result);
			value = super.getJsonString(res);
			redis.setnx(key, value);
			super.renderJsonV1(value);
		}
	}
}