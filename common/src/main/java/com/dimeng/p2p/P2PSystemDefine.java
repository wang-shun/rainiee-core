package com.dimeng.p2p;

import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.SystemDefine;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.message.MessageProvider;
import com.dimeng.framework.resource.Resource;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;

public abstract class P2PSystemDefine extends SystemDefine implements P2PConst {

	protected Map<Class<? extends Resource>, String> schemas = new HashMap<>();

	public P2PSystemDefine() {
		super();
		schemas.put(ConfigureProvider.class, DB_CONFIG);
		schemas.put(FileStore.class, DB_CONFIG);
		schemas.put(MessageProvider.class, DB_CONFIG);
	}

	@Override
	public String getSystemName() {
		return "迪蒙网贷系统";
	}

	@Override
	public String getSystemDescription() {
		return "迪蒙网贷系统";
	}

	@Override
	public String getDataProvider(Class<? extends Resource> resourceType) {
		return DB_MASTER_PROVIDER;
	}

	@Override
	public String getSchemaName(Class<? extends Resource> resourceType) {
		return resourceType == null ? null : schemas.get(resourceType);
	}

	@Override
	public String getSiteDomainKey() {
		return SystemVariable.SITE_DOMAIN.getKey();
	}

	@Override
	public String getSiteIndexKey() {
		return URLVariable.INDEX.getKey();
	}
}
