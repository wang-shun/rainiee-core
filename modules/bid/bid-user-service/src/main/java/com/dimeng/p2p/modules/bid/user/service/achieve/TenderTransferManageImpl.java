package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6147_F04;
import com.dimeng.p2p.S61.enums.T6148_F02;
import com.dimeng.p2p.S62.entities.T6260;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6507;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.common.RiskLevelCompareUtil;
import com.dimeng.p2p.modules.bid.user.service.TenderTransferManage;
import com.dimeng.p2p.variables.defines.RegulatoryPolicyVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.DateHelper;
import com.dimeng.util.parser.BooleanParser;

public class TenderTransferManageImpl extends AbstractBidManage implements TenderTransferManage
{
    
    public TenderTransferManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int purchase(int transferId)
        throws Throwable
    {
        if (transferId <= 0)
        {
            throw new LogicalException("线上债权转让申请不存在");
        }
        int accountId = serviceResource.getSession().getAccountId();
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                /*T6110_F06 F06 = selectT6110F06(connection, accountId);
                if (F06 == null || T6110_F06.ZRR != F06)
                {
                    throw new LogicalException("企业机构不能购买债权");
                }*/
                // 锁定转让申请
                T6260 t6260 = selectT6260(connection, transferId);
                if (t6260 == null)
                {
                    throw new LogicalException("线上债权转让申请不存在");
                }
                T6110 t6110 = selectT6110(connection, accountId);
                if (T6110_F17.F == t6110.F17 && T6110_F06.FZRR == t6110.F06)
                {
                    throw new LogicalException("您的账号不能购买债权");
                }
                // 查询是否有逾期未还
                int count = selectYqInfo(connection, accountId);
                if (count > 0)
                {
                    throw new LogicalException("您有逾期未还的借款，请先还完再操作。");
                }
                if (t6260.F07 != T6260_F07.ZRZ)
                {
                    throw new ParameterException("线上债权不是转让中状态，不能操作。");
                }
                if (!DateHelper.beforeOrMatchDate(getCurrentDate(connection), t6260.F06))
                {
                    throw new LogicalException("线上债权转让申请已到截至日期,不能转让。");
                }
                
                int bidId = 0;
                int zqrId = 0;
                try (PreparedStatement ps = connection.prepareStatement("SELECT F03,F04 FROM S62.T6251 WHERE F01=?"))
                {
                    ps.setInt(1, t6260.F02);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            bidId = rs.getInt(1);
                            zqrId = rs.getInt(2);
                        }
                    }
                }
                T6110_F06 F06 = selectT6110F06(connection, accountId);
                /*2016-03-31 增加判断用户等级与债权风险等级是否匹配的判断*/
                //是否开启风险承受能力评估
                boolean isOpenRiskAccess =
                    Boolean.parseBoolean(serviceResource.getResource(ConfigureProvider.class)
                        .getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
                boolean isInvestLimit =
                    Boolean.parseBoolean(serviceResource.getResource(ConfigureProvider.class)
                        .getProperty(RegulatoryPolicyVariavle.IS_INVEST_LIMIT));
                if (isOpenRiskAccess
                    && isInvestLimit
                    && !RiskLevelCompareUtil.compareRiskLevel(getUserRiskLevel(connection, accountId),
                        getProductRiskLevel(connection, bidId).name()) && T6110_F06.ZRR == F06)
                {
                    throw new LogicalException("您的风险承受等级不可购买该债权。");
                }
                int jkrId = 0;//如果该债权的借款人是自己，则不能购买
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT T6230.F02 FROM S62.T6230 INNER JOIN S62.T6251 ON T6251.F03 = T6230.F01 WHERE T6251.F01 = ?"))
                {
                    ps.setInt(1, t6260.F02);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            jkrId = rs.getInt(1);
                        }
                    }
                }
                if (accountId == jkrId)
                {
                    throw new LogicalException("不能购买自己的借款项目的债权。");
                }
                //获取标的担保方ID（购买人不能购买自己担保的标）
                int assureId = selectT6236(connection, bidId);
                if (accountId == assureId)
                {
                    throw new LogicalException("不可购买自己担保的标债权");
                }
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                boolean zqzrBol = BooleanParser.parse(configureProvider.getProperty(SystemVariable.ZQZR_SFZJKT));
                if (!zqzrBol && accountId == zqrId)
                {
                    throw new LogicalException("不能购买自己的债权。");
                }
                // 锁定投资人资金账户
                BigDecimal fee = t6260.F03.multiply(t6260.F08);
                //BigDecimal total = t6260.F03.add(fee);
                BigDecimal total = t6260.F03;
                T6101 investor = selectT6101(connection, accountId, T6101_F03.WLZH, false);
                if (investor == null)
                {
                    throw new LogicalException("用户往来账户不存在。");
                }
                if (investor.F06.compareTo(total) < 0)
                {
                    throw new LogicalException("可用余额不足。");
                }
                T6501 t6501 = new T6501();
                t6501.F02 = OrderType.BID_EXCHANGE.orderType();
                t6501.F03 = T6501_F03.DTJ;
                t6501.F04 = getCurrentTimestamp(connection);
                t6501.F07 = T6501_F07.YH;
                t6501.F08 = accountId;
                t6501.F13 = total;
                int orderId = insertT6501(connection, t6501);
                T6507 t6507 = new T6507();
                t6507.F01 = orderId;
                t6507.F02 = transferId;
                t6507.F03 = accountId;
                t6507.F04 = t6260.F04;
                t6507.F05 = t6260.F03;
                t6507.F06 = fee;
                insertT6507(connection, t6507);
                
                serviceResource.commit(connection);
                return orderId;
            }
            catch (RuntimeException e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * 查询担保方ID
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    private int selectT6236(Connection connection, int F02)
        throws SQLException
    {
        int F03 = 0;
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F03 FROM S62.T6236 WHERE T6236.F02 = ?"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    F03 = resultSet.getInt(1);
                }
            }
        }
        return F03;
    }
    
    /**
     * 查询产品风险等级
     * <功能详细描述>
     * @param connection
     * @param bidId
     * @return
     * @throws Throwable
     */
    private T6148_F02 getProductRiskLevel(Connection connection, int bidId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6216.F18 FROM S62.T6230 INNER JOIN S62.T6216  ON T6230.F32=T6216.F01 WHERE T6230.F01=? LIMIT 1"))
        {
            pstmt.setInt(1, bidId);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    return T6148_F02.parse(rs.getString(1));
                }
            }
        }
        return T6148_F02.BSX;
    }
    
    /**
     * 查询用户的风险等级
     * <功能详细描述>
     * @param connection
     * @param id
     * @return
     * @throws Throwable
     */
    private T6147_F04 getUserRiskLevel(Connection connection, int id)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F04 FROM S61.T6147 WHERE T6147.F02 = ? ORDER BY F06 DESC LIMIT 1"))
        {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    return T6147_F04.parse(rs.getString(1));
                }
            }
        }
        return null;
    }
    
    /*protected int insertT6501(Connection connection, T6501 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setString(2, entity.F03.name());
            pstmt.setTimestamp(3, entity.F04);
            pstmt.setTimestamp(4, entity.F05);
            pstmt.setTimestamp(5, entity.F06);
            pstmt.setString(6, entity.F07.name());
            pstmt.setInt(7, entity.F08);
            pstmt.setInt(8, entity.F09);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }*/
    
    private T6260 selectT6260(Connection connection, int F01)
        throws SQLException
    {
        T6260 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S62.T6260 WHERE F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6260();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = resultSet.getDate(6);
                    record.F07 = T6260_F07.parse(resultSet.getString(7));
                    record.F08 = resultSet.getBigDecimal(8);
                }
            }
        }
        return record;
    }
    
    protected void insertT6507(Connection connection, T6507 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6507 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setInt(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setBigDecimal(6, entity.F06);
            pstmt.execute();
        }
    }
    
    @Override
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
    
    private int selectYqInfo(Connection connection, int F03)
        throws SQLException
    {
        int count = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(*) FROM S62.T6252 WHERE F03 = ? AND F08 < CURDATE() AND F09 = ? "))
        {
            pstmt.setInt(1, F03);
            pstmt.setString(2, T6252_F09.WH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    count = resultSet.getInt(1);
                }
            }
        }
        return count;
    }
    
    @Override
    public void checkPurchase(int transferId)
        throws Throwable
    {
        if (transferId <= 0)
        {
            throw new LogicalException("线上债权转让申请不存在");
        }
        int accountId = serviceResource.getSession().getAccountId();
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                // 锁定转让申请
                T6260 t6260 = selectT6260(connection, transferId);
                if (t6260 == null)
                {
                    throw new LogicalException("线上债权转让申请不存在。");
                }
                // 查询是否有逾期未还
                int count = selectYqInfo(connection, accountId);
                if (count > 0)
                {
                    throw new LogicalException("您有逾期未还的借款，请先还完再操作。");
                }
                if (t6260.F07 != T6260_F07.ZRZ)
                {
                    throw new LogicalException("线上债权转让申请不是转让中状态,不能转让。");
                }
                if (!DateHelper.beforeOrMatchDate(getCurrentDate(connection), t6260.F06))
                {
                    throw new LogicalException("线上债权转让申请已到截至日期,不能转让。");
                }
                int zqrId = 0;
                try (PreparedStatement ps = connection.prepareStatement("SELECT F04 FROM S62.T6251 WHERE F01=?"))
                {
                    ps.setInt(1, t6260.F02);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            zqrId = rs.getInt(1);
                        }
                    }
                }
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                boolean zqzrBol = BooleanParser.parse(configureProvider.getProperty(SystemVariable.ZQZR_SFZJKT));
                if (!zqzrBol && accountId == zqrId)
                {
                    throw new LogicalException("不能购买自己的债权。");
                }
                // 锁定投资人资金账户
                //BigDecimal fee = t6260.F03.multiply(t6260.F08);
                //BigDecimal total = t6260.F03.add(fee);
                BigDecimal total = t6260.F03;
                T6101 investor = selectT6101(connection, accountId, T6101_F03.WLZH, false);
                if (investor == null)
                {
                    throw new LogicalException("用户往来账户不存在。");
                }
                if (investor.F06.compareTo(total) < 0)
                {
                    throw new LogicalException("可用余额不足。");
                }
                
                serviceResource.commit(connection);
            }
            catch (RuntimeException e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private T6110_F06 selectT6110F06(Connection connection, int F01)
        throws SQLException
    {
        T6110_F06 F06 = null;
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F06 FROM S61.T6110 WHERE T6110.F01 = ?"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    F06 = T6110_F06.parse(resultSet.getString(1));
                }
            }
        }
        return F06;
    }

	@Override
	public T6260 getT6260(int zqzrId) throws Throwable {
        try (final Connection connection = getConnection())
        {
            return this.selectT6260(connection, zqzrId);
        }
    }
}
