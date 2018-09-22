/*
 * 文 件 名:  AbstractAppServiceImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年3月30日
 */
package com.dimeng.p2p.app.service;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.http.session.VerifyCode;
import com.dimeng.framework.http.session.VerifyCodeGenerator;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.service.AbstractP2PService;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.LongParser;

/**
 * 与验证码有关操作接口的实现类
 * 
 * @author  suwei
 * @version  [版本号, 2016年3月30日]
 */
public class AbstractVerifyCodeServiceImpl extends AbstractP2PService implements AbstractVerifyCodeService
{
    // 验证码的长度
    private static final int VERRIFY_CODE_LENGTH = 4;
    
    // 验证码保存的时间
    private static final int VERIFY_CODE_TIME_OUT = 1800000;
    
    private long id = 0L;
    
    /** <默认构造函数>
     */
    public AbstractVerifyCodeServiceImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    protected SQLConnectionProvider getConnectionProvider()
        throws ResourceNotFoundException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER);
    }
    
    // 获取数据库连接，连接的是"S11"库
    protected Connection getConnection()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection(P2PConst.DB_USER_SESSION);
    }
    
    protected Connection getConnection(String db)
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection(db);
    }
    
    @Override
    public String getVerifyCode(String type, VerifyCodeGenerator generator, String cookie)
        throws Throwable
    {
        if (StringHelper.isEmpty(type))
        {
            return null;
        }
        String verifyCode = null;
        try (Connection connection = getConnection())
        {
            if (!type.startsWith("p"))
            {
                try (PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT F05 FROM _1031 WHERE F02 = ?");)
                {
                    preparedStatement.setString(1, type);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next())
                    {
                        Timestamp expires = resultSet.getTimestamp(1);
                        
                        // 验证码还未过期时并且验证码生成时间小于30分钟，不允许重复获取验证码
                        if ((System.currentTimeMillis() >= (expires.getTime() - 1800000))
                            && (System.currentTimeMillis() <= expires.getTime()))
                        {
                            return "repeat";
                        }
                    }
                }
            }
            
            if (verifyCode == null)
            {
                if (generator == null)
                {
                    generator = DEFAULT_VERIFY_CODE_GENERATOR;
                }
                VerifyCode newCode = generator.newVerifyCode();
                
                try (PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO _1031 (F01,F02,F03,F04,F05) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE F03 = VALUES(F03), F04 = VALUES(F04), F05 = VALUES(F05)"))
                {
                    preparedStatement.setLong(1, getId(cookie));
                    preparedStatement.setString(2, type);
                    preparedStatement.setString(3, newCode.getDisplayValue());
                    preparedStatement.setString(4, newCode.getMatchValue());
                    preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis() + generator.getTTL()));
                    
                    preparedStatement.executeUpdate();
                }
                
                verifyCode = newCode.getDisplayValue();
            }
        }
        return verifyCode;
    }
    
    // 初始化验证码生成器，使用默认的生成器
    protected final VerifyCodeGenerator DEFAULT_VERIFY_CODE_GENERATOR = new VerifyCodeGenerator()
    {
        private final SecureRandom random = new SecureRandom();
        
        public VerifyCode newVerifyCode()
        {
            int length = VERRIFY_CODE_LENGTH;
            char[] value = new char[length];
            
            for (int i = 0; i < length; i++)
            {
                value[i] = ((char)(this.random.nextInt(10) + 48));
            }
            
            final String displayValue = new String(value);
            
            return new VerifyCode()
            {
                public String getMatchValue()
                {
                    return displayValue;
                }
                
                public String getDisplayValue()
                {
                    return displayValue;
                }
                
                public String toString()
                {
                    return displayValue;
                }
            };
        }
        
        public long getTTL()
        {
            long ttl = LongParser.parse(VERIFY_CODE_TIME_OUT);
            
            if (0L == ttl)
            {
                return 180000L;
            }
            return ttl;
        }
    };
    
    protected Long getId(String cookie)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM _1030 WHERE F02 = ?"))
            {
                pstmt.setString(1, cookie);
                
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        this.id = resultSet.getLong(1);
                    }
                }
            }
        }
        return this.id;
    }
    
    @Override
    public void authenticateVerifyCode(VerifyCodeAuthentication authentication, String cookie)
        throws Throwable
    {
        if (authentication == null)
        {
            return;
        }
        String verifyCode = authentication.getVerifyCode();
        if ((verifyCode == null) || (verifyCode.isEmpty()))
        {
            throw new AuthenticationException("必须提供校验码.");
        }
        String type = authentication.getVerifyCodeType();
        if (StringHelper.isEmpty(type))
        {
            throw new AuthenticationException("未指定验证码类型.");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT F04, F05 FROM _1031 WHERE F01 = ? AND F02 = ?"))
            {
                preparedStatement.setLong(1, getId(cookie));
                preparedStatement.setString(2, type);
                
                try (ResultSet resultSet = preparedStatement.executeQuery())
                {
                    if (resultSet.next())
                    {
                        String code = resultSet.getString(1);
                        Timestamp expires = resultSet.getTimestamp(2);
                        if (expires.getTime() <= System.currentTimeMillis())
                        {
                            throw new AuthenticationException("无效的验证码或验证码已过期.");
                        }
                        if (!verifyCode.equalsIgnoreCase(code))
                        {
                            throw new AuthenticationException("无效的验证码或验证码已过期.");
                        }
                    }
                    else
                    {
                        throw new AuthenticationException("无效的验证码或验证码已过期.");
                    }
                }
            }
        }
    }
    
    public String getCookie(Cookie[] cookies)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM _1030"))
            {
                List<String> results = new ArrayList<String>();
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        results.add(resultSet.getString(1));
                    }
                    List<String> cookieList = new ArrayList<String>();
                    for (Cookie cookie : cookies)
                    {
                        cookieList.add(cookie.getValue());
                    }
                    if (cookieList.retainAll(results))
                    {
                        return cookieList.get(0);
                    }
                }
            }
        }
        return null;
    }
}
