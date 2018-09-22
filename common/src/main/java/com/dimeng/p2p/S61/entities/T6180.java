package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6180_F03;

/** 
 * 担保方基础信息
 */
public class T6180 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 担保资质描述
     */
    public String F02;

    /** 
     * 机构类型状态,DBGS:担保公司;XDGS:小贷公司;RZZZGS:融资租债公司;
     */
    public T6180_F03 F03;
    
    /** 
     * 担保机构描述
     */
    public String F04;

}
