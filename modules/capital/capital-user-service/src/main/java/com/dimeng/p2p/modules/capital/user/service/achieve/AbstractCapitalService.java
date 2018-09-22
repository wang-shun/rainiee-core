package com.dimeng.p2p.modules.capital.user.service.achieve;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.service.AbstractP2PService;

public abstract class AbstractCapitalService extends AbstractP2PService
		implements Service {

	public AbstractCapitalService(ServiceResource serviceResource) {
		super(serviceResource);
	}

}
