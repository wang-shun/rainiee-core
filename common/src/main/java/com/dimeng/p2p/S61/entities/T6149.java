package com.dimeng.p2p.S61.entities
        ;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6149_F07;
import java.sql.Timestamp;

/**
 * 风险评估问题信息表
 */
public class T6149 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    public int F01;

    /**
     * 问题内容
     */
    public String F02;

    /**
     * 选项A
     */
    public String F03;

    /**
     * 选项B
     */
    public String F04;

    /**
     * 选项C
     */
    public String F05;

    /**
     * 选项D
     */
    public String F06;

    /**
     * 状态，QY：启用；TY：停用
     */
    public T6149_F07 F07;

    /**
     * 排序字段
     */
    public int F08;

    /**
     * 最后操作时间
     */
    public Timestamp F09;

    /**
     * 操作人
     */
    public int F10;

}
