package tv.zhiping.mec.feed.ctrl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jxl.read.biff.BiffException;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.Person;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mdm.model.Song;
import tv.zhiping.mec.feed.model.Baike;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Element.ElementType;
import tv.zhiping.mec.feed.model.Music;
import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.mec.util.model.FfmpegHistory;
import tv.zhiping.utils.DateUtil;
import tv.zhiping.utils.FileUtil;
import tv.zhiping.utils.XlsUtil;

import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

/**
 * excel导入
 * @author 张有良
 */

public class FeedXlsCtrl extends FeedBaseCtrl{	
	
	/**
	 * excel导入 index
	 * */
	public void index(){
		renderJsp("/page/feed/feedXls/input.jsp");
	}
	
	
	public void save() {
		String str = "搞定";
		UploadFile uploadFile = getFile("upload");//Cons.UPLOAD_TMP
		
		Long program_id = getParaToLong("program_id");
		Long episode_id = getParaToLong("episode_id");
//		String file_path = getPara("file_path").replace("\\","/");
//		File file = new File(file_path);
		
		if(program_id==null || episode_id==null){
			str = "节目剧集文件不能为空";
		}else{
			if(uploadFile!=null){
				File file = uploadFile.getFile();
				try {
					String fileSuffix =FileUtil.getFileSuffix(uploadFile.getOriginalFileName());
					if("xls".equalsIgnoreCase(fileSuffix)){//xls文件
						Program program = Program.dao.findById(program_id);
						if(program!=null){
							List<SceneWarp> list = new ArrayList<SceneWarp>();
							StringBuilder ffmpeg = new StringBuilder();
							
							getData(file,program,episode_id,list,ffmpeg);
							delByEpisodeId(episode_id);
							save(list);
							str = ffmpeg.toString();
						}else{
							str = "节目id不存在";
						}
					}
				} catch (Exception e) {
					log.error(e.getMessage(),e);
					str = e.getMessage()+" 未知错误，请联系管理员";
				}finally{//释放上传的内容
					file.delete();
				}
			}
			
		}
		log.info("导入返回:"+str);
		renderText(str);
	}
	
	//偏移图片
	public static int IMG_SKIP_SENCOND = 0;
		
	
	
