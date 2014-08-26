package tv.zhiping.mec.tmallbox.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mec.api.common.ApiCons;
import tv.zhiping.mec.feed.ctrl.FeedBaseCtrl;
import tv.zhiping.mec.feed.ctrl.FeedXlsCtrl.ElementWarp;
import tv.zhiping.mec.feed.model.Baike;
import tv.zhiping.mec.feed.model.Element;
import tv.zhiping.mec.feed.model.Element.ElementType;
import tv.zhiping.mec.feed.model.MecPerson;
import tv.zhiping.mec.feed.model.Music;
import tv.zhiping.mec.feed.model.Question;
import tv.zhiping.mec.feed.model.QuestionOption;
import tv.zhiping.mec.feed.model.Scene;
import tv.zhiping.utils.FileUtil;
import tv.zhiping.utils.ValidateUtil;
import tv.zhiping.utils.XlsUtil;
import tv.zhiping.utils.ZipUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.upload.UploadFile;

/**
 * 元素管理
 * @author 张有良
 */
public class TmallBoxFeedCtrl extends FeedBaseCtrl{	
	
	/**
	 * 元素的预导入
	 * */
	public void xls_input(){
		Long episode_id = getParaToLong("episode_id");
		if(episode_id!=null){
			Episode episode = Episode.dao.findById(episode_id);
			if(episode!=null){
				setAttr("episode",episode);
				Program program = Program.dao.findById(episode.getPid());
				setAttr("program",program);
			}
		}
		renderJsp("/page/feed/tmallbox/xls_input.jsp");
	}
	
	/**
	 * 元素的预导入
	 */
	public void xls_save() {
		UploadFile uploadFile = getFile("upload");//Cons.UPLOAD_TMP
		
		Long episode_id =  getParaToLong("episode_id");//112804L;
		Long program_id = null;
		
		StringBuilder msg = new StringBuilder();
		if(episode_id!=null){
			Episode episode = Episode.dao.findById(episode_id);
			if(episode!=null){
				program_id = episode.getPid();
			}
		}
		if(episode_id == null &&  program_id==null){
			msg.append("节目或剧集为空");
		}else{
			if(uploadFile!=null){
				File file = uploadFile.getFile();
				File unzipTarFile = null;
				try {
					File xlsFile = null;
					String upload_file_folder = null;
					String fileSuffix =FileUtil.getFileSuffix(uploadFile.getOriginalFileName());
					if("zip".equalsIgnoreCase(fileSuffix)){
						String tarFilePath = FileUtil.getFileParentPath(file.getAbsolutePath())+"/"+ComUtil.getRandomFileFolder();//最终解压的目录
						unzipTarFile = new File(tarFilePath);
						ZipUtils.unzipByAnt(file.getAbsolutePath(),tarFilePath);
						
						File realDataTarFile = getTargetFile(unzipTarFile);
						upload_file_folder = realDataTarFile.getAbsolutePath().replace("", "");
						xlsFile = getTargetXlsFile(realDataTarFile,msg);
					}else if("xls".equalsIgnoreCase(fileSuffix)){//压缩文件
						xlsFile = file;
					}else{
						msg.append(uploadFile.getOriginalFileName()+" 文件不能识别，上传的excel文件只能上传Microsoft Excel 97/2000/XP/2003 文件(*.xls)");
					}
					
					if(xlsFile!=null){
						List<ElementWarp> elements = new ArrayList<ElementWarp>();
						List<QuestionWarp> questions = new ArrayList<QuestionWarp>();
						
						List <String[]> list = XlsUtil.getXlsData(xlsFile,0);//人物介绍
						if(list!=null && list.size()>1){
							uploadPerson(program_id, episode_id, list, msg, elements,upload_file_folder);
						}
						
						list = XlsUtil.getXlsData(xlsFile,1);//百科
						if(list!=null && list.size()>1){
							uploadBaike(program_id, episode_id, list, msg, elements,upload_file_folder);
						}
						
						list = XlsUtil.getXlsData(xlsFile,2);//音乐
						if(list!=null && list.size()>1){
							uploadMusic(program_id, episode_id, list, msg, elements,upload_file_folder);
						}
						
						list = XlsUtil.getXlsData(xlsFile,3);//互动
						if(list!=null && list.size()>1){
							uploadQuestion(program_id, episode_id, list, msg,questions);
						}
						if(msg.length() == 0){
							delHsyByEpisodeId(episode_id);
							save(episode_id,elements,questions);
						}
					}
				} catch (Exception e) {
					log.error(e.getMessage(),e);
					msg.append(e.getMessage()+" 未知错误，请联系管理员");
				}finally{//释放上传的内容
					file.delete();
					if(unzipTarFile!=null){
						FileUtil.clearAllFiles(unzipTarFile,false);
					}
				}
			}else{
				msg.append("请选择上传的文件");
			}
		}
		
		JSONObject json = new JSONObject();
		if(msg.length() == 0){
			json.put("status",ApiCons.STATUS_SUC);
			json.put("msg","导入成功");
		}else{
			json.put("status",ApiCons.STATUS_ERROR);
			json.put("msg",msg.toString()+" ，请修改错误数据，并将正确版本再次导入，谢谢。");
		}
		renderJson(json.toJSONString());
	}
	
