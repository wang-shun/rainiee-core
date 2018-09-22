package com.dimeng.p2p.modules.capital.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.enums.RealNameVerfiyStatus;
import com.dimeng.p2p.modules.capital.user.service.ChargeManage;
import com.dimeng.p2p.modules.capital.user.service.entity.Auth;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;

public class ChargeManageImpl extends AbstractCapitalService implements
		ChargeManage {

	public ChargeManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public BigDecimal balance() throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F05 FROM T6023 WHERE F01=?")) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getBigDecimal(1);
					}
				}
			}
		}
		return new BigDecimal(0);
	}

	@Override
	public int addOrder(BigDecimal amount, String payType) throws Throwable {
		if (serviceResource.getSession() == null
				|| !serviceResource.getSession().isAuthenticated()) {
			throw new ParameterException("鉴权失败");
		}
		ConfigureProvider configureProvider = serviceResource
				.getResource(ConfigureProvider.class);
		String mPhone = configureProvider
				.getProperty(PayVariavle.CHARGE_MUST_PHONE);
		String mRealName = configureProvider
				.getProperty(PayVariavle.CHARGE_MUST_NCIIC);
		String mWithPsd = configureProvider
				.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
		Auth auth = getAutnInfo();
		if ("true".equals(mPhone) && !auth.phone) {
			throw new LogicalException("手机号未认证");
		}
		if ("true".equals(mRealName) && !auth.realName) {
			throw new LogicalException("未实名认证");
		}
		if ("true".equals(mWithPsd) && !auth.withdrawPsw) {
			throw new LogicalException("交易密码未设置");
		}
		if (amount.compareTo(new BigDecimal(0)) <= 0
				|| StringHelper.isEmpty(payType)) {
			throw new ParameterException("金额或支付类型错误");
		}

		String min = configureProvider
				.getProperty(PayVariavle.CHARGE_MIN_AMOUNT);
		String max = configureProvider
				.getProperty(PayVariavle.CHARGE_MAX_AMOUNT);
		if (amount.compareTo(new BigDecimal(min)) < 0
				|| amount.compareTo(new BigDecimal(max)) > 0) {
			StringBuilder builder = new StringBuilder("充值金额必须大于");
			builder.append(min);
			builder.append("元小于");
			builder.append(max);
			builder.append("元");
			throw new LogicalException(builder.toString());
		}
		String rate = configureProvider.getProperty(PayVariavle.CHARGE_RATE);
		if (StringHelper.isEmpty(rate)) {
			return 0;
		}
		BigDecimal pondage = amount.multiply(new BigDecimal(rate));
		BigDecimal maxPondage = BigDecimalParser.parse(configureProvider
				.getProperty(PayVariavle.CHARGE_MAX_POUNDAGE));
		pondage = pondage.compareTo(maxPondage) >= 0 ? maxPondage : pondage;
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement(
							"INSERT INTO S60.T6033(F02,F03,F04,F06,F09) VALUES(?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS)) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				ps.setBigDecimal(3, amount);
				ps.setString(4, payType);
				ps.setBigDecimal(5, pondage);
				ps.execute();
				try (ResultSet resultSet = ps.getGeneratedKeys()) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
				}
			}
		}
		return 0;
	}

	@Override
	public Auth getAutnInfo() throws Throwable {
		Auth auth = new Auth();
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM T6023 WHERE F01=?")) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						if (!StringHelper.isEmpty(resultSet.getString(1))) {
							auth.withdrawPsw = true;
						}
					}
				}
			}
			String p1 = null;
			int accountId = serviceResource.getSession().getAccountId();
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02,F08 FROM T6011 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						p1 = resultSet.getString(1);
						if (RealNameVerfiyStatus.YYZ.toString().equals(
								resultSet.getString(2))) {
							auth.realName = true;
						}
					}
				}
			}
			String p2 = null;
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F04 FROM T6010 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						p2 = resultSet.getString(1);
					}
				}
			}
			if (!StringHelper.isEmpty(p1) || !StringHelper.isEmpty(p2)) {
				auth.phone = true;
			}
		}
		return auth;
	}

	public static class ChargeManageFactory implements
			ServiceFactory<ChargeManage> {

		@Override
		public ChargeManage newInstance(ServiceResource serviceResource) {
			return new ChargeManageImpl(serviceResource);
		}

	}

}
