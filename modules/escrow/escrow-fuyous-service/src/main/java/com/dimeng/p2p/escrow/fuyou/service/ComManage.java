package com.dimeng.p2p.escrow.fuyou.service;

import java.sql.SQLException;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.escrow.fuyou.cond.ComRegisterCond;
import com.dimeng.p2p.escrow.fuyou.entity.ComRegisterEntity;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryEntity;

/**
 * 
 * 法人处理接口
 * 
 * @author heshiping
 * @version [版本号, 2015年5月25日]
 */
public abstract interface ComManage extends Service
{
    
    /**
     * 生成法人用户开户跳转地址
     * 
     * @param cond
     *            传入的参数
     * @return 返回跳转地址
     * @throws Throwable
     */
    public abstract Map<String, String> createRegisterUri(ComRegisterCond cond)
        throws Throwable;
    
    /**
     * 查询法人相关信息
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public ComRegisterEntity selectLegealInfo()
        throws Throwable;
    
    /**
     * 查询用户第三方账号是否存在
     * 
     * @param F03 手机号（第三方账号）
     * @return
     * @throws Throwable
     */
    public boolean selectT6119(String F03)
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
     * 更新法人注册信息
     * 
     * @param entity
     *            法人注册实体类
     * @throws SQLException
     * @throws Throwable
     */
    public void updateLegealInfo(Map<String, String> retMap)
        throws SQLException, Throwable;
    
    /**
     * 解析企业用户开户返回数据, 成功解析的必要条件：数据正确返回，验签成功
     * 
     * @param request
     * @return 若未成功解析返回null
     * @throws Throwable
     */
    public abstract boolean registerReturnDecoder(Map<String, String> params)
        throws Throwable;
    
}
