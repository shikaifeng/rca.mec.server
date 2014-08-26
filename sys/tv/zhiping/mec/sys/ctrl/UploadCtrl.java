package tv.zhiping.mec.sys.ctrl;

import java.io.File;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.common.util.ComUtil;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.utils.FileUtil;

import com.jfinal.upload.UploadFile;

/**
 * 文件上传
 * @author 张有良
 * @version 1.0
 * @since 2012-01-09
*/
public class UploadCtrl extends SysBaseCtrl{
	
	public void sign_save(){
		UploadFile uploadFile = getFile("upload");//Cons.UPLOAD_TMP
		File file = uploadFile.getFile();
		
		String path = file.getParent().replace("\\","/")+"/"+ComUtil.getRandomFileName(file.getName());
		FileUtil.copy(file,path);
		
		path = path.replace(CacheFun.getConVal(ConfigKeys.ST_LOCAL_FOLDER),"");
		
		System.out.println(file.getAbsolutePath().replace("\\","/"));
		System.out.println(CacheFun.getConVal(ConfigKeys.ST_LOCAL_FOLDER));
		
		path = ComUtil.getStHttpPath(path);
		
		String str = null;
		String msg = null;
		Long w = 0L;
		Long h = 0L;
		str = "{\"path\":\""+path+"\",\"size\":\""+file.length()+"\",\"msg\":\""+msg+"\",\"w\":\""+w+"\",\"h\":\""+h+"\"}";
		
		renderJson(str);
	}
}
