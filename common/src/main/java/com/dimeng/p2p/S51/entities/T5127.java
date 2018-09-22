package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5127_F02;
import com.dimeng.p2p.S51.enums.T5127_F03;
import com.dimeng.p2p.S51.enums.T5127_F06;
import java.math.BigDecimal;

/** 
 * 等级信息
 */
public class T5127 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 等级类型,TZ:投资;JK:借款;
     */
    public T5127_F02 F02;

    /** 
     * 等级,YJ:一级;EJ:二级,SJ:三级,SIJ:四级,WJ:五级,LJ六级:,QJ:七级;
     */
    public T5127_F03 F03;

    /** 
     * 上限
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 下限
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 状态,QY:启用;TY:停用;
     */
    public T5127_F06 F06;

}
