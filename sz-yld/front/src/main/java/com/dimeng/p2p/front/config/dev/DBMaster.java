package com.dimeng.p2p.front.config.dev;

import com.dimeng.framework.data.sql.mysql.AbstractDriverConnectionProvider;
import com.dimeng.framework.log.Logger;
import com.dimeng.framework.resource.InitParameterProvider;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceRetention;
import com.dimeng.p2p.common.DBConfigurator.DevelopmentConfigurator;
import com.dimeng.p2p.variables.P2PConst;

@ResourceAnnotation(ResourceRetention.DEVELOMENT)
public class DBMaster extends AbstractDriverConnectionProvider
{
    
    public DBMaster(InitParameterProvider parameterProvider, Logger logger)
    {
        super(parameterProvider, logger);
        DevelopmentConfigurator configurator = new DevelopmentConfigurator();
        driver = configurator.getDriver();
        url = configurator.getUrl(getDataBaseName());
        properties = configurator.getProperties();
    }
    
    @Override
    public String getName()
    {
        return P2PConst.DB_MASTER_PROVIDER;
    }

    public String getDataBaseName()
    {
        return "S51";
    }
}
