package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/** 
 * 活动适用用户组
 */
public enum T6337_F09{


    /** 
     * 普通用户
     */
    PTYH("普通用户"),

    /** 
     * 用友用户
     */
    YYYH("用友用户"),

    /** 
     * 所有用户
     */
    SYYH("所有用户");

    protected final String chineseName;

    private T6337_F09(String chineseName){
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
     * @return {@link T6337_F09}
     */
    public static final T6337_F09 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6337_F09.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
