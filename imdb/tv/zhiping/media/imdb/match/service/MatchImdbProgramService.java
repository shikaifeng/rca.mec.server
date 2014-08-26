package tv.zhiping.media.imdb.match.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import tv.zhiping.common.Cons;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mdm.model.Program.ProgramType;
import tv.zhiping.media.model.ImdbEpisode;

import com.jfinal.plugin.activerecord.Page;

public class MatchImdbProgramService {
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 匹配电视剧
	 */
	public void matchTvSeries(){
		String type = "tvSeries";
		ImdbEpisode obj = new ImdbEpisode();
		obj.setType(type);
		obj.setMatch_state(Cons.THREAD_STATE_WAIT);
		while(true){
			Page<ImdbEpisode> page = ImdbEpisode.dao.paginateWaitMatchTv(1,Cons.MAX_PAGE_SIZE);
			List<ImdbEpisode> list = page.getList();
			if(list==null || list.isEmpty()){
				break;
			}else{
				for(int i=0;i<list.size();i++){
					ImdbEpisode imdb = list.get(i);
					try {
						imdb.setMatch_state(Cons.THREAD_STATE_DEALING);
						imdb.setUpdDef();
						imdb.update();
						
						matchTvEpisode(imdb);
					} catch (Exception e) {
						log.error(e.getMessage(),e);
					}
					imdb.setUpdDef();
					imdb.update();
				}
			}
		}
	}
	
	
	//匹配集
	public void matchTvEpisode(ImdbEpisode imdb_episode) throws Exception{
		imdb_episode.setMatch_state(Cons.THREAD_STATE_FAIL);
		if(StringUtils.isNoneBlank(imdb_episode.getPid())){
			ImdbEpisode imdb_program = ImdbEpisode.dao.findById(imdb_episode.getPid());
			if(imdb_program!=null){
				String title = Program.dao.queryByOrigTitleType(imdb_program.getOrig_title(),ProgramType.TV.toString());//中文标题,如果没有直接使用之前的。
				if(StringUtils.isBlank(title)){
					title = imdb_program.getTitle();//没有直接使用父亲的
				}
				
				String minYear = ImdbEpisode.dao.queryMinYearByPidSeason(imdb_episode.getPid(),imdb_episode.getSeason());
				if(StringUtils.isNotBlank(minYear)){
					Program program = Program.dao.queryByObj(imdb_episode.getMdm_program_id(),null,imdb_program.getOrig_title(),minYear,ProgramType.TV.toString());
					if(program == null){
						program = new Program();
						imdb_program.parseMdmProgram(imdb_episode,program,title);
						
						imdb_episode.setMatch_type("program add");
						
						program.setAddDef();
						program.save();
					}else{
						boolean upd = false;
						upd = setUpdFiledsProgram(imdb_episode, imdb_program,program, upd);
						if(upd){
							imdb_episode.setMatch_type("program upd");
							program.setUpdDef();
							program.update();
						}
					}
					
					Episode episode = Episode.dao.queryByPidcurrentEpisodea(program.getId(),imdb_episode.getCurrent_episode());
					if(episode == null){
						episode = new Episode();
						imdb_episode.parseMdmEpisode(program, episode,title);
						imdb_episode.setMatch_type(imdb_episode.getMatch_type()+" episode add");
						episode.setAddDef();
						episode.save();
					}else{
						boolean upd = setUpdEpisode(imdb_episode, program,episode,title);
						if(upd){
							imdb_episode.setMatch_type(imdb_episode.getMatch_type()+" episode upd");
							episode.setUpdDef();
							episode.update();
						}
					}
					
					imdb_episode.setMdm_program_id(program.getId());
					imdb_episode.setMdm_episode_id(episode.getId());
					imdb_episode.setMatch_state(Cons.THREAD_STATE_SUC);
					
					if(!Cons.THREAD_STATE_SUC.equals(imdb_program.getMatch_state())){
						imdb_program.setMsg("mdm program_id="+program.getId());
						imdb_program.setMatch_state(Cons.THREAD_STATE_SUC);
						imdb_program.setUpdDef();
						imdb_program.update();
					}
				}else{
					imdb_episode.setMsg("tvEpisode year is null");	
				}
			}else{//要退出
				imdb_episode.setMsg("tvEpisode pid db is null");
			}
		}else{
			imdb_episode.setMsg("tvEpisode pid isnull");
		}
	}

