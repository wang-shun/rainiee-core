package com.dimeng.p2p.modules.bid.pay.service.achieve;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6103;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S61.enums.T6103_F08;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6147_F04;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.enums.T6216_F18;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F28;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F27;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S63.entities.T6342;
import com.dimeng.p2p.S63.entities.T6344;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6342_F04;
import com.dimeng.p2p.S63.enums.T6344_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.common.RiskLevelCompareUtil;
import com.dimeng.p2p.modules.bid.pay.service.TenderManage;
import com.dimeng.p2p.variables.defines.RegulatoryPolicyVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

public class TenderManageImpl extends AbstractBidService implements TenderManage
{
    
    public TenderManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public Map<String, String> bid(final int bidId, final BigDecimal amount, String userReward, String tranPwd,
        String myRewardType, String hbRule, String jxqRule, Map<String, String> parMap)
        throws Throwable
    {
        String userId = parMap.get("userId");
        int accountId = 0;
        if(!StringHelper.isEmpty(userId)){
            accountId = Integer.parseInt(userId);
        }else {
            accountId = serviceResource.getSession().getAccountId();
        }

        try (Connection connection = getConnection())
        {
            //验证交易密码正确性
            validateTranPwd(tranPwd, connection, accountId);

            return wgBid(bidId, amount, userReward, myRewardType, hbRule, jxqRule, parMap, connection, accountId);
        }
    }

    protected Map<String, String> wgBid(final int bidId, final BigDecimal amount, String userReward, String myRewardType,
                                        String hbRule, String jxqRule, Map<String, String> parMap, Connection connection, int accountId)
        throws Throwable
    {
        if (bidId <= 0)
        {
            throw new ParameterException("没有指定要投的标。");
        }

        if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0)
        {
            throw new ParameterException("投资金额必须大于零。");
        }

        BigDecimal _a = amount.stripTrailingZeros();
        if (_a.toPlainString().contains("."))
        {
            throw new LogicalException("投资金额必须为整数。");
        }
        if(accountId == 0){
            accountId = serviceResource.getSession().getAccountId();
        }

        T6110 t6110 = selectT6110(connection, accountId);
        if (T6110_F06.FZRR == t6110.F06 && T6110_F17.F == t6110.F17)
        {
            throw new LogicalException(t6110.F02 + "不允许投资");
        }

