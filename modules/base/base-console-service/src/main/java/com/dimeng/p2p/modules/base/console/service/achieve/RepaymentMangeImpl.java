package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.entities.T6290;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6290_F04;
import com.dimeng.p2p.S62.enums.T6290_F06;
import com.dimeng.p2p.common.entities.BillRemindSet;
import com.dimeng.p2p.modules.base.console.service.RepaymentManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class RepaymentMangeImpl extends AbstractInformationService implements RepaymentManage
{
    
    public static class TermManageFactory implements ServiceFactory<RepaymentManage>
    {
        
        @Override
        public RepaymentManage newInstance(ServiceResource serviceResource)
        {
            return new RepaymentMangeImpl(serviceResource);
        }
        
    }
    
    public RepaymentMangeImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int add(T6290 t6290)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            //return insert(getConnection(),
            return insert(connection,
                "INSERT INTO S62.T6290 SET F02= ?,F03=?,F04=?,F05=?,F06=?",
                t6290.F02,
                t6290.F03,
                t6290.F04,
                t6290.F05,
                t6290.F06);
        }
    }
    
    @Override
    public void update(T6290 t6290)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                execute(connection,
                    "UPDATE S62.T6290 SET F02= ?,F03=?,F04=?,F05=?,F06=? WHERE F01 = ?",
                    t6290.F02,
                    t6290.F03,
                    t6290.F04,
                    t6290.F05,
                    t6290.F06,
                    t6290.F01);
                writeLog(connection, "操作日志", "账单提醒修改");
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        
    }
    
    /*@Override
    public T6290 get(int repaymentId)
        throws Throwable
    {
        if (repaymentId <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            //return select(getConnection(),
            return select(connection, new ItemParser<T6290>()
            {
                
                @Override
                public T6290 parse(ResultSet rs)
                    throws SQLException
                {
                    T6290 product = null;
                    if (rs.next())
                    {
                        product = new T6290();
                        product.F01 = rs.getInt(1);
                        product.F02 = rs.getString(2);
                        product.F03 = rs.getInt(3);
                        product.F04 = rs.getInt(4);
                        product.F05 = rs.getInt(5);
                        product.F06 = T6290_F06.parse(rs.getString(6));
                        
                    }
                    return product;
                }
                
            }, "SELECT F01,F02,F03,F04,F05,F06 FROM S62.T6290 WHERE F01 = ? ", repaymentId);
        }
    }*/
    
    @Override
    public void enable(int repaymentId)
        throws Throwable
    {
        if (repaymentId <= 0)
        {
            return;
        }
        try (Connection connection = getConnection("S62"))
        {
            //execute(getConnection("S62"),
            execute(connection, "UPDATE T6290 SET F06 =? WHERE F01 = ?", T6290_F06.QY, repaymentId);
        }
        
    }
    
    @Override
    public void disable(int repaymentId)
        throws Throwable
    {
        if (repaymentId <= 0)
        {
            return;
        }
        try (Connection connection = getConnection("S62"))
        {
            //execute(getConnection("S62"),
            execute(connection, "UPDATE T6290 SET F06 =? WHERE F01 = ?", T6290_F06.TY, repaymentId);
        }
        
    }
    
    /*@Override
    public T6290 getValidRepaymentSet()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            //return select(getConnection(),
            return select(connection, new ItemParser<T6290>()
            {
                
                @Override
                public T6290 parse(ResultSet rs)
                    throws SQLException
                {
                    T6290 product = null;
                    if (rs.next())
                    {
                        product = new T6290();
                        product.F01 = rs.getInt(1);
                        product.F02 = rs.getString(2);
                        product.F03 = rs.getInt(3);
                        product.F04 = rs.getInt(4);
                        product.F05 = rs.getInt(5);
                        product.F06 = T6290_F06.parse(rs.getString(6));
                        
                    }
                    return product;
                }
                
            }, "SELECT F01,F02,F03,F04,F05,F06 FROM S62.T6290 WHERE F06 = ? ", T6290_F06.QY);
        }
    }*/
    
    /*@Override
    public T6290 getRepaymentSet()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            //return select(getConnection(),
            return select(connection, new ItemParser<T6290>()
            {
                
                @Override
                public T6290 parse(ResultSet rs)
                    throws SQLException
                {
                    T6290 product = null;
                    if (rs.next())
                    {
                        product = new T6290();
                        product.F01 = rs.getInt(1);
                        product.F02 = rs.getString(2);
                        product.F03 = rs.getInt(3);
                        product.F04 = rs.getInt(4);
                        product.F05 = rs.getInt(5);
                        product.F06 = T6290_F06.parse(rs.getString(6));
                        
                    }
                    return product;
                }
                
            }, "SELECT F01,F02,F03,F04,F05,F06 FROM S62.T6290");
        }
    }*/
    
    @Override
    public T6252[] selectT6252s(String condition)
        throws Throwable
    {
        //Connection connection=getConnection();
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT t.F01, t.F02, t.F03, t.F04, t.F05, t.F06, SUM(t.F07), t.F08, t.F09, t.F10, t.F11,s.F02 as accountName,b.F03 as bidTitle,s.F04 as phone, s.F05 as email FROM S62.T6252 t "
                    + " LEFT JOIN S61.T6110 s ON t.F03=s.F01 "
                    + " LEFT JOIN S62.T6230 b ON b.F01=t.F02 "
                    + " WHERE  t.F09 = ? ";
            
            sql += " AND " + condition + " GROUP BY t.F02 ";
            ArrayList<T6252> list = null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setString(1, T6252_F09.WH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6252 record = new T6252();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getInt(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getDate(8);
                        record.F09 = T6252_F09.parse(resultSet.getString(9));
                        record.F10 = resultSet.getTimestamp(10);
                        record.F11 = resultSet.getInt(11);
                        record.accountName = resultSet.getString(12);
                        record.bidTitle = resultSet.getString(13);
                        record.phone = resultSet.getString(14);
                        record.email = resultSet.getString(15);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6252[list.size()]));
        }
    }
    
    @Override
    public void addT6124(int letterId, String title)
        throws Throwable
    {
        String sql = "INSERT INTO S61.T6124 SET F01=?,F02=?";
        
        try (Connection connection = getConnection())
        {
            //try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql))
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, letterId);
                ps.setString(2, title);
                ps.executeUpdate();
            }
        }
        
    }
    
    @Override
    public int addT6123(int userId, String title, Timestamp timestamp)
        throws Throwable
    {
        int inta = 0;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                //this.getConnection()
                connection.prepareStatement("INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = CURRENT_TIMESTAMP(), F05 = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, userId);
                pstmt.setString(2, title);
                pstmt.setString(3, T6123_F05.WD.name());
                pstmt.execute();
                try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                {
                    if (resultSet.next())
                    {
                        inta = resultSet.getInt(1);
                    }
                }
            }
        }
        
        return inta;
    }
    
    @Override
    public int getPassedCount(Date date)
        throws Throwable
    {
        int inta = 0;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
            //this.getConnection()
                connection.prepareStatement("SELECT DATEDIFF(NOW()," + date + ") FROM DUAL"))
            {
                
                try (ResultSet resultSet = pstmt.executeQuery();)
                {
                    if (resultSet.next())
                    {
                        inta = resultSet.getInt(1);
                    }
                }
            }
        }
        
        return inta;
    }
    
    @Override
    public List<T6290> getT6290List(T6290_F04 F04, T6290_F06 F06)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6290.F01,T6290.F02,T6290.F03,T6290.F04,T6290.F05,T6290.F06 FROM S62.T6290 WHERE T6290.F04=?");
        if (null != F06)
        {
            sql.append(" AND T6290.F06=?");
        }
        sql.append(" ORDER BY T6290.F01");
        ArrayList<T6290> list = null;
        try (Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql.toString());)
        {
            pstmt.setString(1, F04.name());
            if (null != F06)
            {
                pstmt.setString(2, F06.name());
            }
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                T6290 record = null;
                while (resultSet.next())
                {
                    record = new T6290();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = T6290_F04.parse(resultSet.getString(4));
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = T6290_F06.parse(resultSet.getString(6));
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
    
    @Override
    public void updateT6290(BillRemindSet billRemindSet)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            serviceResource.openTransactions(connection);
            try
            {
                //修改还款提醒设置信息
                updateT6290(billRemindSet, connection);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private void updateT6290(BillRemindSet billRemindSet, Connection connection)
        throws Throwable
    {
        Timestamp now = getCurrentTimestamp(connection);
        String[] hkDay = billRemindSet.hkDay;
        String[] yqDay = billRemindSet.yqDay;
        String[] hktxType = billRemindSet.hktxType;
        String hkStatus = billRemindSet.hkStatus;
        String[] yqtxType = billRemindSet.yqtxType;
        String yqStatus = billRemindSet.yqStatus;
        if (null == hkDay || null == yqDay || null == hktxType || StringHelper.isEmpty(hkStatus) || null == yqtxType
            || StringHelper.isEmpty(yqStatus))
        {
            throw new ParameterException("参数错误");
        }
        
        delT6290s(connection);
        for (int i = 0, j = hkDay.length; i < j; i++)
        {
            addT6290(connection,
                StringUtils.join(hktxType, ","),
                IntegerParser.parse(hkDay[i]),
                T6290_F04.HKTX,
                now,
                T6290_F06.parse(hkStatus));
        }
        for (int i = 0, j = yqDay.length; i < j; i++)
        {
            addT6290(connection,
                StringUtils.join(yqtxType, ","),
                IntegerParser.parse(yqDay[i]),
                T6290_F04.YQTX,
                now,
                T6290_F06.parse(yqStatus));
        }
        
    }
    
    private void delT6290s(Connection connection)
        throws Throwable
    {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM S62.T6290"))
        {
            ps.execute();
        }
    }
    
    private int addT6290(Connection connection, String F02, int F03, T6290_F04 F04, Timestamp F05, T6290_F06 F06)
        throws Throwable
    {
        return insert(connection,
            "INSERT INTO S62.T6290 SET F02= ?,F03=?,F04=?,F05=?,F06=?",
            F02,
            F03,
            F04.name(),
            F05,
            F06.name());
    }
    
}
