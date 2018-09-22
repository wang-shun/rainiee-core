package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/**
 * 评估等级
 */
public enum T6147_F04{

    /**
     * 激进型
     */
    JJX("激进型"),

    /**
     * 进取型
     */
    JQX("进取型"),

    /**
     * 稳健型
     */
    WJX("稳健型"),

    /**
     * 谨慎性
     */
    JSX("谨慎型"),

    /**
     * 保守型
     */
    BSX("保守型");

    protected final String chineseName;

    private T6147_F04(String chineseName){
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
     * @return {@link T6147_F04}
     */
    public static final T6147_F04 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6147_F04.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
