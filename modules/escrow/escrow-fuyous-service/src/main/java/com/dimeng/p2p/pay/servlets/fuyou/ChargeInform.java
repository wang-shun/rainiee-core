package com.dimeng.p2p.pay.servlets.fuyou;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.executor.FYChargeOrderExecutor;
import com.dimeng.p2p.escrow.fuyou.service.InformManage;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.google.gson.Gson;

/**
 * 
 * 富友托管充值成功，通知Servlet
 * <当用户充值成功时通知P2P平台>
 * <为防止充值过程的冲突发情况，而无法同步充值数据，
 * 此类需于富绑定服务器：http://平台域名/pay/fuyou/chrgeInform.htm(方可生效)。
 * eg: http://61.145.159.156:8315/pay/fuyou/chargeInform.htm>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月1日]
 */
public class ChargeInform extends AbstractFuyouServlet
{
    /**
     * 富友充值通知
     */
    private static final long serialVersionUID = -5619826875360535359L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        logger.info("富友托管充值成功-访问地址：" + request.getRemoteAddr());
        Gson gs = new Gson();
        String jsonData = gs.toJson(reversRequest(request));
        logger.info("富友充值成功信息：" + jsonData);
        String noticeMessage = null;
        if (jsonData.length() < 5)
        {
            noticeMessage = "欢迎光临！";
            createNoticeMessagePage(getConfigureProvider().format(URLVariable.INDEX), noticeMessage, response);
            return;
        }
        InformManage informManage = serviceSession.getService(InformManage.class);
        @SuppressWarnings("unchecked")
        Map<String, String> params = gs.fromJson(jsonData, Map.class);
        boolean flag = informManage.informReturnDecoder(params);
        if (flag)
        {
            // 流水号
            final String mchnt_txn_ssn = params.get("mchnt_txn_ssn");
            final String mobile_no = params.get("mobile_no");
            Map<String, String> ordMap = informManage.selectT6501(mchnt_txn_ssn, true);
            if (ordMap != null
                && (T6501_F03.CG.name().equals(ordMap.get("state")) || T6501_F03.DQR.name().equals(ordMap.get("state"))))
            {
                logger.info("平台充值通知,流水号=" + mchnt_txn_ssn + ",订单号=" + ordMap.get("orderId") + ",手机号=" + mobile_no
                    + "-已更处理！");
                sendxml(response, params, informManage);
                return;
            }
            int orderId;
            BigDecimal mchnt_amt = BigDecimal.ZERO;
            if (ordMap != null && mchnt_txn_ssn.contains(FuyouTypeEnum.YHCZ.name()))
            {
                orderId = Integer.parseInt(ordMap.get("orderId"));
                mchnt_amt = informManage.selectT6502(orderId);
                logger.info("平台充值通知,流水号=" + mchnt_txn_ssn + ",订单号=" + orderId + ",手机号=" + mobile_no);
            }
            else
            {
                logger.info("poss充值通知,流水号=" + mchnt_txn_ssn + ",手机号=" + mobile_no);
                // 增加订单
                informManage.addOrder(params);
                if (params.get("NO") != null)
                {
                    logger.info("该订单非平台用户：" + mobile_no + "-流水号：" + mchnt_txn_ssn);
                    sendxml(response, params, informManage);
                    return;
                }
                orderId = Integer.parseInt(params.get("orderId"));
                params.put("POSS", "POSS");
                logger.info("POSS机充值/委托充值通知,流水号=" + mchnt_txn_ssn + ",订单号=" + orderId + ",手机号=" + mobile_no);
            }
            FYChargeOrderExecutor executor = getResourceProvider().getResource(FYChargeOrderExecutor.class);
            // DQR订单
            executor.submit(orderId, params);
            // 执充值业务操作
            executor.confirm(orderId, params);
            informManage.updateT6502(mchnt_txn_ssn, orderId, mchnt_amt);
            sendxml(response, params, informManage);
        }
        else
        {
            logger.info("验签失败");
        }
    }
    
    /**
     * 成功处理-响应
     * <功能详细描述>
     * @param response
     * @param params
     * @param informManage
     * @throws Exception
     * @throws Throwable
     */
    public void sendxml(HttpServletResponse response, Map<String, String> params, InformManage informManage)
        throws Exception, Throwable
    {
        Map<String, String> xmap = new HashMap<String, String>();
        xmap.put("resp_code", "0000");
        xmap.put("mchnt_cd", params.get("mchnt_cd"));
        xmap.put("mchnt_txn_ssn", params.get("mchnt_txn_ssn"));
        String sendxml = informManage.encryptByRSA(xmap);
        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(sendxml);
        response.getWriter().close();
    }
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
}
