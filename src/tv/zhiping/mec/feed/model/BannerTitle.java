package tv.zhiping.mec.feed.model;

import java.util.ArrayList;
import java.util.List;
import com.jfinal.plugin.activerecord.Page;
import tv.zhiping.common.Cons;
import tv.zhiping.jfinal.BasicModel;

/**
 * @author banner的文案
 * @version 1.0
 * @since 2014-08-15
 */
@SuppressWarnings("serial")
public class BannerTitle extends BasicModel<BannerTitle> {

	public static final BannerTitle dao = new BannerTitle();

	/**分页查询所有banner_title*/
	public Page<BannerTitle> paginate(int pageNumber, int pageSize) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("from " + tableName
				+ " where status=?");
		params.add(Cons.STATUS_VALID);
		sql.append("  order by question_status");
		return paginate(pageNumber, pageSize, "select *", sql.toString(),
				params.toArray());
	}

	/**
	 * 查询所欲的banner的文案
	 * 
	 * @return
	 */
	public List<BannerTitle> queryByQuestion_status(Integer question_status) {
		List<Object> params = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder("select * from " + tableName
				+ " where status=?");
		params.add(Cons.STATUS_VALID);

		if (question_status != null) {
			sql.append(" and question_status=?");
			params.add(question_status);
		}

		return find(sql.toString(), params.toArray());
	}

	/**
	 * 查询所欲的banner的文案
	 * 
	 * @return
	 */
	public List<BannerTitle> queryByAll() {
		List<Object> params = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder("select * from " + tableName
				+ " where status=?");
		params.add(Cons.STATUS_VALID);

		return find(sql.toString(), params.toArray());
	}

	public java.lang.String getQuestion_status() {
		return this.get("question_status");
	}

	public BannerTitle getQuestion_status(java.lang.Integer question_status) {
		super.set("question_status", question_status);
		return this;
	}

	public java.lang.String getTitle() {
		return this.get("title");
	}

	public BannerTitle setTitle(java.lang.String title) {
		super.set("title", title);
		return this;
	}
}