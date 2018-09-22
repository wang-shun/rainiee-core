package com.dimeng.p2p.pay.servlets.fuyou.ret;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.executor.FYChargeOrderExecutor;
import com.dimeng.p2p.escrow.fuyou.service.ChargeManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.google.gson.Gson;

/**
 * 
 * 富友，充值返回地址
 * <充值后台返回结果-包括手续费>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月11日]
 */
public class ChargeNotice extends AbstractFuyouServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        response.setContentType("text/html");
        response.setCharacterEncoding(charSet);
        logger.info("充值-后台通知——IP:" + request.getRemoteAddr());
        Gson gson = new Gson();
        String jsonData = gson.toJson(reversRequest(request));
        logger.info("充值-后台通知：" + jsonData);
        if (jsonData.length() < 5) {
            String noticeMessage = "欢迎光临！";
            createNoticeMessagePage(getConfigureProvider().format(URLVariable.INDEX), noticeMessage, response);
            return;
        }
        // 分析从对方传回来的数据
        ChargeManage chargeManage = serviceSession.getService(ChargeManage.class);
        @SuppressWarnings("unchecked")
        Map<String, String> retMap = gson.fromJson(jsonData, Map.class);
        //根据流水号查充值类型
        T6502 t6502 = chargeManage.selectT6502BySsn(retMap.get("mchnt_txn_ssn"));
        boolean flag = chargeManage.chargeRetDecode(retMap,t6502);
        if (!flag) {
            logger.info("验签失败");
            return;
        }
        // 流水号
        final String MCHNT_TXN_SSN = retMap.get("mchnt_txn_ssn");
        Map<String, String> params = chargeManage.selectT6501Success(MCHNT_TXN_SSN);
        // 查询此订单是否已经被处理
        if (params != null && (T6501_F03.CG.name().equals(params.get("state")) || T6501_F03.DQR.name().equals(params.get("state")))) {
            logger.info("网银充值-已有处理-流水号：" + MCHNT_TXN_SSN);
            return;
        }
        int orderId = Integer.parseInt(params.get("orderId"));
        if (FuyouRespCode.JYCG.getRespCode().equals(retMap.get("resp_code"))) {
            FYChargeOrderExecutor executor = getResourceProvider().getResource(FYChargeOrderExecutor.class);
            // DQR订单
            executor.submit(orderId, retMap);
            // 执充值业务操作
            executor.confirm(orderId, retMap);
            logger.info("充值-处理成功-订单：" + orderId);
        } else {
            // 充值失败
            chargeManage.updateT6501(orderId);
            logger.info("网银充值-失败-订单：" + orderId + BackCodeInfo.info(retMap.get("resp_code")));
        }        
    }
    
    @Override
    protected boolean mustAuthenticated() {
        return false;
    }
    
}
