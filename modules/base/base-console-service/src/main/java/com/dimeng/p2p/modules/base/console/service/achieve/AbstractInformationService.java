package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.service.AbstractP2PService;
import com.dimeng.p2p.variables.P2PConst;

public abstract class AbstractInformationService extends AbstractP2PService {

	public AbstractInformationService(ServiceResource serviceResource) {
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
				.getConnection(P2PConst.DB_INFORMATION);
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
    
    /**
     * @param type
     *            操作类别
     * @param log
     *            日志内容
     * @throws Throwable
     */
    protected void writeLog(Connection connection, String type, String log)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S71.T7120 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setString(3, type);
            pstmt.setString(4, log);
            pstmt.setString(5, serviceResource.getSession().getRemoteIP());
            pstmt.execute();
        }
    }
}
