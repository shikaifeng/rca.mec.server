package tv.zhiping.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import tv.zhiping.utils.FileUtil;

public class HttpUtil {
	public static boolean isAjaxReq(HttpServletRequest request) {
		String head = request.getHeader("accept");
		if (StringUtils.isNoneBlank(head)
				&& head.indexOf("application/json") > -1) {
			return true;
		}

		head = request.getHeader("X-Requested-With");
		if (StringUtils.isNoneBlank(head)
				&& head.indexOf("XMLHttpRequest") > -1) {
			return true;
		}
		return false;
	}
	
	//获取客户端ip
	public static String getClientIp(HttpServletRequest request){
		if (request != null) {
			String ip = request.getHeader("X-Forwarded-For");//反向代理X-Forwarded-For
			if(StringUtils.isBlank(ip)){
				return request.getRemoteAddr();	
			}else{
				return ip;
			}
		}
		return null;
	}
	
	/**
	 * 得到重定向后的地址
	 * @param url
	 * @return 
	 * @throws IOException 
	 */
	public static String getRedirectUrl(String path) throws Exception{
		HttpURLConnection conn = null;
		String result = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection)url.openConnection();
			conn.getResponseCode();
			result = conn.getURL().toString();
		} catch (Exception e) {
			throw e;
		}finally{
			if(conn!=null){
				conn.disconnect();				
			}
		}
		return result;
	}
	
	/**
	   * 下载图片
	   * @param strUrl
	   * @param path
	   * @throws Exception
	   */
	public static void download(String strUrl,String parent,String path) throws Exception{
		URL url = null;
		InputStream is = null;
		FileOutputStream os = null;
		try {
			url = new URL(strUrl);
			is = url.openStream();
			
			String filePath = parent+"/"+path;
			FileUtil.createParentFilePath(new File(filePath));
			os = new FileOutputStream(filePath);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while((bytesRead = is.read(buffer,0,8192))!=-1){
				os.write(buffer,0,bytesRead);
			}
		}catch (Exception e) {
			throw e;
		}finally{
			try {
				if(os!=null){
					os.close();
				}
			}catch (IOException e) {
    		  e.printStackTrace();
			}
			try {
				if(is!=null){
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
      }
	  }

    public static String doGet(String string) {
        return null;
    }
}
