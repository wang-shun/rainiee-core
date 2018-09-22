package com.dimeng.p2p.S62.entities
;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6272_F06;

/**
 * 
 * 协议保全
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2016年6月16日]
 */
public class T6272 extends AbstractEntity{

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
     * 协议版本号,参考T5126.F02
     */
    public int F03;
    
    /** 
     * 保全ID
     */
    public String F04;

    /** 
     * 网签时间
     */
    public Timestamp F05;

    /** 
     * 保全状态，WBQ:未保全;YBQ:已保全;
     */
    public T6272_F06 F06;

    /** 
     * 协议本地存储路径
     */
    public String F07;
    
    /**
     * 协议编号
     */
    public String F08;

}
