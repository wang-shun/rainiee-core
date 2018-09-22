/*
 * 文 件 名:  T6353_F05.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月16日
 */
package com.dimeng.p2p.S63.enums;

import com.dimeng.util.StringHelper;

/**
 * <筛选条件类型>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月16日]
 */
public enum T6353_F05
{
    score("积分"),
    amount("金额");

    protected final String chineseName;

    private T6353_F05(String chineseName){
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
     * @return {@link T6353_F05}
     */
    public static final T6353_F05 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6353_F05.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
