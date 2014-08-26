package tv.zhiping.mec.feed.model;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;
import tv.zhiping.mdm.model.Episode;


/**
 * banner的图片
 * @author 张有良
 * @version 1.0
 * @since 2014-08-15
 */
@SuppressWarnings("serial")
public class BannerImg extends BasicModel<BannerImg> {
	public static final BannerImg dao = new BannerImg();
	
	public Page<BannerImg> paginate(int pageNumber, int pageSize) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("from " + tableName
				+ " where status=?");
		params.add(Cons.STATUS_VALID);
		sql.append("  order by lev");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),
				params.toArray());
	}
	
	/**
	 *  优先选择 feed -> 节目 -> 剧集 返回feed 
	 * @param program_id  选填
	 * @param episode_id  必填
	 * @param question_id 选填
	 * @return
	 */
	public BannerImg querySwitchPriorityByObj(Long program_id,Long episode_id,Long question_id) {
		BannerImg obj = null;
		
		//互动问答级别的banner
		if(question_id!=null){
			obj = queryByObj(null,episode_id,question_id,Cons.FEED_LEVEL);			
		}
		
		if(obj == null){
			if(episode_id != null){
				obj = queryByObj(null,episode_id,null,Cons.EPISODE_LEVEL);				
			}
		}
		
		if(obj == null){
			if(program_id == null){
				if(episode_id!=null){
					Episode episode = Episode.dao.findById(episode_id);
					if(episode!=null){
						program_id = episode.getPid();
					}
				}
			}
			
			if(program_id != null){
				obj = queryByObj(program_id,null,null,Cons.PROGRAM_LEVEL);
			}
		}
		
		if(obj == null){
			obj = queryByObj(null,null,null,Cons.SYS_LEVEL);				
		}
		return obj;
	}
	
	
	/**
	 * 查询对应的banner图片
	 * @return
	 */
	public BannerImg queryByObj(Long program_id,Long episode_id,Long question_id,Integer lev) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(episode_id !=null ){
			sql.append(" and episode_id=?");
			params.add(episode_id);
		}else if(program_id !=null ){
			sql.append(" and program_id=?");
			params.add(program_id);
		}
		
		if(lev!=null){
			sql.append(" and lev=?");
			params.add(lev);
		}
		
		return findFirst(sql.toString(),params.toArray());
	}
	
	/**
	 * 查询所欲的banner的文案
	 * @return
	 */
	public List<BannerImg> queryByAll() {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		return find(sql.toString(),params.toArray());
	}
	
	public java.lang.Long getProgram_id(){
		return this.get("program_id");
	}
	
	public BannerImg setProgram_id(java.lang.Long program_id){
		super.set("program_id",program_id);
		return this;
	}
	public java.lang.Long getEpisode_id(){
		return this.get("episode_id");
	}
	
	public BannerImg setEpisode_id(java.lang.Long episode_id){
		super.set("episode_id",episode_id);
		return this;
	}
	public java.lang.Long getQuestion_id(){
		return this.get("question_id");
	}
	
	public BannerImg setQuestion_id(java.lang.Long question_id){
		super.set("question_id",question_id);
		return this;
	}
	public java.lang.String getPath(){
		return this.get("path");
	}
	
	public BannerImg setPath(java.lang.String path){
		super.set("path",path);
		return this;
	}
	public java.lang.Long getLev(){
		return this.get("lev");
	}
	
	public BannerImg setLev(java.lang.Long lev){
		super.set("lev",lev);
		return this;
	}
}