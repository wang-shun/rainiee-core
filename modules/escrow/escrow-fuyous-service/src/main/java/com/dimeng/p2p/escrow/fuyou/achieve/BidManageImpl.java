package com.dimeng.p2p.escrow.fuyou.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.S65.entities.T6514;
import com.dimeng.p2p.S65.entities.T6554;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.S65.enums.T6514_F07;
import com.dimeng.p2p.escrow.fuyou.cond.TbdzCond;
import com.dimeng.p2p.escrow.fuyou.entity.console.TbdzEntity;
import com.dimeng.p2p.escrow.fuyou.service.BidManage;
import com.dimeng.p2p.modules.bid.pay.service.achieve.TenderManageImpl;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 标实现类
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月23日]
 */
public class BidManageImpl extends TenderManageImpl implements BidManage
{
    
    public BidManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public Map<String, String> bid(final int bidId, final BigDecimal amount, String userReward, String tranPwd,
        String myRewardType, String hbRule, String jxqRule, Map<String, String> parMap)
        throws Throwable
    {
        // 富友托管需要交易密码
        if (StringUtils.isEmpty(tranPwd))
        {
            throw new LogicalException("交易密码不能为空！");
        }
        final int accountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
            int maxCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
            int count = psdInputCount(connection, 0);
            if (count >= maxCount)
            {
                throw new LogicalException("您今日交易密码输入错误已到最大次数，请改日再试!");
            }
            String tran_pwd = selectTranPwd(connection, accountId);
            if (!tranPwd.equals(tran_pwd))
            {
                addInputCount(connection, accountId);
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
            return wgBid(bidId, amount, userReward, myRewardType, hbRule, jxqRule, parMap, connection, 0);
        }
    }
    
