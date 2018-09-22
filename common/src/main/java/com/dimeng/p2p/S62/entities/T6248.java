package com.dimeng.p2p.S62.entities ;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6248_F05;
import java.sql.Timestamp;

/**
 * 标动态信息表
 */
public class T6248 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    public int F01;

    /**
     * 创建用户ID,参考T7110.F01
     */
    public int F02;

    /**
     * 标ID，参考T6230.F01
     */
    public int F03;

    /**
     * 主题标题
     */
    public String F04;

    /**
     * 状态,YFB:已发布；WFB:未发布;
     */
    public T6248_F05 F05;

    /**
     * 简要介绍
     */
    public String F06;

    /**
     * 发布时间
     */
    public Timestamp F07;

    /**
     * 标题时间
     */
    public Timestamp F08;

    /**
     * 查看更多,外链地址
     */
    public String F09;

    /**
     * 发布者(用于显示)
     */
    public String sysName;

}
