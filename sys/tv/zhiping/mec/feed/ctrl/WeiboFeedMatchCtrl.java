package tv.zhiping.mec.feed.ctrl;

import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;

/**
 * imdb的匹配
 * @author 张有良
 */

public class WeiboFeedMatchCtrl extends SysBaseCtrl{	
	/**
	 * App系统配置 index
	 * */
	public void index(){
		renderJsp("/page/util/weiboFeedMatch/input.jsp");
	}

//	public void save(){
//		StringBuilder msg = new StringBuilder();
//		
//		Episode episode = Episode.dao.findById(getPara("episode_id"));
//		
//		if(episode == null){
//			msg.append("请核对节目id是否存在");
//		}else{
//			//delByEpisodeId(episode.getId());
//			matchWeiboFeed(episode);		
//			msg.append("搞定");
//		}
//		renderText(msg.toString());
//	}
//	
//	public void delByEpisodeId(Long episode_id){
////		ElementAddon.dao.deleteByEpisodeId(episode_id);
//		Element.dao.deleteByEpisodeId(episode_id);
//		Scene.dao.deleteByEpisodeId(episode_id);
//	}
//	
//	private void matchWeiboFeed(Episode episode){ 
//		Long episode_id = episode.getId();
//		Program program = Program.dao.findById(episode.getPid());
//		String title = ComUtil.getEpisodeAppTitle(episode, program);
//		List<WeiboFeed> list = WeiboFeed.dao.queryByEpisode(episode_id);
//		if(list!=null && !list.isEmpty()){
//			for(int i=0;i<list.size();i++){
//				WeiboFeed weibo = list.get(i);
//				try {
//					Element element = new Element();
//					element.setFid(weibo.getId());
//					element.setType(Element.ElementType.weibo.toString());
//					element.setProgram_id(weibo.getProgram_id());
//					element.setEpisode_id(weibo.getEpisode_id());
//					
//					element.setStart_time(weibo.getStart_time());
//					element.setEnd_time(weibo.getStart_time());
//					element.setDuration(element.getEnd_time() - element.getStart_time());
//					
//					Element db_element = null; //Element.dao.queryByObj(element);
//					if(db_element==null){//数据库没有，需要保存
//						Scene scene = Scene.dao.queryByEpisodeTime(episode_id,element.getStart_time(),element.getEnd_time());
//						if(scene == null){
//							scene = new Scene();
//							scene.setPid(episode_id);
//							scene.setTitle(title+"微直播");
//							scene.setStart_time(weibo.getStart_time());
//							
//							Scene gtScene = Scene.dao.queryByEpisodeGtStartTime(episode_id,element.getStart_time());
//							if(gtScene!=null){
//								scene.setEnd_time(gtScene.getStart_time()-1);
//							}else{
//								scene.setEnd_time(list.get(list.size()-1).getStart_time());
//							}
//							scene.setDuration(scene.getEnd_time() - scene.getStart_time());
//							scene.setSummary(scene.getTitle());
//							
//							scene.setAddDef();
//							scene.save();
//						}
//						
//						element.setScene_id(scene.getId());
//						if(element.getScene_id() != null){
//							element.setAddDef();
//							element.save();
//						}
//					}
//				} catch (Exception e) {
//					log.error(e.getMessage(),e);
//				}
//			}
//		}
//	}

}

