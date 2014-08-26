package tv.zhiping.mec.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import tv.zhiping.common.Cons;
import tv.zhiping.mec.luck.model.LuckyDrawBefore;
import tv.zhiping.mec.luck.model.LuckyDrawEvent;
import tv.zhiping.mec.luck.model.LuckyDrawOption;
import tv.zhiping.mec.user.model.UserQuestion;
import tv.zhiping.utils.RandomUtil;

public class ArithLuckDrawService {
	private Logger log = Logger.getLogger(this.getClass());
	
	public static final ArithLuckDrawService service = new ArithLuckDrawService();
	
    /**
     * 计算中奖预估人数
     * 参考：
     * 算法实现
     * @param event
     * @throws Exception 
     */
    public void processLuckyDraw(LuckyDrawEvent event) throws Exception {
    	Long lucky_id = event.getId();
    	
    	//预估的剩余奖项
    	List<Long> surplus_prizes = new ArrayList<Long>();
    	//预估抽奖人数
    	Long before_person_count = getSupplusPrizes(event.getId(),surplus_prizes);
    	//计算中奖顺序
    	if(!surplus_prizes.isEmpty()){//还有剩余奖品
    		before_person_count = switchBeforePersonCount(event,before_person_count);
    		
    		List<LuckyDrawBefore> luckys = new ArrayList<LuckyDrawBefore>();
    		
    		Set<Integer> win_set = new HashSet<Integer>();
    		Integer win_seq = 0;
    		int sum_count = 0;
    		for(int i=0;i<surplus_prizes.size();i++){
    			Long option_id = surplus_prizes.get(i);
    			while(true){
    				win_seq = RandomUtil.getRandomInt(before_person_count.intValue()+1);
    				
    				if(!win_seq.equals(Cons.DEF_NULL_INT) && !win_set.contains(win_seq)){
    					win_set.add(win_seq);
    					
    					LuckyDrawBefore obj = new LuckyDrawBefore();
    					obj.setLucky_id(lucky_id);
    					obj.setOption_id(option_id);
    					obj.setSeq(new Long(win_seq));
    					
    					luckys.add(obj);
    					break;
    				}
    				sum_count++;
    				if(sum_count>10000){
    					throw new Exception("arith luck draw result count>10000");
    				}
    			}
    		}
    		
    		LuckyDrawBefore.dao.updateBeforeInvalid(event.getId());
    		event.setWin_seq(0L);
    		event.setUpdDef();
    		event.update();
    		for(int i=0;i<luckys.size();i++){
    			LuckyDrawBefore obj = luckys.get(i);
    			obj.setAddDef();
    			obj.save();
    		}
    	}else{
    		log.error("奖品数已经为空");
    	}
	}

    /**
     * 根据预估人数和参与人数比较 求出最大的人数
     * @param event
     * @param before_person_count 根据概率计算的预估人数
     * @return
     */
	private Long switchBeforePersonCount(LuckyDrawEvent event,Long before_person_count) {
		//判断参与活动的人数正确人数
    	Long user_question_count = UserQuestion.dao.queryCountByLucIdUdid(event.getId(),null,event.getStart_time(),event.getEnd_time());
    	if(user_question_count == null){
    		user_question_count = 0L;
    	}
    	
    	//预估人数和实际人数比较,取大值
    	before_person_count = before_person_count > user_question_count ? before_person_count : user_question_count;
		return before_person_count;
	}

    /**
     * 获取剩余的奖项信息
     * @param surplus_prizes
     * @return
     */
	private Long getSupplusPrizes(Long lucky_id,List<Long> surplus_prizes) {
		Long before_person_count = 0L;
		List<LuckyDrawOption> opts = LuckyDrawOption.dao.queryByLuckyId(lucky_id,Cons.YES);
    	if(opts!=null && !opts.isEmpty()){
			for(int i=0;i<opts.size();i++){
				LuckyDrawOption obj = opts.get(i);
				
				Integer surplus_prize_count = obj.getSurplus_prize_count();
				if(StringUtils.isNotBlank(obj.getWin_rate()) && surplus_prize_count!=null && surplus_prize_count > 0){//大于0
					String[] srr = obj.getWin_rate().split("/");
					Double numerator = Double.parseDouble(srr[0]);//分子 奖品数
					Double denominator = Double.parseDouble(srr[1]);//分母 预估人数
					Integer opt_person_count = new Double(surplus_prize_count * denominator / numerator).intValue();
					
					//预估人数
					//before_person_count += opt_person_count;
					before_person_count = before_person_count > opt_person_count ? before_person_count : opt_person_count;
					
					//剩余奖品数
					for(int j=0;j<obj.getSurplus_prize_count();j++){
						surplus_prizes.add(obj.getId());
					}	
				}
			}
		}
		return before_person_count;
	}
    
    public static void main(String[] args) {
		String str = "30/10000";
//		numerator/denominator
		Long surplus_prize_count = 10L;
		String[] srr = str.split("/");
		Double numerator = Double.parseDouble(srr[0]);
		Double denominator = Double.parseDouble(srr[1]);
		
		System.out.println(numerator);
		System.out.println(denominator);
		
//		System.out.println(ArithUtils.div(surplus_prize_count,ArithUtils.div(numerator,denominator)));
		Integer max = new Double(surplus_prize_count * denominator / numerator).intValue();
		
		System.out.println(max);
		System.out.println();
		
		int surplus_all_prize_count = 10;
		
		for(int i=0;i<surplus_all_prize_count;i++){
			System.out.println(i+": "+RandomUtil.getRandomInt(max));
		}
//		surplus_prize_count * denominator / numerator
	}

	/**
     * 快到抽奖时间的活动
     * @return
     */
    public List<LuckyDrawEvent> getLastStartLuckyDraw(){
    	LuckyDrawEvent event = LuckyDrawEvent.dao.findById(1L);
    	
    	List list = new ArrayList();
    	list.add(event);
    	
    	return list;
    }
}
