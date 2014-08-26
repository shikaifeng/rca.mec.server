package tv.zhiping.mec.api.jfinal;

import tv.zhiping.mec.api.feed.ctrl.TmallBoxV1ApiCtrl;
import tv.zhiping.mec.api.person.ctrl.PersonApiCtrl;
import tv.zhiping.mec.api.program.ctrl.ElementApiCtrl;
import tv.zhiping.mec.api.program.ctrl.EpisodeApiCtrl;
import tv.zhiping.mec.api.program.ctrl.SceneApiCtrl;
import tv.zhiping.mec.api.user.ctrl.UserApiCtrl;

import com.jfinal.config.Routes;

public class ApiRoute extends Routes{

	@Override
	public void config() {
		add("api/user",UserApiCtrl.class);//用户相关
		add("api/person",PersonApiCtrl.class);//明星相关
		add("api/episode",EpisodeApiCtrl.class);//个人有关的收听收藏历史记录接口
		add("api/scene",SceneApiCtrl.class);//场景相关
		add("api/element",ElementApiCtrl.class);//元素有关
		add("api/v1", TmallBoxV1ApiCtrl.class);
	}
}
