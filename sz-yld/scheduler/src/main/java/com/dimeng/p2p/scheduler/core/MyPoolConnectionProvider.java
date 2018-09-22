package com.dimeng.p2p.scheduler.core;/*
                                      * 文 件 名:  MyPoolConnectionProvider.java
                                      * 版    权:  深圳市迪蒙网络科技有限公司
                                      * 描    述:  <描述>
                                      * 修 改 人:  heluzhu
                                      * 修改时间: 2016/4/9
                                      */

import java.sql.Connection;
import java.sql.SQLException;

import org.quartz.utils.ConnectionProvider;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.common.ResourceProviderUtil;

public class MyPoolConnectionProvider implements ConnectionProvider
{
    /**
     * 避免每次通过反射获取。
     */
    private static SQLConnectionProvider sQLConnectionProvider;
    
    /**
     * @return connection managed by this provider
     * @throws SQLException
     */
    @Override
    
    public Connection getConnection()
        throws SQLException
    {
        return sQLConnectionProvider.getConnection("S66");
    }
    
    @Override
    public void shutdown()
        throws SQLException
    {
        sQLConnectionProvider = null;
    }
    
    @Override
    public void initialize()
        throws SQLException
    {
        sQLConnectionProvider = ResourceProviderUtil.getResourceProvider()
            .getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER);
    }
}
