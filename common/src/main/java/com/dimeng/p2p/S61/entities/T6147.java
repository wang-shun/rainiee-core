package com.dimeng.p2p.S61.entities
        ;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6147_F04;
import java.sql.Timestamp;

/**
 * 用户风险评估记录表
 */
public class T6147 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    public int F01;

    /**
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /**
     * 评估分数
     */
    public int F03;

    /**
     * 评估等级:JJX:激进型；JQX：进取型；,WJX：稳健型；,JSX：谨慎性；,BSX：保守型
     */
    public T6147_F04 F04;

    /**
     * 已评估总次数
     */
    public int F05;

    /**
     * 评估时间
     */
    public Timestamp F06;

}
