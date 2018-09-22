package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S51.enums.T5124_F05;
import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.S62.entities.T6240;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6252;
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
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6236_F04;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.modules.bid.user.service.BidManage;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdlb;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdylx;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdysx;
import com.dimeng.p2p.modules.bid.user.service.entity.Dbxx;
import com.dimeng.p2p.modules.bid.user.service.entity.Hkjllb;
import com.dimeng.p2p.modules.bid.user.service.entity.Mbxx;
import com.dimeng.p2p.modules.bid.user.service.entity.TzjlEntity;
import com.dimeng.p2p.modules.bid.user.service.entity.Tztjxx;
import com.dimeng.p2p.modules.bid.user.service.query.BidQuery;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

public class BidManageImpl extends AbstractBidManage implements BidManage
{
    
    public BidManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public Bdxq get(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Bdxq record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26 FROM S62.T6230 WHERE T6230.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new Bdxq();
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
                        record.jsTime = getJssj(record.F01);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public void updateOrderStatus(List<Integer> orderId)
        throws Throwable
    {
        if (orderId == null || orderId.size() <= 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                for (int id : orderId)
                {
                    T6501_F03 status = T6501_F03.CG;
                    try (PreparedStatement ps =
                        connection.prepareStatement("SELECT F03 FROM S65.T6501 WHERE F01=? FOR UPDATE"))
                    {
                        ps.setInt(1, id);
                        try (ResultSet rs = ps.executeQuery())
                        {
                            if (rs.next())
                            {
                                status = EnumParser.parse(T6501_F03.class, rs.getString(1));
                            }
                        }
                    }
                    if (status == T6501_F03.SB)
                    {
                        try (PreparedStatement ps =
                            connection.prepareStatement("UPDATE S65.T6501 SET F03=? WHERE F01=?"))
                        {
                            ps.setString(1, T6501_F03.DTJ.name());
                            ps.setInt(2, id);
                            ps.executeUpdate();
                        }
                    }
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /*
     * 投资结束时间
     */
    private Timestamp getJssj(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT ADDDATE(T6231.F10,INTERVAL T6230.F08 DAY)  FROM S62.T6231 INNER JOIN S62.T6230 ON T6231.F01 = T6230.F01 WHERE T6230.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getTimestamp(1);
                    }
                }
            }
            return null;
        }
        
    }
    
