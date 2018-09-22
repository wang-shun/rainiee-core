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
import com.dimeng.p2p.common.enums.PlatformFundType;
import com.dimeng.p2p.modules.finance.console.service.PtzjManage;
import com.dimeng.p2p.modules.finance.console.service.entity.Ptzjgl;
import com.dimeng.p2p.modules.finance.console.service.entity.PtzjglRecord;
import com.dimeng.p2p.modules.finance.console.service.query.PtzjglRecordQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;

public class PtzjManageImpl extends AbstractFinanceService implements
		PtzjManage {

	public static class PlatformFundFactory implements
			ServiceFactory<PtzjManage> {

		@Override
		public PtzjManage newInstance(ServiceResource serviceResource) {
			return new PtzjManageImpl(serviceResource);
		}
	}

	public PtzjManageImpl(ServiceResource serviceResource) {
		super(serviceResource);

	}

	@Override
    public Ptzjgl getPlatFormFundInfo() throws Throwable {
		Ptzjgl platformFund = new Ptzjgl();
		try (Connection conn = getConnection()) {
			try (PreparedStatement pst = conn
					.prepareStatement("SELECT F01, F02, F03,F05,F06,F07 FROM T7025")) {
				try (ResultSet rst = pst.executeQuery()) {
					if (rst.next()) {
						platformFund.platformAmount = rst.getBigDecimal(1);
						platformFund.totalReplay = rst.getBigDecimal(2);
						platformFund.totalIncome = rst.getBigDecimal(3);
						platformFund.updateTime = rst.getTimestamp(4);
						platformFund.profitLoss = platformFund.totalIncome
								.add(rst.getBigDecimal(5)
										.subtract(platformFund.totalReplay)
										.subtract(rst.getBigDecimal(6)));
					}
				}
			}
		}
		return platformFund;
	}

	@Override
    public PagingResult<PtzjglRecord> search(PtzjglRecordQuery query,
			Paging paging) throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT T7026.F02, T7026.F06, T7026.F03, T7026.F04, T7026.F05,T7026.F07,T6010.F02 AS USERNAME FROM T7026 LEFT JOIN "
						+ P2PConst.DB_USER
						+ ".T6010 ON T7026.F09=T6010.F01 WHERE 1=1");
		ArrayList<Object> parameters = new ArrayList<>();
		if (query != null) {
			PlatformFundType type = query.getType();
			if (type != null) {
				sql.append(" AND T7026.F06= ?");
				parameters.add(type);
			}
			String userName = query.getUserName();
			if (!StringHelper.isEmpty(userName)) {
				sql.append(" AND T6010.F02 LIKE ?");
				parameters.add(getSQLConnectionProvider().allMatch(userName));
			}
			Timestamp date = query.getStartPayTime();
			if (date != null) {
				sql.append(" AND DATE(T7026.F02) >=?");
				parameters.add(date);
			}
			date = query.getEndPayTime();
			if (date != null) {
				sql.append(" AND DATE(T7026.F02) <=?");
				parameters.add(date);
			}
		}
		sql.append(" ORDER BY T7026.F01 DESC");
		try(Connection connection = getConnection(P2PConst.DB_CONSOLE))
		{
			return selectPaging(connection,
					new ArrayParser<PtzjglRecord>() {
						ArrayList<PtzjglRecord> list = new ArrayList<PtzjglRecord>();

						@Override
						public PtzjglRecord[] parse(ResultSet rst)
								throws SQLException {
							while (rst.next()) {
								PtzjglRecord platform = new PtzjglRecord();
								platform.playTime = rst.getTimestamp(1);
								platform.type = EnumParser.parse(
										PlatformFundType.class, rst.getString(2));
								platform.income = rst.getBigDecimal(3);
								platform.replay = rst.getBigDecimal(4);
								platform.remain = rst.getBigDecimal(5);
								platform.remark = rst.getString(6);
								platform.loginName = rst.getString(7);
								list.add(platform);
							}
							return list.toArray(new PtzjglRecord[list.size()]);
						}
					}, paging, sql.toString(), parameters);
		}
	}

	@Override
    public void export(PtzjglRecord[] records, OutputStream outputStream,
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
			writer.write("序号");
			writer.write(",");
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

			int i = 1;
			for (PtzjglRecord record : records) {
				if (record == null) {
					continue;
				}
				writer.write(" " + i++);
				writer.write(",");
				writer.write(" "
						+ DateParser.format(record.playTime,
								"yyyy-MM-dd HH:mm:ss"));
				writer.write(",");
				writer.write(record.loginName != null ? record.loginName : "");
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
				String select = "SELECT F01 FROM S70.T7025 FOR UPDATE";
				BigDecimal totalAmount = selectBigDecimal(P2PConst.DB_CONSOLE, select);
				String update = "UPDATE S70.T7025 SET F01=F01+?,F03=F03+?,F07=F07+?";
				Timestamp now = new Timestamp(System.currentTimeMillis());
				execute(connection, update, ammount, ammount, ammount);
				String insert = "INSERT INTO S70.T7026 SET F02=?,F03=?,F05=?,F06=?,F07=?";
				int rtn =
						insert(connection, insert, now, ammount, totalAmount.add(ammount), PlatformFundType.PTZHCZ, remark);

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
				String select = "SELECT F01 FROM S70.T7025 FOR UPDATE";
				BigDecimal totalAmount = selectBigDecimal(P2PConst.DB_CONSOLE, select);
				if (totalAmount == null || totalAmount.compareTo(ammount) < 0)
				{
					throw new ParameterException("账户余额不足,请重新调整金额.");
				}
				String update = "UPDATE S70.T7025 SET F01=F01-?,F02=F02+?,F06=F06+?";
				execute(connection, update, ammount, ammount, ammount);
				String insert = "INSERT INTO S70.T7026 SET F02=?,F04=?,F05=?,F06=?,F07=?";
				int rtn =
						insert(connection, insert, getCurrentTimestamp(connection),ammount, totalAmount.subtract(ammount), PlatformFundType.PTZHTX, remark);

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
