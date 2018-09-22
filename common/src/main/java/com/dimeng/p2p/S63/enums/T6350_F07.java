/*
 * 文 件 名:  T6350_F07.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月15日
 */
package com.dimeng.p2p.S63.enums;

import com.dimeng.util.StringHelper;

/**
 * <商品类别属性>
 * <kind:实物，virtual:虚拟>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月15日]
 */
public enum T6350_F07
{
    kind("实物"),
    virtual("虚拟");

    protected final String chineseName;

    private T6350_F07(String chineseName){
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
     * @return {@link T6350_F07}
     */
    public static final T6350_F07 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6350_F07.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
