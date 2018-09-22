/*
 * 文 件 名:  ZqzrApply.java
 * 版    权:  © 2014 DM. All rights reserved.
 * 描    述:  <描述>
 * 修 改 人:  zengzhihua
 * 修改时间:  2015/5/8
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.dimeng.p2p.modules.base.pay.service.entity;

import com.dimeng.p2p.S62.entities.T6251;

import java.sql.Timestamp;

/**
 * 债权转让申请
 *
 * @author zengzhihua
 * @version [1.0, 2015/5/8]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ZqzrApplyRecord extends T6251{
    private static final long serialVersionUID = 1L;
    /**
     * 下一个还款日,参考T6231.F06
     */
    public Timestamp nextRepayDate;

    /**
     * 债权转让申请ID，参考T6260.F01
     */
    public int zqAplId;

}
