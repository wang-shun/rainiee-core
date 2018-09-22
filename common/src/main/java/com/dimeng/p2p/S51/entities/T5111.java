package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5111_F05;
import java.sql.Timestamp;

/** 
 * 文章
 */
public class T5111 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 文章自增ID
     */
    public int F01;

    /** 
     * 文章栏目ID,参考T5110.F01
     */
    public int F02;

    /** 
     * 浏览次数
     */
    public int F03;

    /** 
     * 排序值
     */
    public int F04;

    /** 
     * 发布状态,WFB:未发布;YFB:已发布;
     */
    public T5111_F05 F05;

    /** 
     * 文章标题
     */
    public String F06;

    /** 
     * 文章来源
     */
    public String F07;

    /** 
     * 文章摘要
     */
    public String F08;

    /** 
     * 封面图片文件编码
     */
    public String F09;

    /** 
     * 创建者ID,参考T7110.F01
     */
    public int F10;

    /** 
     * 创建时间
     */
    public Timestamp F11;

    /** 
     * 定时发布时间
     */
    public Timestamp F12;

    /** 
     * 实际发布时间
     */
    public Timestamp F13;

    /** 
     * 最后更新时间
     */
    public Timestamp F14;

}
