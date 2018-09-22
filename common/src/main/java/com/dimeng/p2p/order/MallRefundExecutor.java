/*
 * 文 件 名:  MallRefundExecutor.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月28日
 */
package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S63.entities.T6351;
import com.dimeng.p2p.S63.entities.T6352;
import com.dimeng.p2p.S63.entities.T6359;
import com.dimeng.p2p.S63.enums.T6352_F06;
import com.dimeng.p2p.S63.enums.T6359_F08;
import com.dimeng.p2p.S65.entities.T6528;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.Formater;

/**
 * <商品退款执行器>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月28日]
 */
@ResourceAnnotation
public class MallRefundExecutor extends AbstractOrderExecutor
{
    
    public MallRefundExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try
        {
            // 查询订单
            T6528 t6528 = selectT6528(connection, orderId);
            if (t6528 == null)
            {
                throw new LogicalException("订单记录不存在");
            }
            T6359 t6359 = selectT6359(connection, t6528.F04);
            if (null == t6359)
            {
                throw new ParameterException("订单明细不存在");
            }
            if (!(T6359_F08.refunding.name().equals(t6359.F08.name()) || T6359_F08.pendding.name()
                .equals(t6359.F08.name())))
            {
                throw new ParameterException("不是申请退款或待审核状态");
            }
            int pid = getPTID(connection); // 平台用户id
            T6101 skT6101 = selectT6101(connection, t6528.F02, T6101_F03.WLZH, true);
            if (skT6101 == null)
            {
                throw new LogicalException("收款账号不存在");
            }
            T6101 ptT6101 = selectT6101(connection, pid, T6101_F03.WLZH, true);
            if (ptT6101 == null)
            {
                throw new LogicalException("平台账号不存在");
            }
            if (ptT6101.F06.compareTo(t6528.F03) < 0)
            {
                throw new ParameterException("平台余额不足");
            }
            T6351 t6351 = selectT6351(connection, t6359.F03);
            if (t6351 == null)
            {
                throw new ParameterException("订单" + orderId + "对应的商品不存在");
            }
            T6352 t6352 = selectT6352(connection, t6359.F02);
            String goodName = t6351 == null ? "" : t6351.F03;
            int feeCode =
                t6359.F08.name().equals(T6359_F08.pendding.name()) ? FeeCode.MALL_NOPASS : FeeCode.MALL_REFUND;
            {
                skT6101.F06 = skT6101.F06.add(t6528.F03);
                //收款人增加入账账户金额
                updateT6101(connection, skT6101.F06, skT6101.F01);
                // 资金流水
                T6102 t6102 = new T6102();
                t6102.F02 = skT6101.F01;
                t6102.F03 = feeCode;
                t6102.F04 = ptT6101.F01;
                t6102.F06 = t6528.F03;
                t6102.F08 = skT6101.F06;
                t6102.F09 = String.format("订单号:%s，商品名称：%s", t6352.F03, goodName);
                insertT6102(connection, t6102);
            }
            {
                //平台减少入账账户金额
                ptT6101.F06 = ptT6101.F06.subtract(t6528.F03);
                updateT6101(connection, ptT6101.F06, ptT6101.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = ptT6101.F01;
                t6102.F03 = feeCode;
                t6102.F04 = skT6101.F01;
                t6102.F07 = t6528.F03;
                t6102.F08 = ptT6101.F06;
                t6102.F09 = String.format("订单号:%s，商品名称：%s", t6352.F03, goodName);
                insertT6102(connection, t6102);
            }
            if (t6359.F08 == T6359_F08.pendding)
                t6359.F08 = T6359_F08.nopass; //如果是审核不通过
            if (t6359.F08 == T6359_F08.refunding)
                t6359.F08 = T6359_F08.refund;//如果是退款
            updateT6359(connection, t6359.F08, t6359.F01);
            
            //退款、审核不通过修改库存
            rollBackT6351(t6359.F06, t6351.F01, connection);
            
            Map<String, String> map = sendParams(connection, t6528.F04);
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("userName", map.get("userName"));
            envionment.set("time", map.get("time"));
            envionment.set("commodityName", map.get("commodityName"));
            envionment.set("num", map.get("num"));
            String title = "";
            String content = "";
            String msgContent = "";
            if (t6359.F08.name().equals(T6359_F08.refund.name()))
            {
                title = "商品退款成功";
                content = configureProvider.format(LetterVariable.MALLREFUND, envionment);
                msgContent = configureProvider.format(MsgVariavle.MALLREFUND, envionment);
            }
            else if (t6359.F08.name().equals(T6359_F08.nopass.name()))
            {
                title = LetterVariable.MALLNOPASSLETTER.getDescription();
                envionment.set("payment", "余额");
                envionment.set("reason", params.get("reason"));
                content = configureProvider.format(LetterVariable.MALLNOPASSLETTER, envionment);
                msgContent = configureProvider.format(MsgVariavle.MALLNOPASSSMS, envionment);
            }
            sendLetter(connection, t6528.F02, title, content);
            String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
            if ("1".equals(isUseYtx))
            {
                SMSUtils smsUtil = new SMSUtils(configureProvider);
                if (t6359.F08.name().equals(T6359_F08.nopass.name()))
                {
                    int type = smsUtil.getTempleId(MsgVariavle.MALLNOPASSSMS.getDescription());
                    sendMsg(connection,
                        map.get("tel"),
                        smsUtil.getSendContent(map.get("userName"),
                            map.get("time"),
                            map.get("commodityName"),
                            map.get("num"),
                            map.get("reason"),
                            map.get("payment")),
                        type);
                }
                else
                {
                    int type = smsUtil.getTempleId(MsgVariavle.MALLREFUND.getDescription());
                    sendMsg(connection,
                        map.get("tel"),
                        smsUtil.getSendContent(map.get("userName"),
                            map.get("time"),
                            map.get("commodityName"),
                            map.get("num")),
                        type);
                }
            }
            else
            {
                sendMsg(connection, map.get("tel"), msgContent);
            }
            // 托管用
            callFace(connection, orderId, params);
        }
        catch (Exception e)
        {
            logger.error(e, e);
            rollbackFace(connection, orderId, params);
            throw e;
        }
        
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return MallRefundExecutor.class;
    }
    
