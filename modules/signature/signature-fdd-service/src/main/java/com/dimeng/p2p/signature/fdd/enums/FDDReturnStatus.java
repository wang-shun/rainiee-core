package com.dimeng.p2p.signature.fdd.enums;

/**
 * 法大大接口返回状态
 * 文  件  名：ActionType.java
 * 版        权：深圳市迪蒙网络科技有限公司
 * 类  描  述：
 * 修  改  人：ZhangXu
 * 修改时间：2016年12月13日
 */
public enum FDDReturnStatus
{
    SUCCESS("1000", "成功"), 
    ERROR2001("2001", "参数缺失或不合法"), 
    ERROR2002("2002", "失败"), 
    ERROR2003("2003", "其他错误，请联系法大大"),
    SIGN_SUCCESS("3000","签章成功"),
    SIGN_FAIL("3001","签章成功");
    
    
    /**
     * 代码
     */
    String code;
    
    /**
     * 中文名称
     */
    String chineseName;
    
    private FDDReturnStatus(String code, String chineseName)
    {
        this.code = code;
        this.chineseName = chineseName;
    }
    
    public String code()
    {
        return code;
    }
    
    public String getChineseName()
    {
        return chineseName;
    }
}
