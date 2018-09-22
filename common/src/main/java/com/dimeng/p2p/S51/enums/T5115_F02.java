package com.dimeng.p2p.S51.enums;


import com.dimeng.util.StringHelper;

/** 
 * 类型
 */
public enum T5115_F02{


    /** 
     * 活动
     */
    HD("活动"),

    /** 
     * 系统
     */
    XT("系统");

    protected final String chineseName;

    private T5115_F02(String chineseName){
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
     * @return {@link T5115_F02}
     */
    public static final T5115_F02 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5115_F02.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
