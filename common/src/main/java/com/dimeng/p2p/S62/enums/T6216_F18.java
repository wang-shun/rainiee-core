package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/**
 * 投资限制
 */
public enum T6216_F18 {

    /**
     * 保守型
     */
    BSX("保守型及以上"),
    
    /**
     * 谨慎性
     */
    JSX("谨慎型及以上"),
    
    /**
     * 稳健型
     */
    WJX("稳健型及以上"),
    
    /**
     * 进取型
     */
    JQX("进取型及以上"),
    /**
     * 
     * 激进型
     */
    JJX("激进型");

    protected final String chineseName;

    private T6216_F18(String chineseName){
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
     * @return {@link T6216_F18}
     */
    public static final T6216_F18 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6216_F18.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
