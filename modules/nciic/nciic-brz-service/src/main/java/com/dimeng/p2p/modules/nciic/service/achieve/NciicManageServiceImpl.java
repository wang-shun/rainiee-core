package com.dimeng.p2p.modules.nciic.service.achieve;

import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;

public class NciicManageServiceImpl extends AbstractService implements INciicManageService
{
    
    public NciicManageServiceImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    /**
     * 实名认证.
     * 
     * @param id
     *            身份证号码
     * @param name
     *            姓名
     * @return {@code boolean} 是否验证通过
     * @throws Throwable
     */
    @Override
    public boolean check(String id, String name, String terminal, int accountId)
        throws Throwable
    {
        return check(id, name, false, terminal, accountId);
    }
    
    /**
     * 实名认证.
     * 
     * @param id
     *            身份证号码
     * @param name
     *            姓名
     * @param duplicatedName
     *            是否允许重名
     * @return {@code boolean} 是否验证通过
     * @throws Throwable
     */
    @Override
    public boolean check(String id, String name, boolean duplicatedName, String terminal, int accountId)
        throws Throwable
    {
        return true;
    }
    
}
