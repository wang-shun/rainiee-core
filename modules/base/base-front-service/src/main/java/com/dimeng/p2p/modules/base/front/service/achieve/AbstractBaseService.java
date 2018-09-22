package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;

public abstract class AbstractBaseService extends AbstractService {

	public AbstractBaseService(ServiceResource serviceResource) {
		super(serviceResource);
	}

	protected Connection getConnection() throws ResourceNotFoundException,
			SQLException {
		return serviceResource.getDataConnectionProvider(
				SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
				.getConnection("S50");
	}

	protected Connection getConnection(String schema)
			throws ResourceNotFoundException, SQLException {
		return serviceResource.getDataConnectionProvider(
				SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
				.getConnection(schema);
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
