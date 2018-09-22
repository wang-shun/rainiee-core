package com.dimeng.p2p.S66.enums;

import com.dimeng.p2p.S62.enums.T6247_F05;
import com.dimeng.util.StringHelper;

/**
 * 
 * 定时任务状态
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月27日]
 */
public enum T6601_F06
{
    /** 
     * 启用
     */
    ENABLE("启用"),

    /** 
     * 禁用
     */
    DISABLE("禁用");
    protected final String chineseName;

    private T6601_F06(String chineseName){
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
     * @return {@link T6247_F05}
     */
    public static final T6601_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6601_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
