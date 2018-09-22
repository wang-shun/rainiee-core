package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6270_F05{


    /** 
     * 待审核
     */
    DSH("待审核"),

    /** 
     * 已还款
     */
    YHK("已还款"),

    /** 
     * 不通过
     */
    BTG("不通过");

    protected final String chineseName;

    private T6270_F05(String chineseName){
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
     * @return {@link T6270_F05}
     */
    public static final T6270_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6270_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
