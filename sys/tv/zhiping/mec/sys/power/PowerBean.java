package tv.zhiping.mec.sys.power;

import java.io.Serializable;

public class PowerBean implements Serializable {
	private Long userId;//人员编号
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
