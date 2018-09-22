/*
 * 文 件 名:  ExperienceTotalInfo.java
 * 版    权:  © 2014 DM. All rights reserved.
 * 描    述:  体验金统计概要信息
 * 修 改 人:  linxiaolin
 * 修改时间:  2014/9/25
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.dimeng.p2p.modules.account.console.experience.service.entity;

import java.math.BigDecimal;

/**
 * 体验金统计概要信息
 * @author linxiaolin
 * @version [版本号, 2014/9/25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ExperienceTotalInfo {

    /**
     * 派出体验金汇总金额
     */
    public BigDecimal totalMoney = new BigDecimal(0);
    /**
     * 已使用金额
     */
    public BigDecimal totalUsedMoney = new BigDecimal(0);

    /**
     * 投资利息金额
     */
    public BigDecimal returnMoney = new BigDecimal(0);

}
