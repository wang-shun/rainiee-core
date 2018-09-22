package com.dimeng.p2p.weixin.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * json消息解析工具类
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年4月11日]
 */
public abstract class JSONUtils
{
    /**
     * 将二进制流解析成json消息
     * 
     * @param in 二进制流
     * @return json消息
     */
    public static JsonObject parseJsonByStream(final InputStream in)
    {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        final JsonParser jsonPaser = new JsonParser();
        final JsonElement jsonElement = jsonPaser.parse(reader);
        return jsonElement.getAsJsonObject();
    }
    
    /**
     * 将字符串解析成json消息
     * 
     * @param str 字符串
     * @return json消息
     */
    public static JsonObject parseJsonByStr(final String str)
    {
        final JsonParser jsonPaser = new JsonParser();
        final JsonElement jsonElement = jsonPaser.parse(str);
        return jsonElement.getAsJsonObject();
    }
}
