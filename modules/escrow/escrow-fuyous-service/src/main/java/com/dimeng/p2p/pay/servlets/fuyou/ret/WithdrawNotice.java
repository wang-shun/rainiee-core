package com.dimeng.p2p.pay.servlets.fuyou.ret;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.service.WithDrawManage;
import com.dimeng.p2p.order.WithdrawExecutor;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.google.gson.Gson;

/**
 * 
 * 用户提现后台通知
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月30日]
 */
public class WithdrawNotice extends AbstractFuyouServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        logger.info("用户提现后台通知——IP:" + request.getRemoteAddr());
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        Gson gs = new Gson();
        String jsonData = gs.toJson(reversRequest(request));
        logger.info("用户提现后台通知：" + jsonData);
        if (jsonData.length() < 5) {
            createNoticeMessagePage(getConfigureProvider().format(URLVariable.INDEX), "欢迎光临！", response);
            return;
        }
        WithDrawManage withDrawManage = serviceSession.getService(WithDrawManage.class);
        @SuppressWarnings("unchecked")
        Map<String, String> params = gs.fromJson(jsonData, Map.class);
        // 验签
        boolean flag = withDrawManage.withdrawReturnDecoder(params);
        if (flag) {
            //如果验证签名通过
            if (FuyouRespCode.JYCG.getRespCode().equals(params.get("resp_code"))) {
                // 根据流水号更新订单，更新用户资金、插入流水
                int orderId = withDrawManage.updateOrder(params);
                WithdrawExecutor executor = getResourceProvider().getResource(WithdrawExecutor.class);
                // 更新用户资金，插入流水记录，修改订单状态
                executor.confirm(orderId, null);
                logger.info("提现成功，订单流水号为：" + params.get("mchnt_txn_ssn") + "用户为：" + params.get("login_id") + ",返回码："
                    + params.get("resp_code") + ",提现金额" + params.get("amt"));
            }
        } else {
            logger.info("用户提现后台通知-验签失败");
        }
    }
    
    @Override
    protected boolean mustAuthenticated() {
        return false;
    }
}
