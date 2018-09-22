package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 友情链接
 */
public class T5114 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 友情链接自增ID
     */
    public int F01;

    /** 
     * 浏览次数
     */
    public int F02;

    /** 
     * 排序值
     */
    public int F03;

    /** 
     * 名称
     */
    public String F04;

    /** 
     * 链接地址
     */
    public String F05;

    /** 
     * 创建者,参考T7011.F01
     */
    public int F06;

    /** 
     * 创建时间
     */
    public Timestamp F07;

    /** 
     * 最后更新时间
     */
    public Timestamp F08;

}
