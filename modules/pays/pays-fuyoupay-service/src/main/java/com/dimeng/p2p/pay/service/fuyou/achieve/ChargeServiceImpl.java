package com.dimeng.p2p.pay.service.fuyou.achieve;

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
import com.dimeng.p2p.S50.entities.T5020;
import com.dimeng.p2p.S50.enums.T5020_F03;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F18;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.S61.enums.T6114_F10;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6118_F05;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.pay.service.fuyou.ChargeService;
import com.dimeng.p2p.pay.service.fuyou.entity.Auth;
import com.dimeng.p2p.pay.service.fuyou.entity.ChargeOrder;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.EnumParser;

public class ChargeServiceImpl extends AbstractPayService implements ChargeService
{
    
    public ChargeServiceImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public ChargeOrder addOrder(BigDecimal amount, int payCompanyCode, String bankNum, String payWay, int bankId,
        String city,String source)
            throws Throwable
    {
        if (serviceResource.getSession() == null || !serviceResource.getSession().isAuthenticated())
        {
            throw new ParameterException("鉴权失败,用户未登录");
        }
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        String mPhone = configureProvider.getProperty(PayVariavle.CHARGE_MUST_PHONE);
        String mRealName = configureProvider.getProperty(PayVariavle.CHARGE_MUST_NCIIC);
        String mWithPsd = configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
        Auth auth = getAutnInfo();
        if ("S".equals(mPhone) && !auth.phone)
        {
            throw new LogicalException("手机号未认证");
        }
        if ("S".equals(mRealName) && !auth.realName)
        {
            throw new LogicalException("未实名认证");
        }
        if ("S".equals(mWithPsd) && !auth.withdrawPsw)
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
        t6501.F04 = new Timestamp(System.currentTimeMillis());
        t6501.F07 = T6501_F07.YH;
        t6501.F08 = accountId;
        t6501.F09 = 0;
        t6501.F13 = amount;
        int oId = 0;
        try (Connection connection = getConnection())
        {
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
            t6502.F06 = bankNum;
            t6502.F07 = payCompanyCode;
            t6502.F11 = payWay;
            t6502.chargeSource = source;
            insertT6502(connection, t6502);
            order = new ChargeOrder();
            order.id = oId;
            order.amount = amount;
            order.orderTime = t6501.F04;
            order.payCompanyCode = payCompanyCode;
            
            if (payWay.equals("QUICK") && null == selectT6114(connection, accountId, bankNum))
            {
                T6110 t6110 = this.selectT6110(accountId);
                T5020 t5020 = selectT5020(bankId);
                execute(connection, "DELETE FROM S61.T6114 WHERE F02 = ? ", accountId);
                StringBuilder sb = new StringBuilder();
                sb.append(bankNum.substring(0, 3));
                sb.append("*************");
                sb.append(bankNum.substring(bankNum.length() - 4, bankNum.length()));
                insert(connection,
                    "INSERT INTO S61.T6114 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ? ",
                    serviceResource.getSession().getAccountId(),
                    bankId,
                    city,
                    t5020.F02,
                    sb.toString(),
                    StringHelper.encode(bankNum),
                    T6114_F08.TY.name(),
                    getCurrentTimestamp(connection),
                    T6114_F10.BTG.name(),
                    t6110.F02,
                    t6110.F06 == T6110_F06.ZRR ? 1 : 2);
            }
        }
        return order;
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
            try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT T6501.F01 AS F01, T6501.F04 AS F02, T6502.F03 AS F03, T6502.F07 AS F04, T6501.F03 AS F05 FROM S65.T6501 INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 WHERE T6501.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, orderId);
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
    
