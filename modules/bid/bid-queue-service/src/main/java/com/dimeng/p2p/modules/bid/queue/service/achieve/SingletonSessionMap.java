/*
 * 文 件 名:  SingletonSessionMap.java
 * 版    权:  © 2016 DM. All rights reserved.
 * 描    述:  
 * 修 改 人:  zengzhihua
 * 修改时间:  2017-4-12
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.dimeng.p2p.modules.bid.queue.service.achieve;

import javax.websocket.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 一句话功能简述
 * 功能详细描述
 *
 * @author zengzhihua
 * @version [版本号, 2017-4-12]
 */
public class SingletonSessionMap {
    //在线的客户端session集合，只在第一次new的时候初始化。
    public static Map<String, Session> sessionMap = new HashMap<String, Session>();
    private SingletonSessionMap (){
    }
    private static SingletonSessionMap singletonSessionMap;
    public static SingletonSessionMap getSingletonSessionMap(){
        if(singletonSessionMap==null)
        {
            synchronized (SingletonSessionMap.class) {
                if(singletonSessionMap==null) {
                    singletonSessionMap = new SingletonSessionMap();
                }
            }
        }
        return singletonSessionMap;
    }
    public void put(String key, Session value) {
        sessionMap.put(key, value);
    }

    public Session get(String key) {
        return sessionMap.get(key);
    }

    public void remove(String key) {
        sessionMap.remove(key);
    }
}
