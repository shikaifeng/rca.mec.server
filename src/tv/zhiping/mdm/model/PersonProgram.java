package tv.zhiping.mdm.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;


/**
 * 媒资库中人物节目表
 * @author 樊亚容
 * @since 2014-05-16
 */
@SuppressWarnings("serial")
public class PersonProgram extends BasicModel<PersonProgram> {

	public static final PersonProgram dao = new PersonProgram();
	
	/**
	 * 查询某明星所参演的所有节目id
	 */
	public PersonProgram queryCountByPersonId(Long person_id){
		List<Object> params = new ArrayList<Object>();
		params.add(person_id);
		params.add(Cons.STATUS_VALID);
		String sql = "select count(distinct program_id) as count from person_program where person_id=? and status=?";
		return findFirst(sql,params.toArray());
	}
	
	
	/**
	 * 查询某明星所参演的所有节目id
	 */
	public PersonProgram queryCountByProgramId(Long program_id){
		List<Object> params = new ArrayList<Object>();
		params.add(program_id);
		params.add(Cons.STATUS_VALID);
		String sql = "select count(distinct person_id) as count from person_program where program_id=? and status=?";
		return findFirst(sql,params.toArray());
	}
	
	/**
	 * 查询某明星所参演的所有节目id
	 */
	public LinkedList<Long> queryPersonProgram(java.lang.Long person_id){
		List<Object> params = new ArrayList<Object>();
		params.add(person_id);
		params.add(Cons.STATUS_VALID);
		String sql = "select program_id from person_program where person_id=? and status=?";
		List<PersonProgram> l1 = find(sql, params.toArray());		
		LinkedList<Long> result = new LinkedList<Long>();
		for(PersonProgram a : l1){
			result.add(a.getProgram_id());
		}
		return result;
	}
	
	/**
	 * 根据角色名称，用户id，节目id
	 */
	public List<PersonProgram> queryPersonProgramId(java.lang.Long person_id,Long program_id,String profession){
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select * from person_program where status=? and person_id=? and program_id=?");
		params.add(Cons.STATUS_VALID);
		params.add(person_id);
		params.add(program_id);
		
		if(StringUtils.isNoneBlank(profession)){
			sql.append(" and profession=?");
			params.add(profession);
		}
		return find(sql.toString(),params.toArray());
	}

	public java.lang.Long getPerson_id(){
		return this.getLong("person_id");
	}
	
	public PersonProgram setPerson_id(java.lang.Long person_id){
		super.set("person_id",person_id);
		return this;
	}
	public java.lang.Long getProgram_id(){
		return this.getLong("program_id");
	}
	
	public PersonProgram setProgram_id(java.lang.Long program_id){
		super.set("program_id",program_id);
		return this;
	}
	public java.lang.String getProfession(){
		return this.getStr("profession");
	}
	
	public PersonProgram setProfession(java.lang.String profession){
		super.set("profession",profession);
		return this;
	}
	public java.lang.String getCharacter_name(){
		return this.getStr("character_name");
	}
	
	public PersonProgram setCharacter_name(java.lang.String character_name){
		super.set("character_name",character_name);
		return this;
	}
	public java.lang.String getCharacter_name_en(){
		return this.getStr("character_name_en");
	}
	
	public PersonProgram setCharacter_name_en(java.lang.String character_name_en){
		super.set("character_name_en",character_name_en);
		return this;
	}
	public java.lang.String getCharacter_avatar(){
		return this.getStr("character_avatar");
	}
	
	public PersonProgram setCharacter_avatar(java.lang.String character_avatar){
		super.set("character_avatar",character_avatar);
		return this;
	}
	public java.lang.Boolean getIs_primary(){
		return this.getBoolean("is_primary");
	}
	
	public PersonProgram setIs_primary(java.lang.Boolean is_primary){
		super.set("is_primary",is_primary);
		return this;
	}
	public java.lang.String getCharacter_desc(){
		return this.getStr("character_desc");
	}
	
	public PersonProgram setCharacter_desc(java.lang.String character_desc){
		super.set("character_desc",character_desc);
		
		return this;
	}
	public java.lang.String getCharacter_avatar_mtime(){
		return this.getStr("character_avatar_mtime");
	}
	
	public PersonProgram setCharacter_avatar_mtime(java.lang.String character_avatar_mtime){
		super.set("character_avatar_mtime",character_avatar_mtime);
		return this;
	}

}