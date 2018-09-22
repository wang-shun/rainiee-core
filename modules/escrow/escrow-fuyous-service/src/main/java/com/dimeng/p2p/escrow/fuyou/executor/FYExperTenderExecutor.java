package com.dimeng.p2p.escrow.fuyou.executor;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6285;
import com.dimeng.p2p.S62.enums.T6285_F09;
import com.dimeng.p2p.S65.entities.T6520;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.ExperTenderRepaymentExecutor;

/**
 * 
 * 体验金返还执行器
 * <优化——20160129>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月12日]
 */
@AchieveVersion(version = 20160112)
@ResourceAnnotation
public class FYExperTenderExecutor extends ExperTenderRepaymentExecutor {
    
    public FYExperTenderExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        params = new HashMap<String, String>();
        super.doConfirm(connection, orderId, params);
    }
    
    @Override
    protected void callFace(SQLConnection connection, int orderId, Map<String, String> params) throws SQLException {
        T6520 t6520 = selectT6520(connection, orderId);
        // 查询体验金利息返还记录
        T6285 t6285 = selectT6285F(connection, t6520.F08);
        // 锁定利息返还平台往来账户
        T6101 hkrAcount = selectT6101F(connection, t6285.F03, T6101_F03.WLZH);
        // 锁定收款人往来账户
        T6101 skrAcount = null;
        if (t6285.F03 == t6285.F04) {
            skrAcount = hkrAcount;
        } else {
            skrAcount = selectT6101F(connection, t6285.F04, T6101_F03.WLZH);
        }
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            // 流水号
            String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.TLJL.name());
            // 付款账号(平台第三方账号)
            String out_cust_no = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
            // 收款人账号
            String in_cust_no = getUserCustId(connection, skrAcount.F02);
            String amt = PayUtil.getAmt(t6285.F07);
            // ***P2P平台调用转账接
            TransferManage manage = serviceSession.getService(TransferManage.class);
            Map<String, Object> map = null;
            if (t6285.F07.compareTo(BigDecimal.ZERO) > 0) {
                map = manage.createTransferMap(mchnt_txn_ssn, out_cust_no, in_cust_no, amt, "transferBmu");
            }
            // 判断转账是否成功
            if (map != null && FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code"))) {
                params.put("YN", "true");
                params.put("amt", amt);
                params.put("in_cust_no", in_cust_no);
                params.put("mchnt_txn_ssn", mchnt_txn_ssn);
            } else {
                logger.info("FYExperTenderExecutor.class,转账失败-体验金利息返");
                throw new LogicalException("转账失败-体验金利息返!");
            }
        } catch (IOException e) {
            logger.error("转账失败-体验金利息返!", e);
            throw new LogicalException("转账失败-体验金利息返!");
        } catch (Throwable e) {
            logger.error("转账失败-体验金利息返!", e);
            throw new LogicalException("转账失败-体验金利息返!");
        }
    }
    
    @Override
    protected void rollbackFace(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        if (!Boolean.parseBoolean(params.get("YN"))) {
            return;
        }
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        // 转账接口地址（商户与个人）
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            // 退款
        	TransferManage manage = serviceSession.getService(TransferManage.class);
            Map<String, Object> map = null;
            for (int i = 0; i < 3; i++) {
                map = manage.createTransferMap(params.get("mchnt_txn_ssn") + i, params.get("in_cust_no"),
                        configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME), params.get("amt"), "transferBmu");
                if (map != null && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")))) {
                    logger.info("体验金返还-退款成功");
                    i = 3;
                } else {
                    logger.info("体验金返还-退款失败-第" + i + "次");
                }
            }
        }
        
    }
    
    private T6285 selectT6285F(Connection connection, int F01) throws SQLException {
        T6285 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6285 WHERE T6285.F01 = ? ")){
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6285();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getDate(8);
                    record.F09 = T6285_F09.parse(resultSet.getString(9));
                    record.F10 = resultSet.getTimestamp(10);
                    record.F11 = resultSet.getBigDecimal(11);
                }
            }
        }
        return record;
    }
    
    private T6101 selectT6101F(Connection connection, int F02, T6101_F03 F03) throws SQLException {
        T6101 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? ")) {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03.name());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6101();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = T6101_F03.parse(resultSet.getString(3));
                    record.F04 = resultSet.getString(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getTimestamp(7);
                }
            }
        }
        return record;
    }
    
}
