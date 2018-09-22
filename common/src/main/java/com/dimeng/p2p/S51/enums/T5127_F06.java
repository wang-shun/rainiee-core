package com.dimeng.p2p.S51.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T5127_F06{


    /** 
     * 启用
     */
    QY("启用"),

    /** 
     * 停用
     */
    TY("停用");

    protected final String chineseName;

    private T5127_F06(String chineseName){
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
     * @return {@link T5127_F06}
     */
    public static final T5127_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5127_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
