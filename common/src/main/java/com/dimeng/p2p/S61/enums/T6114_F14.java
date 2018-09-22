package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 认证状态
 */
public enum T6114_F14 {


    /**
     * 未认证
     */
    WRZ("未认证"),

    /**
     * 认证中
     */
    RZZ("认证中"),
    
    /**
     * 认证成功
     */
    RZCG("认证成功"),

    /**
     * 认证失败
     */
    RZSB("认证失败");

    protected final String chineseName;

    private T6114_F14(String chineseName){
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
     * @return {@link T6114_F14}
     */
    public static final T6114_F14 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6114_F14.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
