package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/**
 * 状态
 */
public enum T6360_F03{

    SH("申请"),

    /**
     * 修改
     */
    XG("修改"),

    /**
     * 发货
     */
    FH("发货"),

    /**
     * 退货
     */
    TH("退货"),

    /**
     * 申请退款
     */
    SQTK("申请退款"),

    /**
     * 退款
     */
    TK("退款"),
    
    /**
     * 拒绝退款
     */
    JJTK("拒绝退款");

    protected final String chineseName;

    private T6360_F03(String chineseName){
        this.chineseName = chineseName;
    }
    /**
     * 获取中文名称.
     *
     * @return {@link String}
     */
    public String getChineseName() {
        return chineseName;
    }
    /**
     * 解析字符串.
     *
     * @return {@link T6360_F03}
     */
    public static final T6360_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6360_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
