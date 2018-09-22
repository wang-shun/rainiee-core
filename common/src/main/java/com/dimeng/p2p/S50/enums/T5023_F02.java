/*
 * 文 件 名:  T5023_F02.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年11月9日
 */
package com.dimeng.p2p.S50.enums;

import com.dimeng.util.StringHelper;

/**
 * <功能说明类型>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年11月9日]
 */
public enum T5023_F02
{
    
    /** 
     * 充值
     */
    CHARGE("充值"),
    
    /** 
     * 线下充值
     */
    CHARGELINE("线下充值"),

    /** 
     * 提现
     */
    WITHDRAW("提现");

    protected final String chineseName;

    private T5023_F02(String chineseName){
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
     * @return {@link T5023_F02}
     */
    public static final T5023_F02 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5023_F02.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
    
}
