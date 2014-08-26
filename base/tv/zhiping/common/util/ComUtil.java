package tv.zhiping.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.Cons;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Person;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mdm.model.Program.ProgramType;
import tv.zhiping.utils.DateUtil;
import tv.zhiping.utils.FileUtil;


public class ComUtil {
	
	//日期索引保存的路径
	public static String getDateIndexSavePath(String srcPath){
		return DateUtil.getFileDateIndex()+"/"+getRandomFileName(srcPath);
	}
		
	//文件上传获取本地的一个文件名
	public static String getRandomFileName(String srcPath){
		return getRandomFileFolder()+"."+FileUtil.getFileSuffix(srcPath);
	}
	
	public static String getRandomFileFolder(){
		return System.currentTimeMillis()+RandomStringUtils.randomNumeric(10);
	}
	
	/**
	 * 获取linux的路径表示方式
	 * @param path
	 * @return
	 */
	public static String getLinuxPath(String path){
		return path.replace("\\","/");
	}
	
	//根据数据库的路径，返回html5的完整路径
	public static String getApiHttpPath(String path){
		if(StringUtils.isNotBlank(path)){
    		if(isRemotePath(path)){
    			return path;
    		}else{
    			return CacheFun.getConVal(ConfigKeys.WEB_HTTP_PATH)+"/"+path;
    		}
    	}else{
    		return null;
    	}
    }
	
	//根据数据库的路径，返回html5的完整路径
	public static String getHuHttpPath(String path){
		if(StringUtils.isNotBlank(path)){
    		if(isRemotePath(path)){
    			return path;
    		}else{
    			return CacheFun.getConVal(ConfigKeys.HU_WEB_HTTP_URL)+path;
    		}
    	}else{
    		return null;
    	}
    }
		
	//根据数据库的路径，返回图片的完整路径
	public static String getStHttpPath(String path){
		if(StringUtils.isNotBlank(path)){
    		if(isRemotePath(path)){
    			return path;
    		}else{
    			return CacheFun.getConVal(ConfigKeys.ST_WEB_HTTP_URL)+path;
    		}
    	}else{
    		return null;
    	}
    }
	
	 //判断是否是远程路径
    public static boolean isRemotePath(String path){
    	if(path!=null && path.indexOf("http://")>-1){
    		return true;
    	}
    	return false;
    }

    /**
	 * 0:06:09 -> 00609.jpg
	 * @param str
	 * @return
	 */
	public static String getTimeFileName(String str){
		if(StringUtils.isNoneBlank(str) && str.length() == 7){
			return str.replace(":","").replaceFirst("：","");
		}
		return null;
	}
	
	//url 去除?
	public static String getHttpReqPath(String str) {
		int index = str.indexOf("?");
		if(index>-1){
			str = str.substring(0,index);
		}
		index = str.indexOf("#");
		if(index>-1){
			str = str.substring(0,index);
		}
		return str;
	}
	
	/**
	 * 01:01:01
	 * 时间转换
	 * @param str
	 * @return
	 */
	public static Long getTime(String str){
		Long time = null; 
		if(StringUtils.isNotBlank(str) && str.length() == 7){
			int hour = Integer.parseInt(str.substring(0,1));
			int minture = Integer.parseInt(str.substring(2,4));
			int secon = Integer.parseInt(str.substring(5,7));
			
			time = secon + minture*60L + hour*60*60;
		}
		return time;
	}
//	/**
//	 * 2014-08-06 22:09:28转为long
//	 * 时间转换
//	 * @param str
//	 * @return
//	 * @throws ParseException 
//	 */
//	public static Long getYMRtoHmsa(String str) throws ParseException{
//		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date = sdf.parse(str);
//		if(date!=null){
//			return date.getTime();
//		}
//		return null;
////		System.out.println("len="+str.length());
////		if(StringUtils.isNotBlank(str) && str.length() == 19){
////			String[] strs = str.split(" ");
////			Long hms = ComUtil.getTime(strs[1]);
////			int year = Integer.parseInt(strs[0].substring(0,4));
////			int month = Integer.parseInt(strs[0].substring(5,7));
////			int day = Integer.parseInt(strs[0].substring(8,10));
////			System.out.println("**"+year);
////			System.out.println("**"+month);
////			System.out.println("**"+day);
////		}
//	}

