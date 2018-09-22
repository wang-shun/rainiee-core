/*
 * 文 件 名:  RedisMsgPubSubListener.java
 * 版    权:  © 2016 DM. All rights reserved.
 * 描    述:  
 * 修 改 人:  zengzhihua
 * 修改时间:  2017-4-11
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.dimeng.p2p.modules.bid.queue.service.achieve;

import redis.clients.jedis.JedisPubSub;

import javax.websocket.Session;
import java.io.IOException;

/**
 * 一句话功能简述
 * 功能详细描述
 *
 * @author zengzhihua
 * @version [版本号, 2017-4-11]
 */
public class RedisMsgPubSubListener extends JedisPubSub {
    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }

    @Override
    public void unsubscribe(String... channels) {
        super.unsubscribe(channels);
    }

    @Override
    public void subscribe(String... channels) {
        super.subscribe(channels);
    }

    @Override
    public void psubscribe(String... patterns) {
        super.psubscribe(patterns);
    }

    @Override
    public void punsubscribe() {
        super.punsubscribe();
    }

    @Override
    public void punsubscribe(String... patterns) {
        super.punsubscribe(patterns);
    }

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("监听：channel:" + channel + ", message:" + message);
        String userId = message.substring(message.indexOf("|")+1, message.length());
        message = message.substring(0,message.indexOf("|"));
        SingletonSessionMap singletonSessionMap= SingletonSessionMap.getSingletonSessionMap();
        Session value = singletonSessionMap.get(userId);
        System.out.println("监听：userId:"+userId+",session="+value);
        if(value == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("延迟1s取session");
            value = singletonSessionMap.get(userId);
        }
        if(value != null) {
            try {
                value.getBasicRemote().sendText(message);
            } catch (IOException e) {
                System.out.println("监听：给用户"+userId+"发送消息失败");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println("channel:" + channel + "is been subscribed:" + subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println("channel:" + channel + "is been unsubscribed:" + subscribedChannels);
    }
}