    @Override
    public T6231 getExtra(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6231 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F21, F22, F34 FROM S62.T6231 WHERE T6231.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6231();
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
                        record.F34 = resultSet.getTimestamp(19);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public BigDecimal getYqje(int loanId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND DATEDIFF(?,F08)>0 AND F09 = ? AND F05 IN (?,?,?,?)"))
            {
                pstmt.setInt(1, loanId);
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
    
    @Override
    public List<Integer> addOrder(int loanId)
        throws Throwable
    {
        if (loanId <= 0)
        {
            throw new ParameterException("指定的记录不存在");
        }
        
        try (Connection connection = getConnection())
        {
            int orderId = 0;
            int accountId = serviceResource.getSession().getAccountId();
            List<Integer> orderIds = new ArrayList<>();
            try
            {
                serviceResource.openTransactions(connection);
                if (!isYQ(connection, loanId))
                {
                    throw new LogicalException("该标的没有逾期");
                }
                if (!isNetSign(accountId, connection))
                {
                    throw new LogicalException("请先进行网签认证，才可以垫付，<br/>请您到<a id='to_validate' href=\'"
                        + serviceResource.getResource(ConfigureProvider.class).format(URLVariable.USER_NETSIGN_URL)
                        + "\' class=\'blue\'>网签合同</a>设置！");
                }
                // 垫付人
                int dfrId = 0;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F03 FROM S62.T6236 WHERE T6236.F02 = ? AND T6236.F03 = ? AND T6236.F04 = ? LIMIT 1"))
                {
                    pstmt.setInt(1, loanId);
                    pstmt.setInt(2, accountId);
                    pstmt.setString(3, T6236_F04.S.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            dfrId = resultSet.getInt(1);
                        }
                    }
                }
                if (dfrId <= 0)
                {
                    throw new LogicalException("没有找到垫付方");
                }
                T6252[] t6252s = selectAllT6252(connection, loanId);
                if (t6252s == null)
                {
                    throw new LogicalException("没有找到还款记录");
                }
                T6101 dfrzh = selectT6101(connection, accountId, T6101_F03.FXBZJZH, false);
                if (dfrzh == null)
                {
                    throw new LogicalException("垫付人风险保证金账户不存在");
                }
                // 垫付总额
                BigDecimal amount = BigDecimal.ZERO;
                for (T6252 t6252 : t6252s)
                {
                    amount = amount.add(t6252.F07);
                }
                if (dfrzh.F06.compareTo(amount) < 0)
                {
                    throw new LogicalException("风险保证金余额不足，不能进行垫付！");
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01 FROM S65.T6514 WHERE T6514.F02 = ?"))
                {
                    pstmt.setInt(1, loanId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            orderIds.add(resultSet.getInt(1));
                        }
                    }
                }
                if (orderIds.size() == 0)
                {
                    T6501 t6501 = null;
                    Timestamp timestmp = this.getCurrentTimestamp(connection);
                    for (T6252 t6252 : t6252s)
                    {
                        if (t6252 == null)
                        {
                            continue;
                        }
                        if (orderIds.size() < t6252s.length)
                        {
                            
                            /*
                             * try (PreparedStatement pstmt =
                             * connection.prepareStatement(
                             * "INSERT INTO S65.T6501 SET F02 = ?,F03 = ?, F04 = ?, F07 = ?, F08 = ?"
                             * , PreparedStatement.RETURN_GENERATED_KEYS)) {
                             * pstmt.setInt(1, OrderType.ADVANCE.orderType());
                             * pstmt.setString(2, T6501_F03.DTJ.name());
                             * pstmt.setTimestamp(3,
                             * getCurrentTimestamp(connection));
                             * pstmt.setString(4, T6501_F07.YH.name());
                             * pstmt.setInt(5, accountId); pstmt.execute(); try
                             * (ResultSet resultSet = pstmt.getGeneratedKeys();)
                             * { if (resultSet.next()) { orderId =
                             * resultSet.getInt(1); } } }
                             */
                            t6501 = new T6501();
                            t6501.F02 = OrderType.ADVANCE.orderType();
                            t6501.F03 = T6501_F03.DTJ;
                            t6501.F04 = timestmp;
                            t6501.F07 = T6501_F07.YH;
                            t6501.F08 = accountId;
                            t6501.F13 = t6252.F07;
                            orderId = insertT6501(connection, t6501);
                            
                            try (PreparedStatement pstmt =
                                connection.prepareStatement("INSERT INTO S65.T6514 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F08 = ?"))
                            {
                                pstmt.setInt(1, orderId);
                                pstmt.setInt(2, loanId);
                                pstmt.setInt(3, t6252.F11);
                                pstmt.setInt(4, dfrId);
                                pstmt.setBigDecimal(5, t6252.F07);
                                pstmt.setInt(6, t6252.F05);
                                pstmt.setInt(7, t6252.F06);
                                pstmt.execute();
                            }
                            orderIds.add(orderId);
                        }
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
    
    @Override
    public List<Integer> addPtdfOrder(int loanId)
        throws Throwable
    {
        if (loanId <= 0)
        {
            throw new ParameterException("指定的记录不存在");
        }
        int orderId = 0;
        List<Integer> orderIds = new ArrayList<Integer>();
        try (Connection connection = getConnection())
        {
            T6252[] t6252s = selectPtdfAllT6252(connection, loanId);
            if (t6252s == null)
            {
                throw new LogicalException("没有找到还款记录");
            }
            // 获取平台用户id
            int pid = getPTID(connection);
            // 获取平台准备金账户
            T6101 dfrzh = selectT6101(connection, pid, T6101_F03.WLZH, false);
            if (dfrzh == null)
            {
                throw new LogicalException("垫付人账户不存在");
            }
            // 垫付总额
            BigDecimal amount = BigDecimal.ZERO;
            for (T6252 t6252 : t6252s)
            {
                amount = amount.add(t6252.F07);
            }
            if (dfrzh.F06.compareTo(amount) < 0)
            {
                throw new LogicalException("往来账户余额不足，不能进行垫付！");
            }
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S65.T6514 WHERE T6514.F02 = ?"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        orderIds.add(resultSet.getInt(1));
                    }
                }
            }
            
            if (orderIds.size() == 0)
            {
                T6501 t6501 = null;
                Timestamp timeStamp = getCurrentTimestamp(connection);
                for (T6252 t6252 : t6252s)
                {
                    if (t6252 == null)
                    {
                        continue;
                    }
                    if (orderIds.size() < t6252s.length)
                    {
                        /*
                         * try (PreparedStatement pstmt =
                         * connection.prepareStatement(
                         * "INSERT INTO S65.T6501 SET F02 = ?,F03 = ?, F04 = ?, F07 = ?, F08 = ?"
                         * , PreparedStatement.RETURN_GENERATED_KEYS)) {
                         * pstmt.setInt(1, OrderType.ADVANCE.orderType());
                         * pstmt.setString(2, T6501_F03.DTJ.name());
                         * pstmt.setTimestamp(3, timeStamp); pstmt.setString(4,
                         * T6501_F07.YH.name()); pstmt.setInt(5, pid);
                         * pstmt.execute(); try (ResultSet resultSet =
                         * pstmt.getGeneratedKeys();) { if (resultSet.next()) {
                         * orderId = resultSet.getInt(1); } } }
                         */
                        
                        t6501 = new T6501();
                        t6501.F02 = OrderType.ADVANCE.orderType();
                        t6501.F03 = T6501_F03.DTJ;
                        t6501.F04 = timeStamp;
                        t6501.F07 = T6501_F07.YH;
                        t6501.F08 = pid;
                        t6501.F13 = t6252.F07;
                        orderId = insertT6501(connection, t6501);
                        
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("INSERT INTO S65.T6514 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?"))
                        {
                            pstmt.setInt(1, orderId);
                            pstmt.setInt(2, loanId);
                            pstmt.setInt(3, t6252.F11);
                            pstmt.setInt(4, pid);
                            pstmt.setBigDecimal(5, t6252.F07);
                            pstmt.setInt(6, t6252.F05);
                            pstmt.execute();
                        }
                        orderIds.add(orderId);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("BidManageImpl.addPtdfOrder() error", e);
            throw e;
        }
        return orderIds;
    }
    
    /**
     * 平台垫付查询还款记录
     * 
     * @param loanId
     *            标id
     * @return
     * @throws SQLException
     */
    protected T6252[] selectPtdfAllT6252(Connection connection, int loanId)
        throws SQLException
    {
        StringBuilder sb = new StringBuilder("SELECT F05, F07, F11 FROM S62.T6252 WHERE T6252.F02 = ? AND F09=?");
        List<Object> parameters = new ArrayList<>();
        parameters.add(loanId);
        parameters.add(T6252_F09.WH);
        if (T5131_F02.BJ == selectT5131(connection))
        {
            sb.append(" AND F05=?");
            parameters.add(FeeCode.TZ_BJ);
        }
        sb.append(" ORDER BY T6252.F05 DESC");
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
     * 查询平台垫付类型
     */
    private T5131_F02 selectT5131(Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S51.T5131 LIMIT 1"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return T5131_F02.parse(resultSet.getString(1));
                }
            }
        }
        return null;
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
        try (PreparedStatement ps = connection.prepareStatement("SELECT F12 FROM S62.T6230 WHERE F01=?"))
        {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    t6230_F12 = EnumParser.parse(T6230_F12.class, rs.getString(1));
                }
            }
        }
        StringBuilder sb = new StringBuilder("SELECT F05, SUM(F07),F11 FROM S62.T6252 WHERE T6252.F02 = ? AND F09=?");
        List<Object> parameters = new ArrayList<>();
        parameters.add(loanId);
        parameters.add(T6252_F09.WH);
        if (t6230_F12 == T6230_F12.BJQEDB)
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
    
    @Override
    public Blob getAttachment(int id)
        throws Throwable
    {
        String sql = "SELECT F06 FROM S62.T6233 WHERE F01=?";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBlob(1);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public PagingResult<TzjlEntity> searchTzjl(TzjlEntity query, Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T6230.F01 AS F01, T6230.F03 AS F02, T6230.F06 AS F03, T6230.F20 AS F04, T6231.F18 AS F05, T6250.F05 AS F06, T6250.F06 AS F07 ");
        sql.append(" FROM S62.T6250 INNER JOIN S62.T6230 ON T6250.F02 = T6230.F01 ");
        sql.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 ");
        sql.append(" WHERE T6250.F03 = ? ");
        parameters.add(serviceResource.getSession().getAccountId());
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        String name = query.name;
        if (!StringHelper.isEmpty(name))
        {
            sql.append(" AND T6230.F03 LIKE ? ");
            parameters.add(sqlConnectionProvider.allMatch(name));
        }
        T6230_F20 type = query.type;
        if (type != null)
        {
            sql.append(" AND T6230.F20 = ? ");
            parameters.add(type.name());
        }
        Timestamp tjStartTime = query.tjStartTime;
        if (tjStartTime != null)
        {
            sql.append(" AND DATE(T6250.F06) >= ? ");
            parameters.add(tjStartTime);
        }
        Timestamp tjEndTime = query.tjEndTime;
        if (tjEndTime != null)
        {
            sql.append(" AND DATE(T6250.F06) <= ? ");
            parameters.add(tjEndTime);
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<TzjlEntity>()
            {
                
                @Override
                public TzjlEntity[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<TzjlEntity> list = null;
                    while (rs.next())
                    {
                        TzjlEntity entity = new TzjlEntity();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        entity.id = rs.getInt(1);
                        entity.name = rs.getString(2);
                        entity.rate = rs.getBigDecimal(3);
                        entity.type = T6230_F20.parse(rs.getString(4));
                        entity.endTime = rs.getTimestamp(5);
                        entity.money = rs.getBigDecimal(6);
                        entity.startTime = rs.getTimestamp(7);
                        list.add(entity);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new TzjlEntity[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public Mbxx getMbxx(int loanId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Mbxx record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0), F08 FROM S62.T6252 WHERE F02=?  AND F05 IN (?,?) AND F09 = ?  LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                pstmt.setInt(2, FeeCode.TZ_LX);
                pstmt.setInt(3, FeeCode.TZ_BJ);
                pstmt.setString(4, T6252_F09.WH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new Mbxx();
                        record.dhje = resultSet.getBigDecimal(1);
                        record.F08 = resultSet.getDate(2);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public boolean isXgwj(int loanId)
        throws Throwable
    {
        boolean b = false;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S62.T6232 WHERE T6232.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        b = true;
                    }
                }
            }
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S62.T6233 WHERE T6233.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        b = true;
                    }
                }
            }
        }
        return b;
    }
    
    @Override
    public T6250[] getRecord(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6250> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6250 WHERE T6250.F02 = ? AND T6250.F07 = ?  ORDER BY F06 DESC"))
            {
                pstmt.setInt(1, id);
                pstmt.setString(2, T6250_F07.F.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6250 record = new T6250();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6250_F07.parse(resultSet.getString(7));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6250[list.size()]));
        }
    }
    
    @Override
    public Bdylx[] getDylb(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Bdylx> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F03 FROM S62.T6234 WHERE T6234.F02 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Bdylx record = new Bdylx();
                        record.F01 = resultSet.getInt(1);
                        record.F03 = resultSet.getInt(2);
                        record.dyName = getDblx(record.F03);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new Bdylx[list.size()]));
        }
    }
    
