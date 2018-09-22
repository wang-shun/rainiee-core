package com.dimeng.p2p.account.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F12;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S62.entities.T6280;
import com.dimeng.p2p.S62.enums.T6280_F10;
import com.dimeng.p2p.account.front.service.UserManage;
import com.dimeng.p2p.variables.defines.BusinessVariavle;
import com.dimeng.util.StringHelper;

public class UserManageImpl extends AbstractAccountService implements UserManage
{
    
    public UserManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class UserManageFactory implements ServiceFactory<UserManage>
    {
        @Override
        public UserManage newInstance(ServiceResource serviceResource)
        {
            return new UserManageImpl(serviceResource);
        }
    }
    
    @Override
    public String getAccountName()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
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
    public boolean isAccountExists(String accountName)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F02 = ? LIMIT 1"))
            {
                pstmt.setString(1, accountName);
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
    public int readAccountId(String condition, String password)
        throws AuthenticationException, SQLException
    {
        int accountId = 0;
        String sql = "SELECT F01,F03,F07 FROM S61.T6110 WHERE F02=? OR F04=? OR F05=?";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, condition);
                ps.setString(2, condition);
                ps.setString(3, condition);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        accountId = rs.getInt(1);
                        String pwd = rs.getString(2);
                        String status = rs.getString(3);
                        if ("SD".equals(status))
                        {
                            throw new AuthenticationException("该账号被锁定，禁止登录。");
                        }
                        /*if ("HMD".equals(status))
                        {
                            throw new AuthenticationException("该账号被拉黑，禁止登录。");
                        }*/
                        if (!UnixCrypt.crypt(password, DigestUtils.sha256Hex(password)).equals(pwd))
                        {
                            throw new AuthenticationException("用户名或密码错误.");
                        }
                    }
                    else
                    {
                        throw new AuthenticationException("用户名或密码错误.");
                    }
                }
            }
        }
        return accountId;
    }
    
    @Override
    public int getUnReadLetter()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(F01) FROM S61.T6123 WHERE T6123.F02 = ? AND T6123.F05 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6123_F05.WD.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public void log(int accountId, String ip)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("INSERT INTO S61.T6190 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?,F06=?"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                ps.setTimestamp(2, getCurrentTimestamp(connection));
                ps.setString(3, "登录日志");
                ps.setString(4, "登录前台系统 ");
                ps.setString(5, ip);
                ps.executeUpdate();
            }
        }
        
    }
    
    @Override
    public String getUsrCustId()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
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
    public T6119 getUsrAute()
        throws Throwable
    {
        T6119 t6119 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT * FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
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
            return t6119;
        }
    }
    
    @Override
    public boolean isFirstLogin()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F06 <> ? AND T6110.F08 = ? AND T6110.F12 = ? AND F01 = ? LIMIT 1"))
            {
                pstmt.setString(1, T6110_F06.ZRR.name());
                pstmt.setString(2, T6110_F08.HTTJ.name());
                pstmt.setString(3, T6110_F12.S.name());
                pstmt.setInt(4, serviceResource.getSession().getAccountId());
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
    public void updatePassword(String password)
        throws Throwable
    {
        int userID = serviceResource.getSession().getAccountId();
        String encodePassword = UnixCrypt.crypt(password, DigestUtils.sha256Hex(password));
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03,F02 FROM S61.T6110 WHERE F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userID);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (encodePassword.equals(resultSet.getString(1)))
                        {
                            throw new LogicalException("修改后密码不能与原密码相同!");
                        }
                        if (password.equals(resultSet.getString(2)))
                        {
                            throw new ParameterException("密码不能与用户名一致");
                        }
                    }
                }
            }
            
            execute(connection,
                "UPDATE S61.T6110 SET F03 = ?, F12 = ? WHERE F01 = ?",
                encodePassword,
                T6110_F12.F.name(),
                userID);
        }
    }
    
    @Override
    public String getUserHeadPhoto()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F05 FROM S61.T6141 WHERE T6141.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
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
    public int checkCodeExist(String code)
        throws Throwable
    {
        int result = -1;
        final ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
        try (Connection connection = getConnection())
        {
            if (!StringHelper.isEmpty(code))
            {
                String selectT6111 =
                    "SELECT T6110.F06 FROM S61.T6111 INNER JOIN S61.T6110 ON T6111.F01 = T6110.F01 WHERE T6111.F02 = ? OR T6110.F04= ? LIMIT 1";
                try (PreparedStatement pstmt = connection.prepareStatement(selectT6111))
                {
                    pstmt.setString(1, code);
                    pstmt.setString(2, code);
                    try (ResultSet rs = pstmt.executeQuery())
                    {
                        if (rs.next())
                        {
                            String userType = rs.getString(1);
                            if (T6110_F06.ZRR.name().equals(userType))
                            {
                                result = 0;
                            }
                            else
                            {
                                result = -2;
                            }
                        }
                    }
                }
                if (result != 0 && is_business)
                {
                    String selectT7110 =
                        "SELECT T7110.F12 FROM S71.T7110 WHERE T7110.F12 = ? AND T7110.F05 = 'QY' LIMIT 1";
                    try (PreparedStatement pstmt = connection.prepareStatement(selectT7110))
                    {
                        pstmt.setString(1, code);
                        try (ResultSet rs = pstmt.executeQuery())
                        {
                            if (rs.next())
                            {
                                result = 0;
                            }
                        }
                    }
                }
            }
            
        }
        
        return result;
        
    }
    
    @Override
    public String selectT6280(int F01)
        throws Throwable
    {
        
        T6280 t6280 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6280 WHERE T6280.F01 = ?"))
            {
                ps.setInt(1, F01);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6280 = new T6280();
                        t6280.F01 = resultSet.getInt(1);
                        t6280.F02 = resultSet.getBigDecimal(2);
                        t6280.F03 = resultSet.getBigDecimal(3);
                        t6280.F04 = resultSet.getBigDecimal(4);
                        t6280.F05 = resultSet.getInt(5);
                        t6280.F06 = resultSet.getInt(6);
                        t6280.F07 = resultSet.getInt(7);
                        t6280.F08 = resultSet.getInt(8);
                        t6280.F09 = resultSet.getBigDecimal(9);
                        t6280.F10 = T6280_F10.parse(resultSet.getString(10));
                        t6280.F11 = resultSet.getTimestamp(11);
                    }
                }
            }
        }
        if (t6280 != null && t6280.F07 > 0)
            return "Y";
        else
            return "N";
    }
    
    @Override
    public int checkNumExist(String code)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            if (!StringHelper.isEmpty(code))
            {
                String selectT6111 = "SELECT T7110.F12 FROM S71.T7110 WHERE T7110.F12 = ? AND T7110.F05 = 'QY' LIMIT 1";
                try (PreparedStatement pstmt = connection.prepareStatement(selectT6111))
                {
                    pstmt.setString(1, code);
                    try (ResultSet rs = pstmt.executeQuery())
                    {
                        if (rs.next())
                        {
                            return 0;
                        }
                    }
                }
            }
            return -1;
        }
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
    public boolean isEnterpriseNameExists(String enterpriseName)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F04 = ? LIMIT 1"))
            {
                pstmt.setString(1, enterpriseName);
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
    
}
