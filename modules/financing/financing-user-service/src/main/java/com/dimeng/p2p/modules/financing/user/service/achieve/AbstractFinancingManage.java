package com.dimeng.p2p.modules.financing.user.service.achieve;

import java.sql.*;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.variables.P2PConst;

public abstract class AbstractFinancingManage extends AbstractService {

	public AbstractFinancingManage(ServiceResource serviceResource) {
		super(serviceResource);
	}

	protected Connection getConnection() throws ResourceNotFoundException,
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
