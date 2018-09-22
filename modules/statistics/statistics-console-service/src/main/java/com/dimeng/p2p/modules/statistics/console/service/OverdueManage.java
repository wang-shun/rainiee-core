/*
 * 文 件 名:  OverdueManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月8日
 */
package com.dimeng.p2p.modules.statistics.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S70.entities.T7053;

/**
 * <逾期数据统计>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月8日]
 */
public interface OverdueManage extends Service
{
    public abstract T7053[] getT7053s(int year)
        throws Throwable;
    
    public abstract Integer[] getT7053Year()
        throws Throwable;
}
