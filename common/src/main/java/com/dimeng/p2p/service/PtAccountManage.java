/**
 * 
 */
package com.dimeng.p2p.service;

import java.sql.SQLException;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.Service;
import com.dimeng.p2p.common.DimengRSAPrivateKey;
import com.dimeng.p2p.common.DimengRSAPulicKey;

/**
 * @author guopeng
 * 
 */
public interface PtAccountManage extends Service {
    public abstract void addPtAccount()
        throws Throwable;
    
    public abstract void addRSAKey(ResourceProvider resourceProviderParam)
        throws Throwable;
    
    public abstract DimengRSAPulicKey getPublicKey()
        throws Exception, SQLException;
    
    public abstract DimengRSAPrivateKey getPrivateKey()
        throws Exception, SQLException;
}
