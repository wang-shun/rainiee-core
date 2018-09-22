package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/** 
 * 使用状态
 */
public enum T6338_F05{


    /** 
     * 已使用
     */
    YSY("已使用"),

    /** 
     * 未使用
     */
    WSY("未使用");

    protected final String chineseName;

    private T6338_F05(String chineseName){
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
     * @return {@link T6338_F05}
     */
    public static final T6338_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6338_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
