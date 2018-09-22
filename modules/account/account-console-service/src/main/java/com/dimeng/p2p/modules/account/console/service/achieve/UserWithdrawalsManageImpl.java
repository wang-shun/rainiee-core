/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service.achieve;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S50.entities.T5019;
import com.dimeng.p2p.S50.enums.T5020_F03;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6130;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S61.enums.T6130_F16;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.modules.account.console.service.UserWithdrawalsManage;
import com.dimeng.p2p.modules.account.console.service.entity.Bank;
import com.dimeng.p2p.modules.account.console.service.entity.Txgl;
import com.dimeng.p2p.modules.account.console.service.entity.UserWithdrawals;
import com.dimeng.p2p.modules.account.console.service.query.TxglRecordQuery;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;

/**
 * @author guopeng
 * 
 */
public class UserWithdrawalsManageImpl extends AbstractUserService implements UserWithdrawalsManage
{
    
    public UserWithdrawalsManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<UserWithdrawals> search(TxglRecordQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6130.F01 AS F01, T6130.F02 AS F02, T6130.F03 AS F03, T6130.F04 AS F04, T6130.F06 AS F05, T6130.F07 AS F06, T6130.F08 AS F07, T6130.F09 AS F08, T6130.F10 AS F09, T6130.F11 AS F10, T6130.F12 AS F11, T6130.F13 AS F12, T6130.F14 AS F13, T6130.F15 AS F14, T6110.F02 AS F15, T6110.F06 AS F16,T6114.F04 AS F17,T5020.F02 AS F18,T6114.F07 AS F19,T6114.F05 AS F20,T6130.F16 AS F21,T6114.F11 AS F22 FROM S61.T6130 INNER JOIN S61.T6110 ON T6130.F02 = T6110.F01 INNER JOIN S61.T6114 ON T6130.F03=T6114.F01 INNER JOIN S50.T5020 ON T6114.F03=T5020.F01 WHERE 1=1 AND T6130.F02 <> (SELECT T7101.F01 FROM S71.T7101) ");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String userName = query.getYhm();
            if (!StringHelper.isEmpty(userName))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(userName));
            }
            String yhkh = query.getYhkh();
            if (!StringHelper.isEmpty(yhkh))
            {
                sql.append(" AND T6114.F06 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(yhkh));
            }
            String lsh = query.getLsh();
            if (!StringHelper.isEmpty(lsh))
            {
                sql.append(" AND T6130.F01 =?");
                parameters.add(lsh);
            }
            int bankId = query.getBankId();
            if (bankId > 0)
            {
                sql.append(" AND T6114.F03 = ?");
                parameters.add(bankId);
            }
            T6130_F09 status = query.getStatus();
            Timestamp datetime = query.getStartExtractionTime();
            if (datetime != null)
            {
                if (status == T6130_F09.DSH)
                {
                    sql.append(" AND DATE(T6130.F08)>=?");
                }
                else if (status == T6130_F09.DFK)
                {
                    sql.append(" AND DATE(T6130.F11) >= ?");
                }
                else if (status == T6130_F09.YFK)
                {
                    sql.append(" AND DATE(T6130.F14) >= ?");
                }
                else if (status == T6130_F09.TXSB)
                {
                    sql.append(" AND (DATE(T6130.F14) >= ? or DATE(T6130.F11)>=?)");
                    parameters.add(datetime);
                }
                else if (status == T6130_F09.FKZ)
                {
                    sql.append(" AND DATE(T6130.F14) >= ?");
                }
                parameters.add(datetime);
            }
            datetime = query.getEndExtractionTime();
            if (datetime != null)
            {
                if (status == T6130_F09.DSH)
                {
                    sql.append(" AND DATE(T6130.F08)<=?");
                }
                else if (status == T6130_F09.DFK)
                {
                    sql.append(" AND DATE(T6130.F11) <= ?");
                }
                else if (status == T6130_F09.YFK)
                {
                    sql.append(" AND DATE(T6130.F14) <= ?");
                }
                else if (status == T6130_F09.TXSB)
                {
                    sql.append(" AND (DATE(T6130.F14) <= ? or DATE(T6130.F11)<=?)");
                    parameters.add(datetime);
                }
                else if (status == T6130_F09.FKZ)
                {
                    sql.append(" AND DATE(T6130.F14) <= ?");
                }
                parameters.add(datetime);
            }
            if (status != null)
            {
                sql.append(" AND T6130.F09=?");
                parameters.add(status);
            }
            sql.append(" GROUP BY T6130.F01");
            if (status == T6130_F09.DSH)
            {
                sql.append(" ORDER BY T6130.F08 DESC");
            }
            else if (status == T6130_F09.DFK)
            {
                sql.append(" ORDER BY T6130.F11 DESC");
            }
            else if (status == T6130_F09.YFK)
            {
                sql.append(" ORDER BY T6130.F14 DESC");
            }
            else if (status == T6130_F09.TXSB)
            {
                sql.append(" ORDER BY T6130.F11 DESC");
            }
            else if (status == T6130_F09.FKZ)
            {
                sql.append(" ORDER BY T6130.F11 DESC");
            }
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<UserWithdrawals>()
            {
                
                @Override
                public UserWithdrawals[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<UserWithdrawals> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        UserWithdrawals record = new UserWithdrawals();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F06 = resultSet.getBigDecimal(5);
                        record.F07 = resultSet.getBigDecimal(6);
                        record.F08 = resultSet.getTimestamp(7);
                        record.F09 = T6130_F09.parse(resultSet.getString(8));
                        record.F10 = resultSet.getInt(9);
                        record.F11 = resultSet.getTimestamp(10);
                        record.F12 = resultSet.getString(11);
                        record.F13 = resultSet.getInt(12);
                        record.F14 = resultSet.getTimestamp(13);
                        record.F15 = resultSet.getString(14);
                        record.userName = resultSet.getString(15);
                        record.userType = T6110_F06.parse(resultSet.getString(16));
                        record.location = getRegion(resultSet.getInt(17), connection);
                        record.extractionBank = resultSet.getString(18);
                        record.bankId = resultSet.getString(19);
                        record.subbranch = resultSet.getString(20);
                        record.shName = getName(connection, record.F10);
                        record.txName = getName(connection, record.F13);
                        record.F16 = T6130_F16.parse(resultSet.getString(21));
                        record.realName =
                            StringHelper.isEmpty(resultSet.getString(22)) ? getUserName(record.F02,
                                record.userType.name(),
                                connection) : resultSet.getString(22);
                        T5019 t5019 = getShengName(connection, resultSet.getInt(17));
                        if (t5019 != null)
                        {
                            record.shengName = t5019.F06;
                            record.shiName = t5019.F07;
                        }
                        lists.add(record);
                    }
                    return lists.toArray(new UserWithdrawals[lists.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    protected String getName(Connection connection, int id)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S71.T7110 WHERE T7110.F01 = ? LIMIT 1"))
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
        return "";
    }
    
    @Override
    public UserWithdrawals get(int id)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6130.F01 AS F01, T6130.F02 AS F02, T6130.F03 AS F03, T6130.F04 AS F04, T6130.F06 AS F05, T6130.F07 AS F06, T6130.F08 AS F07, T6130.F09 AS F08, T6130.F10 AS F09, T6130.F11 AS F10, T6130.F12 AS F11, T6130.F13 AS F12, T6130.F14 AS F13, T6130.F15 AS F14, T6110.F02 AS F15, T6110.F06 AS F16,T6114.F04 AS F17,T5020.F02 AS F18,T6114.F07 AS F19,T6114.F05 AS F20,T6130.F16 AS F21, T6114.F11 AS F22, T6114.F12 AS F23, T5020.F04 AS F24 FROM S61.T6130 INNER JOIN S61.T6110 ON T6130.F02 = T6110.F01 INNER JOIN S61.T6114 ON T6130.F03=T6114.F01 INNER JOIN S50.T5020 ON T6114.F03=T5020.F01 WHERE T6130.F01=?");
        try (Connection connection = getConnection())
        {
            UserWithdrawals userWithdrawals = select(connection, new ItemParser<UserWithdrawals>()
            {
                @Override
                public UserWithdrawals parse(ResultSet resultSet)
                    throws SQLException
                {
                    UserWithdrawals record = new UserWithdrawals();
                    if (resultSet.next())
                    {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F06 = resultSet.getBigDecimal(5);
                        record.F07 = resultSet.getBigDecimal(6);
                        record.F08 = resultSet.getTimestamp(7);
                        record.F09 = T6130_F09.parse(resultSet.getString(8));
                        record.F10 = resultSet.getInt(9);
                        record.F11 = resultSet.getTimestamp(10);
                        record.F12 = resultSet.getString(11);
                        record.F13 = resultSet.getInt(12);
                        record.F14 = resultSet.getTimestamp(13);
                        record.F15 = resultSet.getString(14);
                        record.userName = resultSet.getString(15);
                        record.userType = T6110_F06.parse(resultSet.getString(16));
                        record.location = getRegion(resultSet.getInt(17), connection);
                        record.extractionBank = resultSet.getString(18);
                        record.bankId = resultSet.getString(19);
                        record.subbranch = resultSet.getString(20);
                        record.shName = getName(connection, record.F10);
                        record.txName = getName(connection, record.F13);
                        record.realName =
                            StringHelper.isEmpty(resultSet.getString(22)) ? getUserName(record.F02,
                                record.userType.name(),
                                connection) : resultSet.getString(22); // 开户名
                        // (兴业银行)银行卡类型0-储蓄卡;1-信用卡;2-企业账户 & 人行体系支付号
                        record.bankCardType = resultSet.getInt(23);
                        record.bankSystemCode = resultSet.getString(24);
                    }
                    return record;
                }
            },
                sql.toString(),
                id);
            // 注释 by chenbin@taojinde.com 2015-7-8 产品邮件定的统一显示方式
            /*
             * if (userWithdrawals != null) { if (userWithdrawals.userType ==
             * T6110_F06.ZRR) { try (Connection connection = getConnection()) {
             * try (PreparedStatement ps = connection
             * .prepareStatement("SELECT F02 FROM S61.T6141 WHERE F01=?")) {
             * ps.setInt(1, userWithdrawals.F02); try (ResultSet rs =
             * ps.executeQuery()) { if (rs.next()) { userWithdrawals.realName =
             * rs.getString(1); } } } } } else if (userWithdrawals.userType ==
             * T6110_F06.FZRR) { try (Connection connection = getConnection()) {
             * try (PreparedStatement ps = connection
             * .prepareStatement("SELECT F04 FROM S61.T6161 WHERE F01=?")) {
             * ps.setInt(1, userWithdrawals.F02); try (ResultSet rs =
             * ps.executeQuery()) { if (rs.next()) { userWithdrawals.realName =
             * rs.getString(1); } } } } } }
             */
            return userWithdrawals;
        }
    }
    
    @Override
    public void check(boolean passed, String check_reason, int... ids)
        throws Throwable
    {
        if (ids == null || ids.length == 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                int accountId = serviceResource.getSession().getAccountId();
                T6130_F09 t6130_F09 = passed ? T6130_F09.DFK : T6130_F09.TXSB;
                Timestamp currentTimestamp = getCurrentTimestamp(connection);
                for (int id : ids)
                {
                    T6110 t6110 = getUserInfo(id, connection);
                    if (T6110_F07.HMD == t6110.F07)
                    {
                        throw new LogicalException("用户:" + t6110.F02 + "已经被拉黑，不能进行提现！");
                    }
                    UserWithdrawals userWithdrawals = get(id);
                    if (userWithdrawals == null)
                    {
                        throw new LogicalException("提现申请记录不存在");
                    }
                    if (userWithdrawals.F09 != T6130_F09.DSH)
                    {
                        throw new LogicalException("提现申请不是待审核状态,不能进行审核操作");
                    }
                    try (PreparedStatement ps =
                        connection.prepareStatement("UPDATE S61.T6130 SET F09 = ?, F10 = ?, F11 = ?, F12 = ? WHERE F01 = ?"))
                    {
                        ps.setString(1, t6130_F09.name());
                        ps.setInt(2, accountId);
                        ps.setTimestamp(3, currentTimestamp);
                        ps.setString(4, check_reason);
                        ps.setInt(5, id);
                        ps.execute();
                    }
                    if (!passed)
                    {
                        rollback(connection, userWithdrawals);
                    }
                }
                if (passed)
                {
                    writeLog(connection, "操作日志", "提现审核通过");
                }
                else
                {
                    writeLog(connection, "操作日志", String.format("提现审核不通过,原因:%s", check_reason));
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
    
    @Override
    public int[] fk(boolean passed, String check_reason, int... ids)
        throws Throwable
    {
        if (ids == null || ids.length == 0)
        {
            return new int[0];
        }
        try (Connection connection = getConnection())
        {
            int[] orderIds = new int[ids.length];
            int index = 0;
            try
            {
                serviceResource.openTransactions(connection);
                // 开启事物
                connection.setAutoCommit(false);
                int accountId = serviceResource.getSession().getAccountId();
                T6130_F09 t6130_F09 = passed ? T6130_F09.YFK : T6130_F09.TXSB;
                Timestamp currentTimestamp = getCurrentTimestamp(connection);
                for (int id : ids)
                {
                    T6110 t6110 = getUserInfo(id, connection);
                    
                    if (T6110_F07.HMD == t6110.F07)
                    {
                        throw new LogicalException("用户:" + t6110.F02 + "已经被拉黑，不能进行提现！");
                    }
                    UserWithdrawals userWithdrawals = get(id);
                    if (userWithdrawals == null)
                    {
                        throw new LogicalException("提现申请记录不存在");
                    }
                    if (userWithdrawals.F09 != T6130_F09.DFK)
                    {
                        throw new LogicalException("提现申请不是待放款状态,不能进行放款操作");
                    }
                    if (passed)
                    {
                        try (PreparedStatement ps =
                            connection.prepareStatement("UPDATE S61.T6130 SET F13 = ?, F14 = ?, F15 = ? WHERE F01 = ?"))
                        {
                            ps.setInt(1, accountId);
                            ps.setTimestamp(2, currentTimestamp);
                            ps.setString(3, check_reason);
                            ps.setInt(4, id);
                            ps.execute();
                        }
                        orderIds[index++] = addOrder(connection, userWithdrawals);
                    }
                    else
                    {
                        try (PreparedStatement ps =
                            connection.prepareStatement("UPDATE S61.T6130 SET F09 = ?, F13 = ?, F14 = ?, F15 = ? WHERE F01 = ?"))
                        {
                            ps.setString(1, t6130_F09.name());
                            ps.setInt(2, accountId);
                            ps.setTimestamp(3, currentTimestamp);
                            ps.setString(4, check_reason);
                            ps.setInt(5, id);
                            ps.execute();
                        }
                        rollback(connection, userWithdrawals);
                    }
                }
                if (passed)
                {
                    writeLog(connection, "操作日志", "提现放款通过");
                }
                else
                {
                    writeLog(connection, "操作日志", String.format("提现放款不通过,原因:%s", check_reason));
                }
                // 提交事物，同时解除订单的锁定状态
                connection.commit();
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            finally
            {
                
                connection.setAutoCommit(true);
            }
            return orderIds;
        }
    }
    
    // 创建订单
    private int addOrder(Connection connection, UserWithdrawals userWithdrawals)
        throws Throwable
    {
        int orderId = 0;
        String status = "";
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T.F01 ,T.F03 FROM  S65.T6501 T left join  S65.T6503 T1 on T.F01=T1.F01  WHERE  T1.F09=? order by T.F01 desc limit 1"))
        {
            pstmt.setInt(1, userWithdrawals.F01);
            
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    orderId = rs.getInt(1);
                    status = rs.getString(2);
                }
            }
            if (orderId > 0)
            {
                writeLog(connection, "操作日志放款审核订单已存在.......", "orderId: " + orderId + "  status:  " + status);
                return orderId;
            }
        }
        
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?,F03 = ?, F04 = ?, F07 = ?, F09 = ?, F08 = ? ",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, OrderType.WITHDRAW.orderType());
            pstmt.setString(2, T6501_F03.DTJ.name());
            pstmt.setTimestamp(3, getCurrentTimestamp(connection));
            pstmt.setString(4, T6501_F07.HT.name());
            pstmt.setInt(5, serviceResource.getSession().getAccountId());
            pstmt.setInt(6, userWithdrawals.F02);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    orderId = resultSet.getInt(1);
                }
            }
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6503 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?,F09 = ?"))
        {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, userWithdrawals.F02);
            pstmt.setBigDecimal(3, userWithdrawals.F04);
            pstmt.setBigDecimal(4, userWithdrawals.F06);
            pstmt.setBigDecimal(5, userWithdrawals.F07);
            pstmt.setString(6, StringHelper.decode(userWithdrawals.bankId));
            pstmt.setInt(7, userWithdrawals.F01);
            pstmt.execute();
        }
        return orderId;
    }
    
    /**
     * 提现失败
     * 
     * @param check_reason
     * @param txglRecord
     * @throws Throwable
     */
    private void rollback(Connection connection, UserWithdrawals userWithdrawals)
        throws Throwable
    {
        if (userWithdrawals == null)
        {
            throw new ParameterException("提现记录不存在");
        }
        T6101 sdzh = selectT6101(connection, userWithdrawals.F02, T6101_F03.SDZH, true);
        if (sdzh == null)
        {
            throw new LogicalException("用户锁定资金账户不存在");
        }
        T6101 wlzh = selectT6101(connection, userWithdrawals.F02, T6101_F03.WLZH, true);
        if (wlzh == null)
        {
            throw new LogicalException("用户往来资金账户不存在");
        }
        {
            if (sdzh.F06.compareTo(userWithdrawals.F04.add(userWithdrawals.F07)) < 0)
            {
                throw new LogicalException("用户锁定资金账户余额不足");
            }
            {
                T6102 t6102 = new T6102();
                t6102.F02 = sdzh.F01;
                t6102.F03 = FeeCode.TXSB;
                t6102.F04 = wlzh.F01;
                t6102.F07 = userWithdrawals.F04;
                sdzh.F06 = sdzh.F06.subtract(userWithdrawals.F04);
                t6102.F08 = sdzh.F06;
                t6102.F09 = "提现撤销,本金返还";
                insertT6102(connection, t6102);
            }
            {
                T6102 t6102 = new T6102();
                t6102.F02 = sdzh.F01;
                t6102.F03 = FeeCode.TXSB_SXF;
                t6102.F04 = wlzh.F01;
                t6102.F07 = userWithdrawals.F07;
                sdzh.F06 = sdzh.F06.subtract(userWithdrawals.F07);
                t6102.F08 = sdzh.F06;
                t6102.F09 = "提现撤销,手续费返还";
                insertT6102(connection, t6102);
            }
            // 扣减锁定账户资金
            updateT6101(connection, sdzh.F01, sdzh.F06);
        }
        {
            {
                T6102 t6102 = new T6102();
                t6102.F02 = wlzh.F01;
                t6102.F03 = FeeCode.TXSB;
                t6102.F04 = sdzh.F01;
                t6102.F06 = userWithdrawals.F04;
                wlzh.F06 = wlzh.F06.add(userWithdrawals.F04);
                t6102.F08 = wlzh.F06;
                t6102.F09 = "提现撤销,本金返还";
                insertT6102(connection, t6102);
            }
            {
                T6102 t6102 = new T6102();
                t6102.F02 = wlzh.F01;
                t6102.F03 = FeeCode.TXSB_SXF;
                t6102.F04 = sdzh.F01;
                t6102.F06 = userWithdrawals.F07;
                wlzh.F06 = wlzh.F06.add(userWithdrawals.F07);
                t6102.F08 = wlzh.F06;
                t6102.F09 = "提现撤销,手续费返还";
                insertT6102(connection, t6102);
            }
            // 增加往来账户资金
            updateT6101(connection, wlzh.F01, wlzh.F06);
        }
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("name", wlzh.F05);
        envionment.set("datetime", DateTimeParser.format(userWithdrawals.F08));
        // boolean flag =
        // Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.TXSXF_KCFS));
        BigDecimal je = userWithdrawals.F04.add(userWithdrawals.F07);
        /*
         * if(flag){ je = je.add(userWithdrawals.F07); }
         */
        envionment.set("amount", je.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        String content = configureProvider.format(LetterVariable.TX_SB, envionment);
        sendLetter(connection, userWithdrawals.F02, "提现失败", content);
        // sendMsg(connection, wlzh.F04, content);
        T6110 t6110 = selectT6110(connection, userWithdrawals.F02);
        String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
        if ("1".equals(isUseYtx))
        {
            SMSUtils smsUtils = new SMSUtils(configureProvider);
            int type = smsUtils.getTempleId(MsgVariavle.TX_SB.getDescription());
            sendMsg(connection,
                t6110.F04,
                smsUtils.getSendContent(envionment.get("name"), envionment.get("datetime"), envionment.get("amount")),
                type);
            
        }
        else
        {
            String msgContent = configureProvider.format(MsgVariavle.TX_SB, envionment);
            sendMsg(connection, t6110.F04, msgContent);
        }
    }
    
    private void updateT6101(Connection connection, int F01, BigDecimal F06)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ?  WHERE F01 = ? "))
        {
            pstmt.setBigDecimal(1, F06);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F01);
            pstmt.execute();
        }
    }
    
    @Override
    public Bank[] getBanks()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectAll(connection, new ArrayParser<Bank>()
            {
                
                @Override
                public Bank[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<Bank> banks = new ArrayList<>();
                    while (resultSet.next())
                    {
                        Bank bank = new Bank();
                        bank.id = resultSet.getInt(1);
                        bank.name = resultSet.getString(2);
                        banks.add(bank);
                    }
                    return banks.toArray(new Bank[banks.size()]);
                }
            }, "SELECT F01,F02 FROM S50.T5020 WHERE F03 = ?", T5020_F03.QY);
        }
    }
    
    @Override
    public void importData(InputStream inputStream, String real_name, String charset)
        throws Throwable
    {
        
    }
    
    /**
     * 查询区域名称
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    protected String getRegion(int id, Connection connection)
        throws SQLException
    {
        if (id <= 0)
        {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F06,F07,F08 FROM S50.T5019 WHERE F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        sb.append(rs.getString(1));
                        sb.append(",");
                        sb.append(rs.getString(2));
                        sb.append(",");
                        sb.append(rs.getString(3));
                    }
                }
            }
        }
        return sb.toString();
    }
    
    @Override
    public Txgl getExtractionInfo()
        throws Throwable
    {
        Txgl extraction = new Txgl();
        try (Connection conn = getConnection())
        {
            try (PreparedStatement pst =
                conn.prepareStatement("SELECT F01, F02, F03 FROM S70.T7023 ORDER BY F03 DESC LIMIT 1"))
            {
                try (ResultSet rst = pst.executeQuery())
                {
                    if (rst.next())
                    {
                        extraction.totalAmount = rst.getBigDecimal(1);
                        extraction.charge = rst.getBigDecimal(2);
                        extraction.updateTime = rst.getTimestamp(3);
                    }
                }
            }
        }
        return extraction;
    }
    
    @Override
    public void export(UserWithdrawals[] txglRecords, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        if (txglRecords == null)
        {
            return;
        }
        
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            // writer.write("流水号");
            writer.write("用户名");
            writer.write("提现银行");
            writer.write("开户名");
            writer.write("省");
            writer.write("市");
            writer.write("所在支行 ");
            writer.write("银行卡号");
            writer.write("提现金额(元)");
            writer.write("手续费(元)");
            writer.write("是否到账");
            writer.write("审核时间");
            writer.write("操作人");
            writer.newLine();
            for (UserWithdrawals txglRecord : txglRecords)
            {
                if (txglRecord == null)
                {
                    continue;
                }
                // writer.write(txglRecord.F01);
                writer.write(txglRecord.userName);
                writer.write(txglRecord.extractionBank);
                writer.write(txglRecord.realName);
                writer.write(txglRecord.shengName);
                writer.write(txglRecord.shiName);
                writer.write(txglRecord.subbranch);
                writer.write(StringHelper.decode(txglRecord.bankId) + "\t");
                writer.write(txglRecord.F04);
                writer.write(txglRecord.F07);
                writer.write("否");
                writer.write(DateTimeParser.format(txglRecord.F11) + "\t");
                writer.write(txglRecord.shName);
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportYtx(UserWithdrawals[] txglRecords, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        if (txglRecords == null)
        {
            return;
        }
        
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            // writer.write("流水号");
            writer.write("用户名");
            writer.write("提现银行");
            writer.write("开户名");
            writer.write("省");
            writer.write("市");
            writer.write("所在支行 ");
            writer.write("银行卡号");
            writer.write("提现金额(元)");
            writer.write("手续费(元)");
            writer.write("是否到账");
            writer.write("放款时间");
            writer.write("放款人");
            writer.newLine();
            for (UserWithdrawals txglRecord : txglRecords)
            {
                if (txglRecord == null)
                {
                    continue;
                }
                // writer.write(txglRecord.F01);
                writer.write(txglRecord.userName);
                writer.write(txglRecord.extractionBank);
                writer.write(txglRecord.realName);
                writer.write(txglRecord.shengName);
                writer.write(txglRecord.shiName);
                writer.write(txglRecord.subbranch);
                writer.write(StringHelper.decode(txglRecord.bankId) + "\t");
                writer.write(txglRecord.F04);
                writer.write(txglRecord.F07);
                writer.write(txglRecord.F16.getChineseName());
                writer.write(DateTimeParser.format(txglRecord.F14) + "\t");
                writer.write(StringHelper.isEmpty(txglRecord.txName) ? "自动放款" : txglRecord.txName);
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportYtxContent(UserWithdrawals[] txglRecords, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        if (txglRecords == null)
        {
            return;
        }
        
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("流水号");
            writer.write("用户名");
            writer.write("提现银行");
            writer.write("所在支行 ");
            writer.write("银行卡号");
            writer.write("提现金额(元)");
            writer.write("手续费(元)");
            writer.write("是否到账");
            writer.write("放款时间");
            writer.write("放款人");
            writer.newLine();
            for (UserWithdrawals txglRecord : txglRecords)
            {
                if (txglRecord == null)
                {
                    continue;
                }
                writer.write(txglRecord.F01);
                writer.write(txglRecord.userName);
                writer.write(txglRecord.extractionBank);
                writer.write(txglRecord.subbranch);
                writer.write("\t" + StringHelper.decode(txglRecord.bankId));
                writer.write(txglRecord.F04);
                writer.write(txglRecord.F07);
                writer.write(txglRecord.F16.getChineseName());
                writer.write(DateTimeParser.format(txglRecord.F14) + "\t");
                writer.write(txglRecord.txName);
                writer.newLine();
            }
        }
    }
    
    @Override
    public BigDecimal getTxze()
        throws Throwable
    {
        BigDecimal txze = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6503.F03),0) FROM S65.T6503 INNER JOIN S65.T6501 ON T6501.F01=T6503.F01 WHERE T6501.F03=? AND T6503.F02 <> (SELECT T7101.F01 FROM S71.T7101)"))
            {
                ps.setString(1, T6501_F03.CG.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        txze = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return txze;
    }
    
    @Override
    public BigDecimal getTxsxf()
        throws Throwable
    {
        BigDecimal txsxf = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F07) FROM S61.T6130 WHERE F09=? AND T6130.F02 <> (SELECT T7101.F01 FROM S71.T7101)"))
            {
                ps.setString(1, T6130_F09.YFK.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        txsxf = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return txsxf;
    }
    
    protected T6110 getUserInfo(int id, Connection conn)
        throws Throwable
    {
        
        //锁定提现表(如果不锁定在几个页面同时点放款的时候就会出现问题)
        T6130 record = null;
        try (PreparedStatement pstmt =
            conn.prepareStatement("SELECT  F01, F02  FROM S61.T6130 WHERE  F01 = ?  FOR UPDATE "))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6130();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                }
            }
        }
        
        T6110 t6110 = new T6110();
        try (PreparedStatement ps =
            conn.prepareStatement("SELECT T6110.F02,T6110.F07 FROM S61.T6110 WHERE T6110.F01= ?  "))
        {
            ps.setInt(1, record.F02);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    t6110.F02 = rs.getString(1);
                    t6110.F07 = T6110_F07.parse(rs.getString(2));
                }
            }
        }
        return t6110;
    }
    
    protected String getUserName(int id, String userType, Connection connection)
        throws SQLException
    {
        
        try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S61.T6141 WHERE F01=?"))
        {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getString(1);
                }
            }
        }
        return "";
    }
    
    protected T5019 getShengName(Connection connection, int id)
        throws SQLException
    {
        // 托管前缀
        String escrow = serviceResource.getResource(ConfigureProvider.class).getProperty(SystemVariable.ESCROW_PREFIX);
        T5019 record = null;
        if ("FUYOU".equals(escrow))
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F06, F07, F08 FROM S50.T5019 WHERE T5019.F01 = ? OR T5019.F03 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                pstmt.setInt(2, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T5019();
                        record.F01 = resultSet.getInt(1);
                        record.F06 = resultSet.getString(2);
                        record.F07 = resultSet.getString(3);
                        record.F08 = resultSet.getString(4);
                    }
                }
            }
        }
        else if (!StringHelper.isEmpty(escrow) && "shuangqian".equals(escrow.toLowerCase()))
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F05, F06 FROM S50.T5019_SQ WHERE T5019_SQ.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T5019();
                        record.F01 = resultSet.getInt(1);
                        record.F06 = resultSet.getString(2);
                        record.F07 = resultSet.getString(3);
                    }
                }
            }
        }
        else
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F06, F07, F08 FROM S50.T5019 WHERE T5019.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T5019();
                        record.F01 = resultSet.getInt(1);
                        record.F06 = resultSet.getString(2);
                        record.F07 = resultSet.getString(3);
                        record.F08 = resultSet.getString(4);
                    }
                }
            }
        }
        return record;
    }
}
