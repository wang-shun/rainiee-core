package com.dimeng.p2p.pay.servlets.fuyou;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.escrow.fuyou.service.InformManage;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.google.gson.Gson;

/**
 * 
 *  提现退票通知接口，通知Servlet
 * 此类需于富绑定服务器：http://平台域名/pay/fuyou/refundInform.htm(方可生效)。
 * eg: http://61.145.159.156:8315/pay/fuyou/refundInform.htm>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月29日]
 */
public class RefundInform extends AbstractFuyouServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 8361298118048132970L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        logger.info("富友托管提现退票通知-访问地址：" + request.getRemoteAddr());
        Gson gs = new Gson();
        String jsonData = gs.toJson(reversRequest(request));
        logger.info("富友托管提现退票通知：" + jsonData);
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
            Map<String, String> ordMap = informManage.selectT6501(mchnt_txn_ssn, false);
            if (ordMap == null)
            {
                response.getWriter().write("SUCCESS");
                response.getWriter().close();
                return;
            }
            final int orderId = Integer.parseInt(ordMap.get("orderId"));
            if (T6501_F03.SB.name().equals(ordMap.get("state")))
            {
                logger.info("提现退票,流水号:" + mchnt_txn_ssn + ",订单号=" + orderId + ",手机号=" + mobile_no + "-已更处理！");
                response.getWriter().write("SUCCESS");
                response.getWriter().close();
                return;
            }
            else if (T6501_F03.CG.name().equals(ordMap.get("state")))
            {
                logger.info("提现退票(平台成功)，水流号：" + mchnt_txn_ssn);
                if(informManage.refundWithderaw(orderId, mchnt_txn_ssn)){
                    response.getWriter().write("SUCCESS");
                    response.getWriter().close();
                }
            }
        }
    }
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
}
