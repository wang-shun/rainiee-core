/**
 * 
 */
package com.dimeng.p2p.service;

import com.dimeng.framework.config.entity.VariableBean;
import com.dimeng.framework.service.Service;
/**
 * 获取安全信息页面的地址
 */
public interface SafetymsgViewManage extends Service {
    public abstract VariableBean getSafetymsgView()
        throws Throwable;
}
