package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S71.enums.T7151_F06;
import java.sql.Timestamp;

/** 
 * 拉黑记录表
 */
public class T7151 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 拉黑记录表自增ID
     */
    public int F01;

    /** 
     * 被黑ID,参考T6010.F01
     */
    public int F02;

    /** 
     * 拉黑管理员ID,参考T7011.F01
     */
    public int F03;

    /** 
     * 拉黑详情
     */
    public String F04;

    /** 
     * 拉黑时间
     */
    public Timestamp F05;

    /** 
     * 状态,LH:拉黑;QXLH:取消拉黑
     */
    public T7151_F06 F06;

}
