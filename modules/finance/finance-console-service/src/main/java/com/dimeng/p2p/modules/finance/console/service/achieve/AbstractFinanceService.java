package com.dimeng.p2p.modules.finance.console.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.service.AbstractP2PService;
import com.dimeng.p2p.variables.P2PConst;

public abstract class AbstractFinanceService extends AbstractP2PService {

	public AbstractFinanceService(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
    protected Connection getConnection() throws ResourceNotFoundException,
			SQLException {
		return serviceResource.getDataConnectionProvider(
				SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
				.getConnection(P2PConst.DB_CONSOLE);
	}

	protected Connection getUserConnection() throws ResourceNotFoundException,
			SQLException {
		return serviceResource.getDataConnectionProvider(
				SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
				.getConnection(P2PConst.DB_USER);
	}

	protected Connection getConnection(String db)
			throws ResourceNotFoundException, SQLException {
		return serviceResource.getDataConnectionProvider(
				SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
				.getConnection(db);
	}

	@Override
    protected SQLConnectionProvider getSQLConnectionProvider()
			throws ResourceNotFoundException, SQLException {
		return serviceResource.getDataConnectionProvider(
				SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER);
	}

    protected BigDecimal selectBigDecimal(String db, String sql,
			Object... paramters) throws SQLException {
		final BigDecimal decimal = new BigDecimal(0);
		try(Connection connection = getConnection(db))
		{
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
}
