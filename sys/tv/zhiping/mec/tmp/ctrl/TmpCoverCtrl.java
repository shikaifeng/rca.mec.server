package tv.zhiping.mec.tmp.ctrl;

import java.io.File;
import java.util.List;

import tv.zhiping.mdm.model.Episode;
import tv.zhiping.mdm.model.Person;
import tv.zhiping.mdm.model.Program;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.utils.FileUtil;

/**
 * imdb的匹配
 * @author 张有良
 */

public class TmpCoverCtrl extends SysBaseCtrl{	
	/**
	 * App系统配置 index
	 * */
	public void index(){
		String abs_root_folder = "d:/";
		
//		select * from person where avatar like 'images/ib%'
//	    select * from program where cover like 'images/ib%'
//	    select * from episode where cover like 'images/ib%'
		processMovie(abs_root_folder);
		renderText("ok");
	}
	
	private void processMovie(String abs_root_folder) {
		String sql = "select * from episode where cover like 'images/ib%'";
		List<Episode> list = Episode.dao.find(sql);
		for(int i=0;i<list.size();i++){
			Episode episode = list.get(i);
			
			String absSrcAvatar = abs_root_folder+"/"+episode.getCover();
			//images/program/2008/132735/cover/1402024162.5.jpg
			String tarAvatar = "images/program/"+episode.getYear()+"/"+episode.getId()+"/cover/";
			
			File file = new File(absSrcAvatar);
			tarAvatar = tarAvatar+file.getName();
			
			String absTargetAvatar = abs_root_folder+"/"+tarAvatar;
			
			FileUtil.createParentFilePath(new File(absTargetAvatar));
			FileUtil.copy(absSrcAvatar, absTargetAvatar);
			
			episode.setCover(tarAvatar);
			episode.update();
			
			System.out.println(episode.getId()+" "+episode.getCover()+"  "+absTargetAvatar);
			System.out.println(episode.getCover());
		}
	}
	
	private void processProgram(String abs_root_folder) {
		String sql = "select * from program where cover like 'images/ib%'";
		List<Program> list = Program.dao.find(sql);
		for(int i=0;i<list.size();i++){
			Program program = list.get(i);
			
			String absSrcAvatar = abs_root_folder+"/"+program.getCover();
			//images/program/2008/132735/cover/1402024162.5.jpg
			String tarAvatar = "images/program/"+program.getYear()+"/"+program.getId()+"/cover/";
			
			File file = new File(absSrcAvatar);
			tarAvatar = tarAvatar+file.getName();
			
			String absTargetAvatar = abs_root_folder+"/"+tarAvatar;
			
			FileUtil.createParentFilePath(new File(absTargetAvatar));
			FileUtil.copy(absSrcAvatar, absTargetAvatar);
			
			program.setCover(tarAvatar);
			program.update();
			
			System.out.println(program.getId()+" "+program.getCover()+"  "+absTargetAvatar);
			System.out.println(program.getCover());
		}
	}

	private void processPerson(String abs_root_folder) {
		String sql = "select id,avatar from person where avatar like 'images/ib%'";
		List<Person> list = Person.dao.find(sql);
		for(int i=0;i<list.size();i++){
			Person person = list.get(i);
			
			String absSrcAvatar = abs_root_folder+"/"+person.getAvatar();
			String tarAvatar = "images/person/"+person.getId()+"/avatar/";
			File file = new File(absSrcAvatar);
			tarAvatar = tarAvatar+file.getName();
			
			String absTargetAvatar = abs_root_folder+"/"+tarAvatar;
			
			FileUtil.createParentFilePath(new File(absTargetAvatar));
			FileUtil.copy(absSrcAvatar, absTargetAvatar);
			
			person.setAvatar(tarAvatar);
			person.update();
			
			System.out.println(person.getId()+" "+person.getAvatar()+"  "+absTargetAvatar);
			System.out.println(person.getAvatar());
		}
	}
	
	
	
	
	
	
}

