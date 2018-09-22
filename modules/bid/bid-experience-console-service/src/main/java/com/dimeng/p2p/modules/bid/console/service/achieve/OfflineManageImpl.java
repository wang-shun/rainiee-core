/**
 * 
 */
package com.dimeng.p2p.modules.bid.console.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6234;
import com.dimeng.p2p.S62.entities.T6235;
import com.dimeng.p2p.S62.entities.T6236;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.entities.T6240;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.enums.T6211_F03;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F27;
import com.dimeng.p2p.S62.enums.T6236_F04;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.modules.bid.console.service.OfflineManage;
import com.dimeng.p2p.modules.bid.console.service.entity.Bid;
import com.dimeng.p2p.modules.bid.console.service.entity.BidDyw;
import com.dimeng.p2p.modules.bid.console.service.entity.BidDywsx;
import com.dimeng.p2p.modules.bid.console.service.entity.Dbxx;
import com.dimeng.p2p.modules.bid.console.service.entity.Hkjllb;
import com.dimeng.p2p.modules.bid.console.service.entity.TbRecord;
import com.dimeng.p2p.modules.bid.console.service.entity.Tztjxx;
import com.dimeng.p2p.modules.bid.console.service.entity.Zqzrxx;
import com.dimeng.p2p.modules.bid.console.service.query.LoanQuery;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * @author guopeng
 * 
 */
