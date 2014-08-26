package tv.zhiping.mec.feed.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tv.zhiping.mdm.model.Episode;

/**
 * 暂时无用，imdb导入后自动回补充
 * 补充剧集信息
 * @author 张有良
 */

public class SupplyEpisodeCtrl extends FeedBaseCtrl{	
	/**
	 *
//		select (select count(1) from episode c where c.pid=a.id) as esp_count,a.id,a.title,a.orig_title,a.year,a.current_season,a.imdb_url from program a where orig_title='the good wife' and type='TV' order by current_season
 * 导出数据： select a.id,b.title,a.title,a.current_episode from episode a,program b where a.pid=b.id and a.status='1' and b.orig_title in ('24','the good wife','the good wife') 
and b.type='TV' order by a.pid,b.current_season,a.current_episode
	 * */
	public void index(){
//		Long[] program_id = new Long[]{140860L,140861L};
//		Long[] episode_count = new Long[]{12L,13L};
//		String name = "美国恐怖故事";
//		String type = "TV";
//		Map<Integer,String> seasonHzMap = getSeasonHz();
//		for(int i=0;i<program_id.length;i++){
//			List<Episode> list = Episode.dao.queryByPid(program_id[i]);
//			Map<Long, Episode> map = getAllEpisodeMap(list);
//			String seasonHz = seasonHzMap.get(new Integer(i+1));
//			for(Long j=0L;j<episode_count[i];j++){
//				Long episode_num = j+1;
//				Episode episode = map.get(episode_num);
//				if(episode==null){
//					episode = new Episode();
//				}
//				episode.setPid(program_id[i]);
//				episode.setCurrent_episode(episode_num);
//				episode.setType(type);
//				
//				episode.setTitle(name+" 第"+seasonHz+"季-第"+episode_num+"集"); //24小时 第一季-第1集
//				if(episode.getId() != null){
//					episode.setUpdDef();
//					episode.update();
//				}else{
//					episode.setAddDef();
//					episode.save();
//				}
//			}
//			
//		}
		renderText("搞定");
	}
	
	private Map<Integer,String> getSeasonHz(){
		Map<Integer,String> map = new HashMap();
		
		map.put(1,"一");
		map.put(2,"二");
		map.put(3,"三");
		map.put(4,"四");
		map.put(5,"五");
		map.put(6,"六");
		map.put(7,"七");
		map.put(8,"八");
		map.put(9,"九");
		map.put(10,"十");
		
		
		return map;
	}

	private Map<Long, Episode> getAllEpisodeMap(List<Episode> list) {
		Map<Long,Episode> map = new HashMap();
		for(int j=0;j<list.size();j++){
			Episode episode = list.get(j);
			map.put(episode.getCurrent_episode(),episode);
		}
		return map;
	}
	
	
	
}

