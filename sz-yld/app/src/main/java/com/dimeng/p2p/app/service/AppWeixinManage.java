package com.dimeng.p2p.app.service;

import java.math.BigDecimal;
import java.util.Map;

import com.dimeng.framework.service.Service;

/**
 * 平台账号关联微信号
 * 
 */
public interface AppWeixinManage extends Service {
	
    /** 
     * 修改app版本
     * @throws Throwable
     */
    public abstract void updateAppWeixin(String weixinAccount, int id)
        throws Throwable;
    
    /**
     * 删除
     */
    public abstract String deleteAppWeixin(String weixinAccount)
        throws Throwable;
    
	/**
	 * 查询微信关联账号
	 * @return 
	 * @throws Throwable
	 */
    public abstract int searchAppWeixin(String weixinAccount)
        throws Throwable;
    
    /**
     * 查询账号信息通过accountId
     * @return 
     * @throws Throwable
     */
    public abstract Map<String, String> getWeixinAccountInfo(int accountId)
        throws Throwable;
    
    /**
     * 查询账号信息通过accountId
     * @return 
     * @throws Throwable
     */
    public abstract Map<String, String> getAccountInfo(int accountId)
        throws Throwable;
   
    public abstract void goOnAccount(int accountId)
        throws Throwable;
    
    public BigDecimal getLoanAmount(int accountId) throws Throwable;
}
