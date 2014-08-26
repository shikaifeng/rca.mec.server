package tv.zhiping.mec.api.feed.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mec.luck.model.LuckyDrawBefore;
import tv.zhiping.mec.luck.model.LuckyDrawEvent;
import tv.zhiping.mec.luck.model.LuckyDrawHistory;
import tv.zhiping.mec.luck.model.LuckyDrawOption;
import tv.zhiping.utils.RandomUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * 抽奖的业务逻辑
 * @author liang
 *
 */
public class LuckyService {
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 抽奖的记录
	 * @param udid
	 * @param event
	 * @param data
	 */
	public void excuteAddHistory(String udid,LuckyDrawEvent event,JSONObject data){
		event.addSeq();
		
		boolean isluck = Cons.FALSE;
		Long id = event.getId();
		Long seq = event.getSeq(); 
		
		LuckyDrawHistory history = new LuckyDrawHistory();
		history.setUdid(udid);
		history.setLucky_id(id);
		history.setSeq(seq);
		history.setAddDef();
		history.save();
		
		LuckyDrawOption option = null;
		data.put("isluck",isluck);
		List<LuckyDrawOption> options = LuckyDrawOption.dao.queryByLuckyId(id,Cons.NO);
		if(options!=null && !options.isEmpty()){
			int size = RandomUtil.getRandomInt(options.size());
			option = options.get(size);	
		}
		
		if(option!=null){
			data.put("position",option.getPosition());
		}
	}
	
	/**
	 * 抽奖的记录
	 * @param udid
	 * @param event
	 * @param data
	 */
	public void excuteLucky(String udid,LuckyDrawEvent event,JSONObject data){
		event.addWin_seq();
		
		boolean isluck = Cons.FALSE;
		Long id = event.getId(); 
		
		LuckyDrawHistory history = new LuckyDrawHistory();
		history.setUdid(udid);
		history.setLucky_id(id);
		history.setSeq(event.getSeq());
		history.setAddDef();
		
		LuckyDrawBefore lucky = LuckyDrawBefore.dao.queryByLuckyIdSeq(id,event.getWin_seq());
		LuckyDrawOption option = null;
		if(lucky!=null){//中奖了
			history.setOption_id(lucky.getOption_id());
			history.setStatus(LuckyDrawBefore.WIND);
			
			lucky.setStatus(LuckyDrawBefore.WIND);
			lucky.setUpdDef();
			lucky.update();
			
			option = LuckyDrawOption.dao.findById(lucky.getOption_id());
			if(option!=null){
				Integer surplus_prize_count = option.getSurplus_prize_count();
				if(surplus_prize_count!=null && surplus_prize_count>0){
					option.setSurplus_prize_count(surplus_prize_count - 1);
					option.setUpdDef();
					option.update();
					isluck = Cons.TRUE;					
				}
			}
		}
						
		history.save();
		
		data.put("isluck",isluck);
		if(isluck){//中奖了
			data.put("url",getLucky_asy_notify(option.getUrl(),history.getId()));
		}else{
			List<LuckyDrawOption> options = LuckyDrawOption.dao.queryByLuckyId(id,Cons.NO);
			if(options!=null && !options.isEmpty()){
				int size = RandomUtil.getRandomInt(options.size());
				option = options.get(size);	
			}
		}
		
		if(option!=null){
			data.put("position",option.getPosition());
		}
	}
	
	//拼凑抽奖回调url
	public String getLucky_asy_notify(String url,Long id){
		StringBuilder str = new StringBuilder(url);
		if(StringUtils.isNotBlank(url)){
			if(url.indexOf("?")>-1){
				str.append("&");
			}else{
				str.append("?");	
			}
			str.append("success_callback_url=");
			try {
				str.append(URLEncoder.encode(ComUtil.getApiHttpPath("api/v1/lucky_asy_notify/"+id),Cons.CHARACTER));
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(),e);
			}
		}
		return str.toString();
	}
}
