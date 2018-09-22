package com.dimeng.p2p.S51.entities
;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5128_F05;

/** 
 * 节假日信息
 */
public class T5128 extends AbstractEntity{

    private static final long serialVersionUID = 1L;
    
    /**
     * 自增ID
     */
    public int F01;

    /** 
     * 节假日名称
     */
    public String F02;

    /** 
     * 节假日日期
     */
    public Timestamp F03;

    /** 
     * 节假日天数
     */
    public int F04;

    /** 
     * 状态
     */
    public T5128_F05 F05;

    /**
     * 最后更新时间
     */
    public Timestamp F06;

}
