package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6241_F05{


    /** 
     * 已处理
     */
    YCL("审核通过"),

    /** 
     * 未处理
     */
    WCL("审核不通过"),
    
    /**
     * 已作废
     */
    YZF("已作废");
    
    protected final String chineseName;

    private T6241_F05(String chineseName){
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
     * @return {@link T6241_F05}
     */
    public static final T6241_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6241_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
