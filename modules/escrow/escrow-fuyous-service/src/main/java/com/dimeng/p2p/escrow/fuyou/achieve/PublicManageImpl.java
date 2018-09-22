package com.dimeng.p2p.escrow.fuyou.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryDetailedEntity;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouEnum;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.face.TransactionQueryFace;
import com.dimeng.p2p.escrow.fuyou.service.PublicManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.modules.bid.console.service.entity.BidReturn;
import com.dimeng.util.DateHelper;
import com.dimeng.util.StringHelper;

/**
 * 
 * 还款、机构垫付，公共方法
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月12日]
 */
public class PublicManageImpl extends AbstractEscrowService implements PublicManage
{
    
    public PublicManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public void updateT6501(int orderId, String mchnt_txn_ssn)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6501 SET F10 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, mchnt_txn_ssn);
                pstmt.setInt(2, orderId);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void searchT6501(ServiceSession serviceSession, int orderId, Map<String, String> params, boolean flag)
        throws Throwable
    {
        T6501 t6501 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6501.F01, T6501.F02, T6501.F03, T6501.F04, T6501.F08, T6501.F10 FROM S65.T6501 WHERE T6501.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, orderId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6501 = new T6501();
                        t6501.F01 = resultSet.getInt(1);
                        t6501.F02 = resultSet.getInt(2);
                        t6501.F03 = T6501_F03.parse(resultSet.getString(3));
                        t6501.F04 = resultSet.getTimestamp(4);
                        t6501.F08 = resultSet.getInt(5);
                        t6501.F10 = resultSet.getString(6);
                    }
                }
            }
            
            switch (t6501.F03.toString())
            {
                case "DTJ":
                    params.put("state", "DTJ");
                    break;
                case "CG":
                    params.put("state", "CG");
                    break;
                case "DQR":
                case "SB":
                    // 放款无需查询
                    if (OrderType.BID_CONFIRM.orderType() == t6501.F02)
                    {
                        updateT6501(connection,
                            MchntTxnSsn.getMts(FuyouTypeEnum.SDFK.name()),
                            t6501.F01,
                            T6501_F03.DTJ.name());
                        params.put("state", "DTJ");
                        break;
                    }
                    String busi_tp;
                    String czzh;
                    if (flag)
                    {
                        int usId = selectT6501(connection, orderId);
                        if (usId == getPTID(connection))
                        {
                            busi_tp = FuyouEnum.PWPC.name();
                            czzh = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
                        }
                        else
                        {
                            busi_tp = FuyouEnum.PW03.name();
                            czzh = selectT6119(connection, t6501.F08);
                        }
                    }
                    else
                    {
                        busi_tp = FuyouEnum.PW03.name();
                        czzh = selectT6119(connection, t6501.F08);
                    }
                    
                    // 对账<是否已功-成功则确认，未成功则更新流水号>
                    selectFuyou(czzh, t6501, busi_tp, connection, serviceSession, params);
                    break;
            }
        }
    }
    
    /**
     * 机构垫付增加流水号
     * @param connection
     * @param orderId
     * @param mchnt_txn_ssn
     * @throws Throwable
     */
    public void updateT6501DF(Connection connection, int orderId, String mchnt_txn_ssn)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6501 SET F10 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, mchnt_txn_ssn);
            pstmt.setInt(2, orderId);
            pstmt.execute();
        }
    }
    
    /**
     * 对账
     * @param czzh
     * @param t6501
     * @param busi_tp
     * @param connection
     * @param serviceSession
     * @param params
     * @throws Throwable
     */
    private void selectFuyou(String czzh, T6501 t6501, String busi_tp, Connection connection,
        ServiceSession serviceSession, Map<String, String> params)
        throws Throwable
    {
        //调用第三方平台
        TransactionQueryResponseEntity result =
            TransactionQueryFace.executeTransactionQuery(configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID),
                MchntTxnSsn.getMts(FuyouTypeEnum.JYCX.name()),
                busi_tp,
                new SimpleDateFormat("yyyyMMdd").format(t6501.F04),
                new SimpleDateFormat("yyyyMMdd").format(t6501.F04),
                t6501.F10,
                czzh,
                "",
                "",
                "",
                "",
                configureProvider.format(FuyouVariable.FUYOU_TRADINGQUERY_URL),
                serviceSession);
        String resultCode = result.respCode;
        // 定义返回结果详情对象
        List<TransactionQueryDetailedEntity> respDetailList = null;
        if (FuyouRespCode.JYCG.getRespCode().equals(resultCode))
        {
            //结果详情
            respDetailList = result.detailedEntity;
            if (respDetailList != null && respDetailList.size() > 0)
            {
                for (TransactionQueryDetailedEntity detail : respDetailList)
                {
                    // 获取流水号
                    String serialNumStr = detail.getMchntSsn();
                    // 通过流水号 判断该笔交易是不是 要查询的那笔交易
                    if (!StringHelper.isEmpty(serialNumStr) && serialNumStr.equals(t6501.F10))
                    {
                        if (FuyouRespCode.JYCG.getRespCode().equals(detail.getTxnRspCd()))
                        { // 说明在托管平台成功了，在P2P这边意外失败了,现只差确认成功
                            updateT6501(connection, t6501.F10, t6501.F01, T6501_F03.DQR.name());
                            params.put("state", "DQR");
                        }
                        else
                        {
                            // 说明第托管平台失败了，P2P重新生成流水号
                            logger.info("订单：" + t6501.F01 + " -重新生成流水号");
                            t6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.NEW.name());
                            updateT6501(connection, t6501.F10, t6501.F01, T6501_F03.DTJ.name());
                            params.put("state", "DTJ");
                        }
                    }
                }
            }
            else
            {
                // 查询结果无，说明托管系统无此记录
                updateT6501(connection, t6501.F10, t6501.F01, T6501_F03.DTJ.name());
                params.put("state", "DTJ");
            }
        }
    }
    
    /**
     * 将订单改为待确认状态
     * @param connection
     * @param mchnt_txn_ssn 流水号
     * @param F01 订单ID
     * @throws SQLException
     */
    private void updateT6501(Connection connection, String mchnt_txn_ssn, int F01, String state)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F10 = ? WHERE F01 = ? "))
        {
            pstmt.setString(1, state);
            pstmt.setString(2, mchnt_txn_ssn);
            pstmt.setInt(3, F01);
            pstmt.execute();
        }
    }
    
    @Override
    public void updateT6252(int bidId, int term)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S62.T6252 SET F09 = ? WHERE F02 = ? AND F06 = ? AND F09 = ?"))
            {
                pstmt.setString(1, T6252_F09.WH.name());
                pstmt.setInt(2, bidId);
                pstmt.setInt(3, term);
                pstmt.setString(4, T6252_F09.HKZ.name());
                pstmt.execute();
            }
        }
    }
    
    @Override
    public BidReturn selectLoan(int bidId)
        throws Throwable
    {
        if (bidId <= 0)
        {
            throw new LogicalException("标记录不存在!");
        }
        BidReturn bidReturn = null;
        try (Connection connection = getConnection())
        {
            String state = selectT6230(connection, bidId);
            if (StringHelper.isEmpty(state))
            {
                throw new LogicalException("标记录不存在!");
            }
            if (!T6230_F20.DFK.name().equals(state))
            {
                throw new LogicalException("不是待放款状态,不能放款!");
            }
            int[] orderIds = selectT6501Loan(connection, bidId);
            if (orderIds == null)
            {
                return bidReturn;
            }
            bidReturn = new BidReturn();
            // 放款单
            bidReturn.bidOrderIds = orderIds;
            // 体验单
            bidReturn.experOrderIds = selectT6286Loan(connection, bidId);
            // 红包
            bidReturn.couponOrderIds = selectT6288Loan(connection, bidId);
        }
        return bidReturn;
    }
    
    @Override
    public int[] selectPayment(int bidId, int number)
        throws Throwable
    {
        if (bidId <= 0)
        {
            throw new LogicalException("标记录不存在!");
        }
        int[] orderIds = null;
        
        try (Connection connection = getConnection())
        {
            try
            {
            	serviceResource.openTransactions(connection);
                // 是否已生成订单
                orderIds = selectT6501Payment(connection, bidId, number);
                if (orderIds == null)
                {
                    serviceResource.commit(connection);
                    return orderIds;
                }
                String state = selectT6230(connection, bidId);
                if (StringHelper.isEmpty(state))
                {
                    throw new LogicalException("标记录不存在!");
                }
                if (!T6230_F20.HKZ.name().equals(state) && !T6230_F20.YDF.name().equals(state))
                {
                    throw new LogicalException("不是还款中状态,不能还款!");
                }
                
                T6252[] t6252s = selectAllT6252(connection, bidId, number);
                Date currentDate = getCurrentDate(connection);
                if (DateHelper.beforeDate(currentDate, t6252s[0].F08))
                {
                    throw new LogicalException("还未到还款时间，不能还款!");
                }
                BigDecimal temp = BigDecimal.ZERO;
                for (int i = 0; i < t6252s.length; i++)
                {
                    temp = temp.add(t6252s[i].F07);
                }
                if (t6252s == null || t6252s.length == 0)
                {
                    throw new LogicalException("不是还款中状态,不能还款!");
                }
                int accountId = serviceResource.getSession().getAccountId();
                //还款人往来账户
                T6101 hkrAcount = selectT6101(connection, accountId, T6101_F03.WLZH, false);
                if (hkrAcount.F06.compareTo(temp) == -1)
                {
                    throw new LogicalException("可用余额不足,不能还款!");
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        return orderIds;
    }
    
    protected Date getCurrentDate(Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_DATE()"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getDate(1);
                }
            }
        }
        return null;
    }
    
    protected T6252[] selectAllT6252(Connection connection, int F02, int F06)
        throws SQLException
    {
        ArrayList<T6252> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 = ? AND T6252.F09 = ? ORDER BY T6252.F05 DESC FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            pstmt.setInt(2, F06);
            pstmt.setString(3, T6252_F09.WH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6252 record = new T6252();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getDate(8);
                    record.F09 = T6252_F09.parse(resultSet.getString(9));
                    record.F10 = resultSet.getTimestamp(10);
                    record.F11 = resultSet.getInt(11);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        if (list != null && list.size() > 0)
        {
            for (T6252 t6252 : list)
            {
                try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6252 SET F09 = ? WHERE F01 =?"))
                {
                    ps.setString(1, T6252_F09.HKZ.name());
                    ps.setInt(2, t6252.F01);
                    ps.executeUpdate();
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6252[list.size()]));
    }
    
    /**
     * 统计标还款订单数
     * <还款订单>
     * @param connection
     * @param bidId 标ID
     * @param number 期数
     * @return <还款订单>
     * @throws Throwable
     */
    private int[] selectT6501Payment(Connection connection, int bidId, int number)
        throws Throwable
    {
        int[] orderIds = null;
        int index = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT DISTINCT(T6501.F01) FROM S65.T6501 LEFT JOIN S65.T6506 ON T6506.F01 = T6501.F01"
                + " LEFT JOIN S62.T6252 ON T6252.F02 = T6506.F03 AND T6252.F06 = T6506.F05 WHERE T6501.F02 = ? AND T6252.F02 = ? AND T6252.F06 = ? "))
        {
            pstmt.setInt(1, OrderType.BID_REPAYMENT.orderType());
            pstmt.setInt(2, bidId);
            pstmt.setInt(3, number);
            //            pstmt.setString(4, T6252_F09.HKZ.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    if (orderIds == null)
                    {
                        orderIds = new int[selectCountT6252(connection, bidId, number)];
                    }
                    orderIds[index++] = resultSet.getInt(1);
                }
            }
        }
        return orderIds;
    }
    
    /**
     * 统计标还款记录
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    private int selectCountT6252(Connection connection, int bidiId, int number)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT count(*) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 = ? "))
        {
            pstmt.setInt(1, bidiId);
            pstmt.setInt(2, number);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     *  添加加息券放款订单
     * <加息卷>
     * @param connection
     * @param bidId
     * @return
     * @throws SQLException 
     */
    private String selectT6288Loan(Connection connection, int bidId)
        throws SQLException
    {
        int countT6288 = selectCountT6288(connection, bidId);
        
        if (countT6288 == 0)
        {
            return null;
        }
        
        int[] couponOrderIds = new int[countT6288];
        StringBuilder strCouponOrderIds = new StringBuilder();
        int index = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT count(T6501.F01) FROM S65.T6501 "
                + "INNER JOIN S65.T6524 ON T6524.F01 = T6501.F01 WHERE T6524.F03 = ? "))
        {
            pstmt.setInt(1, bidId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    couponOrderIds[index] = resultSet.getInt(1);
                    strCouponOrderIds.append(couponOrderIds[index] + ",");
                    index++;
                }
            }
        }
        return String.valueOf(strCouponOrderIds);
    }
    
    /**
     * 统计标放款订单数
     * <体验金投标记录>
     * @param connection
     * @param bidId
     * @return
     * @throws SQLException 
     */
    private String selectT6286Loan(Connection connection, int bidId)
        throws SQLException
    {
        int countT6286 = selectCountT6286(connection, bidId);
        
        if (countT6286 == 0)
        {
            return null;
        }
        
        int[] experOrderIds = new int[countT6286];
        StringBuffer strExperOrderIds = new StringBuffer();
        int index = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6501.F01 FROM S65.T6501 "
                + "INNER JOIN S65.T6519 ON T6519.F01 = T6501.F01 WHERE T6519.F03 = ? "))
        {
            pstmt.setInt(1, bidId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    experOrderIds[index] = resultSet.getInt(1);
                    strExperOrderIds.append(experOrderIds[index] + ",");
                    index++;
                }
            }
        }
        return String.valueOf(strExperOrderIds);
    }
    
    /**
     * 统计标放款订单数
     * <放款订单>
     * @param connection
     * @param bidId 标ID
     * @return <放款订单>
     * @throws Throwable
     */
    private int[] selectT6501Loan(Connection connection, int bidId)
        throws Throwable
    {
        int[] orderIds = null;
        int index = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6501.F01 FROM S65.T6501 "
                + "INNER JOIN S65.T6505 ON T6505.F01 = T6501.F01 WHERE T6505.F03 = ? "))
        {
            pstmt.setInt(1, bidId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    if (orderIds == null)
                    {
                        orderIds = new int[selectCountT6250(connection, bidId)];
                    }
                    orderIds[index++] = resultSet.getInt(1);
                }
            }
        }
        return orderIds;
    }
    
    /**
     * 统计投标记录
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    private int selectCountT6250(Connection connection, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT count(*) FROM S62.T6250 WHERE F02 = ? "))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     * 统计体验记录
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    private int selectCountT6288(Connection connection, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT  count(*) FROM S62.T6288 WHERE F02 = ? "))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     * 统计体验记录
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    private int selectCountT6286(Connection connection, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT  count(*) FROM S62.T6286 WHERE F02 = ? "))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     * 查询标状态
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private String selectT6230(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F20 FROM S62.T6230 WHERE T6230.F01 = ? "))
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
    
    @Override
    public void updateT6250(T6250_F08 F08, int F01)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S62.T6250 SET T6250.F08 = ? WHERE T6250.F01 = ( SELECT T6505.F04 FROM S65.T6505 WHERE T6505.F01 = ? )"))
            {
                pstmt.setString(1, F08.name());
                pstmt.setInt(2, F01);
                pstmt.execute();
            }
        }
        
    }
    
    /**
     * 用户ID
     * <功能详细描述>
     * @param connection
     * @param orderId
     * @return
     * @throws SQLException
     */
    private int selectT6501(Connection connection, int orderId)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6501.F08 FROM S65.T6501 WHERE T6501.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    @Override
    public void updtateT6501F10(int[] orderIds, String type)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            
            for (int orderId : orderIds)
            {
                String F10 = null;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT T6501.F10 FROM S65.T6501 WHERE T6501.F01 = ? LIMIT 1"))
                {
                    pstmt.setInt(1, orderId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            F10 = resultSet.getString(1);
                        }
                    }
                }
                if (StringHelper.isEmpty(F10))
                {
                    String mchnt_txn_ssn = MchntTxnSsn.getMts(type);
                    updateT6501DF(connection, orderId, mchnt_txn_ssn);
                }
            }
        }
    }
}
