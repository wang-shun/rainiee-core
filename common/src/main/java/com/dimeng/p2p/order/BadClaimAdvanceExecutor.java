package com.dimeng.p2p.order;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.S51.enums.T5122_F03;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.*;
import com.dimeng.p2p.S62.enums.*;
import com.dimeng.p2p.S65.entities.T6529;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6529_F08;
import com.dimeng.p2p.XyType;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.variables.defines.BadClaimVariavle;
import com.dimeng.p2p.variables.defines.EmailVariavle;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.StringHelper;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * 购买不良债权付款执行器
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月17日]
 */
@ResourceAnnotation
public class BadClaimAdvanceExecutor extends AbstractOrderExecutor
{
    
    public BadClaimAdvanceExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return BadClaimAdvanceExecutor.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try
        {
            //债权价值
            BigDecimal creditPrice = new BigDecimal(params.get("creditPrice"));
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            T6529 t6529 = selectT6529(connection, orderId);
            
            if (t6529 == null)
            {
                logger.info("订单不存在：" + orderId);
                return;
            }
            if (t6529.F08 == T6529_F08.S)
            {
                logger.info("该订单已完成：" + orderId);
                return;
            }
            
            T6251 t6251 = selectT6251(connection, t6529.F05);
            
            if (t6251 == null)
            {
                throw new LogicalException("债权不存在");
            }
            
            //锁定不良债权转让申请记录
            T6264 zqsq = selectT6264(connection, t6529.F03);
            
            if (zqsq == null)
            {
                throw new LogicalException("不良债权转让申请不存在");
            }
            
            if (zqsq.F04 != T6264_F04.ZRZ)
            {
                throw new ParameterException("不良债权不是转让中状态，不能进行购买！");
            }
            
            T6230 t6230 = selectT6230(connection, t6251.F03);
            if (t6230 == null)
            {
                throw new LogicalException("借款标不存在");
            }
            if (t6230.F20 != T6230_F20.HKZ)
            {
                throw new LogicalException("借款标不是还款中状态，不能进行购买操作。");
            }
            
            //校验标的是否已被担保方垫付
            if (!checkBidAdvance(t6251.F03))
            {
                throw new LogicalException("标的已被担保方垫付,不能进行购买操作！");
            }
            
            T6101 gmrzh = selectT6101(connection, t6529.F04, T6101_F03.FXBZJZH, true);
            if (gmrzh == null)
            {
                throw new LogicalException("购买人风险保证金账户不存在");
            }
            T6101 tzrzh = selectT6101(connection, t6251.F04, T6101_F03.WLZH, true);
            if (tzrzh == null)
            {
                throw new LogicalException("投资人往来账户不存在");
            }
            if (gmrzh.F06.compareTo(t6529.F06) < 0)
            {
                throw new LogicalException("风险保证金账户余额不足，不能进行不良债权购买！");
            }
            // 插入不良债权转让记录
            {
                T6265 t6265 = selectT6265(connection, t6529.F03);
                int zqzrId = 0;
                if (t6265 == null)
                {
                    T6265 iT6265 = new T6265();
                    iT6265.F02 = t6529.F03;
                    iT6265.F03 = t6529.F04;
                    iT6265.F04 = t6230.F02;
                    iT6265.F05 = creditPrice;
                    iT6265.F06 = t6529.F06;
                    zqzrId = insertT6265(connection, iT6265);
                }
                else
                {
                    updateT6265(connection, t6529.F06, t6265.F01);
                    zqzrId = t6265.F01;
                }
                
                //插入不良债权转让明细
                T6266 t6266 = new T6266();
                t6266.F02 = zqzrId;
                t6266.F03 = t6529.F06;
                t6266.F04 = t6251.F04;
                t6266.F05 = t6529.F07;
                t6266.F06 = t6251.F01;
                insertT6266(connection, t6266);
                
            }
            // 扣除购买人账户金额，并插流水
            if (gmrzh.F06.compareTo(t6529.F06) < 0)
            {
                throw new LogicalException("购买人风险保证金账户余额不足");
            }
            {
                gmrzh.F06 = gmrzh.F06.subtract(t6529.F06);
                updateT6101(connection, gmrzh.F06, gmrzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = gmrzh.F01;
                t6102.F03 = FeeCode.BLZQ_GM;
                t6102.F04 = tzrzh.F01;
                t6102.F07 = t6529.F06;
                t6102.F08 = gmrzh.F06;
                t6102.F09 = String.format("不良债权转让:%s，标题:%s", zqsq.F02, t6230.F03);
                t6102.F12 = t6230.F01;
                insertT6102(connection, t6102);
            }
            // 增加投资人账户金额，并插流水
            {
                tzrzh.F06 = tzrzh.F06.add(t6529.F06);
                updateT6101(connection, tzrzh.F06, tzrzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = tzrzh.F01;
                t6102.F03 = t6529.F07;
                t6102.F04 = gmrzh.F01;
                t6102.F06 = t6529.F06;
                t6102.F08 = tzrzh.F06;
                t6102.F09 = String.format("不良债权转让:%s，标题:%s", t6230.F25, t6230.F03);
                t6102.F12 = t6230.F01;
                insertT6102(connection, t6102);
            }
            if (t6529.F07 == FeeCode.TZ_BJ)
            {
                // 是否有债权转让，有则下架
                offCreditor(connection, t6251);
                updateT6251(connection, t6529.F05);
            }
            //增加购买机构的担保额度
            {
                /*boolean isHasGuarant =
                    BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));*/
                if (FeeCode.TZ_BJ == t6529.F07)
                {
                    rebackAmount(connection, t6529.F06, gmrzh.F02);
                }
            }
            
            {
                //当前时间
                Timestamp currentTime = getCurrentTimestamp(connection);
                // 更新转让订单状态
                try (PreparedStatement ps = connection.prepareStatement("UPDATE S65.T6529 SET F08=? WHERE F01=?"))
                {
                    ps.setString(1, T6529_F08.S.name());
                    ps.setInt(2, orderId);
                    ps.executeUpdate();
                }
                
                // 将还款计划状态改为已还
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6252 SET  F09 = ?,F10 = ? WHERE F02 = ?  AND F09 = ? AND F05 = ? AND F11 = ? "))
                {
                    pstmt.setString(1, T6252_F09.YH.name());
                    pstmt.setTimestamp(2, currentTime);
                    pstmt.setInt(3, t6529.F02);
                    pstmt.setString(4, T6252_F09.WH.name());
                    pstmt.setInt(5, t6529.F07);
                    pstmt.setInt(6, t6529.F05);
                    pstmt.execute();
                }
                
                //不良债权订单数量
                int orderIdNum = 0;
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT COUNT(1) FROM (SELECT F01 FROM S65.T6529 WHERE F02=?  AND F04=? AND F03 = ? GROUP BY F03,F04,F05,F07) t"))
                {
                    ps.setInt(1, t6529.F02);
                    ps.setInt(2, t6529.F04);
                    ps.setInt(3, t6529.F03);
                    
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            orderIdNum = rs.getInt(1);
                        }
                    }
                }
                
                // 已完成订单数量
                int num = 0;
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT COUNT(F01) FROM S65.T6529 WHERE F02=? AND F08=? AND F04=?  AND F03 = ? "))
                {
                    ps.setInt(1, t6529.F02);
                    ps.setString(2, T6529_F08.S.name());
                    ps.setInt(3, t6529.F04);
                    ps.setInt(4, t6529.F03);
                    
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            num = rs.getInt(1);
                        }
                    }
                }
                
