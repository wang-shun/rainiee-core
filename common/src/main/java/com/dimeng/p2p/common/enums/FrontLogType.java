package com.dimeng.p2p.common.enums;

public enum FrontLogType
{
    DLRZ("登录日志"), CZ("充值"), TX("提现"), SDTB("手动投资"), GMZQ("购买债权"), ZRZQ("转让债权"), JGDF("担保垫付"), TQHK("提前还款"), ZDTB("自动投资"), SDHK(
        "手动还款"), ZDHK("自动还款"), QXZQ("取消债权转让"), GMBLZQ("购买不良债权");
    
    protected final String name;
    
    private FrontLogType(String name)
    {
        this.name = name;
    }
    
    /**
     * 获取中文名称.
     * 
     * @return {@link String}
     */
    public String getName()
    {
        return name;
    }
}
