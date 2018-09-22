/*
 * 文 件 名:  AbstractGuarantorService
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/13
 */
package com.dimeng.p2p.modules.apply.guarantor.service.achieve;

import java.sql.Connection;
import java.sql.SQLException;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.service.AbstractP2PService;
/**
 *
 *<一句话功能简述>
 * <功能详细描述>
 *
 * @author  heluzhu
 * @version [版本号, 2016/6/13]
 */
public abstract class AbstractGuarantorService extends AbstractP2PService {

	public AbstractGuarantorService(ServiceResource serviceResource) {
		super(serviceResource);
	}

	protected Connection getConnection() throws ResourceNotFoundException,
			SQLException {
		return serviceResource.getDataConnectionProvider(
				SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
				.getConnection();
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
				SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER);
	}
}
