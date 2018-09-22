package com.dimeng.p2p.modules.salesman.console.service.achieve;

import java.sql.*;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.variables.P2PConst;

public abstract class AbstractSalesmanService extends AbstractService implements
		Service {

	public AbstractSalesmanService(ServiceResource serviceResource) {
		super(serviceResource);
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

}
