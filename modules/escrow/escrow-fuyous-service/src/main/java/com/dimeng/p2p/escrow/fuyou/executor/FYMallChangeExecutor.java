package com.dimeng.p2p.escrow.fuyou.executor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.dimeng.p2p.S65.entities.T6555;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.escrow.fuyou.entity.console.BalanceEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.face.BalanceFace;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.MallChangeExecutor;

/**
 * 
 * 购买商品执行器
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月19日]
 */
@AchieveVersion(version = 20160101)
@ResourceAnnotation
public class FYMallChangeExecutor extends MallChangeExecutor {
    
    public FYMallChangeExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    public void confirm(int orderId, Map<String, String> params) throws Throwable {
        params = new HashMap<String, String>();
        if (!balanchQuery(orderId, params)) {
            throw new LogicalException("您的余额不足！");
        }
        String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.GMXP.name());
        if (!updateT6501(orderId, mchnt_txn_ssn)) {
            throw new LogicalException("购买失败！");
        }
        logger.info("商户购买,订单：" + orderId);
        super.confirm(orderId, params);
        if (!selectT6501F03(orderId)) {
            throw new LogicalException("购买失败！");
        }
        params.put("mchnt_txn_ssn", mchnt_txn_ssn);
        doConfirmFyou(orderId, params);
    }
    
    private boolean balanchQuery(int orderId, Map<String, String> params) throws SQLException {
        ArrayList<BalanceEntity> listBalance = null;
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            BalanceFace face = new BalanceFace();
            String mchnt_cd = configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID);
            String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.YECX.name());
            String mchnt_txn_dt = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String cust_no = null;
            T6555 t6555 = null;
            try (Connection connection = getConnection()) {
                // 商品购买订单 
                t6555 = selectT6555(connection, orderId);
                cust_no = getUserCustId((SQLConnection)connection, t6555.F02);
                params.put("cust_no", cust_no);
            } catch (Throwable e) {
                logger.error(e);
            }
            String actionUrl = configureProvider.format(FuyouVariable.FUYOU_QUERY_BLANCE);
            listBalance = face.executeBalance(mchnt_cd, mchnt_txn_ssn, mchnt_txn_dt, cust_no, actionUrl, serviceSession);
            if (listBalance.get(0).ca_balance.compareTo(t6555.F04) < 0) {
                return false;
            }
        } catch (IOException e) {
            logger.error(e);
        } catch (Throwable e) {
        	logger.error(e);
        }
        return true;
    }
    
    /**
     * 调用富友接口
     * <放款>
     * @param connection
     * @param orderId
     * @throws IOException 
     * @throws SQLException 
     */
    private void doConfirmFyou(int orderId, Map<String, String> params) throws IOException, SQLException {
        try (Connection connection = getConnection()) {
            // 商品购买订单 
            T6555 t6555 = selectT6555(connection, orderId);
            if (t6555 == null) {
                throw new LogicalException("订单详细不存在！");
            }
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
            try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            	TransferManage manage = serviceSession.getService(TransferManage.class);
                String amt = PayUtil.getAmt(t6555.F04);
                Map<String, Object> map = manage.createTransferMap(
                        params.get("mchnt_txn_ssn"), params.get("cust_no"), configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME),
                        amt, "transferBmu");
                if (map != null && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")))) {
                    logger.info("购物成功-订单：" + orderId);
                    updateT6501F12(orderId, T6501_F03.CG.name(), "支付成功");
                    params.put("success", "true");
                    params.put("resp_code", map.get("resp_code").toString());
                    params.put("IN_CUST_NO", configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME));
                    params.put("OUT_CUST_NO", params.get("cust_no"));
                    params.put("AMT", amt);
                } else {
                    params.put("success", "false");
                    logger.info("购物支付失败-订单");
                    updateT6501F12(orderId, T6501_F03.SB.name(), "支付失败");
                    throw new LogicalException(BackCodeInfo.info(params.get("resp_code")));
                }
            } catch (Throwable e) {
                logger.error("购物支付失败-订单:" + orderId, e);
            }
        } catch (Throwable e1) {
            logger.error(e1);
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
    
    private boolean selectT6501F03(int orderId) throws Throwable {
        try (Connection connection = getConnection()){
            T6501 t6501 = selectT6501(connection, orderId);
            if (T6501_F03.CG != t6501.F03) {
                return false;
            }
        }
        return true;
    }
    
    private void updateT6501F12(int orderId, String F03, String F12) throws Throwable {
        try (Connection connection = getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F12 = ? WHERE F01 = ?")) {
                pstmt.setString(1, F03);
                pstmt.setString(2, F12);
                pstmt.setInt(3, orderId);
                pstmt.execute();
            }
        }
    }
    
}