    private int insertT6501(Connection connection, T6501 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement(
            "INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F13 = ?",
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
            pstmt.setBigDecimal(9, entity.F13);
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
    
    private void insertT6502(Connection connection, T6502 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement(
            "INSERT INTO S65.T6502 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F09 = ?, F11 = ?, chargeSource = ?"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setBigDecimal(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setString(6, entity.F06);
            pstmt.setInt(7, entity.F07);
            pstmt.setString(8, entity.F09);
            pstmt.setString(9, entity.F11);
            pstmt.setString(10, entity.chargeSource);
            pstmt.execute();
        }
    }
    
    private Auth getAutnInfo()
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
    public T5020 selectT5020(int bankId)
        throws Throwable
    {
        T5020 t5020 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F03,F04 FROM S50.T5020 WHERE F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, bankId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t5020 = new T5020();
                        t5020.F01 = resultSet.getInt(1);
                        t5020.F02 = resultSet.getString(2);
                        t5020.F03 = T5020_F03.parse(resultSet.getString(3));
                        t5020.F04 = resultSet.getString(4);
                    }
                }
            }
        }
        return t5020;
    }
    
    @Override
    public T6141 selectT6141(int userId, boolean isSession)
        throws Throwable
    {
        userId = isSession ? this.serviceResource.getSession().getAccountId() : userId;
        T6141 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S61.T6141 WHERE T6141.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6141();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = T6141_F03.parse(resultSet.getString(3));
                        record.F04 = T6141_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = StringHelper.decode(resultSet.getString(7));
                        record.F08 = resultSet.getDate(8);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public void updateT6501(int orderId, T6501_F03 F03)
        throws Throwable
    {
        try (Connection connection = this.getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F06 = SYSDATE() WHERE F01 = ?"))
            {
                pstmt.setString(1, F03.name());
                pstmt.setInt(2, orderId);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public T6502 selectT6502(int F01)
        throws Throwable
    {
        T6502 record = null;
        try (Connection connection = this.getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F11,chargeSource FROM S65.T6502 WHERE T6502.F01 = ?"))
            {
                pstmt.setInt(1, F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6502();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getInt(7);
                        record.F08 = resultSet.getString(8);
                        record.F09 = resultSet.getString(9);
                        record.F11 = resultSet.getString(10);
                        record.chargeSource = resultSet.getString("chargeSource");
                    }
                }
            }
        }
        return record;
    }
    
    public T6114 selectT6114(Connection connection, int userId, String bankNum)
        throws Throwable
    {
        T6114 record = null;
        try (PreparedStatement pstmt = connection.prepareStatement(
            "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F11 FROM S61.T6114 WHERE T6114.F02 = ? AND F07 = ? AND F08='QY'"))
        {
            pstmt.setInt(1, userId);
            pstmt.setString(2, StringHelper.encode(bankNum));
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6114();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = StringHelper.decode(resultSet.getString(7));
                    record.F08 = T6114_F08.parse(resultSet.getString(8));
                }
            }
        }
        return record;
    }
    
    protected T6110 selectT6110(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6110 record = null;
            try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F15, F18, F19 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6110();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = T6110_F06.parse(resultSet.getString(6));
                        record.F07 = T6110_F07.parse(resultSet.getString(7));
                        record.F08 = T6110_F08.parse(resultSet.getString(8));
                        record.F09 = resultSet.getTimestamp(9);
                        record.F10 = T6110_F10.parse(resultSet.getString(10));
                        record.F15 = resultSet.getTimestamp(11);
                        record.F18 = T6110_F18.parse(resultSet.getString(12));
                        record.F19 = T6110_F19.parse(resultSet.getString(13));
                    }
                }
            }
            return record;
        }
    }
    
    protected Timestamp getCurrentTimestamp(Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_TIMESTAMP()"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getTimestamp(1);
                }
            }
        }
        return null;
    }
    
    @Override
    public void updateT6114(String bankNum)
        throws Throwable
    {
        try (Connection connection = this.getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6114 SET F08 = ?, F10 = ? WHERE F07 = ?"))
            {
                pstmt.setString(1, T6114_F08.QY.name());
                pstmt.setString(2, T6114_F10.TG.name());
                pstmt.setString(3, StringHelper.encode(bankNum));
                pstmt.execute();
            }
        }
    }
    
    @Override
    public boolean isBindleBanked()
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(
                "SELECT T6114.F01 FROM S61.T6114 INNER JOIN S50.T5020 ON T6114.F03 = T5020.F01 WHERE T6114.F02 = ? AND T6114.F08 = ? LIMIT 1"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                ps.setString(2, T6114_F08.QY.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return true;
                    }
                }
            }
            
            return false;
        }
    }
    
}
