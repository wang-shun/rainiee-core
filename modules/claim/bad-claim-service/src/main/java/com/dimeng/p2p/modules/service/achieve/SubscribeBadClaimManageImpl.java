/*
 * 文 件 名:  SubscribeBadClaimManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月15日
 */
package com.dimeng.p2p.modules.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.XyType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F18;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.entities.T6264;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6264_F04;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.service.AbstractBadClaimService;
import com.dimeng.p2p.repeater.claim.SubscribeBadClaimManage;
import com.dimeng.p2p.repeater.claim.entity.SubscribeBadClaim;
import com.dimeng.p2p.repeater.claim.entity.SubscribeBadClaimTotal;
import com.dimeng.p2p.repeater.claim.entity.YZRLoanEntity;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;

/**
 * 
 * 认购不良债权管理实现类
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月15日]
 */
public class SubscribeBadClaimManageImpl extends AbstractBadClaimService implements SubscribeBadClaimManage
{
    
    /** <默认构造函数>
     */
    public SubscribeBadClaimManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<SubscribeBadClaim> getCanBuyBadClaimList(Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6264.F01 AS F01, T6264.F02 AS F02, T6230.F03 AS F03, (T6230.F05 - T6230.F07) AS F04, T6231.F03 AS F05, T6231.F02 AS F06, T6230.F06 AS F07, T6231.F06 AS F08, ");
        sql.append("( SELECT DATEDIFF(?, T6252.F08) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 = 'WH' ORDER BY T6252.F06 LIMIT 1 ) AS F09, ");
        sql.append("( SELECT SUM(T6252.F07) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 = 'WH' ) AS F10, ");
        sql.append("( CASE WHEN T6230.F11 = 'S' AND T6230.F12 = 'BJQEDB' THEN ( SELECT SUM(T6252.F07) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 = 'WH' AND T6252.F05 = '7001' ) ");
        sql.append("ELSE ( SELECT SUM(T6252.F07) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 = 'WH' ) END ) AS F11,T6264.F03 AS F12 ");
        sql.append("FROM S62.T6264  LEFT JOIN S62.T6230 ON T6230.F01 = T6264.F03 LEFT JOIN S62.T6231 ON T6230.F01 = T6231.F01 ");
        sql.append("LEFT JOIN S62.T6236 ON T6230.F01 = T6236.F02 WHERE T6264.F04 = 'ZRZ' AND (T6236.F03 != ? OR T6236.F03 IS NULL) ORDER BY T6264.F08 DESC");
        List<Object> params = new ArrayList<Object>();
        try (Connection connection = getConnection())
        {
            Date newDate = getCurrentDate(connection);
            params.add(newDate);
            params.add(serviceResource.getSession().getAccountId());
            return selectPaging(connection, new ArrayParser<SubscribeBadClaim>()
            {
                
                @Override
                public SubscribeBadClaim[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<SubscribeBadClaim> list = null;
                    SubscribeBadClaim subBadClaim = null;
                    while (rs.next())
                    {
                        subBadClaim = new SubscribeBadClaim();
                        subBadClaim.id = rs.getInt(1);
                        subBadClaim.creditNo = rs.getString(2);
                        subBadClaim.loanTitle = rs.getString(3);
                        subBadClaim.loanAmount = rs.getBigDecimal(4);
                        subBadClaim.syPeriods = rs.getInt(5);
                        subBadClaim.zPeriods = rs.getInt(6);
                        subBadClaim.yearRate = rs.getBigDecimal(7);
                        subBadClaim.nextDate = rs.getDate(8);
                        subBadClaim.overdueDays = rs.getInt(9);
                        subBadClaim.creditPrice = rs.getBigDecimal(10);
                        subBadClaim.subscribePrice = rs.getBigDecimal(11);
                        subBadClaim.bidId = rs.getInt(12);
                        if (list == null)
                        {
                            list = new ArrayList<SubscribeBadClaim>();
                        }
                        list.add(subBadClaim);
                    }
                    return list == null ? null : list.toArray(new SubscribeBadClaim[list.size()]);
                }
                
            }, paging, sql.toString(), params);
        }
    }
    
