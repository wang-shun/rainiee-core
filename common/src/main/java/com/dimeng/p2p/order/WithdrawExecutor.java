package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S61.enums.T6130_F16;
import com.dimeng.p2p.S65.entities.T6503;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.parser.DateTimeParser;

@ResourceAnnotation
public class WithdrawExecutor extends AbstractOrderExecutor
{
    
    public WithdrawExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return WithdrawExecutor.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try
        {
            // 查询订单
            T6503 t6503 = selectT6503(connection, orderId);
            if (t6503 == null)
            {
                throw new LogicalException("订单记录不存在");
            }
            int pid = getPTID(connection);
            // 用户锁定账户信息
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            T6101 uT6101 = selectT6101(connection, t6503.F02, T6101_F03.SDZH, true);
            if (uT6101 == null)
            {
                throw new LogicalException("用户账户不存在");
            }
            // 平台往来账户信息
            T6101 cT6101 = selectT6101(connection, pid, T6101_F03.WLZH, true);
            if (cT6101 == null)
            {
                throw new LogicalException("平台往来账户不存在");
            }
            if (uT6101.F06.compareTo(t6503.F03.add(t6503.F05)) < 0)
            {
                throw new LogicalException("用户账户金额不足");
            }
            // 插资金流水
            {
                uT6101.F06 = uT6101.F06.subtract(t6503.F03);
                T6102 t6102 = new T6102();
                t6102.F02 = uT6101.F01;
                t6102.F03 = FeeCode.TX;
                t6102.F04 = uT6101.F01;
                t6102.F07 = t6503.F03;
                t6102.F08 = uT6101.F06;
                t6102.F09 = "提现成功，扣除提现金额";
                insertT6102(connection, t6102);
            }
            if (t6503.F05.compareTo(BigDecimal.ZERO) > 0)
            {
                uT6101.F06 = uT6101.F06.subtract(t6503.F05);
                T6102 t6102 = new T6102();
                t6102.F02 = uT6101.F01;
                t6102.F03 = FeeCode.TX_SXF;
                t6102.F04 = uT6101.F01;
                t6102.F07 = t6503.F05;
                t6102.F08 = uT6101.F06;
                t6102.F09 = "提现成功，扣除提现手续费";
                insertT6102(connection, t6102);
            }
            updateT6101(connection, uT6101.F06, uT6101.F01);
            if (t6503.F04.compareTo(BigDecimal.ZERO) > 0)
            {
                cT6101.F06 = cT6101.F06.add(t6503.F04);
                updateT6101(connection, cT6101.F06, cT6101.F01);
                {
                    T6102 t6102 = new T6102();
                    t6102.F02 = cT6101.F01;
                    t6102.F03 = FeeCode.TX_SXF;
                    t6102.F04 = uT6101.F01;
                    t6102.F06 = t6503.F05;
                    t6102.F08 = cT6101.F06;
                    t6102.F09 = "提现手续费";
                    insertT6102(connection, t6102);
                }
            }
            if (t6503.F09 > 0)
            {
                // 修改提现申请放款状态
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S61.T6130 SET F09 = ? ,F11 = now(),F14 =now(), F16= ? WHERE F01 = ?"))
                {
                    ps.setString(1, T6130_F09.YFK.name());
                    ps.setString(2, T6130_F16.S.name());
                    ps.setInt(3, t6503.F09);
                    ps.execute();
                }
            }
            
            T6110 t6110 = selectT6110(connection, t6503.F02);
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("name", t6110.F02);
            envionment.set("datetime", DateTimeParser.format(new Date()));
            envionment.set("amount", t6503.F03.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            envionment.set("poundage", t6503.F05.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            String content = configureProvider.format(LetterVariable.TX_CG, envionment);
            sendLetter(connection, t6503.F02, "提现成功", content);
            String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
            if ("1".equals(isUseYtx))
            {
                SMSUtils smsUtil = new SMSUtils(configureProvider);
                int type = smsUtil.getTempleId(MsgVariavle.TX_CG.getDescription());
                sendMsg(connection,
                    t6110.F04,
                    smsUtil.getSendContent(envionment.get("datetime"),
                        envionment.get("amount"),
                        configureProvider.getProperty(SystemVariable.SITE_CUSTOMERSERVICE_TEL)),
                    type);
            }
            else
            {
                String msgContent = configureProvider.format(MsgVariavle.TX_CG, envionment);
                sendMsg(connection, t6110.F04, msgContent);
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    protected void updateT6101(Connection connection, BigDecimal F01, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    public void updateT6101(Connection connection, BigDecimal F01, String userName, T6101_F03 F03, int userId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101,S61.T6110 SET T6101.F06 = ?, F07 = ? WHERE T6110.F01 = ? AND T6101.F03 = ? AND T6101.F02 = T6110.F01"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, userId);
            pstmt.setString(4, F03.name());
            pstmt.execute();
        }
    }
    
    protected T6503 selectT6503(Connection connection, int F01)
        throws SQLException
    {
        T6503 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S65.T6503 WHERE T6503.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6503();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getString(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = resultSet.getBigDecimal(10);
                    record.F11 = resultSet.getBigDecimal(11);
                }
            }
        }
        return record;
    }
}