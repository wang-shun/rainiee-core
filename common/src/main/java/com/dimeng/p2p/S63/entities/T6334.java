package com.dimeng.p2p.S63.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6334_F03;
import com.dimeng.p2p.S63.enums.T6334_F04;
import java.math.BigDecimal;

/** 
 * 券发放规则信息表
 */
public class T6334 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增主键ID
     */
    public int F01;

    /** 
     * 活动ID，参考T6333.F01
     */
    public int F02;

    /** 
     * 发放场景,ZC:注册;CZ:充值
     */
    public T6334_F03 F03;

    /** 
     * 状态,QY:启用;TY:停用
     */
    public T6334_F04 F04;

    /** 
     * 每次发放数量
     */
    public int F05;

    /** 
     * 基数金额，单位：元
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 基数倍上限（单笔金额/基数金额）
     */
    public int F07;

}
