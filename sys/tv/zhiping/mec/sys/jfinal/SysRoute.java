package tv.zhiping.mec.sys.jfinal;

import tv.zhiping.mec.api.tmallbox.ctrl.LuckyDrawCtrl;
import tv.zhiping.mec.feed.ctrl.BaikeCtrl;
import tv.zhiping.mec.feed.ctrl.BannerCtrl;
import tv.zhiping.mec.feed.ctrl.EpisodeFilePathMatchCtrl;
import tv.zhiping.mec.feed.ctrl.FeedCtrl;
import tv.zhiping.mec.feed.ctrl.FeedXlsCtrl;
import tv.zhiping.mec.feed.ctrl.FfmpegExportCtrl;
import tv.zhiping.mec.feed.ctrl.LuckCtrl;
import tv.zhiping.mec.feed.ctrl.LuckyDrawEpisodeCtrl;
import tv.zhiping.mec.feed.ctrl.MecPersonCtrl;
import tv.zhiping.mec.feed.ctrl.MusicCtrl;
import tv.zhiping.mec.feed.ctrl.QuestionCtrl;
import tv.zhiping.mec.feed.ctrl.SupplyEpisodeCtrl;
import tv.zhiping.mec.mdm.ctrl.EpisodeCtrl;
import tv.zhiping.mec.mdm.ctrl.MecScheduleCtrl;
import tv.zhiping.mec.mdm.ctrl.PersonCtrl;
import tv.zhiping.mec.mdm.ctrl.ProgramCtrl;
import tv.zhiping.mec.mdm.ctrl.WeiboFeedCtrl;
import tv.zhiping.mec.sys.ctrl.AppConfigCtrl;
import tv.zhiping.mec.sys.ctrl.EnumValueCtrl;
import tv.zhiping.mec.sys.ctrl.IndexCtrl;
import tv.zhiping.mec.sys.ctrl.LeftMenuCtrl;
import tv.zhiping.mec.sys.ctrl.LoginCtrl;
import tv.zhiping.mec.sys.ctrl.SysConfigCtrl;
import tv.zhiping.mec.sys.ctrl.UploadCtrl;
import tv.zhiping.mec.sys.ctrl.UserCtrl;
import tv.zhiping.mec.sys.demo.blog.BlogCtrl;
import tv.zhiping.mec.tmallbox.ctrl.TmallBoxFeedCtrl;
import tv.zhiping.mec.tmp.ctrl.TmpCoverCtrl;

import com.jfinal.config.Routes;

public class SysRoute extends Routes{

	@Override
	public void config() {
		add("sys/blog", BlogCtrl.class);
		add("sys/login", LoginCtrl.class);
		add("sys/index", IndexCtrl.class);
		add("sys/base/leftMenu", LeftMenuCtrl.class);
		add("sys/user", UserCtrl.class);
		add("sys/appconfig",AppConfigCtrl.class);
		add("sys/sysconfig",SysConfigCtrl.class);
		add("sys/feedXls",FeedXlsCtrl.class);
		add("sys/tmpCover",TmpCoverCtrl.class);
		add("sys/supplyEpisode",SupplyEpisodeCtrl.class);
		add("sys/episodeFilePathMatch",EpisodeFilePathMatchCtrl.class);
		add("sys/ffmpegExport",FfmpegExportCtrl.class);
		
		add("sys/enumValue",EnumValueCtrl.class);
		
		add("sys/program",ProgramCtrl.class);
		add("sys/episode",EpisodeCtrl.class);
		add("sys/person",PersonCtrl.class);
		add("sys/music",MusicCtrl.class);
		add("sys/feed",FeedCtrl.class);
		add("sys/mec_schedule",MecScheduleCtrl.class);
		
		add("sys/tmall_box_feed",TmallBoxFeedCtrl.class);
		add("sys/question",QuestionCtrl.class);
		add("sys/mec_person",MecPersonCtrl.class);
		add("sys/baike",BaikeCtrl.class);
		add("sys/upload",UploadCtrl.class);
		
		add("sys/luck", LuckCtrl.class);
		add("sys/weibo_feed",WeiboFeedCtrl.class);
		add("sys/lucky_draw_episode",LuckyDrawEpisodeCtrl.class);
		add("sys/banner",BannerCtrl.class);
	}
}
