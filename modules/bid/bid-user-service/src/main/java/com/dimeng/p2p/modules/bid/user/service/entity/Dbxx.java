package com.dimeng.p2p.modules.bid.user.service.entity;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6236_F04;

/**
 * 担保信息
 *
 */
public class Dbxx extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 标ID,参考T6230.F01
     */
    public int F02;

    /** 
     * 担保方ID,参考T6110.F01
     */
    public int F03;

    /** 
     * 是否主担保方,S:是;F:否;
     */
    public T6236_F04 F04;

    /** 
     * 担保资质描述
     */
    public String F05;
    /**
     * 担保机构名称
     */
    public String F06;
    /**
     * 担保情况
     */
    public String F07;
	
}
