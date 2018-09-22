package com.dimeng.p2p.modules.financing.front.service.achieve;

import java.sql.Connection;
import java.sql.SQLException;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.variables.P2PConst;

public abstract class AbstractFinancingManage extends AbstractService {

    public AbstractFinancingManage(final ServiceResource serviceResource) {
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

    
}
