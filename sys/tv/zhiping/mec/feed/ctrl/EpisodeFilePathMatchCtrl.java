package tv.zhiping.mec.feed.ctrl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.utils.FileUtil;

/**
 * imdb的导入
 * @author 张有良
 */

public class EpisodeFilePathMatchCtrl extends SysBaseCtrl{	
	/**
	 * App系统配置 index
	 * */
	public void index(){
		renderJsp("/page/util/episode/filePathMatch/input.jsp");
	}
	
	public void save() throws IOException{
		String str = getPara("name_en");
		if(StringUtils.isNotBlank(str)){
			Map<String,String>pathMap = new HashMap<String,String>(); 
			getAllPath(pathMap,new File(ROOT_PATH));//获取路径信息
			
			String[] srr = str.replace("，", ",").split(",");
			List<Episode>list = Episode.dao.queryByNameEns(srr);
			for(int i=0;i<list.size();i++){
				Episode obj = list.get(i);
				Program program = Program.dao.findById(obj.getPid());
				
				String path = getFilePath(obj,program,pathMap);
				if(StringUtils.isNotBlank(path)){
					obj.setFile_path(path);
					obj.setUpdDef();
					obj.update();
				}else{
//					System.out.println(obj.getId()+" "+program.getTitle()+"  "+program.getCurrent_season()+"  "+obj.getCurrent_episode()+" ="+path);
				}
			}
		}
		renderText("搞定");
	}
	
	/**
	 * 获取文件路径
	 * @param obj
	 * @param program
	 * @param pathMap
	 * @return
	 */
	private String getFilePath(Episode episode, Program program,Map<String, String> pathMap) {
		String program_title = program.getTitle();
		
		Iterator<String> ite = pathMap.keySet().iterator();
		while(ite.hasNext()){
			String path = ite.next();
			if(path.indexOf("《"+program_title+"》")>-1){
				String key = getSeasonEpisodeKey(program.getCurrent_season(),episode.getCurrent_episode());
				if(path.indexOf(key)>-1){
					return path;
				}
				
				key = getLowerSeasonEpisodeKey(program.getCurrent_season(),episode.getCurrent_episode());
				if(path.indexOf(key)>-1){
					return path;
				}
			}
		}
		return null;
	}
	
	public String getLowerSeasonEpisodeKey(Long current_season,Long current_episode){
		String key = "s";
//		01E13
		if(current_season<10L){
			key = key+"0";
		}
		key = key + current_season;
		key = key+"e";
		if(current_episode<10L){
			key = key+"0";
		}
		key = key + current_episode;
		return key;
	}
	
	public String getSeasonEpisodeKey(Long current_season,Long current_episode){
		String key = "S";
//		01E13
		if(current_season<10L){
			key = key+"0";
		}
		key = key + current_season;
		key = key+"E";
		if(current_episode<10L){
			key = key+"0";
		}
		key = key + current_episode;
		return key;
	}

	public static final String ROOT_PATH= "G:/";
	
	public static void main(String[] args) {
//		
//		File f = new File(ROOT_PATH);
//		System.out.println(f.list().length);
//		if(true){
//			return;
//		}
		Map<String,String>pathMap = new HashMap<String,String>(); 
		getAllPath(pathMap,new File(ROOT_PATH));
		
		Iterator<String> ite = pathMap.keySet().iterator();
		while(ite.hasNext()){
			String name = ite.next();
			String path = pathMap.get(name);
			System.out.println(name+"==================="+path);
		}
	}
	
	
	
	
	public static void getAllPath(Map<String,String>pathMap,File file){
		if(file.isFile()){
			String fileName = file.getName();
			String sufix = FileUtil.getFileSuffix(fileName);
			if(sufix.equalsIgnoreCase("mkv") || sufix.equalsIgnoreCase("avi") || sufix.equalsIgnoreCase("flv")){
				pathMap.put(file.getAbsolutePath().replace("\\","/").replace(ROOT_PATH,""),"'");
			}	
		}else{
			File[] fs = file.listFiles();
			if(fs!=null){
				for(int i=0;i<fs.length;i++){
					getAllPath(pathMap,fs[i]);
				}
			}
		}
	}
}

