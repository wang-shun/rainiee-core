package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/**
 * 支付方式
 */
public enum T6352_F06{

    score("积分"), balance("余额");

    protected final String chineseName;

    private T6352_F06(String chineseName){
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
     * @return {@link T6352_F06}
     */
    public static final T6352_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6352_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
