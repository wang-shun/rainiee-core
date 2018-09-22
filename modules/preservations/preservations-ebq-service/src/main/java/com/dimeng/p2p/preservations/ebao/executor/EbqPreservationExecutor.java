/**
 * 
 */
package com.dimeng.p2p.preservations.ebao.executor;

import com.dimeng.framework.resource.AchieveVersion;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.order.PreservationExecutor;
import com.dimeng.p2p.preservations.ebao.service.PreservationManager;

/**
 * 易保全保全实现
 * @author dengwenwu
 *
 */
@AchieveVersion(version = 20160628)
@ResourceAnnotation
public class EbqPreservationExecutor extends PreservationExecutor {

	public EbqPreservationExecutor(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}
	
	@Override
	public void contractPreservation(int preservationId) throws Throwable {
		try (ServiceSession serviceSession =
                resourceProvider.getResource(ServiceProvider.class).createServiceSession())
            {
				PreservationManager manager = serviceSession.getService(PreservationManager.class);
				manager.contractPreservation(preservationId);
            }
	}
	
	@Override
	public void agreementPreservation(int preservationId) throws Throwable {
		try (ServiceSession serviceSession =
                resourceProvider.getResource(ServiceProvider.class).createServiceSession())
            {
				PreservationManager manager = serviceSession.getService(PreservationManager.class);
				manager.agreementPreservation(preservationId);
            }
	}
}