    @Override
    public PagingResult<SubscribeBadClaim> getAlreadyBuyBadClaimList(Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6265.F01 AS F01, T6264.F02 AS F02, T6230.F03 AS F03, (T6230.F05 - T6230.F07) AS F04, T6231.F03 AS F05, T6231.F02 AS F06, T6230.F06 AS F07, ");
        sql.append("T6231.F06 AS F08, T6264.F05 AS F09, T6265.F05 AS F10, T6265.F06 AS F11 ,T6265.F07 AS F12 ,T6264.F03 AS F13 ");
        sql.append("FROM S62.T6264 LEFT JOIN S62.T6265 ON T6264.F01 = T6265.F02 LEFT JOIN S62.T6230 ON T6230.F01 = T6264.F03 ");
        sql.append("LEFT JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6264.F04 = 'YZR' AND T6265.F03 = ? ORDER BY T6265.F07 DESC");
        List<Object> params = new ArrayList<Object>();
        params.add(serviceResource.getSession().getAccountId());
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<SubscribeBadClaim>()
            {
                
                @Override
                public SubscribeBadClaim[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<SubscribeBadClaim> list = null;
                    SubscribeBadClaim subBadClaim = null;
                    while (rs.next())
                    {
                        subBadClaim = new SubscribeBadClaim();
                        subBadClaim.id = rs.getInt(1);
                        subBadClaim.creditNo = rs.getString(2);
                        subBadClaim.loanTitle = rs.getString(3);
                        subBadClaim.loanAmount = rs.getBigDecimal(4);
                        subBadClaim.syPeriods = rs.getInt(5);
                        subBadClaim.zPeriods = rs.getInt(6);
                        subBadClaim.yearRate = rs.getBigDecimal(7);
                        subBadClaim.nextDate = rs.getDate(8);
                        subBadClaim.overdueDays = rs.getInt(9);
                        subBadClaim.creditPrice = rs.getBigDecimal(10);
                        subBadClaim.subscribePrice = rs.getBigDecimal(11);
                        subBadClaim.subscribeTime = rs.getTimestamp(12);
                        subBadClaim.bidId = rs.getInt(13);
                        if (list == null)
                        {
                            list = new ArrayList<SubscribeBadClaim>();
                        }
                        list.add(subBadClaim);
                    }
                    return list == null ? null : list.toArray(new SubscribeBadClaim[list.size()]);
                }
                
            }, paging, sql.toString(), params);
        }
    }
    
    @Override
    public SubscribeBadClaimTotal getSubscribeBadClaimTotal()
        throws Throwable
    {
        String sql =
            "SELECT SUM(T6265.F05) AS F01, SUM(T6265.F06) AS F02,COUNT(1) AS F03 FROM S62.T6265 WHERE T6265.F03 = ?";
        SubscribeBadClaimTotal sbct = new SubscribeBadClaimTotal();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        sbct.totalCreditPrice = rs.getBigDecimal(1);
                        sbct.totalSubscribePrice = rs.getBigDecimal(2);
                        sbct.subscribeCount = rs.getInt(3);
                    }
                }
            }
        }
        return sbct;
    }
    
    @Override
    public List<Integer> addOrder(int transferId)
        throws Throwable
    {
        if (transferId <= 0)
        {
            throw new LogicalException("不良债权转让申请不存在");
        }
        int accountId = serviceResource.getSession().getAccountId();
        int orderId = 0;
        List<Integer> orderIds = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                
                if (!isNetSign(accountId, connection))
                {
                    throw new LogicalException("未进行网签合同认证，不能购买不良债权。");
                }
                T6110 t6110 = getUserInfo(connection, accountId);
                if (t6110.F07 == T6110_F07.HMD)
                {
                    throw new LogicalException("用户已被拉黑，不能购买不良债权！");
                }
                if (t6110.F06 != T6110_F06.FZRR || t6110.F10 == T6110_F10.F || t6110.F19 == T6110_F19.F)
                {
                    throw new LogicalException("您没有购买不良债权的权限。");
                }
                // 锁定转让申请
                T6264 t6264 = selectT6264(connection, transferId);
                
                if (t6264 == null)
                {
                    throw new LogicalException("不良债权转让申请不存在");
                }
                
                int loanId = t6264.F03;
                
                //锁定标的
                T6230 t6230 = selectT6230(connection, loanId);
                
                if (t6230 == null)
                {
                    throw new LogicalException("借款标不存在");
                }
                
                if (t6230.F20 != T6230_F20.HKZ)
                {
                    throw new LogicalException("借款标不是还款中状态，不能进行购买操作。");
                }
                
                if (t6264.F04 != T6264_F04.ZRZ)
                {
                    throw new ParameterException("该不良债权不是转让中状态，不能操作。");
                }
                
                //校验债权是否已经被其他用户购买
                if (!checkBid(connection, loanId, accountId, transferId))
                {
                    throw new ParameterException("该不良债权已被其他用户购买，不能操作。");
                }
                
                int dbfId = 0;
                try (PreparedStatement ps = connection.prepareStatement("SELECT F03 FROM S62.T6236 WHERE F02=?"))
                {
                    ps.setInt(1, loanId);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            dbfId = rs.getInt(1);
                        }
                    }
                }
                if (accountId == dbfId)
                {
                    throw new LogicalException("不能购买自己担保的不良债权。");
                }
                
                T6252[] t6252s = selectAllT6252(connection, loanId);
                if (t6252s == null)
                {
                    throw new LogicalException("没有找到还款记录");
                }
                T6101 dbrzh = selectT6101(connection, accountId, T6101_F03.FXBZJZH, false);
                if (dbrzh == null)
                {
                    throw new LogicalException("用户风险保证金账户不存在");
                }
                // 购买债权总额
                BigDecimal amount = BigDecimal.ZERO;
                for (T6252 t6252 : t6252s)
                {
                    amount = amount.add(t6252.F07);
                }
                if (dbrzh.F06.compareTo(amount) < 0)
                {
                    throw new LogicalException("风险保证金账户余额不足，不能购买不良债权！");
                }
                
                T6501 t6501 = new T6501();
                for (T6252 t6252 : t6252s)
                {
                    if (t6252 == null)
                    {
                        continue;
                    }
                    if (orderIds.size() < t6252s.length)
                    {
                        t6501.F02 = OrderType.BUY_BAD_CLAIM.orderType();
                        t6501.F03 = T6501_F03.DTJ;
                        t6501.F04 = getCurrentTimestamp(connection);
                        t6501.F07 = T6501_F07.YH;
                        t6501.F08 = accountId;
                        t6501.F13 = t6252.F07;
                        orderId = insertT6501(connection, t6501);
                        
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("INSERT INTO S65.T6529 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F09 = ? "))
                        {
                            pstmt.setInt(1, orderId);
                            pstmt.setInt(2, loanId);
                            pstmt.setInt(3, t6264.F01);
                            pstmt.setInt(4, accountId);
                            pstmt.setInt(5, t6252.F11);
                            pstmt.setBigDecimal(6, t6252.F07);
                            pstmt.setInt(7, t6252.F05);
                            pstmt.setInt(8, t6252.F06);
                            pstmt.execute();
                        }
                        orderIds.add(orderId);
                    }
                }
                
                serviceResource.commit(connection);
                return orderIds;
            }
            catch (Throwable e)
            {
                serviceResource.rollback(connection);
                logger.error(e, e);
                throw e;
            }
        }
    }
    
    /** 
     * 查询标的信息并锁定
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private T6230 selectT6230(Connection connection, int F01)
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
     * 查询不良债权转让申请记录并锁定
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private T6264 selectT6264(Connection connection, int F01)
        throws SQLException
    {
        T6264 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S62.T6264 WHERE F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
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
     * 查询用户信息
     * @param connection
     * @param userId
     * @return
     * @throws Throwable
     */
    public T6110 getUserInfo(Connection connection, int userId)
        throws Throwable
    {
        T6110 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F15, F18, F19 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, userId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6110();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getString(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = T6110_F06.parse(resultSet.getString(6));
                    record.F07 = T6110_F07.parse(resultSet.getString(7));
                    record.F08 = T6110_F08.parse(resultSet.getString(8));
                    record.F09 = resultSet.getTimestamp(9);
                    record.F10 = T6110_F10.parse(resultSet.getString(10));
                    record.F15 = resultSet.getTimestamp(11);
                    record.F18 = T6110_F18.parse(resultSet.getString(12));
                    record.F19 = T6110_F19.parse(resultSet.getString(13));
                }
            }
        }
        return record;
    }
    
    /**
     * 查询还款记录
     * 
     * @param loanId
     * @return
     * @throws SQLException
     */
    protected T6252[] selectAllT6252(Connection connection, int loanId)
        throws SQLException
    {
        // 担保方式
        T6230_F12 t6230_F12 = T6230_F12.BJQEDB;
        //是否为担保标
        T6230_F11 t6230_F11 = T6230_F11.F;
        try (PreparedStatement ps = connection.prepareStatement("SELECT F12,F11 FROM S62.T6230 WHERE F01=?"))
        {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    t6230_F12 = EnumParser.parse(T6230_F12.class, rs.getString(1));
                    t6230_F11 = EnumParser.parse(T6230_F11.class, rs.getString(2));
                }
            }
        }
        StringBuilder sb =
            new StringBuilder("SELECT F05, SUM(F07), F11, F06 FROM S62.T6252 WHERE T6252.F02 = ? AND F09=?");
        List<Object> parameters = new ArrayList<>();
        parameters.add(loanId);
        parameters.add(T6252_F09.WH);
        if (t6230_F11 == T6230_F11.S && t6230_F12 == T6230_F12.BJQEDB)
        {
            sb.append(" AND F05=?");
            parameters.add(FeeCode.TZ_BJ);
        }
        sb.append(" GROUP BY F05,F11");
        return selectAll(connection, new ArrayParser<T6252>()
        {
            
            @Override
            public T6252[] parse(ResultSet resultSet)
                throws SQLException
            {
                ArrayList<T6252> list = null;
                while (resultSet.next())
                {
                    T6252 record = new T6252();
                    record.F05 = resultSet.getInt(1);
                    record.F07 = resultSet.getBigDecimal(2);
                    record.F11 = resultSet.getInt(3);
                    record.F06 = resultSet.getInt(4);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
                return ((list == null || list.size() == 0) ? null : list.toArray(new T6252[list.size()]));
            }
        }, sb.toString(), parameters);
    }
    
    /** 
     * 校验标的是否已被别人购买过
     * @param connection
     * @param loanId
     * @param userId
     * @param zqzrsqId
     * @return
     * @throws SQLException
     */
    protected Boolean checkBid(Connection connection, int loanId, int userId, int zqzrsqId)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6529.F04 FROM S65.T6529 LEFT JOIN S65.T6501 ON T6501.F01 = T6529.F01 WHERE T6529.F02 = ? AND T6529.F03 = ? AND T6501.F02 = ? AND T6501.F03 = ? "))
        {
            pstmt.setInt(1, loanId);
            pstmt.setInt(2, zqzrsqId);
            pstmt.setInt(3, OrderType.BUY_BAD_CLAIM.orderType());
            pstmt.setString(4, T6501_F03.CG.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    //购买人id
                    int gmrId = resultSet.getInt(1);
                    //如果该债权转让已经存在一条成功的订单并且购买人不是当前用户，则返回false
                    if (gmrId != userId)
                    {
                        return false;
                    }
                }
            }
        }
        
        return true;
        
    }
    
    @Override
    public BigDecimal getCreditPrice(int blzqId)
        throws Throwable
    {
        BigDecimal creditPrice = BigDecimal.ZERO;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6252.F07) FROM S62.T6252 INNER JOIN S62.T6264 ON T6252.F02 = T6264.F03 WHERE T6264.F01 = ? AND T6252.F09 = 'WH' "))
            {
                ps.setInt(1, blzqId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        creditPrice = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return creditPrice;
    }
    
    @Override
    public int getOverdueDays(int blzqId)
        throws Throwable
    {
        int overdueDays = 0;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement(" SELECT DATEDIFF(?, T6252.F08) FROM S62.T6252 LEFT JOIN S62.T6264 ON T6252.F02 = T6264.F03 WHERE T6264.F01 = ? AND T6252.F09 = 'WH' ORDER BY T6252.F06 LIMIT 1"))
            {
                ps.setDate(1, getCurrentDate(connection));
                ps.setInt(2, blzqId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        overdueDays = rs.getInt(1);
                    }
                }
            }
        }
        return overdueDays;
    }
    
    @Override
    public boolean isNetSign(int userId)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean isHasNetSign = BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_HAS_NETSIGN));
        if (isHasNetSign)
        {
            try (Connection connection = getConnection())
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT T6272.F01 FROM S62.T6272 WHERE T6272.F02 = ? AND T6272.F03 = (SELECT T5125.F02 FROM S51.T5125 WHERE T5125.F01 = ? ORDER BY T5125.F02 DESC LIMIT 1)  LIMIT 1"))
                {
                    ps.setInt(1, userId);
                    ps.setInt(2, XyType.WQXY);
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
        else
        {
            return true;
        }
    }
    
    @Override
    public PagingResult<YZRLoanEntity> getAlreadyTransferLoan(Paging paging)
        throws Throwable
    {
        // 分页结果
        PagingResult<YZRLoanEntity> resultList = null;
        try
        {
            // 打开数据连接
            try (final Connection connection = getConnection())
            {
                // 参数
                ArrayList<Object> parameters = new ArrayList<>();
                StringBuilder sql =
                    new StringBuilder(
                        "SELECT T6230.F01, T6230.F02, T6230.F03, T6230.F04, T6230.F05, T6230.F06, T6230.F07, T6230.F08, T6230.F09, T6230.F10, T6230.F11, T6230.F12, T6230.F13, T6230.F14, T6230.F15, T6230.F16, T6230.F17, T6230.F18,");
                sql.append(" T6230.F19, T6230.F20, T6230.F21, T6230.F22, T6230.F23, T6230.F24, T6230.F25, (SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F05 = '7001') AS F26, T6231.F21 AS F27, T6231.F22 AS F28, ");
                sql.append(" T6265.F01 AS F29, T6265.F06 AS F30, T6265.F07 AS F31, T6231.F02 AS F32, T6231.F03 AS F33 FROM S62.T6230 LEFT JOIN S62.T6231 ON T6230.F01 = T6231.F01 LEFT JOIN S62.T6264 ON T6230.F01 = T6264.F03 LEFT JOIN S62.T6265 ON T6264.F01 = T6265.F02 ");
                sql.append(" WHERE T6264.F04 = ? AND T6230.F02 = ? AND T6230.F20 = ? ORDER BY T6265.F07 DESC");
                
                parameters.add(T6264_F04.YZR.name());
                // 用户名
                parameters.add(serviceResource.getSession().getAccountId());
                parameters.add(T6230_F20.YZR.name());
                // 执行查询
                resultList = selectPaging(connection, new ArrayParser<YZRLoanEntity>()
                {
                    @Override
                    public YZRLoanEntity[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<YZRLoanEntity> list = null;
                        while (resultSet.next())
                        {
                            YZRLoanEntity record = new YZRLoanEntity();
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
                            T6231 t6231 = new T6231();
                            t6231.F21 = T6231_F21.parse(resultSet.getString(27));
                            t6231.F22 = resultSet.getInt(28);
                            t6231.F02 = resultSet.getInt(32);
                            t6231.F03 = resultSet.getInt(33);
                            record.F28 = t6231;
                            record.zrId = resultSet.getInt(29);
                            record.zrTotle = resultSet.getBigDecimal(30);
                            record.zrTime = resultSet.getTimestamp(31);
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            list.add(record);
                        }
                        return list == null || list.size() == 0 ? null : list.toArray(new YZRLoanEntity[list.size()]);
                    }
                }, paging, sql.toString(), parameters);
            }
        }
        catch (Exception e)
        {
            logger.error("SubscribeBadClaimManageImpl.getAlreadyTransferLoan() error:", e);
        }
        return resultList;
    }
    
    @Override
    public boolean checkBidBadClaim(int loanId)
        throws Throwable
    {
        try (final Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6529.F04 FROM S65.T6529 LEFT JOIN S65.T6501 ON T6501.F01 = T6529.F01 WHERE T6529.F02 = ?  AND T6501.F02 = ? AND T6501.F03 = ? "))
            {
                pstmt.setInt(1, loanId);
                pstmt.setInt(2, OrderType.BUY_BAD_CLAIM.orderType());
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
    
    @Override
    public boolean checkIsBuyBadClaim(int userId)
        throws Throwable
    {
        boolean flag = false;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT count(1) FROM S62.T6265 WHERE T6265.F03 = ? "))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        int count = resultSet.getInt(1);
                        if (count > 0)
                        {
                            flag = true;
                        }
                    }
                }
            }
        }
        
        return flag;
    }
}
