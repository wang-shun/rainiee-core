
package  com.dimeng.p2p.modules.bid.queue.service.achieve;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceProvider;
import javax.websocket.Session;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.modules.bid.pay.service.TenderManage;
import com.dimeng.p2p.modules.bid.queue.service.QueueManage;
import com.dimeng.p2p.order.TenderOrderExecutor;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dimeng.p2p.common.ResourceProviderUtil.getResourceProvider;

/**
 * 投标队列业务处理类
 * 功能详细描述
 *
 * @author zengzhihua
 * @version [版本号, 2017-3-18]
 */
public class TaskConsumer extends AbstractQueueService implements Runnable{

    String bidId;
    public volatile boolean exit = true;

    public TaskConsumer(ServiceResource serviceResource, String loandId) {
        super(serviceResource);
        bidId = loandId;
    }

    public void run() {
        Jedis jedis = getJedis();
        String userIdTemp = "";
        int loandIdTemp = 0;
        String amountTemp = "";
        ServiceProvider serviceProvider = ResourceProviderUtil.getResourceProvider().getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            QueueManage queueManage = serviceSession.getService(QueueManage.class);
            try {
                TenderManage tenderManage = serviceSession.getService(TenderManage.class);
                TenderOrderExecutor executor = getResourceProvider().getResource(TenderOrderExecutor.class);

                while (exit) {
                    if (!jedis.exists(bidId + "-bid-queue")) {
                        //如果队列为空，跳出当次循环
                        continue;
                    }
                    //从任务队列中获取一个任务,并将此任务保存到临时队列中(用以记录处理失败的请求)
                    String pramJsonStr = jedis.rpoplpush(bidId + "-bid-queue", bidId + "-temp-queue");
                    JSONObject jasonObject = JSONObject.fromObject(pramJsonStr);
                    Map<String, Object> map = (Map) jasonObject;

                    Map<String, String> parMap = new HashMap<String, String>();
                    String userId = String.valueOf(map.get("userId")) == "null" ? "" : String.valueOf(map.get("userId"));
                    userIdTemp = userId;
                    int loanId = (int) map.get("loanId");
                    loandIdTemp = loanId;
                    Integer amount = (Integer) map.get("amount");
                    amountTemp = String.valueOf(amount);
                    parMap.put("source", String.valueOf(map.get("source")) == "null" ? "" : String.valueOf(map.get("source")));
                    parMap.put("usedExp", String.valueOf(map.get("usedExp")) == "null" ? "" : String.valueOf(map.get("usedExp")));
                    String userReward = String.valueOf(map.get("userReward")) == "null" ? "" : String.valueOf(map.get("userReward"));
                    String tranPwd = String.valueOf(map.get("tranPwd")) == "null" ? "" : String.valueOf(map.get("tranPwd"));
                    String myRewardType = String.valueOf(map.get("myRewardType")) == "null" ? "" : String.valueOf(map.get("myRewardType"));
                    String hbRule = String.valueOf(map.get("hbRule")) == "null" ? "" : String.valueOf(map.get("hbRule"));
                    String jxqRule = String.valueOf(map.get("jxqRule")) == "null" ? "" : String.valueOf(map.get("jxqRule"));
                    String usedExp = String.valueOf(map.get("usedExp")) == "null" ? "" : String.valueOf(map.get("usedExp"));
                    parMap.put("userId", userId);

                    ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
                    boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
                    if (isOpenWithPsd) {
                        if (StringHelper.isEmpty(tranPwd)) {
                            throw new LogicalException("交易密码不能为空");
                        }
                        tranPwd = RSAUtils.decryptStringByJs(tranPwd);
                        tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
                    }
                    Map<String, String> rtnMap = tenderManage.bid(loanId, new BigDecimal(amount), userReward, tranPwd, myRewardType, hbRule, jxqRule, parMap);

                    rtnMap.put("userReward", userReward);
                    rtnMap.put("myRewardType", myRewardType);
                    rtnMap.put("hbRule", hbRule);
                    rtnMap.put("jxqRule", jxqRule);
                    rtnMap.put("usedExp", usedExp);
                    rtnMap.put("usedExp", usedExp);


                    //余额投资订单
                    executor.submit(IntegerParser.parse(rtnMap.get("orderId")), rtnMap);
                    //确认订单
                    executor.confirm(IntegerParser.parse(rtnMap.get("orderId")), rtnMap);
                    writeFrontLog(FrontLogType.SDTB.getName(), "前台手动投资", Integer.parseInt(userId));

                    //发送提示消息
                    queueManage.sendWebSocketInfo(userIdTemp, "恭喜您，投资成功。");

                    String surplusAmount = rtnMap.get("surplusAmount");
                    if ("0.00".equals(surplusAmount)) {
                        //标投满或筹款到期，则退出线程
                        exit = false;
                        logger.info("标" + loanId + "投资已满，退出线程。");
                    }

                    //投资成功，移除临时队列中的数据
                    jedis.rpop(bidId + "-temp-queue");

                }
            } catch (Throwable throwable) {
                getResourceProvider().log(throwable);

                if (jedis.exists(loandIdTemp + "-amount")) {
                    //处理失败，则减去对应的累计投资金额
                    BigDecimal sumAmount = BigDecimalParser.parse(jedis.get(loandIdTemp + "-amount"));
                    jedis.set(loandIdTemp + "-amount", sumAmount.subtract(BigDecimalParser.parse(amountTemp)).toString());
                }

                try {
                    //发送消息
                    String errorMsg = throwable.getMessage().toString();
                    String regEx = "[\u4e00-\u9fa5]";
                    Pattern pat = Pattern.compile(regEx);
                    Matcher matcher = pat.matcher(errorMsg);
                    String sendMsg = errorMsg;
                    //如果异常信息中不存在中文，则提示投标失败
                    if (!matcher.find()) {
                        sendMsg = "投标失败，请稍后再试。";
                    }
                    //发送提示消息
                    queueManage.sendWebSocketInfo(userIdTemp, sendMsg);
                } catch (Exception e) {
                    getResourceProvider().log(throwable);
                    logger.info("给用户" + userIdTemp + "发送消息失败");
                    logger.error(e);
                }
            }
        }
    }
}