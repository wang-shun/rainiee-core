package com.dimeng.p2p.S61.entities
        ;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6148_F02;
import java.sql.Timestamp;

/**
 * 风险评估类型设置表
 */
public class T6148 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    public int F01;

    /**
     * 评估等级:JJX:激进型；JQX：进取型；,WJX：稳健型；,JSX：谨慎性；,BSX：保守型
     */
    public T6148_F02 F02;

    /**
     * 最小分值
     */
    public int F03;

    /**
     * 最大分值
     */
    public int F04;

    /**
     * 最后修改时间
     */
    public Timestamp F05;

}
