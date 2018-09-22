package com.dimeng.p2p.S51.enums;


import com.dimeng.util.StringHelper;

/** 
 * 展示方式
 */
public enum T5110_F05{


    /** 
     * 多标签
     */
    DBQ("多标签"),

    /** 
     * 文字列表
     */
    WZLB("文字列表"),

    /** 
     * 图片列表
     */
    TPLB("图片列表");

    protected final String chineseName;

    private T5110_F05(String chineseName){
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
     * @return {@link T5110_F05}
     */
    public static final T5110_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5110_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
