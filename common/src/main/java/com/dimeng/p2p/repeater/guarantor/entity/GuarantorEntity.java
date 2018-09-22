/*
 * 文 件 名:  GuarantorEntity
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/13
 */
package com.dimeng.p2p.repeater.guarantor.entity;

import com.dimeng.p2p.S61.enums.T6125_F05;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 申请担保方查询返回结果实体
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/13]
 */
public class GuarantorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 申请担保方ID
     */
    public int id;

    /**
     * 用户名
     */
    public String userName;

    /**
     * 用户类型
     */
    public String userType;

    /**
     * 真实姓名
     */
    public String realName;

    /**
     * 担保码
     */
    public String code;

    /**
     * 担保额度
     */
    public BigDecimal guarantAmount = BigDecimal.ZERO;

    /**
     * 状态
     */
    public T6125_F05 status;

    /**
     * 最后更新时间
     */
    public Timestamp updateTime;

    /**
     * 审核人
     */
    public String auditor;

    /**
     * 审核时间
     */
    public Timestamp auditTime;

    /**
     * 审核意见
     */
    public String auditDesc;

    /**
     * 用户ID
     */
    public int userId;

}