                if (num == orderIdNum)
                {
                    
                    //逾期天数
                    int overdueDays = Integer.parseInt(params.get("overdueDays"));
                    T6265 zqzrjl = selectT6265(connection, t6529.F03);
                    T6264 t6264 = new T6264();
                    t6264.F01 = t6529.F03;
                    t6264.F04 = T6264_F04.YZR;
                    t6264.F05 = overdueDays;
                    t6264.F09 = zqzrjl.F05;
                    t6264.F10 = zqzrjl.F06;
                    //更新债权转让申请表
                    updateT6264(connection, t6264);
                    
                    if (t6230.F11 == T6230_F11.S && t6230.F12 == T6230_F12.BJQEDB)
                    {
                        // 免去借款利息罚息
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("UPDATE S62.T6252 SET  F09 = ? WHERE F02 = ?  AND F09 = ?"))
                        {
                            pstmt.setString(1, T6252_F09.DF.name());
                            pstmt.setInt(2, t6529.F02);
                            pstmt.setString(3, T6252_F09.WH.name());
                            pstmt.execute();
                        }
                    }
                    else
                    {
                        
                        // 将还款计划状态改为已还
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("UPDATE S62.T6252 SET  F09 = ?,F10 = ? WHERE F02 = ?  AND F09 = ?"))
                        {
                            pstmt.setString(1, T6252_F09.YH.name());
                            pstmt.setTimestamp(2, currentTime);
                            pstmt.setInt(3, t6529.F02);
                            pstmt.setString(4, T6252_F09.WH.name());
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
                    
                    // 将标的状态改为已转让
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6230 SET  F20 = ? WHERE F01 = ? "))
                    {
                        pstmt.setString(1, T6230_F20.YZR.name());
                        pstmt.setInt(2, t6529.F02);
                        pstmt.execute();
                    }
                    
                    //更新标的转让时间
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6231 SET F34 = ? WHERE F01 = ?"))
                    {
                        pstmt.setTimestamp(1, currentTime);
                        pstmt.setInt(2, t6529.F02);
                        pstmt.execute();
                    }
                    
                    //生成合同
                    createAgreement(connection, zqzrjl.F01);
                    
                    //合同保全
                    Boolean isAllowBadClaim =
                        Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_ALLOW_BADCLAIM_TRANSFER));
                    if (isAllowBadClaim)
                    {
                        //生成合同保全记录
                        createContractPreservation(connection, zqzrjl, zqsq);
                    }
                    
                    // 发站内信、短信、邮件给借款人
                    T6110 jkrt6110 = selectT6110(connection, t6230.F02);
                    
                    Envionment envionment = configureProvider.createEnvionment();
                    envionment.set("bid", t6230.F03);
                    envionment.set("amount", t6230.F05.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    envionment.set("creditPrice", zqzrjl.F05.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    envionment.set("subscribePrice", zqzrjl.F06.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    String content = configureProvider.format(LetterVariable.JKR_BLZQZR, envionment);
                    sendLetter(connection, t6230.F02, "不良债权转让", content);
                    
                    String cont = configureProvider.format(EmailVariavle.JKR_BLZQZR, envionment);
                    sendEmail(connection, "不良债权转让", cont, jkrt6110.F05);
                    
                    String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
                    if ("1".equals(isUseYtx))
                    {
                        SMSUtils smsUtils = new SMSUtils(configureProvider);
                        int type = smsUtils.getTempleId(MsgVariavle.JKR_BLZQZR.getDescription());
                        sendMsg(connection,
                            jkrt6110.F04,
                            smsUtils.getSendContent(envionment.get("bid"),
                                envionment.get("creditPrice"),
                                envionment.get("subscribePrice")),
                            type);
                    }
                    else
                    {
                        String msgContent = configureProvider.format(MsgVariavle.JKR_BLZQZR, envionment);
                        sendMsg(connection, jkrt6110.F04, msgContent);
                    }
                    
                    // 发站内信、短信、邮件给购买人
                    T6110 gmrt6110 = selectT6110(connection, zqzrjl.F03);
                    
                    Envionment gmrEnvionment = configureProvider.createEnvionment();
                    gmrEnvionment.set("title", t6230.F03);
                    gmrEnvionment.set("money", zqzrjl.F06.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    String gmContent = configureProvider.format(LetterVariable.BLZQZR_BUY, gmrEnvionment);
                    sendLetter(connection, zqzrjl.F03, "购买不良债权", gmContent);
                    
                    String gmcont = configureProvider.format(EmailVariavle.BLZQZR_BUY, gmrEnvionment);
                    sendEmail(connection, "购买不良债权", gmcont, gmrt6110.F05);
                    
                    String isUseYtxGmr = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
                    if ("1".equals(isUseYtxGmr))
                    {
                        SMSUtils smsUtils = new SMSUtils(configureProvider);
                        int type = smsUtils.getTempleId(MsgVariavle.BLZQZR_BUY.getDescription());
                        sendMsg(connection,
                            gmrt6110.F04,
                            smsUtils.getSendContent(gmrEnvionment.get("title"), gmrEnvionment.get("money")),
                            type);
                    }
                    else
                    {
                        String msgContent = configureProvider.format(MsgVariavle.BLZQZR_BUY, gmrEnvionment);
                        sendMsg(connection, gmrt6110.F04, msgContent);
                    }
                }
            }
            // 发站内信、短信、邮件给投资人
            T6110 t6110 = selectT6110(connection, t6251.F04);
            T5122 t5122 = selectT5122(connection, t6529.F07);
            
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("bid", t6230.F03);
            envionment.set("feeType", t5122.F02);
            envionment.set("amount", t6529.F06.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            String content = configureProvider.format(LetterVariable.TZR_BLZQZR, envionment);
            sendLetter(connection, t6251.F04, "不良债权转让", content);
            
            String cont = configureProvider.format(EmailVariavle.TZR_BLZQZR, envionment);
            sendEmail(connection, "不良债权转让", cont, t6110.F05);
            
            String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
            if ("1".equals(isUseYtx))
            {
                SMSUtils smsUtils = new SMSUtils(configureProvider);
                int type = smsUtils.getTempleId(MsgVariavle.TZR_BLZQZR.getDescription());
                sendMsg(connection,
                    t6110.F04,
                    smsUtils.getSendContent(envionment.get("bid"), envionment.get("feeType"), envionment.get("amount")),
                    type);
            }
            else
            {
                String msgContent = configureProvider.format(MsgVariavle.TZR_BLZQZR, envionment);
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
            pstmt.setInt(2, FeeCode.DB_BLZQGM_ZS);
            pstmt.setTimestamp(3, getCurrentTimestamp(connection));
            pstmt.setBigDecimal(4, amount);
            pstmt.setBigDecimal(5, dbTotalAmount.add(amount));
            pstmt.setString(6, "购买不良债权担保额度赠送");
            pstmt.execute();
        }
    }
    
    protected String getBatchId(String ids, List<Object> params)
    {
        if (!StringHelper.isEmpty(ids))
        {
            String[] idarr = ids.split(",");
            StringBuilder idsql = new StringBuilder();
            if (idarr.length > 0)
            {
                for (String id : idarr)
                {
                    idsql.append("?,");
                    params.add(id);
                }
                idsql = new StringBuilder(idsql.toString().substring(0, idsql.toString().length() - 1));
            }
            else
            {
                idsql.append("?");
                params.add(ids);
            }
            return idsql.toString();
        }
        return null;
    }
    
    /** 
     * 查询不良债权转让订单
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    protected T6529 selectT6529(Connection connection, int F01)
        throws SQLException
    {
        T6529 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S65.T6529 WHERE T6529.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6529();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = T6529_F08.parse(resultSet.getString(8));
                }
            }
        }
        return record;
    }
    
    /** 
     * 更新不良债权转让的认购价格
     * @param connection
     * @param F01
     * @param F02
     * @throws Throwable
     */
    private void updateT6265(Connection connection, BigDecimal F01, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6265 SET F06 =F06+ ?, F07 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    /** 
     * 更新不良债权转让申请表
     * @param connection
     * @param t6264
     * @throws Throwable
     */
    private void updateT6264(Connection connection, T6264 t6264)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6264 SET F04 = ?, F05 = ? , F08 = ?, F09 = ?, F10 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, t6264.F04.name());
            pstmt.setInt(2, t6264.F05);
            pstmt.setTimestamp(3, getCurrentTimestamp(connection));
            pstmt.setBigDecimal(4, t6264.F09);
            pstmt.setBigDecimal(5, t6264.F10);
            pstmt.setInt(6, t6264.F01);
            pstmt.execute();
        }
    }
    
    /** 
     * 查询不良债权转让申请
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    private T6264 selectT6264(Connection connection, int F02)
        throws SQLException
    {
        T6264 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S62.T6264 WHERE T6264.F01 = ?  FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6264();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = T6264_F04.parse(resultSet.getString(4));
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getTimestamp(7);
                    record.F08 = resultSet.getTimestamp(8);
                    record.F09 = resultSet.getBigDecimal(9);
                    record.F10 = resultSet.getBigDecimal(10);
                }
            }
        }
        return record;
    }
    
    /** 
     * 查询不良债权转让记录
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    private T6265 selectT6265(Connection connection, int F02)
        throws SQLException
    {
        T6265 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6265 WHERE T6265.F02 = ? LIMIT 1 FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6265();
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
    
    /**
     * 生成不良债权转让记录
     * @param connection
     * @param entity
     * @return
     * @throws Throwable
     */
    private int insertT6265(Connection connection, T6265 entity)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6265 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setBigDecimal(4, entity.F05);
            pstmt.setBigDecimal(5, entity.F06);
            pstmt.setTimestamp(6, getCurrentTimestamp(connection));
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
     * 插入不良债权转让垫付明细
     * @param connection
     * @param entity
     * @return
     * @throws Throwable
     */
    private int insertT6266(Connection connection, T6266 entity)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6266 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ? ",
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
    
    /** 
     * 查询交易类型
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
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
    
    /** 
     * 查询标的信息并锁定
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    public T6230 selectT6230(Connection connection, int F01)
        throws SQLException
    {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26 FROM S62.T6230 WHERE T6230.F01 = ? FOR UPDATE"))
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
    
    /**
     * 更新资金账户表
     * @param connection
     * @param F01
     * @param F02
     * @throws Throwable
     */
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
    
    /** 
     * 查询标的债权记录
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
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
    
    /**
     * 如果有需要还款的加息券数据,则更新加息券利息返还记录为失效
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
    
    /** 
     * 生成不良债权转让电子合同
     * @param connection
     * @param blzqzrId
     * @throws Throwable
     */
    protected void createAgreement(Connection connection, int blzqzrId)
        throws Throwable
    {
        int version = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S51.T5125 WHERE T5125.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, XyType.BLZQZRXY);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    version = resultSet.getInt(1);
                }
            }
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6267 (F01, F02) VALUES (?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02)"))
        {
            pstmt.setInt(1, blzqzrId);
            pstmt.setInt(2, version);
            pstmt.execute();
        }
        logger.info("创建电子合同成功！");
    }
    
    /** 
     * <创建合同保全>
     * @param connection
     * @param t6265
     * @param t6264
     * @throws Throwable
     */
    private void createContractPreservation(Connection connection, T6265 t6265, T6264 t6264)
        throws Throwable
    {
        //生成购买人合同保全记录
        T6271 t6271 = new T6271();
        t6271.F02 = t6265.F03;
        t6271.F03 = t6264.F03;
        t6271.F07 = T6271_F07.WBQ;
        t6271.F08 = T6271_F08.BLZQZRHT;
        t6271.F10 = T6271_F10.SRR;
        t6271.F12 = t6265.F01;
        insertT6271(connection, t6271);
        
        //生成投资人合同保全记录
        List<T6266> t6266s = selectT6266(connection, t6265.F01);
        if (t6266s.size() > 0)
        {
            for (T6266 t6266 : t6266s)
            {
                T6271 zrrt6271 = new T6271();
                zrrt6271.F02 = t6266.F04;
                zrrt6271.F03 = t6264.F03;
                zrrt6271.F07 = T6271_F07.WBQ;
                zrrt6271.F08 = T6271_F08.BLZQZRHT;
                zrrt6271.F10 = T6271_F10.ZCR;
                zrrt6271.F11 = t6266.F06;
                zrrt6271.F12 = t6265.F01;
                insertT6271(connection, zrrt6271);
            }
        }
        logger.info("创建合同保全记录成功！");
    }
    
    /** 
     * 插入合同保全记录
     * @param t6271
     * @throws Throwable
     */
    private void insertT6271(Connection connection, T6271 t6271)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder("INSERT INTO S62.T6271 SET F02 = ?,F03 = ?,F07 = ?,F08 = ?,F10 = ?,F12 = ?");
        if (t6271.F11 > 0)
        {
            sql.append(",F11 = ?");
        }
        try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
        {
            ps.setInt(1, t6271.F02);
            ps.setInt(2, t6271.F03);
            ps.setString(3, t6271.F07.name());
            ps.setString(4, t6271.F08.name());
            ps.setString(5, t6271.F10.name());
            ps.setInt(6, t6271.F12);
            if (t6271.F11 > 0)
            {
                ps.setInt(7, t6271.F11);
            }
            ps.execute();
        }
    }
    
    /** 
     * 分组查询债权转让明细
     * @param connection
     * @param F02
     * @return
     * @throws Throwable
     */
    private List<T6266> selectT6266(Connection connection, int F02)
        throws Throwable
    {
        List<T6266> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S62.T6266 WHERE T6266.F02 = ? GROUP BY T6266.F06"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6266 record = new T6266();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return list;
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
                String zqComment = "不良债权转让，债权下架";
                Envionment envionment = configureProvider.createEnvionment();
                envionment.set("userName", t6110.F02);
                envionment.set("zqId", t6251.F02);
                String content = configureProvider.format(LetterVariable.BLZQZR_ZQXJ, envionment);
                sendLetter(connection, t6251.F04, zqComment, content);
            }
        }
    }
    
    private void updateT6251(Connection connection, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, T6251_F08.F.name());
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    /** 
     * <获取债权转让申请信息>
     * @param connection
     * @param F02
     * @return
     * @throws Throwable
     */
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
     * 校验标的是否已被担保方垫付
     * @param loanId
     * @return
     * @throws Throwable
     */
    private boolean checkBidAdvance(int loanId)
        throws Throwable
    {
        try (final Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6514.F04 FROM S65.T6514 LEFT JOIN S65.T6501 ON T6501.F01 = T6514.F01 WHERE T6514.F02 = ?  AND T6501.F02 = ? AND T6501.F03 = ? "))
            {
                pstmt.setInt(1, loanId);
                pstmt.setInt(2, OrderType.ADVANCE.orderType());
                pstmt.setString(3, T6501_F03.CG.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
