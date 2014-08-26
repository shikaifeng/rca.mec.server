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


import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @ClassName: JsonUtil
 * @Description: 利用json-lib实现json字符串的解析，以及对象与json字符串之间的转换
 * 
 * @author kaifeng.shi
 * 
 */
public class JsonUtil {
    //存放jsonStr中，指定key的value
    private static JSONArray values;
    /**
     * 解析json字符串中指定key的value
     * 
     * @param jsonString
     *            json字符串
     * @param key
     *            json字符串中的key
     * 
     * @return Object key对应的value
     * 
     */
    public static JSONArray parseJsonStr(String jsonString, String key) {
        values = new JSONArray();
        if (jsonString != null || key != null) {
            if (jsonString.startsWith("[")) {
                JSONArray jsonArr = JSONArray.fromObject(jsonString);
                getValueFromJSONArray(jsonArr, key);
            } else if (jsonString.startsWith("{")) {
                JSONObject jsonObj = JSONObject.fromObject(jsonString);
                getValueFromJSONObject(jsonObj, key);
            }
        }
        return values;
    }

    /**
     * 递归解析JSONArray中的指定key
     * */
    private static void getValueFromJSONArray(JSONArray jsonArr, String key) {
        for (int i = 0; i < jsonArr.size(); i++) {
            Object obj = jsonArr.get(i);
            if (obj.toString().startsWith("[")) {
                JSONArray subArr = JSONArray.fromObject(obj);
                getValueFromJSONArray(subArr, key);
            } else if (obj.toString().startsWith("{")) {
                JSONObject subObj = JSONObject.fromObject(obj);
                getValueFromJSONObject(subObj, key);
            }
        }
    }

    /**
     * 递归解析JSONObject中的指定key
     * */
    private static void getValueFromJSONObject(JSONObject jsonObj, String key) {
        Object value = null;
        if (jsonObj.containsKey(key)) {//json字符串的查找，终结于所有的JSONObject,找到后即可返回
            value = jsonObj.get(key);
            values.add(value);
            return;
        }
        Set keys = jsonObj.keySet();
        @SuppressWarnings("unchecked")
        Iterator<String> keyIt = keys.iterator();
        while (keyIt.hasNext()) {
            String onekey = keyIt.next();
            Object vObj = jsonObj.get(onekey);
            if (vObj.toString().startsWith("{")) {
                JSONObject subJs = JSONObject.fromObject(vObj);
                getValueFromJSONObject(subJs, key);
            } else if (vObj.toString().startsWith("[")) {
                JSONArray subArr = JSONArray.fromObject(vObj);
                getValueFromJSONArray(subArr, key);
            }
        }
    }

}