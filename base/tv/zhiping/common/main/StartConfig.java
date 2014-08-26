package tv.zhiping.common.main;


import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.Cons;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.jfinal.CommonCtrl;
import tv.zhiping.mdm.model.Album;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Person;
import tv.zhiping.mdm.model.PersonProgram;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mdm.model.Song;
import tv.zhiping.mdm.model.WeiboFeed;
import tv.zhiping.mec.api.jfinal.ApiInterceptor;
import tv.zhiping.mec.api.jfinal.ApiRoute;
import tv.zhiping.mec.app.model.AppConfig;
import tv.zhiping.mec.epg.model.MecChannel;
import tv.zhiping.mec.epg.model.MecEpgProgramType;
import tv.zhiping.mec.epg.model.MecSchedule;
import tv.zhiping.mec.feed.model.Baike;
import tv.zhiping.mec.feed.model.BaikeProperty;
import tv.zhiping.mec.feed.model.BannerImg;
import tv.zhiping.mec.feed.model.BannerTitle;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.EpisodeMdmEpg;
import tv.zhiping.mec.feed.model.ImdbFact;
import tv.zhiping.mec.feed.model.ImdbFactName;
import tv.zhiping.mec.feed.model.ImdbFactTitle;
import tv.zhiping.mec.feed.model.MecPerson;
import tv.zhiping.mec.feed.model.MecWeiboFeed;
import tv.zhiping.mec.feed.model.Music;
import tv.zhiping.mec.feed.model.ProgramMdmEpg;
import tv.zhiping.mec.feed.model.Question;
import tv.zhiping.mec.feed.model.QuestionOption;
import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.mec.feed.model.SynWeiboTask;
import tv.zhiping.mec.luck.model.LuckyDrawBefore;
import tv.zhiping.mec.luck.model.LuckyDrawEpisode;
import tv.zhiping.mec.luck.model.LuckyDrawEvent;
import tv.zhiping.mec.luck.model.LuckyDrawHistory;
import tv.zhiping.mec.luck.model.LuckyDrawOption;
import tv.zhiping.mec.m.jfinal.MRoute;
import tv.zhiping.mec.service.CacheService;
import tv.zhiping.mec.sys.demo.blog.Blog;
import tv.zhiping.mec.sys.jfinal.SysInterceptor;
import tv.zhiping.mec.sys.jfinal.SysRoute;
import tv.zhiping.mec.sys.model.EnumValue;
import tv.zhiping.mec.sys.model.SysConfig;
import tv.zhiping.mec.sys.model.SysUser;
import tv.zhiping.mec.user.model.User;
import tv.zhiping.mec.user.model.UserEpisode;
import tv.zhiping.mec.user.model.UserQuestion;
import tv.zhiping.mec.util.model.DownHistory;
import tv.zhiping.mec.util.model.FfmpegHistory;
import tv.zhiping.media.jfinal.MediaRoute;
import tv.zhiping.media.model.ImdbEpisode;
import tv.zhiping.media.model.ImdbEvent;
import tv.zhiping.media.model.ImdbImage;
import tv.zhiping.media.model.ImdbLog;
import tv.zhiping.media.model.ImdbMergeSound;
import tv.zhiping.media.model.ImdbPerson;
import tv.zhiping.media.model.ImdbPersonPrincipalt;
import tv.zhiping.media.model.ImdbPersonVideo;
import tv.zhiping.media.model.ImdbPlot;
import tv.zhiping.media.model.ImdbProduct;
import tv.zhiping.media.model.ImdbScene;
import tv.zhiping.media.model.ImdbSoundAlbum;
import tv.zhiping.media.model.ImdbSoundAlbumVideo;
import tv.zhiping.media.model.ImdbSoundItem;
import tv.zhiping.media.model.ImdbSoundItemVideo;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.ext.plugin.redis.JedisKit;
import com.jfinal.ext.plugin.redis.JedisPlugin;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;

/**
 */
public class StartConfig extends JFinalConfig {
	
	public void afterJFinalStart(){
		CacheService.service.initSysConfigDb();
	}
	
	public void beforeJFinalStop(){
	}
	
	public void configConstant(Constants me) {
		loadPropertyFile("classes/config"+Cons.CONFIG_SUFFIX+".properties");
		
		CacheService.service.loadPropertyFile("classes/config"+Cons.CONFIG_SUFFIX+".properties");
		
		me.setDevMode(getPropertyToBoolean("devMode", false));
		me.setViewType(ViewType.JSP);
		me.setMaxPostSize(getPropertyToInt("maxPostSize"));//上传文件大小
		
		me.setUploadedFileSaveDirectory(CacheFun.getConVal(ConfigKeys.UPLOAD_TEMP_FOLDER));
	}
	
	public void configRoute(Routes me) {
		me.add("/", CommonCtrl.class);
		
		me.add(new ApiRoute());//api
		me.add(new SysRoute());//web运营工具后台
		me.add(new MRoute());//html5
		
		if(Cons.MEDIA_DEV){//外部媒体信息
			me.add(new MediaRoute());
		}
	}
	
