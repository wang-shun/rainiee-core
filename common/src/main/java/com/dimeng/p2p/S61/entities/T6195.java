package com.dimeng.p2p.S61.entities
        ;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6195_F06;
import com.dimeng.p2p.S61.enums.T6195_F08;
import java.sql.Timestamp;

/**
 * 用户投诉建议表
 */
public class T6195 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    public int F01;

    /**
     * 用户ID,T6110.F01
     */
    public int F02;

    /**
     * 投诉/建议内容
     */
    public String F03;

    /**
     * 投诉/建议时间
     */
    public Timestamp F04;

    /**
     * 回复内容
     */
    public String F05;

    /**
     * 是否回复,yes:已回复；no:未回复;
     */
    public T6195_F06 F06;

    /**
     * 回复人
     */
    public int F07;

    /**
     * 发布状态,YFB:已发布；WFB:未发布;
     */
    public T6195_F08 F08;

    /**
     * 发布时间
     */
    public Timestamp F09;

}
