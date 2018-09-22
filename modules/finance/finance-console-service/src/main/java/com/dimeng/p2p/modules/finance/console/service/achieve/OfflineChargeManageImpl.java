package com.dimeng.p2p.modules.finance.console.service.achieve;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S60.enums.T6032_F03;
import com.dimeng.p2p.S70.entities.T7049;
import com.dimeng.p2p.S70.enums.T7026_F06;
import com.dimeng.p2p.S70.enums.T7049_F05;
import com.dimeng.p2p.modules.finance.console.service.OfflineChargeManage;
import com.dimeng.p2p.modules.finance.console.service.entity.OfflineChargeRecord;
import com.dimeng.p2p.modules.finance.console.service.query.OfflineChargeQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class OfflineChargeManageImpl extends AbstractFinanceService implements
        OfflineChargeManage {

    public static class OfflineChargeManageFactory implements
            ServiceFactory<OfflineChargeManage> {

        @Override
        public OfflineChargeManage newInstance(ServiceResource serviceResource) {
            return new OfflineChargeManageImpl(serviceResource);
        }
    }

    public OfflineChargeManageImpl(ServiceResource serviceResource) {
        super(serviceResource);

    }

    @Override
    public PagingResult<OfflineChargeRecord> search(OfflineChargeQuery query,
                                                    Paging paging) throws Throwable {
        StringBuilder sql = new StringBuilder(
                "SELECT T7049.F01 AS F01, T7049.F02 AS F02, T7049.F03 AS F03, T7049.F04 AS F04, T7049.F05 AS F05, T7049.F06 AS F06, T7049.F07 AS F07, T7049.F08 AS F08, T7049.F09 AS F09, T7049.F10 AS F10, T6010.F02 AS F11, T7011.F02 AS F12, A.F02 AS F13 FROM S70.T7049 INNER JOIN S60.T6010 ON T7049.F02 = T6010.F01 LEFT JOIN S70.T7011 ON T7049.F04 = T7011.F01 LEFT JOIN S70.T7011 AS A ON T7049.F08 = A.F01 WHERE 1 = 1");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null) {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String account = query.getAccount();
            if (!StringHelper.isEmpty(account)) {
                sql.append(" AND T6010.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(account));
            }
            Date date = query.getCreateStartDate();
            if (date != null) {
                sql.append(" AND DATE(T7049.F03) >= ?");
                parameters.add(date);
            }
            date = query.getCreateEndDate();
            if (date != null) {
                sql.append(" AND DATE(T7049.F03) <= ?");
                parameters.add(date);
            }
            T7049_F05 status = query.getStatus();
            if (status != null) {
                sql.append(" AND T7049.F05 = ?");
                parameters.add(status.name());
            }
        }
        sql.append(" ORDER BY T7049.F01 DESC");
        try(Connection connection = getConnection(P2PConst.DB_CONSOLE))
        {
            return selectPaging(connection,
                    new ArrayParser<OfflineChargeRecord>() {
                        ArrayList<OfflineChargeRecord> list = new ArrayList<OfflineChargeRecord>();

                        @Override
                        public OfflineChargeRecord[] parse(ResultSet rs)
                                throws SQLException {
                            while (rs.next()) {
                                OfflineChargeRecord record = new OfflineChargeRecord();
                                record.F01 = rs.getInt(1);
                                record.F02 = rs.getInt(2);
                                record.F03 = rs.getTimestamp(3);
                                record.F04 = rs.getInt(4);
                                record.F05 = T7049_F05.parse(rs.getString(5));
                                record.F06 = rs.getBigDecimal(6);
                                record.F07 = rs.getString(7);
                                record.F08 = rs.getInt(8);
                                record.F09 = rs.getTimestamp(9);
                                record.F10 = rs.getString(10);
                                record.F11 = rs.getString(11);
                                record.F12 = rs.getString(12);
                                record.F13 = rs.getString(13);
                                list.add(record);
                            }
                            return list.toArray(new OfflineChargeRecord[list.size()]);
                        }
                    }, paging, sql.toString(), parameters);
        }
    }

    @Override
    public int add(String accountName, BigDecimal amount, String remark)
            throws Throwable {
        if (StringHelper.isEmpty(accountName)) {
            throw new ParameterException("用户账号不存在");
        }
        if (amount == null) {
            throw new ParameterException("必须填写充值金额");
        }
        if (amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new ParameterException("充值金额必须大于零");
        }
        try(Connection connection = getConnection())
        {
            int userId;
            try {
                serviceResource.openTransactions(connection);
                try (PreparedStatement ps = connection
                        .prepareStatement("SELECT F01 FROM S60.T6010 WHERE F02 = ?")) {
                    ps.setString(1, accountName);
                    try (ResultSet resultSet = ps.executeQuery()) {
                        if (resultSet.next()) {
                            userId = resultSet.getInt(1);
                        } else {
                            throw new LogicalException("用户账号不存在");
                        }
                    }
                }
                int rtn =
                        insert(connection,
                                "INSERT INTO S70.T7049 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?",
                                userId, getCurrentTimestamp(connection),
                                serviceResource.getSession().getAccountId(),
                                T7049_F05.DSH,
                                amount,
                                remark);

                serviceResource.commit(connection);
                return rtn;
            } catch (Exception e) {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }

    @Override
    public void cancel(int id) throws Throwable {
        try(Connection connection = getConnection())
        {
            try {
                serviceResource.openTransactions(connection);
                try (PreparedStatement ps = connection
                        .prepareStatement("SELECT F05 FROM S70.T7049 WHERE F01 = ? FOR UPDATE")) {
                    ps.setInt(1, id);
                    try (ResultSet resultSet = ps.executeQuery()) {
                        if (resultSet.next()) {
                            T7049_F05 t7049_F05 = T7049_F05.parse(resultSet
                                    .getString(1));
                            if (t7049_F05 == T7049_F05.YQX) {
                                return;
                            }
                            if (t7049_F05 != T7049_F05.DSH) {
                                throw new LogicalException("不是待审核状态,不能取消");
                            }
                        } else {
                            throw new LogicalException("充值记录不存在");
                        }
                    }
                }
                try (PreparedStatement ps = connection
                        .prepareStatement("UPDATE S70.T7049 SET F05 = ? WHERE F01 = ?")) {
                    ps.setString(1, T7049_F05.YQX.name());
                    ps.setInt(2, id);
                    ps.executeUpdate();
                }
                serviceResource.commit(connection);
            } catch (Exception e) {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }

    @Override
    public void check(int id, boolean passed) throws Throwable {
        T7049 t7049 = new T7049();
        try(Connection connection = getConnection())
        {
            try {
                serviceResource.openTransactions(connection);
                try (PreparedStatement ps = connection
                        .prepareStatement("SELECT F02, F05, F06, F07 FROM S70.T7049 WHERE F01 = ? FOR UPDATE")) {
                    ps.setInt(1, id);
                    try (ResultSet resultSet = ps.executeQuery()) {
                        if (resultSet.next()) {
                            t7049.F01 = id;
                            t7049.F02 = resultSet.getInt(1);
                            t7049.F05 = T7049_F05.parse(resultSet.getString(2));
                            t7049.F06 = resultSet.getBigDecimal(3);
                            t7049.F07 = resultSet.getString(4);
                            if (t7049.F05 != T7049_F05.DSH) {
                                throw new LogicalException("不是待审核状态,不能审核通过！");
                            }
                        } else {
                            throw new LogicalException("充值记录不存在！");
                        }
                    }
                }
                Timestamp time = getCurrentTimestamp(connection);
                if (!passed)
                {
                    try (PreparedStatement ps =
                                 connection.prepareStatement("UPDATE S70.T7049 SET F05 = ?, F08 = ?, F09 = ? WHERE F01 = ?"))
                    {
                        ps.setString(1, T7049_F05.YQX.name());
                        ps.setInt(2, serviceResource.getSession().getAccountId());
                        ps.setTimestamp(3,time);
                        ps.setInt(4, id);
                        ps.executeUpdate();
                    }
                }
                else
                {
                    BigDecimal balance = null; // 用户剩余金额
                    try (PreparedStatement ps =
                                 connection.prepareStatement("SELECT F03 FROM S60.T6023 WHERE F01 = ? FOR UPDATE"))
                    {
                        ps.setInt(1, t7049.F02);
                        try (ResultSet resultSet = ps.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                balance = resultSet.getBigDecimal(1);
                            }
                            else
                            {
                                throw new LogicalException("用户资金账户不存在！");
                            }
                        }
                    }
                    // 更新用户账号信息
                    try (PreparedStatement ps =
                                 connection.prepareStatement("UPDATE S60.T6023 SET F03 = F03 + ?, F05 = F05 + ? WHERE F01 = ?"))
                    {
                        ps.setBigDecimal(1, t7049.F06);
                        ps.setBigDecimal(2, t7049.F06);
                        ps.setInt(3, t7049.F02);
                        ps.executeUpdate();
                    }
                    // 插入资金交易记录，充值
                    try (PreparedStatement ps =
                                 connection.prepareStatement("INSERT INTO S60.T6032 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F07 = ?, F08 = ?, F09 = ?"))
                    {
                        ps.setInt(1, t7049.F02);
                        ps.setString(2, T6032_F03.XXCZ.name());
                        ps.setTimestamp(3,time);
                        ps.setBigDecimal(4, t7049.F06);
                        ps.setBigDecimal(5, balance.add(t7049.F06));
                        ps.setInt(6, t7049.F01);
                        ps.setString(7, t7049.F07);
                        ps.execute();
                    }
                }
                BigDecimal sysBalance = null; // 平台剩余金额
                try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S70.T7025 "))
                {
                    try (ResultSet resultSet = ps.executeQuery()) {
                        if (resultSet.next()) {
                            sysBalance = resultSet.getBigDecimal(1);
                        } else {
                            throw new LogicalException("平台资金账户不存在！");
                        }
                    }
                }
                // 更新平台资金账户信息
                try (PreparedStatement ps =
                             connection.prepareStatement("UPDATE S70.T7025 SET F01 = F01+?, F03 = F03+?, F07 = F07+?"))
                {
                    ps.setBigDecimal(1, t7049.F06);
                    ps.setBigDecimal(2, t7049.F06);
                    ps.setInt(3, t7049.F02);
                    ps.executeUpdate();
                }
                // 插入平台资金流水表信息
                try (PreparedStatement ps =
                             connection.prepareStatement("INSERT INTO S70.T7026 SET F02 = ?, F03 = ?, F05 = ?, F06 = ?, F07 = ?,F08 = ?,F09 =? "))
                {
                    ps.setTimestamp(1,time);
                    ps.setBigDecimal(2, t7049.F06);
                    ps.setBigDecimal(3, sysBalance.add(t7049.F06));
                    ps.setString(4, T7026_F06.XXCZ.name());
                    ps.setString(5, t7049.F07);
                    ps.setInt(6, t7049.F01);
                    ps.setInt(7, t7049.F02);
                    ps.execute();
                }
                try (PreparedStatement ps =
                             connection.prepareStatement("UPDATE S70.T7049 SET F05 = ?, F08 = ?, F09 = ? WHERE F01 = ?"))
                {
                    ps.setString(1, T7049_F05.YCZ.name());
                    ps.setInt(2, serviceResource.getSession().getAccountId());
                    ps.setTimestamp(3,time);
                    ps.setInt(4, id);
                    ps.executeUpdate();
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

}
