package com.dimeng.p2p.order;

import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;

/**
 * 解冻资金
 * @author dengwenwu
 *
 */
@ResourceAnnotation
public class UnFreezeExecutor extends AbstractOtherExecutor {

	protected final Logger logger = Logger.getLogger(getClass());
	
	public UnFreezeExecutor(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	@Override
	public Class<? extends Resource> getIdentifiedType() {
		return UnFreezeExecutor.class;
	}

	@Override
	protected void doSubmit(SQLConnection connection, Map<String, String> params) throws Throwable {}

}
