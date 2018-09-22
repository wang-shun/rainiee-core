/*
 * 文 件 名:  UserInfoManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月29日
 */
package com.dimeng.p2p.service;

import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6118;
import com.dimeng.p2p.common.entities.Safety;

/**
 * 
 * 用户信息管理接口
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月29日]
 */
public interface UserInfoManage extends Service
{
    /**
     * 查询用户基本信息
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract T6110 getUserInfo(int userId)
        throws Throwable;
    
    /**
     * 获取商户客户号 
     * @return
     * @throws Throwable
     */
    public String getUsrCustId()
        throws Throwable;
    
    /**
     * 获取当前用户的认证实体
     * @return
     * @throws Throwable
     */
    public abstract T6118 getVerifyEntity()
        throws Throwable;
    
    /**
     * 查询用户风险备用金资金信息
     * @return
     * @throws Throwable
     */
    public abstract T6101 searchFxbyj()
        throws Throwable;
    
    /**
     * 获取用户安全信息
     * @param acount
     * @return
     * @throws Throwable
     */
    public abstract Safety get()
        throws Throwable;
    
    /**
     * 检查账户各必要认证项（实名、手机、邮箱等）
     * @return
     */
    public abstract Map<String, String> checkAccountInfo()
        throws Throwable;
    
    /**
     * 检查账户各必要认证项（实名、手机、邮箱等）
     * @return
     */
    public abstract Map<String, String> checkAccountInfo(String accountName)
        throws Throwable;
    
}
