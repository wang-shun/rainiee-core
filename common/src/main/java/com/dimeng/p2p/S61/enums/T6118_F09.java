package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 视频认证状态
 */
public enum T6118_F09{


    /** 
     * 通过
     */
    TG("通过"),

    /** 
     * 不通过
     */
    BTG("不通过");

    protected final String chineseName;

    private T6118_F09(String chineseName){
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
     * @return {@link T6118_F09}
     */
    public static final T6118_F09 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6118_F09.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