	/**
	 * 得到xls的文件
	 * @param f
	 * @return
	 */
	private File getTargetXlsFile(File f,StringBuilder msg){
		File[] fs = f.listFiles();
		int xls_len = 0;
		File xls_file = null;
		if(fs!=null && fs.length>0){
			for(int i=0;i<fs.length;i++){
				if(fs[i].isFile() && "xls".equals(FileUtil.getFileSuffix(fs[i].getName()))){
					xls_len++;
					xls_file = fs[i];
				}
			}
		}
		if(xls_len > 1){
			msg.append("压缩包中存在多个xls文件");
		}
		return xls_file;
	}
	
	
	
	/**
	 * 根据解压的目录，得到真实的数据目录
	 * @param f
	 * @return
	 */
	private File getTargetFile(File f){
		File[] fs = f.listFiles();
		if(fs.length == 1 && fs[0].isDirectory()){
			return fs[0];
		}
		return f;
	}
	
	
	
	/**
	 * 数据保存
	 * @param program_id
	 * @param episode_id
	 * @param elementList
	 */
	private void save(Long episode_id,List<ElementWarp> elementList,List<QuestionWarp> questions){
		Scene scene = new Scene();
		scene.setPid(episode_id);
		scene.setStart_time(0L);
		scene.setEnd_time(new Long(Integer.MAX_VALUE));
		scene.setDuration(scene.getEnd_time() - scene.getStart_time());
		scene.setAddDef();
		scene.save();
		
		for(int j=0;j<elementList.size();j++){
			ElementWarp elementWarp = elementList.get(j);
			
			Element element = elementWarp.getElment();
			
			
			element.setScene_id(scene.getId());
			
//			if(element.getEnd_time() == null){
//				element.setEnd_time(element.getStart_time());
//			}
			if(element.getStart_time()!=null && element.getEnd_time()!=null){
				element.setDuration(element.getEnd_time() - element.getStart_time());
			}
			element.setAddDef();
			element.save();
		}
		
		for(int j=0;j<questions.size();j++){
			QuestionWarp questionWarp = questions.get(j);
			
			Question question = questionWarp.getQuestion();
			question.save();
			
			int answer_index = question.getAnswer_id().intValue();
			
			List<QuestionOption> opts = questionWarp.getOpts();
			if(opts!=null && !opts.isEmpty()){
				for(int i=0;i<opts.size();i++){
					QuestionOption opt = opts.get(i);
					opt.setQuestion_id(question.getId());
					opt.setAddDef();
					opt.save();
					
					if(answer_index == i){
						question.setAnswer_id(opt.getId());
						question.setUpdDef();
						question.update();
					}
				}
			}
		}
	}
	
