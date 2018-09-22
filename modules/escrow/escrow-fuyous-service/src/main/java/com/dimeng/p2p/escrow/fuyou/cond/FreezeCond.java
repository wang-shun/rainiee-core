package com.dimeng.p2p.escrow.fuyou.cond;

/**
 * 资金冻结请求参数
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月2日]
 */
public interface FreezeCond
{
    /**
     * 商户代码(必填)
     */
    public abstract String mchntCd()
        throws Throwable;
    
    /**
     * 流水号(必填)
     */
    public abstract String mchntTxnSsn()
        throws Throwable;
    
    /**
     * 冻结目标登录账户(必填)
     */
    public abstract String custNo();
    
    /**
     * 冻结金额(必填)
     */
    public abstract String amt();
    
    /**
     * 备注(非必填)
     */
    public abstract String rem();

}
