package com.dimeng.p2p.modules.account.console.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.entities.T6120;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F13;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6120_F05;
import com.dimeng.p2p.S62.enums.T6280_F10;
import com.dimeng.p2p.S71.entities.T7110;
import com.dimeng.p2p.S71.entities.T7152;
import com.dimeng.p2p.S71.enums.T7110_F05;
import com.dimeng.p2p.S71.enums.T7151_F06;
import com.dimeng.p2p.S71.enums.T7152_F04;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

public class UserManageImpl extends AbstractUserService implements UserManage
{
    
    public UserManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public void lock(int userId, String lockDesc)
        throws Throwable
    {
        if (userId > 0)
        {
            T6110_F07 f07 = null;
            try (Connection connection = getConnection())
            {
                try
                {
                    serviceResource.openTransactions(connection);
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT F07 FROM S61.T6110 WHERE F01 = ? FOR UPDATE"))
                    {
                        pstmt.setInt(1, userId);
                        try (ResultSet resultSet = pstmt.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                f07 = T6110_F07.parse(resultSet.getString(1));
                            }
                        }
                    }
                    
                    if (f07 == null)
                    {
                        throw new ParameterException("账号不存在");
                    }
                    if (T6110_F07.SD == f07)
                    {
                        throw new ParameterException("此账号已锁定");
                    }
                    if (T6110_F07.HMD == f07)
                    {
                        throw new ParameterException("此账号已拉黑");
                    }
                    
                    execute(connection, "UPDATE S61.T6110 SET F07 = ? WHERE F01 = ?", T6110_F07.SD.name(), userId);
                    execute(connection, "UPDATE S62.T6280 SET F10 = ? WHERE F01 = ?", T6280_F10.TY.name(), userId);
                    //锁定用户，删除用户的会话信息
                    execute(connection, "DELETE FROM S11._1030 WHERE F04 = ? ", userId);
                    writeLog(connection, "操作日志", "锁定用户");
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
    
    @Override
    public void unLock(int userId, String lockDesc)
        throws Throwable
    {
        if (userId > 0)
        {
            T6110_F07 f07 = null;
            try (Connection connection = getConnection())
            {
                try
                {
                    serviceResource.openTransactions(connection);
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT F07 FROM S61.T6110 WHERE F01 = ? FOR UPDATE"))
                    {
                        pstmt.setInt(1, userId);
                        try (ResultSet resultSet = pstmt.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                f07 = T6110_F07.parse(resultSet.getString(1));
                            }
                        }
                    }
                    
                    if (f07 == null)
                    {
                        throw new ParameterException("账号不存在");
                    }
                    if (T6110_F07.QY == f07)
                    {
                        throw new ParameterException("此账号已启用");
                    }
                    if (T6110_F07.HMD == f07)
                    {
                        throw new ParameterException("此账号已拉黑");
                    }
                    
                    execute(connection, "UPDATE S61.T6110 SET F07 = ? WHERE F01 = ?", T6110_F07.QY.name(), userId);
                    execute(connection, "UPDATE S62.T6280 SET F10 = ? WHERE F01 = ?", T6280_F10.QY.name(), userId);
                    writeLog(connection, "操作日志", "解锁用户");
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
    
    @Override
    public void black(int id, String blacklistDesc)
        throws Throwable
    {
        if (id <= 0 || StringHelper.isEmpty(blacklistDesc))
        {
            throw new ParameterException("参数不能为空");
        }
        T6110_F07 f07 = null;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F07 FROM S61.T6110 WHERE F01=? FOR UPDATE"))
                {
                    pstmt.setInt(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            f07 = T6110_F07.parse(resultSet.getString(1));
                        }
                    }
                }
                
                if (f07 == null)
                {
                    throw new ParameterException("账号不存在");
                }
                if (T6110_F07.HMD == f07)
                {
                    throw new ParameterException("此账号已拉黑");
                }
                if (T6110_F07.SD == f07)
                {
                    throw new ParameterException("此账号已锁定");
                }
                
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6110 SET F07 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, T6110_F07.HMD.name());
                    pstmt.setInt(2, id);
                    pstmt.executeUpdate();
                }
                
                insert(connection,
                    "INSERT INTO S71.T7151 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?",
                    id,
                    serviceResource.getSession().getAccountId(),
                    blacklistDesc,
                    getCurrentTimestamp(connection),
                    T7151_F06.LH.name());
                
                boolean exists = false;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01 FROM S62.T6280  WHERE F01=? FOR UPDATE"))
                {
                    pstmt.setInt(1, id);
                    try (ResultSet rs = pstmt.executeQuery();)
                    {
                        if (rs.next())
                        {
                            exists = true;
                        }
                    }
                }
                
                if (exists)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6280 SET F10 = ? WHERE F01 = ?"))
                    {
                        pstmt.setString(1, T6280_F10.TY.name());
                        pstmt.setInt(2, id);
                        pstmt.executeUpdate();
                    }
                }
                
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S11._1030 SET F04 = NULL WHERE F04 = ?"))
                {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                }
                writeLog(connection, "操作日志", "拉黑用户");
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
    public void unBlack(int id, String blacklistDesc)
        throws Throwable
    {
        T6110_F07 f07 = null;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F07 FROM S61.T6110 WHERE F01=? FOR UPDATE"))
                {
                    pstmt.setInt(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            f07 = T6110_F07.parse(resultSet.getString(1));
                        }
                    }
                }
                
                if (f07 == null)
                {
                    throw new ParameterException("账号不存在");
                }
                if (T6110_F07.QY == f07)
                {
                    throw new ParameterException("此账号已启用");
                }
                if (T6110_F07.SD == f07)
                {
                    throw new ParameterException("此账号已锁定");
                }
                
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F02 FROM S71.T7151 WHERE F01=? FOR UPDATE"))
                {
                    ps.setInt(1, id);
                    
                    ps.executeQuery();
                }
                
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6110 SET F07 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, T6110_F07.QY.name());
                    pstmt.setInt(2, id);
                    
                    pstmt.executeUpdate();
                }
                
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S71.T7151 SET F06 = ? WHERE F02 = ?"))
                {
                    pstmt.setString(1, T7151_F06.QXLH.name());
                    pstmt.setInt(2, id);
                    
                    pstmt.executeUpdate();
                }
                writeLog(connection, "操作日志", "取消拉黑");
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
    public boolean isExists(String userName)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE F02= ?"))
            {
                pstmt.setString(1, userName);
                try (ResultSet rs = pstmt.executeQuery())
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
    public int getUserIdByName(String userName)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE F02= ?"))
            {
                pstmt.setString(1, userName);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public T6112 getFc(int userId)
        throws Throwable
    {
        T6112 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S61.T6112 WHERE T6112.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6112();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getFloat(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getInt(8);
                        record.F09 = resultSet.getString(9);
                        record.F10 = resultSet.getString(10);
                        record.F11 = resultSet.getBigDecimal(11);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public T6113 getCc(int userId)
        throws Throwable
    {
        T6113 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6113 WHERE T6113.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6113();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public T6120 getAuthent(int userId)
        throws Throwable
    {
        T6120 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6120 WHERE T6120.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6120();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = T6120_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = resultSet.getInt(7);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public void updateUserFc(T6112 t6112)
        throws Throwable
    {
        if (t6112 == null)
        {
            throw new ParameterException("参数不能为空.");
        }
        
        try (Connection connection = getConnection())
        {
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6112 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ? WHERE F01 = ?"))
            {
                pstmt.setInt(1, t6112.F02);
                pstmt.setString(2, t6112.F03);
                pstmt.setFloat(3, t6112.F04);
                pstmt.setInt(4, t6112.F05);
                pstmt.setBigDecimal(5, t6112.F06);
                pstmt.setBigDecimal(6, t6112.F07);
                pstmt.setInt(7, t6112.F08);
                pstmt.setString(8, t6112.F09);
                pstmt.setString(9, t6112.F10);
                pstmt.setBigDecimal(10, t6112.F11);
                pstmt.setInt(11, t6112.F01);
                pstmt.execute();
            }
        }
        
    }
    
    @Override
    public void updateUserCc(T6113 t6113)
        throws Throwable
    {
        if (t6113 == null)
        {
            throw new ParameterException("参数不能为空.");
        }
        
        try (Connection connection = getConnection())
        {
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6113 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ? WHERE F01 = ?"))
            {
                pstmt.setInt(1, t6113.F02);
                pstmt.setString(2, t6113.F03);
                pstmt.setString(3, t6113.F04);
                pstmt.setInt(4, t6113.F05);
                pstmt.setBigDecimal(5, t6113.F06);
                pstmt.setBigDecimal(6, t6113.F07);
                pstmt.setInt(7, t6113.F01);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public T7152[] csjlSearch(int userId)
        throws Throwable
    {
        ArrayList<T7152> list = null;
        
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T7152.F08,T7152.F04,T7152.F05,T7152.F06 FROM S71.T7152 WHERE T7152.F03 = ? ORDER BY T7152.F10 DESC"))
            {
                ps.setInt(1, userId);
                
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        T7152 t7152 = new T7152();
                        
                        t7152.F08 = rs.getTimestamp(1);
                        t7152.F04 = EnumParser.parse(T7152_F04.class, rs.getString(2));
                        t7152.F05 = rs.getString(3);
                        t7152.F06 = rs.getString(4);
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(t7152);
                    }
                }
            }
        }
        
        return list == null ? null : list.toArray(new T7152[list.size()]);
    }
    
    @Override
    public T6110_F06 getUserType(int userId)
        throws Throwable
    {
        if (userId <= 0)
        {
            throw new ParameterException("参数不能为空");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F06 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return T6110_F06.parse(resultSet.getString(1));
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public T6110_F17 getUserInvestorType(int userId)
        throws Throwable
    {
        if (userId <= 0)
        {
            throw new ParameterException("参数不能为空");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F17 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return T6110_F17.parse(resultSet.getString(1));
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public String getUserNameById(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return "";
    }
    
    @Override
    public T6110 getFrontUserByName(String userName)
        throws Throwable
    {
        T6110 t6110 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F07 FROM S61.T6110 WHERE T6110.F02 = ? LIMIT 1"))
            {
                pstmt.setString(1, userName);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6110 = new T6110();
                        t6110.F01 = resultSet.getInt(1);
                        t6110.F07 = T6110_F07.parse(resultSet.getString(2));
                        return t6110;
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public T7110 getConsoleUserByName(String userName)
        throws Throwable
    {
        T7110 t7110 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F05 FROM S71.T7110 WHERE T7110.F02 = ? LIMIT 1"))
            {
                pstmt.setString(1, userName);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t7110 = new T7110();
                        t7110.F01 = resultSet.getInt(1);
                        t7110.F05 = T7110_F05.parse(resultSet.getString(2));
                        return t7110;
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public void updateUserCredit(int userId, BigDecimal creditAmount)
        throws Throwable
    {
        if (creditAmount.compareTo(new BigDecimal(0)) <= 0)
        {
            throw new LogicalException("调整额度必须大于零");
        }
        try (Connection connection = getConnection())
        {
            BigDecimal remain;
            try
            {
                serviceResource.rollback(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F03 FROM S61.T6116 WHERE T6116.F01 = ? FOR UPDATE"))
                {
                    pstmt.setInt(1, userId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            remain = resultSet.getBigDecimal(1);
                        }
                        else
                        {
                            throw new LogicalException("用户信用记录不存在");
                        }
                    }
                }
                if (creditAmount.compareTo(remain) == 0)
                {
                    serviceResource.rollback(connection);
                    return;
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6116 SET F03 = ? WHERE F01 = ?"))
                {
                    pstmt.setBigDecimal(1, creditAmount);
                    pstmt.setInt(2, userId);
                    pstmt.execute();
                }
                if (creditAmount.compareTo(remain) > 0)
                {
                    remain = creditAmount.subtract(remain);
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S61.T6117 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?, F07 = ?, F08 = ?"))
                    {
                        pstmt.setInt(1, userId);
                        pstmt.setInt(2, FeeCode.XY_CZ);
                        pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                        pstmt.setBigDecimal(4, remain);
                        pstmt.setBigDecimal(5, creditAmount);
                        pstmt.setString(6, "信用额度调整");
                        pstmt.execute();
                    }
                }
                else
                {
                    remain = remain.subtract(creditAmount);
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S61.T6117 SET F02 = ?, F03 = ?, F04 = ?,F06 = ?, F07 = ?, F08 = ?"))
                    {
                        pstmt.setInt(1, userId);
                        pstmt.setInt(2, FeeCode.XY_CZ);
                        pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                        pstmt.setBigDecimal(4, remain);
                        pstmt.setBigDecimal(5, creditAmount);
                        pstmt.setString(6, "信用额度调整");
                        pstmt.execute();
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
    
    @Override
    public void updateUserCreditLevel(int userId, String creditLevel)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6116 SET F05 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, creditLevel);
                pstmt.setInt(2, userId);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public BigDecimal getUserCredit(int userId)
        throws Throwable
    {
        if (userId <= 0)
        {
            throw new ParameterException("参数不能为空");
        }
        BigDecimal creditAmount = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6116 WHERE T6116.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        creditAmount = resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return creditAmount;
    }
    
    @Override
    public String getAccountName(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S71.T7110 WHERE F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return "";
    }
    
    @Override
    public void del(int userId)
        throws Throwable
    {
        if (userId <= 0)
        {
            throw new ParameterException("指定的用户不存在");
        }
        try (Connection connection = getConnection())
        {
            execute(connection, "UPDATE S61.T6110 SET F13 = ? WHERE F01 = ?", T6110_F13.S, userId);
        }
    }
    
    @Override
    public boolean isTg(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public T6110_F10 getDb(int userId)
        throws Throwable
    {
        if (userId <= 0)
        {
            throw new ParameterException("参数不能为空");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F10 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return T6110_F10.parse(resultSet.getString(1));
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public int getUserLoginError(String userName, String ip)
        throws Throwable
    {
        int errorTimes = 0;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S10._1037 WHERE _1037.F01 = ? AND _1037.F02 = ? "))
            {
                pstmt.setString(1, userName);
                pstmt.setString(2, ip);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        errorTimes = resultSet.getInt(1);
                    }
                }
            }
        }
        return errorTimes;
    }
    
    @Override
    public void clearErrorCount(String userName, String ip)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S10._1037 SET F03= 0 WHERE _1037.F01 = ? AND _1037.F02 = ? "))
            {
                pstmt.setString(1, userName);
                pstmt.setString(2, ip);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void resetLoginErrorNum(String userName)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String phoneStr = "";
            String emalStr = "";
            try (PreparedStatement pst = connection.prepareStatement("SELECT F04, F05 FROM S61.T6110 WHERE F02 = ?"))
            {
                pst.setString(1, userName);
                try (ResultSet rs = pst.executeQuery())
                {
                    if (rs.next())
                    {
                        phoneStr = rs.getString(1);
                        emalStr = rs.getString(2);
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S11._1037 SET F03 = 0 WHERE _1037.F01 IN (?, ?, ?) "))
            {
                pstmt.setString(1, userName);
                pstmt.setString(2, phoneStr);
                pstmt.setString(3, emalStr);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void clearSession(int accountId)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            int id = select(connection, new ItemParser<Integer>()
            {
                
                @Override
                public Integer parse(ResultSet resultSet)
                    throws SQLException
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            }, "SELECT F01 FROM S10._1030 WHERE F04 = ? ORDER BY F05 DESC LIMIT 1", accountId);
            
            execute(connection, "UPDATE S10._1030 SET F04 = NULL WHERE F04 = ? AND F01 <> ?", accountId, id);
        }
    }
    
    @Override
    public T6119 selectT6119(int userId)
        throws Throwable
    {
        T6119 t6119 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6119 = new T6119();
                        t6119.F01 = resultSet.getInt(1);
                        t6119.F02 = resultSet.getInt(2);
                        t6119.F03 = resultSet.getString(3);
                        t6119.F04 = resultSet.getString(4);
                    }
                }
            }
        }
        return t6119;
    }
    
}
