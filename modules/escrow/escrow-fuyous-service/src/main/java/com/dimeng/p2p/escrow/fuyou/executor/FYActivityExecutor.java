package com.dimeng.p2p.escrow.fuyou.executor;

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
import com.dimeng.p2p.S62.entities.T6289;
import com.dimeng.p2p.S62.enums.T6289_F09;
import com.dimeng.p2p.S65.entities.T6525;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.ActivityRepaymentExecutor;

/**
 * 
 * 加息券利息返还执行器
 * <优化——20160129>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月12日]
 */
@AchieveVersion(version = 20160112)
@ResourceAnnotation
public class FYActivityExecutor extends ActivityRepaymentExecutor {
    
    public FYActivityExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        params = new HashMap<String, String>();
        super.doConfirm(connection, orderId, params);
    }
    
    /**
     * {@inheritDoc}
     *  托管调用接口
     */
    @Override
    protected void callFace(SQLConnection connection, int orderId, Map<String, String> params) throws SQLException {
        try {
            T6525 t6525 = getT6525F(connection, orderId);
            T6289 t6289 = selectT6289F(connection, t6525.F08);
            // 利息返还平台往来账户
            T6101 hkrAcount = selectT6101F(connection, t6289.F03, T6101_F03.WLZH);
            // 收款人往来账户
            T6101 skrAcount = null;
            if (t6289.F03 == t6289.F04) {
                skrAcount = hkrAcount;
            } else {
                skrAcount = selectT6101F(connection, t6289.F04, T6101_F03.WLZH);
            }
            if (skrAcount == null) {
                throw new LogicalException("加息券还款，收款人往来账户不存在!");
            }
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
            try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
                // 流水号
                String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.JXJL.name());
                // 付款账号(平台第三方账号)
                String out_cust_no = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
                // 收款人账号
                String in_cust_no = getUserCustId(connection, skrAcount.F02);
                String amt = PayUtil.getAmt(t6289.F07);
                // ***P2P平台调用转账接
                TransferManage manage = serviceSession.getService(TransferManage.class);
                Map<String, Object> map = null;
                if (t6289.F07.compareTo(BigDecimal.ZERO) > 0) {
                    map = manage.createTransferMap(mchnt_txn_ssn, out_cust_no, in_cust_no, amt, "transferBmu");
                }
                if (map != null && FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code"))) {
                    params.put("YN", "true");
                    params.put("amt", amt);
                    params.put("in_cust_no", in_cust_no);
                    params.put("mchnt_txn_ssn", mchnt_txn_ssn);
                    updateT6501(connection, mchnt_txn_ssn, orderId);
                } else {
                    params.put("YN", "false");
                    logger.error("FYActivityExecutor.class,转账失败-加息券利息返还");
                    throw new LogicalException("转账失败-加息券利息返还!");
                }
            }
        } catch (Throwable e) {
            logger.error("转账失败-加息券利息返还!", e);
            throw new LogicalException("转账失败-加息券利息返还!");
        }
    }
    
    /**
     * {@inheritDoc}
     * 异常回滚
     */
    @Override
    protected void rollbackFace(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        if (!Boolean.parseBoolean(params.get("YN"))){
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
                map = manage.createTransferMap(params.get("mchnt_txn_ssn") + i,
                        params.get("in_cust_no"), configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME),
                        params.get("amt"), "transferBmu");
                if (map != null && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")))) {
                    logger.info("加息券-退款成功");
                    i = 3;
                } else {
                    logger.info("加息券-退款失败-第" + i + "次");
                }
            }
        }
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
    
    /**
     * 查询是否有加息券还款订单
     *
     * @param orderId
     * @return
     * @throws Throwable
     */
    private T6525 getT6525F(SQLConnection connection, int orderId) throws Throwable {
        T6525 t6525 = null;
        try (PreparedStatement pstm = connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S65.T6525 WHERE F01 = ? ")) {
            pstm.setInt(1, orderId);
            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) {
                    t6525 = new T6525();
                    t6525.F01 = resultSet.getInt(1);
                    t6525.F02 = resultSet.getInt(2);
                    t6525.F03 = resultSet.getInt(3);
                    t6525.F04 = resultSet.getInt(4);
                    t6525.F05 = resultSet.getBigDecimal(5);
                    t6525.F06 = resultSet.getInt(6);
                    t6525.F07 = resultSet.getInt(7);
                    t6525.F08 = resultSet.getInt(8);
                    t6525.F09 = resultSet.getInt(9);
                }
            }
        }
        return t6525;
    }
    
    /**
     * 查询加息券利息返还记录
     *
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private T6289 selectT6289F(Connection connection, int F01) throws SQLException {
        T6289 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13 FROM S62.T6289 WHERE T6289.F01 = ? LIMIT 1 ")) {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6289();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getDate(8);
                    record.F09 = T6289_F09.parse(resultSet.getString(9));
                    record.F10 = resultSet.getTimestamp(10);
                    record.F11 = resultSet.getBigDecimal(11);
                    record.F12 = resultSet.getInt(12);
                    record.F13 = resultSet.getInt(13);
                }
            }
        }
        return record;
    }
    
}
