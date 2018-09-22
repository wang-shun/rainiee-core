package com.dimeng.p2p.modules.bid.front.service.achieve;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.service.AbstractP2PService;
import com.dimeng.p2p.variables.P2PConst;

import java.sql.*;

public abstract class AbstractBidManage extends AbstractP2PService {

    public AbstractBidManage(final ServiceResource serviceResource) {
        super(serviceResource);
    }
    
    
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
    
    protected SQLConnectionProvider getSQLConnectionProvider()
			throws ResourceNotFoundException, SQLException {
		return serviceResource.getDataConnectionProvider(
				SQLConnectionProvider.class, P2PConst.DB_USER);
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
}
