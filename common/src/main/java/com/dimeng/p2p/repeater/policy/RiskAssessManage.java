/*
 * 文 件 名:  RiskAssessManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年3月8日
 */
package com.dimeng.p2p.repeater.policy;

import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6147;
import com.dimeng.p2p.S61.entities.T6148;
import com.dimeng.p2p.repeater.policy.entity.RiskSetCondition;

/**
 * <风险评估>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年3月8日]
 */
public abstract interface RiskAssessManage extends Service
{
    
    /**
     * 查询用户风险评估记录
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract T6147 getT6147(int userId)
        throws Throwable;
    
    /**
     * 更新用户风险评估记录次数
     * @param userId
     * @return int 已评估总次数
     * @throws Throwable
     */
    public abstract int updateT6147F05(int userId)
        throws Throwable;
    
    /**
     * <获取风险评估类型设置>
     * @param type
     * @return List<T6353>
     */
    public abstract List<T6148> getT6148List()
        throws Throwable;
    
    /**
     * 更新用户风险评估记录次数
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract void updateT6148s(RiskSetCondition riskSetCondition)
        throws Throwable;
}
