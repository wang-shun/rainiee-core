package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6230_Ext;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.enums.T6230_Ext_F04;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F19;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.common.enums.PaymentStatus;
import com.dimeng.p2p.modules.bid.user.service.WdjkManage;
import com.dimeng.p2p.modules.bid.user.service.entity.ForwardRepayInfo;
import com.dimeng.p2p.modules.bid.user.service.entity.HkEntity;
import com.dimeng.p2p.modules.bid.user.service.entity.LoanCount;
import com.dimeng.p2p.modules.bid.user.service.entity.RepayInfo;
import com.dimeng.p2p.modules.bid.user.service.entity.RepayLoanDetail;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.EnumParser;

public class WdjkManageImpl extends AbstractBidManage implements WdjkManage
{
    
    public WdjkManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public LoanCount getMyLoanCount()
        throws Throwable
    {
        LoanCount loanCount = new LoanCount();
        loanCount.countMoney = getJkje();
        loanCount.newRepayMoney = get30dh();
        loanCount.overdueMoney = getYqje();
        loanCount.repayMoney = getWhje();
        return loanCount;
    }
    
    /**
     * 成功借款金额
     * 
     * @return
     * @throws Throwable
     */
    private BigDecimal getJkje()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F05-F07),0) FROM S62.T6230 WHERE F02 = ? AND F20 IN (?,?,?,?)"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6230_F20.HKZ.name());
                pstmt.setString(3, T6230_F20.YDF.name());
                pstmt.setString(4, T6230_F20.YJQ.name());
                pstmt.setString(5, T6230_F20.YZR.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    /**
     * 逾期金额
     */
    private BigDecimal getYqje()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F03 = ? AND DATEDIFF(?,F08)>0 AND F09 = ? AND F05 IN (?,?,?,?)"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setDate(2, getCurrentDate(connection));
                pstmt.setString(3, T6252_F09.WH.name());
                pstmt.setInt(4, FeeCode.TZ_BJ);
                pstmt.setInt(5, FeeCode.TZ_LX);
                pstmt.setInt(6, FeeCode.TZ_FX);
                pstmt.setInt(7, FeeCode.TZ_WYJ);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    /**
     * 未还金额
     */
    private BigDecimal getWhje()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F03 = ?  AND F09 = ? AND F05 IN (?,?,?,?)"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6252_F09.WH.name());
                pstmt.setInt(3, FeeCode.TZ_BJ);
                pstmt.setInt(4, FeeCode.TZ_LX);
                pstmt.setInt(5, FeeCode.TZ_FX);
                pstmt.setInt(6, FeeCode.TZ_WYJ);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    /**
     * 近30天待还
     */
    private BigDecimal get30dh()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Calendar calendar = Calendar.getInstance();
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F03 = ?  AND F09 = ? AND F05 IN (?,?,?,?) AND F08>=? AND F08<=?"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6252_F09.WH.name());
                pstmt.setInt(3, FeeCode.TZ_BJ);
                pstmt.setInt(4, FeeCode.TZ_LX);
                pstmt.setInt(5, FeeCode.TZ_FX);
                pstmt.setInt(6, FeeCode.TZ_WYJ);
                
                pstmt.setDate(7, new Date(calendar.getTimeInMillis()));
                calendar.add(Calendar.DAY_OF_MONTH, 30);
                pstmt.setDate(8, new Date(calendar.getTimeInMillis()));
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    /**
     * 根据标ID 查询已还清的标的还款总额
     * 
     * @param loanId
     * @return
     */
    private BigDecimal getHkze(Connection connection, int loanId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F03 = ?  AND F09 = ? AND F02 = ?"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            pstmt.setString(2, T6252_F09.YH.name());
            pstmt.setInt(3, loanId);
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
    
    /**
     * 根据标ID 查询还款中下期的还款金额
     * 
     * @param loanId
     * @return
     */
    private BigDecimal getXqhkje(Connection connection, int loanId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F03 = ? AND T6252.F09 = ? AND F06 = (SELECT MIN(F06) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F03=? AND T6252.F09 = ?)"))
        {
            pstmt.setInt(1, loanId);
            pstmt.setInt(2, serviceResource.getSession().getAccountId());
            pstmt.setString(3, T6252_F09.WH.name());
            pstmt.setInt(4, loanId);
            pstmt.setInt(5, serviceResource.getSession().getAccountId());
            pstmt.setString(6, T6252_F09.WH.name());
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
    
    /**
     * 根据标ID 查询t6231 判读是否逾期
     * 
     * @param loanId
     * @return
     */
    private T6231 getT6231(Connection connection, int loanId)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F19 FROM S62.T6231 WHERE F01 = ?"))
        {
            pstmt.setInt(1, loanId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    T6231 t6231 = new T6231();
                    t6231.F19 = T6231_F19.parse(resultSet.getString(1));
                    return t6231;
                }
            }
        }
        return new T6231();
    }
    
    @Override
    public PagingResult<HkEntity> getHkzJk(Paging paging)
        throws Throwable
    {
        // 分页结果
        PagingResult<HkEntity> resultList = null;
        try
        {
            // 打开数据连接
            try (final Connection connection = getConnection())
            {
                // 参数
                ArrayList<Object> parameters = new ArrayList<>();
                String sql =
                    "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26 FROM S62.T6230 WHERE T6230.F02 = ? AND T6230.F20 IN (?,?) ORDER BY T6230.F01 DESC";//改变输出序列,前台才能倒序输出
                
                // 用户名
                parameters.add(serviceResource.getSession().getAccountId());
                parameters.add(T6230_F20.HKZ.name());
                parameters.add(T6230_F20.YDF.name());
                
                // 执行查询
                resultList = selectPaging(connection, new ArrayParser<HkEntity>()
                {
                    @Override
                    public HkEntity[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<HkEntity> list = null;
                        while (resultSet.next())
                        {
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            HkEntity record = new HkEntity();
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
                            record.repayInfo = getHkRepay(record.F01);
                            record.forwardRepayInfo = getForwardHkRepay(connection, record.F01);
                            record.leftNum = getLeftNum(connection, record.F01);
                            record.totalNum = getTotalNum(connection, record.F01);
                            try
                            {
                                record.dqyhje = getXqhkje(connection, record.F01);
                                record.F28 = getT6231(connection, record.F01);
                            }
                            catch (Throwable throwable)
                            {
                                logger.error("WdjkManageImpl.getHkzJk() set recode error", throwable);
                            }
                            try
                            {
                                record.ext = selectT6230_Ext(connection, record.F02, record.F01);
                            }
                            catch (Throwable e)
                            {
                                logger.error("WdjkManageImpl.getHkzJk() set ext error", e);
                            }
                            list.add(record);
                        }
                        return list == null || list.size() == 0 ? null : list.toArray(new HkEntity[list.size()]);
                    }
                }, paging, sql, parameters);
            }
        }
        catch (Exception e)
        {
            logger.error("WdjkManageImpl.getHkzJk() error:", e);
        }
        return resultList;
    }
    
    private T6230_Ext selectT6230_Ext(Connection connection, int F02, int F03)
        throws Throwable
    {
        T6230_Ext record = null;
        try (PreparedStatement sts =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05 FROM S62.T6230_EXT WHERE F02=? AND F03=?  AND F04='S' "))
        {
            sts.setInt(1, F02);
            sts.setInt(2, F03);
            try (ResultSet set = sts.executeQuery())
            {
                if (set.next())
                {
                    record = new T6230_Ext();
                    record.setF01(set.getInt(1));
                    record.setF02(set.getInt(2));
                    record.setF03(set.getInt(3));
                    record.setF04(T6230_Ext_F04.parse(set.getString(4)));
                    record.setF05(set.getString(5));
                }
            }
        }
        return record;
    }
    
    /**
     * 查询剩余期数
     * @param connecction
     * @param bid
     * @return
     * @throws SQLException
     */
    private int getLeftNum(Connection connection, int bid)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(1) FROM (SELECT COUNT(1) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F09 = ? GROUP BY T6252.F06) T"))
        {
            pstmt.setInt(1, bid);
            pstmt.setString(2, T6252_F09.WH.name());
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     * 查询总期数
     * @param connecction
     * @param bid
     * @return
     * @throws SQLException
     */
    private int getTotalNum(Connection connecction, int bid)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connecction.prepareStatement("SELECT COUNT(1) FROM (SELECT COUNT(1) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F09 <> ? GROUP BY T6252.F06) T"))
        {
            pstmt.setInt(1, bid);
            pstmt.setString(2, T6252_F09.DF.name());
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     * app获取用户借款
     */
    @Override
    public PagingResult<HkEntity> getMobileHkzJk(Paging paging)
        throws Throwable
    {
        try (final Connection connection = getConnection())
        {
            String sql =
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26 FROM S62.T6230 WHERE T6230.F02 = ? AND T6230.F20 IN (?,?) ORDER BY T6230.F01 DESC";
            return selectPaging(connection, new ArrayParser<HkEntity>()
            {
                @Override
                public HkEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<HkEntity> list = null;
                    while (resultSet.next())
                    {
                        HkEntity record = new HkEntity();
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
                        try
                        {
                            record.dqyhje = getXqhkje(connection, record.F01);
                        }
                        catch (Throwable e)
                        {
                        }
                        record.repayInfo = getMobileHkRepay(connection, record.F01);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new HkEntity[list.size()]);
                }
            }, paging, sql, serviceResource.getSession().getAccountId(), T6230_F20.HKZ.name(), T6230_F20.YDF.name());
        }
    }
    
    /**
     * app
     * @param loanId
     * @return
     * @throws SQLException
     */
    private RepayInfo getMobileHkRepay(Connection connection, int loanId)
        throws SQLException
    {
        String sql =
            "SELECT F02,F06,F05,F07 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F03 = ? AND T6252.F09 = ? AND F06 = (SELECT MIN(F06) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F03=? AND T6252.F09 = ?)";
        List<Object> paramters = new ArrayList<>();
        paramters.add(loanId);
        paramters.add(serviceResource.getSession().getAccountId());
        paramters.add(T6252_F09.WH.name());
        paramters.add(loanId);
        paramters.add(serviceResource.getSession().getAccountId());
        paramters.add(T6252_F09.WH.name());
        final RepayInfo repayInfo = select(connection, new ItemParser<RepayInfo>()
        {
            
            @Override
            public RepayInfo parse(ResultSet rs)
                throws SQLException
            {
                final RepayInfo repayInfo = new RepayInfo();
                BigDecimal loanMustMoney = new BigDecimal(0);
                BigDecimal overdueInterest = new BigDecimal(0);
                while (rs.next())
                {
                    repayInfo.loanID = rs.getInt(1);
                    repayInfo.number = rs.getInt(2);
                    int typeId = rs.getInt(3);
                    BigDecimal amount = rs.getBigDecimal(4);
                    if (FeeCode.TZ_BJ == typeId || FeeCode.TZ_LX == typeId)
                    {
                        loanMustMoney = loanMustMoney.add(amount);
                    }
                    if (FeeCode.TZ_FX == typeId)
                    {
                        overdueInterest = overdueInterest.add(amount);
                    }
                    if (FeeCode.TZ_YQGLF == typeId)
                    {
                        repayInfo.overdueManage = amount;
                    }
                    if (FeeCode.JK_GLF == typeId)
                    {
                        repayInfo.loanManageAmount = amount;
                    }
                    repayInfo.loanMustMoney = loanMustMoney;
                    repayInfo.overdueInterest = overdueInterest;
                    repayInfo.loanArrMoney = repayInfo.overdueInterest.add(repayInfo.overdueManage);
                    if (FeeCode.TZ_BJ == typeId)
                    {
                        repayInfo.yhbj = amount;
                    }
                    repayInfo.loanTotalMoney =
                        repayInfo.loanMustMoney.add(repayInfo.loanManageAmount).add(repayInfo.loanArrMoney);
                }
                return repayInfo;
            }
        },
            sql,
            paramters);
        //repayInfo.accountAmount = getAmount();
        //TODO
        return repayInfo;
    }
    
    /**
     * app已还清的借款
     * @return
     * @throws Throwable
     */
    @Override
    public PagingResult<HkEntity> getMobileYhqJk(Paging paging)
        throws Throwable
    {
        try (final Connection connection = getConnection())
        {
            String sql =
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26 FROM S62.T6230 WHERE T6230.F02 = ? AND T6230.F20 = ? ORDER BY T6230.F01 DESC";
            return selectPaging(connection, new ArrayParser<HkEntity>()
            {
                @Override
                public HkEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<HkEntity> list = null;
                    while (resultSet.next())
                    {
                        HkEntity record = new HkEntity();
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
                        try
                        {
                            record.hkTotle = getHkze(connection, record.F01);
                        }
                        catch (Throwable e)
                        {
                            logger.error("WdjkManageImpl.getMobileYhqJk() error", e);
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new HkEntity[list.size()]);
                }
            }, paging, sql, serviceResource.getSession().getAccountId(), T6230_F20.YJQ.name());
        }
    }
    
    @Override
    public PagingResult<HkEntity> getYhqJk(Paging paging)
        throws Throwable
    {
        // 分页结果
        PagingResult<HkEntity> resultList = null;
        try
        {
            // 打开数据连接
            try (final Connection connection = getConnection())
            {
                // 参数
                ArrayList<Object> parameters = new ArrayList<>();
                String sql =
                    "SELECT T6230.F01, T6230.F02, T6230.F03, T6230.F04, T6230.F05, T6230.F06, T6230.F07, T6230.F08, T6230.F09, T6230.F10, T6230.F11, T6230.F12, T6230.F13, T6230.F14, T6230.F15, T6230.F16, T6230.F17, T6230.F18, T6230.F19, T6230.F20, T6230.F21, T6230.F22, T6230.F23, T6230.F24, T6230.F25, (SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F05 = '7001') AS F26, T6231.F21 AS F27, T6231.F22 AS F28 FROM S62.T6230, S62.T6231 WHERE T6230.F01 = T6231.F01 AND T6230.F02 = ? AND T6230.F20 = ? ORDER BY T6230.F01 DESC";
                
                // 用户名
                parameters.add(serviceResource.getSession().getAccountId());
                parameters.add(T6230_F20.YJQ.name());
                // 执行查询
                resultList = selectPaging(connection, new ArrayParser<HkEntity>()
                {
                    @Override
                    public HkEntity[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<HkEntity> list = null;
                        while (resultSet.next())
                        {
                            HkEntity record = new HkEntity();
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
                            try
                            {
                                record.hkTotle = getHkze(connection, record.F01);
                            }
                            catch (Throwable throwable)
                            {
                                logger.error("WdjkManageImpl.getYhqJk() getHkze() error:", throwable);
                            }
                            T6231 t6231 = new T6231();
                            t6231.F21 = T6231_F21.parse(resultSet.getString(27));
                            t6231.F22 = resultSet.getInt(28);
                            record.F28 = t6231;
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            list.add(record);
                        }
                        return list == null || list.size() == 0 ? null : list.toArray(new HkEntity[list.size()]);
                    }
                }, paging, sql, parameters);
            }
        }
        catch (Exception e)
        {
            logger.error("WdjkManageImpl.getYhqJk() error:", e);
        }
        return resultList;
    }
    
    @Override
    public PagingResult<RepayLoanDetail> getRepayLoanDetail(final int loanId, Paging paging)
        throws Throwable
    {
        
        //final LinkedHashMap<Integer, RepayLoanDetail> maps = new LinkedHashMap<Integer, RepayLoanDetail>();
        try (final Connection connection = getConnection())
        {
            String sql =
                "SELECT F02,F06,SUM(F07),F08,F09 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F03= ? AND T6252.F09 <> 'DF'  GROUP BY F06 ";
            return selectPaging(connection, new ArrayParser<RepayLoanDetail>()
            {
                @Override
                public RepayLoanDetail[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<RepayLoanDetail> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        RepayLoanDetail detail = new RepayLoanDetail();
                        detail.F02 = resultSet.getInt(1);
                        detail.F06 = resultSet.getInt(2);
                        detail.F07 = resultSet.getBigDecimal(3);
                        detail.F08 = resultSet.getDate(4);
                        
                        int days = getOverduePay(connection, detail.F02, detail.F06);
                        
                        String F09 = resultSet.getString(5);
                        if (T6252_F09.TQH.name().equals(F09))
                        {
                            F09 = T6252_F09.YH.name();
                        }
                        
                        detail.paymentStatus = EnumParser.parse(PaymentStatus.class, F09);
                        if (detail.paymentStatus != PaymentStatus.HKZ && detail.paymentStatus != PaymentStatus.YH)
                        {
                            if (days > 0 && days <= 30)
                            {
                                detail.paymentStatus = PaymentStatus.YQ;
                            }
                            else if (days > 0 && days >= 31)
                            {
                                detail.paymentStatus = PaymentStatus.YZYQ;
                            }
                        }
                        detail.repayInfo = getRepay(connection, detail.F02, detail.F06);
                        lists.add(detail);
                    }
                    
                    return lists.toArray(new RepayLoanDetail[lists.size()]);
                }
            }, paging, sql, loanId, serviceResource.getSession().getAccountId());
        }
    }
    
    private RepayInfo getHkRepay(int loanId)
        throws SQLException
    {
        String sql =
            "SELECT F02,F06,F05,F07 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F03 = ? AND T6252.F09 = ? AND F06 = (SELECT MIN(F06) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F03=? AND T6252.F09 = ?)";
        List<Object> paramters = new ArrayList<>();
        paramters.add(loanId);
        paramters.add(serviceResource.getSession().getAccountId());
        paramters.add(T6252_F09.WH.name());
        paramters.add(loanId);
        paramters.add(serviceResource.getSession().getAccountId());
        paramters.add(T6252_F09.WH.name());
        try (Connection connection = getConnection())
        {
            final RepayInfo repayInfo = select(connection, new ItemParser<RepayInfo>()
            {
                
                @Override
                public RepayInfo parse(ResultSet rs)
                    throws SQLException
                {
                    final RepayInfo repayInfo = new RepayInfo();
                    BigDecimal loanMustMoney = new BigDecimal(0);
                    BigDecimal overdueInterest = new BigDecimal(0);
                    while (rs.next())
                    {
                        repayInfo.loanID = rs.getInt(1);
                        repayInfo.number = rs.getInt(2);
                        int typeId = rs.getInt(3);
                        BigDecimal amount = rs.getBigDecimal(4);
                        if (FeeCode.TZ_BJ == typeId || FeeCode.TZ_LX == typeId)
                        {
                            loanMustMoney = loanMustMoney.add(amount);
                        }
                        if (FeeCode.TZ_FX == typeId)
                        {
                            overdueInterest = overdueInterest.add(amount);
                        }
                        if (FeeCode.TZ_YQGLF == typeId)
                        {
                            repayInfo.overdueManage = amount;
                        }
                        if (FeeCode.JK_GLF == typeId)
                        {
                            repayInfo.loanManageAmount = amount;
                        }
                        repayInfo.loanMustMoney = loanMustMoney;
                        repayInfo.overdueInterest = overdueInterest;
                        repayInfo.loanArrMoney = repayInfo.overdueInterest.add(repayInfo.overdueManage);
                        if (FeeCode.TZ_BJ == typeId)
                        {
                            repayInfo.yhbj = amount;
                        }
                        repayInfo.loanTotalMoney =
                            repayInfo.loanMustMoney.add(repayInfo.loanManageAmount).add(repayInfo.loanArrMoney);
                    }
                    return repayInfo;
                }
            },
                sql,
                paramters);
            repayInfo.accountAmount = getAmount(connection);
            return repayInfo;
        }
    }
    
    /**
     * 计算提前还款信息<一句话功能简述>
     * <功能详细描述>
     * @param loanId
     * @return
     * @throws SQLException
     */
    private ForwardRepayInfo getForwardHkRepay(final Connection connection, final int loanId)
        throws SQLException
    {
        String sql =
            "SELECT F02 AS F01, F06 AS F02 ,F05 AS F03 ,F07 AS F04,(SELECT MIN(F06) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F03=? AND T6252.F09 = ?) AS F05 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F03 = ? AND T6252.F09 = ? ";
        List<Object> paramters = new ArrayList<>();
        paramters.add(loanId);
        paramters.add(serviceResource.getSession().getAccountId());
        paramters.add(T6252_F09.WH.name());
        paramters.add(loanId);
        paramters.add(serviceResource.getSession().getAccountId());
        paramters.add(T6252_F09.WH.name());
        
        final ForwardRepayInfo repayInfo = select(connection, new ItemParser<ForwardRepayInfo>()
        {
            
            @Override
            public ForwardRepayInfo parse(ResultSet rs)
                throws SQLException
            {
                
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                BigDecimal forwardRp =
                    BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.FORWARD_REPAY_POUNDAGE));
                BigDecimal forwardBp =
                    BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.FORWARD_REPAY_BREACH_POUNDAGE));
                final ForwardRepayInfo repayInfo = new ForwardRepayInfo();
                BigDecimal loanMustMoney = new BigDecimal(0); //当前期的本息
                BigDecimal sybj = new BigDecimal(0); //剩余本金
                BigDecimal sylx = new BigDecimal(0); //剩余利息
                int minNumber = 0;
                while (rs.next())
                {
                    
                    repayInfo.loanID = rs.getInt(1);
                    repayInfo.number = rs.getInt(2);
                    int typeId = rs.getInt(3);
                    BigDecimal amount = rs.getBigDecimal(4);
                    minNumber = rs.getInt(5);
                    //计算当前期的本息
                    if (repayInfo.number == minNumber)
                    {
                        if (FeeCode.TZ_BJ == typeId || FeeCode.TZ_LX == typeId)
                        {
                            loanMustMoney = loanMustMoney.add(amount);
                        }
                    }
                    //计算剩余本金
                    else if (repayInfo.number > minNumber)
                    {
                        if (FeeCode.TZ_BJ == typeId)
                        {
                            sybj = sybj.add(amount);
                        }
                        if (FeeCode.TZ_LX == typeId)
                        {
                            sylx = sylx.add(amount);
                        }
                    }
                }
                repayInfo.number = minNumber;
                repayInfo.sybj = sybj;
                repayInfo.sylx = sylx;
                repayInfo.loanManageAmount = repayInfo.sybj.multiply(forwardRp).setScale(2, BigDecimal.ROUND_HALF_UP);
                repayInfo.loanPenalAmount = getWyj(connection, loanId, minNumber, forwardBp);
                repayInfo.loanMustMoney = loanMustMoney;
                repayInfo.loanTotalMoney =
                    loanMustMoney.add(repayInfo.sybj)
                        .add(repayInfo.loanManageAmount)
                        .add(repayInfo.loanPenalAmount)
                        .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                
                return repayInfo;
            }
        },
            sql,
            paramters);
        repayInfo.accountAmount = getAmount(connection);
        return repayInfo;
        
    }
    
    private RepayInfo getRepay(Connection connection, int loanId, int number)
        throws SQLException
    {
        
        RepayInfo repayInfo = new RepayInfo();
        repayInfo.loanID = loanId;
        repayInfo.number = number;
        BigDecimal bj = getMoney(connection, loanId, number, FeeCode.TZ_BJ);
        BigDecimal lx = getMoney(connection, loanId, number, FeeCode.TZ_LX);
        repayInfo.loanMustMoney = bj.add(lx);
        repayInfo.overdueInterest = getMoney(connection, loanId, number, FeeCode.TZ_FX);
        BigDecimal yxglf = getMoney(connection, loanId, number, FeeCode.TZ_YQGLF);
        repayInfo.loanArrMoney = repayInfo.overdueInterest.add(yxglf);
        repayInfo.loanTotalMoney = repayInfo.loanMustMoney.add(repayInfo.loanManageAmount).add(repayInfo.loanArrMoney);
        repayInfo.accountAmount = getAmount(connection);
        return repayInfo;
    }
    
    private BigDecimal getMoney(Connection connection, int loanId, int number, int typeId)
        throws SQLException
    {
        String sql = "SELECT SUM(F07) FROM S62.T6252 WHERE F02=?  AND F06=? AND F05 = ? AND T6252.F09 <> 'DF'";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, loanId);
            ps.setInt(2, number);
            ps.setInt(3, typeId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getBigDecimal(1);
                }
            }
        }
        return null;
    }
    
    /**
     * 查询账户可用余额
     */
    public BigDecimal getAmount(Connection connection)
        throws SQLException
    {
        String selectT6023 = "SELECT F06 FROM S61.T6101 WHERE F02=? AND F03=?";
        BigDecimal acountAmount =
            selectBigDecimal(connection, selectT6023, serviceResource.getSession().getAccountId(), T6101_F03.WLZH);
        return acountAmount == null ? new BigDecimal(0) : acountAmount;
    }
    
    /**
     * 查询提前还款违约金
     */
    private BigDecimal getWyj(Connection connection, int F02, int F06, BigDecimal forwardBp)
        throws SQLException
    {
        BigDecimal wyj = BigDecimal.ZERO;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 > ? AND T6252.F09 = ? AND T6252.F05 = ? GROUP BY T6252.F04"))
        {
            pstmt.setInt(1, F02);
            pstmt.setInt(2, F06);
            pstmt.setString(3, T6252_F09.WH.name());
            pstmt.setInt(4, FeeCode.TZ_BJ);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    wyj = wyj.add(resultSet.getBigDecimal(1).multiply(forwardBp).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
        return wyj;
    }
    
    private int getOverduePay(Connection connection, int bid, int qh)
        throws SQLException
    {
        // 查询逾期天数
        String sql = "SELECT DATEDIFF(CURDATE(),F08) FROM S62.T6252 WHERE F02=? AND F06=? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, bid);
            ps.setInt(2, qh);
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
    
    @Override
    public boolean isWf(int loanId, int repayId)
        throws Throwable
    {
        String sql = "SELECT F01 FROM S62.T6252 WHERE F09=? AND F02=? AND F01<?";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setInt(2, loanId);
                ps.setInt(3, repayId);
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        return true;
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01 FROM S62.T6252 WHERE F01=? AND F08>DATE_ADD(?,INTERVAL 1 MONTH)"))
            {
                ps.setInt(1, repayId);
                ps.setDate(2, getCurrentDate(connection));
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean isYuqi()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT DATEDIFF(?,F08) FROM S62.T6252 WHERE F09=? AND F03=? AND F08 < SYSDATE()"))
            {
                ps.setTimestamp(1, getCurrentTimestamp(connection));
                ps.setString(2, T6252_F09.WH.name());
                ps.setInt(3, serviceResource.getSession().getAccountId());
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
        }
        return false;
    }
    
}
