package com.dimeng.p2p.modules.bid.queue.service.achieve;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.modules.bid.queue.service.QueueManage;
import com.dimeng.util.parser.BigDecimalParser;
import org.codehaus.jackson.map.ObjectMapper;
import redis.clients.jedis.Jedis;

import javax.websocket.Session;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 投标队列接口实现类
 * <功能详细描述>
 *
 * @author zengzhihua
 * @version [版本号, 2017/3/30]
 */
public class QueueManageImpl extends AbstractQueueService implements QueueManage
{

    public QueueManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }

    @Override
    public String insertBidQueue(Map<String, Object> pramMap, ServiceSession serviceSession) throws Exception {
        try{
            ObjectMapper objectMapper = new ObjectMapper();// 封装JSON
            String loandId = String.valueOf(pramMap.get("loanId")); //标ID
            String amount = String.valueOf(pramMap.get("amount"));  //投资金额
            String jkje = String.valueOf(pramMap.get("jkje"));      //借款金额

            //获取redis客户端
            Jedis jedis = getJedis();
            jedis.lpush(loandId+"-bid-queue", objectMapper.writeValueAsString(pramMap)); //将投标请求加入队列

            //判断已请求的投资金额是否超过接口金额
            if(jedis.exists(loandId+"-amount")){
                String alreadyAmount = jedis.get(loandId + "-amount");
                BigDecimal sumAmount = BigDecimalParser.parse(alreadyAmount).add(BigDecimalParser.parse(amount));
                if(sumAmount.compareTo(BigDecimalParser.parse(jkje)) > 0) {
                    //本次投资后的总投资金额大于借款金额，则不允许投资
                    return "投资金额已达上限，请稍后再试或投资其他标，谢谢。";
                }
                jedis.set(loandId + "-amount", sumAmount.toString());
            }else {
                jedis.set(loandId + "-amount", amount);
            }

            //一个标只用一个线程处理
            if(findThread(loandId+"bid-tread")){
                return "";
            }

            //创建线程去处理队列中的投标请求
            Thread thread = new Thread(new TaskConsumer(serviceResource, loandId));
            thread.setName(loandId+"bid-tread");
            thread.setDaemon(true);
            thread.start();
        }catch(Exception e){
            new Exception();
        }
        return "";
    }

    @Override
    public void sendWebSocketInfo(String userId, String sendInfo) throws Exception {
        Session value = SingletonSessionMap.getSingletonSessionMap().get(userId);
        logger.info("sendWebSocketInfo:userId:"+userId+",本机session="+value);
        if(value != null) {
            value.getBasicRemote().sendText(sendInfo);
        }else{
            logger.info("sendWebSocketInfo：userId:"+userId+"，通过redis发布消息");
            Jedis jedis = getJedis();
            jedis.publish("bidChat", sendInfo+"|"+userId);
        }
    }

    @Override
    public void subscribeRedisMsg() throws Exception {
        logger.info("subscribeRedisMsg:开始订阅");
        if(findThread("redis-subscrebe-thread")){
            logger.info("subscribeRedisMsg:订阅线程已存在");
            return;
        }
        Thread t = new Thread(new Runnable(){
            public void run(){
                RedisMsgPubSubListener listener = new RedisMsgPubSubListener();
                Jedis jedis = null;
                try {
                    jedis = getJedis();
                    jedis.subscribe(listener, "bidChat");
                }catch (Exception e){
                    logger.error("订阅失败："+e);
                }
            }});
        t.setName("redis-subscrebe-thread");
        t.setDaemon(true);
        t.start();
    }

    /**
     * 判断指定线程是否是活动状态
     *
     * @param threadName
     * @return
     */
    public static boolean findThread(String threadName) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while(group != null) {
            Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
            int count = group.enumerate(threads, true);
            for(int i = 0; i < count; i++) {
                if(threadName.equals(threads[i].getName()) && threads[i].isAlive()) {
                    return true;
                }
            }
            group = group.getParent();
        }
        return false;
    }
}
