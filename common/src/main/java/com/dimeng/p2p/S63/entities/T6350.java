package com.dimeng.p2p.S63.entities
        ;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6350_F04;
import com.dimeng.p2p.S63.enums.T6350_F07;

import java.sql.Timestamp;

/**
 * 积分商品类别
 */
public class T6350 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    public int F01;

    /**
     * 商品类别编码
     */
    public String F02;

    /**
     * 商品类别名称
     */
    public String F03;

    /**
     * 状态（on:启用、off:停用）
     */
    public T6350_F04 F04;

    /**
     * 创建人
     */
    public int F05;

    /**
     * 创建时间
     */
    public Timestamp F06;
    
    /**
     * kind:实物，fee:话费
     */
    public T6350_F07 F07;

}
