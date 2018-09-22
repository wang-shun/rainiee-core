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

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.TradeType;
import com.dimeng.p2p.modules.finance.console.service.PtfxbyjManage;
import com.dimeng.p2p.modules.finance.console.service.entity.Ptfxbyjgl;
import com.dimeng.p2p.modules.finance.console.service.entity.PtfxbyjglRecord;
import com.dimeng.p2p.modules.finance.console.service.query.PtfxbyjglRecordQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;

public class PtfxbyjManageImpl extends AbstractFinanceService implements
		PtfxbyjManage {

	public static class PlatformReserveFundManageFactory implements
			ServiceFactory<PtfxbyjManage> {

		@Override
		public PtfxbyjManage newInstance(ServiceResource serviceResource) {
			return new PtfxbyjManageImpl(serviceResource);
		}
	}

	public PtfxbyjManageImpl(ServiceResource serviceResource) {
		super(serviceResource);

	}

	@Override
	public Ptfxbyjgl getPlatformFundInfo() throws Throwable {
		Ptfxbyjgl ptfxbyjgl = new Ptfxbyjgl();
		try (Connection conn = getConnection()) {
			try (PreparedStatement pst = conn
					.prepareStatement("SELECT F01, F02, F03,F05,F06,F07 FROM T7027 LIMIT 1")) {
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						ptfxbyjgl.riskFund = rs.getBigDecimal(1);
						ptfxbyjgl.totalReplay = rs.getBigDecimal(2);
						ptfxbyjgl.totalIncome = rs.getBigDecimal(3);
						ptfxbyjgl.updateTime = rs.getTimestamp(4);
						ptfxbyjgl.profitLoss = ptfxbyjgl.totalIncome.add(rs
								.getBigDecimal(5)
								.subtract(ptfxbyjgl.totalReplay)
								.subtract(rs.getBigDecimal(6)));
					}
				}
			}
		}
		return ptfxbyjgl;
	}

	@Override
	public PagingResult<PtfxbyjglRecord> search(PtfxbyjglRecordQuery query,
			Paging paging) throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT T7028.F02, T7028.F06, T7028.F03, T7028.F04, T7028.F05,T7028.F07,T6010.F02 AS USERNAME FROM T7028 LEFT JOIN "
						+ P2PConst.DB_USER
						+ ".T6010 ON T7028.F09=T6010.F01 WHERE 1=1");
		ArrayList<Object> parameters = new ArrayList<>();
		if (query != null) {
			TradeType type = query.getType();
			if (type != null) {
				sql.append(" AND T7028.F06= ?");
				parameters.add(type);
			}
			String userName = query.getUserName();
			if (!StringHelper.isEmpty(userName)) {
				sql.append(" AND T6010.F02 LIKE ?");
				parameters.add(getSQLConnectionProvider().allMatch(userName));
			}
			Timestamp date = query.getStartPayTime();
			if (date != null) {
				sql.append(" AND DATE(T7028.F02) >=?");
				parameters.add(date);
			}
			date = query.getEndPayTime();
			if (date != null) {
				sql.append(" AND DATE(T7028.F02) <=?");
				parameters.add(date);
			}
		}
		sql.append(" ORDER BY T7028.F01 DESC");
		try(Connection connection = getConnection(P2PConst.DB_CONSOLE))
		{
			return selectPaging(connection,
					new ArrayParser<PtfxbyjglRecord>() {
						ArrayList<PtfxbyjglRecord> list = new ArrayList<PtfxbyjglRecord>();

						@Override
						public PtfxbyjglRecord[] parse(ResultSet rst)
								throws SQLException {
							while (rst.next()) {
								PtfxbyjglRecord record = new PtfxbyjglRecord();
								record.playTime = rst.getTimestamp(1);
								record.type = EnumParser.parse(TradeType.class,
										rst.getString(2));
								record.income = rst.getBigDecimal(3);
								record.replay = rst.getBigDecimal(4);
								record.remain = rst.getBigDecimal(5);
								record.remark = rst.getString(6);
								record.userName = rst.getString(7);
								list.add(record);
							}
							return list.toArray(new PtfxbyjglRecord[list.size()]);
						}
					}, paging, sql.toString(), parameters);
		}
	}

	@Override
	public void export(PtfxbyjglRecord[] records, OutputStream outputStream,
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
			for (PtfxbyjglRecord record : records) {
				if (record == null) {
					continue;
				}
				writer.write(" " + DateParser.format(record.playTime));
				writer.write(",");
				writer.write(record.userName != null ? record.userName : "");
				writer.write(",");
				writer.write(record.type != null ? record.type.getName() : "");
				writer.write(",");
				writer.write(format(record.income));
				writer.write(",");
				writer.write(format(record.replay));
				writer.write(",");
				writer.write(format(record.remain));
				writer.write(",");
				writer.write(record.remark == null ? "" : record.remark
						.replaceAll(",", ""));
				writer.newLine();
			}
			writer.flush();
		}
	}

	@Override
	public int recharge(BigDecimal ammount, String remark) throws Throwable {
		if (ammount == null) {
			throw new ParameterException("充值金额不能为空.");
		}
		try(Connection connection = getConnection())
		{
			try{
				serviceResource.openTransactions(connection);
				String select = "SELECT F01 FROM T7027 FOR UPDATE";
				BigDecimal totalAmount = selectBigDecimal(P2PConst.DB_CONSOLE, select);
				String update = "UPDATE T7027 SET F01=F01+?,F03=F03+?,F07=F07+?,F05=?";
				Timestamp now = new Timestamp(System.currentTimeMillis());
				execute(connection, update, ammount, ammount, ammount, now);
				String insert = "INSERT INTO T7028 SET F02=?,F03=?,F05=?,F06=?,F07=?";
				int rtn = insert(connection, insert, now, ammount, totalAmount.add(ammount), TradeType.SDZJBZJ, remark);
				serviceResource.commit(connection);
				return rtn;
			}
			catch (Exception e)
			{
				serviceResource.rollback(connection);
				throw e;
			}
		}
	}

	@Override
	public int withdrawal(BigDecimal ammount, String remark) throws Throwable {
		if (ammount == null) {
			throw new ParameterException("提现金额不能为空.");
		}
		try(Connection connection = getConnection())
		{
			try
			{
				serviceResource.openTransactions(connection);
				String select = "SELECT F01 FROM T7027 FOR UPDATE";
				BigDecimal totalAmount = selectBigDecimal(P2PConst.DB_CONSOLE, select);
				if (totalAmount == null || totalAmount.compareTo(ammount) < 0)
				{
					throw new ParameterException("账户余额不足,请重新调整金额.");
				}
				Timestamp time = getCurrentTimestamp(connection);
				String update = "UPDATE T7027 SET F01=F01-?,F02=F02+?,F06=F06+?,F05=?";
				execute(connection, update, ammount, ammount, ammount,time);
				String insert = "INSERT INTO T7028 SET F02=?,F04=?,F05=?,F06=?,F07=?";
				int rtn = insert(connection, insert,time, ammount, totalAmount.subtract(ammount), TradeType.SDKCBZJ, remark);

				serviceResource.commit(connection);
				return rtn;
			}
			catch (Exception e)
			{
				serviceResource.rollback(connection);
				throw e;
			}
		}
	}
}
