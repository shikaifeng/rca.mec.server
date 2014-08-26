package tv.zhiping.mec.mdm.ctrl;

import tv.zhiping.common.Cons;
import tv.zhiping.mdm.model.WeiboFeed;
import tv.zhiping.mec.feed.ctrl.FeedBaseCtrl;
import tv.zhiping.mec.feed.model.SynWeiboTask;

/**
 * 微博feed管理
 * @author liang
 */
public class WeiboFeedCtrl extends FeedBaseCtrl {
	
	/**
	 * 屏蔽或启用
	 */
	public void disable_opt(){
		Long id = getParaToLong("id");
		Integer opt = getParaToInt("opt");
		
		WeiboFeed obj = new WeiboFeed();
		obj.setId(id);
		obj.setStatus(opt);
		obj.setUpdDef();
		obj.update();
		
		renderJson(Cons.JSON_SUC);
	}
	
	/**
	 * 设置task的周期
	 */
	public void save_weibo_time(){
		Long program_id = getParaToLong("program_id");
		Long episode_id = getParaToLong("episode_id");
		SynWeiboTask obj = getModel(SynWeiboTask.class,"obj");
		if(episode_id!=null){
			if(obj!=null){
				obj.setProgram_id(program_id);
				obj.setEpisode_id(episode_id);
				if(obj.getId()!=null){
					obj.update();
				}else{
					obj.save();
				}
			}
		}
		renderJson(Cons.JSON_SUC);
	}
	
	
	public void set_weibo_time(){
		Long program_id = getParaToLong("program_id");
		Long episode_id = getParaToLong("episode_id");
		SynWeiboTask obj = SynWeiboTask.dao.queryByEpisodeId(episode_id);
		setAttr("program_id",program_id);
		setAttr("episode_id",episode_id);
		setAttr("obj",obj);
		render("/page/feed/weibo/input_time.jsp");
	}
	
}
