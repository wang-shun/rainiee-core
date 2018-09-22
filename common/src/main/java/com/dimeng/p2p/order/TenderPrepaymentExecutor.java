package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.entities.T6253;
import com.dimeng.p2p.S62.entities.T6260;
import com.dimeng.p2p.S62.entities.T6289;
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
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.S62.enums.T6289_F09;
import com.dimeng.p2p.S65.entities.T6521;
import com.dimeng.p2p.S65.entities.T6525;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.Formater;
import com.dimeng.util.parser.DateTimeParser;

@ResourceAnnotation
public class TenderPrepaymentExecutor extends AbstractOrderExecutor
{
    public TenderPrepaymentExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return TenderPrepaymentExecutor.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try
        {
            // 订单查询
            T6521 t6521 = selectT6521(connection, orderId);
            if (t6521 == null)
            {
                throw new LogicalException("提前还款订单明细记录不存在");
            }
            // 锁定标
            T6230 t6230 = selectT6230(connection, t6521.F03);
            if (t6230 == null)
            {
                throw new LogicalException("标记录不存在");
            }
            if (FeeCode.TZ_BJ == t6521.F07 || FeeCode.TZ_LX == t6521.F07)
            {
                // 查询还款记录
                T6252 t6252 = selectT6252(connection, t6521.F07, t6521.F05, t6521.F04);
                if (t6252 == null)
                {
                    throw new LogicalException("还款记录不存在");
                }
                if (t6252.F09 == T6252_F09.YH)
                {
                    throw new LogicalException("还款记录不存在");
                }
            }
            // 锁定还款人往来账户
            T6101 hkrAcount = selectT6101(connection, t6521.F09, T6101_F03.WLZH, true);
            if (hkrAcount == null)
            {
                throw new LogicalException("还款人往来账户不存在");
            }
            // 锁定收款人往来账户
            T6101 skrAcount = null;
            if (t6521.F09 == t6521.F02)
            {
                skrAcount = hkrAcount;
            }
            else
            {
                skrAcount = selectT6101(connection, t6521.F02, T6101_F03.WLZH, true);
            }
            if (skrAcount == null)
            {
                throw new LogicalException("收款人往来账户不存在");
            }
            // 还款
            String comment = String.format("散标提前还款:%s 第 %s期", t6230.F25, Integer.toString(t6521.F05));
            {
                hkrAcount.F06 = hkrAcount.F06.subtract(t6521.F06);
                if (hkrAcount.F06.compareTo(BigDecimal.ZERO) < 0)
                {
                    throw new LogicalException("还款人账户余额不足");
                }
                updateT6101(connection, hkrAcount.F06, hkrAcount.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = hkrAcount.F01;
                t6102.F03 = t6521.F07;
                t6102.F04 = skrAcount.F01;
                t6102.F07 = t6521.F06;
                t6102.F08 = hkrAcount.F06;
                t6102.F09 = comment;
                insertT6102(connection, t6102);
            }
            {
                skrAcount.F06 = skrAcount.F06.add(t6521.F06);
                updateT6101(connection, skrAcount.F06, skrAcount.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = skrAcount.F01;
                t6102.F03 = t6521.F07;
                t6102.F04 = hkrAcount.F01;
                t6102.F06 = t6521.F06;
                t6102.F08 = skrAcount.F06;
                t6102.F09 = comment;
                insertT6102(connection, t6102);
            }
            
            if (FeeCode.TZ_WYJ == t6521.F07 || FeeCode.TZ_WYJ_SXF == t6521.F07)
            {
                insertT6252(connection, T6252_F09.YH, t6521);
            }
            else
            {
                updateT6252(connection, t6521.F07, t6521.F05, t6521.F04);
            }
            
            // 更新债权持有金额
            if (t6521.F07 == FeeCode.TZ_BJ)
            {
                // 锁定债权信息
                T6251 t6251 = selectT6251(connection, t6521.F04);
                if (t6251 == null)
                {
                    throw new LogicalException("债权记录不存在");
                }
                t6251.F07 = t6251.F07.subtract(t6521.F06);
                if (t6251.F07.compareTo(BigDecimal.ZERO) <= 0)
                {
                    t6251.F07 = BigDecimal.ZERO;
                }
                // 是否有债权转让，有则下架
                offCreditor(connection, t6251);
                updateT6251(connection, t6251.F07, t6521.F04);
                {
                    // 添加借款人信用额度
                    BigDecimal ed = BigDecimal.ZERO;
                    try (PreparedStatement ps =
                        connection.prepareStatement("SELECT F03 FROM S61.T6116 WHERE F01 = ? FOR UPDATE"))
                    {
                        ps.setInt(1, t6230.F02);
                        try (ResultSet resultSet = ps.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                ed = resultSet.getBigDecimal(1);
                            }
                        }
                    }
                    ed = ed.add(t6521.F06);
                    try (PreparedStatement ps =
                        connection.prepareStatement("UPDATE S61.T6116 SET F03=F03+? WHERE F01=?"))
                    {
                        ps.setBigDecimal(1, t6521.F06);
                        ps.setInt(2, t6230.F02);
                        ps.execute();
                    }
                    try (PreparedStatement ps =
                        connection.prepareStatement("INSERT INTO S61.T6117 SET F02=?,F03=?,F04=?,F05=?,F07=?,F08=?"))
                    {
                        ps.setInt(1, t6230.F02);
                        ps.setInt(2, FeeCode.XY_HK_FH);
                        ps.setTimestamp(3, getCurrentTimestamp(connection));
                        ps.setBigDecimal(4, t6521.F06);
                        ps.setBigDecimal(5, ed);
                        ps.setString(6, "提前还款信用额度返回");
                        ps.execute();
                    }
                    
                    // 查询担保信息
                    int dbjgId = 0;
                    try (PreparedStatement ps =
                        connection.prepareStatement("SELECT T6236.F03 FROM S62.T6236 WHERE T6236.F02 = ? FOR UPDATE"))
                    {
                        ps.setInt(1, t6230.F01);
                        try (ResultSet resultSet = ps.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                dbjgId = resultSet.getInt(1);
                            }
                        }
                    }
                    
                    // 如果有担保，添加担保机构信用额度
                    if (dbjgId > 0)
                    {
                        rebackAmount(connection, t6521.F06, dbjgId);
                    }
                }
            }
            else if (t6521.F07 == FeeCode.TZ_LX || t6521.F07 == FeeCode.TZ_FX)
            {
                // 计算投资管理费
                T6238 t6238 = selectT6238(connection, t6230.F01);
                if (t6238 == null)
                {
                    throw new LogicalException("标的费率不存在");
                }
                BigDecimal fee = t6521.F06.multiply(t6238.F03).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (t6238.F03.compareTo(BigDecimal.ZERO) > 0 && fee.compareTo(BigDecimal.ZERO) > 0)
                {
                    int pid = getPTID(connection);
                    // 锁定平台往来账户
                    T6101 ptwl = selectT6101(connection, pid, T6101_F03.WLZH, true);
                    if (ptwl == null)
                    {
                        throw new LogicalException("平台往来账户不存在");
                    }
                    String feeComment = String.format("投资管理费:%s 第 %s期", t6230.F25, Integer.toString(t6521.F05));
                    {
                        // 扣减收款人管理费
                        skrAcount.F06 = skrAcount.F06.subtract(fee);
                        updateT6101(connection, skrAcount.F06, skrAcount.F01);
                        T6102 t6102 = new T6102();
                        t6102.F02 = skrAcount.F01;
                        t6102.F03 = FeeCode.GLF;
                        t6102.F04 = ptwl.F01;
                        t6102.F07 = fee;
                        t6102.F08 = skrAcount.F06;
                        t6102.F09 = feeComment;
                        insertT6102(connection, t6102);
                    }
                    {
                        // 平台收投资管理费
                        ptwl.F06 = ptwl.F06.add(fee);
                        updateT6101(connection, ptwl.F06, ptwl.F01);
                        T6102 t6102 = new T6102();
                        t6102.F02 = ptwl.F01;
                        t6102.F03 = FeeCode.GLF;
                        t6102.F04 = skrAcount.F01;
                        t6102.F06 = fee;
                        t6102.F08 = ptwl.F06;
                        t6102.F09 = feeComment;
                        insertT6102(connection, t6102);
                    }
                }
            }
            
            if (FeeCode.TZ_BJ == t6521.F07)
            {
                int remain = getWHCount(connection, t6230);
                if (remain == 0)
                {
                    updateT6252(connection, t6521);
                    updatT6230(connection, t6230.F01);
                    updateT6231(connection, t6230.F01);
                    // 根据提前还款订单，统计提前还款总额
                    List<T6521> t6521List = selectT6521List(connection, t6230.F01);
                    Map<String, Object> retMap = getT6521Sum(t6521List);
                    BigDecimal amount = (BigDecimal)retMap.get("repaySum");
                    
                    // 给借款人发站内信、短信
                    sendMsgLetter(connection,
                        t6521.F03,
                        LetterVariable.JKR_TQHK_JKHK,
                        amount,
                        t6521.F09,
                        MsgVariavle.JKR_TQHK_JKHK);
                    Map<String, BigDecimal> investorMap = (Map<String, BigDecimal>)retMap.get("investorMap");
                    Set<Map.Entry<String, BigDecimal>> entry = investorMap.entrySet();
                    Iterator<Map.Entry<String, BigDecimal>> it = entry.iterator();
                    int pId = getPTID(connection);
                    while (it.hasNext())
                    {
                        Map.Entry<String, BigDecimal> me = it.next();
                        String key = me.getKey();
                        BigDecimal value = me.getValue();
                        
                        if (pId != Integer.parseInt(key))
                        { // 如果是平台收的提前还款违约金手续费，则不需要发站内信
                            // 给投资人发站内信、短信
                            sendMsgLetter(connection,
                                t6521.F03,
                                LetterVariable.TZR_TQHK_TBSK,
                                value,
                                Integer.parseInt(key),
                                MsgVariavle.TZR_TQHK_TBSK);
                        }
                    }
                }
            }
            // 如果有需要还款的加息券数据，则更新加息券利息返还记录为失效
            updateT6289(connection, t6521);
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    /**
     * 还款归还担保方的担保额度
     * @param connection
     * @param amount
     * @param dbUserId
     * @throws Throwable
     */
    private void rebackAmount(Connection connection, BigDecimal amount, int dbUserId)
        throws Throwable
    {
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
            pstmt.setInt(2, FeeCode.DB_HK_FH);
            pstmt.setTimestamp(3, getCurrentTimestamp(connection));
            pstmt.setBigDecimal(4, amount);
            pstmt.setBigDecimal(5, dbTotalAmount.add(amount));
            pstmt.setString(6, "提前还款担保额度返还");
            pstmt.execute();
        }
    }
    
