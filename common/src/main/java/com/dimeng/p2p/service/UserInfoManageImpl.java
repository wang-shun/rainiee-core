/*
 * 文 件 名:  UserInfoManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月29日
 */
package com.dimeng.p2p.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6118;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F18;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.S61.enums.T6118_F05;
import com.dimeng.p2p.common.entities.Safety;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

/**
 * 用户信息管理实现类
 * 
 * @author huqinfu
 * @version [版本号, 2016年6月29日]
 */
public class UserInfoManageImpl extends AbstractP2PService implements
		UserInfoManage {

	/**
	 * <默认构造函数>
	 */
	public UserInfoManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public T6110 getUserInfo(int userId) throws Throwable {
		try (Connection connection = getConnection()) {
			T6110 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F15, F18, F19 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, userId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6110();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						record.F03 = resultSet.getString(3);
						record.F04 = resultSet.getString(4);
						record.F05 = resultSet.getString(5);
						record.F06 = T6110_F06.parse(resultSet.getString(6));
						record.F07 = T6110_F07.parse(resultSet.getString(7));
						record.F08 = T6110_F08.parse(resultSet.getString(8));
						record.F09 = resultSet.getTimestamp(9);
						record.F10 = T6110_F10.parse(resultSet.getString(10));
						record.F15 = resultSet.getTimestamp(11);
						record.F18 = T6110_F18.parse(resultSet.getString(12));
						record.F19 = T6110_F19.parse(resultSet.getString(13));
					}
				}
			}
			return record;
		}
	}

	@Override
	public String getUsrCustId() throws Throwable {
		return getUsrCustId(serviceResource.getSession().getAccountId());
	}

	private String getUsrCustId(int userId) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F03 FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, userId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
		}
		return null;
	}

	private String getUsrCustIdForSina(int userId) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F03 FROM S61.T6119 WHERE T6119.F01 = ? AND F04 ='Y'  LIMIT 1")) {
				pstmt.setInt(1, userId);
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
	public T6118 getVerifyEntity() throws Throwable {
		return getVerifyEntity(serviceResource.getSession().getAccountId());
	}

	private T6118 getVerifyEntity(int userId) throws Throwable {
		T6118 t6118 = new T6118();
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F02, F03, F04, F05 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, userId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						t6118.F02 = T6118_F02.parse(resultSet.getString(1));
						t6118.F03 = T6118_F03.parse(resultSet.getString(2));
						t6118.F04 = T6118_F04.parse(resultSet.getString(3));
						t6118.F05 = T6118_F05.parse(resultSet.getString(4));
					}
				}
			}
		}
		return t6118;
	}

	@Override
	public T6101 searchFxbyj() throws Throwable {
		try (Connection connection = getConnection()) {
			T6101 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? LIMIT 1")) {
				pstmt.setInt(1, serviceResource.getSession().getAccountId());
				pstmt.setString(2, T6101_F03.FXBZJZH.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6101();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = T6101_F03.parse(resultSet.getString(3));
						record.F04 = resultSet.getString(4);
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getBigDecimal(6);
					}
				}
			}
			return record;
		}
	}

	@Override
	public Safety get() throws Throwable {
		int acount = serviceResource.getSession().getAccountId();
		try (final Connection connection = getConnection()) {
			Safety safety = select(
					connection,
					new ItemParser<Safety>() {
						@Override
						public Safety parse(ResultSet rs) throws SQLException {
							Safety s = new Safety();
							if (rs.next()) {
								s.username = rs.getString(1);
								s.password = rs.getString(2);
								s.phoneNumber = rs.getString(3);
								s.emil = rs.getString(4);
								s.name = rs.getString(5);
								s.idCard = rs.getString(6);
								s.isIdCard = isSmrz(connection);
								s.sfzh = rs.getString(7);
								s.qyName = rs.getString(8);
								s.userType = rs.getString(9);
								try {
									s.birthday = getBirthday(StringHelper
											.decode(s.sfzh));
								} catch (Throwable throwable) {
									logger.error(throwable, throwable);
								}
								s.sex = getSex(s.sfzh);
							}
							return s;
						}
					},
					"SELECT T6110.F02 AS F01, T6110.F03 AS F02, T6110.F04 AS F03, T6110.F05 AS F04, T6141.F02 AS F05, T6141.F06 AS F06,T6141.F07 AS F07,T6161.F04 AS F08 ,T6110.F06 AS F09 FROM S61.T6110 LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01 LEFT JOIN S61.T6161 ON T6110.F01 = T6161.F01 WHERE T6110.F01 = ? ",
					acount);

			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F04, F08, F03,F10 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, acount);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						safety.isEmil = resultSet.getString(1);
						safety.txpassword = resultSet.getString(2);
						safety.isPhone = resultSet.getString(3);
						safety.isPwdSafety = resultSet.getString(4);
					}
				}
			}
			return safety;
		}
	}

	/**
	 * 是否实名通过
	 * 
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	private String isSmrz(Connection connection) throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F02 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1")) {
			pstmt.setInt(1, serviceResource.getSession().getAccountId());
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					if (T6118_F02.parse(resultSet.getString(1)) == T6118_F02.TG) {
						return "TG";
					}
				}
			}
		}
		return null;
	}

	/**
	 * 根据身份证得到出生年月
	 * 
	 * @param cardID
	 * @return
	 */
	private static String getBirthday(String cardID) {
		StringBuffer tempStr = new StringBuffer("");
		if (cardID != null && cardID.trim().length() > 0) {
			if (cardID.trim().length() == 15) {
				tempStr.append(cardID.substring(6, 12));
				tempStr.insert(4, '-');
				tempStr.insert(2, '-');
				tempStr.insert(0, "19");
			} else if (cardID.trim().length() == 18) {
				tempStr = new StringBuffer(cardID.substring(6, 14));
				tempStr.insert(6, '-');
				tempStr.insert(4, '-');
			}
		}
		return tempStr.toString();
	}

	/**
	 * 根据身份证号码获取性别
	 * 
	 * @param sfzh
	 * @return
	 */
	private String getSex(String sfzh) {
		if (StringHelper.isEmpty(sfzh)) {
			return null;
		}

		String sexSfzh = null;
		String sex = null;
		try {
			sexSfzh = StringHelper.decode(sfzh);
			if (Integer.parseInt(sexSfzh.substring(sexSfzh.length() - 2,
					sexSfzh.length() - 1)) % 2 != 0) {
				sex = "男";
			} else {
				sex = "女";
			}
		} catch (Throwable throwable) {
			sex = null;
		}
		return sex;
	}

	@Override
	public Map<String, String> checkAccountInfo() throws Throwable {
		return setCheckAccountInfo(serviceResource.getSession().getAccountId());
	}

	@Override
	public Map<String, String> checkAccountInfo(String accountName)
			throws Throwable {
		Integer F01 = getT6110F01(accountName);
		if (null == F01) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("checkFlag", "false");
			retMap.put("checkMessage", "用户账号不存在");
			return retMap;
		}
		return setCheckAccountInfo(F01);
	}

	private Map<String, String> setCheckAccountInfo(int userId)
			throws Throwable {
		ConfigureProvider configureProvider = serviceResource
				.getResource(ConfigureProvider.class);
		// 托管前缀
		String escrowPrefix = configureProvider
				.getProperty(SystemVariable.ESCROW_PREFIX);
		// 是否资金托管
		boolean isTg = BooleanParser.parse(configureProvider
				.getProperty(SystemVariable.SFZJTG));
		// 托管开户引导页面地址
		String escrowUrl = configureProvider
				.format(URLVariable.OPEN_ESCROW_GUIDE);
		// 自然人实名认证地址
		String safetyView_zrr = configureProvider
				.format(URLVariable.USER_BASES);
		// 非自然人实名认证地址
		String safetyView_fzrr = configureProvider.format(URLVariable.COM_FZRR);
		// 充值是否需要手机认证
		boolean mPhone = BooleanParser.parse(configureProvider
				.getProperty(PayVariavle.CHARGE_MUST_PHONE));
		// 充值是否需要邮箱认证
		boolean mYxrz = BooleanParser.parse(configureProvider
				.getProperty(PayVariavle.CHARGE_MUST_EMAIL));
		// 充值是否需要实名认证
		boolean mRealName = BooleanParser.parse(configureProvider
				.getProperty(PayVariavle.CHARGE_MUST_NCIIC));
		// 系统是否需要交易密码
		boolean mWithPsd = BooleanParser.parse(configureProvider
				.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));

		// 查询用户基本信息
		T6110 t6110RZ = getUserInfo(userId);
		// 获取用户第三方托管账号
		String usrCustId = getUsrCustId(userId);
		// 获取认证情况
		T6118 t6118 = getVerifyEntity(userId);

		String rzUrl = ""; // 需要跳转到的认证地址
		String checkFlag = "true"; // 验证结果：true：通过；false:不通过
		StringBuffer checkMessage = new StringBuffer(""); // 验证失败描述
		Map<String, String> retMap = new HashMap<>();
		List<String> list = new ArrayList<>();

		// 托管版本：没有实名认证，直接跳转到“托管开户引导页面”
		if (isTg && StringHelper.isEmpty(usrCustId)) {
			checkMessage.append("请您先在第三方托管平台上进行注册！");
			retMap.put("checkFlag", "false");
			retMap.put("checkMessage", checkMessage.toString());
			retMap.put("rzUrl", escrowUrl);
			retMap.put("failReason", "thirdNull");
			return retMap;
		}

		checkMessage.append("必须先<span class='red'>");
		// 特殊托管：富有版本，需要设置交易密码
		if ("FUYOU".equals(escrowPrefix) && mWithPsd
				&& t6118.F05.equals(T6118_F05.WSZ)) {
			checkMessage.append("设置交易密码</span>！");
			checkFlag = "false";
		}

		// 新浪存管
		if ("sina".equals(escrowPrefix) && t6110RZ.F06 == T6110_F06.FZRR) {
			usrCustId = getUsrCustIdForSina(userId);
			if (StringHelper.isEmpty(usrCustId)) {
				retMap.put("checkFlag", "false");
				retMap.put("checkMessage", checkMessage.toString());
				retMap.put("rzUrl", escrowUrl);
				retMap.put("failReason", "thirdNull");
				return retMap;
			}
		}

		// 网关版本
		if (!isTg) {
			if (mRealName && t6118.F02.equals(T6118_F02.BTG)) {
				list.add("实名认证");
			}
			if (mPhone && t6118.F03.equals(T6118_F03.BTG)
					&& StringHelper.isEmpty(t6110RZ.F04)) {
				list.add("手机号认证");
			}
			if (mYxrz && t6118.F04.equals(T6118_F04.BTG)
					&& StringHelper.isEmpty(t6110RZ.F05)) {
				list.add("邮箱认证");
			}
			if (mWithPsd && t6118.F05.equals(T6118_F05.WSZ)) {
				list.add("设置交易密码");
			}

			int i = 0;
			for (String msg : list) {
				i++;
				if (i == 1) {
					checkFlag = "false";
					checkMessage.append(msg);
					continue;
				}
				checkMessage.append("、");
				checkMessage.append(msg);
			}
			checkMessage.append("</span>！");
		}

		if (T6110_F06.FZRR == t6110RZ.F06) {
			rzUrl = safetyView_fzrr;
		} else {
			rzUrl = safetyView_zrr;
		}

		retMap.put("checkFlag", checkFlag);
		retMap.put("checkMessage", checkMessage.toString());
		retMap.put("rzUrl", rzUrl);
		return retMap;
	}

	private Integer getT6110F01(String accountName) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F01 FROM S61.T6110 WHERE F02 = ? LIMIT 1")) {
				ps.setString(1, accountName);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}

				}
			}
			return null;
		}
	}

}
