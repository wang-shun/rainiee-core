package com.dimeng.p2p.S51.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T5128_F05{


    /** 
     * 启用
     */
    QY("启用"),

    /** 
     * 停用
     */
    TY("停用");

    protected final String chineseName;

    private T5128_F05(String chineseName){
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
     * @return {@link T5128_F05}
     */
    public static final T5128_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5128_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