	/**
	 * 根据节目类型，显示不同的标题
	 * @param episode
	 * @param program
	 * @return
	 */
	public static String getEpisodeAppTitle(Episode episode, Program program) {
		String title = program.getTitle();
		if(episode!=null){
			if(ProgramType.Variety.toString().equals(program.getType())){//综艺  中国梦想秀 20140509
				if(StringUtils.isNoneBlank(episode.getSeries())){
					title = title +" "+episode.getSeries().replace("-","");
				}
			}else if(ProgramType.TV.toString().equals(program.getType())){//电视剧 
				Long season = program.getCurrent_season();
				if(season!=null && !Cons.DEF_NULL_NUMBER.equals(season)){//有剧集
					title = title + " 第"+season+"季"; 	
				}
				title = title + " 第"+episode.getCurrent_episode()+"集" ;//例如：金玉良缘 第1集
				
			}			
		}
		return title;
	}
	
	/**
	 * 根据节目类型，显示不同的标题
	 * @param episode
	 * @param program
	 * @return
	 */
	public static String getEpisodeCover(Episode episode, Program program) {
		String cover = null;
		if(episode!=null){
			cover = episode.getCover();
		}
		cover = StringUtils.isNoneBlank(cover) ? cover : program.getView_cover();
		return cover;
	}
	
	/**
	 * 生成明星的头像地址
	 * 明星 person : images/person/用户id/avatar/数字.jpg
	 * @param person
	 * @param srcUrlPath 源目标头像
	 * @return
	 */
	
	public static String getPersonAvatarPath(Person person,String srcUrlPath) {
		person.setAvatar("images/person/"+person.getId()+"/avatar/"+getRandomFileName(srcUrlPath));
		return person.getAvatar();
	}
	
	/**
	 * 生成节目的海报地址
	 * images/program/节目的年份/节目的id/cover/数字.jpg
	 * @param program
	 * @param srcUrlPath
	 * @return
	 */
	public static String getProgramCoverPath(Program program,String srcUrlPath) {
		return "images/program/"+program.getYear()+"/"+program.getId()+"/cover/"+getRandomFileName(srcUrlPath);
	}
	
	
	/**
	 * 生成剧集的海报地址
	 * images/program/节目的年份/节目的id/cover/数字.jpg
	 * @param program
	 * @param srcUrlPath
	 * @return
	 */
	public static String getEpisodeCoverPath(Program program,Episode episode,String srcUrlPath) {
		return "images/program/"+program.getYear()+"/"+program.getId()+"/cover/"+getRandomFileName(srcUrlPath);
	}
	
	/**
	 * 时间格式化: 3723 转换成1:02:03
	 * @param episode
	 * @param program
	 * @return
	 */
	public static String secondFormate(Long second) {
		String t = null;
		if(second!=null){
			if(second > -1){
				int hour = (int) (second/3600);
				int min = (int) (second/60 % 60);
				int sec = (int) (second % 60);
				int day = hour/24;
				if (day > 0) {
					hour = hour - 24 * day;
					t = day + "day " + hour + ":";
				}else{
					t = hour + ":";   
				}
				if(min < 10){
					t += "0";
				}
				t += min + ":";
				if(sec < 10){
					t += "0";
				}
				t += sec;
			}
		}
		return t;
	}

	/**
	 * 替换成html的换行
	 * @param str
	 * @return
	 */
	public static Object getHtmlBrStr(String str) {
		if(StringUtils.isNotBlank(str)){
			return str.replace("\n","<br \\>");
		}
		return null;
	}
}