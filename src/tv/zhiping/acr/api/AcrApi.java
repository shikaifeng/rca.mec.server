package tv.zhiping.acr.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import tv.zhiping.acr.bean.AcrReponse;
import tv.zhiping.acr.bean.AcrReponse.AcrNonlive;
import tv.zhiping.acr.bean.AcrReponse.AcrResponseData;
import tv.zhiping.common.Cons;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mec.epg.model.MecSchedule;

import com.alibaba.fastjson.JSONObject;

public class AcrApi {
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 解析acr返回的字符串，返回mec的数据
	 * @param acrCallBack
	 * @return 
	 */
	public AcrChooseRes parset2MdmResult(String acrCallBack) {
		AcrChooseRes res = new AcrChooseRes();
		AcrReponse acrRes = parse2Response(acrCallBack);
		
		//epg接口 返回剧集信息
		if(AcrReponse.SUC.equals(acrRes.getStatus())){
			AcrResponseData acrData = acrRes.getData();
			if(acrData!=null){
				
				List<AcrNonlive> nonliveList = acrData.getNonlive();
				if(nonliveList!=null && !nonliveList.isEmpty()){//点播
					parseNonlive(res, nonliveList);
				}
				
//				else{
//					List<AcrLive> liveList = acrData.getLive();
//					if(liveList!=null && !liveList.isEmpty()){//直播
//						AcrLive live = liveList.get(0);
//						acr_info[2] = new Long(Cons.YES);
//						MecChannel mec_channel = MecChannel.dao.queryByPy(live.getChannel_name());
//						if(mec_channel!=null){
//							Long now = System.currentTimeMillis();
//							
//							MecSchedule schedule = MecSchedule.dao.queryByChannelAndTime(mec_channel.getRid(),now);
//							
//							if(schedule!=null && StringUtils.isNoneBlank(schedule.getEpg_episode_id())){
//								EpisodeMdmEpg ext = EpisodeMdmEpg.dao.queryByEgpId(new Long(schedule.getEpg_episode_id()));
//								if(ext!=null){
//									acr_info[0] = ext.getMdm_id();
//								}
//								acr_info[1] = now - schedule.getMec_start_at();
//							}
//						}
//					}
//				}
			}
		}
		return res;
	}

	
	/**
	 * 解析 acr2.2版本返回的协议
	 * @param str
	 * @return
	 */
	public AcrReponse parse2Response(String str){
		return JSONObject.parseObject(str,AcrReponse.class);
	}
	
	
	/**
	 * 解析acr返回的字符串，返回mec的数据
	 * @param acrCallBack
	 * @return 
	 */
	public TmallBoxAcrRes tmallRecognitionRes(String type,String reference_id,Long time) {
		TmallBoxAcrRes res = new TmallBoxAcrRes();
		
		Long episode_id = null;
		Long relative_time = null;
		if("live".equals(type)){//直播
			MecSchedule schedule = MecSchedule.dao.queryByChannelPyAndTime(reference_id,time);
			if(schedule!=null){
				episode_id = schedule.getMdm_episode_id();
				relative_time = time - schedule.getMec_start_at();
			}
		}else{//非直播
			episode_id = Long.parseLong(reference_id);
			relative_time = time;
		}
		res.setEpisode_id(episode_id);
		res.setOffsettime(relative_time);;
		return res;
	}

	
	/**
	 * 解析非直播
	 * @param acr_info 0:episodeId 1:offset 2:live_flag 3: 节目id
	 * @param nonliveList
	 */
	private void parseNonlive(AcrChooseRes res, List<AcrNonlive> nonliveList) {
		Set<Long> programIdList = new HashSet<Long>();
		Set<Long> episodeIdList = new HashSet<Long>();
		
		Map<Long,Set<Long>> exisodeCount = new HashMap<Long,Set<Long>>();
		Map<Long,Integer> offsetCount = new HashMap<Long,Integer>();
		
		AcrNonlive maxlive = null;
		Long maxProgramId = null;
		Long maxEpisodeId = null;
		
		Long lastProgramId = null;
		Episode episode  = null;
		Long episode_id = null;
		for(int i=0;i<nonliveList.size();i++){//非
			AcrNonlive nolive = nonliveList.get(i);
			episode_id = nolive.getMeta_customer_id();
			episodeIdList.add(episode_id);
			
			Integer count = offsetCount.get(episode_id);
			if(count == null){
				count = 0;
			}
			count++;
			offsetCount.put(episode_id,count);
			
//			if(programIdList.size()<10){//超过5个就不查了
			episode = Episode.dao.findById(episode_id);
			if(episode!=null){
				lastProgramId = episode.getPid();
				programIdList.add(lastProgramId);
				Set<Long> set = exisodeCount.get(lastProgramId);
				if(set == null){
					set = new HashSet<Long>();
				}
				set.add(episode_id);
				exisodeCount.put(episode.getPid(),set);
				
				if(maxlive == null || maxlive.getHit_count()<nolive.getHit_count()){
					maxlive = nolive;
					maxProgramId = lastProgramId;
					maxEpisodeId = episode_id;
				}
			}else{//代表空值
				log.warn("data errror episode_id:"+nolive.getMeta_customer_id());
			}
//			}
		}
	
		//设置最可能的剧集信息
		AcrChooseEpisode acrEpisode = new AcrChooseEpisode();
		acrEpisode.setEpisode_id(maxlive.getMeta_customer_id());
		acrEpisode.setOffset(maxlive.getMaster_end());
		
		if(episode==null || !episode.getId().equals(acrEpisode.getEpisode_id()) ){//判断最后一次查询是否和最适配的信息一致
			episode = Episode.dao.findById(acrEpisode.getEpisode_id());
		}
		
		if(episode!=null){
			acrEpisode.setProgram_id(episode.getPid());
			acrEpisode.setCurrent_episode(episode.getCurrent_episode());
		}
		res.setPossible_acr_chooose_episode(acrEpisode);
		
		res.setProgrma_count(programIdList.size());
		res.setEpisode_count(episodeIdList.size());
		
		Set<Long> set = exisodeCount.get(maxProgramId);
		Integer count = 0;
		if(set!=null){
			count = set.size();
		}
		
		res.setPossible_acr_chooose_episode_count(count);//最可能的剧集的节目 下的剧集数
		res.setPossible_acr_chooose_offset_count(offsetCount.get(maxEpisodeId));//最有可能的剧集的offset个数
	}
	