	/**
	 * 音乐的导入
	 * 0:问题	1:答题出现时间	2:结束答题时间	3:答案揭晓时间	4:问答结束时间	5:答案	6:A	7:B	8:C	9:D
	 * @param list
	 */
	private void uploadQuestion(Long program_id, Long episode_id,List<String[]> list,StringBuilder msg,List<QuestionWarp> questions){
		list.remove(0);
		for(int i=0;i<list.size();i++){
			String[] srr = (String[])list.get(i);
			String str = null;
			
			if((str = checkQuestionMsg(srr))==null){
				try {//歌曲
					Question question = new Question();
					question.setTitle(srr[0]);
					question.setProgram_id(program_id);
					question.setEpisode_id(episode_id);
					question.setStart_time(ComUtil.getTime(srr[1]));
					question.setEnd_time(ComUtil.getTime(srr[4]));
					question.setDeadline(ComUtil.getTime(srr[2]));
					question.setPublic_time(ComUtil.getTime(srr[3]));
					setLastEditSysUser(question);
					if(StringUtils.isNotBlank(srr[5])){
						question.setAnswer_id(Long.parseLong(srr[5]));
					}
					
					List<QuestionOption> opts = new ArrayList<QuestionOption>();
					for(int j=6;j<srr.length;j++){
						if(StringUtils.isNotBlank(srr[j])){
							QuestionOption opt = new QuestionOption();
							opt.setTitle(srr[j]);
							opts.add(opt);
						}
					}
					
					QuestionWarp warp = new QuestionWarp(question,opts);
					questions.add(warp);
				} catch (Exception e) {
					msg.append("互动问答第"+(i+2)+"行 错误提示:").append(e.getMessage()).append("\n");
				}
			}else{
				msg.append("互动问答第"+(i+2)+"行 错误提示:").append(str).append("\n");
				
			}
		}
	}
	
	/**
	 * 歌曲的校验
	 * @param srr
	 * @return
	 */
	private String checkQuestionMsg(String[] srr) {
		StringBuilder msg = new StringBuilder();
		
		try {
			if(srr.length < 8){
				msg.append("互动问答格式错误,请安规范导入,");
			}else{
				if(StringUtils.isBlank(srr[0])){
					msg.append("互动问答标题不能为空,");
				}
				if(srr[0].length()>50){
					msg.append("互动问答标题不能超过50个字符,");
				}
				if(StringUtils.isNotBlank(srr[1]) && ComUtil.getTime(srr[1]) < 0){
					msg.append("互动问答答题出现时间填写不规范"+srr[1]);
				}
				
				if(StringUtils.isNotBlank(srr[2]) && ComUtil.getTime(srr[2]) < 0){
					msg.append("互动问答结束答题时间填写不规范"+srr[2]);
				}
				
				if(StringUtils.isNotBlank(srr[3]) && ComUtil.getTime(srr[3]) < 0){
					msg.append("互动问答答案揭晓时间填写不规范"+srr[3]);
				}
				
				if(StringUtils.isNotBlank(srr[4]) && ComUtil.getTime(srr[4]) < 0){
					msg.append("互动问答问答结束时间填写不规范"+srr[4]);
				}
				
				if(StringUtils.isNotBlank(srr[5])){
					if(srr[5].getBytes().length!=1){
						msg.append("互动问答答案只能填写一个字符");
					}else{//问题	答题出现时间	结束答题时间	答案揭晓时间	问答结束时间	答案	A	B	C	D
						int awser_index = getAwardIndex(srr[5]);//从0开始
						int option_len = getOptionLen(srr,6);//从1开始
						if(option_len<2 || option_len>5){
							msg.append("互动问答答案个数在2~5个内");
						}
						
						if(awser_index >= option_len){
							msg.append("互动问答答案数字超过了选项数");
						}else{
							srr[5] = String.valueOf(awser_index);//重新赋值
						}						
					}
				}
				
			}
		} catch (Exception e) {
			msg.append(e.getMessage());
		}
		
		if(msg.length()>1){
			return msg.toString();			
		}
		return null;
	}
	