        //是否开启风险承受能力评估
        boolean isOpenRiskAccess = Boolean.parseBoolean(serviceResource.getResource(ConfigureProvider.class)
                        .getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
        //是否开启投资限制
        boolean isInvestLimit = Boolean.parseBoolean(serviceResource.getResource(ConfigureProvider.class)
                        .getProperty(RegulatoryPolicyVariavle.IS_INVEST_LIMIT));
        if (isOpenRiskAccess
            && isInvestLimit
            && !RiskLevelCompareUtil.compareRiskLevel(getUserRiskLevel(connection, accountId),
                getProductRiskLevel(connection, bidId).name()) && T6110_F06.ZRR == t6110.F06)
        {
            throw new LogicalException("您的风险承受等级不可投资该项目。");
        }

        //获取标的担保方ID（购买人不能购买自己担保的标）
        int assureId = selectT6236(connection, bidId);
        if (accountId == assureId)
        {
            throw new LogicalException("不可投资自己担保的标");
        }

        // 查询是否有逾期未还
        int count = selectYqInfo(connection, accountId);
        if (count > 0)
        {
            throw new LogicalException("您有逾期未还的借款，请先还完再操作。");
        }

        amount.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        try
        {
            serviceResource.openTransactions(connection);
            // 锁定标
            T6230 t6230 = selectT6230(connection, bidId);
            T6231 t6231 = getExtra(t6230.F01, connection);
            if (t6230 == null || t6231 == null)
            {
                throw new LogicalException("指定的标记录不存在。");
            }
            if (T6230_F28.S.equals(t6230.xsb) && (getXsbCount(accountId, connection) > 0 || getZqzrCount(accountId, connection) > 0))
            {
                throw new LogicalException("感谢您的支持！<br/>此标为新手标，只有未成功投资过并且没有购买过债权的新用户才可以投资。");
            }

            boolean zjb = BooleanParser.parse(serviceResource.getResource(ConfigureProvider.class)
                            .getProperty(SystemVariable.BID_SFZJKT));
            if (!zjb && accountId == t6230.F02)
            {
                throw new LogicalException("您是该标的借款人，不能投资。");
            }
            if (t6230.F20 != T6230_F20.TBZ)
            {
                throw new LogicalException("指定的标不是投资中状态,不能投资。");
            }
            if (amount.compareTo(t6230.F07) > 0)
            {
                throw new LogicalException("投资金额大于可投金额。");
            }
            if (amount.compareTo(t6231.F25) < 0)
            {
                throw new LogicalException("投资金额不能低于最低起投金额。");
            }
            // 最大投资额
            BigDecimal zdtzje = selectYtje(connection, accountId, bidId);
            if (amount.compareTo(t6231.F26.subtract(zdtzje)) > 0)
            {
                throw new LogicalException("您该项目的投资总金额已超过最大投资金额" + t6231.F26 + "元;当前可投金额为"
                    + t6231.F26.subtract(zdtzje) + "元。");
            }
            t6230.F07 = t6230.F07.subtract(amount);
            if (t6230.F07.compareTo(new BigDecimal(0)) > 0 && t6230.F07.compareTo(t6231.F25) < 0)
            {
                throw new LogicalException("剩余可投金额不能低于最低起投金额。");
            }
            // 锁定投资人资金账户
            T6101 investor = selectT6101(connection, accountId, T6101_F03.WLZH, false);
            if (investor == null)
            {
                throw new LogicalException("用户往来账户不存在。");
            }

            T6344 t6344 = null;
            if (!StringHelper.isEmpty(userReward)) {
                //如果使用了奖励投资
                t6344 = validateReward(amount, myRewardType, hbRule, jxqRule, parMap, connection, t6230, t6231, investor, accountId);
            }
            if (StringHelper.isEmpty(hbRule)) {
                if (investor.F06.compareTo(amount) < 0)
                {
                    throw new LogicalException("账户余额不足。");
                }
            }

            // 插入投资订单
            T6501 t6501 = new T6501();
            t6501.F02 = OrderType.BID.orderType();
            t6501.F03 = T6501_F03.DTJ;
            t6501.F04 = getCurrentTimestamp(connection);
            t6501.F07 = T6501_F07.YH;
            t6501.F08 = accountId;
            t6501.F13 = amount;
            int ordId = insertT6501(connection, t6501);

            insertT6504(connection, ordId, accountId, bidId, amount, parMap.get("source"));
            Map<String, String> rtnMap = new HashMap<String, String>();
            rtnMap.put("orderId", String.valueOf(ordId));
            rtnMap.put("surplusAmount", t6230.F07.toString());
            String usedExp = parMap.get("usedExp");
            if (!StringHelper.isEmpty(userReward))
            {
                if (!StringHelper.isEmpty(hbRule))
                {
                    t6501 = new T6501();
                    t6501.F02 = OrderType.BID_RED_PACKET.orderType();
                    t6501.F03 = T6501_F03.DTJ;
                    t6501.F04 = getCurrentTimestamp(connection);
                    t6501.F07 = T6501_F07.YH;
                    t6501.F08 = accountId;
                    t6501.F13 = t6344.F05;
                    int hbOrdId = insertT6501(connection, t6501);

                    insertT6527(connection, hbOrdId, accountId, bidId, t6344.F05, ordId);
                    rtnMap.put("hbOrdId", String.valueOf(hbOrdId));
                }

                if (!StringHelper.isEmpty(jxqRule))
                {
                    // 我的奖励不能投奖励标、新手标
                    throwRewardException(t6231.F27.name(), t6230.xsb.name());
                    int jxqOrdId = insertJxqOrd(connection, bidId, ordId, accountId, jxqRule, amount, accountId);
                    rtnMap.put("jxqOrdId", String.valueOf(jxqOrdId));
                }

                if ("yes".equals(usedExp))
                {
                    // 我的奖励不能投奖励标、新手标
                    throwRewardException(t6231.F27.name(), t6230.xsb.name());
                    int expOrdId = insertExperienceOrd(bidId, connection, ordId, accountId);
                    rtnMap.put("expOrdId", String.valueOf(expOrdId));
                }
            }
            serviceResource.commit(connection);
            return rtnMap;
        }
        catch (Exception e)
        {
            serviceResource.rollback(connection);
            throw e;
        }
    }

