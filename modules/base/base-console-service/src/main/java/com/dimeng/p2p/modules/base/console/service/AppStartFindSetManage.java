package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementType;

/**
 * App启动发现页设置
 * 
 */
public interface AppStartFindSetManage extends Service {
    /**
     * 更新启动发现页图片
     * <功能详细描述>
     * @param advertisement
     * @throws Throwable
     */
    public abstract void updateAppStartFindPic(AdvertisementType advertisement)
        throws Throwable;
    
    /**
     * 查询IOS启动页图片、Android启动页图片、发现页广告图片
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract AdvertisementRecord[] search()
        throws Throwable;
    
    /**
     *  根据图片类型查询
     * <功能详细描述>
     * @param advType
     * @return
     * @throws Throwable
     */
    public abstract AdvertisementRecord selectPic(String advType)
        throws Throwable;
}