    @Override
    public boolean getCheck(int loanId, String flag)
        throws Throwable
    {
        boolean isCheck = false;
        try (Connection connection = getConnection())
        {
            StringBuffer sql =
                new StringBuffer("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S62.T6250 WHERE T6250.F02 = ? ");
            if ("NotLoan".equals(flag))
                sql = sql.append("AND T6250.F07=? ");
            else if ("Loan".equals(flag))
                sql = sql.append("AND T6250.F08=? ");
            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
            {
                pstmt.setInt(1, loanId);
                if ("NotLoan".equals(flag))
                    pstmt.setString(2, T6250_F07.S.name());
                else if ("Loan".equals(flag))
                    pstmt.setString(2, T6250_F08.S.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        isCheck = true;
                    }
                }
            }
            return isCheck;
        }
    }
    
    @Override
    public void updateT6501(int orderId, String mchnt_txn_ssn)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6501 SET F10 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, mchnt_txn_ssn);
                pstmt.setInt(2, orderId);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public PagingResult<TbdzEntity> search(TbdzCond query, Paging paging)
        throws Throwable
    {
        try (Connection conn = getConnection())
        {
            StringBuilder sql = new StringBuilder("");
            ArrayList<Object> parameters = new ArrayList<>();
            if (query != null)
            {
                if (OrderType.CHARGE.toString().equals(query.tradingType()))//查询充值记录
                {
                    sql = createChargeQuery(sql, query, parameters);
                }
                if (OrderType.BID.toString().equals(query.tradingType()))//查询投标记录
                {
                    sql = createBidQuery(sql, query, parameters);
                }
                if (OrderType.ADVANCE.toString().equals(query.tradingType()))//查询垫付记录
                {
                    sql = createAdvanceQuery(sql, query, parameters);
                }
                if (OrderType.BID_REPAYMENT.toString().equals(query.tradingType()))//查询还款记录
                {
                    sql = createRepayment(sql, query, parameters);
                }
                if (OrderType.BID_EXCHANGE.toString().equals(query.tradingType()))//查询债权转让记录
                {
                    sql = createBidExchangeQuery(sql, query, parameters);
                }
                if (OrderType.GYBTRANSFER.toString().equals(query.tradingType()))//查询公益标投标记录
                {
                    sql = createDonationQuery(sql, query, parameters);
                }
                if (OrderType.MALLTRANSFER.toString().equals(query.tradingType()))//查询商品购买记录
                {
                    sql = createMallBuyQuery(sql, query, parameters);
                }
                if (OrderType.MALLREFUND.toString().equals(query.tradingType()))//查询商品退款记录
                {
                    sql = createMallRefundQuery(sql, query, parameters);
                }
                if (OrderType.BUY_BAD_CLAIM.toString().equals(query.tradingType()))//查询不良债权购买记录
                {
                    sql = createBadclaimQuery(sql, query, parameters);
                }
            }
            
            return selectPaging(conn, new ArrayParser<TbdzEntity>()
            {
                
                @Override
                public TbdzEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<TbdzEntity> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        TbdzEntity record = new TbdzEntity();
                        record.F01 = resultSet.getInt(1);
                        record.F03 = T6501_F03.parse(resultSet.getString(2));
                        record.F04 = resultSet.getTimestamp(3);
                        record.F05 = resultSet.getTimestamp(4);
                        record.F06 = resultSet.getTimestamp(5);
                        record.F10 = resultSet.getString(6) != null ? resultSet.getString(6) : "";
                        record.amount = resultSet.getBigDecimal(7);
                        record.userName = resultSet.getString(8);
                        record.F11 = T6501_F11.parse(resultSet.getString(9));
                        record.F12 = resultSet.getString(10) != null ? resultSet.getString(10) : "";
                        record.F02 = resultSet.getInt(11);
                        lists.add(record);
                    }
                    return lists.toArray(new TbdzEntity[lists.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    private StringBuilder createDonationQuery(StringBuilder sql, TbdzCond query, ArrayList<Object> parameters)
        throws Throwable
    {
        sql.append("SELECT T6501.F01 AS F01,T6501.F03 AS F02,T6501.F04 F03,T6501.F05 AS F04,T6501.F06 AS F05,T6501.F10 AS F06,"
            + "T6554.F04 AS F07,T6110.F02 AS F08, T6501.F11 AS F09, T6501.F12 AS F10, T6501.F02 AS F11 FROM S65.T6501 INNER JOIN S65.T6554 ON T6501.F01=T6554.F01 "
            + "INNER JOIN S61.T6110 ON T6554.F02=T6110.F01 WHERE 1=1 AND T6501.F02='"
            + OrderType.GYBTRANSFER.orderType() + "'");
        sql.append(QueryTerms(query, parameters));
        return sql;
    }
    
    private StringBuilder createBidExchangeQuery(StringBuilder sql, TbdzCond query, ArrayList<Object> parameters)
        throws Throwable
    {
        sql.append("SELECT T6501.F01 AS F01,T6501.F03 AS F02,T6501.F04 F03,T6501.F05 AS F04,T6501.F06 AS F05,T6501.F10 AS F06,"
            + "T6507.F05 AS F07,T6110.F02 AS F08, T6501.F11 AS F09, T6501.F12 AS F10, T6501.F02 AS F11 FROM S65.T6501 INNER JOIN S65.T6507 ON T6501.F01=T6507.F01 "
            + "INNER JOIN S61.T6110 ON T6507.F03=T6110.F01 "
            + "WHERE 1=1 AND T6501.F02='"
            + OrderType.BID_EXCHANGE.orderType() + "'");
        sql.append(QueryTerms(query, parameters));
        return sql;
    }
    
    private StringBuilder createAdvanceQuery(StringBuilder sql, TbdzCond query, ArrayList<Object> parameters)
        throws Throwable
    {
        sql.append("SELECT T6501.F01 AS F01,T6501.F03 AS F02,T6501.F04 F03,T6501.F05 AS F04,T6501.F06 AS F05,T6501.F10 AS F06,"
            + "T6514.F05 AS F07,T6110.F02 AS F08, T6501.F11 AS F09, T6501.F12 AS F10, T6501.F02 AS F11  FROM S65.T6501 "
            + "INNER JOIN S65.T6514 ON T6501.F01=T6514.F01 INNER JOIN S61.T6110 ON T6514.F04=T6110.F01 "
            + "WHERE 1=1 AND T6501.F02='" + OrderType.ADVANCE.orderType() + "'");
        sql.append(QueryTerms(query, parameters));
        return sql;
    }
    
    private StringBuilder createBidQuery(StringBuilder sql, TbdzCond query, ArrayList<Object> parameters)
        throws Throwable
    {
        sql.append("SELECT T6501.F01 AS F01,T6501.F03 AS F02,T6501.F04 F03,T6501.F05 AS F04,T6501.F06 AS F05,T6501.F10 AS F06,"
            + "T6504.F04 AS F07,T6110.F02 AS F08, T6501.F11 AS F09, T6501.F12 AS F10, T6501.F02 AS F11  FROM S65.T6501 "
            + "INNER JOIN S65.T6504 ON T6501.F01=T6504.F01 INNER JOIN S61.T6110 ON T6504.F02=T6110.F01 "
            + "WHERE 1=1 AND T6501.F02='" + OrderType.BID.orderType() + "'");
        sql.append(QueryTerms(query, parameters));
        return sql;
    }
    
    private StringBuilder createChargeQuery(StringBuilder sql, TbdzCond query, ArrayList<Object> parameters)
        throws Throwable
    {
        sql.append("SELECT T6501.F01 AS F01,T6501.F03 AS F02,T6501.F04 F03,T6501.F05 AS F04,T6501.F06 AS F05,T6501.F10 AS F06,"
            + "T6502.F03 AS F07,T6110.F02 AS F08, T6501.F11 AS F09, T6501.F12 AS F10, T6501.F02 AS F11 FROM S65.T6501 "
            + "INNER JOIN S65.T6502 ON T6501.F01=T6502.F01 INNER JOIN S61.T6110 ON T6502.F02=T6110.F01 WHERE 1=1 "
            + "AND T6501.F03 IN('"
            + T6501_F03.DQR
            + "','"
            + T6501_F03.DTJ
            + "','"
            + T6501_F03.SB
            + "')"
            + "AND (T6501.F02='"
            + OrderType.CHARGE.orderType()
            + "' OR T6501.F02='"
            + OrderType.PLATFORM_CHARGE.orderType() + "')");
        sql.append(QueryTerms(query, parameters));
        return sql;
    }
    
    private StringBuilder createRepayment(StringBuilder sql, TbdzCond query, ArrayList<Object> parameters)
        throws Throwable
    {
        sql.append("SELECT T6501.F01 AS F01,T6501.F03 AS F02,T6501.F04 F03,T6501.F05 AS F04,T6501.F06 AS F05,T6501.F10 AS F06,"
            + "T6506.F06 AS F07,T6110.F02 AS F08, T6501.F11 AS F09, T6501.F12 AS F10, T6501.F02 AS F11  FROM S65.T6501 "
            + "INNER JOIN S65.T6506 ON T6501.F01=T6506.F01 "
            + "INNER JOIN S62.T6230 ON T6230.F01=T6506.F03 "
            + "INNER JOIN S61.T6110 ON T6230.F02=T6110.F01 " + "WHERE 1=1 ");
        sql.append("AND T6501.F02='" + OrderType.BID_REPAYMENT.orderType() + "'");
        if (!"".equals(query.state()))
        {
            sql.append(" AND T6501.F03 = '" + query.state() + "'");
        }
        String account = query.userName();
        if (!StringHelper.isEmpty(account))
        {
            sql.append(" AND T6110.F02 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(account));
        }
        String orderId = query.f01();
        if (!StringHelper.isEmpty(orderId))
        {
            sql.append(" AND T6501.F01 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(orderId));
        }
        String loanNo = query.f10();
        if (!StringHelper.isEmpty(loanNo))
        {
            sql.append(" AND T6501.F10 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(loanNo));
        }
        Timestamp datetime = query.getStartExpireDatetime();
        if (datetime != null)
        {
            sql.append(" AND DATE(T6501.F04) >=?");
            parameters.add(datetime);
        }
        datetime = query.getEndExpireDatetime();
        if (datetime != null)
        {
            sql.append(" AND DATE(T6501.F04) <= ?");
            parameters.add(datetime);
        }
        sql.append(" UNION ALL "
            + "SELECT T6501.F01 AS F01,T6501.F03 AS F02,T6501.F04 F03,T6501.F05 AS F04,T6501.F06 AS F05,T6501.F10 AS F06,"
            + "T6521.F06 AS F07,T6110.F02 AS F08, T6501.F11 AS F09, T6501.F12 AS F10, T6501.F02 AS F11 FROM S65.T6501 "
            + "INNER JOIN S65.T6521 ON T6501.F01=T6521.F01 INNER JOIN S62.T6230 ON T6230.F01=T6521.F03 "
            + "INNER JOIN S61.T6110 ON T6230.F02=T6110.F01 " + "WHERE 1=1 ");
        sql.append("AND T6501.F02='" + OrderType.PREPAYMENT_LOAN.orderType() + "'");
        if (!"".equals(query.state()))
        {
            sql.append(" AND T6501.F03 = '" + query.state() + "'");
        }
        if (!StringHelper.isEmpty(account))
        {
            sql.append(" AND T6110.F02 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(account));
        }
        if (!StringHelper.isEmpty(orderId))
        {
            sql.append(" AND T6501.F01 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(orderId));
        }
        if (!StringHelper.isEmpty(loanNo))
        {
            sql.append(" AND T6501.F10 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(loanNo));
        }
        if (datetime != null)
        {
            sql.append(" AND DATE(T6501.F04) >=?");
            parameters.add(datetime);
        }
        datetime = query.getEndExpireDatetime();
        if (datetime != null)
        {
            sql.append(" AND DATE(T6501.F04) <= ?");
            parameters.add(datetime);
        }
        sql.append(" ORDER BY F01 DESC");
        return sql;
    }
    
    private StringBuilder createMallBuyQuery(StringBuilder sql, TbdzCond query, ArrayList<Object> parameters)
        throws Throwable
    {
        sql.append("SELECT T6501.F01 AS F01, T6501.F03 AS F02, T6501.F04 AS F03, T6501.F05 AS F04, T6501.F06 AS F05, "
            + "T6501.F10 AS F06, T6555.F04 AS F07, T6110.F02 AS F08, T6501.F11 AS F09, T6501.F12 AS F10, T6501.F02 AS F11 "
            + "FROM S65.T6501 INNER JOIN S65.T6555 ON T6501.F01 = T6555.F01 INNER JOIN S61.T6110 ON T6555.F02 = T6110.F01 "
            + "WHERE 1 = 1 AND T6501.F02 = " + OrderType.MALLTRANSFER.orderType());
        sql.append(QueryTerms(query, parameters));
        return sql;
    }
    
    private StringBuilder createMallRefundQuery(StringBuilder sql, TbdzCond query, ArrayList<Object> parameters)
        throws Throwable
    {
        sql.append("SELECT T6501.F01 AS F01, T6501.F03 AS F02, T6501.F04 AS F03, T6501.F05 AS F04, T6501.F06 AS F05, "
            + "T6501.F10 AS F06, T6528.F03 AS F07, T6110.F02 AS F08, T6501.F11 AS F09, T6501.F12 AS F10, T6501.F02 AS F11 "
            + "FROM S65.T6501 INNER JOIN S65.T6528 ON T6501.F01 = T6528.F01 INNER JOIN S61.T6110 ON T6528.F02 = T6110.F01 "
            + "WHERE 1 = 1 AND T6501.F02 = " + OrderType.MALLREFUND.orderType());
        sql.append(QueryTerms(query, parameters));
        return sql;
    }
    
    private StringBuilder createBadclaimQuery(StringBuilder sql, TbdzCond query, ArrayList<Object> parameters)
        throws Throwable
    {
        sql.append("SELECT T6501.F01 AS F01, T6501.F03 AS F02, T6501.F04 AS F03, T6501.F05 AS F04, T6501.F06 AS F05, "
            + "T6501.F10 AS F06, T6264.F09 AS F07, T6110.F02 AS F08, T6501.F11 AS F09, T6501.F12 AS F10, T6501.F02 AS F11 "
            + "FROM S65.T6501 INNER JOIN S65.T6529 ON T6501.F01 = T6529.F01 INNER JOIN S61.T6110 ON T6529.F04 = T6110.F01 "
            + "INNER JOIN S62.T6264 ON T6529.F03 = T6264.F01 WHERE 1 = 1 AND T6501.F02 = "
            + OrderType.BUY_BAD_CLAIM.orderType());
        sql.append(QueryTerms(query, parameters));
        return sql;
    }
    
    private StringBuilder QueryTerms(TbdzCond query, ArrayList<Object> parameters)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("");
        String account = query.userName();
        if (!StringHelper.isEmpty(account))
        {
            sql.append(" AND T6110.F02 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(account));
        }
        String orderId = query.f01();
        if (!StringHelper.isEmpty(orderId))
        {
            sql.append(" AND T6501.F01 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(orderId));
        }
        String loanNo = query.f10();
        if (!StringHelper.isEmpty(loanNo))
        {
            sql.append(" AND T6501.F10 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(loanNo));
        }
        Timestamp datetime = query.getStartExpireDatetime();
        if (datetime != null)
        {
            sql.append(" AND DATE(T6501.F04) >=?");
            parameters.add(datetime);
        }
        datetime = query.getEndExpireDatetime();
        if (datetime != null)
        {
            sql.append(" AND DATE(T6501.F04) <= ?");
            parameters.add(datetime);
        }
        if (!"".equals(query.state()))
        {
            sql.append(" AND T6501.F03 = '" + query.state() + "'");
        }
        sql.append(" ORDER BY T6501.F01 DESC");
        return sql;
    }
    
    @Override
    public T6554 selectT6554(int orderId)
        throws Throwable
    {
        T6554 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S65.T6554 WHERE T6554.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, orderId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6554();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getInt(5);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public String selectT6119(int usr_id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, usr_id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public T6501_F03 selectT6501(int orderId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S65.T6501 WHERE T6501.F01 = ? "))
            {
                pstmt.setInt(1, orderId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return T6501_F03.parse(resultSet.getString(1));
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public T6514 selectT6514(int F01)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return this.selectT6514(connection, F01);
        }
    }
    
    private T6514 selectT6514(Connection connection, int F01)
        throws SQLException
    {
        T6514 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06,F07 FROM S65.T6514 WHERE T6514.F01 = ? LIMIT 1"))
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
                    record.F07 = T6514_F07.parse(resultSet.getString(7));
                }
            }
        }
        return record;
    }
    
}
