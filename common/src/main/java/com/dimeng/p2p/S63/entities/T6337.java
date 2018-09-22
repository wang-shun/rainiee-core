package com.dimeng.p2p.S63.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6337_F03;
import com.dimeng.p2p.S63.enums.T6337_F09;
import com.dimeng.p2p.S63.enums.T6337_F13;
import java.math.BigDecimal;
import java.sql.Timestamp;

/** 
 * 友金币活动信息表
 */
public class T6337 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增主键ID
     */
    public int F01;

    /** 
     * 活动名称
     */
    public String F02;

    /** 
     * 友金币获取方式,AQRZ:安全认证;CZ:充值;TJ:推荐;TZ:投资;ZC:注册;TZSYY:投资送用友
     */
    public T6337_F03 F03;

    /** 
     * 活动开始时间
     */
    public Timestamp F04;

    /** 
     * 活动结束时间
     */
    public Timestamp F05;

    /** 
     * 友金币有效期
     */
    public int F06;

    /** 
     * 发放数量
     */
    public int F07;

    /** 
     * 友金币金额，单位：元
     */
    public BigDecimal F08 = BigDecimal.ZERO;

    /** 
     * 活动适用用户组,PTYH:普通用户;YYYH:用友用户;SYYH:所有用户;
     */
    public T6337_F09 F09;

    /** 
     * 友金币使用说明
     */
    public String F10;

    /** 
     * 活动说明
     */
    public String F11;

    /** 
     * 友金币生效时间
     */
    public Timestamp F12;

    /** 
     * 状态,QY:启用;TY:停用
     */
    public T6337_F13 F13;

}