	private boolean setUpdEpisode(ImdbEpisode imdb_episode, Program program,
			Episode episode,String title) {
		boolean upd = false;
		
		if(!episode.getPid().equals(program.getId())){
			log.debug("episode_pid"+episode.getPid()+"  to="+program.getId());
			episode.setPid(program.getId());
			upd = true;
		}
		
		if(StringUtils.isBlank(episode.getCover()) &&  StringUtils.isBlank(episode.getImages()) && StringUtils.isNoneBlank(imdb_episode.getImages())){
			episode.setImages(imdb_episode.getImages());
			upd = true;
		}
		
		if(StringUtils.isBlank(episode.getTitle())){
			episode.setTitle(title);
			upd = true;
		}
		if(StringUtils.isBlank(episode.getOrig_title())){
			episode.setOrig_title(imdb_episode.getOrig_title());
			upd = true;
		}
		if(StringUtils.isBlank(episode.getSummary()) && StringUtils.isNotBlank(imdb_episode.getSummary())){
			episode.setSummary(imdb_episode.getSummary());
			upd = true;
		}
		if(StringUtils.isBlank(episode.getImdb_url()) || !episode.getImdb_url().equals(imdb_episode.getUrl())){
			episode.setImdb_url(imdb_episode.getUrl());
			upd = true;
		}
		return upd;
	}

	private boolean setUpdFiledsProgram(ImdbEpisode imdb_episode,
			ImdbEpisode imdb_program, Program program, boolean upd) {
		if(StringUtils.isBlank(program.getCover()) && StringUtils.isBlank(program.getImages()) && StringUtils.isNotBlank(imdb_program.getImages())){
			program.setImages(imdb_program.getImages());
			upd = true;
		}
		if(program.getCurrent_season()==null || Cons.DEF_NULL_NUMBER.equals(program.getCurrent_season())){
			if(StringUtils.isNoneBlank(imdb_episode.getSeason())){
				program.setCurrent_season(new Long(imdb_episode.getSeason()));
				upd = true;								
			}
		}
		if(StringUtils.isBlank(program.getSummary()) && StringUtils.isNotBlank(imdb_program.getSummary())){
			program.setSummary(imdb_program.getSummary());
			upd = true;
		}
		
		if(StringUtils.isBlank(program.getImdb_url()) || !program.getImdb_url().equals(imdb_episode.getUrl())){
			program.setImdb_url(imdb_episode.getUrl());
			upd = true;
		}
		return upd;
	}

		
	
	/**
	 * 匹配电影
	 */
	public void matchMovie(){
		String type = "movie";
		ImdbEpisode obj = new ImdbEpisode();
		obj.setType(type);
		obj.setMatch_state(Cons.THREAD_STATE_WAIT);
		
		while(true){
			Page<ImdbEpisode> page = ImdbEpisode.dao.paginateByMovie(obj,1,Cons.MAX_PAGE_SIZE);
			List<ImdbEpisode> list = page.getList();
			if(list==null || list.isEmpty()){
				break;
			}else{
				for(int i=0;i<list.size();i++){
					ImdbEpisode imdb = list.get(i);
					
					Program mdm = Program.dao.queryByObj(imdb.getMdm_program_id(),imdb.getUrl(),imdb.getTitle(),imdb.getYear(),ProgramType.Movie.toString());//imdb.getType()
					if(mdm!=null){
						imdb.setMatch_state(Cons.THREAD_STATE_SUC);
						imdb.setMdm_program_id(mdm.getId());
						
						boolean upd = false;
						if(StringUtils.isBlank(mdm.getCover()) && StringUtils.isBlank(mdm.getImages()) && StringUtils.isNoneBlank(imdb.getImages())){
							mdm.setImages(imdb.getImages());
							upd = true;
						}
						
						if(StringUtils.isBlank(mdm.getSummary()) && StringUtils.isNotBlank(imdb.getSummary())){
							mdm.setSummary(imdb.getSummary());
							upd = true;
						}
						if(StringUtils.isBlank(mdm.getImdb_url()) || !mdm.getImdb_url().equals(imdb.getUrl())){
							mdm.setImdb_url(imdb.getUrl());
							upd = true;
						}
						if(upd){
							mdm.setUpdDef();
							mdm.update();
							imdb.setMatch_type("program upd");//需要有增加功能
						}
						
						Episode episode = Episode.dao.queryMovieByPid(mdm.getId());
						if(episode==null){
							episode = new Episode();
							episode.setPid(mdm.getId());
							episode.setTitle(mdm.getTitle());
							episode.setType(mdm.getType());
							episode.setYear(mdm.getYear());
							episode.setSummary(mdm.getSummary());
							episode.setImdb_url(imdb.getUrl());
							
							imdb.setMatch_type(imdb.getMatch_type()+" episode add");
							
							episode.setAddDef();
							episode.save();
						}else{//去修改
							if(StringUtils.isBlank(episode.getImdb_url()) || !episode.getImdb_url().equals(imdb.getUrl())){
								imdb.setMatch_type(imdb.getMatch_type()+" episode upd");
								episode.setImdb_url(imdb.getUrl());
								episode.setUpdDef();
								episode.update();
							}
							
						}
						imdb.setMdm_episode_id(episode.getId());
					}else{
						imdb.setMsg("program is null");
						imdb.setMatch_state(Cons.THREAD_STATE_FAIL);
					}
					
					imdb.setUpdDef();
					imdb.update();
				}
			}
		}
	}
	
	
	
}
