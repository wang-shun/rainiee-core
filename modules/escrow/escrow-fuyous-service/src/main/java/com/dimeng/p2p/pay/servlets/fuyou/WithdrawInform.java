package com.dimeng.p2p.pay.servlets.fuyou;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.InformManage;
import com.dimeng.p2p.order.WithdrawExecutor;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.google.gson.Gson;

/**
 * 
 * 富友托管提现成功，通知Servlet
 * <只有成功才通知，二部分：平台提现，委托提现>
 * 此类需于富绑定服务器：http://平台域名/pay/fuyou/withdrawInform.htm(方可生效)。
 * eg: http://61.145.159.156:8315/pay/fuyou/withdrawInform.htm>
 * @author  heshiping
 * @version  [版本号, 2015年12月7日]
 */
public class WithdrawInform extends AbstractFuyouServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        logger.info("富友托管提现成功-IP：" + request.getRemoteAddr());
        Gson gs = new Gson();
        String jsonData = gs.toJson(reversRequest(request));
        logger.info("富友托管提现成功信息：" + jsonData);
        if (jsonData.length() < 5)
        {
            createNoticeMessagePage(getConfigureProvider().format(URLVariable.INDEX), "欢迎光临！", response);
            return;
        }
        InformManage informManage = serviceSession.getService(InformManage.class);
        @SuppressWarnings("unchecked")
        Map<String, String> params = gs.fromJson(jsonData, Map.class);
        boolean flag = informManage.informReturnDecoder(params);
        if (flag)
        {
            final String mchnt_txn_ssn = params.get("mchnt_txn_ssn");
            final String mobile_no = params.get("mobile_no");
            Map<String, String> ordMap = informManage.selectT6501(mchnt_txn_ssn, true);
            if (ordMap != null
                && (T6501_F03.CG.name().equals(ordMap.get("state")) || T6501_F03.DQR.name().equals(ordMap.get("state"))))
            {
                logger.info("平台提现通知,流水号=" + mchnt_txn_ssn + ",订单号=" + ordMap.get("orderId") + ",手机号=" + mobile_no
                    + "-已更处理！");
                sendxml(response, params, informManage);
                return;
            }
            
            int orderId;
            if (ordMap != null && mchnt_txn_ssn.contains(FuyouTypeEnum.YHTX.name()))
            {
                orderId = Integer.parseInt(ordMap.get("orderId"));
                informManage.updateT6503(orderId);
                params.put("orderId", ordMap.get("orderId"));
                logger.info("平台提现通知,流水号=" + mchnt_txn_ssn + ",订单号=" + orderId + ",手机号=" + mobile_no);
            }
            else
            {
                logger.info("委托通知,流水号=" + mchnt_txn_ssn + ",手机号=" + mobile_no);
                // 增加订单
                informManage.addOrderEntrust(params);
                if (params.get("NO") != null)
                {
                    logger.info("该订单非平台用户：" + mobile_no + "-流水号：" + mchnt_txn_ssn);
                    sendxml(response, params, informManage);
                    return;
                }
                orderId = Integer.parseInt(params.get("orderId"));
                params.put("WT", "WT");
                logger.info("富友系统委托提现通知,流水号=" + mchnt_txn_ssn + ",订单号=" + orderId + ",手机号=" + mobile_no);
            }
            informManage.updateOrder(params);
            WithdrawExecutor executor = getResourceProvider().getResource(WithdrawExecutor.class);
            // 更新用户资金，插入流水记录，修改订单状态
            executor.confirm(orderId, null);
            logger.info("提现成功，订单流水号为：" + mchnt_txn_ssn + "用户手机号：" + params.get("mobile_no") + ",返回码："
                + params.get("resp_code") + ",提现金额" + params.get("amt"));
            sendxml(response, params, informManage);
            return;
        }
        else
        {
            logger.error("验签失败");
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
        String sendxml = null;
        try
        {
            Map<String, String> xmap = new HashMap<String, String>();
            xmap.put("resp_code", "0000");
            xmap.put("mchnt_cd", params.get("mchnt_cd"));
            xmap.put("mchnt_txn_ssn", params.get("mchnt_txn_ssn"));
            sendxml = informManage.encryptByRSA(xmap);
            logger.info(sendxml);
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        finally
        {
            response.getWriter().write(sendxml);
            response.getWriter().close();
        }
    }
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
}
