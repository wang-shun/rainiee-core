package com.dimeng.p2p.modules.finance.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.TradingType;
import com.dimeng.p2p.modules.finance.console.service.YhjyjlManage;
import com.dimeng.p2p.modules.finance.console.service.entity.Yhjyjl;
import com.dimeng.p2p.modules.finance.console.service.entity.YhjyjlRecord;
import com.dimeng.p2p.modules.finance.console.service.query.YhjyjlRecordQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;

public class YhjyjlManageImpl extends AbstractFinanceService implements
		YhjyjlManage {

	public static class UserTradeManageFactory implements
			ServiceFactory<YhjyjlManage> {

		@Override
		public YhjyjlManage newInstance(ServiceResource serviceResource) {
			return new YhjyjlManageImpl(serviceResource);
		}
	}

	public YhjyjlManageImpl(ServiceResource serviceResource) {
		super(serviceResource);

	}

	public Yhjyjl getUserTradeInfo(int id) throws Throwable {
		Yhjyjl userTrade = new Yhjyjl();
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement pst = conn
					.prepareStatement("SELECT T2.F02, T3.F06, SUM(T1.F05), SUM(T1.F06) FROM T6032 T1 INNER JOIN T6010 T2 ON T1.F02=T2.F01 INNER JOIN T6011 T3 ON T1.F02=T3.F01 WHERE T1.F02=?")) {
				pst.setInt(1, id);
				try (ResultSet rst = pst.executeQuery()) {
					if (rst.next()) {
						userTrade.loginName = rst.getString(1);
						userTrade.userName = rst.getString(2);
						userTrade.totalIncome = rst.getBigDecimal(3);
						userTrade.totalReplay = rst.getBigDecimal(4);
					}
				}
			}
		}
		return userTrade;
	}

	public PagingResult<YhjyjlRecord> search(YhjyjlRecordQuery query,
			Paging paging) throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT F04, F03, F05, F06,F07,F09 FROM T6032 WHERE 1=1");
		ArrayList<Object> parameters = new ArrayList<>();
		if (query != null) {
			int id = query.getId();
			if (id > 0) {
				sql.append(" AND F02=?");
				parameters.add(id);
			}
			TradingType type = query.getType();
			if (type != null) {
				sql.append(" AND F03= ?");
				parameters.add(type);
			}
			Timestamp date = query.getStartPayTime();
			if (date != null) {
				sql.append(" AND DATE(F04) >=?");
				parameters.add(date);
			}
			date = query.getEndPayTime();
			if (date != null) {
				sql.append(" AND DATE(F04) <=?");
				parameters.add(date);
			}
		}
		sql.append(" ORDER BY F01 DESC");
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectPaging(connection,
					new ArrayParser<YhjyjlRecord>() {
						ArrayList<YhjyjlRecord> lists = new ArrayList<YhjyjlRecord>();

						public YhjyjlRecord[] parse(ResultSet rst)
								throws SQLException {
							while (rst.next()) {
								YhjyjlRecord record = new YhjyjlRecord();
								record.playTime = rst.getTimestamp(1);
								record.type = EnumParser.parse(TradingType.class,
										rst.getString(2));
								record.income = rst.getBigDecimal(3);
								record.replay = rst.getBigDecimal(4);
								record.remain = rst.getBigDecimal(5);
								record.remark = rst.getString(6);
								lists.add(record);
							}
							return lists.toArray(new YhjyjlRecord[lists.size()]);
						}
					}, paging, sql.toString(), parameters);
		}
	}

	public void export(YhjyjlRecord[] records, OutputStream outputStream,
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
			for (YhjyjlRecord record : records) {
				if (record == null) {
					continue;
				}
				writer.write(" " + DateParser.format(record.playTime));
				writer.write(",");
				writer.write(record.type != null ? record.type.getName() : "");
				writer.write(",");
				writer.write(format(record.income));
				writer.write(",");
				writer.write(format(record.replay));
				writer.write(",");
				writer.write(format(record.remain));
				writer.write(",");
				writer.write(record.remark == null ? "" : record.remark.replaceAll(",", ""));
				writer.newLine();
			}
			writer.flush();
		}
	}
}