	public void configPlugin(Plugins me) {
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim(),getProperty("driverClassName").trim());
		me.add(c3p0Plugin);
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.addMapping("app_config", AppConfig.class);
		arp.addMapping("baike", Baike.class);
		arp.addMapping("baike_property", BaikeProperty.class);
		arp.addMapping("blog", Blog.class);
		arp.addMapping("enum_value", EnumValue.class);
		arp.addMapping("user",User.class);	
		arp.addMapping("sys_user", SysUser.class);
		arp.addMapping("sys_config", SysConfig.class);
		arp.addMapping("user_episode", UserEpisode.class);
		arp.addMapping("episode_mdm_epg", EpisodeMdmEpg.class);
		arp.addMapping("program_mdm_epg", ProgramMdmEpg.class);
		arp.addMapping("scene", Scene.class);
		arp.addMapping("element", Element.class);
		arp.addMapping("music", Music.class);
		arp.addMapping("mec_channel",MecChannel.class);
		arp.addMapping("mec_schedule",MecSchedule.class);
		arp.addMapping("down_history",DownHistory.class);
		arp.addMapping("mec_epg_program_type",MecEpgProgramType.class);
		arp.addMapping("ffmpeg_history",FfmpegHistory.class);
		arp.addMapping("imdb_fact",ImdbFact.class);
		arp.addMapping("imdb_fact_name",ImdbFactName.class);
		arp.addMapping("imdb_fact_title",ImdbFactTitle.class);
		arp.addMapping("question",Question.class);
		arp.addMapping("question_option",QuestionOption.class);
		arp.addMapping("mec_person",MecPerson.class);
		arp.addMapping("user_question",UserQuestion.class);
		arp.addMapping("mec_weibo_feed",MecWeiboFeed.class);
		arp.addMapping("syn_weibo_task",SynWeiboTask.class);
		
		arp.addMapping("lucky_draw_event", LuckyDrawEvent.class);
		arp.addMapping("lucky_draw_episode", LuckyDrawEpisode.class);
		arp.addMapping("lucky_draw_option", LuckyDrawOption.class);
		arp.addMapping("lucky_draw_history", LuckyDrawHistory.class);
		arp.addMapping("lucky_draw_before", LuckyDrawBefore.class);
		arp.addMapping("lucky_draw_before", LuckyDrawBefore.class);
		arp.addMapping("banner_img", BannerImg.class);
		arp.addMapping("banner_title", BannerTitle.class);
		
		//媒资库
		C3p0Plugin mdmDbPlugin = new C3p0Plugin(getProperty("mdmJdbcUrl"), getProperty("mdmUser"), getProperty("mdmPassword").trim(),getProperty("mdmDriverClassName").trim());
		me.add(mdmDbPlugin);   
		ActiveRecordPlugin arpMdm = new ActiveRecordPlugin(Cons.DB_NAME_MDM, mdmDbPlugin);
		me.add(arpMdm);
		arpMdm.addMapping("person",Person.class);
		arpMdm.addMapping("program", Program.class);
		arpMdm.addMapping("episode", Episode.class);
		arpMdm.addMapping("person_program", PersonProgram.class);
		arpMdm.addMapping("song", Song.class);
		arpMdm.addMapping("album", Album.class);
		arpMdm.addMapping("weibo_feed",WeiboFeed.class);
		
		
		if(Cons.MEDIA_DEV){//外部媒体信息
			C3p0Plugin mediaDbPlugin = new C3p0Plugin(getProperty("mediaJdbcUrl"), 
					getProperty("mediaUser"), getProperty("mediaPassword").trim(),
					getProperty("mediaDriverClassName").trim(),
					getPropertyToInt("mediaMaxPoolSize"),getPropertyToInt("mediaMinPoolSize"),getPropertyToInt("mediaInitialPoolSize"),null,null);
			me.add(mediaDbPlugin);   
			ActiveRecordPlugin arpMedia = new ActiveRecordPlugin(Cons.DB_NAME_MEDIA, mediaDbPlugin);
			me.add(arpMedia);
//		arpMedia.addMapping("epg_program",EpgProgram.class); arpMedia.addMapping("epg_episode", EpgEpisode.class); arpMedia.addMapping("imdb_program",ImdbProgram.class);
			arpMedia.addMapping("imdb_episode",ImdbEpisode.class);
			arpMedia.addMapping("imdb_event",ImdbEvent.class);
			arpMedia.addMapping("imdb_image",ImdbImage.class);
			arpMedia.addMapping("imdb_person",ImdbPerson.class);
			arpMedia.addMapping("imdb_person_video",ImdbPersonVideo.class);
			arpMedia.addMapping("imdb_plot",ImdbPlot.class);
			arpMedia.addMapping("imdb_scene",ImdbScene.class);
			arpMedia.addMapping("imdb_sound_album",ImdbSoundAlbum.class);
			arpMedia.addMapping("imdb_sound_album_video",ImdbSoundAlbumVideo.class);
			arpMedia.addMapping("imdb_sound_item",ImdbSoundItem.class);
			arpMedia.addMapping("imdb_sound_item_video",ImdbSoundItemVideo.class);
			arpMedia.addMapping("imdb_product",ImdbProduct.class);
			arpMedia.addMapping("imdb_person_principalt",ImdbPersonPrincipalt.class);
			arpMedia.addMapping("imdb_log",ImdbLog.class);
			arpMedia.addMapping("imdb_merge_sound",ImdbMergeSound.class);
		}
		
		if(getPropertyToBoolean("jobRun")){
			me.add(new QuartzPlugin("job.properties"));
		}
		
		if(Cons.DEVELOPING){
			me.add(new JedisPlugin());
		}
	}
	
	public void configInterceptor(Interceptors me) {
		me.add(new SysInterceptor());
		me.add(new ApiInterceptor());
	}

	public void configHandler(Handlers me) {
		
	}
	
	public static void main(String[] args) {
		JFinal.start("WebRoot", 80, "/", 5);
	}
}
