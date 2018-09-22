package com.dimeng.p2p.account.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.enums.T6118_F10;
import com.dimeng.p2p.account.front.service.PasswordManage;
import com.dimeng.util.StringHelper;

public class PasswordManageImpl extends AbstractAccountService implements PasswordManage
{
    
    public PasswordManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class PasswordManageFactory implements ServiceFactory<PasswordManage>
    {
        @Override
        public PasswordManage newInstance(ServiceResource serviceResource)
        {
            return new PasswordManageImpl(serviceResource);
        }
    }
    
    @Override
    public int emailExist(String email, String accountType)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F05 = ? AND T6110.F06 = ? LIMIT 1"))
            {
                pstmt.setString(1, email);
                pstmt.setString(2, accountType);
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
    public int phoneExist(String phone, String accountType)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F04 = ? AND T6110.F06 = ? LIMIT 1"))
            {
                pstmt.setString(1, phone);
                pstmt.setString(2, accountType);
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
    public boolean isSetPwdQues(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F10 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return T6118_F10.parse(resultSet.getString(1)) == T6118_F10.TG ? true : false;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public int accountNameExist(String accountName)
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
                        return resultSet.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public String getJyPassword(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F08 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
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
        return null;
    }
    
    @Override
    public void updatePassword(String password, int userId)
        throws Throwable
    {
        if (StringHelper.isEmpty(password))
        {
            throw new ParameterException("密码为空");
        }
        else if (userId <= 0)
        {
            throw new ParameterException("用户id不小于等于0");
        }
        try (Connection connection = getConnection())
        {
            /*String jyPsd = getJyPassword(connection, userId);
            password = UnixCrypt.crypt(password,DigestUtils.sha256Hex(password));
            if(password.equals(jyPsd)){
            	throw new ParameterException("登录密码不能与交易密码相同");
            }*/
            try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S61.T6110 WHERE F01=?"))
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        String userName = rs.getString(1);
                        if (userName.equals(password))
                        {
                            throw new ParameterException("密码不能与用户名一致！");
                        }
                    }
                }
            }
            try (PreparedStatement ps = connection.prepareStatement("UPDATE S61.T6110 SET F03=? WHERE F01=?"))
            {
                ps.setString(1, UnixCrypt.crypt(password, DigestUtils.sha256Hex(password)));
                ps.setInt(2, userId);
                ps.executeUpdate();
            }
        }
    }
    
    @Override
    public int questionNum(int userId)
        throws Throwable
    {
        if (userId <= 0)
        {
            throw new ParameterException("用户id不小于等于0");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(F01) FROM S61.T6194 WHERE F02=?"))
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery())
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
    
    /*@Override
    protected void writeLog(String arg0, String arg1)
        throws Throwable
    {
        // TODO Auto-generated method stub
        
    }*/
    
    /**
     * 根据手机号码更新用户密码
     * 
     * @param password 用户新密码
     * @param phone 用户手机号码
     * @throws Throwable 异常信息
     */
    @Override
    public void updatePasswordByPhone(String password, String phone)
        throws Throwable
    {
        if (StringHelper.isEmpty(password))
        {
            throw new ParameterException("密码为空");
        }
        else if (StringHelper.isEmpty(phone))
        {
            throw new ParameterException("手机号码不能为空");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE S61.T6110 SET F03=? WHERE F04 =?"))
            {
                ps.setString(1, UnixCrypt.crypt(password, DigestUtils.sha256Hex(password)));
                ps.setString(2, phone);
                ps.executeUpdate();
            }
        }
        
    }
    
    @Override
    public int psdInputCount()
        throws Throwable
    {
        try (Connection connection = getConnection())
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
    }
    
    @Override
    public void addInputCount()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection, "UPDATE S61.T6110 SET F11 = F11+1 WHERE F01 = ?", serviceResource.getSession()
                .getAccountId());
        }
    }
    
}
