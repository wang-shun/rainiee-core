package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 担保方案
 */
public enum T6230_F12{


    /** 
     * 本息全额担保
     */
    BXQEDB("本息全额担保"),

    /** 
     * 本金全额担保
     */
    BJQEDB("本金全额担保");

    protected final String chineseName;

    private T6230_F12(String chineseName){
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
     * @return {@link T6230_F12}
     */
    public static final T6230_F12 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6230_F12.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
