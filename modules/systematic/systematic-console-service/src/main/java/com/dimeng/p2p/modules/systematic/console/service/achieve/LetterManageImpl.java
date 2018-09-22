package com.dimeng.p2p.modules.systematic.console.service.achieve;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.enums.T7160_F07;
import com.dimeng.p2p.common.enums.LetterStatus;
import com.dimeng.p2p.common.enums.SendType;
import com.dimeng.p2p.modules.systematic.console.service.LetterManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.Letter;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

public class LetterManageImpl extends AbstractSystemService implements LetterManage {
	
	public static class LetterManageFactory implements ServiceFactory<LetterManage> {
		@Override
		public LetterManage newInstance(ServiceResource serviceResource) {
			return new LetterManageImpl(serviceResource);
		}
	}

	public LetterManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public int addLetter(T7160_F07 sendType, String title, String content, String... userNames) throws Throwable {
		try (Connection connection = getConnection()) {
			if (T7160_F07.SY == sendType) {
				// 查询所有用户账号
				userNames = getUserNames(connection);
			}
			if (userNames == null || userNames.length == 0) {
				throw new ParameterException("没有指定用户账号信息.");
			}
			/**
			 * 站内信推广
			 */
			String t7160 = "INSERT INTO S71.T7160 SET F02=?,F03=?,F04=?,F05=?,F06=?,F07=?";
			/**
			 * 站内信推广指定人
			 */
			String t7161 = "INSERT INTO S71.T7161 SET F02=?,F03=?";
			/**
			 * 站内信
			 */
			String t6123 = "INSERT INTO S61.T6123 SET F02=?,F03=?,F04=?,F05=?";
			/**
			 * 站内信内容
			 */
			String t6124 = "INSERT INTO S61.T6124 SET F01=?,F02=?";
			Timestamp now = new Timestamp(System.currentTimeMillis());
			List<String> userLists = new ArrayList<>();
			for (String s : userNames) {
				if (!StringHelper.isEmpty(s) && getUserId(s, connection) > 0) {
					userLists.add(s);
				}
			}

			int letterId = 0;
			try {
				serviceResource.openTransactions(connection);
				letterId = insert(connection, t7160, title, content, userLists.size(), serviceResource.getSession().getAccountId(), now, sendType);
				for (String userName : userLists) {
					// 发送站内信
					int userId = getUserId(userName, connection);
					if (userId > 0) {
						execute(connection, t7161, letterId, userName);
						int id = insert(connection, t6123, userId, title, now, LetterStatus.WD);
						execute(connection, t6124, id, content);
					}
				}
				serviceResource.commit(connection);
				return letterId;
			} catch (Exception e) {
				serviceResource.rollback(connection);
				throw e;
			}
		}
	}

	private int getUserId(String userName, Connection connection) throws Throwable {
		String sql = "SELECT F01 FROM S61.T6110 WHERE F02=?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, userName);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return 0;
	}

