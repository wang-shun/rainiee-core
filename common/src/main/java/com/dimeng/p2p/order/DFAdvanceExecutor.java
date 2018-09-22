package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.S51.enums.T5122_F03;
import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6253;
import com.dimeng.p2p.S62.entities.T6255;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6289_F09;
import com.dimeng.p2p.S65.entities.T6514;
import com.dimeng.p2p.S65.enums.T6514_F07;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 平台垫付
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  beiweiyuan
 * @version  [版本号, 2016年7月11日]
 */
@ResourceAnnotation
public class DFAdvanceExecutor extends AbstractOrderExecutor
{
    
    public DFAdvanceExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return DFAdvanceExecutor.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F07 FROM S65.T6514 WHERE F01=?"))
            {
                ps.setInt(1, orderId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        T6514_F07 t6514_F07 = EnumParser.parse(T6514_F07.class, rs.getString(1));
                        if (t6514_F07 == T6514_F07.S)
                        {
                            throw new LogicalException("该订单已垫付。");
                        }
                    }
                }
            }
            T6514 t6514 = selectT6514(connection, orderId);
            T6251 t6251 = selectT6251(connection, t6514.F03);
            if (t6251 == null)
            {
                throw new LogicalException("债权不存在");
            }
            T6230 t6230 = selectT6230(connection, t6251.F03);
            if (t6230 == null)
            {
                throw new LogicalException("借款标不存在");
            }
            
            //获取平台用户id
            int pid = getPTID(connection);
            //获取平台准备金账户
            T6101 dfrzh = selectT6101(connection, pid, T6101_F03.WLZH, true);
            if (dfrzh == null)
            {
                throw new LogicalException("垫付人往来账户账户不存在");
            }
            T6101 tzrzh = selectT6101(connection, t6251.F04, T6101_F03.WLZH, true);
            if (tzrzh == null)
            {
                throw new LogicalException("投资人往来账户不存在");
            }
            if (dfrzh.F06.compareTo(t6514.F05) < 0)
            {
                throw new LogicalException("平台往来账户余额不足，不能进行垫付！");
            }
            // 插入垫付记录
            {
                T6253 t6253 = selectT6253(connection, t6251.F03);
                int dfId = 0;
                if (t6253 == null)
                {
                    T6253 iT6253 = new T6253();
                    iT6253.F02 = t6251.F03;
                    iT6253.F03 = pid;
                    iT6253.F04 = t6230.F02;
                    iT6253.F05 = t6514.F05;
                    iT6253.F09 = params.get("accountId") == null ? "" : params.get("accountId");
                    iT6253.F10 = selectT5131(connection).name();
                    iT6253.F11 = params.get("period") == null ? 0 : IntegerParser.parse(params.get("period"));
                    dfId = insertT6253(connection, iT6253);
                }
                else
                {
                    updateT6253(connection, t6514.F05, t6253.F01);
                    dfId = t6253.F01;
                }
                
                //插入垫付明细
                T6255 t6255 = new T6255();
                t6255.F02 = dfId;
                t6255.F03 = t6514.F05;
                t6255.F04 = t6251.F04;
                t6255.F05 = t6514.F06;
                t6255.F06 = t6251.F01;
                insertT6255(connection, t6255);
            }
            // 扣除垫付人账户金额，并插流水
            if (dfrzh.F06.compareTo(t6514.F05) < 0)
            {
                throw new LogicalException("垫付人风险备用余额不足");
            }
            {
                dfrzh.F06 = dfrzh.F06.subtract(t6514.F05);
                updateT6101(connection, dfrzh.F06, dfrzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = dfrzh.F01;
                t6102.F03 = t6514.F06;
                t6102.F04 = tzrzh.F01;
                t6102.F07 = t6514.F05;
                t6102.F08 = dfrzh.F06;
                t6102.F09 = String.format("逾期垫付:%s，标题:%s", t6251.F03, t6230.F03);
                t6102.F12 = t6230.F01;
                insertT6102(connection, t6102);
            }
            // 增加投资人账户金额，并插流水
            {
                tzrzh.F06 = tzrzh.F06.add(t6514.F05);
                updateT6101(connection, tzrzh.F06, tzrzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = tzrzh.F01;
                t6102.F03 = t6514.F06;
                t6102.F04 = dfrzh.F01;
                t6102.F06 = t6514.F05;
                t6102.F08 = tzrzh.F06;
                t6102.F09 = String.format("垫付还款:%s，标题:%s", t6251.F03, t6230.F03);
                t6102.F12 = t6230.F01;
                insertT6102(connection, t6102);
            }
            // 更新还款记录收款人为垫付人
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S62.T6252 SET F04 = ? WHERE F09 = ? AND F11 = ? AND F05=?"))
            {
                pstmt.setInt(1, pid);
                pstmt.setString(2, T6252_F09.WH.name());
                pstmt.setInt(3, t6514.F03);
                pstmt.setInt(4, t6514.F06);
                pstmt.execute();
            }
            // 逻辑
            T5131_F02 t5131_f02 = selectT5131(connection);
            if (t5131_f02 == T5131_F02.BX)
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6252 SET F04 = ? WHERE F09 = ? AND F11 = ? AND (F05=? OR F05=?)"))
                {
                    pstmt.setInt(1, pid);
                    pstmt.setString(2, T6252_F09.WH.name());
                    pstmt.setInt(3, t6514.F03);
                    pstmt.setInt(4, FeeCode.TZ_LX);
                    pstmt.setInt(5, FeeCode.TZ_FX);
                    pstmt.execute();
                }
            }
            {
                // 更新垫付状态
                try (PreparedStatement ps = connection.prepareStatement("UPDATE S65.T6514 SET F07=? WHERE F01=?"))
                {
                    ps.setString(1, T6514_F07.S.name());
                    ps.setInt(2, orderId);
                    ps.executeUpdate();
                }
                // 垫付订单数量
                int count = 0;
                try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(F01) FROM S65.T6514 WHERE F02=?"))
                {
                    ps.setInt(1, t6514.F02);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            count = rs.getInt(1);
                        }
                    }
                }
                // 已垫付订单数量
                int num = 0;
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT COUNT(F01) FROM S65.T6514 WHERE F02=? AND F07=?"))
                {
                    ps.setInt(1, t6514.F02);
                    ps.setString(2, T6514_F07.S.name());
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            num = rs.getInt(1);
                        }
                    }
                }
                
                if (num == count)
                {
                    // 本金全额担保
                    if (t5131_f02 == T5131_F02.BJ)
                    {
                        // 免去借款利息
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("UPDATE S62.T6252 SET F04 = ?, F09 = ? WHERE F02 = ? AND F05 = ? AND F09 = ?"))
                        {
                            pstmt.setInt(1, pid);
                            pstmt.setString(2, T6252_F09.DF.name());
                            pstmt.setInt(3, t6514.F02);
                            pstmt.setInt(4, FeeCode.TZ_LX);
                            pstmt.setString(5, T6252_F09.WH.name());
                            pstmt.execute();
                        }
                        // 罚息返还给机构
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("UPDATE S62.T6252 SET F04 = ? WHERE F09 = ? AND F02 = ? AND F05 = ? "))
                        {
                            pstmt.setInt(1, pid);
                            pstmt.setString(2, T6252_F09.WH.name());
                            pstmt.setInt(3, t6514.F02);
                            pstmt.setInt(4, FeeCode.TZ_FX);
                            pstmt.execute();
                        }
                    }
                    // 扣减转出人持有债权
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6251 SET F07 = ? WHERE F03 = ?"))
                    {
                        pstmt.setBigDecimal(1, BigDecimal.ZERO);
                        pstmt.setInt(2, t6251.F03);
                        pstmt.execute();
                    }
                    // 生成新的债权编码
                    int index = 0;
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT COUNT(*) FROM S62.T6251 WHERE F11 = ?"))
                    {
                        pstmt.setInt(1, t6251.F11);
                        try (ResultSet resultSet = pstmt.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                index = resultSet.getInt(1) + 1;
                            }
                            else
                            {
                                index = 1;
                            }
                        }
                    }
                    // 插入受让人债权记录
                    T6251 newT6251 = new T6251();
                    if (t6251.F02.contains("-"))
                    {
                        newT6251.F02 = t6251.F02.substring(0, t6251.F02.length() - 1) + index;
                    }
                    else
                    {
                        newT6251.F02 = String.format("%s-%s", t6251.F02, Integer.toString(index));
                    }
                    newT6251.F03 = t6251.F03;
                    newT6251.F04 = t6514.F04;
                    newT6251.F05 = t6251.F07;
                    newT6251.F06 = t6251.F07;
                    newT6251.F07 = t6251.F07;
                    newT6251.F08 = T6251_F08.F;
                    newT6251.F09 = getCurrentDate(connection);
                    newT6251.F10 = t6251.F10;
                    newT6251.F11 = t6251.F11;
                    newT6251.F12 = orderId;
                    insertT6251(connection, newT6251);
                    //				// 更改债权持有人为垫付人
                    //				try (PreparedStatement pstmt = connection
                    //						.prepareStatement("UPDATE S62.T6251 SET F04 = ? WHERE F01 = ?")) {
                    //					pstmt.setInt(1, t6514.F04);
                    //					pstmt.setInt(2, t6514.F03);
                    //					pstmt.execute();
                    //				}
                    try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6230 SET F20=? WHERE F01=?"))
                    {
                        ps.setString(1, T6230_F20.YDF.name());
                        ps.setInt(2, t6514.F02);
                        ps.executeUpdate();
                    }
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6231 SET F14 = ? WHERE F01 = ?"))
                    {
                        pstmt.setTimestamp(1, getCurrentTimestamp(connection));
                        pstmt.setInt(2, t6514.F02);
                        pstmt.execute();
                    }
                }
            }
            // 发站内信、短信
            T6110 t6110 = selectT6110(connection, t6251.F04);
            T5122 t5122 = selectT5122(connection, t6514.F06);
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("bid", t6230.F03);
            envionment.set("feeType", t5122.F02);
            envionment.set("amount", t6514.F05.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            String content = configureProvider.format(LetterVariable.YQ_DF, envionment);
            sendLetter(connection, t6251.F04, "逾期垫付", content);
            String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
            if ("1".equals(isUseYtx))
            {
                SMSUtils smsUtils = new SMSUtils(configureProvider);
                int type = smsUtils.getTempleId(MsgVariavle.YQ_DF.getDescription());
                sendMsg(connection,
                    t6110.F04,
                    smsUtils.getSendContent(envionment.get("bid"), envionment.get("feeType"), envionment.get("amount")),
                    type);
            }
            else
            {
                String msgContent = configureProvider.format(MsgVariavle.YQ_DF, envionment);
                sendMsg(connection, t6110.F04, msgContent);
            }
            
            //如果有需要还款的加息券数据,则更新加息券利息返还记录为失效
            updateT6289(connection, t6230.F01);
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    private void updateT6253(Connection connection, BigDecimal F01, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6253 SET F05 =F05+ ?, F07 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    private T6253 selectT6253(Connection connection, int F02)
        throws SQLException
    {
        T6253 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6253 WHERE T6253.F02 = ? LIMIT 1 FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6253();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getTimestamp(7);
                }
            }
        }
        return record;
    }
    
    private int insertT6253(Connection connection, T6253 entity)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6253 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?,F08 = ?,F09 = ?,F10 = ?,F11 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setBigDecimal(4, entity.F05);
            pstmt.setBigDecimal(5, entity.F06);
            pstmt.setTimestamp(6, getCurrentTimestamp(connection));
            pstmt.setInt(7, entity.F11);
            pstmt.setString(8, entity.F09);
            pstmt.setString(9, entity.F10);
            pstmt.setInt(10, entity.F11);
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
    
    private T5122 selectT5122(Connection connection, int F01)
        throws SQLException
    {
        T5122 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03 FROM S51.T5122 WHERE T5122.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T5122();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = T5122_F03.parse(resultSet.getString(3));
                }
            }
        }
        return record;
    }
    
    private T6230 selectT6230(Connection connection, int F01)
        throws SQLException
    {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26 FROM S62.T6230 WHERE T6230.F01 = ?"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
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
                }
            }
        }
        return record;
    }
    
    private void updateT6101(Connection connection, BigDecimal F01, int F02)
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
    
    protected T6251 selectT6251(Connection connection, int F01)
        throws SQLException
    {
        T6251 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6251 WHERE T6251.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6251();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = T6251_F08.parse(resultSet.getString(8));
                    record.F09 = resultSet.getDate(9);
                    record.F10 = resultSet.getDate(10);
                    record.F11 = resultSet.getInt(11);
                }
            }
        }
        return record;
    }
    
    protected T6514 selectT6514(Connection connection, int F01)
        throws SQLException
    {
        T6514 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S65.T6514 WHERE T6514.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6514();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getInt(6);
                }
            }
        }
        return record;
    }
    
    private int insertT6251(Connection connection, T6251 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6251 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setString(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setBigDecimal(4, entity.F05);
            pstmt.setBigDecimal(5, entity.F06);
            pstmt.setBigDecimal(6, entity.F07);
            pstmt.setString(7, entity.F08.name());
            pstmt.setDate(8, entity.F09);
            pstmt.setDate(9, entity.F10);
            pstmt.setInt(10, entity.F11);
            pstmt.setInt(11, entity.F12);
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
     * 查询平台垫付类型
     */
    private T5131_F02 selectT5131(Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S51.T5131 LIMIT 1"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return T5131_F02.parse(resultSet.getString(1));
                }
            }
        }
        return null;
    }
    
    /**
     * 如果有需要还款的加息券数据,则更新加息券利息返还记录为失效
     *
     * @param connection
     * @param loanId
     * @throws Throwable
     */
    protected void updateT6289(Connection connection, int loanId)
        throws Throwable
    {
        
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6289 SET T6289.F09 = ?, T6289.F10 = ? WHERE  T6289.F02 = ? AND T6289.F09 = ?"))
        {
            pstmt.setString(1, T6289_F09.YSX.name());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, loanId);
            pstmt.setString(4, T6289_F09.WFH.name());
            pstmt.execute();
        }
        
    }
    
    private int insertT6255(Connection connection, T6255 entity)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6255 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ? ",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setBigDecimal(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setInt(4, entity.F05);
            pstmt.setInt(5, entity.F06);
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
}
