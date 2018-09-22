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

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S60.enums.T6028_F10;
import com.dimeng.p2p.S60.enums.T6038_F07;
import com.dimeng.p2p.S60.enums.T6041_F12;
import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.common.enums.DsStatus;
import com.dimeng.p2p.common.enums.InvestType;
import com.dimeng.p2p.common.enums.LetterStatus;
import com.dimeng.p2p.common.enums.PlatformFundType;
import com.dimeng.p2p.common.enums.RepayStatus;
import com.dimeng.p2p.common.enums.RepaymentType;
import com.dimeng.p2p.common.enums.TradeType;
import com.dimeng.p2p.common.enums.TradingType;
import com.dimeng.p2p.modules.finance.console.service.FkshManage;
import com.dimeng.p2p.modules.finance.console.service.entity.CjRecord;
import com.dimeng.p2p.modules.finance.console.service.entity.Fksh;
import com.dimeng.p2p.modules.finance.console.service.entity.Jkr;
import com.dimeng.p2p.modules.finance.console.service.entity.Tg;
import com.dimeng.p2p.modules.finance.console.service.entity.Tzr;
import com.dimeng.p2p.modules.finance.console.service.query.CjRecordQuery;
import com.dimeng.p2p.modules.finance.console.service.query.FkshQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.TimestampParser;