	public static void main(String[] args) {
		Set<String> md_str = new HashSet<String>();
		md_str.add("1");
		md_str.add("2");
		
		System.out.println(md_str.toString());
	}
	
	
	/**
	 * 人员配置
	 * @param list
	 */
	private String logPerson(List<Person> list){
		StringBuilder sbuf = new StringBuilder();
		
		if(list!=null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Person obj = list.get(i);
				
				sbuf.append("   id="+obj.getId()+" name="+obj.getName());
				if(StringUtils.isNotBlank(obj.getName_en())){
					sbuf.append(" 英文名="+obj.getName_en());
				}
				if(StringUtils.isNotBlank(obj.getBorn_place())){
					sbuf.append(" 出生地="+obj.getBorn_place());
				}
				if(obj.getBirthday()!=null){
					sbuf.append(" 生日="+DateUtil.getDateSampleString(obj.getBirthday()));
				}
				if(StringUtils.isNotBlank(obj.getMtime_url())){
					sbuf.append(" 时光网url="+obj.getMtime_url());
				}
				sbuf.append("\r\n");
			}
		}
		return sbuf.toString();
	}
	
	
	/**
	 * 返回解析后的数据
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public void getData(File file,Program program,Long episode_id,List<SceneWarp> result,StringBuilder ffmpeg) throws Exception {
		List<String[]> list = XlsUtil.getXlsData(file);
		
		list.remove(0);
		list.remove(0);
		list.remove(0);
		
		Long program_id = program.getId();
		
		Scene scene = null;
		String[] srr = null;
		SceneWarp sceneWarp = null;
		
//		StringBuilder md_str = new StringBuilder();
		Set<String> md_str = new HashSet<String>();
		
//		StringBuilder cover_ffmpeg = new StringBuilder();
		Set<String> cover_ffmpeg = new HashSet<String>();
		
//		StringBuilder video_ffmpeg = new StringBuilder();
		Set<String> video_ffmpeg = new HashSet<String>();
		
		Set<String> error_msg = new HashSet<String>();
		
		
		
		String partnerPath = PathKit.getWebRootPath()+"/upload";
		
		String scene_start_time;//0 场景开始时间
		String scene_end_time;//1 场景截止时间
		String scene_copy;//2 场景文案
		
		String person_start_time;//3 明星开始时间
		String person_name;//4 演员名称
		String person_charact;//5 演员角色
		String person_title = null;//6 演员备注标题  无
		
		String video_start_time;//6视频开始时间
		String video_end_time;//7视频截止时间
		String video_title;//8 视频标题
		String video_copy;//9 视频文案
		
		String music_type;//10 歌曲类型
		String music_name;//11 歌曲名称
		String music_start_time;//12歌曲开始时间
		String music_singer;//13歌曲歌手
		String music_pic;//14歌曲图片
		String music_url;//15歌曲url
		
		String baike_title;//16 百科名称
		String baike_url;//17百科url
		String baike_start_time;//18百科开始时间
		String baike_note;//19百科简介
		String baike_pic;//20百科图片
		
		for(int i=0;i<list.size();i++){
			srr = list.get(i);
			
			scene_start_time = srr[0];//0 场景开始时间
			scene_end_time = srr[1];//1 场景截止时间
			scene_copy = srr[2];//2 场景文案

			person_start_time = srr[3];//3 明星开始时间
			person_name = srr[4];//4 演员名称
			person_charact = srr[5];//5 演员角色
//			person_title = srr[6];//6 演员备注标题
			person_title = null;

			video_start_time = srr[6];//6视频开始时间
			video_end_time = srr[7];//7视频截止时间
			video_title = srr[8];//8 视频标题
			video_copy = srr[9];//9 视频文案

			music_type = srr[10];//10 歌曲类型
			music_name = srr[11];//11 歌曲名称
			music_start_time = srr[12];//12歌曲开始时间
			music_singer = srr[13];//13歌曲歌手
			music_pic = srr[14];//14歌曲图片
			music_url = srr[15];//15歌曲url

			baike_title = srr[16];//16 百科名称
			baike_url = srr[17];//17百科url
			baike_start_time = srr[18];//18百科开始时间
			baike_note = srr[19];//19百科简介
			baike_pic = srr[20];//20百科图片
			
			printStrArr(srr);
			
			if(StringUtils.isNoneBlank(scene_start_time)){
				scene = new Scene();
				scene.setStart_time(ComUtil.getTime(scene_start_time));
				
				scene.setPid(episode_id);
				
				sceneAddImgSkipSecond(program, episode_id, scene);
				
				printTvImg(episode_id, scene, cover_ffmpeg);
				
				md_str.add("md "+FileUtil.getFileParentPath(scene.getCover()).replace("/","\\"));
				
				scene.setTitle(scene_copy);
				scene.setSummary(scene_copy);
				scene.setEnd_time(ComUtil.getTime(scene_end_time));
				scene.setDuration(scene.getEnd_time() - scene.getStart_time());
				
				sceneWarp = new SceneWarp();
				sceneWarp.setScene(scene);;
				result.add(sceneWarp);
			}
			
			//明星信息
			if(srr.length > 3 && StringUtils.isNoneBlank(person_start_time)){
				try {
					Element element = new Element();
					setElementProgram(program_id, episode_id, element);
					
					element.setType(ElementType.person.toString());
					element.setTitle(person_title);
					element.setStart_time(ComUtil.getTime(person_start_time));
					
					elementAddImgSkipSecond(program, episode_id, program_id,
							md_str, element);
					
					element.setTag(person_charact);
					
					printTvImg(episode_id, cover_ffmpeg, element);
					
					List<Person> personList = Person.dao.queryByProgramIdName(program_id,person_name);
					if(personList!=null && !personList.isEmpty()){
						
						String str = logPerson(personList);
						if(personList.size()>1){
							throw new Exception(person_name+" 匹配到的明星有多个 分别是:"+str);	
						}
						Person person = personList.get(0);
						element.setFid(person.getId());//明星的id
					}else{
						personList = Person.dao.queryByName(person_name);
						if(personList!=null && !personList.isEmpty()){
							String str = logPerson(personList);
							throw new Exception(person_name+" 明星有多个,请做好和节目的关联，分别是："+str);	
						}else{
							throw new Exception(person_name+" 查询不到明星,请补充以及做好关联");
						}
					}
					
					//c1:字段表示出演角色 c2:剧中角色描述
//					ElementAddon addon = new ElementAddon();
//					addon.setC1(person_charact);
//					addon.setC2(person_title);
					
					sceneWarp.addElement(element);
				} catch (Exception e) {
					e.printStackTrace();
					error_msg.add(e.getMessage());
				}
			}
			
			if(srr.length > 7 && StringUtils.isNoneBlank(video_start_time)){//经典片段 视频
				Element element = new Element();
				setElementProgram(program_id, episode_id, element);
				
				element.setType(ElementType.video.toString());
				element.setStart_time(ComUtil.getTime(video_start_time));
				element.setEnd_time(ComUtil.getTime(video_end_time));
				
				elementAddImgSkipSecond(program, episode_id, program_id,
						md_str, element);
				
				printTvImg(episode_id, cover_ffmpeg, element);
				
				element.setTitle(video_title);
				
				// c1: 文件类型：mp3,3gp c2: 本地保存路径 t1: 为台词字段
//				ElementAddon addon = new ElementAddon();
//				addon = new ElementAddon();
//				addon.setC1("mp4");
//				addon.setC2("videos/element/"+program.getYear()+"/"+program_id+"/"+episode_id+"/"+element.getStart_time()+".mp4");//视频保存路径
				
				element.setUrl("videos/element/"+program.getYear()+"/"+program_id+"/"+episode_id+"/"+element.getStart_time()+".mp4");
				md_str.add("md "+FileUtil.getFileParentPath(element.getUrl()).replace("/","\\"));
				
				printTvVideo(episode_id, video_ffmpeg, element);
//				addon.setT1(video_copy);
				
				element.setContent(video_copy);
				sceneWarp.addElement(element);
			}
			
			if(srr.length > 11 && StringUtils.isNoneBlank(music_type)){//歌曲
				Element element = new Element();
				setElementProgram(program_id, episode_id, element);
				element.setType(ElementType.music.toString());
				
				element.setCover("images/music/"+ComUtil.getDateIndexSavePath(music_pic));///////////////////////////////////下载
				String downPath = down(music_pic,partnerPath,element.getCover(),"music_cover");
				element.setCover(downPath);
				
				element.setTitle(music_name);///////只是记录,还是用music的记录
				element.setStart_time(ComUtil.getTime(music_start_time));
				element.setTag(music_type);
				
//				element.setEnd_time(ComUtil.getTime(srr[14]));
//				music_url = ComUtil.getHttpReqPath(music_url);
				
				Music music = Music.dao.queryBySourceUrl(music_url);
				if(music == null){
					music = new Music();
				}
				
				Song song = Song.dao.queryByIdOrSourceUrl(null,music_url);
				if(song != null){
					music.setSong_id(song.getId());
				}
				
				music.setTitle(element.getTitle());
				music.setCover(element.getCover());
				music.setSinger(music_singer);
				music.setSource_url(music_url);
				music.setTag(music_type);
				
				if(music.getId() == null){
					music.setAddDef();
					music.save();					
				}else{
					music.setUpdDef();
					music.update();	
				}
				
				element.setFid(music.getId());
				
				//c1: 文件类型：mp3,3gp c2: 本地保存路径 c3: 歌曲类型
//				ElementAddon addon = new ElementAddon();
//				addon.setC1("mp4");
//				addon.setC2("medio/"+program_id+"/"+episode_id+"/"+ComUtil.getTimeFileName(srr[12])+".mp4");//mp3保存路径
//				addon.setC3(music_type);
//				sceneWarp.addElement(element, addon);
			}
			
			if(srr.length > 17 && StringUtils.isNoneBlank(baike_title)){//百科的地址
				Element element = new Element();
				setElementProgram(program_id, episode_id, element);				
				element.setType(ElementType.baike.toString());
				
				element.setCover("images/baike/"+ComUtil.getDateIndexSavePath(baike_pic));
				String downPath = down(baike_pic,partnerPath,element.getCover(),"baike_cover");
				element.setCover(downPath);
				
				element.setTitle(baike_title);
				element.setStart_time(ComUtil.getTime(baike_start_time));
				
				baike_url = ComUtil.getHttpReqPath(baike_url);//去除url的统计
				
//				Baike baike = Baike.dao.queryBySourceUrl(baike_url);				
				Baike baike = Baike.dao.queryByProgramIdName(program_id,baike_title);
				if(baike == null){
					baike = new Baike();
				}
				
				baike.setTitle(element.getTitle());
				baike.setSource_url(baike_url);
				baike.setSummary(baike_note);
				baike.setCover(element.getCover());
				
				if(baike.getId() == null){
					baike.setAddDef();
					baike.save();
				}else{
					baike.setUpdDef();
					baike.update();
				}
				
				element.setFid(baike.getId());
				sceneWarp.addElement(element);
			}
			
			if(srr.length > 21 && StringUtils.isNoneBlank(srr[21])){//百科的物品
				baike_title = srr[21];//22 百科名称
				baike_url = srr[22];//23 百科url
				baike_start_time = srr[23];//24 百科开始时间
				baike_note = srr[24];//25 百科简介
				baike_pic = srr[25];//26 百科图片
				
				Element element = new Element();
				setElementProgram(program_id, episode_id, element);
				element.setType(ElementType.baike.toString());
				element.setTitle(baike_title);
				element.setStart_time(ComUtil.getTime(baike_start_time));
				
				element.setCover("images/baike/"+ComUtil.getDateIndexSavePath(baike_pic));
				String downPath = down(baike_pic,partnerPath,element.getCover(),"baike_cover");
				element.setCover(downPath);
				
				baike_url = ComUtil.getHttpReqPath(baike_url);//去除url的统计
				
//				Baike baike = Baike.dao.queryBySourceUrl(baike_url);
				Baike baike = Baike.dao.queryByProgramIdName(program_id,baike_title);
				if(baike == null){
					baike = new Baike();
				}
				baike.setSource_url(baike_url);
				baike.setSummary(baike_note);
				baike.setTitle(element.getTitle());
				baike.setCover(element.getCover());
				
				if(baike.getId() == null){
					baike.setAddDef();
					baike.save();
				}else{
					baike.setUpdDef();
					baike.update();
				}
				
				element.setFid(baike.getId());
				
				sceneWarp.addElement(element);
			}
		}
		
		Iterator<String> ite = md_str.iterator();
		while(ite.hasNext()){
			ffmpeg.append(ite.next()).append("\r\n");
		}
		ffmpeg.append("\r\n");
		
		ite = cover_ffmpeg.iterator();
		while(ite.hasNext()){
			ffmpeg.append(ite.next()).append("\r\n");
		}
		ffmpeg.append("\r\n");
		
		ite = video_ffmpeg.iterator();
		while(ite.hasNext()){
			ffmpeg.append(ite.next()).append("\r\n");
		}
		ffmpeg.append("\r\n");
		
		ite = error_msg.iterator();
		while(ite.hasNext()){
			ffmpeg.append(ite.next()).append("\r\n");
		}
		ffmpeg.append("\r\n");
	}


	private void sceneAddImgSkipSecond(Program program, Long episode_id,
			Scene scene) {
		scene.setCover("images/scene/"+program.getYear()+"/"+program.getId()+"/"+episode_id+"/"+(scene.getStart_time()+IMG_SKIP_SENCOND)+".jpg");
	}

	
	/**
	 * 增加偏移
	 * @param program
	 * @param episode_id
	 * @param program_id
	 * @param md_str
	 * @param element
	 */
	private void elementAddImgSkipSecond(Program program, Long episode_id,
			Long program_id, Set<String> md_str, Element element) {
		
		element.setCover("images/element/"+program.getYear()+"/"+program_id+"/"+episode_id+"/"+(element.getStart_time()+IMG_SKIP_SENCOND)+".jpg");//电视海报
		md_str.add("md "+FileUtil.getFileParentPath(element.getCover()).replace("/","\\"));
	}


	private void printTvVideo(Long episode_id, Set<String> video_ffmpeg, Element element) {
		String type="element_video";
		Long start_time = element.getStart_time();
		Long end_time = element.getEnd_time();
		String filepath = element.getUrl();
		if(end_time == null){
			end_time = start_time;
		}
		
		
		FfmpegHistory hist = FfmpegHistory.dao.queryByEpisodeTimeType(episode_id, start_time, end_time, type);
		if(hist == null){
			String cmd = "ffmpeg -ss "+start_time+" -i "+episode_id+".flv -s 512x288 -b:v 500000 -vcodec libx264 -acodec aac -b:a 50000 -strict -2 -f mp4 -profile:v baseline -t "+(end_time-start_time)+" "+filepath;
			video_ffmpeg.add(cmd);
			
			hist = new FfmpegHistory();
			hist.setEpisode_id(episode_id);
			hist.setStart_time(start_time);
			hist.setEnd_time(end_time);
			hist.setFilename(filepath);
			hist.setType(type);
			hist.setCmd(cmd);
			hist.save();
		}
	}
	

	

	/**
	 * 截取元素图
	 * @param episode_id
	 * @param cover_ffmpeg
	 * @param element
	 */
	private void printTvImg(Long episode_id, Set<String> cover_ffmpeg,
			Element element) {
		String type="element_img";
		Long start_time = element.getStart_time()+IMG_SKIP_SENCOND;
		Long end_time = element.getEnd_time();
		String filepath = element.getCover();
		if(end_time == null){
			end_time = start_time;
		}else{
			end_time = end_time+IMG_SKIP_SENCOND;
		}

		judgeFfmpegImg(episode_id, cover_ffmpeg, type, start_time, end_time,
				filepath);
		
	}

	
	/**
	 * 截取场景图
	 * @param episode_id
	 * @param scene
	 * @param cover_ffmpeg
	 */
	private void printTvImg(Long episode_id, Scene scene,
			Set<String> cover_ffmpeg) {
		String type="scene_img";
		Long start_time = scene.getStart_time()+IMG_SKIP_SENCOND;
		Long end_time = scene.getEnd_time();
		String filepath = scene.getCover();
		if(end_time == null){
			end_time = start_time;
		}else{
			end_time = end_time+IMG_SKIP_SENCOND;
		}
		
		judgeFfmpegImg(episode_id, cover_ffmpeg, type, start_time, end_time,
				filepath);
	}

	
	/**
	 * 判断是否截图
	 * @param episode_id
	 * @param cover_ffmpeg
	 * @param type
	 * @param start_time
	 * @param end_time
	 * @param filepath
	 */
	private void judgeFfmpegImg(Long episode_id, Set<String> cover_ffmpeg,
			String type, Long start_time, Long end_time, String filepath) {
		FfmpegHistory hist = FfmpegHistory.dao.queryByEpisodeTimeType(episode_id, start_time, end_time, type);
		if(hist == null){
			String cmd = "ffmpeg -ss "+start_time+" -i "+episode_id+".flv -y -f image2 -t 0.001 "+filepath;
			cover_ffmpeg.add(cmd);
			
			hist = new FfmpegHistory();
			hist.setEpisode_id(episode_id);
			hist.setStart_time(start_time);
			hist.setEnd_time(end_time);
			hist.setFilename(filepath);
			hist.setType(type);
			hist.setCmd(cmd);
			hist.setThread_state(Cons.THREAD_STATE_WAIT);
			hist.save();
		}
	}

	
	public void save(List<SceneWarp> list){
		for(int i=0;i<list.size();i++){
			SceneWarp sceneWarp = list.get(i);
			
			Scene scene = sceneWarp.getScene();
			scene.setAddDef();
			scene.save();
			
			List<ElementWarp> elementList = sceneWarp.getList();
			for(int j=0;j<elementList.size();j++){
				ElementWarp elementWarp = elementList.get(j);
				
				Element element = elementWarp.getElment();
			
				element.setScene_id(scene.getId());
				
				if(element.getEnd_time() == null){
					element.setEnd_time(element.getStart_time());
				}
				if(element.getStart_time()!=null && element.getEnd_time()!=null){
					element.setDuration(element.getEnd_time() - element.getStart_time());
				}
				element.setAddDef();
				element.save();
			}
		}
	}

	/**
	 * 查询
	 * @throws IOException 
	 * @throws BiffException 
	 * */
	public void localParse(){
		String str = "搞定";
		try {
			String xlsFileName = null;
			Long program_id = null;
			Long episode_id = null;
			
			List<SceneWarp> list = null;
			//步步惊情_第32集
			xlsFileName = "E:/work/project/xunbao寻宝/运营/20140523/实时素材/国产剧/步步惊情/32.xls";
			program_id = 128113L;
			episode_id = 50922L;
//			list = getData(program_id,episode_id);
//			save(list);
					
			//步步惊情_第33集
			xlsFileName = "E:/work/project/xunbao寻宝/运营/20140523/实时素材/国产剧/步步惊情/33.xls";
			program_id = 128113L;
			episode_id = 50923L;
//			list = getData(xlsFileName,program_id,episode_id);
//			save(list);
			
			//金玉良缘_第1集
//			xlsFileName = "E:/work/project/xunbao寻宝/运营/20140523/实时素材/国产剧/金玉良缘/1.xls";
//			program_id = 128126L;
//			episode_id = 50846L;
//			list = getData(xlsFileName,program_id,episode_id);
//			save(list);
			
			//金玉良缘_第2集
			xlsFileName = "E:/work/project/xunbao寻宝/运营/20140523/实时素材/国产剧/金玉良缘/2.xls";
			program_id = 128126L;
			episode_id = 50847L;
//			list = getData(xlsFileName,program_id,episode_id);
//			save(list);
			
			//裸婚之后_第4集
//			xlsFileName = "E:/work/project/xunbao寻宝/运营/20140523/实时素材/国产剧/裸婚之后/4.xls";
//			program_id = 128149L;
//			episode_id = 50935L;
//			list = getData(xlsFileName,program_id,episode_id);
//			save(list);
			
//			//裸婚之后_第5集
			xlsFileName = "E:/work/project/xunbao寻宝/运营/20140523/实时素材/国产剧/裸婚之后/5.xls";
			program_id = 128149L;
			episode_id = 50936L;
//			list = getData(xlsFileName,program_id,episode_id);
//			save(list);
			
			//中国梦想秀_20140509期
//			xlsFileName = "E:/work/days/23/实时素材/综艺/中国梦想秀/中国梦想秀509 (自动保存的).xls";
//			program_id = 140732L;
//			episode_id = 51002L;
//			list = getData(xlsFileName,program_id,episode_id);
//			save(list);
			
			//花儿与少年_20140509期
			xlsFileName = "E:/work/project/xunbao寻宝/运营/20140523/实时素材/综艺/花儿与少年/花儿与少年.xls";
			program_id = 140733L;
			episode_id = 51003L;
//			list = getData(xlsFileName,program_id,episode_id);
//			save(list);
			
			//第二次运营//////////////////////////////////////////////////////////////////////////
			//步步惊情_第32集 
			xlsFileName = "E:/work/project/xunbao寻宝/运营/20140529/步步惊情.xls";
			program_id = 128113L;
			episode_id = 50922L;
//			list = getData(xlsFileName,program_id,episode_id);
//			save(list);
			
			//裸婚之后_第4集
			xlsFileName = "E:/work/project/xunbao寻宝/运营/20140529/裸婚之后 第四集.xls";
			program_id = 128149L;
			episode_id = 50935L;
//			list = getData(xlsFileName,program_id,episode_id);
//			save(list);
			
			//金玉良缘_第1集
			xlsFileName = "E:/work/project/xunbao寻宝/运营/20140529/金玉良缘.xls";
			program_id = 128126L;
			episode_id = 50846L;
//			list = getData(xlsFileName,program_id,episode_id);
//			save(list);
			
		} catch (Exception e) {
			e.printStackTrace();
			str = e.getMessage();
		}
		renderText(str);
	}
	
	
	public static void printStrArr(String[] srr){
		for(int i=0;i<srr.length;i++){
			System.out.print(srr[i]);
			System.out.print(",");
		}
		System.out.println();
	}
	
	
	public static class SceneWarp{
		private Scene scene;
		private List<ElementWarp> list = new ArrayList<ElementWarp>();
		
		public void addElement(Element element){
			list.add(new ElementWarp(element));
		}
		
		public Scene getScene() {
			return scene;
		}
		public void setScene(Scene scene) {
			this.scene = scene;
		}
		public List<ElementWarp> getList() {
			return list;
		}
		public void setList(List<ElementWarp> list) {
			this.list = list;
		}
	}

	public static class ElementWarp{
		private Element elment;
		
		
		public ElementWarp(Element elment){
			this.elment = elment;
		}
		
		public Element getElment() {
			return elment;
		}
		public void setElment(Element elment) {
			this.elment = elment;
		}
	}
}