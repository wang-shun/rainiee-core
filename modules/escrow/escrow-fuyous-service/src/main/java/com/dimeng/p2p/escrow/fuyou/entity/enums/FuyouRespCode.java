package com.dimeng.p2p.escrow.fuyou.entity.enums;

/**
 * 
 * 富友返回码
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年3月3日]
 */
public enum FuyouRespCode
{
    /** 
     * 交易成功
     */
    JYCG("0000"),
    
    /** 
     * 交易已完成
     */
    JYWC("3122"),
    
    /** 
     * 用户已开户
     */
    YHYKH("5343"),
    
    /** 
     * 商户流水号重复
     */
    LSCF("5345");
    
    protected final String respCode;
    
    private FuyouRespCode(String respCode)
    {
        this.respCode = respCode;
    }
    
    /**
     * 获取代码
     * 
     * @return {@link String}
     */
    public String getRespCode()
    {
        return respCode;
    }
}
