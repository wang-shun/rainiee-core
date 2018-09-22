package com.dimeng.p2p.S51.enums;


import com.dimeng.util.StringHelper;

/** 
 * 编号
 */
public enum T5129_F03{


    /** 
     * 标的编号
     */
    BDBH("标的编号"),

    /**
     * 公益标的编号
     */
    GYBDBH("公益标的编号"),

    /** 
     * 合同编号
     */
    HTBH("合同编号");

    protected final String chineseName;

    private T5129_F03(String chineseName){
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
     * @return {@link T5128_F03}
     */
    public static final T5129_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5129_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
