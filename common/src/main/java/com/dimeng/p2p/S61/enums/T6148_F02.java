package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/**
 * 评估等级
 */
public enum T6148_F02{
   
    /**
     * 保守型
     */
    BSX("保守型"),
    
    /**
     * 谨慎性
     */
    JSX("谨慎性"),
    
    /**
     * 稳健型
     */
    WJX("稳健型"),
    
    /**
     * 进取型
     */
    JQX("进取型"),
    
    /**
     * 激进型
     */
    JJX("激进型");

    protected final String chineseName;

    private T6148_F02(String chineseName){
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
     * @return {@link T6148_F02}
     */
    public static final T6148_F02 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6148_F02.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