    /**
     * 使用奖励投资校验
     * @param amount
     * @param myRewardType
     * @param hbRule
     * @param jxqRule
     * @param parMap
     * @param connection
     * @param t6230
     * @param t6231
     * @param investor
     * @return
     * @throws Throwable
     */
    private T6344 validateReward(BigDecimal amount, String myRewardType, String hbRule, String jxqRule, Map<String, String> parMap, Connection connection, T6230 t6230, T6231 t6231, T6101 investor, int accountId) throws Throwable {
        //用户体验金
        T6103 t6103 = getT6103(t6230.F01, accountId);
        if("ALL".equals(myRewardType))
        {
            boolean usedHb = isUsedReward(t6230.F01, T6340_F03.redpacket.name(), accountId);
            boolean usedJxq = isUsedReward(t6230.F01, T6340_F03.interest.name(), accountId);
            if(usedHb && !StringHelper.isEmpty(hbRule))
            {
                throw new LogicalException("只能使用一次红包投此标");
            }

            if(usedJxq && !StringHelper.isEmpty(jxqRule))
            {
                throw new LogicalException("只能使用一次加息券投此标");
            }

            String usedExp = parMap.get("usedExp");
            if(t6103 != null && "yes".equals(usedExp))
            {
                throw new LogicalException("只能使用一次体验金投此标");
            }
        }else {
            //用户活动
            T6342 t6342 = getT6342(t6230.F01, accountId);
            if (null != t6103 || null != t6342) {
                throw new LogicalException("只能使用一次我的奖励投此标");
            }
        }
        T6344 t6344 = null;
        if (!StringHelper.isEmpty(hbRule)) {
            // 用户红包投资
            t6344 = hbBid(amount, hbRule, connection, t6230, t6231, investor, accountId);
        }

        //用户加息券校验
        if (!StringHelper.isEmpty(jxqRule))
        {
            T6342 t6342 = selectT6342(connection, IntegerParser.parse(jxqRule), T6342_F04.WSY.name(), accountId);
            if (null == t6342)
            {
                throw new LogicalException("用户加息券不存在或已使用。");
            }
            //红包
            t6344 = selectT6344(connection, t6342.F03, T6340_F03.interest.name());
            if (null == t6344)
            {
                throw new LogicalException("用户加息券不存在或已使用。");
            }
        }
        return t6344;
    }

    /**
     * 红包投资校验
     * @param amount
     * @param hbRule
     * @param connection
     * @param t6230
     * @param t6231
     * @param investor
     * @return
     * @throws Throwable
     */
    private T6344 hbBid(BigDecimal amount, String hbRule, Connection connection, T6230 t6230, T6231 t6231, T6101 investor, int accountId) throws Throwable {
        T6342 t6342 = selectT6342(connection, IntegerParser.parse(hbRule), T6342_F04.WSY.name(), accountId);
        if (null == t6342)
        {
            throw new LogicalException("红包不存在或已使用。");
        }
        // 红包不能投奖励标、新手标
        throwRewardException(t6231.F27.name(), t6230.xsb.name());

        //红包
        T6344 t6344 = selectT6344(connection, t6342.F03, T6340_F03.redpacket.name());
        if (null == t6344)
        {
            throw new LogicalException("红包不存在或已使用。");
        }
        if (amount.compareTo(t6344.F07) < 0)
        {
            throw new LogicalException("投资满" + t6344.F07 + "元才可以使用。");
        }
        // 实际投资金额 = 投资金额 - 红包金额
        BigDecimal sjAmount = amount.subtract(t6344.F05);
        if (sjAmount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new LogicalException("红包金额小于投资金额才可使用。");
        }
        sjAmount = sjAmount.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : sjAmount;

        if (investor.F06.compareTo(sjAmount) < 0)
        {
            throw new LogicalException("账户余额不足。");
        }

        boolean tg = BooleanParser.parse(serviceResource.getResource(ConfigureProvider.class)
                    .getProperty(SystemVariable.SFZJTG));
        String escrow = serviceResource.getResource(ConfigureProvider.class).getProperty(SystemVariable.ESCROW_PREFIX);
        // 红包投资易宝托管需要进行实际投资金额是否大于该笔投资成交服务费判断
        if (tg && escrow.equals("yeepay"))
        {
            T6238 t6238 = this.selectT6238(connection, t6230.F01);
            BigDecimal fee = amount.multiply(t6238.F02).setScale(2, RoundingMode.HALF_UP);
            if (sjAmount.compareTo(fee) <= 0 || sjAmount.compareTo(new BigDecimal(50)) < 0)
            {
                throw new LogicalException("投资实际支付金额" + sjAmount + "元不能低于成交服务费" + fee + "元！");
            }
        }

        return t6344;
    }