    protected T6528 selectT6528(Connection connection, int F01)
        throws SQLException
    {
        T6528 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01,F02,F03,F04 FROM S65.T6528 WHERE T6528.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6528();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getInt(4);
                }
            }
        }
        return record;
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
    
    protected void updateT6351(Connection connection, int F01, int F02, int F03)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S63.T6351 SET F06 = ?,F07 = ? WHERE F01 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F03);
            pstmt.execute();
        }
    }
    
    protected T6351 selectT6351(Connection connection, int id)
        throws Throwable
    {
        T6351 t6351 = null;
        try (PreparedStatement statement =
            connection.prepareStatement("SELECT F01,F02,F03 FROM S63.T6351 WHERE F01 = ?"))
        {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery())
            {
                if (rs.next())
                {
                    t6351 = new T6351();
                    t6351.F01 = rs.getInt(1);
                    t6351.F02 = rs.getString(2);
                    t6351.F03 = rs.getString(3);
                }
            }
        }
        return t6351;
    }
    
    protected T6359 selectT6359(Connection connection, int id)
        throws SQLException
    {
        T6359 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01,F02,F03,F06,F08,F18 FROM S63.T6359 WHERE F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6359();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F06 = resultSet.getInt(4);
                    record.F08 = T6359_F08.parse(resultSet.getString(5));
                    record.F18 = resultSet.getBigDecimal(6);
                }
            }
        }
        return record;
    }
    
    protected T6352 selectT6352(Connection connection, int id)
        throws SQLException
    {
        T6352 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06 FROM S63.T6352 WHERE F01 = ?"))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6352();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getTimestamp(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = T6352_F06.parse(resultSet.getString(6));
                }
            }
        }
        return record;
    }
    
    protected void updateT6359(Connection connection, T6359_F08 F08, int F01)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S63.T6359 SET F08=?,F19=? WHERE F01=?"))
        {
            pstmt.setString(1, F08.name());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F01);
            pstmt.execute();
        }
    }
    
    protected Map<String, String> sendParams(Connection connection, int F01)
        throws SQLException
    {
        Map<String, String> map = new HashMap<String, String>();
        try
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6110.F02 F01,T6352.F04 F02,T6351.F03 F03,T6359.F06 F04,T6110.F04 F05 FROM S63.T6359 LEFT JOIN S63.T6351 ON T6351.F01=T6359.F03 LEFT JOIN S63.T6352 ON T6352.F01=T6359.F02 LEFT JOIN S61.T6110 ON T6352.F02=T6110.F01 WHERE T6359.F01=? LIMIT 1"))
            {
                pstmt.setInt(1, F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        map.put("userName", resultSet.getString(1));
                        map.put("time", Formater.formatDateTime(resultSet.getTimestamp(2)));
                        map.put("commodityName", resultSet.getString(3));
                        map.put("num", resultSet.getInt(4) + "");
                        map.put("tel", resultSet.getString(5));
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
        return map;
    }
    
    /** 根据订单明细ID来回滚商品库存，不会回滚成交数量（产品规定已付款就算是成交，与后台是否审核通过无关）
     * <功能详细描述>
     * @param num 购买数量
     * @param t6351Id t6351.F01
     * @return T6501ID
     * @throws Throwable
     */
    public void rollBackT6351(int num, int t6351Id, Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S63.T6351 SET F06 = F06+? WHERE F01 = ? "))
        {
            pstmt.setInt(1, num);
            pstmt.setInt(2, t6351Id);
            pstmt.execute();
        }
    }
    
}
