package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.dimeng.p2p.S61.entities.T6103;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6286;
import com.dimeng.p2p.S62.entities.T6288;
import com.dimeng.p2p.S62.entities.T6292;
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
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6286_F06;
import com.dimeng.p2p.S62.enums.T6286_F07;
import com.dimeng.p2p.S62.enums.T6288_F06;
import com.dimeng.p2p.S62.enums.T6288_F07;
import com.dimeng.p2p.S62.enums.T6288_F08;
import com.dimeng.p2p.S62.enums.T6292_F06;
import com.dimeng.p2p.S62.enums.T6292_F07;
import com.dimeng.p2p.S63.entities.T6342;
import com.dimeng.p2p.S63.enums.T6342_F04;
import com.dimeng.p2p.S65.entities.T6508;
import com.dimeng.p2p.S65.entities.T6522;
import com.dimeng.p2p.S65.entities.T6526;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

@ResourceAnnotation
public class TenderCancelExecutor extends AbstractOrderExecutor
{
    
    public TenderCancelExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return TenderCancelExecutor.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try
        {
            // 订单详情
            T6508 t6508 = selectT6508(connection, orderId);
            if (t6508 == null)
            {
                throw new LogicalException("订单信息不存在");
            }
            // 锁定投资记录
            T6250 t6250 = selectT6250(connection, t6508.F03);
            if (t6250 == null)
            {
                throw new LogicalException("投资记录不存在");
            }
            if (t6250.F07 == T6250_F07.S)
            {
                throw new LogicalException("不能重复取消");
            }
            // 标的详情,锁定标
            T6230 t6230 = selectT6230(connection, t6250.F02);
            if (t6230 == null)
            {
                throw new LogicalException("标不存在");
            }
            if (T6230_F20.TBZ != t6230.F20 && T6230_F20.DFK != t6230.F20)
            {
                throw new LogicalException("不是投资中或待放款状态,不能取消投资");
            }
            // 锁定投资人锁定账户
            T6101 sdzh = selectT6101(connection, t6508.F02, T6101_F03.SDZH, true);
            if (sdzh == null)
            {
                throw new LogicalException("投资人锁定账户不存在");
            }
            // 锁定投资人往来账户
            T6101 wlzh = selectT6101(connection, t6508.F02, T6101_F03.WLZH, true);
            if (wlzh == null)
            {
                throw new LogicalException("投资人往来账户不存在");
            }
            // 红包
            T6292 t6292 = selectT6292(connection, t6508.F03, T6292_F07.F);
            BigDecimal sjAmount = t6250.F04;
            if (t6292 != null)
            {
                sjAmount = sjAmount.subtract(t6292.F04);
                sjAmount = sjAmount.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : sjAmount;
                updateT6292(connection, T6292_F06.S, t6292.F01);
                updateT6342(connection, t6292.F10, T6342_F04.WSY);
            }
            Timestamp timestamp = getCurrentTimestamp(connection);
            {
                // 扣减投资人锁定账户
                sdzh.F06 = sdzh.F06.subtract(sjAmount);
                if (sdzh.F06.compareTo(BigDecimal.ZERO) < 0)
                {
                    throw new LogicalException("投资人锁定账户余额不足");
                }
                updateT6101(connection, sdzh.F06, sdzh.F01);
                // 资金流水
                T6102 t6102 = new T6102();
                t6102.F02 = sdzh.F01;
                t6102.F03 = FeeCode.TZ_CX;
                t6102.F04 = wlzh.F01;
                t6102.F05 = timestamp;
                t6102.F07 = sjAmount;
                t6102.F08 = sdzh.F06;
                t6102.F09 = String.format("撤销散标投资:%s", t6230.F03);
                insertT6102(connection, t6102);
            }
            {
                // 增加投资人往来账户
                wlzh.F06 = wlzh.F06.add(sjAmount);
                updateT6101(connection, wlzh.F06, wlzh.F01);
                // 资金流水
                T6102 t6102 = new T6102();
                t6102.F02 = wlzh.F01;
                t6102.F03 = FeeCode.TZ_CX;
                t6102.F04 = sdzh.F01;
                t6102.F05 = timestamp;
                t6102.F06 = sjAmount;
                t6102.F08 = wlzh.F06;
                t6102.F09 = String.format("撤销散标投资:%s", t6230.F03);
                insertT6102(connection, t6102);
            }
            
            // T6115 t6115 = selectT6115(connection, t6508.F02);
            // if (t6115 != null) {
            // // 回滚理财统计数据
            // try (PreparedStatement pstmt = connection
            // .prepareStatement("UPDATE S61.T6115 SET F03 = ? WHERE F01 = ?"))
            // {
            // t6115.F03 = t6115.F03.subtract(t6250.F04);
            // pstmt.setBigDecimal(1, t6115.F03);
            // pstmt.setInt(2, t6115.F01);
            // pstmt.execute();
            // }
            // }
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            // 更新投资记录为取消
            updateT6250(connection, T6250_F07.S, t6250.F01);
            
            if (T6230_F20.TBZ == t6230.F20)
            {
                // 更新可投金额
                t6230.F07 = t6230.F07.add(t6250.F04);
                updateT6230(connection, t6230.F07, t6230.F01);
            }
            else if (T6230_F20.DFK == t6230.F20)
            {
                // 判断是否全部投资均已取消,是则更新标状态为已流标
                int count = 0;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT COUNT(*) FROM S62.T6250 WHERE F02 = ? AND F07 = 'F'"))
                {
                    pstmt.setInt(1, t6230.F01);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            count = resultSet.getInt(1);
                        }
                    }
                }
                if (count == 0)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6230 SET F20 = 'YLB' WHERE F01 = ?"))
                    {
                        pstmt.setInt(1, t6230.F01);
                        pstmt.executeUpdate();
                    }
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6231 SET F15 = ? WHERE F01 = ?"))
                    {
                        pstmt.setTimestamp(1, timestamp);
                        pstmt.setInt(2, t6230.F01);
                        pstmt.executeUpdate();
                    }
                    // 回滚借款人信用额度
                    rollbackCreditLineGr(connection, t6230);
                    rebackAmount(connection, t6230);
                }
            }
            
            //投资人站内信
            T6110 t6110 = selectT6110(connection, t6508.F02);
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("title", t6230.F03);
            String content = configureProvider.format(LetterVariable.TZR_TBLB, envionment);
            sendLetter(connection, t6508.F02, "投资流标", content);
            
            //是否已流标
            boolean isLb = false;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S62.T6230 WHERE T6230.F20 = 'YLB' AND T6230.F01 = ?"))
            {
                pstmt.setInt(1, t6230.F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        isLb = true;
                    }
                }
            }
            
            if (isLb)
            {
                //借款人站内信
                envionment.set("datetime", DateTimeParser.format(t6230.F24));
                envionment.set("title", t6230.F03);
                envionment.set("reason", params.get("des"));
                content = configureProvider.format(LetterVariable.LOAN_FAILED, envionment);
                sendLetter(connection, t6230.F02, "标的流标", content);
            }
            
            String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
            if ("1".equals(isUseYtx))
            {
                SMSUtils smsUtils = new SMSUtils(configureProvider);
                int type = smsUtils.getTempleId(MsgVariavle.TZR_TBLB.getDescription());
                sendMsg(connection, t6110.F04, t6230.F03, type);
            }
            else
            {
                String msgContent = configureProvider.format(MsgVariavle.TZR_TBLB, envionment);
                sendMsg(connection, t6110.F04, msgContent);
            }
            
            // 体验金投资取消
            cancelExper(connection, params, t6230, configureProvider, t6110, envionment, isUseYtx);
            
            // 取消加息券投资订单
            cancelCoupon(connection, t6230, configureProvider, t6110, envionment, isUseYtx, orderId);
            
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    /**
     * 体验金投资取消
     * 
     * @param connection
     * @param params
     * @param t6230
     * @param configureProvider
     * @param t6110
     * @param envionment
     * @param isUseYtx
     * @throws Throwable
     */
    private void cancelExper(SQLConnection connection, Map<String, String> params, T6230 t6230,
        ConfigureProvider configureProvider, T6110 t6110, Envionment envionment, String isUseYtx)
        throws Throwable
    {
        String experOrdIds = params.get("experOrderId");
        if (!StringHelper.isEmpty(experOrdIds))
        {
            String[] arrayExperOrdIds = experOrdIds.split(",");
            String experContent = configureProvider.format(LetterVariable.TZR_TYJ_TBLB, envionment);
            for (String strExperOrd : arrayExperOrdIds)
            {
                int intExperOrd = IntegerParser.parse(strExperOrd);
                if (intExperOrd > 0)
                {
                    // 订单详情
                    T6522 t6522 = selectT6522(connection, intExperOrd);
                    if (t6522 == null)
                    {
                        throw new LogicalException("体验金流标订单信息不存在");
                    }
                    // 锁定投资记录
                    T6286 t6286 = selectT6286(connection, t6522.F03);
                    if (t6286 == null)
                    {
                        throw new LogicalException("体验金投资记录不存在");
                    }
                    if (t6286.F06 == T6286_F06.S)
                    {
                        throw new LogicalException("不能重复取消体验金投资记录");
                    }
                    // 更新投资记录为取消
                    updateT6286(connection, T6286_F06.S, t6286.F01);
                    // 锁定体验金信息
                    T6103 t6103 = selectT6103(connection, t6286.F10);
                    T6103_F06 t6103_F06 = T6103_F06.WSY;
                    if (t6103.F05.compareTo(getCurrentDate(connection)) < 0)
                    {
                        t6103_F06 = T6103_F06.YGQ;
                    }
                    // 更新体验金状态
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S61.T6103 SET F06 = ? WHERE F01 = ?"))
                    {
                        pstmt.setString(1, t6103_F06.name());
                        pstmt.setInt(2, t6103.F01);
                        pstmt.execute();
                    }
                    // 更新体验金订单
                    updateConfirm(connection, T6501_F03.CG, intExperOrd);
                    Envionment experEnvionment = configureProvider.createEnvionment();
                    experEnvionment.set("title", t6230.F03);
                    sendLetter(connection, t6522.F02, "体验金投资流标", experContent);
                }
            }
            if ("1".equals(isUseYtx))
            {
                SMSUtils smsUtils = new SMSUtils(configureProvider);
                int type = smsUtils.getTempleId(MsgVariavle.TZR_TYJ_TBLB.getDescription());
                sendMsg(connection, t6110.F04, t6230.F03, type);
            }
            else
            {
                String msgExperContent = configureProvider.format(MsgVariavle.TZR_TYJ_TBLB, envionment);
                sendMsg(connection, t6110.F04, msgExperContent);
            }
        }
    }
    
    /**
     * 体验金投资取消
     * 
     * @param connection
     * @param t6230
     * @param configureProvider
     * @param t6110
     * @param envionment
     * @param isUseYtx
     * @throws Throwable
     */
    private void cancelCoupon(SQLConnection connection, T6230 t6230, ConfigureProvider configureProvider, T6110 t6110,
        Envionment envionment, String isUseYtx, int cancelOrderId)
        throws Throwable
    {
        T6526 t6526 = getT6526(connection, cancelOrderId);
        if (t6526 == null)
        {
            return;
        }
        
        String experContent = configureProvider.format(LetterVariable.TZR_JXQ_TBLB, envionment);
        // 锁定投资记录
        T6288 t6288 = selectT6288(connection, t6526.F03);
        if (t6288 == null)
        {
            return;
        }
        if (t6288.F06 == T6288_F06.S)
        {
            return;
        }
        // 更新投资记录为取消
        updateT6288(connection, T6288_F06.S, t6288.F01);
        // 锁定加息券信息
        T6342 t6342 = getT6342(connection, t6288.F02, t6288.F03);
        T6342_F04 t6342_F04 = T6342_F04.WSY;
        if (!t6342.F08.after(new Date()))
        {
            t6342_F04 = T6342_F04.YGQ;
        }
        // 更新加息券状态
        updateT6342(connection, t6342.F01, t6342_F04);
        // 更新加息券订单
        updateConfirm(connection, T6501_F03.CG, t6526.F01);
        Envionment experEnvionment = configureProvider.createEnvionment();
        experEnvionment.set("title", t6230.F03);
        sendLetter(connection, t6288.F03, "加息券投资流标", experContent);
        
        if ("1".equals(isUseYtx))
        {
            SMSUtils smsUtils = new SMSUtils(configureProvider);
            int type = smsUtils.getTempleId(MsgVariavle.TZR_JXQ_TBLB.getDescription());
            sendMsg(connection, t6110.F04, t6230.F03, type);
        }
        else
        {
            String msgExperContent = configureProvider.format(MsgVariavle.TZR_JXQ_TBLB, envionment);
            sendMsg(connection, t6110.F04, msgExperContent);
        }
    }
    
    /**
     * 根据标流标订单查询是否有加息券流标订单
     * 
     * @param cancelOrderId
     * @return
     * @throws Throwable
     */
    protected T6526 getT6526(SQLConnection connection, int cancelOrderId)
        throws Throwable
    {
        T6526 t6526 = null;
        try (PreparedStatement pstm =
            connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S65.T6526 WHERE F04 = ? FOR UPDATE"))
        {
            pstm.setInt(1, cancelOrderId);
            try (ResultSet resultSet = pstm.executeQuery())
            {
                if (resultSet.next())
                {
                    t6526 = new T6526();
                    t6526.F01 = resultSet.getInt(1);
                    t6526.F02 = resultSet.getInt(2);
                    t6526.F03 = resultSet.getInt(3);
                    t6526.F04 = resultSet.getInt(4);
                }
            }
        }
        return t6526;
    }
    
    /**
     * 回滚借款人信用额度
     * 
     * @param connection
     * @param t6230
     * @throws Throwable
     */
    private void rollbackCreditLineGr(SQLConnection connection, T6230 t6230)
        throws Throwable
    {
        BigDecimal creditAmount = BigDecimal.ZERO;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F03 FROM S61.T6116 WHERE F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, t6230.F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    creditAmount = resultSet.getBigDecimal(1);
                }
                else
                {
                    throw new LogicalException("借款人信用记录不存在");
                }
            }
        }
        creditAmount = creditAmount.add(t6230.F05);
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6116 SET F03 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, creditAmount);
            pstmt.setInt(2, t6230.F02);
            pstmt.executeUpdate();
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6117 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = 0, F07 = ?, F08 = ?"))
        {
            pstmt.setInt(1, t6230.F02);
            pstmt.setInt(2, FeeCode.XY_LB_FH);
            pstmt.setTimestamp(3, getCurrentTimestamp(connection));
            pstmt.setBigDecimal(4, t6230.F05);
            pstmt.setBigDecimal(5, creditAmount);
            pstmt.setString(6, String.format("流标返还:%s", t6230.F25));
            pstmt.execute();
        }
    }
    
    /**
     * 回滚机构信用额度
     * 
     * @param connection
     * @param t6230
     * @throws Throwable
     */
    private void rollbackCreditLineJG(SQLConnection connection, T6230 t6230)
        throws Throwable
    {
        // 查询担保机构的信用额度
        BigDecimal dbtotalAmount = new BigDecimal(0);
        BigDecimal creditAmount = BigDecimal.ZERO;
        int dbUserId = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6116.F03,T6236.F03 FROM S62.T6236 INNER JOIN S61.T6116 ON T6236.F03 = T6116.F01 WHERE T6236.F02 = ? AND T6236.F04 = 'S' LIMIT 1"))
        {
            pstmt.setInt(1, t6230.F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    dbtotalAmount = resultSet.getBigDecimal(1);
                    dbUserId = resultSet.getInt(2);
                }
            }
        }
        if (t6230.F11 == T6230_F11.S && dbUserId > 0)
        {
            creditAmount = dbtotalAmount.add(t6230.F05);
            // 更新机构的信用额度
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6116 SET F03 = F03 + ? WHERE F01 = ?"))
            {
                pstmt.setBigDecimal(1, t6230.F05);
                pstmt.setInt(2, dbUserId);
                pstmt.execute();
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6117 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?, F06 = 0, F07 = ?, F08 = ?"))
            {
                pstmt.setInt(1, dbUserId);
                pstmt.setInt(2, FeeCode.XY_LB_FH);
                pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                pstmt.setBigDecimal(4, t6230.F05);
                pstmt.setBigDecimal(5, creditAmount);
                pstmt.setString(6, String.format("流标返还:%s", t6230.F25));
                pstmt.execute();
            }
        }
    }
    
    /**
     * 流标返还担保方的担保额度
     * @param connection
     * @param t6230
     * @throws Throwable
     */
    private void rebackAmount(Connection connection, T6230 t6230)
        throws Throwable
    {
        BigDecimal amount = t6230.F05;
        if (t6230.F11 == T6230_F11.S)
        {
            int dbUserId = 0;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6236.F03 FROM S62.T6236 WHERE T6236.F02 = ? AND T6236.F04 = 'S' LIMIT 1"))
            {
                pstmt.setInt(1, t6230.F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        dbUserId = resultSet.getInt(1);
                    }
                }
            }
            BigDecimal dbTotalAmount = new BigDecimal(0);
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04 FROM S61.T6125 WHERE T6125.F01 = (SELECT T1.F01 FROM S61.T6125 T1 WHERE T1.F02 = ? LIMIT 1) FOR UPDATE"))
            {
                pstmt.setInt(1, dbUserId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        dbTotalAmount = resultSet.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6125 SET F04 = ? WHERE F02 = ?"))
            {
                pstmt.setBigDecimal(1, dbTotalAmount.add(amount));
                pstmt.setInt(2, dbUserId);
                pstmt.execute();
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6126 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?, F07 = ?, F08 = ?"))
            {
                pstmt.setInt(1, dbUserId);
                pstmt.setInt(2, FeeCode.DB_LB_FH);
                pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                pstmt.setBigDecimal(4, amount);
                pstmt.setBigDecimal(5, dbTotalAmount.add(amount));
                pstmt.setString(6, "流标担保额度返还");
                pstmt.execute();
            }
        }
    }
    
    private void updateT6250(Connection connection, T6250_F07 F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6250 SET F07 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01.name());
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    private void updateT6292(Connection connection, T6292_F06 F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6292 SET F06 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01.name());
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    private void updateT6230(Connection connection, BigDecimal F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6230 SET F07 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    protected T6508 selectT6508(Connection connection, int F01)
        throws SQLException
    {
        T6508 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03 FROM S65.T6508 WHERE T6508.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6508();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
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
    
    private T6230 selectT6230(Connection connection, int F01)
        throws SQLException
    {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F27 FROM S62.T6230 WHERE F01 = ? FOR UPDATE"))
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
                    record.F27 = T6230_F27.parse(resultSet.getString(27));
                }
            }
        }
        return record;
    }
    
    private T6292 selectT6292(Connection connection, int F01, T6292_F07 F07)
        throws SQLException
    {
        T6292 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F03, F04,F10 FROM S62.T6292 WHERE T6292.F09 = ? AND T6292.F07 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            pstmt.setString(2, F07.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6292();
                    record.F01 = resultSet.getInt(1);
                    record.F03 = resultSet.getInt(2);
                    record.F04 = resultSet.getBigDecimal(3);
                    record.F10 = resultSet.getInt(4);
                }
            }
        }
        return record;
    }
    
    protected T6250 selectT6250(Connection connection, int F01)
        throws SQLException
    {
        T6250 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6250 WHERE T6250.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6250();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getTimestamp(6);
                    record.F07 = T6250_F07.parse(resultSet.getString(7));
                }
            }
        }
        return record;
    }
    
    protected T6522 selectT6522(Connection connection, int F01)
        throws SQLException
    {
        T6522 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03 FROM S65.T6522 WHERE T6522.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6522();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                }
            }
        }
        return record;
    }
    
    private T6286 selectT6286(Connection connection, int F01)
        throws SQLException
    {
        T6286 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07,F10 FROM S62.T6286 WHERE T6286.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6286();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = T6286_F06.parse(resultSet.getString(6));
                    record.F07 = T6286_F07.parse(resultSet.getString(7));
                    record.F10 = resultSet.getInt(8);
                }
            }
        }
        return record;
    }
    
    private void updateT6286(Connection connection, T6286_F06 F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6286 SET F06 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01.name());
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    @Override
    protected void doSubmit(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        String expOrdId = params.get("experOrderId");
        if (!StringHelper.isEmpty(expOrdId))
        {
            try
            {
                String[] arrayExperOrdId = expOrdId.split(",");
                for (String strExperOrd : arrayExperOrdId)
                {
                    if (IntegerParser.parse(strExperOrd) > 0)
                    {
                        // 修改订单状态
                        updateSubmit(connection, T6501_F03.DQR, IntegerParser.parse(strExperOrd));
                    }
                }
            }
            catch (Exception e)
            {
                // 异常处理
                handleError(connection, orderId);
                // 修改订单状态
                updateSubmit(connection, T6501_F03.SB, IntegerParser.parse(expOrdId));
                // 记录日志
                logger.error(e, e);
            }
        }
    }
    
    private T6103 selectT6103(Connection connection, int F01)
        throws SQLException
    {
        T6103 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01,F02, F03, F04, F05 FROM S61.T6103 WHERE T6103.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6103();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getTimestamp(4);
                    record.F05 = resultSet.getTimestamp(5);
                }
            }
        }
        return record;
    }
    
    /**
     * 更新活动状态为：未使用
     * 
     * @param connection
     * @param id
     * @throws Throwable
     */
    private void updateT6342(Connection connection, int id, T6342_F04 t6342_F04)
        throws Throwable
    {
        // 更新活动信息
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S63.T6342 SET F04 = ? , F05= ? ,F06= ?  WHERE F01 = ? "))
        {
            pstmt.setString(1, t6342_F04.name());
            pstmt.setString(2, null);
            pstmt.setString(3, null);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * 获取加息券投资记录
     * 
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private T6288 selectT6288(Connection connection, int F01)
        throws SQLException
    {
        T6288 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6288 WHERE T6288.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6288();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = T6288_F06.parse(resultSet.getString(6));
                    record.F07 = T6288_F07.parse(resultSet.getString(7));
                    record.F08 = T6288_F08.parse(resultSet.getString(8));
                    record.F09 = resultSet.getInt(9);
                    record.F10 = resultSet.getInt(10);
                    record.F11 = resultSet.getBigDecimal(11);
                }
            }
        }
        return record;
    }
    
    /**
     * 更新加息券投资记录
     * 
     * @param connection
     * @param F01
     * @param F02
     * @throws SQLException
     */
    private void updateT6288(Connection connection, T6288_F06 F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6288 SET F06 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01.name());
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    /**
     * 获取用户用于投资的活动
     * 
     * @param connection
     * @param loanId
     * @param userId
     * @return
     * @throws Throwable
     */
    private T6342 getT6342(Connection connection, int loanId, int userId)
        throws Throwable
    {
        T6342 t6342 = null;
        try (PreparedStatement pstm =
            connection.prepareStatement("SELECT F01, F08 FROM S63.T6342 WHERE F06 = ? AND F02 = ? LIMIT 1 FOR UPDATE"))
        {
            pstm.setInt(1, loanId);
            pstm.setInt(2, userId);
            try (ResultSet resultSet = pstm.executeQuery())
            {
                if (resultSet.next())
                {
                    t6342 = new T6342();
                    t6342.F01 = resultSet.getInt(1);
                    t6342.F08 = resultSet.getTimestamp(2);
                    return t6342;
                }
            }
        }
        return t6342;
    }
}
