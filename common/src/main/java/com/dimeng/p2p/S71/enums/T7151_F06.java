package com.dimeng.p2p.S71.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T7151_F06{


    /** 
     * 拉黑
     */
    LH("拉黑"),

    /** 
     * 取消拉黑
     */
    QXLH("取消拉黑");

    protected final String chineseName;

    private T7151_F06(String chineseName){
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
     * @return {@link T7151_F06}
     */
    public static final T7151_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T7151_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
