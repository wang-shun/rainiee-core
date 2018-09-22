/*
 * 文 件 名:  EmailVerifyCodeManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月10日
 */
package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Random;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.account.user.service.EmailVerifyCodeManage;
import com.dimeng.p2p.variables.defines.EmailVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.LongParser;

/**
 * 邮箱验证链接中的验证码
 * @author xiaoqi
 * @version [版本号, 2015年11月10日]
 */
public class EmailVerifyCodeManageImpl extends AbstractAccountService implements
		EmailVerifyCodeManage {

	public EmailVerifyCodeManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class EmailVerifyCodeManageFactory implements
			ServiceFactory<EmailVerifyCodeManage> {
		@Override
		public EmailVerifyCodeManage newInstance(ServiceResource serviceResource) {
			return new EmailVerifyCodeManageImpl(serviceResource);
		}
	}

	@Override
	public String getVerifyCode(int uid, String type) throws Throwable {
		if (StringHelper.isEmpty(type)) {
			return null;
		}
		String verifyCode = null;
		try (Connection connection = getConnection()) {
			if (!(type.startsWith("p"))) {
				try (PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT F03, F05 FROM S10._1055 WHERE F01 = ? AND F02 = ?")) {
					preparedStatement.setLong(1, uid);
					preparedStatement.setString(2, type);
					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						if (resultSet.next()) {
							String code = resultSet.getString(1);
							Timestamp expires = resultSet.getTimestamp(2);
							if (expires.getTime() > System.currentTimeMillis()) {
								verifyCode = code;
							}
						}
					}
				}
			}
			if (verifyCode == null) {
				try (PreparedStatement preparedStatement = connection
						.prepareStatement("INSERT INTO S10._1055 (F01,F02,F03,F04,F05) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE F03 = VALUES(F03), F04 = VALUES(F04), F05 = VALUES(F05)")) {
					verifyCode = this.getRandomCode(6);
					long ttl = LongParser.parse(EmailVariavle.EMAIL_LINK_VALID_PERIOD.getValue())*3600000;
					preparedStatement.setLong(1, uid);
					preparedStatement.setString(2, type);
					preparedStatement.setString(3, verifyCode);
					preparedStatement.setString(4, verifyCode);
					preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()+ttl));
					preparedStatement.executeUpdate();
				}
			}
		}
		return verifyCode;
	}

	private synchronized String getRandomCode(int length) {
		StringBuilder sb=new StringBuilder();
		Random r = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(r.nextInt(10));
		}
		return sb.toString();
	}

	/** {@inheritDoc} */

	@Override
	public void authenticateVerifyCode(int uid, String type, String verifyCode) throws Throwable {
		if (uid == 0) {
			return;
		}
		if (StringHelper.isEmpty(verifyCode)) {
			throw new Exception("必须提供校验码.");
		}
		if (StringHelper.isEmpty(type)) {
			throw new Exception("未指定验证码类型.");
		}
		try (Connection connection = getConnection()) {
			try (PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT F04, F05 FROM S10._1055 WHERE F01 = ? AND F02 = ?")) {
				preparedStatement.setLong(1, uid);
				preparedStatement.setString(2, type);
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						String code = resultSet.getString(1);
						Timestamp expires = resultSet.getTimestamp(2);
						if (expires.getTime() <= System.currentTimeMillis()) {
							throw new Exception("无效的验证码或验证码已过期.");
						}
						if (verifyCode.equalsIgnoreCase(code)) {
							try (PreparedStatement pstmt = connection
									.prepareStatement("DELETE FROM S10._1055 WHERE F01 = ? AND F02 = ?")) {
								pstmt.setLong(1, uid);
								pstmt.setString(2, type);
								pstmt.executeUpdate();
							}
						} else {
							throw new Exception("无效的验证码或验证码已过期.");
						}
					} else {
						throw new Exception("无效的验证码或验证码已过期.");
					}
				}
			}
		}
	}

	/** {@inheritDoc} */

	@Override
	public void invalidVerifyCode(int uid, String type) throws Throwable {
		if (StringHelper.isEmpty(type)) {
			return;
		}
		if (uid == 0) {
			return;
		}
		try (Connection connection = getConnection()) {
			try (PreparedStatement preparedStatement = connection
					.prepareStatement("DELETE FROM S11._1031 WHERE F01 = ? AND F02 = ?")) {
				preparedStatement.setLong(1, uid);
				preparedStatement.setString(2, type);
				preparedStatement.executeUpdate();
			}
		}
	}

}
