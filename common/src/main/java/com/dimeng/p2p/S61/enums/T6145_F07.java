package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 审核状态
 */
public enum T6145_F07{

    /** 
     * 待审核
     */
    DSH("待审核"),

    /** 
     * 审核通过
     */
    SHTG("审核通过"),

    /** 
     * 审核不通过
     */
    SHBTG("审核不通过");


    protected final String chineseName;

    private T6145_F07(String chineseName){
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
     * @return {@link T6145_F07}
     */
    public static final T6145_F07 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6145_F07.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
