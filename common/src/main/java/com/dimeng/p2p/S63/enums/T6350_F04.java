package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/**
 * 状态
 */
public enum T6350_F04{

    on("启用"),
    off("停用");

    protected final String chineseName;

    private T6350_F04(String chineseName){
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
     * @return {@link T6350_F04}
     */
    public static final T6350_F04 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6350_F04.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
