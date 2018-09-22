package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 用户类型
 */
public enum T6110_F06{


    /** 
     * 自然人
     */
    ZRR("自然人"),

    /** 
     * 非自然人
     */
    FZRR("非自然人");

    protected final String chineseName;

    private T6110_F06(String chineseName){
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
     * @return {@link T6110_F06}
     */
    public static final T6110_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6110_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
