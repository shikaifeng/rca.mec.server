package tv.zhiping.media.imdb.match.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import tv.zhiping.common.Cons;
import tv.zhiping.mdm.model.Person;
import tv.zhiping.mdm.model.PersonProgram;
import tv.zhiping.media.model.ImdbEpisode;
import tv.zhiping.media.model.ImdbLog;
import tv.zhiping.media.model.ImdbPerson;
import tv.zhiping.media.model.ImdbPersonPrincipalt;
import tv.zhiping.media.model.ImdbPersonVideo;
import tv.zhiping.utils.DateUtil;

import com.jfinal.plugin.activerecord.Page;

public class MatchImdbPersonService {
	private Logger log = Logger.getLogger(this.getClass());
	
	public void matchPerson(){
		while(true){
			Page<ImdbPerson> page = ImdbPerson.dao.paginateByWiatMatch(1,Cons.MAX_PAGE_SIZE);
			List<ImdbPerson> list = page.getList();
			if(list==null || list.isEmpty()){
				break;
			}else{
				for(int i=0;i<list.size();i++){
					ImdbPerson imdb = list.get(i);
					imdb.setMatch_state(Cons.THREAD_STATE_DEALING);
					imdb.update();
					
					matchSignPerson(imdb);
					
					ImdbPerson db = ImdbPerson.dao.findById(imdb.getId());
					if(!Cons.THREAD_STATE_SUC.equals(db.getMatch_state())){
						imdb.setUpdDef();
						imdb.update();
					}
				}
			}
		}
	}

	private void matchSignPerson(ImdbPerson imdb) {
		if(ImdbPerson.ADD_MDM_ID_SIGN.equals(imdb.getMdm_id())){//手工处理
			addPerson(imdb);
		}else{
			List<Person> mdms = Person.dao.queryByIdOrImdbUrlOrNameEn(imdb.getMdm_id(),imdb.getImdb_url(),imdb.getName_en());
			imdb.setMatch_state(Cons.THREAD_STATE_FAIL);
			if(mdms!=null && !mdms.isEmpty()){
				if(mdms.size()>1){
					imdb.setMsg("明星有多个"+logPerson(mdms));
					
					matchSignPersonVideoRelation(imdb,mdms);
				}else{
					Person person = mdms.get(0);
					
					updPersonByMatch(imdb, person);
				}
			}else{//无任何数据
				addPerson(imdb);
			}
		}
	}

	private void updPersonByMatch(ImdbPerson imdb, Person person) {
		imdb.setMatch_type("upd");
		imdb.setMdm_id(person.getId());
		imdb.setMatch_state(Cons.THREAD_STATE_SUC);
		
		imdb.parseUpdPerson2Imdb(person);
		
		person.setUpdDef();
		person.update();
	}

	private void addPerson(ImdbPerson imdb) {
		Person person = new Person();
		
		imdb.parseImdb2Person(person);
		
		person.setAddDef();
		person.save();
		
		imdb.setMatch_type("add");
		imdb.setMdm_id(person.getId());
		imdb.setMatch_state(Cons.THREAD_STATE_SUC);
	}
	
