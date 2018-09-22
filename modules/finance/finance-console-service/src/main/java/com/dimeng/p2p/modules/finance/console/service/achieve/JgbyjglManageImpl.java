package com.dimeng.p2p.modules.finance.console.service.achieve;

import java.io.BufferedWriter;
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

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.EmptyPageResult;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S60.enums.T6041_F12;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.common.enums.OrganizationType;
import com.dimeng.p2p.common.enums.TradeType;
import com.dimeng.p2p.modules.finance.console.service.JgbyjglManage;
import com.dimeng.p2p.modules.finance.console.service.entity.JgbyjStatistics;
import com.dimeng.p2p.modules.finance.console.service.entity.Jgfxbyj;
import com.dimeng.p2p.modules.finance.console.service.entity.JgfxbyjDetail;
import com.dimeng.p2p.modules.finance.console.service.entity.JgfxbyjRecord;
import com.dimeng.p2p.modules.finance.console.service.entity.Jgxyjl;
import com.dimeng.p2p.modules.finance.console.service.entity.Jgyw;
import com.dimeng.p2p.modules.finance.console.service.entity.JgywRecord;
import com.dimeng.p2p.modules.finance.console.service.query.JgfxbyjglDetailQuery;
import com.dimeng.p2p.modules.finance.console.service.query.JgfxbyjglQuery;
import com.dimeng.p2p.modules.finance.console.service.query.JgywmxQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;

