package com.dimeng.p2p.S71.enums;


import com.dimeng.util.StringHelper;

/** 
 * 关怀类型
 */
public enum T7166_F03{


    /** 
     * 生日
     */
    SR("生日"),

    /** 
     * 节日
     */
    JR("节日");

    protected final String chineseName;

    private T7166_F03(String chineseName){
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
     * @return {@link T7166_F03}
     */
    public static final T7166_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T7166_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