	//单个明星匹配
	private void matchSignPersonVideoRelation(ImdbPerson imdb,List<Person> mdms) {
		List<ImdbEpisode> list = ImdbEpisode.dao.queryByPersonId(imdb.getId());//参演的剧集
		
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				ImdbEpisode imdb_episode = list.get(i);
				
				boolean flag = true;
				if(imdb_episode.getMdm_program_id() == null || Cons.DEF_NULL_NUMBER.equals(imdb_episode.getMdm_program_id()) ||
						imdb_episode.getMdm_episode_id() == null || Cons.DEF_NULL_NUMBER.equals(imdb_episode.getMdm_episode_id())){
					flag = false;
				}
				
				if(flag){
					List<Person> personList = Person.dao.queryProgramIdName(imdb_episode.getMdm_program_id(),imdb.getName());//判断这个人是否也参演了这个节目
					if(personList!=null && !personList.isEmpty()){//节目剧集是否一致
						int size = personList.size();
						if (size == 1) {//等于1匹配
							Person person = personList.get(0);
							
							updPersonByMatch(imdb, person);
							
							return;
						}else{
							logDb("warn", "match_person","节目id:"+imdb_episode.getMdm_program_id()+":"+imdb.getName()+"多个匹配上");
						}
					}
				}
			}
		}else{
			imdb.setMsg("imdb_vidoe_rel is null "+imdb.getMsg());
		}
		
		matchPersonByOther(imdb,mdms);
	}
	
	//单个明星生日去匹配
	private void matchPersonByOther(ImdbPerson imdb,List<Person> personList) {
		Person match_person = null;//最匹配的值
		int match_count = 0;
		int have_detail_count = 0;
		for(int i=0;i<personList.size();i++){
			Person person = personList.get(i);
			
			if(person.isNoHaveDetail()){
				have_detail_count++;
				if(imdb.getBirthday()!=null && person.getBirthday()!=null){
					String src_year = DateUtil.getDateSampleString(imdb.getBirthday()).substring(0,4);
					String target_year = DateUtil.getDateSampleString(person.getBirthday()).substring(0,4);
					if(src_year.equals(target_year)){//生日相同
						match_count++;
						match_person = person;
					}
				}else if(StringUtils.isNotBlank(imdb.getBorn_place()) && imdb.getBorn_place().equalsIgnoreCase(person.getBorn_place())){
					match_count++;
					match_person = person;
				}
			}
		}
		
		if(match_count == 1){//最匹配的。
			updPersonByMatch(imdb,match_person);
		}else if(have_detail_count == 0){//没有一个是有详情的
			addPerson(imdb);
		}
	}
	
	
	//匹配人物关系
	public void matchPersonVideoRelation(){ 
		while(true){
			Page<ImdbPersonVideo> page = ImdbPersonVideo.dao.paginateWait(1,Cons.MAX_PAGE_SIZE);
			List<ImdbPersonVideo> list = page.getList();
			if(list==null || list.isEmpty()){
				break;
			}else{
				for(int i=0;i<list.size();i++){
					ImdbPersonVideo imdb = list.get(i);
					try {
						imdb.setMatch_state(Cons.THREAD_STATE_DEALING);
						imdb.setUpdDef();
						imdb.update();
						
						matchPersonVideoRelation(imdb);
					} catch (Exception e) {
						log.error(e.getMessage(),e);
					}
					imdb.setUpdDef();
					imdb.update();
				}
			}
		}
	}
	
	public void matchPersonPrincipalt(){
		while(true){
			List<ImdbPersonPrincipalt> list = ImdbPersonPrincipalt.dao.queryByWaitMatch();
			if(list==null || list.isEmpty()){
				break;
			}else{
				for(int i=0;i<list.size();i++){
					ImdbPersonPrincipalt imdb = list.get(i);
					imdb.setMatch_state(Cons.THREAD_STATE_WAIT);
					imdb.setUpdDef();
					imdb.update();
					
					matchSignPrincipalt(imdb);
					
					imdb.setUpdDef();
					imdb.update();
				}
			}
		}
	}

	//单个匹配
	private void matchSignPrincipalt(ImdbPersonPrincipalt imdb) {
		Long mdm_person_id = null;
		
		ImdbPerson person = ImdbPerson.dao.findById(imdb.getName_id());
		if(person!=null){
			mdm_person_id = person.getMdm_id();
		}
		
		Long mdm_program_id = imdb.getLong("mdm_program_id");
		boolean flag = true;
		if(mdm_person_id == null || Cons.DEF_NULL_NUMBER.equals(mdm_person_id)){
			imdb.setMsg("mdm_person_id is null mdm_person_id="+mdm_person_id);
			flag = false;
		}
		
		if(mdm_program_id == null || Cons.DEF_NULL_NUMBER.equals(mdm_program_id)){
			imdb.setMsg("mdm_program_id is null mdm_program_id="+mdm_program_id);
			flag = false;
		}
		imdb.setMatch_state(Cons.THREAD_STATE_FAIL);
		
		if(flag){
			List<PersonProgram> pps = PersonProgram.dao.queryPersonProgramId(mdm_person_id,mdm_program_id,null);
			
			if(pps!=null && !pps.isEmpty()){
				for(int j=0;j<pps.size();j++){
					PersonProgram pp = pps.get(j);
					if(!pp.getIs_primary()){
						pp.setIs_primary(Cons.TRUE);
						pp.setUpdDef();
						pp.update();
					}
					imdb.setMatch_state(Cons.THREAD_STATE_SUC);
					imdb.setMdm_id(pp.getId());
				}
			}else{
				imdb.setMsg("person_program is null");
			}
		}
	}


	private void matchPersonVideoRelation(ImdbPersonVideo imdb) {
		imdb.setMatch_state(Cons.THREAD_STATE_FAIL);
		ImdbEpisode imdb_episode = ImdbEpisode.dao.findById(imdb.getVideo_id());
		ImdbPerson imdb_person = ImdbPerson.dao.findById(imdb.getPerson_id());
		boolean flag = true;
		if(imdb_episode == null){
			imdb.setMsg("episode id entity is null id="+imdb.getVideo_id());
			flag = false;
		}
		
		if(imdb_episode.getMdm_program_id() == null || Cons.DEF_NULL_NUMBER.equals(imdb_episode.getMdm_program_id()) ||
				imdb_episode.getMdm_episode_id() == null || Cons.DEF_NULL_NUMBER.equals(imdb_episode.getMdm_episode_id())){
			imdb.setMsg("video_id="+imdb.getVideo_id()+" mdm_program_id="+imdb_episode.getMdm_program_id()+" mdm_episode_id="+imdb_episode.getMdm_episode_id());
			flag = false;
		}
		if(imdb_person == null){
			imdb.setMsg("person id entity is null id="+imdb.getPerson_id());
			flag = false;
		}
		if(imdb_person.getMdm_id() == null || Cons.DEF_NULL_NUMBER.equals(imdb_person.getMdm_id())){
			imdb.setMsg("person_id="+imdb.getPerson_id()+" mdm_id="+imdb_person.getMdm_id());
			flag = false;
		}
		
		if(imdb.getMdm_id()!=null && !Cons.DEF_NULL_NUMBER.equals(imdb.getMdm_id()) ){//已经匹配过
			imdb.setMatch_state(Cons.THREAD_STATE_SUC);
			flag = false;
		}
		
//			临时使用的策略
//			if(imdb.getMdm_id()!=null && !Cons.DEF_NULL_NUMBER.equals(imdb.getMdm_id())){//不相同
//				PersonProgram pp = PersonProgram.dao.findById(imdb.getMdm_id());
//				if(pp!=null){
//					if(pp.getCharacter_name().equals(pp.getCharacter_name_en())){//表示相同
//						if(!pp.getProgram_id().equals(imdb_episode.getMdm_program_id())){//不相同
//							pp.setProgram_id(imdb_episode.getMdm_program_id());
//							
//							pp.setUpdDef();
//							pp.update();
//							flag = false;
//							
//							imdb.setMatch_state(Cons.THREAD_STATE_SUC);
//							imdb.setMdm_id(pp.getId());
//						}
//					}
//				}
//			}
		
		if(flag){
			List<PersonProgram> pps = PersonProgram.dao.queryPersonProgramId(imdb_person.getMdm_id(),imdb_episode.getMdm_program_id(), imdb.getProfession());
			matchPersonProgramData(imdb, imdb_episode, imdb_person, pps);
		}
	}
	

	/**
	 * 匹配剧集人物的关系
	 * @param imdb
	 * @param imdb_episode
	 * @param imdb_person
	 * @param pps
	 */
	private boolean matchPersonProgramData(ImdbPersonVideo imdb,
			ImdbEpisode imdb_episode, ImdbPerson imdb_person,List<PersonProgram> pps) {
		boolean flag = false;
		if(pps == null || pps.isEmpty()){
			PersonProgram pp = new PersonProgram();
			pp.setPerson_id(imdb_person.getMdm_id());
			pp.setProgram_id(imdb_episode.getMdm_program_id());
			pp.setProfession(imdb.getProfession());
			pp.setCharacter_name(imdb.getCharacter_name());
			pp.setCharacter_name_en(imdb.getCharacter_name_en());
			pp.setAddDef();
			pp.save();
			imdb.setMatch_state(Cons.THREAD_STATE_SUC);
			imdb.setMdm_id(pp.getId());
			flag = true;
		}else if(pps.size()==1){
			PersonProgram pp = pps.get(0);
			setUpdPersonProgram(imdb, pp);
			flag = true;
		}else {
			imdb.setMsg("有多个关系"+logPersonProgram(pps));

			String imdbCharacter = imdb.getCharacter_name_en();
			if(StringUtils.isNotBlank(imdbCharacter)){
				for(int i=0;i<pps.size();i++){
					PersonProgram pp = pps.get(0);
					if(imdbCharacter.equalsIgnoreCase(pp.getCharacter_name()) || imdbCharacter.equalsIgnoreCase(pp.getCharacter_name_en())){
						setUpdPersonProgram(imdb, pp);
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}

	private void setUpdPersonProgram(ImdbPersonVideo imdb, PersonProgram pp) {
		if(StringUtils.isBlank(pp.getCharacter_name_en())){
			pp.setCharacter_name_en(imdb.getCharacter_name_en());
			pp.setUpdDef();
			pp.update();
		}
		imdb.setMatch_state(Cons.THREAD_STATE_SUC);
		imdb.setMdm_id(pp.getId());
	}

	/**
	 * 人员节目关系配置
	 * @param list
	 */
	private String logPersonProgram(List<PersonProgram> list){
		StringBuilder sbuf = new StringBuilder();
		
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				PersonProgram obj = list.get(i);
				sbuf.append("   id="+obj.getId()+" profession="+obj.getProfession());
				if(StringUtils.isNotBlank(obj.getCharacter_name())){
					sbuf.append(" 角色名="+obj.getCharacter_name());
				}
				sbuf.append("\r\n");
			}
		}
		return sbuf.toString();
	}
	
	/**
	 * 日志记录
	 * @param lev
	 * @param type
	 * @param txt
	 */
	private void logDb(String lev,String type,String txt){
		ImdbLog obj = new ImdbLog();
		obj.setLev(lev);
		obj.setType(type);
		obj.setTxt(txt);
		obj.setAddDef();
		obj.save();
	}
	

	/**
	 * 人员配置
	 * @param list
	 */
	private String logPerson(List<Person> list){
		StringBuilder sbuf = new StringBuilder();
		
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Person obj = list.get(i);
				
				sbuf.append("   id="+obj.getId()+" name="+obj.getName());
				if(StringUtils.isNotBlank(obj.getName_en())){
					sbuf.append(" 英文名="+obj.getName_en());
				}
				if(StringUtils.isNotBlank(obj.getBorn_place())){
					sbuf.append(" 出生地="+obj.getBorn_place());
				}
				if(obj.getBirthday()!=null){
					sbuf.append(" 生日="+DateUtil.getDateSampleString(obj.getBirthday()));
				}
				if(StringUtils.isNotBlank(obj.getMtime_url())){
					sbuf.append(" 时光网url="+obj.getMtime_url());
				}
				sbuf.append("\r\n");
			}
		}
		return sbuf.toString();
	}	
}
