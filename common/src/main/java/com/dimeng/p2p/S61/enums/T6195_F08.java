package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/**
 * 发布状态
 */
public enum T6195_F08{


    /**
     * 已发布
     */
    YFB("已发布"),

    /**
     * 未发布
     */
    WFB("未发布");

    protected final String chineseName;

    private T6195_F08(String chineseName){
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
     * @return {@link T6195_F08}
     */
    public static final T6195_F08 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6195_F08.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
