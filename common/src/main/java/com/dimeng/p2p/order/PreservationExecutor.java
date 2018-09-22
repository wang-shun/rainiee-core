/**
 * 
 */
package com.dimeng.p2p.order;

import java.sql.Connection;

import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;

/**
 * 合同保全默认实现
 * @author dengwenwu
 *
 */
@ResourceAnnotation
public class PreservationExecutor extends AbstractPreservationExecutor {

	public PreservationExecutor(ResourceProvider resourceProvider) {
		super(resourceProvider);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dimeng.p2p.order.AbstractPreservationExecutor#contractPreservation(int)
	 */
	@Override
	public void contractPreservation(int preservationId) throws Throwable {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.dimeng.p2p.order.AbstractPreservationExecutor#agreementPreservation(int)
	 */
	@Override
	public void agreementPreservation(int preservationId) throws Throwable {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.dimeng.framework.resource.Resource#getIdentifiedType()
	 */
	@Override
	public Class<? extends Resource> getIdentifiedType() {
		return PreservationExecutor.class;
	}

	/* (non-Javadoc)
	 * @see com.dimeng.framework.resource.Resource#initilize(java.sql.Connection)
	 */
	@Override
	public void initilize(Connection connection) throws Throwable {
		// TODO Auto-generated method stub

	}

}