public class OfflineManageImpl extends AbstractBidService implements
		OfflineManage {

	public OfflineManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	protected static final String SQL = "SELECT T6230.F01 AS F01, T6230.F02 AS F02, T6230.F03 AS F03, T6230.F04 AS F04, T6230.F05 AS F05, T6230.F06 AS F06, T6230.F07 AS F07, T6230.F08 AS F08, T6230.F09 AS F09, T6230.F10 AS F10, T6230.F11 AS F11, T6230.F12 AS F12, T6230.F13 AS F13, T6230.F14 AS F14, T6230.F15 AS F15, T6230.F16 AS F16, T6230.F17 AS F17, T6230.F18 AS F18, T6230.F19 AS F19, T6230.F20 AS F20, T6230.F21 AS F21, T6230.F22 AS F22, T6230.F23 AS F23, T6230.F24 AS F24, T6230.F25 AS F25, T6230.F26 AS F26, T6231.F02 AS F27, T6231.F03 AS F28, T6231.F07 AS F29, T6231.F08 AS F30, T6231.F10 AS F31, T6231.F11 AS F32, T6231.F12 AS F33, T6231.F13 AS F34, T6231.F14 AS F35, T6231.F15 AS F36, T6110.F02 AS F37, T6110.F06 AS F38, T6110.F10 AS F39 ,T6211.F02 AS F40 FROM S62.T6230 INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 INNER JOIN S62.T6231 ON T6230.F01=T6231.F01 INNER JOIN S62.T6211 ON T6230.F04=T6211.F01 WHERE T6230.F27=?";
	protected static final ArrayParser<Bid> ARRAY_PARSER = new ArrayParser<Bid>() {

		@Override
		public Bid[] parse(ResultSet resultSet) throws SQLException {
			List<Bid> bids = new ArrayList<>();
			while (resultSet.next()) {
				Bid record = new Bid();
				record.F01 = resultSet.getInt(1);
				record.F02 = resultSet.getInt(2);
				record.F03 = resultSet.getString(3);
				record.F04 = resultSet.getInt(4);
				record.F05 = resultSet.getBigDecimal(5);
				record.F06 = resultSet.getBigDecimal(6);
				record.F07 = resultSet.getBigDecimal(7);
				record.F08 = resultSet.getInt(8);
				record.F09 = resultSet.getInt(9);
				record.F10 = T6230_F10.parse(resultSet.getString(10));
				record.F11 = T6230_F11.parse(resultSet.getString(11));
				record.F12 = T6230_F12.parse(resultSet.getString(12));
				record.F13 = T6230_F13.parse(resultSet.getString(13));
				record.F14 = T6230_F14.parse(resultSet.getString(14));
				record.F15 = T6230_F15.parse(resultSet.getString(15));
				record.F16 = T6230_F16.parse(resultSet.getString(16));
				record.F17 = T6230_F17.parse(resultSet.getString(17));
				record.F18 = resultSet.getInt(18);
				record.F19 = resultSet.getInt(19);
				record.F20 = T6230_F20.parse(resultSet.getString(20));
				record.F21 = resultSet.getString(21);
				record.F22 = resultSet.getTimestamp(22);
				record.F23 = resultSet.getInt(23);
				record.F24 = resultSet.getTimestamp(24);
				record.F25 = resultSet.getString(25);
				record.F26 = resultSet.getBigDecimal(26);
				record.F27 = resultSet.getInt(27);
				record.F28 = resultSet.getInt(28);
				record.F29 = resultSet.getInt(29);
				record.F30 = resultSet.getString(30);
				record.F31 = resultSet.getTimestamp(31);
				record.F32 = resultSet.getTimestamp(32);
				record.F33 = resultSet.getTimestamp(33);
				record.F34 = resultSet.getTimestamp(34);
				record.F35 = resultSet.getTimestamp(35);
				record.F36 = resultSet.getTimestamp(36);
				record.F37 = resultSet.getString(37);
				record.F38 = EnumParser.parse(T6110_F06.class,
						resultSet.getString(38));
				record.F39 = EnumParser.parse(T6110_F10.class,
						resultSet.getString(39));
				record.F40 = resultSet.getString(40);
				bids.add(record);
			}
			return bids.toArray(new Bid[bids.size()]);
		}
	};

	@Override
	public PagingResult<Bid> search(LoanQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb = new StringBuilder(SQL);
        List<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F27.S);
        if (query != null)
        {
            String title = query.getLoanTitle();
            if (!StringHelper.isEmpty(title))
            {
                sb.append(" AND T6230.F03 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(title));
            }
            int typeId = query.getType();
            if (typeId > 0)
            {
                sb.append(" AND T6211.F01 =?");
                parameters.add(typeId);
            }
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6230.F24) >=?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6230.F24) <=?");
                parameters.add(timestamp);
            }
            String name = query.getName();
            if (!StringHelper.isEmpty(name))
            {
                sb.append(" AND T6110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            T6230_F20 status = query.getStatus();
            if (status != null)
            {
                sb.append(" AND T6230.F20 =?");
                parameters.add(status);
            }
            else
            {
                sb.append(" AND T6230.F20 <>?");
                parameters.add(T6230_F20.YZF);
            }
        }
        sb.append(" ORDER BY T6230.F24 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sb.toString(), parameters);
        }
    }

	@Override
	public T6230 get(int id) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("参数错误");
		}
		try (Connection connection = getConnection()) {
			T6230 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6230.F01, T6230.F02, T6230.F03, T6230.F04, T6230.F05, T6230.F06, T6230.F07, T6230.F08, T6230.F09, T6230.F10, T6230.F11, T6230.F12, T6230.F13, T6230.F14, T6230.F15, T6230.F16, T6230.F17, T6230.F18, T6230.F19, T6230.F20, T6230.F21, T6230.F22, T6230.F23, T6230.F24, T6230.F25, T6230.F26,T6231.F07 FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01=T6231.F01 WHERE T6230.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6230();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getString(3);
						record.F04 = resultSet.getInt(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getBigDecimal(6);
						record.F07 = resultSet.getBigDecimal(7);
						record.F08 = resultSet.getInt(8);
						record.F09 = resultSet.getInt(9);
						record.F10 = T6230_F10.parse(resultSet.getString(10));
						record.F11 = T6230_F11.parse(resultSet.getString(11));
						record.F12 = T6230_F12.parse(resultSet.getString(12));
						record.F13 = T6230_F13.parse(resultSet.getString(13));
						record.F14 = T6230_F14.parse(resultSet.getString(14));
						record.F15 = T6230_F15.parse(resultSet.getString(15));
						record.F16 = T6230_F16.parse(resultSet.getString(16));
						record.F17 = T6230_F17.parse(resultSet.getString(17));
						record.F18 = resultSet.getInt(18);
						record.F19 = resultSet.getInt(19);
						record.F20 = T6230_F20.parse(resultSet.getString(20));
						record.F21 = resultSet.getString(21);
						record.F22 = resultSet.getTimestamp(22);
						record.F23 = resultSet.getInt(23);
						record.F24 = resultSet.getTimestamp(24);
						record.F25 = resultSet.getString(25);
						record.F26 = resultSet.getBigDecimal(26);
					}
				}
			}
			return record;
		}
	}

	@Override
	public T6231 getExtra(int id) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("参数错误");
		}
		try (Connection connection = getConnection()) {
			T6231 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16 FROM S62.T6231 WHERE T6231.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6231();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getBigDecimal(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getDate(6);
						record.F07 = resultSet.getInt(7);
						record.F08 = resultSet.getString(8);
						record.F09 = resultSet.getString(9);
						record.F10 = resultSet.getTimestamp(10);
						record.F11 = resultSet.getTimestamp(11);
						record.F12 = resultSet.getTimestamp(12);
						record.F13 = resultSet.getTimestamp(13);
						record.F14 = resultSet.getTimestamp(14);
						record.F15 = resultSet.getTimestamp(15);
						record.F16 = resultSet.getString(16);
					}
				}
			}
			return record;
		}
	}

	@Override
	public TbRecord[] getRecord(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T6250> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6250.F01, T6250.F02, T6250.F03, T6250.F04, T6250.F05, T6250.F06, T6250.F07,T6110.F02 FROM S62.T6250 INNER JOIN S61.T6110 ON T6250.F03=T6110.F01 WHERE T6250.F02 = ?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						TbRecord record = new TbRecord();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getBigDecimal(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getTimestamp(6);
						record.F07 = T6250_F07.parse(resultSet.getString(7));
						record.userName = resultSet.getString(8);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new TbRecord[list.size()]));
		}
	}

	@Override
	public Hkjllb[] getHk(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<Hkjllb> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT SUM(F07), F08, F09, F10, T5122.F02 FROM S62.T6252 LEFT JOIN S51.T5122 ON  T6252.F05 = T5122.F01 WHERE T6252.F02 = ? GROUP BY T6252.F05,T6252.F06 ORDER BY T6252.F06,T6252.F05 ASC")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						Hkjllb record = new Hkjllb();
						record.F01 = resultSet.getBigDecimal(1);
						record.F02 = resultSet.getDate(2);
						record.F03 = T6252_F09.parse(resultSet.getString(3));
						record.F04 = resultSet.getTimestamp(4);
						record.F05 = resultSet.getString(5);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new Hkjllb[list.size()]));
		}
	}

	@Override
	public T6251[] getZqxx(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T6251> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S62.T6251 WHERE T6251.F03 = ?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T6251 record = new T6251();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getInt(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getBigDecimal(6);
						record.F07 = resultSet.getBigDecimal(7);
						record.F08 = T6251_F08.parse(resultSet.getString(8));
						record.F09 = resultSet.getDate(9);
						record.F10 = resultSet.getDate(10);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T6251[list.size()]));
		}
	}

	@Override
	public Zqzrxx[] getZqzrxx(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<Zqzrxx> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6262.F01 AS F01, T6262.F02 AS F02, T6262.F03 AS F03, T6262.F04 AS F04, T6262.F05 AS F05, T6262.F06 AS F06, T6262.F07 AS F07, T6262.F08 AS F08, T6262.F09 AS F09, T6260.F03 AS F10, T6260.F04 AS F11 FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01 WHERE T6251.F03 = ?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						Zqzrxx record = new Zqzrxx();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getBigDecimal(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getBigDecimal(6);
						record.F07 = resultSet.getTimestamp(7);
						record.F08 = resultSet.getBigDecimal(8);
						record.F09 = resultSet.getBigDecimal(9);
						record.F10 = resultSet.getBigDecimal(10);
						record.F11 = resultSet.getBigDecimal(11);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new Zqzrxx[list.size()]));
		}
	}

	@Override
	public Tztjxx getStatistics() throws Throwable {
		Tztjxx statistics = new Tztjxx();
		String sql = "SELECT IFNULL(SUM(F26),0),COUNT(*) FROM S62.T6230 WHERE F20 IN (?,?,?)";
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(sql);) {
				ps.setString(1, T6230_F20.YDF.name());
				ps.setString(2, T6230_F20.YJQ.name());
				ps.setString(3, T6230_F20.HKZ.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						statistics.totleMoney = rs.getBigDecimal(1);
						statistics.totleCount = rs.getLong(2);
					}
				}
			}
		}
		statistics.userEarnMoney = getEarnMoney();
		return statistics;
	}

	private BigDecimal getEarnMoney() throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE F05 IN (?,?) AND F09 = ?")) {
				ps.setInt(1, FeeCode.TZ_LX);
				ps.setInt(2, FeeCode.TZ_WYJ);
				ps.setString(3, T6252_F09.YH.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getBigDecimal(1);
					}
				}
			}
		}
		return new BigDecimal(0);
	}

	@Override
	public int add(T6230 entity, T6231 t6231, T6238 t6238, T6240 t6240)
        throws Throwable
    {
        if (entity == null || t6238 == null || t6240 == null)
        {
            return 0;
        }
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        try (Connection connection = getConnection())
        {
            try
            {
				serviceResource.openTransactions(connection);
				int id = 0;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S62.T6230 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = ?, F16 = ?, F17 = ?, F18 = ?, F19 = ?, F20 = ?, F21 = ?, F24 = ?, F25 = ? ,F26=?, F27=?",
                        PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    pstmt.setInt(1, entity.F02);
                    pstmt.setString(2, t6240.F05);
                    pstmt.setInt(3, entity.F04);
                    pstmt.setBigDecimal(4, entity.F05);
                    pstmt.setBigDecimal(5, entity.F06.divide(new BigDecimal(100)));
                    pstmt.setBigDecimal(6, entity.F05);
                    pstmt.setInt(7, entity.F08);
                    pstmt.setInt(8, entity.F09);
                    pstmt.setString(9, entity.F10.name());
                    pstmt.setString(10, entity.F11 == null ? T6230_F11.F.name() : T6230_F11.S.name());
                    pstmt.setString(11, entity.F12.name());
                    pstmt.setString(12, entity.F13 == null ? T6230_F13.F.name() : T6230_F13.S.name());
                    pstmt.setString(13, entity.F14 == null ? T6230_F14.F.name() : T6230_F14.S.name());
                    pstmt.setString(14, entity.F15 == null ? T6230_F15.F.name() : entity.F15.name());
                    pstmt.setString(15, T6230_F16.F.name());
                    pstmt.setString(16, entity.F17.name());
                    int qxr = IntegerParser.parse(configureProvider.getProperty(SystemVariable.QXTS));
                    if (entity.F17 == T6230_F17.GDR)
                    {
                        pstmt.setInt(17, entity.F18);
                    }
                    else if (entity.F17 == T6230_F17.ZRY)
                    {
                        pstmt.setInt(17, qxr);
                    }
                    pstmt.setInt(18, entity.F19);
                    pstmt.setString(19, T6230_F20.SQZ.name());
                    pstmt.setString(20, entity.F21);
                    pstmt.setTimestamp(21, getCurrentTimestamp(connection));
                    pstmt.setString(22, getCrid(connection));
                    pstmt.setBigDecimal(23, entity.F05);
                    pstmt.setString(24, T6230_F27.S.name());
                    pstmt.execute();
                    try (ResultSet resultSet = pstmt.getGeneratedKeys())
                    {
                        if (resultSet.next())
                        {
                            id = resultSet.getInt(1);
                            t6231.F01 = id;
                            t6231.F02 = entity.F09;
                            t6231.F03 = entity.F09;
                            t6231.F04 = entity.F06.divide(new BigDecimal(12 * 100), 9, BigDecimal.ROUND_HALF_UP);
                            t6231.F05 =
                                entity.F06.divide(new BigDecimal(
                                    100 * IntegerParser.parse(configureProvider.format(SystemVariable.REPAY_DAYS_OF_YEAR))),
                                    9,
                                    BigDecimal.ROUND_HALF_UP);
                            t6238.F01 = id;
                            t6240.F01 = id;
                            addT6231(t6231, t6238, t6240, connection);
                        }

                    }
                }
				serviceResource.commit(connection);
				return id;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }

	// 生成标的编号
    private String getCrid(Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement(" SELECT MAX(F01) FROM S62.T6230 FOR UPDATE"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    DecimalFormat df = new DecimalFormat("00000000000");
                    return df.format(resultSet.getInt(1));
                }
                else
                {
                    return "00000000000";
				}
			}
		}
	}

	/**
	 * 插入扩展信息
	 * 
	 * @param entity
	 * @throws Throwable
	 */
    private void addT6231(T6231 entity, T6238 t6238, T6240 t6240, Connection connection)
			throws Throwable {
		if (entity == null || t6238 == null || t6240 == null) {
			return;
		}
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6231 (F01, F02,F03,F04, F05, F07, F08, F09,F16) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F04 = VALUES(F04), F05 = VALUES(F05), F07 = VALUES(F07), F08 = VALUES(F08), F09 = VALUES(F09), F16 = VALUES(F16)"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setInt(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setInt(6, entity.F07);
            pstmt.setString(7, entity.F08);
            pstmt.setString(8, entity.F09);
            pstmt.setString(9, entity.F16);
            pstmt.execute();
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6238 (F01, F02, F03, F04) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03), F04 = VALUES(F04)"))
        {
            pstmt.setInt(1, t6238.F01);
            pstmt.setBigDecimal(2, t6238.F02);
            pstmt.setBigDecimal(3, t6238.F03);
            pstmt.setBigDecimal(4, t6238.F04);
            pstmt.execute();
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6240 (F01, F02, F03, F04, F05, F06, F07) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03), F04 = VALUES(F04), F05 = VALUES(F05), F06 = VALUES(F06), F07 = VALUES(F07)"))
        {
            pstmt.setInt(1, t6240.F01);
            pstmt.setInt(2, t6240.F02);
            pstmt.setInt(3, t6240.F03);
            pstmt.setBigDecimal(4, t6240.F04);
            pstmt.setString(5, t6240.F05);
            pstmt.setBigDecimal(6, t6240.F06);
            pstmt.setBigDecimal(7, t6240.F07);
            pstmt.execute();
		}
	}

	@Override
    public void submit(int loanId)
        throws Throwable
    {
        String sql = "UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?";
        try (Connection connection = getConnection())
        {
            execute(connection, sql, T6230_F20.DSH, loanId);
        }
    }

	@Override
	public void addGuarantee(T6236 t6236) throws Throwable {
		if (t6236 == null) {
			return;
		}
		try (Connection connection = getConnection()) {
			T6236_F04 t6236_F04 = T6236_F04.S;
			int id = 0;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S62.T6236 WHERE T6236.F02 = ? AND T6236.F04 = ? LIMIT 1")) {
				pstmt.setInt(1, t6236.F02);
				pstmt.setString(2, T6236_F04.S.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						t6236_F04 = T6236_F04.F;
					}
				}
			}
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S62.T6236 WHERE T6236.F02 = ? AND T6236.F03 = ? LIMIT 1")) {
				pstmt.setInt(1, t6236.F02);
				pstmt.setInt(2, t6236.F03);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						id = resultSet.getInt(1);
					}
				}
			}
			if (id > 0) {
				try (PreparedStatement pstmt = connection
						.prepareStatement("UPDATE S62.T6236 SET F05 = ? WHERE F01 = ?")) {
					pstmt.setString(1, t6236.F05);
					pstmt.setInt(2, id);
					pstmt.execute();
				}
			} else {
				try (PreparedStatement pstmt = connection
						.prepareStatement("INSERT INTO S62.T6236 SET F02 = ?, F03 = ?, F04 = ?,F05=?")) {
					pstmt.setInt(1, t6236.F02);
					pstmt.setInt(2, t6236.F03);
					pstmt.setString(3, t6236_F04.name());
					pstmt.setString(4, t6236.F05);
					pstmt.execute();
				}
			}
			markYCL(connection, t6236.F01);
		}
	}

	@Override
	public void updateGuarantee(T6236 t6236) throws Throwable {
		if (t6236 == null) {
			return;
		}
		try (Connection connection = getConnection()) {
			int bidId;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F02 FROM S62.T6236 WHERE F01 = ?")) {
				pstmt.setInt(1, t6236.F01);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						bidId = resultSet.getInt(1);
					} else {
						return;
					}
				}
			}
			try (PreparedStatement pstmt = connection
					.prepareStatement("UPDATE S62.T6236 SET F05 = ? WHERE F01 = ?")) {
				pstmt.setString(1, t6236.F05);
				pstmt.setInt(2, t6236.F01);
				pstmt.execute();
			}
			markYCL(connection, bidId);
		}
	}

	private void markYCL(Connection connection, int bidId) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S62.T6241 SET F05 = 'YCL' WHERE F02 = ? AND F05 = 'WCL'")) {
			pstmt.setInt(1, bidId);
			pstmt.execute();
		}
	}

	@Override
	public void addFx(T6237 t6237) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO S62.T6237 (F01, F02, F03 ) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE F02 = VALUES(F02), F03 = VALUES(F03)")) {
				pstmt.setInt(1, t6237.F01);
				pstmt.setString(2, t6237.F02);
				pstmt.setString(3, t6237.F03);
				pstmt.execute();
			}
			markYCL(connection, t6237.F01);
		}
	}

	@Override
	public void update(T6230 entity, T6231 t6231, T6238 t6238, T6240 t6240)
        throws Throwable
    {
        if (entity == null || t6238 == null)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
            	serviceResource.openTransactions(connection);
            	ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6230 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12=?,F13 = ?, F14 = ?,F15=?,F16=?,F17=?, F24 = ?,F26=?,F18=?,F19=? WHERE F01 = ?"))
                {
                    pstmt.setString(1, t6240.F05);
                    pstmt.setInt(2, entity.F04);
                    pstmt.setBigDecimal(3, entity.F05);
                    pstmt.setBigDecimal(4, entity.F06.divide(new BigDecimal(100)));
                    pstmt.setBigDecimal(5, entity.F05);
                    pstmt.setInt(6, entity.F08);
                    pstmt.setInt(7, entity.F09);
                    pstmt.setString(8, entity.F10.name());
                    pstmt.setString(9, entity.F11 == null ? T6230_F11.F.name() : T6230_F11.S.name());
                    pstmt.setString(10, entity.F12.name());
                    pstmt.setString(11, entity.F13 == null ? T6230_F13.F.name() : T6230_F13.S.name());
                    pstmt.setString(12, entity.F14 == null ? T6230_F14.F.name() : T6230_F14.S.name());
                    pstmt.setString(13, entity.F15 == null ? T6230_F15.F.name() : entity.F15.name());
                    pstmt.setString(14, T6230_F16.F.name());
                    pstmt.setString(15, entity.F17.name());
                    pstmt.setTimestamp(16, getCurrentTimestamp(connection));
                    pstmt.setBigDecimal(17, entity.F05);
                    int qxr = IntegerParser.parse(configureProvider.getProperty(SystemVariable.QXTS));
                    if (entity.F17 == T6230_F17.GDR)
                    {
                        pstmt.setInt(18, entity.F18);
                    }
                    else if (entity.F17 == T6230_F17.ZRY)
                    {
                        pstmt.setInt(18, qxr);
                    }
                    pstmt.setInt(19, entity.F19);
                    pstmt.setInt(20, entity.F01);
                    pstmt.execute();
                }
                t6231.F01 = entity.F01;
                t6231.F02 = entity.F09;
                t6231.F03 = entity.F09;
                t6231.F04 = entity.F06.divide(new BigDecimal(12 * 100), 9, BigDecimal.ROUND_HALF_UP);
                t6231.F05 =
                    entity.F06.divide(new BigDecimal(
                        100 * IntegerParser.parse(configureProvider.format(SystemVariable.REPAY_DAYS_OF_YEAR))),
                        9,
                        BigDecimal.ROUND_HALF_UP);
                t6238.F01 = entity.F01;
                t6240.F01 = entity.F01;
                addT6231(t6231, t6238, t6240, connection);
                markYCL(connection, entity.F01);
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
	public T6238 getBidFl(int id) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("参数错误");
		}
		T6238 record = new T6238();
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6238 WHERE T6238.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getBigDecimal(2);
						record.F03 = resultSet.getBigDecimal(3);
						record.F04 = resultSet.getBigDecimal(4);
					}
				}
			}
		}
		return record;
	}

	@Override
	public T6211[] getBidType() throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T6211> list = new ArrayList<>();
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02 FROM S62.T6211 WHERE T6211.F03 = ?")) {
				pstmt.setString(1, T6211_F03.QY.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T6211 record = new T6211();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T6211[list.size()]));
		}
	}

	@Override
	public T6230 getBidType(int loanId) throws Throwable {
		T6230 record = new T6230();
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6230.F11, T6230.F13, T6230.F14, T6230.F20,T6230.F05 FROM S62.T6230 WHERE T6230.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, loanId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record.F11 = T6230_F11.parse(resultSet.getString(1));
						record.F13 = T6230_F13.parse(resultSet.getString(2));
						record.F14 = T6230_F14.parse(resultSet.getString(3));
						record.F20 = T6230_F20.parse(resultSet.getString(4));
						record.F05 = resultSet.getBigDecimal(5);
					}
				}
			}
		}
		return record;
	}

	@Override
    public void addBidDyw(BidDyw bidDyw)
        throws Throwable
    {
        if (bidDyw == null)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                int dywId = 0;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S62.T6234 SET F02 = ?, F03 = ?, F04 = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    pstmt.setInt(1, bidDyw.F02);
                    pstmt.setInt(2, bidDyw.F03);
                    pstmt.setString(3, bidDyw.F04);
                    pstmt.execute();
                    try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                    {
                        if (resultSet.next())
                        {
                            dywId = resultSet.getInt(1);
                        }
                    }
                }
                if (bidDyw.t6235s != null)
                {
                    for (T6235 t6235 : bidDyw.t6235s)
                    {
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("INSERT INTO S62.T6235 SET F02 = ?, F03 = ?, F04 = ?"))
                        {
                            pstmt.setInt(1, dywId);
                            pstmt.setInt(2, t6235.F03);
                            pstmt.setString(3, t6235.F04);
                            pstmt.execute();
                        }
                    }
                }
                markYCL(connection, bidDyw.F02);
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
    public void updateBidDyw(BidDyw bidDyw)
        throws Throwable
    {
        if (bidDyw == null)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {

                if (bidDyw.F01 > 0 && bidDyw.t6235s != null)
                {
                    int bidId;
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT F02 FROM S62.T6234 WHERE F01 = ?"))
                    {
                        pstmt.setInt(1, bidDyw.F01);
                        try (ResultSet resultSet = pstmt.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                bidId = resultSet.getInt(1);
                            }
                            else
                            {
                                return;
                            }
                        }
                    }
                    for (T6235 t6235 : bidDyw.t6235s)
                    {
                        int dywsxId = 0;
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("SELECT F01 FROM S62.T6235 WHERE T6235.F02 = ? AND T6235.F03=? LIMIT 1"))
                        {
                            pstmt.setInt(1, bidDyw.F01);
                            pstmt.setInt(2, t6235.F03);
                            try (ResultSet resultSet = pstmt.executeQuery())
                            {
                                if (resultSet.next())
                                {
                                    dywsxId = resultSet.getInt(1);
                                }
                            }
                        }
                        if (dywsxId > 0)
                        {
                            try (PreparedStatement pstmt =
                                connection.prepareStatement("UPDATE S62.T6235 SET F02 = ?,F03 = ?, F04 = ? WHERE F01 = ?"))
                            {
                                pstmt.setInt(1, bidDyw.F01);
                                pstmt.setInt(2, t6235.F03);
                                pstmt.setString(3, t6235.F04);
                                pstmt.setInt(4, dywsxId);
                                pstmt.execute();
                            }
                        }
                    }
                    markYCL(connection, bidId);
                }
                
            }
            catch (Exception e)
            {
                throw e;
            }
        }
    }

	@Override
	public T6234 getBidDyw(int id) throws Throwable {
		T6234 record = new T6234();
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6234 WHERE T6234.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
					}
				}
			}
		}
		return record;
	}

	@Override
	public T6234[] searchBidDyw(int loanId) throws Throwable {
		ArrayList<T6234> list = new ArrayList<>();
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6234 WHERE F02=?")) {
				pstmt.setInt(1, loanId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T6234 record = new T6234();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
						list.add(record);
					}
				}
			}
		}
		return list.toArray(new T6234[list.size()]);
	}

	@Override
	public BidDywsx[] searchBidDywsx(int id) throws Throwable {
		ArrayList<BidDywsx> list = new ArrayList<>();
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6235.F01, T6235.F02, T6235.F03, T6235.F04,T6214.F03 FROM S62.T6235 INNER JOIN S62.T6214 ON T6235.F03=T6214.F01 WHERE T6235.F02 = ?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						BidDywsx record = new BidDywsx();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
						record.name = resultSet.getString(5);
						list.add(record);
					}
				}
			}
		}
		return list.toArray(new BidDywsx[list.size()]);
	}

	@Override
	public Dbxx[] searchBidDb(int loanId) throws Throwable {
		ArrayList<Dbxx> list = new ArrayList<>();
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6236.F01 AS F01, T6236.F02 AS F02, T6236.F03 AS F03, T6236.F04 AS F04, T6236.F05 AS F05, T6161.F04 AS F06 FROM S62.T6236 INNER JOIN S61.T6161 ON T6236.F03 = T6161.F01 WHERE T6236.F02 = ?")) {
				pstmt.setInt(1, loanId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						Dbxx record = new Dbxx();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = T6236_F04.parse(resultSet.getString(4));
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getString(6);
						list.add(record);
					}
				}
			}
		}
		return list.toArray(new Dbxx[list.size()]);
	}

	@Override
	public Dbxx getBidDb(int id) throws Throwable {
		Dbxx record = new Dbxx();
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6236.F01 AS F01, T6236.F02 AS F02, T6236.F03 AS F03, T6236.F04 AS F04, T6236.F05 AS F05, T6161.F04 AS F06 FROM S62.T6236 INNER JOIN S61.T6161 ON T6236.F03 = T6161.F01 WHERE T6236.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = T6236_F04.parse(resultSet.getString(4));
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getString(6);
					}
				}
			}
		}
		return record;
	}

	@Override
	public T6237 getBidFx(int loanId) throws Throwable {
		T6237 record = new T6237();
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03 FROM S62.T6237 WHERE T6237.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, loanId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						record.F03 = resultSet.getString(3);
					}
				}
			}
		}
		return record;
	}

	@Override
    public void through(int loanId)
        throws Throwable
    {
        if (loanId <= 0)
        {
            throw new ParameterException("参数不能为空");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp timestamp = getCurrentTimestamp(connection);
                int userId = 0;
                BigDecimal amount = new BigDecimal(0);
                T6230_F11 t6230_F11 = T6230_F11.F;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F02,F05,F11 FROM S62.T6230 WHERE T6230.F01 = ? LIMIT 1"))
                {
                    pstmt.setInt(1, loanId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            userId = resultSet.getInt(1);
                            amount = resultSet.getBigDecimal(2);
                            t6230_F11 = EnumParser.parse(T6230_F11.class, resultSet.getString(3));
                        }
                    }
                }
                int xyTypeId = 0;
                if (t6230_F11 == T6230_F11.S)
                {
                    xyTypeId = 1002;
                }
                else
                {
                    xyTypeId = 1001;
                }
                int version = 0;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F02 FROM S51.T5125 WHERE T5125.F01 = ? LIMIT 1"))
                {
                    pstmt.setInt(1, xyTypeId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            version = resultSet.getInt(1);
                        }
                    }
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S62.T6239 (F01, F02, F03) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03)"))
                {
                    pstmt.setInt(1, loanId);
                    pstmt.setInt(2, xyTypeId);
                    pstmt.setInt(3, version);
                    pstmt.execute();
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, T6230_F20.DFB.name());
                    pstmt.setInt(2, loanId);
                    pstmt.execute();
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6231 SET F10 = ? WHERE F01 = ?"))
                {
                    pstmt.setTimestamp(1, timestamp);
                    pstmt.setInt(2, loanId);
                    pstmt.execute();
                }
                BigDecimal totalAmount = new BigDecimal(0);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F03 FROM S61.T6116 WHERE T6116.F01 = ? FOR UPDATE"))
                {
                    pstmt.setInt(1, userId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            totalAmount = resultSet.getBigDecimal(1);
                        }
                    }
                }
                if (totalAmount.compareTo(amount) < 0)
                {
                    throw new LogicalException("用户信用额度小于借款金额不能审核通过");
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6116 SET F03 = ? WHERE F01 = ?"))
                {
                    pstmt.setBigDecimal(1, totalAmount.subtract(amount));
                    pstmt.setInt(2, userId);
                    pstmt.execute();
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S61.T6117 SET F02 = ?, F03 = ?, F04 = ?,F06 = ?, F07 = ?, F08 = ?"))
                {
                    pstmt.setInt(1, userId);
                    pstmt.setInt(2, 1101);
                    pstmt.setTimestamp(3, timestamp);
                    pstmt.setBigDecimal(4, amount);
                    pstmt.setBigDecimal(5, totalAmount.subtract(amount));
                    pstmt.setString(6, "标的审核调整信用额度");
                    pstmt.execute();
                }
                writeLog(connection, "操作日志", "审核标的通过");
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
    public void notThrough(int loanId, String des)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, T6230_F20.SQZ.name());
                    pstmt.setInt(2, loanId);
                    pstmt.execute();
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6231 SET F10 = ? WHERE F01 = ?"))
                {
                    pstmt.setTimestamp(1, getCurrentTimestamp(connection));
                    pstmt.setInt(2, loanId);
                    pstmt.execute();
                }
                writeLog(connection, "操作日志", "审核标的不通过,原因:" + des);
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
    public void release(int loanId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
            	serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6230 SET F20 = ?, F22 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, T6230_F20.TBZ.name());
                    pstmt.setTimestamp(2, getCurrentTimestamp(connection));
                    pstmt.setInt(3, loanId);
                    pstmt.execute();
                }
                writeLog(connection, "操作日志", "发布标的成功");
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
    public void preRelease(int loanId, Timestamp releaseTime)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
            	serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6230 SET F20 = ?, F22 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, T6230_F20.YFB.name());
                    pstmt.setTimestamp(2, releaseTime);
                    pstmt.setInt(3, loanId);
                    pstmt.execute();
                }
                writeLog(connection, "操作日志", "预发布成功，发布时间" + DateTimeParser.format(releaseTime));
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
	public T6240 getXxzq(int id) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("参数不能为空");
		}
		T6240 record = new T6240();
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6240 WHERE T6240.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getBigDecimal(4);
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getBigDecimal(6);
						record.F07 = resultSet.getBigDecimal(7);
					}
				}
			}
		}
		return record;
	}

	@Override
    public void delDyw(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM S62.T6234 WHERE F01=?"))
                {
                    ps.setInt(1, id);
                    ps.execute();
                }
                try (PreparedStatement ps = connection.prepareStatement("DELETE FROM S62.T6235 WHERE F02=?"))
                {
                    ps.setInt(1, id);
                    ps.execute();
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
	public void delDbxx(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("DELETE FROM S62.T6236 WHERE F01=?")) {
				ps.setInt(1, id);
				ps.execute();
			}
		}
	}

	@Override
	public void updateBidStatus(int loanId) throws Throwable {
		if (loanId <= 0) {
			return;
		}
		try (Connection connection = getConnection()) {
			T6230_F20 t6230_F20 = T6230_F20.SQZ;
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F20 FROM S62.T6230 WHERE F01=? FOR UPDATE")) {
				ps.setInt(1, loanId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						t6230_F20 = EnumParser.parse(T6230_F20.class,
								rs.getString(1));
					}
				}
			}
			if (t6230_F20 == T6230_F20.SQZ || t6230_F20 == T6230_F20.DSH) {
				try (PreparedStatement ps = connection
						.prepareStatement("UPDATE S62.T6230 SET F20=? WHERE F01=?")) {
					ps.setString(1, T6230_F20.YZF.name());
					ps.setInt(2, loanId);
					ps.executeUpdate();
				}
			}
		}
	}

	@Override
	public void updateBidDywDate(T6235 t6235) throws Throwable {
		if(t6235 == null){
			return;
		}
		
		int count = 0;
		try(Connection connection = getConnection()){
			try{
				serviceResource.openTransactions(connection);
				try(PreparedStatement pstmt =connection.prepareStatement("SELECT COUNT(1) FROM S62.T6235 where T6235.F05 =?")){
					pstmt.setInt(1, t6235.F05);
					try(ResultSet resultSet = pstmt.executeQuery()){
						if (resultSet.next()) {
							count = resultSet.getInt(1);
						}
					}
				}
				
				if(count > 0){
					try(PreparedStatement pstmt =connection.prepareStatement("UPDATE S62.T6235 SET T6235.F04 = ? where T6235.F05 =?")){
						pstmt.setString(1, t6235.F04);
						pstmt.setInt(2, t6235.F05);
						pstmt.execute();
					}
				}else{
					try(PreparedStatement pstmt =connection.prepareStatement("INSERT INTO S62.T6235 SET T6235.F04 = ?,T6235.F05 = ?")){
						pstmt.setString(1, t6235.F04);
						pstmt.setInt(2, t6235.F05);
						pstmt.execute();
					}
				}
				serviceResource.commit(connection);
			}catch(Exception e){
				serviceResource.rollback(connection);
                throw e;
			}
			
		}
	}

	@Override
	public T6235 finBidDywDate(int loanId) throws Throwable {
		 try (Connection connection = getConnection()) {
	            T6235 record = null;
	            try (PreparedStatement pstmt = connection
	                    .prepareStatement("SELECT F04 FROM S62.T6235 WHERE T6235.F05 = ?")) {
	            	pstmt.setInt(1, loanId);
	                try (ResultSet resultSet = pstmt.executeQuery()) {
	                    if (resultSet.next()) {
	                        record = new T6235();
	                        record.F04 = resultSet.getString(1);
	                    }
	                }
	            }
	            return record;
	        }
	}
}
