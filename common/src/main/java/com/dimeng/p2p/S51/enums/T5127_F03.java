package com.dimeng.p2p.S51.enums;


import com.dimeng.util.StringHelper;

/** 
 * 等级
 */
public enum T5127_F03{


    /** 
     * 一级
     */
    YJ("一级"),

    /** 
     * 二级
     */
    EJ("二级"),

    /** 
     * 三级
     */
    SJ("三级"),

    /** 
     * 四级
     */
    SIJ("四级"),

    /** 
     * 五级
     */
    WJ("五级"),
    
    /**
     * 六级
     */
    LJ("六级"),

    /** 
     * 七级
     */
    QJ("七级");

    protected final String chineseName;

    private T5127_F03(String chineseName){
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
     * @return {@link T5127_F03}
     */
    public static final T5127_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5127_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
