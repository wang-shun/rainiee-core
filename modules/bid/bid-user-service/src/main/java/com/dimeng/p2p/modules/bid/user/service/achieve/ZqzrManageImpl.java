package com.dimeng.p2p.modules.bid.user.service.achieve;

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
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.enums.*;
import com.dimeng.p2p.XyType;
import com.dimeng.p2p.modules.bid.user.service.ZqzrManage;
import com.dimeng.p2p.modules.bid.user.service.entity.*;
import com.dimeng.p2p.modules.bid.user.service.query.addTransfer;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.DateHelper;
import com.dimeng.util.Formater;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ZqzrManageImpl extends AbstractBidManage implements ZqzrManage
{
    
    public ZqzrManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public SellFinacingInfo getSellFinacingInfo()
        throws Throwable
    {
        SellFinacingInfo info = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F02,F03,F06,F04,F07,F05,F08 FROM S62.T6263 WHERE F01=?"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        info = new SellFinacingInfo();
                        info.money = rs.getBigDecimal(1);
                        info.inMoney = rs.getBigDecimal(2);
                        info.outMoney = rs.getBigDecimal(3);
                        info.inAssetsMoney = rs.getBigDecimal(4);
                        info.outAssetsMoney = rs.getBigDecimal(5);
                        info.inNum = rs.getInt(6);
                        info.outNum = rs.getInt(7);
                    }
                }
                return info;
            }
        }
    }
    
    @Override
    public PagingResult<ZrzdzqEntity> getSellFinacing(Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT T6260.F01 AS F01, T6260.F02 AS F02, T6260.F03 AS F03, T6260.F04 AS F04, T6260.F05 AS F05, T6260.F06 AS F06, T6260.F07 AS F07, T6260.F08 AS F08, T6251.F02 AS F09, T6251.F03 AS F10, T6251.F04 AS F11, T6251.F05 AS F12, T6251.F06 AS F13, T6251.F07 AS F14, T6251.F08 AS F15, T6251.F09 AS F16, T6251.F10 AS F17, T6251.F11 AS F18, T6230.F06 AS F19, T6231.F02 AS F20, T6231.F03 AS F21 FROM S62.T6260 INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 INNER JOIN S62.T6231 ON T6251.F03 = T6231.F01 WHERE T6260.F07 IN (?,?)  AND T6251.F04 = ? ORDER BY T6260.F05 DESC";
            return selectPaging(connection, new ArrayParser<ZrzdzqEntity>()
            {
                @Override
                public ZrzdzqEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<ZrzdzqEntity> list = null;
                    while (resultSet.next())
                    {
                        ZrzdzqEntity record = new ZrzdzqEntity();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6260_F07.parse(resultSet.getString(7));
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getString(9);
                        record.F10 = resultSet.getInt(10);
                        record.F11 = resultSet.getInt(11);
                        record.F12 = resultSet.getBigDecimal(12);
                        record.F13 = resultSet.getBigDecimal(13);
                        record.F14 = resultSet.getBigDecimal(14);
                        record.F15 = T6251_F08.parse(resultSet.getString(15));
                        record.F16 = resultSet.getDate(16);
                        record.F17 = resultSet.getDate(17);
                        record.F18 = resultSet.getInt(18);
                        record.F19 = resultSet.getBigDecimal(19);
                        record.F20 = resultSet.getInt(20);
                        record.F21 = resultSet.getInt(21);
                        record.F22 = record.F07.getChineseName();
                        record.F23 = Formater.formatRate(record.F19);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new ZrzdzqEntity[list.size()]);
                }
            }, paging, sql, T6260_F07.ZRZ, T6260_F07.DSH, serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public PagingResult<MaySettleFinacing> getMaySettleFinacing(Paging paging)
        throws Throwable
    {
        try (final Connection connection = getConnection())
        {
            Timestamp timestamp = getCurrentTimestamp(connection);
            String sql =
                "SELECT T6251.F02 AS F01, T6251.F03 AS F02, T6251.F07 AS F03, T6231.F02 AS F04, T6231.F03 AS F05, T6231.F06 AS F06, T6230.F06 AS F07,T6251.F01 AS F08 FROM S62.T6251, S62.T6231, S62.T6230 WHERE T6251.F03 = T6230.F01 AND T6251.F03 = T6231.F01 "
                    + "AND T6251.F04 = ? AND T6230.F20 = ? AND T6251.F08 = ? AND T6231.F19 = ? AND DATE_ADD( ?, INTERVAL 3 DAY ) < T6231.F06 AND T6251.F07 > 0 AND ? >= DATE_ADD(T6251.F09, INTERVAL ? DAY ) ORDER BY T6231.F12 DESC";
            return selectPaging(connection,
                new ArrayParser<MaySettleFinacing>()
                {
                    @Override
                    public MaySettleFinacing[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<MaySettleFinacing> list = null;
                        while (resultSet.next())
                        {
                            MaySettleFinacing record = new MaySettleFinacing();
                            record.F01 = resultSet.getString(1);
                            record.F02 = resultSet.getInt(2);
                            record.F03 = resultSet.getBigDecimal(3);
                            record.F04 = resultSet.getInt(4);
                            record.F05 = resultSet.getInt(5);
                            record.F06 = resultSet.getTimestamp(6);
                            record.F07 = resultSet.getBigDecimal(7);
                            record.F08 = resultSet.getInt(8);
                            try
                            {
                                record.money = getDsbx(connection, record.F08);
                            }
                            catch (Throwable e)
                            {
                                logger.error(e, e);
                            }
                            record.F09 = Formater.formatAmount(record.money);
                            record.F10 = Formater.formatRate(record.F07);
                            record.F11 = TimestampParser.format(record.F06, "yyyy-MM-dd");
                            record.F12 = Formater.formatAmount(record.F03);
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            list.add(record);
                        }
                        return list == null || list.size() == 0 ? null
                            : list.toArray(new MaySettleFinacing[list.size()]);
                    }
                },
                paging,
                sql,
                serviceResource.getSession().getAccountId(),
                T6230_F20.HKZ,
                T6251_F08.F,
                T6231_F18.F,
                timestamp,
                timestamp,
                serviceResource.getResource(ConfigureProvider.class).getProperty(SystemVariable.ZQZR_CY_DAY));
        }
    }
    
    // 待收本息
    private BigDecimal getDsbx(Connection connection, int zqId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F11 = ? AND T6252.F05 IN (?,?) AND T6252.F09 = ?"))
        {
            pstmt.setInt(1, zqId);
            pstmt.setInt(2, FeeCode.TZ_LX);
            pstmt.setInt(3, FeeCode.TZ_BJ);
            pstmt.setString(4, T6252_F09.WH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getBigDecimal(1);
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public PagingResult<OutSellFinacing> getOutSellFinacing(Paging paging)
        throws Throwable
    {
        String sql =
            "SELECT T6262.F02 AS F01, T6262.F03 AS F02, T6262.F04 AS F03, T6262.F05 AS F04, T6262.F06 AS F05, T6262.F07 AS F06, T6262.F08 AS F07, T6262.F09 AS F08 ,T6251.F03 F09,T6251.F02 F10 FROM S62.T6251 INNER JOIN S62.T6260 ON T6260.F02 = T6251.F01 INNER JOIN S62.T6262 ON T6260.F01 = T6262.F02  WHERE T6251.F04 = ? ORDER BY T6262.F07 DESC";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<OutSellFinacing>()
            {
                @Override
                public OutSellFinacing[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<OutSellFinacing> list = null;
                    while (resultSet.next())
                    {
                        OutSellFinacing record = new OutSellFinacing();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.jkbId = resultSet.getInt(9);
                        record.zqNub = resultSet.getString(10);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new OutSellFinacing[list.size()]);
                }
            }, paging, sql, serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public PagingResult<InSellFinacing> getInSellFinacing(Paging paging)
        throws Throwable
    {
        String sql =
            "SELECT T6262.F01 AS F111,  T6262.F02 AS F01, T6262.F03 AS F02, T6262.F04 AS F03, T6262.F05 AS F04, T6262.F06 AS F05, T6262.F07 AS F06, T6262.F08 AS F07, T6262.F09 AS F08, T6260.F01 AS F09, T6251.F03 AS F10 FROM S62.T6251 INNER JOIN S62.T6260 ON T6260.F02 = T6251.F01 INNER JOIN S62.T6262 ON T6260.F01 = T6262.F02 WHERE T6262.F03 = ? ORDER BY T6262.F07 DESC";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<InSellFinacing>()
            {
                @Override
                public InSellFinacing[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<InSellFinacing> list = null;
                    while (resultSet.next())
                    {
                        InSellFinacing record = new InSellFinacing();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getBigDecimal(9);
                        record.assestsID = resultSet.getInt(10);
                        record.jkbId = resultSet.getInt(11);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new InSellFinacing[list.size()]);
                }
            }, paging, sql, serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public void cancel(int zcbId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            int userId = serviceResource.getSession().getAccountId();
            String zqNumber = "";
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F08, F02 FROM S62.T6251 WHERE F01 = (SELECT F02 FROM S62.T6260 WHERE F01=?) AND F04 = ?");)
            {
                ps.setInt(1, zcbId);
                ps.setInt(2, userId);
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        if (EnumParser.parse(T6251_F08.class, rs.getString(1)) == T6251_F08.F)
                        {
                            throw new LogicalException("该债权已下架！");
                        }
                        zqNumber = rs.getString(2);
                    }
                    else
                    {
                        //防止取消他人的债权
                        throw new LogicalException("债权不存在！");
                    }
                    
                }
            }
            
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6260 SET F07 = ? WHERE F01 = ?");)
                {
                    ps.setString(1, T6260_F07.YQX.toString());
                    ps.setInt(2, zcbId);
                    ps.executeUpdate();
                }
                
                try (PreparedStatement pss =
                    connection.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 = (SELECT F02 FROM S62.T6260 WHERE F01=?) AND F04 = ?");)
                {
                    pss.setString(1, T6251_F08.F.name());
                    pss.setInt(2, zcbId);
                    pss.setInt(3, userId);
                    pss.executeUpdate();
                }
                
                T6110 t6110 = selectT6110(connection, userId);
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                Envionment envionment = configureProvider.createEnvionment();
                envionment.set("userName", t6110.F02);
                envionment.set("title", zqNumber);
                String content = configureProvider.format(LetterVariable.ZQ_MANUAL_CANCEL, envionment);
                sendLetter(connection, userId, "手动下架债权", content);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * 转让债权
     * @param zcbId
     * @throws Throwable
     */
    @Override
    public void transfer(addTransfer addTransfer, String tranPwd)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (isOpenWithPsd)
        { //网关投资需要验证交易密码
            if (StringUtils.isEmpty(tranPwd))
            {
                throw new LogicalException("交易密码不能为空！");
            }
            final int accountId = serviceResource.getSession().getAccountId();
            try (Connection connection = getConnection())
            {
                int maxCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
                int count = psdInputCount(connection);
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
        transfer(addTransfer);
    }
    
    private String selectTranPwd(Connection connection, int UserId)
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

    @Override
    public void transfer(addTransfer query)
        throws Throwable
    {
        if (query == null)
        {
            throw new ParameterException("没有线上债权转让信息");
        }
        BigDecimal money = null;
        {
            money = query.getTransferValue();
            if (money.compareTo(BigDecimal.ONE) < 0)
            {
                throw new ParameterException("转让价格不能小于1");
            }
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                int userId = serviceResource.getSession().getAccountId();
                int id = query.getTransferId();// 债权ID
                T6251 t6251 = selectT6251(connection, id);
                if (t6251 == null)
                {
                    throw new LogicalException("债权记录不存在！");
                }
                int day = IntegerParser.parse(serviceResource.getResource(ConfigureProvider.class).getProperty(SystemVariable.ZQZR_CY_DAY));
                if(day > 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(t6251.F09);
                    calendar.add(Calendar.DAY_OF_MONTH, day);
                    if (!DateHelper.beforeDate(calendar.getTime(), new Date())) {
                        throw new LogicalException("该债权不能转让！");
                    }
                }
                if (t6251.F08 == T6251_F08.S)
                {
                    throw new LogicalException("该债权正在转让！");
                }
                if (userId != t6251.F04)
                {
                    //防止转让他人的债权
                    throw new LogicalException("债权记录不存在！");
                }
                
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT 1 FROM S62.T6251 INNER JOIN S62.T6252 ON T6252.F11 = T6251.F01 AND T6252.F09 = 'HKZ' WHERE T6251.F01 = ? LIMIT 1 "))
                {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            throw new LogicalException("该债权正在还款中，不能转让");
                        }
                    }
                }
                BigDecimal transferAmount = query.getBidValue();
                if (transferAmount == null || transferAmount.compareTo(BigDecimal.ZERO) <= 0)
                {
                    throw new ParameterException("转让债权必须大于0");
                }
                if (t6251.F07.compareTo(transferAmount) < 0)
                {
                    throw new ParameterException("转让债权必须小于持有债权。");
                }
                if (isExpired(connection, id))
                {
                    throw new ParameterException("该债权有逾期未还的借款，无法转让。");
                }
                int zqsqId =
                    insert(connection,
                        "INSERT INTO S62.T6260 ( F02, F03, F04, F05, F06, F07, F08 ) VALUES (?,?,?,?,?,?,?)",
                        id,
                        money,
                        t6251.F07,
                        getCurrentTimestamp(connection),
                        getEndDate(connection, id),
                        T6260_F07.DSH,
                        query.getRateMoney());
                
                try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6251 SET F08 = ? WHERE F01 = ?");)
                {
                    ps.setString(1, T6251_F08.S.name());
                    ps.setInt(2, id);
                    ps.executeUpdate();
                }
                
                insert(connection, "INSERT INTO S62.T6261 SET F01 = ?, F02 = ?", zqsqId, selectint(connection));
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private int selectint(Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S51.T5125 WHERE T5125.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, XyType.ZQZRXY);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
            return 0;
        }
    }
    
    private boolean isExpired(Connection connection, int F11)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(*) FROM S62.T6252 WHERE T6252.F08 < ? AND T6252.F09 = ? AND T6252.F11 = ? "))
        {
            pstmt.setDate(1, getCurrentDate(connection));
            pstmt.setString(2, T6252_F09.WH.name());
            pstmt.setInt(3, F11);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1) > 0;
                }
                else
                {
                    return false;
                }
            }
        }
    }
    
    private Date getEndDate(Connection connection, int F11)
        throws Throwable
    {
        java.sql.Date date = getCurrentDate(connection);
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT DISTINCT F08 FROM S62.T6252 WHERE T6252.F08 >= ? AND T6252.F09 = ? AND T6252.F11 = ? ORDER BY F06 ASC"))
        {
            pstmt.setDate(1, date);
            pstmt.setString(2, T6252_F09.WH.name());
            pstmt.setInt(3, F11);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    Date nextDate = resultSet.getDate(1);
                    nextDate.setTime(nextDate.getTime() - DateHelper.DAY_IN_MILLISECONDS * 3);
                    if (DateHelper.beforeOrMatchDate(nextDate, date))
                    {
                        throw new LogicalException("离下个还款日少于3天，不能转让。");
                    }
                    return nextDate;
                }
                else
                {
                    throw new LogicalException("不存在未还记录。");
                }
            }
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
    
    /**
     * 查询预计收益
     * 
     * @param loanId
     * @return
     * @throws Throwable
     */
    @Override
    public BigDecimal getDslx(int loanId)
        throws Throwable
    {
        String sql = "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND F04 = ? AND F09 = ? AND F05 IN (?,?)";
        BigDecimal bigDecimal = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql);)
            {
                ps.setInt(1, loanId);
                ps.setInt(2, serviceResource.getSession().getAccountId());
                ps.setString(3, T6252_F09.WH.name());
                ps.setInt(4, FeeCode.TZ_BJ);
                ps.setInt(5, FeeCode.TZ_LX);
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                    
                }
            }
            return bigDecimal;
        }
    }
    
    @Override
    public int getCrid()
        throws Throwable
    {
        StringBuffer sb = new StringBuffer();
        sb.append(" select max(F02) from  ");
        sb.append(" S62.T6260 where date_format(F05,'%Y-%m')=date_format(?,'%Y-%m') ");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Integer>()
            {
                @Override
                public Integer parse(ResultSet rs)
                    throws SQLException
                {
                    int id = 0;
                    if (rs.next())
                    {
                        id = rs.getInt(1);
                    }
                    return id;
                }
            }, sb.toString(), getCurrentTimestamp(connection));
        }
    }
    @Override
    public PagingResult<InSellFinacingExt> getInSellFinacingExt(Paging paging)
        throws Throwable
    {
        
        String sql =
            "SELECT T6262.F01 AS F111,  T6262.F02 AS F01, T6262.F03 AS F02, T6262.F04 AS F03, T6262.F05 AS F04, T6262.F06 AS F05, T6262.F07 AS F06, T6262.F08 AS F07, T6262.F09 AS F08, T6260.F01 AS F09, T6251.F03 AS F10, T6231.F03 AS F11, T6231.F02 AS F12, T6230.F06 AS F13 FROM S62.T6251 INNER JOIN S62.T6260 ON T6260.F02 = T6251.F01 INNER JOIN S62.T6262 ON T6260.F01 = T6262.F02 INNER JOIN S62.T6231 ON T6251.F03 = T6231.F01 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 WHERE T6262.F03 = ? ORDER BY T6262.F07 DESC";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<InSellFinacingExt>()
            {
                @Override
                public InSellFinacingExt[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<InSellFinacingExt> list = null;
                    while (resultSet.next())
                    {
                        InSellFinacingExt record = new InSellFinacingExt();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getBigDecimal(9);
                        record.assestsID = resultSet.getInt(10);
                        record.jkbId = resultSet.getInt(11);
                        record.setSubTerm(resultSet.getInt(12));
                        record.setTotalTerm(resultSet.getInt(13));
                        record.setRate(resultSet.getBigDecimal(14));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new InSellFinacingExt[list.size()]);
                }
            }, paging, sql, serviceResource.getSession().getAccountId());
        }
        //		return null;
    }
    
    private int psdInputCount(Connection connection)
        throws Throwable
    {
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
    
    private void addInputCount(Connection connection, int accountId)
        throws Throwable
    {
        execute(connection, "UPDATE S61.T6110 SET F11 = F11+1 WHERE F01 = ?", accountId);
    }
    
}
