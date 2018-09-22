package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 认证结果
 */
public enum T6121_F05{


    /** 
     * 通过
     */
    TG("通过"),

    /** 
     * 不通过
     */
    BTG("不通过"),
    
    /** 
     * 待审核
     */
    DSH("待审核");

    protected final String chineseName;

    private T6121_F05(String chineseName){
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
     * @return {@link T6121_F05}
     */
    public static final T6121_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6121_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
