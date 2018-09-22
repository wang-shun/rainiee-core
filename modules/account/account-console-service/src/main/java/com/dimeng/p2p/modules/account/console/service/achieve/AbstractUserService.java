package com.dimeng.p2p.modules.account.console.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.service.AbstractP2PService;

public abstract class AbstractUserService extends AbstractP2PService {

	public AbstractUserService(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
    protected SQLConnectionProvider getSQLConnectionProvider()
			throws ResourceNotFoundException, SQLException {
		return serviceResource.getDataConnectionProvider(
				SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER);
	}

	@Override
    protected Connection getConnection() throws ResourceNotFoundException,
			SQLException {
		return serviceResource.getDataConnectionProvider(
				SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
				.getConnection();
	}

	protected Connection getConnection(String db)
			throws ResourceNotFoundException, SQLException {
		return serviceResource.getDataConnectionProvider(
				SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
				.getConnection(db);
	}

	@Override
    protected void writeLog(Connection connection, String type, String log)
        throws Throwable
    {
			try (PreparedStatement pstmt = connection
					.prepareStatement(
							"INSERT INTO S71.T7120 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?",
							PreparedStatement.RETURN_GENERATED_KEYS)) {
				pstmt.setInt(1, serviceResource.getSession().getAccountId());
				pstmt.setTimestamp(2, getCurrentTimestamp(connection));
				pstmt.setString(3, type);
				pstmt.setString(4, log);
				pstmt.setString(5, serviceResource.getSession().getRemoteIP());
				pstmt.execute();
			}
	}

    protected BigDecimal selectBigDecimal(String db, String sql, Object... paramters)
        throws SQLException
    {
        final BigDecimal decimal = new BigDecimal(0);
        try (Connection connection = getConnection(db))
        {
            return select(connection, new ItemParser<BigDecimal>()
            {
                @Override
                public BigDecimal parse(ResultSet resultSet)
                    throws SQLException
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                    return decimal;
                }
            }, sql, paramters);
        }
    }

	protected BigDecimal selectBigDecimal(Connection connection, String sql,
			Object... paramters) throws SQLException {
		final BigDecimal decimal = new BigDecimal(0);
		return select(connection, new ItemParser<BigDecimal>() {
			@Override
			public BigDecimal parse(ResultSet resultSet) throws SQLException {
				if (resultSet.next()) {
					return resultSet.getBigDecimal(1);
				}
				return decimal;
			}
		}, sql, paramters);
	}

	protected BigDecimal selectBigDecimal(Connection connection, String sql,
			List<Object> paramters) throws SQLException {
		final BigDecimal decimal = new BigDecimal(0);
		return select(connection, new ItemParser<BigDecimal>() {
			@Override
			public BigDecimal parse(ResultSet resultSet) throws SQLException {
				if (resultSet.next()) {
					return resultSet.getBigDecimal(1);
				}
				return decimal;
			}
		}, sql, paramters);
	}

	protected static final DecimalFormat AMOUNT_SPLIT_FORMAT = new DecimalFormat(
			"0.00");

	protected String format(BigDecimal amount) {
		if (amount == null) {
			return "0";
		}
		return AMOUNT_SPLIT_FORMAT.format(amount);
	}

	protected void sendLetter(Connection connection, int userId, String title,
			String content) throws Throwable {
		int letterId = insertT6123(connection, userId, title, T6123_F05.WD);
		insertT6124(connection, letterId, content);
	}

	private int insertT6123(Connection connection, int F02, String F03,
			T6123_F05 F05) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, F02);
			pstmt.setString(2, F03);
			pstmt.setTimestamp(3, getCurrentTimestamp(connection));
			pstmt.setString(4, F05.name());
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		}
	}

	protected void insertT6124(Connection connection, int F01, String F02)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("INSERT INTO S61.T6124 SET F01 = ?, F02 = ?")) {
			pstmt.setInt(1, F01);
			pstmt.setString(2, F02);
			pstmt.execute();
		}
	}


}
