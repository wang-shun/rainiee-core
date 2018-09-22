package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5110_F04;
import com.dimeng.p2p.S51.enums.T5110_F05;

/** 
 * 文章栏目
 */
public class T5110 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 栏目名称
     */
    public String F02;

    /** 
     * 排序值
     */
    public int F03;

    /** 
     * 状态,QY:启用;TY:停用;
     */
    public T5110_F04 F04;

    /** 
     * 展示方式,DBQ:多标签;WZLB:文字列表;TPLB:图片列表;
     */
    public T5110_F05 F05;

    /** 
     * 栏目类别
     */
    public int F06;

}
