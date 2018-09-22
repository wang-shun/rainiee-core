package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 交易密码
 */
public enum T6118_F05{


    /** 
     * 已设置
     */
    YSZ("已设置"),

    /** 
     * 未设置
     */
    WSZ("未设置");

    protected final String chineseName;

    private T6118_F05(String chineseName){
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
     * @return {@link T6118_F05}
     */
    public static final T6118_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6118_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
