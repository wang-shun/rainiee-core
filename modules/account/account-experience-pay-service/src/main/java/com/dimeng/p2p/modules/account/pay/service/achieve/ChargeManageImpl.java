package com.dimeng.p2p.modules.account.pay.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6118_F05;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.common.enums.ChargeStatus;
import com.dimeng.p2p.modules.account.pay.service.ChargeManage;
import com.dimeng.p2p.modules.account.pay.service.entity.AllinpayCheckOrder;
import com.dimeng.p2p.modules.account.pay.service.entity.Auth;
import com.dimeng.p2p.modules.account.pay.service.entity.ChargeOrder;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.EnumParser;

public class ChargeManageImpl extends AbstractPayService implements ChargeManage
{
    
    public ChargeManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    
    /**
     * 插入站内信内容
     * 
     * @param connection
     * @param F01
     * @param F02
     * @throws SQLException
     */
    protected void insertT6124(Connection connection, int F01, String F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6124 SET F01 = ?, F02 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setString(2, F02);
            pstmt.execute();
        }
    }
    
    /**
     * 插入站内信
     * 
     * @param connection
     * @param F02
     * @param F03
     * @param F04
     * @param F05
     * @return
     * @throws SQLException
     */
    protected int insertT6123(Connection connection, int F02, String F03, Timestamp F04, T6123_F05 F05)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03);
            pstmt.setTimestamp(3, F04);
            pstmt.setString(4, F05.name());
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
    }
    
    /**
     * 更新充值订单流水单号
     * 
     * @param connection
     * @param F01
     * @param F02
     * @throws SQLException
     */
    protected void updateT6502(Connection connection, String F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6502 SET F08 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01);
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    /**
     * 更新订单表
     * 
     * @param connection
     * @param F01
     * @param F02
     * @param F03
     * @throws SQLException
     */
    protected void updateT6501(Connection connection, T6501_F03 F01, Timestamp F02, int F03)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F06 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01.name());
            pstmt.setTimestamp(2, F02);
            pstmt.setInt(3, F03);
            pstmt.execute();
        }
    }
    
    /**
     * 更新用户往来账户
     * 
     * @param connection
     * @param F01
     * @param F02
     * @param F03
     * @throws SQLException
     */
    protected void updateT6101(Connection connection, BigDecimal F01, int F02, T6101_F03 F03)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = ?  WHERE F02 = ? AND F03 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.setString(4, F03.name());
            pstmt.execute();
        }
    }
    
    
    /**
     * 查询充值次数
     * 
     * @param connection
     * @param F03
     * @param F02
     * @return
     * @throws SQLException
     */
    protected int selectChargeCount(Connection connection, T6501_F03 F03, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(T6501.F01) FROM S65.T6501 INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 WHERE T6501.F03 = ? AND T6502.F02 = ?"))
        {
            pstmt.setString(1, F03.name());
            pstmt.setInt(2, F02);
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
    public AllinpayCheckOrder getAllinpayCheckOrder(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F01,F03,F05,F07 FROM T6033 WHERE F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        AllinpayCheckOrder checkOrder = new AllinpayCheckOrder();
                        checkOrder.id = resultSet.getInt(1);
                        checkOrder.createTime = resultSet.getTimestamp(2);
                        checkOrder.chargeStatus = ChargeStatus.valueOf(resultSet.getString(3));
                        checkOrder.orderNo = resultSet.getString(4);
                        return checkOrder;
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public ChargeOrder addOrder(BigDecimal amount, int payCompanyCode)
        throws Throwable
    {
        if (serviceResource.getSession() == null || !serviceResource.getSession().isAuthenticated())
        {
            throw new ParameterException("鉴权失败");
        }
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        String mPhone = configureProvider.getProperty(PayVariavle.CHARGE_MUST_PHONE);
        String mRealName = configureProvider.getProperty(PayVariavle.CHARGE_MUST_NCIIC);
        String mWithPsd = configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
        Auth auth = getAutnInfo();
        if ("true".equals(mPhone) && !auth.phone)
        {
            throw new LogicalException("手机号未认证");
        }
        if ("true".equals(mRealName) && !auth.realName)
        {
            throw new LogicalException("未实名认证");
        }
        if ("true".equals(mWithPsd) && !auth.withdrawPsw)
        {
            throw new LogicalException("交易密码未设置");
        }
        if (amount.compareTo(new BigDecimal(0)) <= 0 || payCompanyCode <= 0)
        {
            throw new ParameterException("金额或支付类型错误");
        }
        
        String min = configureProvider.getProperty(PayVariavle.CHARGE_MIN_AMOUNT);
        String max = configureProvider.getProperty(PayVariavle.CHARGE_MAX_AMOUNT);
        if (amount.compareTo(new BigDecimal(min)) < 0 || amount.compareTo(new BigDecimal(max)) > 0)
        {
            StringBuilder builder = new StringBuilder("充值金额必须大于");
            builder.append(min);
            builder.append("元小于");
            builder.append(max);
            builder.append("元");
            throw new LogicalException(builder.toString());
        }
        String rate = configureProvider.getProperty(PayVariavle.CHARGE_RATE);
        if (StringHelper.isEmpty(rate))
        {
            return null;
        }
        BigDecimal ysPondage = amount.multiply(new BigDecimal(rate)); // 应收手续费
        BigDecimal maxPondage = BigDecimalParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MAX_POUNDAGE)); // 最大手续费限额
        BigDecimal ssPoundage = ysPondage.compareTo(maxPondage) >= 0 ? maxPondage : ysPondage; // 实收手续费
        
        ChargeOrder order = null;
        int accountId = serviceResource.getSession().getAccountId();
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.CHARGE.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F07 = T6501_F07.YH;
        t6501.F08 = accountId;
        t6501.F13 = amount;
        int oId = 0;
        try (Connection connection = getConnection("S65"))
        {
            t6501.F04 = getCurrentTimestamp(connection);
            oId = insertT6501(connection, t6501);
            if (oId <= 0)
            {
                throw new LogicalException("数据库异常");
            }
            T6502 t6502 = new T6502();
            t6502.F01 = oId;
            t6502.F02 = accountId;
            t6502.F03 = amount;
            t6502.F04 = ysPondage;
            t6502.F05 = ssPoundage;
            t6502.F07 = payCompanyCode;
            insertT6502(connection, t6502);
            order = new ChargeOrder();
            order.id = oId;
            order.amount = amount;
            order.orderTime = t6501.F04;
            order.payCompanyCode = payCompanyCode;
        }
        return order;
    }
    
    protected void updateT6501(Connection connection, T6501_F03 F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6501 SET F03 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01.name());
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    /*protected int insertT6501(Connection connection, T6501 entity) throws SQLException {
    	try (PreparedStatement pstmt = connection.prepareStatement(
    			"INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
    			PreparedStatement.RETURN_GENERATED_KEYS)) {
    		pstmt.setInt(1, entity.F02);
    		pstmt.setString(2, entity.F03.name());
    		pstmt.setTimestamp(3, entity.F04);
    		pstmt.setTimestamp(4, entity.F05);
    		pstmt.setTimestamp(5, entity.F06);
    		pstmt.setString(6, entity.F07.name());
    		pstmt.setInt(7, entity.F08);
    		pstmt.setInt(8, entity.F09);
    		pstmt.execute();
    		try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
    			if (resultSet.next()) {
    				return resultSet.getInt(1);
    			}
    			return 0;
    		}
    	}
    }*/
    
    protected void insertT6502(Connection connection, T6502 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6502 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setBigDecimal(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setString(6, entity.F06);
            pstmt.setInt(7, entity.F07);
            pstmt.execute();
        }
    }
    
    @Override
    public Auth getAutnInfo()
        throws Throwable
    {
        try (Connection connection = getConnection("S61"))
        {
            Auth auth = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F05 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        auth = new Auth();
                        auth.realName = T6118_F02.TG.name().equals(resultSet.getString(1));
                        auth.phone = T6118_F03.TG.name().equals(resultSet.getString(2));
                        auth.withdrawPsw = T6118_F05.YSZ.name().equals(resultSet.getString(3));
                    }
                }
            }
            return auth;
        }
    }
    
    @Override
    public ChargeOrder getOrder(int orderId)
        throws Throwable
    {
        if (orderId <= 0)
        {
            return null;
        }
        ChargeOrder order = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6501.F01 AS F01, T6501.F04 AS F02, T6502.F03 AS F03, T6502.F07 AS F04, T6501.F03 AS F05 FROM S65.T6501 INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 WHERE T6501.F01 = ? AND T6502.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, orderId);
                pstmt.setInt(2, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        order = new ChargeOrder();
                        order.id = resultSet.getInt(1);
                        order.orderTime = resultSet.getTimestamp(2);
                        order.amount = resultSet.getBigDecimal(3);
                        order.payCompanyCode = resultSet.getInt(4);
                        order.status = EnumParser.parse(T6501_F03.class, resultSet.getString(5));
                    }
                }
            }
        }
        return order;
    }
    
    @Override
    public void updateOrder(int orderId)
        throws Throwable
    {
        if (orderId <= 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6501 SET F03=? WHERE F01=?"))
            {
                pstmt.setString(1, T6501_F03.DQR.name());
                pstmt.setInt(2, orderId);
                pstmt.executeUpdate();
            }
        }
    }
    
    @Override
    public ChargeOrder getChargeOrder(int orderId)
        throws Throwable
    {
        if (orderId <= 0)
        {
            return null;
        }
        ChargeOrder order = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6501.F01 AS F01, T6501.F04 AS F02, T6502.F03 AS F03, T6502.F07 AS F04, T6501.F03 AS F05 FROM S65.T6501 INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 WHERE T6501.F01 = ? AND T6501.F08 =? LIMIT 1"))
            {
                pstmt.setInt(1, orderId);
                pstmt.setInt(2, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        order = new ChargeOrder();
                        order.id = resultSet.getInt(1);
                        order.orderTime = resultSet.getTimestamp(2);
                        order.amount = resultSet.getBigDecimal(3);
                        order.payCompanyCode = resultSet.getInt(4);
                        order.status = EnumParser.parse(T6501_F03.class, resultSet.getString(5));
                    }
                }
            }
        }
        return order;
    }
    
    @Override
    public ChargeOrder getLockChargeOrder(int orderId)
        throws Throwable
    {
        if (orderId <= 0)
        {
            return null;
        }
        ChargeOrder order = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM S65.T6501 WHERE T6501.F01 = ?"))
            {
                pstmt.setInt(1, orderId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        order = new ChargeOrder();
                        order.id = resultSet.getInt(1);
                        order.orderTime = resultSet.getTimestamp(4);
                        order.status = EnumParser.parse(T6501_F03.class, resultSet.getString(3));
                        try (PreparedStatement pstmt2 =
                            connection.prepareStatement("SELECT * FROM S65.T6502 WHERE T6502.F01 = ?"))
                        {
                            pstmt2.setInt(1, orderId);
                            try (ResultSet resultSet2 = pstmt2.executeQuery())
                            {
                                if (resultSet2.next())
                                {
                                    order.amount = resultSet2.getBigDecimal(3);
                                    order.payCompanyCode = resultSet2.getInt(7);
                                }
                            }
                        }
                    }
                }
            }
        }
        return order;
    }
}
