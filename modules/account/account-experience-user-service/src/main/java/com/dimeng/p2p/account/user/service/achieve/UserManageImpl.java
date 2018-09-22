package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F12;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.account.user.service.UserManage;
import com.dimeng.p2p.account.user.service.entity.UserLog;

public class UserManageImpl extends AbstractAccountService implements
		UserManage {

	public UserManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class UserManageFactory implements ServiceFactory<UserManage> {
		@Override
		public UserManage newInstance(ServiceResource serviceResource) {
			return new UserManageImpl(serviceResource);
		}
	}

	@Override
	public String getAccountName() throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F02 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, serviceResource.getSession().getAccountId());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
		}
		return null;

	}

	@Override
	public int readAccountId(String condition, String password)
			throws AuthenticationException, SQLException {
		int accountId = 0;
		String sql = "SELECT F01,F03,F07 FROM S61.T6110 WHERE F02=? OR F04=? OR F05=?";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setString(1, condition);
				ps.setString(2, condition);
				ps.setString(3, condition);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						accountId = rs.getInt(1);
						String pwd = rs.getString(2);
						String status = rs.getString(3);
						if ("SD".equals(status)) {
                            throw new AuthenticationException("该账号被锁定，禁止登录。");
						}
//						if ("HMD".equals(status)) {
                        //							throw new AuthenticationException("该账号被拉黑, 禁止登录。");
//						}
                        //第三方登录修改密码校验
						if (!password.equals(pwd)) {
                            throw new AuthenticationException("用户名或密码错误.");
						}
					} else {
                        throw new AuthenticationException("用户名或密码错误.");
					}
				}
			}
		}
		return accountId;
	}

	@Override
	public void log(int accountId, String ip) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("INSERT INTO S61.T6190 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?,F06=?")) {
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
	public String getUsrCustId() throws Throwable {
		try(Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F03 FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, serviceResource.getSession().getAccountId());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
		}
		return null;
	}
    
    @Override
    public UserLog getLastLoginTime(int accountId)
        throws Throwable
    {
        UserLog userLog = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6190.F01,T6190.F03,T6190.F04,T6190.F05,T6190.F06,T6110.F02 AS NAME FROM S61.T6190 INNER JOIN S61.T6110 ON T6190.F02=T6110.F01 WHERE 1=1 AND T6190.F02=? AND T6190.F04=? AND T6190.F05=? ORDER BY T6190.F03 DESC LIMIT 1,1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, "登录日志");
                pstmt.setString(3, "登录前台系统 ");
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        userLog = new UserLog();
                        userLog.F01 = resultSet.getInt(1);
                        userLog.F03 = resultSet.getTimestamp(2);
                        userLog.F04 = resultSet.getString(3);
                        userLog.F05 = resultSet.getString(4);
                        userLog.F06 = resultSet.getString(5);
                        userLog.accountName = resultSet.getString(6);
                    }
                }
            }
        }
        return userLog;
    }

	@Override
	public boolean isFirstLogin() throws Throwable {
		try(Connection connection = getConnection()){
			 try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F06 <> ? AND T6110.F08 = ? AND T6110.F12 = ? AND F01 = ? LIMIT 1")) {
			        pstmt.setString(1, T6110_F06.ZRR.name());
			        pstmt.setString(2, T6110_F08.HTTJ.name());
			        pstmt.setString(3, T6110_F12.S.name());
			        pstmt.setInt(4, serviceResource.getSession().getAccountId());
			        try(ResultSet resultSet = pstmt.executeQuery()) {
			            if(resultSet.next()) {
			                return true;
			            }
			        }
			    }
		}
		return false;
	}

	@Override
	public String selectT6119(int F01) throws Throwable {
		
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F04 FROM S61.T6119 WHERE T6119.F01 = ?")) {
				ps.setInt(1, F01);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
                        if (StringUtils.isEmpty(resultSet.getString(1)))
                        {
                            return "N";
                        }
                        else
                        {
                            return "Y";
						}
					}else{
					    return "N";
					}
				}
			}
		}
	}
	
	@Override
	public String getPhoneNum(int accountId) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F04 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, accountId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
		}
		return null;
	}

	@Override
	public int getUnReadLetter() throws Throwable {
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
}
