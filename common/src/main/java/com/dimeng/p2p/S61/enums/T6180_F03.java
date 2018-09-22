package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 机构类型状态
 */
public enum T6180_F03{


    /** 
     * 融资租债公司
     */
    RZZZGS("融资租债公司"),

    /** 
     * 小贷公司
     */
    XDGS("小贷公司"),

    /** 
     * 担保公司
     */
    DBGS("担保公司");

    protected final String chineseName;

    private T6180_F03(String chineseName){
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
     * @return {@link T6180_F03}
     */
    public static final T6180_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6180_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
