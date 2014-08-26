package tv.zhiping.acr.bean;

import java.util.List;

public class AcrReponse {
	public final static Integer SUC= 100;
	private Integer status;
	private String msg;
	
	private AcrResponseData data;
	
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

	public AcrResponseData getData() {
		return data;
	}

	public void setData(AcrResponseData data) {
		this.data = data;
	}

	public static class AcrResponseData{
		private List<AcrNonlive> nonlive;
		private List<AcrLive> live;
		public List<AcrNonlive> getNonlive() {
			return nonlive;
		}
		public void setNonlive(List<AcrNonlive> nonlive) {
			this.nonlive = nonlive;
		}
		public List<AcrLive> getLive() {
			return live;
		}
		public void setLive(List<AcrLive> live) {
			this.live = live;
		}
	}
	
	/**
	 * 点播信息
	 * @author liang
	 */
	public static class AcrNonlive{
		private String meta_uuid;
		private Long meta_customer_id;
		private Long hit_count;
		private String meta_title;
		private Long master_offset;
		private Long master_end;
		private Long hit_duration;
		
		
		public Long getMaster_end() {
			return master_end;
		}
		public void setMaster_end(Long master_end) {
			this.master_end = master_end;
		}
		public Long getHit_count() {
			return hit_count;
		}
		public void setHit_count(Long hit_count) {
			this.hit_count = hit_count;
		}
		public Long getHit_duration() {
			return hit_duration;
		}
		public void setHit_duration(Long hit_duration) {
			this.hit_duration = hit_duration;
		}
		public String getMeta_uuid() {
			return meta_uuid;
		}
		public void setMeta_uuid(String meta_uuid) {
			this.meta_uuid = meta_uuid;
		}
		public Long getMeta_customer_id() {
			return meta_customer_id;
		}
		public void setMeta_customer_id(Long meta_customer_id) {
			this.meta_customer_id = meta_customer_id;
		}
		public String getMeta_title() {
			return meta_title;
		}
		public void setMeta_title(String meta_title) {
			this.meta_title = meta_title;
		}
		public Long getMaster_offset() {
			return master_offset;
		}
		public void setMaster_offset(Long master_offset) {
			this.master_offset = master_offset;
		}
	}
	
	/**
	 * 直播信息
	 * @author liang
	 *
	 */
	public static class AcrLive{
		private Long channel_id;
		private String channel_name;
		
		public Long getChannel_id() {
			return channel_id;
		}
		public void setChannel_id(Long channel_id) {
			this.channel_id = channel_id;
		}
		public String getChannel_name() {
			return channel_name;
		}
		public void setChannel_name(String channel_name) {
			this.channel_name = channel_name;
		}
	}
}
