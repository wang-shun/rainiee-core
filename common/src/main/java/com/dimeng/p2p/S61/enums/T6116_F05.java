package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 信用等级
 */
public enum T6116_F05 {


    /**
     * AAA
     */
    AAA("AAA"),

    /**
     * AA
     */
    AA("AA"),

    /**
     * A
     */
    A("A"),

    /**
     * B
     */
    B("B");

    protected final String chineseName;

    private T6116_F05(String chineseName){
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
     * @return {@link T6116_F05}
     */
    public static final T6116_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6116_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
