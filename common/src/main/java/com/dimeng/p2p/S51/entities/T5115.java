package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5115_F02;
import com.dimeng.p2p.S51.enums.T5115_F04;
import java.sql.Timestamp;

/** 
 * 网站公告
 */
public class T5115 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 网站公告自增ID
     */
    public int F01;

    /** 
     * 类型,XT:系统;HD:活动
     */
    public T5115_F02 F02;

    /** 
     * 浏览次数
     */
    public int F03;

    /** 
     * 发布状态,WFB:未发布;YFB:已发布
     */
    public T5115_F04 F04;

    /** 
     * 公告标题
     */
    public String F05;

    /** 
     * 创建者ID,参考T7110.F01
     */
    public int F06;

    /** 
     * 创建时间
     */
    public Timestamp F07;

    /** 
     * 定时发布时间
     */
    public Timestamp F08;

    /** 
     * 实际发布时间
     */
    public Timestamp F09;

    /** 
     * 最后修改时间
     */
    public Timestamp F10;

}
