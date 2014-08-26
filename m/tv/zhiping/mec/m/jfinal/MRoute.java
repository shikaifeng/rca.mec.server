package tv.zhiping.mec.m.jfinal;


import tv.zhiping.mec.api.person.ctrl.AboutMCtrl;
import tv.zhiping.mec.api.person.ctrl.PersonMCtrl;
import tv.zhiping.mec.api.tmallbox.ctrl.LuckyDrawCtrl;
import tv.zhiping.mec.api.tmallbox.ctrl.TmallBoxCtrl;

import com.jfinal.config.Routes;

public class MRoute extends Routes{

	@Override
	public void config() {
		//手机端
		add("m/person", PersonMCtrl.class);
		add("m/about",AboutMCtrl.class); 
		add("m/tmallbox", TmallBoxCtrl.class);
		add("m/lucky_draw",LuckyDrawCtrl.class);
	}
}
