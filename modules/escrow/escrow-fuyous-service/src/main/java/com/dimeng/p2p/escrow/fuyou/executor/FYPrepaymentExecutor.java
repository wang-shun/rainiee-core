package com.dimeng.p2p.escrow.fuyou.executor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F27;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6517;
import com.dimeng.p2p.S65.entities.T6521;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.TenderPrepaymentExecutor;

/**
 * 
 * 提前还款
 * <富友托管>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月28日]
 */
@ResourceAnnotation
public class FYPrepaymentExecutor extends TenderPrepaymentExecutor {
	
    public FYPrepaymentExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType() {
        return FYPrepaymentExecutor.class;
    }
    
    @Override
    protected void doSubmit(SQLConnection connection, int orderId, Map<String, String> ps) throws Throwable {
        // 订单查询
        T6521 t6521 = selectT6521(connection, orderId);
        // 根据订单ID查询订单
        T6501 t6501 = selectT6501(connection, orderId);
        // 锁定标
        T6230 t6230 = selectT6230(connection, t6521.F03);
        if (t6230 == null) {
            throw new LogicalException("标记录不存在!");
        }
        if (t6521 == null || t6501 == null) {
            throw new LogicalException("查询提前还款订单失败!");
        }
        if (t6521.F06.compareTo(BigDecimal.ZERO) == 0) {
            ps.put("success", "true");
            logger.info("金额为0，不调用第三方接口");
            return;
        }
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            // 出账账号(还款人账户)
            String czzh = getUserCustId(connection, t6521.F09);
            String out_cust_no = czzh;
            String mchnt_txn_ssn = t6501.F10;
            TransferManage manage = serviceSession.getService(TransferManage.class);
            
            //判断类型是否为手续费，如果是，则收款方为平台，调用的接口为 商户与个人间的转账接口
            if (FeeCode.TZ_WYJ_SXF == t6521.F07) {
                // 把借款人的提前还款手续费 转账到平台账户上去
                Map<String, Object> map = manage.createTransferMap(mchnt_txn_ssn, out_cust_no,
                        configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME), PayUtil.getAmt(t6521.F06), "transferBmu");
                // 判断是否成功
                if (map != null && FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code"))) {
                    logger.info("平台收取还款人的提前还款手续费成功");
                    ps.put("success", "true");
                } else {
                	this.updateT6252(connection, t6521, "WH");
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                    logger.info("平台收取还款人的提前还款手续费失败，还款人ID：" + t6521.F09 + ",手续费金额：" + t6521.F06 + ",还款时间：" + time  + ",返回码：" + map.get("resp_code"));
                    ps.put("success", "false");
                }
            } else {
            	BigDecimal fee = BigDecimal.ZERO;
                // 接口地址
                if (t6521.F07 == FeeCode.TZ_LX || t6521.F07 == FeeCode.TZ_FX) {
                    // 计算投资管理费
                    T6238 t6238 = selectT6238(connection, t6230.F01);
                    fee = t6521.F06.multiply(t6238.F03).setScale(2, BigDecimal.ROUND_HALF_UP);                    
                }
                // 把借款者的钱划拨到投资者的账户上去
                Map<String, Object> map = manage.createTransferMap(
                        mchnt_txn_ssn, out_cust_no, getUserCustId(connection, t6521.F02), PayUtil.getAmt(t6521.F06.subtract(fee)), "");
                // 判断是否成功
                if (map != null && FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code"))) {
                    logger.info("投资人收取还款人的提前还款金额成功");
                    ps.put("success", "true");
                    if (fee.compareTo(BigDecimal.ZERO) > 0) { 
                    	 logger.info(String.format("散标还款投资管理费:%s,标题：%s,债权ID:%s,还款期号：%s", t6230.F25, t6230.F03,t6521.F04,t6521.F05));
                        // 插入转账订单
                        T6501 zzt6501 = new T6501();
                        zzt6501.F02 = OrderType.TRANSFER.orderType();
                        zzt6501.F03 = T6501_F03.DTJ;
                        zzt6501.F07 = T6501_F07.XT;
                        zzt6501.F08 = t6230.F02;
                       // zzt6501.F08 = t6521.F02;
                        zzt6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.GLF.name());
                        zzt6501.F12 = String.format("散标还款投资管理费:%s，标题：%s", t6230.F25, t6230.F03);
                        zzt6501.F04 = getCurrentTimestamp(connection);
                        zzt6501.F13 = fee;
                        int ordId = insertT6501(connection, zzt6501);
                        T6517 t6517 = new T6517();
                        t6517.F01 = ordId;
                        t6517.F02 = fee;
                       t6517.F03 = t6230.F02;
                      //  t6517.F03 = t6521.F02;
                        t6517.F04 = getPTID(connection);
                        t6517.F05 = String.format("散标还款投资管理费:%s，标题：%s", t6230.F25, t6230.F03);
                        t6517.F06 = FeeCode.GLF;
                        insertT6517(connection, t6517);
                    }
                } else {
                	this.updateT6252(connection, t6521, "WH");
                    String msg = BackCodeInfo.info(map.get("resp_code").toString());
                    ps.put("msg", msg);
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                    logger.info("投资人收取还款人的提前还款金额失败，还款人ID：" + t6521.F09 + ",金额：" + t6521.F06 + ",还款时间：" + time + ",备注：" + msg);
                    ps.put("success", "false");
                }
            }
        }
    }
    
    protected T6230 selectT6230(Connection connection, int F01) throws SQLException {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F27 FROM S62.T6230 WHERE T6230.F01 = ?")) {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6230();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = T6230_F10.parse(resultSet.getString(10));
                    record.F11 = T6230_F11.parse(resultSet.getString(11));
                    record.F12 = T6230_F12.parse(resultSet.getString(12));
                    record.F13 = T6230_F13.parse(resultSet.getString(13));
                    record.F14 = T6230_F14.parse(resultSet.getString(14));
                    record.F15 = T6230_F15.parse(resultSet.getString(15));
                    record.F16 = T6230_F16.parse(resultSet.getString(16));
                    record.F17 = T6230_F17.parse(resultSet.getString(17));
                    record.F18 = resultSet.getInt(18);
                    record.F19 = resultSet.getInt(19);
                    record.F20 = T6230_F20.parse(resultSet.getString(20));
                    record.F21 = resultSet.getString(21);
                    record.F22 = resultSet.getTimestamp(22);
                    record.F23 = resultSet.getInt(23);
                    record.F24 = resultSet.getTimestamp(24);
                    record.F25 = resultSet.getString(25);
                    record.F26 = resultSet.getBigDecimal(26);
                    record.F27 = T6230_F27.parse(resultSet.getString(27));
                }
            }
        }
        return record;
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
    
    protected void updateT6252(Connection connection, T6521 t6521, String status) throws Throwable {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6252 SET F09= ? WHERE F02 = ? AND F04 = ? AND F05 = ? AND F06 = ? AND F11 = ?")) {
        	ps.setString(1, status);
            ps.setInt(2, t6521.F03);
            ps.setInt(3, t6521.F02);
            ps.setInt(4, t6521.F07);
            ps.setInt(5, t6521.F05);
            ps.setInt(6, t6521.F04);
            ps.executeUpdate();
        }
    }
    
}
