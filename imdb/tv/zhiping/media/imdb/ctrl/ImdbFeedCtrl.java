package tv.zhiping.media.imdb.ctrl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.common.Cons;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mec.feed.model.ImdbFact;
import tv.zhiping.mec.feed.model.ImdbFactName;
import tv.zhiping.mec.feed.model.ImdbFactTitle;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.media.model.ImdbEpisode;
import tv.zhiping.media.model.ImdbEpisode.ImdbEpisodeType;
import tv.zhiping.media.model.ImdbEvent;
import tv.zhiping.media.model.ImdbImage;
import tv.zhiping.media.model.ImdbLog;
import tv.zhiping.media.model.ImdbPerson;
import tv.zhiping.media.model.ImdbPersonPrincipalt;
import tv.zhiping.media.model.ImdbPersonVideo;
import tv.zhiping.media.model.ImdbPlot;
import tv.zhiping.media.model.ImdbProduct;
import tv.zhiping.media.model.ImdbScene;
import tv.zhiping.media.model.ImdbSoundAlbum;
import tv.zhiping.media.model.ImdbSoundAlbumVideo;
import tv.zhiping.media.model.ImdbSoundItem;
import tv.zhiping.media.model.ImdbSoundItemVideo;
import tv.zhiping.media.spilder.imdb.ImdbSpilder;
import tv.zhiping.utils.DateUtil;
import tv.zhiping.utils.FileUtil;
import tv.zhiping.utils.ZipUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.upload.UploadFile;

/**
 * imdb的导入
 * @author 张有良
 */

public class ImdbFeedCtrl extends SysBaseCtrl{	
	/**
	 * App系统配置 index
	 * */
	public void index(){
		renderJsp("/page/imdb/imdbFeed/input.jsp");
	}
	
