package tv.zhiping.mec.feed.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;


/**
 * @author 作者
 * @version 1.0
 * @since 2014-07-16
 */
@SuppressWarnings("serial")
public class QuestionOption extends BasicModel<QuestionOption> {

	public static final QuestionOption dao = new QuestionOption();

	/**
	 * 得到这个互动下的所有问题
	 * @param question_id
	 * @return
	 */
	public List<QuestionOption> queryByQuestionId(Long question_id) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("select * from "+tableName+" where status=?");
		params.add(Cons.STATUS_VALID);
		
		if(question_id!=null){
			sql.append(" and question_id=?");
			params.add(question_id);
		}
		
		
		sql.append(" order by id");
		return find(sql.toString(),params.toArray());
	}
	
	
	public java.lang.Long getQuestion_id(){
		return this.getLong("question_id");
	}
	
	public QuestionOption setQuestion_id(java.lang.Long question_id){
		super.set("question_id",question_id);
		return this;
	}
	public java.lang.String getTitle(){
		return this.getStr("title");
	}
	
	public QuestionOption setTitle(java.lang.String title){
		super.set("title",title);
		return this;
	}
}