package com.dimeng.p2p.app.config.dev;

import java.util.Properties;

import com.dimeng.framework.data.sql.mysql.AbstractDriverConnectionProvider;
import com.dimeng.framework.log.Logger;
import com.dimeng.framework.resource.InitParameterProvider;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceRetention;
import com.dimeng.p2p.variables.P2PConst;

@ResourceAnnotation(ResourceRetention.DEVELOMENT)
public class DBMaster extends AbstractDriverConnectionProvider {

	public DBMaster(InitParameterProvider parameterProvider, Logger logger) {
		super(parameterProvider, logger);
		driver = "com.mysql.jdbc.Driver";
        //        url = "jdbc:mysql://localhost:9005/S51"; // 本地环境库
        url = "jdbc:mysql://localhost:3306/S51"; //测试环境库
        properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "root");
        properties.setProperty("useUnicode", "true");
        properties.setProperty("characterEncoding", "UTF8");
	}

	@Override
	public String getName() {
		return P2PConst.DB_MASTER_PROVIDER;
	}

}
