package com.dimeng.p2p.S71.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T7166_F07{

    QY("启用"),

    /** 
     * 停用
     */
    TY("停用");

    protected final String chineseName;

    private T7166_F07(String chineseName){
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
     * @return {@link T7166_F07}
     */
    public static final T7166_F07 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T7166_F07.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
