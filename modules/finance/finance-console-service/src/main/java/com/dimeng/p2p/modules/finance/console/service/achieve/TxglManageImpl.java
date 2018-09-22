package com.dimeng.p2p.modules.finance.console.service.achieve;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.enums.T5020_F03;
import com.dimeng.p2p.common.enums.LetterStatus;
import com.dimeng.p2p.common.enums.PlatformFundType;
import com.dimeng.p2p.common.enums.TradingType;
import com.dimeng.p2p.common.enums.WithdrawStatus;
import com.dimeng.p2p.modules.finance.console.service.TxglManage;
import com.dimeng.p2p.modules.finance.console.service.entity.Bank;
import com.dimeng.p2p.modules.finance.console.service.entity.Txgl;
import com.dimeng.p2p.modules.finance.console.service.entity.TxglRecord;
import com.dimeng.p2p.modules.finance.console.service.query.TxglRecordQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.TimestampParser;

public class TxglManageImpl extends AbstractFinanceService implements
		TxglManage {

	public static class ExtractionManageFactory implements
			ServiceFactory<TxglManage> {

		@Override
		public TxglManage newInstance(ServiceResource serviceResource) {
			return new TxglManageImpl(serviceResource);
		}
	}

	/**
	 * 发站内信
	 */
	private static final String t6035 = "INSERT INTO T6035 SET F02=?,F03=?,F04=?,F05=?,F06=?";
	/**
	 * 站内信内容
	 */
	private static final String t6046 = "INSERT INTO T6046 SET F01=?,F02=?";

	public TxglManageImpl(ServiceResource serviceResource) {
		super(serviceResource);

	}

	@Override
	public Txgl getExtractionInfo() throws Throwable {
		Txgl extraction = new Txgl();
		try (Connection conn = getConnection()) {
			try (PreparedStatement pst = conn
					.prepareStatement("SELECT F01, F02, F03 FROM T7023 ORDER BY F03 DESC LIMIT 1")) {
				try (ResultSet rst = pst.executeQuery()) {
					if (rst.next()) {
						extraction.totalAmount = rst.getBigDecimal(1);
						extraction.charge = rst.getBigDecimal(2);
						extraction.updateTime = rst.getTimestamp(3);
					}
				}
			}
		}
		return extraction;
	}

	@Override
	public PagingResult<TxglRecord> search(TxglRecordQuery query, Paging paging)
			throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT T1.F01, T2.F02, T3.F06 AS F07, T5.F02 AS BANK, T4.F04, T4.F05, T4.F06 AS F15, T1.F04 AS F16, T1.F06, T1.F08, T1.F12, T1.F10, T1.F07 AS STATUS,T2.F01 AS USERID,T1.F03 AS CREATETIME,T1.F11,T1.F09 FROM T6034 T1");
		sql.append(" INNER JOIN S60.T6010 T2 ON T1.F02 = T2.F01");
		sql.append(" INNER JOIN S60.T6011 T3 ON T1.F02 = T3.F01");
		sql.append(" INNER JOIN S60.T6024 T4 ON T1.F05 = T4.F01");
		sql.append(" INNER JOIN S50.T5020 T5 ON T4.F03=T5.F01 WHERE 1=1");
		ArrayList<Object> parameters = new ArrayList<>();
		if (query != null) {
			SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
			int lsj = query.getLsh();
			String yhm = query.getYhm();
			String xm = query.getXm();
			String yhkh = query.getYhkh();
			WithdrawStatus status = query.getStatus();
			if (lsj > 0) {
				sql.append(" AND T1.F01 = ?");
				parameters.add(lsj);
			}
			if (!StringHelper.isEmpty(yhm)) {
				sql.append(" AND  T2.F02 LIKE ?");
				parameters.add(sqlConnectionProvider.allMatch(yhm));
			}
			if (!StringHelper.isEmpty(xm)) {
				sql.append(" AND  T3.F06 LIKE ?");
				parameters.add(sqlConnectionProvider.allMatch(xm));
			}
			if (!StringHelper.isEmpty(yhkh)) {
				sql.append(" AND T4.F06 LIKE ?");
				parameters.add(sqlConnectionProvider.allMatch(yhkh));
			}
			int bankId = query.getBankId();
			if (bankId > 0) {
				sql.append(" AND T4.F03 = ?");
				parameters.add(bankId);
			}
			Timestamp datetime = query.getStartExtractionTime();
			if (datetime != null) {
				if (status == WithdrawStatus.WSH) {
					sql.append(" AND DATE(T1.F03)>=?");
				} else if (status == WithdrawStatus.SHTG
						|| status == WithdrawStatus.SHSB) {
					sql.append(" AND DATE(T1.F08) >= ?");
				} else if (status == WithdrawStatus.TXCG
						|| status == WithdrawStatus.TXSB) {
					sql.append(" AND DATE(T1.F12) >= ?");
				}
				parameters.add(datetime);
			}
			datetime = query.getEndExtractionTime();
			if (datetime != null) {
				if (status == WithdrawStatus.WSH) {
					sql.append(" AND DATE(T1.F03)<=?");
				} else if (status == WithdrawStatus.SHTG
						|| status == WithdrawStatus.SHSB) {
					sql.append(" AND DATE(T1.F08) <= ?");
				} else if (status == WithdrawStatus.TXCG
						|| status == WithdrawStatus.TXSB) {
					sql.append(" AND DATE(T1.F12) <= ?");
				}
				parameters.add(datetime);
			}
			if (status != null) {
				sql.append(" AND T1.F07=?");
				parameters.add(status);
			}
			if (status == WithdrawStatus.WSH) {
				sql.append(" ORDER BY T1.F03 DESC");
			} else if (status == WithdrawStatus.SHTG
					|| status == WithdrawStatus.SHSB) {
				sql.append(" ORDER BY T1.F08 DESC");
			} else if (status == WithdrawStatus.TXCG) {
				sql.append(" ORDER BY T1.F12 DESC");
			}
		}
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectPaging(connection,
					new ArrayParser<TxglRecord>() {
						ArrayList<TxglRecord> list = new ArrayList<TxglRecord>();

						@Override
						public TxglRecord[] parse(ResultSet rst)
								throws SQLException {
							while (rst.next()) {
								TxglRecord user = new TxglRecord();
								user.id = rst.getInt(1);
								user.loginName = rst.getString(2);
								user.userName = rst.getString(3);
								user.extractionBank = rst.getString(4);
								user.location = getRegion(rst.getInt(5));
								user.subbranch = rst.getString(6);
								user.bankId = rst.getString(7);
								user.extractionAmount = rst.getBigDecimal(8);
								user.charge = rst.getBigDecimal(9);
								user.checkDateTime = rst.getTimestamp(10);
								user.applyDateTime = rst.getTimestamp(11);
								user.approver = getName(rst.getInt(12));
								user.status = EnumParser.parse(
										WithdrawStatus.class, rst.getString(13));
								user.userId = rst.getInt(14);
								user.createTime = rst.getTimestamp(15);
								user.loan = getName(rst.getInt(16));
								user.remark = rst.getString(17);
								list.add(user);
							}
							return list.toArray(new TxglRecord[list.size()]);
						}
					}, paging, sql.toString(), parameters);
		}
	}

	/**
	 * 查询区域名称
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	private String getRegion(int id) throws SQLException {
		if (id <= 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		try (Connection connection = getConnection(P2PConst.DB_INFORMATION)) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F06,F07,F08 FROM T5019 WHERE F01=?")) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						sb.append(rs.getString(1));
						sb.append(",");
						sb.append(rs.getString(2));
						sb.append(",");
						sb.append(rs.getString(3));
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 查询审核人
	 * 
	 * @param userId
	 * @return
	 */
	private String getName(int id) throws SQLException {
		try(Connection connection = getConnection())
		{
			try (PreparedStatement pstmt = connection.prepareStatement(
					"SELECT F02 FROM T7011 WHERE F01=?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
		}
		return "";
	}

	@Override
	public TxglRecord get(int id) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("指定的记录不存在.");
		}
		StringBuilder sql = new StringBuilder(
				"SELECT T1.F01 AS F01, T2.F02 AS F02, T3.F06 AS F03, T5.F02 AS F04, T4.F04 AS F05, T4.F05 AS F06, T4.F06 AS F07, T1.F04 AS F08, T1.F06 AS F09, T1.F08 AS F10, T1.F12 AS F11, T1.F10 AS F12, T1.F07 AS F13,T2.F01 AS F14,T1.F03 AS F15,T1.F11 AS F16,T1.F09 AS F12 FROM T6034 T1");
		sql.append(" INNER JOIN T6010 T2 ON T1.F02 = T2.F01");
		sql.append(" INNER JOIN T6011 T3 ON T1.F02 = T3.F01");
		sql.append(" LEFT JOIN T6024 T4 ON T1.F05 = T4.F01");
		sql.append(" LEFT JOIN S50.T5020 T5 ON T4.F03=T5.F01");
		sql.append(" WHERE T1.F01=?");
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return select(connection,
					new ItemParser<TxglRecord>() {
						@Override
						public TxglRecord parse(ResultSet rst) throws SQLException {
							TxglRecord user = new TxglRecord();
							if (rst.next()) {
								user.id = rst.getInt(1);
								user.loginName = rst.getString(2);
								user.userName = rst.getString(3);
								user.extractionBank = rst.getString(4);
								user.location = getRegion(rst.getInt(5));
								user.subbranch = rst.getString(6);
								user.bankId = rst.getString(7);
								user.extractionAmount = rst.getBigDecimal(8);
								user.charge = rst.getBigDecimal(9);
								user.checkDateTime = rst.getTimestamp(10);
								user.applyDateTime = rst.getTimestamp(11);
								user.approver = getName(rst.getInt(12));
								user.status = EnumParser.parse(
										WithdrawStatus.class, rst.getString(13));
								user.userId = rst.getInt(14);
								user.createTime = rst.getTimestamp(15);
								user.loan = getName(rst.getInt(16));
								user.remark = rst.getString(17);
							}
							return user;
						}
					}, sql.toString(), id);
		}
	}

	/**
	 * 更新对象记录
	 * 
	 * @param id
	 * @param status
	 * @param check_reason
	 * @throws Throwable
	 */
    private void update(int id, WithdrawStatus status, String check_reason, Connection connection)
			throws Throwable {
		TxglRecord txglRecord = get(id);
		switch (status) {
            case SHTG:
                break;
            case SHSB:
                update2(check_reason, txglRecord, connection);
                break;
            case TXCG:
                update3(id, check_reason, txglRecord, connection);
                break;
            case TXSB:
                update4(check_reason, txglRecord, connection);
                break;
            default:
                return;
		}
	}

	/**
	 * 审核失败
	 * 
	 * @param check_reason
	 * @param txglRecord
	 * @throws SQLException
	 * @throws ResourceNotFoundException
	 */
    private void update2(String check_reason, TxglRecord txglRecord, Connection connection)
			throws Throwable {
		if (txglRecord == null) {
			throw new ParameterException("提现记录不存在");
		}
		BigDecimal lock = txglRecord.extractionAmount.add(txglRecord.charge);
		String select = "SELECT F04 FROM T6023 WHERE F01=? FOR UPDATE";
        BigDecimal djAmount = selectBigDecimal(connection, select, txglRecord.userId);
		if (djAmount.compareTo(lock) < 0) {
			lock = djAmount;
		}
		String update = "UPDATE T6023 SET F04=F04-?,F05=F05+? WHERE F01=?";
        execute(connection, update, lock, lock, txglRecord.userId);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		// 发站内信
		ConfigureProvider configureProvider = serviceResource
				.getResource(ConfigureProvider.class);
		String template = configureProvider.getProperty(LetterVariable.TX_SB);
		Envionment envionment = configureProvider.createEnvionment();
		envionment.set("name", txglRecord.loginName);
		envionment.set("datetime", TimestampParser.format(now));
		envionment.set("amount", txglRecord.extractionAmount.toString());
        int letterId =
            insert(connection,
                t6035,
                now,
				LetterVariable.TX_SB.getDescription(), "", txglRecord.userId,
				LetterStatus.WD);
        execute(connection, t6046, letterId,
				StringHelper.format(template, envionment));
	}

	/**
	 * 提现成功
	 * 
	 * @param id
	 * @param check_reason
	 * @param txglRecord
	 */
    private void update3(int id, String check_reason, TxglRecord txglRecord, Connection connection)
			throws Throwable {
		if (txglRecord == null) {
			throw new ParameterException("提现记录不存在");
		}
		// 查询用户资金总额
		String select = "SELECT F03 FROM T6023 WHERE F01=? FOR UPDATE";
		String selectT6023 = "SELECT F04 FROM T6023 WHERE F01=?";
		String selectT7023 = "SELECT F01 FROM T7023 FOR UPDATE";
		String selectT7025 = "SELECT F01 FROM T7025 FOR UPDATE";
		BigDecimal total = selectBigDecimal(connection, select, txglRecord.userId);
        BigDecimal djAmount = selectBigDecimal(connection, selectT6023, txglRecord.userId);
		selectBigDecimal(connection, selectT7023);
		// 查询平台资金余额
		BigDecimal sysBalance = selectBigDecimal(connection, selectT7025);
		// 锁定金额
		BigDecimal lock = txglRecord.extractionAmount.add(txglRecord.charge);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if (djAmount.compareTo(lock) < 0) {
			lock = djAmount;
		}
		// 提现成功更新用户帐户总表
        execute(connection,
				"UPDATE T6023 SET F03=F03-?,F04=F04-? WHERE F01=?", lock, lock,
				txglRecord.userId);
		// 添加用户资金交易记录
		String t6032 = "INSERT INTO T6032 SET F02=?,F03=?,F04=?,F05=?,F06=?,F07=?,F08=?,F09=?";
        execute(connection,
            t6032,
            txglRecord.userId,
            TradingType.CGTX,
            now,
            0,
            txglRecord.extractionAmount,
            total.subtract(txglRecord.extractionAmount),
            id,
            TradingType.CGTX.getName());
		now = new Timestamp(System.currentTimeMillis());
        execute(connection,
            t6032,
            txglRecord.userId,
				TradingType.TXSXF, now, 0, txglRecord.charge,
				total.subtract(lock), id, TradingType.TXSXF.getName());
		// 发站内信
		ConfigureProvider configureProvider = serviceResource
				.getResource(ConfigureProvider.class);
		String template = configureProvider.getProperty(LetterVariable.TX_CG);
		Envionment envionment = configureProvider.createEnvionment();
		envionment.set("datetime", TimestampParser.format(now));
        envionment.set("amount", txglRecord.extractionAmount.add(txglRecord.charge).toString());
        envionment.set("poundage", txglRecord.charge.toString());
        int letterId =
            insert(connection,
                t6035,
                now,
				LetterVariable.TX_CG.getDescription(), "", txglRecord.userId,
				LetterStatus.WD);
        execute(connection, t6046, letterId,
				StringHelper.format(template, envionment));
		// 更新平台资金账户
		String updateT7025 = "UPDATE T7025 SET F01=F01+?,F03=F03+?,F04=F04+?";
        execute(connection, updateT7025, txglRecord.charge,
				txglRecord.charge, txglRecord.charge);
		// 添加平台资金交易记录
		String t7026 = "INSERT INTO T7026 SET F02=?,F03=?,F04=?,F05=?,F06=?,F07=?,F08=?";
        execute(connection,
            t7026,
            now,
            txglRecord.charge,
            0,
            sysBalance.add(txglRecord.charge),
            PlatformFundType.TXSXF,
            "提现审核通过扣除手续费",
            id);
		// 更新提现统计
		String updateT7023 = "UPDATE T7023 SET F01=F01+?,F02=F02+?,F03=?";
        execute(connection, updateT7023, txglRecord.extractionAmount, txglRecord.charge, now);
	}

	/**
	 * 提现失败
	 * 
	 * @param check_reason
	 * @param txglRecord
	 * @throws SQLException
	 */
    private void update4(String check_reason, TxglRecord txglRecord, Connection connection)
			throws SQLException {
		if (txglRecord == null) {
			throw new ParameterException("提现记录不存在");
		}
		String select = "SELECT F04 FROM T6023 WHERE F01=? FOR UPDATE";
        BigDecimal djAmount = selectBigDecimal(connection, select, txglRecord.userId);
		// 锁定金额
		BigDecimal lock = txglRecord.extractionAmount.add(txglRecord.charge);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if (djAmount.compareTo(lock) < 0) {
			lock = djAmount;
		}
		String t6023 = "UPDATE T6023 SET F04=F04-?,F05=F05+? WHERE F01=?";
        execute(connection, t6023, lock, lock, txglRecord.userId);
		// 发站内信
		ConfigureProvider configureProvider = serviceResource
				.getResource(ConfigureProvider.class);
		String template = configureProvider.getProperty(LetterVariable.TX_SB);
		Envionment envionment = configureProvider.createEnvionment();
		envionment.set("name", txglRecord.loginName);
		envionment.set("datetime", TimestampParser.format(now));
		envionment.set("amount", txglRecord.extractionAmount.toString());
        int letterId =
            insert(connection,
                t6035,
                now,
				LetterVariable.TX_SB.getDescription(), "", txglRecord.userId,
				LetterStatus.WD);
        execute(connection, t6046, letterId,
				StringHelper.format(template, envionment));
	}

	@Override
	public void check(WithdrawStatus status, String check_reason, int id)
			throws Throwable {
		if (id <= 0) {
			throw new ParameterException("指定的记录不存在.");
		}
		try(Connection connection = getConnection())
		{
			if (checkStatus(status, id, connection))
			{
				return;
			}

			try{
				serviceResource.openTransactions(connection);

				String update1 = "UPDATE S60.T6034 SET F07=?,F08=?,F09=?,F10=? WHERE F01=?";
				String update2 = "UPDATE S60.T6034 SET F07=?,F09=?,F11=?,F12=? WHERE F01=?";
				if (status == WithdrawStatus.SHSB || status == WithdrawStatus.SHTG)
				{
					execute(connection,
							update1,
							status,
							new Timestamp(System.currentTimeMillis()),
							check_reason,
							serviceResource.getSession().getAccountId(),
							id);
				}
				else if (status == WithdrawStatus.TXCG || status == WithdrawStatus.TXSB)
				{
					execute(connection,
							update2,
							status,
							check_reason,
							serviceResource.getSession().getAccountId(),
							new Timestamp(System.currentTimeMillis()),
							id);
				}
				update(id, status, check_reason, connection);
				serviceResource.commit(connection);
			}
			catch (Exception e)
			{
				serviceResource.rollback(connection);
				throw e;
			}
		}
	}

    private boolean checkStatus(WithdrawStatus status, int id, Connection connection)
        throws Throwable
    {
		String select = "SELECT F07 FROM T6034 WHERE F01=? FOR UPDATE";
		WithdrawStatus oldStatus = null;
        try (PreparedStatement ps = connection.prepareStatement(select))
        {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    oldStatus = EnumParser.parse(WithdrawStatus.class, rs.getString(1));
				}
			}
		}
		if (oldStatus == null) {
			return true;
		}
		if ((status == WithdrawStatus.SHTG && oldStatus != WithdrawStatus.WSH)
				|| (status == WithdrawStatus.SHSB && oldStatus != WithdrawStatus.WSH)) {
			return true;
		}
		if ((status == WithdrawStatus.TXCG && oldStatus != WithdrawStatus.SHTG)
				|| (status == WithdrawStatus.TXSB && oldStatus != WithdrawStatus.SHTG)) {
			return true;
		}
		return false;
	}

	@Override
	public void checkBatch(WithdrawStatus status, String check_reason,
			int... ids) throws Throwable {

		if (ids == null || ids.length == 0) {
			throw new ParameterException("指定的记录不存在.");
		}
		try(Connection connection = getConnection())
		{
			try
			{
				serviceResource.openTransactions(connection);
				String update = "UPDATE S60.T6034 SET F07=?,F08=?,F09=?,F10=? WHERE F01=?";
				for (int id : ids)
				{
					if (id <= 0)
					{
						continue;
					}
					if (checkStatus(status, id, connection))
					{
						continue;
					}
					execute(connection,
							update,
							status,
							new Timestamp(System.currentTimeMillis()),
							check_reason,
							serviceResource.getSession().getAccountId(),
							id);
					update(id, status, check_reason, connection);
				}
				serviceResource.commit(connection);
			}
			catch (Exception e)
			{
				serviceResource.rollback(connection);
				throw e;
			}
		}

	}

	@Override
    public void importData(InputStream inputStream, String real_name,
			String charset) throws Throwable {
		// if (inputStream == null) {
		// return;
		// }
		// if (charset == null) {
		// charset = "GBK";
		// }
		// PreparedStatement ps = null;
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		// try (BufferedReader reader = new BufferedReader(new
		// InputStreamReader(
		// inputStream, charset))) {
		// String line = "";
		// int i = 0;
		// while ((line = reader.readLine()) != null) {
		// if (i > 0 && line.indexOf(",") != -1) {
		// String[] split = line.split(",");
		// int id = 0;
		// int status = 0;
		// String withdraw_reason = "";
		// if (split.length > 0) {
		// id = IntegerParser.parse(split[0]);
		// }
		// if (!isExists(id)) {
		// continue;
		// }
		// if (getStatus(id) != 1) {
		// continue;
		// }
		// if (split.length > 10) {
		// if (StringHelper.isEmpty(split[10])) {
		// continue;
		// }
		// status = split[10].equals("1") ? 3 : 4;
		// }
		// if (split.length > 11) {
		// if (!StringHelper.isEmpty(split[11])) {
		// withdraw_reason = split[11];
		// }
		// }
		// ps = getConnection()
		// .prepareStatement(
		// "UPDATE withdraw SET status=?,withdraw_reason=?,lastupdate=?,withdraw_time=?,financial=? WHERE id=?");
		// ps.setInt(1, status);
		// ps.setString(2, withdraw_reason);
		// ps.setTimestamp(3, now);
		// ps.setTimestamp(4, now);
		// ps.setInt(5, accountId);
		// ps.setInt(6, id);
		// ps.executeUpdate();
		// update(id, status, withdraw_reason, real_name);
		// }
		// i++;
		// }
		// }
	}

	@Override
	public void export(TxglRecord[] txglRecords, OutputStream outputStream,
			String charset) throws Throwable {
		if (outputStream == null) {
			return;
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		if (txglRecords == null) {
			return;
		}
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			writer.write("流水号");
			writer.write(",");
			writer.write("用户名");
			writer.write(",");
			writer.write("真实姓名");
			writer.write(",");
			writer.write("提现银行");
			writer.write(",");
			writer.write("所在地");
			writer.write(",");
			writer.write("所在支行");
			writer.write(",");
			writer.write("银行卡号");
			writer.write(",");
			writer.write("提现金额");
			writer.write(",");
			writer.write("手续费");
			writer.write(",");
			writer.write("审核时间");
			writer.write(",");
			writer.write("提现结果");
			writer.write(",");
			writer.write("提现失败原因");
			writer.newLine();
			for (TxglRecord txglRecord : txglRecords) {
				if (txglRecord == null) {
					continue;
				}
				writer.write(Integer.toString(txglRecord.id));
				writer.write(",");
				writer.write(txglRecord.loginName == null ? ""
						: txglRecord.loginName);
				writer.write(",");
				writer.write(txglRecord.userName == null ? ""
						: txglRecord.userName);
				writer.write(",");
				writer.write(txglRecord.extractionBank == null ? ""
						: txglRecord.extractionBank);
				writer.write(",");
				writer.write(txglRecord.location == null ? ""
						: txglRecord.location);
				writer.write(",");
				writer.write(txglRecord.subbranch == null ? ""
						: txglRecord.subbranch);
				writer.write(",");
				writer.write(txglRecord.bankId == null ? "" : txglRecord.bankId);
				writer.write(",");
				writer.write(format(txglRecord.extractionAmount));
				writer.write(",");
				writer.write(format(txglRecord.charge));
				writer.write(",");
				writer.write(" "
						+ DateParser.format(txglRecord.checkDateTime,
								"yyyy-MM-dd HH:mm"));
				writer.newLine();
			}
			writer.flush();
		}
	}

	@Override
	public Bank[] getBanks() throws Throwable {
		try(Connection connection = getConnection(P2PConst.DB_INFORMATION))
		{
			return selectAll(connection,
					new ArrayParser<Bank>() {

						@Override
						public Bank[] parse(ResultSet resultSet)
								throws SQLException {
							List<Bank> banks = new ArrayList<>();
							while (resultSet.next()) {
								Bank bank = new Bank();
								bank.id = resultSet.getInt(1);
								bank.name = resultSet.getString(2);
								banks.add(bank);
							}
							return banks.toArray(new Bank[banks.size()]);
						}
					}, "SELECT F01,F02 FROM T5020 WHERE F03 = ?", T5020_F03.QY);
		}

	}
}
