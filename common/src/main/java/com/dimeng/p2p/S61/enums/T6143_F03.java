package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 工作状态
 */
public enum T6143_F03{


    /** 
     * 在职
     */
    ZZ("在职"),

    /** 
     * 离职
     */
    LZ("离职");

    protected final String chineseName;

    private T6143_F03(String chineseName){
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
     * @return {@link T6143_F03}
     */
    public static final T6143_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6143_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
