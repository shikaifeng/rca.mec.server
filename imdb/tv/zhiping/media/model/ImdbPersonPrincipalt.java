package tv.zhiping.media.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

import com.jfinal.plugin.activerecord.Db;

/**
 * @author 作者
 * @version 1.0
 * @since 2014-07-02
 */
@SuppressWarnings("serial")
public class ImdbPersonPrincipalt extends BasicModel<ImdbPersonPrincipalt> {

	public static final ImdbPersonPrincipalt dao = new ImdbPersonPrincipalt();
	
	/**
	 * 匹配失败的重新匹配
	 */
	public void backFail2Wait() {
		StringBuilder sql = new StringBuilder("update "+tableName+" set match_state=? where status=? and match_state=?");
		List<Object> params = new ArrayList<Object>();
		params.add(Cons.THREAD_STATE_WAIT);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_FAIL);
		
		Db.use(Cons.DB_NAME_MEDIA).update(sql.toString(),params.toArray());
	}
	

	public ImdbPersonPrincipalt queryByPidNameId(String pid, String name_id) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from "+ tableName+ " where status=? and pid=? and name_id=?";
		params.add(Cons.STATUS_VALID);
		params.add(pid);
		params.add(name_id);
		
		return findFirst(sql, params.toArray());
	}
	
	/**
	 * 待处理得到
	 * @return
	 */
	public List<ImdbPersonPrincipalt> queryByWaitMatch() {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select a.*,b.mdm_program_id as mdm_program_id");
		sql.append(" from "+tableName+" a,imdb_episode b where a.pid=b.id and a.status=? and b.status=? and a.match_state=? limit 0,1000");
		params.add(Cons.STATUS_VALID);
		params.add(Cons.STATUS_VALID);
		params.add(Cons.THREAD_STATE_WAIT);
		
		return find(sql.toString(), params.toArray());
	}
	
	public java.lang.String getPid() {
		return this.getStr("pid");
	}

	public ImdbPersonPrincipalt setPid(java.lang.String pid) {
		if (StringUtils.isNotBlank(pid)) {
			super.set("pid", pid);
		}
		return this;
	}

	public java.lang.String getName() {
		return this.getStr("name");
	}

	public ImdbPersonPrincipalt setName(java.lang.String name) {
		if (StringUtils.isNotBlank(name)) {
			super.set("name", name);
		}
		return this;
	}

	public java.lang.String getName_id() {
		return this.getStr("name_id");
	}

	public ImdbPersonPrincipalt setName_id(java.lang.String name_id) {
		if (StringUtils.isNotBlank(name_id)) {
			super.set("name_id", name_id);
		}
		return this;
	}
	
	public java.lang.Integer getSort_num(){
		return this.getInt("sort_num");
	}
	
	public ImdbPersonPrincipalt setSort_num(java.lang.Integer sort_num){
		if(sort_num!=null){
			super.set("sort_num",sort_num);
		}
		return this;
	}
	
	public java.lang.Integer getMatch_state(){
		return this.getInt("match_state");
	}
	
	public ImdbPersonPrincipalt setMatch_state(java.lang.Integer match_state){
		if(match_state!=null){
			super.set("match_state",match_state);
		}
		return this;
	}
	public java.lang.Long getMdm_id(){
		return this.getLong("mdm_id");
	}
	
	public ImdbPersonPrincipalt setMdm_id(java.lang.Long mdm_id){
		if(mdm_id != null){
			super.set("mdm_id",mdm_id);
		}
		return this;
	}
	
	public java.lang.String getJson_txt() {
		return this.getStr("json_txt");
	}

	public ImdbPersonPrincipalt setJson_txt(java.lang.String json_txt) {
		if (StringUtils.isNotBlank(json_txt)) {
			super.set("json_txt", json_txt);
		}
		return this;
	}
	
	public ImdbPersonPrincipalt setMsg(java.lang.String msg){
		if(StringUtils.isNotBlank(msg)){
			if(msg.length()>200){
				msg = msg.substring(0,200);
			}
			super.set("msg",msg);
		}
		return this;
	}
}