public class JgbyjglManageImpl extends AbstractFinanceService implements
		JgbyjglManage {

	public static class OrganizationReserveFundManageFactory implements
			ServiceFactory<JgbyjglManage> {
		@Override
		public JgbyjglManage newInstance(ServiceResource serviceResource) {
			return new JgbyjglManageImpl(serviceResource);
		}
	}

	public JgbyjglManageImpl(ServiceResource serviceResource) {
		super(serviceResource);

	}

	@Override
	public JgbyjStatistics getJgfxbyj() throws Throwable {
		try(Connection connection = getConnection())
		{
			return select(connection, new ItemParser<JgbyjStatistics>() {
				@Override
				public JgbyjStatistics parse(ResultSet resultSet)
						throws SQLException {
					final JgbyjStatistics statistics = new JgbyjStatistics();
					if (resultSet.next()) {
						statistics.ysbyjAmount = resultSet.getBigDecimal(1);
						statistics.byjAmount = resultSet.getBigDecimal(2);
						statistics.xyAmount = resultSet.getBigDecimal(3);
						statistics.kyAmount = resultSet.getBigDecimal(4);
					}
					return statistics;
				}
			}, "SELECT SUM(F11),SUM(F12),SUM(F09),SUM(F10) FROM T7029");
		}
	}

	@Override
	public PagingResult<Jgfxbyj> search(JgfxbyjglQuery query, Paging paging)
			throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT F01, F02, F03, F09, F10, F11, F12 FROM T7029 WHERE F05='YX'");
		ArrayList<Object> parameters = new ArrayList<>();
		if (query != null) {
			SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
			String organizationName = query.getOrganizationName();
			if (!StringHelper.isEmpty(organizationName)) {
				sql.append(" AND F02 LIKE ?");
				parameters
						.add(sqlConnectionProvider.allMatch(organizationName));
			}
			OrganizationType type = query.getType();
			if (type != null) {
				sql.append(" AND F03=?");
				parameters.add(type);
			}
			sql.append(" ORDER BY F01 DESC");
		}
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<Jgfxbyj>() {
				ArrayList<Jgfxbyj> list = new ArrayList<Jgfxbyj>();

				@Override
				public Jgfxbyj[] parse(ResultSet rst) throws SQLException {
					while (rst.next()) {
						Jgfxbyj org = new Jgfxbyj();
						org.id = rst.getInt(1);
						org.organizationName = rst.getString(2);
						org.type = OrganizationType.valueOf(rst.getString(3));
						org.organizationCreditAmount = rst.getBigDecimal(4);
						org.organizationUsableAmount = rst.getBigDecimal(5);
						org.originalReserveAmount = rst.getBigDecimal(6);
						org.originalReserveBalance = rst.getBigDecimal(7);
						list.add(org);
					}
					return list.toArray(new Jgfxbyj[list.size()]);
				}
			}, paging, sql.toString(), parameters);
		}
	}

	@Override
	public JgfxbyjRecord getJgfxbyjRecord(int id) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("指定的记录不存在.");
		}
		JgfxbyjRecord jgfxbyjRecord = new JgfxbyjRecord();
		String sql = "SELECT SUM(F04),SUM(F05) FROM T7030 WHERE F02=? AND (F07= ? OR F07= ?)";
		try (Connection conn = getConnection()) {
			try (PreparedStatement pst = conn
					.prepareStatement("SELECT T7029.F02,T7029.F12,SUM(T7030.F04),SUM(T7030.F05) FROM T7030 INNER JOIN T7029 ON T7030.F02=T7029.F01 WHERE T7030.F02=?")) {
				pst.setInt(1, id);
				try (ResultSet rst = pst.executeQuery()) {
					if (rst.next()) {
						jgfxbyjRecord.orgName = rst.getString(1);
						jgfxbyjRecord.riskAmount = rst.getBigDecimal(2);
						jgfxbyjRecord.incomeAmount = rst.getBigDecimal(3);
						jgfxbyjRecord.payAmount = rst.getBigDecimal(4);
					}
				}
			}
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, id);
				ps.setString(2, TradeType.SDKCBZJ.name());
				ps.setString(3, TradeType.SDZJBZJ.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						if (rs.getBigDecimal(2) == null
								|| rs.getBigDecimal(1) == null) {
							return jgfxbyjRecord;
						}
						jgfxbyjRecord.lossAmount = jgfxbyjRecord.incomeAmount
								.add(rs.getBigDecimal(2))
								.subtract(rs.getBigDecimal(1))
								.subtract(jgfxbyjRecord.payAmount);
					}
				}
			}
		}
		return jgfxbyjRecord;
	}

	@Override
	public PagingResult<JgfxbyjDetail> serarchDetail(
			JgfxbyjglDetailQuery query, Paging paging) throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT T.F03,T.F07, T.F04, T.F05, T.F06, T.F08,T6010.F02 AS USERNAME FROM T7030 T INNER JOIN T7029 T1 ON T.F02 = T1.F01 LEFT JOIN "
						+ P2PConst.DB_USER
						+ ".T6010 ON T.F10=T6010.F01 WHERE 1=1");
		ArrayList<Object> parameters = new ArrayList<>();
		if (query != null) {
			int id = query.getId();
			if (id > 0) {
				sql.append(" AND T.F02=?");
				parameters.add(id);
			}
			String userName = query.getUserName();
			if (!StringHelper.isEmpty(userName)) {
				sql.append(" AND T6010.F02 LIKE ?");
				parameters.add(getSQLConnectionProvider().allMatch(userName));
			}
			TradeType type = query.getType();
			if (type != null) {
				sql.append(" AND T.F07 = ?");
				parameters.add(type);
			}
			Timestamp timestamp = query.getStartDateTime();
			if (timestamp != null) {
				sql.append(" AND DATE(T.F03) >=?");
				parameters.add(timestamp);
			}
			timestamp = query.getEndDatetime();
			if (timestamp != null) {
				sql.append(" AND DATE(T.F03) <=?");
				parameters.add(timestamp);
			}
		}
		sql.append(" ORDER BY T.F01 DESC");
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<JgfxbyjDetail>() {
				ArrayList<JgfxbyjDetail> list = new ArrayList<JgfxbyjDetail>();

				@Override
				public JgfxbyjDetail[] parse(ResultSet rst) throws SQLException {
					while (rst.next()) {
						JgfxbyjDetail org = new JgfxbyjDetail();
						org.tradeDateTime = rst.getTimestamp(1);
						org.type = EnumParser.parse(TradeType.class,
								rst.getString(2));
						org.organizationCreditAmount = rst.getBigDecimal(3);
						org.organizationUsableAmount = rst.getBigDecimal(4);
						org.originalReserveAmount = rst.getBigDecimal(5);
						org.remark = rst.getString(6);
						org.userName = rst.getString(7);
						list.add(org);
					}
					return list.toArray(new JgfxbyjDetail[list.size()]);
				}
			}, paging, sql.toString(), parameters);
		}
	}

	@Override
	public Jgxyjl getJgxyjl(int id) throws Throwable {
		// 更具机构查询合同信息
		List<Integer> constanctIds = new ArrayList<>();
		String selectT7031 = "SELECT F01 FROM S70.T7031 WHERE F02=?";
		try (Connection connection = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = connection
					.prepareStatement(selectT7031)) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						constanctIds.add(rs.getInt(1));
					}
				}
			}

			if (constanctIds.size() <= 0) {
				return new Jgxyjl();
			}
			List<Integer> userIds = new ArrayList<>();
			Jgxyjl jgxyjl = new Jgxyjl();
			StringBuilder sbT6036 = new StringBuilder("SELECT F02 FROM T6036");
			sbT6036.append(" WHERE F25 IN(");
			for (int i = 0; i < constanctIds.size(); i++) {
				if (i > 0) {
					sbT6036.append(",");
				}
				sbT6036.append(constanctIds.get(i));
			}
			sbT6036.append(")");
			sbT6036.append(" GROUP BY F02");

			try (PreparedStatement ps = connection.prepareStatement(sbT6036
					.toString())) {
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						userIds.add(rs.getInt(1));
					}
				}
			}
			if (userIds.size() <= 0) {
				return new Jgxyjl();
			}
			StringBuilder selectT6045 = new StringBuilder(
					"SELECT SUM(F02),SUM(F03),SUM(F07),SUM(F08),SUM(F09) FROM T6045");
			selectT6045.append(" WHERE F01 IN(");
			for (int i = 0; i < userIds.size(); i++) {
				if (i > 0) {
					selectT6045.append(",");
				}
				selectT6045.append(userIds.get(i));
			}
			selectT6045.append(")");

			StringBuilder selectT6029 = new StringBuilder(
					"SELECT SUM(F02) FROM T6029");
			selectT6029.append(" WHERE F01 IN(");
			for (int i = 0; i < userIds.size(); i++) {
				if (i > 0) {
					selectT6029.append(",");
				}
				selectT6029.append(userIds.get(i));
			}
			selectT6029.append(")");

			StringBuilder selectT6041 = new StringBuilder(
					"SELECT SUM(F05) FROM T6041 WHERE F12=?");
			selectT6041.append(" AND F03 IN(");
			for (int i = 0; i < userIds.size(); i++) {
				if (i > 0) {
					selectT6041.append(",");
				}
				selectT6041.append(userIds.get(i));
			}
			selectT6041.append(")");

			StringBuilder selectT6041_1 =
					new StringBuilder("SELECT SUM(F05+F06+F07+F08+F09) FROM T6041 WHERE DATEDIFF("
							+ getCurrentDate(connection) + ",F10)>0 AND F12=?");
			selectT6041_1.append(" AND F03 IN(");
			for (int i = 0; i < userIds.size(); i++) {
				if (i > 0) {
					selectT6041_1.append(",");
				}
				selectT6041_1.append(userIds.get(i));
			}
			selectT6041_1.append(")");
			try (PreparedStatement ps = connection.prepareStatement(selectT6045
					.toString())) {
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						jgxyjl.yqcsCount = rs.getInt(1);
						jgxyjl.yzyqCount = rs.getInt(2);
						jgxyjl.hqbsCount = rs.getInt(3);
						jgxyjl.sqjkCount = rs.getInt(4);
						jgxyjl.cgjkCount = rs.getInt(5);
					}
				}
			}
			jgxyjl.dhbjAmount = selectBigDecimal(connection,
					selectT6041.toString(), T6041_F12.WH);
			jgxyjl.yqjeAmount = selectBigDecimal(connection,
					selectT6041_1.toString(), getCurrentDate(connection), T6041_F12.WH);
			jgxyjl.jkTotalAmount = selectBigDecimal(connection,
					selectT6029.toString());
			return jgxyjl;
		}
	}

	@Override
	public Jgfxbyj getJgxbyj(int id) throws Throwable {
		try(Connection connection = getConnection())
		{
			return select(connection, new ItemParser<Jgfxbyj>() {
				@Override
				public Jgfxbyj parse(ResultSet resultSet) throws SQLException {
					final Jgfxbyj jgfxbyj = new Jgfxbyj();
					if (resultSet.next()) {
						jgfxbyj.organizationName = resultSet.getString(1);
						jgfxbyj.organizationCreditAmount = resultSet
								.getBigDecimal(2);
						jgfxbyj.organizationUsableAmount = resultSet
								.getBigDecimal(3);
					}
					return jgfxbyj;
				}
			}, "SELECT F02,F09,F10 FROM T7029 WHERE F01=?", id);
		}
	}

	@Override
	public void setJgxyed(int id, BigDecimal ammount) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("指定的记录不存在.");
		}
		if (ammount == null) {
			throw new ParameterException("调整的信用额度不能为空.");
		}
		String select = "SELECT F09,F10 FROM T7029 WHERE F01=? FOR UPDATE";
		BigDecimal totalLimit = new BigDecimal(0);
		BigDecimal usableLimit = new BigDecimal(0);
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(select)) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						totalLimit = rs.getBigDecimal(1);
						usableLimit = rs.getBigDecimal(2);
					}
				}
			}
			BigDecimal limie = totalLimit;
			if (ammount.compareTo(usableLimit) > 0) {
				limie = totalLimit.add(ammount.subtract(usableLimit));
			} else if (ammount.compareTo(usableLimit) < 0) {
				limie = totalLimit.subtract(usableLimit.subtract(ammount));
			}
			String update = "UPDATE T7029 SET F09=?,F10=? WHERE F01=?";
			execute(connection, update, limie, ammount, id);
		}
	}

	@Override
	public void export(Jgfxbyj[] records, OutputStream outputStream,
			String charset) throws Throwable {
		if (outputStream == null) {
			return;
		}
		if (records == null) {
			return;
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			writer.write("机构名称");
			writer.write(",");
			writer.write("机构类型");
			writer.write(",");
			writer.write("机构信用额度");
			writer.write(",");
			writer.write("机构可用额度");
			writer.write(",");
			writer.write("原始备用金总额");
			writer.write(",");
			writer.write("备用金金额");
			writer.newLine();
			for (Jgfxbyj record : records) {
				if (record == null) {
					continue;
				}
				writer.write(record.organizationName == null ? ""
						: record.organizationName);
				writer.write(",");
				writer.write(record.type != null ? record.type.getName() : "");
				writer.write(",");
				writer.write(format(record.organizationCreditAmount));
				writer.write(",");
				writer.write(format(record.organizationUsableAmount));
				writer.write(",");
				writer.write(format(record.originalReserveAmount));
				writer.write(",");
				writer.write(format(record.originalReserveBalance));
				writer.newLine();
			}
			writer.flush();
		}
	}

	@Override
	public void export(JgfxbyjDetail[] records, OutputStream outputStream,
			String charset) throws Throwable {
		if (outputStream == null) {
			return;
		}
		if (records == null) {
			return;
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			writer.write("时间");
			writer.write(",");
			writer.write("用户名");
			writer.write(",");
			writer.write("类型明细");
			writer.write(",");
			writer.write("收入");
			writer.write(",");
			writer.write("支出");
			writer.write(",");
			writer.write("结余");
			writer.write(",");
			writer.write("备注");
			writer.newLine();
			for (JgfxbyjDetail record : records) {
				if (record == null) {
					continue;
				}
				writer.write(" " + DateParser.format(record.tradeDateTime));
				writer.write(",");
				writer.write(record.userName != null ? record.userName : "");
				writer.write(",");
				writer.write(record.type != null ? record.type.getName() : "");
				writer.write(",");
				writer.write(format(record.organizationCreditAmount));
				writer.write(",");
				writer.write(format(record.organizationUsableAmount));
				writer.write(",");
				writer.write(format(record.originalReserveAmount));
				writer.write(",");
				writer.write(record.remark == null ? "" : record.remark
						.replaceAll(",", ""));
				writer.newLine();
			}
			writer.flush();
		}
	}

	@Override
	public int recharge(int id, BigDecimal ammount, String remark)
			throws Throwable {
		if (id <= 0) {
			throw new ParameterException("指定的记录不存在.");
		}
		if (ammount == null) {
			throw new ParameterException("充值金额不能为空.");
		}
		try(Connection connection = getConnection())
		{
			try {
				serviceResource.openTransactions(connection);
				String select = "SELECT F12 FROM T7029 WHERE F01=? FOR UPDATE ";
				BigDecimal totalAmount = selectBigDecimal(connection, select, id);
				int count = 0;
				try (PreparedStatement ps = connection
						.prepareStatement("SELECT COUNT(F01) FROM T7030 WHERE F02=? AND F07=?")) {
					ps.setInt(1, id);
					ps.setString(2, TradeType.SDZJBZJ.name());
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next()) {
							count = rs.getInt(1);
						}
					}
				}
				Timestamp now = new Timestamp(System.currentTimeMillis());
				if (count <= 0)
				{
					String update = "UPDATE T7029 SET F11=F11+?,F12=F12+?,F08=? WHERE F01=?";
					execute(connection, update, ammount, ammount, now, id);
				}
				else
				{
					String update = "UPDATE T7029 SET F12=F12+?,F08=? WHERE F01=?";
					execute(connection, update, ammount, now, id);
				}
				String insert = "INSERT INTO T7030 SET F02=?,F03=?,F04=?,F06=?,F07=?,F08=?";
				int rtn = insert(connection,
						insert,
						id,
						now,
						ammount,
						totalAmount.add(ammount),
						TradeType.SDZJBZJ,
						remark);
				serviceResource.commit(connection);
				return rtn;
			}catch (Exception e){
				serviceResource.rollback(connection);
				throw e;
			}
		}

	}

	@Override
	public int withdrawal(int id, BigDecimal ammount, String remark)
			throws Throwable {
		if (id <= 0) {
			throw new ParameterException("指定的机构不存在.");
		}
		if (ammount == null) {
			throw new ParameterException("提现金额不能为空.");
		}
		try(Connection connection = getConnection())
		{
			try{
				serviceResource.openTransactions(connection);
				String select = "SELECT F12 FROM T7029 WHERE F01=? FOR UPDATE ";
				BigDecimal totalAmount = selectBigDecimal(P2PConst.DB_CONSOLE, select, id);
				if (totalAmount == null || totalAmount.compareTo(ammount) < 0)
				{
					throw new ParameterException("账户余额不足,请重新调整金额.");
				}
				Timestamp time = getCurrentTimestamp(connection);
				String update = "UPDATE T7029 SET F10=F10-?,F12=F12-?,F08=? WHERE F01=?";
				execute(connection, update, ammount, ammount, time, id);
				String insert = "INSERT INTO T7030 SET F02=?,F03=?,F05=?,F06=?,F07=?,F08=?";
				int rtn = insert(connection, insert, id, time,ammount, totalAmount.subtract(ammount), TradeType.SDKCBZJ, remark);
				serviceResource.commit(connection);
				return rtn;
			}catch (Exception e)
			{
				serviceResource.rollback(connection);
				throw e;
			}
		}
	}

	@Override
	public Jgyw getJgyw(int id) throws Throwable {
		Jgyw jgyw = new Jgyw();
		List<Integer> contanctIds = new ArrayList<>();
		String t7031 = "SELECT F01 FROM T7031 WHERE F02=?";
		String t7029 = "SELECT F02 FROM T7029 WHERE F01=?";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(t7031)) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						contanctIds.add(rs.getInt(1));
					}
				}
			}
			try (PreparedStatement ps = connection.prepareStatement(t7029)) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						jgyw.orgName = rs.getString(1);
					}
				}
			}

			List<Integer> userIds = new ArrayList<>();
			if(contanctIds.size() > 0){
				StringBuilder sql = new StringBuilder("SELECT F13 FROM T7031 WHERE 1=1");
				StringBuilder sb = new StringBuilder();
				sql.append(" AND F01 IN(");
				for (int contantId : contanctIds) {
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(contantId);
				}
				sql.append(sb.toString());
				sql.append(")");
				try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							userIds.add(rs.getInt(1));
						}
					}
				}
			}
			jgyw.jkTotalAmount = succJk(userIds);
			jgyw.dfTotalAmount = dhbjTotle(userIds);
			return jgyw;
		}
	}
	
	/**
	 * 待还本金总和
	 * @return
	 * @throws Throwable
	 */
	private BigDecimal dhbjTotle(List<Integer> userIds)throws Throwable{
		StringBuilder sql = new StringBuilder("SELECT IFNULL(SUM(T6041.F05), 0) FROM T6036, T6041 WHERE T6041.F02 = T6036.F01 AND T6041.F12 = ?");
		if(userIds.size() > 0){
			StringBuilder sb = new StringBuilder();
			sql.append(" AND T6036.F02 IN(");
			for (int userId : userIds) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(userId);
			}
			sql.append(sb.toString());
			sql.append(")");
			try(Connection connection = getConnection(P2PConst.DB_USER))
			{
				return selectBigDecimal(connection, sql.toString(),T6041_F12.WH);
			}
		}
		return new BigDecimal(0);
	}
	
	

	/**
	 * 成功借款总和
	 * @param userIds
	 * @return
	 * @throws Throwable
	 */
	private BigDecimal succJk(List<Integer> userIds)throws Throwable{
		StringBuilder sql = new StringBuilder("SELECT IFNULL(SUM(T6029.F02),0) FROM T6029 WHERE 1=1");
		if(userIds.size() > 0){
			StringBuilder sb = new StringBuilder();
			sql.append(" AND F01 IN(");
			for (int userId : userIds) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(userId);
			}
			sql.append(sb.toString());
			sql.append(")");
			try(Connection connection = getConnection(P2PConst.DB_USER))
			{
				return selectBigDecimal(connection, sql.toString());
			}
		}
		return new BigDecimal(0);
	}

	@Override
	public PagingResult<JgywRecord> searchJgywmx(JgywmxQuery query,
			Paging paging) throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT T6036.F04,T6010.F02,T6036.F06,T6036.F09,T6036.F08,T6036.F20,T6036.F01 FROM T6036 INNER JOIN T6010 ON T6036.F02=T6010.F01 WHERE 1=1");
		ArrayList<Object> parameters = new ArrayList<>();
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			if (query != null) {
				int id = query.getId();
				List<Integer> contanctIds = new ArrayList<>();
				String t7031 = "SELECT F01 FROM S70.T7031 WHERE F02=?";
				try (PreparedStatement ps = connection.prepareStatement(t7031)) {
					ps.setInt(1, id);
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							contanctIds.add(rs.getInt(1));
						}
					}
				}

				if (contanctIds.size() > 0) {
					StringBuilder sb = new StringBuilder();
					sql.append(" AND T6036.F25 IN(");
					for (Integer contantId : contanctIds) {
						if (sb.length() > 0) {
							sb.append(",");
						}
						sb.append(contantId);
					}
					sql.append(sb.toString());
					sql.append(")");
				} else {
					return new EmptyPageResult<JgywRecord>();
				}
				CreditStatus status = query.getStatus();
				if (status != null) {
					sql.append(" AND T6036.F20 = ?");
					parameters.add(status);
				}
				String account = query.getAccount();
				if (!StringHelper.isEmpty(account)) {
					sql.append(" AND T6010.F02 LIKE ?");
					parameters.add(getSQLConnectionProvider().allMatch(account));
				}
				String title = query.getTitle();
				if (!StringHelper.isEmpty(title)) {
					sql.append(" AND T6036.F04 LIKE ?");
					parameters.add(getSQLConnectionProvider().allMatch(title));
				}
			}

			return selectPaging(connection,
					new ArrayParser<JgywRecord>() {
						ArrayList<JgywRecord> lists = new ArrayList<JgywRecord>();

						@Override
						public JgywRecord[] parse(ResultSet rs) throws SQLException {
							while (rs.next()) {
								JgywRecord recoed = new JgywRecord();
								recoed.title = rs.getString(1);
								recoed.accountName = rs.getString(2);
								recoed.loanAmount = rs.getBigDecimal(3);
								recoed.proportion = rs.getBigDecimal(4);
								recoed.deadline = rs.getInt(5);

								recoed.status = EnumParser.parse(
										CreditStatus.class, rs.getString(6));
								try {
									recoed.dfAmount = dhbj(rs.getInt(7));
								} catch (Throwable e) {
									logger.error(e, e);
								}
								lists.add(recoed);
							}
							return lists.toArray(new JgywRecord[lists.size()]);
						}
					}, paging, sql.toString(), parameters);
		}
	}
	
	/**
	 * 单个标待还本金
	 * @return
	 * @throws Throwable
	 */
	private BigDecimal dhbj(int jkbId)throws Throwable{
		String sql = "SELECT IFNULL(SUM(T6041.F05), 0) FROM T6036, T6041 WHERE T6041.F02 = T6036.F01 AND T6041.F12 = ? AND T6036.F01 = ?";
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectBigDecimal(connection, sql, T6041_F12.WH,jkbId);
		}
	}

	@Override
	public void export(JgywRecord[] records, OutputStream outputStream,
			String charset) throws Throwable {
		if (outputStream == null) {
			return;
		}
		if (records == null) {
			return;
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			writer.write("借款标题");
			writer.write(",");
			writer.write("用户名");
			writer.write(",");
			writer.write("借款金额(元)");
			writer.write(",");
			writer.write("年化利率");
			writer.write(",");
			writer.write("期限(月)");
			writer.write(",");
			writer.write("待还本金(元)");
			writer.write(",");
			writer.write("状态");
			writer.newLine();
			for (JgywRecord record : records) {
				if (record == null) {
					continue;
				}
				writer.write(record.title == null ? "" : record.title);
				writer.write(",");
				writer.write(record.accountName == null ? ""
						: record.accountName);
				writer.write(",");
				writer.write(format(record.loanAmount));
				writer.write(",");
				writer.write(format(record.proportion));
				writer.write(",");
				writer.write(Integer.toString(record.deadline));
				writer.write(",");
				writer.write(format(record.loanAmount));
				writer.write(",");
				writer.write(record.status != null ? record.status.getName()
						: "");
				writer.newLine();
			}
			writer.flush();
		}
	}
}
