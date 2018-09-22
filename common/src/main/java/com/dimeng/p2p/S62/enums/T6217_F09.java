package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/**
 * 
 * 定向组-状态
 * @author  zhongsai
 * @version  [V7.0, 2018年2月7日]
 */
public enum T6217_F09
{


    /** 
     * 启用
     */
    QY("启用"),

    /** 
     * 停用
     */
    TY("停用");

    protected final String chineseName;

    private T6217_F09(String chineseName){
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
     * @return {@link T6217_F09}
     */
    public static final T6217_F09 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6217_F09.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