	/**
	 * 音乐的导入
	 * 0：标签	1:名称	2:歌手	3：图片（花絮类的百科不要求）	4：音乐链接（虾米音乐）	5：开始时间	6：截止时间
	 * @param list
	 */
	private void uploadMusic(Long program_id, Long episode_id,List<String[]> list,StringBuilder msg,List<ElementWarp> elements,String upload_file_folder){
		list.remove(0);
		
		String partnerPath = getPartnerPathRemoveLast();
		
		for(int i=0;i<list.size();i++){
			String[] srr = (String[])list.get(i);
			String str = null;
			
			if((str = checkMusicMsg(srr))==null){
				try {//歌曲
					String music_type = srr[0];//10 歌曲类型
					String music_name = srr[1];//11 歌曲名称
					String music_singer = srr[2];//13歌曲歌手
					String music_pic = srr[3];//14歌曲图片
					String music_url = srr[4];//15歌曲url
					String music_start_time = srr[5];//12歌曲开始时间
					String music_end_time = srr[6];//12歌曲开始时间
					
					Element element = new Element();
					setElementProgram(program_id, episode_id, element);
					element.setType(ElementType.music.toString());
					element.setTag(music_type);
					
					downMusic(partnerPath, music_pic, element,upload_file_folder);
					
					element.setTitle(music_name);
					element.setStart_time(ComUtil.getTime(music_start_time));
					element.setEnd_time(ComUtil.getTime(music_end_time));
					
					Music music = null;
					if(StringUtils.isNotBlank(music_url)){
						music = Music.dao.queryBySourceUrl(music_url);
					}else{
						music = Music.dao.queryByNameSinger(music_name,music_singer);
					}
					if(music == null){
						music = new Music();
					}
					music.setTag(element.getTag());
					
					saveOrUpdMusic(music_singer,music_url, element, music);
					
					element.setFid(music.getId());
					
					ElementWarp warp = new ElementWarp(element);
					elements.add(warp);
				} catch (Exception e) {
					msg.append("音乐第"+(i+2)+"行 错误提示:").append(e.getMessage()).append("\n");
				}
			}else{
				msg.append("音乐第"+(i+2)+"行 错误提示:").append(str).append("\n");
				
			}
		}
	}
	
	/**
	 * 歌曲的校验
	 * @param srr
	 * @return
	 */
	private String checkMusicMsg(String[] srr) {
		StringBuilder msg = new StringBuilder();
		if(srr.length < 7){
			msg.append("音乐格式错误,请安规范导入,");
		}else{
			if(StringUtils.isBlank(srr[0])){
				msg.append("音乐标签不能为空,如果这行内容都为空，请删除该行，");
			}
			if(srr[0].length()>10){
				msg.append("音乐标签不能超过10个字符,");
			}
			if(StringUtils.isBlank(srr[1])){
				msg.append("音乐名称不能为空,");
			}
			if(srr[1].length()>30){
				msg.append("音乐名称不能超过30个字符,");
			}
			
			if(StringUtils.isBlank(srr[2])){
				msg.append("音乐歌手不能为空,");
			}
			if(srr[2].length()>20){
				msg.append("音乐歌手不能超过10个字符,");
			}
			
			if(StringUtils.isBlank(srr[3])){
				msg.append("音乐图片不能为空,");
			}else if(!ValidateUtil.isPic(srr[3])){
				msg.append("音乐图片不规范，需要jpg，png，bmp，gif结尾,");
			}
			
			if(StringUtils.isBlank(srr[4])){
				msg.append("音乐链接不能为空,");
			}
			
			if(StringUtils.isNotBlank(srr[5]) && ComUtil.getTime(srr[5]) < 0){
				msg.append("音乐开始时间填写不规范"+srr[5]);
			}
			if(StringUtils.isNotBlank(srr[6]) && ComUtil.getTime(srr[6]) < 0){
				msg.append("音乐截止时间填写不规范"+srr[6]);
			}
		}
		if(msg.length()>1){
			return msg.toString();			
		}
		return null;
	}

