package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6123_F05;
import java.sql.Timestamp;

/** 
 * 用户站内信
 */
public class T6123 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 接收用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 标题
     */
    public String F03;

    /** 
     * 发送时间
     */
    public Timestamp F04;

    /** 
     * 状态,WD:未读;YD:已读;SC:删除;
     */
    public T6123_F05 F05;

}
