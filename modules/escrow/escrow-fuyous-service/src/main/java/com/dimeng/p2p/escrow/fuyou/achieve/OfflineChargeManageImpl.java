package com.dimeng.p2p.escrow.fuyou.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6509;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S71.entities.T7150;
import com.dimeng.p2p.S71.enums.T7150_F03;
import com.dimeng.p2p.S71.enums.T7150_F05;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.OfflineChargeManage;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;

/**
 * 
 * 线下充值审核通过
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月24日]
 */
public class OfflineChargeManageImpl extends AbstractEscrowService implements OfflineChargeManage
{
    
    public OfflineChargeManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int checkCharge(int id, boolean passed)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new LogicalException("充值记录不存在!");
        }
        int orderId = 0;
        try (Connection connection = getConnection())
        {
            serviceResource.openTransactions(connection);
            try
            {
                T7150 t7150 = selectT7150(connection, id);
                if (t7150 == null)
                {
                    throw new LogicalException("充值记录不存在!");
                }
                if (t7150.F05 != T7150_F05.DSH)
                {
                    throw new LogicalException("不是待审核状态,不能审核通过!");
                }
                if (passed)
                {
                    orderId = addOfflineChargeOrder(connection, t7150);
                }
                else
                {
                    try (PreparedStatement ps =
                        connection.prepareStatement("UPDATE S71.T7150 SET F05 = ?, F09 = ?, F10 = CURRENT_TIMESTAMP() WHERE F01 = ?"))
                    {
                        ps.setString(1, T7150_F05.YQX.name());
                        ps.setInt(2, serviceResource.getSession().getAccountId());
                        ps.setInt(3, id);
                        ps.executeUpdate();
                    }
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        return orderId;
    }
    
    /**
     * 添加线下充值订单
     * @param connection
     * @param t7150
     * @return
     * @throws Throwable
     */
    private int addOfflineChargeOrder(Connection connection, T7150 t7150)
        throws Throwable
    {
        /*int orderId = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?,F03 = ?, F04 = CURRENT_TIMESTAMP(), F07 = ?, F09 = ?, F08 = ?, F10 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, OrderType.OFFLINECHARGE.orderType());
            pstmt.setString(2, T6501_F03.DTJ.name());
            pstmt.setString(3, T6501_F07.HT.name());
            pstmt.setInt(4, serviceResource.getSession().getAccountId());
            pstmt.setInt(5, t7150.F02);
            pstmt.setString(6, MchntTxnSsn.getMts(FuyouTypeEnum.XXCZ.name()));
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    orderId = resultSet.getInt(1);
                }
            }
        }*/
        
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.OFFLINECHARGE.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F07 = T6501_F07.HT;
        t6501.F08 = t7150.F02;
        t6501.F09 = serviceResource.getSession().getAccountId();
        t6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.XXCZ.name());
        t6501.F13 = t7150.F04;
        int orderId = insertT6501(connection, t6501);
        
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6509 SET F01 = ?, F02 = ?, F03 = ?, F06 = ?"))
        {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, t7150.F02);
            pstmt.setBigDecimal(3, t7150.F04);
            pstmt.setInt(4, t7150.F01);
            pstmt.execute();
        }
        return orderId;
        
    }
    
    @Override
    public void exectCheck(ServiceSession serviceSession, int id) throws Throwable {
        if (id <= 0) {
            throw new LogicalException("充值记录不存在!");
        }
        try (Connection connection = getConnection()) {
            serviceResource.openTransactions(connection);
            try {
                T6509 t6509 = selectT6509(connection, id);
                if (t6509 == null) {
                    throw new LogicalException("充值记录不存在!");
                }
                T7150 t7150 = selectT7150(connection, t6509.F06);
                T6101 yhT6101 = selectT6101(connection, t6509.F02, T6101_F03.WLZH, true);
                if (yhT6101 == null) {
                    throw new LogicalException("用户资金账户不存在!");
                }
                TransferManage manage = serviceSession.getService(TransferManage.class);
                String mchnt_txn_ssn = selectT6501F10(connection, t6509.F01);
                Map<String, Object> param = manage.createTransferMap(mchnt_txn_ssn,
                        configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME),
                        selectT6119(connection, t6509.F02),
                        getAmt(t6509.F03),
                        "transferBmu");
                if (param == null) {
                    throw new LogicalException("转账接口失败！");
                } else if (!FuyouRespCode.JYCG.getRespCode().equals(param.get("resp_code"))) {
                    throw new LogicalException(BackCodeInfo.info(param.get("resp_code").toString()));
                }
                
                T6101 ptT6101 = selectT6101(connection, getPTID(connection), T6101_F03.WLZH, true);
                // 更新平台账号信息
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 - ?, F07 = CURRENT_TIMESTAMP() WHERE F01 = ?")) {
                    pstmt.setBigDecimal(1, t6509.F03);
                    pstmt.setInt(2, ptT6101.F01);
                    pstmt.executeUpdate();
                }
                
                // 插入平台资金交易记录，充值
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 =CURRENT_TIMESTAMP(), F07 = ?, F08 = ?, F09 = ?")) {
                    pstmt.setInt(1, ptT6101.F01);
                    pstmt.setInt(2, FeeCode.CZ_XX);
                    pstmt.setInt(3, yhT6101.F01);
                    pstmt.setBigDecimal(4, t6509.F03);
                    pstmt.setBigDecimal(5, ptT6101.F06.subtract(t6509.F03));
                    pstmt.setString(6, String.format("线下充值:%s", t7150.F08));
                    pstmt.execute();
                }
                
                // 更新用户账号信息
                try (PreparedStatement pstmt =  connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = CURRENT_TIMESTAMP() WHERE F01 = ?")) {
                    pstmt.setBigDecimal(1, t6509.F03);
                    pstmt.setInt(2, yhT6101.F01);
                    pstmt.executeUpdate();
                }
                
                // 插入用户资金交易记录，充值
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 =CURRENT_TIMESTAMP(), F06 = ?, F08 = ?, F09 = ?")) {
                    pstmt.setInt(1, yhT6101.F01);
                    pstmt.setInt(2, FeeCode.CZ_XX);
                    pstmt.setInt(3, yhT6101.F01);
                    pstmt.setBigDecimal(4, t6509.F03);
                    pstmt.setBigDecimal(5, yhT6101.F06.add(t6509.F03));
                    pstmt.setString(6, String.format("线下充值:%s", t7150.F08));
                    pstmt.execute();
                }
                
                try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S71.T7150 SET F05 = ?, F09 = ?, F10 = CURRENT_TIMESTAMP() WHERE F01 = ?")) {
                    pstmt.setString(1, T7150_F05.YCZ.name());
                    pstmt.setInt(2, serviceResource.getSession().getAccountId());
                    pstmt.setInt(3, t7150.F01);
                    pstmt.executeUpdate();
                }
                try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6509 SET F05 = ? WHERE F01 = ?")) {
                    pstmt.setString(1, mchnt_txn_ssn);
                    pstmt.setInt(2, t6509.F01);
                    pstmt.executeUpdate();
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F05 = CURRENT_TIMESTAMP(), F06 =CURRENT_TIMESTAMP() WHERE F01 = ?")) {
                    pstmt.setString(1, T6501_F03.CG.name());
                    pstmt.setInt(2, id);
                    pstmt.executeUpdate();
                }
                writeLog(connection, "操作日志", "线下充值审核通过");
                serviceResource.commit(connection);
            } catch (Exception e) {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * 线下充值订单
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private T6509 selectT6509(Connection connection, int F01)
        throws SQLException
    {
        T6509 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S65.T6509 WHERE T6509.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6509();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getInt(6);
                }
            }
        }
        return record;
    }
    
    /**
     * 流水号
     * <功能详细描述>
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private String selectT6501F10(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F10 FROM S65.T6501 WHERE T6501.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return null;
    }
    
    private T7150 selectT7150(Connection connection, int F01)
        throws SQLException
    {
        T7150 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S71.T7150 WHERE T7150.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T7150();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = T7150_F03.parse(resultSet.getString(3));
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = T7150_F05.parse(resultSet.getString(5));
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getTimestamp(7);
                    record.F08 = resultSet.getString(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = resultSet.getTimestamp(10);
                    record.F11 = resultSet.getString(11);
                }
            }
        }
        return record;
    }
}