	/**
	 * 百科的导入
	 * 0：名称	1：详细介绍	2：图片（花絮类的百科不要求）	3：百科地址	4：开始时间	5：截止时间
	 * @param list
	 */
	private void uploadBaike(Long program_id, Long episode_id,List<String[]> list,StringBuilder msg,List<ElementWarp> elements,String upload_file_folder){
		list.remove(0);
		
		String partnerPath = getPartnerPathRemoveLast();
		
		for(int i=0;i<list.size();i++){
			String[] srr = (String[])list.get(i);
			
			String str = null;
			
			if((str = checkBaikeMsg(srr))==null){
				try {
					Baike obj = null;
					
//					if(StringUtils.isNotBlank(srr[3])){//如果有外部链接
//						obj = Baike.dao.queryBySourceUrl(srr[3]);
//					}else{
					obj = Baike.dao.queryByProgramIdName(program_id,srr[0]);
//					}
					if(obj == null){
						obj = new Baike();
					}
					obj.setProgram_id(program_id);
					obj.setTitle(srr[0]);
					obj.setSummary(srr[1]);
					obj.setCover(srr[2]);
					if(StringUtils.isNotBlank(srr[3])){
						obj.setSource_url(srr[3]);
					}
					
					Element element = new Element();
					setElementProgram(program_id, episode_id, element);				
					element.setType(ElementType.baike.toString());
					
					if(StringUtils.isNotBlank(obj.getCover())){
						downBaikeCover(element,obj.getCover(),partnerPath,upload_file_folder);
					}
					
					element.setTitle(obj.getTitle());
					element.setStart_time(ComUtil.getTime(srr[4]));
					element.setEnd_time(ComUtil.getTime(srr[5]));
					
					obj.setCover(element.getCover());
					
					setLastEditSysUser(obj);
					if(obj.getId() == null){
						obj.save();
					}else{
						obj.update();				
					}
					element.setFid(obj.getId());
					
					ElementWarp warp = new ElementWarp(element);
					elements.add(warp);
				} catch (Exception e) {
					msg.append("百科第"+(i+2)+"行 错误提示:").append(e.getMessage()).append("\n");
				}
			}else{
				msg.append("百科第"+(i+2)+"行 错误提示:").append(str).append("\n");
				
			}
		}
	}

	private String getPartnerPathRemoveLast() {
		String partnerPath;
		partnerPath = CacheFun.getConVal(ConfigKeys.ST_LOCAL_FOLDER);
		if(partnerPath != null && partnerPath.endsWith("/")){
			partnerPath = partnerPath.substring(0,partnerPath.length() - 1);
		}
		return partnerPath;
	}
	
