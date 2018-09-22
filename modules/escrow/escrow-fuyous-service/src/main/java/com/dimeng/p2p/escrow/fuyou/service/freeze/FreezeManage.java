package com.dimeng.p2p.escrow.fuyou.service.freeze;

import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.escrow.fuyou.cond.FreezeCond;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.FYT6101;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.Freeze;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.FreezeRet;

/**
 * 资金冻结管理
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月3日]
 */
public interface FreezeManage extends Service
{
    
    /**
     * 获取所有用户
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<FYT6101> getT6101(String name, Paging paging)
        throws Throwable;
    
    /**
     * 更新冻结金额
     * @param id
     * @param type
     * @param money
     * @return
     * @throws Throwable
     */
    public abstract boolean updateT6101(Freeze freeze)
        throws Throwable;
    
    /**
     * 获取冻结账户信息
     * @param name
     * @return
     * @throws Throwable
     */
    public abstract T6101 selectT6101(String name, T6101_F03 F03)
        throws Throwable;
    
    /**
     * 生成冻结资金信息
     * @param FreezeCond
     * @return
     * @throws Throwable
     */
    public abstract Map<String, String> createFreeze(FreezeCond cond)
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
    public abstract FreezeRet freezeReturnDecoder(HashMap<String, Object> xmlMap, String plain)
        throws Throwable;
    
    /**
     * 查询用户第三方托管信息
     * 
     * @param userAccount
     * @return
     * @throws Throwable
     */
    public abstract String getAccountId(String userAccount)
        throws Throwable;
    
}
