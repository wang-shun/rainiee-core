package com.dimeng.p2p.modules.bid.queue.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;

import java.sql.SQLException;
import java.util.Map;

/**
 * 投标队列接口类
 * <功能详细描述>
 *
 * @author zengzhihua
 * @version [版本号, 2017/3/30]
 */
public abstract interface QueueManage extends Service
{
    /**
     * 将投标请求放入redis队列
     * @throws Exception
     * @throws SQLException
     */
    public abstract String insertBidQueue(Map<String, Object> pramMap, ServiceSession serviceSession) throws Exception;

    /**
     * 发送Websoket信息
     * @throws Exception
     * @throws SQLException
     */
    public abstract void sendWebSocketInfo(String userId, String sendInfo) throws Exception;

    /**
     * 订阅redis消息
     * @throws Exception
     * @throws SQLException
     */
    public abstract void subscribeRedisMsg() throws Exception;

}
