/**
 * zhiping.tv Inc.
 * Copyright (c) 2010-2014 All Rights Reserved.
 */
package tv.zhiping.util;

/**
 * 
 * @author kaifeng.shi
 * @version $Id: JsonUtil.java, v 0.1 2014年8月24日 下午10:52:14 root Exp $
 */

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
    /**
     * GET请求 ,默认采用UTF-8编码
     * 
     * @param url
     *            待请求的URL
     * @return String 返回响应结果字符串
     * 
     */
    @SuppressWarnings("null")
    public static  Map<String, Object> doGet(String url) {
        HttpClient client = new DefaultHttpClient();
        Map<String, Object> strResult = null;
        try {
            HttpGet httpget = new HttpGet(url);
            HttpResponse httpResponse = client.execute(httpget);
            // 取得返回的字符串
            HttpEntity httpEntity = httpResponse.getEntity();
            strResult.put("httpResponse", httpResponse);
            strResult.put("httpEntity", httpEntity);
         //   strResult = EntityUtils.toString(response);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return strResult;
    }
}
