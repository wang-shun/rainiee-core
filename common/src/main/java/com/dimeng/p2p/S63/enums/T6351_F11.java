package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/**
 * 状态
 */
public enum T6351_F11{

    sold("已上架"),
    unsold("已下架"),
 forsold("待上架");

    protected final String chineseName;

    private T6351_F11(String chineseName){
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
     * @return {@link T6351_F11}
     */
    public static final T6351_F11 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6351_F11.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
