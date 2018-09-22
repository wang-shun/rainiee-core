/*
 * 文 件 名:  T6247_F05.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月9日
 */
package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * 审核状态
 * <功能详细描述>
 * 状态,YCL:已处理;WCL:未处理;
 * @author  guomianyun
 * @version  [版本号, 2015年3月9日]
 */
public enum T6247_F05
{
    /** 
     * 是
     */
    YCL("审核通过"),

    /** 
     * 否
     */
    WCL("审核不通过");
    
    protected final String chineseName;

    private T6247_F05(String chineseName){
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
    public static final T6247_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6247_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }}
