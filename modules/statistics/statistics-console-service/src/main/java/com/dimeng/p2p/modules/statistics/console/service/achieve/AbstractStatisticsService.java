package com.dimeng.p2p.modules.statistics.console.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.service.AbstractP2PService;

public abstract class AbstractStatisticsService extends AbstractP2PService
		implements Service {

	public AbstractStatisticsService(ServiceResource serviceResource) {
		super(serviceResource);
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
    
    protected BigDecimal selectBigDecimal(Connection connection, String sql, Object... paramters)
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
    
    protected BigDecimal selectBigDecimal(Connection connection, String sql, List<Object> paramters)
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

    @Override
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