    /**
     * 获取担保类型名称
     * 
     * @param id
     * @return
     */
    private String getDblx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S62.T6213 WHERE T6213.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
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
    
    @Override
    public Bdysx[] getDysx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Bdysx> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03, F04 FROM S62.T6235 WHERE T6235.F02 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Bdysx record = new Bdysx();
                        record.F03 = resultSet.getInt(1);
                        record.F04 = resultSet.getString(2);
                        record.dxsxName = getDbsx(record.F03);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new Bdysx[list.size()]));
        }
    }
    
    /**
     * 获取担保属性名称
     * 
     * @param id
     * @return
     */
    private String getDbsx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S62.T6214 WHERE T6214.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
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
    
    @Override
    public T6240 getXXZQ(int bidId)
        throws Throwable
    {
        if (bidId <= 0)
            return null;
        T6240 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6240 WHERE T6240.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, bidId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6240();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public Dbxx getDB(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6236.F01 AS F01, T6236.F02 AS F02, T6236.F03 AS F03, T6236.F04 AS F04, T6180.F02 AS F05,(SELECT T6161.F04 FROM S61.T6161 WHERE T6161.F01 = T6236.F03), T6236.F05 AS F07 FROM S62.T6236 LEFT JOIN S61.T6180 ON T6236.F03 = T6180.F01 WHERE T6236.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    Dbxx record = null;
                    if (resultSet.next())
                    {
                        record = new Dbxx();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = T6236_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
                    }
                    return record;
                }
            }
        }
    }
    
    @Override
    public T6237 getFk(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03 FROM S62.T6237 WHERE T6237.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    T6237 record = null;
                    if (resultSet.next())
                    {
                        record = new T6237();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                    }
                    return record;
                }
            }
        }
    }
    
    @Override
    public Tztjxx getStatisticsGr()
        throws Throwable
    {
        Tztjxx statistics = new Tztjxx();
        String sql =
            "SELECT IFNULL(SUM(T6230.F05),0),COUNT(*) FROM S62.T6230,S61.T6110 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN (?,?,?) AND T6230.F27 = ? AND T6110.F06 = ?";
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(sql);)
            {
                ps.setString(1, T6230_F20.YDF.name());
                ps.setString(2, T6230_F20.YJQ.name());
                ps.setString(3, T6230_F20.HKZ.name());
                ps.setString(4, T6230_F27.F.name());
                ps.setString(5, T6110_F06.ZRR.name());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        statistics.totleMoney = rs.getBigDecimal(1);
                        statistics.totleCount = rs.getLong(2);
                    }
                }
            }
        }
        
        statistics.userEarnMoney = getEarnMoneyGr();
        return statistics;
    }
    
    private BigDecimal getEarnMoneyGr()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6252.F07) FROM S62.T6252,S62.T6230,S61.T6110 WHERE"
                    + " T6252.F02 = T6230.F01 AND T6230.F02 = T6110.F01 AND T6252.F05 IN (?,?) AND T6252.F09 = ? AND  T6110.F06 = ? AND  T6230.F27 = ? "))
            {
                ps.setInt(1, FeeCode.TZ_LX);
                ps.setInt(2, FeeCode.TZ_WYJ);
                ps.setString(3, T6252_F09.YH.name());
                ps.setString(4, T6110_F06.ZRR.name());
                ps.setString(5, T6230_F27.F.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public T5124[] getLevel()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T5124> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02 FROM S51.T5124 WHERE T5124.F05 = ?"))
            {
                pstmt.setString(1, T5124_F05.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T5124 record = new T5124();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T5124[list.size()]));
        }
    }
    
    @Override
    public PagingResult<Bdlb> search(BidQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, T6231.F21 AS F19, T6231.F22 AS F20 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F27 = 'F' AND T6230.F20 IN (?,?,?,?,?,?) AND T6110.F06 = ?");
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.DFK);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YDF);
        parameters.add(T6110_F06.ZRR);
        if (query != null)
        {
            boolean notFirst = false;
            T6211[] t6211s = query.getType();
            if (t6211s != null && t6211s.length > 0)
            {
                Set<T6211> valieLevels = new LinkedHashSet<>();
                for (T6211 t6211 : t6211s)
                {
                    if (t6211 == null)
                    {
                        continue;
                    }
                    valieLevels.add(t6211);
                }
                if (valieLevels.size() > 0)
                {
                    notFirst = false;
                    sql.append(" AND (");
                    for (T6211 info : valieLevels)
                    {
                        
                        if (notFirst)
                        {
                            sql.append(" OR ");
                        }
                        else
                        {
                            notFirst = true;
                        }
                        sql.append("T6230.F04 = ?");
                        parameters.add(info.F01);
                    }
                    sql.append(")");
                }
            }
            
            T6230_F20[] levels = query.getStatus();
            if (levels != null && levels.length > 0)
            {
                Set<T6230_F20> valieLevels = new LinkedHashSet<>();
                for (T6230_F20 level : levels)
                {
                    if (level == null)
                    {
                        continue;
                    }
                    valieLevels.add(level);
                }
                if (valieLevels.size() > 0)
                {
                    notFirst = false;
                    sql.append(" AND (");
                    for (T6230_F20 valieLevel : valieLevels)
                    {
                        
                        if (notFirst)
                        {
                            sql.append(" OR ");
                        }
                        else
                        {
                            notFirst = true;
                        }
                        sql.append(" T6230.F20 = ?");
                        parameters.add(valieLevel.name());
                    }
                    sql.append(")");
                }
            }
            CreditTerm[] terms = query.getTerm();
            if (terms != null && terms.length > 0)
            {
                Set<CreditTerm> validTerms = new LinkedHashSet<>();
                for (CreditTerm term : terms)
                {
                    if (term == null)
                    {
                        continue;
                    }
                    validTerms.add(term);
                }
                if (validTerms.size() > 0)
                {
                    notFirst = false;
                    sql.append(" AND (");
                    for (CreditTerm term : validTerms)
                    {
                        if (notFirst)
                        {
                            sql.append(" OR ");
                        }
                        else
                        {
                            notFirst = true;
                        }
                        switch (term)
                        {
                            case SGYYX:
                            {
                                sql.append(" T6230.F09 < 3 ");
                                break;
                            }
                            case SDLGY:
                            {
                                sql.append("(T6230.F09 >= 3 AND T6230.F09 <= 6)");
                                break;
                            }
                            case LDJGY:
                            {
                                sql.append("(T6230.F09 >= 6 AND T6230.F09 <= 9)");
                                break;
                            }
                            case JDSEGY:
                            {
                                sql.append("(T6230.F09 >= 9 AND T6230.F09 <= 12)");
                                break;
                            }
                            case SEGYYS:
                            {
                                sql.append(" T6230.F09 > 12 ");
                                break;
                            }
                            default:
                                break;
                        }
                    }
                    sql.append(")");
                    
                }
            }
        }
        sql.append("  ORDER BY T6230.F20, T6230.F22 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Bdlb>()
            {
                @Override
                public Bdlb[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Bdlb> list = null;
                    while (resultSet.next())
                    {
                        Bdlb record = new Bdlb();
                        record.F01 = resultSet.getString(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getInt(10);
                        record.F11 = T6230_F20.parse(resultSet.getString(11));
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = resultSet.getString(15);
                        record.F16 = T6230_F11.parse(resultSet.getString(16));
                        record.F17 = T6230_F13.parse(resultSet.getString(17));
                        record.F18 = T6230_F14.parse(resultSet.getString(18));
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        record.F19 = T6231_F21.parse(resultSet.getString(19));
                        record.F20 = resultSet.getInt(20);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Bdlb[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public Hkjllb[] getHk(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Hkjllb> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F07), F08, F09, F10, ( SELECT T5122.F02 FROM S51.T5122 WHERE T6252.F05 = T5122.F01 ) FROM S62.T6252 WHERE T6252.F02 = ? GROUP BY T6252.F08,F05"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Hkjllb record = new Hkjllb();
                        record.F01 = resultSet.getBigDecimal(1);
                        record.F02 = resultSet.getDate(2);
                        record.F03 = T6252_F09.parse(resultSet.getString(3));
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getString(5);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new Hkjllb[list.size()]));
        }
    }
    
    @Override
    public Tztjxx getStatisticsQy()
        throws Throwable
    {
        Tztjxx statistics = new Tztjxx();
        String sql =
            "SELECT IFNULL(SUM(T6230.F05),0),COUNT(*) FROM S62.T6230,S61.T6110 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN (?,?,?) AND T6230.F27 = ? AND T6110.F06 = ?";
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(sql);)
            {
                ps.setString(1, T6230_F20.YDF.name());
                ps.setString(2, T6230_F20.YJQ.name());
                ps.setString(3, T6230_F20.HKZ.name());
                ps.setString(4, T6230_F27.F.name());
                ps.setString(5, T6110_F06.FZRR.name());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        statistics.totleMoney = rs.getBigDecimal(1);
                        statistics.totleCount = rs.getLong(2);
                    }
                }
            }
        }
        
        statistics.userEarnMoney = getEarnMoneyQy();
        return statistics;
    }
    
    private BigDecimal getEarnMoneyQy()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6252.F07) FROM S62.T6252,S62.T6230,S61.T6110 WHERE"
                    + " T6252.F02 = T6230.F01 AND T6230.F02 = T6110.F01 AND T6252.F05 IN (?,?,?) AND T6252.F09 = ? AND  T6230.F27 = ? "))
            {
                ps.setInt(1, FeeCode.TZ_LX);
                ps.setInt(2, FeeCode.TZ_WYJ);
                ps.setInt(3, FeeCode.TZ_FX);
                ps.setString(4, T6252_F09.YH.name());
                ps.setString(5, T6230_F27.F.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public T6232[] getFjgk(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6232> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S62.T6232 WHERE T6232.F02 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6232 record = new T6232();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getInt(6);
                        record.F07 = resultSet.getString(7);
                        record.F08 = resultSet.getTimestamp(8);
                        record.F09 = resultSet.getInt(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6232[list.size()]));
        }
    }
    
    @Override
    public T6233[] getFjfgk(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6233> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S62.T6233 WHERE T6233.F02 = ?"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6233 record = new T6233();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
                        record.F08 = resultSet.getTimestamp(8);
                        record.F09 = resultSet.getInt(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6233[list.size()]));
        }
    }
    
    @Override
    public T6212[] getT6212(int loanId, boolean b)
        throws Throwable
    {
        String sql = "";
        if (b)
        {
            sql = "SELECT F01, F02 FROM S62.T6212 WHERE  T6212.F01 IN  (SELECT F03 FROM S62.T6233 WHERE T6233.F02 = ?)";
        }
        else
        {
            sql = "SELECT F01, F02 FROM S62.T6212 WHERE  T6212.F01 IN  (SELECT F03 FROM S62.T6232 WHERE T6232.F02 = ?)";
        }
        try (Connection connection = getConnection())
        {
            ArrayList<T6212> list = null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6212 record = new T6212();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6212[list.size()]));
        }
    }
    
    @Override
    public Tztjxx getStatisticsXxzq()
        throws Throwable
    {
        Tztjxx statistics = new Tztjxx();
        String sql =
            "SELECT IFNULL(SUM(T6230.F26),0),COUNT(*) FROM S62.T6230 WHERE  T6230.F20 IN (?,?,?) AND T6230.F27 = ?";
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(sql);)
            {
                ps.setString(1, T6230_F20.YDF.name());
                ps.setString(2, T6230_F20.YJQ.name());
                ps.setString(3, T6230_F20.HKZ.name());
                ps.setString(4, T6230_F27.S.name());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        statistics.totleMoney = rs.getBigDecimal(1);
                        statistics.totleCount = rs.getLong(2);
                    }
                }
            }
            statistics.userEarnMoney = getEarnMoneyXxzq(conn);
        }
        
        return statistics;
    }
    
    private BigDecimal getEarnMoneyXxzq(Connection connection)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT SUM(T6252.F07) FROM S62.T6252,S62.T6230 WHERE"
                + " T6252.F02 = T6230.F01  AND T6252.F05 IN (?,?) AND T6252.F09 = ? AND  T6230.F27 = ? "))
        {
            ps.setInt(1, FeeCode.TZ_LX);
            ps.setInt(2, FeeCode.TZ_WYJ);
            ps.setString(3, T6252_F09.YH.name());
            ps.setString(4, T6230_F27.S.name());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getBigDecimal(1);
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public PagingResult<Bdlb> searchCredit(int userId, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18,T6230.F10 AS F19 FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S51.T5124 ON T6230.F23 = T5124.F01");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append(" WHERE T6230.F20 IN (?,?,?,?) AND T6230.F02 = ?");
        parameters.add(T6230_F20.TBZ);
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YFB);
        parameters.add(T6230_F20.DFK);
        parameters.add(userId);
        
        sql.append("  ORDER BY T6230.F20 , T6230.F22 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Bdlb>()
            {
                @Override
                public Bdlb[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Bdlb> list = null;
                    while (resultSet.next())
                    {
                        Bdlb record = new Bdlb();
                        record.F01 = resultSet.getString(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getInt(10);
                        record.F11 = T6230_F20.parse(resultSet.getString(11));
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = resultSet.getString(15);
                        record.F16 = T6230_F11.parse(resultSet.getString(16));
                        record.F17 = T6230_F13.parse(resultSet.getString(17));
                        record.F18 = T6230_F14.parse(resultSet.getString(18));
                        record.F21 = T6230_F10.parse(resultSet.getString(19));
                        record.proess =
                            (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Bdlb[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public int updateBidStatus(int loanId)
        throws Throwable
    {
        if (loanId <= 0)
        {
            return 0;
        }
        int num = 0;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6230_F20 t6230_F20 = T6230_F20.SQZ;
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F20 FROM S62.T6230 WHERE F01 = ? FOR UPDATE"))
                {
                    ps.setInt(1, loanId);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            t6230_F20 = EnumParser.parse(T6230_F20.class, rs.getString(1));
                        }
                    }
                }
                if (t6230_F20 == T6230_F20.SQZ || t6230_F20 == T6230_F20.DSH)
                {
                    try (PreparedStatement ps =
                        connection.prepareStatement("UPDATE S62.T6230 SET F20=? WHERE F01=? AND F02=?"))
                    {
                        ps.setString(1, T6230_F20.YZF.name());
                        ps.setInt(2, loanId);
                        ps.setInt(3, serviceResource.getSession().getAccountId());
                        num = ps.executeUpdate();
                    }
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            return num;
        }
    }
    
    @Override
    public Bdxq getT6230(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Bdxq record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26,ADDDATE(T6230.F22,INTERVAL T6230.F08 DAY) FROM S62.T6230"
                    + " WHERE T6230.F01 = ? AND T6230.F20 IN (?,?,?,?,?,?,?)"))
            {
                pstmt.setInt(1, id);
                pstmt.setString(2, T6230_F20.YFB.name());
                pstmt.setString(3, T6230_F20.TBZ.name());
                pstmt.setString(4, T6230_F20.DFK.name());
                pstmt.setString(5, T6230_F20.HKZ.name());
                pstmt.setString(6, T6230_F20.YJQ.name());
                pstmt.setString(7, T6230_F20.YDF.name());
                pstmt.setString(8, T6230_F20.YLB.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new Bdxq();
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
                        record.jsTime = resultSet.getTimestamp(27);
                        // record.djmc = getDjmc(record.F23);
                        record.proess =
                            (record.F05.doubleValue() - record.F07.doubleValue()) / record.F05.doubleValue();
                    }
                }
            }
            return record;
        }
    }
    
    /** {@inheritDoc} */
    
    @Override
    public boolean isYQ(int loanId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return isYQ(connection, loanId);
        }
    }
    
    private boolean isYQ(Connection connection, int loanId)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT COUNT(1) FROM S62.T6252 INNER JOIN S62.T6230 ON T6252.F02 = T6230.F01 WHERE T6252.F08 < CURRENT_DATE() AND T6252.F09 = ? AND T6230.F01 = ? "))
        {
            ps.setString(1, T6252_F09.WH.name());
            ps.setInt(2, loanId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next() && rs.getInt(1) > 0)
                {
                    return true;
                }
            }
        }
        return false;
    }
    
}
