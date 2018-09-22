package com.dimeng.p2p.modules.bid.donation.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.service.AbstractP2PService;
import com.dimeng.p2p.variables.P2PConst;

public abstract class AbstractBidManage extends AbstractP2PService {

    public AbstractBidManage(final ServiceResource serviceResource) {
        super(serviceResource);
    }
    
    
    @Override
    protected Connection getConnection() throws ResourceNotFoundException,
	SQLException {
	return serviceResource.getDataConnectionProvider(
			SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
			.getConnection(P2PConst.DB_INFORMATION);
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

    protected Date getCurrentDate() throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT CURRENT_DATE()")) {
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getDate(1);
					}
				}
			}
		}
		return null;
	}
    
    protected BigDecimal selectBigDecimal(Connection connection, String sql, ArrayList<Object> paramters)
        throws SQLException
    {
        final BigDecimal decimal = new BigDecimal(0);
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
