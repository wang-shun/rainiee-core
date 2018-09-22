package com.dimeng.p2p.order;

import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;

/**
 * 企业、机构用户审核
 * @author dengwenwu
 *
 */
@ResourceAnnotation
public class QyRegisterCheckExecutor extends AbstractOtherExecutor {

	protected final Logger logger = Logger.getLogger(getClass());
	
	public QyRegisterCheckExecutor(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	public Class<? extends Resource> getIdentifiedType() {
		return QyRegisterCheckExecutor.class;
	}

	@Override
	protected void doSubmit(SQLConnection connection, Map<String, String> params) throws Throwable {}

	

}
