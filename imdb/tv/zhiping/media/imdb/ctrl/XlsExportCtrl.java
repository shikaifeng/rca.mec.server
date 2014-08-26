package tv.zhiping.media.imdb.ctrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import tv.zhiping.common.ConfigKeys;
import tv.zhiping.common.cache.CacheFun;
import tv.zhiping.mec.feed.model.ImdbFact;
import tv.zhiping.mec.sys.jfinal.SysBaseCtrl;
import tv.zhiping.media.model.ImdbMergeSound;
import tv.zhiping.media.model.ImdbPerson;
import tv.zhiping.utils.FileUtil;
import tv.zhiping.utils.XlsUtil;

/**
 * imdb的导入
 * @author 张有良
 */

public class XlsExportCtrl extends SysBaseCtrl{	
	/**
	 * App系统配置 index
	 * */
	public void index(){
		renderJsp("/page/imdb/xlsExport/input.jsp");
	}
	
	public void save() throws IOException{
		String type = getPara("type");
		String path = null;
		if("imdb_fact".equals(type)){
			path = exportImdbFact();
		}else if("imdb_person_match_error".equals(type)){
			path = exportImdbPersonMatchError();
		}else if("imdb_sound_match_error".equals(type)){
			path = exportImdbSoundMatchError();
		}
		renderFile(new File(path));
	}

	/**
	 * 歌曲匹配失败的
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String exportImdbSoundMatchError() throws FileNotFoundException, IOException {
		List<ImdbMergeSound> list = ImdbMergeSound.dao.queryMatchErrorAll();
		
		List<Object[]> datas = new ArrayList<Object[]>();
		if(list!=null && !list.isEmpty()){
			ImdbMergeSound obj = null;
			for(int i=0;i<list.size();i++){
				obj = list.get(i);
				
				String[] srr = new String[7];
				
				srr[0] = String.valueOf(obj.getId());
				srr[1] = obj.getName();
				srr[2] = obj.getR_image();
				srr[3] = obj.getArtist();
				srr[4] = obj.getProduct_key();
				srr[5] = obj.getSummary();
				srr[6] = obj.getMsg();
				datas.add(srr);
			}
		}
		
		OutputStream os = null;
		
		String path = CacheFun.getConVal(ConfigKeys.UPLOAD_TEMP_FOLDER)+"imdb_sound_match_error.xls";
		try{
			FileUtil.createParentFilePath(new File(path));
			os = new FileOutputStream(path);
			
			String[] heads = {"id","名称","海报","作者","amazone_product_key","介绍","错误原因"};
			XlsUtil.downXlsData(heads,datas,os);
		}finally{
			if(null != os){
				os.flush();
				os.close();
				os = null;
			}
		}
		return path;
	}
	
	/**
	 * 人物匹配失败的
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String exportImdbPersonMatchError() throws FileNotFoundException, IOException {
		List<ImdbPerson> list = ImdbPerson.dao.queryMatchErrorAll();
		
		List<Object[]> datas = new ArrayList<Object[]>();
		if(list!=null && !list.isEmpty()){
			ImdbPerson obj = null;
			for(int i=0;i<list.size();i++){
				obj = list.get(i);
				
				String[] srr = new String[6];
				
				srr[0] = obj.getId();
				srr[1] = "";
				srr[2] = obj.getName();
				srr[3] = obj.getR_avatar();
				srr[4] = obj.getImdb_url();
				srr[5] = obj.getMsg();
				datas.add(srr);
			}
		}
		
		OutputStream os = null;
		String path = CacheFun.getConVal(ConfigKeys.UPLOAD_TEMP_FOLDER)+"imdb_person_match_error.xls";
		try{
			FileUtil.createParentFilePath(new File(path));
			os = new FileOutputStream(path);
			
			String[] heads = {"id","媒资库id 新增请用-1","名称","头像","imdb_url","错误原因"};
			XlsUtil.downXlsData(heads,datas,os);
		}finally{
			if(null != os){
				os.flush();
				os.close();
				os = null;
			}
		}
		return path;
	}
	
	/**
	 * 人物匹配失败的
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String exportImdbFact() throws FileNotFoundException, IOException {
		List<ImdbFact> list = ImdbFact.dao.queryAll();
		
		List<Object[]> datas = new ArrayList<Object[]>();
		if(list!=null && !list.isEmpty()){
			ImdbFact obj = null;
			for(int i=0;i<list.size();i++){
				obj = list.get(i);
				
				String[] srr = new String[3];
				
				srr[0] = String.valueOf(obj.getId());
				srr[1] = obj.getType();
				srr[2] = obj.getText();
				datas.add(srr);
			}
		}
		
		OutputStream os = null;
		String path = CacheFun.getConVal(ConfigKeys.UPLOAD_TEMP_FOLDER)+"/"+"imdb_fact.xls";
		try{
			FileUtil.createParentFilePath(new File(path));
			os = new FileOutputStream(path);
			
			String[] heads = {"id","类型","英文内容","中文内容"};
			XlsUtil.downXlsData(heads,datas,os);
		}finally{
			if(null != os){
				os.flush();
				os.close();
				os = null;
			}
		}
		return path;
	}
	
	
	/**
	 * 百科
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String exportImdbFact2() throws FileNotFoundException, IOException {
		List<ImdbFact> list = ImdbFact.dao.queryAll();
		
		List<Object[]> datas = new ArrayList<Object[]>();
		
		if(list!=null && !list.isEmpty()){
			String key = "";
			String prevText = null;
			
			ImdbFact obj = null;
			for(int i=0;i<list.size();i++){
				obj = list.get(i);
				if(prevText == null){
					prevText = obj.getText();
				}
				
				if(!prevText.equals(obj.getText())){
					parseXlsColumn(datas, key, prevText, obj);
					
					prevText = obj.getText();
					key = String.valueOf(obj.getId());
				}else{
					key = key +","+obj.getId();
				}
			}
			
			//最后一个数据特殊处理
			if(obj!=null){
				parseXlsColumn(datas, key, prevText, obj);
			}
		}
		
		OutputStream os = null;
		String path = CacheFun.getConVal(ConfigKeys.UPLOAD_TEMP_FOLDER)+"imdb_fact.xls";
		try{
			FileUtil.createParentFilePath(new File(path));
			os = new FileOutputStream(path);
			
			String[] heads = {"id","英文内容","中文内容"};
//			XlsUtil.setHead("imdb_fact.xls", response);
			XlsUtil.downXlsData(heads,datas,os);
		}finally{
			if(null != os){
				os.flush();
				os.close();
				os = null;
			}
		}
		return path;
	}

	private void parseXlsColumn(List<Object[]> datas, String key,
			String prevText, ImdbFact obj) {
		String[] srr = new String[4];//把上一轮的信息先保存
		if(key.startsWith(",")){
			key = key.substring(1);
		}
		srr[0] = key;
		srr[1] = obj.getType();
		srr[2] = prevText;
		srr[3] = "";
		datas.add(srr);
	}
}

