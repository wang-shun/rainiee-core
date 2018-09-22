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
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6507;
import com.dimeng.p2p.S65.entities.T6517;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.TenderExchangeExecutor;

/**
 * 
 * 购买债权处理方法
 * <优化版-20160127>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年5月25日]
 */
@AchieveVersion(version = 20160127)
@ResourceAnnotation
public class FYBidExchangeExecutor extends TenderExchangeExecutor {
    
    public FYBidExchangeExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)  throws Throwable {
        // 过虑该债权是否为借款人的标
        if (isOneself(connection, orderId)) {
            throw new LogicalException("自己不能购买自己的标债权!");
        }
        
        params = new HashMap<String, String>();
        // 插入流水号
        String MCHNT_TXN_SSN = MchntTxnSsn.getMts(FuyouTypeEnum.GMZQ.name());
        params.put("mchnt_txn_ssn", MCHNT_TXN_SSN);
        updateT6501(connection, MCHNT_TXN_SSN, orderId);
        super.doConfirm(connection, orderId, params);
        
    }
    
    /**
     * {@inheritDoc}
     * 接口调用
     */
    @Override
    protected void callFace(SQLConnection connection, int orderId, Map<String, String> params) throws SQLException {
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        T6507 t6507 = selectT6507(connection, orderId);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            String IN_CUST_NO = getSellCustId(connection, t6507.F01);
            String OUT_CUST_NO = getUserCustId(connection, t6507.F03);
            if (OUT_CUST_NO.equals(IN_CUST_NO)) {
                throw new LogicalException("自己不能购买自己转让的债权!");
            }
            TransferManage manage = serviceSession.getService(TransferManage.class);
            Map<String, Object> map = manage.createTransferMap(
                    params.get("mchnt_txn_ssn"), OUT_CUST_NO, IN_CUST_NO,
                    PayUtil.getAmt(t6507.F05.subtract(t6507.F06)), "");
            if (map != null && FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code"))) {
                logger.info("债权购买成功-订单：" + t6507.F01);
                // 记录购买记录，防止确认时出错——>调用退钱
                params.put("YN", "true");
                params.put("out_cust_no", OUT_CUST_NO);
                params.put("in_cust_no", IN_CUST_NO);
                params.put("amt", PayUtil.getAmt(t6507.F05.subtract(t6507.F06)));
                params.put("mchnt_amt", PayUtil.getAmt(t6507.F06));
                if (t6507.F06.compareTo(BigDecimal.ZERO) > 0) { 
                    // 插入转账订单
                    T6501 zzt6501 = new T6501();
                    zzt6501.F02 = OrderType.TRANSFER.orderType();
                    zzt6501.F03 = T6501_F03.DTJ;
                    zzt6501.F07 = T6501_F07.XT;
                    zzt6501.F08 = t6507.F03;
                    zzt6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.ZQZR_SXF.name());
                    zzt6501.F12 = String.format("散标债权转让手续费");
                    zzt6501.F04 = getCurrentTimestamp(connection);
                    zzt6501.F13 = t6507.F06;
                    int ordId = insertT6501(connection, zzt6501);
                    T6517 t6517 = new T6517();
                    t6517.F01 = ordId;
                    t6517.F02 = t6507.F06;
                    t6517.F03 = t6507.F03;
                    t6517.F04 = getPTID(connection);
                    t6517.F05 = String.format("散标债权转让手续费");
                    t6517.F06 = FeeCode.ZQZR_SXF;
                    insertT6517(connection, t6517);
                }
            } else {
                params.put("YN", "false");
                logger.info("债权购买失败");
                throw new LogicalException("购买债权失败!");
            }
        } catch (IOException e) {
            logger.error("购买债权失败!", e);
            throw new LogicalException("购买债权失败!");
        } catch (Throwable e) {
            logger.error("购买债权失败!", e);
            throw new LogicalException("购买债权失败!");
        }
    }
    
    protected int insertT6501(Connection connection, T6501 entity) throws Throwable {
        int orderId = 0;
        StringBuilder sql = new StringBuilder("INSERT INTO S65.T6501 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F10 = ?,F11 = ?,F12 = ?,F13 = ?");
        if (entity.F09 != null) {
            sql.append(",F09 = ?");
        }
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, entity.F02);
            pstmt.setString(2, entity.F03.name());
            pstmt.setTimestamp(3, entity.F04);
            pstmt.setTimestamp(4, entity.F05);
            pstmt.setTimestamp(5, entity.F06);
            pstmt.setString(6, entity.F07.name());
            pstmt.setInt(7, entity.F08);
            pstmt.setString(8, entity.F10);
            pstmt.setString(9, entity.F11 == null ? T6501_F11.F.name() : entity.F11.name());
            pstmt.setString(10, entity.F12);
            pstmt.setBigDecimal(11, entity.F13);
            if (entity.F09 != null) {
                pstmt.setInt(12, entity.F09);
            }
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                    orderId = resultSet.getInt(1);
                }
            }
        }
        if (orderId == 0)  {
            logger.error("TenderConfirmExecutor.insertT6501():数据库异常");
            throw new SQLException("数据库异常");
        }
        return orderId;
    }
    
    protected void insertT6517(Connection connection, T6517 t6517) throws Throwable {
        try (PreparedStatement ps =
            connection.prepareStatement("INSERT INTO S65.T6517 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?")) {
            ps.setInt(1, t6517.F01);
            ps.setBigDecimal(2, t6517.F02);
            ps.setInt(3, t6517.F03);
            ps.setInt(4, t6517.F04);
            ps.setString(5, t6517.F05);
            ps.setInt(6, t6517.F06);
            ps.execute();
        }
    }
    
    /**
     * {@inheritDoc}
     * 接口回滚
     */
    @Override
    protected void rollbackFace(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        if (!Boolean.parseBoolean(params.get("YN"))) {  
        	return;
        }
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        String MCHNT_TXN_SSN = params.get("mchnt_txn_ssn").concat("BACK");
        // 购买者
        String IN_CUST_NO = params.get("out_cust_no");
        // 转卖者
        String OUT_CUST_NO = params.get("in_cust_no");
        String AMT = params.get("amt");
        if (OUT_CUST_NO.equals(IN_CUST_NO)) {
            throw new LogicalException("自己不能购买自己转让的债权!");
        }
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
        	TransferManage manage = serviceSession.getService(TransferManage.class);
            Map<String, Object> map = manage.createTransferMap(MCHNT_TXN_SSN, OUT_CUST_NO, IN_CUST_NO, AMT,  "");
            if (map != null && FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code"))) {
                // 手续退还
                doSubmitFyouBackFee(configureProvider, connection, orderId, params, serviceSession);
                logger.info("债权购买失败后退单-成功-订单：" + orderId);
            } else {
                logger.info("债权购买失败后退单,失败");
                throw new LogicalException("债权购买失败后退单，失败!");
            }
        }
    }
    
    /**
     * 更新失败 回滚 手续费
     * <债权转用>
     * @param connection
     * @param orderId
     * @throws Throwable 
     */
    private void doSubmitFyouBackFee(ConfigureProvider configureProvider, Connection connection, int orderId,  Map<String, String> params, ServiceSession serviceSession) throws Throwable {
        String MCHNT_TXN_SSN = params.get("mchnt_txn_ssn").concat("BACKF");
        // 购买者
        String IN_CUST_NO = params.get("out_cust_no");
        // 平台账号
        String OUT_CUST_NO = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
        String AMT = params.get("mchnt_amt");
        // 把借款人的提前还款手续费 转账到平台账户上去
        TransferManage manage = serviceSession.getService(TransferManage.class);
        Map<String, Object> map = manage.createTransferMap(MCHNT_TXN_SSN, OUT_CUST_NO, IN_CUST_NO, AMT, "transferBmu");
        if (map != null && FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code"))) {
            logger.info("债权购买失败后退单-成功还手续费-订单：" + orderId);
        } else {
            logger.info("债权购买失败后退单-手续费,失败");
            throw new LogicalException("债权购买失败后退单-手续费，失败!");
        }
    }
    
    /**
     * 查询债权转让订单
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    public T6507 selectT6507(Connection connection, int F01) throws SQLException {
        T6507 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S65.T6507 WHERE T6507.F01 = ? FOR UPDATE")){
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6507();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                }
            }
        }
        return record;
    }
    
    /**
     * 债权人第三方标识
     * <功能详细描述>
     * @param orderId
     * @return
     * @throws Throwable
     */
    private String getSellCustId(SQLConnection connection, int orderId) throws Throwable {
        // 获取用户商户号
        StringBuffer sb = new StringBuffer("SELECT T6119.F03 FROM S65.T6507 ");
        sb.append(" LEFT JOIN S62.T6260 ON T6260.F01=T6507.F02");
        sb.append(" LEFT JOIN S62.T6251 ON T6251.F01=T6260.F02");
        sb.append(" LEFT JOIN S61.T6119 ON T6119.F01=T6251.F04");
        sb.append(" WHERE T6507.F01 = ? LIMIT 1 ");
        try (PreparedStatement pstmt = connection.prepareStatement(sb.toString())) {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(1);
                }
            }
        }
        return "0";
    }
    
    /**
     * 判断此债权是否为自己的标
     * <不能购买自己的标>
     * @param connection
     * @param orderId
     * @return
     * @throws Throwable
     */
    private boolean isOneself(SQLConnection connection, int orderId) throws Throwable {
        T6501 t6501 = selectT6501(connection, orderId);
        // 标用户ID
        StringBuffer sb = new StringBuffer("SELECT T6230.F02 FROM S65.T6507");
        sb.append(" LEFT JOIN S62.T6260 ON T6260.F01 = T6507.F02");
        sb.append(" LEFT JOIN S62.T6251 ON T6251.F01 = T6260.F02");
        sb.append(" LEFT JOIN S62.T6230 ON T6230.F01 = T6251.F03");
        sb.append(" WHERE T6507.F01 = ? LIMIT 1 ");
        try (PreparedStatement pstmt = connection.prepareStatement(sb.toString())) {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) == t6501.F08) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