    /**
     * 是否有债权转让，有则下架
     * 
     * @param connection
     * @param t6251
     * @throws Throwable
     */
    private void offCreditor(SQLConnection connection, T6251 t6251)
        throws Throwable
    {
        if (T6251_F08.S == t6251.F08)
        {
            // 更新债权转让的状态
            T6260 t6260 = selectT6260(connection, t6251.F01);
            if (t6260 != null)
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6260 SET F06 = ?, F07 = ? WHERE T6260.F01 = ?"))
                {
                    pstmt.setTimestamp(1, getCurrentTimestamp(connection));
                    pstmt.setString(2, T6260_F07.YXJ.name());
                    pstmt.setInt(3, t6260.F01);
                    pstmt.execute();
                }
                // 发站内信
                ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
                T6110 t6110 = selectT6110(connection, t6251.F04);
                String zqComment = "提前还款，债权下架";
                Envionment envionment = configureProvider.createEnvionment();
                envionment.set("userName", t6110.F02);
                envionment.set("zqId", t6251.F02);
                String content = configureProvider.format(LetterVariable.TQHK_ZQXJ, envionment);
                sendLetter(connection, t6251.F04, zqComment, content);
            }
        }
    }
    
    /**
     * 根据标的ID和未返还状态，查询所有未返回加息券利息的记录
     * <功能详细描述>
     * @param connection
     * @param t6521
     * @return
     * @throws SQLException
     */
    protected List<T6289> getT6289s(Connection connection, T6521 t6521)
        throws SQLException
    {
        List<T6289> list = new ArrayList<T6289>();
        ;
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT F02,F12 FROM S62.T6289 WHERE T6289.F02=? AND T6289.F09=? GROUP BY F02,F12 "))
        {
            ps.setInt(1, t6521.F03);
            ps.setString(2, T6289_F09.WFH.name());
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    T6289 t6289 = new T6289();
                    t6289.F02 = rs.getInt(1);
                    t6289.F12 = rs.getInt(2);
                    list.add(t6289);
                }
            }
        }
        return list;
    }
    
    /**
     * 统计借款人、投资人的还款、收款总额
     * 
     * @param t6521List
     * @return
     * @throws Throwable
     */
    private Map<String, Object> getT6521Sum(List<T6521> t6521List)
        throws Throwable
    {
        // 还款总金额
        BigDecimal repaySum = BigDecimal.ZERO;
        Map<String, Object> retMap = new HashMap<>();
        Map<String, BigDecimal> investorMap = new HashMap<>();
        for (int i = 0; i < t6521List.size(); i++)
        {
            T6521 t6521 = t6521List.get(i);
            repaySum = repaySum.add(t6521.F06);
            String investorId = String.valueOf(t6521.F02);
            BigDecimal investmentAmount = investorMap.get(investorId);
            if (investmentAmount != null)
            {
                investorMap.remove(investorId);
                investorMap.put(investorId, investmentAmount.add(t6521.F06));
            }
            else
            {
                investorMap.put(investorId, t6521.F06);
            }
        }
        retMap.put("repaySum", repaySum);
        retMap.put("investorMap", investorMap);
        return retMap;
    }
    
    private void updatT6230(SQLConnection connection, int loanId)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6230 SET F20 = 'YJQ' WHERE F01 = ?"))
        {
            pstmt.setInt(1, loanId);
            pstmt.execute();
        }
    }
    
    private void updateT6252(SQLConnection connection, T6521 t6521)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6252 SET F07 = 0, F09 = ?, F10 = ? WHERE F02 = ? AND F06 > ? AND F05 = ? "))
        {
            pstmt.setString(1, T6252_F09.TQH.name());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, t6521.F03);
            pstmt.setInt(4, t6521.F08);
            pstmt.setInt(5, FeeCode.TZ_LX);
            pstmt.execute();
        }
    }
    
    /**
     * 获取指定标未还的数量
     * 
     * @param connection
     * @param t6230
     * @return
     * @throws SQLException
     */
    private int getWHCount(SQLConnection connection, T6230 t6230)
        throws SQLException
    {
        // 查询该标是否还完
        int remain = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(*) FROM S62.T6252 WHERE F02 = ? AND F05 = ? AND F09 IN (?,?) "))
        {
            pstmt.setInt(1, t6230.F01);
            pstmt.setInt(2, FeeCode.TZ_BJ);
            pstmt.setString(3, T6252_F09.WH.name());
            pstmt.setString(4, T6252_F09.HKZ.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    remain = resultSet.getInt(1);
                }
            }
        }
        return remain;
    }
    
    /**
     * 发站内信、短信
     * 
     * @param connection
     * @param loanId
     * @param letterVariable
     * @param amount
     * @param userId
     * @throws Throwable
     */
    protected void sendMsgLetter(SQLConnection connection, int loanId, LetterVariable letterVariable,
        BigDecimal amount, int userId, MsgVariavle msgVariable)
        throws Throwable
    {
        // 发站内信
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        T6110 t6110 = selectT6110(connection, userId);
        String amountStr = Formater.formatAmount(amount);
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("userName", t6110.F02);
        envionment.set("title", selectString(connection, loanId));
        envionment.set("amount", amountStr);
        String content = configureProvider.format(letterVariable, envionment);
        sendLetter(connection, userId, "散标提前还款", content);
        
        String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
        if ("1".equals(isUseYtx))
        {
            SMSUtils smsUtils = new SMSUtils(configureProvider);
            int type = smsUtils.getTempleId(msgVariable.getDescription());
            sendMsg(connection,
                t6110.F04,
                smsUtils.getSendContent(DateTimeParser.format(new java.util.Date()), envionment.get("amount")),
                type);
        }
        else
        {
            String msgContent = configureProvider.format(msgVariable, envionment);
            sendMsg(connection, t6110.F04, msgContent);
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
    
    private void updateT6251(Connection connection, BigDecimal F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6251 SET F07 = ?, F08 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setString(2, T6251_F08.F.name());
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    private T6251 selectT6251(Connection connection, int F01)
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
    
    private String selectString(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F03 FROM S62.T6230 WHERE T6230.F01 = ? LIMIT 1"))
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
    
    private void updateT6252(Connection connection, int F05, int F06, int F11)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6252 SET F09 = ?, F10 = ? WHERE F05 = ? AND F06 = ? AND F11 = ?"))
        {
            pstmt.setString(1, T6252_F09.YH.name());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F05);
            pstmt.setInt(4, F06);
            pstmt.setInt(5, F11);
            pstmt.execute();
        }
    }
    
    private int insertT6252(Connection connection, T6252_F09 F09, T6521 t6521)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6252 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, t6521.F03);
            pstmt.setInt(2, t6521.F09);
            pstmt.setInt(3, t6521.F02);
            pstmt.setInt(4, t6521.F07);
            pstmt.setInt(5, t6521.F05);
            pstmt.setBigDecimal(6, t6521.F06);
            pstmt.setTimestamp(7, getCurrentTimestamp(connection));
            pstmt.setString(8, F09.name());
            pstmt.setTimestamp(9, getCurrentTimestamp(connection));
            pstmt.setInt(10, t6521.F04);
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
    
    protected T6252 selectT6252(Connection connection, int F05, int F06, int F11)
        throws SQLException
    {
        T6252 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6252 WHERE T6252.F05 = ? AND T6252.F06 = ? AND T6252.F11 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F05);
            pstmt.setInt(2, F06);
            pstmt.setInt(3, F11);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6252();
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
                }
            }
        }
        return record;
    }
    
    protected T6521 selectT6521(Connection connection, int F01)
        throws SQLException
    {
        T6521 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S65.T6521 WHERE T6521.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6521();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getInt(9);
                }
            }
        }
        return record;
    }
    
    /**
     * 获取提前还款的所有数据
     * 
     * @param connection
     * @param bidId
     * @return
     * @throws SQLException
     */
    protected List<T6521> selectT6521List(Connection connection, int bidId)
        throws SQLException
    {
        List<T6521> t6521List = new ArrayList<>();
        T6521 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("select s.* from(SELECT * from S65.T6521 ORDER BY F01 desc ) s where s.F03 = ? GROUP BY s.F03,s.F04,s.F05,s.F07"))
        {
            pstmt.setInt(1, bidId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    record = new T6521();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getInt(9);
                    t6521List.add(record);
                }
            }
        }
        return t6521List;
    }
    
    protected T6253 selectT6253(Connection connection, int loanId)
        throws SQLException
    {
        T6253 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6253 WHERE T6253.F02 = ? LIMIT 1"))
        {
            pstmt.setInt(1, loanId);
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
    
    protected T6230 selectT6230(Connection connection, int F01)
        throws SQLException
    {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F27 FROM S62.T6230 WHERE T6230.F01 = ? FOR UPDATE"))
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
    
    protected T6238 selectT6238(Connection connection, int F01)
        throws SQLException
    {
        T6238 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6238 WHERE T6238.F01 = ? "))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6238();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getBigDecimal(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                }
            }
        }
        return record;
    }
    
    private void updateT6231(Connection connection, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6231 SET F03 = 0, F13 = ? WHERE F01 = ?"))
        {
            pstmt.setTimestamp(1, getCurrentTimestamp(connection));
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    protected T6260 selectT6260(Connection connection, int F02)
        throws Throwable
    {
        T6260 t6260 = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6260.F01 AS F01, T6260.F02 AS F02, T6260.F03 AS F03, T6260.F04 AS F04, T6260.F05 AS F05, T6260.F06 AS F06, T6260.F07 AS F07, T6260.F08 AS F08"
                + " FROM S62.T6251,S62.T6260 WHERE T6260.F02 = ? AND T6260.F07 != ? FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, T6260_F07.YJS.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    t6260 = new T6260();
                    t6260.F01 = resultSet.getInt(1);
                    t6260.F02 = resultSet.getInt(2);
                    t6260.F03 = resultSet.getBigDecimal(3);
                    t6260.F04 = resultSet.getBigDecimal(4);
                    t6260.F05 = resultSet.getTimestamp(5);
                    t6260.F06 = resultSet.getDate(6);
                    t6260.F07 = T6260_F07.parse(resultSet.getString(8));
                    t6260.F08 = resultSet.getBigDecimal(8);
                }
            }
        }
        return t6260;
    }
    
    /**
     * 如果有需要还款的加息券数据,则更新加息券利息返还记录为失效
     * 
     * @param connection
     * @param t6521
     * @throws Throwable
     */
    protected void updateT6289(Connection connection, T6521 t6521)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6289 SET T6289.F09 = ?, T6289.F10 = ? WHERE T6289.F06 > ? AND T6289.F02 = ? AND T6289.F09 = ?"))
        {
            pstmt.setString(1, T6289_F09.YSX.name());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, t6521.F05);
            pstmt.setInt(4, t6521.F03);
            pstmt.setString(5, T6289_F09.WFH.name());
            pstmt.execute();
        }
    }
    
    /**
     * 根据标还款订单查询是否有加息券还款订单
     * 
     * @param paymentOrderId
     * @return
     * @throws Throwable
     */
    protected T6525 getT6525(SQLConnection connection, int paymentOrderId)
        throws Throwable
    {
        T6525 t6525 = null;
        try (PreparedStatement pstm =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S65.T6525 WHERE F09 = ? FOR UPDATE"))
        {
            pstm.setInt(1, paymentOrderId);
            try (ResultSet resultSet = pstm.executeQuery())
            {
                if (resultSet.next())
                {
                    t6525 = new T6525();
                    t6525.F01 = resultSet.getInt(1);
                    t6525.F02 = resultSet.getInt(2);
                    t6525.F03 = resultSet.getInt(3);
                    t6525.F04 = resultSet.getInt(4);
                    t6525.F05 = resultSet.getBigDecimal(5);
                    t6525.F06 = resultSet.getInt(6);
                    t6525.F07 = resultSet.getInt(7);
                    t6525.F08 = resultSet.getInt(8);
                    t6525.F09 = resultSet.getInt(9);
                }
            }
        }
        return t6525;
    }
    
    @Override
    protected void updateSubmit(Connection connection, T6501_F03 t6501_F03, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F05 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, t6501_F03.name());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    private String selectSubject(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S51.T5122 WHERE T5122.F01 = ? LIMIT 1"))
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
}