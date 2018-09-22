package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 兴趣类型
 */
public enum T6141_F03{


    /** 
     * 理财
     */
    LC("理财"),

    /** 
     * 借款
     */
    JK("借款");

    protected final String chineseName;

    private T6141_F03(String chineseName){
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
     * @return {@link T6141_F03}
     */
    public static final T6141_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6141_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
