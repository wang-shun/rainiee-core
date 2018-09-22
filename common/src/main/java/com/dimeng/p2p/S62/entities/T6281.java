package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6281_F14;
import com.dimeng.p2p.S62.enums.T6281_F18;
import com.dimeng.p2p.S62.enums.T6281_F19;
import com.dimeng.p2p.S62.enums.T6281_F20;
import java.math.BigDecimal;
import java.sql.Timestamp;

/** 
 * 企业融资申请
 */
public class T6281 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 用户ID，参考T6110.F01
     */
    public int F02;

    /** 
     * 企业名称
     */
    public String F03;

    /** 
     * 注册号
     */
    public String F04;

    /** 
     * 联系人
     */
    public String F05;

    /** 
     * 联系电话
     */
    public String F06;

    /** 
     * 身份证
     */
    public String F07;

    /** 
     * 公司地址
     */
    public String F08;

    /** 
     * 融资金额
     */
    public BigDecimal F09 = BigDecimal.ZERO;

    /** 
     * 借款类型ID,参考T6211.F01
     */
    public int F10;

    /** 
     * 所在区域ID，参考T5019.F01
     */
    public int F11;

    /** 
     * 预计筹款期限
     */
    public String F12;

    /** 
     * 借款描述
     */
    public String F13;

    /** 
     * 处理状态,WCL:未处理;YCL:已处理
     */
    public T6281_F14 F14;

    /** 
     * 处理人,参考T7110.F01
     */
    public int F15;

    /** 
     * 申请时间
     */
    public Timestamp F16;

    /** 
     * 处理时间
     */
    public Timestamp F17;

    /** 
     * 是否有抵押,S:是;F:否;
     */
    public T6281_F18 F18;

    /** 
     * 是否实地认证,S:是;F:否;
     */
    public T6281_F19 F19;

    /** 
     * 是否有担保,S:是;F:否;
     */
    public T6281_F20 F20;

    /** 
     * 处理结果
     */
    public String F21;
    
    /**
     * 借款期限，单位月
     */
    public int F22;

}