	@Override
	public int getCheckUserId(String userName) throws Throwable {
		String sql = "SELECT F01 FROM S61.T6110 WHERE F02=?";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setString(1, userName);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			}
		}
		return 0;
	}

	private String[] getUserNames(Connection connection) throws Throwable {
		List<String> userNames = new ArrayList<>();
		String sql = "SELECT F02 FROM S61.T6110 WHERE F01 <> 1";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					if (!StringHelper.isEmpty(rs.getString(1))) {
						userNames.add(rs.getString(1));
					}
				}
			}
		}
		return userNames.toArray(new String[userNames.size()]);
	}

	@Override
	public PagingResult<Letter> serarch(String title, String name, String beginTime, String endTime, Paging paging)
			throws Throwable {
		String sql = "SELECT T7160.F01,T7160.F02,T7160.F03,T7160.F04,T7160.F06,T7160.F07,T7110.F02 AS ACCOUNTNAME FROM S71.T7160 INNER JOIN S71.T7110 ON T7160.F05=T7110.F01 WHERE 1=1";
		StringBuilder sb = new StringBuilder();
		sb.append(sql);
		List<Object> parameters = new ArrayList<>();
		if (!StringHelper.isEmpty(title)) {
			sb.append(" AND T7160.F02 LIKE ?");
			parameters.add(getSQLConnectionProvider().allMatch(title));
		}
		if (!StringHelper.isEmpty(name)) {
			sb.append(" AND T7110.F02 LIKE ?");
			parameters.add(getSQLConnectionProvider().allMatch(name));
		}
		if (!StringHelper.isEmpty(beginTime)) {
			sb.append(" AND DATE(T7160.F06)>=?");
			parameters.add(beginTime);
		}
		if (!StringHelper.isEmpty(endTime)) {
			sb.append(" AND DATE(T7160.F06)<=?");
			parameters.add(endTime);
		}
		sb.append(" ORDER BY T7160.F06 DESC");
		try (Connection connection = getConnection()) {
			return selectPaging(connection, new ArrayParser<Letter>() {

				@Override
				public Letter[] parse(ResultSet resultSet) throws SQLException {
					List<Letter> letters = new ArrayList<>();
					while (resultSet.next()) {
						Letter letter = new Letter();
						letter.id = resultSet.getInt(1);
						letter.title = resultSet.getString(2);
						letter.content = resultSet.getString(3);
						letter.count = resultSet.getInt(4);
						letter.createTime = resultSet.getTimestamp(5);
						letter.sendType = EnumParser.parse(SendType.class, resultSet.getString(6));
						letter.name = resultSet.getString(7);
						letters.add(letter);
					}
					return letters.toArray(new Letter[letters.size()]);
				}
			}, paging, sb.toString(), parameters);
		}
	}

	@Override
	public Letter get(int id) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("指定的站内信推广ID不存在.");
		}
		String sql = "SELECT T7160.F01,T7160.F02,T7160.F03,T7160.F04,T7160.F06,T7160.F07,T7110.F02 AS ACCOUNTNAME FROM S71.T7160 INNER JOIN S71.T7110 ON T7160.F05=T7110.F01 WHERE T7160.F01=?";
		try (Connection connection = getConnection()) {
			return select(connection, new ItemParser<Letter>() {

				@Override
				public Letter parse(ResultSet resultSet) throws SQLException {
					Letter letter = new Letter();
					if (resultSet.next()) {
						letter.id = resultSet.getInt(1);
						letter.title = resultSet.getString(2);
						letter.content = resultSet.getString(3);
						letter.count = resultSet.getInt(4);
						letter.createTime = resultSet.getTimestamp(5);
						letter.sendType = EnumParser.parse(SendType.class, resultSet.getString(6));
						letter.name = resultSet.getString(7);
						letter.userNames = getSendName(letter.id);
					}
					return letter;
				}
			}, sql, id);
		}
	}

	private String[] getSendName(int letterId) {
		if (letterId <= 0) {
			throw new ParameterException("指定的站内信推广ID不存在.");
		}
		String sql = "SELECT F03 FROM S71.T7161 WHERE F02=?";
		try {
			try (Connection connection = getConnection()) {
				return selectAll(connection, new ArrayParser<String>() {

					@Override
					public String[] parse(ResultSet resultSet) throws SQLException {
						List<String> lists = new ArrayList<>();
						while (resultSet.next()) {
							lists.add(resultSet.getString(1));
						}
						return lists.toArray(new String[lists.size()]);
					}
				}, sql, letterId);
			}
		} catch (Throwable e) {
			logger.error(e, e);
		}
		return null;
	}

	@Override
	public String[] importUser(InputStream inputStream, String charset) throws Throwable {
		if (inputStream == null) {
			throw new ParameterException("读取文件流不存在.");
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		List<String> userNames = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				userNames.add(line);
			}
		}
		return userNames.toArray(new String[userNames.size()]);
	}

	@Override
	public void export(OutputStream outputStream, String charset, int id) throws Throwable {
		if (outputStream == null) {
			throw new ParameterException("写入文件流不存在.");
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		String[] accounts = getSendName(id);
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset))) {
			for (String account : accounts) {
				if (StringHelper.isEmpty(account)) {
					continue;
				}
				writer.write(account);
				writer.newLine();
			}
		}
	}
}
