package com.dimeng.p2p.account.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S50.entities.T5023;
import com.dimeng.p2p.S50.enums.T5023_F02;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6118;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.S61.enums.T6118_F05;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S61.enums.T6130_F16;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S71.enums.T7150_F05;
import com.dimeng.p2p.account.user.service.TxManage;
import com.dimeng.p2p.account.user.service.entity.BankCard;
import com.dimeng.p2p.account.user.service.entity.Order;
import com.dimeng.p2p.account.user.service.entity.OrderXxcz;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

public class TxManageImpl extends AbstractAccountService implements TxManage
{
    
    public TxManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public BankCard[] bankCards()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectAll(connection,
                new ArrayParser<BankCard>()
                {
                    @Override
                    public BankCard[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        List<BankCard> bankCards = null;
                        while (resultSet.next())
                        {
                            if (bankCards == null)
                            {
                                bankCards = new ArrayList<>();
                            }
                            BankCard bankCard = new BankCard();
                            bankCard.id = resultSet.getInt(1);
                            bankCard.BankID = resultSet.getInt(2);
                            bankCard.BankNumber = resultSet.getString(3);
                            bankCard.Bankname = resultSet.getString(4);
                            bankCards.add(bankCard);
                        }
                        return bankCards == null ? null : bankCards.toArray(new BankCard[bankCards.size()]);
                    }
                },
                "SELECT T6114.F01 AS F01,T6114.F03 AS F02,T6114.F06 AS F03,T5020.F02 AS F04 FROM S61.T6114 INNER JOIN S50.T5020 ON T6114.F03 = T5020.F01 WHERE T6114.F02 = ? AND T6114.F08 = ?",
                serviceResource.getSession().getAccountId(),
                BankCardStatus.QY);
        }
    }
    
    @Override
    public int withdraw(BigDecimal funds, String withdrawPsd, int cardId, T6101_F03 f03, BigDecimal poundage,
        boolean txkcfs)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        BigDecimal min = new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_MIN_FUNDS));
        BigDecimal max = new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_FUNDS));
        BigDecimal zero = new BigDecimal(0);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (funds.compareTo(min) < 0 || funds.compareTo(max) > 0 || funds.compareTo(zero) <= 0)
        {
            throw new LogicalException("提现金额不能低于" + min + "元，不能高于" + max + "元");
        }
        int accountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            int count = psdInputCount(connection);
            int maxCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
            if (count >= maxCount)
            {
                throw new LogicalException("您今日交易密码输入错误已到最大次数，请改日再试!");
            }
            // 校验身份认证、手机、交易密码是否都有设置
            checkIdentityOrPhoneOrTradingPsw(connection);
            if (isYuqi(connection, accountId))
            {
                throw new LogicalException("您有逾期未还的借款，请先还完再操作。");
            }
            
            try
            {
                
                boolean aa = false;// 标记交易密码是否正确
                if (isOpenWithPsd)
                {
                    try (PreparedStatement ps =
                        connection.prepareStatement("SELECT F01 FROM S61.T6118 WHERE F01=? AND F08=?"))
                    {
                        ps.setInt(1, accountId);
                        ps.setString(2, UnixCrypt.crypt(withdrawPsd, DigestUtils.sha256Hex(withdrawPsd)));
                        try (ResultSet resultSet = ps.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                aa = true;
                            }
                        }
                    }
                }
                serviceResource.openTransactions(connection);
                if (!aa && isOpenWithPsd)
                {
                    addInputCount(connection);
                    serviceResource.commit(connection);
                    String errorMsg = null;
                    if (count + 1 >= maxCount)
                    {
                        errorMsg = "您今日交易密码输入错误已到最大次数，请改日再试!";
                    }
                    else
                    {
                        StringBuilder builder = new StringBuilder("交易密码错误,您最多还可以输入");
                        builder.append(maxCount - (count + 1));
                        builder.append("次！");
                        errorMsg = builder.toString();
                    }
                    throw new LogicalException(errorMsg);
                }
                
                String cardNumber = null;
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F06 FROM S61.T6114 WHERE F01=? AND F02=?"))
                {
                    ps.setInt(1, cardId);
                    ps.setInt(2, accountId);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            cardNumber = resultSet.getString(1);
                        }
                    }
                }
                if (StringHelper.isEmpty(cardNumber))
                {
                    throw new LogicalException("银行卡不存在！");
                }
                
                // 提现手续费扣除方式
                // boolean txkcfs =
                // Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.TXSXF_KCFS));
                BigDecimal amount = BigDecimal.ZERO;// 提现应付金额
                BigDecimal money = new BigDecimal(configureProvider.format(SystemVariable.WITHDRAW_LIMIT_FUNDS));
                if (txkcfs)
                {
                    amount = funds;
                    funds = funds.subtract(poundage);
                    if (amount.doubleValue() < poundage.doubleValue())
                    {
                        throw new LogicalException("提现金额不能小于提现手续费！");
                    }
                }
                else
                {
                    amount = funds.add(poundage);
                    money = money.add(poundage);
                }
                
                // 往来账户
                T6101 wlzh = lock(connection, accountId, T6101_F03.WLZH);
                if (amount.compareTo(wlzh.F06) > 0)
                {
                    throw new LogicalException("账户余额不足！");
                }
                /*
                 * if (!txkcfs) {//如果是外扣方式,则审核金额需要+手续费. money =
                 * money.add(poundage); }
                 */
                // 锁定账户
                int id = 0;
                T6101 sdzh = lock(connection, accountId, T6101_F03.SDZH);
                String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
                
                if (amount.doubleValue() > money.doubleValue() || money.doubleValue() <= 0 || "ALLINPAY".equals(escrow))
                {
                    id =
                        checkWithdraw(connection,
                            accountId,
                            funds,
                            amount,
                            cardId,
                            poundage,
                            wlzh,
                            sdzh,
                            zero,
                            configureProvider);
                }
                
                else
                {
                    id =
                        uncheckWithdraw(connection,
                            accountId,
                            funds,
                            amount,
                            cardId,
                            poundage,
                            wlzh,
                            sdzh,
                            zero,
                            configureProvider);
                }
                serviceResource.commit(connection);
                return id;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * 提现不需要审核 <功能详细描述>
     * 
     * @param connection
     * @param accountId
     * @param funds
     * @param amount
     * @param cardId
     * @param poundage
     * @param wlzh
     * @param sdzh
     * @param zero
     * @param configureProvider
     * @return
     * @throws Throwable
     */
    private int uncheckWithdraw(Connection connection, int accountId, BigDecimal funds, BigDecimal amount, int cardId,
        BigDecimal poundage, T6101 wlzh, T6101 sdzh, BigDecimal zero, ConfigureProvider configureProvider)
        throws Throwable
    {
        int id = 0;
        String insertT6130Sql =
            "INSERT INTO S61.T6130(F02,F03,F04,F06,F07,F08,F09,F14,F16,F17,F18) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        Timestamp timestamp = getCurrentTimestamp(connection);
        try (PreparedStatement ps =
            connection.prepareStatement(insertT6130Sql, PreparedStatement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, accountId);
            ps.setInt(2, cardId);
            ps.setBigDecimal(3, funds);
            ps.setBigDecimal(4, poundage);
            ps.setBigDecimal(5, poundage);
            ps.setTimestamp(6, timestamp);
            ps.setString(7, T6130_F09.YFK.toString());
            ps.setTimestamp(8, timestamp);
            ps.setString(9, T6130_F16.S.name());
            Map<String, Object> result = getEmpInfo(accountId, connection);
            ps.setString(10, (String)result.get("empNum"));
            ps.setString(11, (String)result.get("empStatus"));
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys())
            {
                if (rs.next())
                {
                    id = rs.getInt(1);
                }
            }
        }
        
        try (PreparedStatement ps =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 - ?, F07 = ?  WHERE F01 = ?"))
        {
            ps.setBigDecimal(1, amount);
            ps.setTimestamp(2, timestamp);
            ps.setInt(3, wlzh.F01);
            ps.executeUpdate();
        }
        {
            // 往来账户流水
            T6102 t6102 = new T6102();
            t6102.F02 = wlzh.F01;
            t6102.F03 = FeeCode.TX;
            t6102.F04 = sdzh.F01;
            t6102.F07 = funds;
            wlzh.F06 = wlzh.F06.subtract(funds);
            t6102.F08 = wlzh.F06;
            t6102.F09 = "提现金额";
            insertT6102(connection, t6102);
        }
        if (poundage.compareTo(zero) > 0)
        {
            // 往来账户流水
            T6102 t6102 = new T6102();
            t6102.F02 = wlzh.F01;
            t6102.F03 = FeeCode.TX_SXF;
            t6102.F04 = sdzh.F01;
            t6102.F07 = poundage;
            wlzh.F06 = wlzh.F06.subtract(poundage);
            t6102.F08 = wlzh.F06;
            t6102.F09 = "提现手续费";
            insertT6102(connection, t6102);
        }
        
        // 订单ID
        /*int orderId = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?,F03 = ?, F04 = ?,F05 = ?,F06 = ?, F07 = ?, F09 = ?, F08 = ? ",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, OrderType.WITHDRAW.orderType());
            pstmt.setString(2, T6501_F03.CG.name());
            pstmt.setTimestamp(3, timestamp);
            pstmt.setTimestamp(4, timestamp);
            pstmt.setTimestamp(5, timestamp);
            pstmt.setString(6, T6501_F07.HT.name());
            pstmt.setInt(7, 0);
            pstmt.setInt(8, accountId);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    orderId = resultSet.getInt(1);
                }
            }
        }*/
        
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.WITHDRAW.orderType();
        t6501.F03 = T6501_F03.CG;
        t6501.F04 = timestamp;
        t6501.F05 = timestamp;
        t6501.F06 = timestamp;
        t6501.F07 = T6501_F07.HT;
        t6501.F08 = accountId;
        t6501.F09 = 0;
        t6501.F13 = funds;
        int orderId = insertT6501(connection, t6501);
        
        String carNum = "";
        try (PreparedStatement ps = connection.prepareStatement("SELECT F07 FROM S61.T6114 WHERE F01=? AND F02=?"))
        {
            ps.setInt(1, cardId);
            ps.setInt(2, accountId);
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    carNum = resultSet.getString(1);
                }
            }
        }
        if (StringHelper.isEmpty(carNum))
        {
            throw new LogicalException("银行卡不存在");
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6503 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?,F09 = ? "))
        {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, accountId);
            pstmt.setBigDecimal(3, funds);
            pstmt.setBigDecimal(4, poundage);
            pstmt.setBigDecimal(5, poundage);
            pstmt.setString(6, StringHelper.decode(carNum));
            pstmt.setInt(7, id);
            pstmt.execute();
        }
        
        T6110 t6110 = selectT6110(connection, accountId);
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("name", t6110.F02);
        envionment.set("datetime", DateTimeParser.format(new Date()));
        envionment.set("amount", funds.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        envionment.set("poundage", poundage.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        String content = configureProvider.format(LetterVariable.TX_CG, envionment);
        sendLetter(connection, accountId, "提现成功", content);
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
        return id;
        
    }
    
    /**
     * 提现需要审核 <功能详细描述>
     * 
     * @param connection
     * @param accountId
     * @param funds
     * @param amount
     * @param cardId
     * @param poundage
     * @param wlzh
     * @param sdzh
     * @param zero
     * @param configureProvider
     * @return
     * @throws Throwable
     */
    private int checkWithdraw(Connection connection, int accountId, BigDecimal funds, BigDecimal amount, int cardId,
        BigDecimal poundage, T6101 wlzh, T6101 sdzh, BigDecimal zero, ConfigureProvider configureProvider)
        throws Throwable
    {
        int id = 0;
        String insertT6130Sql =
            "INSERT INTO S61.T6130(F02,F03,F04,F06,F07,F08,F09,F16,F17,F18) VALUES(?,?,?,?,?,?,?,?,?,?)";
        Timestamp timestamp = getCurrentTimestamp(connection);
        try (PreparedStatement ps =
            connection.prepareStatement(insertT6130Sql, PreparedStatement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, accountId);
            ps.setInt(2, cardId);
            ps.setBigDecimal(3, funds);
            ps.setBigDecimal(4, poundage);
            ps.setBigDecimal(5, poundage);
            ps.setTimestamp(6, timestamp);
            ps.setString(7, T6130_F09.DSH.toString());
            ps.setString(8, T6130_F16.F.name());
            Map<String, Object> result = getEmpInfo(accountId, connection);
            ps.setString(9, (String)result.get("empNum"));
            ps.setString(10, (String)result.get("empStatus"));
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys())
            {
                if (rs.next())
                {
                    id = rs.getInt(1);
                }
            }
        }
        
        try (PreparedStatement ps =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 - ?, F07 = ?  WHERE F01 = ?"))
        {
            ps.setBigDecimal(1, amount);
            ps.setTimestamp(2, timestamp);
            ps.setInt(3, wlzh.F01);
            ps.executeUpdate();
        }
        {
            // 往来账户流水
            T6102 t6102 = new T6102();
            t6102.F02 = wlzh.F01;
            t6102.F03 = FeeCode.TX;
            t6102.F04 = sdzh.F01;
            t6102.F07 = funds;
            wlzh.F06 = wlzh.F06.subtract(funds);
            t6102.F08 = wlzh.F06;
            t6102.F09 = "申请提现成功";
            insertT6102(connection, t6102);
        }
        if (poundage.compareTo(zero) > 0)
        {
            // 往来账户流水
            T6102 t6102 = new T6102();
            t6102.F02 = wlzh.F01;
            t6102.F03 = FeeCode.TX_SXF;
            t6102.F04 = sdzh.F01;
            t6102.F07 = poundage;
            wlzh.F06 = wlzh.F06.subtract(poundage);
            t6102.F08 = wlzh.F06;
            t6102.F09 = "申请提现成功";
            insertT6102(connection, t6102);
        }
        try (PreparedStatement ps =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = ? WHERE F01 = ? "))
        {
            ps.setBigDecimal(1, amount);
            ps.setTimestamp(2, timestamp);
            ps.setInt(3, sdzh.F01);
            ps.executeUpdate();
        }
        {
            // 锁定账户流水
            T6102 t6102 = new T6102();
            t6102.F02 = sdzh.F01;
            t6102.F03 = FeeCode.TX;
            t6102.F04 = wlzh.F01;
            t6102.F06 = funds;
            sdzh.F06 = sdzh.F06.add(funds);
            t6102.F08 = sdzh.F06;
            t6102.F09 = "申请提现成功，冻结提现金额";
            insertT6102(connection, t6102);
        }
        if (poundage.compareTo(zero) > 0)
        {
            // 锁定账户流水
            T6102 t6102 = new T6102();
            t6102.F02 = sdzh.F01;
            t6102.F03 = FeeCode.TX_SXF;
            t6102.F04 = wlzh.F01;
            t6102.F06 = poundage;
            sdzh.F06 = sdzh.F06.add(poundage);
            t6102.F08 = sdzh.F06;
            t6102.F09 = "申请提现成功，冻结提现手续费";
            insertT6102(connection, t6102);
        }
        
        T6110 t6110 = selectT6110(connection, accountId);
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("name", t6110.F02);
        envionment.set("datetime", DateTimeParser.format(new Date()));
        envionment.set("amount", funds.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        String content = configureProvider.format(MsgVariavle.TX_SQ, envionment);
        String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
        if ("1".equals(isUseYtx))
        {
            SMSUtils smsUtil = new SMSUtils(configureProvider);
            int type = smsUtil.getTempleId(MsgVariavle.TX_SQ.getDescription());
            sendMsg(connection,
                t6110.F04,
                smsUtil.getSendContent(envionment.get("name"), envionment.get("datetime"), envionment.get("amount")),
                type);
        }
        else
        {
            sendMsg(connection, t6110.F04, content);
        }
        return id;
    }
    
    /*
     * @Override protected void sendMsg(Connection connection, String mobile,
     * String content) throws Throwable { try { if
     * (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile)) { long
     * msgId = 0; try (PreparedStatement ps = connection.prepareStatement(
     * "INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)",
     * Statement.RETURN_GENERATED_KEYS)) { ps.setInt(1, 0); ps.setString(2,
     * content); ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
     * ps.setString(4, "W"); ps.execute(); try (ResultSet resultSet =
     * ps.getGeneratedKeys()) { if (resultSet.next()) { msgId =
     * resultSet.getLong(1); } } } if (msgId > 0) { try (PreparedStatement ps =
     * connection
     * .prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)")) {
     * ps.setLong(1, msgId); ps.setString(2, mobile); ps.execute(); } } return;
     * } } catch (Exception e) { logger.error(e, e); throw e; } }
     */
    
    @Override
    protected void sendLetter(Connection connection, int userId, String title, String content)
        throws Throwable
    {
        int letterId = insertT6123(connection, userId, title, T6123_F05.WD);
        insertT6124(connection, letterId, content);
    }
    
    private int insertT6123(Connection connection, int F02, String F03, T6123_F05 F05)
        throws Throwable
    {
        try
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, F02);
                pstmt.setString(2, F03);
                pstmt.setTimestamp(3, getCurrentTimestamp(connection));
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
        catch (Exception e)
        {
            logger.error("TxManageImpl.insertT6123() error", e);
            throw e;
        }
    }
    
    @Override
    protected void insertT6124(Connection connection, int F01, String F02)
        throws SQLException
    {
        try
        {
            try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6124 SET F01 = ?, F02 = ?"))
            {
                pstmt.setInt(1, F01);
                pstmt.setString(2, F02);
                pstmt.execute();
            }
        }
        catch (Exception e)
        {
            logger.error("TxManageImpl.insertT6124() error", e);
            throw e;
        }
    }
    
    /**
     * 容联云通讯发送短信方法
     * 
     * @param connection
     * @param mobile
     *            手机
     * @param content
     *            内容
     * @param tempId
     *            模板id
     * @throws Throwable
     */
    /*
     * @Override protected void sendMsg(Connection connection, String mobile,
     * String content, int tempId) throws Throwable { try { if
     * (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile)) { long
     * msgId = 0; try (PreparedStatement ps = connection .prepareStatement(
     * "INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)",
     * Statement.RETURN_GENERATED_KEYS)) { ps.setInt(1, tempId); ps.setString(2,
     * content); ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
     * ps.setString(4, "W"); ps.execute(); try (ResultSet resultSet =
     * ps.getGeneratedKeys()) { if (resultSet.next()) { msgId =
     * resultSet.getLong(1); } } } if (msgId > 0) { try (PreparedStatement ps =
     * connection
     * .prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)")) {
     * ps.setLong(1, msgId); ps.setString(2, mobile); ps.execute(); } } return;
     * } } catch (Exception e) { logger.error(e, e); throw e; } }
     */
    /**
     * 检查交易密码是否正确
     * 
     * @param withdrawPsd
     * @return
     * @throws Throwable
     */
    @Override
    public boolean checkWithdrawPassword(String withdrawPsd)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        
        int accountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            int count = psdInputCount(connection);
            int maxCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
            if (count >= maxCount)
            {
                throw new LogicalException("您今日交易密码输入错误已到最大次数，请改日再试!");
            }
            // 校验身份认证、手机、交易密码是否都有设置
            checkIdentityOrPhoneOrTradingPsw(connection);
            boolean aa = false;// 标记交易密码是否正确
            try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S61.T6118 WHERE F01=? AND F08=?"))
            {
                ps.setInt(1, accountId);
                ps.setString(2, UnixCrypt.crypt(withdrawPsd, DigestUtils.sha256Hex(withdrawPsd)));
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        aa = true;
                    }
                }
            }
            if (!aa)
            {
                addInputCount(connection);
                String errorMsg = null;
                if (count + 1 >= maxCount)
                {
                    errorMsg = "您今日交易密码输入错误已到最大次数，请改日再试!";
                }
                else
                {
                    StringBuilder builder = new StringBuilder("交易密码错误,您最多还可以输入");
                    builder.append(maxCount - (count + 1));
                    builder.append("次");
                    errorMsg = builder.toString();
                }
                throw new LogicalException(errorMsg);
            }
            return aa;
        }
    }
    
    private T6101 lock(Connection connection, int F02, T6101_F03 F03)
        throws SQLException
    {
        T6101 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6101.F01, T6101.F02, T6101.F03, T6101.F04, T6101.F05, T6101.F06, T6101.F07 FROM S61.T6101 WHERE T6101.F01 = (SELECT T.F01 FROM S61.T6101 T WHERE T.F02 = ? AND T.F03 = ?) FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6101();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = T6101_F03.parse(resultSet.getString(3));
                    record.F04 = resultSet.getString(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getTimestamp(7);
                }
            }
        }
        return record;
    }
    
    @Override
    public void addBankCard(String bank, String bankAddr, String branchBank, String cardNumber)
        throws Throwable
    {
        if (!checkBankCard(cardNumber))
        {
            throw new ParameterException("输入银行卡错误");
        }
        
    }
    
    // 校验身份认证、手机、交易密码是否都有设置
    public void checkIdentityOrPhoneOrTradingPsw(Connection connection)
        throws Throwable
    {
        StringBuffer verifyMsg = null;
        if (!getVerifyStatus(connection))
        {
            verifyMsg = new StringBuffer("手机未认证、实名未认证");
        }
        if (!getVerifyTradingPsw(connection))
        {
            if (verifyMsg == null)
            {
                verifyMsg = new StringBuffer("交易密码未设置");
            }
            else
            {
                verifyMsg.append("或交易密码未设置");
            }
        }
        if (verifyMsg != null)
        {
            throw new LogicalException(verifyMsg.toString());
        }
    }
    
    @Override
    public boolean getVerifyStatus()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (resultSet.getString(1).equals(T6118_F02.TG.name())
                            && resultSet.getString(2).equals(T6118_F03.TG.name()))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private boolean getVerifyStatus(Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02, F03, F04 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    if (resultSet.getString(1).equals(T6118_F02.TG.name())
                        && resultSet.getString(2).equals(T6118_F03.TG.name()))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean getVerifyTradingPsw()
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (!isOpenWithPsd)
        {
            // 如果不需要交易密码，则不校验
            return true;
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F05 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (resultSet.getString(1).equals(T6118_F05.YSZ.name()))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private boolean getVerifyTradingPsw(Connection connection)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (!isOpenWithPsd)
        {
            // 如果不需要交易密码，则不校验
            return true;
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F05 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    if (resultSet.getString(1).equals(T6118_F05.YSZ.name()))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * 校验银行卡卡号
     * 
     * @param cardId
     * @return
     */
    protected boolean checkBankCard(String cardId)
    {
        if (cardId.trim().length() < 16)
        {
            return false;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N')
        {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }
    
    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * 
     * @param nonCheckCodeCardId
     * @return
     */
    protected char getBankCardCheckCode(String nonCheckCodeCardId)
    {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
            || !nonCheckCodeCardId.matches("\\d+"))
        {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++)
        {
            int k = chs[i] - '0';
            if (j % 2 == 0)
            {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }
    
    private int psdInputCount(Connection connection)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (!isOpenWithPsd)
        {
            // 如果不需要交易密码，则不校验
            return 0;
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F11 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getShort(1);
                }
            }
        }
        return 0;
    }
    
    private void addInputCount(Connection connection)
        throws Throwable
    {
        execute(connection, "UPDATE S61.T6110 SET F11 = F11+1 WHERE F01 = ?", serviceResource.getSession()
            .getAccountId());
    }
    
    @Override
    public PagingResult<Order> search(Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection,
                new ArrayParser<Order>()
                {
                    
                    @Override
                    public Order[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        List<Order> orders = null;
                        while (resultSet.next())
                        {
                            if (orders == null)
                            {
                                orders = new ArrayList<>();
                            }
                            Order order = new Order();
                            order.id = resultSet.getInt(1);
                            order.amount = resultSet.getBigDecimal(2);
                            order.status = EnumParser.parse(T6501_F03.class, resultSet.getString(3));
                            order.orderTime = resultSet.getTimestamp(4);
                            order.amountStr = Formater.formatAmount(order.amount);
                            order.statusName = order.status.getChineseName();
                            order.orderTimeStr = DateTimeParser.format(order.orderTime, "yyyy-MM-dd HH:mm:ss");
                            orders.add(order);
                        }
                        return orders == null ? null : orders.toArray(new Order[orders.size()]);
                    }
                },
                paging,
                "SELECT T6502.F01 AS F01, T6502.F03 AS F02, T6501.F03 AS F03, T6501.F04 AS F04 FROM S65.T6502 INNER JOIN S65.T6501 ON T6502.F01 = T6501.F01 WHERE T6502.F02 = ? AND (T6501.F03 = ? OR T6501.F03=?)  ORDER BY T6501.F04 DESC",
                serviceResource.getSession().getAccountId(),
                T6501_F03.SB.name(),
                T6501_F03.DQR);
        }
    }
    
    @Override
    public PagingResult<OrderXxcz> searchXxcz(Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection,
                new ArrayParser<OrderXxcz>()
                {
                    
                    @Override
                    public OrderXxcz[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        List<OrderXxcz> orders = null;
                        while (resultSet.next())
                        {
                            if (orders == null)
                            {
                                orders = new ArrayList<>();
                            }
                            
                            OrderXxcz orderXxcz = new OrderXxcz();
                            orderXxcz.id = resultSet.getInt(1);
                            orderXxcz.amount = resultSet.getBigDecimal(2);
                            orderXxcz.orderTime = resultSet.getTimestamp(3);
                            orderXxcz.amountStr = Formater.formatAmount(orderXxcz.amount);
                            orderXxcz.status = EnumParser.parse(T7150_F05.class, resultSet.getString(4));
                            orderXxcz.statusName = orderXxcz.status.getChineseName();
                            orderXxcz.orderTimeStr = DateTimeParser.format(orderXxcz.orderTime, "yyyy-MM-dd HH:mm:ss");
                            orderXxcz.remark = resultSet.getString(5);
                            orders.add(orderXxcz);
                        }
                        return orders == null ? null : orders.toArray(new OrderXxcz[orders.size()]);
                    }
                },
                paging,
                "SELECT T7150.F01 AS F01, T7150.F04 AS F02, T7150.F07 AS F03, T7150.F05 AS F04, T7150.F08 AS F05 FROM S71.T7150 WHERE T7150.F02 = ? AND (T7150.F05 = ? OR T7150.F05=?) ORDER BY T7150.F07 DESC",
                serviceResource.getSession().getAccountId(),
                T7150_F05.DSH.name(),
                T7150_F05.YQX.name());
        }
    }
    
    @Override
    public int withdrawHdw(BigDecimal funds, String withdrawPsd, int cardId, T6101_F03 f03)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        BigDecimal zero = new BigDecimal(0);
        if (funds.compareTo(zero) <= 0)
        {
            throw new LogicalException("可用余额不足");
        }
        int accountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            int count = psdInputCount(connection);
            int maxCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
            if (count >= maxCount)
            {
                throw new LogicalException("您今日交易密码输入错误已到最大次数，请改日再试!");
            }
            // 校验身份认证、手机、交易密码是否都有设置
            checkIdentityOrPhoneOrTradingPsw(connection);
            int id = 0;
            try
            {
                
                boolean aa = false;// 标记交易密码是否正确
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F01 FROM S61.T6118 WHERE F01=? AND F08=?"))
                {
                    ps.setInt(1, accountId);
                    ps.setString(2, UnixCrypt.crypt(withdrawPsd, DigestUtils.sha256Hex(withdrawPsd)));
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            aa = true;
                        }
                    }
                }
                serviceResource.openTransactions(connection);
                if (!aa)
                {
                    addInputCount(connection);
                    serviceResource.commit(connection);
                    String errorMsg = null;
                    if (count + 1 >= maxCount)
                    {
                        errorMsg = "您今日交易密码输入错误已到最大次数，请改日再试!";
                    }
                    else
                    {
                        StringBuilder builder = new StringBuilder("交易密码错误,您最多还可以输入");
                        builder.append(maxCount - (count + 1));
                        builder.append("次");
                        errorMsg = builder.toString();
                    }
                    throw new LogicalException(errorMsg);
                }
                Timestamp timestamp = getCurrentTimestamp(connection);
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F01 FROM S62.T6252 WHERE DATEDIFF(?,F08)>0 AND F09=? AND F03=?"))
                {
                    ps.setTimestamp(1, timestamp);
                    ps.setString(2, T6252_F09.WH.name());
                    ps.setInt(3, accountId);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            throw new LogicalException("您有逾期未还的借款，请先还完再操作。");
                        }
                    }
                }
                String cardNumber = null;
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F06 FROM S61.T6114 WHERE F01=? AND F02=?"))
                {
                    ps.setInt(1, cardId);
                    ps.setInt(2, accountId);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            cardNumber = resultSet.getString(1);
                        }
                    }
                }
                if (StringHelper.isEmpty(cardNumber))
                {
                    throw new LogicalException("银行卡不存在");
                }
                BigDecimal proportion =
                    new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_PROPORTION));
                BigDecimal poundage = funds.multiply(proportion);
                BigDecimal amount = funds.add(poundage);// 提现应付金额
                // 往来账户
                T6101 wlzh = lock(connection, accountId, T6101_F03.WLZH);
                if (amount.compareTo(wlzh.F06) > 0)
                {
                    throw new LogicalException("账户余额不足");
                }
                // 锁定账户
                T6101 sdzh = lock(connection, accountId, T6101_F03.SDZH);
                try (PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO S61.T6130(F02,F03,F04,F06,F07,F08,F09) VALUES(?,?,?,?,?,?,?)",
                        PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    ps.setInt(1, accountId);
                    ps.setInt(2, cardId);
                    ps.setBigDecimal(3, funds);
                    ps.setBigDecimal(4, poundage);
                    ps.setBigDecimal(5, poundage);
                    ps.setTimestamp(6, timestamp);
                    ps.setString(7, T6130_F09.DSH.toString());
                    ps.execute();
                    try (ResultSet rs = ps.getGeneratedKeys())
                    {
                        if (rs.next())
                        {
                            id = rs.getInt(1);
                        }
                    }
                }
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 - ?, F07 = ?  WHERE F01 = ?"))
                {
                    ps.setBigDecimal(1, amount);
                    ps.setTimestamp(2, timestamp);
                    ps.setInt(3, wlzh.F01);
                    ps.executeUpdate();
                }
                {
                    // 往来账户流水
                    T6102 t6102 = new T6102();
                    t6102.F02 = wlzh.F01;
                    t6102.F03 = FeeCode.TX;
                    t6102.F04 = sdzh.F01;
                    t6102.F07 = funds;
                    wlzh.F06 = wlzh.F06.subtract(funds);
                    t6102.F08 = wlzh.F06;
                    t6102.F09 = "提现金额";
                    insertT6102(connection, t6102);
                }
                if (poundage.compareTo(zero) > 0)
                {
                    // 往来账户流水
                    T6102 t6102 = new T6102();
                    t6102.F02 = wlzh.F01;
                    t6102.F03 = FeeCode.TX_SXF;
                    t6102.F04 = sdzh.F01;
                    t6102.F07 = poundage;
                    wlzh.F06 = wlzh.F06.subtract(poundage);
                    t6102.F08 = wlzh.F06;
                    t6102.F09 = "提现手续费";
                    insertT6102(connection, t6102);
                }
                
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = ?  WHERE F01 = ? "))
                {
                    ps.setBigDecimal(1, amount);
                    ps.setTimestamp(2, timestamp);
                    ps.setInt(3, sdzh.F01);
                    ps.executeUpdate();
                }
                {
                    // 锁定账户流水
                    T6102 t6102 = new T6102();
                    t6102.F02 = sdzh.F01;
                    t6102.F03 = FeeCode.TX;
                    t6102.F04 = wlzh.F01;
                    t6102.F06 = funds;
                    sdzh.F06 = sdzh.F06.add(funds);
                    t6102.F08 = sdzh.F06;
                    t6102.F09 = "提现金额";
                    insertT6102(connection, t6102);
                }
                if (poundage.compareTo(zero) > 0)
                {
                    // 锁定账户流水
                    T6102 t6102 = new T6102();
                    t6102.F02 = sdzh.F01;
                    t6102.F03 = FeeCode.TX_SXF;
                    t6102.F04 = wlzh.F01;
                    t6102.F06 = poundage;
                    sdzh.F06 = sdzh.F06.add(poundage);
                    t6102.F08 = sdzh.F06;
                    t6102.F09 = "提现手续费";
                    insertT6102(connection, t6102);
                }
                
                serviceResource.commit(connection);
                return id;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private boolean isYuqi(Connection connection, int accountId)
        throws Throwable
    {
        String sql = "SELECT DATEDIFF(?,F08) FROM S62.T6252 WHERE F09=? AND F03=? AND F08 < SYSDATE()";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setTimestamp(1, getCurrentTimestamp(connection));
            ps.setString(2, T6252_F09.WH.name());
            ps.setInt(3, accountId);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    int days = rs.getInt(1);
                    if (days > 0)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public T6118 getVerifyEntity()
        throws Throwable
    {
        T6118 t6118 = new T6118();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04, F05 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6118.F02 = T6118_F02.parse(resultSet.getString(1));
                        t6118.F03 = T6118_F03.parse(resultSet.getString(2));
                        t6118.F04 = T6118_F04.parse(resultSet.getString(3));
                        t6118.F05 = T6118_F05.parse(resultSet.getString(4));
                    }
                }
            }
        }
        return t6118;
    }
    
    /**
     * 未完成充值记录数
     * 
     * @return int
     * @throws Throwable
     */
    @Override
    public int getCount()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(1) FROM S65.T6502 INNER JOIN S65.T6501 ON T6502.F01 = T6501.F01 WHERE T6502.F02 = ? AND (T6501.F03 = ? OR T6501.F03=?)"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6501_F03.SB.name());
                pstmt.setString(3, T6501_F03.DQR.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public T5023 getT5023(T5023_F02 F02)
        throws Throwable
    {
        if (F02 == null)
        {
            return null;
        }
        String sql = "SELECT F01,F02,F03,F04,F05 FROM S50.T5023 WHERE T5023.F02 = ? LIMIT 1";
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, sql, F02.name());
        }
    }
    
    protected static final ItemParser<T5023> ITEM_PARSER = new ItemParser<T5023>()
    {
        
        @Override
        public T5023 parse(ResultSet rs)
            throws SQLException
        {
            T5023 t5023 = null;
            if (rs.next())
            {
                t5023 = new T5023();
                t5023.F01 = rs.getInt(1);
                t5023.F02 = T5023_F02.valueOf(rs.getString(2));
                t5023.F03 = rs.getString(3);
                t5023.F04 = rs.getTimestamp(4);
                t5023.F05 = rs.getTimestamp(5);
            }
            return t5023;
        }
    };
    
    @Override
    public T6118 getVerifyEntity(int userId)
        throws Throwable
    {
        T6118 t6118 = new T6118();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04, F05 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6118.F02 = T6118_F02.parse(resultSet.getString(1));
                        t6118.F03 = T6118_F03.parse(resultSet.getString(2));
                        t6118.F04 = T6118_F04.parse(resultSet.getString(3));
                        t6118.F05 = T6118_F05.parse(resultSet.getString(4));
                    }
                }
            }
        }
        return t6118;
    }
    
    @Override
    public int getXxczCount()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(1) FROM S71.T7150 WHERE  T7150.F02 = ?  AND T7150.F05 = ?"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T7150_F05.DSH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
}