	/**
	 * 天猫魔盒的acr识别完接口
	 * @author liang
	 */
	public static class TmallBoxAcrRes{
		private Long episode_id;
		private Long offsettime;
		public Long getEpisode_id() {
			return episode_id;
		}
		public void setEpisode_id(Long episode_id) {
			this.episode_id = episode_id;
		}
		public Long getOffsettime() {
			return offsettime;
		}
		public void setOffsettime(Long offsettime) {
			this.offsettime = offsettime;
		}
	}
	
	
	public static class AcrChooseRes{
		private Integer progrma_count;
		private Integer episode_count;
		private Integer live_flag = Cons.NO;
		
		//最可能的节目信息
		private AcrChooseEpisode possible_acr_chooose_episode;
	
		//最可能的剧集的节目的剧集数
		private Integer possible_acr_chooose_episode_count;
		
		//最可能的节目的剧集的offsettime
		private Integer possible_acr_chooose_offset_count;
		
		
		public Integer getPossible_acr_chooose_offset_count() {
			return possible_acr_chooose_offset_count;
		}

		public void setPossible_acr_chooose_offset_count(
				Integer possible_acr_chooose_offset_count) {
			this.possible_acr_chooose_offset_count = possible_acr_chooose_offset_count;
		}

		public Integer getPossible_acr_chooose_episode_count() {
			return possible_acr_chooose_episode_count;
		}

		public void setPossible_acr_chooose_episode_count(
				Integer possible_acr_chooose_episode_count) {
			this.possible_acr_chooose_episode_count = possible_acr_chooose_episode_count;
		}

		public Integer getProgrma_count() {
			return progrma_count;
		}
	
		public void setProgrma_count(Integer progrma_count) {
			this.progrma_count = progrma_count;
		}
	
		public Integer getEpisode_count() {
			return episode_count;
		}
	
		public void setEpisode_count(Integer episode_count) {
			this.episode_count = episode_count;
		}
	
		public AcrChooseEpisode getPossible_acr_chooose_episode() {
			return possible_acr_chooose_episode;
		}

		public void setPossible_acr_chooose_episode(
				AcrChooseEpisode possible_acr_chooose_episode) {
			this.possible_acr_chooose_episode = possible_acr_chooose_episode;
		}

		public Integer getLive_flag() {
			return live_flag;
		}
	
		public void setLive_flag(Integer live_flag) {
			this.live_flag = live_flag;
		}
	}
	
	
	public static class AcrChooseEpisode{
		private Long program_id;
		private Long episode_id;
		private Long current_episode;
		private Long offset;
		
		public Long getCurrent_episode() {
			return current_episode;
		}
		public void setCurrent_episode(Long current_episode) {
			this.current_episode = current_episode;
		}
		public Long getOffset() {
			return offset;
		}
		public void setOffset(Long offset) {
			this.offset = offset;
		}
		public Long getProgram_id() {
			return program_id;
		}
		public void setProgram_id(Long program_id) {
			this.program_id = program_id;
		}
		public Long getEpisode_id() {
			return episode_id;
		}
		public void setEpisode_id(Long episode_id) {
			this.episode_id = episode_id;
		}
	}
}

