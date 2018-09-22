package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/** 
 * 交易类型
 */
public enum T6331_F06{

    ZQZR("债权转让"),
    YXLC("优选理财"),
    FX("返现"),
    TB("投资");

    protected final String chineseName;

    private T6331_F06(String chineseName){
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
     * @return {@link T6331_F06}
     */
    public static final T6331_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6331_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
