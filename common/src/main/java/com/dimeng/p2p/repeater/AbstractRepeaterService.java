package com.dimeng.p2p.repeater;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;

public abstract class AbstractRepeaterService extends AbstractService
{
    
    protected static final int DECIMAL_SCALE = 9;
    
    protected final Logger logger = Logger.getLogger(getClass());

    public AbstractRepeaterService(ServiceResource serviceResource) {
        super(serviceResource);
    }

    protected SQLConnectionProvider getSQLConnectionProvider()
            throws ResourceNotFoundException, SQLException {
        return serviceResource.getDataConnectionProvider(
                SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER);
    }

    protected Connection getConnection() throws ResourceNotFoundException,
            SQLException {
        return serviceResource.getDataConnectionProvider(
                SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
                .getConnection();
    }

    protected Timestamp getCurrentTimestamp(Connection connection)
            throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_TIMESTAMP()"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getTimestamp(1);
                }
            }
        }
        return null;
    }

    protected Date getCurrentDate(Connection connection) throws Throwable {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_DATE()"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getDate(1);
                }
            }
        }
        return null;
    }


}
