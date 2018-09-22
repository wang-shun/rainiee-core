package com.dimeng.email.sender.config;

import java.util.Properties;

import com.dimeng.framework.data.sql.mysql.AbstractPooledConnectionProvider;
import com.dimeng.framework.log.Logger;
import com.dimeng.framework.resource.InitParameterProvider;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;

@ResourceAnnotation
public class Master extends AbstractPooledConnectionProvider {
	public Master(InitParameterProvider parameterProvider, Logger logger) {
		super(parameterProvider, logger);
		Properties properties= new Properties();
		properties.setProperty("user",
				parameterProvider.getInitParameter("user"));
		properties.setProperty("jdbcUrl",
				parameterProvider.getInitParameter("jdbcUrl"));
        try
        {
            properties.setProperty("password", StringHelper.decode(parameterProvider.getInitParameter("password")));
        }
        catch (Throwable e)
        {
            logger.log("database password not Encryption!");
            logger.log(e);
        }
		properties.setProperty("useUnicode", "true");
		properties.setProperty("characterEncoding", "UTF8");
	}

	@Override
	public String getName() {
		return P2PConst.DB_MASTER_PROVIDER;
	}
}
