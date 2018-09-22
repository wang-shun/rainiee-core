package com.dimeng.p2p.modules.account.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6118;
import com.dimeng.p2p.S61.entities.T6125;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F12;
import com.dimeng.p2p.S61.enums.T6110_F13;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6110_F18;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.S61.enums.T6118_F05;
import com.dimeng.p2p.S61.enums.T6125_F05;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S61.enums.T6141_F09;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.common.MD5Utils;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.modules.account.console.service.ZhglManage;
import com.dimeng.p2p.modules.account.console.service.entity.User;
import com.dimeng.p2p.modules.account.console.service.query.UserQuery;
import com.dimeng.p2p.variables.defines.BusinessVariavle;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

public class ZhglManageImpl extends AbstractUserService implements ZhglManage {

	public ZhglManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public PagingResult<User> search(UserQuery userQuery, Paging paging)
			throws Throwable {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT T6110.F01 AS F01, T6110.F02 AS F02, T6110.F04 AS F03, T6110.F05 AS F04,");
		sql.append("  T6110.F06 AS F05, T6110.F07 AS F06, T6110.F08 AS F07, T6110.F09 AS F08, T6110.F10 AS F09, T6110.F14 AS F14 ");
		sql.append("  FROM S61.T6110 WHERE 1=1 AND F01 != 1 ");
		ArrayList<Object> parameters = new ArrayList<>();
		sql.append(" AND F13 = ?");
		parameters.add(T6110_F13.F);
		if (userQuery != null) {
			SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
			String userName = userQuery.userName;
			if (!StringHelper.isEmpty(userName)) {
				sql.append(" AND F02 LIKE ?");
				parameters.add(sqlConnectionProvider.allMatch(userName));
			}
			String phone = userQuery.phone;
			if (!StringHelper.isEmpty(phone)) {
				sql.append(" AND F04 LIKE ?");
				parameters.add(sqlConnectionProvider.allMatch(phone));
			}
			String eamil = userQuery.eamil;
			if (!StringHelper.isEmpty(eamil)) {
				sql.append(" AND F05 LIKE ?");
				parameters.add(sqlConnectionProvider.allMatch(eamil));
			}
			T6110_F07 status = userQuery.status;
			if (status != null) {
				sql.append(" AND F07 = ?");
				parameters.add(status.name());
			}
			T6110_F08 zcly = userQuery.zcly;
			if (zcly != null) {
				sql.append(" AND F08 = ?");
				parameters.add(zcly.name());
			}
			Timestamp startTime = userQuery.startTime;
			if (startTime != null) {
				sql.append(" AND DATE(F09) >= ?");
				parameters.add(startTime);
			}
			Timestamp endTime = userQuery.endTime;
			if (endTime != null) {
				sql.append(" AND DATE(F09) <= ?");
				parameters.add(endTime);
			}
			String zhlx = userQuery.zhlx;
			if (!StringHelper.isEmpty(zhlx)) {
				if (zhlx.equals("GR")) {
					sql.append(" AND F06 = ?");
					parameters.add(T6110_F06.ZRR.name());
				} else if (zhlx.equals("QY")) {
					sql.append(" AND F06 = ?");
					sql.append(" AND F10 = ?");
					parameters.add(T6110_F06.FZRR.name());
					parameters.add(T6110_F10.F.name());
				} else if (zhlx.equals("JG")) {
					sql.append(" AND F06 = ?");
					sql.append(" AND F10 = ?");
					parameters.add(T6110_F06.FZRR.name());
					parameters.add(T6110_F10.S.name());
				}
			}

			String employNum = userQuery.employNum;
			if (!StringHelper.isEmpty(employNum)) {
				sql.append(" AND F14 LIKE ?");
				parameters.add(sqlConnectionProvider.allMatch(employNum));
			}
		}
		sql.append(" ORDER BY T6110.F09 DESC");
		try (Connection connection = getConnection()) {
			return selectPaging(connection, new ArrayParser<User>() {

				@Override
				public User[] parse(ResultSet resultSet) throws SQLException {
					ArrayList<User> list = null;
					while (resultSet.next()) {
						User record = new User();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						record.F03 = resultSet.getString(3);
						record.F04 = resultSet.getString(4);
						record.F05 = T6110_F06.parse(resultSet.getString(5));
						record.F06 = T6110_F07.parse(resultSet.getString(6));
						record.F07 = T6110_F08.parse(resultSet.getString(7));
						record.F08 = resultSet.getTimestamp(8);
						record.F09 = T6110_F10.parse(resultSet.getString(9));
						record.employNum = resultSet.getString(10);
						record.isYzyq = isYzyq(connection, record.F01);
						record.isSafe = isSafe(connection, record.F01);
						record.isYq = isYq(connection, record.F01);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
					return ((list == null || list.size() == 0) ? null : list
							.toArray(new User[list.size()]));
				}
			}, paging, sql.toString(), parameters);
		}
	}

	// 企业组织社会信用代码不能重复
	protected boolean isShxydmExist(String F19) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F19 = ? LIMIT 1")) {
				pstmt.setString(1, F19);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return true;
					}
				}
			}
			return false;
		}
	}

	// 判断用户是否有逾期
	public boolean isYq(Connection connection, int id) throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT T6252.F01 FROM S62.T6252 WHERE T6252.F03 = ? AND T6252.F09=? AND T6252.F08<CURRENT_DATE()")) {
			pstmt.setInt(1, id);
			pstmt.setString(2, T6252_F09.WH.name());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return true;
				}
			}
		}
		return false;
	}

	// 是否严重逾期
	public boolean isYzyq(Connection connection, int id) throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT T6252.F01 FROM S62.T6252 WHERE T6252.F03 = ? AND T6252.F09 = 'WH' AND curdate() > date_add(T6252.F08,interval 30 day)")) {
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean isSafe(Connection connection, int id) throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT T6118.F02,T6118.F03,T6118.F04,T6118.F05 FROM S61.T6118 WHERE T6118.F01 = ? ")) {
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				T6118 t6118 = new T6118();
				if (rs.next()) {
					t6118.F02 = EnumParser.parse(T6118_F02.class,
							rs.getString(1));
					t6118.F03 = EnumParser.parse(T6118_F03.class,
							rs.getString(2));
					t6118.F04 = EnumParser.parse(T6118_F04.class,
							rs.getString(3));
					t6118.F05 = EnumParser.parse(T6118_F05.class,
							rs.getString(4));
					if (t6118.F02 == T6118_F02.TG && t6118.F03 == T6118_F03.TG
							&& t6118.F04 == T6118_F04.TG
							&& t6118.F05 == T6118_F05.YSZ) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isAccountExists(String account) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F02 = ? LIMIT 1")) {
				pstmt.setString(1, account);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public int addGr(String userName, String password, String employNum)
			throws Throwable {
		/**
		 * 用户账号表
		 */
		String t6110 = "INSERT INTO S61.T6110 SET F02 = ?, F03 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?,F14=? ";
		/**
		 * 用户理财统计表
		 */
		String t6115 = "INSERT INTO S61.T6115 SET F01 = ?";
		/**
		 * 用户信用账户表
		 */
		String t6116 = "INSERT INTO S61.T6116 SET F01 = ?";
		/**
		 * 用户安全认证表
		 */
		String t6118 = "INSERT INTO S61.T6118 SET F01 = ?";
		/**
		 * 用户站内信
		 */
		String t6123 = "INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?";
		/**
		 * 用户站内信内容
		 */
		String t6124 = "INSERT INTO S61.T6124 SET F01 = ?, F02 = ?";
		/**
		 * 个人基础信息
		 */
		String t6141 = "INSERT INTO S61.T6141 SET F01 = ? ";
		/**
		 * 资金账户
		 */
		String t6101 = "INSERT INTO S61.T6101 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?";
		/**
		 * 推广奖励统计
		 */
		String t6310 = "INSERT INTO S63.T6310 SET F01 = ?";
		/**
		 * 优选理财统计
		 */
		String t6413 = "INSERT INTO S64.T6413 SET F01 = ?";

		/**
		 * 实名认证统计
		 */
		String t6198 = "INSERT INTO S61.T6198 SET F02 = ? ";

		ConfigureProvider configureProvider = serviceResource
				.getResource(ConfigureProvider.class);
		if (StringHelper.isEmpty(userName)) {
			throw new ParameterException("用户名不能为空");
		}
		if (StringHelper.isEmpty(password)) {
			throw new ParameterException("密码不能为空");
		}
		if (password.equals(userName)) {
			throw new LogicalException("密码不能与用户名一致");
		}
		userName = RSAUtils.decryptStringByJs(userName);
		if (isAccountExists(userName)) {
			throw new LogicalException("该用户名已存在，请输入其他用户名.");
		}
		password = RSAUtils.decryptStringByJs(password);

		// 查询我的推广码
		String selectT6111 = "SELECT F02 FROM S61.T6111";
		String myCode = getCode();
		// 邀请码
		String code = "";
		boolean isExists = false;
		try (Connection connection = getConnection()) {
			try {
				serviceResource.openTransactions(connection);
				try (PreparedStatement ps = connection
						.prepareStatement(selectT6111)) {
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							String s = rs.getString(1);
							if (!StringHelper.isEmpty(s) && s.equals(code)) {
								isExists = true;
							}
							if (!StringHelper.isEmpty(s) && s.equals(myCode)) {
								myCode = getCode();
							}
						}
					}
				}

				if (!isExists) {
					code = "";
				}
				String jmPassword = UnixCrypt.crypt(password,
						DigestUtils.sha256Hex(password));
				Timestamp now = new Timestamp(System.currentTimeMillis());
				// 当前注册用户ID
				int userId = insert(connection, t6110, userName, jmPassword,
						T6110_F06.ZRR.name(), T6110_F07.QY.name(),
						T6110_F08.HTTJ.name(), now, T6110_F10.F.name(),
						employNum);
				execute(connection, t6115, userId);
				execute(connection, t6116, userId);
				execute(connection, t6118, userId);
				String template = configureProvider
						.getProperty(LetterVariable.REGESTER_SUCCESS);
				Envionment envionment = configureProvider.createEnvionment();
				envionment.set("name", userName);
				int letterId = insert(connection, t6123, userId, "注册成功", now);
				execute(connection, t6124, letterId,
						StringHelper.format(template, envionment));
				execute(connection, t6141, userId);

				for (T6101_F03 t : T6101_F03.values()) {
					execute(connection, t6101, userId, t.name(),
							getAccount(t.name(), userId),
							getAccountName(userId, connection));
				}
				execute(connection, t6310, userId);
				// 推广人ID
				int tgrID = 0;
				if (!StringHelper.isEmpty(code)) {
					String t6311 = "INSERT INTO S63.T6311 SET F02 = ?, F03 = ?";
					tgrID = getUserID(code, connection);
					execute(connection, t6311, tgrID, userId);
				}
				// 推广统计人数+1
				if (tgrID > 0) {
					execute(connection,
							"UPDATE S63.T6310 SET F02 = F02 + 1 WHERE F01 = ?",
							tgrID);
					// 填写的邀请码正确 插入的用户推广信息表
					T6110 tgrUser = getUser(tgrID, connection);
					execute(connection,
							"INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?,F04 = ?, F05 = ?",
							userId, myCode, code, tgrUser.F04, tgrUser.F02);
				} else {
					// 填写的邀请码不正确 插入的用户推广信息表
					execute(connection,
							"INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?",
							userId, myCode, code);
				}
				// 用户认证信息
				Integer[] rzxIds = getRzxID(connection);
				if (rzxIds != null) {
					for (Integer id : rzxIds) {
						execute(connection,
								"INSERT INTO S61.T6120 SET F01 = ?, F02 = ?",
								userId, id);
					}
				}
				execute(connection, "INSERT INTO S61.T6144 SET F01 = ?", userId);
				execute(connection, t6413, userId);

				// 实名认证统计
				execute(connection, t6198, userId);

				// 默认信用额度
				BigDecimal creditAmount = new BigDecimal(
						configureProvider
								.getProperty(SystemVariable.DEFAULT_CREDIT_AMOUNT));
				updateUserCredit(connection, userId, creditAmount);

				serviceResource.commit(connection);
				return userId;
			} catch (Exception e) {
				serviceResource.rollback(connection);
				throw e;
			}
		}
	}

	/**
	 * 生成资金账户 账号
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	protected String getAccount(String type, int id) {
		DecimalFormat df = new DecimalFormat("00000000000");
		StringBuilder sb = new StringBuilder();
		sb.append(type.substring(0, 1));
		sb.append(df.format(id));
		return sb.toString();
	}

	/**
	 * 通过填写的邀请码得到用户ID
	 * 
	 * @return
	 */
	protected int getUserID(String code, Connection connection)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01 FROM S61.T6111 WHERE T6111.F02 = ? LIMIT 1")) {
			pstmt.setString(1, code);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		}
		return 0;
	}

	/**
	 * 通过id找到用户信息
	 * 
	 * @param id
	 * @return
	 */
	protected T6110 getUser(int id, Connection connection) throws Throwable {
		T6110 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F02, F04 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")) {
			pstmt.setInt(1, id);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6110();
					record.F02 = resultSet.getString(1);
					record.F04 = resultSet.getString(2);
				}
			}
		}
		return record;
	}

	// 得到所有启用的信用认证项ID
	protected Integer[] getRzxID(Connection connection) throws SQLException {
		ArrayList<Integer> list = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01 FROM S51.T5123 ")) {
			// pstmt.setString(1, T5123_F04.QY.name());
			try (ResultSet resultSet = pstmt.executeQuery()) {
				while (resultSet.next()) {
					if (list == null)
						list = new ArrayList<>();
					list.add(resultSet.getInt(1));
				}
			}
		}
		return ((list == null || list.size() == 0) ? null : list
				.toArray(new Integer[list.size()]));

	}

	// 生成我的邀请码
	protected String getCode() {
		char[] chs = { 'a', 'b', 'c', '1', '2', '3', '4', '5', 'd', 'e', 'f',
				'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
				's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '6', '7', '8', '9' };
		SecureRandom random = new SecureRandom();
		final char[] value = new char[6];
		for (int i = 0; i < 6; i++) {
			value[i] = chs[random.nextInt(chs.length)];
		}
		return String.valueOf(value);
	}

	// 通过用户ID得到用户名
	protected String getAccountName(int userID, Connection connection)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F02 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")) {
			pstmt.setInt(1, userID);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getString(1);
				}
			}
		}
		return null;
	}

	@Override
	public int addJg(T6161 entity, String dbjgms, String jgjs, String lxName,
			String lxTel, String mobile, String email, T6110_F17 t6110_f17,
			T6110_F19 t6110_f19) throws Throwable {
		if (entity.F03 != null && entity.F03 != "" && isZchExist(entity.F03)) {
			throw new ParameterException("营业执照登记注册号已存在");
		}
		if (entity.F05 != null && entity.F05 != "" && isNshExist(entity.F05)) {
			throw new ParameterException("企业纳税号已存在");
		}
		if (entity.F06 != null && entity.F06 != "" && isZzjgExist(entity.F06)) {
			throw new ParameterException("企业组织机构代码已存在");
		}
		if (checkCardExists(entity.F13, T6110_F06.FZRR)) {
			throw new ParameterException("身份证号不能重复");
		}
		if (isPhoneExist(mobile)) {
			throw new ParameterException("法人手机号码已经存在");
		}
		if (isPhoneExist(lxTel)) {
			throw new ParameterException("联系人手机号码已经存在");
		}
		if (entity.F19 != null && entity.F19 != "" && isShxydmExist(entity.F19)) {
			throw new ParameterException("企业社会信用代码已存在");
		}
		if (!StringHelper.isEmpty(email) && isEmailExist(email)) {
			throw new ParameterException("法人邮箱地址已经存在");
		}
		/**
		 * 用户账号表
		 */
		String t6110 = "INSERT INTO S61.T6110 SET F02 = ?, F03 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F17 = ?, F18 = ?,F19 = ? ";
		/**
		 * 用户理财统计表
		 */
		String t6115 = "INSERT INTO S61.T6115 SET F01 = ?";
		/**
		 * 用户信用账户表
		 */
		String t6116 = "INSERT INTO S61.T6116 SET F01 = ?";
		/**
		 * 用户安全认证表
		 */
		String t6118 = "INSERT INTO S61.T6118 SET F01 = ?";
		/**
		 * 用户站内信
		 */
		String t6123 = "INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?";
		/**
		 * 用户站内信内容
		 */
		String t6124 = "INSERT INTO S61.T6124 SET F01 = ?, F02 = ?";
		/**
		 * 个人基础信息
		 */
		String t6141 = "INSERT INTO S61.T6141 SET F01 = ? ";

		/**
		 * 资金账户
		 */
		String t6101 = "INSERT INTO S61.T6101 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?";
		/**
		 * 推广奖励统计
		 */
		String t6310 = "INSERT INTO S63.T6310 SET F01 = ?";

		/**
		 * 实名认证统计
		 */
		String t6198 = "INSERT INTO S61.T6198 SET F02 = ? ";

		// 查询我的推广码
		String selectT6111 = "SELECT F02 FROM S61.T6111";
		String myCode = getCode();
		// 邀请码
		String code = "";
		boolean isExists = false;
		try (Connection connection = getConnection()) {
			try {
				serviceResource.openTransactions(connection);
				try (PreparedStatement ps = connection
						.prepareStatement(selectT6111)) {
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							String s = rs.getString(1);
							if (!StringHelper.isEmpty(s) && s.equals(code)) {
								isExists = true;
							}
							if (!StringHelper.isEmpty(s) && s.equals(myCode)) {
								myCode = getCode();
							}
						}
					}
				}

				if (!isExists) {
					code = "";
				}
				String jmPassword = UnixCrypt.crypt("888888",
						DigestUtils.sha256Hex("888888"));
				Timestamp now = new Timestamp(System.currentTimeMillis());

				// 当前注册用户ID
				String userName = getJgZhName();
				int userId = insert(connection, t6110, userName, jmPassword,
						T6110_F06.FZRR.name(), T6110_F07.QY.name(),
						T6110_F08.HTTJ.name(), now, T6110_F10.S.name(),
						t6110_f17.name(),
						t6110_f17 == T6110_F17.S ? T6110_F18.S.name()
								: T6110_F18.F.name(), t6110_f19.name());
				execute(connection, t6115, userId);
				execute(connection, t6116, userId);
				execute(connection, t6118, userId);
				execute(connection, t6141, userId);
				ConfigureProvider configureProvider = serviceResource
						.getResource(ConfigureProvider.class);
				/***** begin *******/
				// 添加担保码（申请担保贷专用2016-06-13添加）
				T6125 t6125 = new T6125();
				t6125.F02 = userId;
				t6125.F04 = BigDecimalParser.parse(configureProvider
						.getProperty(SystemVariable.DEFAULT_GUARATTE_AMOUNT));
				t6125.F05 = T6125_F05.SQCG;
				t6125.F06 = now;
				t6125.F07 = 1000;
				t6125.F08 = now;
				applyGuarantor(connection, t6125);
				/***** end *******/
				String template = configureProvider
						.getProperty(LetterVariable.REGESTER_SUCCESS);
				Envionment envionment = configureProvider.createEnvionment();
				envionment.set("name", userName);
				int letterId = insert(connection, t6123, userId, "注册成功", now);
				execute(connection, t6124, letterId,
						StringHelper.format(template, envionment));
				for (T6101_F03 t : T6101_F03.values()) {
					execute(connection, t6101, userId, t.name(),
							getAccount(t.name(), userId),
							getAccountName(userId, connection));
				}
				execute(connection, t6310, userId);
				// 推广人ID
				int tgrID = 0;
				if (!StringHelper.isEmpty(code)) {
					String t6311 = "INSERT INTO S63.T6311 SET F02 = ?, F03 = ?";
					tgrID = getUserID(code, connection);
					execute(connection, t6311, tgrID, userId);
				}
				// 推广统计人数+1
				if (tgrID > 0) {
					execute(connection,
							"UPDATE S63.T6310 SET F02 = F02 + 1 WHERE F01 = ?",
							tgrID);
					// 填写的邀请码正确 插入的用户推广信息表
					T6110 tgrUser = getUser(tgrID, connection);
					execute(connection,
							"INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?,F04 = ?, F05 = ?",
							userId, myCode, code, tgrUser.F04, tgrUser.F02);
				} else {
					// 填写的邀请码不正确 插入的用户推广信息表
					execute(connection,
							"INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?",
							userId, myCode, code);
				}
				// 用户认证信息
				// Integer[] rzxIds = getRzxID();
				// if (rzxIds != null) {
				// for (Integer id : rzxIds) {
				// execute(connection,
				// "INSERT INTO S61.T6120 SET F01 = ?, F02 = ?", userId,
				// id);
				// }
				// }
				// 用户信用档案表
				execute(connection, "INSERT INTO S61.T6144 SET F01 = ?", userId);
				// 企业联系信息
				execute(connection,
						"INSERT INTO S61.T6164 SET F01 = ?, F04 = ?, F06 = ?, F07 = ?",
						userId, lxTel, mobile, lxName);
				// 企业介绍资料
				execute(connection, "INSERT INTO S61.T6162 SET F01 = ?", userId);
				// 企业基础信息
				/*
				 * try (PreparedStatement pstmt = connection.prepareStatement(
				 * "INSERT INTO S61.T6161 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = ?, F18 = ?"
				 * )) { pstmt.setInt(1, userId); pstmt.setString(2, userName);
				 * pstmt.setString(3, entity.F03); pstmt.setString(4,
				 * entity.F04); pstmt.setString(5, entity.F05);
				 * pstmt.setString(6, entity.F06); pstmt.setInt(7, entity.F07);
				 * pstmt.setBigDecimal(8, entity.F08); pstmt.setString(9,
				 * entity.F09); pstmt.setInt(10, entity.F10);
				 * pstmt.setString(11, entity.F11); //String sfz = entity.F12;
				 * pstmt.setString(12, entity.F12); pstmt.setString(13,
				 * entity.F13); pstmt.setBigDecimal(14, entity.F14);
				 * pstmt.setBigDecimal(15, entity.F15); pstmt.setString(16,
				 * entity.F18); pstmt.execute(); }
				 */
				if (!StringHelper.isEmpty(entity.F19)) {
					try (PreparedStatement pstmt = connection
							.prepareStatement("INSERT INTO S61.T6161 SET F01 = ?, F02 = ?, F04 = ?,"
									+ "F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = ?, F18 = ?,F19= ?,F20= ?,F21 = ?")) {
						pstmt.setInt(1, userId);
						pstmt.setString(2, userName);
						pstmt.setString(3, entity.F04);
						pstmt.setInt(4, entity.F07);
						pstmt.setBigDecimal(5, entity.F08);
						pstmt.setString(6, entity.F09);
						pstmt.setInt(7, entity.F10);
						pstmt.setString(8, entity.F11);
						pstmt.setString(9, entity.F12);
						pstmt.setString(10, entity.F13);
						pstmt.setBigDecimal(11, entity.F14);
						pstmt.setBigDecimal(12, entity.F15);
						pstmt.setString(13, entity.F18);
						pstmt.setString(14, entity.F19);
						pstmt.setString(15, entity.F20);
						pstmt.setString(16, entity.F21);
						pstmt.execute();
					}
				} else {
					try (PreparedStatement pstmt = connection
							.prepareStatement("INSERT INTO S61.T6161 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, "
									+ "F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = ?, F18 = ?,F20 = ?,F21 = ?")) {
						pstmt.setInt(1, userId);
						pstmt.setString(2, userName);
						pstmt.setString(3, entity.F03);
						pstmt.setString(4, entity.F04);
						pstmt.setString(5, entity.F05);
						pstmt.setString(6, entity.F06);
						pstmt.setInt(7, entity.F07);
						pstmt.setBigDecimal(8, entity.F08);
						pstmt.setString(9, entity.F09);
						pstmt.setInt(10, entity.F10);
						pstmt.setString(11, entity.F11);
						// String sfz = entity.F12;
						pstmt.setString(12, entity.F12);
						pstmt.setString(13, entity.F13);
						pstmt.setBigDecimal(14, entity.F14);
						pstmt.setBigDecimal(15, entity.F15);
						pstmt.setString(16, entity.F18);
						pstmt.setString(17, entity.F20);
						pstmt.setString(18, entity.F21);
						pstmt.execute();
					}
				}
				execute(connection,
						"INSERT INTO S61.T6180 SET F01 = ?, F02 = ?, F04 = ?",
						userId, jgjs, dbjgms);
				// 更新信用认证信息
				execute(connection,
						"UPDATE S61.T6118 SET F02=?,F03=?,F04=?,F06=?,F07=? WHERE F01=?",
						T6118_F02.TG, T6118_F03.TG,
						StringHelper.isEmpty(email) ? T6118_F04.BTG
								: T6118_F04.TG, mobile, email, userId);
				// 更新用户信息
				execute(connection,
						"UPDATE S61.T6110 SET F04=?,F05=? WHERE F01=?", mobile,
						email, userId);
				Timestamp date = TimestampParser.parse(getBirthday(StringHelper
						.decode(entity.F13)));
				// 更新个人身份证
				execute(connection,
						"UPDATE S61.T6141 SET F02=?,F04=?,F06=?,F07=?,F08=?,F09=? WHERE F01=?",
						entity.F11, T6141_F04.TG, entity.F12, entity.F13, date,
						getSexNotDecode(StringHelper.decode(entity.F13)),
						userId);

				// 实名认证统计
				execute(connection, t6198, userId);

				// 默认信用额度
				BigDecimal creditAmount = new BigDecimal(
						configureProvider
								.getProperty(SystemVariable.DEFAULT_CREDIT_AMOUNT));
				updateUserCredit(connection, userId, creditAmount);

				serviceResource.commit(connection);
				return userId;
			} catch (Exception e) {
				serviceResource.rollback(connection);
				throw e;
			}
		}
	}

	@Override
	public int addQy(T6161 entity, String lxName, String lxTel, String mobile,
			String email, T6110_F17 t6110_f17) throws Throwable {
		if (entity.F03 != null && entity.F03 != "" && isZchExist(entity.F03)) {
			throw new ParameterException("营业执照登记注册号重复");
		}
		if (entity.F05 != null && entity.F05 != "" && isNshExist(entity.F05)) {
			throw new ParameterException("企业纳税号已存在");
		}
		if (entity.F06 != null && entity.F06 != "" && isZzjgExist(entity.F06)) {
			throw new ParameterException("企业组织机构代码已存在");
		}
		if (checkCardExists(entity.F13, T6110_F06.FZRR)) {
			throw new ParameterException("身份证号不能重复");
		}
		if (isPhoneExist(mobile)) {
			// 检查企业、机构的法人手机号码是否存在
			throw new ParameterException("法人手机号码已经存在");
		}
		if (isPhoneExist(lxTel)) {
			throw new ParameterException("联系人手机号码已经存在");
		}
		if (entity.F19 != null && entity.F19 != "" && isShxydmExist(entity.F19)
				&& "Y".equals(entity.F20)) {
			throw new ParameterException("企业社会信用代码已存在");
		}
		if (!StringHelper.isEmpty(email) && isEmailExist(email)) {
			throw new ParameterException("法人邮箱地址已经存在");
		}
		/**
		 * 用户账号表
		 */
		String t6110 = "INSERT INTO S61.T6110 SET F02 = ?, F03 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F17 = ?, F18 = ?";
		/**
		 * 用户理财统计表
		 */
		String t6115 = "INSERT INTO S61.T6115 SET F01 = ?";
		/**
		 * 用户信用账户表
		 */
		String t6116 = "INSERT INTO S61.T6116 SET F01 = ?";
		/**
		 * 用户安全认证表
		 */
		String t6118 = "INSERT INTO S61.T6118 SET F01 = ?";
		/**
		 * 用户站内信
		 */
		String t6123 = "INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?";
		/**
		 * 用户站内信内容
		 */
		String t6124 = "INSERT INTO S61.T6124 SET F01 = ?, F02 = ?";
		/**
		 * 个人基础信息
		 */
		String t6141 = "INSERT INTO S61.T6141 SET F01 = ? ";
		/**
		 * 资金账户
		 */
		String t6101 = "INSERT INTO S61.T6101 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?";
		/**
		 * 推广奖励统计
		 */
		String t6310 = "INSERT INTO S63.T6310 SET F01 = ?";

		/**
		 * 实名认证统计
		 */
		String t6198 = "INSERT INTO S61.T6198 SET F02 = ? ";

		// 查询我的推广码
		String selectT6111 = "SELECT F02 FROM S61.T6111";
		String myCode = getCode();
		// 邀请码
		String code = "";
		boolean isExists = false;
		try (Connection connection = getConnection()) {
			try {
				serviceResource.openTransactions(connection);
				try (PreparedStatement ps = connection
						.prepareStatement(selectT6111)) {
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next())

						{
							String s = rs.getString(1);
							if (!StringHelper.isEmpty(s) && s.equals(code)) {
								isExists = true;
							}
							if (!StringHelper.isEmpty(s) && s.equals(myCode)) {
								myCode = getCode();
							}

						}
					}

				}

				if (!isExists) {
					code = "";
				}
				String jmPassword = UnixCrypt.crypt("888888",
						DigestUtils.sha256Hex("888888"));
				Timestamp now = new Timestamp(System.currentTimeMillis());

				// 当前注册用户ID
				String userName = getZhName();
				int userId = insert(connection, t6110, userName, jmPassword,
						T6110_F06.FZRR.name(), T6110_F07.QY.name(),
						T6110_F08.HTTJ.name(), now, T6110_F10.F.name(),
						t6110_f17.name(), t6110_f17.name());
				execute(connection, t6115, userId);
				execute(connection, t6116, userId);
				execute(connection, t6118, userId);
				execute(connection, t6141, userId);
				ConfigureProvider configureProvider = serviceResource
						.getResource(ConfigureProvider.class);
				String template = configureProvider
						.getProperty(LetterVariable.REGESTER_SUCCESS);
				Envionment envionment = configureProvider.createEnvionment();
				envionment.set("name", userName);
				int letterId = insert(connection, t6123, userId, "注册成功", now);
				execute(connection, t6124, letterId,
						StringHelper.format(template, envionment));
				for (T6101_F03 t : T6101_F03.values()) {
					execute(connection, t6101, userId, t.name(),
							getAccount(t.name(), userId),
							getAccountName(userId, connection));
				}
				execute(connection, t6310, userId);
				// 推广人ID
				int tgrID = 0;
				if (!StringHelper.isEmpty(code)) {
					String t6311 = "INSERT INTO S63.T6311 SET F02 = ?, F03 = ?";
					tgrID = getUserID(code, connection);
					execute(connection, t6311, tgrID, userId);
				}
				// 推广统计人数+1
				if (tgrID > 0) {
					execute(connection,
							"UPDATE S63.T6310 SET F02 = F02 + 1 WHERE F01 = ?",
							tgrID);
					// 填写的邀请码正确 插入的用户推广信息表
					T6110 tgrUser = getUser(tgrID, connection);
					execute(connection,
							"INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?,F04 = ?, F05 = ?",
							userId, myCode, code, tgrUser.F04, tgrUser.F02);
				} else {
					// 填写的邀请码不正确 插入的用户推广信息表
					execute(connection,
							"INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?",
							userId, myCode, code);
				}
				// 用户认证信息
				// Integer[] rzxIds = getRzxID();
				// if (rzxIds != null) {
				// for (Integer id : rzxIds) {
				// execute(connection,
				// "INSERT INTO S61.T6120 SET F01 = ?, F02 = ?", userId,
				// id);
				// }
				// }

				// 企业联系信息
				execute(connection,
						"INSERT INTO S61.T6164 SET F01 = ?, F04 = ?,F06 = ?, F07 = ?",
						userId, lxTel, mobile, lxName);
				// 用户信用档案表
				execute(connection, "INSERT INTO S61.T6144 SET F01 = ?", userId);
				// 企业介绍资料
				execute(connection, "INSERT INTO S61.T6162 SET F01 = ?", userId);
				// 企业基础信息
				/*
				 * try (PreparedStatement pstmt = connection .prepareStatement(
				 * "INSERT INTO S61.T6161 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = ?,F16 = ?,F17 = ?"
				 * )) { pstmt.setInt(1, userId); pstmt.setString(2, userName);
				 * pstmt.setString(3, entity.F03); pstmt.setString(4,
				 * entity.F04); pstmt.setString(5, entity.F05);
				 * pstmt.setString(6, entity.F06); pstmt.setInt(7, entity.F07);
				 * pstmt.setBigDecimal(8, entity.F08); pstmt.setString(9,
				 * entity.F09); pstmt.setInt(10, entity.F10);
				 * pstmt.setString(11, entity.F11); // String sfz = entity.F12;
				 * pstmt.setString(12, entity.F12); pstmt.setString(13,
				 * entity.F13); pstmt.setBigDecimal(14, entity.F14);
				 * pstmt.setBigDecimal(15, entity.F15); pstmt.setString(16,
				 * entity.F16); pstmt.setString(17, entity.F17);
				 * pstmt.execute(); }
				 */
				if (!StringHelper.isEmpty(entity.F19)) {
					try (PreparedStatement pstmt = connection
							.prepareStatement("INSERT INTO S61.T6161 SET F01 = ?, F02 = ?, F04 = ?, F07 = ?, F08 = ?, "
									+ "F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = ?,F16 = ?,F17 = ?,F19= ?,F20= ?,F21= ?,F18 = ?,F23=?,F24=?")) {
						pstmt.setInt(1, userId);
						pstmt.setString(2, userName);
						pstmt.setString(3, entity.F04);
						pstmt.setInt(4, entity.F07);
						pstmt.setBigDecimal(5, entity.F08);
						pstmt.setString(6, entity.F09);
						pstmt.setInt(7, entity.F10);
						pstmt.setString(8, entity.F11);
						pstmt.setString(9, entity.F12);
						pstmt.setString(10, entity.F13);
						pstmt.setBigDecimal(11, entity.F14);
						pstmt.setBigDecimal(12, entity.F15);
						pstmt.setString(13, entity.F16);
						pstmt.setString(14, entity.F17);
						pstmt.setString(15, entity.F19);
						pstmt.setString(16, entity.F20);
						pstmt.setString(17, entity.F21);
						pstmt.setString(18, entity.F18);
						pstmt.setString(19, entity.F23);
						pstmt.setString(20, entity.F24);
						pstmt.execute();
					}
				} else {
					try (PreparedStatement pstmt = connection
							.prepareStatement("INSERT INTO S61.T6161 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, "
									+ "F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = ?,F16 = ?,F17 = ?,F20 = ?,F21 = ?,F18 = ?,F23=?,F24=?")) {
						pstmt.setInt(1, userId);
						pstmt.setString(2, userName);
						pstmt.setString(3, entity.F03);
						pstmt.setString(4, entity.F04);
						pstmt.setString(5, entity.F05);
						pstmt.setString(6, entity.F06);
						pstmt.setInt(7, entity.F07);
						pstmt.setBigDecimal(8, entity.F08);
						pstmt.setString(9, entity.F09);
						pstmt.setInt(10, entity.F10);
						pstmt.setString(11, entity.F11);
						// String sfz = entity.F12;
						pstmt.setString(12, entity.F12);
						pstmt.setString(13, entity.F13);
						pstmt.setBigDecimal(14, entity.F14);
						pstmt.setBigDecimal(15, entity.F15);
						pstmt.setString(16, entity.F16);
						pstmt.setString(17, entity.F17);
						pstmt.setString(18, entity.F20);
						pstmt.setString(19, entity.F21);
						pstmt.setString(20, entity.F18);
						pstmt.setString(21, entity.F23);
						pstmt.setString(22, entity.F24);
						pstmt.execute();
					}
				}
				// 更新信用认证信息
				execute(connection,
						"UPDATE S61.T6118 SET F02=?,F03=?,F04=?,F06=?,F07=? WHERE F01=?",
						T6118_F02.TG, T6118_F03.TG,
						StringHelper.isEmpty(email) ? T6118_F04.BTG
								: T6118_F04.TG, mobile, email, userId);
				// 更新用户信息
				execute(connection,
						"UPDATE S61.T6110 SET F04=?,F05=? WHERE F01=?", mobile,
						email, userId);
				Timestamp date = TimestampParser.parse(getBirthday(StringHelper
						.decode(entity.F13)));
				// 更新个人身份证
				execute(connection,
						"UPDATE S61.T6141 SET F02=?,F04=?,F06=?,F07=?,F08=?,F09=? WHERE F01=?",
						entity.F11, T6141_F04.TG, entity.F12, entity.F13, date,
						getSexNotDecode(StringHelper.decode(entity.F13)),
						userId);

				// 实名认证统计
				execute(connection, t6198, userId);

				// 默认信用额度
				BigDecimal creditAmount = new BigDecimal(
						configureProvider
								.getProperty(SystemVariable.DEFAULT_CREDIT_AMOUNT));
				updateUserCredit(connection, userId, creditAmount);

				serviceResource.commit(connection);
				return userId;
			} catch (Exception e) {
				serviceResource.rollback(connection);
				throw e;
			}
		}
	}

	protected boolean checkCardExists(String card) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S61.T6141 WHERE T6141.F07 = ? LIMIT 1")) {
				pstmt.setString(1, card);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	protected boolean checkCardExists(String card, T6110_F06 t6110_f06)
			throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6141.F01 FROM S61.T6141 INNER JOIN S61.T6110 ON T6141.F01=T6110.F01 WHERE T6141.F07 = ? AND T6110.F06=? LIMIT 1")) {
				pstmt.setString(1, card);
				pstmt.setString(2, t6110_f06.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// 生成企业账号
	protected synchronized String getJgZhName() throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT MAX(F01) FROM S61.T6110")) {
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						DecimalFormat df = new DecimalFormat("JGZH"
								+ MD5Utils.get5Radom() + "000000");
						return df.format(resultSet.getInt(1));
					} else {
						return "JGZH" + MD5Utils.get5Radom() + "000000";
					}
				}
			}
		}
	}

	// 生成企业账号
	protected synchronized String getZhName() throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT MAX(F01) FROM S61.T6110")) {
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						DecimalFormat df = new DecimalFormat("QYZH"
								+ MD5Utils.get5Radom() + "000000");
						return df.format(resultSet.getInt(1));
					} else {
						return "QYZH" + MD5Utils.get5Radom() + "000000";
					}
				}
			}
		}
	}

	// 营业执照登记注册号是否重复
	protected boolean isZchExist(String zch) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F03 = ? LIMIT 1")) {
				pstmt.setString(1, zch);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// 企业纳税号不能重复
	protected boolean isNshExist(String F05) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F05 = ? LIMIT 1")) {
				pstmt.setString(1, F05);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return true;
					}
				}
			}
			return false;
		}
	}

	// 企业联系手机不能重复
	protected boolean isPhoneExist(String phone) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F04 = ? LIMIT 1")) {
				pstmt.setString(1, phone);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return true;
					}
				}
			}
			return false;
		}
	}

	// 企业组织机构代码不能重复
	protected boolean isZzjgExist(String F06) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F06 = ? LIMIT 1")) {
				pstmt.setString(1, F06);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return true;
					}
				}
			}
			return false;
		}
	}

	/* 检查邮箱地址是否已经被占用 */
	protected boolean isEmailExist(String email) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F05 = ? LIMIT 1")) {
				ps.setString(1, email);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void export(User[] users, OutputStream outputStream, String charset)
			throws Throwable {
		if (outputStream == null) {
			return;
		}
		if (users == null) {
			return;
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		final ConfigureProvider configureProvider = serviceResource
				.getResource(ConfigureProvider.class);
		boolean is_business = Boolean.parseBoolean(configureProvider
				.getProperty(BusinessVariavle.IS_BUSINESS));
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			CVSWriter writer = new CVSWriter(out);
			writer.write("序号");
			writer.write("用户名");
			writer.write("联系电话");
			writer.write("邮箱");
			writer.write("状态");
			writer.write("注册来源 ");
			writer.write("注册时间");
			writer.write("用户类型");
			if (is_business) {
				writer.write("业务员工号");
			}
			writer.newLine();
			int index = 1;
			for (User user : users) {
				writer.write(index++);
				writer.write(user.F02);
				writer.write(user.F03);
				writer.write(user.F04);
				writer.write(user.F06.getChineseName());
				writer.write(user.F07.getChineseName());
				writer.write(DateTimeParser.format(user.F08) + "\t");
				if (user.F05 == T6110_F06.ZRR) {
					writer.write("个人");
				} else if (user.F05 == T6110_F06.FZRR
						&& user.F09 == T6110_F10.F) {
					writer.write("企业");
				} else if (user.F05 == T6110_F06.FZRR
						&& user.F09 == T6110_F10.S) {
					writer.write("机构");
				}
				if (is_business) {
					writer.write(user.employNum);
				}
				writer.newLine();
			}
		}
	}

	@Override
	public int addJgZxq(T6161 entity, String jgjs, String lxName, String lxTel)
			throws Throwable {

		if (isZchExist(entity.F03)) {
			throw new ParameterException("营业执照登记注册号已存在");
		}
		if (isNshExist(entity.F05)) {
			throw new ParameterException("企业纳税号已存在");
		}
		if (isZzjgExist(entity.F06)) {
			throw new ParameterException("企业组织机构代码已存在");
		}

		/**
		 * 用户账号表
		 */
		String t6110 = "INSERT INTO S61.T6110 SET F02 = ?, F03 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?";
		/**
		 * 用户理财统计表
		 */
		String t6115 = "INSERT INTO S61.T6115 SET F01 = ?";
		/**
		 * 用户信用账户表
		 */
		String t6116 = "INSERT INTO S61.T6116 SET F01 = ?";
		/**
		 * 用户安全认证表
		 */
		String t6118 = "INSERT INTO S61.T6118 SET F01 = ?";
		/**
		 * 用户站内信
		 */
		String t6123 = "INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?";
		/**
		 * 用户站内信内容
		 */
		String t6124 = "INSERT INTO S61.T6124 SET F01 = ?, F02 = ?";
		/**
		 * 个人基础信息
		 */
		String t6141 = "INSERT INTO S61.T6141 SET F01 = ? ";

		/**
		 * 资金账户
		 */
		String t6101 = "INSERT INTO S61.T6101 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?";
		/**
		 * 推广奖励统计
		 */
		String t6310 = "INSERT INTO S63.T6310 SET F01 = ?";

		// 查询我的推广码
		String selectT6111 = "SELECT F02 FROM S61.T6111";
		String myCode = getCode();
		// 邀请码
		String code = "";
		boolean isExists = false;
		try (Connection connection = getConnection()) {
			try {
				serviceResource.openTransactions(connection);
				try (PreparedStatement ps = connection
						.prepareStatement(selectT6111)) {
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							String s = rs.getString(1);
							if (!StringHelper.isEmpty(s) && s.equals(code)) {
								isExists = true;
							}
							if (!StringHelper.isEmpty(s) && s.equals(myCode)) {
								myCode = getCode();
							}
						}
					}
				}

				if (!isExists) {
					code = "";
				}
				String jmPassword = UnixCrypt.crypt("888888",
						DigestUtils.sha256Hex("888888"));
				Timestamp now = new Timestamp(System.currentTimeMillis());

				// 当前注册用户ID
				String userName = getJgZhName();
				int userId = insert(connection, t6110, userName, jmPassword,
						T6110_F06.FZRR.name(), T6110_F07.QY.name(),
						T6110_F08.HTTJ.name(), now, T6110_F10.S.name());
				execute(connection, t6115, userId);
				execute(connection, t6116, userId);
				execute(connection, t6118, userId);
				execute(connection, t6141, userId);
				ConfigureProvider configureProvider = serviceResource
						.getResource(ConfigureProvider.class);
				String template = configureProvider
						.getProperty(LetterVariable.REGESTER_SUCCESS);
				Envionment envionment = configureProvider.createEnvionment();
				envionment.set("name", userName);
				int letterId = insert(connection, t6123, userId, "注册成功", now);
				execute(connection, t6124, letterId,
						StringHelper.format(template, envionment));
				for (T6101_F03 t : T6101_F03.values()) {
					execute(connection, t6101, userId, t.name(),
							getAccount(t.name(), userId),
							getAccountName(userId, connection));
				}
				execute(connection, t6310, userId);
				// 推广人ID
				int tgrID = 0;
				if (!StringHelper.isEmpty(code)) {
					String t6311 = "INSERT INTO S63.T6311 SET F02 = ?, F03 = ?";
					tgrID = getUserID(code, connection);
					execute(connection, t6311, tgrID, userId);
				}
				// 推广统计人数+1
				if (tgrID > 0) {
					execute(connection,
							"UPDATE S63.T6310 SET F02 = F02 + 1 WHERE F01 = ?",
							tgrID);
					// 填写的邀请码正确 插入的用户推广信息表
					T6110 tgrUser = getUser(tgrID, connection);
					execute(connection,
							"INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?,F04 = ?, F05 = ?",
							userId, myCode, code, tgrUser.F04, tgrUser.F02);
				} else {
					// 填写的邀请码不正确 插入的用户推广信息表
					execute(connection,
							"INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?",
							userId, myCode, code);
				}
				// 用户认证信息
				Integer[] rzxIds = getRzxID(connection);
				if (rzxIds != null) {
					for (Integer id : rzxIds) {
						execute(connection,
								"INSERT INTO S61.T6120 SET F01 = ?, F02 = ?",
								userId, id);
					}
				}
				// 用户信用档案表
				execute(connection, "INSERT INTO S61.T6144 SET F01 = ?", userId);
				// 企业联系信息
				execute(connection,
						"INSERT INTO S61.T6164 SET F01 = ?, F07 = ?, F04 = ?",
						userId, lxName, lxTel);
				// 企业介绍资料
				execute(connection, "INSERT INTO S61.T6162 SET F01 = ?", userId);
				// 企业基础信息
				try (PreparedStatement pstmt = connection
						.prepareStatement("INSERT INTO S61.T6161 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = ?, F18 = ?")) {
					pstmt.setInt(1, userId);
					pstmt.setString(2, userName);
					pstmt.setString(3, entity.F03);
					pstmt.setString(4, entity.F04);
					pstmt.setString(5, entity.F05);
					pstmt.setString(6, entity.F06);
					pstmt.setInt(7, entity.F07);
					pstmt.setBigDecimal(8, entity.F08);
					pstmt.setString(9, entity.F09);
					pstmt.setInt(10, entity.F10);
					pstmt.setString(11, entity.F11);
					String sfz = entity.F12;
					pstmt.setString(12, sfz.substring(0, 2) + "***************");
					pstmt.setString(13, StringHelper.encode(sfz));
					pstmt.setBigDecimal(14, entity.F14);
					pstmt.setBigDecimal(15, entity.F15);
					pstmt.setString(16, entity.F18);
					pstmt.execute();
				}
				execute(connection,
						"INSERT INTO S61.T6180 SET F01 = ?, F02 = ?", userId,
						jgjs);
				// 更新信用认证信息
				execute(connection,
						"UPDATE S61.T6118 SET F02=?,F03=?,F06=? WHERE F01=?",
						T6118_F02.TG, T6118_F03.TG, lxTel, userId);
				// 更新用户信息
				execute(connection, "UPDATE S61.T6110 SET F04=? WHERE F01=?",
						lxTel, userId);
				// 更新个人身份证
				execute(connection,
						"UPDATE S61.T6141 SET F02=?,F04=?,F06=?,F07=? WHERE F01=?",
						entity.F04, T6141_F04.TG, entity.F03,
						StringHelper.encode(entity.F03), userId);

				serviceResource.commit(connection);
				return userId;
			} catch (Exception e) {
				serviceResource.rollback(connection);
				throw e;
			}
		}
	}

	@Override
	public int addQyZxq(T6161 entity, String lxName, String lxTel)
			throws Throwable {

		if (isZchExist(entity.F03)) {
			throw new ParameterException("营业执照登记注册号重复");
		}
		/**
		 * 用户账号表
		 */
		String t6110 = "INSERT INTO S61.T6110 SET F02 = ?, F03 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?";
		/**
		 * 用户理财统计表
		 */
		String t6115 = "INSERT INTO S61.T6115 SET F01 = ?";
		/**
		 * 用户信用账户表
		 */
		String t6116 = "INSERT INTO S61.T6116 SET F01 = ?";
		/**
		 * 用户安全认证表
		 */
		String t6118 = "INSERT INTO S61.T6118 SET F01 = ?";
		/**
		 * 用户站内信
		 */
		String t6123 = "INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?";
		/**
		 * 用户站内信内容
		 */
		String t6124 = "INSERT INTO S61.T6124 SET F01 = ?, F02 = ?";
		/**
		 * 个人基础信息
		 */
		String t6141 = "INSERT INTO S61.T6141 SET F01 = ? ";
		/**
		 * 资金账户
		 */
		String t6101 = "INSERT INTO S61.T6101 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?";
		/**
		 * 推广奖励统计
		 */
		String t6310 = "INSERT INTO S63.T6310 SET F01 = ?";

		// 查询我的推广码
		String selectT6111 = "SELECT F02 FROM S61.T6111";
		String myCode = getCode();
		// 邀请码
		String code = "";
		boolean isExists = false;
		try (Connection connection = getConnection()) {
			try {
				serviceResource.openTransactions(connection);
				try (PreparedStatement ps = connection
						.prepareStatement(selectT6111)) {
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							String s = rs.getString(1);
							if (!StringHelper.isEmpty(s) && s.equals(code)) {
								isExists = true;
							}
							if (!StringHelper.isEmpty(s) && s.equals(myCode)) {
								myCode = getCode();
							}
						}
					}
				}

				if (!isExists) {
					code = "";
				}
				String jmPassword = UnixCrypt.crypt("888888",
						DigestUtils.sha256Hex("888888"));
				Timestamp now = new Timestamp(System.currentTimeMillis());

				// 当前注册用户ID
				String userName = getZhName();
				int userId = insert(connection, t6110, userName, jmPassword,
						T6110_F06.FZRR.name(), T6110_F07.QY.name(),
						T6110_F08.HTTJ.name(), now, T6110_F10.F.name());
				execute(connection, t6115, userId);
				execute(connection, t6116, userId);
				execute(connection, t6118, userId);
				execute(connection, t6141, userId);
				ConfigureProvider configureProvider = serviceResource
						.getResource(ConfigureProvider.class);
				String template = configureProvider
						.getProperty(LetterVariable.REGESTER_SUCCESS);
				Envionment envionment = configureProvider.createEnvionment();
				envionment.set("name", userName);
				int letterId = insert(connection, t6123, userId, "注册成功", now);
				execute(connection, t6124, letterId,
						StringHelper.format(template, envionment));
				for (T6101_F03 t : T6101_F03.values()) {
					execute(connection, t6101, userId, t.name(),
							getAccount(t.name(), userId),
							getAccountName(userId, connection));
				}
				execute(connection, t6310, userId);
				// 推广人ID
				int tgrID = 0;
				if (!StringHelper.isEmpty(code)) {
					String t6311 = "INSERT INTO S63.T6311 SET F02 = ?, F03 = ?";
					tgrID = getUserID(code, connection);
					execute(connection, t6311, tgrID, userId);
				}
				// 推广统计人数+1
				if (tgrID > 0) {
					execute(connection,
							"UPDATE S63.T6310 SET F02 = F02 + 1 WHERE F01 = ?",
							tgrID);
					// 填写的邀请码正确 插入的用户推广信息表
					T6110 tgrUser = getUser(tgrID, connection);
					execute(connection,
							"INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?,F04 = ?, F05 = ?",
							userId, myCode, code, tgrUser.F04, tgrUser.F02);
				} else {
					// 填写的邀请码不正确 插入的用户推广信息表
					execute(connection,
							"INSERT INTO S61.T6111 SET F01 = ?, F02 = ?, F03 = ?",
							userId, myCode, code);
				}
				// 用户认证信息
				Integer[] rzxIds = getRzxID(connection);
				if (rzxIds != null) {
					for (Integer id : rzxIds) {
						execute(connection,
								"INSERT INTO S61.T6120 SET F01 = ?, F02 = ?",
								userId, id);
					}
				}

				// 企业联系信息
				execute(connection,
						"INSERT INTO S61.T6164 SET F01 = ?, F07 = ?, F04 = ?",
						userId, lxName, lxTel);
				// 用户信用档案表
				execute(connection, "INSERT INTO S61.T6144 SET F01 = ?", userId);
				// 企业介绍资料
				execute(connection, "INSERT INTO S61.T6162 SET F01 = ?", userId);
				// 企业基础信息
				try (PreparedStatement pstmt = connection
						.prepareStatement("INSERT INTO S61.T6161 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = ?,F16 = ?,F17 = ?")) {
					pstmt.setInt(1, userId);
					pstmt.setString(2, userName);
					pstmt.setString(3, entity.F03);
					pstmt.setString(4, entity.F04);
					pstmt.setString(5, entity.F05);
					pstmt.setString(6, entity.F06);
					pstmt.setInt(7, entity.F07);
					pstmt.setBigDecimal(8, entity.F08);
					pstmt.setString(9, entity.F09);
					pstmt.setInt(10, entity.F10);
					pstmt.setString(11, entity.F11);
					String sfz = entity.F12;
					pstmt.setString(12, sfz.substring(0, 2) + "***************");
					pstmt.setString(13, StringHelper.encode(sfz));
					pstmt.setBigDecimal(14, entity.F14);
					pstmt.setBigDecimal(15, entity.F15);
					pstmt.setString(16, entity.F16);
					pstmt.setString(17, entity.F17);
					pstmt.execute();
				}
				// 更新信用认证信息
				execute(connection,
						"UPDATE S61.T6118 SET F02=?,F03=?,F06=? WHERE F01=?",
						T6118_F02.TG, T6118_F03.TG, lxTel, userId);
				// 更新用户信息
				execute(connection, "UPDATE S61.T6110 SET F04=? WHERE F01=?",
						lxTel, userId);
				// 更新个人身份证
				execute(connection,
						"UPDATE S61.T6141 SET F02=?,F04=?,F06=?,F07=? WHERE F01=?",
						entity.F04, T6141_F04.TG, entity.F03,
						StringHelper.encode(entity.F03), userId);

				serviceResource.commit(connection);
				return userId;
			} catch (Exception e) {
				serviceResource.rollback(connection);
				throw e;
			}
		}
	}

	// 企业组织机构代码不能重复
	@Override
	public boolean isQylxrdhExist(String F04) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S61.T6164 WHERE T6164.F04 = ? LIMIT 1")) {
				pstmt.setString(1, F04);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return true;
					}
				}
			}
			return false;
		}
	}

	/**
	 * 设置默认信用额度
	 * 
	 * @param connection
	 * @param userId
	 * @param creditAmount
	 * @throws Throwable
	 */
	private void updateUserCredit(Connection connection, int userId,
			BigDecimal creditAmount) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S61.T6116 SET F03 = ? WHERE F01 = ?")) {
			pstmt.setBigDecimal(1, creditAmount);
			pstmt.setInt(2, userId);
			pstmt.execute();
		}
		try (PreparedStatement pstmt = connection
				.prepareStatement("INSERT INTO S61.T6117 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?, F07 = ?, F08 = ?")) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, FeeCode.XY_MR);
			pstmt.setTimestamp(3, getCurrentTimestamp(connection));
			pstmt.setBigDecimal(4, creditAmount);
			pstmt.setBigDecimal(5, creditAmount);
			pstmt.setString(6, "默认信用额度");
			pstmt.execute();
		}
	}

	@Override
	public PagingResult<User> searchUsers(UserQuery userQuery, Paging paging)
			throws Throwable {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT T6110.F01 AS F01, T6110.F02 AS F02, T6110.F04 AS F03, T6110.F05 AS F04,");
		sql.append("  T6110.F06 AS F05, T6110.F07 AS F06, T6110.F08 AS F07, T6110.F09 AS F08, T6110.F10 AS F09 ");
		sql.append("  FROM S61.T6110 WHERE 1=1 AND F01 != 1 ");
		ArrayList<Object> parameters = new ArrayList<>();
		sql.append(" AND F13 = ?");
		parameters.add(T6110_F13.F);
		sql.append(" AND F01 <> ?");
		parameters.add(1);
		if (userQuery != null) {
			SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
			String userName = userQuery.userName;
			if (!StringHelper.isEmpty(userName)) {
				sql.append(" AND F02 LIKE ?");
				parameters.add(sqlConnectionProvider.allMatch(userName));
			}
			String phone = userQuery.phone;
			if (!StringHelper.isEmpty(phone)) {
				sql.append(" AND F04 LIKE ?");
				parameters.add(sqlConnectionProvider.allMatch(phone));
			}
			String eamil = userQuery.eamil;
			if (!StringHelper.isEmpty(eamil)) {
				sql.append(" AND F05 LIKE ?");
				parameters.add(sqlConnectionProvider.allMatch(eamil));
			}
			T6110_F07 status = userQuery.status;
			if (status != null) {
				sql.append(" AND F07 = ?");
				parameters.add(status.name());
			}
			T6110_F08 zcly = userQuery.zcly;
			if (zcly != null) {
				sql.append(" AND F08 = ?");
				parameters.add(zcly.name());
			}
			Timestamp startTime = userQuery.startTime;
			if (startTime != null) {
				sql.append(" AND DATE(F09) >= ?");
				parameters.add(startTime);
			}
			Timestamp endTime = userQuery.endTime;
			if (endTime != null) {
				sql.append(" AND DATE(F09) <= ?");
				parameters.add(endTime);
			}
			String zhlx = userQuery.zhlx;
			if (!StringHelper.isEmpty(zhlx)) {
				if (zhlx.equals("GR")) {
					sql.append(" AND F06 = ?");
					parameters.add(T6110_F06.ZRR.name());
				} else if (zhlx.equals("QY")) {
					sql.append(" AND F06 = ?");
					sql.append(" AND F10 = ?");
					parameters.add(T6110_F06.FZRR.name());
					parameters.add(T6110_F10.F.name());
				} else if (zhlx.equals("JG")) {
					sql.append(" AND F06 = ?");
					sql.append(" AND F10 = ?");
					parameters.add(T6110_F06.FZRR.name());
					parameters.add(T6110_F10.S.name());
				}
			}
		}
		sql.append(" ORDER BY T6110.F09 DESC");
		try (Connection connection = getConnection()) {
			return selectPaging(connection, new ArrayParser<User>() {

				@Override
				public User[] parse(ResultSet resultSet) throws SQLException {
					ArrayList<User> list = null;
					while (resultSet.next()) {
						User record = new User();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						record.F03 = resultSet.getString(3);
						record.F04 = resultSet.getString(4);
						record.F05 = T6110_F06.parse(resultSet.getString(5));
						record.F06 = T6110_F07.parse(resultSet.getString(6));
						record.F07 = T6110_F08.parse(resultSet.getString(7));
						record.F08 = resultSet.getTimestamp(8);
						record.F09 = T6110_F10.parse(resultSet.getString(9));
						record.isYzyq = isYzyq(connection, record.F01);
						record.isSafe = isSafe(connection, record.F01);
						record.isYq = isYq(connection, record.F01);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
					return ((list == null || list.size() == 0) ? null : list
							.toArray(new User[list.size()]));
				}
			}, paging, sql.toString(), parameters);
		}
	}

	@Override
	public int replaceEmployNum(String employeeNumNew, String employeeNumOld)
			throws Throwable {
		if (StringHelper.isEmpty(employeeNumNew)) {
			throw new ParameterException("被替换的业务工号不能为空");
		}
		if (StringHelper.isEmpty(employeeNumOld)) {
			throw new ParameterException("替换的业务工号不能为空");
		}
		if (StringHelper.isEmpty(checkNumExist(employeeNumNew))) {
			throw new ParameterException("被替换的业务工号不存在");
		}
		if (StringHelper.isEmpty(checkNumExist(employeeNumOld))) {
			throw new ParameterException("替换的业务工号不存在");
		}

		int inta = 0;
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("UPDATE S61.T6110 SET F14 = ? WHERE F14 = ? ")) {
				pstmt.setString(1, employeeNumNew.toUpperCase());
				pstmt.setString(2, employeeNumOld);
				pstmt.execute();

			}
			inta = 1;

		}
		return inta;
	}

	@Override
	public String checkNumExist(String code) throws Throwable {
		try (Connection connection = getConnection()) {
			if (!StringHelper.isEmpty(code)) {
				String selectT6111 = "SELECT T7110.F04 FROM S71.T7110 WHERE T7110.F12 = ? AND T7110.F05 = 'QY' LIMIT 1";
				try (PreparedStatement pstmt = connection
						.prepareStatement(selectT6111)) {
					pstmt.setString(1, code);
					try (ResultSet rs = pstmt.executeQuery()) {
						if (rs.next()) {
							return rs.getString(1);
						}
						return null;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 申请担保人
	 *
	 * @param t6125
	 * @return
	 * @throws Throwable
	 */
	private String applyGuarantor(Connection connection, T6125 t6125)
			throws Throwable {
		String code = "";
		try (PreparedStatement pstmt = connection
				.prepareStatement(" SELECT MAX(F03) FROM S61.T6125 ")) {
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					code = rs.getString(1);
				}
			}
		}
		if (StringHelper.isEmpty(code)) {
			code = "DBM0001";
		} else {
			code = "DBM"
					+ String.format("%04d", IntegerParser.parse(code.substring(
							3, code.length())) + 1);
		}
		t6125.F03 = code;
		insert(connection,
				"INSERT INTO S61.T6125 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ? ",
				t6125.F02, t6125.F03, t6125.F04, t6125.F05.name(), t6125.F06,
				t6125.F07, t6125.F08);
		try (PreparedStatement pstmt = connection
				.prepareStatement("INSERT INTO S61.T6126 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?, F07 = ?, F08 = ?")) {
			pstmt.setInt(1, t6125.F02);
			pstmt.setInt(2, FeeCode.DB_MR);
			pstmt.setTimestamp(3, getCurrentTimestamp(connection));
			pstmt.setBigDecimal(4, t6125.F04);
			pstmt.setBigDecimal(5, t6125.F04);
			pstmt.setString(6, "默认担保额度");
			pstmt.execute();
		}
		return code;
	}

	private String getSexNotDecode(String sfzh) {
		if (StringHelper.isEmpty(sfzh)) {
			return null;
		}
		String sex = null;
		try {
			if (Integer.parseInt(sfzh.substring(sfzh.length() - 2,
					sfzh.length() - 1)) % 2 != 0) {
				sex = T6141_F09.M.name();
			} else {
				sex = T6141_F09.F.name();
			}
		} catch (Throwable throwable) {
			logger.error("SafetyManageImpl.getSexNotDecode error", throwable);
		}
		return sex;
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

	@Override
	public List<User> SynchronizeUserCode() throws Throwable {
        try (Connection connection = getConnection())
        {
            List<User> userList = new ArrayList<User>();
            String sql =
                "SELECT T6110.F01 FROM S61.T6141 LEFT JOIN S61.T6110 ON T6110.F01 = T6141.F01 WHERE T6110.F20 IS NULL AND T6110.F04 IS NOT NULL AND T6110.F06 = 'ZRR' "; //企业用户不能直接同步
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                try (ResultSet rs = pstmt.executeQuery())
                {
                    User t6110 = null;
                    while (rs.next())
                    {
                        t6110 = new User();
                        t6110.F01 = rs.getInt(1);
                        userList.add(t6110);
                    }
                }
            }
            return userList;
        }
    }

	@Override
	public T6110 getT6110(int userId) throws Throwable {
        try (Connection connection = getConnection())
        {
            T6110 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13,F19,F20 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
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
                        record.F11 = resultSet.getShort(11);
                        record.F12 = T6110_F12.parse(resultSet.getString(12));
                        record.F13 = T6110_F13.parse(resultSet.getString(13));
                        record.F19 = T6110_F19.parse(resultSet.getString(14));
                        record.F20 = resultSet.getString(15);
                    }
                }
            }
            return record;
        }
    }

	@Override
	public T6110 getT6110ByCusNo(String fddCusNo) throws Throwable {
        try (Connection connection = getConnection())
        {
            T6110 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13,F20 FROM S61.T6110 WHERE T6110.F20 = ? LIMIT 1"))
            {
                pstmt.setString(1, fddCusNo);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
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
                        record.F11 = resultSet.getShort(11);
                        record.F12 = T6110_F12.parse(resultSet.getString(12));
                        record.F13 = T6110_F13.parse(resultSet.getString(13));
                        record.F20 = resultSet.getString("F20");
                    }
                }
            }
            return record;
        }
    }

	@Override
	public void updateFddCusNo(int userId, String cusNo) throws Throwable {
        
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6110 SET F20=? WHERE T6110.F01 = ?"))
            {
                pstmt.setString(1, cusNo);
                pstmt.setInt(2, userId);
                pstmt.execute();
            }
        }
    }
	
}
