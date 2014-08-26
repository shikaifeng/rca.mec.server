package tv.zhiping.mec.api.jfinal;

import tv.zhiping.mec.api.common.ApiCons;

public class ApiRes {
	private Integer status = ApiCons.STATUS_ERROR;
	private String msg;
	
	private Object data;

	private ApiScore score;
	
	private ApiPage page;
	
	public void setScore(Integer oldCount,Integer newCount){
		this.score = new ApiScore();
		this.score.setOldCount(oldCount);
		this.score.setNewCount(newCount);
	}
	
	public void setPage(Integer page_num,Integer page_size,Integer total_page){
		this.page  = new ApiPage();
		this.page.setPage_num(page_num);
		this.page.setPage_size(page_size);
		this.page.setTotal_page(total_page);
	}
	
	
	public ApiPage getPage() {
		return page;
	}

	public void setPage(ApiPage page) {
		this.page = page;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public static class ApiPage{
		private Integer page_num;
		private Integer page_size;
		private Integer total_page;
		public Integer getPage_num() {
			return page_num;
		}
		public void setPage_num(Integer page_num) {
			this.page_num = page_num;
		}
		public Integer getPage_size() {
			return page_size;
		}
		public void setPage_size(Integer page_size) {
			this.page_size = page_size;
		}
		public Integer getTotal_page() {
			return total_page;
		}
		public void setTotal_page(Integer total_page) {
			this.total_page = total_page;
		}
	}
	
	public static class ApiScore{
		private Integer oldCount;
		private Integer newCount;
		public Integer getOldCount() {
			return oldCount;
		}
		public void setOldCount(Integer oldCount) {
			this.oldCount = oldCount;
		}
		public Integer getNewCount() {
			return newCount;
		}
		public void setNewCount(Integer newCount) {
			this.newCount = newCount;
		}
	}
}
