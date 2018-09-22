/*
 * 文 件 名:  T6125
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/13
 */
package com.dimeng.p2p.S61.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6125_F05;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 担保方申请表
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/13]
 */
public class T6125 extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    public int F01;

    /**
     * 用户ID（参考T6110.F01）
     */
    public int F02;

    /**
     * 担保码
     */
    public String F03;

    /**
     * 担保额度
     */
    public BigDecimal F04;

    /**
     * 状态：''SQDCL''：申请担保待处理,''QXDCL''：取消担保待处理,''SQCG''：申请担保成功,''SQSB''：申请担保失败,''QXCG''：取消担保成功,''QXSB''：取消担保失败
     */
    public T6125_F05 F05;

    /**
     * 申请时间
     */
    public Timestamp F06;

    /**
     * 审核人（参考T7110.F01）
     */
    public int F07;

    /**
     * 审核时间
     */
    public Timestamp F08;

    /**
     * 更新时间
     */
    public Timestamp F09;

    /**
     * 审核意见
     */
    public String F10;
}
