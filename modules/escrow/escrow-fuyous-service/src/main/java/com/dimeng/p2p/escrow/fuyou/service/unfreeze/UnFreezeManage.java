package com.dimeng.p2p.escrow.fuyou.service.unfreeze;

import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.escrow.fuyou.cond.UnFreezeCond;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.T6170;
import com.dimeng.p2p.escrow.fuyou.entity.unfreeze.UnFreezeRet;

/**
 * 资金解冻管理
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月3日]
 */
public interface UnFreezeManage extends Service
{
    /**
     * 更新解冻金额
     * @param id
     * @param type
     * @param money
     * @return
     * @throws Throwable
     */
    public abstract boolean updateT6101(UnFreezeRet unFreeze)
        throws Throwable;
    
    /**
     * 获取解冻账户信息
     * @param name
     * @return
     * @throws Throwable
     */
    public abstract T6101 selectT6101(String name)
        throws Throwable;
    
    /**
     * 获取用户所有冻结记录
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T6170> getT6170(String name, String serialNumber, String status, Paging paging)
        throws Throwable;
    
    /**
     * 生成解冻资金信息
     * @param FreezeCond
     * @return
     * @throws Throwable
     */
    public abstract Map<String, String> createUnFreeze(UnFreezeCond cond)
        throws Throwable;
    
    /**
     * 验签
     * @param cond
     * @return 
     * @throws Throwable
     */
    public abstract boolean verifyByRSA(String plain, String chkValue)
        throws Throwable;
    
    /**
     * 解析资金冻结返回参数
     * 
     * @param request
     * @return FreezeRet
     * @throws Throwable
     */
    public abstract UnFreezeRet unfreezeRetDecoder(HashMap<String, Object> xmlMap, String plain)
        throws Throwable;
    
    /**
     * 查询用户第三方托管信息
     * 
     * @param F01
     * @return
     * @throws Throwable
     */
    public abstract String getAccountId(String userAccount)
        throws Throwable;
    
    /**
     * 查询资金冻结记录
     * <查询单个用户，名下所有的资金冻结记录>
     * @param serialNo
     * @return
     * @throws Throwable
     */
    public abstract T6170 selectT6170(String serialNo)
        throws Throwable;
    
    /**
     * 获取所有的需要自动解冻流水号
     * @param name
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract T6170[] getT6170s()
        throws Throwable;
}
