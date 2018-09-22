package com.dimeng.p2p.escrow.fuyou.cond;

/**
 * 资金解冻请求参数
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月2日]
 */
public interface UnFreezeCond
{
    /**
     * 商户代码(必填)
     */
    public String mchntCd()
        throws Throwable;
    
    /**
     * 流水号(必填)
     */
    public String mchntTxnSsn()
        throws Throwable;
    
    /**
     * 解冻目标登录账户(必填)
     */
    public String custNo()
        throws Throwable;
    
    /**
     * 解冻金额(必填)
     */
    public String amt()
        throws Throwable;
    
    /**
     * 备注(非必填)
     */
    public String rem()
        throws Throwable;
    
}
