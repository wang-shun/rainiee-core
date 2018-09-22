package com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.withdrawdzgl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.AbstractDzglServlet;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouEnum;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.executor.FYChargeOrderExecutor;
import com.dimeng.p2p.escrow.fuyou.service.ChargeWithdrawManage;
import com.dimeng.p2p.escrow.fuyou.service.InformManage;
import com.dimeng.p2p.escrow.fuyou.service.QueryManage;
import com.dimeng.p2p.escrow.fuyou.service.WithDrawManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.order.WithdrawExecutor;
import com.dimeng.util.StringHelper;
import com.google.gson.Gson;

/**
 * 
 * 提现订单查询
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月25日]
 */
@Right(id = "P2P_C_FINANCE_WITHDRAWQUERY", moduleId = "P2P_C_FUYOU_TXDZGL", order = 1, name = "提现对账操作")
public class WithdrawQuery extends AbstractDzglServlet {

    private static final long serialVersionUID = 4546213436828344790L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable {
        logger.info("提现对账——IP:" + request.getRemoteAddr());
        final int orderId = Integer.parseInt(request.getParameter("orderNo"));
        QueryManage manage = serviceSession.getService(QueryManage.class);
        InformManage informManage = serviceSession.getService(InformManage.class);
        T6501 t6501 = manage.selectT6501(orderId);
        if (t6501 == null) {
            throw new LogicalException("订单不存在!");
        }
        // 对账订单必须大于5分钟[防止订单未提交第三方面对账]
        
        String f04 = String.valueOf(t6501.F04);
        if (compareDate(f04, 300000)) {
            processRequest(request, response, "5分钟内订单请稍后对账!");
            return;
        }
        if (T6501_F07.HT == t6501.F07) {
            manage.updateT6501(orderId, T6501_F11.S.name(), "平台充值", null);
            processRequest(request, response, "SUCCESS");
            return;
        }
        if (t6501.F02 != OrderType.CHARGE.orderType() && t6501.F02 != OrderType.WITHDRAW.orderType()) {
            logger.info("对账类型不符合");
            processRequest(request, response, "对账类型不符合!");
            return;
        }
        String busi_tp;
        boolean bp;
        if (OrderType.CHARGE.orderType() == t6501.F02) {
            busi_tp = FuyouEnum.PW11.name();
            bp = true;
        } else {
            busi_tp = FuyouEnum.PWTX.name();
            bp = false;
        }
        ChargeWithdrawManage chargeWithdrawManage = serviceSession.getService(ChargeWithdrawManage.class);
        Map<String, Object> params = chargeWithdrawManage.createChargeWithdraw(
                MchntTxnSsn.getMts(FuyouTypeEnum.CTCX.name()), busi_tp, t6501.F10, manage.selectT6119(t6501.F08));
        logger.info(new Gson().toJson(params));
        
        if (FuyouRespCode.JYCG.getRespCode().equals(params.get("resp_code"))) {
            if (FuyouRespCode.JYCG.getRespCode().equals(params.get("txn_rsp_cd"))) {
                boolean flag = true;
                if (bp) {
                    // 成功处理
                    FYChargeOrderExecutor executor = null;
                    Map<String, String> retMap = null;
                    switch (t6501.F03.name()) {
                        case "DTJ":
                            executor = getResourceProvider().getResource(FYChargeOrderExecutor.class);
                            retMap = new HashMap<String, String>();
                            retMap.put("mchnt_txn_ssn", t6501.F10);
                            // DQR订单
                            executor.submit(orderId, retMap);
                            //修改T6052  F04赋值给F05
                            informManage.selectT6502(orderId);
                            // 执充值业务操作
                            executor.confirm(orderId, retMap);
                            break;
                        case "DQR":
                            executor = getResourceProvider().getResource(FYChargeOrderExecutor.class);
                            retMap = new HashMap<String, String>();
                            retMap.put("mchnt_txn_ssn", t6501.F10);
                            //修改T6052  F04赋值给F05
                            informManage.selectT6502(orderId);
                            // 执充值业务操作
                            executor.confirm(orderId, retMap);
                            break;
                        case "CG":
                            String remark;
                            if (T6501_F07.PS == t6501.F07) {
                                remark = "确认,poss机充值\\委托充值 .";
                            } else {
                                remark = "确认,已到账.";
                            }
                            manage.updateT6501(orderId, T6501_F11.S.name(), remark, null);
                            flag = false;
                            break;
                        default:
                            executor = getResourceProvider().getResource(FYChargeOrderExecutor.class);
                            manage.updateT6501(orderId, T6501_F11.F.name(), "对账处理中", T6501_F03.DQR.name());
                            retMap = new HashMap<String, String>();
                            retMap.put("mchnt_txn_ssn", t6501.F10);
                            //修改T6052  F04赋值给F05
                            informManage.selectT6502(orderId);
                            // 执充值业务操作
                            executor.confirm(orderId, retMap);
                            break;
                    }
                } else {
                    // 成功处理
                    WithdrawExecutor executor = null;
                    Map<String, String> retMap = null;
                    WithDrawManage withDrawManage = serviceSession.getService(WithDrawManage.class);
                    switch (t6501.F03.name()) {
                        case "DTJ":
                            executor = getResourceProvider().getResource(WithdrawExecutor.class);
                            retMap = new HashMap<String, String>();
                            retMap.put("mchnt_txn_ssn", t6501.F10);
                            // DQR订单
                            executor.submit(orderId, retMap);
                            //修改T6053  F04赋值给F05
                            informManage.updateT6503(orderId);
                            withDrawManage.updateOrder(retMap);
                            // 执充值业务操作
                            executor.confirm(orderId, retMap);
                            break;
                        case "DQR":
                            executor = getResourceProvider().getResource(WithdrawExecutor.class);
                            retMap = new HashMap<String, String>();
                            retMap.put("mchnt_txn_ssn", t6501.F10);
                            //修改T6053  F04赋值给F05
                            informManage.updateT6503(orderId);
                            withDrawManage.updateOrder(retMap);
                            // 执充值业务操作
                            executor.confirm(orderId, retMap);
                            break;
                        case "CG":
                            String remark;
                            if (T6501_F07.WT == t6501.F07) {
                                remark = "确认,委托提现.";
                            } else {
                                remark = "已确认.";
                            }
                            manage.updateT6501(orderId, T6501_F11.S.name(), remark, null);
                            flag = false;
                            break;
                        default:
                            executor = getResourceProvider().getResource(WithdrawExecutor.class);
                            manage.updateT6501(orderId, T6501_F11.F.name(), "对账处理中", T6501_F03.DQR.name());
                            retMap = new HashMap<String, String>();
                            retMap.put("mchnt_txn_ssn", t6501.F10);
                            //修改T6053  F04赋值给F05
                            informManage.updateT6503(orderId);
                            withDrawManage.updateOrder(retMap);
                            // 执充值业务操作
                            executor.confirm(orderId, retMap);
                            break;
                    }
                }
                if (flag) {
                    T6501 t6501s = manage.selectT6501(orderId);
                    if (T6501_F03.CG == t6501s.F03) {
                        manage.updateT6501(orderId, T6501_F11.S.name(), "对账成功处理", null);
                        
                    }
                }
            } else {
                // 1.8天，富友未对账 则说明此订单失败
                if (compareDate(f04, 160000000)) {
                    processRequest(request, response, "充值提现未确认成功单于2个工作日后确定.");
                    return;
                }
                String F12 = params.get("rsp_cd_desc") + "";
                if (StringHelper.isEmpty(F12) || "null".equals(F12)) {
                    F12 = "订单未提交第三方";
                }
                if (t6501.F10.contains(FuyouTypeEnum.YHTX.name())) {
                    manage.updateT6501TxSb(orderId, F12);
                } else {
                    manage.updateT6501(orderId, T6501_F11.S.name(), F12, T6501_F03.SB.name());
                }
            }
        } else {
            logger.info("提现对账接口查询异常！");
        }
        processRequest(request, response, "SUCCESS");
    }
    
    public boolean compareDate(String t6501_F04, int time) throws ParseException {
        Date now = new Date();
        Date d2 = new Date(now.getTime() - time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = simpleDateFormat.parse(t6501_F04);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        
        int result = c1.compareTo(c2);
        if (result >= 0)
            return true;
        else
            return false;
    }
}
