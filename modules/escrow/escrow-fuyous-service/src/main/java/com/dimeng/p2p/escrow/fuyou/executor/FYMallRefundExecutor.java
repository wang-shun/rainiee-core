package com.dimeng.p2p.escrow.fuyou.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.AchieveVersion;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6528;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.MallRefundExecutor;

/**
 * 
 * 撤销商品交易执行器
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月19日]
 */
@AchieveVersion(version = 20160101)
@ResourceAnnotation
public class FYMallRefundExecutor extends MallRefundExecutor {
    
    public FYMallRefundExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    public void confirm(int orderId, Map<String, String> params) throws Throwable {
        params = new HashMap<String, String>();
        String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.XPTK.name());
        params.put("mchnt_txn_ssn", mchnt_txn_ssn);
        if (!updateT6501(orderId, mchnt_txn_ssn))
        {
            throw new LogicalException("退款失败！");
        }
        logger.info("商户退款,订单：" + orderId);
        super.confirm(orderId, params);
    }
    
    @Override
    protected void callFace(SQLConnection connection, int orderId, Map<String, String> params) throws SQLException {
        // 平台商城退款订单
        T6528 t6528 = selectT6528(connection, orderId);
        if (t6528 == null) {
            throw new LogicalException("订单详细不存在！");
        }
        // 购买人账户
        String gmrzh = getUserCustId(connection, t6528.F02);
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            // 商户代码
            String out_cust_no = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
            String amt = PayUtil.getAmt(t6528.F03);
            TransferManage manage = serviceSession.getService(TransferManage.class);
            Map<String, Object> map = manage.createTransferMap(
                    params.get("mchnt_txn_ssn"), out_cust_no, gmrzh, amt, "transferBmu");
            if (map != null && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")))) {
                logger.info("撤销商品交易成功-订单：" + orderId);
                updateT6501F12(connection, orderId, "撤销商品交易");
                params.put("YN", "true");
                params.put("resp_code", map.get("resp_code").toString());
                params.put("in_cust_no", gmrzh);
                params.put("out_cust_no", out_cust_no);
                params.put("amt", amt);
            } else {
                params.put("YN", "false");
                logger.info("撤销商品交易失败-订单");
                throw new LogicalException(BackCodeInfo.info(params.get("resp_code")));
            }
        } catch (Throwable e) {
            logger.error("撤销商品交易失败-订单：" + orderId, e);
            throw new LogicalException("撤销商品交易失败!");
        }
    }
    
    @Override
    protected void rollbackFace(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        if (!Boolean.parseBoolean(params.get("YN"))) {
            return;
        }
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
        	TransferManage manage = serviceSession.getService(TransferManage.class);
            Map<String, Object> map = manage.createTransferMap(
                    params.get("mchnt_txn_ssn") + "X", params.get("in_cust_no"), params.get("out_cust_no"), params.get("amt"), "transferBmu");
            if (map != null && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")) || FuyouRespCode.JYWC.getRespCode().equals(map.get("resp_code")))) {
                logger.info("退款失败-退还资金-成功");
            } else {
                logger.info("退款失败-退还资金-失败");
            }
        }
    }
    
    private boolean updateT6501(int orderId, String mchnt_txn_ssn) throws Throwable {
        try (SQLConnection connection = getConnection()) {
            T6501 t6501 = selectT6501(connection, orderId);
            if (T6501_F03.DQR != t6501.F03) {
                return false;
            }
            updateT6501(connection, mchnt_txn_ssn, orderId);
        }
        return true;
    }
    
    private void updateT6501F12(Connection connection, int orderId, String F12) throws Throwable {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6501 SET F12 = ? WHERE F01 = ?")) {
            pstmt.setString(1, F12);
            pstmt.setInt(2, orderId);
            pstmt.execute();
        }
    }
    
}
