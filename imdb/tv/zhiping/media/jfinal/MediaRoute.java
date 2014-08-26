package tv.zhiping.media.jfinal;


import tv.zhiping.media.imdb.ctrl.ImdbFactMatchCtrl;
import tv.zhiping.media.imdb.ctrl.ImdbFeedCtrl;
import tv.zhiping.media.imdb.ctrl.ImdbMatchCtrl;
import tv.zhiping.media.imdb.ctrl.ImdbPersonMatchCtrl;
import tv.zhiping.media.imdb.ctrl.XlsExportCtrl;

import com.jfinal.config.Routes;

public class MediaRoute extends Routes{

	@Override
	public void config() {
		add("sys/imdbFeed",ImdbFeedCtrl.class);
		add("sys/imdbMatch",ImdbMatchCtrl.class);
		add("sys/xlsExport",XlsExportCtrl.class);
		add("sys/imdbFactMatch",ImdbFactMatchCtrl.class);
		add("sys/imdbPersonMatch",ImdbPersonMatchCtrl.class);
		
//		add("sys/weiboFeedMatch",WeiboFeedMatchCtrl.class);
	}
}
