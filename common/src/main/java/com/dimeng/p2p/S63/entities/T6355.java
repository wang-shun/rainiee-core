package com.dimeng.p2p.S63.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.common.enums.YesOrNo;

/**
 * 用户收货信息表
 */
public class T6355 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    public int F01;

    /**
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /**
     * 收货人
     */
    public String F03;

    /**
     * 区域
     */
    public int F04;

    /**
     * 详细地址
     */
    public String F05;

    /**
     * 联系电话
     */
    public String F06;

    /**
     * 邮编
     */
    public String F07;

    /**
     * 是否为默认地址
     */
    public YesOrNo F08;
    
    /**
     * 所在地区
     */
    public String szdq;

}
