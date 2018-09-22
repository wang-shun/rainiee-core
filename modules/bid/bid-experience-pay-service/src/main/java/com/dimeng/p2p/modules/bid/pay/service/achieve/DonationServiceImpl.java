package com.dimeng.p2p.modules.bid.pay.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S62.enums.T6242_F10;
import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.bid.pay.service.DonationService;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.parser.BooleanParser;

/**
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  公益捐款
 * 修 改 人:  wangming
 * 修改时间:  15-3-10
 */
public class DonationServiceImpl extends AbstractBidService implements DonationService
{
    protected static final int DECIMAL_SCALE = 9;
    
    public DonationServiceImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public T6242 selectT6242(Connection connection, int loadId)
        throws Throwable
    {
        T6242 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6242.F01, T6242.F02, T6242.F03, T6242.F04, T6242.F05, T6242.F06, T6242.F07, T6242.F08, T6242.F09, T6242.F10, T6242.F11, T6242.F12, T6242.F13, T6242.F14, T6242.F15, "
                + "T6242.F16, T6242.F17, T6242.F18, T6242.F19, T6242.F20, T6242.F21, T6242.F22, T6242.F23, T6242.F24"
                + "  FROM S62.T6242  WHERE T6242.F01 = ? FOR UPDATE "))
        {
            pstmt.setInt(1, loadId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6242();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = T6242_F10.parse(resultSet.getString(10));
                    record.F11 = T6242_F11.parse(resultSet.getString(11));
                    record.F12 = resultSet.getString(12);
                    record.F13 = resultSet.getTimestamp(13);
                    record.F14 = resultSet.getInt(14);
                    record.F15 = resultSet.getTimestamp(15);
                    record.F16 = resultSet.getTimestamp(16);
                    record.F17 = resultSet.getTimestamp(17);
                    record.F18 = resultSet.getTimestamp(18);
                    record.F19 = resultSet.getTimestamp(19);
                    record.F20 = resultSet.getTimestamp(20);
                    record.F21 = resultSet.getString(21);
                    record.F22 = resultSet.getString(22);
                    record.F23 = resultSet.getInt(23);
                    record.F24 = resultSet.getString(24);
                    
                }
            }
        }
        return record;
    }
    
    @Override
    public T6242 selectT6242(int loadId)
        throws Throwable
    {
        T6242 record = null;
        try (Connection connection = this.getConnection())
        {
            record = this.selectT6242(connection, loadId);
        }
        return record;
    }
    
    private String selectTranPwd(Connection connection, int UserId)
        throws SQLException
    {
        String tranPwd = "";
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT  F08 FROM  S61.T6118 WHERE T6118.F01 = ? "))
        {
            pstmt.setInt(1, UserId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    tranPwd = resultSet.getString(1);
                }
            }
        }
        return tranPwd;
    }
    
    /**
     * 生成捐款订单
     * @param bidId
     * @param amount
     * @return
     * @throws Throwable
     */
    @Override
    public int bid(final int bidId, final BigDecimal amount, String tranPwd)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean isOpenWsd =
            BooleanParser.parseObject(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        
        if (isOpenWsd)
        { //网关投资需要验证交易密码
            if (StringUtils.isEmpty(tranPwd))
            {
                throw new LogicalException("输入正确的交易密码。");
            }
            final int accountId = serviceResource.getSession().getAccountId();
            try (Connection connection = getConnection())
            {
                String tran_pwd = selectTranPwd(connection, accountId);
                if (!tranPwd.equals(tran_pwd))
                {
                    throw new LogicalException("输入正确的交易密码。");
                }
            }
        }
        return bid(bidId, amount);
    }
    
    @Override
    public int bid(int bidId, BigDecimal amount)
        throws Throwable
    {
        if (bidId <= 0)
        {
            throw new ParameterException("没有指定要捐款的标。");
        }
        if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0)
        {
            throw new ParameterException("捐款金额必须大于零。");
        }
        final int accountId = serviceResource.getSession().getAccountId();
        boolean zjb =
            BooleanParser.parse(serviceResource.getResource(ConfigureProvider.class)
                .getProperty(SystemVariable.BID_SFZJKT));
        int ordId = 0;
        /*BigDecimal _a = amount.stripTrailingZeros();
        if (_a.toPlainString().contains("."))
        {
            throw new LogicalException("捐款金额必须为整数。");
        }*/
        amount.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                
                // 查询是否有逾期未还
                int count = selectYqInfo(connection, accountId);
                if (count > 0)
                {
                    throw new LogicalException("您有逾期未还的借款，请先还完再操作。");
                }
                
                // 锁定标
                T6242 t6242 = selectT6242(connection, bidId);
                if (t6242 == null)
                {
                    throw new LogicalException("指定的捐款标记录不存在。");
                }
                if (!zjb && accountId == t6242.F02)
                {
                    throw new LogicalException("您是该公益标的发起者，不能捐款。");
                }
                if (t6242.F11 != T6242_F11.JKZ)
                {
                    throw new LogicalException("指定的捐款标不是捐款中状态,不能捐款。");
                }
                if (amount.compareTo(t6242.F07) > 0)
                {
                    throw new LogicalException("捐款金额大于可捐金额。");
                }
                // 校验最低起捐金额
                BigDecimal min = t6242.F06;
                if (amount.compareTo(min) < 0)
                {
                    throw new LogicalException("捐款金额不能低于最低起捐金额。");
                }
                t6242.F07 = t6242.F07.subtract(amount);
                if (t6242.F07.compareTo(new BigDecimal(0)) > 0 && t6242.F07.compareTo(min) < 0)
                {
                    throw new LogicalException("剩余可捐金额不能低于最低起捐金额。");
                }
                // 锁定投资人资金账户
                T6101 investor = selectT6101(connection, accountId, T6101_F03.WLZH, false);
                if (investor == null)
                {
                    throw new LogicalException("用户往来账户不存在。");
                }
                if (investor.F06.compareTo(amount) < 0)
                {
                    throw new LogicalException("账户余额不足。");
                }
                // 插入投资订单
                //ordId = insertT6501(connection, OrderType.GYBTRANSFER.orderType(), T6501_F03.DTJ, T6501_F07.YH, accountId);
                
                T6501 t6501 = new T6501();
                t6501.F02 = OrderType.GYBTRANSFER.orderType();
                t6501.F03 = T6501_F03.DTJ;
                t6501.F04 = getCurrentTimestamp(connection);
                t6501.F07 = T6501_F07.YH;
                t6501.F08 = accountId;
                t6501.F13 = amount;
                ordId = insertT6501(connection, t6501);
                
                insertT6554(connection, ordId, accountId, bidId, amount);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        return ordId;
    }
    
    @Override
    public T6110 selectT6110()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6110 t6110 = new T6110();
            final int accountId = serviceResource.getSession().getAccountId();
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6110.F01 = resultSet.getInt(1);
                        t6110.F02 = resultSet.getString(2);
                        t6110.F03 = resultSet.getString(3);
                    }
                }
            }
            return t6110;
        }
    }
    
    @Override
    public T6501 selectT6501()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6501 t6501 = new T6501();
            final int accountId = serviceResource.getSession().getAccountId();
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S65.T6554 where F02 = ? ORDER BY F01 DESC LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6501.F01 = resultSet.getInt(1);
                    }
                }
            }
            return t6501;
        }
    }
    
    /*private int insertT6501(Connection connection, int F02, T6501_F03 F03, T6501_F07 F07, int F08)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = CURRENT_TIMESTAMP(), F07 = ?, F08 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03.name());
            pstmt.setString(3, F07.name());
            pstmt.setInt(4, F08);
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
    
    private void insertT6554(Connection connection, int F01, int F02, int F03, BigDecimal F04)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6554 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F03);
            pstmt.setBigDecimal(4, F04);
            pstmt.execute();
        }
    }
    
    private int selectYqInfo(Connection connection, int F03)
        throws SQLException
    {
        int count = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(1) FROM S62.T6252 WHERE F03 = ? AND F08 < CURDATE() AND F09 = ? "))
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
    public void checkBid(int bidId, BigDecimal amount)
        throws Throwable
    {
        if (bidId <= 0)
        {
            throw new ParameterException("没有指定要捐款的标。");
        }
        if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0)
        {
            throw new ParameterException("捐款金额必须大于零。");
        }
        final int accountId = serviceResource.getSession().getAccountId();
        boolean zjb =
            BooleanParser.parse(serviceResource.getResource(ConfigureProvider.class)
                .getProperty(SystemVariable.BID_SFZJKT));
        amount.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                
                // 查询是否有逾期未还
                int count = selectYqInfo(connection, accountId);
                if (count > 0)
                {
                    throw new LogicalException("您有逾期未还的借款，请先还完再操作。");
                }
                
                // 锁定标
                T6242 t6242 = selectT6242(connection, bidId);
                if (t6242 == null)
                {
                    throw new LogicalException("指定的捐款标记录不存在。");
                }
                if (!zjb && accountId == t6242.F02)
                {
                    throw new LogicalException("您是该公益标的发起者，不能捐款。");
                }
                if (t6242.F11 != T6242_F11.JKZ)
                {
                    throw new LogicalException("指定的捐款标不是捐款中状态,不能捐款。");
                }
                if (amount.compareTo(t6242.F07) > 0)
                {
                    throw new LogicalException("捐款金额大于可捐金额。");
                }
                // 校验最低起捐金额
                BigDecimal min = t6242.F06;
                if (amount.compareTo(min) < 0)
                {
                    throw new LogicalException("捐款金额不能低于最低起捐金额。");
                }
                t6242.F07 = t6242.F07.subtract(amount);
                if (t6242.F07.compareTo(new BigDecimal(0)) > 0 && t6242.F07.compareTo(min) < 0)
                {
                    throw new LogicalException("剩余可捐金额不能低于最低起捐金额。");
                }
                // 锁定投资人资金账户
                T6101 investor = selectT6101(connection, accountId, T6101_F03.WLZH, true);
                if (investor == null)
                {
                    throw new LogicalException("用户往来账户不存在。");
                }
                if (investor.F06.compareTo(amount) < 0)
                {
                    throw new LogicalException("账户余额不足。");
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
}
