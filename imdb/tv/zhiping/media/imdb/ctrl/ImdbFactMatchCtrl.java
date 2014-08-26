package tv.zhiping.media.imdb.ctrl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.mec.feed.ctrl.FeedBaseCtrl;
import tv.zhiping.mec.feed.model.ImdbFact;
import tv.zhiping.utils.FileUtil;
import tv.zhiping.utils.ValidateUtil;
import tv.zhiping.utils.XlsUtil;

import com.jfinal.upload.UploadFile;

/**
 * imdb的百科中文匹配
 * @author 张有良
 */

public class ImdbFactMatchCtrl extends FeedBaseCtrl{	
	/**
	 * App系统配置 index
	 * */
	public void index(){
		renderJsp("/page/imdb/imdbFactMatch/input.jsp");
	}
	
	
	public void save() throws IOException{
		StringBuilder msg = new StringBuilder();
		UploadFile uploadFile = getFile("upload");
		if(uploadFile!=null){
			File file = uploadFile.getFile();
			try {
				String originalFileName = uploadFile.getOriginalFileName();
				String fileSuffix =FileUtil.getFileSuffix(originalFileName);
				
				if("xls".equalsIgnoreCase(fileSuffix)){//压缩文件
					List<String[]> list = XlsUtil.getXlsData(file);
					
					if(list!=null && !list.isEmpty()){
						list.remove(0);
						
						int index = 1;
						String[] srr = null;
						ImdbFact obj = new ImdbFact();
						for(int i=0;i<list.size();i++){
							index ++;
							srr = list.get(i);
							
							if(checkMsg(index,srr,msg)){
								obj.setId(new Long(srr[0]));									
								obj.setText_zh(srr[3]);
								obj.setUpdDef();
								obj.update();
							}
						}						
					}
				}else{
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
			msg.append("搞定");
		}
		renderText(msg.toString());
	}
	
	
	/**
	 * 校验数据
	 * @param srr
	 * @return
	 */
	public boolean checkMsg(int index,String[] srr,StringBuilder msg){
		boolean flag = true;
		if(srr.length>3 ){
			if(StringUtils.isBlank(srr[0])){
				flag = false;
				msg.append("第"+index+"行,id数据不能为空，请检查").append("\r\n");
			}
			
			if(!ValidateUtil.isNum(srr[0])){
				flag = false;
				msg.append("第"+index+"行,id数据只能是数字，请检查").append("\r\n");
			}
			
			if(StringUtils.isBlank(srr[3])){
				flag = false;
				msg.append("第"+index+"行,中文内容不能为空，请检查").append("\r\n");
			}
		}else{
			flag = false;
			msg.append("第"+index+"行,数据错误，请检查").append("\r\n");
		}
		return flag;
	}
}