	/**
	 * 百科的校验
	 * @param srr
	 * @return
	 */
	private String checkBaikeMsg(String[] srr) {
		StringBuilder msg = new StringBuilder();
		if(srr.length < 6){
			msg.append("百科格式错误,请安规范导入,");
		}else{
			if(StringUtils.isBlank(srr[0])){
				msg.append("百科名称不能为空,如果这行内容都为空，请删除该行，");
			}
			
			if(srr[0].length()>15){
				msg.append("百科名称不能超过15个字符,");
			}
			
			if(StringUtils.isBlank(srr[1])){
				msg.append("百科介绍不能为空,");
			}
			
			if(srr[1].length()>30){
				msg.append("百科介绍不能超过30个字符,");
			}
			
//			if(StringUtils.isBlank(srr[2])){
//				msg.append("百科图片不能为空,");
//			}else 
			if(StringUtils.isNotBlank(srr[2]) && !ValidateUtil.isPic(srr[2])){
				msg.append("百科图片不规范，需要jpg，png，bmp，gif结尾,");
			}
			
//			if(StringUtils.isBlank(srr[3])){
//				msg.append("百科地址不能为空,");
//			}
			
			if(StringUtils.isNotBlank(srr[4]) && ComUtil.getTime(srr[4]) < 0){
				msg.append("百科开始时间填写不规范"+srr[4]);
			}
			if(StringUtils.isNotBlank(srr[5]) && ComUtil.getTime(srr[5]) < 0){
				msg.append("百科截止时间填写不规范"+srr[5]);
			}
		}
		
		if(msg.length()>1){
			return msg.toString();			
		}
		return null;
	}
	
	
	/**
	 * 明星的导入
	 * 0:名称 1:出生日期  2:出生地	3:教育背景/工作背景 4:简介	5:图片	6:开始时间	7:截止时间
	 * @param list
	 */
	private void uploadPerson(Long program_id, Long episode_id,List<String[]> list,StringBuilder msg,List<ElementWarp> elements,String upload_file_folder){
		list.remove(0);
		
		String partnerPath = getPartnerPathRemoveLast();
		
		for(int i=0;i<list.size();i++){
			String[] srr = (String[])list.get(i);
			
			String str = null;
			
			if((str = checkPersonMsg(srr))==null){
				try {
					MecPerson obj = MecPerson.dao.queryByProgramIdName(program_id,srr[0],null);
					if(obj == null){
						obj = new MecPerson();	
					}
					
					obj.setProgram_id(program_id);
					obj.setName(srr[0]);						
					obj.setDescription(srr[1]);
					obj.setAvatar_mtime(srr[2]);
					obj.setBaike_url(srr[3]);
					setLastEditSysUser(obj);
					
					boolean save = false;
					if(obj.getId() == null){
						obj.save();
						save = true;
					}else{
						obj.update();
					}
					
					if(save){
						downPersonAvatar(obj,partnerPath,upload_file_folder);
						obj.update();
					}
					
					Element element = new Element();
					setElementProgram(program_id, episode_id, element);				
					element.setType(ElementType.mec_person.toString());
					
					element.setFid(obj.getId());
					element.setTitle(obj.getName());
					element.setStart_time(ComUtil.getTime(srr[4]));
					element.setEnd_time(ComUtil.getTime(srr[5]));
					
					ElementWarp warp = new ElementWarp(element);
					elements.add(warp);
				} catch (Exception e) {
					msg.append("人物第"+(i+2)+"行 错误提示:").append(e.getMessage()).append("\n");
				}
			}else{
				msg.append("人物第"+(i+2)+"行 错误提示:").append(str).append("\n");
			}
		}
	}
	
	
	/**
	 * 百科的校验
	 * @param srr
	 * @return
	 */
	private String checkPersonMsg(String[] srr) {
		StringBuilder msg = new StringBuilder();
		if(srr.length < 6){
			msg.append("人物格式错误,请安规范导入,");
		}else{
			if(StringUtils.isBlank(srr[0])){
				msg.append("人物名称不能为空,如果这行内容都为空，请删除该行，");
			}
			if(srr[0].length()>10){
				msg.append("人物名称不能超过10个字符,");
			}
			
			if(StringUtils.isBlank(srr[1])){
				msg.append("人物简介不能为空,");
			}
			if(srr[1].length()>30){
				msg.append("人物简介不能超过30个字符,");
			}
			
//			try {
//				if(StringUtils.isNotBlank(srr[1]) && DateUtil.getXlsTime(srr[1])==null){
//					msg.append("人物出生日期格式出错,");
//				}
//			} catch (ParseException e) {
//				msg.append("人物出生日期格式出错,"+e.getMessage());
//			}
			
			if(StringUtils.isBlank(srr[2])){
				msg.append("人物图片不能为空,");
			}else if(!ValidateUtil.isPic(srr[2])){
				msg.append("人物图片不规范，需要jpg，png，bmp，gif结尾,");
			}
			
			if(StringUtils.isNotBlank(srr[4]) && ComUtil.getTime(srr[4]) < 0){
				msg.append("人物开始时间填写不规范"+srr[4]);
			}
			if(StringUtils.isNotBlank(srr[4]) && ComUtil.getTime(srr[5]) < 0){
				msg.append("人物截止时间填写不规范"+srr[5]);
			}
		}
		
		if(msg.length()>1){
			return msg.toString();			
		}
		return null;
	}
	
	public static class QuestionWarp{
		private Question question;
		private List<QuestionOption> opts;
		
		public QuestionWarp(Question question,List<QuestionOption> opts){
			this.question = question;
			this.opts = opts;
		}

		public Question getQuestion() {
			return question;
		}

		public void setQuestion(Question question) {
			this.question = question;
		}

		public List<QuestionOption> getOpts() {
			return opts;
		}

		public void setOpts(List<QuestionOption> opts) {
			this.opts = opts;
		}
	}
}
