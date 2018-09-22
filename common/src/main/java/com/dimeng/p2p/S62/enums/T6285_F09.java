package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 返还状态
 */
public enum T6285_F09{


    /** 
     * 未返还
     */
    WFH("未返还"),
    
    /** 
     * 返还中
     */
    FHZ("返还中"),

    /** 
     * 已返还
     */
    YFH("已返还");

    protected final String chineseName;

    private T6285_F09(String chineseName){
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
     * @return {@link T6285_F09}
     */
    public static final T6285_F09 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6285_F09.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
