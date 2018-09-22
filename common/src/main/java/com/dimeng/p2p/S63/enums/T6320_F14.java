package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6320_F14{


    /** 
     * 已结束
     */
    YJS("已结束"),

    /** 
     * 生效
     */
    SX("生效"),

    /** 
     * 新建
     */
    XJ("新建");

    protected final String chineseName;

    private T6320_F14(String chineseName){
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
     * @return {@link T6320_F14}
     */
    public static final T6320_F14 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6320_F14.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
