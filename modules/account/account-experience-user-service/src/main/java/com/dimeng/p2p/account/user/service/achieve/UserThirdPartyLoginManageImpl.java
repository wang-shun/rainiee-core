package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6171_F03;
import com.dimeng.p2p.account.user.service.UserThirdPartyLoginManage;
import com.dimeng.p2p.account.user.service.entity.ThirdUser;
import com.dimeng.util.StringHelper;

public class UserThirdPartyLoginManageImpl extends AbstractAccountService implements UserThirdPartyLoginManage
{
    
    public UserThirdPartyLoginManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public ThirdUser getUserInfoByThirdID(String openID)
        throws Throwable
    {
        if (StringHelper.isEmpty(openID))
        {
            throw new ParameterException("参数不能为空");
        }
        ThirdUser user = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT t1.F02 AS F01, t1.F03 AS F02, t1.F01 AS F03, t2.F05 AS F04, t2.F07 AS F05, t2.F08 AS F06, t1.F07 AS F07 FROM S61.T6110 t1, S61.T6171 t2 WHERE t1.F01 = t2.F01 AND t2.F04 = ? AND t2.F03 = ? LIMIT 1"))
            {
                pstmt.setString(1, openID);
                pstmt.setString(2, T6171_F03.Y.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        user = new ThirdUser();
                        user.F02 = resultSet.getString(1);
                        user.F10 = resultSet.getString(2);
                        user.F01 = resultSet.getInt(3);
                        user.loginDate = resultSet.getTimestamp(4);
                        user.token = resultSet.getString(5);
                        user.tokenTime = resultSet.getLong(6);
                        user.F06 = T6110_F07.parse(resultSet.getString(7));
                    }
                }
            }
        }
        return user;
    }
    
    @Override
    public void updateUserLoginTime(String openID)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6171 t SET t.F05 =  CURRENT_TIMESTAMP()  WHERE t.F04 = ? AND t.F06 = 'Y'"))
            {
                pstmt.setString(1, openID);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void updateUserAccessTokenAndLoginTime(String openID, String token)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6171 t SET t.F07 = ?, t.F08 =  ? ,t.F05 =  CURRENT_TIMESTAMP() WHERE t.F04 = ? AND t.F06 = 'Y'"))
            {
                
                pstmt.setString(1, token);
                pstmt.setLong(2, System.currentTimeMillis());
                pstmt.setString(3, openID);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public ThirdUser getUserInfoByUsrID(String userId, String passWord)
        throws Throwable
    {
        if (StringHelper.isEmpty(userId))
        {
            throw new ParameterException("参数不能为空");
        }
        ThirdUser user = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT t1.F02,t1.F03,t1.F01 FROM S61.T6110 t1 WHERE t1.F07 = ? AND t1.F02 = ? AND t1.F03 = ?"))
            {
                pstmt.setString(1, T6110_F07.QY.name());
                pstmt.setString(2, userId);
                pstmt.setString(3, passWord);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        user = new ThirdUser();
                        user.F02 = resultSet.getString(1);
                        user.F10 = resultSet.getString(2);
                        user.F01 = resultSet.getInt(3);
                    }
                }
            }
        }
        return user;
    }
    
    @Override
    public ThirdUser getUserInfoByID(int openID)
        throws Throwable
    {
        ThirdUser user = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT t2.F04 FROM S61.T6171 t2 WHERE  t2.F01 = ?"))
            {
                pstmt.setInt(1, openID);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        user = new ThirdUser();
                        user.openId = resultSet.getString(1);
                    }
                }
            }
        }
        return user;
    }
    
    @Override
    public void updateUserLoginTime(String openID, String type)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("UPDATE S61.T6171 t SET t.F05 = ?  ");
        sql.append("WHERE t.F04 = ?  ");
        if ("wx".equals(type))
        {
            sql.append("AND t.F09 = 'Y' ");
        }
        else
        {
            sql.append("AND t.F06 = 'Y' ");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
            {
                pstmt.setTimestamp(1, getCurrentTimestamp(connection));
                pstmt.setString(2, openID);
                pstmt.execute();
            }
        }
    }
    
}
