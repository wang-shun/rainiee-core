package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6211_F03{


    /** 
     * 启用
     */
    QY("启用"),

    /** 
     * 停用
     */
    TY("停用");

    protected final String chineseName;

    private T6211_F03(String chineseName){
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
     * @return {@link T6211_F03}
     */
    public static final T6211_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6211_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