	public void save() throws IOException{
		StringBuilder msg = new StringBuilder();
		UploadFile uploadFile = getFile("upload");//Cons.UPLOAD_TMP
		if(uploadFile!=null){
			File file = uploadFile.getFile();
			try {
				String absPath = ComUtil.getLinuxPath(file.getParentFile().getAbsolutePath());
				String originalFileName = uploadFile.getOriginalFileName();
				String fileSuffix =FileUtil.getFileSuffix(originalFileName);
				if("zip".equalsIgnoreCase(fileSuffix)){//压缩文件
					String tarFilePath = FileUtil.getFileParentPath(file.getAbsolutePath())+"/"+ComUtil.getRandomFileFolder();//最终解压的目录
					File unzipTarFile = new File(tarFilePath);
					try {//解压
						ZipUtils.unzipByAnt(file.getAbsolutePath(),tarFilePath);
						
						File realDataTarFile = getTargetFile(unzipTarFile);
						
						ImdbEpisode root_episode = processCheckFile(absPath,realDataTarFile,msg);
						
						if(msg.length()==0){
							processFileToParseFile(root_episode,absPath,realDataTarFile,msg);
						}
					} catch (Exception e) {
						log.error(e.getMessage(),e);
						msg.append(e.getMessage()+" 未知错误，请联系管理员");
					}finally{//释放上传的zip文件解压后的内容
						FileUtil.clearAllFiles(unzipTarFile,false);
					}
				}
//				else if("txt".equalsIgnoreCase(fileSuffix) || "json".equalsIgnoreCase(fileSuffix)){//单个文件不支持
//					parseJsonFile(absPath,episode_id,file,msg);
//				}
				else{
					msg.append(originalFileName+" 文件不能识别，请按说明上传");
				}
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				msg.append(e.getMessage()+" 未知错误，请联系管理员");
			}finally{//释放上传的内容
				file.delete();
			}
			
		}else{
			msg.append("请选择上传的文件");
		}

		if(msg.length() == 0){
			msg.append("解析完毕，请换个文件导入吧");
		}
		renderText(msg.toString());
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
	 * 校验文件，如果是文件，解析文件，如果是文件夹，进入文件夹继续遍历，再去解析
	 * @param absPath
	 * @param f
	 * @param msg
	 */
	private ImdbEpisode processCheckFile(String absPath,File f,StringBuilder msg){
		StringBuilder folder_msg = new StringBuilder();
		List<File> list = listFile(absPath,f.listFiles(), folder_msg);//首先是program,episode,event,folder
		ImdbEpisode root_episode = null;
		
		if(list!=null && !list.isEmpty()){
			//解析第一个文件的root信息
			File file = list.get(0);//第一个文件必须是剧集信息
			String tip_file_name = getTipFileName(absPath, file);
			if(file.isFile()){
				root_episode = getEpisodeByFile(folder_msg, tip_file_name,null,file);
				
				if(root_episode == null || StringUtils.isBlank(root_episode.getPid())){
					folder_msg.append(tip_file_name+" 首个文件解析失败，请检查数据").append("\r\n");
				}
				if(folder_msg.length() == 0){
					if("movie".equals(root_episode.getType())){
						if(list.size() == 2){
							//校验第一个节目文件
							tip_file_name = getTipFileName(absPath, list.get(0));
							checkEpisodeFile(root_episode, list.get(0), folder_msg, tip_file_name);
							
							//校验第二个剧集文件
							String fileName = list.get(1).getName();
							tip_file_name = getTipFileName(absPath, list.get(1));
							checkEventFile(root_episode, list.get(1), folder_msg, tip_file_name, fileName);
						}else{
							folder_msg.append(f.getName()+" 电影文件夹只能保存：program.txt event.txt").append("\r\n");
						}
					}else if("tv".equals(root_episode.getType())){
						getImdbAllEpisode(root_episode.getPid());//首先到imdb加载所有的信息
						for(int i=0;i<list.size();i++){
							file = list.get(i);
							if(file.isFile()){//文件
								checkParseJsonFile(absPath,root_episode,file,folder_msg);
							}else{//文件夹
								processCheckFile(absPath,file, folder_msg);
							}
						}
					}
				}
			}else{
				folder_msg.append(tip_file_name+" 首个文件解析失败,必须是文件，请检查数据").append("\r\n");
//				for(int i=0;i<list.size();i++){
//					file = list.get(i);
//					processCheckFile(absPath,file,folder_msg);
//				}
			}
		}
		msg.append(folder_msg);
		return root_episode;
	}

	/**
	 * 获取待校验的节目信息
	 * @param msg
	 * @param tip_file_name
	 * @param json_txt
	 * @return
	 */
	private ImdbEpisode getEpisodeByFile(StringBuilder msg, String tip_file_name,String json_txt,File file) {
		ImdbEpisode root_episode = new ImdbEpisode();
//		String fileName = file.getName();
		if(json_txt == null){
			json_txt = getTextContent(file);
		}
		if(StringUtils.isBlank(json_txt)){
			msg.append(tip_file_name+" 节目文件解析失败，请检查数据").append("\r\n");
		}else{
			JSONObject obj = JSONObject.parseObject(json_txt);
			JSONObject resource = obj.getJSONObject("resource");
			
			if("imdb.api.title.fulldetails".equals(resource.getString("@type"))){//节目剧集信息
				JSONObject base = resource.getJSONObject("base");
				String id = base.getString("id");
				
				root_episode.setId(id);
				root_episode.setPid(id);
				
				JSONObject parentTitle = base.getJSONObject("parentTitle");
				if(parentTitle != null){
					root_episode.setPid(parentTitle.getString("id"));
				}
//				else{
//					msg.append(tip_file_name+" 文件不存在parentTitle信息，请检查数据").append("\r\n");
//				}
				
				String titleType = base.getString("titleType");
				
				if(!ImdbEpisodeType.movie.toString().equalsIgnoreCase(titleType)){//不是电影文件
					root_episode.setId(null);
					root_episode.setType(ImdbEpisodeType.tv.toString());
				}else{//电影
					root_episode.setType(ImdbEpisodeType.movie.toString());
				}
			}else{
				msg.append(tip_file_name+" 节目文件解析失败，请检查数据").append("\r\n");	
			}
		}
		return root_episode;
	}


	/**
	 * 解析json文件
	 * @param absPath
	 * @param episode_id
	 * @param file
	 * @param msg
	 */
	public void checkParseJsonFile(String abs_path,ImdbEpisode root_episode,File file,StringBuilder msg){
		String tip_file_name = getTipFileName(abs_path, file);
		try {
			String fileNmae = file.getName();
			//解析优先级:program episode event
			if(fileNmae.indexOf("program")==0 || fileNmae.indexOf("episode")==0){
				checkEpisodeFile(root_episode, file, msg, tip_file_name);
			}else if(fileNmae.indexOf("event")==0){
				checkEventFile(root_episode, file, msg, tip_file_name, fileNmae);
			}else{
				msg.append(tip_file_name+" 解析失败，请检查数据").append("\r\n");
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			msg.append(tip_file_name+" 解析失败:"+e.getMessage()).append("\r\n");
		}
	}
	
	public static void main(String[] args) {
		String path = "C:/Users/liang/Documents/Tencent Files/415323267/FileRecv/the_closer/event_0606.txt";
		String json_txt = getTextContent(new File(path));
		System.out.println(json_txt.substring(9572));
		if(StringUtils.isNotBlank(json_txt)){
			JSONObject resource = JSONObject.parseObject(json_txt).getJSONObject("resource");
			
		}
	}

	/**
	 * 校验场景文件
	 * @param root_episode
	 * @param file
	 * @param msg
	 * @param tip_file_name
	 * @param fileNmae
	 */
	private void checkEventFile(ImdbEpisode root_episode, File file,
			StringBuilder msg, String tip_file_name, String fileNmae) {
		String json_txt = getTextContent(file);
		if(StringUtils.isBlank(json_txt)){
			msg.append(tip_file_name+" 文件内容空，请检查").append("\r\n");
		}else{
			JSONObject resource = JSONObject.parseObject(json_txt).getJSONObject("resource");
			if(!"imdb.api.video.events".equals(resource.getString("@type"))){
				msg.append(tip_file_name+" 应该是场景信息文件，文件命名错误，请检查").append("\r\n");
			}else{
				String fileNum = getFileNameNum(file.getName());
				if(!"movie".equals(root_episode.getType())){
					if(StringUtils.isNotBlank(fileNum) && fileNmae.length()>3){//判断文件时间剧集是否存在
						Long season = new Long(fileNum.substring(1,3));
						Long current_episode = new Long(fileNum.substring(3));
						
						ImdbEpisode dbObj = ImdbEpisode.dao.queryBySelfOrPidSeasonEpisode(null,root_episode.getPid(),season,current_episode);
						if(dbObj == null){//判断数据库是否有影视信息
							msg.append(tip_file_name+" 场景文件，对应的季集信息部存在，请检查").append("\r\n");								
						}
					}else{
						msg.append(tip_file_name+" 场景文件，季集数字错误，请检查文件名").append("\r\n");
					}
				}
			}
		}
	}


	private void checkEpisodeFile(ImdbEpisode root_episode, File file,
			StringBuilder msg, String tip_file_name) {
		String json_txt = getTextContent(file);
		if(StringUtils.isBlank(json_txt)){
			msg.append(tip_file_name+" 文件内容空，请检查").append("\r\n");
		}else{
			JSONObject resource = JSONObject.parseObject(json_txt).getJSONObject("resource");
			if(!"imdb.api.title.fulldetails".equals(resource.getString("@type"))){//节目剧集信息
				msg.append(tip_file_name+" 应该是剧集信息文件，文件命名错误，请检查").append("\r\n");
			}else{
				ImdbEpisode obj = getEpisodeByFile(msg, tip_file_name, json_txt,file);//检查root信息是否一样,type信息是否一样
				if(obj.getPid()==null || !obj.getPid().equals(root_episode.getPid())){
					msg.append(tip_file_name+" 所属的 parentTitleId不一致，请检查").append("\r\n");
				}
				if(!checkType(root_episode.getType(), obj.getType())){//是否类型都一样
					msg.append(tip_file_name+" 类型不一致："+root_episode.getType()+":"+obj.getType()+"，请检查").append("\r\n");
				}
			}
		}
	}
	
	/**
	 * 判断类型
	 * @param src_type
	 * @param target_type
	 * @return
	 */
	public boolean checkType(String src_type,String target_type){
		boolean flag = false;
		if(src_type.equalsIgnoreCase(target_type)){
			flag = true;
		}else if(("tvSeries".equals(src_type) || "tvEpisode".equals(src_type)) && "tvSeries".equals(target_type) || "tvEpisode".equals(target_type)){
			flag = true;
		}
		return flag;
	}
	/**
	 * 处理文件，如果是文件，解析文件，如果是文件夹，进入文件夹继续遍历，再去解析
	 * @param absPath
	 * @param f
	 * @param msg
	 */
	private void processFileToParseFile(ImdbEpisode root_episode,String absPath,File f,StringBuilder msg){
		List<File> list = listFile(absPath,f.listFiles(), msg);
		for(int i=0;i<list.size();i++){
			File file = list.get(i);
			if(file.isFile()){//文件
//				String tip_file_name = getTipFileName(absPath, file);
				parseJsonFile(root_episode,absPath,file,msg);
			}else{//文件夹
//				processFileToParseFile(absPath,file, msg);
			}
		}
	}
	
	/**
	 * 根据文件名得到文件的数字，和节目剧集id匹配
	 * @param fileName
	 * @return
	 */
	public static String getFileNameNum(String fileName){
		int index = fileName.lastIndexOf(".");
		if(index>0 && index<fileName.length()){
			fileName = fileName.substring(0,fileName.lastIndexOf("."));
		}
		fileName = fileName.replace("program","").replace("episode","").replace("event","");
		return fileName;
	}
	
	
	
	/**
	 * 解析json文件
	 * @param absPath
	 * @param episode_id
	 * @param file
	 * @param msg
	 */
	public void parseJsonFile(ImdbEpisode root_episode,String absPath,File file,StringBuilder msg){
		try {//解析优先级:program episode event
			String json_txt = getTextContent(file);
			if(StringUtils.isBlank(json_txt)){
				msg.append(file.getAbsolutePath().replace(absPath,"")+" 文件空，请检查数据").append("\r\n");
			}else{
				JSONObject obj = JSONObject.parseObject(json_txt);
				JSONObject resource = obj.getJSONObject("resource");
				
				obj = JSONObject.parseObject(json_txt);
				resource = obj.getJSONObject("resource");
				if("imdb.api.title.fulldetails".equals(resource.getString("@type"))){//节目剧集信息
					parseEpisode(resource);//解析节目剧集
				}else if("imdb.api.video.events".equals(resource.getString("@type"))){
					String fileNum = getFileNameNum(file.getName());
					
					Long season = null;
					Long current_episode = null;
					if(fileNum.length()>4){
						season = new Long(fileNum.substring(1,3));
						current_episode = new Long(fileNum.substring(3));
					}
					
					ImdbEpisode dbObj = ImdbEpisode.dao.queryBySelfOrPidSeasonEpisode(root_episode.getId(),root_episode.getPid(),season,current_episode);
					parseEventFile(dbObj.getId(),resource);
				}else{
					msg.append(file.getAbsolutePath().replace(absPath,"")+" 解析失败，请检查数据").append("\r\n");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			msg.append(getTipFileName(absPath, file)+" 解析失败:"+e.getMessage()).append("\r\n");
		}
	}
	
	/**
	 * 得到文件夹的内容
	 * @param file
	 * @return
	 */
	private static String getTextContent(File file){
		BufferedReader read = null;
		try {
			read = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
			String str = null;
			StringBuilder sbuf = new StringBuilder();
			while((str = read.readLine())!=null){
				sbuf.append(str);
			}
			return sbuf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(read!=null){
				try {
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 解析剧集信息
	 * @param pid  节目id
	 * @param base 剧集详情
	 */
	private String parseEpisode(JSONObject resource) {
		JSONObject base = resource.getJSONObject("base");
		String id = parseEpisodeBase(base,2,resource.toJSONString());
		
		//解析详情
		JSONObject plot = resource.getJSONObject("plot");		
		parsePlot(id,plot);
		
//		//演员信息从event过来
//		JSONObject names = resource.getJSONObject("names");
//		parseNames(id,names,1);
		
		JSONArray principals = resource.getJSONArray("principals");
		parsePrincipals(id,principals);
		
		//解析导演
		JSONObject credits = resource.getJSONObject("credits");
		parseCredits(id,"episode",credits);
		
		//歌曲信息
		JSONArray soundtrackItems  = resource.getJSONArray("soundtrackItems");//歌曲信息
		parseSoundtrackItems(id,"episode",soundtrackItems);
		
		//专辑信息
		JSONArray soundtrackAlbums  = resource.getJSONArray("soundtrackAlbums");//唱片信息
		parseSoundtrackAlbums(id,"episode",soundtrackAlbums);
		return id;
	}
	
	
	/**
	 * 主要演员解析
	 * @param pid
	 * @param principals
	 */
	private void parsePrincipals(String pid, JSONArray principals) {
		if(principals!=null && !principals.isEmpty()){
			for(int i=0;i<principals.size();i++){
				JSONObject json = principals.getJSONObject(i);
				String name_id = json.getString("id");
				ImdbPersonPrincipalt obj = ImdbPersonPrincipalt.dao.queryByPidNameId(pid,name_id);
				if(obj==null){
					obj = new ImdbPersonPrincipalt();
					obj.setPid(pid);
					obj.setName(json.getString("name"));
					obj.setName_id(name_id);
					obj.setSort_num(i);
					
					obj.setJson_txt(json.toJSONString());
					
					if(obj.getId() == null){
						obj.setMatch_state(Cons.THREAD_STATE_WAIT);
						obj.setAddDef();
						obj.save();
					}					
				}
			}
		}		
	}


	//解析音乐信息
	private void parseSoundtrackItems(String pid,String lev,JSONArray sounds) {
		if(sounds!=null && !sounds.isEmpty()){
			for(int i=0;i<sounds.size();i++){
				JSONObject json = sounds.getJSONObject(i);
				saveOrUpdSoundItem(json);
				
				String id = json.getString("id");
				ImdbSoundItemVideo pp = ImdbSoundItemVideo.dao.queryByObj(id,pid,lev);
				if(pp == null){
					pp = new ImdbSoundItemVideo();
					pp.setSound_item_id(id);
					pp.setVideo_id(pid);
					pp.setLev(lev);	
					pp.setAddDef();
					pp.save();
				}
			}
		}
	}

	
	/**
	 * 只解析节目信息
	 * @param base
	 */
	private String parseEpisodeBase(JSONObject base,Integer source,String json_txt) {
		String id = base.getString("id");
		String title = base.getString("title");
		String titleType = base.getString("titleType");
		String year = base.getString("year");
		
		ImdbEpisode episode = ImdbEpisode.dao.findById(id);
		boolean save = false;
		if(episode == null){
			save = true;
			episode = new ImdbEpisode();
		}
		
		episode.setId(id);
		episode.setTitle(title);
		episode.setOrig_title(title);
		episode.setType(titleType);
		episode.setYear(year);
		episode.setImages(parseImg(id,"episode",base.getJSONObject("image")));
		episode.setStart_year(base.getString("seriesStartYear"));
		episode.setEnd_year(base.getString("seriesEndYear"));
		if(base.containsKey("numberOfEpisodes")){
			episode.setEpisodes_count(base.getLong("numberOfEpisodes"));
		}
		
		if(episode.getSource()==null || episode.getSource() > source){
			episode.setSource(source);
		}
		
		episode.setSeason(base.getString("season"));
		if(base.containsKey("episode")){
			episode.setCurrent_episode(base.getLong("episode"));
		}
		episode.setNext_episode(base.getString("nextEpisode"));
		episode.setPrevious_episode(base.getString("previousEpisode"));
		
		
		String url = "http://www.imdb.com"+id;
		episode.setUrl(url);		
		episode.setJson_txt(json_txt);
		
		JSONObject parentTitle = base.getJSONObject("parentTitle");
		if(parentTitle != null){
			parseEpisodeBase(parentTitle,1,null);
			episode.setPid(parentTitle.getString("id"));
		}
		
		if(save){
			episode.setMatch_state(Cons.THREAD_STATE_WAIT);
			episode.setAddDef();
			episode.save();
		}else{
			episode.setUpdDef();
			episode.update();
		}
		return id;
	}

	
	//解析plot 节目简介
	private void parsePlot(String id, JSONObject plot_json){
		if(plot_json!=null){
			String type = "outline";
			JSONObject outline = plot_json.getJSONObject(type);
			if(outline!=null){
				String author = outline.getString("author");
				String text = outline.getString("text");
				
				saveOrUpdPlot(id, type, author, text,outline.toJSONString());
				
			}
			type = "summaries";
			JSONArray summaries = plot_json.getJSONArray(type);
			if(summaries!=null && !summaries.isEmpty()){
				for(int i=0;i<summaries.size();i++){
					JSONObject json = summaries.getJSONObject(i);
					
					String author = json.getString("author");
					String text = json.getString("text");
					
					saveOrUpdPlot(id, type, author, text,json.toJSONString());
				}
				
				//设置详情备注
				JSONObject json = summaries.getJSONObject(0);
				ImdbEpisode episode = ImdbEpisode.dao.findById(id);
				episode.setSummary(json.getString("text"));
				if(StringUtils.isNotBlank(episode.getSummary())){
					episode.setUpdDef();
					episode.update();
				}
				
			}	
		}
	}
	
	/**
	 * 解析活动有关的
	 * @param id 节目或剧集id
	 * @param resource
	 * @throws Exception
	 */
	private void parseEventFile(String id,JSONObject resource){
		JSONObject names  = resource.getJSONObject("names");//明星信息
		parseNames(id,names,3);
		
		//解析场景
		JSONArray scenes  = resource.getJSONArray("scenes");
		parseScene(id,scenes);
		
//		//解析场景的元素
		JSONArray events  = resource.getJSONArray("events");
		parseEvent(id, events);
		matchSceneEvent(id);
		
		//解析唱片信息
		JSONArray soundtrackAlbums  = resource.getJSONArray("soundtrackAlbums");
		parseSoundtrackAlbums(id,"event",soundtrackAlbums);
		
		//解析歌曲信息
		JSONObject soundtrackItems  = resource.getJSONObject("soundtrackItems");
		parseSoundtrackItems(id,"event",soundtrackItems);

		//解析百科
		JSONObject facts  = resource.getJSONObject("facts");
		parseFact(id,facts);
	}
	
	
	
	/**
	 * 解析场景和时间的匹配
	 * @param pid 节目或剧集id
	 * @param events 场景元素
	 */
	private void matchSceneEvent(String pid) {
		List<ImdbScene> list = ImdbScene.dao.queryByAll(pid);
		for(int i=0;i<list.size();i++){
			ImdbScene scene = list.get(i);
			
			List<ImdbEvent> events = ImdbEvent.dao.queryBySceneObj(pid,scene.getStart_time(),scene.getEnd_time());
			
			for(int j=0;j<events.size();j++){
				ImdbEvent event = events.get(j);
				event.setScene_id(scene.getId());
				
				event.setUpdDef();
				event.update();
			}
		}
	}

	/**
	 * 解析场景
	 * @param pid 节目或剧集id
	 * @param scenes 场景信息
	 */
	private void parseScene(String pid,JSONArray scenes) {
		for(int i=0;i<scenes.size();i++){
			JSONObject json  = scenes.getJSONObject(i);
			JSONArray when = json.getJSONArray("when");
			
			ImdbScene scene = ImdbScene.dao.queryByObj(pid,when.getLong(0),when.getLong(1));
			if(scene == null){
				scene = new ImdbScene();					
			}
			scene.setPid(pid);
			scene.setStart_time(when.getLong(0));
			scene.setEnd_time(when.getLong(1));
			scene.setDuration(scene.getEnd_time() - scene.getStart_time());
			scene.setTitle(json.getString("scene"));
			scene.setSummary(scene.getTitle());
			scene.setJson_txt(json.toJSONString());
		
			if(scene.getId()==null){
				scene.setMatch_state(Cons.THREAD_STATE_WAIT);
				scene.setAddDef();
				scene.save();
			}else{
				scene.setUpdDef();
				scene.update();
			}
		}
	}
	
	//解析演员信息信息
	private void parseNames(String videoId,JSONObject names,Integer source) {
		if(names!=null){
			Iterator<String> ite = names.keySet().iterator();
			while(ite.hasNext()){//补充用户信息
				String name_id = ite.next();
				JSONObject json = names.getJSONObject(name_id);
				if(json!=null){
					JSONObject base = json.getJSONObject("base");
					parseNameBase(base,source,json);
					
//					暂时不解析
//					JSONArray knownFors = json.getJSONArray("knownFor");
//					String nameId = base.getString("id");
//					parseKnowFor(knownFors, nameId);
				}
			}
		}
	}
	
	//解析人物关系
	private void parseKnowFor(JSONArray knownFors, String nameId) {
		if(knownFors !=null && !knownFors.isEmpty()){
			for(int i=0;i<knownFors.size();i++){
				JSONObject json = knownFors.getJSONObject(i);
				String videoId = json.getString("id");
				//String id, String lev,String profession, JSONObject json, String person_id, String name
				String profession = null;//导演，演员
				String character = null;//角色名
				
				JSONArray casts = json.getJSONArray("cast");
				if(casts!=null && !casts.isEmpty()){
					JSONObject cast = casts.getJSONObject(0);
					
					JSONArray characters = cast.getJSONArray("characters");//扮演角色
					if(characters!=null && !characters.isEmpty()){
						character =  characters.getString(0);
					}
					profession =  cast.getString("category");//导演，演员
				}
				
				saveOrUpdPersonVideoRelation(videoId,"episode",profession,json,nameId,character);
				parseEpisode(json);
			}
		}
	}
	
	
	//解析音乐信息
	private void parseSoundtrackAlbums(String pid,String lev,JSONArray sounds) {
		if(sounds!=null && !sounds.isEmpty()){
			for(int i=0;i<sounds.size();i++){
				JSONObject json = sounds.getJSONObject(i);
				
				String amazoneKey = parseAmazoneProductId(json.getJSONObject("productId"),"sound_album");
				if(StringUtils.isNoneBlank(amazoneKey)){
					ImdbSoundAlbum sound = ImdbSoundAlbum.dao.queryByAmazonProductKey(amazoneKey);
					if(sound == null){
						sound = new ImdbSoundAlbum();						
					}
					sound.setTitle(json.getString("albumTitle"));
					sound.setArtist(json.getString("artist"));
					sound.setR_image(parseImg(amazoneKey,"sound_album",json.getJSONObject("image")));
					sound.setProduct_key(amazoneKey);
					sound.setJson_txt(json.toJSONString());
					
					if(sound.getId() == null){
						sound.setMatch_state(Cons.THREAD_STATE_WAIT);
						sound.setAddDef();
						sound.save();
					}else{
						sound.setUpdDef();
						sound.update();
					}
					
					updAmazoneProductFid(amazoneKey,"sound_album",String.valueOf(sound.getId()));
					
					ImdbSoundAlbumVideo pp = ImdbSoundAlbumVideo.dao.queryByObj(sound.getId(),pid,lev);
					if(pp == null){				
						pp = new ImdbSoundAlbumVideo();
						pp.setSound_albums_id(sound.getId());
						pp.setVideo_id(pid);
						pp.setLev(lev);
						
						pp.setAddDef();
						pp.save();
					}
				}else{
					logDb("warn", "sound_album", "无amazoneproductId"+json.toJSONString());
				}
			}
		}
	}
	
	//解析音乐信息
	private void parseSoundtrackItems(String pid,String lev,JSONObject sounds) {
		if(sounds!=null && !sounds.isEmpty()){
			Iterator<String> ite = sounds.keySet().iterator();
			while(ite.hasNext()){
				String id = ite.next();
				JSONObject json = sounds.getJSONObject(id);
				saveOrUpdSoundItem(json);
			}
			
			ite = sounds.keySet().iterator();
			while(ite.hasNext()){//补充用户信息
				String id = ite.next();
				
				ImdbSoundItemVideo pp = ImdbSoundItemVideo.dao.queryByObj(id,pid,lev);
				if(pp == null){
					pp = new ImdbSoundItemVideo();
					pp.setSound_item_id(id);
					pp.setVideo_id(pid);
					pp.setLev(lev);	
					pp.setAddDef();
					pp.save();
				}
			}
		}
	}

	private void saveOrUpdSoundItem(JSONObject json) {
		ImdbSoundItem sound = ImdbSoundItem.dao.findById(json.getString("id"));
		boolean save = false;
		if(sound == null){
			save = true;
			sound = new ImdbSoundItem();
		}
		
		sound.setId(json.getString("id"));
		sound.setName(json.getString("name"));
		sound.setSummary(json.getString("comment"));
		
		JSONArray products = json.getJSONArray("products");
		int product_count = 0;
		List<String> amazondIdList = new ArrayList<String>();
		if(products!=null && !products.isEmpty()){
			product_count = products.size();
			String rimage = null;
			StringBuilder names = new StringBuilder();
			for(int i=0;i<product_count;i++){
				JSONObject product = products.getJSONObject(i);
				names.append(product.getString("artist"));
				if(rimage == null){//选第一张
					rimage = parseImg(sound.getId(),"sound_item_product",product.getJSONObject("image"));					
				}else{
					parseImg(sound.getId(),"sound_item_product",product.getJSONObject("image"));
				}
				
				String amazoneKey = parseAmazoneProductId(product.getJSONObject("productId"),"sound_item");
				if(StringUtils.isNotBlank(amazoneKey)){
					amazondIdList.add(amazoneKey);
				}
				if((i+1)!=product_count){
					names.append(",");
				}
			}
			sound.setR_image(rimage);
			sound.setArtist(names.toString());
		}
		sound.setProducts_count(new Long(product_count));
		if(!amazondIdList.isEmpty()){//选第一个amzone_id
			sound.setProduct_key(amazondIdList.get(0));
		}
		
		JSONArray relatedNames = json.getJSONArray("relatedNames");
		int related_name_count = 0;
		if(relatedNames!=null && !relatedNames.isEmpty()){
			related_name_count = relatedNames.size();
		}
		sound.setRelated_name_count(new Long(related_name_count));
		sound.setJson_txt(json.toJSONString());
		
		if(save){
			sound.setMatch_state(Cons.THREAD_STATE_WAIT);
			sound.setAddDef();
			sound.save();
		}else{
			sound.setUpdDef();
			sound.update();
		}
		
		updAmazoneProductFid(amazondIdList, "sound_item", sound.getId());
	}
	

	private void parseNameBase(JSONObject base,Integer source,JSONObject json) {
		String id = base.getString("id");
		ImdbPerson person = ImdbPerson.dao.findById(id);
		boolean save = false;
		if(person == null){
			save = true;
			person = new ImdbPerson();						
		}
		person.setId(id);
		person.setName(base.getString("name"));
		person.setName_en(base.getString("name"));
		person.setAka(base.getString("akas"));
		person.setAka_en(base.getString("akas"));
		person.setReal_name(base.getString("realName"));
		person.setR_avatar(parseImg(id,"person",base.getJSONObject("image")));
		person.setNickname(base.getString("nicknames"));
		person.setHeight(base.getString("heightCentimeters"));
		person.setBorn_place(base.getString("birthPlace"));
		if(person.getSource()==null || person.getSource() > source){
			person.setSource(source);
		}
		
		if(base.containsKey("birthDate")){
			person.setBirthday(new Timestamp(DateUtil.getDate(base.getString("birthDate")).getTime()));
		}
		person.setBorn_place(base.getString("birthPlace"));
		person.setHeight(base.getString("heightCentimeters"));
		
		person.setImdb_url("http://www.imdb.com"+id);
		if(json!=null){
			person.setJson_txt(json.toJSONString());
		}
		if(save){
			person.setMatch_state(Cons.THREAD_STATE_WAIT);
			person.setAddDef();
			person.save();
		}else{//修改了重新进行匹配
			person.setUpdDef();
			person.update();
		}
	}
	
	
	

	//解析导演关系
	private void parseCredits(String id,String lev,JSONObject credits) {
		if(credits!=null && !credits.isEmpty()){
			JSONArray director = credits.getJSONArray("director");
			if(director!=null && !director.isEmpty()){
				for(int i=0;i<director.size();i++){
					JSONObject json = director.getJSONObject(i);
					String person_id = json.getString("id");
					String name = json.getString("name");
					String profession = json.getString("category");
					
					saveOrUpdPersonVideoRelation(id, lev, profession, json,person_id, name);
				}
			}
		}
	}

	/**
	 * 增加关系
	 * @param id
	 * @param type
	 * @param profession
	 * @param json
	 * @param person_id
	 * @param name
	 */
	private void saveOrUpdPersonVideoRelation(String id, String lev,String profession, JSONObject json, String person_id, String name) {
		ImdbPersonVideo pp = ImdbPersonVideo.dao.queryByObj(person_id, id, profession);
		boolean save = false;
		if(pp == null){
			save = true;
			pp = new ImdbPersonVideo();
			pp.setMatch_state(Cons.THREAD_STATE_WAIT);
		}
		pp.setPerson_id(person_id);
		pp.setVideo_id(id);
		pp.setProfession(profession);
		pp.setCharacter_name(name);
		pp.setCharacter_name_en(name);
		pp.setLev(lev);
		if(json!=null){
			pp.setJson_txt(json.toJSONString());			
		}
		if(save){
			pp.setAddDef();
			pp.save();
		}else{
			pp.setUpdDef();
			pp.update();
		}
	}

	private void saveOrUpdPlot(String id, String type, String author,
			String text,String json_txt) {
		ImdbPlot plot = ImdbPlot.dao.queryByObj(id, type, author);
		boolean save = false;
		if(plot == null){
			save = true;
			plot = new ImdbPlot();
		}
		plot.setPid(id);
		plot.setType(type);
		plot.setAuthor(author);
		plot.setText(text);
		plot.setJson_txt(json_txt);
		
		if(save){
			plot.setAddDef();
			plot.save();
		}else{
			plot.setUpdDef();
			plot.update();
		}
	}

//	/**
//	 * 解析节目剧集信息
//	 * @param resource
//	 */
//	private void parseProgramEpisode(JSONObject resource) {
//		JSONObject base = resource.getJSONObject("base");
//		String id = base.getString("id");
//		
//		JSONObject parentTitle = resource.getJSONObject("parentTitle");
//		
//		JSONObject plot = resource.getJSONObject("plot");//详情简介
//		JSONObject credits = resource.getJSONObject("credits");//导演
//		JSONObject names = resource.getJSONObject("names");//演员信息
//		
//		if(parentTitle!=null){//如果有parent信息---表示是电视剧
//			String pid = parseProgramBase(parentTitle);
//			parseEpisode(pid,base);
//			
//			parsePlot(id,plot);//解析详情
//			parseCredits(id,"episode",credits);//解析导演
//			parseNames(names);//解析用户
//		}else{//如果没有表示电影
//			parseProgram(base,plot,credits,names);
//		}
//	}

	
	/**
	 * 解析场景元素
	 * @param pid 节目或剧集id
	 * @param events 场景元素
	 */
	private void parseFact(String pid,JSONObject facts) {
		if(facts == null){
			return;
		}
		Iterator ite = facts.keySet().iterator();
		while(ite.hasNext()){
			String imdb_id = (String)ite.next();
			JSONObject json  = facts.getJSONObject(imdb_id);
			ImdbFact obj = ImdbFact.dao.queryByImdbId(imdb_id);
			if(obj == null){
				obj = new ImdbFact();
			}
			obj.setImdb_id(imdb_id);
			obj.setType(json.getString("factType"));
			obj.setPid(pid);
			obj.setText(json.getString("text"));
			obj.setJson_txt(json.toJSONString());
			if(obj.getId() == null){
				obj.setAddDef();
				obj.save();
			}else{
				obj.setUpdDef();
				obj.update();
			}
			
			JSONArray relatedTitles = json.getJSONArray("relatedTitles");
			parseRelatedTitle(imdb_id,relatedTitles);
			
			JSONArray relatedNames = json.getJSONArray("relatedNames");
			parseRelatedName(imdb_id,relatedNames);
			
		}
	}

	private void parseRelatedName(String id,JSONArray relatedNames) {
		if(relatedNames!=null && !relatedNames.isEmpty()){
			for(int j=0;j<relatedNames.size();j++){
				JSONObject name = relatedNames.getJSONObject(j);
				
				parseNameBase(name,4,null);
				
				String name_id = name.getString("id");
				ImdbFactName fact_title = ImdbFactName.dao.queryByObj(id,name_id);
				if(fact_title == null){
					fact_title = new ImdbFactName();
				}
				fact_title.setFact_id(id);
				fact_title.setName_id(name_id);
				fact_title.setJson_txt(name.toJSONString());
				
				if(fact_title.getId() == null){
					fact_title.setAddDef();
					fact_title.save();
				}else{
					fact_title.setUpdDef();
					fact_title.update();
				}
			}
		}
	}
	
	private void parseRelatedTitle(String id,JSONArray relatedTitles) {
		if(relatedTitles!=null && !relatedTitles.isEmpty()){
			for(int j=0;j<relatedTitles.size();j++){
				JSONObject title = relatedTitles.getJSONObject(j);
				String title_id = title.getString("id");
				
				parseEpisodeBase(title,4,null);
				
				ImdbFactTitle fact_title = ImdbFactTitle.dao.queryByObj(id,title_id);
				if(fact_title == null){
					fact_title = new ImdbFactTitle();
				}
				
				fact_title.setFact_id(id);
				fact_title.setTitle_id(title_id);
				fact_title.setJson_txt(title.toJSONString());
				
				if(fact_title.getId() == null){
					fact_title.setAddDef();
					fact_title.save();
				}else{
					fact_title.setUpdDef();
					fact_title.update();
				}
			}
		}
	}
	

	/**
	 * 解析amazone的产品id
	 * @param image
	 * @return
	 */
	private String parseAmazoneProductId(JSONObject product,String type){
		if(product!=null){
			String amazonProductKey = product.getString("key");
			
			ImdbProduct obj = ImdbProduct.dao.findById(amazonProductKey);
			boolean save = false;
			if(obj == null){
				save = true;
				obj = new ImdbProduct(); 
			}
			obj.setId(amazonProductKey);
			obj.setAmazon_marketplace_id(product.getString("amazonMarketplaceId"));
			obj.setType(type);
			obj.setKey(product.getString("key"));
			obj.setKey_type(product.getString("keyType"));
			obj.setRegion(product.getString("region"));
			
			if(save){
				obj.setAddDef();
				obj.save();
			}else{
				obj.setUpdDef();
				obj.update();
			}
			return amazonProductKey;
		}
		return null;
	}
	
	/**
	 * 日志记录
	 * @param lev
	 * @param type
	 * @param txt
	 */
	private void logDb(String lev,String type,String txt){
		ImdbLog obj = new ImdbLog();
		obj.setLev(lev);
		obj.setType(type);
		obj.setTxt(txt);
		obj.setAddDef();
		obj.save();
	}
	
	private void updAmazoneProductFid(List<String> amazonMarketplaceIds,String type,String fid){
		ImdbProduct obj = new ImdbProduct();
		for(int i=0;i<amazonMarketplaceIds.size();i++){
			obj.setId(amazonMarketplaceIds.get(i));
			obj.setFid(fid);
			obj.setType(type);
			obj.setUpdDef();
			obj.update();
		}
	}
	
	private void updAmazoneProductFid(String amazonMarketplaceId,String type,String fid){
		ImdbProduct obj = new ImdbProduct();
		obj.setId(amazonMarketplaceId);
		obj.setFid(fid);
		obj.setType(type);
		obj.setUpdDef();
		obj.update();
	}
	
	/**
	 * 解析图片
	 * @param image
	 * @return
	 */
	private String parseImg(String fid,String type,JSONObject image){
		if(image!=null){
			String url = image.getString("url");
			
			ImdbImage obj = ImdbImage.dao.queryByUrl(fid,type,url);
			if(obj == null){
				obj = new ImdbImage(); 
				obj.setRid(image.getString("id"));
				obj.setFid(fid);
				obj.setType(type);
				obj.setUrl(url);
				obj.setHeight(image.getLong("height"));
				obj.setWidth(image.getLong("width"));
				
				obj.setAddDef();
				obj.save();
			}
			return url;
		}
		return null;
	}
	
	/**
	 * 解析场景元素
	 * @param pid 节目或剧集id
	 * @param events 场景元素
	 */
	private void parseEvent(String pid,JSONArray events) {
		for(int i=0;i<events.size();i++){
			JSONObject json  = events.getJSONObject(i);
			JSONArray when = json.getJSONArray("when");
			
			String fid = null;
			String type = null;
			String c1 = null;
			
			//人员有关信息
			if(json.containsKey("actor")){
				fid = json.getString("actor");//用户id
				c1 = json.getString("character");//扮演角色
				
				saveOrUpdPersonVideoRelation(pid,"event","actor",json,fid,c1);
				
				type = "person";
			}else if(json.containsKey("soundtrackItemId")){//音乐信息
				type = "music";
				fid = json.getString("soundtrackItemId");
			}else if(json.containsKey("factId")){//百科信息
				type = "fact";
				fid = json.getString("factId");
			}else{//对于元素没有处理好
				log.warn("对于这样的元素没有处理="+json.toJSONString());
				logDb("warn", "event", pid+"对于这样的元素没有处理="+json.toJSONString());
			}
			
			if(type!=null){
				ImdbEvent obj = ImdbEvent.dao.queryByObj(pid,fid,type,when.getLong(0),when.getLong(1));
				if(obj == null){
					obj = new ImdbEvent();
				}
				obj.setFid(fid);
				obj.setC1(c1);
				obj.setPid(pid);
				obj.setType(type);
				obj.setStart_time(when.getLong(0));
				obj.setEnd_time(when.getLong(1));
				obj.setDuration(obj.getEnd_time() - obj.getStart_time());
				obj.setJson_txt(json.toJSONString());
				
				if(obj.getId() == null){
					obj.setMatch_state(Cons.THREAD_STATE_WAIT);
					obj.setAddDef();
					obj.save();
				}else{
					obj.setUpdDef();
					obj.update();
				}
			}
		}
	}
	
	/**
	 * 文件遍历  安节目，剧集 事件排序
	 * @param absPath 跟路径
	 * @param fs 文件夹列表
	 * @param msg 返回的提示信息
	 * @return
	 */
	public List<File> listFile(String absPath,File[] fs,StringBuilder msg){
		List<File> program = new ArrayList<File>();
		if(fs!=null){
			List<File> episode = new ArrayList<File>();
			List<File> event = new ArrayList<File>();
			List<File> fileList = new ArrayList<File>();
			for(int i=0;i<fs.length;i++){
				File f = fs[i];
				if(f.isFile()){//文件
					String name = f.getName();
					if(name.startsWith("program")){//节目
						program.add(f);
					}else if(name.startsWith("episode")){//剧集
						episode.add(f);
					}else if(name.startsWith("event")){//事件
						event.add(f);
					}else{
						msg.append(f.getAbsolutePath().replace(absPath,"")+" 不能匹配，请安规范命名").append("\r\n");
					}
				}else{//文件夹
					fileList.add(f);
				}
			}
			program.addAll(episode);
			program.addAll(event);
			program.addAll(fileList);
		}
		return program;
	}
	
	private String getTipFileName(String absPath, File file) {
		String tip_file_name = file.getAbsolutePath().replace(absPath,"");
		return tip_file_name;
	}
	
	/***
	 * 获取所有的剧集信息
	 * @param root_program_id
	 */
	private void getImdbAllEpisode(String root_program_id) {
		ImdbSpilder spilder = new ImdbSpilder();
		try {
			List<ImdbEpisode> list = spilder.getAllEpisodeBySearizeId(root_program_id);
			if(list!=null && !list.isEmpty()){
				for(int i=0;i<list.size();i++){
					ImdbEpisode obj = list.get(i);
					ImdbEpisode db = ImdbEpisode.dao.findById(obj.getId());
					if(db == null){
						obj.setSource(1);
						obj.setMatch_state(Cons.THREAD_STATE_WAIT);
						obj.setAddDef();
						obj.save();
					}else{
						obj.setSource(1);
						obj.setUpdDef();
						obj.update();
					}
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(),e);	
		}
	}
	
//	//通过时间匹配
//	public SceneWarp getAdapterScene(Long start,Long end,List<SceneWarp> scenelist){
//		for(int i=0;i<scenelist.size();i++){
//			SceneWarp scenwarp = scenelist.get(i);
//			Scene scene = scenwarp.getScene();
//			if(scene.getStart_time()<=start && end < scene.getEnd_time()){
//				return scenwarp;
//			}
//		}
//		return null;
//	}
//	public static void printStrArr(String[] srr){
//		for(int i=0;i<srr.length;i++){
//			System.out.print(srr[i]);
//			System.out.print(",");
//		}
//		System.out.println();
//	}
	//设置公共参数
//		private void setElementProgram(Long program_id, Long episode_id,
//				Element element) {
//			element.setProgram_id(program_id);
//			element.setEpisode_id(episode_id);
//		}
		
//	//解析剧集
//	private String parseProgramBase(JSONObject base){
//		parseProgram(base,null,null,null);
//		return base.getString("id");
//	}
//	/**
//	 * 解析节目信息
//	 * @param base
//	 * @param plot
//	 * @param credits
//	 * @param names
//	 */
//	private void parseProgram(JSONObject base,JSONObject plot,JSONObject credits,JSONObject names){
//		String id = base.getString("id");
//		String title = base.getString("title");
//		String titleType = base.getString("titleType");
//		String year = base.getString("year");
//		
//		ImdbProgram program = ImdbProgram.dao.findById(id);
//		boolean save = false;
//		if(program == null){
//			save = true;
//			program = new ImdbProgram();
//		}
//		
//		program.setId(id);
//		program.setTitle(title);
//		program.setOrig_title(title);
//		program.setType(titleType);
//		program.setYear(year);
//		program.setImages(parseImg(base.getJSONObject("image")));
//		program.setStart_year(base.getString("seriesStartYear"));
//		program.setEnd_year(base.getString("seriesEndYear"));
//		program.setEpisodes_count(base.getLong("numberOfEpisodes"));
//		String url = "http://www.imdb.com"+id;
//		program.setUrl(url);		
//		
//		
//		if(save){
//			program.setAddDef();
//			program.save();
//		}else{
//			program.setUpdDef();
//			program.update();
//		}
//		
//		//解析详情
//		parsePlot(id,plot);
//		
//		//演员信息
//		parseNames(names);
//		
//		//解析导演
//		parseCredits(id,"program",credits);
//	}
	
}

