package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 企业帐号是否允许投资
 */
public enum T6110_F17{

    S("是"), F("否");

    protected final String chineseName;

    private T6110_F17(String chineseName){
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
     * @return {@link T6110_F17}
     */
    public static final T6110_F17 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6110_F17.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
