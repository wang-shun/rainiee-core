package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6193_F06{


    /** 
     * 未使用
     */
    WSY("未使用"),

    /** 
     * 已放款
     */
    YFK("已放款"),

    /** 
     * 已作废
     */
    YZF("已作废");

    protected final String chineseName;

    private T6193_F06(String chineseName){
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
     * @return {@link T6193_F06}
     */
    public static final T6193_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6193_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