public class FkshManageImpl extends AbstractFinanceService implements
		FkshManage {

	public static class LoanCheckManageFactory implements
			ServiceFactory<FkshManage> {

		@Override
		public FkshManage newInstance(ServiceResource serviceResource) {
			return new FkshManageImpl(serviceResource);
		}
	}

	public FkshManageImpl(ServiceResource serviceResource) {
		super(serviceResource);

	}

	/**
	 * 发站内信
	 */
	private static final String t6035 = "INSERT INTO S60.T6035 SET F02=?,F03=?,F04=?,F05=?,F06=?";
	/**
	 * 站内信内容
	 */
	private static final String t6046 = "INSERT INTO T6046 SET F01=?,F02=?";

	@Override
	public PagingResult<Fksh> search(FkshQuery query, Paging paging)
			throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT T6036.F01,T6036.F03,T6036.F04,T6010.F02,T6036.F06,T6036.F40,T6036.F09,T6036.F08,T6036.F29,T6036.F19 FROM T6036 ");
		sql.append("INNER JOIN T6010 ON T6036.F02=T6010.F01 WHERE T6036.F20=?");
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(CreditStatus.YMB);
		if (query != null) {
			String account = query.getAccount();
			if (!StringHelper.isEmpty(account)) {
				sql.append(" AND T6010.F02 LIKE ?");
				parameters.add(getSQLConnectionProvider().allMatch(account));
			}
			int zqId = query.getZqId();
			if (zqId > 0) {
				sql.append(" AND T6036.F03 LIKE ?");
				parameters.add(getSQLConnectionProvider().allMatch(
						Integer.toString(zqId)));
			}
			CreditLevel level = query.getLevel();
			if (level != null) {
				sql.append(" AND T6036.F40=?");
				parameters.add(level);
			}
			InvestType investType = query.getType();
			if (investType != null) {
				switch (investType) {
				case XYRZB: {
					sql.append(" AND (T6036.F19 = ? OR T6036.F19 = ? )");
					parameters.add(CreditType.SYD);
					parameters.add(CreditType.XJD);
					break;
				}
				case JGDBB: {
					sql.append(" AND T6036.F19 = ?");
					parameters.add(CreditType.XYDB);
					break;
				}
				case SDRZB: {
					sql.append(" AND T6036.F19 = ?");
					parameters.add(CreditType.SDRZ);
					break;
				}
				default:
					break;
				}
			}
			Timestamp datetime = query.getStartExpireDatetime();
			if (datetime != null) {
				sql.append(" AND DATE(T6036.F29) >=?");
				parameters.add(datetime);
			}
			datetime = query.getEndExpireDatetime();
			if (datetime != null) {
				sql.append(" AND DATE(T6036.F29) <= ?");
				parameters.add(datetime);
			}
		}
		sql.append(" ORDER BY T6036.F29 DESC");
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectPaging(connection,
					new ArrayParser<Fksh>() {
						ArrayList<Fksh> lists = new ArrayList<Fksh>();

						@Override
						public Fksh[] parse(ResultSet rst) throws SQLException {
							while (rst.next()) {
								Fksh loanCheck = new Fksh();
								loanCheck.id = rst.getInt(1);
								loanCheck.zqId = rst.getInt(2);
								loanCheck.title = rst.getString(3);
								loanCheck.accountName = rst.getString(4);
								loanCheck.loanAmount = rst.getBigDecimal(5);
								loanCheck.level = EnumParser.parse(
										CreditLevel.class, rst.getString(6));
								loanCheck.proportion = rst.getBigDecimal(7);
								loanCheck.day = rst.getInt(8);
								loanCheck.expireDatetime = rst.getTimestamp(9);
								CreditType type = EnumParser.parse(
										CreditType.class, rst.getString(10));
								if (type == CreditType.XJD
										|| type == CreditType.SYD) {
									loanCheck.type = InvestType.XYRZB;
								} else if (type == CreditType.SDRZ) {
									loanCheck.type = InvestType.SDRZB;
								} else if (type == CreditType.XYDB) {
									loanCheck.type = InvestType.JGDBB;
								}
								lists.add(loanCheck);
							}
							return lists.toArray(new Fksh[lists.size()]);
						}
					}, paging, sql.toString(), parameters);
		}
	}

	@Override
	public void checkBatch(int[] ids, CreditStatus status) throws Throwable {
		if (ids == null || ids.length <= 0) {
			throw new ParameterException("指定的记录ID不存在.");
		}
		for (int id : ids) {
			if (id <= 0) {
				continue;
			}
			check(id, status, "");
		}
	}

	private boolean checkStatus(CreditStatus status, int id) throws Throwable {
		String select = "SELECT F20 FROM T6036 WHERE F01=? FOR UPDATE";
		CreditStatus oldStatus = null;
		try (Connection connection = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = connection.prepareStatement(select)) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						oldStatus = EnumParser.parse(CreditStatus.class,
								rs.getString(1));
					}
				}
			}
		}
		if (oldStatus == null) {
			return true;
		}
		if (status == CreditStatus.YFK && oldStatus != CreditStatus.YMB) {
			return true;
		}
		if (status == CreditStatus.LB && oldStatus != CreditStatus.YMB) {
			return true;
		}
		return false;
	}

	@Override
	public void check(int id, CreditStatus status, String des) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("指定的记录ID不存在.");
		}
		if (checkStatus(status, id)) {
			return;
		}
		try(Connection connection = getConnection())
		{
			try {
				serviceResource.openTransactions(connection);
				String sql = "UPDATE S60.T6036 SET F20=?,F30=?,F31=?,F33=? WHERE F01=?";
				execute(connection,
						sql,
						status,
						serviceResource.getSession().getAccountId(),
						new Timestamp(System.currentTimeMillis()),
						des,
						id);
				// 查询借款人信息
				Jkr jkr = getJkr(id, connection);
				String selectT6021 = "SELECT F04 FROM S60.T6021 WHERE F01=? FOR UPDATE";
				selectBigDecimal(connection, selectT6021, jkr.userId);
				String selectT6037 =
						"SELECT T6037.F03,T6037.F04,T6036.F04 AS TITLE FROM S60.T6037 INNER JOIN S60.T6036 ON T6037.F02=T6036.F01 WHERE T6037.F02=? ORDER BY T6037.F05 DESC";
				// 查询所有投资人
				List<Tzr> holders = new ArrayList<>();
				try (PreparedStatement ps = connection
						.prepareStatement(selectT6037)) {
					ps.setInt(1, id);
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							Tzr tzr = new Tzr();
							tzr.userId = rs.getInt(1);
							tzr.amount = rs.getBigDecimal(2);
							tzr.title = rs.getString(3);
							holders.add(tzr);
						}
					}
				}

				if (status == CreditStatus.YFK)
				{
					// 查询推广人信息
					List<Tg> lists = getTg(id);
					String selectT6028 = "SELECT F10 FROM S60.T6028 WHERE F01=?";
					execute(connection, selectT6028, jkr.userId);
					String selectT6029 = "SELECT F01 FROM S60.T6029 WHERE F01=? FOR UPDATE";
					execute(connection, selectT6029, jkr.userId);
					String selectT6045 = "SELECT F09 FROM S60.T6045 WHERE F01=? FOR UPDATE";
					execute(connection, selectT6045, jkr.userId);
					String selectT7025 = "SELECT F01 FROM S60.T7025 FOR UPDATE";
					selectBigDecimal(P2PConst.DB_CONSOLE, selectT7025);
					String selectT7027 = "SELECT F01 FROM S70.T7027 FOR UPDATE";
					BigDecimal t7027TotalAmount = selectBigDecimal(P2PConst.DB_CONSOLE, selectT7027);
					int jgId = getJdId(jkr.contrantId);
					String selectT7029 = "SELECT F12 FROM S70.T7029 WHERE F01=? FOR UPDATE";
					BigDecimal t7029TotalAmount = selectBigDecimal(P2PConst.DB_CONSOLE, selectT7029, jgId);
					// 扣除投资人金额
					kcHolderAmount(id, jkr.mfje, holders, connection);
					// 添加借款人金额
					addJkrAmount(id, jkr, t7027TotalAmount, t7029TotalAmount, jgId, connection);
					// 生成还款记录
					execPayMent(id, jkr, connection);
					// 生成债权人收款记录
					execSkjl(id, jkr, connection);
					// 添加推广金额
					addTg(id, jkr.userId, lists, connection);
					// 更新自动投资设置
					String updateT6028 = "UPDATE S60.T6028 SET F10=? WHERE F01=?";
					execute(connection, updateT6028, T6028_F10.TY, id);
					// 更新用户借款统计表
					String updateT6045 = "UPDATE S60.T6045 SET F09=F09+? WHERE F01=?";
					execute(connection, updateT6045, 1, jkr.userId);
				}
				else if (status == CreditStatus.LB)
				{
					// 添加用户可用信用额度
					String updateT6021 = "UPDATE S60.T6021 SET F04=F04+? WHERE F01=?";
					execute(connection, updateT6021, jkr.amount, jkr.userId);
					String selectT7031 = "SELECT F09 FROM S70.T7031 WHERE F01=? FOR UPDATE";
					execute(connection, selectT7031, jkr.contrantId);
					// 解冻投资人资金
					jdHolderAmount(id, holders);
					if (jkr.contrantId > 0)
					{
						String updateT7031 = "UPDATE S70.T7031 SET F09=F09+? WHERE F01=?";
						execute(connection, updateT7031, jkr.amount, jkr.contrantId);
					}
				}
				// 放款站内信
				sendLetter(id, status, connection);
				serviceResource.commit(connection);
			}catch (Exception e){
				serviceResource.rollback(connection);
				throw e;
			}
		}
	}

	/**
	 * 生成还款记录
	 * 
	 * @throws SQLException
	 */
    private void execPayMent(int loadId, Jkr jkr,Connection connection) throws SQLException {
		if (jkr == null) {
			return;
		}
		String selectT6041 = "SELECT COUNT(*) FROM S60.T6041 WHERE F02=?";
		String insertT6041 = "INSERT INTO S60.T6041 SET F02=?,F03=?,F04=?,F05=?,F06=?,F09=?,F10=DATE_ADD(CURDATE(),INTERVAL ? MONTH),F12=?";
		String updateT6036 = "UPDATE S60.T6036 SET F13 =?,F23 =DATE_ADD(CURDATE(),INTERVAL 1 MONTH),F24 = ? WHERE F01 = ?";
		int count = 0;
        try (PreparedStatement ps = connection.prepareStatement(selectT6041))
        {
            ps.setInt(1, loadId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    count = rs.getInt(1);
				}
			}
		}
		if (count >= jkr.days) {
			return;
		}
		if (jkr.repayType != RepaymentType.DEBX) {
			return;
		}
		// 将金额拆分成N个100
		BigDecimal onehundred = new BigDecimal(100);
		if (jkr.amount.compareTo(onehundred) < 0) {
			return;
		}
		final ConfigureProvider config = serviceResource
				.getResource(ConfigureProvider.class);
		// 借款管理费利率
		BigDecimal ration = new BigDecimal(
				config.get(SystemVariable.LMONEY_SUCCESS_RATION.getKey()));
		// 多少个100
		BigDecimal num = jkr.amount.divide(onehundred, BigDecimal.ROUND_DOWN);
		// 月利率
		double monthRate = jkr.yearRate / 12;
		BigDecimal bigDecimal = onehundred.multiply(new BigDecimal(monthRate));
		double bigDecimal2 = Math.pow(1 + monthRate, jkr.days);
		BigDecimal bigDecimal3 = bigDecimal
				.multiply(new BigDecimal(bigDecimal2));
		double bigDecimal4 = Math.pow(1 + monthRate, jkr.days) - 1;
		// 每月还款本息
		BigDecimal payAmount = bigDecimal3.divide(new BigDecimal(bigDecimal4),
				BigDecimal.ROUND_DOWN).setScale(2, BigDecimal.ROUND_HALF_UP);
        execute(connection, updateT6036, payAmount.multiply(num), jkr.days, loadId);
		int curPeriod = 1;
		BigDecimal amount_remain = onehundred;
		BigDecimal totalYhbj = new BigDecimal(0);
		// 总共借款管理费
		BigDecimal jkglf = new BigDecimal(0);
		// 总共本金
		BigDecimal dhbj = new BigDecimal(0);
		// 总共利息
		BigDecimal dhlx = new BigDecimal(0);
		while (curPeriod <= jkr.days) {
			BigDecimal amount_cur = payAmount.subtract(
					amount_remain.multiply(new BigDecimal(monthRate)))
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			amount_remain = amount_remain.subtract(amount_cur).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			BigDecimal interest_cur = payAmount.subtract(amount_cur).setScale(
					2, BigDecimal.ROUND_HALF_UP);
			if (curPeriod < jkr.days) {
				totalYhbj = totalYhbj.add(amount_cur).setScale(2,
						BigDecimal.ROUND_HALF_UP);
			}
			if (curPeriod == jkr.days) {
				amount_cur = onehundred.subtract(totalYhbj).setScale(2,
						BigDecimal.ROUND_HALF_UP);
				interest_cur = amount_cur.multiply(new BigDecimal(monthRate))
						.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
            execute(connection,
                insertT6041,
                loadId,
                jkr.userId,
                curPeriod,
                amount_cur.multiply(num),
                interest_cur.multiply(num),
                jkr.amount.multiply(ration),
                curPeriod,
                RepayStatus.WH);
			jkglf = jkglf.add(jkr.amount.multiply(ration));
			dhbj = dhbj.add(amount_cur.multiply(num));
			dhlx = dhlx.add(interest_cur.multiply(num));
			curPeriod++;
		}
        BigDecimal dhje =
            selectBigDecimal(connection,
				"SELECT SUM(F05+F06+F07+F08+F09) FROM S60.T6041 WHERE F12=? AND F03=?",
				T6041_F12.WH, jkr.userId);
		// 更新用户档案信息
		String updateT6045 = "UPDATE S60.T6045 SET F05=? WHERE F01=?";
        execute(connection, updateT6045, dhje, jkr.userId);
		// 更新我的借款统计表
		String updateT6029 = "UPDATE S60.T6029 SET F02=F02+?,F04=F04+?,F05=F05+?,F07=F07+?,F12=F12+? WHERE F01=?";
        execute(connection, updateT6029, jkr.amount, jkglf, dhbj, dhlx, jkglf, jkr.userId);
	}

	/**
	 * 生成债权人收款记录
	 * 
	 * @throws SQLException
	 */
    private void execSkjl(int loadId, Jkr jkr,Connection connection) throws SQLException {
		if (jkr == null) {
			return;
		}
		String selectT6041 = "SELECT COUNT(*) FROM S60.T6056 WHERE F02=?";
		String insertT6041 = "INSERT INTO S60.T6056 SET F02=?,F03=?,F04=?,F05=?,F06=?,F08=DATE_ADD(CURDATE(),INTERVAL ? MONTH),F10=?,F11=?,F12=?,F13=?";
		int count = 0;
        try (PreparedStatement ps = connection.prepareStatement(selectT6041))
        {
            ps.setInt(1, loadId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    count = rs.getInt(1);
				}
			}
		}
		if (count >= jkr.days) {
			return;
		}
		if (jkr.repayType != RepaymentType.DEBX) {
			return;
		}
		String selectT6038 = "SELECT F03,F10 FROM S60.T6038 WHERE F02=?";
        try (PreparedStatement ps = connection.prepareStatement(selectT6038))
        {
            ps.setInt(1, loadId);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    int tzrUserId = rs.getInt(1);
                    BigDecimal tzrAmount = rs.getBigDecimal(2);
                    // 将金额拆分成N个100
                    BigDecimal onehundred = new BigDecimal(100);
                    if (tzrAmount.compareTo(onehundred) < 0)
                    {
                        return;
                    }
                    // 多少个100
                    BigDecimal num = tzrAmount.divide(onehundred, BigDecimal.ROUND_DOWN);
                    // 月利率
                    double monthRate = jkr.yearRate / 12;
                    BigDecimal bigDecimal = onehundred.multiply(new BigDecimal(monthRate));
                    double bigDecimal2 = Math.pow(1 + monthRate, jkr.days);
                    BigDecimal bigDecimal3 = bigDecimal.multiply(new BigDecimal(bigDecimal2));
                    double bigDecimal4 = Math.pow(1 + monthRate, jkr.days) - 1;
                    // 每月还款本息
                    BigDecimal payAmount =
                        bigDecimal3.divide(new BigDecimal(bigDecimal4), BigDecimal.ROUND_DOWN).setScale(2,
                            BigDecimal.ROUND_HALF_UP);
                    int curPeriod = 1;
                    BigDecimal amount_remain = onehundred;
                    BigDecimal totalYhbj = new BigDecimal(0);
                    while (curPeriod <= jkr.days)
                    {
                        BigDecimal amount_cur =
                            payAmount.subtract(amount_remain.multiply(new BigDecimal(monthRate))).setScale(2,
                                BigDecimal.ROUND_HALF_UP);
                        amount_remain = amount_remain.subtract(amount_cur).setScale(2, BigDecimal.ROUND_HALF_UP);
                        BigDecimal interest_cur = payAmount.subtract(amount_cur).setScale(2, BigDecimal.ROUND_HALF_UP);
                        if (curPeriod < jkr.days)
                        {
                            totalYhbj = totalYhbj.add(amount_cur).setScale(2, BigDecimal.ROUND_HALF_UP);
						}
                        if (curPeriod == jkr.days)
                        {
                            amount_cur = onehundred.subtract(totalYhbj)
									.setScale(2, BigDecimal.ROUND_HALF_UP);
                            interest_cur =
                                amount_cur.multiply(new BigDecimal(monthRate)).setScale(2,
									BigDecimal.ROUND_HALF_UP);
						}
                        execute(connection,
                            insertT6041,
                            loadId,
                            tzrUserId,
                            curPeriod,
                            amount_cur.multiply(num),
                            interest_cur.multiply(num),
                            curPeriod,
                            DsStatus.WS,
                            num,
                            amount_cur,
                            interest_cur);
                        curPeriod++;
					}
				}
			}
		}
	}

	/**
	 * 添加推广奖励
	 */
    private void addTg(int loanId, int userId, List<Tg> lists, Connection connection)
        throws Throwable
    {
		final ConfigureProvider config = serviceResource
				.getResource(ConfigureProvider.class);
		BigDecimal jsAmount = BigDecimalParser.parse(config
				.get(SystemVariable.TG_TZJS.getKey()));
		BigDecimal jlAmount = BigDecimalParser.parse(config
				.get(SystemVariable.TG_TZJL.getKey()));
		for (Tg tg : lists) {
			if (tg == null || tg.tgId <= 0) {
				continue;
			}
			int num = tg.amount.divide(jsAmount, BigDecimal.ROUND_DOWN)
					.intValue();
			if (jsAmount.compareTo(tg.amount) <= 0) {
				tg.jlAmount = jlAmount.multiply(new BigDecimal(num));
			}
			// 添加推广表记录
			String insertT6055 = "INSERT INTO S60.T6055 SET F02=?,F03=?,F04=?,F05=?,F06=?";
            insert(connection, insertT6055, tg.tgId,
					tg.btgId, tg.amount, tg.jlAmount, tg.time);
			if (tg.jlAmount.compareTo(new BigDecimal(0)) > 0) {
				String updateT6053 = "UPDATE S60.T6053 SET F03=F03+?,F04=F04+? WHERE F01=?";
                execute(connection, updateT6053,
						tg.amount, tg.jlAmount, tg.tgId);
				// 添加推广人金额
				String updateT6023 = "UPDATE S60.T6023 SET F03=F03+?,F05=F05+? WHERE F01=?";
                execute(connection, updateT6023,
						tg.jlAmount, tg.jlAmount, tg.tgId);
                BigDecimal acountAmount =
                    selectBigDecimal(connection,
						"SELECT F03 FROM S60.T6023 WHERE F01 = ?", tg.tgId);
				String insertT6032 = "INSERT INTO S60.T6032 SET F02=?,F03=?,F04=?,F05=?,F07=?,F08=?,F09=?";
                execute(connection,
                    insertT6032,
                    tg.tgId,
                    TradingType.CXTGJL,
                    new Timestamp(System.currentTimeMillis()),
                    tg.jlAmount,
                    acountAmount,
                    loanId,
                    tg.title);
				// 投资人推广奖励站内信
                sendTzrTg(tg.tgId, tg.amount, tg.jlAmount, connection);
				// 扣除平台账号金额
				String updateT7025 = "UPDATE S70.T7025 SET F01=F01-?,F02=F02+?,F04=F04-?";
                execute(connection, updateT7025, tg.jlAmount, tg.jlAmount, tg.jlAmount);
				String insertT7026 = "INSERT INTO S70.T7026 SET F02=?,F04=?,F05=?,F06=?,F07=?,F08=?,F09=?";
				String selectT7025 = "SELECT F01 FROM S70.T7025";
                BigDecimal ptTotalAmount = selectBigDecimal(connection, selectT7025);
                execute(connection,
                    insertT7026,
                    new Timestamp(System.currentTimeMillis()),
                    tg.jlAmount,
                    ptTotalAmount,
                    PlatformFundType.TGCXJL,
                    "推广持续奖励扣除:" + Formater.formatAmount(tg.jlAmount) + "元",
                    loanId,
                    tg.tgId);
			}
		}
	}

	private List<Tg> getTg(int loanId) throws SQLException {
		String sql = "SELECT T6037.F04,T6037.F05,T6011.F01,T6011.F10,T6036.F04 AS TITLE FROM S60.T6037 INNER JOIN S60.T6036 ON T6037.F02=T6036.F01 INNER JOIN S60.T6011 ON T6037.F03=T6011.F01 WHERE T6037.F02=?";
		String sql_1 = "SELECT F01 FROM S60.T6011 WHERE F11=?";
		String selectT6023 = "SELECT F05 FROM S60.T6023 WHERE F01=? FOR UPDATE";
		String selectT6053 = "SELECT F01 FROM S60.T6053 WHERE F01=? FOR UPDATE";
		List<Tg> lists = new ArrayList<>();
		try (Connection connection = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, loanId);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Tg tg = new Tg();
						tg.amount = rs.getBigDecimal(1);
						tg.time = rs.getTimestamp(2);
						tg.btgId = rs.getInt(3);
						tg.code = rs.getString(4);
						tg.title = rs.getString(5);
						lists.add(tg);
					}
				}
			}
			for (Tg tg : lists) {
				// 查询推广人
				try (PreparedStatement ps = connection.prepareStatement(sql_1)) {
					ps.setString(1, tg.code);
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							tg.tgId = rs.getInt(1);
							execute(connection, selectT6023, tg.tgId);
							execute(connection, selectT6053, tg.tgId);
						}
					}
				}
			}
		}
		return lists;
	}

    private Jkr getJkr(int loanId, Connection connection)
        throws SQLException
    {
		String sql = "SELECT T6036.F02,T6036.F06,T6036.F40,T6036.F19,T6036.F25,T6036.F08,T6036.F09,T6036.F31,T6036.F11,T6036.F37,T6036.F04 FROM S60.T6036 WHERE T6036.F01=? FOR UPDATE";
		Jkr jkr = select(connection, new ItemParser<Jkr>() {

			@Override
			public Jkr parse(ResultSet resultSet) throws SQLException {
				Jkr jkr = new Jkr();
				if (resultSet.next()) {
					jkr.userId = resultSet.getInt(1);
					jkr.amount = resultSet.getBigDecimal(2);
					jkr.level = EnumParser.parse(CreditLevel.class,
							resultSet.getString(3));
					jkr.type = EnumParser.parse(CreditType.class,
							resultSet.getString(4));
					jkr.contrantId = resultSet.getInt(5);
					jkr.days = resultSet.getInt(6);
					jkr.yearRate = resultSet.getDouble(7);
					jkr.fkDate = resultSet.getTimestamp(8);
					jkr.repayType = EnumParser.parse(RepaymentType.class,
							resultSet.getString(9));
					jkr.mfje = resultSet.getBigDecimal(10);
					jkr.title = resultSet.getString(11);
				}
				return jkr;
			}
		}, sql, loanId);
		String selectT6023 = "SELECT F05 FROM S60.T6023 WHERE F01=? FOR UPDATE";
		if (jkr.amount != null) {
			// String rate = "";
			// switch (jkr.level) {
			// case AA: {
			// rate =
			// config.get(SystemVariable.SUCCESS_BMONEY_RATE_AA
			// .getKey());
			// break;
			// }
			// case A: {
			// rate =
			// config.get(SystemVariable.SUCCESS_BMONEY_RATE_A
			// .getKey());
			// break;
			// }
			// case B: {
			// rate =
			// config.get(SystemVariable.SUCCESS_BMONEY_RATE_B
			// .getKey());
			// break;
			// }
			// case C: {
			// rate =
			// config.get(SystemVariable.SUCCESS_BMONEY_RATE_C
			// .getKey());
			// break;
			// }
			// case D: {
			// rate =
			// config.get(SystemVariable.SUCCESS_BMONEY_RATE_D
			// .getKey());
			// break;
			// }
			// case E: {
			// rate =
			// config.get(SystemVariable.SUCCESS_BMONEY_RATE_E
			// .getKey());
			// break;
			// }
			// case HR: {
			// rate =
			// config.get(SystemVariable.SUCCESS_BMONEY_RATE_HR
			// .getKey());
			// break;
			// }
			// default:
			// break;
			// }
			// cjfwf = jkr.amount.multiply(BigDecimalParser.parse(rate));
			// jkr.cjfwf = cjfwf;
		}
		jkr.acountAmount = selectBigDecimal(connection, selectT6023, jkr.userId);
		return jkr;
	}

	/**
	 * 添加借款人金额,扣除手续费
	 * 
	 * @param loadId
	 */
    private void addJkrAmount(int loanId, Jkr jkr, BigDecimal t7027TotalAmount, BigDecimal t7029TotalAmount, int jgId,
        Connection connection)
        throws Throwable
    {
		String selectT6023 = "SELECT F03 FROM S60.T6023 WHERE F01=?";
        BigDecimal amount = selectBigDecimal(connection, selectT6023, jkr.userId);
		String updateT6023_1 = "UPDATE S60.T6023 SET F03=F03+?,F05=F05+? WHERE F01=?";
		String updateT6023_2 = "UPDATE S60.T6023 SET F03=F03-?,F05=F05-? WHERE F01=?";
		execute(connection, updateT6023_1, jkr.amount, jkr.amount, jkr.userId);
		execute(connection, updateT6023_2, jkr.cjfwf, jkr.cjfwf, jkr.userId);
		String insertT6032 = "INSERT INTO S60.T6032 SET F02=?,F03=?,F04=?,F05=?,F07=?,F08=?,F09=?";
		amount = amount.add(jkr.amount);
		execute(connection, insertT6032, jkr.userId, TradingType.ZBCG,
				new Timestamp(System.currentTimeMillis()), jkr.amount, amount,
				loanId, jkr.title);
		if (jkr.cjfwf.compareTo(new BigDecimal(0)) > 0) {
			amount = amount.subtract(jkr.cjfwf);
			insertT6032 = "INSERT INTO S60.T6032 SET F02=?,F03=?,F04=?,F06=?,F07=?,F08=?,F09=?";
			execute(connection, insertT6032, jkr.userId, TradingType.CJFWF,
					new Timestamp(System.currentTimeMillis()), jkr.cjfwf,
					amount, loanId, jkr.title + ":平台成交服务费");
		}
		// 添加成交服务费
        addCjf(loanId, jkr, t7027TotalAmount, t7029TotalAmount, jgId, amount, connection);
	}

	/**
	 * 添加成交服务费
	 * 
	 * @param loadId
	 * @param jkr
	 */
    private void addCjf(int loanId, Jkr jkr, BigDecimal t7027TotalAmount, BigDecimal t7029TotalAmount, int jgId,
        BigDecimal amount, Connection connection)
			throws Throwable {
		if (jkr.cjfwf.compareTo(new BigDecimal(0)) <= 0) {
			return;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String updateT7027 = "UPDATE S70.T7027 SET F01=F01+?,F03=F03+?,F04=F04+?,F05=?";
        execute(connection, updateT7027, jkr.cjfwf, jkr.cjfwf, jkr.cjfwf,
				now);
		String insertT7028 = "INSERT INTO S70.T7028 SET F02=?,F03=?,F05=?,F06=?,F07=?,F08=?,F09=?";
        execute(connection,
            insertT7028,
            now,
            jkr.cjfwf,
            t7027TotalAmount.add(jkr.cjfwf),
            TradeType.JKCJFWF,
            "平台借款成交服务费:" + Formater.formatAmount(jkr.cjfwf) + "元",
            loanId,
            jkr.userId);
		if (jkr.type == CreditType.SDRZ || jkr.type == CreditType.XYDB) {
            double jdFwfl = getJdFwfl(jkr.contrantId, connection);
			BigDecimal temp = jkr.amount.multiply(new BigDecimal(jdFwfl));
			String updateT7029 = "UPDATE S70.T7029 SET F08=?,F12=F12+? WHERE F01=?";
            execute(connection, updateT7029, now, temp, jgId);
			String insertT7030 = "INSERT INTO S70.T7030 SET F02=?,F03=?,F04=?,F06=?,F07=?,F08=?,F09=?,F10=?";
            execute(connection,
                insertT7030,
                jgId,
                now,
                temp,
                t7029TotalAmount.add(temp),
                TradeType.JKCJFWF,
                "机构借款成交服务费:" + Formater.formatAmount(temp) + "元",
                loanId,
                jkr.userId);
			String updateT6023 = "UPDATE S60.T6023 SET F03=F03-?,F05=F05-? WHERE F01=?";
            execute(connection, updateT6023, temp, temp,
					jkr.userId);
			String insertT6032 = "INSERT INTO S60.T6032 SET F02=?,F03=?,F04=?,F06=?,F07=?,F08=?,F09=?";
			amount = amount.subtract(temp);
            execute(connection,
                insertT6032,
                jkr.userId,
                TradingType.CJFWF,
                new Timestamp(System.currentTimeMillis()),
                temp,
                amount,
                loanId,
                jkr.title + ":机构成交服务费");
		}
	}

	/**
	 * 根据合同找机构
	 * 
	 * @param contranctId
	 * @return
	 * @throws Throwable
	 */
	private int getJdId(int contranctId) throws Throwable {
		String t7029 = "SELECT F02 FROM T7031 WHERE F01=?";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(t7029)) {
				ps.setInt(1, contranctId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 根据合同找机构服务费率
	 * 
	 * @param contranctId
	 * @return
	 * @throws Throwable
	 */
    private double getJdFwfl(int contranctId, Connection connection)
        throws Throwable
    {
		String t7029 = "SELECT F07 FROM T7031 WHERE F01=?";
        try (PreparedStatement ps = connection.prepareStatement(t7029))
        {
            ps.setInt(1, contranctId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getDouble(1);
				}
			}
		}
		return 0;
	}

	/**
	 * 放款完成扣除投资人金额
	 * 
	 * @param info
	 * @throws Throwable
	 */
    private void kcHolderAmount(int loanId, BigDecimal mfje, List<Tzr> holders, Connection connection)
			throws Throwable {
		String updateT6023 = "UPDATE S60.T6023 SET F03=F03-?,F04=F04-? WHERE F01=?";
		String insertT6032 = "INSERT INTO S60.T6032 SET F02=?,F03=?,F04=?,F06=?,F07=?,F08=?,F09=?";
		String insertT6038 = "INSERT INTO S60.T6038 SET F02=?,F03=?,F04=?,F05=?,F06=?,F07=?,F08=?,F10=?";
		String selectT6023 = "SELECT F03 FROM S60.T6023 WHERE F01=? FOR UPDATE";
		String selectT6037 = "SELECT F03,SUM(F04) FROM S60.T6037 WHERE F02=? GROUP BY F03 ORDER BY F05 DESC";
		Timestamp now = new Timestamp(System.currentTimeMillis());
        try (PreparedStatement ps = connection.prepareStatement(selectT6037))
        {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    int tzrId = rs.getInt(1);
                    BigDecimal amount = rs.getBigDecimal(2);
                    // 添加用户债权
                    execute(connection,
                        insertT6038,
                        loanId,
                        tzrId,
                        amount,
                        now,
                        now,
                        T6038_F07.F,
                        amount.divide(mfje, BigDecimal.ROUND_DOWN).intValue(),
                        amount);
				}
			}
        }
        for (Tzr tzr : holders)
        {
            if (tzr == null)
            {
                continue;
			}
            BigDecimal amount = tzr.amount;
            int tzrId = tzr.userId;
            execute(connection, updateT6023, amount, amount, tzrId);
            BigDecimal accountAmount = selectBigDecimal(connection, selectT6023, tzrId);
            execute(connection, insertT6032, tzrId, TradingType.TBCG, now, amount, accountAmount, loanId, tzr.title);
            // 站内信
            sendRecovery(loanId, tzrId, LetterVariable.TZR_TBCG, connection);
		}
	}

	/**
	 * 放款失败解冻投资人冻结金额
	 * 
	 * @param info
	 * @throws Throwable
	 */
	private void jdHolderAmount(int loanId, List<Tzr> holders) throws Throwable {
		String selectT6023 = "SELECT F03 FROM S60.T6023 WHERE F01=? FOR UPDATE";
		String updateT6023 = "UPDATE S60.T6023 SET F04=F04-?,F05=F05+? WHERE F01=?";
		try(Connection connection = getConnection(P2PConst.DB_USER)) {
			for (Tzr tzr : holders) {
				BigDecimal amount = tzr.amount;
				selectBigDecimal(P2PConst.DB_USER, selectT6023, tzr.userId);
				execute(connection, updateT6023, amount, amount, tzr.userId);
				// 站内信
				sendRecovery(loanId, tzr.userId, LetterVariable.TZR_TBLB, connection);
			}
		}
	}

	/**
	 * 投资人投资成功
	 * 
	 * @param id
	 * @throws Throwable
	 */
	private void sendRecovery(int loanId, int userId, LetterVariable variable, Connection connection)
			throws Throwable {
		String sql = "SELECT F04 FROM S60.T6036 WHERE F01=?";
		String title = "";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, loanId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					title = rs.getString(1);
				}
			}
		}

		ConfigureProvider configureProvider = serviceResource
				.getResource(ConfigureProvider.class);
		String template = configureProvider.getProperty(variable);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Envionment envionment = configureProvider.createEnvionment();
		envionment.set("title", title);
		int letterId = insert(connection, t6035, now,
				variable.getDescription(), "", userId, LetterStatus.WD);
		execute(connection, t6046, letterId,
				StringHelper.format(template, envionment));
	}

	/**
	 * 投资人推广奖励站内信
	 * 
	 * @param id
	 * @throws Throwable
	 */
    private void sendTzrTg(int userId, BigDecimal amount, BigDecimal jlAmount, Connection connection)
			throws Throwable {
		ConfigureProvider configureProvider = serviceResource
				.getResource(ConfigureProvider.class);
		String template = configureProvider.getProperty(LetterVariable.TG_CXJL);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Envionment envionment = configureProvider.createEnvionment();
		envionment.set("tz", Formater.formatAmount(amount));
		envionment.set("jl", Formater.formatAmount(jlAmount));
        int letterId =
            insert(connection, t6035, now, LetterVariable.TG_CXJL.getDescription(), "", userId, LetterStatus.WD);
        execute(connection, t6046, letterId, StringHelper.format(template, envionment));
	}

	/**
	 * 发站内信
	 * 
	 * @throws SQLException
	 * @throws ResourceNotFoundException
	 */
    private void sendLetter(int id, CreditStatus status, Connection connection)
        throws Throwable
    {
		String sql = "SELECT F02,F04 FROM S60.T6036 WHERE F01=?";
		Fksh fksh = new Fksh();
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    fksh.userId = rs.getInt(1);
                    fksh.title = rs.getString(2);
				}
			}
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		ConfigureProvider configureProvider = serviceResource
				.getResource(ConfigureProvider.class);
		if (status == CreditStatus.YFK) {
			String template = configureProvider
					.getProperty(LetterVariable.LOAN_SUCCESS);
			Envionment envionment = configureProvider.createEnvionment();
			envionment.set("datetime", TimestampParser.format(now));
			envionment.set("title", fksh.title);
			envionment.set("lookUrl", configureProvider.format(URLVariable.USER_CREDIT));
			int letterId = insert(connection, t6035, now,
					LetterVariable.LOAN_SUCCESS.getDescription(), "",
					fksh.userId, LetterStatus.WD);
            execute(connection, t6046, letterId, StringHelper.format(template, envionment));
		} else if (status == CreditStatus.LB) {
			String template = configureProvider
					.getProperty(LetterVariable.LOAN_FAILED);
			Envionment envionment = configureProvider.createEnvionment();
			envionment.set("datetime", TimestampParser.format(now));
			envionment.set("title", fksh.title);
			int letterId = insert(connection, t6035, now,
					LetterVariable.LOAN_FAILED.getDescription(), "",
					fksh.userId, LetterStatus.WD);
            execute(connection, t6046, letterId, StringHelper.format(template, envionment));
		}
	}

	@Override
	public PagingResult<CjRecord> search(CjRecordQuery query, Paging paging)
			throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT T6036.F01,T6036.F31,T6010.F02,T6036.F40,T6036.F19,T6036.F06,T6036.F08,T7011.F02 AS LOANNAME FROM S60.T6036 ");
		sql.append("INNER JOIN S60.T6010 ON T6036.F02=T6010.F01 INNER JOIN ")
				.append(P2PConst.DB_CONSOLE).append('.')
				.append("S70.T7011 ON T6036.F30=T7011.F01 ")
				.append("WHERE (T6036.F20=? OR T6036.F20=? OR T6036.F20=?)");
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(CreditStatus.YFK);
		parameters.add(CreditStatus.YDF);
		parameters.add(CreditStatus.YJQ);
		if (query != null) {
			String loanName = query.getLoanName();
			if (!StringHelper.isEmpty(loanName)) {
				sql.append(" AND T7011.F02 LIKE ?");
				parameters.add(getSQLConnectionProvider().allMatch(loanName));
			}
			CreditLevel level = query.getLevel();
			if (level != null) {
				sql.append(" AND T6036.F40 =?");
				parameters.add(level);
			}
			InvestType investType = query.getType();
			if (investType != null) {
				switch (investType) {
				case XYRZB: {
					sql.append(" AND (T6036.F19 = ? OR T6036.F19 = ? )");
					parameters.add(CreditType.SYD);
					parameters.add(CreditType.XJD);
					break;
				}
				case JGDBB: {
					sql.append(" AND T6036.F19 = ?");
					parameters.add(CreditType.XYDB);
					break;
				}
				case SDRZB: {
					sql.append(" AND T6036.F19 = ?");
					parameters.add(CreditType.SDRZ);
					break;
				}
				default:
					break;
				}
			}
			CreditTerm term = query.getTerm();
			if (term != null) {
				switch (term) {
				case SGYYX: {
					sql.append(" T6230.F09 < 3 ");
					break;
				}
				case SDLGY: {
					sql.append("(T6230.F09 >= 3 AND T6230.F09 <= 6)");
					break;
				}
				case LDJGY: {
					sql.append("(T6230.F09 >= 6 AND T6230.F09 <= 9)");
					break;
				}
				case JDSEGY: {
					sql.append("(T6230.F09 >= 9 AND T6230.F09 <= 12)");
					break;
				}
				case SEGYYS: {
					sql.append(" T6230.F09 > 12 ");
					break;
				}
				default:
					break;
				}
			}
			Timestamp datetime = query.getStartTime();
			if (datetime != null) {
				sql.append(" AND DATE(T6036.F31) >=?");
				parameters.add(datetime);
			}
			datetime = query.getEndTime();
			if (datetime != null) {
				sql.append(" AND DATE(T6036.F31) <= ?");
				parameters.add(datetime);
			}
		}
		sql.append(" ORDER BY T6036.F31 DESC");
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectPaging(connection,
					new ArrayParser<CjRecord>() {
						ArrayList<CjRecord> lists = new ArrayList<CjRecord>();

						@Override
						public CjRecord[] parse(ResultSet rst) throws SQLException {
							while (rst.next()) {
								CjRecord record = new CjRecord();
								record.id = rst.getInt(1);
								record.loanTime = rst.getTimestamp(2);
								record.accountName = rst.getString(3);
								record.level = EnumParser.parse(CreditLevel.class,
										rst.getString(4));
								CreditType type = EnumParser.parse(
										CreditType.class, rst.getString(5));
								if (type == CreditType.XJD
										|| type == CreditType.SYD) {
									record.type = InvestType.XYRZB;
								} else if (type == CreditType.SDRZ) {
									record.type = InvestType.SDRZB;
								} else if (type == CreditType.XYDB) {
									record.type = InvestType.JGDBB;
								}
								record.ammount = rst.getBigDecimal(6);
								record.day = rst.getInt(7);
								record.loanName = rst.getString(8);
								lists.add(record);
							}
							return lists.toArray(new CjRecord[lists.size()]);
						}
					}, paging, sql.toString(), parameters);
		}
	}

	@Override
	public BigDecimal totalAmount() throws Throwable {
		String sql = "SELECT SUM(F06) FROM S60.T6036 WHERE (F20=? OR F20=? OR F20=?)";
		return selectBigDecimal(P2PConst.DB_USER, sql, CreditStatus.YFK,
				CreditStatus.YDF, CreditStatus.YJQ);
	}

	@Override
	public void export(CjRecord[] records, OutputStream outputStream,
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
			writer.write("放款时间");
			writer.write(",");
			writer.write("借款账号");
			writer.write(",");
			writer.write("信用等级");
			writer.write(",");
			writer.write("借款类型");
			writer.write(",");
			writer.write("借款ID");
			writer.write(",");
			writer.write("借款金额(元)");
			writer.write(",");
			writer.write("借款期限");
			writer.write(",");
			writer.write("放款人");
			writer.newLine();
			for (CjRecord record : records) {
				if (record == null) {
					continue;
				}
				writer.write(" " + DateParser.format(record.loanTime));
				writer.write(",");
				writer.write(record.accountName == null ? ""
						: record.accountName);
				writer.write(",");
				writer.write(record.level != null ? record.level.getName() : "");
				writer.write(",");
				writer.write(record.type != null ? record.type.getName() : "");
				writer.write(",");
				writer.write(Integer.toString(record.id));
				writer.write(",");
				writer.write(format(record.ammount));
				writer.write(",");
				writer.write(Integer.toString(record.day) + "个月");
				writer.write(",");
				writer.write(record.loanName == null ? "" : record.loanName);
				writer.newLine();
			}
			writer.flush();
		}
	}
}
