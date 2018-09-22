package com.dimeng.p2p.escrow.fuyou.service;

import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6114_EXT;
import com.dimeng.p2p.escrow.fuyou.cond.BankCond;
import com.dimeng.p2p.escrow.fuyou.cond.BankQueryCond;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryEntity;

/**
 * 
 * 更新银行卡
 * <处理第三方注册成功，而银行卡未更新>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月15日]
 */
public interface BankManage extends Service
{
    
    /**
     * 获取用户第三方账号
     * 及银行卡信息
     * @return
     * @throws Throwable
     */
    public HashMap<String, String> getUsrCustInfo()
        throws Throwable;
    
    /**
     * 加载用户信息查询-富友
     * 
     * @param Mchnt_cd
     *            商户代号
     * @param user_ids
     *            用户账号
     * @return UserQueryEntity 返回用户信息查询实体类
     * @throws Throwable
     */
    public abstract UserQueryEntity userChargeQuery(String Mchnt_cd, String user_ids)
        throws Throwable;
    
    /**
     * 更新银行卡
     * @param params 相关信息
     * @param flag true:自然人; false:非自然人
     * @return
     * @throws Throwable
     */
    public boolean updateBank(HashMap<String, String> params, boolean flag)
        throws Throwable;
    
    /**
     * 修改银行卡
     * @param cond
     * @throws Throwable
     */
    public Map<String, String> bankUpdate(BankCond cond)
        throws Throwable;
    
    /**
     * 修改银行卡
     * @param cond
     * @throws Throwable
     */
    public Map<String, String> bankQuery(BankQueryCond cond)
        throws Throwable;
    
    /**
     * 开户行行别
     * @param F01
     * @return
     * @throws Throwable
     */
    public String selectT5020(int F01)
        throws Throwable;
    
    public boolean verifyByRSA(String plain, String string)
        throws Exception;
    
    /**
     * 银行卡修改申请记录
     * <功能详细描述>
     * @param mchnt_txn_ssn
     * @throws Throwable
     */
    public void insertT6114Ext(int bankId, String mchnt_txn_ssn)
        throws Throwable;
    
    /**
     * 申请更换银行卡
     * @param param
     * @return
     * @throws Throwable
     */
    public boolean bankRetDecode(Map<String, String> params)
        throws Throwable;
    
    /**
     * 更新申请记录
     * @param params
     * @throws Throwable
     */
    public void updateT6114Ext(Map<String, String> params)
        throws Throwable;
    
    /**
     * 查询用户申请更换银行卡记录
     * @return
     * @throws Throwable
     */
    public boolean selectT6114Ext()
        throws Throwable;
    
    /**
     * 查询更换银行卡记录
     * @param mchnt_txn_ssn 流水号
     * @return
     * @throws Throwable
     */
    public T6114_EXT selectT6114Ext(String mchnt_txn_ssn)
        throws Throwable;
    
    /**
     * 致富友查询银行卡更换信息
     * @param serviceSession
     * @param mchnt_txn_ssn
     * @param mchnt_cd
     * @param url
     * @return
     * @throws Throwable
     */
    public String queryFuyou(ServiceSession serviceSession, String mchnt_txn_ssn, String mchnt_cd, String url, boolean flag)
        throws Throwable;
    
}
