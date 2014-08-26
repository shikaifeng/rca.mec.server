package tv.zhiping.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


/**
 * http client 的一些工具
 * @author 张有良
 * @version 1.0
 * @since 2012-06-20
 */
public class HttpClientUtils {
	//设置post参数
	public static void setStrHttpParam(String name,String value,List<NameValuePair> params){
		if(StringUtils.isNotBlank(value)){
			params.add(new BasicNameValuePair(name,value));
		}
	}
	
	//设置post参数数据
	public static void setArr2HttpParam(String name,String[] values,List<NameValuePair> params){
		if(values!=null){
			int size = values.length;
			for(int i=0;i<size;i++){
				if(StringUtils.isNotBlank(values[i])){
					params.add(new BasicNameValuePair(name,values[i]));
				}
			}
		}
	}
	
	//得到请求后的值
	/**
	 * @param url  请求的url
	 * @param charset 请求的url的字符集  接收的字符集是不是也一样的？
	 * @param params  参数
	 * @return
	 * @throws Exception
	 */
	public static String postStr(String url,String charset,List<NameValuePair> params) throws Exception{
		HttpPost post = new HttpPost(url);
		if(params!=null && !params.isEmpty()){
			post.setEntity(new UrlEncodedFormEntity(params,charset));
		}
		HttpClient http = new DefaultHttpClient();
		
		StringBuilder result = new StringBuilder();
		try {
			HttpResponse response = http.execute(post);
			HttpEntity entityRsp = response.getEntity();
			BufferedReader rd = new BufferedReader(new InputStreamReader(entityRsp.getContent(),charset));
			String tempLine = null;
			while ((tempLine = rd.readLine()) != null) {
				result.append(tempLine);
			}
		} catch (Exception e) {
			throw e;
		}finally{// 释放连接  
			http.getConnectionManager().shutdown();			
		}
		return result.toString();
	}

	//失败重复次数
	public final static Integer ERROR_REPEAT_NUM = 3;
	
	//自动重试
	public static String getRenewPostStr(String url, String charset,List<NameValuePair> params) throws Exception {
		return getRenewPostStr(url,charset,params,500L);
	}
	
	//自动重试
	public static String getRenewPostStr(String url, String charset,List<NameValuePair> params,long sleepTime) throws Exception {
		for(int i=0;i<ERROR_REPEAT_NUM;i++){
			try {
				return postStr(url,charset,params);
			} catch (Exception e) {
				if((i+1)>=ERROR_REPEAT_NUM){
					throw e;
				}
				Thread.sleep(sleepTime);
			}
		}
		return null;
	}
	
	public static String getStr(String url,String charset) throws Exception {  
		BufferedReader in = null;
        String content = null;
        try {
            // 定义HttpClient  
            HttpClient client = new DefaultHttpClient();  
            // 实例化HTTP方法  
            HttpGet request = new HttpGet();  
            request.setURI(new URI(url));  
            HttpResponse response = client.execute(request);  
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),charset));  
            StringBuilder sb = new StringBuilder();  
            String line = "";  
            while ((line = in.readLine()) != null) {  
                sb.append(line);  
            }  
            in.close();  
            content = sb.toString();  
        }finally {
        	if(in!=null){
        		in.close();
        	}
        }
        return content;
    }
	  
}
