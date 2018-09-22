package com.dimeng.p2p.S51.enums;


import com.dimeng.util.StringHelper;

/** 
 * 是否发布
 */
public enum T5118_F03{


    /** 
     * 已发布
     */
    YFB("已发布"),

    /** 
     * 未发布
     */
    WFB("未发布");

    protected final String chineseName;

    private T5118_F03(String chineseName){
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
     * @return {@link T5118_F03}
     */
    public static final T5118_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5118_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