    /**
     * 查询担保机构
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    private int selectT6236(Connection connection, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F03 FROM S62.T6236 WHERE T6236.F02 = ?"))
        {
            pstmt.setInt(1, F02);
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
    
    private T6147_F04 getUserRiskLevel(Connection connection, int accountId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F04 FROM S61.T6147 WHERE T6147.F02=? ORDER BY F06 DESC LIMIT 1"))
        {
            pstmt.setInt(1, accountId);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    return T6147_F04.parse(rs.getString(1));
                }
            }
        }
        return null;
    }
    
    /**
    * 查询标的对应的产品风险等级
    * <功能详细描述>
    * @param connection
    * @param id
    * @return
    * @throws Throwable
    */
    private T6216_F18 getProductRiskLevel(Connection connection, int id)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6216.F18 FROM S62.T6216 INNER JOIN S62.T6230 ON T6216.F01=T6230.F32 WHERE T6230.F01=? "))
        {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    return T6216_F18.parse(rs.getString(1));
                }
            }
        }
        return T6216_F18.BSX;
    }
    
    private void throwRewardException(String jlb, String xsb)
        throws Throwable
    {
        if (T6231_F27.S.name().equals(jlb))
        {
            throw new LogicalException("奖励标不允许使用加息券投资。");
        }
        if (T6230_F28.S.name().equals(xsb))
        {
            throw new LogicalException("新手标不允许使用加息券投资。");
        }
    }
    
    public int insertExperienceOrd(int bidId, Connection connection, int ordId, int accountId)
        throws Throwable
    {
        // 锁定用户体验金
        T6103 t6103 = selectT6103(connection, accountId);
        // 判断用户是否有体验金
        if (t6103 == null || t6103.F03.compareTo(BigDecimal.ZERO) == 0)
        {
            return 0;
        }

        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.BID_EXPERIENCE.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F07 = T6501_F07.YH;
        t6501.F08 = accountId;
        t6501.F13 = t6103.F03;
        int expOrdId = insertT6501(connection, t6501);
        
        insertT6518(connection, expOrdId, accountId, bidId, t6103.F03, ordId);
        
        return expOrdId;
    }
    
    private T6103 selectT6103(Connection connection, int accountId)
        throws Throwable
    {
        Date currDate = getCurrentDate(connection);
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, SUM(F03), F04, F05, F06, F07, F08, F09, F10 FROM S61.T6103 WHERE T6103.F02 = ? AND T6103.F06 = ? AND DATE(T6103.F04) <= ? AND T6103.F05 >= ? FOR UPDATE"))
        {
            pstmt.setInt(1, accountId);
            pstmt.setString(2, T6103_F06.WSY.name());
            pstmt.setDate(3, currDate);
            pstmt.setDate(4, currDate);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    T6103 record = new T6103();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getTimestamp(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = T6103_F06.parse(resultSet.getString(6));
                    record.F07 = resultSet.getInt(7);
                    record.F08 = T6103_F08.parse(resultSet.getString(8));
                    record.F09 = resultSet.getString(9);
                    record.F10 = resultSet.getTimestamp(10);
                    return record;
                }
            }
        }
        return null;
    }
    
    private void insertT6518(Connection connection, int F01, int F02, int F03, BigDecimal F04, int ordId)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6518 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F06 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F03);
            pstmt.setBigDecimal(4, F04);
            pstmt.setInt(5, ordId);
            pstmt.execute();
        }
    }
    
    private void insertT6504(Connection connection, int F01, int F02, int F03, BigDecimal F04, String F07)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6504 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F07 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F03);
            pstmt.setBigDecimal(4, F04);
            pstmt.setString(5, F07);
            pstmt.execute();
        }
    }
    
    private int selectYqInfo(Connection connection, int F03)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(1) FROM S62.T6252 WHERE F03 = ? AND F08 < CURDATE() AND F09 = ? "))
        {
            pstmt.setInt(1, F03);
            pstmt.setString(2, T6252_F09.WH.name());
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
    
    protected String selectTranPwd(Connection connection, int UserId)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT  F08 FROM  S61.T6118 WHERE T6118.F01 = ? "))
        {
            pstmt.setInt(1, UserId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return "";
    }

    public T6230 selectT6230(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F28 FROM S62.T6230 WHERE T6230.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    T6230 record = new T6230();
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
                    record.xsb = T6230_F28.parse(resultSet.getString(27));
                    return record;
                }
            }
        }
        return null;
    }

    /**
    * 查询标的拓展信息
    * 
    * @param id
    * @return
    * @throws Throwable
    */
    protected T6231 getExtra(int id, Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F21 AS F17, F22 AS F18, F25 AS F19, F26 AS F20, F27 AS F21, F28 AS F22 FROM S62.T6231 WHERE T6231.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    T6231 record = new T6231();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getDate(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getString(8);
                    record.F09 = resultSet.getString(9);
                    record.F10 = resultSet.getTimestamp(10);
                    record.F11 = resultSet.getTimestamp(11);
                    record.F12 = resultSet.getTimestamp(12);
                    record.F13 = resultSet.getTimestamp(13);
                    record.F14 = resultSet.getTimestamp(14);
                    record.F15 = resultSet.getTimestamp(15);
                    record.F16 = resultSet.getString(16);
                    record.F21 = T6231_F21.parse(resultSet.getString(17));
                    record.F22 = resultSet.getInt(18);
                    record.F25 = resultSet.getBigDecimal(19);
                    record.F26 = resultSet.getBigDecimal(20);
                    record.F27 = T6231_F27.parse(resultSet.getString(21));
                    record.F28 = resultSet.getBigDecimal(22);
                    return record;
                }
            }
        }
        return null;
    }
    
    /**
    * 查询某用户对标的的投资金额之和
    * 
    * @param connection
    * @param userId
    * @param bidId
    * @return
    * @throws SQLException
    */
    private BigDecimal selectYtje(Connection connection, int userId, int bidId)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT SUM(t6504.F04) FROM S65.T6504 t6504 LEFT JOIN S65.T6501 t6501 ON t6504.F01 = t6501.F01 WHERE t6504.F02 = ? AND t6504.F03 = ? AND t6501.F03 = ?"))
        {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bidId);
            pstmt.setString(3, T6501_F03.CG.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getBigDecimal(1);
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    /**
    * 获取某用户的债权转让数量
    * 
    * @param userId
    * @return
    * @throws Throwable
    */
    protected int getZqzrCount(int userId, Connection connection)
        throws SQLException
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT count(*) FROM S62.T6262  WHERE T6262.F03 = ? "))
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
    * 已投新手标数量 <功能详细描述>
    * 
    * @param userId
    * @return
    * @throws Throwable
    */
    protected int getXsbCount(int userId, Connection connection)
        throws SQLException
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT count(*) FROM S62.T6250  WHERE T6250.F03 = ? "))
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    private int insertJxqOrd(Connection connection, int bidId, int ordId, int userId, String jxqRule, BigDecimal amount, int accountId)
        throws Throwable
    {
        
        // 用户活动表
        T6342 t6342 = selectT6342(connection, IntegerParser.parse(jxqRule), T6342_F04.WSY.name(), accountId);
        // 判断用户是否有加息券
        if (t6342 == null)
        {
            return 0;
        }
        T6344 t6344 = selectT6344(connection, t6342.F03, T6340_F03.interest.name());
        if (amount.compareTo(t6344.F07) < 0)
        {
            throw new LogicalException("投资满" + t6344.F07 + "元使用");
        }
        //int jxqOrdId = insertT6501(connection, OrderType.BID_COUPON.orderType(), T6501_F03.DTJ, T6501_F07.YH, userId);
        
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.BID_COUPON.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F07 = T6501_F07.YH;
        t6501.F08 = userId;
        t6501.F13 = amount;
        int jxqOrdId = insertT6501(connection, t6501);
        
        insertT6523(connection, jxqOrdId, userId, bidId, t6344.F05.divide(new BigDecimal(100)), ordId);
        return jxqOrdId;
    }
    
    private void insertT6523(Connection connection, int F01, int F02, int F03, BigDecimal F04, int ordId)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6523 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F06 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F03);
            pstmt.setBigDecimal(4, F04);
            pstmt.setInt(5, ordId);
            pstmt.execute();
        }
    }
    
    private void insertT6527(Connection connection, int F01, int F02, int F03, BigDecimal F04, int ordId)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6527 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F06 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F03);
            pstmt.setBigDecimal(4, F04);
            pstmt.setInt(5, ordId);
            pstmt.execute();
        }
    }
    
    private T6342 selectT6342(Connection connection, int F01, String F04, int accountId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S63.T6342 WHERE T6342.F01 = ? AND T6342.F02=? AND T6342.F04 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, accountId);
            pstmt.setString(3, F04);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    T6342 record = new T6342();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = T6342_F04.parse(resultSet.getString(4));
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getTimestamp(7);
                    record.F08 = resultSet.getTimestamp(8);
                    return record;
                }
            }
        }
        return null;
    }
    
    private T6344 selectT6344(Connection connection, int F01, String rewardType)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6344.F01, T6344.F02, T6344.F03, T6344.F04, T6344.F05, T6344.F06, T6344.F07, "
                + "T6344.F08, T6344.F09 FROM S63.T6344 INNER JOIN S63.T6340 ON T6344.F02=T6340.F01 WHERE T6344.F01 = ? AND T6340.F03= ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            pstmt.setString(2, rewardType);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    T6344 record = new T6344();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = T6344_F09.parse(resultSet.getString(9));
                    return record;
                }
            }
        }
        return null;
    }
    
    /**
    * 查询某个标是否用过体验金
    * 
    * @param bidId
    * @return
    * @throws Throwable
    */
    @Override
    public T6103 getT6103(int bidId, int accountId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            if(accountId ==0 ){
                accountId = serviceResource.getSession().getAccountId() ;
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13 FROM S61.T6103 where F02 = ? AND F13 = ? LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                pstmt.setInt(2, bidId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        T6103 record = new T6103();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = T6103_F06.parse(resultSet.getString(6));
                        record.F07 = resultSet.getInt(7);
                        record.F08 = T6103_F08.parse(resultSet.getString(8));
                        record.F09 = resultSet.getString(9);
                        record.F10 = resultSet.getTimestamp(10);
                        record.F11 = resultSet.getTimestamp(11);
                        record.F12 = resultSet.getInt(12);
                        record.F13 = resultSet.getInt(13);
                        return record;
                    }
                }
            }
            return null;
        }
    }
    
    /**
    * 查询用户活动表
    * 
    * @param bidId
    * @return
    * @throws Throwable
    */
    @Override
    public T6342 getT6342(int bidId, int accountId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            if(accountId ==0 ){
                accountId = serviceResource.getSession().getAccountId() ;
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S63.T6342 WHERE T6342.F02 = ? AND T6342.F06 = ? LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                pstmt.setInt(2, bidId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        T6342 record = new T6342();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = T6342_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = resultSet.getInt(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.F08 = resultSet.getTimestamp(8);
                        return record;
                    }
                }
            }
            return null;
        }
    }

    /**
     * 查询用户活动表
     *
     * @param bidId
     * @param rewardType
     * @return
     * @throws Throwable
     */
    public boolean isUsedReward(int bidId, String rewardType, int accountId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(1) FROM S63.T6342 LEFT JOIN S63.T6344 ON T6344.F01 = T6342.F03 LEFT JOIN S63.T6340 ON T6340.F01 = T6344.F02 WHERE T6342.F02 = ? AND T6342.F06 = ? AND T6340.F03 = ? LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                pstmt.setInt(2, bidId);
                pstmt.setString(3, rewardType);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1) > 0;
                    }
                }
            }
            return false;
        }
    }
    
    protected int psdInputCount(Connection connection, int accountId)
        throws Throwable
    {
        if(accountId == 0){
            accountId = serviceResource.getSession().getAccountId();
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F11 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, accountId);
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
    
    protected void addInputCount(Connection connection, int accountId)
        throws Throwable
    {
        execute(connection, "UPDATE S61.T6110 SET F11 = F11+1 WHERE F01 = ?", accountId);
    }
    
    /**
     * 查询标的费率信息
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    protected T6238 selectT6238(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6238 WHERE T6238.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    T6238 record = new T6238();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getBigDecimal(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    return record;
                }
            }
        }
        return null;
    }

    /**
     * 如果交易密码不为空，则验证密码正确性
     * @param tranPwd
     * @param connection
     * @throws Throwable
     */
    private void validateTranPwd(String tranPwd, Connection connection, int accountId) throws Throwable {
        if (!StringHelper.isEmpty(tranPwd))
        {
            ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
            int maxCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
            int count = psdInputCount(connection, accountId);
            if (count >= maxCount)
            {
                throw new LogicalException("您今日交易密码输入错误已到最大次数，请改日再试!");
            }

            String tran_pwd = selectTranPwd(connection, accountId);
            if (!tranPwd.equals(tran_pwd))
            {
                addInputCount(connection, accountId);
                if (count + 1 >= maxCount)
                {
                    throw new LogicalException("您今日交易密码输入错误已到最大次数，请改日再试!");
                }
                throw new LogicalException("交易密码错误,您最多还可以输入"+(maxCount - (count + 1))+"次");
            }
        }
    }
}
