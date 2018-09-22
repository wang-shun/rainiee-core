package com.dimeng.p2p.modules.capital.user.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.ChargeStatus;
import com.dimeng.p2p.common.enums.TradingType;
import com.dimeng.p2p.common.enums.WithdrawStatus;
import com.dimeng.p2p.modules.capital.user.service.TradingRecordManage;
import com.dimeng.p2p.modules.capital.user.service.entity.TradingRecordEntity;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;

public class TradingRecordManageImpl extends AbstractCapitalService implements
		TradingRecordManage {

	public TradingRecordManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
		getFunds();
	}

	protected BigDecimal balance = BigDecimal.ZERO;
	protected BigDecimal availableFunds = BigDecimal.ZERO;
	protected BigDecimal freezeFunds = BigDecimal.ZERO;

	protected void getFunds() {
		String sql = "SELECT F03,F04,F05 FROM T6023 WHERE F01=? LIMIT 1";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						balance = resultSet.getBigDecimal(1);
						freezeFunds = resultSet.getBigDecimal(2);
						availableFunds = resultSet.getBigDecimal(3);
					}
				}
			}
		} catch (Exception e) {
		    logger.error(e,e);
		}
	}

	@Override
	public BigDecimal balance() throws Throwable {
		return balance;
	}

	@Override
	public BigDecimal availableFunds() throws Throwable {
		return availableFunds;
	}

	@Override
	public BigDecimal freezeFunds() throws Throwable {
		return freezeFunds;
	}

	@Override
	public BigDecimal rechargeFunds() throws Throwable {
		BigDecimal rechargeFunds = BigDecimal.ZERO;
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT IFNULL(SUM(F04),0) FROM T6033 WHERE F02=? AND F05=?")) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				ps.setString(2, ChargeStatus.ZFCG.toString());
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						rechargeFunds = resultSet.getBigDecimal(1);
					}
				}
			}
		}
		return rechargeFunds;
	}

	@Override
	public BigDecimal withdrawFunds() throws Throwable {
		BigDecimal withdrawFunds = BigDecimal.ZERO;
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT IFNULL(SUM(F04),0) FROM T6034 WHERE F02=? AND F07=?")) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				ps.setString(2, WithdrawStatus.TXCG.name());
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						withdrawFunds = resultSet.getBigDecimal(1);
					}
				}
			}
		}
		return withdrawFunds;
	}

	@Override
	public PagingResult<TradingRecordEntity> search(TradingType tradingType,
 Date startTime, Date endTime, Paging paging)
        throws Throwable
    {
        StringBuilder builder = new StringBuilder("SELECT F01,F03,F04,F05,F06,F07,F09 FROM T6032 WHERE F02=?");
        List<Object> params = new ArrayList<>();
        params.add(serviceResource.getSession().getAccountId());
        {
            if (tradingType != null)
            {
                builder.append(" AND F03=?");
                params.add(tradingType.toString());
            }
            if (startTime != null)
            {
                builder.append(" AND DATE(F04)>=?");
                params.add(DateParser.format(startTime));
            }
            if (endTime != null)
            {
                builder.append(" AND DATE(F04)<=?");
                params.add(DateParser.format(endTime));
            }
        }
        builder.append(" ORDER BY F01 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<TradingRecordEntity>()
            {
                
                @Override
                public TradingRecordEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<TradingRecordEntity> records = null;
                    while (resultSet.next())
                    {
                        if (records == null)
                        {
                            records = new ArrayList<>();
                        }
                        TradingRecordEntity record = new TradingRecordEntity();
                        record.id = resultSet.getInt(1);
                        record.type = EnumParser.parse(TradingType.class, resultSet.getString(2));
                        record.tradeTime = resultSet.getTimestamp(3);
                        record.amountIn = resultSet.getBigDecimal(4);
                        record.amountOut = resultSet.getBigDecimal(5);
                        record.balance = resultSet.getBigDecimal(6);
                        record.remark = resultSet.getString(7);
                        records.add(record);
                    }
                    return records == null ? null : records.toArray(new TradingRecordEntity[records.size()]);
                }
            }, paging, builder.toString(), params);
        }
    }

	@Override
	public void export(TradingRecordEntity[] records,
			OutputStream outputStream, String charset) throws Throwable {
		if (outputStream == null) {
			return;
		}
		if (records == null) {
			return;
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			CVSWriter writer = new CVSWriter(out);
			writer.write("交易时间");
			writer.write("交易类型");
			writer.write("收入");
			writer.write("支出");
			writer.write("结余");
			writer.write("备注");
			writer.newLine();
			for (TradingRecordEntity record : records) {
				writer.write(DateTimeParser.format(record.tradeTime)+ "\t");
				writer.write(record.type.getName());
				writer.write(record.amountIn.toString());
				writer.write(record.amountOut.toString());
				writer.write(record.balance.toString());
				writer.write(record.remark);
				writer.newLine();
			}
		}
	}

	public static class TradingRecordManageFactory implements
			ServiceFactory<TradingRecordManage> {

		@Override
		public TradingRecordManage newInstance(ServiceResource serviceResource) {
			return new TradingRecordManageImpl(serviceResource);
		}

	}

}
