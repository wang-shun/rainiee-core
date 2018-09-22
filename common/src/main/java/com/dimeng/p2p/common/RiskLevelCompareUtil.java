/*
 * 文 件 名:  RiskLevelCompareUtil.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年3月30日
 */
package com.dimeng.p2p.common;

import com.dimeng.p2p.S61.enums.T6147_F04;
import com.dimeng.p2p.S61.enums.T6148_F02;

/**
 * 产品风险等级和用户风险等级匹配工具类
 * @author  xiaoqi
 * @version  [版本号, 2016年3月30日]
 */
public class RiskLevelCompareUtil
{
    
    /**
     * 比较用户风险等级和产品风险等级
     * <功能详细描述>
     * @param userRiskLevelT6147F04 用户风险等级
     * @param productRiskLevel 产品风险等级
     * @return true 匹配成功  false 匹配失败
     */
    public static boolean compareRiskLevel(T6147_F04 userRiskLevelT6147F04, String productRiskLevel)
    {
        
        if (null == userRiskLevelT6147F04)
        {
            return false;
        }
        //产品是保守型，所有用户都可以投标
        if (T6148_F02.BSX.name().equals(productRiskLevel))
        {
            return true;
        }
        String userRiskLevel = userRiskLevelT6147F04.name();
        if (T6148_F02.JSX.name().equals(productRiskLevel))
        {
            if (T6148_F02.BSX.name().equals(userRiskLevel))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        if (T6148_F02.WJX.name().equals(productRiskLevel))
        {
            if (T6148_F02.BSX.name().equals(userRiskLevel) || T6148_F02.JSX.name().equals(userRiskLevel))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        if (T6148_F02.JQX.name().equals(productRiskLevel))
        {
            if (T6148_F02.JQX.name().equals(userRiskLevel) || T6148_F02.JJX.name().equals(userRiskLevel))
            {
                return true;
            }
        }
        if (T6148_F02.JJX.name().equals(productRiskLevel) && T6148_F02.JJX.name().equals(userRiskLevel))
        {
            return true;
        }
        return false;
    }
}
