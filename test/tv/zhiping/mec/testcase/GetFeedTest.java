/**
 * zhiping.tv Inc.
 * Copyright (c) 2010-2014 All Rights Reserved.
 */
package tv.zhiping.mec.testcase;

/**
 * 
 * @author kaifeng.shi
 * @version $Id: JsonUtil.java, v 0.1 2014年8月24日 下午10:52:14 Exp $
 * 
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import au.com.bytecode.opencsv.CSVReader;
import tv.zhiping.mec.testdata.DataProviderTest;
import tv.zhiping.util.JsonUtil;

public class GetFeedTest {
    private String name = this.getClass().getName();
    /**
     *天猫魔盒实时获取feed接口校验
     * 
     * @param 用例名称、描述和构造url请求的参数
     * url示例:http://192.168.0.110:9080/api/v1/get_feeds?type=nolive&reference_id=112804&start_time=480000&udid=3307
     *            
     */
    @Test(dataProvider = "test1", dataProviderClass = DataProviderTest.class)
    public void getFeedTest(final String caseNo, final String description, final String host,
                            final String path, final String type, final String reference_id,
                            final String start_time, final String udid) throws ParseException,
                                                                       IOException {
        try {
            // 构造一个url
            URI url = new URIBuilder().setScheme("http").setHost(host).setPath(path)
                .setParameter("type", type).setParameter("reference_id", reference_id)
                .setParameter("start_time", start_time).setParameter("udid", udid).build();

            // 获取http响应的内容
            HttpGet httpget = new HttpGet(url);
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(httpget);

            // 校验http响应
            Assert.assertEquals(httpResponse.getProtocolVersion().toString(), "HTTP/1.1");
            Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
            Assert.assertEquals(httpResponse.getStatusLine().getReasonPhrase(), "OK");
            
            // 获取http实体
            HttpEntity httpEntity = httpResponse.getEntity();

            // 获取http实体中的百科信息
            JSONArray jsonBaikei = JsonUtil.parseJsonStr(EntityUtils.toString(httpEntity), "baike");
            JSONObject baikeiObj = new JSONObject().fromObject(jsonBaikei.get(0));
            // 校验百科信息
            Assert
                .assertEquals(baikeiObj.get("cover"),
                    "http://192.168.0.110:9080/st/upload/images/mec_person/16/201407/17/14056027868347560633503.jpg");
            Assert.assertEquals(baikeiObj.get("summary"), "代表作：花火、飞得更高、怒放的生命、北京北京、春天里");
            Assert.assertEquals(baikeiObj.get("title"), "汪峰");
            Assert.assertEquals(baikeiObj.get("updated_at").toString(), "1406724348000");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void getCsvDataTest() throws IOException {
        CSVReader reader = new CSVReader(new FileReader(
            "/home/thinkpad-x230/workspace/rca.mec.server/test/tv/zhiping/mec/testdata/test.csv"),
            ',');

        String[] csvRow = null;
        //        reader.readNext();
        ArrayList<String[]> csvList = new ArrayList<String[]>();

        while ((csvRow = reader.readNext()) != null) {
            // csvRow[] is an array of values from the line
            csvList.add(csvRow);
            for (int i = 0; i < csvRow.length; i++) {
                System.out.println(csvRow[i]);
            }
            //            System.out.println(nextLine[0] + nextLine[1] + "etc...");
        }
        System.out.println("__________________________");
        System.out.println(csvList.size());
        System.out.println("__________________________");

        for (int row = 0; row < csvList.size(); row++) {
            for (int i = 0; i < csvList.get(row).length; i++) {
                String cell = csvList.get(row)[i];
                System.out.println(cell);
            }
        }

        Assert.assertTrue(false);
    }

}

//
//{
//data: {
//feeds: {
//baike: {
//cover: "http://192.168.0.110:9080/st/upload/images/mec_person/16/201407/17/14056027868347560633503.jpg",
//id: 83,
//summary: "代表作：花火、飞得更高、怒放的生命、北京北京、春天里",
//title: "汪峰",
//updated_at: 1406724348000
//}
//},
//offset_time: 480,
//page: "http://192.168.0.110:9080/m/tmallbox/index",
//weibo: {
//content: "这一组演唱的感觉非常温暖，很有画面感。张新每一个音都唱得非常精准，王拓被那姐形容为快乐的“小巫婆”！李秋泽擅长的是HIP-POP节奏性很强的歌曲，这一次他尝试了新的曲风，让我们看到了另一面的李秋泽。王拓擅长转音，今天她跳出框框，找到了新的感觉，最后阿妹选择了王拓！ @王拓W",
//id: 32042,
//links: [
//{
//thumbnail_pic: "http://ww1.sinaimg.cn/bmiddle/a806f328tw1e91daafhrej208c0goac0.jpg",
//type: "image",
//url: "http://ww1.sinaimg.cn/bmiddle/a806f328tw1e91daafhrej208c0goac0.jpg"
//}
//],
//sender_avatar: "http://tp1.sinaimg.cn/2819027752/180/40057947630/1",
//sender_name: "中国好声音",
//updated_at: 1406191599000
//}
//},
//msg: "success",
//page: null,
//status: 0
//}




//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//System.out.println(httpget.getURI());
//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//System.out.println(httpResponse.getProtocolVersion());
//System.out.println(httpResponse.getStatusLine().getStatusCode());
//System.out.println(httpResponse.getStatusLine().getReasonPhrase());
//System.out.println(httpResponse.getStatusLine().toString());
//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

//JSONArray bkCover = JsonUtil.parseJsonStr(jsonBaikei.get(0).toString(), "cover");
//JSONArray bkId = JsonUtil.parseJsonStr(jsonBaikei.get(0).toString(), "id");
//JSONArray bkSummary = JsonUtil.parseJsonStr(jsonBaikei.get(0).toString(), "summary");
//JSONArray bkTitle = JsonUtil.parseJsonStr(jsonBaikei.get(0).toString(), "title");
//JSONArray bkupdatedat = JsonUtil.parseJsonStr(jsonBaikei.get(0).toString(), "updated_at");

//System.out.println(jsonBaikei.get(0));
//System.out.println(baikeiObj.get("cover"));
//System.out.println(baikeiObj.get("summary"));
//System.out.println(baikeiObj.get("title"));
//System.out.println(baikeiObj.get("updated_at"));

